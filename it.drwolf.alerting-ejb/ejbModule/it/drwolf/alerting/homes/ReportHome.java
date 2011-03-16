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

	@In(create = true)
	EntityManager entityManager;

	@In(create = true)
	ReportList reportList;

	private String newColumn;
	private List<String> columns;
	
	private Date dataInizioRicerca;
	private Date dataFineRicerca;

	public ReportHome() {
		dataFineRicerca = new Date();
		dataInizioRicerca = new Date();
		dataInizioRicerca = new Date(dataInizioRicerca.getTime() - 365L * Timer.ONE_DAY);  
	}
	public Date getDataInizioRicerca() {
		return dataInizioRicerca;
	}

	public Date getDataFineRicerca() {
		return dataFineRicerca;
	}

	public void setDataInizioRicerca(Date dataInizioRicerca) {
		this.dataInizioRicerca = dataInizioRicerca;
	}

	public void setDataFineRicerca(Date dataFineRicerca) {
		this.dataFineRicerca = dataFineRicerca;
	}

	public void setReportId(Integer id) {
		setId(id);
	}

	public Integer getReportId() {
		return (Integer) getId();
	}

	@Override
	protected Report createInstance() {
		Report report = new Report();
		return report;
	}

	public void wire() {
		this.setColumns(this.getInstance().getColumns());
	}

	@Override
	public String update() {
		this.getInstance().setColumns(this.getColumns());
		return super.update();
	}

	@Override
	public String persist() {
		this.getInstance().setColumns(this.getColumns());
		return super.persist();
	}

	public boolean isWired() {
		return true;
	}

	public Report getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public String getNewColumn() {
		return newColumn;
	}

	public void setNewColumn(String newColumn) {
		this.newColumn = newColumn;
	}

	public void pushNewColumn() {
		this.getColumns().add(newColumn);
		newColumn = new String();
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
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

	public List<Object[]> getResults() {
		Report report = this.getInstance();
		List<Object[]> resultSet = new ArrayList<Object[]>();
		if (report != null) {
			resultSet = entityManager.createNativeQuery(report.getQuery())
			.setParameter("inizio", dataInizioRicerca)
			.setParameter("fine", dataFineRicerca)
			.getResultList();
		}

		return resultSet;
	}
	
	public void refreshView(){
		
	}
}
