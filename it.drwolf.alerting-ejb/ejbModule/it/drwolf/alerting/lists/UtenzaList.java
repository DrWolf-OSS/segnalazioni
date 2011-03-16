package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.Utenza;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("utenzaList")
public class UtenzaList extends EntityQuery<Utenza> {

	private static final long serialVersionUID = 2051651092012844750L;

	@Override
	public String getEjbql() {
		return "from Utenza";
	}

	@Override
	public Integer getMaxResults() {
		return 99999;
	}

	@Override
	public String getOrder() {
		return "descrizione";
	}

}
