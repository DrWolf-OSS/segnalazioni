package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.*;


import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.framework.EntityQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import javax.persistence.EntityManager;

@Name("ufficioList")
public class UfficioList extends EntityQuery {

	@In(create = true)
	EntityManager entityManager;
	
	
	private static final String[] RESTRICTIONS = {
			"lower(ufficio.descrizione) like concat(lower(#{ufficioList.ufficio.descrizione}),'%')",
			"lower(ufficio.telefono) like concat(lower(#{ufficioList.ufficio.telefono}),'%')",
			"lower(ufficio.fax) like concat(lower(#{ufficioList.ufficio.fax}),'%')",
			"lower(ufficio.email) like concat(lower(#{ufficioList.ufficio.email}),'%')",
			"lower(ufficio.nome) like concat(lower(#{ufficioList.ufficio.nome}),'%')",
			"lower(ufficio.fax1) like concat(lower(#{ufficioList.ufficio.fax1}),'%')", };

	private Ufficio ufficio = new Ufficio();

	@Override
	public String getEjbql() {
		return "select ufficio from Ufficio ufficio";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

	public Ufficio getUfficio() {
		return ufficio;
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

	public List<Ufficio> getAllUfficios(){
		List<Ufficio> uffici = new ArrayList<Ufficio>();
		uffici = entityManager.createQuery("SELECT u from Ufficio u order by Nome asc").getResultList();
		return uffici;
	}
}
