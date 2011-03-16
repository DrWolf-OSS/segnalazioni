package it.drwolf.alerting.entity;

import it.drwolf.alerting.util.AlertingRevisionListener;

import javax.persistence.Entity;

import org.jboss.envers.DefaultRevisionEntity;
import org.jboss.envers.RevisionEntity;

@Entity
@RevisionEntity(AlertingRevisionListener.class)
public class AlertingRevisionEntity extends DefaultRevisionEntity {

	private String username;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
