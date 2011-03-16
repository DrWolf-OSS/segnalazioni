package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.SottocategoriaUtenza;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sottocategoriaUtenzaHome")
public class SottocategoriaUtenzaHome extends EntityHome<SottocategoriaUtenza> {

	private static final long serialVersionUID = 4730863238970560495L;

	public Integer getSottocategoriaUtenzaId() {
		return (Integer) this.getId();
	}

	public void setSottocategoriaUtenzaId(Integer id) {
		this.setId(id);
	}
}
