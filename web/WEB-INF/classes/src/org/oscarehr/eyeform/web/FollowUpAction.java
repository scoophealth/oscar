package org.oscarehr.eyeform.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.eyeform.dao.FollowUpDao;
import org.oscarehr.eyeform.model.FollowUp;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class FollowUpAction extends DispatchAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
    	request.setAttribute("providers",providerDao.getActiveProviders());
    	
    	
    	/*
    	ProcedureBookDao dao = (ProcedureBookDao)SpringUtils.getBean("ProcedureBookDAO");
    	
    	
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	ProcedureBook data = (ProcedureBook)f.get("data");
    	if(data.getId() != null && data.getId().intValue()>0) {
    		data = dao.find(data.getId()); 
    	}
    	
    	f.set("data", data);
    */	        
        return mapping.findForward("form");
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {    	
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	FollowUp data = (FollowUp)f.get("followup");
    	if(data.getId()!=null && data.getId()==0) {
    		data.setId(null);
    	}
    	FollowUpDao dao = (FollowUpDao)SpringUtils.getBean("FollowUpDAO");
    	//data.setProvider(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());	
    	dao.save(data);
    	
    	
    	return mapping.findForward("success");
    }
}
