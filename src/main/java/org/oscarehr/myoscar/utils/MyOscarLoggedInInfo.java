/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.myoscar.utils;

import java.io.Serializable;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.oscarehr.myoscar.client.ws_manager.AccountManager;
import org.oscarehr.myoscar.client.ws_manager.MyOscarLoggedInInfoInterface;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.myoscar_server.ws.PersonTransfer3;

import oscar.OscarProperties;

public final class MyOscarLoggedInInfo implements Serializable, MyOscarLoggedInInfoInterface
{
	private static final String myOscarServerBaseUrl = initMyOscarServerBaseUrl();
	private static final String MY_OSCAR_LOGGED_IN_INFO_SESSION_KEY = "MY_OSCAR_LOGGED_IN_INFO_SESSION_KEY";

	private Long loggedInPersonId = null;
	private String loggedInPersonSecurityToken;
	private String loggedInSessionId;
	private Locale locale;

	public MyOscarLoggedInInfo(Long loggedInPersonId, String loggedInPersonSecurityToken, String loggedInSessionId, Locale locale)
	{
		this.loggedInPersonId = loggedInPersonId;
		this.loggedInPersonSecurityToken = loggedInPersonSecurityToken;
		this.loggedInSessionId = loggedInSessionId;
		this.locale=locale;
	}
	
	public static String initMyOscarServerBaseUrl()
	{
		// this is here for compatibility reasons, can remove in the future, changed on 2013-02-04, we use to end the url with /ws
		String temp=(String)OscarProperties.getInstance().get("myoscar_server_base_url");
		if (temp.endsWith("/ws")) temp=temp.substring(0, temp.length()-3);
		return(temp);
	}

	public PersonTransfer3 getLoggedInPerson() throws NotAuthorisedException_Exception
	{
		return(AccountManager.getPerson(this, loggedInPersonId));
	}

	@Override
	public String getLoggedInPersonSecurityToken()
	{
		return(loggedInPersonSecurityToken);
	}

	public void setLoggedInPersonSecurityToken(String loggedInPersonSecurityToken)
	{
		this.loggedInPersonSecurityToken = loggedInPersonSecurityToken;
	}

	@Override
	public Long getLoggedInPersonId()
	{
		return(loggedInPersonId);
	}

	public void setLoggedInPersonId(Long loggedInPersonId)
	{
		this.loggedInPersonId = loggedInPersonId;
	}

	@Override
	public String getLoggedInSessionId()
	{
		return(loggedInSessionId);
	}

	public void setLoggedInSessionId(String loggedInSessionId)
	{
		this.loggedInSessionId = loggedInSessionId;
	}

	public boolean isLoggedIn()
	{
		return(loggedInPersonId != null && loggedInPersonSecurityToken != null);
	}

	@Override
	public String toString()
	{
		return("loggedInPersonId=" + loggedInPersonId);
	}

	public static MyOscarLoggedInInfo getLoggedInInfo(HttpSession session)
	{
		return (MyOscarLoggedInInfo) (session.getAttribute(MY_OSCAR_LOGGED_IN_INFO_SESSION_KEY));
	}

	public static void setLoggedInInfo(HttpSession session, MyOscarLoggedInInfo loggedInInfo)
	{
		session.setAttribute(MY_OSCAR_LOGGED_IN_INFO_SESSION_KEY, loggedInInfo);
	}

	@Override
	public String getServerBaseUrl()
	{
		return(myOscarServerBaseUrl);
	}

	/**
	 * This is duplicate of getServerBaseUrl() but is required because this one is static while the other implements an interface
	 * @return String
	 */
	public static String getMyOscarServerBaseUrl()
	{
		return(myOscarServerBaseUrl);
	}

	@Override
    public Locale getLocale() {
	    return(locale);
    }
}
