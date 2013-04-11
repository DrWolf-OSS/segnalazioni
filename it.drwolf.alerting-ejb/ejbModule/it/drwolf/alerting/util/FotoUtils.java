package it.drwolf.alerting.util;

import it.drwolf.alerting.entity.Segnalazione;

import java.util.ArrayList;

import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.jboss.seam.annotations.Name;

@Name("fotoUtils")
public class FotoUtils {

	CmisUtils cmisUtils = new CmisUtils();

	@SuppressWarnings("unchecked")
	public ArrayList getFoto(Segnalazione segnalazione) {
		this.cmisUtils.login();
		Session session = this.cmisUtils.getSession();
		ItemIterable<QueryResult> results = session.query(
				"select d.*, c.* from cmis:document as d join dw:caleearth as c on d.cmis:objectId  = c.cmis:objectId where c.dw:segnalazione = 1106", false);
		System.out.println("ci passo");

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
