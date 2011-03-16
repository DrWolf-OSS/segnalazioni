package it.drwolf.iscrizioni.session;

import it.drwolf.iscrizioni.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("appParamHome")
public class AppParamHome extends EntityHome<AppParam> {

	public void setAppParamId(String id) {
		setId(id);
	}

	public String getAppParamId() {
		return (String) getId();
	}

	@Override
	protected AppParam createInstance() {
		AppParam appParam = new AppParam();
		return appParam;
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public AppParam getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
