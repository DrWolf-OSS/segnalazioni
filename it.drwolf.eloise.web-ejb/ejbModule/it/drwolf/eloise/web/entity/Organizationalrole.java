package it.drwolf.eloise.web.entity;

// Generated 17-giu-2008 14.55.38 by Hibernate Tools 3.2.0.CR1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.NotNull;

/**
 * Organizationalrole generated by hbm2java
 */
@Entity
@Table(name = "`OrganizationalRole`", catalog = "eloise")
public class Organizationalrole implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2054428227193171008L;
	private Integer organizationalRoleId;
	private Roletype roletype;
	private Ufficio ufficio;
	private Ente ente;
	private Area area;
	private Set<People> peoples = new HashSet<People>(0);

	public Organizationalrole() {
	}

	public Organizationalrole(Roletype roletype, Ente ente) {
		this.roletype = roletype;
		this.ente = ente;
	}

	public Organizationalrole(Roletype roletype, Ente ente, Area area) {
		this.roletype = roletype;
		this.ente = ente;
		this.area = area;
	}

	public Organizationalrole(Roletype roletype, Ente ente, Area area,
			Ufficio ufficio) {
		this.roletype = roletype;
		this.ente = ente;
		this.area = area;
		this.ufficio = ufficio;
	}

	public Organizationalrole(Roletype roletype, Ufficio ufficio, Ente ente,
			Area area, Set<People> peoples) {
		this.roletype = roletype;
		this.ufficio = ufficio;
		this.ente = ente;
		this.area = area;
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
		if (!(obj instanceof Organizationalrole)) {
			return false;
		}
		Organizationalrole other = (Organizationalrole) obj;
		if (this.organizationalRoleId == null) {
			if (other.organizationalRoleId != null) {
				return false;
			}
		} else if (!this.organizationalRoleId
				.equals(other.organizationalRoleId)) {
			return false;
		}
		return true;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDSettore")
	public Area getArea() {
		return this.area;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDEnte", nullable = false)
	@NotNull
	public Ente getEnte() {
		return this.ente;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getOrganizationalRoleId() {
		return this.organizationalRoleId;
	}

	@Transient
	public String getPeopleAsString() {
		String stringa = new String();
		for (People people : this.getPeoples()) {
			stringa += people + ", ";
		}
		if ((stringa != null) && (stringa.length() > 2)) {
			stringa = stringa.substring(0, stringa.length() - 2);

		}
		return stringa;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "organizationalroles")
	public Set<People> getPeoples() {
		return this.peoples;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RoleTypeID", nullable = false)
	@NotNull
	public Roletype getRoletype() {
		return this.roletype;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDUfficio")
	public Ufficio getUfficio() {
		return this.ufficio;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	public void setOrganizationalRoleId(Integer organizationalRoleId) {
		this.organizationalRoleId = organizationalRoleId;
	}

	public void setPeoples(Set<People> peoples) {
		this.peoples = peoples;
	}

	public void setRoletype(Roletype roletype) {
		this.roletype = roletype;
	}

	public void setUfficio(Ufficio ufficio) {
		this.ufficio = ufficio;
	}

	@Override
	public String toString() {
		return this.getRoletype() + " - " + this.getUfficio();
	}
}
