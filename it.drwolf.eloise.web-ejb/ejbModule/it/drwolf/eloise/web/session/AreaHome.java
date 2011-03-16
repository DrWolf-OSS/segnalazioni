package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("areaHome")
public class AreaHome extends EntityHome<Area> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8900543826953633173L;
	@In(create = true)
	EnteHome enteHome;

	public void setAreaIdsettore(Integer id) {
		setId(id);
	}

	public Integer getAreaIdsettore() {
		return (Integer) getId();
	}

	@Override
	protected Area createInstance() {
		Area area = new Area();
		return area;
	}

	public void wire() {
		getInstance();
		Ente ente = enteHome.getDefinedInstance();
		if (ente != null) {
			getInstance().setEnte(ente);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Area getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Organizationalrole> getOrganizationalroles() {
		return getInstance() == null ? null
				: new ArrayList<Organizationalrole>(getInstance()
						.getOrganizationalroles());
	}

	public List<Ufficio> getUfficios() {
		return getInstance() == null ? null : new ArrayList<Ufficio>(
				getInstance().getUfficios());
	}

}
