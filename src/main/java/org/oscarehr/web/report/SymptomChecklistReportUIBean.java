package org.oscarehr.web.report;

import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.myoscar_server.ws.SurveyResultTransfer;
import org.oscarehr.myoscar_server.ws.SurveyTemplateTransfer;
import org.oscarehr.myoscar_server.ws.SurveyWs;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.util.MyOscarServerWebServicesManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class SymptomChecklistReportUIBean {
	
	private static final Logger logger=MiscUtils.getLogger();
	private static final String MYOSCAR_SYMPTOM_CHECKLIST_REPORT_TEMPLATE_NAME_KEY="MYOSCAR_SYMPTOM_CHECKLIST_REPORT_TEMPLATE_NAME";
	
	private static Long symptomChecklistTemplateId=null;
	
	private static Long getSymptomChecklistTemplateId(PHRAuthentication auth)
	{
		if (symptomChecklistTemplateId==null)
		{
			SurveyWs surveyWs=MyOscarServerWebServicesManager.getSurveyWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
			
			OscarProperties p=OscarProperties.getInstance();
			String templateName=p.getProperty(MYOSCAR_SYMPTOM_CHECKLIST_REPORT_TEMPLATE_NAME_KEY);
			
			SurveyTemplateTransfer result=surveyWs.getSurveyTemplate(templateName, true);
			if (result==null)
			{
				logger.warn("No template matching "+MYOSCAR_SYMPTOM_CHECKLIST_REPORT_TEMPLATE_NAME_KEY+"="+templateName);
			}
			else
			{
				symptomChecklistTemplateId=result.getId();
			}
		}
		
		return(symptomChecklistTemplateId);
	}
	
	public static List<SurveyResultTransfer> getSymptomChecklistReportsSelectList(PHRAuthentication auth, int demographicId, int startIndex, int itemsToReturn)
	{
		DemographicDao demographicDao=(DemographicDao) SpringUtils.getBean("demographicDao");
		Demographic demographic=demographicDao.getDemographicById(demographicId);
		
		SurveyWs surveyWs=MyOscarServerWebServicesManager.getSurveyWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
		List<SurveyResultTransfer> results=surveyWs.getSurveyResults(new Long(demographic.getPin()), getSymptomChecklistTemplateId(auth), true, true, startIndex, itemsToReturn);
		return(results);
	}
}
