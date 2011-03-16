package it.drwolf.eloise.web.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(catalog = "eloise")
public class AdminUser {
	private String userid;
	private String password;

	public String getPassword() {
		return this.password;
	}

	@Id
	public String getUserid() {
		return this.userid;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
}
