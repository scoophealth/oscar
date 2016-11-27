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
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.FunctionalCentreDischargeException;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.common.model.Bed;
import org.oscarehr.common.model.BedDemographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.BedDemographicManager;
import org.oscarehr.managers.BedManager;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.MiscUtilsOld;
import org.oscarehr.util.ShutdownException;

public class BedProgramDischargeTask extends TimerTask {

	private static final Logger log=MiscUtils.getLogger();

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
		try {
            log.debug("start bed program discharge task");

            Program[] bedPrograms = programManager.getBedPrograms();

            if(bedPrograms == null) {
            	log.error("getBedPrograms returned null");
            	return;
            }
            
            for (Program bedProgram : bedPrograms) {
            	MiscUtilsOld.checkShutdownSignaled();
            	
                Date dischargeTime = DateTimeFormatUtils.getTimeFromString(DISCHARGE_TIME);
                Date previousExecutionTime = DateTimeFormatUtils.getTimeFromLong(scheduledExecutionTime() - PERIOD);
                Date currentExecutionTime = DateTimeFormatUtils.getTimeFromLong(scheduledExecutionTime());

                // previousExecutionTime < dischargeTime <= currentExecutionTime
                if (previousExecutionTime.before(dischargeTime) && (dischargeTime.equals(currentExecutionTime) || dischargeTime.before(currentExecutionTime))) {
                    Bed[] reservedBeds = bedManager.getBedsByProgram(bedProgram.getId(), true);

                    if(reservedBeds == null) {
                    	log.error("getBedsByProgram returned null for bed program with id: " + bedProgram.getId());
                    	continue;
                    }
                    
                    for (Bed reservedBed : reservedBeds) {
                    	MiscUtilsOld.checkShutdownSignaled();
                    	
                        BedDemographic bedDemographic = bedDemographicManager.getBedDemographicByBed(reservedBed.getId());
                        
                        if (bedDemographic != null && bedDemographic.getId() != null && bedDemographic.isExpired()) {
                            try {
                                admissionManager.processDischargeToCommunity(null, Program.DEFAULT_COMMUNITY_PROGRAM_ID, bedDemographic.getId().getDemographicNo(), Provider.SYSTEM_PROVIDER_NO, "bed reservation ended - automatically discharged", "0" , null, false);
                            }
                            catch (AdmissionException e) {
                                log.error("Error discharging to community", e);
                            } catch (FunctionalCentreDischargeException e) {
                            	log.error("Error discharging from functional centre:" , e);
                            }
                        }
                    }
                }
            }

            log.debug("finish bed program discharge task");
        } catch (ShutdownException e) {
        	log.debug("BedProgramDischargeTask noticed shutdown signaled.");
        }
        finally {
            DbConnectionFilter.releaseAllThreadDbResources();
        }
	}

}
