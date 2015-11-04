package it.drwolf.alerting.conf;

import java.io.Serializable;

public class ComponentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String mailHost;
	private int mailPort;
	
	public String getMailHost() {
		//return mailHost;
		
		return "mandrillapp";
	}
	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}
	public int getMailPort() {
		return mailPort;
	}
	public void setMailPort(int mailPort) {
		this.mailPort = mailPort;
	}

	
	
} 
