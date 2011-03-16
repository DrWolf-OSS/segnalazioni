package it.drwolf.eloise.web.entity;

// Generated 17-giu-2008 14.55.38 by Hibernate Tools 3.2.0.CR1

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;

/**
 * People generated by hbm2java
 */

@Entity
@Table(name = "`People`", catalog = "eloise")
public class People implements java.io.Serializable {

	private String idpeople;
	private Sede sede;
	private Orario orario;
	private String nome;
	private String cognome;
	private String password;
	private String telefono;
	private String fax;
	private String email;
	private String profiloProfessionale;
	private String telefonoPrivato;
	private String fax1;
	private String email1;
	private String PEC;
	private String descrizione;
	private String cellulare;
	private String numeroBreve;
	private Boolean visible;
	private Set<Ufficio> ufficios = new HashSet<Ufficio>(0);
	private Set<Ente> entes = new HashSet<Ente>(0);
	private Set<Organizationalrole> organizationalroles = new HashSet<Organizationalrole>(
			0);
	private Set<Area> areas = new HashSet<Area>(0);

	public People() {
	}

	public People(String idpeople) {
		this.idpeople = idpeople;
	}

	public People(String idpeople, Sede sede, Orario orario, String nome,
			String cognome, String password, String telefono, String fax,
			String email, String profiloProfessionale, String telefonoPrivato,
			String fax1, String email1, String cellulare, String numeroBreve,
			Boolean visible, Set<Ufficio> ufficios, Set<Ente> entes,
			Set<Organizationalrole> organizationalroles, Set<Area> areas) {
		this.idpeople = idpeople;
		this.sede = sede;
		this.orario = orario;
		this.nome = nome;
		this.cognome = cognome;
		this.password = password;
		this.telefono = telefono;
		this.fax = fax;
		this.email = email;
		this.profiloProfessionale = profiloProfessionale;
		this.telefonoPrivato = telefonoPrivato;
		this.fax1 = fax1;
		this.email1 = email1;
		this.cellulare = cellulare;
		this.numeroBreve = numeroBreve;
		this.visible = visible;
		this.ufficios = ufficios;
		this.entes = entes;
		this.organizationalroles = organizationalroles;
		this.areas = areas;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "peoples")
	public Set<Area> getAreas() {
		return this.areas;
	}

	@Column(name = "Cellulare", length = 20)
	@Length(max = 20)
	public String getCellulare() {
		return this.cellulare;
	}

	@Column(name = "Cognome")
	public String getCognome() {
		return this.cognome;
	}

	@Column(name = "Email")
	@Pattern(regex="(^$)|(^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$)", message="Non è una email valida")
	public String getEmail() {
		return this.email;
	}

	@Column(name = "Email1")
	@Pattern(regex="(^$)|(^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$)", message="Non è una email valida")
	public String getEmail1() {
		return this.email1;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "peoples")
	public Set<Ente> getEntes() {
		return this.entes;
	}

	@Column(name = "Fax", length = 20)
	@Length(max = 20)
	public String getFax() {
		return this.fax;
	}

	@Column(name = "Fax1", length = 20)
	@Length(max = 20)
	public String getFax1() {
		return this.fax1;
	}

	@Id
	@Column(name = "IDPeople", unique = true, nullable = false)
	@NotNull
	public String getIdpeople() {
		return this.idpeople;
	}

	@Column(name = "Nome", length = 64)
	@Length(max = 64)
	public String getNome() {
		return this.nome;
	}

	@Column(name = "NumeroBreve", length = 20)
	@Length(max = 20)
	public String getNumeroBreve() {
		return this.numeroBreve;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDOrario")
	public Orario getOrario() {
		return this.orario;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "eloise.`People2OrganizationalRole`", joinColumns = { @JoinColumn(name = "IDPeople", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "OrganizationalRoleID", nullable = false, updatable = false) })
	public Set<Organizationalrole> getOrganizationalroles() {
		return this.organizationalroles;
	}

	@Column(name = "Password")
	public String getPassword() {
		return this.password;
	}

	@Column(name = "ProfiloProfessionale")
	public String getProfiloProfessionale() {
		return this.profiloProfessionale;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDSede")
	public Sede getSede() {
		return this.sede;
	}

	@Column(name = "Telefono", length = 20)
	@Length(max = 20)
	public String getTelefono() {
		return this.telefono;
	}

	@Column(name = "TelefonoPrivato", length = 20)
	@Length(max = 20)
	public String getTelefonoPrivato() {
		return this.telefonoPrivato;
	}

	@ManyToMany( fetch = FetchType.LAZY, mappedBy = "peoples")
	public Set<Ufficio> getUfficios() {
		return this.ufficios;
	}

	@Column(name = "visible")
	public Boolean getVisible() {
		return this.visible;
	}

	@Pattern(regex="(^$)|(^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$)", message="Non è una email valida")
	public String getPEC() {
		return PEC;
	}
	
	@Lob
	public String getDescrizione() {
		return descrizione;
	}
	public void setAreas(Set<Area> areas) {
		this.areas = areas;
	}

	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public void setEntes(Set<Ente> entes) {
		this.entes = entes;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}

	public void setIdpeople(String idpeople) {
		this.idpeople = idpeople;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setNumeroBreve(String numeroBreve) {
		this.numeroBreve = numeroBreve;
	}

	public void setOrario(Orario orario) {
		this.orario = orario;
	}

	public void setOrganizationalroles(
			Set<Organizationalrole> organizationalroles) {
		this.organizationalroles = organizationalroles;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setProfiloProfessionale(String profiloProfessionale) {
		this.profiloProfessionale = profiloProfessionale;
	}

	public void setSede(Sede sede) {
		this.sede = sede;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setTelefonoPrivato(String telefonoPrivato) {
		this.telefonoPrivato = telefonoPrivato;
	}

	public void setUfficios(Set<Ufficio> ufficios) {
		this.ufficios = ufficios;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public void setPEC(String pEC) {
		PEC = pEC;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Transient
	public List<Ufficio> getUfficiosAsList(){
		return new ArrayList<Ufficio>(this.getUfficios());
	}
	@Transient
	public void setUfficiosAsList(List<Ufficio> ufficios){
		this.setUfficios(new HashSet<Ufficio>(ufficios));
	}

	@Transient
	public List<Ente> getEntesAsList(){
		return new ArrayList<Ente>(this.getEntes());
	}
	@Transient
	public void setEntesAsList(List<Ente> entes){
		this.setEntes(new HashSet<Ente>(entes));
	}

	@Transient
	public List<Organizationalrole> getOrganizationalrolesAsList(){
		return new ArrayList<Organizationalrole>(this.getOrganizationalroles());
		
	}
	@Transient
	public void setOrganizationalrolesAsList(List<Organizationalrole> organizationalroles){
		this.setOrganizationalroles(new HashSet<Organizationalrole>(organizationalroles)); 
	}

	@Transient
	public List<Area> getAreasAsList(){
		return new ArrayList<Area>(this.getAreas());
		
	}
	@Transient
	public void setAreasAsList(List<Area> areas){
		this.setAreas(new HashSet<Area>(areas)); 
	}	
	
	@Override
	public String toString() {
		return this.getNome() + " " + this.getCognome();
	}
}
