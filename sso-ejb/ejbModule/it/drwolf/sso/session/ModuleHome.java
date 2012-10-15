package it.drwolf.sso.session;

import it.drwolf.sso.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("moduleHome")
public class ModuleHome extends EntityHome<Module> {

	public void setModuleId(Integer id) {
		setId(id);
	}

	public Integer getModuleId() {
		return (Integer) getId();
	}

	@Override
	protected Module createInstance() {
		Module module = new Module();
		return module;
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Module getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
