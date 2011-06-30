package it.drwolf.iscrizioni.util;

import it.drwolf.iscrizioni.entity.AppParam;

import javax.persistence.EntityManager;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.jboss.seam.Component;

@Path("/send")
public class MailSender {

	@SuppressWarnings("unchecked")
	@POST
	@Path("/all")
	public String sendToAll(@FormParam("from") String from,
			@FormParam("fromName") String fromName,
			@FormParam("subject") String subject,
			@FormParam("html") String htmlBody,
			@FormParam("text") String textBody,
			@FormParam("secret") String secret) throws Exception {

		AsyncSender asyncSender = (AsyncSender) Component
				.getInstance("asyncSender");
		EntityManager entityManager = (EntityManager) Component
				.getInstance("entityManager");
		if (!entityManager.find(AppParam.class, AppParam.APP_MAIL_SECRET)
				.getValue().equals(secret)) {
			return "SECRET ERRATO!";
		}
		asyncSender.sendToAll(from, fromName, subject, htmlBody, textBody,
				secret);
		return "OK";

	}
}
