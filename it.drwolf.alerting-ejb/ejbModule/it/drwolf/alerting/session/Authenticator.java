package it.drwolf.alerting.session;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.bpm.Actor;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

import it.drwolf.alerting.entity.Cittadino;
import it.drwolf.alerting.util.Constants;
import it.drwolf.iscrizioni.entity.Iscritto;
import it.drwolf.iscrizioni.entity.OpzioneServizio;

@Name("authenticator")
public class Authenticator {
	@SuppressWarnings("unchecked")
	public static Cittadino findCittadino(EntityManager em, String email, String codice) {
		List<Cittadino> l = em.createQuery("from Cittadino where idIscritto=:codice").setParameter("codice", codice)
				.getResultList();
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

		Credentials credentials = this.identity.getCredentials();
		//
		// Controllo presenza iscritto
		Iscritto iscritto = this.entityManager.find(Iscritto.class, credentials.getPassword());
		if (iscritto == null) {
			return false;
		}

		// Controllo che iscritto abbia abilitato il servizio segnalazioni
		boolean segnalazioni = false;
		Iterator<OpzioneServizio> iterator = iscritto.getOpzioniServizi().iterator();
		while (iterator.hasNext()) {
			OpzioneServizio os = iterator.next();
			if (os.getId().equals("segnalazioni.iscrizioni.true")) {
				segnalazioni = true;
			}
		}
		if (!segnalazioni) {
			return false;
		}

		//
		Cittadino c = null;
		@SuppressWarnings("unchecked")
		List<Cittadino> l = this.entityManager.createQuery("from Cittadino where idIscritto=:codice")
				.setParameter("codice", credentials.getPassword()).getResultList();
		if (l != null && l.size() > 0) {
			c = l.get(0);
		} else {
			c = new Cittadino();
			c.setIdIscritto(iscritto.getId());
			this.entityManager.persist(c);
		}

		credentials.setUsername(c.getIdIscritto());

		this.actor.setId(credentials.getUsername());
		this.actor.getGroupActorIds().add(Constants.CITTADINO.toString());
		this.identity.addRole(Constants.CITTADINO.toString());
		this.workSession.setUserFullname(c.getNome() + " " + c.getCognome());

		return true;
	}

	public void conferma() {
		this.identity.getCredentials().setUsername(this.email);
		this.identity.getCredentials().setPassword(this.usercode);
		this.identity.login();
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
