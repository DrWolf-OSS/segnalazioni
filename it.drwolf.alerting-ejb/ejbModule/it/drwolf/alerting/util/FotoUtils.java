package it.drwolf.alerting.util;

import it.drwolf.alerting.entity.Segnalazione;
import it.drwolf.alerting.homes.SegnalazioneHome;

import java.util.ArrayList;

import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("fotoUtils")
public class FotoUtils {

	CmisUtils cmisUtils = new CmisUtils();

	@In
	SegnalazioneHome segnalazioneHome;

	@SuppressWarnings("unchecked")
	public ArrayList getFoto(Segnalazione segnalazione) {
		this.cmisUtils.login();
		Session session = this.cmisUtils.getSession();
		ItemIterable<QueryResult> results = session.query(
				"select d.cmis:objectId from cmis:document as d join dw:caleearth as c on d.cmis:objectId  = c.cmis:objectId where c.dw:segnalazione = "
						+ this.segnalazioneHome.getInstance().getId(), false);
		System.out.println("Cerco fotografie per segnalazione n° " + this.segnalazioneHome.getInstance().getId());

		ArrayList resultList = new ArrayList();
		for (QueryResult hit : results) {
			resultList.add(hit);
			for (PropertyData<?> property : hit.getProperties()) {

				String queryName = property.getQueryName();
				Object value = property.getFirstValue();

				System.out.println(queryName + ": " + value);
			}
			System.out.println("--------------------------------------");
		}

		return resultList;

	}

}
