package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.Ente;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.framework.EntityQuery;

@Name("enteList")
public class EnteList extends EntityQuery<Ente> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2651647915920984332L;

	@In(create = true)
	EntityManager entityManager;

	private static final String[] RESTRICTIONS = { "lower(ente.nome) like concat(lower(#{enteList.ente.nome}),'%')", };

	private Ente ente = new Ente();

	@SuppressWarnings("unchecked")
	public List<Ente> getAllEntes() {
		List<Ente> enti = new ArrayList<Ente>();
		enti = this.entityManager.createQuery(
				"SELECT e from Ente e  order by Nome asc").getResultList();
		return enti;
	}

	@Override
	public String getEjbql() {
		return "select ente from Ente ente";
	}

	public Ente getEnte() {
		return this.ente;
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

	@Override
	public List<String> getRestrictionExpressionStrings() {
		return Arrays.asList(EnteList.RESTRICTIONS);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ValueExpression> getRestrictions() {
		List<String> expressionStrings = Arrays.asList(EnteList.RESTRICTIONS);

		Expressions expressions = new Expressions();
		List<ValueExpression> restrictionVEs = new ArrayList<ValueExpression>(
				expressionStrings.size());
		for (String expressionString : expressionStrings) {
			restrictionVEs.add(expressions
					.createValueExpression(expressionString));
		}
		// setRestrictions(restrictionVEs);
		return restrictionVEs;
	}
}
