package org.oscarehr.PMmodule.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;

public class ClientSearchAction2 extends BaseAction {
	
	private static Log log = LogFactory.getLog(ClientSearchAction2.class);
	
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return form(mapping,form,request,response);
	}
	
	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("form");
	}
	
	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm searchForm = (DynaActionForm)form;
		ClientSearchFormBean formBean = (ClientSearchFormBean)searchForm.get("criteria");
		
		/* do the search */
		formBean.setProgramDomain((List)request.getSession().getAttribute("program_domain"));
		request.setAttribute("clients",clientManager.search(formBean));

		if(formBean.isSearchOutsideDomain()) {
			logManager.log(getProviderNo(request),"read","out of domain client search","",request.getRemoteAddr());
		}
				
		return mapping.findForward("form");
	}
	
}
