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

public class SchedulerJob extends TimerTask {
	private static Logger logger=MiscUtils.getLogger();
	
	private static long lastRun = new Date().getTime();
	private static boolean firstRun = true;

	@Override
	public void run() {
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
				if (lastRun + intervalTime <= new Date().getTime()) {
					// Run now
					SFTPConnector.startAutoFetch();
				}
			} else {
				SFTPConnector.startAutoFetch();
				SchedulerJob.firstRun = false;
			}

			lastRun = new Date().getTime();
			logger.debug("===== HRM JOB RUNNING....");
		} catch (Exception e) {
			logger.error("Error", e);
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}
}
