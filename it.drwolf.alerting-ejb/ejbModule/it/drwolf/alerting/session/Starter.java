package it.drwolf.alerting.session;

import it.drwolf.alerting.entity.AppParam;
import it.drwolf.alerting.util.timer.Timer;

import java.util.Date;

import javax.ejb.Remove;
import javax.persistence.EntityManager;

import org.apache.commons.lang.time.DateUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("starter")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class Starter {

	// public static final String TIMER_CRON = "00 00 06 * * ?";
	// public static final String TIMER_CRON = "*/5 * * * * ?";

	public static final Long TIMER_TIMEOUT = new Long(10 * 1000);

	public static final Date TIMER_START = new Date(System.currentTimeMillis() + DateUtils.MILLIS_PER_MINUTE * 0);

	public static final Date TIMER_END = new Date(System.currentTimeMillis() + DateUtils.MILLIS_PER_DAY * 365 * 100);

	@In
	private Timer alertingTimer;

	@In
	private EntityManager entityManager;

	private String applicationName;

	private String applicationDescription;

	private String welcomeText;

	private String iscrizioniPath;

	private void checkParams() {

		AppParam name = this.entityManager.find(AppParam.class, AppParam.APP_NAME.getKey());
		if (name != null) {
			this.applicationName = name.getValue();
		}
		AppParam description = this.entityManager.find(AppParam.class, AppParam.APP_DESCRIPTION.getKey());
		if (description != null) {
			this.applicationDescription = description.getValue();
		}

		AppParam iscrizioni = this.entityManager.find(AppParam.class, AppParam.ISCRIZIONI_URL.getKey());
		if (description != null) {
			this.iscrizioniPath = iscrizioni.getValue();
		}

		AppParam wt = this.entityManager.find(AppParam.class, AppParam.APP_WELCOME.getKey());
		if (wt != null) {
			this.welcomeText = wt.getValue();
		}
		AppParam ap = this.entityManager.find(AppParam.class, AppParam.APP_ASSEGNAZIONE_POOL.getKey());
		if (ap != null && "true".equals(ap.getValue())) {
			AlertingController.setAssegnazionePool(true);
		}

	}

	@Remove
	@Destroy
	public void destroy() {
	}

	public String getApplicationDescription() {
		return this.applicationDescription;
	}

	public String getApplicationName() {
		return this.applicationName;
	}

	public String getIscrizioniPath() {
		return this.iscrizioniPath;
	}

	public String getWelcomeText() {
		return this.welcomeText;
	}

	public void setIscrizioniPath(String iscrizioniPath) {
		this.iscrizioniPath = iscrizioniPath;
	}

	@Create
	public void start() {
		this.checkParams();
		this.startTimer();
	}

	private void startTimer() {
		this.alertingTimer.cron(this.entityManager.find(AppParam.class, AppParam.CRON_TIME.getKey()).getValue());
	}
}
