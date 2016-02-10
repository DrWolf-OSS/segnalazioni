package it.drwolf.alerting.homes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.EntityManager;

import org.apache.commons.lang.time.DateUtils;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.security.Identity;

import it.drwolf.alerting.entity.AppParam;
import it.drwolf.alerting.entity.CanaleSegnalazione;
import it.drwolf.alerting.entity.CategoriaUtenza;
import it.drwolf.alerting.entity.GlobalRole;
import it.drwolf.alerting.entity.Intervento;
import it.drwolf.alerting.entity.Segnalazione;
import it.drwolf.alerting.entity.SitCivico;
import it.drwolf.alerting.entity.SitStrada;
import it.drwolf.alerting.entity.Sollecito;
import it.drwolf.alerting.entity.SottocategoriaUtenza;
import it.drwolf.alerting.entity.Stato;
import it.drwolf.alerting.entity.Utenza;
import it.drwolf.alerting.session.AlertingController;
import it.drwolf.alerting.session.Authenticator;
import it.drwolf.alerting.session.Reports;
import it.drwolf.alerting.session.WorkSession;
import it.drwolf.alerting.util.Constants;
import it.drwolf.alerting.util.FotoUtils;
import it.drwolf.alerting.util.StradaJS;

@Name("segnalazioneHome")
@AutoCreate
public class SegnalazioneHome extends EntityHome<Segnalazione> {

	private static final long serialVersionUID = -6347594501440769072L;

	@In(create = true)
	private Reports reports;

	@In(create = true)
	private CittadinoHome cittadinoHome;
	@In(create = true)
	private AlertingController alertingController;

	private String newMessage;

	@In
	private Identity identity;

	private SitStrada sitStrada;

	@In(create = true)
	private FotoUtils fotoUtils;

	@In
	private WorkSession workSession;

	public void chiudiSegnalazione() {
		this.getInstance().setStato((Stato) this.getEntityManager().createQuery("from Stato where nome like 'chiuso'")
				.getResultList().get(0));
	}

