package it.drwolf.sso.session;

import it.drwolf.sso.session.interfaces.ITokenManager;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("ssoIS")
public class IdentityStore implements
		org.jboss.seam.security.management.IdentityStore {

	@In
	private ITokenManager tokenManager;
	@In(create = true)
	private Authenticator authenticator;

	public boolean addRoleToGroup(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean authenticate(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean changePassword(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean createRole(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean createUser(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean createUser(String arg0, String arg1, String arg2, String arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteRole(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteUser(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean disableUser(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean enableUser(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<String> getGrantedRoles(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getImpliedRoles(String username) {
		return Collections.emptyList();
	}

	public List<String> getRoleGroups(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean grantRole(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isUserEnabled(String username) {
		for (Entry<String, HashMap<String, String>> entry : this.tokenManager
				.getTokens().entrySet()) {
			if (entry.getValue() != null
					&& entry.getValue().get("username").equals(username)) {
				this.authenticator.setUuid(entry.getKey());
				return true;
			}
		}
		return false;
	}

	public List<String> listGrantableRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Principal> listMembers(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> listRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> listUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> listUsers(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean removeRoleFromGroup(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean revokeRole(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean roleExists(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean supportsFeature(Feature arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean userExists(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
