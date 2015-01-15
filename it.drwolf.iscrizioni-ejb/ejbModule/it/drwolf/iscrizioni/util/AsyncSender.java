package it.drwolf.iscrizioni.util;

import it.drwolf.iscrizioni.entity.AppParam;
import it.drwolf.iscrizioni.entity.Iscritto;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.codec.digest.DigestUtils;
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

	@In
	private MailMonitor mailMonitor;

	@SuppressWarnings("unchecked")
	@Asynchronous
	public void sendToAll(String from, String fromName, String subject,
			String htmlBody, String textBody, String secret, String groups,
			String services) throws Exception {

		AppParam port = this.entityManager.find(AppParam.class,
				AppParam.APP_MAIL_PORT);
		String mailHost = this.entityManager.find(AppParam.class,
				AppParam.APP_MAIL_HOST).getValue();
		String mailTrap = this.entityManager.find(AppParam.class,
				AppParam.APP_MAIL_TRAP).getValue();

		String mailUser = null;
		try {
			mailUser = this.entityManager.find(AppParam.class,
					AppParam.APP_MAIL_USER).getValue();
		} catch (Exception e1) {

		}
		String mailPass = null;
		try {
			mailPass = this.entityManager.find(AppParam.class,
					AppParam.APP_MAIL_PASSWORD).getValue();
		} catch (Exception e1) {

		}

		Query q = this.entityManager.createQuery("from Iscritto order by id");

		if (groups != null) {
			q = this.entityManager
					.createQuery(
							"select i from Iscritto i join i.gruppi g where g.id in (:l) group by i.id")
							.setParameter("l", Arrays.asList(groups.split(",")));
		} else if (services != null) {
			q = this.entityManager
					.createQuery(
							"select i from Iscritto i join i.opzioniServizi o where o.id in (:l) group by i.id")
							.setParameter("l", Arrays.asList(services.split(",")));
		}

		List<Iscritto> resultList = q.getResultList();

		String text = from + fromName + subject + htmlBody + textBody
				+ resultList + groups + services;

		String hash = DigestUtils.shaHex(text);

		if (this.mailMonitor.getInCorso().get(hash) != null) {
			return;
		} else {
			this.mailMonitor.getInCorso().put(hash, new LinkedList<Iscritto>());
			for (Iscritto i : resultList) {
				this.mailMonitor.getInCorso().get(hash).add(i);
			}
		}

		while (this.mailMonitor.getInCorso().get(hash).peek() != null) {
			Iscritto i = this.mailMonitor.getInCorso().get(hash).poll();
			try {
				Email email = null;
				if (i.isTextMail() && ("" + textBody).length() > 0) {
					email = new SimpleEmail();
					email.setMsg(textBody);
				} else {
					email = new HtmlEmail();
					((HtmlEmail) email).setHtmlMsg(htmlBody);
				}
				if (!"null".equals(mailTrap)) {
					email.addTo(mailTrap);
				} else {
					if (i.getCognome() != null) {
						email.addTo(i.getEmail(),
								i.getNome() + " " + i.getCognome());
					} else {
						email.addTo(i.getEmail());
					}
				}
				email.setFrom(from, fromName);
				email.setHostName(mailHost);

				if (mailUser != null && mailPass != null) {
					email.setAuthentication(mailUser, mailPass);
				}

				email.setSubject(subject);

				if (port != null) {
					email.setSmtpPort(Integer.parseInt(port.getValue()));
				}
				email.send();
				this.mailMonitor.setLastLog("Invio: " + new Date() + " - "
						+ i.getEmail());
			} catch (Exception e) {
				this.mailMonitor.setLastLog("ERRORE Invio: " + new Date()
				+ " - " + i.getEmail());
				System.out.println("Errore invio a " + i.getEmail());
				e.printStackTrace();
			}

		}
		this.mailMonitor.getInCorso().remove(hash);

	}
}
