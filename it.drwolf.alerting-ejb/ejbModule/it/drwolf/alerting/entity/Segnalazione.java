package it.drwolf.alerting.entity;

// Generated 17-giu-2008 14.53.44 by Hibernate Tools 3.2.1.GA

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.jboss.envers.Versioned;

/**
 * Segnalazione generated by hbm2java
 */
@Entity
@Table(name = "Segnalazione", catalog = "segnalazioni")
public class Segnalazione implements java.io.Serializable {

	private static final long serialVersionUID = 2973871102772058213L;

	private Integer id;

	private Boolean noreply = false;

	private Boolean notificatoRitardo = false;

	private CanaleSegnalazione canaleSegnalazione;

	private CanaleSegnalazione canaleRisposta;

	private SottotipoSegnalazione sottotipoSegnalazione;

	private Cittadino cittadino;

	private String idutenteInseritore;

	private String via;

	private EsitoSegnalazione esitoSegnalazione;

	private String ubicazione;

	private String civico;
	private String localita;
	private String comune;
	private Stato stato;
	private Utenza utenza;

	private CategoriaUtenza categoriaUtenza;

	private SottocategoriaUtenza sottocategoriaUtenza;

	private String referente;

	private String telefonoReferente;

	private BPMInfo bpmInfo = new BPMInfo();

	private List<Intervento> interventos = new ArrayList<Intervento>(0);

	private String oggetto;

	private Set<Sollecito> solleciti = new TreeSet<Sollecito>();

	// Apertura
	private Date data;

	private String messaggio;

	private Date scadenza;
	private Date chiusura;

	private List<Risposta> risposte;

	public Segnalazione() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Segnalazione)) {
			return false;
		}
		Segnalazione other = (Segnalazione) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public BPMInfo getBpmInfo() {
		return this.bpmInfo;
	}

	@ManyToOne
	public CanaleSegnalazione getCanaleRisposta() {
		return this.canaleRisposta;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCanale")
	public CanaleSegnalazione getCanaleSegnalazione() {
		return this.canaleSegnalazione;
	}

	@ManyToOne
	public CategoriaUtenza getCategoriaUtenza() {
		return this.categoriaUtenza;
	}

	@Temporal(TemporalType.DATE)
	public Date getChiusura() {
		return this.chiusura;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCittadino")
	public Cittadino getCittadino() {
		return this.cittadino;
	}

	@Column(name = "Civico")
	public String getCivico() {
		return this.civico;
	}

	@Column(name = "Comune")
	public String getComune() {
		return this.comune;
	}

	public Date getData() {
		return this.data;
	}

	@ManyToOne
	public EsitoSegnalazione getEsitoSegnalazione() {
		return this.esitoSegnalazione;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	@Column(name = "IDUtenteInseritore")
	public String getIdutenteInseritore() {
		return this.idutenteInseritore;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "segnalazione")
	@OrderBy("scadenza")
	public List<Intervento> getInterventos() {
		return this.interventos;
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

	public Boolean getNoreply() {
		return this.noreply;
	}

	public Boolean getNotificatoRitardo() {
		return this.notificatoRitardo;
	}

	public String getOggetto() {
		return this.oggetto;
	}

	public String getReferente() {
		return this.referente;
	}

	@OneToMany(mappedBy = "segnalazione")
	@OrderBy("data desc, id desc")
	public List<Risposta> getRisposte() {
		return this.risposte;
	}

	@Temporal(TemporalType.DATE)
	public Date getScadenza() {
		return this.scadenza;
	}

	@OneToMany(mappedBy = "segnalazione", cascade = CascadeType.ALL)
	public Set<Sollecito> getSolleciti() {
		return this.solleciti;
	}

	@Transient
	public List<Sollecito> getSollecitiList() {
		return new ArrayList<Sollecito>(this.getSolleciti());
	}

	@Transient
	public List<Sollecito> getSollecitiSenzaRisposta() {
		List<Sollecito> l = this.getSollecitiList();
		Iterator<Sollecito> i = l.iterator();
		while (i.hasNext()) {
			if (i.next().getRisposta() != null) {
				i.remove();
			}
		}
		return l;
	}

	@ManyToOne
	public SottocategoriaUtenza getSottocategoriaUtenza() {
		return this.sottocategoriaUtenza;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idSottotipoSegnalazione")
	public SottotipoSegnalazione getSottotipoSegnalazione() {
		return this.sottotipoSegnalazione;
	}

	@ManyToOne
	public Stato getStato() {
		return this.stato;
	}

	public String getTelefonoReferente() {
		return this.telefonoReferente;
	}

	@Lob
	public String getUbicazione() {
		return this.ubicazione;
	}

	@ManyToOne
	public Utenza getUtenza() {
		return this.utenza;
	}

	@Column(name = "Via")
	public String getVia() {
		return this.via;
	}

	@Override
	public int hashCode() {
		final int prime = 13231;
		int result = 1;
		result = prime * result + (this.id == null ? 0 : this.id.hashCode());
		return result;
	}

	public void setBpmInfo(BPMInfo bpmInfo) {
		this.bpmInfo = bpmInfo;
	}

	public void setCanaleRisposta(CanaleSegnalazione canaleRisposta) {
		this.canaleRisposta = canaleRisposta;
	}

	public void setCanaleSegnalazione(CanaleSegnalazione canaleSegnalazione) {
		this.canaleSegnalazione = canaleSegnalazione;
	}

	public void setCategoriaUtenza(CategoriaUtenza categoriaUtenza) {
		this.categoriaUtenza = categoriaUtenza;
	}

	public void setChiusura(Date chiusura) {
		this.chiusura = chiusura;
	}

	public void setCittadino(Cittadino cittadino) {
		this.cittadino = cittadino;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setEsitoSegnalazione(EsitoSegnalazione esitoSegnalazione) {
		this.esitoSegnalazione = esitoSegnalazione;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIdutenteInseritore(String idutenteInseritore) {
		this.idutenteInseritore = idutenteInseritore;
	}

	public void setInterventos(List<Intervento> interventos) {
		this.interventos = interventos;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public void setNoreply(Boolean noreply) {
		this.noreply = noreply != null ? noreply : false;
	}

	public void setNotificatoRitardo(Boolean notificatoRitardo) {
		this.notificatoRitardo = notificatoRitardo;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public void setReferente(String referente) {
		this.referente = referente;
	}

	public void setRisposte(List<Risposta> risposte) {
		this.risposte = risposte;
	}

	public void setScadenza(Date scadenza) {
		this.scadenza = scadenza;
	}

	public void setSolleciti(Set<Sollecito> solleciti) {
		this.solleciti = solleciti;
	}

	public void setSottocategoriaUtenza(
			SottocategoriaUtenza sottocategoriaUtenza) {
		this.sottocategoriaUtenza = sottocategoriaUtenza;
	}

	public void setSottotipoSegnalazione(
			SottotipoSegnalazione sottotipoSegnalazione) {
		this.sottotipoSegnalazione = sottotipoSegnalazione;
	}

	public void setStato(Stato stato) {
		this.stato = stato;
	}

	public void setTelefonoReferente(String telefonoReferente) {
		this.telefonoReferente = telefonoReferente;
	}

	public void setUbicazione(String ubicazione) {
		this.ubicazione = ubicazione;
	}

	public void setUtenza(Utenza utenza) {
		this.utenza = utenza;
	}

	public void setVia(String via) {
		this.via = via;
	}

	@Override
	public String toString() {
		return this.getId() + " - " + this.getOggetto();
	}

}