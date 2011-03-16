package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.SottotipoSegnalazione;
import it.drwolf.alerting.entity.TipoSegnalazione;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tipoSegnalazioneHome")
public class TipoSegnalazioneHome extends EntityHome<TipoSegnalazione> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5267808839538558503L;

	@Override
	protected TipoSegnalazione createInstance() {
		TipoSegnalazione tipoSegnalazione = new TipoSegnalazione();
		return tipoSegnalazione;
	}

	public TipoSegnalazione getDefinedInstance() {
		return this.isIdDefined() ? this.getInstance() : null;
	}

	public List<SottotipoSegnalazione> getSottotipoSegnalaziones() {
		return this.getInstance() == null ? null
				: new ArrayList<SottotipoSegnalazione>(this.getInstance()
						.getSottotipoSegnalaziones());
	}

	public Integer getTipoSegnalazioneId() {
		return (Integer) this.getId();
	}

	public boolean isWired() {
		return true;
	}

	public void setTipoSegnalazioneId(Integer id) {
		this.setId(id);
	}

	public void wire() {
	}

}
