package it.drwolf.alerting.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class StradaJS implements Comparable<StradaJS> {
	private String strada;
	private List<String> civici;

	public StradaJS() {

	}

	public StradaJS(String strada, List<String> civici) {
		super();
		this.strada = strada;
		this.civici = civici;
	}

	public int compareTo(StradaJS o) {
		return this.getStrada().compareTo(o.getStrada());
	}

	public List<String> getCivici() {
		return this.civici;
	}

	public String getCiviciString() {
		return StringUtils.join(this.getCivici().toArray(), ",");
	}

	public String getStrada() {
		return this.strada;
	}

	public void setCivici(List<String> civici) {
		this.civici = civici;
	}

	public void setStrada(String strada) {
		this.strada = strada;
	}
}
