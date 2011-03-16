package it.drwolf.alerting.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CollectionOfElements;

@Entity
public class SquadraIntervento {
	private Integer id;
	private String nome;
	private List<String> componenti;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof SquadraIntervento) {
			return false;
		}
		SquadraIntervento other = (SquadraIntervento) obj;
		if (this.getNome() == null) {
			if (other.getNome() != null) {
				return false;
			}
		} else if (!this.getNome().equals(other.getNome())) {
			return false;
		}
		return true;
	}

	@CollectionOfElements
	public List<String> getComponenti() {
		return this.componenti;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return this.id;
	}

	public String getNome() {
		return this.nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.getNome() == null) ? 0 : this.getNome().hashCode());
		return result;
	}

	public void setComponenti(List<String> componenti) {
		this.componenti = componenti;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return this.nome;
	}

}
