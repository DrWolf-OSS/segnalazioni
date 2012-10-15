package it.drwolf.sso.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.jboss.seam.annotations.security.TokenUsername;
import org.jboss.seam.annotations.security.TokenValue;

@Entity
public class AuthenticationToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6366382348561892185L;

	private Integer tokenId;

	private String username;

	private String value;

	@Id
	@GeneratedValue
	public Integer getTokenId() {

		return this.tokenId;

	}

	@TokenUsername
	public String getUsername() {

		return this.username;

	}

	@TokenValue
	public String getValue() {

		return this.value;

	}

	public void setTokenId(Integer tokenId) {

		this.tokenId = tokenId;

	}

	public void setUsername(String username) {

		this.username = username;

	}

	public void setValue(String value) {

		this.value = value;

	}

}