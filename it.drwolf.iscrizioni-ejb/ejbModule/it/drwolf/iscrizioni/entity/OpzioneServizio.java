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
	private Boolean def = false;
	private CategoriaOpzioniServizio categoriaOpzioniServizio;

	@ManyToOne
	public CategoriaOpzioniServizio getCategoriaOpzioniServizio() {
		return this.categoriaOpzioniServizio;
	}

	public Boolean getDef() {
		return this.def != null ? this.def : false;
	}

	@Id
	public String getId() {
		return this.id;
	}

	public String getNome() {
		return this.nome;
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();

	}

	public void setCategoriaOpzioniServizio(
			CategoriaOpzioniServizio categoriaOpzioniServizio) {
		this.categoriaOpzioniServizio = categoriaOpzioniServizio;
	}

	public void setDef(Boolean def) {
		this.def = def;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
