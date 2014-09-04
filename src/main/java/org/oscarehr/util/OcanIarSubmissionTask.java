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

package org.oscarehr.util;

import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.web.OcanReportUIBean;

public class OcanIarSubmissionTask extends TimerTask {

	private static final Logger logger = MiscUtils.getLogger();

	@Override
	public void run() {
		try {
			logger.info("Running OCAN IAR Submission Task");
			Facility currentWorkingFacility=null;

			FacilityDao facilityDao = (FacilityDao) SpringUtils.getBean("facilityDao");
			List<Facility> facilities = facilityDao.findAll(null);
			
			// odd, by having this for loop here it means we only submit the first facility which has ocan enabled? 
			// sounds weird but I'm not going to change the behaviour right now...
			for (Facility facility : facilities) {
				if (!facility.isDisabled() && facility.isEnableOcanForms() && Integer.valueOf(facility.getOcanServiceOrgNumber()).intValue() != 0) {
					currentWorkingFacility=facility;
					break;
				}
			}

			int submissionId_full = OcanReportUIBean.sendSubmissionToIAR(currentWorkingFacility,OcanReportUIBean.generateOCANSubmission(currentWorkingFacility.getId(), "FULL","all"));
			logger.info("FULL OCAN upload Completed: submissionId=" + submissionId_full);

			int submissionId_self = OcanReportUIBean.sendSubmissionToIAR(currentWorkingFacility,OcanReportUIBean.generateOCANSubmission(currentWorkingFacility.getId(), "SELF","all"));
			logger.info("SELF OCAN upload Completed: submissionId=" + submissionId_self);

			int submissionId_core = OcanReportUIBean.sendSubmissionToIAR(currentWorkingFacility,OcanReportUIBean.generateOCANSubmission(currentWorkingFacility.getId(), "CORE","all"));
			logger.info("CORE OCAN upload Completed: submissionId=" + submissionId_core);
		} catch (Exception e) {
			logger.error("Error", e);
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}
}
