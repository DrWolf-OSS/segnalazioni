package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.Stato;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("statoHome")
public class StatoHome extends EntityHome<Stato> {

	private static final long serialVersionUID = -1753038957467166037L;

	@Override
	protected Stato createInstance() {
		Stato stato = new Stato();
		return stato;
	}

	public Integer getStatoId() {

		return (Integer) this.getId();
	}

	public void setStatoId(Integer id) {
		this.setId(id);
	}

}
