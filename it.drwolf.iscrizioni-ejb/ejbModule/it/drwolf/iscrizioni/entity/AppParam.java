package it.drwolf.iscrizioni.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(catalog = "iscrizioni")
public class AppParam {

	public static final String APP_SKIN = "app.skin";

	public static final String APP_PRIVACY_URL = "app.privacy.url";

	public static final String APP_SECRET = "app.secret";

	public static final String LOCAL_ADMIN_PASSWORD = "local.admin.password";

	public static final String APP_NAME = "app.name";

	public static final String APP_NEW_SUBSCRIPTION = "app.new.subscription";

	public static final String APP_EDIT_SUBSCRIPTION = "app.edit.subscription";

	public static final String APP_MAIL_HOST = "app.mail.host";

	public static final String APP_MAIL_FROM_ADDRESS = "app.mail.from.address";

	public static final String APP_MAIL_FROM_NAME = "app.mail.from.name";

	public static final String APP_MAIL_PORT = "app.mail.port";

	public static final String APP_MAIL_USER = "app.mail.user";

	public static final String APP_MAIL_PASSWORD = "app.mail.password";

	public static final String APP_URL = "app.url";

	public static final String APP_INCOMPLETI_QUERY = "app.incompleti.query";

	public static final String APP_MAIL_SECRET = "app.mail.secret";

	public static final String APP_FIELDS_REQ = "app.fields.required";

	public static final String APP_FIELDS_HIDDEN = "app.fields.hidden";

	public static AppParam[] defaults = new AppParam[] {
			new AppParam(AppParam.LOCAL_ADMIN_PASSWORD, "changeme"),
			new AppParam("sso.url", "http://localhost:8180/sso"),
			new AppParam("app.url", "http://localhost:8180/iscrizioni"),
			new AppParam("sso.appid", "iscrizioni"),
			new AppParam(AppParam.APP_MAIL_HOST, "localhost"),
			new AppParam(AppParam.APP_MAIL_FROM_ADDRESS, "jboss@drwolf.it"),
			new AppParam(AppParam.APP_MAIL_FROM_NAME,
					"Iscrizione ai servizi del Comune"),
			new AppParam(AppParam.APP_SECRET, UUID.randomUUID().toString()),
			new AppParam(AppParam.APP_SKIN, "blueSky"),
			new AppParam(AppParam.APP_NEW_SUBSCRIPTION,
					"I dati per completare l'iscrizione sono stati inviati all'inidirizzo {0}"),
			new AppParam(AppParam.APP_EDIT_SUBSCRIPTION,
					"Il codice utente e' stato inviato all'indirizzo {0}"),
			new AppParam(AppParam.APP_PRIVACY_URL, "/"),
			new AppParam(
					AppParam.APP_INCOMPLETI_QUERY,
					"SELECT I.id, I.cognome, I.nome, I.email, I.cellulare FROM Iscritto I where (I.id not in (select iscritti_id from Gruppo_Iscritto)) and (I.email is null and I.id in (select Iscritto_id from Iscritto_OpzioneServizio where opzioniServizi_id like ('newsletter%') or opzioniServizi_id like ('eventi%') or opzioniServizi_id like ('comunicati%')))"),
			new AppParam(AppParam.APP_NAME, "Sistema di iscrizione ai servizi"),
			new AppParam(AppParam.APP_MAIL_SECRET, "mailsecret")

	};

	private String id;

	private String value;

	public AppParam() {

	}

	public AppParam(String id, String value) {
		super();
		this.id = id;
		this.value = value;
	}

	@Id
	public String getId() {
		return this.id;
	}

	public String getValue() {
		return this.value;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
