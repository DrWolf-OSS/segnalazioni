package it.drwolf.alerting.session;

import it.drwolf.alerting.entity.AppParam;
import it.drwolf.alerting.entity.Segnalazione;
import it.drwolf.alerting.entity.UfficioCompetente;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.bpm.BusinessProcess;

@Name("alertingProcess")
@Scope(ScopeType.BUSINESS_PROCESS)
@AutoCreate
public class AlertingProcess implements Serializable {

	private static final long serialVersionUID = 2043887922342345L;

	private Set<String> smistatori = new HashSet<String>();

	@In(create = true)
	private EntityManager entityManager;

	@In(scope = ScopeType.BUSINESS_PROCESS, required = false)
	private Integer bpmInfoId;

	private String impiegatoUfficioCompetente = null;

	private String testoApertura = null;

	private String testoChiusura = null;

	private String[] poolCompetente = new String[0];

	private UfficioCompetente ufficio;

	public Integer getBpmInfoId() {
		return this.bpmInfoId;
	}

	public String getImpiegatoUfficioCompetente() {
		return this.impiegatoUfficioCompetente;
	}

	public String[] getPoolCompetente() {
		return this.poolCompetente;
	}

	@SuppressWarnings("unchecked")
	public Segnalazione getSegnalazione() {
		List<Segnalazione> l = this.entityManager.createQuery(
				"from Segnalazione where bpmInfo.processId=:pid").setParameter(
				"pid", BusinessProcess.instance().getProcessId())
				.getResultList();

		if (l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	public String[] getSmistatori() {
		// Segnalazione segnalazione = this.getSegnalazione();
		// if (segnalazione != null) {
		// return segnalazione.getSottotipoSegnalazione()
		// .getTipoSegnalazione().getUfficioSmistatore().getGestori()
		// .toArray(new String[] {});
		// }
		// return new String[] {};
		return this.smistatori.toArray(new String[] {});
	}

	public String getTestoApertura() {
		return this.testoApertura;
	}

	public String getTestoChiusura() {
		return this.testoChiusura;
	}

	public UfficioCompetente getUfficio() {
		return this.ufficio;
	}

	public void setBpmInfoId(Integer bpmInfoId) {
		this.bpmInfoId = bpmInfoId;
	}

	public void setImpiegatoUfficioCompetente(String assegnatario) {
		this.impiegatoUfficioCompetente = assegnatario;
	}

	public void setPoolCompetente(String[] poolCompetente) {
		this.poolCompetente = poolCompetente;
	}

	public void setSmistatori(Set<String> smistatori) {
		this.smistatori = smistatori;
	}

	public void setTestoApertura(String testoApertura) {
		this.testoApertura = testoApertura;
	}

	public void setTestoChiusura(String testoChiusura) {
		this.testoChiusura = testoChiusura;
	}

	public void setUfficio(UfficioCompetente ufficio) {
		this.ufficio = ufficio;

		this.poolCompetente = ufficio.getGestori().toArray(new String[0]);
	}

	public boolean ripassaAUrp(){
		if(this.getSegnalazione() != null && this.getSegnalazione().getCittadino() != null){
			String parametro = this.entityManager.find( AppParam.class, AppParam.RIPASSA_URP_CON_MAIL_TEL.getKey()).getValue();
			if (parametro != null && parametro.trim().equals("true")) {
				String telefono = this.getSegnalazione().getCittadino().getTelefono();
				String email = this.getSegnalazione().getCittadino().getEmail();
				if ((telefono != null && !telefono.trim().equals("")) || (email != null && !email.trim().equals(""))) {
					return true;
				}
			}
		}
		return false;
	}
}
