package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sottotipoSegnalazioneHome")
public class SottotipoSegnalazioneHome extends
		EntityHome<SottotipoSegnalazione> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9125465124282512442L;
	@In(create = true)
	TipoSegnalazioneHome tipoSegnalazioneHome;

	public void setSottotipoSegnalazioneId(Integer id) {
		setId(id);
	}

	public Integer getSottotipoSegnalazioneId() {
		return (Integer) getId();
	}

	@Override
	protected SottotipoSegnalazione createInstance() {
		SottotipoSegnalazione sottotipoSegnalazione = new SottotipoSegnalazione();
		return sottotipoSegnalazione;
	}

	public void wire() {
		TipoSegnalazione tipoSegnalazione = tipoSegnalazioneHome
				.getDefinedInstance();
		if (tipoSegnalazione != null) {
			getInstance().setTipoSegnalazione(tipoSegnalazione);
		}
	}

	public boolean isWired() {
		if (getInstance().getTipoSegnalazione() == null)
			return false;
		return true;
	}

	public SottotipoSegnalazione getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Segnalazione> getSegnalaziones() {
		return getInstance() == null ? null : new ArrayList<Segnalazione>(
				getInstance().getSegnalaziones());
	}

}
