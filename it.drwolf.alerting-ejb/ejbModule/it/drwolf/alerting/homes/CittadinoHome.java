package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("cittadinoHome")
public class CittadinoHome extends EntityHome<Cittadino> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1914333157852640104L;

	public void setCittadinoId(Integer id) {
		setId(id);
	}

	public Integer getCittadinoId() {
		return (Integer) getId();
	}

	@Override
	protected Cittadino createInstance() {
		Cittadino cittadino = new Cittadino();
		return cittadino;
	}

	public void wire() {
	}

	public boolean isWired() {
		return true;
	}

	public Cittadino getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Segnalazione> getSegnalaziones() {
		return getInstance() == null ? null : new ArrayList<Segnalazione>(
				getInstance().getSegnalaziones());
	}

}
