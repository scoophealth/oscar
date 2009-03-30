package org.oscarehr.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorUpdateTask;

import oscar.OscarProperties;

public class ContextStartupListener implements javax.servlet.ServletContextListener {
	private static final Logger logger = LogManager.getLogger(ContextStartupListener.class);

	public void contextInitialized(javax.servlet.ServletContextEvent sce) {
		// need tc6 for this?
		// logger.info("Server processes starting. context=" + sce.getServletContext().getContextPath());

		try {
			OscarProperties properties = OscarProperties.getInstance();
			String vmstatLoggingPeriod = properties.getProperty("VMSTAT_LOGGING_PERIOD");
			VmStat.startContinuousLogging(Long.parseLong(vmstatLoggingPeriod));
		} catch (Exception e) {
			logger.error("Unexpected error.", e);
		}

		MiscUtils.setShutdownSignaled(false);
		MiscUtils.registerShutdownHook();

		CaisiIntegratorUpdateTask.startTask();
	}

	public void contextDestroyed(javax.servlet.ServletContextEvent sce) {
		// need tc6 for this?
		// logger.info("Server processes stopping. context=" + sce.getServletContext().getContextPath());

		CaisiIntegratorUpdateTask.stopTask();
		VmStat.stopContinuousLogging();

		try {
			MiscUtils.checkShutdownSignaled();
			MiscUtils.deregisterShutdownHook();
			MiscUtils.setShutdownSignaled(true);
		} catch (ShutdownException e) {
			// do nothing it's okay.
		}
	}
}
