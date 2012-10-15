package it.drwolf.sso.session;

import it.drwolf.sso.api.SSOModule;
import it.drwolf.sso.entity.AdminUser;
import it.drwolf.sso.entity.AppParam;
import it.drwolf.sso.entity.SSOToken;
import it.drwolf.sso.session.interfaces.ITokenManager;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.security.Identity;

@Scope(ScopeType.APPLICATION)
@Name("tokenManager")
@AutoCreate
@Stateful
public class TokenManager implements ITokenManager, Serializable {

	/**
	 * @author MarcoD
	 * 
	 *         ho promosso il metodo a public static
	 */
	public static void addModule(List<SSOModule> list, String name) {
		Object o = null;
		try {
			o = new InitialContext().lookup(name + "/local");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (o instanceof SSOModule) {
			list.add((SSOModule) o);
		} else if (o != null) {
			final Object proxy = o;
			list.add(new SSOModule() {

				public boolean changePassword(String username,
						String oldPassword, String newPassword) {
					Method m;
					try {
						m = proxy.getClass().getMethod("changePassword",
								String.class, String.class, String.class);
						return (Boolean) m.invoke(proxy, username, oldPassword,
								newPassword);
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}

				@SuppressWarnings("unchecked")
				public List<String> listUsers() {
					Method m;
					try {
						m = proxy.getClass().getMethod("listUsers");
						return (List<String>) m.invoke(proxy);
					} catch (Exception e) {
						e.printStackTrace();
						return Arrays.asList(new String[] {});
					}
				}

				@SuppressWarnings("unchecked")
				public HashMap<String, String> login(String username,
						String password) {
					Method m;
					try {
						m = proxy.getClass().getMethod("login", String.class,
								String.class);
						return (HashMap<String, String>) m.invoke(proxy,
								username, password);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}

				public String remindPassword(String username) {
					Method m;
					try {
						m = proxy.getClass().getMethod("remindPassword",
								String.class);
						return (String) m.invoke(proxy, username);
					} catch (Exception e) {
						e.printStackTrace();
						return "";
					}
				}

			});
		}

	}

	private HashMap<String, HashMap<String, String>> tokens = new HashMap<String, HashMap<String, String>>();

	private String secret = "secret";

	@In
	private EntityManager entityManager;

	@Create
	public void create() {

		if (this.entityManager.createQuery("from AdminUser").getResultList()
				.size() == 0) {
			AdminUser admin = new AdminUser();
			admin.setUsername("admin");
			admin.setPassword("changeme");
			this.entityManager.persist(admin);
		}

		AppParam secret = this.entityManager.find(AppParam.class, "secret");
		if (secret == null) {
			secret = new AppParam();
			secret.setId("secret");
			secret.setValue(UUID.randomUUID().toString());
			this.entityManager.persist(secret);
		}
		this.secret = secret.getValue();

	}

	@Remove
	@Destroy
	public void destroy() {
		// TODO Auto-generated method stub
	}

	public String getSecret() {
		return this.secret;
	}

	@SuppressWarnings("unchecked")
	public List<SSOModule> getSsoModules() {
		List<SSOModule> list = new ArrayList<SSOModule>();
		for (String name : (List<String>) this.entityManager.createQuery(
				"select name from Module order by position").getResultList()) {
			TokenManager.addModule(list, name);
		}
		return list;
	}

	public HashMap<String, HashMap<String, String>> getTokens() {
		List<SSOToken> resultList = this.entityManager.createQuery(
				"from SSOToken").getResultList();
		this.tokens.clear();
		for (SSOToken ssoToken : resultList) {
			this.tokens.put(ssoToken.getUuid(), ssoToken.getInfo());
		}
		return this.tokens;
	}

	public void remindPassword() {
		for (SSOModule m : this.getSsoModules()) {
			final String email = m.remindPassword(Identity.instance()
					.getUsername());
			if (!"".equals(email)) {
				FacesMessages.instance().add(
						"La password per accedere è stata inviata all'indirizzo "
								+ email);
				break;
			}
		}
	}

}
