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
