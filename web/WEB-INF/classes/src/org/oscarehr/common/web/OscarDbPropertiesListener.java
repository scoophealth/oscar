/*
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for 
 * Centre for Research on Inner City Health, St. Michael's Hospital, 
 * Toronto, Ontario, Canada 
 */
package org.oscarehr.common.web;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.common.OscarProperties;

public class OscarDbPropertiesListener implements ServletContextListener {
	
	private static Log log = LogFactory.getLog(OscarDbPropertiesListener.class);

	public void contextInitialized(ServletContextEvent event) {
		String contextPath = event.getServletContext().getInitParameter("contextPath");
		
		if (contextPath != null) {
			log.info(" found contextPath override.");
		} else {
			String path = event.getServletContext().getRealPath("/");
			String[] splitPath = path.split(String.valueOf(File.separatorChar));
			contextPath = splitPath[splitPath.length - 1];
		}

		log.info("using context path (" + contextPath + ")");

		Properties p = System.getProperties();
		String workingDir = (String) p.get("user.home");
		String propName = contextPath + ".properties";
		File f = new File(workingDir, propName);
		
		if (!f.exists()) {
			log.error("properties file \"" + propName + "\" does not exist in \"" + workingDir + "\"...cannot load db properties.");
			throw new RuntimeException(f.getAbsolutePath() + " not found");
		}
		
		p = new Properties();
		
		try {
			p.load(new FileInputStream(f));
		} catch (Exception e) {
			throw new RuntimeException("Unable to read properties file " + f.getAbsolutePath());
		}
		
		OscarProperties.setProperties(p);
	}

	public void contextDestroyed(ServletContextEvent arg0) {}

}
