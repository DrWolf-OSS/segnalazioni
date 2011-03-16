package it.drwolf.alerting.util.converters;

import it.drwolf.eloise.web.entity.Ufficio;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("ufficioConverter")
@org.jboss.seam.annotations.faces.Converter(id = "ufficioConverter")
@BypassInterceptors
public class UfficioConverter implements Converter {

	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if (arg2 == null || arg2.equals("null") || arg2.trim().equals("")) {
			return null;
		}
		this.entityManager = (EntityManager) org.jboss.seam.Component
				.getInstance("entityManager");
		List l = this.entityManager.createQuery(
				"select idufficio from Ufficio where nome=:nome").setParameter(
				"nome", arg2).getResultList();

		if (l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 == null) {
			return null;
		}
		this.entityManager = (EntityManager) org.jboss.seam.Component
				.getInstance("entityManager");
		return this.entityManager.find(Ufficio.class,
				Integer.parseInt(arg2.toString())).getNome();
	}

}