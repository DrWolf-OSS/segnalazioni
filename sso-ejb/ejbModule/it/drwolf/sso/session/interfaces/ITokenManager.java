package it.drwolf.sso.session.interfaces;

import it.drwolf.sso.api.SSOModule;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;

@Local
public interface ITokenManager {

	public abstract void create();

	public void destroy();

	public abstract String getSecret();

	public abstract List<SSOModule> getSsoModules();

	public abstract HashMap<String, HashMap<String, String>> getTokens();

	public void remindPassword();

}