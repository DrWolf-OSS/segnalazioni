package it.drwolf.alerting.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Stato {

	public final static Stato[] defaults = new Stato[] {
			new Stato("daesaminare", "Da Esaminare"),
			new Stato("aperto", "Aperto"),
			new Stato("dilazionato", "Dilazionato"),
			new Stato("chiuso", "Chiuso") };

	private Integer id;

	private String nome;

	private String descrizione;

	public Stato() {
	}

	public Stato(String nome, String descrizione) {
		super();
		this.descrizione = descrizione;
		this.nome = nome;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	public Integer getId() {
		return this.id;
	}

	@Column(insertable = true, updatable = false, unique = true)
	public String getNome() {
		return this.nome;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return this.getDescrizione();
	}

}
