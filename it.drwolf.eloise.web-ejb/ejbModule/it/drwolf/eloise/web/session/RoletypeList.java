package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.framework.EntityQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

@Name("roletypeList")
public class RoletypeList extends EntityQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5596399614785307392L;

	private static final String[] RESTRICTIONS = { "lower(roletype.role) like concat(lower(#{roletypeList.roletype.role}),'%')", };

	private Roletype roletype = new Roletype();

	@Override
	public String getEjbql() {
		return "select roletype from Roletype roletype";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

	public Roletype getRoletype() {
		return roletype;
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
