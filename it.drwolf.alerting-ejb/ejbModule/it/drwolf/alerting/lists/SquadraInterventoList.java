package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.SquadraIntervento;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("squadraInterventoList")
public class SquadraInterventoList extends EntityQuery<SquadraIntervento> {

	private static final long serialVersionUID = -7814979321808312868L;

	@Override
	public String getEjbql() {
		return "from SquadraIntervento";
	}

	@Override
	public Integer getMaxResults() {
		return 99999;
	}

	@Override
	public String getOrder() {
		return "nome";
	}

}
