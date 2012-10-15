package it.drwolf.sso.dummy;

import it.drwolf.sso.api.SSOModule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jboss.seam.annotations.Name;

@Name("dummySSOModule")
public class DummySSOModule implements SSOModule {

	public boolean changePassword(String username, String oldPassword,
			String newPassword) {
		return true;
	}

	public List<String> listUsers() {
		return Arrays.asList(new String[] { "agea" });
	}

	public HashMap<String, String> login(String username, String password) {
		HashMap<String, String> info = new HashMap<String, String>();
		info.put("username", "agea");
		info.put("nome", "Andrea");
		info.put("cognome", "Agili");
		info.put("email", "agea@drwolf.it");
		return info;
	}

	public String remindPassword(String username) {

		return "";
	}

}
