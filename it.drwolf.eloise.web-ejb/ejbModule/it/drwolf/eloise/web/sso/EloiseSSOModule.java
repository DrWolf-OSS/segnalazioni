package it.drwolf.eloise.web.sso;

import it.drwolf.eloise.web.entity.AppParamWeb;
import it.drwolf.eloise.web.entity.People;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

import org.apache.commons.mail.SimpleEmail;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("eloiseSSOModule")
@Stateful
@EJB(name = "EloiseSSOModule", beanInterface = SSOModuleIF.class)
public class EloiseSSOModule implements SSOModuleIF {

	@In
	private EntityManager entityManager;

	public boolean changePassword(String username, String oldPassword,
			String newPassword) {
		People people = this.entityManager.find(People.class, username);
		if (people != null) {
			if (people.getPassword().equals(oldPassword)) {
				people.setPassword(newPassword);
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<String> listUsers() {
		return this.entityManager.createQuery("select idpeople from People")
				.getResultList();
	}

	public HashMap<String, String> login(String username, String password) {
		HashMap<String, String> info = new HashMap<String, String>();
		People p = this.entityManager.find(People.class, username);
		if (p != null && p.getPassword().equals(password)) {
			info.put("username", p.getIdpeople());
			info.put("cognome", p.getCognome());
			info.put("nome", p.getNome());
			info.put("email", p.getEmail());
			return info;
		}
		return null;
	}

	public String remindPassword(String username) {
		String res = "";
		People people = this.entityManager.find(People.class, username);
		if (people == null) {
			return res;
		}
		if (people.getPassword() == null
				|| "".equals(people.getPassword().trim())) {
			people.setPassword(UUID.randomUUID().toString().split("-")[0]);
			this.entityManager.merge(people);
		}
		try {
			String fromAddress = "noreply@noreply.com";
			String fromName = "Gestione Utenti";
			String subject = "Richiesta invio password";
			String defaultTo = "agea@drwolf.it";

			try {
				fromAddress = this.entityManager.find(AppParamWeb.class,
						"mail.from.address").getValue();
			} catch (Exception e) {
			}
			try {
				fromName = this.entityManager.find(AppParamWeb.class,
						"mail.from.name").getValue();
			} catch (Exception e) {
			}
			try {
				defaultTo = this.entityManager.find(AppParamWeb.class,
						"mail.default.to").getValue();
				res = defaultTo;
			} catch (Exception e) {
			}
			try {
				subject = this.entityManager.find(AppParamWeb.class,
						"mail.subject").getValue();
			} catch (Exception e) {
			}

			SimpleEmail email = new SimpleEmail();
			email.setHostName(this.entityManager.find(AppParamWeb.class,
					"mail.host").getValue());
			email.setSmtpPort(Integer.parseInt(this.entityManager.find(
					AppParamWeb.class, "mail.port").getValue()));

			if (people.getEmail() != null) {
				email.addTo(people.getEmail(), people.getCognome() + " "
						+ people.getNome());
				res = people.getEmail();
			} else {
				email.addTo(defaultTo, people.getCognome() + " "
						+ people.getNome());
			}

			email.setFrom(fromAddress, fromName);
			email.setSubject(subject);
			email.setMsg("Username: " + people.getIdpeople() + "\r\n"
					+ "Password: " + people.getPassword());
			email.send();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		return res;
	}

	@Destroy
	@Remove
	public void remove() {

	}

}
