package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.SottotipoIntervento;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("sottotipoInterventoList")
public class SottotipoInterventoList extends EntityQuery<SottotipoIntervento> {

	private static final long serialVersionUID = -8450825899888775487L;

	private static final String[] RESTRICTIONS = { "lower(sottotipoIntervento.descrizione) like concat(lower(#{sottotipoInterventoList.sottotipoIntervento.descrizione}),'%')", };

	private SottotipoIntervento sottotipoIntervento = new SottotipoIntervento();

	public SottotipoInterventoList() {
		this.setRestrictionExpressionStrings(Arrays
				.asList(SottotipoInterventoList.RESTRICTIONS));
	}

	@Override
	public String getEjbql() {
		return "select sottotipoIntervento from SottotipoIntervento sottotipoIntervento";
	}

	@Override
	public Integer getMaxResults() {
		return 999999;
	}

	public SottotipoIntervento getSottotipoIntervento() {
		return this.sottotipoIntervento;
	}

}
