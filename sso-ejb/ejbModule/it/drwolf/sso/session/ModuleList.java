package it.drwolf.sso.session;

import it.drwolf.sso.entity.Module;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("moduleList")
public class ModuleList extends EntityQuery {

	private static final String[] RESTRICTIONS = { "lower(module.name) like concat(lower(#{moduleList.module.name}),'%')", };

	private Module module = new Module();

	@Override
	public String getEjbql() {
		return "select module from Module module";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

	public Module getModule() {
		return this.module;
	}

	@Override
	public List<String> getRestrictions() {
		return Arrays.asList(ModuleList.RESTRICTIONS);
	}

	@Override
	public List getResultList() {
		return this.getEntityManager().createQuery("from Module order by name")
				.getResultList();
	}

}
