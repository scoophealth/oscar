package org.oscarehr.PMmodule.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.service.ProviderManager;

public class ProviderSearchAction extends Action {
	
	private ProviderManager providerManager;
	
	public void setProviderManager(ProviderManager mgr) {
		this.providerManager = mgr;
	}
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("q");
		if(name == null) {
			name = "";
		}
		request.setAttribute("providers",providerManager.search(name));
		return mapping.findForward("results");
	}
}
