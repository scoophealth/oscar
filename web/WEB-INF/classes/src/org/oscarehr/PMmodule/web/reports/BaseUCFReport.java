package org.oscarehr.PMmodule.web.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.web.BaseAction;
import org.oscarehr.survey.SurveyReport;
import org.oscarehr.survey.SurveyReportEntry;

import org.oscarehr.surveymodel.SurveyDocument.Survey;

public class BaseUCFReport extends BaseAction {

	protected String getFormId() {
		return "6";
	}
	
	protected List getClients() {
		return clientManager.getClients();
	}
	
	protected SurveyReport getConfiguration() {
		SurveyReport config = new SurveyReport();
		SurveyReportEntry entry = new SurveyReportEntry();
		
		//get average age
		entry.setOperation(SurveyReport.OPERATION_AVERAGE);
		entry.setPageNumber(1);
		entry.setSectionId(0);
		entry.setQuestionId(1);
		
		config.getEntries().add(entry);
		
		return config;
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return report(mapping,form,request,response);
	}
	public ActionForward report(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		//get clients
		List clients = getClients();
		
		//list of OscarFormInstance
		List forms = surveyManager.getCurrentForms(getFormId(), clients);
		
		//get configuration
		SurveyReport config = getConfiguration();
		
		//getSurvey
		Survey survey = surveyManager.getFormModel(getFormId());
		
		//Generate the report
		
		
		return mapping.findForward("report");
	}
}
