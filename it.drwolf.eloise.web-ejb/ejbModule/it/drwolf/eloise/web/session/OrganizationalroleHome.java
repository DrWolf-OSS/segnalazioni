package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("organizationalroleHome")
public class OrganizationalroleHome extends EntityHome<Organizationalrole> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 623025582341643725L;
	@In(create = true)
	RoletypeHome roletypeHome;
	@In(create = true)
	UfficioHome ufficioHome;
	@In(create = true)
	EnteHome enteHome;
	@In(create = true)
	AreaHome areaHome;

	public void setOrganizationalroleOrganizationalRoleId(Integer id) {
		setId(id);
	}

	public Integer getOrganizationalroleOrganizationalRoleId() {
		return (Integer) getId();
	}

	@Override
	protected Organizationalrole createInstance() {
		Organizationalrole organizationalrole = new Organizationalrole();
		return organizationalrole;
	}

	public void wire() {
		getInstance();
		Roletype roletype = roletypeHome.getDefinedInstance();
		if (roletype != null) {
			getInstance().setRoletype(roletype);
		}
		Ufficio ufficio = ufficioHome.getDefinedInstance();
		if (ufficio != null) {
			getInstance().setUfficio(ufficio);
		}
		Ente ente = enteHome.getDefinedInstance();
		if (ente != null) {
			getInstance().setEnte(ente);
		}
		Area area = areaHome.getDefinedInstance();
		if (area != null) {
			getInstance().setArea(area);
		}
	}

	public boolean isWired() {
		if (getInstance().getRoletype() == null)
			return false;
		if (getInstance().getEnte() == null)
			return false;
		return true;
	}

	public Organizationalrole getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
