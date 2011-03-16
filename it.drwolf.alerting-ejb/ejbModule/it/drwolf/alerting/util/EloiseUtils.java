package it.drwolf.alerting.util;

import it.drwolf.alerting.util.converters.PeopleConverter;
import it.drwolf.eloise.web.entity.Organizationalrole;
import it.drwolf.eloise.web.entity.People;
import it.drwolf.eloise.web.entity.Ufficio;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("eloiseUtils")
public class EloiseUtils {

	@In
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public Map<String, String> getPeople() {
		TreeMap<String, String> uffici = new TreeMap<String, String>();
		for (People people : (List<People>) this.entityManager.createQuery(
				"from People order by cognome").getResultList()) {
			uffici.put(people.getCognome() + PeopleConverter.nameSep
					+ people.getNome(), people.getIdpeople());
		}
		return uffici;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> getUffici() {
		TreeMap<String, Integer> uffici = new TreeMap<String, Integer>();
		for (Ufficio ufficio : (List<Ufficio>) this.entityManager.createQuery(
				"from Ufficio order by nome").getResultList()) {
			uffici.put(ufficio.getNome(), ufficio.getIdufficio());
		}
		return uffici;
	}

	public String getUserRoles(String username) {
		People p = this.entityManager.find(People.class, username);
		if (p == null) {
			return "";
		} else {

			if (p.getOrganizationalroles().size() == 0) {
				return "";
			} else {
				String res = " (";

				Iterator<Organizationalrole> it = p.getOrganizationalroles()
						.iterator();
				while (it.hasNext()) {
					Organizationalrole or = it.next();

					res += or.getRoletype().getRole();
					if (or.getUfficio() != null) {
						res += ", " + or.getUfficio().getNome();
					} else if (or.getArea() != null) {
						res += ", " + or.getArea().getNome();
					} else if (or.getEnte() != null) {
						res += ", " + or.getEnte().getNome();
					}
					if (it.hasNext()) {
						res += " - ";
					}

				}
				res += ")";
				return res;
			}

		}
	}

}
