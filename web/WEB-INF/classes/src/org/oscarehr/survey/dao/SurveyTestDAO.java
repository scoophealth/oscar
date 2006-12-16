package org.oscarehr.survey.dao;

import org.oscarehr.survey.model.SurveyTestData;
import org.oscarehr.survey.model.SurveyTestInstance;

public interface SurveyTestDAO {
	public SurveyTestInstance getSurveyInstance(Long id);
	public SurveyTestInstance getSurveyInstance(Long surveyId,Long clientId);
	
	public void saveSurveyInstance(SurveyTestInstance instance);
	public void saveSurveyData(SurveyTestData data);
	
	public void clearTestData(Long surveyId);
}
