package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.Cittadino;
import it.drwolf.alerting.entity.Risposta;
import it.drwolf.alerting.util.converters.PeopleConverter;
import it.drwolf.eloise.web.entity.People;

import java.util.Date;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.security.Identity;

@Name("rispostaHome")
public class RispostaHome extends EntityHome<Risposta> {

	private static final long serialVersionUID = 1167852216804545717L;

	private Risposta risposta = new Risposta();

	@In
	private Renderer renderer;

	@In
	private SegnalazioneHome segnalazioneHome;

	public void addRisposta(Risposta risposta) {
		risposta
				.setMittente(Identity.instance().getCredentials().getUsername());
		risposta.setSegnalazione(this.segnalazioneHome.getInstance());
		this.getEntityManager().persist(risposta);
		this.segnalazioneHome.getInstance().getRisposte().add(risposta);
		this.getEntityManager().merge(this.segnalazioneHome.getInstance());
		this.risposta = new Risposta();
	}

	public boolean canSendEmail(Risposta r) {

		final Cittadino cittadino = r.getSegnalazione().getCittadino();
		if (cittadino != null) {
			return cittadino.hasEmail();
		}
		String email = this.getEntityManager().find(People.class,
				r.getSegnalazione().getIdutenteInseritore()).getEmail();
		return email != null && email.length() > 5;
	}

	@Override
	protected Risposta createInstance() {
		Risposta risposta = new Risposta();
		risposta.setData(new Date());
		risposta
				.setMittente(Identity.instance().getCredentials().getUsername());
		risposta.setSegnalazione(this.segnalazioneHome.getInstance());
		return risposta;
	}

	public void elimina(Risposta r) {
		r.getSegnalazione().getRisposte().remove(r);
		this.getEntityManager().remove(r);
	}

	public String getFromAddress() {
		return this.getEntityManager().find(People.class,
				this.getInstance().getMittente()).getEmail();
	}

	public String getFromName() {
		return PeopleConverter.formatPeople(this.getEntityManager().find(
				People.class, this.getInstance().getMittente()));
	}

	public Risposta getRisposta() {

		return this.risposta;
	}

	public Integer getRispostaId() {
		return (Integer) this.getId();
	}

	public String getToAddress() {
		final Cittadino cittadino = this.getInstance().getSegnalazione()
				.getCittadino();
		if (cittadino != null) {
			return cittadino.getEmail();
		}
		return this.getEntityManager().find(People.class,
				this.getInstance().getSegnalazione().getIdutenteInseritore())
				.getEmail();
	}

	public String getToName() {
		final Cittadino cittadino = this.getInstance().getSegnalazione()
				.getCittadino();
		if (cittadino != null) {
			return cittadino.getCognome() + " " + cittadino.getNome();
		}
		return PeopleConverter.formatPeople(this.getEntityManager().find(
				People.class,
				this.getInstance().getSegnalazione().getIdutenteInseritore()));
	}

	public void mailRisposta(Risposta r) {
		this.setId(r.getId());
		this.renderer.render("/app/segnalazione/mailRisposta.xhtml");
		this.setSent();
		FacesMessages.instance().add("Email inviata");
	}

	public void setRisposta(Risposta risposta) {
		this.risposta = risposta;
	}

	public void setRispostaId(Integer rispostaId) {
		this.setId(rispostaId);
	}

	public void setSent() {
		Risposta r = this.getInstance();
		if (this.isIdDefined() && !r.getSent()) {
			r.setSent(true);
			this.getEntityManager().merge(r);
		}
	}
}
