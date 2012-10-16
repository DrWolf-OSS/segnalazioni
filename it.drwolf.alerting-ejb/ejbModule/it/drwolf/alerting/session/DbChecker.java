package it.drwolf.alerting.session;

import it.drwolf.alerting.entity.AppParam;
import it.drwolf.alerting.entity.CanaleSegnalazione;
import it.drwolf.alerting.entity.CodiceTriage;
import it.drwolf.alerting.entity.GlobalRole;
import it.drwolf.alerting.entity.Stato;
import it.drwolf.alerting.session.interfaces.IStarter;
import it.drwolf.alerting.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

@Stateful
@Name("dbChecker")
@Scope(ScopeType.APPLICATION)
@Startup
public class DbChecker implements IStarter {
	@In
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	private void checkCanali() {
		HashMap<String, String> stati = new HashMap<String, String>();
		stati.put(Constants.CANALE_WWW_NOME.toString(),
				Constants.CANALE_WWW_DESC.toString());
		stati.put(Constants.CANALE_TEL_NOME.toString(),
				Constants.CANALE_TEL_DESC.toString());
		stati.put(Constants.CANALE_PERSONA_NOME.toString(),
				Constants.CANALE_PERSONA_DESC.toString());

		for (Entry<String, String> entry : stati.entrySet()) {
			List<CanaleSegnalazione> l = this.entityManager
					.createQuery("from CanaleSegnalazione where nome=:nome")
					.setParameter("nome", entry.getKey()).getResultList();
			if (l.size() == 0) {
				CanaleSegnalazione s = new CanaleSegnalazione();
				s.setNome(entry.getKey());
				s.setDescrizioneCanale(entry.getValue());
				this.entityManager.persist(s);
			}
		}

	}

	private void checkCodiciTriage() {
		for (CodiceTriage ct : CodiceTriage.defaults) {
			if (this.entityManager.find(CodiceTriage.class, ct.getId()) == null) {
				this.entityManager.persist(ct);
			}
		}
	}

	private void checkParams() {

		for (AppParam ap : AppParam.defaults) {
			AppParam find = this.entityManager
					.find(AppParam.class, ap.getKey());
			if (find == null) {
				this.entityManager.persist(ap);
			} else {
				ap.setValue(find.getValue());
			}
		}

	}

	private void checkRoles() {
		for (String role : GlobalRole.defaults) {
			if (this.entityManager
					.createQuery("from GlobalRole where name=:nome")
					.setParameter("nome", role).getResultList().size() == 0) {
				GlobalRole gr = new GlobalRole();
				gr.setName(role);
				this.entityManager.persist(gr);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void checkStati() {

		for (Stato stato : Stato.defaults) {
			List<Stato> l = this.entityManager
					.createQuery("from Stato where nome=:nome")
					.setParameter("nome", stato.getNome()).getResultList();
			if (l.size() == 0) {
				Stato s = new Stato();
				s.setNome(stato.getNome());
				s.setDescrizione(stato.getDescrizione());
				this.entityManager.persist(s);
			}
		}

	}

	@Destroy
	@Remove
	public void destroy() {

	}

	@Create
	public void start() {
		this.checkStati();
		this.checkCodiciTriage();
		this.checkParams();
		this.checkRoles();
		this.checkCanali();
	}

}
