package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.framework.EntityQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

@Name("sedeList")
public class SedeList extends EntityQuery {

	private static final String[] RESTRICTIONS = { "lower(sede.descrizione) like concat(lower(#{sedeList.sede.descrizione}),'%')", };

	private Sede sede = new Sede();

	@Override
	public String getEjbql() {
		return "select sede from Sede sede";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
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

	public Sede getSede() {
		return sede;
	}

	@Override
	public List<String> getRestrictionExpressionStrings() {
		return Arrays.asList(RESTRICTIONS);
	}

}
