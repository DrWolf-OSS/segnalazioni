package it.drwolf.iscrizioni.session;

import it.drwolf.iscrizioni.entity.AppParam;

import java.net.URL;

import javax.persistence.EntityManager;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Identity;

@Name("iscrizionisso")
@Scope(ScopeType.SESSION)
@AutoCreate
public class Sso {

	private boolean logged = false;

	@In
	private Identity identity;

	@In
	private EntityManager entityManager;

	private String token;

	public String getSsoAppId() {
		return this.entityManager.find(AppParam.class, "sso.appid").getValue();
	}

	public String getSsoBaseURL() {
		return this.entityManager.find(AppParam.class, "sso.url").getValue();
	}

	public String getSsoLoginURL() {
		return this.getSsoBaseURL() + "/login.seam?s=" + this.getSsoAppId();
	}

	public String getToken() {
		return this.token;
	}

	public boolean isLogged() {
		return this.logged;
	}

	public boolean login() throws Exception {
		boolean go = true;
		if (!this.identity.isLoggedIn()) {
			go = false;
			String ssobase = this.getSsoBaseURL();
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new URL(ssobase + "/check.seam?token="
					+ this.token));
			Element username = doc.getRootElement().element("username");
			if (username != null) {
				go = true;
			}
			this.identity.getCredentials().setPassword(this.token);
			this.identity.getCredentials().setUsername(username.getTextTrim());
		}
		if (go) {
			this.logged = true;
			this.identity.login();
			return true;
		}
		this.logged = false;
		this.token = null;
		return false;
	}

	public void logout() {
		this.token = null;
		this.logged = false;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
