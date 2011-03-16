package it.drwolf.alerting.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class SottocategoriaUtenza {
	private Integer id;
	private String nome;
	private CategoriaUtenza categoriaUtenza;
	private List<Utenza> utenze = new ArrayList<Utenza>(0);

	@ManyToOne
	public CategoriaUtenza getCategoriaUtenza() {
		return this.categoriaUtenza;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return this.id;
	}

	public String getNome() {
		return this.nome;
	}

	@OneToMany(mappedBy = "sottocategoriaUtenza", fetch = FetchType.EAGER)
	public List<Utenza> getUtenze() {
		return this.utenze;
	}

	public void setCategoriaUtenza(CategoriaUtenza categoriaUtenza) {
		this.categoriaUtenza = categoriaUtenza;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setUtenze(List<Utenza> utenze) {
		this.utenze = utenze;
	}

	@Override
	public String toString() {
		return this.getCategoriaUtenza() + ": " + this.nome;
	}
}
