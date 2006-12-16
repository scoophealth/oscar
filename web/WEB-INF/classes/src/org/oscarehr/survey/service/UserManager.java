package org.oscarehr.survey.service;

import javax.servlet.http.HttpServletRequest;

public interface UserManager {
	public long getUserId(HttpServletRequest request);
	public String getUsername(HttpServletRequest request);
	public boolean isLoggedIn(HttpServletRequest request);
	public boolean isAdmin(HttpServletRequest request);
}
