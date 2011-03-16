package it.drwolf.alerting.entity;

import it.drwolf.eloise.web.entity.Ufficio;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.validator.NotNull;
import org.jboss.seam.Component;

@Entity
public class UfficioCompetente {
	private Integer id;
	private Integer eloiseId;
	private List<String> gestori;

	private Boolean alert = false;

	public Boolean getAlert() {
		return this.alert;
	}

	@NotNull
	public Integer getEloiseId() {
		return this.eloiseId;
	}

	@CollectionOfElements
	public List<String> getGestori() {
		return this.gestori;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return this.id;
	}

	@Transient
	public String getNome() {
		try {
			return ((EntityManager) Component.getInstance("entityManager"))
					.find(Ufficio.class, this.getEloiseId()).getNome();
		} catch (Exception e) {
			return "--";
		}
	}

	public void setAlert(Boolean alert) {
		this.alert = alert != null ? alert : false;
	}

	public void setEloiseId(Integer eloiseId) {
		this.eloiseId = eloiseId;
	}

	public void setGestori(List<String> smistatori) {
		this.gestori = smistatori;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
