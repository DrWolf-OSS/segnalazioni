package it.drwolf.sso.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Info {

	private SSOToken ssoToken;

	private Integer id;

	private String key;
	private String value;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return this.id;
	}

	@Column(name = "key_col")
	public String getKey() {
		return this.key;
	}

	@ManyToOne
	public SSOToken getSsoToken() {
		return this.ssoToken;
	}

	public String getValue() {
		return this.value;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setSsoToken(SSOToken ssoToken) {
		this.ssoToken = ssoToken;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
