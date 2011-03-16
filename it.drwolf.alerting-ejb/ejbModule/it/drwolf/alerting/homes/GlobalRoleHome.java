package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.GlobalRole;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("globalRoleHome")
public class GlobalRoleHome extends EntityHome<GlobalRole> {

	private static final long serialVersionUID = 1283194657499746L;

	public Integer getGlobalRoleId() {
		return (Integer) this.getId();
	}

	public void setGlobalRoleId(Integer id) {
		this.setId(id);
	}

}
