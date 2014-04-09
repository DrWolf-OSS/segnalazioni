package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.TipoIntervento;
import it.drwolf.alerting.util.EloiseUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("tipoInterventoList")
public class TipoInterventoList extends EntityQuery<TipoIntervento> {

	private static final long serialVersionUID = -8260372104942617936L;

	private static final String[] RESTRICTIONS = { "lower(tipoIntervento.descrizione) like concat(lower(#{tipoInterventoList.tipoIntervento.descrizione}),'%')", };

	private TipoIntervento tipoIntervento = new TipoIntervento();

	public TipoInterventoList() {
		this.setRestrictionExpressionStrings(Arrays.asList(TipoInterventoList.RESTRICTIONS));
	}

	@Override
	public String getEjbql() {
		return "select tipoIntervento from TipoIntervento tipoIntervento";
	}

	@Override
	public Integer getMaxResults() {
		return 999999;
	}

	public TipoIntervento getTipoIntervento() {
		return this.tipoIntervento;
	}

	public void pulisciGestori() {
		EloiseUtils eu = (EloiseUtils) Component.getInstance("eloiseUtils");
		Map<String, String> people = eu.getPeople();
		for (TipoIntervento tipoIntervento : this.getResultList()) {
			Iterator<String> i = tipoIntervento.getGestoriIntervento().iterator();
			while (i.hasNext()) {
				if (!people.values().contains(i.next())) {
					i.remove();
				}
			}
		}

	}

}
