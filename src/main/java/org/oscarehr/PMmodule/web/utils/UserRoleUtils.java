package org.oscarehr.PMmodule.web.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserRoleUtils {

	public static final String USER_ROLE_SESSION_KEY = "userrole";
	public static final String USER_SEPARATOR = ",";

	public enum Roles {
		doctor, admin, receptionist, nurse, external, er_clerk
	}
	
	/**
	 * This method will return an array of strings representing
	 * the roles as extracted from the session. If there are no
	 * roles then an empty array is returned, it should never
	 * return null.
	 */
	public static String[] getUserRoles(HttpServletRequest request) {
		
		HttpSession session = request.getSession();

		String temp = (String)session.getAttribute(USER_ROLE_SESSION_KEY);

		if (temp == null) return(new String[0]);
		else return(temp.split(USER_SEPARATOR));
	}

	/**
	 * This method checks to see if the currently logged in user has the role.
	 * This method is inefficient and just iterates through all the roles right now.
	 */
	public static boolean hasRole(HttpServletRequest request, Roles role) {
		return(hasRole(request,role.name()));
	}
		
	/**
	 * This method checks to see if the currently logged in user has the role.
	 * This method is inefficient and just iterates through all the roles right now.
	 */
	public static boolean hasRole(HttpServletRequest request, String role) {
		
		for (String temp : getUserRoles(request)) {
			if (temp.equals(role)) return(true);
		}

		return(false);
	}

}
