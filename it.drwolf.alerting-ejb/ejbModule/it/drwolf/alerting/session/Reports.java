package it.drwolf.alerting.session;

import it.drwolf.alerting.entity.Cittadino;
import it.drwolf.alerting.entity.EsitoSegnalazione;
import it.drwolf.alerting.entity.GlobalRole;
import it.drwolf.alerting.entity.Segnalazione;
import it.drwolf.alerting.entity.SottotipoSegnalazione;
import it.drwolf.alerting.entity.Stato;
import it.drwolf.alerting.entity.UfficioCompetente;
import it.drwolf.alerting.entity.Utenza;
import it.drwolf.alerting.util.Constants;
import it.drwolf.alerting.util.SegnalazioniInCarico;
import it.drwolf.alerting.util.converters.PeopleConverter;
import it.drwolf.alerting.util.converters.UfficioConverter;
import it.drwolf.eloise.web.entity.People;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.time.DateUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Identity;
import org.jbpm.JbpmContext;
import org.jbpm.taskmgmt.exe.TaskInstance;

@Name("reports")
@Scope(ScopeType.CONVERSATION)
public class Reports {

	private List<Segnalazione> results;

	private String BASE_QUERY = " DATE(s.data)>=DATE(:aperturaDa) and DATE(s.data)<=DATE(:aperturaA) and "
			+ "(s.chiusura is null or (DATE(s.chiusura)>=DATE(:chiusuraDa) and DATE(s.chiusura)<=DATE(:chiusuraA))) and "
			+ "(s.scadenza is null or (DATE(s.scadenza)>=DATE(:scadenzaDa) and DATE(s.scadenza) <=DATE(:scadenzaA))) and "
			+ "s.stato in (:stati) and lower(s.oggetto) like :oggetto ";

	private SottotipoSegnalazione sottotipoSegnalazione;

	private Integer numero = null;

	private String incaricato;

	private String ufficio;

	private String cittadino;

	private String via;

	private String inseritore;

	private Utenza utenza;

	private Boolean senzaRisposta = false;

	private Boolean senzaRispostaInterne = false;

	@In(required = false)
	private JbpmContext jbpmContext;

	private Date aperturaDa = null;

	private Date aperturaA = null;

	private Date chiusuraDa = null;

	private Date chiusuraA = null;

	private Date scadenzaDa = null;

	private Date scadenzaA = null;

	private List<Stato> stati = new ArrayList<Stato>(0);

	private static SimpleDateFormat fulldate = new SimpleDateFormat(
			"dd/MM/yyyy");

	private static SimpleDateFormat month = new SimpleDateFormat("MM/yyyy");

	private static SimpleDateFormat month4sorting = new SimpleDateFormat(
			"yyyy/MM");

	@In(create = true)
	private UfficioConverter ufficioConverter;

	@In(create = true)
	private AlertingController alertingController;

	@In
	private Identity identity;

	@In
	private EntityManager entityManager;

	private HashMap<Segnalazione, String> ownerCaches = new HashMap<Segnalazione, String>();

	private HashMap<Segnalazione, String> descriptionCaches = new HashMap<Segnalazione, String>();

	private HashMap<Segnalazione, String> officeCaches = new HashMap<Segnalazione, String>();

	private String oggetto = "";

	private EsitoSegnalazione esitoSegnalazione;

