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

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Security;


/**
 * The loggedInProvider fields should only be used if this is a user based thread, i.e. a thread handling a user request.
 * If this is an internal system thread, those fields should be ignored and left null.
 * The initiatingCode field can be used for both internal threads as well as user requests.
 * It should signify where the code started for the most part, i.e. the thread class name,
 * or the jsp name, or web service name and method.
 */
public final class LoggedInInfo
{
	private static Logger logger=MiscUtils.getLogger();
	public static final ThreadLocal<LoggedInInfo> loggedInInfo = new ThreadLocal<LoggedInInfo>();

	public HttpSession session=null;
	public Facility currentFacility=null;
	public Provider loggedInProvider=null;
	public String initiatingCode=null;
	public Security loggedInSecurity=null;
	public Locale locale=null;
		
	@Override
    public String toString()
	{
		return(ReflectionToStringBuilder.toString(this));
	}
	
	/**
	 * This method is intended to be used by timer task or background threads to 
	 * setup the thread local loggedInInfo. It should do basic checks to see if 
	 * there's lingering data, then set the thread local internalThreadDescription 
	 * to the name of the class that called this method, i.e. your thread class name.
	 */
	public static void setLoggedInInfoToCurrentClassAndMethod()
	{
		checkForLingeringData();
		
		// get caller
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();

		// create and set new thread local
		LoggedInInfo x = new LoggedInInfo();
		x.initiatingCode=ste[2].getClassName()+'.'+ste[2].getMethodName();
		loggedInInfo.set(x);
	}
	
	protected static void checkForLingeringData()
	{
		LoggedInInfo x = loggedInInfo.get();
		if (x != null) logger.warn("Logged in info should be null on new requests but it wasn't. oldUser=" + x);				
	}

}
