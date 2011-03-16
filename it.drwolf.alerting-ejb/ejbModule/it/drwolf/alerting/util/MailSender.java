package it.drwolf.alerting.util;

import it.drwolf.alerting.entity.AppParam;

import java.util.List;

import javax.mail.Session;
import javax.persistence.EntityManager;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("mailSender")
public class MailSender {
	@In("org.jboss.seam.mail.mailSession")
	private Session session;
	@In
	private EntityManager entityManager;

	public void sendSimpleMail(List<String> recipients, String subject,
			String msg) {
		try {
			SimpleEmail email = new SimpleEmail();
			email.setMailSession(this.session);
			for (String to : recipients) {
				email.addTo(to);
			}
			email.setFrom(this.entityManager.find(AppParam.class,
					AppParam.APP_MAIL_FROM.getKey()).getValue());
			email.setSubject(this.entityManager.find(AppParam.class,
					AppParam.APP_NAME.getKey()).getValue()
					+ " - " + subject);

			email.setMsg(msg);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

}
