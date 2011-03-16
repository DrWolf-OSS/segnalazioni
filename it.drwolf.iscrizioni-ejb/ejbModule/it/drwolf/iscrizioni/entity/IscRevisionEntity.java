package it.drwolf.iscrizioni.entity;

import it.drwolf.iscrizioni.util.RevisionListener;

import javax.persistence.Entity;

import org.jboss.envers.DefaultRevisionEntity;
import org.jboss.envers.RevisionEntity;

@Entity
@RevisionEntity(RevisionListener.class)
public class IscRevisionEntity extends DefaultRevisionEntity {

	private String username;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
