package it.drwolf.alerting.session.interfaces;

import javax.ejb.Local;

@Local
public interface IStarter {
	public void destroy();

	public void start();

}
