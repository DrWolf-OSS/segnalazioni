package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.Stato;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("statoList")
public class StatoList extends EntityQuery<Stato> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7420809215998492239L;

	@Override
	public String getEjbql() {
		return "from Stato";
	}

	@Override
	public Integer getMaxResults() {
		return 999999;
	}
}
