package it.drwolf.iscrizioni.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
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

	@Override
	public boolean equals(Object obj) {
		try {
			return ((Iscritto) obj).getId().equals(this.getId());
		} catch (Exception e) {
			return false;
		}
	}

	@Versioned
	public String getCap() {
		return this.cap;
	}

	@Versioned
	@Column(unique = true)
	public String getCellulare() {
		return this.cellulare;
	}

	@Versioned
	public String getCognome() {
		return this.cognome;
	}

	@Versioned
	public Boolean getConfermato() {
		return this.confermato;
	}

	@Versioned
	public Boolean getConsenso() {
		return this.consenso;
	}

	@Versioned
	@Column(unique = true)
	public String getEmail() {
		return this.email;
	}

	@Versioned
	public String getFax() {
		return this.fax;
	}

	@ManyToMany(mappedBy = "iscritti", cascade = { CascadeType.MERGE,
			CascadeType.PERSIST })
	@OrderBy("nome")
	public List<Gruppo> getGruppi() {
		return this.gruppi;
	}

	@Id
	public String getId() {
		return this.id;
	}

	@Versioned
	public String getIndirizzo() {
		return this.indirizzo;
	}

	@Versioned
	public String getLocalita() {
		return this.localita;
	}

	@Versioned
	public String getNome() {
		return this.nome;
	}

	@ManyToMany
	@JoinTable(catalog = "iscrizioni")
	public List<OpzioneServizio> getOpzioniServizi() {
		return this.opzioniServizi;
	}

	@Versioned
	public String getRagioneSociale() {
		return this.ragioneSociale;
	}

	@ManyToMany
	@JoinTable(catalog = "iscrizioni")
	public List<Servizio> getServizi() {
		return this.servizi;
	}

	@Versioned
	public String getTelefono() {
		return this.telefono;
	}

	@Override
	public int hashCode() {
		return ("" + this.getId()).hashCode();
	}

	@Versioned
	public boolean isTextMail() {
		return this.textMail;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public void setCellulare(String cellulare) {
		if (cellulare == null) {
			this.cellulare = null;
		} else {
			this.cellulare = cellulare.replaceAll("\\D", "");
			if (this.cellulare.equals("")) {
				this.cellulare = null;
			}
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
		if (this.cognome != null && this.email != null
				&& !this.email.trim().equals("")) {
			return String.format("%s %s (%s)", this.nome, this.cognome,
					this.email);
		} else if (this.cognome != null) {
			return String.format("%s %s", this.nome, this.cognome);
		} else if (this.email != null && !this.email.trim().equals("")) {
			return this.email;
		}
		return this.id;
	}
}
