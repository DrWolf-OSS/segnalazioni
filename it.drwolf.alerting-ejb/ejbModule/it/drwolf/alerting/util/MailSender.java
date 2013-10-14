package it.drwolf.alerting.util;

import it.drwolf.alerting.entity.AppParam;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("mailSender")
public class MailSender {

	@In
	private EntityManager entityManager;

	public void sendSimpleMail(List<String> recipients, String subject,
			String msg) {
		try {
			SimpleEmail email = new SimpleEmail();

			EntityManager em = this.entityManager;

			email.setHostName(em.find(AppParam.class, AppParam.APP_MAIL_HOST)
					.getValue());
			email.setFrom(
					em.find(AppParam.class, AppParam.APP_MAIL_FROM_ADDRESS)
							.getValue(),
					em.find(AppParam.class, AppParam.APP_MAIL_FROM_NAME)
							.getValue());

			AppParam port = em.find(AppParam.class, AppParam.APP_MAIL_PORT);
			if (port != null) {
				email.setSmtpPort(Integer.parseInt(port.getValue()));
			}

			AppParam pwd = em.find(AppParam.class, AppParam.APP_MAIL_PASSWORD);
			if (pwd != null) {
				AppParam user = em.find(AppParam.class, AppParam.APP_MAIL_USER);
				email.setAuthentication(user.getValue(), pwd.getValue());
			}

			String mailTrap = this.entityManager.find(AppParam.class,
					"app.mail.trap").getValue();
			if (mailTrap.equalsIgnoreCase("null")) {
				for (String to : recipients) {
					email.addTo(to);
				}
			} else {
				email.addTo(mailTrap);
			}
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
