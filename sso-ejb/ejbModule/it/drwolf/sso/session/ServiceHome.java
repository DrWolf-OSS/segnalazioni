package it.drwolf.sso.session;

import it.drwolf.sso.api.SSOModule;
import it.drwolf.sso.entity.Service;
import it.drwolf.sso.session.interfaces.ITokenManager;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("serviceHome")
public class ServiceHome extends EntityHome<Service> {

	private static final long serialVersionUID = -1912444564884484086L;

	@In
	private ITokenManager tokenManager;

	@In
	private EntityManager entityManager;

	private Service checkpoint = null;

	private List<String> availableUsers = null;

	@Override
	protected Service createInstance() {
		Service service = new Service();
		return service;
	}

	public List<String> getAvailableUsers() {

		boolean inquiry = this.availableUsers == null;
		if (!inquiry) {
			inquiry = this.checkpoint != this.instance;
		}
		if (inquiry) {
			TreeSet<String> users = new TreeSet<String>();
			for (SSOModule module : this.getSsoModules()) {
				List<String> list = module.listUsers();
				users.addAll(list);
			}
			this.availableUsers = new ArrayList<String>(users);
			this.checkpoint = this.instance;
		}
		return this.availableUsers;
	}

	public Service getDefinedInstance() {
		return this.isIdDefined() ? this.getInstance() : null;
	}

	public String getServiceId() {
		return (String) this.getId();
	}

	/**
	 * @author MarcoD
	 * @return lista dei moduli che possono listare i propri utenti
	 */
	@SuppressWarnings("unchecked")
	public List<SSOModule> getSsoModules() {
		List<SSOModule> list = new ArrayList<SSOModule>();
		for (String name : (List<String>) this.entityManager
				.createQuery(
						"select name from Module where listUsers = true order by position")
				.getResultList()) {
			TokenManager.addModule(list, name);
		}
		return list;
	}

	/**
	 * @return lista degli utenti del servizio filtrata
	 */
	public List<String> getUsernames() {
		List<String> usernames = this.getInstance().getUsernames();
		List<String> users = this.getAvailableUsers();
		List<String> toRemove = new ArrayList<String>(users.size());
		for (String s : usernames) {
			if (!users.contains(s)) {
				toRemove.add(s);
			}
		}
		for (String s : toRemove) {
			usernames.remove(s);
		}
		return usernames;
	}

	public boolean isWired() {
		return true;
	}

	public void remindAll(Service service) {
		List<String> list = service.getUsernames();
		for (SSOModule m : this.tokenManager.getSsoModules()) {
			if (list.size() == 0) {
				for (String people : m.listUsers()) {
					m.remindPassword(people);
				}
			} else {
				for (String people : list) {
					m.remindPassword(people);
				}

			}
		}

	}

	public void setServiceId(String id) {
		this.setId(id);
	}

	public void setUsernames(List<String> usernames) {
		this.getInstance().setUsernames(usernames);
	}

	public void wire() {
		this.getInstance();
	}

}
