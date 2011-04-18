package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.AppParam;
import it.drwolf.alerting.entity.BPMInfo;
import it.drwolf.alerting.entity.Cittadino;
import it.drwolf.alerting.entity.CodiceTriage;
import it.drwolf.alerting.entity.Intervento;
import it.drwolf.alerting.entity.OreLavorate;
import it.drwolf.alerting.entity.Segnalazione;
import it.drwolf.alerting.entity.SottotipoIntervento;
import it.drwolf.alerting.entity.SquadraIntervento;
import it.drwolf.alerting.entity.Stato;
import it.drwolf.alerting.session.AlertingController;
import it.drwolf.alerting.session.WorkSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang.time.DateUtils;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.bpm.BusinessProcess;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.security.Identity;
import org.jbpm.JbpmContext;
import org.jbpm.taskmgmt.exe.TaskInstance;

@Name("interventoHome")
@AutoCreate
public class InterventoHome extends EntityHome<Intervento> {

	private static final long serialVersionUID = -314149847165319727L;

	@In
	private WorkSession workSession;

	@In(required = false)
	private JbpmContext jbpmContext;

	@In(create = true)
	private EntityManager entityManager;

	@SuppressWarnings("unused")
	@Out
	private OreLavorate oreLavorate = new OreLavorate();

	@In(required = false)
	private AlertingController alertingController;

	private List<SquadraIntervento> squadre = new ArrayList<SquadraIntervento>(
			0);

	private Date scadenzaSegnalazione = new Date();

	public void addOre(OreLavorate oreLavorate) {
		this.getInstance().getOreLavorate().add(oreLavorate);
		oreLavorate.setIntervento(this.instance);
		if (this.isManaged()) {
			this.getEntityManager().persist(oreLavorate);
			this.update();
		}
		this.oreLavorate = new OreLavorate();
	}

	@Override
	protected Intervento createInstance() {
		Intervento intervento = new Intervento();
		intervento.setCodiceTriage(this.getEntityManager().find(
				CodiceTriage.class, "blu"));
		if ((this.alertingController != null)
				&& BusinessProcess.instance().hasCurrentProcess()) {
			intervento.setBpmInfo(new BPMInfo());
			Segnalazione s = this.alertingController.getSegnalazione();
			intervento.setSegnalazione(s);
			intervento.setOggetto(s.getOggetto());
			if (!this.workSession.getParam("app.interventi.nomsg").equals("")) {
				intervento.setDescrizione(s.getMessaggio());
			}
			intervento.setLocalita(s.getLocalita());
			intervento.setVia(s.getVia());
			intervento.setCivico(s.getCivico());
			intervento.setComune(s.getComune());

			this.scadenzaSegnalazione = this.alertingController
					.getCurrentTask().getProcessInstance().getStart();

			AppParam parametro = this.entityManager.find(AppParam.class,
					"aperturaIntervento");

			if ((parametro != null) && (parametro.getValue() != null)
					&& !parametro.getValue().equals("")) {
				intervento.setApertura(new Date());
			} else {
				intervento.setApertura(this.scadenzaSegnalazione);
			}

			Cittadino c = s.getCittadino();
			if (c != null) {
				intervento.setNomeReferente(c.getNome() + " " + c.getCognome());
				intervento.setTelefonoReferente(c.getCellulare());
			}
			if ((s.getReferente() != null)
					&& !"".equals(s.getReferente().trim())) {
				intervento.setNomeReferente(s.getReferente());
				intervento.setTelefonoReferente(s.getTelefonoReferente());
			}
			intervento.setUtenza(s.getUtenza());
			intervento.setStato((Stato) this.getEntityManager()
					.createQuery("from Stato where nome=:n")
					.setParameter("n", "aperto").getResultList().get(0));
		} else {
			intervento.setComune(this.getEntityManager()
					.find(AppParam.class, AppParam.APP_COMUNE.getKey())
					.getValue());
		}
		return intervento;
	}

	public void delOre(OreLavorate oreLavorate) {
		this.getInstance().getOreLavorate().remove(oreLavorate);

		this.getEntityManager().remove(oreLavorate);
		this.update();
	}

