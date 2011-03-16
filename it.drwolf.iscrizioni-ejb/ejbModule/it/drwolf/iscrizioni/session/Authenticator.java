package it.drwolf.iscrizioni.session;

import it.drwolf.iscrizioni.entity.AppParam;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

@Name("iscrizioniAuthenticator")
public class Authenticator {
	@Logger
	private Log log;

	@In
	private Identity identity;
	@In
	private Credentials credentials;
	@In
	private EntityManager entityManager;

	@In(value = "iscrizionisso")
	private Sso sso;

	public boolean authenticate() {
		if (sso.isLogged()) {
			return true;
		}
		if ("admin".equals(credentials.getUsername())) {
			final AppParam adminPassword = entityManager.find(AppParam.class,
					AppParam.LOCAL_ADMIN_PASSWORD);
			if (adminPassword.getValue().equals(credentials.getPassword())) {
				identity.addRole("admin");
				return true;
			}
		}

		return false;

	}

	public String getAppName() {
		return entityManager.find(AppParam.class, AppParam.APP_NAME).getValue();
	}

	public String getSkin() {
		return entityManager.find(AppParam.class, AppParam.APP_SKIN).getValue();
	}

}
