package org.oscarehr.PMmodule.web.reports;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.service.ConsentManager;

public class ExitInterviewReportDetailedAction extends DispatchAction {

	private ConsentManager consentManager;
	
	public void setConsentManager(ConsentManager mgr) {
		this.consentManager = mgr;
	}
	
	
	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		request.setAttribute("interview", consentManager.getConsentInterview(id));
		return mapping.findForward("form");
	}
	
}
