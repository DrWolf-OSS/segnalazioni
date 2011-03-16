package it.drwolf.iscrizioni.session;

import it.drwolf.iscrizioni.entity.Gruppo;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("gruppoList")
public class GruppoList extends EntityQuery<Gruppo> {

	private static final String EJBQL = "select gruppo from Gruppo gruppo";

	private static final String[] RESTRICTIONS = {
			"lower(gruppo.id) like concat(lower(#{gruppoList.gruppo.id}),'%')",
			"lower(gruppo.nome) like concat(lower(#{gruppoList.gruppo.nome}),'%')", };

	private Gruppo gruppo = new Gruppo();

	public GruppoList() {
		this.setEjbql(GruppoList.EJBQL);
		this.setRestrictionExpressionStrings(Arrays
				.asList(GruppoList.RESTRICTIONS));
		this.setOrder("nome");
		this.setMaxResults(99999);
	}

	public Gruppo getGruppo() {
		return this.gruppo;
	}
}
