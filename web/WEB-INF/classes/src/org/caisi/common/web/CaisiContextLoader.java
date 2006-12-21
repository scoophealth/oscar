package org.caisi.common.web;

import javax.servlet.ServletContext;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class CaisiContextLoader extends ContextLoader {

	private String CAISI_CONTEXT_PATH = "/WEB-INF/caisiApplicationContext.xml";

	protected WebApplicationContext createWebApplicationContext(ServletContext servletContext, ApplicationContext parent) throws BeansException {
		Class contextClass = DEFAULT_CONTEXT_CLASS;
		
		String contextClassName = servletContext.getInitParameter(CONTEXT_CLASS_PARAM);
		
		if (contextClassName != null) {
			try {
				contextClass = Class.forName(contextClassName, true, Thread.currentThread().getContextClassLoader());
			} catch (ClassNotFoundException ex) {
				throw new ApplicationContextException("Failed to load context class [" + contextClassName + "]", ex);
			}
			if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
				throw new ApplicationContextException("Custom context class [" + contextClassName + "] is not of type ConfigurableWebApplicationContext");
			}
		}

		ConfigurableWebApplicationContext wac = (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);
		
		wac.setParent(parent);
		wac.setServletContext(servletContext);
		
		String configLocation = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);

		if (configLocation == null) {
			configLocation = XmlWebApplicationContext.DEFAULT_CONFIG_LOCATION;
		}
		
		wac.setConfigLocations(StringUtils.tokenizeToStringArray(configLocation + "," + CAISI_CONTEXT_PATH, ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS));
		wac.refresh();
		
		return wac;
	}

}
