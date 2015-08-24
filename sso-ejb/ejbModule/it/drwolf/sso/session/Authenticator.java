package it.drwolf.sso.session;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.security.Identity;

import it.drwolf.sso.api.SSOModule;
import it.drwolf.sso.entity.AdminUser;
import it.drwolf.sso.entity.AppParam;
import it.drwolf.sso.entity.SSOToken;
import it.drwolf.sso.entity.Service;
import it.drwolf.sso.session.interfaces.ITokenManager;

@Name("authenticator")
@Scope(ScopeType.SESSION)
public class Authenticator {

	@In
	private Identity identity;

	@In
	private ITokenManager tokenManager;

	@In
	private EntityManager entityManager;

	private String service;

	private String uuid;

	public boolean authenticate() {
		HashMap<String, String> info = new HashMap<String, String>();
		String username = this.identity.getPrincipal() != null ? this.identity.getPrincipal().toString()
				: this.identity.getCredentials().getUsername();

		if (!this.identity.isLoggedIn()) {
			this.uuid = UUID.randomUUID().toString();
			if (username == null) {
				HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
						.getRequest();
				boolean ac = false;
				HttpState initialState = new HttpState();
				for (javax.servlet.http.Cookie sc : req.getCookies()) {
					if (sc.getName() == "__ac") {
						ac = true;
						initialState.addCookie(
								new Cookie(sc.getDomain(), sc.getName(), sc.getValue(), sc.getPath(), null, false));
					}
				}
				if (ac) {
					HttpClient hc = new HttpClient();
					AppParam plone_url = this.entityManager.find(AppParam.class, "PLONE_URL");
					if (plone_url != null) {

						HttpMethod gm = new GetMethod(plone_url.getValue());

						hc.setState(initialState);
						try {
							hc.executeMethod(gm);

							if (gm.getStatusCode() == 200) {
								info.put("token", this.uuid);
								username = gm.getResponseBodyAsString();
								info.put("username", username);
								this.saveToken(info);
								this.redirect(username);
								return true;
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							gm.releaseConnection();
						}
					}

				}
			}
			if ("admin".equals(this.service)) {
				AdminUser admin = this.entityManager.find(AdminUser.class, username);
				if (admin != null && admin.getPassword().equals(this.identity.getCredentials().getPassword())) {

					info.put("username", username);
					this.identity.addRole("admin");
					this.saveToken(info);
					return true;
				}
			}

			for (SSOModule module : this.tokenManager.getSsoModules()) {
				info = module.login(this.identity.getCredentials().getUsername(),
						this.identity.getCredentials().getPassword());
				if (info != null) {
					break;
				}
			}

			if (info == null) {

			}

			if (info != null) {

				info.put("token", this.uuid);
				this.saveToken(info);
				this.redirect(username);

				return true;
			}
			return false;
		} else {
			this.redirect(username);
			return true;
		}

	}

	public void expire() {
		SSOToken token = this.entityManager.find(SSOToken.class, this.uuid);
		if (token != null) {
			this.entityManager.remove(token);
		}
	}

	public String getService() {
		return this.service;
	}

	public String getUsername() {
		HashMap<String, String> info = this.tokenManager.getTokens().get(this.uuid);
		if (info == null) {
			return null;
		}
		return info.get("username");
	}

	public String getUuid() {
		return this.uuid;
	}

	public boolean isAdmin() {
		if (this.identity.getPrincipal() == null) {
			return false;
		}
		return this.identity.isLoggedIn()
				&& this.entityManager.find(AdminUser.class, this.identity.getPrincipal().toString()) != null;
	}

	private void redirect(String username) {
		Service s = this.service == null ? null : this.entityManager.find(Service.class, this.service);

		if (this.service != null && s != null && s.canAccess(username)) {
			try {
				this.service = null;
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(s.getLoginUrl() + "?token=" + this.uuid);

			} catch (IOException e) {
				FacesMessages.instance().add(e.getMessage());
			}

		}
	}

	@SuppressWarnings("unchecked")
	private void saveToken(HashMap<String, String> info) {
		SSOToken ssoToken = new SSOToken();
		ssoToken.setUuid(this.uuid);
		ssoToken.setInfo(info);

		List<SSOToken> uuids = this.entityManager
				.createQuery("select info.ssoToken from Info info where info.key=:uc and info.value=:username")
				.setParameter("username", info.get("username")).setParameter("uc", "username").getResultList();
		for (SSOToken ssoToken2 : uuids) {
			this.entityManager.remove(ssoToken2);
		}
		this.entityManager.persist(ssoToken);
	}

	public void setService(String service) {
		this.service = service;
		if (this.identity.isLoggedIn() && service != null) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect(
						this.entityManager.find(Service.class, this.service).getLoginUrl() + "?token=" + this.uuid);
			} catch (Exception e) {

			}
		}
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
