package it.drwolf.iscrizioni.session;

import it.drwolf.iscrizioni.entity.AppParam;

import javax.ejb.Remove;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("iscrizioniStarter")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class Starter {

	@In
	private EntityManager entityManager;

	@Remove
	@Destroy
	public void destroy() {

	}

	@Create
	public void start() {
		for (AppParam appParam : AppParam.defaults) {
			AppParam fp = this.entityManager.find(AppParam.class, appParam
					.getId());
			if (fp == null) {
				this.entityManager.persist(appParam);
			}
		}
	}
}
