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
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.web.utils.UserRoleUtils;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.MiscUtilsOld;
import org.oscarehr.util.ShutdownException;

public class ErProgramDischargeTask extends TimerTask {

    private static final Logger log=MiscUtils.getLogger();

    private ProviderManager providerManager;

    private AdmissionManager admissionManager;

    //the clients will stay in the service program for 3 days, 
    //then will be automatically discharged.
    private int lengthOfStay = 60 * 24 * 3; //minutes	

    public void setProviderManager(ProviderManager mgr) {
        this.providerManager = mgr;
    }

    public void setAdmissionManager(AdmissionManager mgr) {
        this.admissionManager = mgr;
    }

    public void setLengthOfStay(int minutes) {
        this.lengthOfStay = minutes;
    }

    public void run() {

		try {
            log.debug("running ErProgramDischargeTask");
            //log.info("Running ErProgramDischargeTask.............");

            // get all "ER" Service programs		
            //List providers = providerManager.getProvidersByType("er_clerk");
            List providers = providerManager.getProviders();
            boolean er_clerk = false;

            for (Iterator i = providers.iterator(); i.hasNext();) {
            	MiscUtilsOld.checkShutdownSignaled();
            	
                Provider provider = (Provider)i.next();

                er_clerk = false;
                List<SecUserRole> roles = providerManager.getSecUserRoles(provider.getProviderNo());
                for (Iterator ii = roles.iterator(); ii.hasNext();) {
                    SecUserRole secUserRole = (SecUserRole)ii.next();
                    if (UserRoleUtils.Roles.er_clerk.name().equals(secUserRole.getRoleName())) {
                        er_clerk = true;
                    }
                }

                if (!er_clerk) continue;

                // ER Clerks, they should only have 1 service program
                List programDomain = null;
                programDomain = providerManager.getProgramDomain(provider.getProviderNo());

                ProgramProvider programProvider = null;
                if (programDomain.size() > 0) {
                    programProvider = (ProgramProvider)programDomain.get(0);
                }
                if (programProvider != null) {
                    // loop clients in the ER program
                    List programAdmissions = admissionManager.getCurrentAdmissionsByProgramId(programProvider.getProgramId().toString());

                    if (programAdmissions == null) continue;

                    for (Iterator j = programAdmissions.iterator(); j.hasNext();) {
                    	MiscUtilsOld.checkShutdownSignaled();
                    	
                        Admission admission = (Admission)j.next();

                        // check admission date, determine if we should discharge
                        Date admissionDate = admission.getAdmissionDate();
                        Date currentDate = new Date();
                        long diff = currentDate.getTime() - admissionDate.getTime();

                        log.debug("difference = " + diff);

                        if (diff > (lengthOfStay * 60 * 1000)) {
                            admission.setDischargeDate(new Date());
                            admission.setDischargeNotes("Auto-Discharge");
                            admission.setAdmissionStatus(Admission.STATUS_DISCHARGED);
                            admissionManager.saveAdmission(admission);

                            log.debug("discharged");
                        }
                    }
                }
            }
        } catch (ShutdownException e) {
        	log.debug("ErProgramDischargeTask noticed shutdown hook.");
        }
        finally {
            DbConnectionFilter.releaseAllThreadDbResources();
        }
    }

}
