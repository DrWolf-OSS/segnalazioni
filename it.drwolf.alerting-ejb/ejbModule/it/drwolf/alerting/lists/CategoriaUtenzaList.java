package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.CategoriaUtenza;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("categoriaUtenzaList")
public class CategoriaUtenzaList extends EntityQuery<CategoriaUtenza> {

	private static final long serialVersionUID = 2051651092444844750L;

	@Override
	public String getEjbql() {
		return "from CategoriaUtenza";
	}

	@Override
	public Integer getMaxResults() {
		return 99999;
	}

	@Override
	public String getOrder() {
		return "nome";
	}

}
