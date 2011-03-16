package it.drwolf.iscrizioni.session;

import it.drwolf.iscrizioni.entity.AppParam;
import it.drwolf.iscrizioni.entity.Iscritto;
import it.drwolf.iscrizioni.entity.Servizio;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("iscrittoList")
public class IscrittoList extends EntityQuery<Iscritto> {

	private static final long serialVersionUID = -4035549032065235006L;

	private static final String EJBQL = "select iscritto from Iscritto iscritto";

	private static final String[] RESTRICTIONS = {
			"lower(iscritto.cognome) like concat(lower(#{iscrittoList.iscritto.cognome}),'%')",
			"lower(iscritto.email) like concat(lower(#{iscrittoList.iscritto.email}),'%')",
			"lower(iscritto.cellulare) like concat(lower(#{iscrittoList.iscritto.cellulare}),'%')",
			"#{iscrittoList.servizio} in elements(iscritto.servizi)",
			"lower(iscritto.nome) like concat(lower(#{iscrittoList.iscritto.nome}),'%')", };

	private Iscritto iscritto = new Iscritto();

	private Servizio servizio;

	public IscrittoList() {
		this.setEjbql(IscrittoList.EJBQL);
		this.setRestrictionExpressionStrings(Arrays
				.asList(IscrittoList.RESTRICTIONS));
		this.setMaxResults(10);

	}

	@SuppressWarnings("unchecked")
	public List<Iscritto> getAllResults() {
		return this.getEntityManager()
				.createQuery("from Iscritto order by email").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Factory("eliminatiList")
	public List<Object[]> getEliminatiList() {

		return this
				.getEntityManager()
				.createNativeQuery(
						"SELECT iv.id, iv.cognome, iv.nome, iv.email, iv.cellulare, iv._revision FROM Iscritto_versions iv where iv.id in (SELECT I.id FROM Iscritto_versions I where I._revision_type=2) and iv._revision=(select max(iv2._revision) from Iscritto_versions iv2 where iv2._revision_type<2 and iv2.id=iv.id)")
				.getResultList();

	}

	public String getIdServizio() {
		return this.servizio != null ? this.servizio.getId() : null;
	}

	@SuppressWarnings("unchecked")
	@Factory("incompletiList")
	public List<Object[]> getIncompletiList() {
		String q = this.getEntityManager()
				.find(AppParam.class, AppParam.APP_INCOMPLETI_QUERY).getValue();
		return this.getEntityManager().createNativeQuery(q).getResultList();

	}

	public Iscritto getIscritto() {
		return this.iscritto;
	}

	public Servizio getServizio() {
		return this.servizio;
	}

	public void setIdServizio(String idServizio) {
		if (idServizio != null) {
			this.setServizio(this.getEntityManager().find(Servizio.class,
					idServizio));
		}
	}

	public void setServizio(Servizio servizio) {
		this.servizio = servizio;
	}

}
