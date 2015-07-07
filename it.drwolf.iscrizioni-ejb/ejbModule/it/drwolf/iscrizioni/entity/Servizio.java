package it.drwolf.iscrizioni.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(catalog = Catalog.name)
public class Servizio {
	private String id;
	private Integer posizione;
	private String nome;
	private String url;
	private String userPrefsUrl;

	private List<CategoriaOpzioniServizio> categorieOpzioni = new ArrayList<CategoriaOpzioniServizio>();

	@OneToMany(mappedBy = "servizio")
	@OrderBy("nome")
	public List<CategoriaOpzioniServizio> getCategorieOpzioni() {
		return this.categorieOpzioni;
	}

	@Id
	public String getId() {
		return this.id;
	}

	public String getNome() {
		return this.nome;
	}

	public Integer getPosizione() {
		return this.posizione;
	}

	public String getUrl() {
		return this.url;
	}

	public String getUserPrefsUrl() {
		return this.userPrefsUrl;
	}

	public void setCategorieOpzioni(List<CategoriaOpzioniServizio> categorieOpzioni) {
		this.categorieOpzioni = categorieOpzioni;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPosizione(Integer posizione) {
		this.posizione = posizione;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUserPrefsUrl(String userPrefsUrl) {
		this.userPrefsUrl = userPrefsUrl;
	}

	@Override
	public String toString() {
		return this.nome;
	}
}
