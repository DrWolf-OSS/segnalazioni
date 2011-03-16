package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.EsitoSegnalazione;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("esitoSegnalazioneList")
public class EsitoSegnalazioneList extends EntityQuery<EsitoSegnalazione> {

	private static final long serialVersionUID = 7897424625677184372L;

	private static final String[] RESTRICTIONS = {};

	private EsitoSegnalazione esitoSegnalazione = new EsitoSegnalazione();

	public EsitoSegnalazioneList() {
		this.setRestrictionExpressionStrings(Arrays
				.asList(EsitoSegnalazioneList.RESTRICTIONS));
	}

	@Override
	public String getEjbql() {
		return "select esitoSegnalazione from EsitoSegnalazione esitoSegnalazione";
	}

	public EsitoSegnalazione getEsitoSegnalazione() {
		return this.esitoSegnalazione;
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

}
