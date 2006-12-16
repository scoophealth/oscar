package org.oscarehr.survey.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.survey.dao.oscar.OscarFormDAO;
import org.oscarehr.survey.model.Survey;
import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.service.SurveyLaunchManager;

public class SurveyLaunchManagerOscar implements SurveyLaunchManager {

	private Log log = LogFactory.getLog(getClass());

	private OscarFormDAO oscarFormDAO;
	
	public void setOscarFormDAO(OscarFormDAO dao) {
		this.oscarFormDAO = dao;
	}
	
	public long launch(Survey survey) {
		OscarForm form = new OscarForm();
		form.setDescription(survey.getDescription());
		form.setSurveyData(survey.getSurveyData());
		form.setStatus(OscarForm.STATUS_ACTIVE);
		
		oscarFormDAO.saveOscarForm(form);
		
		return form.getFormId().longValue();
	}
	
	public void close(long formId) {
		this.oscarFormDAO.updateStatus(new Long(formId),new Short(OscarForm.STATUS_INACTIVE));
	}

	public void reopen(long formId) {
		this.oscarFormDAO.updateStatus(new Long(formId),new Short(OscarForm.STATUS_ACTIVE));
	}
}
