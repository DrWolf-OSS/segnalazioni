package it.drwolf.iscrizioni.util;

import it.drwolf.iscrizioni.entity.Iscritto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("mailMonitor")
@Scope(ScopeType.APPLICATION)
public class MailMonitor implements Serializable {

	private static final long serialVersionUID = 6089771503648745008L;

	private String lastLog = "";

	private Map<String, LinkedList<Iscritto>> inCorso = new LinkedHashMap<String, LinkedList<Iscritto>>();

	public Map<String, LinkedList<Iscritto>> getInCorso() {
		return this.inCorso;
	}

	public List<String> getInCorsoKeys() {
		return new ArrayList<String>(this.getInCorso().keySet());
	}

	public String getLastLog() {
		return this.lastLog;
	}

	public void setInCorso(Map<String, LinkedList<Iscritto>> inCorso) {
		this.inCorso = inCorso;
	}

	public void setLastLog(String lastLog) {
		this.lastLog = lastLog;
	}
}
