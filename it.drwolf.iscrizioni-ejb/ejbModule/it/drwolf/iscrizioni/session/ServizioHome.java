package it.drwolf.iscrizioni.session;

import it.drwolf.iscrizioni.entity.CategoriaOpzioniServizio;
import it.drwolf.iscrizioni.entity.Servizio;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("servizioHome")
public class ServizioHome extends EntityHome<Servizio> {

	private static final long serialVersionUID = -7586573028410932133L;

	@Override
	protected Servizio createInstance() {
		Servizio servizio = new Servizio();
		return servizio;
	}

	public List<CategoriaOpzioniServizio> getCategorieOpzioni() {
		return this.getInstance() == null ? null
				: new ArrayList<CategoriaOpzioniServizio>(this.getInstance()
						.getCategorieOpzioni());
	}

	public Servizio getDefinedInstance() {
		return this.isIdDefined() ? this.getInstance() : null;
	}

	public String getServizioId() {
		return (String) this.getId();
	}

	public boolean isWired() {
		return true;
	}

	public void setServizioId(String id) {
		this.setId(id);
	}

	public void wire() {
		this.getInstance();
	}

}
