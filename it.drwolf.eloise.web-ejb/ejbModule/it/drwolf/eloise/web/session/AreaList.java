package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.Area;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.framework.EntityQuery;

@Name("areaList")
public class AreaList extends EntityQuery<Area> {

	private static final long serialVersionUID = 8009800927807461186L;

	@In(create = true)
	EntityManager entityManager;

	private static final String[] RESTRICTIONS = { "lower(area.nome) like concat(lower(#{areaList.area.nome}),'%')", };

	private Area area = new Area();

	@SuppressWarnings("unchecked")
	public List<Area> getAllSettores() {
		List<Area> enti = new ArrayList<Area>();
		enti = this.entityManager.createQuery(
				"SELECT a from Area a order by Nome asc").getResultList();
		return enti;
	}

	public Area getArea() {
		return this.area;
	}

	@Override
	public String getEjbql() {
		return "select area from Area area";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

	@Override
	public List<String> getRestrictionExpressionStrings() {
		return Arrays.asList(AreaList.RESTRICTIONS);
	}

	@Override
	public List<ValueExpression> getRestrictions() {
		List<String> expressionStrings = Arrays.asList(AreaList.RESTRICTIONS);

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
