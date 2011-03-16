package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.SquadraIntervento;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("squadraInterventoHome")
public class SquadraInterventoHome extends EntityHome<SquadraIntervento> {

	private static final long serialVersionUID = -4591335763561932713L;

	public Integer getSquadraInterventoId() {
		return (Integer) this.getId();
	}

	public void setSquadraInterventoId(Integer id) {
		this.setId(id);
	}
}
