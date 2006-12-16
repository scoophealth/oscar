package org.oscarehr.PMmodule.service.impl;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.oscarehr.PMmodule.dao.SurveyDAO;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.model.oscar.OscarFormData;
import org.oscarehr.survey.model.oscar.OscarFormInstance;
import org.oscarehr.surveymodel.SurveyDocument;

public class SurveyManagerImpl implements SurveyManager {

	private SurveyDAO surveyDAO;
	
	public void setSurveyDAO(SurveyDAO dao) {
		this.surveyDAO = dao;
	}
	
	public List getAllForms() {
		return surveyDAO.getAllForms();
	}
	public OscarForm getForm(String formId) {
		return surveyDAO.getForm(Long.valueOf(formId));
	}

	public void saveFormInstance(OscarFormInstance instance) {
		for(Iterator iter=instance.getData().iterator();iter.hasNext();) {
			OscarFormData data = (OscarFormData)iter.next();
			this.surveyDAO.saveFormData(data);
		}
		surveyDAO.saveFormInstance(instance);
	}

	public OscarFormInstance getLatestForm(String formId, String clientId) {
		return surveyDAO.getLatestForm(Long.valueOf(formId),Long.valueOf(clientId));
	}

	public List getForms(String clientId) {
		return surveyDAO.getForms(Long.valueOf(clientId));
	}

	public List getForms(String formId, String clientId) {
		return surveyDAO.getForms(Long.valueOf(formId),Long.valueOf(clientId));
	}

	public void saveFormData(OscarFormData data) {
		surveyDAO.saveFormData(data);
	}

	public SurveyDocument.Survey getFormModel(String formId) {
		OscarForm survey = getForm(formId);
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
	
	public List getCurrentForms(String formId, List clients) {
		return surveyDAO.getCurrentForms(formId,clients);
	}
}
