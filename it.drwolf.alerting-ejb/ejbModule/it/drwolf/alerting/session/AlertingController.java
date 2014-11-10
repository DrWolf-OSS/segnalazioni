package it.drwolf.alerting.session;

import it.drwolf.alerting.entity.AlertingRevisionEntity;
import it.drwolf.alerting.entity.AppParam;
import it.drwolf.alerting.entity.CanaleSegnalazione;
import it.drwolf.alerting.entity.Cittadino;
import it.drwolf.alerting.entity.Intervento;
import it.drwolf.alerting.entity.Segnalazione;
import it.drwolf.alerting.entity.Stato;
import it.drwolf.alerting.entity.UfficioCompetente;
import it.drwolf.alerting.homes.InterventoHome;
import it.drwolf.alerting.homes.SegnalazioneHome;
import it.drwolf.alerting.lists.ListaSegnalazioni;
import it.drwolf.alerting.util.Constants;
import it.drwolf.alerting.util.Info;
import it.drwolf.alerting.util.MailSender;
import it.drwolf.alerting.util.converters.PeopleConverter;
import it.drwolf.eloise.web.entity.Organizationalrole;
import it.drwolf.eloise.web.entity.People;
import it.drwolf.eloise.web.entity.Ufficio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jboss.envers.VersionsReader;
import org.jboss.envers.VersionsReaderFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.bpm.ResumeProcess;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.bpm.Actor;
import org.jboss.seam.bpm.BusinessProcess;
import org.jboss.seam.security.Identity;
import org.jbpm.JbpmContext;
import org.jbpm.db.GraphSession;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.SwimlaneInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

@Name("alertingController")
@Scope(ScopeType.CONVERSATION)
public class AlertingController {

	public static void setAssegnazionePool(Boolean assegnazionePool) {
		AlertingController.assegnazionePool = assegnazionePool;
	}

	private static final String HOME = "home";

	private static Boolean assegnazionePool = false;

	@In
	private Identity identity;

	@In(create = true)
	private MailSender mailSender;

	@In
	private SegnalazioneHome segnalazioneHome;

	@In
	private InterventoHome interventoHome;

	@In
	private Actor actor;

	@In(required = false)
	private JbpmContext jbpmContext;

	@In
	private EntityManager entityManager;

	@In(required = false)
	private ProcessInstance processInstance;

	@In
	private ProcessManager processManager;

	private Long currentTaskId;

	private Long currentProcessId;

	private String newMessage;

	private String newMessageIntervento;

	@In
	private AlertingProcess alertingProcess;

	@In
	private ListaSegnalazioni listaSegnalazioni;

	private HashMap<Segnalazione, List<TaskInstance>> taskCache = new HashMap<Segnalazione, List<TaskInstance>>();

	@RequestParameter("taskId")
	// same value as the 'name' in f:param
	private String taskId;

	private Map<Long, Segnalazione> sCache = new HashMap<Long, Segnalazione>();

	@SuppressWarnings("unchecked")
	public void alertAssignee() {
		List<UfficioCompetente> resultList = this.entityManager.createQuery("select u from UfficioCompetente u where u.alert=true and :user in elements(u.gestori)")
				.setParameter("user", this.alertingProcess.getImpiegatoUfficioCompetente()).getResultList();
		if (resultList.size() > 0) {
			People people = this.entityManager.find(People.class, this.alertingProcess.getImpiegatoUfficioCompetente());
			String msg = "Ti e' stata assegnata una segnalazione, segui il link per vedere i dettagli:";
			msg += "\r\n\r\n" + this.entityManager.find(AppParam.class, AppParam.APP_URL.getKey());
			this.mailSender.sendSimpleMail(Arrays.asList(new String[] { people.getEmail() }), "Segnalazione assegnata", msg);
		}
	}

	public String assegnaSegnalazione() throws Exception {
		SwimlaneInstance swimlaneInstance = this.getCurrentTask().getProcessInstance().getTaskMgmtInstance().getSwimlaneInstance(Constants.UFFICIO_COMPETENTE.toString());
		if (swimlaneInstance != null) {
			swimlaneInstance.setActorId(this.alertingProcess.getImpiegatoUfficioCompetente());
		}
		return "home";
	}

	public void assignTaskToCurrentActor(TaskInstance task) {
		task.setActorId(this.actor.getId());
		this.currentTaskId = task.getId();
	}

