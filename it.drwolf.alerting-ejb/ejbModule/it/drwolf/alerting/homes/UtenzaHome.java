package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.Utenza;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("utenzaHome")
public class UtenzaHome extends EntityHome<Utenza> {

	private static final long serialVersionUID = 6265380952693948485L;

	public Integer getUtenzaId() {
		return (Integer) this.getId();
	}

	public void setUtenzaId(Integer id) {
		this.setId(id);
	}
}
