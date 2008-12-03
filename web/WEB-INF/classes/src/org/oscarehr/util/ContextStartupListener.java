package org.oscarehr.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorUpdateTask;

public class ContextStartupListener implements javax.servlet.ServletContextListener
{
	private static final Logger logger = LogManager.getLogger(ContextStartupListener.class);

	public void contextInitialized(javax.servlet.ServletContextEvent sce)
	{
// need tc6 for this?
//		logger.info("Server processes starting. context=" + sce.getServletContext().getContextPath());

		CaisiIntegratorUpdateTask.startTask();
	}

	public void contextDestroyed(javax.servlet.ServletContextEvent sce)
	{
// need tc6 for this?
//		logger.info("Server processes stopping. context=" + sce.getServletContext().getContextPath());

		CaisiIntegratorUpdateTask.stopTask();
	}
}
