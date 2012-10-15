package it.drwolf.sso.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AdminUser {
	private String username;
	private String password;

	public String getPassword() {
		return this.password;
	}

	@Id
	public String getUsername() {
		return this.username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
