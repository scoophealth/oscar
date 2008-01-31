package com.quatro.web.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

public class QuatroReportListAction extends DispatchAction {
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("reportlist");
	}
	
//	public ActionForward reportlist(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
//		return mapping.findForward("reportlist");
//	}
}