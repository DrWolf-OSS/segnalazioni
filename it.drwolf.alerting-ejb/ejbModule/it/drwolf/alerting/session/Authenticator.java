package it.drwolf.alerting.session;

import it.drwolf.alerting.entity.Cittadino;
import it.drwolf.alerting.util.Constants;

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
		if ((l != null) && (l.size() > 0)) {
			Cittadino c = l.get(0);
			if (email == null) {
				return c;
			}
			if (c.getEmail().equals(email)) {
				return c;
			}
		}
		return null;
	}

	@In
	private Identity identity;

	@In
	private EntityManager entityManager;

	@In(create = true)
	private Actor actor;

	@In
	private WorkSession workSession;

	@In
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

	public void impersonate(String username) throws Exception {
		this.identity.getCredentials().setUsername(username);
		this.sso.login();
		try {
			Conversation.instance().leave();
		} catch (Exception e) {
			// do nothing
		}
	}
}
