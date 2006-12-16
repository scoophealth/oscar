package org.oscarehr.survey.service;

import org.oscarehr.survey.model.SurveyTestInstance;

public interface SurveyTestManager {
	public SurveyTestInstance getSurveyInstance(String id);
	public SurveyTestInstance getSurveyInstance(String surveyId,String clientId);
	
	public void saveSurveyInstance(SurveyTestInstance instance);
	
	public void clearTestData(String surveyId);
}
