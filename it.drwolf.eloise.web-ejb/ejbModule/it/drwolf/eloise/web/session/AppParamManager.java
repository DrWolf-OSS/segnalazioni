package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.AppParamWeb;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Scope(ScopeType.CONVERSATION)
@Name("appParamManager")
public class AppParamManager {

	@In(create = true)
	EntityManager entityManager;

	public String getValueOfId(String id) {
		String value = this.entityManager.find(AppParamWeb.class, id)
				.getValue();

		if (value == null) {
			for (AppParamWeb ap : AppParamWeb.defaults) {
				if (ap.getId().equals(id)) {
					value = ap.getValue();
				}
			}
		}
		return value;
	}

}
