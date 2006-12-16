package org.oscarehr.survey.service.impl;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.survey.dao.SurveyTestDAO;
import org.oscarehr.survey.model.SurveyTestData;
import org.oscarehr.survey.model.SurveyTestInstance;
import org.oscarehr.survey.service.SurveyTestManager;

public class SurveyTestManagerImpl implements SurveyTestManager {

	private Log log = LogFactory.getLog(getClass());

	private SurveyTestDAO surveyTestDAO;
	
	public void setSurveyTestDAO(SurveyTestDAO dao) {
		this.surveyTestDAO = dao;
	}
	
	public SurveyTestInstance getSurveyInstance(String id) {
		return this.surveyTestDAO.getSurveyInstance(Long.valueOf(id));
	}

	public SurveyTestInstance getSurveyInstance(String surveyId, String clientId) {
		return this.surveyTestDAO.getSurveyInstance(Long.valueOf(surveyId),Long.valueOf(clientId));
	}

	public void saveSurveyInstance(SurveyTestInstance instance) {
		log.debug("Saving a test instance");
		for(Iterator iter=instance.getData().iterator();iter.hasNext();) {
			SurveyTestData data = (SurveyTestData)iter.next();
			this.surveyTestDAO.saveSurveyData(data);
		}
		this.surveyTestDAO.saveSurveyInstance(instance);
	}

	public void clearTestData(String surveyId) {
		this.surveyTestDAO.clearTestData(Long.valueOf(surveyId));
	}
}
