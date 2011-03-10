package org.oscarehr.util;

import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorUpdateTask;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.web.OcanReportUIBean;

public class OcanIarSubmissionTask extends TimerTask {

	private static final Logger logger = MiscUtils.getLogger();

	@Override
	public void run() {
		logger.info("Running OCAN IAR Submission Task");
		LoggedInInfo.setLoggedInInfoToCurrentClassAndMethod();
		
		FacilityDao facilityDao = (FacilityDao)SpringUtils.getBean("facilityDao");
		List<Facility> facilities = facilityDao.findAll(null);
		for (Facility facility : facilities) {
			if (!facility.isDisabled() && facility.isEnableOcanForms() && Integer.valueOf(facility.getOcanServiceOrgNumber()).intValue()!=0) {
				LoggedInInfo.loggedInInfo.get().currentFacility = facility;
				break;
			}
		}
		
		int submissionId_full = OcanReportUIBean.sendSubmissionToIAR(OcanReportUIBean.generateOCANSubmission("FULL"));		
		logger.info("FULL OCAN upload Completed: submissionId="+ submissionId_full);
		
		int submissionId_self = OcanReportUIBean.sendSubmissionToIAR(OcanReportUIBean.generateOCANSubmission("SELF"));		
		logger.info("SELF OCAN upload Completed: submissionId="+ submissionId_self);
		
		int submissionId_core = OcanReportUIBean.sendSubmissionToIAR(OcanReportUIBean.generateOCANSubmission("CORE"));		
		logger.info("CORE OCAN upload Completed: submissionId="+ submissionId_core);
		
	}

}
