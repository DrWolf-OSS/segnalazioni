package it.drwolf.iscrizioni.util;

import it.drwolf.iscrizioni.entity.AppParam;
import it.drwolf.iscrizioni.entity.Iscritto;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;

@Name("asyncSender")
public class AsyncSender {

	@In
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Asynchronous
	public void sendToAll(String from, String fromName, String subject,
			String htmlBody, String textBody, String secret) throws Exception {

		AppParam port = this.entityManager.find(AppParam.class,
				AppParam.APP_MAIL_PORT);
		String mailHost = this.entityManager.find(AppParam.class,
				AppParam.APP_MAIL_HOST).getValue();

		for (Iscritto i : (List<Iscritto>) this.entityManager.createQuery(
				"from Iscritto").getResultList()) {
			Email email = null;
			if (i.isTextMail()) {
				email = new SimpleEmail();
				email.setMsg(textBody);
			} else {
				email = new HtmlEmail();
				((HtmlEmail) email).setHtmlMsg(htmlBody);
			}
			email.addTo(i.getEmail(), i.getNome() + " " + i.getCognome());
			email.setFrom(from, fromName);
			email.setHostName(mailHost);
			email.setSubject(subject);
			if (port != null) {
				email.setSmtpPort(Integer.parseInt(port.getValue()));
			}
			email.send();

		}

	}
}
