package it.drwolf.iscrizioni.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(catalog = "iscrizioni")
public class Gruppo {

	private String id;
	private String nome;
	private List<Iscritto> iscritti = new ArrayList<Iscritto>();

	@Id
	public String getId() {
		return this.id;
	}

	@ManyToMany
	@OrderBy("cognome, nome, email")
	public List<Iscritto> getIscritti() {
		return this.iscritti;
	}

	public String getNome() {
		return this.nome;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIscritti(List<Iscritto> iscritti) {
		this.iscritti = iscritti;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
