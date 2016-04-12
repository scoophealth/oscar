/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.hospitalReportManager;

import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
* This job is set to run every minute, and the thing that really controls it is hrm_interval in the property table.
**/
public class SchedulerJob extends TimerTask {
	private static Logger logger=MiscUtils.getLogger();
	
	private static Date lastRun = new Date();
	private static boolean firstRun = true;

	@Override
	public void run() {
		Date startTime = new Date();
		try {
			UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");

			UserProperty hrmInterval = userPropertyDao.getProp("hrm_interval");

			Integer intervalTime = 1800000;

			try {
				if (hrmInterval != null && hrmInterval.getValue() != null && hrmInterval.getValue().trim().length() > 0) {
					intervalTime = Integer.parseInt(hrmInterval.getValue()) * 60000;
				}
			} catch (Exception e) {
				intervalTime = 1800000;
			}


			if (!firstRun) {
				long tmp = lastRun.getTime() + intervalTime;
				Date nowDt = new Date();
				long now= nowDt.getTime();

				if ((tmp/1000) <= ((now/1000)+1)) {
					logger.info("Starting HRM fetch");
					// Run now
					new SFTPConnector().startAutoFetch(null);
					//I want the last run to be when the job started, not the random point it ended.
					//so that I can have consistent polling required for conformance.
					lastRun = startTime;
				}
			} else {
				logger.info("first run");
				new SFTPConnector().startAutoFetch(null);
				SchedulerJob.firstRun = false;
				lastRun = startTime;
			}

			logger.debug("===== HRM JOB DONE RUNNING....");
		} catch (Exception e) {
			logger.error("Error", e);
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}
}
