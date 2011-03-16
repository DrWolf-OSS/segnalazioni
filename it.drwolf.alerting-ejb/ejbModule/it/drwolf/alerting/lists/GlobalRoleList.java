package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.GlobalRole;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("globalRoleList")
@AutoCreate
public class GlobalRoleList extends EntityQuery<GlobalRole> {

	private static final long serialVersionUID = -7814979114808312868L;

	@Override
	public String getEjbql() {
		return "from GlobalRole";
	}

	@Override
	public Integer getMaxResults() {
		return 99999;
	}

	@Override
	public String getOrder() {
		return "name";
	}

}
