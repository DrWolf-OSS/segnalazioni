package it.drwolf.alerting.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Risposta {
	private Integer id;
	private Segnalazione segnalazione;
	private String oggetto;
	private String testo;
	private String mittente;
	private Date data = new Date();
	private Boolean sent = false;
	private Boolean ricevuta = false;

	public Date getData() {
		return this.data;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return this.id;
	}

	public String getMittente() {
		return this.mittente;
	}

	public String getOggetto() {
		return this.oggetto;
	}

	public Boolean getRicevuta() {
		return this.ricevuta;
	}

	@ManyToOne
	public Segnalazione getSegnalazione() {
		return this.segnalazione;
	}

	public Boolean getSent() {
		return this.sent;
	}

	@Lob
	public String getTesto() {
		return this.testo;
	}

	@Transient
	public String getTestoPdf() {
		return this.getTesto().replaceAll("</p>", "</p><br/>");
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMittente(String mittente) {
		this.mittente = mittente;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public void setRicevuta(Boolean ricevuta) {
		this.ricevuta = ricevuta != null ? ricevuta : false;
	}

	public void setSegnalazione(Segnalazione segnalazione) {
		this.segnalazione = segnalazione;
	}

	public void setSent(Boolean sent) {
		if (sent == null) {
			sent = false;
		}
		this.sent = sent;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}
}
