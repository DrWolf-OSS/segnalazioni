package it.drwolf.alerting.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Giornaliero {
	private Integer id;

	private Date data;

	private String note;

	private List<Intervento> interventi = new ArrayList<Intervento>();

	public Date getData() {
		return this.data;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return this.id;
	}

	@ManyToMany
	public List<Intervento> getInterventi() {
		return this.interventi;
	}

	public String getNote() {
		return this.note;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInterventi(List<Intervento> interventi) {
		this.interventi = interventi;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
