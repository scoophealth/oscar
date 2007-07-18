package org.oscarehr.PMmodule.web.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.web.formbean.ActivityReportFormBean;
import org.oscarehr.PMmodule.web.formbean.ClientListsReportFormBean;

public class ClientListsReportAction extends DispatchAction {

	private ProviderManager providerManager;
	
	public void setProviderManager(ProviderManager providerManager) {
    
    	this.providerManager = providerManager;
    }

	private ProgramManager programManager;
	
	public void setProgramManager(ProgramManager programManager) {
    
    	this.programManager = programManager;
    }

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		// need to get the reporting options here, i.e.
		// - provider list
		// - program list
		// - icd-10 isue list?
		
		List<Provider> providers=providerManager.getProviders();
		request.setAttribute("providers", providers);

		List<Program> programs=programManager.getAllPrograms();
		request.setAttribute("programs", programs);
		
		return mapping.findForward("form");
	}

	public ActionForward report(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

System.err.println("I guess I should do some reporting here eh?");

DynaActionForm reportForm = (DynaActionForm)form;
ClientListsReportFormBean formBean = (ClientListsReportFormBean)reportForm.get("form");
System.err.println("form:"+form.toString());
System.err.println("formBean:"+formBean.toString());

		return mapping.findForward("report");
	}
}
