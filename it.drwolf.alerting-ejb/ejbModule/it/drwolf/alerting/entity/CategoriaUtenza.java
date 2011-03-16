package it.drwolf.alerting.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CategoriaUtenza {

	private Integer id;
	private String nome;
	private List<SottocategoriaUtenza> sottocategorie = new ArrayList<SottocategoriaUtenza>(
			0);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return this.id;
	}

	public String getNome() {
		return this.nome;
	}

	@OneToMany(mappedBy = "categoriaUtenza")
	public List<SottocategoriaUtenza> getSottocategorie() {
		return this.sottocategorie;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setSottocategorie(List<SottocategoriaUtenza> sottocategorie) {
		this.sottocategorie = sottocategorie;
	}

	@Override
	public String toString() {
		return this.nome;
	}

}
