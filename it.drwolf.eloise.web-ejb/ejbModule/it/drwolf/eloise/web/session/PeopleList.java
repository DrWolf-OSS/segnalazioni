package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.People;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.framework.EntityQuery;

@Name("peopleList")
public class PeopleList extends EntityQuery<People> {

	private static final long serialVersionUID = -635250574381001194L;

	private static final String[] RESTRICTIONS = {
			"lower(people.idpeople) like concat(lower(#{peopleList.people.idpeople}),'%')",
			"lower(people.nome) like concat(lower(#{peopleList.people.nome}),'%')",
			"lower(people.cognome) like concat(lower(#{peopleList.people.cognome}),'%')",
			"lower(people.password) like concat(lower(#{peopleList.people.password}),'%')",
			"lower(people.telefono) like concat(lower(#{peopleList.people.telefono}),'%')",
			"lower(people.fax) like concat(lower(#{peopleList.people.fax}),'%')",
			"lower(people.email) like concat(lower(#{peopleList.people.email}),'%')",
			"lower(people.profiloProfessionale) like concat(lower(#{peopleList.people.profiloProfessionale}),'%')",
			"lower(people.telefonoPrivato) like concat(lower(#{peopleList.people.telefonoPrivato}),'%')",
			"lower(people.fax1) like concat(lower(#{peopleList.people.fax1}),'%')",
			"lower(people.email1) like concat(lower(#{peopleList.people.email1}),'%')",
			"lower(people.cellulare) like concat(lower(#{peopleList.people.cellulare}),'%')",
			"lower(people.numeroBreve) like concat(lower(#{peopleList.people.numeroBreve}),'%')", };

	private People people = new People();

	@Override
	public String getEjbql() {
		return "select people from People people";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

	public People getPeople() {
		return this.people;
	}

	@Override
	public List<String> getRestrictionExpressionStrings() {
		return Arrays.asList(PeopleList.RESTRICTIONS);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ValueExpression> getRestrictions() {
		List<String> expressionStrings = Arrays.asList(PeopleList.RESTRICTIONS);

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

	@Override
	public List<People> getResultList() {
		return super.getResultList();
	}

}
