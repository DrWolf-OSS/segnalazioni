package it.drwolf.iscrizioni.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.jboss.envers.DefaultRevisionEntity;
import org.jboss.envers.RevisionEntity;

import it.drwolf.iscrizioni.util.RevisionListener;

@Entity
@Table(catalog = Catalog.name)
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
