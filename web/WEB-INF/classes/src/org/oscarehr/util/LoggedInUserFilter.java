/*
* Copyright (c) 2007-2009. CAISI, Toronto. All Rights Reserved.
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
* CAISI, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;

public class LoggedInUserFilter implements javax.servlet.Filter
{
	private static Logger logger = MiscUtils.getLogger();

	/**
	 * The loggedInProvider fields should only be used if this is a user based thread, i.e. a thread handling a user request.
	 * If this is an internal system thread, those fields should be ignored and the 
	 * internalThreadDescription should be used instead. The currentFacility
	 * field maybe applicable to both the logged in user and thread but it's also
	 * possible that a thread is running for all facilities or that a logged in user
	 * is working on behalf of no facility i.e. system administration only.
	 */
	public static class LoggedInInfo
	{
		public org.oscarehr.common.model.Facility currentFacility=null;
		public Provider loggedInProvider=null;
		public String internalThreadDescription=null;
		
		public String toString()
		{
			StringBuilder sb=new StringBuilder();
			
			sb.append("currentFacility=");
			if (currentFacility==null) sb.append("null");
			else sb.append(currentFacility.getId());
			
			sb.append(", loggedInProvider=");
			if (loggedInProvider==null) sb.append("null");
			else sb.append(loggedInProvider.getProviderNo());
			
			sb.append(", internalThreadDescription=");
			sb.append(internalThreadDescription);
			
			return(sb.toString());
		}
	}
	
	public static final ThreadLocal<LoggedInInfo> loggedInInfo = new ThreadLocal<LoggedInInfo>();

	/**
	 * This method is intended to be used by timer task or background threads to 
	 * setup the thread local loggedInInfo. It should do basic checks to see if 
	 * there's lingering data, then set the thread local internalThreadDescription 
	 * to the name of the class that called this method, i.e. your thread class name.
	 */
	public static void setLoggedInInfoToCurrentClassName()
	{
		checkForLingeringData();
		
		// get callers class
		Throwable t = new Throwable();
		StackTraceElement[] ste = t.getStackTrace();
		Class<?> caller = ste[1].getClass();

		// create and set new thread local
		LoggedInInfo x = new LoggedInInfo();
		x.internalThreadDescription=caller.getName();
		LoggedInUserFilter.loggedInInfo.set(x);
	}
	
	private static void checkForLingeringData()
	{
		LoggedInInfo x = loggedInInfo.get();
		if (x != null) logger.warn("Logged in info should be null on new requests but it wasn't. oldUser=" + x);				
	}
	
	public void init(FilterConfig filterConfig) throws ServletException
	{
		// nothing
	}

	public void doFilter(ServletRequest tmpRequest, ServletResponse tmpResponse, FilterChain chain) throws IOException, ServletException
	{
		try
		{
			checkForLingeringData();

			// set new / current data
			HttpServletRequest request=(HttpServletRequest) tmpRequest;
			HttpSession session=request.getSession();
			LoggedInInfo x=new LoggedInInfo();
			x.currentFacility=(Facility) session.getAttribute(SessionConstants.CURRENT_FACILITY);
			x.loggedInProvider=(Provider) session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
			loggedInInfo.set(x);
			
			chain.doFilter(tmpRequest, tmpResponse);
		}
		finally
		{
			loggedInInfo.remove();
		}
	}

	public void destroy()
	{
		// can't think of anything to do right now.
	}
}
