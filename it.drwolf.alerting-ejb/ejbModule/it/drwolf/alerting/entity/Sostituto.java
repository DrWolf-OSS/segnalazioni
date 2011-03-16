package it.drwolf.alerting.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Sostituto {
	private Integer id;
	private String username;
	private String sostituto;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return this.id;
	}

	public String getSostituto() {
		return this.sostituto;
	}

	public String getUsername() {
		return this.username;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSostituto(String sostituto) {
		this.sostituto = sostituto;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
