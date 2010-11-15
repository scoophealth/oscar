package org.oscarehr.PMmodule.web;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.HealthSafety;
import org.oscarehr.PMmodule.service.HealthSafetyManager;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;

public class HealthSafetyAction extends DispatchAction {
	private static Logger log = MiscUtils.getLogger();

    private HealthSafetyManager healthSafetyManager=null;
    
	public void setHealthSafetyManager(HealthSafetyManager healthSafetyManager) {
		this.healthSafetyManager = healthSafetyManager;
	}

	
	public ActionForward unspecified(ActionMapping mapping,	ActionForm form, HttpServletRequest request, HttpServletResponse response) {
//		logManager.log("read","full provider list","",request);
//		log.warn("Program doesn't have a name?");
		return form(mapping,form,request,response);
	}
	
	
	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm healthSafetyForm = (DynaActionForm)form;
		
		String id = request.getParameter("id");
				
		HealthSafety healthsafety = healthSafetyManager.getHealthSafetyByDemographic(Long.valueOf(id));
		if(healthsafety != null) {
			healthSafetyForm.set("healthsafety", healthsafety);
            request.setAttribute("healthsafety", healthsafety);
		}else{
			healthsafety= new HealthSafety();
			Provider provider=(Provider) request.getSession().getAttribute("provider");
			healthsafety.setUserName(provider.getFormattedName());
			healthsafety.setDemographicNo(Long.valueOf(id));
			healthSafetyForm.set("healthsafety", healthsafety);
            request.setAttribute("healthsafety", healthsafety);
		}
		
		return mapping.findForward("edit");
	}
	
	
	public ActionForward savehealthSafety(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		log.debug("Saving health and Safety");
		
		DynaActionForm healthSafetyForm = (DynaActionForm)form;
		
		HealthSafety healthsafety= (HealthSafety)healthSafetyForm.get("healthsafety");
		
		healthsafety.setUpdateDate(new Timestamp(System.currentTimeMillis()));

		healthSafetyManager.saveHealthSafetyByDemographic(healthsafety);	
		
		return mapping.findForward("success");
	}

}
