package it.drwolf.alerting.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.validator.NotNull;

@Entity
public class OreLavorate {
	private Integer id;
	private Date data = new Date();
	private List<String> componenti;
	private Double ore;
	private Intervento intervento;

	@CollectionOfElements
	public List<String> getComponenti() {
		return this.componenti;
	}

	public Date getData() {
		return this.data;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return this.id;
	}

	@ManyToOne
	@NotNull
	@JoinColumn(nullable = false)
	public Intervento getIntervento() {
		return this.intervento;
	}

	public Double getOre() {
		return this.ore;
	}

	public void setComponenti(List<String> componenti) {
		this.componenti = componenti;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIntervento(Intervento intervento) {
		this.intervento = intervento;
	}

	public void setOre(Double ore) {
		this.ore = ore;
	}
}
