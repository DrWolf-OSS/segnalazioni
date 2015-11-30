package it.drwolf.alerting.lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Identity;
import org.jbpm.JbpmContext;

import it.drwolf.alerting.entity.AppParam;
import it.drwolf.alerting.entity.Intervento;
import it.drwolf.alerting.entity.Segnalazione;
import it.drwolf.alerting.entity.Sollecito;
import it.drwolf.alerting.entity.Stato;
import it.drwolf.alerting.session.AlertingController;
import it.drwolf.alerting.session.WorkSession;

@AutoCreate
@Name("listaSegnalazioni")
@Scope(ScopeType.CONVERSATION)
public class ListaSegnalazioni {
	private Long inizio;
	private Long fine;

	@In
	private WorkSession workSession;

	private String utenza;

	private String categoriaUtenza;

	private String sottocategoriaUtenza;

	private int rows = 25;

	private List<Stato> stati = new ArrayList<Stato>(0);

	private Stato cambiaStato;

	private List<Integer> segnalazioniAperte = new ArrayList<Integer>();

	@In(create = true)
	private AlertingController alertingController;

	@In
	private EntityManager entityManager;

	@In
	private Identity identity;

	@In(required = false)
	private JbpmContext jbpmContext;

	public void clean() {

	}

	public Stato getCambiaStato() {
		return this.cambiaStato;
	}

	public String getCategoriaUtenza() {
		return this.categoriaUtenza;
	}

	public Date getDataFine() {

		return this.fine == null ? null : new Date(this.fine);
	}

	public Date getDataInizio() {

		return this.inizio == null ? null : new Date(this.inizio);
	}

	public Long getFine() {
		return this.fine;
	}

	public Long getInizio() {
		return this.inizio;
	}

	@SuppressWarnings("unchecked")
	@Factory("interventi")
	public List<Intervento> getInterventi() {

		Query query = this.entityManager
				.createQuery("from Intervento where sottotipoIntervento.tipoIntervento.id in (:ids) "
						+ (this.inizio != null ? "and scadenza >=:inizio " : "")
						+ (this.fine != null ? "and scadenza <=:fine " : "")
						+ "and stato in (:stati) order by codiceTriage.priorita asc, scadenza asc")
				.setParameter("ids",
						this.alertingController.getIdTipiIntervento(this.identity.getCredentials().getUsername()))
				.setParameter("stati", this.getStati());
		if (this.inizio != null) {
			query.setParameter("inizio", new Date(this.inizio));
		}
		if (this.fine != null) {
			query.setParameter("fine", new Date(this.fine));
		}
		return query.getResultList();

	}

	private List<String> getListStati() {
		List<String> sk = new ArrayList<String>();
		for (Stato s : this.getStati()) {
			sk.add(s.getNome());
		}
		return sk;
	}

	@SuppressWarnings("unchecked")
	@Factory("mie")
	public List<Segnalazione> getMieSegnalazioni() {
		Query query = this.entityManager
				.createQuery("from Segnalazione where idutenteInseritore =:userid "
						+ (this.inizio != null ? "and data >=:inizio " : "")
						+ (this.fine != null ? "and data <=:fine " : "") + "and stato in (:stati)")
				.setParameter("userid", this.identity.getCredentials().getUsername())
				.setParameter("stati", this.getStati());

		if (this.inizio != null) {
			query.setParameter("inizio", new Date(this.inizio));
		}
		if (this.fine != null) {
			query.setParameter("fine", new Date(this.fine));
		}

		return query.getResultList();
	}

	public int getRows() {
		return this.rows;
	}

	public List<Integer> getSegnalazioniAperte() {
		return this.segnalazioniAperte;
	}

	@SuppressWarnings("unchecked")
	@Factory("segnalazioniDaChiudere")
	public List<Segnalazione> getSegnalazioniDaChiudere() {
		Query query = this.entityManager.createQuery("from Segnalazione seg where 50 < ("
				+ " select min(i.stato.posizione) from Intervento i where i.segnalazione.id=seg.id)"
				+ " and 0<(select count(*)from Intervento i where i.segnalazione.id=seg.id)"
				+ " and seg.stato.posizione<50 " + "order by seg.id desc");
		return query.getResultList();

	}

