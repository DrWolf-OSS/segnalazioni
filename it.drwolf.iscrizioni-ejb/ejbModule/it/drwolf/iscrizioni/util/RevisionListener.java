package it.drwolf.iscrizioni.util;

import it.drwolf.iscrizioni.entity.IscRevisionEntity;

import org.jboss.seam.security.Identity;

public class RevisionListener implements org.jboss.envers.RevisionListener {

	public void newRevision(Object entity) {
		IscRevisionEntity ire = (IscRevisionEntity) entity;
		ire.setUsername(Identity.instance().getCredentials().getUsername());
	}

}
