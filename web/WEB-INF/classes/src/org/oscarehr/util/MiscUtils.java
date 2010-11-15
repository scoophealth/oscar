/*
 * Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * When using the shutdown hook...
 * <br /><br />
 * In the context of a normal JVM, you only need to use
 * the register and deregister methods where ever appropriate.
 * In your code you can periodically checkShutdownSignaled on long
 * running threads.
 * <br /><br />
 * In the conext of a application context such as a servlet
 * container. You should register and deregister in the context
 * startup/shutdown methods. In addition you should manually flag
 * set shutdownSignaled=true upon context shutdown as the jvm
 * itself may not be shutting down and no shutdown hook signal
 * maybe sent. Similarly you should set the shutdownSignaled=false
 * upon startup as it may have been set true by a previous context stop
 * even though the jvm itself has not restarted.
 */
public class MiscUtils {
	
	public static final String ENCODING = "UTF-8";
	private static boolean shutdownSignaled = false;
	private static Thread shutdownHookThread = null;

	private static class ShutdownHookThread extends Thread {
		// can't have override until everyone uses jdk1.6
		// @Override
        public void run() {
			shutdownSignaled = true;
		}
	}

	public static void checkShutdownSignaled() throws ShutdownException {
		if (shutdownSignaled) throw (new ShutdownException());
	}

	/**
	 * This method should in most cases only be called by the context startup listener.
	 */
	public static synchronized void registerShutdownHook() {
		if (shutdownHookThread == null) {
			shutdownHookThread = new ShutdownHookThread();
			Runtime.getRuntime().addShutdownHook(shutdownHookThread);
		}
	}

	public static synchronized void deregisterShutdownHook()
	{
		if (shutdownHookThread != null) {
			Runtime.getRuntime().removeShutdownHook(shutdownHookThread);
			shutdownHookThread = null;
		}
	}

	/**
	 * This menthod should only ever be called by a context startup listener. Other than that, the shutdown signal should be set by the shutdown hook.
	 */
	protected static void setShutdownSignaled(boolean shutdownSignaled) {
		MiscUtils.shutdownSignaled=shutdownSignaled;
	}

	public static byte[] propertiesToXmlByteArray(Properties p) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		p.storeToXML(os, null);
		return (os.toByteArray());
	}

	public static Properties xmlByteArrayToProperties(byte[] b) throws IOException {
		Properties p = new Properties();

		ByteArrayInputStream is = new ByteArrayInputStream(b);
		p.loadFromXML(is);

		return (p);
	}

	/**
	 * This method will set the calendar to the beginning of the month, i.e. day=1, hour=0, minute=0, sec=0, ms=0. It will return the same instance passed in (not a clone of it).
	 */
	public static Calendar setToBeginningOfMonth(Calendar cal) {
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return (setToBeginningOfDay(cal));
	}

	/**
	 * This method will set the calenders hour/min/sec//milliseconds all to 0.
	 */
	public static Calendar setToBeginningOfDay(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		// force calculation / materialisation of actual time.
		cal.getTimeInMillis();

		return (cal);
	}

	/**
	 * This method should only really be called once per context in the context startup listener.
	 * 
	 * The purpose of this is to allow customisations to the logging on a per-deployment basis with out
	 * needing to commit the configuration to cvs while at the same time allowing you to keep your
	 * configuration through multiple deployments. As an example, if you modified the WEB-INF/classes/log4j.xml
	 * either you have to commit that and everyone gets your configuration, or you don't commit it,
	 * every new deploy will over write changes you made locally to your log4j.xml. This helper method alleviates this problem.
	 * 
	 * The functionality of this is as follows :
	 * 
	 * The system configuration parameter "log4j.override.configuration" specifies the file to use
	 * to override overlay on top of the default log4j.xml file. This can be a relative or absolute path.
	 * An example maybe "/home/foo/my_override_log4j.xml"
	 * 
	 * The filename specified is allowed to have a special placeholder ${contextName}, this allows the
	 * system variable to be used when multiple oscar contexts exist. As an example it maybe
	 * "/home/foo/${contextName}_log4.xml". During runtime, when each context startup calls this method
	 * the ${contextName} is replaced with the contextPath. So as an example of you had 2 contexts called
	 * "asdf" and "zxcv" respectively, it will look for /home/foo/asdf_log4j.xml and /home/foo/zxcv_log4j.xml.
	 */
	protected static void addLoggingOverrideConfiguration(String contextPath)
	{
		String configLocation = System.getProperty("log4j.override.configuration");
		if (configLocation != null)
		{
			if (configLocation.contains("${contextName}"))
			{	
				if (contextPath != null)
				{
					if (contextPath.length() > 0 && contextPath.charAt(0) == '/') contextPath = contextPath.substring(1);
					if (contextPath.length() > 0 && contextPath.charAt(contextPath.length() - 1) == '/')
						contextPath = contextPath.substring(0, contextPath.length() - 2);
				}
				
				configLocation=configLocation.replace("${contextName}", contextPath);
			}
			
			getLogger().info("loading additional override logging configuration from : "+configLocation);
			DOMConfigurator.configureAndWatch(configLocation);
		}
	}

	/**
	 * This method will return a logger instance which has the name based on the class that's calling this method.
	 */
	public static Logger getLogger()
	{
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		String caller = ste[2].getClassName();
		return(Logger.getLogger(caller));
	}


    //return a vector which contains distinctive string elements
    public static Vector findUniqueElementVector (Vector v){
        Vector retVec=new Vector();
        for(int i=0;i<v.size();i++){
            if(!retVec.contains(v.get(i)))
                retVec.add(v.get(i));
        }
        return retVec;
    }
}
