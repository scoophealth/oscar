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

	private static boolean shutdownSignaled = false;
	private static Thread shutdownHookThread = null;

	private static class ShutdownHookThread extends Thread {
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
	 * This mentod should only ever be called by a context startup listener. Other than that, the shutdown signal should be set by the shutdown hook.
	 */
	protected static void setShutdownSignaled(boolean shutdownSignaled) {

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
	 * This method should only really be called once per context in the context startup listener.	 * @param contextPath
	 */
	protected static void addLoggingOverrideConfiguration(String contextPath)
	{
		String configLocation = System.getProperty("log4j.override.configuration");
		if (configLocation != null)
		{
			if (contextPath != null)
			{
				if (contextPath.length() > 0 && contextPath.charAt(0) == '/') contextPath = contextPath.substring(1);
				if (contextPath.length() > 0 && contextPath.charAt(contextPath.length() - 1) == '/')
					contextPath = contextPath.substring(0, contextPath.length() - 2);
			}

			String resolvedLocation = configLocation.replace("${contextName}", contextPath);
			getLogger().info("loading additional override logging configuration from : "+resolvedLocation);
			DOMConfigurator.configureAndWatch(resolvedLocation);
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
}
