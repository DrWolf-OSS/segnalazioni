package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.Sostituto;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.security.Identity;

@Name("sostitutoList")
public class SostitutoList extends EntityQuery<Sostituto> {

	private static final long serialVersionUID = -8260372104942617936L;

	private static final String[] RESTRICTIONS = { "sostituto.username = #{identity.username}", };

	public SostitutoList() {
		this.setRestrictionExpressionStrings(Arrays
				.asList(SostitutoList.RESTRICTIONS));
	}

	@Override
	public String getEjbql() {
		return "select sostituto from Sostituto sostituto";
	}

	@SuppressWarnings("unchecked")
	public List<Sostituto> getInverseList() {
		return this.getEntityManager().createQuery(
				"from Sostituto where sostituto=:u").setParameter("u",
				Identity.instance().getCredentials().getUsername())
				.getResultList();

	}

	@Override
	public Integer getMaxResults() {
		return 999999;
	}

	@Override
	public String getOrder() {
		return "id";
	}

}
