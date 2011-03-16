package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.SottocategoriaUtenza;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("sottocategoriaUtenzaList")
public class SottocategoriaUtenzaList extends EntityQuery<SottocategoriaUtenza> {

	private static final long serialVersionUID = 2051651092012144750L;

	@Override
	public String getEjbql() {
		return "from SottocategoriaUtenza";
	}

	@Override
	public Integer getMaxResults() {
		return 99999;
	}

	@Override
	public String getOrder() {
		return "categoriaUtenza.nome, nome";
	}

}
