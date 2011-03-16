package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.TipoSegnalazione;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("tipoSegnalazioneList")
public class TipoSegnalazioneList extends EntityQuery<TipoSegnalazione> {

	private static final long serialVersionUID = -7224603587454420066L;

	private static final String[] RESTRICTIONS = { "lower(tipoSegnalazione.descrizione) like concat(lower(#{tipoSegnalazioneList.tipoSegnalazione.descrizione}),'%')", };

	private TipoSegnalazione tipoSegnalazione = new TipoSegnalazione();

	public TipoSegnalazioneList() {
		this.setRestrictionExpressionStrings(Arrays
				.asList(TipoSegnalazioneList.RESTRICTIONS));
	}

	@Override
	public String getEjbql() {
		return "select tipoSegnalazione from TipoSegnalazione tipoSegnalazione";
	}

	@Override
	public Integer getMaxResults() {
		return 999999;
	}

	public TipoSegnalazione getTipoSegnalazione() {
		return this.tipoSegnalazione;
	}

}
