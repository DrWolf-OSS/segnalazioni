package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.framework.EntityQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

@Name("orarioList")
public class OrarioList extends EntityQuery {

	private static final String[] RESTRICTIONS = { "lower(orario.descrizione) like concat(lower(#{orarioList.orario.descrizione}),'%')", };

	private Orario orario = new Orario();

	@Override
	public String getEjbql() {
		return "select orario from Orario orario";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

	public Orario getOrario() {
		return orario;
	}

	@Override
	public List<ValueExpression> getRestrictions()
	{
		List<String> expressionStrings = Arrays.asList(RESTRICTIONS);
		
		Expressions expressions = new Expressions();
	      List<ValueExpression> restrictionVEs = new ArrayList<ValueExpression>(expressionStrings.size());
	      for (String expressionString : expressionStrings)
	      {
	         restrictionVEs.add(expressions.createValueExpression(expressionString));
	      }
	      //setRestrictions(restrictionVEs);		
	   return restrictionVEs;
	}

	
	@Override
	public List<String> getRestrictionExpressionStrings() {
		return Arrays.asList(RESTRICTIONS);
	}

}
