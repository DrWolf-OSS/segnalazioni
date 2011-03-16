package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.CodiceTriage;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("codiceTriageList")
public class CodiceTriageList extends EntityQuery<CodiceTriage> {

	private static final long serialVersionUID = -4494928260431085942L;

	@Override
	public String getEjbql() {
		return "from CodiceTriage";
	}

	@Override
	public String getOrder() {
		return "tempoIntervento desc";
	}

}
