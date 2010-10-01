package org.oscarehr.eyeform.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.eyeform.dao.OcularProcDao;
import org.oscarehr.eyeform.model.OcularProc;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class OcularProcAction extends DispatchAction {
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
    	OcularProcDao dao = (OcularProcDao)SpringUtils.getBean("OcularProcDAO");
    	
    	request.setAttribute("providers",providerDao.getActiveProviders());
    	
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	OcularProc procedure = (OcularProc)f.get("proc");
    	if(procedure.getId() != null && procedure.getId().intValue()>0) {
    		procedure = dao.find(procedure.getId()); 
    	}
    	
    	f.set("proc", procedure);
    	
        return mapping.findForward("form");
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	OcularProc procedure = (OcularProc)f.get("proc");
    	
    	OcularProcDao dao = (OcularProcDao)SpringUtils.getBean("OcularProcDAO");
    	procedure.setProvider(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());	
    	
    	dao.save(procedure);
    	
    	return mapping.findForward("success");
    }

}
