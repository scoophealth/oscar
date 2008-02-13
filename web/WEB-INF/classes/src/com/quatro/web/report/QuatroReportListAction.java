package com.quatro.web.report;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.quatro.service.QuatroReportManager;

public class QuatroReportListAction extends DispatchAction {
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return reportlist(mapping,form,request,response);
	}
	
	public ActionForward reportlist(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String providerNo = (String)request.getSession().getAttribute("user");
		QuatroReportManager reportManager = (QuatroReportManager)WebApplicationContextUtils.getWebApplicationContext(
        		getServlet().getServletContext()).getBean("quatroReportManager");
		List reports = reportManager.GetReportGroupList(providerNo);
		QuatroReportListForm qform = (QuatroReportListForm) form;
		qform.setReportGroups(reports);
		qform.setProvider(providerNo);
		return mapping.findForward("reportlist");
	}
}