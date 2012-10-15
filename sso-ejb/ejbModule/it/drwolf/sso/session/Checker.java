package it.drwolf.sso.session;

import it.drwolf.sso.api.SSOModule;
import it.drwolf.sso.entity.Service;
import it.drwolf.sso.session.interfaces.ITokenManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("checker")
public class Checker {

	@In
	private ITokenManager tokenManager;

	private String token;

	private String secret;

	private String service;

	@In
	private EntityManager entityManager;

	private HashMap<String, String> userInfo = null;

	public String getSecret() {
		return this.secret;
	}

	public String getService() {
		return this.service;
	}

	private Service getServiceObject() {
		if (this.service == null) {
			return null;
		}
		return this.entityManager.find(Service.class, this.service);

	}

	public String getToken() {
		return this.token;
	}

	public String getUserInfo() {
		if (this.userInfo == null) {
			return "<error>No such token</error>";
		}

		if ((this.getServiceObject() != null)
				&& !this.getServiceObject().canAccess(
						this.userInfo.get("username"))) {
			return "<error>User not allowed to acces this service</error>";
		}
		String s = "<userinfo>";
		for (Entry<String, String> e : this.userInfo.entrySet()) {
			s += "<" + e.getKey() + ">";
			s += "<![CDATA[" + e.getValue() + "]]>";
			s += "</" + e.getKey() + ">";
		}
		s += "</userinfo>";

		return s;

	}

	public String getUserList() {
		if (!this.tokenManager.getSecret().equals(this.secret)) {
			return "<error>unauthorized</error>";
		}
		String s = "<userlist>";
		Service so = this.getServiceObject();
		List<SSOModule> ssoModules = this.tokenManager.getSsoModules();
		for (SSOModule module : ssoModules) {
			for (String user : module.listUsers()) {
				if ((so == null) || so.canAccess(user)) {
					s += "<username><![CDATA[" + user + "]]></username>";
				}
			}
		}
		s += "</userlist>";
		return s;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void setToken(String token) {
		this.token = token;

		this.userInfo = this.tokenManager.getTokens().get(token);
	}

}
