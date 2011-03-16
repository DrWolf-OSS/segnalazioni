package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.AppParam;
import it.drwolf.alerting.entity.Giornaliero;
import it.drwolf.alerting.entity.Intervento;
import it.drwolf.alerting.entity.SquadraIntervento;
import it.drwolf.alerting.session.AlertingController;
import it.drwolf.alerting.util.converters.PeopleConverter;
import it.drwolf.eloise.web.entity.People;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.security.Identity;

@Name("giornalieroHome")
public class GiornalieroHome extends EntityHome<Giornaliero> {

	@In(create = true)
	private AlertingController alertingController;

	private static final long serialVersionUID = -175303898756737L;

	private List<Intervento> nuoviInterventi = new ArrayList<Intervento>();

	private int assegnazioneToBeDeleted;

	private Integer sqId;

	private SquadraIntervento squadraIntervento;

	public String convertData(Date data) {
		return new SimpleDateFormat("dd/MM/yyyy").format(data);
	}

	public String convertPeople(String idpeople) {

		return PeopleConverter.formatPeople(this.getEntityManager().find(
				People.class, idpeople));
	}

	@Override
	protected Giornaliero createInstance() {
		Giornaliero giornaliero = new Giornaliero();
		giornaliero.setData(new Date());

		if (this.sqId != null) {
			this.squadraIntervento = this.getEntityManager().find(
					SquadraIntervento.class, this.sqId);
		}

		List<String> ds = Arrays.asList(this.getEntityManager().find(
				AppParam.class, AppParam.APP_DAILY_STATI_DEFAULT.getKey())
				.getValue().split(","));

		List<Integer> idTipiIntervento = this.alertingController
				.getIdTipiIntervento(Identity.instance().getCredentials()
						.getUsername());

		for (Intervento i : this.getListaInterventi(idTipiIntervento)) {
			if (ds.contains(i.getStato().getNome())) {
				giornaliero.getInterventi().add(i);
			}

		}
		return giornaliero;
	}

	public int getAssegnazioneToBeDeleted() {
		return this.assegnazioneToBeDeleted;
	}

	public Integer getGiornalieroId() {

		return (Integer) this.getId();
	}

	public List<Intervento> getInterventiNonInseriti() {
		List<Integer> idTipiIntervento = this.alertingController
				.getIdTipiIntervento(Identity.instance().getCredentials()
						.getUsername());
		List<Intervento> interventi = this.getListaInterventi(idTipiIntervento);
		for (Iterator<Intervento> iterator = interventi.iterator(); iterator
				.hasNext();) {
			Intervento intervento = iterator.next();
			if (this.instance.getInterventi().contains(intervento)) {
				iterator.remove();
			}

		}
		return interventi;
	}

	public List<Entry<SquadraIntervento, List<Intervento>>> getInterventiPerSquadre() {
		Map<SquadraIntervento, List<Intervento>> res = new HashMap<SquadraIntervento, List<Intervento>>();
		for (Intervento i : this.getInstance().getInterventi()) {
			if (res.get(i.getSquadraIntervento()) == null) {
				res.put(i.getSquadraIntervento(), new ArrayList<Intervento>());
			}
			res.get(i.getSquadraIntervento()).add(i);
		}
		return new ArrayList<Entry<SquadraIntervento, List<Intervento>>>(res
				.entrySet());
	}

	@SuppressWarnings("unchecked")
	private List<Intervento> getListaInterventi(List<Integer> idTipiIntervento) {

		List<String> hs = Arrays.asList(this.getEntityManager().find(
				AppParam.class, AppParam.APP_DAILY_STATI_HIDE.getKey())
				.getValue().split(","));

		Query q = this
				.getEntityManager()
				.createQuery(
						"select i from Intervento i left outer join i.stato where "
								+ "i.sottotipoIntervento.tipoIntervento.id in (:idList) and "
								+ "(i.stato is null or i.stato.nome not in (:hs)) "
								+ (this.squadraIntervento == null ? ""
										: "and i.squadraIntervento=:sq ")
								+ "order by i.scadenza");
		q = q.setParameter("hs", hs).setParameter("idList", idTipiIntervento);
		if (this.squadraIntervento != null) {
			q = q.setParameter("sq", this.squadraIntervento);
		}
		return q.getResultList();
	}

	public List<Intervento> getNuoviInterventi() {
		return this.nuoviInterventi;
	}

	public Integer getSqId() {
		return this.sqId;
	}

	public SquadraIntervento getSquadraIntervento() {
		return this.squadraIntervento;
	}

	public void setGiornalieroId(Integer id) {
		this.setId(id);
	}

	public void setSqId(Integer sqId) {
		this.sqId = sqId;
	}

	public void setSquadraIntervento(SquadraIntervento squadraIntervento) {
		this.squadraIntervento = squadraIntervento;
	}

}
