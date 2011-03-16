package it.drwolf.alerting.util;

import it.drwolf.alerting.entity.AlertingRevisionEntity;

import org.jboss.envers.RevisionListener;
import org.jboss.seam.security.Identity;

public class AlertingRevisionListener implements RevisionListener {

	public void newRevision(Object revisionEntity) {
		AlertingRevisionEntity are = (AlertingRevisionEntity) revisionEntity;
		are.setUsername(Identity.instance().getCredentials().getUsername());
	}

}
