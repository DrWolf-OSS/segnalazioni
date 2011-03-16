package it.drwolf.alerting.lists;

import it.drwolf.alerting.entity.Report;

import java.util.Arrays;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

@Name("reportList")
public class ReportList extends EntityQuery<Report> {

	private static final long serialVersionUID = -1247782248878580468L;

	@In
	public EntityManager entityManager;

	private static final String EJBQL = "select report from Report report";

	private static final String[] RESTRICTIONS = {
			"lower(report.nome) like concat(lower(#{reportList.report.nome}),'%')",
			"lower(report.descrizione) like concat(lower(#{reportList.report.descrizione}),'%')", };

	private Report report = new Report();

	public ReportList() {
		this.setEjbql(ReportList.EJBQL);
		this.setRestrictionExpressionStrings(Arrays
				.asList(ReportList.RESTRICTIONS));
		this.setMaxResults(25);
		this.setOrder("id desc");
	}

	public Report getReport() {
		return this.report;
	}

	public Report getReport(String name) {
		for (Report report : this.getResultList()) {
			if (report.getNome().equals(name)) {
				return report;
			}
		}
		return null;
	}

}
