package org.oscarehr.survey.dao;

import java.util.List;

import org.oscarehr.survey.model.Survey;

public interface SurveyDAO {
	public void saveSurvey(Survey survey);
	public Survey getSurvey(Long id);
	public List getSurveys();
	public void deleteSurvey(Long id);
	public Survey getSurveyByName(String name);
}
