package it.drwolf.alerting.conf;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import it.drwolf.alerting.entity.AppParam;




@Name("componentBean")
@AutoCreate
@Scope(ScopeType.EVENT)
public class ComponentBean implements Serializable {

	
	@In(create = true)
	private EntityManager entityManager;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String getMailHost() {
		return this.entityManager.find(AppParam.class,
				"app.mail.host").toString();
	}
	

	public int getMailPort() {
		return Integer.parseInt(this.entityManager.find(AppParam.class,
				"app.mail.port").toString().toString());
	}
	
	public String getMailPassword(){
		return this.entityManager.find(AppParam.class,
				"app.mail.password").toString().toString();
	}

	public String getMailUsername(){
		return this.entityManager.find(AppParam.class,
				"app.mail.user").toString().toString();
	}
	
	
} 
