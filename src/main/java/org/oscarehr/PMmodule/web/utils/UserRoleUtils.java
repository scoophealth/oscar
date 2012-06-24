/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

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
