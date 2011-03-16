package it.drwolf.eloise.web.session.interfaces;

import javax.ejb.Local;

@Local
public interface IStarter {

	public void destroy();

	public void startup();

}
