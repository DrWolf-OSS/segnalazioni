package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.UfficioCompetente;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("ufficioCompetenteHome")
public class UfficioCompetenteHome extends EntityHome<UfficioCompetente> {

	private static final long serialVersionUID = 1283194049916219746L;

	public Integer getUfficioCompetenteId() {
		return (Integer) this.getId();
	}

	public void setUfficioCompetenteId(Integer id) {
		this.setId(id);
	}

}
