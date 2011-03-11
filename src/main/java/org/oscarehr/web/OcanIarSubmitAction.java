package org.oscarehr.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;

public class OcanIarSubmitAction extends DispatchAction {

	Logger logger = MiscUtils.getLogger();
	
	public ActionForward submit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		int submissionId_full = OcanReportUIBean.sendSubmissionToIAR(OcanReportUIBean.generateOCANSubmission("FULL"));
		
		try {
			response.getWriter().println(submissionId_full);
		}catch(IOException e) {
			logger.error(e);
		}
		
		int submissionId_self = OcanReportUIBean.sendSubmissionToIAR(OcanReportUIBean.generateOCANSubmission("SELF"));
		
		try {
			response.getWriter().println(submissionId_self);
		}catch(IOException e) {
			logger.error(e);
		}
		
		int submissionId_core = OcanReportUIBean.sendSubmissionToIAR(OcanReportUIBean.generateOCANSubmission("CORE"));
		
		try {
			response.getWriter().println(submissionId_core);
		}catch(IOException e) {
			logger.error(e);
		}
		
		return null;
	}
}
