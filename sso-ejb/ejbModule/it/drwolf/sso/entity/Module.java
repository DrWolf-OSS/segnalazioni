package it.drwolf.sso.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Module {
	private Integer id;
	private String name;
	private Integer position;
	private boolean changePassword = false;
	private boolean listUsers = false;

	@Id
	@GeneratedValue()
	public Integer getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public Integer getPosition() {
		return this.position;
	}

	public boolean isChangePassword() {
		return this.changePassword;
	}

	public boolean isListUsers() {
		return this.listUsers;
	}

	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setListUsers(boolean listUsers) {
		this.listUsers = listUsers;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
}
