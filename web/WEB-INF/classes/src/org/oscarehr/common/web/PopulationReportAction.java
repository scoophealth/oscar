package org.oscarehr.common.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.common.model.Mortalities;
import org.oscarehr.common.model.ReportStatistic;
import org.oscarehr.common.model.ShelterPopulation;
import org.oscarehr.common.model.ShelterUsage;
import org.oscarehr.common.service.PopulationReportManager;

public class PopulationReportAction extends DispatchAction {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

	// Forwards
	private static final String REPORT = "report";
	
	private PopulationReportManager populationReportManager;
	
	public void setPopulationReportManager(PopulationReportManager populationReportManager) {
	    this.populationReportManager = populationReportManager;
    }
	
	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    return report(mapping, form, request, response);
	}

	public ActionForward report(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get attributes
		Date currentDateTime = Calendar.getInstance().getTime();
		ShelterPopulation shelterPopulation = populationReportManager.getShelterPopulation();
		ShelterUsage shelterUsage = populationReportManager.getShelterUsage();
		Mortalities mortalities = populationReportManager.getMortalities();
		Map<String, ReportStatistic> majorMedicalConditions = populationReportManager.getMajorMedicalConditions();
		Map<String, ReportStatistic> majorMentalIllnesses = populationReportManager.getMajorMentalIllnesses();
		Map<String, ReportStatistic> seriousMedicalConditions = populationReportManager.getSeriousMedicalConditions();
		Map<String, Map<String, String>> categoryCodeDescriptions = populationReportManager.getCategoryCodeDescriptions();
		 
		// set attributes
		request.setAttribute("date", DateTimeFormatUtils.getStringFromDate(currentDateTime, DATE_FORMAT));
		request.setAttribute("time", DateTimeFormatUtils.getStringFromTime(currentDateTime));
		request.setAttribute("shelterPopulation", shelterPopulation);
		request.setAttribute("shelterUsage", shelterUsage);
		request.setAttribute("mortalities", mortalities);
		request.setAttribute("majorMedicalConditions", majorMedicalConditions);
		request.setAttribute("majorMentalIllnesses", majorMentalIllnesses);
		request.setAttribute("seriousMedicalConditions", seriousMedicalConditions);
		request.setAttribute("categoryCodeDescriptions", categoryCodeDescriptions);
		
		// forward to view page
		return mapping.findForward(REPORT);
	}

}