package it.drwolf.sso.session;

import it.drwolf.sso.api.SSOModule;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.validator.NotEmpty;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.security.Identity;

@Name("passwordChanger")
// @Scope(ScopeType.CONVERSATION)
public class PwdChanger {

	private String user;

	private String oldpwd;

	private String newpwd;

	@In
	private Identity identity;

	@In
	private EntityManager entityManager;

	public void change() {

		SSOModule refModule = null;
		List<SSOModule> modules = this.getSsoModules();
		if (this.user != null) {
			for (SSOModule module : modules) {
				List<String> listUsers = module.listUsers();
				if (listUsers.contains(this.user)) {
					refModule = module;
					break;
				}
			}
		}

		if (refModule == null) {
			FacesMessages.instance().add("Nome utente errato.");
			return;
		}

		if (refModule.changePassword(this.user, this.oldpwd, this.newpwd)) {
			FacesMessages.instance().add("La password è stata cambiata.");
		} else {
			FacesMessages.instance().add("La password è errata.");
		}
	}

	@NotEmpty
	public String getNewpwd() {
		return this.newpwd;
	}

	@NotEmpty
	public String getOldpwd() {
		return this.oldpwd;
	}

	@SuppressWarnings("unchecked")
	public List<SSOModule> getSsoModules() {
		List<SSOModule> list = new ArrayList<SSOModule>();
		for (String name : (List<String>) this.entityManager
				.createQuery(
						"select name from Module where listUsers = true order by position")
				.getResultList()) {
			TokenManager.addModule(list, name);
		}
		return list;
	}

	@NotEmpty
	public String getUser() {

		if (this.identity.isLoggedIn()) {
			this.user = this.identity.getUsername();
		}
		return this.user;
	}

	public void setNewpwd(String newpwd) {
		this.newpwd = newpwd;
	}

	public void setOldpwd(String oldpwd) {
		this.oldpwd = oldpwd;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
