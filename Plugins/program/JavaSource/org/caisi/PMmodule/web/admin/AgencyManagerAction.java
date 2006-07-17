package org.caisi.PMmodule.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.caisi.PMmodule.model.Agency;
import org.caisi.PMmodule.model.Provider;
import org.caisi.PMmodule.service.AgencyManager;
import org.caisi.PMmodule.service.IntegratorManager;
import org.caisi.PMmodule.service.LogManager;
import org.caisi.PMmodule.web.formbean.AgencyManagerViewFormBean;

public class AgencyManagerAction extends DispatchAction {
	private static Log log = LogFactory.getLog(AgencyManagerAction.class);

	private AgencyManager agencyManager;
	private IntegratorManager integratorManager;
	private LogManager logManager;
	
	public void setAgencyManager(AgencyManager mgr) {
		this.agencyManager = mgr;
	}
	
	public void setIntegratorManager(IntegratorManager mgr) {
		this.integratorManager = mgr;
	}
	
	public void setLogManager(LogManager mgr) {
		this.logManager = mgr;
	}
	
	public String getProviderNo(HttpServletRequest request) {
		Provider p =  (Provider)request.getSession().getAttribute("provider");
		return p.getProviderNo();
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return view(mapping,form,request,response);
	}
	
	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm agencyForm = (DynaActionForm)form;
		AgencyManagerViewFormBean formBean = (AgencyManagerViewFormBean)agencyForm.get("view");
		
		Agency agency = agencyManager.getLocalAgency();
		request.setAttribute("agency",agency);
		request.setAttribute("integrator_enabled",new Boolean(integratorManager.isEnabled()));
		request.setAttribute("integrator_registered",new Boolean(integratorManager.isRegistered()));
		
		if(formBean.getTab() != null && formBean.getTab().equalsIgnoreCase("community")) {
			if(integratorManager.isEnabled() && integratorManager.isRegistered()) {
				request.setAttribute("agencies",integratorManager.getAgencies());
			}
		}
		return mapping.findForward("view");
	}
	
	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm agencyForm = (DynaActionForm)form;
		
		Agency agency = agencyManager.getLocalAgency();
		agencyForm.set("agency",agency);
		request.setAttribute("id",agency.getId());
		request.setAttribute("integratorEnabled",new Boolean(agency.isIntegratorEnabled()));
		request.setAttribute("integrator_enabled",new Boolean(integratorManager.isEnabled()));
		request.setAttribute("integrator_registered",new Boolean(integratorManager.isRegistered()));
		
		return mapping.findForward("edit");
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm agencyForm = (DynaActionForm)form;
		Agency agency = (Agency)agencyForm.get("agency");
		
		if(this.isCancelled(request)) {
			request.getSession().removeAttribute("agencyManagerForm");
			request.setAttribute("id",agency.getId());	
			return view(mapping,form,request,response);
		}
		agency.setLocal(true);
		agencyManager.saveLocalAgency(agency);
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("agency.saved",agency.getName()));
		saveMessages(request,messages);
		
		request.setAttribute("id",agency.getId());
		request.setAttribute("integratorEnabled",new Boolean(agency.isIntegratorEnabled()));
		
		logManager.log(getProviderNo(request),"write","agency",String.valueOf(agency.getId()),request.getRemoteAddr());
		return mapping.findForward("edit");
	}
	
	public ActionForward enable_integrator(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm agencyForm = (DynaActionForm)form;
		//Agency agency = (Agency)agencyForm.get("agency");

		Agency agency = agencyManager.getLocalAgency();
		agency.setIntegratorEnabled(true);
		agencyManager.saveAgency(agency);
		integratorManager.refresh();
		request.setAttribute("id",agency.getId());
		request.setAttribute("integratorEnabled",new Boolean(agency.isIntegratorEnabled()));
		return mapping.findForward("edit");
	}
	
	public ActionForward disable_integrator(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm agencyForm = (DynaActionForm)form;
		//Agency agency = (Agency)agencyForm.get("agency");

		Agency agency = agencyManager.getLocalAgency();
		agency.setIntegratorEnabled(false);
		agencyManager.saveAgency(agency);
		integratorManager.refresh();
		
		request.setAttribute("id",agency.getId());
		request.setAttribute("integratorEnabled",new Boolean(agency.isIntegratorEnabled()));
		return mapping.findForward("edit");
	}
	
	public ActionForward register(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		try {
			Agency agency = agencyManager.getLocalAgency();
			if(agency.getId().longValue() == 0) {
				String id = integratorManager.register(agency,agency.getIntegratorUserName());
				if(id != null) {
					agency.setId(Long.valueOf(id));
					agencyManager.saveLocalAgency(agency);
				} else {
					log.error("error");
				}
				integratorManager.refresh();
			} else {
				log.warn("already registered!!");
			}
		}catch(Throwable e) {
			log.error(e);
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("integrator.registered.failed"));
			saveMessages(request,messages);		
		}
		return view(mapping,form,request,response);
	}
}
