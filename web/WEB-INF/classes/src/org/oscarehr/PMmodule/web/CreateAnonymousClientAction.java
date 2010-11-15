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
	
	public static Demographic generateAnonymousClient(int programId) {
		logger.info("Create Anonymous Client!");
		ClientManager clientManager = (ClientManager)SpringUtils.getBean("clientManager");
		AdmissionManager admissionManager = (AdmissionManager)SpringUtils.getBean("admissionManager");
		ProgramManager programManager = (ProgramManager)SpringUtils.getBean("programManager");
		//create and save client record.
		Demographic d = createDemographic();
		clientManager.saveClient(d);

		//admit client to program
		Program externalProgram = programManager.getProgram(programId);
		try {
			admissionManager.processAdmission(d.getDemographicNo(), LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo(), externalProgram, "anonymous discharge", "anonymous admission");
		}catch(Exception e) {
			logger.error(e);
			return d;
		}
		
		return d;
	}
	
	public static Demographic createDemographic() {
		Demographic d = new Demographic();
		d.setAnonymous("one-time-anonymous");
		d.setYearOfBirth("1800");
		d.setMonthOfBirth("01");
		d.setDateOfBirth("01");		
		d.setDateJoined(new Date());
		d.setFirstName("Anonymous");
		d.setLastName("["+new Date()+"]");
		d.setPatientStatus("AC");
		d.setProviderNo(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
		d.setSex("M");
		d.setAddress("");
		d.setCity("");
		d.setPostal("");
		d.setPhone("");
		d.setPhone2("");
		d.setEmail("");
		d.setPin("");
		d.setHin("");
		d.setVer("");
		d.setRosterStatus("");
		d.setChartNo("");
		d.setPcnIndicator("");
		d.setFamilyDoctor("");
		
		return d;
	}
}
