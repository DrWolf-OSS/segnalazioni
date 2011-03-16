package it.drwolf.eloise.web.session;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.security.Identity;

@Name("eloiseAuthenticator")
public class Authenticator {

	@In
	private EntityManager entityManager;

	@In
	private Identity identity;

	public boolean authenticate() {
		return this.entityManager.createQuery(
				"from AdminUser where userid=:userid and password=:password")
				.setParameter("userid", this.identity.getUsername())
				.setParameter("password", this.identity.getPassword())
				.getResultList().size() > 0;

	}
}
