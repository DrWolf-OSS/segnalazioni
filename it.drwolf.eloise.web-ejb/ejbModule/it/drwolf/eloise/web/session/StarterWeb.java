package it.drwolf.eloise.web.session;



import it.drwolf.eloise.web.entity.AppParamWeb;

import it.drwolf.eloise.web.session.interfaces.IStarter;

import javax.ejb.Remove;
import javax.persistence.EntityManager;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;

//@Startup
//@Stateful
//@Scope(ScopeType.APPLICATION)
//@Name("starterWeb")
public class StarterWeb implements IStarter {

	@In
	EntityManager entityManager;



	private void checkParams() {

		for (AppParamWeb ap : AppParamWeb.defaults) {
			if (this.entityManager.find(AppParamWeb.class, ap.getId()) == null) {
				this.entityManager.persist(ap);
			}
		}
	}


	@Remove
	@Destroy
	public void destroy() {

	}

//	@Create
	public void startup() {

		this.checkParams();

	}
}
