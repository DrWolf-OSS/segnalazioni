package it.drwolf.iscrizioni.util;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.jboss.seam.Component;

import it.drwolf.iscrizioni.entity.AppParam;

@Path("/send")
public class MailSender {

	private final static Set<String> lastSMS = new HashSet<String>();

	public static String byteArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for (byte b : a) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/sms")
	public String sendSMS(@FormParam("from") String from, @FormParam("text") String text,
			@FormParam("secret") String secret, @FormParam("groups") String groups,
			@FormParam("numbers") String numbers, @FormParam("services") String services,
			@FormParam("urlinfo") String urlinfo) {

		EntityManager entityManager = (EntityManager) Component.getInstance("entityManager");
		if (!entityManager.find(AppParam.class, AppParam.APP_MAIL_SECRET).getValue().equals(secret)) {
			return "SECRET ERRATO!";
		}

		String ht = from + text + groups + services + numbers;

		String hash = DigestUtils.shaHex(ht);

		if (MailSender.lastSMS.contains(hash)) {
			return "SMS duplicato, invio annullato";
		}

		MailSender.lastSMS.add(hash);

		Set<String> numeri = new HashSet<String>();

		if (numbers != null) {
			numeri.addAll(Arrays.asList(numbers.split("\\s+")));
		}

		if (groups != null) {
			numeri.addAll(entityManager
					.createQuery(
							"select distinct (i.cellulare) from Iscritto i join i.gruppi g where i.cellulare is not null and g.id in (:l)")
					.setParameter("l", Arrays.asList(groups.split(","))).getResultList());
		} else if (services != null) {
			numeri.addAll(entityManager
					.createQuery(
							"select distinct (i.cellulare) from Iscritto i join i.opzioniServizi o i.id where i.cellulare is not null and o.id in (:l)")
					.setParameter("l", Arrays.asList(services.split(","))).getResultList());
		}

		if (numeri.size() == 0) {
			return "Nessun destinatario corrispondente";
		}

		Set<String> cn = new HashSet<String>();
		for (String n : numeri) {
			if (n.startsWith("+39")) {
				cn.add(n.trim());
			} else {
				cn.add("+39" + n.trim());
			}
		}
		numeri = cn;

		GetMethod method = new GetMethod("http://smsweb.mobyt.it/sms-gw/sendsmart");
		try {

			String id = entityManager.find(AppParam.class, AppParam.APP_SMS_USER).getValue();

			MessageDigest md = MessageDigest.getInstance("MD5");

			String password = entityManager.find(AppParam.class, AppParam.APP_SMS_PWD).getValue();

			NameValuePair rcpt = null;
			String message = "";

			if (numeri.size() == 1) {
				rcpt = new NameValuePair("rcpt", numeri.iterator().next());
				message = id + "TEXT" + numeri.iterator().next() + from + text + password;
			} else {
				rcpt = new NameValuePair("rcptbatch", StringUtils.join(numeri, ","));
				message = id + "TEXT" + from + text + password;
			}

			byte[] digest = md.digest(message.getBytes());

			String hex = MailSender.byteArrayToHex(digest);

			method.setQueryString(new NameValuePair[] { new NameValuePair("operation", "TEXT"),
					new NameValuePair("id", id), new NameValuePair("ticket", hex), rcpt,
					new NameValuePair("data", text), new NameValuePair("from", from) });

			HttpClient hc = new HttpClient();

			method.addRequestHeader("Content-Type", "charset=UTF-8");
			hc.executeMethod(method);
			return method.getResponseBodyAsString();

		} catch (Exception e) {
			e.printStackTrace();
			return "Errore invio: " + e;

		} finally {
			method.releaseConnection();
		}

	}

	@POST
	@Path("/all")
	public String sendToAll(@FormParam("from") String from, @FormParam("fromName") String fromName,
			@FormParam("subject") String subject, @FormParam("html") String htmlBody,
			@FormParam("text") String textBody, @FormParam("secret") String secret, @FormParam("groups") String groups,
			@FormParam("services") String services) throws Exception {

		AsyncSender asyncSender = (AsyncSender) Component.getInstance("asyncSender");
		EntityManager entityManager = (EntityManager) Component.getInstance("entityManager");
		if (!entityManager.find(AppParam.class, AppParam.APP_MAIL_SECRET).getValue().equals(secret)) {
			return "SECRET ERRATO!";
		}
		asyncSender.sendToAll(from, fromName, subject, htmlBody, textBody, secret, groups, services);
		return "OK";

	}
}
