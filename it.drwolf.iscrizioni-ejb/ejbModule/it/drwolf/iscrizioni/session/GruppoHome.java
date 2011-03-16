package it.drwolf.iscrizioni.session;

import it.drwolf.iscrizioni.entity.Gruppo;
import it.drwolf.iscrizioni.entity.Iscritto;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("gruppoHome")
public class GruppoHome extends EntityHome<Gruppo> {

	public void addIscritto(Iscritto i) {
		this.getInstance().getIscritti().add(i);
		i.getGruppi().add(this.getInstance());
	}

	@Override
	protected Gruppo createInstance() {
		Gruppo gruppo = new Gruppo();
		return gruppo;
	}

	public Gruppo getDefinedInstance() {
		return this.isIdDefined() ? this.getInstance() : null;
	}

	public String getGruppoId() {
		return (String) this.getId();
	}

	public boolean isWired() {
		return true;
	}

	public void removeIscritto(Iscritto i) {
		this.getInstance().getIscritti().remove(i);
		i.getGruppi().remove(this.getInstance());

	}

	public void setGruppoId(String id) {
		this.setId(id);
	}

	public void wire() {
		this.getInstance();
	}

}
