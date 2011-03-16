package it.drwolf.alerting.session;

import it.drwolf.alerting.entity.AppParam;
import it.drwolf.alerting.entity.GlobalRole;
import it.drwolf.alerting.lists.GlobalRoleList;
import it.drwolf.alerting.util.Constants;
import it.drwolf.eloise.web.entity.People;

import java.net.URL;
import java.util.List;

import javax.persistence.EntityManager;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.bpm.Actor;
import org.jboss.seam.security.Identity;

@Name("sso")
@Scope(ScopeType.SESSION)
public class Sso {

	private boolean logged = false;

	private String originalUser;

	@In(create = true)
	private Actor actor;

	@In
	private GlobalRoleList globalRoleList;

	@In
	private WorkSession workSession;

	@In
	private Identity identity;

	@In
	private EntityManager entityManager;

	private String token;

	public String getOriginalUser() {
		return this.originalUser;
	}

	public String getSsoAppId() {
		return this.entityManager.find(AppParam.class, "app.ssoappid")
				.getValue();
	}

	public String getSsoBaseURL() {
		return this.entityManager.find(AppParam.class, "app.ssourl").getValue();
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

	public boolean isSmistatore(String username) {

		return this.entityManager
				.createQuery(
						"from TipoSegnalazione ts where :username in elements(ts.ufficioSmistatore.gestori)")
				.setParameter("username", username).getResultList().size() > 0;
	}

	@SuppressWarnings("unchecked")
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
				this.identity.getCredentials().setUsername(username.getTextTrim());
			}
			
		}
		if (go) {
			this.logged = true;
			if (!this.identity.isLoggedIn()) {
				this.setOriginalUser(this.identity.getCredentials()
						.getUsername());
				this.identity.login();
			}
			People people = this.entityManager.find(People.class, this.identity
					.getCredentials().getUsername());
			if (people != null) {
				this.actor.setId(this.identity.getCredentials().getUsername());
				this.identity.addRole(Constants.IMPIEGATO.toString());
				this.workSession.setUserFullname(people.getNome() + " "
						+ people.getCognome());
				if (this.isSmistatore(this.identity.getCredentials()
						.getUsername())) {
					this.identity.addRole(Constants.SMISTATORE.toString());
				}
				for (GlobalRole gr : this.globalRoleList.getResultList()) {
					if (gr.getUsers().contains(
							this.identity.getCredentials().getUsername())) {
						this.identity.addRole(gr.getName());
					}
				}

				List<Integer> ids = this.entityManager
						.createNativeQuery(
								"select t.TipoIntervento_id from TipoIntervento_gestoriIntervento t where t.element=:username")
						.setParameter("username",
								this.identity.getCredentials().getUsername())
						.getResultList();
				for (Integer i : ids) {
					this.actor.getGroupActorIds().add("gestore." + i);
					this.identity.addRole("gestore." + i);
				}
			}
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

	public void setOriginalUser(String originalUser) {
		this.originalUser = originalUser;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
