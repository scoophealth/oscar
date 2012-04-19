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


package org.oscarehr.common.web;

import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import oscar.OscarProperties;

public final class OscarSpringContextLoader extends ContextLoader {
	
	private static final Logger log = MiscUtils.getLogger();
	private static final String CONTEXTNAME = "classpath:applicationContext";
	private static final String PROPERTYNAME = "ModuleNames";

	@Override
    protected WebApplicationContext createWebApplicationContext(ServletContext servletContext, ApplicationContext parent) throws BeansException {
		String contextClassName = servletContext.getInitParameter(CONTEXT_CLASS_PARAM);

        Class<?> contextClass;
        if (contextClassName != null) {
			try {
				contextClass = Class.forName(contextClassName, true, Thread.currentThread().getContextClassLoader());
			} catch (ClassNotFoundException ex) {
				throw new ApplicationContextException("Failed to load context class [" + contextClassName + "]", ex);
			}
			
			if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
				throw new ApplicationContextException("Custom context class [" + contextClassName + "] is not of type ConfigurableWebApplicationContext");
			}
		} else {
            contextClass = XmlWebApplicationContext.class;
        }

		ConfigurableWebApplicationContext wac = (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);
		wac.setParent(parent);
		wac.setServletContext(servletContext);

		// to load various contexts, we need to get Modules property
		String modules = (String) OscarProperties.getInstance().get(PROPERTYNAME);
		String[] moduleList = new String[0];

		if (modules != null) {
			modules = modules.trim();
			
			if (modules.length() > 0) {
				moduleList = modules.split(",");
			}
		}

		// now we create an list of application context file names
		ArrayList<String> configLocations = new ArrayList<String>();

        // always load applicationContext.xml
        configLocations.add(CONTEXTNAME + ".xml");

        for (String s : moduleList) {
            configLocations.add(CONTEXTNAME + s + ".xml");
		}

        for (String s : configLocations) {
            log.info("Preparing " + s);            
        }

		wac.setConfigLocations(configLocations.toArray(new String[0]));
		wac.refresh();
		
        if (SpringUtils.beanFactory==null) SpringUtils.beanFactory=wac;
        
		return wac;
	}
}
