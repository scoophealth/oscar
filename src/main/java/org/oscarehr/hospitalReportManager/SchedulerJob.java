/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
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
