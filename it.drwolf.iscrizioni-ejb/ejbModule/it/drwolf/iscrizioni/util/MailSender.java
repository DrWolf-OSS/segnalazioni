package it.drwolf.iscrizioni.util;

import it.drwolf.iscrizioni.entity.AppParam;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.jboss.seam.Component;

@Path("/send")
public class MailSender {

	private final static Set<String> lastSMS = new HashSet<String>();

	@SuppressWarnings("unchecked")
	@POST
	@Path("/sms")
	public String sendSMS(@FormParam("from") String from,
			@FormParam("text") String text, @FormParam("secret") String secret,
			@FormParam("groups") String groups,
			@FormParam("services") String services,
			@FormParam("urlinfo") String urlinfo) {

		EntityManager entityManager = (EntityManager) Component
				.getInstance("entityManager");
		if (!entityManager.find(AppParam.class, AppParam.APP_MAIL_SECRET)
				.getValue().equals(secret)) {
			return "SECRET ERRATO!";
		}

		String ht = from + text + groups + services;

		String hash = DigestUtils.shaHex(ht);

		if (MailSender.lastSMS.contains(hash)) {
			return "SMS duplicato, invio annullato";
		}
		MailSender.lastSMS.add(hash);

		Set<String> numeri = new HashSet<String>();

		if (groups != null) {
			numeri.addAll(entityManager
					.createQuery(
							"select distinct (i.cellulare) from Iscritto i join i.gruppi g where i.cellulare is not null and g.id in (:l)")
					.setParameter("l", Arrays.asList(groups.split(",")))
					.getResultList());
		} else if (services != null) {
			numeri.addAll(entityManager
					.createQuery(
							"select distinct (i.cellulare) from Iscritto i join i.opzioniServizi o i.id where i.cellulare is not null and o.id in (:l)")
					.setParameter("l", Arrays.asList(services.split(",")))
					.getResultList());
		}

		if (numeri.size() == 0) {
			return "Nessun destinatario corrispondente";
		}

		try {

			FTPClient f = new FTPClient();
			f.connect("smsftp.mobyt.it");
			f.login(entityManager.find(AppParam.class, AppParam.APP_SMS_USER)
					.getValue(),
					entityManager.find(AppParam.class, AppParam.APP_SMS_PWD)
							.getValue());
			f.changeWorkingDirectory("incoming");
			OutputStream smsout = f.storeFileStream(hash.substring(1, 10)
					+ ".txt");
			OutputStreamWriter smsw = new OutputStreamWriter(smsout);
			for (String n : numeri) {

				if (!("" + n).startsWith("+39")) {
					n = "+39" + n;
				}
				smsw.write(String.format("%-25s%-25s%-160s%010d%-2s%-20s\n", n,
						from, text, 48, "a", ""));
			}
			smsw.close();
			smsout.close();
			if (!f.completePendingCommand()) {
				f.logout();
				f.disconnect();
				return "ERRORE trasferimento sms";
			}
			OutputStream cmdout = f.storeFileStream(hash.substring(1, 10)
					+ ".txt.do_send");
			OutputStreamWriter cmdw = new OutputStreamWriter(smsout);
			cmdw.write("￼￼ACTION=ALERT\nALERT_WEB=on\nURL=" + urlinfo
					+ "\nTICKET=" + hash);
			cmdw.close();
			cmdout.close();
			if (!f.completePendingCommand()) {
				f.logout();
				f.disconnect();
				return "ERRORE trasferimento sms";
			}
			f.logout();
			f.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
			return "Errore invio: " + e;
		}

		return String.format("OK\nInviati %d SMS:\n%s", numeri.size(), numeri);
	}

	@POST
	@Path("/all")
	public String sendToAll(@FormParam("from") String from,
			@FormParam("fromName") String fromName,
			@FormParam("subject") String subject,
			@FormParam("html") String htmlBody,
			@FormParam("text") String textBody,
			@FormParam("secret") String secret,
			@FormParam("groups") String groups,
			@FormParam("services") String services) throws Exception {

		AsyncSender asyncSender = (AsyncSender) Component
				.getInstance("asyncSender");
		EntityManager entityManager = (EntityManager) Component
				.getInstance("entityManager");
		if (!entityManager.find(AppParam.class, AppParam.APP_MAIL_SECRET)
				.getValue().equals(secret)) {
			return "SECRET ERRATO!";
		}
		asyncSender.sendToAll(from, fromName, subject, htmlBody, textBody,
				secret, groups, services);
		return "OK";

	}
}
