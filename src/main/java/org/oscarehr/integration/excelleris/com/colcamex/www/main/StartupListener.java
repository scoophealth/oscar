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
package org.oscarehr.integration.excelleris.com.colcamex.www.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import org.oscarehr.integration.excelleris.com.colcamex.www.core.ControllerHandler;
import org.oscarehr.integration.excelleris.com.colcamex.www.core.PollTimer;

import oscar.OscarProperties;

/**
 * @author Dennis Warren Colcamex Resources
 * 
 * Major Contributors: 
 *  OSCARprn
 *  NERD
 *   
 * This community edition of Expedius is for use at your own risk, without warranty, and 
 * support. 
 * 
 */
public class StartupListener implements ServletContextListener {

	private static Logger logger = Logger.getLogger("ExpediusStartupListener");
	private static Properties properties;
	private static final String keyFilePath = "./keys.txt";
	
    /**
     * Default constructor. 
     */
    public StartupListener() {
        // default constructor
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event) {

    	_init(OscarProperties.getInstance());

		ControllerHandler controllerHandler = null;
		
		if( properties != null && Boolean.parseBoolean( properties.getProperty("EXCELLERIS") )) {
			controllerHandler = ControllerHandler.getInstance(properties);
		} else {
			logger.error("Failed to start autodownloader. Is EXCELLERIS set to true? Is Oscar Properties accessable?");
		}

		if(controllerHandler != null) {
			controllerHandler.start();
			controllerHandler.getMessageHandler().setServiceStatus(Boolean.TRUE);
		}

    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event) {
    	ScheduledExecutorService scheduler = PollTimer.getScheduler();
		if(scheduler != null) {
			scheduler.shutdown();
		}
		
		logger.info(" Autolab download server shutdown occurred.");
    }
    
    private void _init(Properties properties){ //, String context) {	

		if(properties != null) {

			try {
	            verifyProperties(properties);
            } catch (IOException e) {
            	logger.error("Failed to initialize properties file. Verification failed.");
            	StartupListener.properties = null;
            }

			StartupListener.properties = properties;
			
		} else {
			logger.error("Properties file path is missing.");
		}
	}
    
    private static final boolean verifyProperties(Properties properties) throws IOException {

    	String line = "";
    	ArrayList<String> lines = new ArrayList<String>();
    	BufferedReader bufferedReader = new BufferedReader( new FileReader(keyFilePath) );
		while ( ( line = bufferedReader.readLine().trim() ) != null) {
			lines.add(line);
		}
		
		String[] keys = (String[]) lines.toArray();
    	
		String[] array1 = null;
		String[] array2 = null;
		boolean b = true;
		
		if(properties != null) {
			array1  = properties.keySet().toArray(new String[]{});
			Arrays.sort(array1 ); 
			Arrays.sort(keys);
			array2 = keys;		
		}
		
		if (array1 != null && array2 != null){

			if (array1.length != array2.length) {
				b = false;
			} else {
				for (int i = 0; i < array2.length; i++) {
			
					if (! array2[i].equalsIgnoreCase(array1[i]) ) {						
						b = false;    
					}                 
				}
			}
		}else{
			b = false;
		}
		
		if(bufferedReader != null) {
			bufferedReader.close();
		}
	
		return b;
	}
    
}
