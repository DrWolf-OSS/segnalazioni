package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("orarioHome")
public class OrarioHome extends EntityHome<Orario> {

	public void setOrarioIdorario(Integer id) {
		setId(id);
	}

	public Integer getOrarioIdorario() {
		return (Integer) getId();
	}

	@Override
	protected Orario createInstance() {
		Orario orario = new Orario();
		return orario;
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Orario getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<People> getPeoples() {
		return getInstance() == null ? null : new ArrayList<People>(
				getInstance().getPeoples());
	}

	public List<Ufficio> getUfficios() {
		return getInstance() == null ? null : new ArrayList<Ufficio>(
				getInstance().getUfficios());
	}

}
