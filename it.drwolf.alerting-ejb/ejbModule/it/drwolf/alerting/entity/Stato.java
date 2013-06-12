package it.drwolf.alerting.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Stato {

	public final static Stato[] defaults = new Stato[] {
			new Stato("daesaminare", "Da Esaminare", 1),
			new Stato("aperto", "Aperto", 2),
			new Stato("dilazionato", "Dilazionato", 3),
			new Stato("chiuso", "Chiuso", 4) };

	private Integer id;

	private Integer posizione;

	private String nome;

	private String descrizione;

	public Stato() {
	}

	public Stato(String nome, String descrizione, Integer posizione) {
		super();
		this.descrizione = descrizione;
		this.nome = nome;
		this.posizione = posizione;
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

	@Column(insertable = true, updatable = false, unique = true)
	public Integer getPosizione() {
		return this.posizione;
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

	public void setPosizione(Integer posizione) {
		this.posizione = posizione;
	}

	@Override
	public String toString() {
		return this.getDescrizione();
	}

}
