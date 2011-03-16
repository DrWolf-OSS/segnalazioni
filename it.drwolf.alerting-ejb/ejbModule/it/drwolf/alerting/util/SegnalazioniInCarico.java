package it.drwolf.alerting.util;

public class SegnalazioniInCarico implements Comparable<SegnalazioniInCarico> {

	private String ufficio = "";
	private Integer totale = 0;
	private Integer inTempo = 0;
	private Integer scadute = 0;

	public SegnalazioniInCarico() {

	}

	public SegnalazioniInCarico(String ufficio, Integer totale,
			Integer inTempo, Integer scadute) {
		super();
		this.ufficio = ufficio;
		this.totale = totale;
		this.inTempo = inTempo;
		this.scadute = scadute;
	}

	public int compareTo(SegnalazioniInCarico o) {
		return -this.totale.compareTo(o.getTotale());
	}

	public Integer getInTempo() {
		return this.inTempo;
	}

	public Integer getScadute() {
		return this.scadute;
	}

	public Integer getTotale() {
		return this.totale;
	}

	public String getUfficio() {
		return this.ufficio;
	}

	public void setInTempo(Integer inTempo) {
		this.inTempo = inTempo;
	}

	public void setScadute(Integer scadute) {
		this.scadute = scadute;
	}

	public void setTotale(Integer totale) {
		this.totale = totale;
	}

	public void setUfficio(String ufficio) {
		this.ufficio = ufficio;
	}
}
