package org.oscarehr.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorUpdateTask;

import oscar.OscarProperties;

public class ContextStartupListener implements javax.servlet.ServletContextListener {
	private static final Logger logger = LogManager.getLogger(ContextStartupListener.class);
	private static String contextPath=null;

	public void contextInitialized(javax.servlet.ServletContextEvent sce) {
		// need tc6 for this?
		// String contextPath=sce.getServletContext().getContextPath();

		// hack to get context path until tc6 is our standard.
		// /data/cvs/caisi_utils/apache-tomcat-5.5.27/webapps/oscar
		contextPath=sce.getServletContext().getRealPath("");
		int lastSlash=contextPath.lastIndexOf('/');
		contextPath=contextPath.substring(lastSlash+1);

		 logger.info("Server processes starting. context=" + contextPath);

		 MiscUtils.addLoggingOverrideConfiguration(contextPath);

		try {
			OscarProperties properties = OscarProperties.getInstance();
			String vmstatLoggingPeriod = properties.getProperty("VMSTAT_LOGGING_PERIOD");
			VMStat.startContinuousLogging(Long.parseLong(vmstatLoggingPeriod));
		} catch (Exception e) {
			logger.error("Unexpected error.", e);
		}

		MiscUtils.setShutdownSignaled(false);
		MiscUtils.registerShutdownHook();

		CaisiIntegratorUpdateTask.startTask();

		logger.info("Server processes starting completed. context=" + contextPath);
	}

	public void contextDestroyed(javax.servlet.ServletContextEvent sce) {
		// need tc6 for this?
		// logger.info("Server processes stopping. context=" + sce.getServletContext().getContextPath());
		logger.info("Server processes stopping. context=" + contextPath);

		CaisiIntegratorUpdateTask.stopTask();
		VMStat.stopContinuousLogging();

		try {
			MiscUtils.checkShutdownSignaled();
			MiscUtils.deregisterShutdownHook();
			MiscUtils.setShutdownSignaled(true);
		} catch (ShutdownException e) {
			// do nothing it's okay.
		}
	}
}
