package it.drwolf.alerting.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.jboss.seam.annotations.Name;

@Entity
@Name("sollecito")
@Table(name = "Sollecito", catalog = "segnalazioni")
public class Sollecito implements Comparable<Sollecito> {

	private Integer id;
	private Segnalazione segnalazione;
	private Integer idSegnalazione;
	private Date data = new Date();
	private Date dataRisposta;
	private String testo;
	private String risposta;
	private String idinseritore;
	private String idassegnatario;

	public int compareTo(Sollecito o) {
		return o.getData().compareTo(this.getData());
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getData() {
		return this.data;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataRisposta() {
		return this.dataRisposta;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return this.id;
	}

	public String getIdassegnatario() {
		return this.idassegnatario;
	}

	public String getIdinseritore() {
		return this.idinseritore;
	}

	@Transient
	public Integer getIdSegnalazione() {
		return this.idSegnalazione;
	}

	@Lob
	public String getRisposta() {
		return this.risposta;
	}

	@ManyToOne(optional = false)
	public Segnalazione getSegnalazione() {
		return this.segnalazione;
	}

	@Lob
	public String getTesto() {
		return this.testo;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setDataRisposta(Date dataRisposta) {
		this.dataRisposta = dataRisposta;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIdassegnatario(String idassegnatario) {
		this.idassegnatario = idassegnatario;
	}

	public void setIdinseritore(String idinseritore) {
		this.idinseritore = idinseritore;
	}

	public void setIdSegnalazione(Integer idSegnalazione) {
		this.idSegnalazione = idSegnalazione;
	}

	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}

	public void setSegnalazione(Segnalazione segnalazione) {
		this.segnalazione = segnalazione;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}
}
