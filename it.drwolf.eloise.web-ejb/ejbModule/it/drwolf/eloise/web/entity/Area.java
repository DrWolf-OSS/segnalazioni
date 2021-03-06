package it.drwolf.eloise.web.entity;

// Generated 17-giu-2008 14.55.38 by Hibernate Tools 3.2.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.NotNull;

/**
 * Area generated by hbm2java
 */
@Entity
@Table(name = "`Settore`", catalog = "eloise")
public class Area implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1077850156022903264L;
	private Integer idsettore;
	private Ente ente;
	private Boolean visible;
	private String nome;
	private Set<Organizationalrole> organizationalroles = new HashSet<Organizationalrole>(
			0);
	private Set<Ufficio> ufficios = new HashSet<Ufficio>(0);
	private Set<People> peoples = new HashSet<People>(0);

	public Area() {
	}

	public Area(Ente ente) {
		this.ente = ente;
	}

	public Area(Ente ente, Boolean visible, String nome,
			Set<Organizationalrole> organizationalroles, Set<Ufficio> ufficios,
			Set<People> peoples) {
		this.ente = ente;
		this.visible = visible;
		this.nome = nome;
		this.organizationalroles = organizationalroles;
		this.ufficios = ufficios;
		this.peoples = peoples;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Area)) {
			return false;
		}
		Area other = (Area) obj;
		if (this.idsettore == null) {
			if (other.getIdsettore() != null) {
				return false;
			}
		} else if (!this.idsettore.equals(other.getIdsettore())) {
			return false;
		}
		return true;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDEnte", nullable = false)
	@NotNull
	public Ente getEnte() {
		return this.ente;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "IDSettore", unique = true, nullable = false)
	public Integer getIdsettore() {
		return this.idsettore;
	}

	@Column(name = "Nome")
	public String getNome() {
		return this.nome;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "area")
	public Set<Organizationalrole> getOrganizationalroles() {
		return this.organizationalroles;
	}

	@Transient
	public List<Organizationalrole> getOrganizationalrolesAsList() {
		return new ArrayList<Organizationalrole>(this.getOrganizationalroles());

	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "`People2Settore`", joinColumns = { @JoinColumn(name = "IDSettore", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "IDPeople", nullable = false, updatable = false) })
	public Set<People> getPeoples() {
		return this.peoples;
	}

	@Transient
	public List<People> getPeoplesAsList() {
		return new ArrayList<People>(this.getPeoples());

	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "area")
	public Set<Ufficio> getUfficios() {
		return this.ufficios;
	}

	@Transient
	public List<Ufficio> getUfficiosAsList() {
		return new ArrayList<Ufficio>(this.getUfficios());
	}

	@Column(name = "visible")
	public Boolean getVisible() {
		return this.visible;
	}

	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	public void setIdsettore(Integer idsettore) {
		this.idsettore = idsettore;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setOrganizationalroles(
			Set<Organizationalrole> organizationalroles) {
		this.organizationalroles = organizationalroles;
	}

	@Transient
	public void setOrganizationalrolesAsList(
			List<Organizationalrole> organizationalroles) {
		this.setOrganizationalroles(new HashSet<Organizationalrole>(
				organizationalroles));
	}

	public void setPeoples(Set<People> peoples) {
		this.peoples = peoples;
	}

	@Transient
	public void setPeoplesAsList(List<People> peoples) {
		this.setPeoples(new HashSet<People>(peoples));
	}

	public void setUfficios(Set<Ufficio> ufficios) {
		this.ufficios = ufficios;
	}

	@Transient
	public void setUfficiosAsList(List<Ufficio> ufficios) {
		this.setUfficios(new HashSet<Ufficio>(ufficios));
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	@Override
	public String toString() {
		return this.getNome();
	}
}
