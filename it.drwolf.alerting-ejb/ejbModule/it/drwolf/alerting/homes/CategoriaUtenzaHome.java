package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.CategoriaUtenza;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("categoriaUtenzaHome")
public class CategoriaUtenzaHome extends EntityHome<CategoriaUtenza> {

	private static final long serialVersionUID = 4730863238970890495L;

	public Integer getCategoriaUtenzaId() {
		return (Integer) this.getId();
	}

	public void setCategoriaUtenzaId(Integer id) {
		this.setId(id);
	}
}
