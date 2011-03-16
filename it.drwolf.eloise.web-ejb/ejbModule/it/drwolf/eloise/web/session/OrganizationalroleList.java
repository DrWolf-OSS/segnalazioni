package it.drwolf.eloise.web.session;

import it.drwolf.eloise.web.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.List;
import java.util.Arrays;

@Name("organizationalroleList")
public class OrganizationalroleList extends EntityQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7692757369556597185L;

	private static final String[] RESTRICTIONS = {};

	private Organizationalrole organizationalrole = new Organizationalrole();

	@Override
	public String getEjbql() {
		return "select organizationalrole from Organizationalrole organizationalrole";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

	public Organizationalrole getOrganizationalrole() {
		return organizationalrole;
	}

	@Override
	public List<String> getRestrictionExpressionStrings() {
		return Arrays.asList(RESTRICTIONS);
	}

}
