package it.drwolf.alerting.homes;

import it.drwolf.alerting.entity.Report;
import it.drwolf.alerting.lists.ReportList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.timer.Timer;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("reportHome")
public class ReportHome extends EntityHome<Report> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 224500294056629592L;

	@In(create = true)
	EntityManager entityManager;

	@In(create = true)
	ReportList reportList;

	private String newColumn;
	private List<String> columns;

	private Date dataInizioRicerca;
	private Date dataFineRicerca;

	public ReportHome() {
		this.dataFineRicerca = new Date();
		this.dataInizioRicerca = new Date();
		this.dataInizioRicerca = new Date(this.dataInizioRicerca.getTime()
				- 365L * Timer.ONE_DAY);
	}

	@Override
	protected Report createInstance() {
		Report report = new Report();
		return report;
	}

	public List<String> getColumns() {
		return this.columns;
	}

	public Date getDataFineRicerca() {
		return this.dataFineRicerca;
	}

	public Date getDataInizioRicerca() {
		return this.dataInizioRicerca;
	}

	public Report getDefinedInstance() {
		return this.isIdDefined() ? this.getInstance() : null;
	}

	public String getNewColumn() {
		return this.newColumn;
	}

	public Integer getReportId() {
		return (Integer) this.getId();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getResults() {
		Report report = this.getInstance();
		List<Object[]> resultSet = new ArrayList<Object[]>();
		if (report != null) {
			resultSet = this.entityManager.createNativeQuery(report.getQuery())
					.setParameter("inizio", this.dataInizioRicerca)
					.setParameter("fine", this.dataFineRicerca).getResultList();
		}

		return resultSet;
	}

	public boolean isWired() {
		return true;
	}

	@Override
	public String persist() {
		this.getInstance().setColumns(this.getColumns());
		return super.persist();
	}

	public void pushNewColumn() {
		this.getColumns().add(this.newColumn);
		this.newColumn = new String();
	}

	public void refreshView() {

	}

	public void removeColumn(String daRimuovere) {
		List<String> colonneDaRimuovere = new ArrayList<String>();
		for (String colonna : this.getColumns()) {
			if (colonna.equals(daRimuovere)) {
				colonneDaRimuovere.add(colonna);
			}
		}

		this.getColumns().removeAll(colonneDaRimuovere);
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public void setDataFineRicerca(Date dataFineRicerca) {
		this.dataFineRicerca = dataFineRicerca;
	}

	public void setDataInizioRicerca(Date dataInizioRicerca) {
		this.dataInizioRicerca = dataInizioRicerca;
	}

	public void setNewColumn(String newColumn) {
		this.newColumn = newColumn;
	}

	public void setReportId(Integer id) {
		this.setId(id);
	}

	@Override
	public String update() {
		this.getInstance().setColumns(this.getColumns());
		return super.update();
	}

	public void wire() {
		this.setColumns(this.getInstance().getColumns());
	}
}
