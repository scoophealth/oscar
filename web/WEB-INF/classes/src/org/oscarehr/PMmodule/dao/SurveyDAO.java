package org.oscarehr.PMmodule.dao;

import java.util.List;

import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.model.oscar.OscarFormData;
import org.oscarehr.survey.model.oscar.OscarFormInstance;

public interface SurveyDAO {
	public List getAllForms();
	public OscarForm getForm(Long formId);
	public void saveFormInstance(OscarFormInstance instance);
	public OscarFormInstance getLatestForm(Long formId, Long clientId);
	public List getForms(Long clientId);
	public List getForms(Long formId, Long clientId);
	public void saveFormData(OscarFormData data);
	public List getCurrentForms(String formId, List clients);
}
