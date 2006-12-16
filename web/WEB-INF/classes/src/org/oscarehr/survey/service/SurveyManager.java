package org.oscarehr.survey.service;

import java.util.List;

import org.oscarehr.survey.model.Survey;

public interface SurveyManager {
	public void saveSurvey(Survey survey);
	public Survey getSurvey(String surveyId);
	public org.oscarehr.surveymodel.SurveyDocument.Survey getSurveyModel(String surveyId);
	public Survey getSurveyByName(String name);
	public List getSurveys();
	public void deleteSurvey(String surveyId);
	public org.oscarehr.surveymodel.SurveyDocument getSurveyDocument(String surveyId);
	public Survey updateStatus(String surveyId, short status);
}
