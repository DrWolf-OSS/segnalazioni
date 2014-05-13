package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.SquadraIntervento;
import it.drwolf.alerting.util.EloiseUtils;

import java.util.Iterator;
import java.util.Map;

import org.jboss.seam.Component;
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

	public void pulisciGestori() {
		EloiseUtils eu = (EloiseUtils) Component.getInstance("eloiseUtils");
		Map<String, String> people = eu.getPeople();
		for (SquadraIntervento squadraIntervento : this.getResultList()) {
			Iterator<String> i = squadraIntervento.getComponenti().iterator();
			while (i.hasNext()) {
				if (!people.values().contains(i.next())) {
					i.remove();
				}
			}
		}

	}

}