	public void clear() {
		this.results = null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Segnalazione> fetchSegnalazioni() {

		String q = this.BASE_QUERY;

		if (this.aperturaDa == null) {
			this.aperturaDa = (Date) this.entityManager.createQuery(
					"select min(s.data) from Segnalazione s)")
					.getSingleResult();
		}
		if (this.aperturaA == null) {
			this.aperturaA = (Date) this.entityManager.createQuery(
					"select max(s.data) from Segnalazione s)")
					.getSingleResult();
		}
		if (this.chiusuraDa == null) {
			this.chiusuraDa = (Date) this.entityManager.createQuery(
					"select min(s.chiusura) from Segnalazione s)")
					.getSingleResult();
		}
		if (this.chiusuraA == null) {
			this.chiusuraA = (Date) this.entityManager.createQuery(
					"select max(s.chiusura) from Segnalazione s)")
					.getSingleResult();
		}
		if (this.scadenzaDa == null) {
			this.scadenzaDa = (Date) this.entityManager.createQuery(
					"select min(s.scadenza) from Segnalazione s)")
					.getSingleResult();
		}
		if (this.scadenzaA == null) {
			this.scadenzaA = (Date) this.entityManager.createQuery(
					"select max(s.scadenza) from Segnalazione s)")
					.getSingleResult();
		}

		if (this.getNumero() != null) {
			q = " s.id=" + this.numero + " ";
		} else {

			if (this.utenza != null) {
				q += " and s.utenza=:utenza ";
			}

			if (this.inseritore != null) {
				q += " and s.idutenteInseritore=:inseritore ";
			}

			if (this.via != null) {
				q += " and s.via like(:via) ";
			}

			if (this.senzaRisposta || this.senzaRispostaInterne) {
				if (this.senzaRisposta && !this.senzaRispostaInterne) {
					q += " and s.cittadino is not null";
				}
				if (!this.senzaRisposta && this.senzaRispostaInterne) {
					q += " and s.cittadino is null";
				}
				q += " and (select count(*) from Risposta r "
						+ "where r.segnalazione=s and (r.ricevuta is null or r.ricevuta=false))=0 ";
			}

			if (this.cittadino != null) {
				String[] s = this.cittadino.split("\u00a0");
				this.cittadino = s[s.length - 1];
				try {
					Integer.parseInt(this.cittadino);
					q += " and s.cittadino.id=:cittadinos ";
				} catch (Exception e) {
					this.cittadino = null;
				}
			}

			if (this.sottotipoSegnalazione != null) {
				q += " and s.sottotipoSegnalazione=:sottotipoSegnalazione ";
			}

			if (this.esitoSegnalazione != null) {
				q += " and s.esitoSegnalazione=:esitoSegnalazione ";
			}

		}

		Query query;
		if (this.identity.hasRole(GlobalRole.ADMIN)
				|| this.identity.hasRole(GlobalRole.SUPERVISOR)) {
			// ADMIN e SUPERVISOR possono vedere tutte le segnalazioni
			query = this.entityManager.createQuery("from Segnalazione s where "
					+ q + "order by s.id desc");

		} else if (this.identity.hasRole(Constants.CITTADINO.toString())) {
			// il cittadino vede solo quelle che ha inserito

			Cittadino c = Authenticator.findCittadino(this.entityManager, null,
					this.identity.getCredentials().getUsername());
			query = this.entityManager.createQuery(
					"from Segnalazione s where s.cittadino=:cittadinoc and "
							+ q + "  order by s.id desc").setParameter(
					"cittadinoc", c);
			// gli smistatori vedono tutte le segnalazioni di un certo tipo
		} else if (this.alertingController.isSmistatore(this.identity
				.getCredentials().getUsername())) {
			List sts = this.entityManager
					.createQuery(
							"select sottotipoSegnalaziones from TipoSegnalazione ts where"
									+ " :username in elements(ts.ufficioSmistatore.gestori)")
					.setParameter("username",
							this.identity.getCredentials().getUsername())
					.getResultList();

			query = this.entityManager.createQuery(
					"from Segnalazione s where s.sottotipoSegnalazione in (:sts) and "
							+ q + "order by s.id desc")
					.setParameter("sts", sts);

		} else {
			// altrimenti vedo tutte quelle passate per il mio ufficio

			List componenti = this.entityManager
					.createQuery(
							"select elements(u.gestori) from UfficioCompetente u where :user in elements(u.gestori)")
					.setParameter("user",
							this.identity.getCredentials().getUsername())
					.getResultList();
			componenti.add("heretopreventexception");
			List l = this.entityManager
					.createNativeQuery(
							"select distinct bv.id from AlertingRevisionEntity are,BPMInfo_versions bv where bv._revision=are.id and are.username in (:c)")
					.setParameter("c", componenti).getResultList();
			l.add(-1);
			query = this.entityManager.createQuery(
					"from Segnalazione s where s.bpmInfo.id in (:l)  and " + q
							+ " order by s.id desc").setParameter("l", l);

		}
		if (this.getNumero() == null) {
			query.setParameter("aperturaDa", this.aperturaDa)
					.setParameter("aperturaA", (this.aperturaA))
					.setParameter("chiusuraDa", (this.chiusuraDa))
					.setParameter("chiusuraA", (this.chiusuraA))
					.setParameter("scadenzaDa", (this.scadenzaDa))
					.setParameter("scadenzaA", (this.scadenzaA))
					.setParameter("stati", this.getStati())
					.setParameter("oggetto", "%" + this.oggetto + "%");

			if (this.utenza != null) {
				query.setParameter("utenza", this.utenza);
			}

			if (this.inseritore != null) {
				query.setParameter("inseritore", this.inseritore);
			}

			if (this.via != null) {
				query.setParameter("via", "%" + this.via + "%");
			}

			if (this.cittadino != null) {
				query.setParameter("cittadinos",
						Integer.parseInt(this.cittadino));
			}

			if (this.sottotipoSegnalazione != null) {
				query.setParameter("sottotipoSegnalazione",
						this.sottotipoSegnalazione);
			}
			if (this.esitoSegnalazione != null) {
				query.setParameter("esitoSegnalazione", this.esitoSegnalazione);
			}

		}

		final List<Segnalazione> resultList = query.getResultList();
		this.filter(resultList);
		return resultList;
	}

	private void filter(List<Segnalazione> resultList) {
		if ((this.incaricato == null) && (this.ufficio == null)) {
			return;
		}
		Iterator<Segnalazione> it = resultList.iterator();
		while (it.hasNext()) {
			Segnalazione s = it.next();
			if (this.incaricato != null) {
				if (!this.getCurrentTaskOwner(s).toLowerCase()
						.startsWith(this.incaricato.toLowerCase())) {
					it.remove();
				}
			}
			if (this.ufficio != null) {
				if (!this.getCurrentTaskOffice(s).toLowerCase()
						.startsWith(this.ufficio.toLowerCase())) {
					it.remove();
				}
			}
		}

	}

	public String formatDate(Date date) {
		return Reports.fulldate.format(date);
	}

	public String formatMonth(Date date) {
		return Reports.month.format(date);
	}

	public String formatMonthForSorting(Date date) {
		return Reports.month4sorting.format(date);
	}

	public Date getAperturaA() {
		return this.aperturaA;
	}

	public Date getAperturaDa() {
		return this.aperturaDa;
	}

	public String getAssignee(Segnalazione s) {
		final List<TaskInstance> attivitaSegnalazione = this.alertingController
				.getAttivitaSegnalazione(s);
		if (attivitaSegnalazione.size() == 0) {
			return "";
		}
		final String actorId = attivitaSegnalazione.get(0).getActorId();
		if ((actorId == null) || "".equals(actorId)) {
			return "";
		}
		return actorId;
	}

	public Date getChiusuraA() {
		return this.chiusuraA;
	}

	public Date getChiusuraDa() {
		return this.chiusuraDa;
	}

	public Date getChiusuraSegnalazione(Segnalazione s) {
		return this.jbpmContext.getProcessInstance(
				s.getBpmInfo().getProcessId()).getEnd();

	}

	public String getCittadino() {
		return this.cittadino;
	}

	public String getCurrentTaskDescription(Segnalazione s) {
		if (this.descriptionCaches.get(s) == null) {
			this.descriptionCaches.put(s, this.retrieveDescription(s));
		}
		return this.descriptionCaches.get(s);

	}

	public String getCurrentTaskOffice(Segnalazione s) {
		if (this.officeCaches.get(s) == null) {
			this.officeCaches.put(s, this.retrieveOffice(s));
		}
		return this.officeCaches.get(s);
	}

	public String getCurrentTaskOwner(Segnalazione s) {
		if (this.ownerCaches.get(s) == null) {
			this.ownerCaches.put(s, this.retrieveOwner(s));
		}
		return this.ownerCaches.get(s);

	}

	public HashMap<Segnalazione, String> getDescriptionCaches() {
		return this.descriptionCaches;
	}

	public Integer getDurataSegnalazione(Segnalazione s) {
		Date end = null;
		end = this.jbpmContext
				.getProcessInstance(s.getBpmInfo().getProcessId()).getEnd();
		if (end == null) {
			end = new Date();
		}

		return (int) ((end.getTime() - s.getData().getTime()) / DateUtils.MILLIS_PER_DAY);

	}

	public EsitoSegnalazione getEsitoSegnalazione() {
		return this.esitoSegnalazione;
	}

	public Date getFirstDayOfTheYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		return calendar.getTime();
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getIncaricati() {
		TreeMap<String, String> competenti = new TreeMap<String, String>();

		List<UfficioCompetente> ufficiCompetenti = this.entityManager
				.createQuery("from UfficioCompetente").getResultList();
		for (UfficioCompetente uc : ufficiCompetenti) {
			for (String idpeople : uc.getGestori()) {
				People people = this.entityManager.find(People.class, idpeople);

				if (people != null) {
					competenti.put(people.getCognome()
							+ PeopleConverter.nameSep + people.getNome(),
							people.getCognome() + PeopleConverter.nameSep
									+ people.getNome());
				}

			}

		}
		return competenti;
	}

	public String getIncaricato() {
		return this.incaricato;
	}

	public String getInseritore() {
		return this.inseritore;
	}

	public List<Segnalazione> getListaSegnalazioni() {
		if (this.results == null) {
			this.results = this.fetchSegnalazioni();
		}
		return this.results;
	}

	public Integer getNumero() {
		return this.numero;
	}

	public HashMap<Segnalazione, String> getOfficeCaches() {
		return this.officeCaches;
	}

	public String getOggetto() {
		return this.oggetto;
	}

	public HashMap<Segnalazione, String> getOwnerCaches() {
		return this.ownerCaches;
	}

	public Date getScadenzaA() {
		return this.scadenzaA;
	}

	public Date getScadenzaDa() {
		return this.scadenzaDa;
	}

	@SuppressWarnings("unchecked")
	public List<SegnalazioniInCarico> getSegnalazioniPerUfficio() {
		HashMap<String, List<Segnalazione>> map = new HashMap<String, List<Segnalazione>>();
		List<Segnalazione> segnalazioni = this.entityManager.createQuery(
				"from Segnalazione where chiusura is null").getResultList();
		for (Segnalazione s : segnalazioni) {
			String u = this.getCurrentTaskOffice(s);
			if (map.get(u) == null) {
				map.put(u, new ArrayList<Segnalazione>());
			}
			map.get(u).add(s);
		}

		HashMap<String, SegnalazioniInCarico> sicMap = new HashMap<String, SegnalazioniInCarico>();

		for (Entry<String, List<Segnalazione>> e : map.entrySet()) {
			if (sicMap.get(e.getKey()) == null) {
				sicMap.put(e.getKey(), new SegnalazioniInCarico(e.getKey(), 0,
						0, 0));
			}
			SegnalazioniInCarico sic = sicMap.get(e.getKey());
			for (Segnalazione s : e.getValue()) {
				sic.setTotale(sic.getTotale() + 1);
				if ((s.getScadenza() != null)
						&& s.getScadenza().before(new Date())) {
					sic.setScadute(sic.getScadute() + 1);
				} else {
					sic.setInTempo(sic.getInTempo() + 1);
				}
			}

		}

		ArrayList<SegnalazioniInCarico> res = new ArrayList<SegnalazioniInCarico>(
				sicMap.values());
		Collections.sort(res);
		return res;
	}

	public Boolean getSenzaRisposta() {
		return this.senzaRisposta;
	}

	public Boolean getSenzaRispostaInterne() {
		return this.senzaRispostaInterne;
	}

	public SottotipoSegnalazione getSottotipoSegnalazione() {
		return this.sottotipoSegnalazione;
	}

	@SuppressWarnings("unchecked")
	public List<Stato> getStati() {
		if (this.stati.size() == 0) {
			return this.stati = this.entityManager.createQuery(
					"from Stato where nome in ('daesaminare','aperto')")
					.getResultList();
		}
		return this.stati;
	}

	public String getUfficio() {
		return this.ufficio;
	}

	public Utenza getUtenza() {
		return this.utenza;
	}

	public String getVia() {
		return this.via;
	}

	private String retrieveDescription(Segnalazione s) {
		try {
			final List<TaskInstance> attivitaSegnalazione = this.alertingController
					.getAttivitaSegnalazione(s);
			if (attivitaSegnalazione.size() == 0) {
				return "";
			}
			return attivitaSegnalazione.get(0).getDescription();
		} catch (Exception e) {
			return "";
		}
	}

	@SuppressWarnings("unchecked")
	private String retrieveOffice(Segnalazione s) {
		try {
			final List<TaskInstance> attivitaSegnalazione = this.alertingController
					.getAttivitaSegnalazione(s);
			if (attivitaSegnalazione.size() == 0) {
				return "";
			}
			try {
				final String actorId = attivitaSegnalazione.get(0).getActorId();
				if ((actorId == null) || "".equals(actorId)) {
					return "";
				}
				final List<UfficioCompetente> resultList = this.entityManager
						.createQuery(
								"select u from UfficioCompetente u where :user in elements(u.gestori)")
						.setParameter("user", actorId).getResultList();
				if (resultList.size() > 0) {
					return this.ufficioConverter.getAsString(null, null,
							resultList.get(0).getEloiseId());
				}

			} catch (Exception e) {

			}
			return "";
		} catch (Exception e) {
			return "";
		}
	}

	private String retrieveOwner(Segnalazione s) {
		try {

			final String actorId = this.getAssignee(s);
			try {
				return PeopleConverter.formatPeople(this.entityManager.find(
						People.class, actorId));
			} catch (Exception e) {
				return actorId;
			}
		} catch (Exception e) {
			return "";
		}
	}

	public void setAperturaA(Date aperturaA) {
		this.aperturaA = aperturaA;
	}

	public void setAperturaDa(Date aperturaDa) {
		this.aperturaDa = aperturaDa;
	}

	public void setChiusuraA(Date chiusuraA) {
		this.chiusuraA = chiusuraA;
	}

	public void setChiusuraDa(Date chiusuraDa) {
		this.chiusuraDa = chiusuraDa;
	}

	public void setCittadino(String cittadino) {
		if ((cittadino != null) && "".equals(cittadino.trim())) {
			cittadino = null;
		}
		this.cittadino = cittadino;
	}

	public void setDescriptionCaches(
			HashMap<Segnalazione, String> descriptionCaches) {
		this.descriptionCaches = descriptionCaches;
	}

	public void setEsitoSegnalazione(EsitoSegnalazione esitoSegnalazione) {
		this.esitoSegnalazione = esitoSegnalazione;
	}

	public void setIncaricato(String incaricato) {
		if ((incaricato != null) && "".equals(incaricato.trim())) {
			incaricato = null;
		}
		this.incaricato = incaricato;
	}

	public void setInseritore(String inseritore) {
		if ((inseritore != null) && "".equals(inseritore.trim())) {
			inseritore = null;
		}
		this.inseritore = inseritore;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public void setOfficeCaches(HashMap<Segnalazione, String> officeCaches) {
		this.officeCaches = officeCaches;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public void setOwnerCaches(HashMap<Segnalazione, String> ownerCaches) {
		this.ownerCaches = ownerCaches;
	}

	public void setScadenzaA(Date scadenzaA) {
		this.scadenzaA = scadenzaA;
	}

	public void setScadenzaDa(Date scadenzaDa) {
		this.scadenzaDa = scadenzaDa;
	}

	public void setSenzaRisposta(Boolean senzaRisposta) {
		this.senzaRisposta = senzaRisposta;
	}

	public void setSenzaRispostaInterne(Boolean senzaRispostaInterne) {
		this.senzaRispostaInterne = senzaRispostaInterne;
	}

	public void setSottotipoSegnalazione(
			SottotipoSegnalazione sottotipoSegnalazione) {
		this.sottotipoSegnalazione = sottotipoSegnalazione;
	}

	public void setStati(List<Stato> stati) {
		this.stati = stati;
	}

	public void setUfficio(String ufficio) {
		this.ufficio = ufficio;
	}

	public void setUtenza(Utenza utenza) {
		this.utenza = utenza;
	}

	public void setVia(String via) {
		this.via = via;
	}

}