	public void assignTaskToCurrentActorById() {

		Long taskIdAsLong = Long.parseLong(this.taskId);

		TaskInstance task = this.jbpmContext.getTaskInstance(taskIdAsLong);

		this.assignTaskToCurrentActor(task);
	}

	private void beforeTaskEnd() {
		if (this.getSegnalazione() != null) {
			Segnalazione s = this.segnalazioneHome.getInstance();

			if (s.getCanaleSegnalazione() == null) {
				s.setCanaleSegnalazione((CanaleSegnalazione) this.entityManager.createQuery("from CanaleSegnalazione where nome=:www")
						.setParameter("www", Constants.CANALE_WWW_NOME.toString()).getSingleResult());
			}
			s.getBpmInfo().setTokenId(this.getCurrentTask().getProcessInstance().getRootToken().getId());

			this.segnalazioneHome.update();

		}

		if (this.getIntervento() != null) {
			if (this.interventoHome.isManaged()) {
				this.interventoHome.update();
			}
		}

	}

	public void beginTask() {
		this.getCurrentTask().start();
	}

	@ResumeProcess(processId = "#{alertingController.currentProcessId}")
	public String checkTaskStart() {
		if (this.currentTaskId == null) {
			throw new RuntimeException("task id must be specified");
		}
		TaskInstance currentTask = this.getCurrentTask();
		if (currentTask == null) {
			throw new RuntimeException("current user cannot perform this task");
		}
		if (currentTask.getStart() == null) {
			currentTask.start();
		}
		if (!this.segnalazioneHome.isIdDefined()) {
			Segnalazione segnalazione = this.getSegnalazione();
			if (segnalazione != null) {
				this.segnalazioneHome.setSegnalazioneId(segnalazione.getId());
			}
			this.segnalazioneHome.getInstance().getBpmInfo().setCurrentTask(currentTask.getDescription());

		}
		if (!this.interventoHome.isIdDefined()) {
			Intervento intervento = this.getIntervento();
			if (intervento != null) {
				this.interventoHome.setInterventoId(intervento.getId());
			}
			this.interventoHome.getInstance().getBpmInfo().setCurrentTask(currentTask.getDescription());

		}

		return "OK";
	}

