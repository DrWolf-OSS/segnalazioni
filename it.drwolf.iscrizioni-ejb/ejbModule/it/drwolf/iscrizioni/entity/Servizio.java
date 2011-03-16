package it.drwolf.iscrizioni.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(catalog = "iscrizioni")
public class Servizio {
	private String id;
	private String nome;
	private String url;
	private String userPrefsUrl;

	private List<CategoriaOpzioniServizio> categorieOpzioni = new ArrayList<CategoriaOpzioniServizio>();

	@OneToMany(mappedBy = "servizio")
	@OrderBy("nome")
	public List<CategoriaOpzioniServizio> getCategorieOpzioni() {
		return categorieOpzioni;
	}

	@Id
	public String getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getUrl() {
		return url;
	}

	public String getUserPrefsUrl() {
		return userPrefsUrl;
	}

	public void setCategorieOpzioni(
			List<CategoriaOpzioniServizio> categorieOpzioni) {
		this.categorieOpzioni = categorieOpzioni;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUserPrefsUrl(String userPrefsUrl) {
		this.userPrefsUrl = userPrefsUrl;
	}

	@Override
	public String toString() {
		return nome;
	}
}
