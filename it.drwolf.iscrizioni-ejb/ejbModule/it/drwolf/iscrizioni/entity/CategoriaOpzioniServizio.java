package it.drwolf.iscrizioni.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(catalog = "iscrizioni")
public class CategoriaOpzioniServizio {

	private String id;
	private String nome;
	private Boolean multiple = true;

	private Servizio servizio;

	private List<OpzioneServizio> opzioniServizio4Iscritto = new ArrayList<OpzioneServizio>(
			0);

	private List<OpzioneServizio> opzioniServizio = new ArrayList<OpzioneServizio>(
			0);

	private OpzioneServizio opzioneServizio4Iscritto;

	@Id
	public String getId() {
		return id;
	}

	public Boolean getMultiple() {
		return multiple;
	}

	public String getNome() {
		return nome;
	}

	@Transient
	public OpzioneServizio getOpzioneServizio4Iscritto() {
		return opzioneServizio4Iscritto;
	}

	@OneToMany(mappedBy = "categoriaOpzioniServizio")
	public List<OpzioneServizio> getOpzioniServizio() {
		return opzioniServizio;
	}

	@Transient
	public List<OpzioneServizio> getOpzioniServizio4Iscritto() {
		return opzioniServizio4Iscritto;
	}

	@ManyToOne
	public Servizio getServizio() {
		return servizio;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMultiple(Boolean multiple) {
		this.multiple = multiple;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setOpzioneServizio4Iscritto(
			OpzioneServizio opzioneServizio4Iscritto) {
		this.opzioneServizio4Iscritto = opzioneServizio4Iscritto;
	}

	public void setOpzioniServizio(List<OpzioneServizio> opzioniServizio) {
		this.opzioniServizio = opzioniServizio;
	}

	public void setOpzioniServizio4Iscritto(
			List<OpzioneServizio> opzioniServizio4Iscritto) {
		this.opzioniServizio4Iscritto = opzioniServizio4Iscritto;
	}

	public void setServizio(Servizio servizio) {
		this.servizio = servizio;
	}
}
