package it.drwolf.alerting.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EsitoSegnalazione {
	private Integer id;
	private String esito;

	public String getEsito() {
		return this.esito;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return this.id;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
