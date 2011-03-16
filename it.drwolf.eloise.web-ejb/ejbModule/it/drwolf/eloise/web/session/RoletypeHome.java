package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("roletypeHome")
public class RoletypeHome extends EntityHome<Roletype> {

	public void setRoletypeRoleTypeId(Integer id) {
		setId(id);
	}

	public Integer getRoletypeRoleTypeId() {
		return (Integer) getId();
	}

	@Override
	protected Roletype createInstance() {
		Roletype roletype = new Roletype();
		return roletype;
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Roletype getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Organizationalrole> getOrganizationalroles() {
		return getInstance() == null ? null
				: new ArrayList<Organizationalrole>(getInstance()
						.getOrganizationalroles());
	}

}
