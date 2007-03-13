/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.task;

import java.util.Date;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.BedDemographicManager;
import org.oscarehr.PMmodule.service.BedManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;

public class BedProgramDischargeTask extends TimerTask {

	private static final Log log = LogFactory.getLog(BedProgramDischargeTask.class);

	// TODO IC Bedlog bedProgram.getDischargeTime();
	private static final String DISCHARGE_TIME = "8:00 AM";
	private static final long PERIOD = 3600000;

	private ProgramManager programManager;
	private BedManager bedManager;
	private BedDemographicManager bedDemographicManager;
	private AdmissionManager admissionManager;

	public void setProgramManager(ProgramManager programManager) {
		this.programManager = programManager;
	}
	
	public void setBedManager(BedManager bedManager) {
	    this.bedManager = bedManager;
    }

	public void setBedDemographicManager(BedDemographicManager bedDemographicManager) {
		this.bedDemographicManager = bedDemographicManager;
	}

	
	@Override
	public void run() {
		log.info("start bed program discharge task");

		Program[] bedPrograms = programManager.getBedPrograms();

		for (Program bedProgram : bedPrograms) {
			Date dischargeTime = DateTimeFormatUtils.getTimeFromString(DISCHARGE_TIME);
			Date previousExecutionTime = DateTimeFormatUtils.getTimeFromLong(scheduledExecutionTime() - PERIOD);
			Date currentExecutionTime = DateTimeFormatUtils.getTimeFromLong(scheduledExecutionTime());
			
			// previousExecutionTime < dischargeTime <= currentExecutionTime
			if (previousExecutionTime.before(dischargeTime) && (dischargeTime.equals(currentExecutionTime) || dischargeTime.before(currentExecutionTime))) {
				Bed[] reservedBeds = bedManager.getBedsByProgram(bedProgram.getId(), true);
				
				for (Bed reservedBed : reservedBeds) {
					BedDemographic bedDemographic = bedDemographicManager.getBedDemographicByBed(reservedBed.getId());
					
					if (bedDemographic.isExpired()) {
						try {
							admissionManager.processDischargeToCommunity(Program.DEFAULT_COMMUNITY_PROGRAM_ID, bedDemographic.getId().getDemographicNo(), Provider.SYSTEM_PROVIDER_NO, "bed reservation ended - automatically discharged","0");
						} catch (AdmissionException e) {
							log.error("Error discharging to community", e);
						}
					}
                }
			}
		}
		
		log.info("finish bed program discharge task");
	}

}