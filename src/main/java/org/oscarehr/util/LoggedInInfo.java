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

package org.oscarehr.util;

import java.io.Serializable;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;

/**
 * The Provider fields should only be used if this is a user based thread, i.e. a thread handling a user request.
 * If this is an internal system thread, those fields should be ignored and left null.
 * The initiatingCode field can be used for both internal threads as well as user requests.
 * It should signify where the code started for the most part, i.e. the thread class name,
 * or the jsp name, or web service name and method.
 * 
 * NOTE: this class is not intended to be extended
 */
public final class LoggedInInfo implements Serializable {
	
	
	public final String LOGGED_IN_INFO_KEY = LoggedInInfo.class.getName() + ".LOGGED_IN_INFO_KEY";
	//public static final ThreadLocal<LoggedInInfo> loggedInInfo = new ThreadLocal<LoggedInInfo>();
	
	private HttpSession session = null;
	private Facility currentFacility = null;
	private Provider loggedInProvider = null;
	private String initiatingCode = null;
	private Security loggedInSecurity = null;
	private Locale locale = null;
	private String ip = null;

	public LoggedInInfo()
	{
		// do nothing
	}
	
	public LoggedInInfo(HttpSession session, Facility currentFacility,Provider loggedInProvider,Security loggedInSecurity,Locale locale)
	{
		this.session=session;
		this.currentFacility=currentFacility;
		this.loggedInProvider=loggedInProvider;
		this.loggedInSecurity=loggedInSecurity;
		this.locale=locale;
	}
	
	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}

	/**
	 * This method is intended to be used by timer task or background threads to 
	 * setup the thread local loggedInInfo. It should do basic checks to see if 
	 * there's lingering data, then set the thread local internalThreadDescription 
	 * to the name of the class that called this method, i.e. your thread class name.
	 */
	public static LoggedInInfo getLoggedInInfoAsCurrentClassAndMethod() {
		// get caller
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();

		// create and set new thread local
		LoggedInInfo x = new LoggedInInfo();
		x.initiatingCode = ste[2].getClassName() + '.' + ste[2].getMethodName();
		return(x);
	}

	/**
	 * This method should be used for browser requests / end user requests.
	 */
	public static void setLoggedInInfoIntoSession(HttpSession session, LoggedInInfo loggedInInfo) {
	
		session.setAttribute(new LoggedInInfo().LOGGED_IN_INFO_KEY, loggedInInfo);
	}

	/**
	 * This method should be used for browser requests / end user requests.
	 */
	public static LoggedInInfo getLoggedInInfoFromSession(HttpSession session) {
		return ((LoggedInInfo) session.getAttribute(new LoggedInInfo().LOGGED_IN_INFO_KEY));
	}

	/**
	 * This method should be used for browser requests / end user requests.
	 * This will pick out the session from the request object. It doesn't attempt
	 * to get it from the requestAttributes.
	 */
	public static LoggedInInfo getLoggedInInfoFromSession(HttpServletRequest request) {
		return (getLoggedInInfoFromSession(request.getSession()));
	}

	/**
	 * This method should be used for web services.
	 * This will be stored in the requestAttributes, not the session.
	 */
	public static void setLoggedInInfoIntoRequest(HttpServletRequest request, LoggedInInfo loggedInInfo) {
		request.setAttribute(new LoggedInInfo().LOGGED_IN_INFO_KEY, loggedInInfo);
	}

	/**
	 * This method should be used for web services.
	 * This will be retrieved from the requestAttributes, not the session.
	 */
	public static LoggedInInfo getLoggedInInfoFromRequest(HttpServletRequest request) {
		return ((LoggedInInfo) request.getAttribute(new LoggedInInfo().LOGGED_IN_INFO_KEY));
	}

	/**
	 * This is used for logout only, should not be used at the end of a request. 
	 */
	public static void removeLoggedInInfoFromSession(HttpSession session) {
		session.removeAttribute(new LoggedInInfo().LOGGED_IN_INFO_KEY);
	}

	public Facility getCurrentFacility() {
		return (currentFacility);
	}

	public Provider getLoggedInProvider() {
		return (loggedInProvider);
	}

	public String getInitiatingCode() {
		return (initiatingCode);
	}

	public Security getLoggedInSecurity() {
		return (loggedInSecurity);
	}

	public Locale getLocale() {
		return (locale);
	}

	public HttpSession getSession() {
		return (session);
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public void setCurrentFacility(Facility currentFacility) {
		this.currentFacility = currentFacility;
	}

	public void setLoggedInProvider(Provider loggedInProvider) {
		this.loggedInProvider = loggedInProvider;
	}

	public void setInitiatingCode(String initiatingCode) {
		this.initiatingCode = initiatingCode;
	}

	public void setLoggedInSecurity(Security loggedInSecurity) {
		this.loggedInSecurity = loggedInSecurity;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getLoggedInProviderNo()
	{
		Provider p = getLoggedInProvider();
		if( p != null ) {
			return(p.getProviderNo());
		}
		
		return null;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
