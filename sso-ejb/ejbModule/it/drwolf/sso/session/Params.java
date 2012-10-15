package it.drwolf.sso.session;

import it.drwolf.sso.entity.AppParam;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("params")
public class Params {
	@In
	private EntityManager entityManager;

	public String get(String param, String defaultParam) {
		if (param == null) {
			return defaultParam;
		}
		AppParam p = this.entityManager.find(AppParam.class, param);
		return p != null ? p.getValue() : defaultParam;
	}
}
