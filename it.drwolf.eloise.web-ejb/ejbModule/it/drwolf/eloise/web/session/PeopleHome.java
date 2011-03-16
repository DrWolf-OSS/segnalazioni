package it.drwolf.eloise.web.session;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;




import it.drwolf.eloise.web.entity.*;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.arjuna.ats.internal.jdbc.drivers.modifiers.list;

@Name("peopleHome")
public class PeopleHome extends EntityHome<People> {

	@In(create = true)
	SedeHome sedeHome;
	@In(create = true)
	OrarioHome orarioHome;
	@In(create = true)
	EntityManager entityManager;

	private List<Ente> oldEntes = null;
	private List<Ufficio> oldUfficios = null;
	private List<Area> oldAreas = null;
	private List<Organizationalrole> rolesToAdd = new ArrayList<Organizationalrole>();
	private List<Organizationalrole> rolesToRemove = new ArrayList<Organizationalrole>();
	

	
	
	public void setPeopleIdpeople(String id) {
		setId(id);
	}

	public String getPeopleIdpeople() {
		return (String) getId();
	}

	@Override
	protected People createInstance() {
		People people = new People();
		return people;
	}

	public void wire() {
		getInstance();
		if (oldEntes == null){
			oldEntes= new ArrayList<Ente>(getInstance().getEntes());
		}
		if (oldUfficios == null ){
			oldUfficios= new ArrayList<Ufficio>(getInstance().getUfficios());
		}
		if (oldAreas == null){
			oldAreas= new ArrayList<Area>(getInstance().getAreas());
		}

		Sede sede = sedeHome.getDefinedInstance();
		if (sede != null) {
			getInstance().setSede(sede);
		}
		Orario orario = orarioHome.getDefinedInstance();
		if (orario != null) {
			getInstance().setOrario(orario);
		}
	}

	public boolean isWired() {
		return true;
	}

	public People getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	@Override
	public String persist() {
		checkEntes();
		checkAreas();
		checkUfficios();
		checkRoles();
		 return  super.persist();
	}
	
	@Override
	public String update() {
		checkEntes();
		checkAreas();
		checkUfficios();
		checkRoles();
		return super.update();

	}
	
	public void checkEntes(){

		for (Ente ente: this.getInstance().getEntes()) {
			//aggiungo le nuove associazioni
			if (!ente.getPeoples().contains(getInstance())) {
				ente.getPeoples().add(getInstance());
				//this.getEntityManager().merge(ente);
			}
		}
		
		//rimuovo nel DB le associazioni tolte dall'utente
		for (Ente ente : this.oldEntes) {
			if (! this.getInstance().getEntes().contains(ente)) {
				ente.getPeoples().remove(this.getInstance());
				this.getInstance().getEntes().remove(ente);
				//this.getEntityManager().merge(ente);
			}
		}		
	}
	
	public void checkUfficios(){

		for (Ufficio ufficio: this.getInstance().getUfficios()) {
			//aggiungo le nuove associazioni
			if (!ufficio.getPeoples().contains(getInstance())) {
				ufficio.getPeoples().add(getInstance());
				//this.getEntityManager().merge(ente);
			}
		}
		
		//rimuovo nel DB le associazioni tolte dall'utente
		for (Ufficio ufficio : this.oldUfficios) {
			if (! this.getInstance().getUfficios().contains(ufficio)) {
				ufficio.getPeoples().remove(this.getInstance());
				this.getInstance().getUfficios().remove(ufficio);
				//this.getEntityManager().merge(ente);
			}
		}		
	}

	
	public void checkAreas(){

		for (Area area: this.getInstance().getAreas()) {
			//aggiungo le nuove associazioni
			if (!area.getPeoples().contains(getInstance())) {
				area.getPeoples().add(getInstance());
			}
		}
		
		//rimuovo nel DB le associazioni tolte dall'utente
		for (Area area : this.oldAreas) {
			if (! this.getInstance().getAreas().contains(area)) {
				area.getPeoples().remove(this.getInstance());
				this.getInstance().getAreas().remove(area);
			}
		}		
	}
	
	@Override
	public String remove() {
		for (Area area : this.oldAreas) {
			area.getPeoples().remove(this.getInstance());
		}
		for (Ufficio ufficio : this.oldUfficios) {
				ufficio.getPeoples().remove(this.getInstance());
		}		
		for (Ente ente : this.oldEntes) {
				ente.getPeoples().remove(this.getInstance());
		}	
		return super.remove();
	}
	
	public void addRole(Ente ente, Roletype ruolo){
		rolesToAdd.add(new Organizationalrole(ruolo, ente));
	}
	
	public void addRole(Area area, Roletype ruolo){
		rolesToAdd.add(new Organizationalrole(ruolo,area.getEnte(), area));
	}
	
	public void addRole(Ufficio ufficio, Roletype ruolo){
		rolesToAdd.add(new Organizationalrole(ruolo, ufficio.getArea().getEnte(), ufficio.getArea(),ufficio));
	}	
	
	public List<Organizationalrole> getRoles(){
		List<Organizationalrole> lista = new ArrayList<Organizationalrole>(rolesToAdd);
		lista.addAll(this.getInstance().getOrganizationalroles());
		lista.removeAll(rolesToRemove);
		return lista;
	}
	
	public void removeRole(Organizationalrole role){
		if (this.rolesToAdd.contains(role)) {
			this.rolesToAdd.remove(role);
		}
		else {
			rolesToRemove.add(role);
		}
	}

	public void checkRoles(){
		addRoles();
		deleteRoles();
	}

	private void addRoles() {
		for (Organizationalrole or : rolesToAdd) {
			or.getPeoples().add(this.getInstance());
			or.getRoletype().getOrganizationalroles().add(or);
			if(or.getEnte() != null){
				or.getEnte().getOrganizationalroles().add(or);
			}
			if(or.getArea() != null){
				or.getArea().getOrganizationalroles().add(or);
			}
			if(or.getUfficio() != null){
				or.getUfficio().getOrganizationalroles().add(or);
			}	
			this.getInstance().getOrganizationalroles().add(or);
			entityManager.persist(or);
		}
	}
	
	
	private void deleteRoles(){
		
		for (Organizationalrole or : rolesToRemove) {
			or.getPeoples().remove(this.getInstance());
			or.getRoletype().getOrganizationalroles().remove(or);
			if(or.getEnte() != null){
				or.getEnte().getOrganizationalroles().remove(or);
			}
			if(or.getArea() != null){
				or.getArea().getOrganizationalroles().remove(or);
			}
			if(or.getUfficio() != null){
				or.getUfficio().getOrganizationalroles().remove(or);
			}	
			this.getInstance().getOrganizationalroles().remove(or);			
			entityManager.remove(or);
		}
	}

	public List<Ente> getOldEntes() {
		return oldEntes;
	}

	public void setOldEntes(List<Ente> oldEntes) {
		this.oldEntes = oldEntes;
	}

}
