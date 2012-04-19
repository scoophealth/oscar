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


package org.oscarehr.phr.util;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.myoscar_server.ws.AccountWs;
import org.oscarehr.myoscar_server.ws.NoSuchItemException_Exception;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.myoscar_server.ws.PersonTransfer;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.QueueCache;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;

import oscar.OscarProperties;

public final class MyOscarUtils
{
	private static QueueCache<String, Long> userNameToIdCache=new QueueCache<String, Long>(4, 100, DateUtils.MILLIS_PER_HOUR);
	private static QueueCache<Long, String> userIdToNameCache=new QueueCache<Long, String>(4, 100, DateUtils.MILLIS_PER_HOUR);
	private static final DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	
	/**
	 * Note this method must only return the ID, it must never return the PersonTransfer itself since it reads from a cache.
	 * @throws NotAuthorisedException_Exception 
	 */
	public static Long getMyOscarUserId(PHRAuthentication auth, String myOscarUserName)
	{
		int indexOfAt=myOscarUserName.indexOf('@');
		if (indexOfAt!=-1) myOscarUserName = myOscarUserName.substring(0,indexOfAt);
		
		Long myOscarUserId=userNameToIdCache.get(myOscarUserName);
		
		if (myOscarUserId==null)
		{
			AccountWs accountWs=MyOscarServerWebServicesManager.getAccountWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
			PersonTransfer person = null;
			try
			{
				person=accountWs.getPersonByUserName(myOscarUserName, null);
			}
			catch(Exception e)
			{
				MiscUtils.getLogger().error("Myoscar user "+myOscarUserName+" not found ",e);
			}
			if (person!=null)
			{
				myOscarUserId=person.getId();
				userNameToIdCache.put(myOscarUserName, myOscarUserId);
			}
		}
		
		return(myOscarUserId);
	}

	public static Long getMyOscarUserId(HttpSession session, String myOscarUserName)
	{
		PHRAuthentication auth = (PHRAuthentication) session.getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
		return(getMyOscarUserId(auth, myOscarUserName));
	}
	
	/**
	 * Note this method must only return the userName, it must never return the PersonTransfer itself since it reads from a cache.
	 * @throws NotAuthorisedException_Exception 
	 * @throws NoSuchItemException_Exception 
	 */
	public static String getMyOscarUserName(PHRAuthentication auth, Long myOscarUserId) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
	{
		String myOscarUserName=userIdToNameCache.get(myOscarUserId);
		
		if (myOscarUserName==null)
		{
			AccountWs accountWs=MyOscarServerWebServicesManager.getAccountWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
			PersonTransfer person=accountWs.getPerson(myOscarUserId);
			if (person!=null)
			{
				myOscarUserName=person.getUserName();
				userIdToNameCache.put(myOscarUserId, myOscarUserName);
			}
		}
		
		return(myOscarUserName);
	}
	
	public static String getMyOscarUserName(HttpSession session, Long myOscarUserId) throws NotAuthorisedException_Exception, NoSuchItemException_Exception
	{
		PHRAuthentication auth = (PHRAuthentication) session.getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
		return(getMyOscarUserName(auth, myOscarUserId));
	}

	public static Demographic getDemographicByMyOscarUserName(String myOscarUserName)
	{
		DemographicDao demographicDao=(DemographicDao) SpringUtils.getBean("demographicDao");
		Demographic demographic=demographicDao.getDemographicByMyOscarUserName(myOscarUserName);
		return(demographic);
	}
	
	public static PHRAuthentication getPHRAuthentication(HttpSession session)
	{
		return (PHRAuthentication) (session.getAttribute(PHRAuthentication.SESSION_PHR_AUTH));
	}
	
	
	/**
	 * @return true if the myoscar send button should be displayed, false otherwise.
	 */
	public static boolean isVisibleMyOscarSendButton()
	{
		OscarProperties properties=OscarProperties.getInstance();
		String myOscarModule=properties.getProperty("MY_OSCAR");
		if (myOscarModule!=null) myOscarModule=myOscarModule.toLowerCase();
		myOscarModule=StringUtils.trimToNull(myOscarModule);
		boolean module=("yes".equals(myOscarModule) || "true".equals(myOscarModule));
		
		return(module);
	}
	
	public static String getDisabledStringForMyOscarSendButton(PHRAuthentication auth, Integer demographicId)
	{
		boolean enabled=isMyOscarSendButtonEnabled(auth, demographicId);
		
		return(WebUtils.getDisabledString(enabled));
	}

	public static String getDisabledStringForMyOscarSendButton(PHRAuthentication auth, Demographic demographic)
	{
		boolean enabled=isMyOscarSendButtonEnabled(auth, demographic);
		
		return(WebUtils.getDisabledString(enabled));
	}
	
	public static boolean isMyOscarSendButtonEnabled(PHRAuthentication auth, Integer demographicId)
	{
		DemographicDao demographicDao=(DemographicDao) SpringUtils.getBean("demographicDao");
		Demographic demographic=demographicDao.getDemographicById(demographicId);
		
		return(auth!=null && auth.isloggedIn() && demographic!=null && demographic.getMyOscarUserName()!=null);
	}

	public static boolean isMyOscarSendButtonEnabled(PHRAuthentication auth, Demographic demographic)
	{
		return(auth!=null && auth.isloggedIn() && demographic!=null && demographic.getMyOscarUserName()!=null);
	}
	
	public static Long getPatientMyOscarId(PHRAuthentication auth, Integer demographicId)
	{
		Demographic demographic = demographicDao.getDemographicById(demographicId);
		Long patientMyOscarUserId = getMyOscarUserId(auth, demographic.getMyOscarUserName());
		return(patientMyOscarUserId);
	}
}
