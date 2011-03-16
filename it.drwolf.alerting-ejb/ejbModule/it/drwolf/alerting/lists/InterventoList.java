package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.Intervento;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("interventoList")
public class InterventoList extends EntityQuery<Intervento> {

	private static final long serialVersionUID = 298486683394152487L;

	private static final String[] RESTRICTIONS = {
			"lower(intervento.oggetto) like concat(lower(#{interventoList.intervento.oggetto}),'%')",
			"lower(intervento.descrizione) like concat(lower(#{interventoList.intervento.descrizione}),'%')",
			"lower(intervento.nomeSegnalatore) like concat(lower(#{interventoList.intervento.nomeSegnalatore}),'%')",
			"lower(intervento.cognomeSegnalatore) like concat(lower(#{interventoList.intervento.cognomeSegnalatore}),'%')",
			"lower(intervento.telSegnalatore) like concat(lower(#{interventoList.intervento.telSegnalatore}),'%')",
			"lower(intervento.emailSegnalatore) like concat(lower(#{interventoList.intervento.emailSegnalatore}),'%')",
			"lower(intervento.noteSegnalatore) like concat(lower(#{interventoList.intervento.noteSegnalatore}),'%')",
			"lower(intervento.via) like concat(lower(#{interventoList.intervento.via}),'%')",
			"lower(intervento.civico) like concat(lower(#{interventoList.intervento.civico}),'%')",
			"lower(intervento.localita) like concat(lower(#{interventoList.intervento.localita}),'%')",
			"lower(intervento.comune) like concat(lower(#{interventoList.intervento.comune}),'%')", };

	private Intervento intervento = new Intervento();

	public InterventoList() {
		this.setRestrictionExpressionStrings(Arrays
				.asList(InterventoList.RESTRICTIONS));
	}

	@Override
	public String getEjbql() {
		return "select intervento from Intervento intervento";
	}

	public Intervento getIntervento() {
		return this.intervento;
	}

	@Override
	public Integer getMaxResults() {
		return 99999;
	}

}
