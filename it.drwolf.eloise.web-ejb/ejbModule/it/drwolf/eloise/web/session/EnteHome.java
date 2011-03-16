package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.*;

import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("enteHome")
public class EnteHome extends EntityHome<Ente> {

	public void setEnteIdente(Integer id) {
		setId(id);
	}

	public Integer getEnteIdente() {
		return (Integer) getId();
	}

	@Override
	protected Ente createInstance() {
		Ente ente = new Ente();
		return ente;
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Ente getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Area> getAreas() {
		return getInstance() == null ? null : new ArrayList<Area>(getInstance()
				.getAreas());
	}

	public List<Organizationalrole> getOrganizationalroles() {
		return getInstance() == null ? null
				: new ArrayList<Organizationalrole>(getInstance()
						.getOrganizationalroles());
	}
	

}
