package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.Segnalazione;

import java.util.Arrays;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("segnalazioneList")
public class SegnalazioneList extends EntityQuery<Segnalazione> {

	private static final long serialVersionUID = -8205611425086794681L;

	@In(create = true)
	private EntityManager entityManager;

	private static final String[] RESTRICTIONS = { "lower(segnalazione.idutenteInseritore) like concat(lower(#{segnalazioneList.segnalazione.idutenteInseritore}),'%')",
			"lower(segnalazione.via) like concat(lower(#{segnalazioneList.segnalazione.via}),'%')",
			"lower(segnalazione.civico) like concat(lower(#{segnalazioneList.segnalazione.civico}),'%')",
			"lower(segnalazione.localita) like concat(lower(#{segnalazioneList.segnalazione.localita}),'%')",
			"lower(segnalazione.comune) like concat(lower(#{segnalazioneList.segnalazione.comune}),'%')", };

	private Segnalazione segnalazione = new Segnalazione();

	public SegnalazioneList() {
		this.setRestrictionExpressionStrings(Arrays.asList(SegnalazioneList.RESTRICTIONS));
	}

	@Override
	public String getEjbql() {
		return "select segnalazione from Segnalazione segnalazione";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

	public Segnalazione getSegnalazione() {
		return this.segnalazione;
	}

	public Segnalazione getSegnalazione(int id) {
		return this.entityManager.find(Segnalazione.class, id);
	}
}
