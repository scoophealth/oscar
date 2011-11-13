package org.oscarehr.phr.util;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateUtils;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.myoscar_server.ws.AccountWs;
import org.oscarehr.myoscar_server.ws.NoSuchItemException_Exception;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.myoscar_server.ws.PersonTransfer;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.TimeClearedHashMap;

public class MyOscarUtils
{
	private static TimeClearedHashMap<String, Long> userNameToIdCache=new TimeClearedHashMap<String, Long>(DateUtils.MILLIS_PER_DAY, DateUtils.MILLIS_PER_HOUR);
	private static TimeClearedHashMap<Long, String> userIdToNameCache=new TimeClearedHashMap<Long, String>(DateUtils.MILLIS_PER_DAY, DateUtils.MILLIS_PER_HOUR);
	
	/**
	 * Note this method must only return the ID, it must never return the PersonTransfer itself since it reads from a cache.
	 * @throws NotAuthorisedException_Exception 
	 */
	public static Long getMyOscarUserId(PHRAuthentication auth, String myOscarUserName) throws NotAuthorisedException_Exception
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

	public static Long getMyOscarUserId(HttpSession session, String myOscarUserName) throws NotAuthorisedException_Exception
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
	
}