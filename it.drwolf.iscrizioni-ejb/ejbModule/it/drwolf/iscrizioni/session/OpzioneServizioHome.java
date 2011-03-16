package it.drwolf.iscrizioni.session;

import it.drwolf.iscrizioni.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("opzioneServizioHome")
public class OpzioneServizioHome extends EntityHome<OpzioneServizio> {

	@In(create = true)
	CategoriaOpzioniServizioHome categoriaOpzioniServizioHome;

	public void setOpzioneServizioId(String id) {
		setId(id);
	}

	public String getOpzioneServizioId() {
		return (String) getId();
	}

	@Override
	protected OpzioneServizio createInstance() {
		OpzioneServizio opzioneServizio = new OpzioneServizio();
		return opzioneServizio;
	}

	public void wire() {
		getInstance();
		CategoriaOpzioniServizio categoriaOpzioniServizio = categoriaOpzioniServizioHome
				.getDefinedInstance();
		if (categoriaOpzioniServizio != null) {
			getInstance().setCategoriaOpzioniServizio(categoriaOpzioniServizio);
		}
	}

	public boolean isWired() {
		return true;
	}

	public OpzioneServizio getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
