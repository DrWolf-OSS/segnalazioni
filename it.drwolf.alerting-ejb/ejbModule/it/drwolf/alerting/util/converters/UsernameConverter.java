package it.drwolf.alerting.util.converters;

import it.drwolf.alerting.entity.Cittadino;
import it.drwolf.eloise.web.entity.People;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("usernameConverter")
@org.jboss.seam.annotations.faces.Converter(id = "usernameConverter")
@BypassInterceptors
public class UsernameConverter implements Converter {

	private EntityManager entityManager;

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return arg2;
	}

	@SuppressWarnings("unchecked")
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 == null) {
			return null;
		}
		this.entityManager = (EntityManager) org.jboss.seam.Component
				.getInstance("entityManager");
		People people = this.entityManager.find(People.class, arg2.toString());
		if (people == null) {
			List<Cittadino> l = this.entityManager.createQuery(
					"from Cittadino where idIscritto=:username").setParameter(
					"username", arg2.toString()).getResultList();
			if (l.size() > 0) {
				Cittadino cittadino = l.get(0);
				return cittadino.getCognome() + " " + cittadino.getNome();
			} else {
				return arg2.toString();
			}
		} else {
			return people.getCognome() + PeopleConverter.nameSep
					+ people.getNome();
		}
	}

}