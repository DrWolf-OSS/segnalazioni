package it.drwolf.alerting.util;

import it.drwolf.alerting.entity.AppParam;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.Recipient;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;

@Name("mailSender")
public class MailSender {

	@In
	private EntityManager entityManager;

	public void sendSimpleMail(List<String> rcpt, String subject, String msg) {
		try {

			EntityManager em = this.entityManager;

			MandrillApi mandrillApi = new MandrillApi(em.find(AppParam.class,
					AppParam.APP_MAIL_PASSWORD.getKey()).getValue());

			// create your message
			MandrillMessage message = new MandrillMessage();
			message.setSubject(subject);
			message.setText(msg);
			message.setFromName(em.find(AppParam.class,
					AppParam.APP_MAIL_FROM_NAME.getKey()).getValue());
			message.setFromEmail(em.find(AppParam.class,
					AppParam.APP_MAIL_FROM_ADDRESS.getKey()).getValue());

			String mailTrap = this.entityManager.find(AppParam.class,
					"app.mail.trap").getValue();

			ArrayList<Recipient> recipients = new ArrayList<Recipient>();
			for (String to : rcpt) {
				// add recipients
				Recipient recipient = new Recipient();
				if (mailTrap.equalsIgnoreCase("null")) {
					recipient.setEmail(to);
				} else {
					recipient.setEmail(mailTrap);
				}

				recipients.add(recipient);
				message.setTo(recipients);
				message.setPreserveRecipients(true);

			}

			try {
				for (MandrillMessageStatus mms : mandrillApi.messages().send(
						message, false)) {

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