	public void chiudiSegnalazioni() {
		List<Segnalazione> segnalazioni = this.listaSegnalazioni.getSegnalazioniDaChiudere();
		Stato chiuso = (Stato) this.entityManager.createQuery("from Stato where nome like 'chiuso'").getResultList().get(0);
		for (Segnalazione segnalazione : segnalazioni) {
			segnalazione.setStato(chiuso);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Segnalazione creaSegnalazione(Segnalazione s) throws Exception {
		this.alertingProcess.setSmistatori(new HashSet(this.getSmistatori()));

		this.processManager.createProcess();
		s.getBpmInfo().setProcessId(BusinessProcess.instance().getProcessId());
		this.entityManager.merge(s);
		return s;

	}

	@SuppressWarnings("unchecked")
	public void endProcess() throws Exception {
		for (Token t : (List<Token>) this.processInstance.findAllTokens()) {
			t.end();
		}
		this.processInstance.getTaskMgmtInstance().endAll();

		List<Segnalazione> l = this.entityManager.createQuery("from Segnalazione where bpmInfo.processId=:pid").setParameter("pid", this.processInstance.getId()).getResultList();
		if (l.size() > 0) {
			Segnalazione s = l.get(0);
			s.setStato((Stato) this.entityManager.createQuery("from Stato where nome=:n").setParameter("n", Stato.defaults[Stato.defaults.length - 1].getNome()).getResultList()
					.get(0));
			this.entityManager.merge(s);
		}

		System.out.println("process terminated");
	}

	public void generaIntervento() {
		try {
			Intervento intervento = new Intervento();
			intervento.setSegnalazione(this.getSegnalazione());
			intervento.getBpmInfo().setTokenId(this.getCurrentTask().getToken().getId());
			if (!"blu".equals(intervento.getCodiceTriage().getId())) {

				intervento.setScadenza(new Date(this.getCurrentTask().getProcessInstance().getStart().getTime() + intervento.getCodiceTriage().getTempoIntervento()
						* DateUtils.MILLIS_PER_DAY));
			}

			intervento.setUtenza(this.getSegnalazione().getUtenza());

			Cittadino c = this.getSegnalazione().getCittadino();
			if (c != null) {
				intervento.setNomeReferente(c.getNome() + " " + c.getCognome());
				intervento.setTelefonoReferente(c.getCellulare());
			}

			this.entityManager.persist(intervento);
			this.getSegnalazione().getInterventos().add(intervento);
			this.beforeTaskEnd();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public String getAppUrl() {
		return this.entityManager.find(AppParam.class, AppParam.APP_URL.getKey()).getValue();

	}

	public Boolean getAssegnazionePool() {
		return AlertingController.assegnazionePool;
	}

	public List<TaskInstance> getAttivitaSegnalazione(Segnalazione segnalazione) {
		if (this.taskCache.get(segnalazione) == null) {
			this.taskCache.put(segnalazione, this.retrieveAttivita(segnalazione));
		}
		return this.taskCache.get(segnalazione);

	}

	@SuppressWarnings("unchecked")
	public List<Transition> getAvailableTransitions() {
		List<Transition> availableTransitions = this.getCurrentTask().getAvailableTransitions();
		Collections.sort(availableTransitions, new Comparator<Transition>() {

			public int compare(Transition o1, Transition o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});
		return availableTransitions;
	}

	public Map<String, String> getCompetenti() {
		if (this.getSegnalazione().getSottotipoSegnalazione() == null) {
			return new HashMap<String, String>();
		}
		TreeMap<String, String> competenti = new TreeMap<String, String>();

		List<UfficioCompetente> ufficiCompetenti = this.getSegnalazione().getSottotipoSegnalazione().getTipoSegnalazione().getUfficiCompetenti();
		for (UfficioCompetente uc : ufficiCompetenti) {
			for (String idpeople : uc.getGestori()) {

				People people = this.entityManager.find(People.class, idpeople);
				if (people != null) {

					competenti.put(people.getCognome() + PeopleConverter.nameSep + people.getNome(), people.getIdpeople());
				}

				// @Pala
				// Tolto perchè nella pagina di assegnazione task dava eccezione
				// se veniva passato un id di un oggetto People non più
				// presente.
				// Il metodo è usato solo in /app/task/scegliAssegnatario.xhtml
				// quindi il cambiamento non dovrebbe avere ripercussioni
				// altrove
				//
				// else {
				// competenti.put(idpeople, idpeople);
				// }

			}

		}
		return competenti;
	}

	public Long getCurrentProcessId() {
		return this.currentProcessId;
	}

	public TaskInstance getCurrentTask() {

		return this.jbpmContext.getTaskInstance(this.currentTaskId);
	}

	public Long getCurrentTaskId() {
		return this.currentTaskId;
	}

	public String getDirigenti() {

		// if param nascondiDirigenti is present end 'true' this method shall
		// return an empty string
		try {
			String paramValue = this.entityManager.find(AppParam.class, AppParam.NASCONDI_DIRIGENTI.getKey()).getValue();
			if (paramValue.trim().equals("true")) {
				return "";
			}
		} catch (Exception e) {
			return "";
		}

		try {
			TreeSet<String> d = new TreeSet<String>();
			List<UfficioCompetente> uc = this.getSegnalazione().getSottotipoSegnalazione().getTipoSegnalazione().getUfficiCompetenti();
			for (UfficioCompetente u : uc) {
				Ufficio ufficio = this.entityManager.find(Ufficio.class, u.getEloiseId());
				for (Organizationalrole or : ufficio.getArea().getOrganizationalroles()) {
					if (or.getUfficio() == null) {
						for (People p : or.getPeoples()) {
							d.add(p.getCognome() + " " + p.getNome() + " (" + p.getEmail() + ", " + p.getTelefono() + ")");
						}
					}
				}
			}
			return StringUtils.join(d, " - ");
		} catch (Exception e) {

		}
		return "";
	}

	public String getFirstMessage(Segnalazione segnalazione) {
		VersionsReader reader = VersionsReaderFactory.get(this.entityManager);
		if (segnalazione != null && segnalazione.getId() != null) {
			Integer sId = segnalazione.getId();
			for (Number n : reader.getRevisions(Segnalazione.class, sId)) {
				Segnalazione s = reader.find(Segnalazione.class, sId, n);
				if (s.getMessaggio() != null) {
					return s.getMessaggio();
				}
			}
		}
		return "";
	}

	public Map<String, String> getGestori() {
		TreeMap<String, String> competenti = new TreeMap<String, String>();

		List<String> gestori = this.interventoHome.getInstance().getSottotipoIntervento().getTipoIntervento().getGestoriIntervento();
		for (String idpeople : gestori) {
			People people = this.entityManager.find(People.class, idpeople);

			competenti.put(people.getCognome() + PeopleConverter.nameSep + people.getNome(), people.getIdpeople());

		}
		return competenti;
	}

	@SuppressWarnings("unchecked")
	public List<Info> getHistory(Segnalazione segnalazione) {

		List<Object[]> res = this.entityManager.createNativeQuery("SELECT START_,END_,DESCRIPTION_, ACTORID_ FROM JBPM_TASKINSTANCE J where J.PROCINST_=:pi order by ID_ desc")
				.setParameter("pi", segnalazione.getBpmInfo().getProcessId()).getResultList();

		List<Info> messages = new ArrayList<Info>();

		ProcessInstance process = this.jbpmContext.getProcessInstance(segnalazione.getBpmInfo().getProcessId());

		if (process.getEnd() != null) {
			messages.add(new Info(process.getEnd(), process.getEnd(), "Termine segnalazione", null));
		}
		for (Object[] r : res) {
			messages.add(new Info((Date) r[0], (Date) r[1], (String) r[2], (String) r[3]));
		}

		messages.add(new Info(process.getStart(), process.getStart(), "Creazione segnalazione", segnalazione.getIdutenteInseritore()));
		return messages;
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getIdTipiIntervento(String username) {
		return this.entityManager.createNativeQuery("select t.TipoIntervento_id from TipoIntervento_gestoriIntervento t where t.element=:username")
				.setParameter("username", username).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Intervento> getInterventiAttivi() {
		List<Intervento> res = new ArrayList<Intervento>();
		for (Token t : (List<Token>) this.processInstance.findAllTokens()) {
			if (t.getNode() != null) {
				if (t.getNode().getName().equals("aggiornamentoIntervento")) {
					res.addAll(this.entityManager.createQuery("from Intervento where bpmInfo.tokenId=:tid").setParameter("tid", t.getId()).getResultList());
				}
			}
		}
		return res;
	}

	public Intervento getIntervento() {
		BusinessProcess.instance();

		TaskInstance currentTask = this.getCurrentTask();
		return this.getIntervento(currentTask);
	}

	@SuppressWarnings("unchecked")
	public Intervento getIntervento(TaskInstance currentTask) {
		Intervento intervento = null;
		List<Intervento> resultList = this.entityManager.createQuery("from Intervento where bpmInfo.tokenId=:tokenId").setParameter("tokenId", currentTask.getToken().getId())
				.getResultList();
		if (resultList.size() > 0) {
			intervento = resultList.get(0);
		} else {
			resultList = this.entityManager.createQuery("from Intervento where bpmInfo.processId=:processId").setParameter("processId", currentTask.getProcessInstance().getId())
					.getResultList();
			if (resultList.size() > 0) {
				intervento = resultList.get(0);
			}
		}
		return intervento;
	}

	@SuppressWarnings("unchecked")
	public List<Intervento> getListaInterventi() {

		List<Integer> idTipiIntervento = this.getIdTipiIntervento(this.identity.getCredentials().getUsername());

		idTipiIntervento.add(-1);

		return this.entityManager.createQuery("from Intervento where sottotipoIntervento.tipoIntervento.id in (:idList) order by id desc").setParameter("idList", idTipiIntervento)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Segnalazione> getListaSegnalazioni() {
		if (this.identity.hasRole(Constants.ADMIN.toString())) {
			return this.entityManager.createQuery("from Segnalazione order by id desc").getResultList();
		} else if (this.identity.hasRole(Constants.CITTADINO.toString())) {
			Cittadino c = Authenticator.findCittadino(this.entityManager, null, this.identity.getCredentials().getUsername());
			return this.entityManager.createQuery("from Segnalazione where cittadino=:cittadino order by id desc").setParameter("cittadino", c).getResultList();
		} else {
			List<Number> l = this.entityManager
					.createNativeQuery("select distinct bv.id from AlertingRevisionEntity are,BPMInfo_versions bv where bv._revision=are.id and are.username=:username")
					.setParameter("username", this.identity.getCredentials().getUsername()).getResultList();
			l.add(-1);
			return this.entityManager.createQuery("from Segnalazione where bpmInfo.id in (:l) order by id desc").setParameter("l", l).getResultList();

		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getMailFor(String actorId) {

		Map swimlanes = this.processInstance.getTaskMgmtInstance().getSwimlaneInstances();
		if (swimlanes.keySet().contains(actorId)) {
			SwimlaneInstance swimlane = (SwimlaneInstance) swimlanes.get(actorId);
			actorId = swimlane.getActorId();
		}
		People p = this.entityManager.find(People.class, actorId);
		if (p != null) {
			return p.getEmail();
		} else {
			List<Cittadino> l = this.entityManager.createQuery("from Cittadino where username=:username").setParameter("username", actorId).getResultList();
			if (l.size() > 0) {
				return l.get(0).getEmail();
			}
		}
		return null;
	}

	public List<Info> getMessageList(Segnalazione segnalazione) {
		List<Info> messaggi = new ArrayList<Info>();
		VersionsReader reader = VersionsReaderFactory.get(this.entityManager);
		if (segnalazione != null && segnalazione.getId() != null) {
			Integer sId = segnalazione.getId();
			for (Number n : reader.getRevisions(Segnalazione.class, sId)) {
				Segnalazione s = reader.find(Segnalazione.class, sId, n);
				if (s.getMessaggio() != null) {
					AlertingRevisionEntity r = reader.findRevision(AlertingRevisionEntity.class, n);
					messaggi.add(new Info(new Date(r.getTimestamp()), s.getMessaggio(), r.getUsername()));
				}
			}
		}
		Collections.reverse(messaggi);
		return messaggi;
	}

	public String getNewMessage() {
		return this.newMessage;
	}

	public String getNewMessageIntervento() {
		return this.newMessageIntervento;
	}

	private boolean getParameterBoolValue(AppParam appParam) {
		String paramValue = this.entityManager.find(AppParam.class, appParam.getKey()).getValue();
		if (paramValue.trim().equals("") || paramValue.trim().equals("false")) {
			return false;
		}
		return true;
	}

	public Segnalazione getSegnalazione() {
		TaskInstance currentTask = this.getCurrentTask();
		return this.getSegnalazione(currentTask);
	}

	@SuppressWarnings("unchecked")
	public Segnalazione getSegnalazione(TaskInstance currentTask) {
		if (currentTask == null) {
			return null;
		}

		if (this.sCache.get(currentTask.getId()) == null) {
			Segnalazione segnalazione = null;
			List<Segnalazione> resultList = this.entityManager.createQuery("from Segnalazione where bpmInfo.processId=:processId")
					.setParameter("processId", currentTask.getProcessInstance().getId()).getResultList();
			if (resultList.size() > 0) {
				segnalazione = resultList.get(0);

			}
			this.sCache.put(currentTask.getId(), segnalazione);
		}

		return this.sCache.get(currentTask.getId());
	}

	@SuppressWarnings("unchecked")
	public Segnalazione getSegnalazioneFromJBPMTask(org.jbpm.taskmgmt.exe.TaskInstance currentTask) {
		if (currentTask == null) {
			return null;
		}
		Segnalazione segnalazione = null;
		List<Segnalazione> resultList = this.entityManager.createQuery("from Segnalazione where bpmInfo.processId=:processId")
				.setParameter("processId", currentTask.getProcessInstance().getId()).getResultList();
		if (resultList.size() > 0) {
			segnalazione = resultList.get(0);

		}
		return segnalazione;
	}

	@SuppressWarnings("unchecked")
	public TaskInstance getSingleTask() {
		List<TaskInstance> l = this.jbpmContext.getTaskList(Identity.instance().getCredentials().getUsername());
		if (l.size() == 1) {
			return l.get(0);
		}
		return null;
	}

	public String getSmistatoreFinale() {
		if (this.getSegnalazione() != null && this.getSegnalazione().getSottotipoSegnalazione() != null
				&& this.getSegnalazione().getSottotipoSegnalazione().getTipoSegnalazione().getSmistatoreFinale() != null) {
			return this.getSegnalazione().getSottotipoSegnalazione().getTipoSegnalazione().getSmistatoreFinale();
		}
		SwimlaneInstance swimlaneInstance = this.getCurrentTask().getProcessInstance().getTaskMgmtInstance().getSwimlaneInstance(Constants.SMISTATORE.toString());
		if (swimlaneInstance != null) {
			return swimlaneInstance.getActorId();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<String> getSmistatori() {
		String q = "select distinct(elements(uc.gestori)) from UfficioCompetente uc where uc in (select ufficioSmistatore from TipoSegnalazione)";
		return this.entityManager.createQuery(q).getResultList();
	}

	public HashMap<Segnalazione, List<TaskInstance>> getTaskCache() {
		return this.taskCache;
	}

	public String getTestoApertura() {
		return this.alertingProcess.getTestoApertura();
	}

	public String getTestoChiusura() {
		return this.alertingProcess.getTestoChiusura();
	}

	public String getTitleForCurrentTask() {
		return this.getTitleForTask(this.getCurrentTask());
	}

	public String getTitleForTask(TaskInstance task) {
		try {
			Segnalazione segnalazione = this.getSegnalazione(task);
			if (segnalazione == null) {
				return "Nuova Segnalazione";
			}

			return segnalazione.getId() + " - " + segnalazione.getOggetto();
		} catch (Exception e) {
			return "N/D";
		}

	}

	public boolean historyEnabled() {
		return this.getParameterBoolValue(AppParam.HISTORY_ENABLED);
	}

	@Factory("tipiIntervento")
	public List<Integer> idTipiIntervento() {
		return this.getIdTipiIntervento(Identity.instance().getCredentials().getUsername());
	}

	public boolean isGestore(String username) {
		return this.getIdTipiIntervento(username).size() > 0;
	}

	public boolean isSmistatore(String username) {
		return this.identity.hasRole(Constants.SMISTATORE.toString());
	}

	public boolean isTaskAssignee() {
		try {
			if (this.getCurrentTask() == null) {
				return false;
			}
			return this.getCurrentTask().getActorId().equals(this.identity.getCredentials().getUsername());
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	private List<TaskInstance> retrieveAttivita(Segnalazione segnalazione) {
		ArrayList<TaskInstance> res = new ArrayList<TaskInstance>();
		GraphSession gs = this.jbpmContext.getGraphSession();
		if (segnalazione == null) {
			return res;
		}
		ProcessInstance processInstance = gs.getProcessInstance(segnalazione.getBpmInfo().getProcessId());
		if (processInstance != null) {
			Collection<TaskInstance> c = processInstance.getTaskMgmtInstance().getTaskInstances();
			for (TaskInstance ti : c) {
				if (ti.isOpen()) {
					res.add(ti);
				}
			}
		}
		return res;
	}

	public void setCurrentProcessId(Long currentProcessId) {
		this.currentProcessId = currentProcessId;
	}

	public void setCurrentTaskId(Long currentTaskId) {
		this.currentTaskId = currentTaskId;
	}

	public void setNewMessage(String newMessage) {
		this.newMessage = newMessage;
	}

	public void setNewMessageIntervento(String newMessageIntervento) {
		this.newMessageIntervento = newMessageIntervento;
	}

	@SuppressWarnings("unchecked")
	public void setStatoSegnalazione(String stato) {
		List<Stato> l = this.entityManager.createQuery("from Stato where nome=:stato").setParameter("stato", stato).getResultList();
		if (l.size() > 0) {
			Segnalazione segnalazione = this.getSegnalazione();
			segnalazione.setStato(l.get(0));
			this.entityManager.merge(segnalazione);
		}
	}

	public void setTaskCache(HashMap<Segnalazione, List<TaskInstance>> taskCache) {
		this.taskCache = taskCache;
	}

	public void setTestoChiusura(String testoChiusura) {
		this.alertingProcess.setTestoChiusura(testoChiusura);
	}

	public String transition(String transition) throws Exception {

		if (this.getSegnalazione().getStato().getNome().equals(Stato.defaults[0].getNome())) {
			this.getSegnalazione().setStato(
					(Stato) this.entityManager.createQuery("from Stato where nome=:n").setParameter("n", Stato.defaults[1].getNome()).getResultList().get(0));

		}

		this.beforeTaskEnd();
		this.getCurrentTask().end(transition);
		return AlertingController.HOME;
	}

	public String update() {
		this.beforeTaskEnd();
		return AlertingController.HOME;
	}

	public boolean userInLavorazione() {
		return this.getParameterBoolValue(AppParam.UTENTE_IN_LAVORAZIONE);
	}

}
