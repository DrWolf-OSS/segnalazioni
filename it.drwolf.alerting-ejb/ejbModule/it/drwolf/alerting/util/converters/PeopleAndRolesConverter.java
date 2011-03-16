package it.drwolf.alerting.util.converters;

import it.drwolf.alerting.util.EloiseUtils;
import it.drwolf.eloise.web.entity.People;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("peopleAndRolesConverter")
@org.jboss.seam.annotations.faces.Converter(id = "peopleAndRolesConverter")
@BypassInterceptors
public class PeopleAndRolesConverter implements Converter {

	public final static String nameSep = " ";

	public static String formatPeople(People people) {
		if (people == null) {
			return "NULL";
		}
		return people.getCognome() + PeopleAndRolesConverter.nameSep
				+ people.getNome();
	}

	private EntityManager entityManager;

	private EloiseUtils eloiseUtils;

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {

		this.entityManager = (EntityManager) org.jboss.seam.Component
				.getInstance("entityManager");
		this.eloiseUtils = (EloiseUtils) org.jboss.seam.Component
				.getInstance("eloiseUtils");

		if (arg2 == null || arg2.equals("null") || arg2.trim().equals("")) {
			return null;
		}
		arg2 = arg2.replaceAll("\\(.*\\)", "");
		return this.entityManager.createQuery(
				"select idpeople from People "
						+ "where concat(cognome,concat(:sep,nome))=:nc")
				.setParameter("nc", arg2).setParameter("sep",
						PeopleAndRolesConverter.nameSep).getSingleResult();
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 == null) {
			return null;
		}
		this.entityManager = (EntityManager) org.jboss.seam.Component
				.getInstance("entityManager");
		this.eloiseUtils = (EloiseUtils) org.jboss.seam.Component
				.getInstance("eloiseUtils");

		People people = this.entityManager.find(People.class, arg2.toString());
		return PeopleAndRolesConverter.formatPeople(people) + " "
				+ this.eloiseUtils.getUserRoles(people.getIdpeople());
	}

}