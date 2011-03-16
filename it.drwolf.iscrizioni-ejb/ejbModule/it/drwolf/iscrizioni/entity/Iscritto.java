package it.drwolf.iscrizioni.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.jboss.envers.Versioned;

@Entity
@Table(catalog = "iscrizioni")
public class Iscritto {

	private String id;
	private String email;
	private String nome;
	private String cognome;
	private String cellulare;
	private String indirizzo;
	private String ragioneSociale;
	private String cap;
	private String localita;
	private String telefono;
	private String fax;
	private boolean textMail = false;

	private List<Gruppo> gruppi = new ArrayList<Gruppo>();
	private List<OpzioneServizio> opzioniServizi = new ArrayList<OpzioneServizio>(
			0);

	private Boolean consenso = false;

	private Boolean confermato = false;

	private List<Servizio> servizi = new ArrayList<Servizio>(0);

	public Iscritto() {

	}

	public Iscritto(String id, String email, String nome, String cognome,
			String cellulare, String indirizzo, boolean textMail,
			Boolean consenso, Boolean confermato) {
		super();
		this.id = id;
		this.email = email;
		this.nome = nome;
		this.cognome = cognome;
		this.cellulare = cellulare;
		this.indirizzo = indirizzo;
		this.textMail = textMail;
		this.consenso = consenso;
		this.confermato = confermato;
	}

	@Versioned
	public String getCap() {
		return cap;
	}

	@Versioned
	@Column(unique = true)
	public String getCellulare() {
		return cellulare;
	}

	@Versioned
	public String getCognome() {
		return cognome;
	}

	@Versioned
	public Boolean getConfermato() {
		return confermato;
	}

	@Versioned
	public Boolean getConsenso() {
		return consenso;
	}

	@Versioned
	@Column(unique = true)
	public String getEmail() {
		return email;
	}

	@Versioned
	public String getFax() {
		return fax;
	}

	@ManyToMany(mappedBy = "iscritti", cascade = { CascadeType.MERGE,
			CascadeType.PERSIST })
	@OrderBy("nome")
	public List<Gruppo> getGruppi() {
		return gruppi;
	}

	@Id
	public String getId() {
		return id;
	}

	@Versioned
	public String getIndirizzo() {
		return indirizzo;
	}

	@Versioned
	public String getLocalita() {
		return localita;
	}

	@Versioned
	public String getNome() {
		return nome;
	}

	@ManyToMany
	public List<OpzioneServizio> getOpzioniServizi() {
		return opzioniServizi;
	}

	@Versioned
	public String getRagioneSociale() {
		return ragioneSociale;
	}

	@ManyToMany
	public List<Servizio> getServizi() {
		return servizi;
	}

	@Versioned
	public String getTelefono() {
		return telefono;
	}

	@Versioned
	public boolean isTextMail() {
		return textMail;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public void setCellulare(String cellulare) {
		if (cellulare == null) {
			this.cellulare = null;
		} else {
			this.cellulare = cellulare.replaceAll("\\D", "");
		}
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public void setConfermato(Boolean confermato) {
		this.confermato = confermato;
	}

	public void setConsenso(Boolean consenso) {
		this.consenso = consenso;
	}

	public void setEmail(String email) {
		if (email != null && "".equals(email.trim())) {
			email = null;
		}
		this.email = email;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public void setGruppi(List<Gruppo> gruppi) {
		this.gruppi = gruppi;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setOpzioniServizi(List<OpzioneServizio> opzioniServizi) {
		this.opzioniServizi = opzioniServizi;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public void setServizi(List<Servizio> servizi) {
		this.servizi = servizi;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setTextMail(boolean textMail) {
		this.textMail = textMail;
	}

	@Override
	public String toString() {
		if (cognome != null) {
			return nome + " " + cognome;
		} else if (email != null) {
			return email;
		}
		return id;
	}
}
