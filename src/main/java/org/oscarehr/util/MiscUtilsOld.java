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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
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
 * 
 * -----------------------
 * 
 * This file has been renamed to "Old" because this file should no longer be enhanced. A common version of this class
 * is made available from the Utils package. There maybe some methods left here which don't entirely make sense
 * or don't make sense in the context of a general purpose project agnostic utility class. This class still exists as "Old" so
 * we can slowly refactor the non sensical code to use the new common utilities. Any remaining methods which do make sense
 * should then me moved to a generic Oscar Utility class or similar. If the method makes sense in a project
 * agnostic fashion, then it should be moved to the util project itself. 
 */
public final class MiscUtilsOld {
	
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
		MiscUtilsOld.shutdownSignaled=shutdownSignaled;
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
	/*
	 * Null safe getSize()
	 */
	public static Integer getSize(List list){
		if (list == null) return null;
		return list.size();
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
    
    /**
     * You better have a good reason for using this. This ain't something you normally do.
     */
	public static final byte[] serialiseToByteArray(Serializable s) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(s);

		return (baos.toByteArray());
	}

    /**
     * You better have a good reason for using this. This ain't something you normally do.
     */
	public static final Serializable deserialiseFromByteArray(byte b[]) throws IOException, ClassNotFoundException {
		return ((Serializable) (new ObjectInputStream(new ByteArrayInputStream(b))).readObject());
	}
	
	public static String getUserNameNoDomain(String s)
	{
		if (s==null) return(null);
		
		int indexOfAt=s.indexOf('@');
		if (indexOfAt!=-1) s = s.substring(0,indexOfAt);
		return(s);
	}
}
