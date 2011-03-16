package it.drwolf.alerting.util.timer;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.IntervalCron;
import org.jboss.seam.async.QuartzTriggerHandle;

@Name("alertingTimer")
@AutoCreate
public class Timer {

	@In(create = true)
	private Notifier notifier;

	@Asynchronous
	public QuartzTriggerHandle cron(@IntervalCron String cron) {
		QuartzTriggerHandle handle = new QuartzTriggerHandle("Alerting cron");

		this.notifier.checkExpiring();
		this.notifier.checkExpired();

		return handle;

	}
}
