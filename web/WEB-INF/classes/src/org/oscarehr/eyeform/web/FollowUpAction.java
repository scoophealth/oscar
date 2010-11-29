package org.oscarehr.eyeform.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.eyeform.dao.FollowUpDao;
import org.oscarehr.eyeform.model.FollowUp;
import org.oscarehr.util.SpringUtils;

public class FollowUpAction extends DispatchAction {

	static Logger logger = Logger.getLogger(FollowUpAction.class);
	
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
    
    public ActionForward getNoteText(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String appointmentNo = request.getParameter("appointmentNo");
    	
    	FollowUpDao dao = (FollowUpDao)SpringUtils.getBean("FollowUpDAO");
    	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
    	
    	
    	List<FollowUp> followUps = dao.getByAppointmentNo(Integer.parseInt(appointmentNo));
    	StringBuilder sb = new StringBuilder();
    	
    	for(FollowUp f:followUps) {
    		Provider p = providerDao.getProvider(f.getFollowupProvider());
    		sb.append(f.getType()).append(" ").append(f.getTimespan()).append(" ").append(f.getTimeframe());
    		sb.append(" ").append(p.getFormattedName()).append(" ").append(f.getUrgency());
    		sb.append("\n");
    	}
    	
    	try {
    		response.getWriter().print(sb.toString());
    	}catch(IOException e) {logger.error(e);}
    	return null;
    }
}
