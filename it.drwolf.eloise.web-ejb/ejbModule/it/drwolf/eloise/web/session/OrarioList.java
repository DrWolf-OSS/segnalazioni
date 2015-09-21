package it.drwolf.eloise.web.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.framework.EntityQuery;

import it.drwolf.eloise.web.entity.Orario;

@Name("orarioList")
public class OrarioList extends EntityQuery<Orario> {

	/**
	 *
	 */
	private static final long serialVersionUID = 8286431198559706110L;

	private static final String[] RESTRICTIONS = {
			"lower(orario.descrizione) like concat(lower(#{orarioList.orario.descrizione}),'%')", };

	private Orario orario = new Orario();

	@Override
	public String getEjbql() {
		return "select orario from Orario orario";
	}

	@Override
	public Integer getMaxResults() {
		return 250;
	}

	public Orario getOrario() {
		return this.orario;
	}

	@Override
	public List<String> getRestrictionExpressionStrings() {
		return Arrays.asList(OrarioList.RESTRICTIONS);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ValueExpression> getRestrictions() {
		List<String> expressionStrings = Arrays.asList(OrarioList.RESTRICTIONS);

		Expressions expressions = new Expressions();
		List<ValueExpression> restrictionVEs = new ArrayList<ValueExpression>(expressionStrings.size());
		for (String expressionString : expressionStrings) {
			restrictionVEs.add(expressions.createValueExpression(expressionString));
		}
		// setRestrictions(restrictionVEs);
		return restrictionVEs;
	}

}
