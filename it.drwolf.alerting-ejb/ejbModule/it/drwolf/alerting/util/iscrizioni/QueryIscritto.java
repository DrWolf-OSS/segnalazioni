package it.drwolf.alerting.util.iscrizioni;

import it.drwolf.iscrizioni.entity.Iscritto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

public class QueryIscritto {

	@SuppressWarnings("unchecked")
	public static Map<String, Iscritto> getIscrittiMap(
			EntityManager entityManager) {
		Map<String, Iscritto> res = new LinkedHashMap<String, Iscritto>();
		List<Iscritto> iscritti = entityManager.createQuery(
				"from Iscritto order by Cognome, nome, email").getResultList();
		for (Iscritto i : iscritti) {
			res.put(i.getId(), i);
		}
		return res;
	}

}
