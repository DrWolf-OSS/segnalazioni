package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.CanaleSegnalazione;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("canaleSegnalazioneList")
public class CanaleSegnalazioneList extends EntityQuery<CanaleSegnalazione> {

	private static final long serialVersionUID = 7897424625677184372L;

	private static final String[] RESTRICTIONS = { "lower(canaleSegnalazione.descrizioneCanale) like concat(lower(#{canaleSegnalazioneList.canaleSegnalazione.descrizioneCanale}),'%')", };

	private CanaleSegnalazione canaleSegnalazione = new CanaleSegnalazione();

	public CanaleSegnalazioneList() {
		this.setRestrictionExpressionStrings(Arrays
				.asList(CanaleSegnalazioneList.RESTRICTIONS));
	}

	public CanaleSegnalazione getCanaleSegnalazione() {
		return this.canaleSegnalazione;
	}

	@Override
	public String getEjbql() {
		return "select canaleSegnalazione from CanaleSegnalazione canaleSegnalazione";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

}
