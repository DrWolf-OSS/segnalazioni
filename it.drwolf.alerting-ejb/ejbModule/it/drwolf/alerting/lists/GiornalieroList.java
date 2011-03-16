package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.Giornaliero;
import it.drwolf.alerting.session.AlertingController;

import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.security.Identity;

@Name("giornalieroList")
public class GiornalieroList extends EntityQuery<Giornaliero> {

	private static final long serialVersionUID = 7420809298768492239L;

	@In
	private AlertingController alertingController;

	@Override
	public String getEjbql() {
		return "from Giornaliero";
	}

	@SuppressWarnings("unchecked")
	public List<Giornaliero> getListaGiornalieri() {

		return this
				.getEntityManager()
				.createQuery(
						"select distinct g from Giornaliero g left outer join g.interventi i "
								+ "where i.sottotipoIntervento.tipoIntervento.id in (:idList) "
								+ "order by g.data desc").setParameter(
						"idList",
						this.alertingController.getIdTipiIntervento(Identity
								.instance().getCredentials().getUsername()))
				.getResultList();
	}

	@Override
	public Integer getMaxResults() {
		return 999999;
	}

	@Override
	public String getOrder() {
		return "data desc";
	}
}
