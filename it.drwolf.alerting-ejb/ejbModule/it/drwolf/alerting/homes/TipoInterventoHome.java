package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.SottotipoIntervento;
import it.drwolf.alerting.entity.TipoIntervento;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tipoInterventoHome")
public class TipoInterventoHome extends EntityHome<TipoIntervento> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 101870400407184640L;

	@Override
	protected TipoIntervento createInstance() {
		TipoIntervento tipoIntervento = new TipoIntervento();
		return tipoIntervento;
	}

	public TipoIntervento getDefinedInstance() {
		return this.isIdDefined() ? this.getInstance() : null;
	}

	public List<SottotipoIntervento> getSottotipoInterventos() {
		return this.getInstance() == null ? null
				: new ArrayList<SottotipoIntervento>(this.getInstance()
						.getSottotipoInterventos());
	}

	public Integer getTipoInterventoId() {
		return (Integer) this.getId();
	}

	public boolean isWired() {
		return true;
	}

	public void setTipoInterventoId(Integer id) {
		this.setId(id);
	}

	public void wire() {
	}

}
