package it.drwolf.alerting.util.timer;

import it.drwolf.alerting.entity.AppParam;
import it.drwolf.alerting.entity.Segnalazione;
import it.drwolf.alerting.session.Reports;
import it.drwolf.alerting.util.MailSender;
import it.drwolf.alerting.util.converters.PeopleConverter;
import it.drwolf.eloise.web.entity.People;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.poi.hssf.record.formula.functions.True;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.joda.time.DateTime;
import org.joda.time.Days;

@Name("notifier")
public class Notifier {

	private static final String SENZA_RISPOSTA_INTERLOCUTORIA = "(select count(*) from Risposta r where r.segnalazione=s and (r.ricevuta is null or r.ricevuta=false))=0 ";

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"dd/MM/yyyy");

	@In
	private EntityManager entityManager;

	@In(create = true)
	private Reports reports;

	@In(create = true)
	private MailSender mailSender;
	
	@In(create = true)
	PeopleConverter peopleConverter;

	@SuppressWarnings("unchecked")
	@Transactional
	public void checkExpired() {

		List<Segnalazione> daNotificare = this.entityManager
				.createQuery(
						"from Segnalazione s where s.chiusura is null and s.notificatoRitardo!=true and s.cittadino is not null and "
								+ Notifier.SENZA_RISPOSTA_INTERLOCUTORIA
								+ " and s.scadenza <:now").setParameter("now",
						new Date()).getResultList();
		if (daNotificare.size() == 0) {
			return;
		}
		List<String> supervisori = this.entityManager
				.createQuery(
						"select p.email from People p where p.idpeople in (select elements(gr.users) from GlobalRole gr where gr.name='supervisor')")
				.getResultList();

		String msg = "Segnalazioni in ritardo:\r\n\r\n";
		for (Segnalazione s : daNotificare) {
			msg += s.getId() + " - " + s.getOggetto() + "(" + s.getCittadino().toString()
					+ ")  ";
					String parametro = this.entityManager.find( AppParam.class, AppParam.ASSEGNATARIO_IN_MAIL.getKey()).getValue();
					if (parametro != null && parametro.trim().equals("true")) {
						 msg += peopleConverter.getAsString(null, null, this.reports.getAssignee(s)) ;
					}

					msg +=	"\r\n";
			s.setNotificatoRitardo(true);
			this.entityManager.merge(s);
		}
		msg += "\r\n"
				+ this.entityManager.find(AppParam.class, AppParam.APP_URL
						.getKey());
		this.mailSender.sendSimpleMail(supervisori,
				"Notifica ritardo segnalazioni", msg);

	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void checkExpiring() {

		Long start = Long.parseLong(this.entityManager.find(AppParam.class,
				AppParam.APP_SEGNALAZIONE_REMINDER_START.getKey()).getValue());

		Long reminder = Long.parseLong(this.entityManager.find(AppParam.class,
				AppParam.APP_SEGNALAZIONE_REMINDER.getKey()).getValue());

		List<Segnalazione> segnalazioni = this.entityManager.createQuery(
				"from Segnalazione s where s.chiusura is null and "
						+ Notifier.SENZA_RISPOSTA_INTERLOCUTORIA)
				.getResultList();

		for (Segnalazione segnalazione : segnalazioni) {
			DateTime scadenza = new DateTime(segnalazione.getScadenza())
					.minusDays(start.intValue());

			if (scadenza.isBeforeNow()) {

				int days = Days.daysBetween(scadenza, new DateTime()).getDays();
				if ((days % reminder) == 0) {

					People p = this.entityManager.find(People.class,
							this.reports.getAssignee(segnalazione));
					if (p != null) {

						String ritscad = segnalazione.getScadenza().after(
								new Date()) ? "ritardo" : "scadenza";
						String msg = "Segnalazione in " + ritscad + ":\r\n";
						
						msg += segnalazione.getId() + " - " + segnalazione.getOggetto() + "(" + Notifier.sdf.format(segnalazione
								.getScadenza()) 
						+ ")  ";
						String parametro = this.entityManager.find( AppParam.class, AppParam.ASSEGNATARIO_IN_MAIL.getKey()).getValue();
						if (parametro != null && parametro.trim().equals("true")) {
							 msg += peopleConverter.getAsString(null, null, this.reports.getAssignee(segnalazione)) ;
						}

						msg +=	"\r\n";
						
						
						msg += "\r\n"
								+ this.entityManager.find(AppParam.class,
										AppParam.APP_URL.getKey());

						this.mailSender.sendSimpleMail(Arrays
								.asList(new String[] { p.getEmail() }),
								" - Notifica " + ritscad + " segnalazione", msg);

					}
				}
			}

		}
	}

}