	@SuppressWarnings("unchecked")
	@Factory("inLavorazione")
	public List<Object[]> getSegnalazioniinLavorazione() {
		/*
		 * IMPORTANTE!! il concat nel nome è importate perché JPA non riesce a
		 * prendere più campi con lo stesso nome!
		 */
		String queryStr = "select ti.ID_ ,ti.PROCINST_ , s.id , s.oggetto, concat(i.nome,''), i.cognome, i.email, concat(u.descrizione,''), concat(cu.nome,''), concat(su.nome,'') ,concat(st.descrizione,''), s.chiusura, s.scadenza,ti.DESCRIPTION_"
				+ " from JBPM_TASKINSTANCE ti left join BPMInfo bi on bi.processId = ti.PROCINST_ left join Segnalazione s on s.bpminfo_id = bi.id"
				+ " left join Stato st on st.id = s.stato_id left join Cittadino c on s.idCittadino = c.id"
				+ " left join iscrizioni.Iscritto i on c.idIscritto = i.id left join Utenza u on s.utenza_id = u.id"
				+ " left join SottocategoriaUtenza su on u.sottocategoriaUtenza_id = su.id"
				+ " left join CategoriaUtenza cu on su.categoriaUtenza_id = cu.id"
				+ " where ti.ACTORID_ like :user and END_ is null and st.nome in (:stati)";

		if (this.inizio != null) {
			queryStr += " and s.data >=:inizio";
		}
		if (this.fine != null) {
			queryStr += " and s.data <=:fine";
		}

		if (this.workSession.getParam("ordinamento.segnalazioni.asc").equals("true")) {
			queryStr = queryStr + " order by s.id asc";
		} else {
			queryStr = queryStr + " order by s.id desc";
		}
		Query query = this.entityManager.createNativeQuery(queryStr);

		query.setParameter("user", this.identity.getCredentials().getUsername());
		query.setParameter("stati", this.getListStati());
		if (this.inizio != null) {
			query.setParameter("inizio", new Date(this.inizio));
		}
		if (this.fine != null) {
			query.setParameter("fine", new Date(this.fine));
		}

		return query.getResultList();
		// return new ArrayList<Object[]>();

	}

	@SuppressWarnings("unchecked")
	@Factory("sollecitiSenzaRisposta")
	public List<Sollecito> getSollecitiSenzaRisposta() {
		return this.entityManager.createQuery("from Sollecito where idassegnatario=:me and risposta is null")
				.setParameter("me", this.identity.getCredentials().getUsername()).getResultList();
	}

	public String getSottocategoriaUtenza() {
		return this.sottocategoriaUtenza;
	}

	public List<Stato> getStati() {
		if (this.stati.size() == 0) {
			this.setStringStati(null);
		}
		return this.stati;
	}

	public String getStringStati() {
		List<String> sk = this.getListStati();
		return StringUtils.join(sk, ",");
	}

	@SuppressWarnings("unchecked")
	@Factory("ultimiAggiornamenti")
	public List<Object[]> getUltimiAggiornamenti() {
		return this.entityManager
				.createNativeQuery("SELECT S.id, S.Oggetto,FROM_UNIXTIME(vt.t/1000) FROM Segnalazione S, "
						+ "	(SELECT v.id rsid,v._revision rev, are.timestamp t, are.id i "
						+ "		FROM Segnalazione_versions v, AlertingRevisionEntity are where are.id = v._revision and v._revision_type!=0 and v._revision="
						+ "		(select max(v2._revision) from Segnalazione_versions v2 where v2.id = v.id)" + "	)"
						+ "vt where S.id=rsid order by rev desc")
				.setMaxResults(100).getResultList();

	}

	public String getUtenza() {
		return this.utenza;
	}

	public void massUpdate() {
		for (Intervento i : this.getInterventi()) {
			if (i.isCambiaStato()) {
				i.setStato(this.cambiaStato);
				this.entityManager.merge(i);
			}
		}

	}

	public void resetUtenze() {
		this.setCategoriaUtenza(null);
		this.setUtenza(null);
		this.setSottocategoriaUtenza(null);
	}

	public void setCambiaStato(Stato cambiaStato) {
		this.cambiaStato = cambiaStato;
	}

	public void setCategoriaUtenza(String categoriaUtenza) {
		this.categoriaUtenza = categoriaUtenza;
	}

	public void setDataFine(Date fine) {
		if (fine == null) {
			this.fine = null;
		} else {
			this.fine = fine.getTime();
		}

	}

	public void setDataInizio(Date inizio) {
		if (inizio == null) {
			this.inizio = null;
		} else {
			this.inizio = inizio.getTime();
		}

	}

	public void setFine(Long fine) {
		this.fine = fine;
	}

	public void setInizio(Long inizio) {
		this.inizio = inizio;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setSegnalazioniAperte(List<Integer> segnalazioniAperte) {
		this.segnalazioniAperte = segnalazioniAperte;
	}

	public void setSottocategoriaUtenza(String sottocategoriaUtenza) {
		this.sottocategoriaUtenza = sottocategoriaUtenza;
	}

	public void setStati(List<Stato> stati) {
		this.stati = stati;
	}

	@SuppressWarnings("unchecked")
	public void setStringStati(String stringStati) {
		List<String> stati = new ArrayList<String>(0);
		if (stringStati != null) {
			stati = Arrays.asList(stringStati.split(","));
		}
		if (stati.size() == 0) {
			stati.addAll(Arrays.asList(this.entityManager
					.find(AppParam.class, AppParam.APP_FILTRO_STATI_DEFAULT.getKey()).getValue().split(",")));
		}
		this.stati = this.entityManager.createQuery("from Stato where nome in (:s)").setParameter("s", stati)
				.getResultList();

	}

	public void setUtenza(String utenza) {
		this.utenza = utenza;
	}

}
