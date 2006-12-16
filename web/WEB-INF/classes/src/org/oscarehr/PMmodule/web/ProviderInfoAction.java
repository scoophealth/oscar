package org.oscarehr.PMmodule.web;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.model.ProgramProvider;

public class ProviderInfoAction extends BaseAction {
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return view(mapping, form, request, response);
	}

	public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return view(mapping, form, request, response);
	}

	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String providerNo = getProviderNo(request);
		
		request.setAttribute("provider", providerManager.getProvider(providerNo));
		request.setAttribute("agencyDomain", providerManager.getAgencyDomain(providerNo));
		
		List programDomain = providerManager.getProgramDomain(providerNo);
		
		for (Iterator i = programDomain.iterator(); i.hasNext();) {
			ProgramProvider programProvider = (ProgramProvider) i.next();
			programProvider.setProgram(programManager.getProgram(programProvider.getProgramId()));
		}
		
		request.setAttribute("programDomain", programDomain);
		
		return mapping.findForward("view");
	}
	
}