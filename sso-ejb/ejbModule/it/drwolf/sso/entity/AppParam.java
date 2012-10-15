package it.drwolf.sso.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AppParam {
	private String id;
	private String value;

	@Id
	public String getId() {
		return this.id;
	}

	public String getValue() {
		return this.value;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
