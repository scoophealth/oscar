package org.caisi.PMmodule.web;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.caisi.PMmodule.model.ProgramProvider;
import org.caisi.PMmodule.model.Provider;
import org.caisi.PMmodule.service.ProgramManager;
import org.caisi.PMmodule.service.ProviderManager;

public class ProviderInfoAction extends DispatchAction {
	private static Log log = LogFactory.getLog(ProviderInfoAction.class);

	private ProgramManager programManager;
	private ProviderManager providerManager;
	
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}

	public void setProviderManager(ProviderManager mgr) {
		this.providerManager = mgr;
	}
	/*
	public void initApplication(HttpSession session) {
		//for testing
		if(session.getAttribute("user") == null) {
			log.warn("USING TESTING USER, NO OSCAR USER FOUND!");
			session.setAttribute("user","999998");
		}
		
		String oscarUser = (String)session.getAttribute("user");
		if(oscarUser == null) {
			log.info("Not logged in!");
			return;
		}
		
		if(session.getAttribute("provider") == null) {
			log.debug("setting session variable: provider");
			session.setAttribute("provider",providerManager.getProvider(oscarUser));
		}
		if(session.getAttribute("program_domain") == null) {
			log.debug("setting session variable: program_domain");
			session.setAttribute("program_domain",providerManager.getProgramDomain(oscarUser));
		}
		
		if(session.getAttribute("pmm_admin") == null) {
			log.debug("setting session variable: pmm_admin");
			//session.setAttribute("pmm_admin",new Boolean(oscarSecurityManager.hasAdminRole(oscarUser)));
		}
	}
	*/
	public String getProviderNo(HttpServletRequest request) {
		Provider p =  (Provider)request.getSession().getAttribute("provider");
		return p.getProviderNo();
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return view(mapping,form,request,response);
	}
	
	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		//initApplication(request.getSession());
		String providerNo = getProviderNo(request);
		Provider provider = providerManager.getProvider(providerNo);
		request.setAttribute("provider",provider);
		request.setAttribute("agencyDomain",providerManager.getAgencyDomain(providerNo));
		List programDomain = providerManager.getProgramDomain(providerNo);
		for(Iterator iter=programDomain.iterator();iter.hasNext();) {
			ProgramProvider pp = (ProgramProvider)iter.next();
			pp.setProgram(programManager.getProgram(String.valueOf(pp.getProgramId())));
		}
		request.setAttribute("programDomain",programDomain);		
		return mapping.findForward("view");
	}
}
