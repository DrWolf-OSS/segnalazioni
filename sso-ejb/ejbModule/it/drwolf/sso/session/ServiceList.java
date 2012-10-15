package it.drwolf.sso.session;

import it.drwolf.sso.entity.Service;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("serviceList")
public class ServiceList extends EntityQuery {

	private static final String[] RESTRICTIONS = {
			"lower(service.id) like concat(lower(#{serviceList.service.id}),'%')",
			"lower(service.loginUrl) like concat(lower(#{serviceList.service.loginUrl}),'%')",
			"lower(service.logoutUrl) like concat(lower(#{serviceList.service.logoutUrl}),'%')",
			"lower(service.name) like concat(lower(#{serviceList.service.name}),'%')", };

	private Service service = new Service();

	@Override
	public String getEjbql() {
		return "select service from Service service";
	}

	@Override
	public Integer getMaxResults() {
		return 99999;
	}

	@Override
	public List<String> getRestrictions() {
		return Arrays.asList(ServiceList.RESTRICTIONS);
	}

	@Override
	public List getResultList() {
		return this.getEntityManager().createQuery("from Service order by id")
				.getResultList();
	}

	public Service getService() {
		return this.service;
	}

}