	@Override
	protected Segnalazione createInstance() {
		Segnalazione s = new Segnalazione();

		s.setData(new Date());
		s.setScadenza(
				new Date(System.currentTimeMillis() + Integer
						.parseInt(this.getEntityManager()
								.find(AppParam.class, AppParam.APP_SEGNALAZIONE_SCADENZA.getKey()).getValue())
				* DateUtils.MILLIS_PER_DAY));
		s.setIdutenteInseritore(Identity.instance().getCredentials().getUsername());
		if (this.cittadinoHome.isIdDefined()) {
			s.setCittadino(this.cittadinoHome.getInstance());
		} else {
			s.setCittadino(Authenticator.findCittadino(this.getEntityManager(), null,
					this.identity.getCredentials().getUsername()));
		}
		s.setComune(this.getEntityManager().find(AppParam.class, AppParam.APP_COMUNE.getKey()).getValue());
		s.setStato((Stato) this.getEntityManager().createQuery("from Stato where nome=:n")
				.setParameter("n", Stato.defaults[0].getNome()).getResultList().get(0));
		s.setCanaleSegnalazione((CanaleSegnalazione) this.getEntityManager()
				.createQuery("from CanaleSegnalazione where nome='www'").getResultList().get(0));

		return s;
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllIndirizzi() {
		List<SitStrada> strade = this.getEntityManager().createQuery("from SitStrada order by nome").getResultList();
		List<String> res = new ArrayList<String>();
		res.add("");
		for (SitStrada ss : strade) {
			res.add(String.format("%s %s", ss.getTipologiaToponimo().getDescrizione(), ss.getNome()));
		}
		return res;

	}

	public List<String> getCivici() {
		if (this.getSitStrada() != null) {
			List<String> res = new ArrayList<String>();
			for (SitCivico sc : this.getSitStrada().getCivici()) {
				res.add(sc.getNumero() + sc.getEsponente() != null ? " " + sc.getEsponente() : "");
			}
			System.out.println("GETCIVICI: " + res);
			return res;
		}
		return Collections.emptyList();
	}

	public Segnalazione getDefinedInstance() {
		return this.isIdDefined() ? this.getInstance() : null;
	}

	public Integer getDuplicatoId() {
		try {
			return this.getInstance().getDuplicato().getId();
		} catch (NullPointerException e) {
			return null;
		}
	}

	@Factory("fotoSegnalazione")
	public List<Object> getFotoSegnalazione() {
		return this.fotoUtils.getFoto(this.instance);
	}

	public List<Intervento> getInterventos() {
		return this.getInstance() == null ? null : new ArrayList<Intervento>(this.getInstance().getInterventos());
	}

	public String getLinkCittadinoMancante() {
		return this.workSession.getParam("app.iscrizioni.publicUrl") + "/i/"
				+ this.getInstance().getCittadino().getIdIscritto() + "?r="
				+ this.getInstance().getCittadino().getLastRevision();
	}

	public String getNewMessage() {
		return this.newMessage;
	}

	public Integer getSegnalazioneId() {
		return (Integer) this.getId();
	}

	public SitStrada getSitStrada() {
		return this.sitStrada;
	}

	@SuppressWarnings("unchecked")
	public List<StradaJS> indirizzi(Object o) {
		// TODO: non far emergere eccezione Caused by:
		// com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Table
		// 'commercio_calenzano.SitStrada' doesn't exist
		try {
			TreeSet<StradaJS> stringhe = new TreeSet<StradaJS>();
			String[] vars = o.toString().split("\\s");
			for (String v : vars) {
				String sql = "select sitStrada from SitStrada sitStrada where lower(sitStrada.nome) like concat('%',lower(:var),'%')";

				List<SitStrada> strade = this.getEntityManager().createQuery(sql).setParameter("var", v)
						.getResultList();
				for (SitStrada s : strade) {
					stringhe.add(new StradaJS(s.getTipologiaToponimo().getDescrizione() + " " + s.getNome(),
							s.getCiviciString()));
				}

			}
			return new ArrayList<StradaJS>(stringhe);

		} catch (Exception e) {
			return new ArrayList<StradaJS>();
		}

	}

	@SuppressWarnings("unchecked")
	public boolean isVisible() {
		if (this.identity.hasRole(GlobalRole.ADMIN) || this.identity.hasRole(GlobalRole.SUPERVISOR)) {
			return true;
		}
		Segnalazione s = this.getInstance();
		String username = this.identity.getCredentials().getUsername();
		if (username.equals(s.getIdutenteInseritore())) {
			return true;
		}

		if (s.isSoggettoAggiuntivo(username)) {
			return true;
		}

		if (s.getSottotipoSegnalazione() == null) {
			if (this.alertingController.isSmistatore(username)) {
				return true;
			}
		} else {
			if (s.getSottotipoSegnalazione().getTipoSegnalazione().getUfficioSmistatore().getGestori()
					.contains(username)) {
				return true;
			}
		}
		if (this.alertingController.getListaSegnalazioni().contains(s)) {
			return true;
		}
		List<String> componenti = this.getEntityManager()
				.createQuery("select elements(u.gestori) from UfficioCompetente u where :user in elements(u.gestori)")
				.setParameter("user", this.identity.getCredentials().getUsername()).getResultList();

		if (componenti.size() > 0 && this.getEntityManager()
				.createNativeQuery(
						"select bv.id from AlertingRevisionEntity are,BPMInfo_versions bv where bv.id=:id and bv._revision=are.id and are.username in (:c)")
				.setParameter("c", componenti).setParameter("id", s.getBpmInfo().getId()).getResultList().size() > 0) {
			return true;
		}

		return false;
	}

	public boolean isWired() {

		if (this.getInstance().getCanaleSegnalazione() == null) {
			return false;
		}
		if (this.getInstance().getSottotipoSegnalazione() == null) {
			return false;
		}
		return true;
	}

	@Override
	public String persist() {

		if (this.getEntityManager().find(AppParam.class, "utenza.obbligatoria").getValue().equals("true")
				&& !this.identity.hasRole(Constants.CITTADINO.toString())
				&& this.cittadinoHome.getInstance().getId() == null
				&& (this.getInstance().getCategoriaUtenza() == null
						&& this.getInstance().getSottocategoriaUtenza() == null
						&& this.getInstance().getUtenza() == null)) {
			FacesMessages.instance().add(Severity.ERROR, "compilare campo utenza");
			return "ko";
		} else {

			String persist = super.persist();

			try {
				this.alertingController.creaSegnalazione(this.getInstance());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return persist;
		}
	}

	public void rispondiSollecito(Sollecito sollecito) {
		Sollecito s = this.getEntityManager().find(Sollecito.class, sollecito.getId());
		this.setId(s.getIdSegnalazione());
		s.setDataRisposta(new Date());
		s.setRisposta(sollecito.getRisposta());
		this.update();

	}

	public void setCategoriaUtenza(CategoriaUtenza cu) {
		this.getInstance().setSottocategoriaUtenza(null);
		this.getInstance().setCategoriaUtenza(cu);
		this.getInstance().setUtenza(null);
	}

	public void setDuplicatoId(Integer segnalazioneId) {

		if (this.instance != null && this.instance.getId() != null && this.alertingController.isTaskAssignee()) {
			EntityManager entityManager = (EntityManager) Component.getInstance("entityManager");
			this.instance.setDuplicato(entityManager.find(Segnalazione.class, segnalazioneId));
		}
	}

	public void setNewMessage(String newMessage) {
		this.newMessage = newMessage;
	}

	public void setSegnalazioneId(Integer id) {
		this.setId(id);
	}

	public void setSitStrada(SitStrada sitStrada) {
		this.sitStrada = sitStrada;
		this.getInstance().setVia(sitStrada.getTipologiaToponimo().getDescrizione() + " " + sitStrada.getNome());
		// System.out.println("SETSITSTRADA: " + this.getInstance().getVia());
	}

	public void setSottocategoriaUtenza(SottocategoriaUtenza sc) {
		this.getInstance().setSottocategoriaUtenza(sc);
		this.getInstance().setCategoriaUtenza(null);
		this.getInstance().setUtenza(null);
	}

	public void setUtenza(Utenza u) {
		this.getInstance().setSottocategoriaUtenza(null);
		this.getInstance().setCategoriaUtenza(null);
		this.getInstance().setUtenza(u);
	}

	public void sollecita(Sollecito sollecito) {
		this.setId(sollecito.getIdSegnalazione());
		Segnalazione segnalazione = this.getInstance();
		segnalazione.getSolleciti().add(sollecito);
		sollecito.setSegnalazione(segnalazione);
		sollecito.setIdassegnatario(this.reports.getAssignee(segnalazione));
		sollecito.setIdinseritore(this.identity.getCredentials().getUsername());
		this.update();

	}

	@Override
	public String update() {
		if (this.newMessage != null && !this.newMessage.trim().equals("")) {
			this.getInstance().setMessaggio(this.newMessage);
		}
		return super.update();
	}

	public Boolean verificaDataScadenza(Date chiusura, Date scadenza, Boolean addMessage) {
		try {
			boolean scaduta = chiusura == null && scadenza.before(new Date());
			if (scaduta && this.identity.hasRole(Constants.IMPIEGATO.toString()) && addMessage) {

				FacesMessages.instance().add(Severity.ERROR, "Segnalazione scaduta");
			}
			return scaduta;
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean verificaScadenza(Segnalazione segnalazione, Boolean addMessage) {
		try {
			if (segnalazione.getScadenza() == null) {
				return false;
			} else {
				return this.verificaDataScadenza(segnalazione.getChiusura(), segnalazione.getScadenza(), addMessage);
			}
		} catch (Exception e) {
			return false;
		}
	}

}
