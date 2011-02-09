package org.oscarehr.casemgmt.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.service.ProgramManager;

public class ArchiveViewAction extends DispatchAction {
	protected ProgramManager programManager;

	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession(true).setAttribute("archiveView", "true");
		return mapping.findForward("view");
	}

	public ActionForward cmm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.getSession(true).setAttribute("archiveView", "false");
		return mapping.findForward("view");
	}
}