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

@Name("enteList")
public class EnteList extends EntityQuery {

	@In(create = true)
	EntityManager entityManager;
	
	private static final String[] RESTRICTIONS = { "lower(ente.nome) like concat(lower(#{enteList.ente.nome}),'%')", };

	private Ente ente = new Ente();

	@Override
	public String getEjbql() {
		return "select ente from Ente ente";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

	public Ente getEnte() {
		return ente;
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

	public List<Ente> getAllEntes(){
		List<Ente> enti = new ArrayList<Ente>();
		enti = entityManager.createQuery("SELECT e from Ente e  order by Nome asc").getResultList();
		return enti;
	}
}
