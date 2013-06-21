package it.drwolf.alerting.pagebean;

import it.drwolf.alerting.entity.AppParam;
import it.drwolf.alerting.util.MailSender;
import it.drwolf.iscrizioni.entity.Iscritto;
import it.drwolf.iscrizioni.entity.OpzioneServizio;
import it.drwolf.iscrizioni.entity.Servizio;
import it.drwolf.iscrizioni.session.IscrittoHome;
import it.drwolf.iscrizioni.util.IdGenerator;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;

@Name("iscrizioneManager")
public class IscrizioneManager {

	private String nome;

	private String cognome;

	private String email;

	@In(create = true)
	IscrittoHome iscrittoHome;

	@In(create = true)
	MailSender mailSender;

	@In
	EntityManager entityManager;

	public String getCognome() {
		return this.cognome;
	}

	public String getEmail() {
		return this.email;
	}

	public String getNome() {
		return this.nome;
	}

	@SuppressWarnings("unchecked")
	public void nuovaIscrizione() {
		List<Iscritto> l = this.entityManager
				.createQuery("from Iscritto where email=:email")
				.setParameter("email", this.email).getResultList();
		if (!l.isEmpty()) {
			FacesMessages
					.instance()
					.add(org.jboss.seam.international.StatusMessage.Severity.WARN,
							String.format(
									"Il cittadino rispondente alla mail %s risulta già iscritto",
									l.get(0).getEmail()), null);
		} else {
			Iscritto iscritto = new Iscritto();
			iscritto.setId(IdGenerator.newId(this.entityManager));
			iscritto.setEmail(this.email);

			iscritto.setNome(this.nome);
			iscritto.setCognome(this.cognome);
			Servizio segnalazioni = this.entityManager.find(Servizio.class,
					"segnalazioni");
			OpzioneServizio segnalazioniIscrizioniTrue = this.entityManager
					.find(OpzioneServizio.class, "segnalazioni.iscrizioni.true");
			iscritto.getServizi().add(segnalazioni);
			iscritto.getOpzioniServizi().add(segnalazioniIscrizioniTrue);
			this.entityManager.persist(iscritto);

			String url = String.format(
					"%s/conferma.seam?email=%s&usercode=%s",
					this.entityManager.find(AppParam.class,
							AppParam.APP_URL.getKey()).getValue(),
					iscritto.getEmail(), iscritto.getId());

			String msg = String
					.format("L'iscrizione al servizio segnalazioni per l'utente %s %s e' avvenuta con successo."
							+ "\n\nLe credenziali per l'accesso sono:\nemail: %s\ncodice utente: %s"
							+ "\n\nPer accedere direttamente alla compilazione di una nuova segnalazione cliccare sul link sottostante:\n%s",
							iscritto.getNome(), iscritto.getCognome(),
							iscritto.getEmail(), iscritto.getId(), url);
			this.mailSender.sendSimpleMail(Arrays.asList(iscritto.getEmail()),
					"Nuova Iscrizione", msg);
			FacesMessages
					.instance()
					.add(org.jboss.seam.international.StatusMessage.Severity.INFO,
							String.format(
									"L'utente è stato inserito correttamente. E' stata inviata una mail con i codici di accesso all'indirizzo %s",
									this.email), null);
		}
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
