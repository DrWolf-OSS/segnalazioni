package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sedeHome")
public class SedeHome extends EntityHome<Sede> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2637316863596056200L;

	public void setSedeIdsede(Integer id) {
		setId(id);
	}

	public Integer getSedeIdsede() {
		return (Integer) getId();
	}

	@Override
	protected Sede createInstance() {
		Sede sede = new Sede();
		return sede;
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Sede getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Ufficio> getUfficios() {
		return getInstance() == null ? null : new ArrayList<Ufficio>(
				getInstance().getUfficios());
	}

	public List<People> getPeoples() {
		return getInstance() == null ? null : new ArrayList<People>(
				getInstance().getPeoples());
	}

}
