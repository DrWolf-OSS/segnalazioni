package it.drwolf.sso.entity;

import it.drwolf.sso.validators.ValidURL;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;

@Entity
public class Service {

	private String id;
	private String name;
	private String loginUrl;

	private String logoutUrl;

	private List<String> usernames = new ArrayList<String>();

	@Transient
	public boolean canAccess(String username) {
		if (this.getUsernames().size() == 0) {
			return true;
		} else {
			return this.getUsernames().contains(username);
		}
	}

	@Id
	public String getId() {
		return this.id;
	}

	@ValidURL
	public String getLoginUrl() {
		return this.loginUrl;
	}

	@ValidURL
	public String getLogoutUrl() {
		return this.logoutUrl;
	}

	public String getName() {
		return this.name;
	}

	@CollectionOfElements
	public List<String> getUsernames() {
		return this.usernames;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUsernames(List<String> usernames) {
		this.usernames = usernames;
	}
}
