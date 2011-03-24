package it.drwolf.alerting.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CodiceTriage {

	public static final CodiceTriage[] defaults = new CodiceTriage[] {
			new CodiceTriage("nero", "#000000", -1, "libera con orario"),
			new CodiceTriage("blu", "#0000ff", -1, "libera"),
			new CodiceTriage("verde", "#00ff00", 30, "bassa"),
			new CodiceTriage("giallo", "#ffff00", 7, "media"),
			new CodiceTriage("rosso", "#ff0000", 2, "alta") };

	private String id;

	private String colore;

	private String descrizione;

	private Integer tempoIntervento;

	private Integer priorita = 10;

	public CodiceTriage() {

	}

	private CodiceTriage(String id, String colore, Integer tempoIntervento,
			String descrizione) {
		super();
		this.colore = colore;
		this.id = id;
		this.tempoIntervento = tempoIntervento;
		this.descrizione = descrizione;
	}

	public String getColore() {
		return this.colore;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	@Id
	public String getId() {
		return this.id;
	}

	public Integer getPriorita() {
		return this.priorita;
	}

	public Integer getTempoIntervento() {
		return this.tempoIntervento;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPriorita(Integer priorita) {
		this.priorita = priorita;
	}

	public void setTempoIntervento(Integer tempoIntervento) {
		this.tempoIntervento = tempoIntervento;
	}

	@Override
	public String toString() {
		return this.id;
	}
}
