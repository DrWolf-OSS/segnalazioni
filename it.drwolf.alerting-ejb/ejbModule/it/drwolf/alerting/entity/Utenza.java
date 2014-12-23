package it.drwolf.alerting.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Utenza {
	private Integer id;

	private String descrizione;

	private SottocategoriaUtenza sottocategoriaUtenza;

	public String getDescrizione() {
		return this.descrizione;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return this.id;
	}

	@ManyToOne
	public SottocategoriaUtenza getSottocategoriaUtenza() {
		return this.sottocategoriaUtenza;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSottocategoriaUtenza(SottocategoriaUtenza sottocategoriaUtenza) {
		this.sottocategoriaUtenza = sottocategoriaUtenza;
	}

	@Override
	public String toString() {
		return this.descrizione;
	}
}
