package org.oscarehr.util;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;

/**
 * The loggedInProvider fields should only be used if this is a user based thread, i.e. a thread handling a user request.
 * If this is an internal system thread, those fields should be ignored and left null.
 * The initiatingCode field can be used for both internal threads as well as user requests.
 * It should signify where the code started for the most part, i.e. the thread class name,
 * or the jsp name, or web service name and method.
 */
public class LoggedInInfo
{
	private static Logger logger=MiscUtils.getLogger();
	public static final ThreadLocal<LoggedInInfo> loggedInInfo = new ThreadLocal<LoggedInInfo>();

	public Facility currentFacility=null;
	public Provider loggedInProvider=null;
	public String initiatingCode=null;
		
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