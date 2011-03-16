package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.UfficioCompetente;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("ufficioCompetenteList")
public class UfficioCompetenteList extends EntityQuery<UfficioCompetente> {

	private static final long serialVersionUID = -7814979114808312868L;

	@Override
	public String getEjbql() {
		return "from UfficioCompetente";
	}

	@Override
	public Integer getMaxResults() {
		return 99999;
	}

	@Override
	public String getOrder() {
		return "id";
	}

}
