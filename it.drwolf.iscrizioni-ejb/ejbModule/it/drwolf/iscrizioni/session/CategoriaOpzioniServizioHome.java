package it.drwolf.iscrizioni.session;

import it.drwolf.iscrizioni.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("categoriaOpzioniServizioHome")
public class CategoriaOpzioniServizioHome extends
		EntityHome<CategoriaOpzioniServizio> {

	@In(create = true)
	ServizioHome servizioHome;

	public void setCategoriaOpzioniServizioId(String id) {
		setId(id);
	}

	public String getCategoriaOpzioniServizioId() {
		return (String) getId();
	}

	@Override
	protected CategoriaOpzioniServizio createInstance() {
		CategoriaOpzioniServizio categoriaOpzioniServizio = new CategoriaOpzioniServizio();
		return categoriaOpzioniServizio;
	}

	public void wire() {
		getInstance();
		Servizio servizio = servizioHome.getDefinedInstance();
		if (servizio != null) {
			getInstance().setServizio(servizio);
		}
	}

	public boolean isWired() {
		return true;
	}

	public CategoriaOpzioniServizio getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<OpzioneServizio> getOpzioniServizio() {
		return getInstance() == null ? null : new ArrayList<OpzioneServizio>(
				getInstance().getOpzioniServizio());
	}

}
