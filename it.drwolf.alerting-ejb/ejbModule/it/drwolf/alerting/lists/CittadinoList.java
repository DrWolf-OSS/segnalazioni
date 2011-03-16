package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.Cittadino;
import it.drwolf.alerting.util.iscrizioni.QueryIscritto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.collections.CollectionUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

@Name("cittadinoList")
@Scope(ScopeType.CONVERSATION)
public class CittadinoList extends EntityQuery<Cittadino> {

	private static final long serialVersionUID = -6510132938766315467L;

	private static final String[] RESTRICTIONS = {};

	private Cittadino cittadino = new Cittadino();

	private Map<String, it.drwolf.iscrizioni.entity.Iscritto> map;

	private List<Cittadino> results;

	public CittadinoList() {
		this.setRestrictionExpressionStrings(Arrays
				.asList(CittadinoList.RESTRICTIONS));
	}

	public List<String> autocomplete(Object event) {
		List<String> l = new ArrayList<String>();
		for (Cittadino c : this.getResultList()) {
			try {
				if (c.toString().toLowerCase().contains(
						event.toString().toLowerCase())) {
					l
							.add(c.toString()
									+ "<span style=\"display:none;\">&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;"
									+ c.getId() + "</span>");
				}
			} catch (Exception e) {

			}
		}

		return l;

	}

	@SuppressWarnings("unchecked")
	private List<Cittadino> fetchResults() {
		if (this.map == null) {
			this.map = QueryIscritto.getIscrittiMap(this.getEntityManager());
		}
		EntityManager em = this.getEntityManager();

		Collection<String> daInserire = CollectionUtils.subtract(this.map
				.keySet(), em.createQuery("select idIscritto from Cittadino")
				.getResultList());

		for (String codice : daInserire) {
			Cittadino c = new Cittadino();
			c.setIdIscritto(codice);
			em.persist(c);
		}

		List<Cittadino> l = super.getResultList();
		return l;
	}

	public Cittadino getCittadino() {
		return this.cittadino;
	}

	@Override
	public String getEjbql() {
		return "select cittadino from Cittadino cittadino";
	}

	@Override
	public Integer getMaxResults() {
		return 999999;
	}

	@Override
	public List<Cittadino> getResultList() {
		if (this.results == null) {
			this.results = this.fetchResults();
		}
		return this.results;
	}

}
