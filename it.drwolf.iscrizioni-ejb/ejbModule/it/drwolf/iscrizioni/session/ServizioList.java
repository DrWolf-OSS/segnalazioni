package it.drwolf.iscrizioni.session;

import it.drwolf.iscrizioni.entity.Servizio;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("servizioList")
public class ServizioList extends EntityQuery<Servizio> {

	private static final long serialVersionUID = -6059323237907187608L;

	private static final String EJBQL = "select servizio from Servizio servizio";

	private static final String[] RESTRICTIONS = {
			"lower(servizio.nome) like concat(lower(#{servizioList.servizio.nome}),'%')",
			"lower(servizio.url) like concat(lower(#{servizioList.servizio.url}),'%')", };

	private Servizio servizio = new Servizio();

	public ServizioList() {
		this.setEjbql(ServizioList.EJBQL);
		this.setRestrictionExpressionStrings(Arrays
				.asList(ServizioList.RESTRICTIONS));
		this.setOrder("posizione, nome");
		this.setMaxResults(99999);
	}

	public Servizio getServizio() {
		return this.servizio;
	}

}
