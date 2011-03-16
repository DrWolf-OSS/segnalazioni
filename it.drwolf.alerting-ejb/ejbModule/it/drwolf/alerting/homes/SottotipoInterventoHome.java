package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sottotipoInterventoHome")
public class SottotipoInterventoHome extends EntityHome<SottotipoIntervento> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1488355993545793252L;
	@In(create = true)
	TipoInterventoHome tipoInterventoHome;

	public void setSottotipoInterventoId(Integer id) {
		setId(id);
	}

	public Integer getSottotipoInterventoId() {
		return (Integer) getId();
	}

	@Override
	protected SottotipoIntervento createInstance() {
		SottotipoIntervento sottotipoIntervento = new SottotipoIntervento();
		return sottotipoIntervento;
	}

	public void wire() {
		TipoIntervento tipoIntervento = tipoInterventoHome.getDefinedInstance();
		if (tipoIntervento != null) {
			getInstance().setTipoIntervento(tipoIntervento);
		}
	}

	public boolean isWired() {
		if (getInstance().getTipoIntervento() == null)
			return false;
		return true;
	}

	public SottotipoIntervento getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Intervento> getInterventos() {
		return getInstance() == null ? null : new ArrayList<Intervento>(
				getInstance().getInterventos());
	}

}
