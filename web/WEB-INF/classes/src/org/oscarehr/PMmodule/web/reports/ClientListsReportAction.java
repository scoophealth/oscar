package org.oscarehr.PMmodule.web.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.service.ProviderManager;

public class ClientListsReportAction extends DispatchAction {

	private ProviderManager providerManager;
	
	public void setProviderManager(ProviderManager providerManager) {
    
    	this.providerManager = providerManager;
    }

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		// need to get the reporting options here, i.e.
		// - provider list
		// - program list
		// - icd-10 isue list?
		
		List<Provider> providers=providerManager.getProviders();
		request.setAttribute("providers", providers);
		
		return mapping.findForward("form");
	}

	public ActionForward report(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

System.err.println("I guess I should do some reporting here eh?");

		return mapping.findForward("report");
	}
}
