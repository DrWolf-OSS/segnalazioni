package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.EsitoSegnalazione;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("esitoSegnalazioneHome")
public class EsitoSegnalazioneHome extends EntityHome<EsitoSegnalazione> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3911578785549719013L;

	@Override
	protected EsitoSegnalazione createInstance() {
		EsitoSegnalazione canaleSegnalazione = new EsitoSegnalazione();
		return canaleSegnalazione;
	}

	public EsitoSegnalazione getDefinedInstance() {
		return this.isIdDefined() ? this.getInstance() : null;
	}

	public Integer getEsitoSegnalazioneId() {
		return (Integer) this.getId();
	}

	public boolean isWired() {
		return true;
	}

	public void setEsitoSegnalazioneId(Integer id) {
		this.setId(id);
	}

	public void wire() {
	}

}
