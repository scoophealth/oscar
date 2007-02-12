package org.oscarehr.casemgmt.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.struts.DispatchActionSupport;


public class ArchiveViewAction extends DispatchAction {
	protected ProgramManager programManager;
	
	//private static Log log = LogFactory.getLog(CaseManagementEntryAction.class);
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}
	/*
	public ProgramManager getProgramManager() {
		return (ProgramManager) getAppContext().getBean("programManager");
	}

	protected WebApplicationContext getSpringContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServlet().getServletContext());
	}
	public ApplicationContext getAppContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServlet().getServletContext());
	}
	*/
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.getSession().setAttribute("archiveView","true");
		//request.setAttribute("communityPrograms", programManager.getCommunityPrograms());
		
		return mapping.findForward("view");
	}
	
	public ActionForward cmm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		request.getSession().setAttribute("archiveView","false");
		return mapping.findForward("view");
	}
}