package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.UfficioCompetente;
import it.drwolf.alerting.util.EloiseUtils;

import java.util.Iterator;
import java.util.Map;

import org.jboss.seam.Component;
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

	public void pulisciGestori() {
		EloiseUtils eu = (EloiseUtils) Component.getInstance("eloiseUtils");
		Map<String, String> people = eu.getPeople();
		for (UfficioCompetente ufficioCompetente : this.getResultList()) {
			Iterator<String> i = ufficioCompetente.getGestori().iterator();
			while (i.hasNext()) {
				if (!people.values().contains(i.next())) {
					i.remove();
				}
			}
		}

	}
}
