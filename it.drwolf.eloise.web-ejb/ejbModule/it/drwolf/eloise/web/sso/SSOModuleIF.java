package it.drwolf.eloise.web.sso;

import java.util.HashMap;
import java.util.List;

public interface SSOModuleIF {
	public boolean changePassword(String username, String oldPassword,
			String newPassword);

	public List<String> listUsers();

	public HashMap<String, String> login(String username, String password);

	public String remindPassword(String username);
}
