package it.drwolf.alerting.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AppParam implements Serializable {

	private static final long serialVersionUID = -227886573909311966L;

	public static final AppParam APP_WELCOME = new AppParam("app.welcometext",
			"Benvenuti nel sistema di segnalazioni del comune");

	public static final AppParam APP_DESCRIPTION = new AppParam(
			"app.description", "Segnalazione gestione e notifiche interventi");

	public static final AppParam APP_NAME = new AppParam("app.name", "SIGN");

	public static final AppParam APP_MAIL_FROM = new AppParam("app.mail.from",
			"jboss@drwolf.it");

	public static final AppParam APP_SKIN = new AppParam("app.skin", "ruby");

	public static final AppParam APP_COMUNE = new AppParam("app.comune",
			"Lastra a Signa");
	public static final AppParam APP_PROVINCIA = new AppParam("app.provincia",
			"FI");
	public static final AppParam APP_SSOAPPID = new AppParam("app.ssoappid",
			"sign");

	public static final AppParam APP_IMG_HEAD = new AppParam("app.img.head",
			"/img/head_cal.jpg");
	public static final AppParam APP_IMG_FOOT = new AppParam("app.img.foot",
			"/img/foot_cal.jpg");

	public static final AppParam APP_URL = new AppParam("app.url",
			"http://localhost:8080/sign");

	public static final AppParam APP_MAIL_HOST = new AppParam("app.mail.host",
			"mail.drwolf.it");

	public static final AppParam APP_SEGNALAZIONE_SCADENZA = new AppParam(
			"app.segnalazione.scadenza", "20");

	public static final AppParam APP_SEGNALAZIONE_REMINDER = new AppParam(
			"app.segnalazione.reminder", "3");

	public static final AppParam APP_SEGNALAZIONE_REMINDER_START = new AppParam(
			"app.segnalazione.reminder.start", "5");

	public static final AppParam APP_DAILY_STATI_DEFAULT = new AppParam(
			"app.daily.stati.default", "aperto");

	public static final AppParam APP_FILTRO_STATI_DEFAULT = new AppParam(
			"app.filtro.stati.default", "aperto,daesaminare");

	public static final AppParam APP_DAILY_STATI_HIDE = new AppParam(
			"app.daily.stati.hide", "chiuso,dilazionato");

	public static final AppParam IQ_SECRET = new AppParam("app.iq.secret",
			"secret");

	public static final AppParam ISCRIZIONI_URL = new AppParam(
			"app.iscrizioni.url", "/iscrizioni");

	public static final AppParam ISCRIZIONI_PUBLIC_URL = new AppParam(
			"app.iscrizioni.publicUrl", "/iscrizioni");

	public static final AppParam APP_ASSEGNAZIONE_POOL = new AppParam(
			"app.assegnazione.pool", "false");

	public static final AppParam APP_RISP_UFFICIO = new AppParam(
			"app.risposte.ufficio", "Lo Sportello del Cittadino");

	public static final AppParam APP_RISP_SALUTI = new AppParam(
			"app.risposte.saluti", "Distinti saluti,");

	public static final AppParam APP_RISP_SALUTI_PS = new AppParam(
			"app.risposte.salutips", "Sportello del Cittadino");

	public static final AppParam RIPASSA_URP_CON_MAIL_TEL = new AppParam(
			"ripassa.urp.mail.tel", "false");

	public static final AppParam ASSEGNATARIO_IN_MAIL = new AppParam(
			"assegnatario.in.mail", "false");

	public static final AppParam RIPASSA_URP_SEMPRE = new AppParam(
			"ripassa.urp.sempre", "false");

	public static final AppParam ALFRESCO_URL = new AppParam("alfresco.url",
			"http://web.comune.calenzano.fi.it/alfresco");

	public static final AppParam ALFRESCO_USERNAME = new AppParam(
			"alfresco.username", "caleearth");

	public static final AppParam ALFRESCO_PASSWORD = new AppParam(
			"alfresco.password", "pwd4ce@alfresco");

	public static final AppParam ALFRESCO_CALEEARTH_PATH = new AppParam(
			"alfresco.caleearth.path", "/CaleEarth/foto");

	public static final AppParam[] defaults = new AppParam[] {
			AppParam.APP_NAME, AppParam.APP_DESCRIPTION, AppParam.APP_SKIN,
			AppParam.APP_WELCOME, AppParam.APP_COMUNE, AppParam.APP_SSOAPPID,
			AppParam.APP_URL, AppParam.APP_SEGNALAZIONE_SCADENZA,
			AppParam.APP_MAIL_HOST, AppParam.APP_SEGNALAZIONE_REMINDER_START,
			AppParam.APP_MAIL_FROM, AppParam.APP_SEGNALAZIONE_REMINDER,
			AppParam.APP_IMG_FOOT, AppParam.APP_IMG_HEAD, AppParam.IQ_SECRET,
			AppParam.ISCRIZIONI_URL, AppParam.ISCRIZIONI_PUBLIC_URL,
			AppParam.APP_DAILY_STATI_DEFAULT, AppParam.APP_DAILY_STATI_HIDE,
			AppParam.APP_FILTRO_STATI_DEFAULT, AppParam.APP_ASSEGNAZIONE_POOL,
			AppParam.APP_RISP_UFFICIO, AppParam.RIPASSA_URP_CON_MAIL_TEL,
			AppParam.ASSEGNATARIO_IN_MAIL, AppParam.APP_RISP_SALUTI,
			AppParam.APP_RISP_SALUTI_PS, AppParam.RIPASSA_URP_SEMPRE,
			AppParam.ALFRESCO_URL, AppParam.ALFRESCO_USERNAME,
			AppParam.ALFRESCO_PASSWORD, AppParam.ALFRESCO_CALEEARTH_PATH };

	private String key;
	private String value;

	public AppParam() {

	}

	public AppParam(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	@Id
	@Column(name = "key_column")
	public String getKey() {
		return this.key;
	}

	public String getValue() {
		return this.value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
