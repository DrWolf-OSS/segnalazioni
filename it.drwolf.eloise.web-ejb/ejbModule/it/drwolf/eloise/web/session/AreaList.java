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

@Name("areaList")
public class AreaList extends EntityQuery {


	@In(create = true)
	EntityManager entityManager;
	
	private static final String[] RESTRICTIONS = { "lower(area.nome) like concat(lower(#{areaList.area.nome}),'%')", };

	private Area area = new Area();

	@Override
	public String getEjbql() {
		return "select area from Area area";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

	public Area getArea() {
		return area;
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
	
	public List<Area> getAllSettores(){
		List<Area> enti = new ArrayList<Area>();
		enti = entityManager.createQuery("SELECT a from Area a order by Nome asc").getResultList();
		return enti;
	}
}
