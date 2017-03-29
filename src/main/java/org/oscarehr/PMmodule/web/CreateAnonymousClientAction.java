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

package org.oscarehr.PMmodule.web;

import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 * # a new client registration is automatically created
# the first name for this client is "Anonymous[datetimestamp]" where date time stamp is the datetime that the button was clicked, the last name is ".Anonymous" (note the period before the name so that client appears at the top of the list).
# the client anonymous value is "one time unique anonymous"
# the client DOB is 1800-01-01
# the client is admitted into program A at the time that the button was clicked

 * @author marc
 *
 */

public class CreateAnonymousClientAction {

	private static Logger logger = MiscUtils.getLogger();
	
	public static Demographic generateAnonymousClient(String creatorProviderNo, int programId) {
		logger.info("Create Anonymous Client!");
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoAsCurrentClassAndMethod();
		ClientManager clientManager = (ClientManager)SpringUtils.getBean("clientManager");
		AdmissionManager admissionManager = (AdmissionManager)SpringUtils.getBean("admissionManager");
		ProgramManager programManager = (ProgramManager)SpringUtils.getBean("programManager");
		//create and save client record.
		Demographic d = createDemographic(creatorProviderNo);
		clientManager.saveClient(d);

		//admit client to program
		Program externalProgram = programManager.getProgram(programId);
		try {
			admissionManager.processAdmission(loggedInInfo, d.getDemographicNo(), creatorProviderNo, externalProgram, "anonymous discharge", "anonymous admission");
		}catch(Exception e) {
			logger.error("Error", e);
			return d;
		}
		
		return d;
	}

    public static Demographic generatePEClient(String creatorProviderNo, int programId){
        logger.info("Create PE temporary Client!");
        LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoAsCurrentClassAndMethod();
        ClientManager clientManager = (ClientManager)SpringUtils.getBean("clientManager");
        AdmissionManager admissionManager = (AdmissionManager)SpringUtils.getBean("admissionManager");
        ProgramManager programManager = (ProgramManager)SpringUtils.getBean("programManager");
        //create and save client record.
        Demographic d = createDemographic(creatorProviderNo);
        d.setFirstName("phone encounter");
        d.setYearOfBirth("1900");
        clientManager.saveClient(d);

        //admit client to program
        Program externalProgram = programManager.getProgram(programId);
        try {
            admissionManager.processAdmission(loggedInInfo, d.getDemographicNo(), creatorProviderNo, externalProgram, "anonymous discharge", "anonymous admission");
        }catch(Exception e) {
            logger.error("Error", e);
            return d;
        }

        return d;
    }
	
	public static Demographic createDemographic(String creatorProviderNo) {
		Demographic d = new Demographic();
		d.setAnonymous("one-time-anonymous");
		d.setYearOfBirth("1800");
		d.setMonthOfBirth("01");
		d.setDateOfBirth("01");		
		d.setDateJoined(new Date());
		d.setFirstName("Anonymous");
		d.setLastName("["+new Date()+"]");
		d.setPatientStatus("AC");
		d.setProviderNo(creatorProviderNo);
		d.setSex("M");
		
		return d;
	}
}
