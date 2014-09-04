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

package org.oscarehr.PMmodule.task;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.common.model.Admission;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class AnonymousClientDischargeTask extends TimerTask {

	private static final Logger logger = MiscUtils.getLogger();

	public void run() {
		try {
			AdmissionManager admissionManager = (AdmissionManager) SpringUtils.getBean("admissionManager");

			List<Admission> admissions = admissionManager.getActiveAnonymousAdmissions();

			for (Admission admission : admissions) {
				if ((new Date().getTime() - admission.getAdmissionDate().getTime()) > (1000 * 60 * 60 * 24)) {
					logger.info("Admission ready for discharge.");
					admission.setDischargeDate(new Date());
					admission.setDischargeNotes("Auto-Discharge");
					admission.setAdmissionStatus(Admission.STATUS_DISCHARGED);
					admissionManager.saveAdmission(admission);
				} else {
					logger.info("Admission still under 24hrs - " + admission.getAdmissionDate());
				}
			}
		} catch (Exception e) {
			logger.error("Error", e);
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}

}
