package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.Sostituto;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.security.Identity;

@Name("sostitutoHome")
public class SostitutoHome extends EntityHome<Sostituto> {

	private static final long serialVersionUID = 101870400407182440L;

	@Override
	protected Sostituto createInstance() {
		Sostituto s = new Sostituto();
		s.setUsername(Identity.instance().getCredentials().getUsername());
		return s;
	}

	public Integer getSostitutoId() {
		return (Integer) this.getId();
	}

	public void setSostitutoId(Integer sostitutoId) {
		this.setId(sostitutoId);
	}

}
