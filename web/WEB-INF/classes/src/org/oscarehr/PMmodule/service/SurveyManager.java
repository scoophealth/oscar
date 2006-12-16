package org.oscarehr.PMmodule.service;

import java.util.List;

import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.model.oscar.OscarFormData;
import org.oscarehr.survey.model.oscar.OscarFormInstance;
import org.oscarehr.surveymodel.SurveyDocument;

public interface SurveyManager {
	public List getAllForms();
	public OscarForm getForm(String formId);
	public void saveFormInstance(OscarFormInstance instance);
	public OscarFormInstance getLatestForm(String formId, String clientId);
	public List getForms(String clientId);
	public List getForms(String formId, String clientId);
	public void saveFormData(OscarFormData data);
	public SurveyDocument.Survey getFormModel(String formId);
	
	public List getCurrentForms(String formId, List clients);
}
