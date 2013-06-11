package it.drwolf.alerting.util;

import it.drwolf.alerting.entity.AppParam;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("utils")
@Scope(ScopeType.APPLICATION)
public class Utils {

	@In
	EntityManager entityManager;

	public String getParamValue(String key) {
		return this.entityManager.find(AppParam.class, key).getValue();
	}
}
