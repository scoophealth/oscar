/*
* 
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

package org.caisi.common.web;

import java.util.Properties;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.common.OscarProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.struts.ActionServletAwareProcessor;
import org.springframework.web.struts.ContextLoaderPlugIn;

public class CaisiSpringPlugin extends ContextLoaderPlugIn {

	private static Log log = LogFactory.getLog(ContextLoaderPlugIn.class);

	private String CAISI_CONTEXT_PATH = "/WEB-INF/caisiApplicationContext.xml";

	private boolean getFlag() {
		Properties p2 = OscarProperties.getProperties();
		if (p2 == null) {
			log.info("can't find oscar property file");
			return false;
		}
		String flag = p2.getProperty("caisi", "");
		if (flag == null)
			return false;
		if ("on".equalsIgnoreCase(flag) || "true".equalsIgnoreCase(flag)
				|| "yes".equalsIgnoreCase(flag))
			return true;
		else
			return false;
	}

	protected WebApplicationContext createWebApplicationContext(
			WebApplicationContext arg0) throws BeansException {
		if (!getFlag()) {
			log.info("create spring application context");
			return super.createWebApplicationContext(arg0);
		} else {
			log.info("creat caisi spring context");
			return createCaisiWebApplicationContext(arg0);
		}
	}

	protected WebApplicationContext createCaisiWebApplicationContext(
			WebApplicationContext parent) throws BeansException {

		if (logger.isDebugEnabled()) {
			logger.debug("ContextLoaderPlugIn for Struts ActionServlet '"
					+ getServletName() + "', module '" + getModulePrefix()
					+ "' will try to create custom WebApplicationContext "
					+ "context of class '" + getContextClass().getName()
					+ "', using parent context [" + parent + "]");
		}
		if (!ConfigurableWebApplicationContext.class
				.isAssignableFrom(getContextClass())) {
			throw new ApplicationContextException(
					"Fatal initialization error in ContextLoaderPlugIn for Struts ActionServlet '"
							+ getServletName()
							+ "', module '"
							+ getModulePrefix()
							+ "': custom WebApplicationContext class ["
							+ getContextClass().getName()
							+ "] is not of type ConfigurableWebApplicationContext");
		}

		ConfigurableWebApplicationContext wac = (ConfigurableWebApplicationContext) BeanUtils
				.instantiateClass(getContextClass());
		wac.setParent(parent);
		wac.setServletContext(getServletContext());
		wac.setNamespace(getNamespace());

		String ctxLocations = getContextConfigLocation();
		if (ctxLocations == null) {
			ctxLocations = XmlWebApplicationContext.DEFAULT_CONFIG_LOCATION;
		}
		wac.setConfigLocations(StringUtils.tokenizeToStringArray(ctxLocations
				+ "," + CAISI_CONTEXT_PATH,
				ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS));

		wac.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {
			public void postProcessBeanFactory(
					ConfigurableListableBeanFactory beanFactory) {
				beanFactory
						.addBeanPostProcessor(new ActionServletAwareProcessor(
								getActionServlet()));
			}
		});
		wac.refresh();
		return wac;
	}

}
