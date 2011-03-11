package org.oscarehr.util;

import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.web.OcanReportUIBean;

public class OcanIarSubmissionTask extends TimerTask {

	Logger logger = MiscUtils.getLogger();
	
	@Override
	public void run() {
		logger.info("Running OCAN IAR Submission Task");
		FacilityDao facilityDao = (FacilityDao)SpringUtils.getBean("facilityDao");
		LoggedInInfo.loggedInInfo.get().currentFacility = facilityDao.find(1);
		
		int submissionId_full = OcanReportUIBean.sendSubmissionToIAR(OcanReportUIBean.generateOCANSubmission("FULL"));		
		logger.info("FULL OCAN upload Completed: submissionId="+ submissionId_full);
		
		int submissionId_self = OcanReportUIBean.sendSubmissionToIAR(OcanReportUIBean.generateOCANSubmission("SELF"));		
		logger.info("FULL OCAN upload Completed: submissionId="+ submissionId_self);
		
		int submissionId_core = OcanReportUIBean.sendSubmissionToIAR(OcanReportUIBean.generateOCANSubmission("CORE"));		
		logger.info("FULL OCAN upload Completed: submissionId="+ submissionId_core);
		
	}

}
