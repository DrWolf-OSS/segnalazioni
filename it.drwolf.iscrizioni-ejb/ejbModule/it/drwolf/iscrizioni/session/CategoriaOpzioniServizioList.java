package it.drwolf.iscrizioni.session;

import it.drwolf.iscrizioni.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("categoriaOpzioniServizioList")
public class CategoriaOpzioniServizioList extends
		EntityQuery<CategoriaOpzioniServizio> {

	private static final String EJBQL = "select categoriaOpzioniServizio from CategoriaOpzioniServizio categoriaOpzioniServizio";

	private static final String[] RESTRICTIONS = {
			"lower(categoriaOpzioniServizio.id) like concat(lower(#{categoriaOpzioniServizioList.categoriaOpzioniServizio.id}),'%')",
			"lower(categoriaOpzioniServizio.nome) like concat(lower(#{categoriaOpzioniServizioList.categoriaOpzioniServizio.nome}),'%')", };

	private CategoriaOpzioniServizio categoriaOpzioniServizio = new CategoriaOpzioniServizio();

	public CategoriaOpzioniServizioList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public CategoriaOpzioniServizio getCategoriaOpzioniServizio() {
		return categoriaOpzioniServizio;
	}
}
