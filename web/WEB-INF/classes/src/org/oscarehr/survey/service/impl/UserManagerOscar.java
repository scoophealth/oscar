package org.oscarehr.survey.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.oscarehr.survey.service.UserManager;

public class UserManagerOscar implements UserManager {

	public long getUserId(HttpServletRequest request) {
		String value = (String)request.getSession().getAttribute("user");
		return Long.parseLong(value);
	}

	public String getUsername(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isLoggedIn(HttpServletRequest request) {
		if(request.getSession().getAttribute("user") == null) {
			return false;
		}
		return true;
	}

	public boolean isAdmin(HttpServletRequest request) {
		if(isLoggedIn(request) && ((String)request.getSession().getAttribute("userrole")).indexOf("admin")  != -1) {
			return true;
		}
		return false;
	}

}
