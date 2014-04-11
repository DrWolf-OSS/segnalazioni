package it.drwolf.alerting.util.converters;

import it.drwolf.eloise.web.entity.People;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("peopleConverter")
@org.jboss.seam.annotations.faces.Converter(id = "peopleConverter")
@BypassInterceptors
public class PeopleConverter implements Converter {

	public final static String nameSep = " ";

	public static String formatPeople(People people) {
		if (people == null) {
			return "NULL";
		}
		return people.getCognome() + PeopleConverter.nameSep + people.getNome();
	}

	public static String formatPeopleInverse(People people) {
		if (people == null) {
			return "NULL";
		}
		return people.getNome() + PeopleConverter.nameSep + people.getCognome();
	}

	private EntityManager entityManager;

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if (arg2 == null || arg2.equals("null") || arg2.trim().equals("")) {
			return null;
		}
		this.entityManager = (EntityManager) org.jboss.seam.Component.getInstance("entityManager");
		return this.entityManager.createQuery("select idpeople from People " + "where concat(cognome,concat(:sep,nome))=:nc").setParameter("nc", arg2)
				.setParameter("sep", PeopleConverter.nameSep).getSingleResult();
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 == null) {
			return null;
		}
		this.entityManager = (EntityManager) org.jboss.seam.Component.getInstance("entityManager");
		People people = this.entityManager.find(People.class, arg2.toString());
		return PeopleConverter.formatPeople(people);
	}

}