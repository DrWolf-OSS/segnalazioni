package it.drwolf.alerting.util;

import java.util.Date;

public class Info implements Comparable<Info> {
	private String txt;
	private String user;
	private Date date;
	private Date date2;

	public Info() {

	}

	public Info(Date date, Date date2, String txt, String user) {

		this.date = date;
		this.date2 = date2;
		this.txt = txt;
		this.user = user;
	}

	public Info(Date date, String txt, String user) {

		this.date = date;
		this.txt = txt;
		this.user = user;
	}

	public int compareTo(Info o) {
		return o.getDate().compareTo(this.date);
	}

	public Date getDate() {
		return this.date;
	}

	public Date getDate2() {
		return this.date2;
	}

	public String getTxt() {
		return this.txt;
	}

	public String getUser() {
		return this.user;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
