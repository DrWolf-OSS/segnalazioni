package it.drwolf.iscrizioni.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(catalog = "iscrizioni")
public class OpzioneServizio {
	private String id;
	private String nome;
	private CategoriaOpzioniServizio categoriaOpzioniServizio;

	@ManyToOne
	public CategoriaOpzioniServizio getCategoriaOpzioniServizio() {
		return categoriaOpzioniServizio;
	}

	@Id
	public String getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();

	}

	public void setCategoriaOpzioniServizio(
			CategoriaOpzioniServizio categoriaOpzioniServizio) {
		this.categoriaOpzioniServizio = categoriaOpzioniServizio;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
