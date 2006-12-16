package org.oscarehr.survey.service.impl;

import java.io.StringReader;
import java.util.List;

import org.oscarehr.survey.dao.SurveyDAO;
import org.oscarehr.survey.model.Survey;
import org.oscarehr.survey.service.SurveyManager;
import org.oscarehr.surveymodel.SurveyDocument;


public class SurveyManagerImpl implements SurveyManager {

	private SurveyDAO surveyDAO;
	
	public void setSurveyDAO(SurveyDAO dao) {
		this.surveyDAO = dao;
	}
	
	public void saveSurvey(Survey survey) {
		surveyDAO.saveSurvey(survey);
	}

	public Survey getSurvey(String surveyId) {
		return surveyDAO.getSurvey(Long.valueOf(surveyId));
	}

	public List getSurveys() {
		return surveyDAO.getSurveys();
	}

	public void deleteSurvey(String surveyId) {
		surveyDAO.deleteSurvey(Long.valueOf(surveyId));
	}

	public Survey getSurveyByName(String name) {
		return surveyDAO.getSurveyByName(name);
	}
	
	public Survey updateStatus(String surveyId, short status) {
		Survey survey = getSurvey(surveyId);
		if(survey != null && survey.getStatus() != new Short(status)) {
			survey.setStatus(new Short(status));
		}
		saveSurvey(survey);
		return survey;
	}
	
	public org.oscarehr.surveymodel.SurveyDocument.Survey getSurveyModel(String surveyId) {
		Survey survey = getSurvey(surveyId);
		if(survey != null) {
			try {
            	String xml = survey.getSurveyData();
            	SurveyDocument model = SurveyDocument.Factory.parse(new StringReader(xml));
            	return model.getSurvey();
            }catch(Exception e) {
            	e.printStackTrace();
            }
		}
		return null;
	}
	
	public org.oscarehr.surveymodel.SurveyDocument getSurveyDocument(String surveyId) {
		Survey survey = getSurvey(surveyId);
		if(survey != null) {
			try {
            	String xml = survey.getSurveyData();
            	SurveyDocument model = SurveyDocument.Factory.parse(new StringReader(xml));
            	return model;
            }catch(Exception e) {
            	e.printStackTrace();
            }
		}
		return null;
	}
	
}