	public void genera() {

		if (this.instance.getCodiceTriage().getTempoIntervento() >= 0) {
			this.instance.setScadenza(new Date(this.getInstance().getApertura()
					.getTime()
					+ this.instance.getCodiceTriage().getTempoIntervento()
					* DateUtils.MILLIS_PER_DAY));
		}
		boolean ore = false;
		for (SquadraIntervento s : this.squadre) {
			Intervento i = new Intervento();
			if (!ore) {
				for (OreLavorate o : this.instance.getOreLavorate()) {
					o.setIntervento(i);
					i.getOreLavorate().add(o);
				}
				ore = true;
			}
			i.setSquadraIntervento(s);
			// i.setScadenza(this.getScadenza());
			i.setBpmInfo(new BPMInfo());
			i.getBpmInfo().setProcessId(
					this.instance.getBpmInfo().getProcessId());
			i.setChiusura(this.instance.getChiusura());
			i.setScadenza(this.instance.getScadenza());
			i.setCivico(this.instance.getCivico());
			i.setCodiceTriage(this.instance.getCodiceTriage());
			i.setComune(this.instance.getComune());
			i.setDescrizione(this.instance.getDescrizione());
			i.setLocalita(this.instance.getLocalita());
			i.setMessaggio(this.instance.getMessaggio());
			i.setNomeReferente(this.instance.getNomeReferente());
			i.setNoteSegnalatore(this.instance.getNoteSegnalatore());
			i.setOggetto(this.instance.getOggetto());
			i.setScadenza(this.instance.getScadenza());
			i.setSegnalatore(this.instance.getSegnalatore());
			i.setSegnalazione(this.instance.getSegnalazione());
			i.setSottotipoIntervento(this.instance.getSottotipoIntervento());
			i.setStato(this.instance.getStato());
			i.setTelefonoReferente(this.instance.getTelefonoReferente());
			i.setUtenza(this.instance.getUtenza());
			i.setVia(this.instance.getVia());

			this.getEntityManager().persist(i);
			FacesMessages.instance()
					.add("Creato intervento " + i.getOggetto() + " - "
							+ s.getNome());
		}

	}

	@SuppressWarnings("unchecked")
	public List<CodiceTriage> getCodici() {
		return this.getEntityManager()
				.createQuery("from CodiceTriage order by tempoIntervento desc")
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<String> getComponentiSquadre() {
		return this
				.getEntityManager()
				.createNativeQuery(
						"select distinct s.element from SquadraIntervento_componenti s"
								+ " order by s.element").getResultList();
	}

	public String getDatePattern() {
		return this.instance.getDatePattern();
	}

	public Intervento getDefinedInstance() {
		return this.isIdDefined() ? this.getInstance() : null;
	}

	public Integer getInterventoId() {
		return (Integer) this.getId();
	}

	public Date getScadenza() {
		if ((this.getInstance() != null)
				&& (this.getInstance().getCodiceTriage() != null)) {
			Integer t = this.instance.getCodiceTriage().getTempoIntervento();
			if (t > 0) {
				return new Date(this.scadenzaSegnalazione.getTime() + t
						* DateUtils.MILLIS_PER_DAY);
			} else {
				return this.scadenzaSegnalazione;
			}
		} else {
			return this.scadenzaSegnalazione;
		}
	}

	@Factory
	public SottotipoIntervento getSottotipoIntervento() {
		return this.getInstance().getSottotipoIntervento();
	}

	public List<SquadraIntervento> getSquadre() {
		return this.squadre;
	}

	public boolean isWired() {
		if (this.getInstance().getSegnalazione() == null) {
			return false;
		}
		if (this.getInstance().getSottotipoIntervento() == null) {
			return false;
		}

		return true;
	}

	public void setInterventoId(Integer id) {
		this.setId(id);
	}

	public void setScadenza(Date date) {
		this.instance.setScadenza(date);
	}

	public void setSquadre(List<SquadraIntervento> squadre) {
		this.squadre = squadre;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String update() {
		super.update();
		if (this.instance.getSegnalazione() == null) {
			return "INDEX";
		}
		for (Intervento i : this.instance.getSegnalazione().getInterventos()) {
			if (!i.getStato().getNome().equals("chiuso")) {
				return "INDEX";
			}
		}

		Long processId = this.instance.getSegnalazione().getBpmInfo()
				.getProcessId();

		for (TaskInstance t : (List<TaskInstance>) this.jbpmContext
				.getTaskList(Identity.instance().getCredentials().getUsername())) {
			if ((t.getProcessInstance().getId() == processId)
					&& t.getName().equals("aggiornaSegnalazione")) {
				this.alertingController.setCurrentProcessId(processId);
				this.alertingController.setCurrentTaskId(t.getId());
				return "CLOSE";

			}
		}
		return "INDEX";
	}

}
