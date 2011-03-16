package it.drwolf.alerting.util.risposte;

import it.drwolf.alerting.entity.AppParam;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("risposteUtils")
public class RisposteUtils {
	@In
	private EntityManager entityManager;

	public String getFooter() {
		return this.entityManager.find(AppParam.class,
				AppParam.APP_URL.getKey()).getValue()
				+ this.entityManager.find(AppParam.class,
						AppParam.APP_IMG_FOOT.getKey()).getValue();
	}

	public String getHeader() {
		return this.entityManager.find(AppParam.class,
				AppParam.APP_URL.getKey()).getValue()
				+ this.entityManager.find(AppParam.class,
						AppParam.APP_IMG_HEAD.getKey()).getValue();
	}

}
