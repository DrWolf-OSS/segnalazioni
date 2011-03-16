package it.drwolf.alerting.entity;

// Generated 17-giu-2008 14.53.44 by Hibernate Tools 3.2.1.GA

import static javax.persistence.GenerationType.IDENTITY;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.jboss.envers.Versioned;

@Entity
@Table(name = "Intervento", catalog = "segnalazioni")
@EntityListeners(Intervento.Listener.class)
public class Intervento implements java.io.Serializable {

	private boolean cambiaStato = false;

	private static final long serialVersionUID = -2134570531057905624L;

	private Integer id;

	private Segnalazione segnalazione;

	private SottotipoIntervento sottotipoIntervento;

	private BPMInfo bpmInfo = new BPMInfo();

	private Cittadino segnalatore;

	private String oggetto;;

	private String descrizione;

	private CodiceTriage codiceTriage;

	private String noteSegnalatore;

	private String via;

	private String civico;

	private String localita;

	private String comune;

	private String messaggio;

	private Stato stato;

	private Utenza utenza;

	private SquadraIntervento squadraIntervento;

	private String nomeReferente;

	private String telefonoReferente;

	private Date apertura = new Date();

	private Date scadenza;

	private Date chiusura;

	private String esito;

	private List<OreLavorate> oreLavorate = new ArrayList<OreLavorate>(9);

	public Intervento() {

	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getApertura() {
		return this.apertura;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public BPMInfo getBpmInfo() {
		return this.bpmInfo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getChiusura() {
		return this.chiusura;
	}

	@Column(name = "Civico")
	public String getCivico() {
		return this.civico;
	}

	@ManyToOne
	public CodiceTriage getCodiceTriage() {
		return this.codiceTriage;
	}

	@Column(name = "Comune")
	public String getComune() {
		return this.comune;
	}

	@Transient
	public String getDatePattern() {
		if (this.getCodiceTriage() == null
				|| this.getCodiceTriage().getId().equals("nero")) {
			return "dd/MM/yyyy HH:mm";
		}
		return "dd/MM/yyyy";
	}

	@Lob
	public String getDescrizione() {
		return this.descrizione;
	}

	@Lob
	public String getEsito() {
		return this.esito;
	}

	@Transient
	public String getFormattedScadenza() {
		if (this.getScadenza() == null) {
			return null;
		}
		return new SimpleDateFormat(this.getDatePattern()).format(this
				.getScadenza());
	}

	@Transient
	public String getGestore() {
		if (this.sottotipoIntervento == null) {
			return null;
		}
		return "gestore."
				+ this.sottotipoIntervento.getTipoIntervento().getId();
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	@Transient
	public String getIndirizzo() {
		String i = "";
		for (String p : Arrays.asList(new String[] { this.getVia(),
				this.getCivico(), this.getLocalita() })) {
			if (p != null && p.trim() != "") {
				i += p + " ";
			}
		}
		return i;
	}

	@Column(name = "Localita")
	public String getLocalita() {
		return this.localita;
	}

	@Lob
	@Versioned
	public String getMessaggio() {
		return this.messaggio;
	}

	public String getNomeReferente() {
		return this.nomeReferente;
	}

	@Column(name = "NoteSegnalatore")
	public String getNoteSegnalatore() {
		return this.noteSegnalatore;
	}

	@Column(name = "Oggetto")
	public String getOggetto() {
		return this.oggetto;
	}

	@OneToMany(mappedBy = "intervento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OrderBy("data")
	public List<OreLavorate> getOreLavorate() {
		return this.oreLavorate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getScadenza() {
		return this.scadenza;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCittadino")
	public Cittadino getSegnalatore() {
		return this.segnalatore;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idSegnalazione", nullable = true)
	public Segnalazione getSegnalazione() {
		return this.segnalazione;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idSottotipoIntervento")
	public SottotipoIntervento getSottotipoIntervento() {
		return this.sottotipoIntervento;
	}

	@ManyToOne
	public SquadraIntervento getSquadraIntervento() {
		return this.squadraIntervento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Stato getStato() {
		return this.stato;
	}

	public String getTelefonoReferente() {
		return this.telefonoReferente;
	}

	@ManyToOne
	public Utenza getUtenza() {
		return this.utenza;
	}

	@Column(name = "Via")
	public String getVia() {
		return this.via;
	}

	@Transient
	public boolean isCambiaStato() {
		return this.cambiaStato;
	}

	public void setApertura(Date apertura) {
		this.apertura = apertura;
	}

	public void setBpmInfo(BPMInfo bpmInfo) {
		this.bpmInfo = bpmInfo;
	}

	public void setCambiaStato(boolean cambiaStato) {
		this.cambiaStato = cambiaStato;
	}

	public void setChiusura(Date chiusura) {
		this.chiusura = chiusura;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public void setCodiceTriage(CodiceTriage codiceTriage) {
		this.codiceTriage = codiceTriage;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public void setNomeReferente(String nomeReferente) {
		this.nomeReferente = nomeReferente;
	}

	public void setNoteSegnalatore(String noteSegnalatore) {
		this.noteSegnalatore = noteSegnalatore;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public void setOreLavorate(List<OreLavorate> oreLavorate) {
		this.oreLavorate = oreLavorate;
	}

	public void setScadenza(Date scadenza) {
		this.scadenza = scadenza;
	}

	public void setSegnalatore(Cittadino segnalatore) {
		this.segnalatore = segnalatore;
	}

	public void setSegnalazione(Segnalazione segnalazione) {
		this.segnalazione = segnalazione;
	}

	public void setSottotipoIntervento(SottotipoIntervento sottotipoIntervento) {
		this.sottotipoIntervento = sottotipoIntervento;
	}

	public void setSquadraIntervento(SquadraIntervento squadraIntervento) {
		this.squadraIntervento = squadraIntervento;
	}

	public void setStato(Stato stato) {
		this.stato = stato;
	}

	public void setTelefonoReferente(String telefonoReferente) {
		this.telefonoReferente = telefonoReferente;
	}

	public void setUtenza(Utenza utenza) {
		this.utenza = utenza;
	}

	public void setVia(String via) {
		this.via = via;
	}

	@Override
	public String toString() {
		return this.oggetto;
	}

	public static class Listener {
		@PrePersist
		@PreUpdate
		public void update(Intervento intervento) { 
			if (intervento.getStato().getNome().equals("chiuso") && intervento.getChiusura() == null) {
				intervento.setChiusura(new Date());
			}
		}
	}
}
