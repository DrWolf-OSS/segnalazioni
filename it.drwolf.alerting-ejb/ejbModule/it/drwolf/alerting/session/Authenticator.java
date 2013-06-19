package it.drwolf.alerting.session;

import it.drwolf.alerting.entity.Cittadino;
import it.drwolf.alerting.util.Constants;
import it.drwolf.iscrizioni.entity.OpzioneServizio;

import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.bpm.Actor;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

@Name("authenticator")
public class Authenticator {
	@SuppressWarnings("unchecked")
	public static Cittadino findCittadino(EntityManager em, String email,
			String codice) {
		List<Cittadino> l = em
				.createQuery("from Cittadino where idIscritto=:codice")
				.setParameter("codice", codice).getResultList();
		if (l != null && l.size() > 0) {
			Cittadino c = l.get(0);

			boolean segnalazioni = false;
			for (OpzioneServizio os : c.getIscritto().getOpzioniServizi()) {
				if (os.getId().equals("segnalazioni.iscrizioni.true")) {
					segnalazioni = true;
					break;
				}
			}

			if (segnalazioni) {
				if (email == null) {
					return c;
				}
				if (c.getEmail().equals(email)) {
					return c;
				}
			}
		}
		return null;
	}

	private String email;

	private String usercode;

	@In
	private Identity identity;

	@In
	private EntityManager entityManager;

	@In(create = true)
	private Actor actor;

	@In
	private WorkSession workSession;

	@In(create = true)
	private Sso sso;

	public boolean authenticate() {

		if (this.sso.isLogged()) {
			return true;
		}
		EntityManager em = this.entityManager;

		Credentials credentials = this.identity.getCredentials();
		Cittadino c = Authenticator.findCittadino(em,
				credentials.getUsername(), credentials.getPassword());
		if (c != null) {
			credentials.setUsername(c.getIdIscritto());

			this.actor.setId(credentials.getUsername());
			this.actor.getGroupActorIds().add(Constants.CITTADINO.toString());
			this.identity.addRole(Constants.CITTADINO.toString());
			this.workSession
					.setUserFullname(c.getNome() + " " + c.getCognome());
			return true;
		}

		return false;
	}

	public void conferma() {
		this.identity.getCredentials().setUsername(this.email);
		this.identity.getCredentials().setPassword(this.usercode);
		String login = this.identity.login();
		System.out.println(login);
	}

	public String getEmail() {
		return this.email;
	}

	public String getUsercode() {
		return this.usercode;
	}

	public void impersonate(String username) throws Exception {
		this.identity.getCredentials().setUsername(username);
		this.sso.login();
		try {
			Conversation.instance().leave();
		} catch (Exception e) {
			// do nothing
		}
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
}
