package org.oscarehr.PMmodule.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.common.model.ReportStatistic;

public class GenericIntakeReportAction extends BaseGenericIntakeAction {

	private static Log LOG = LogFactory.getLog(GenericIntakeReportAction.class);
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	// Forwards
	private static final String FORWARD_REPORT = "report";

	public ActionForward report(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String intakeType = getType(request);
		Integer programId = getProgramId(request);
		Date startDate = getStartDate(request);
		Date endDate = getEndDate(request);

		Map<String, SortedSet<ReportStatistic>> questionStatistics = genericIntakeManager.getQuestionStatistics(intakeType, programId, startDate, endDate);
		
		request.setAttribute("intakeType", StringUtils.capitalize(intakeType));
		request.setAttribute("startDate", DateTimeFormatUtils.getStringFromDate(startDate, DATE_FORMAT));
		request.setAttribute("endDate", DateTimeFormatUtils.getStringFromDate(endDate, DATE_FORMAT));
		request.setAttribute("questionStatistics", questionStatistics);

		return mapping.findForward(FORWARD_REPORT);
	}

}
