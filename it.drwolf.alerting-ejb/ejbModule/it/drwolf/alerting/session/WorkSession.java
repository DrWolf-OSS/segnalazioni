package it.drwolf.alerting.session;

import it.drwolf.alerting.entity.AppParam;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Identity;

@Name("workSession")
@Scope(ScopeType.SESSION)
@AutoCreate
public class WorkSession {

	private String currentTab = null;

	private String skin;

	private String userFullname = null;

	@In(required = false)
	private EntityManager entityManager;

	@Create
	public void create() {
		this.skin = this.entityManager.find(AppParam.class, "app.skin")
				.getValue();
	}

	@Destroy
	public void destroy() {
		Identity.instance().getCredentials().setUsername(null);
		Identity.instance().getCredentials().setPassword(null);
	}

	public String getCurrentTab() {
		return this.currentTab;
	}

	public String getParam(String param) {
		return "" + this.entityManager.find(AppParam.class, param);
	}

	public String getSkin() {
		return this.skin;
	}

	public String getUserFullname() {
		return this.userFullname;
	}

	public void setCurrentTab(String currentTab) {
		this.currentTab = currentTab;
	}

	public void setUserFullname(String userFullname) {
		this.userFullname = userFullname;
	}

}
