package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.SottotipoSegnalazione;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("sottotipoSegnalazioneList")
public class SottotipoSegnalazioneList extends
		EntityQuery<SottotipoSegnalazione> {

	private static final long serialVersionUID = 7745649861261718669L;

	private static final String[] RESTRICTIONS = { "lower(sottotipoSegnalazione.descrizione) like concat(lower(#{sottotipoSegnalazioneList.sottotipoSegnalazione.descrizione}),'%')", };

	private SottotipoSegnalazione sottotipoSegnalazione = new SottotipoSegnalazione();

	public SottotipoSegnalazioneList() {
		this.setRestrictionExpressionStrings(Arrays
				.asList(SottotipoSegnalazioneList.RESTRICTIONS));
	}

	@Override
	public String getEjbql() {
		return "select sottotipoSegnalazione from SottotipoSegnalazione sottotipoSegnalazione";
	}

	@Override
	public Integer getMaxResults() {
		return 999999;
	}

	public SottotipoSegnalazione getSottotipoSegnalazione() {
		return this.sottotipoSegnalazione;
	}

}
