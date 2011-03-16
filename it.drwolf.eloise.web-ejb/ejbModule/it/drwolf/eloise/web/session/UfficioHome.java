package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("ufficioHome")
public class UfficioHome extends EntityHome<Ufficio> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3521785061469828878L;
	@In(create = true)
	AreaHome areaHome;
	@In(create = true)
	SedeHome sedeHome;
	@In(create = true)
	OrarioHome orarioHome;

	public void setUfficioIdufficio(Integer id) {
		setId(id);
	}

	public Integer getUfficioIdufficio() {
		return (Integer) getId();
	}

	@Override
	protected Ufficio createInstance() {
		Ufficio ufficio = new Ufficio();
		return ufficio;
	}

	public void wire() {
		getInstance();
		Area area = areaHome.getDefinedInstance();
		if (area != null) {
			getInstance().setArea(area);
		}
		Sede sede = sedeHome.getDefinedInstance();
		if (sede != null) {
			getInstance().setSede(sede);
		}
		Orario orario = orarioHome.getDefinedInstance();
		if (orario != null) {
			getInstance().setOrario(orario);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Ufficio getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Organizationalrole> getOrganizationalroles() {
		return getInstance() == null ? null
				: new ArrayList<Organizationalrole>(getInstance()
						.getOrganizationalroles());
	}

}
