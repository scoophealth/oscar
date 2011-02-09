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
import org.oscarehr.eyeform.dao.ProcedureBookDao;
import org.oscarehr.eyeform.model.ProcedureBook;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class ProcedureBookAction extends DispatchAction {

	static Logger logger = Logger.getLogger(ProcedureBookAction.class);
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	ProcedureBookDao dao = (ProcedureBookDao)SpringUtils.getBean("ProcedureBookDAO");
    	
    	
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	ProcedureBook data = (ProcedureBook)f.get("data");
    	if(data.getId() != null && data.getId().intValue()>0) {
    		data = dao.find(data.getId()); 
    	}
    	
    	f.set("data", data);
    	        
        return mapping.findForward("form");
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {    	
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	ProcedureBook data = (ProcedureBook)f.get("data");
    	if(data.getId()!=null && data.getId()==0) {
    		data.setId(null);
    	}
    	ProcedureBookDao dao = (ProcedureBookDao)SpringUtils.getBean("ProcedureBookDAO");
    	data.setProvider(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());	
    	dao.save(data);
    	
    	return mapping.findForward("success");
    }

    public ActionForward getNoteText(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String appointmentNo = request.getParameter("appointmentNo");
    	ProcedureBookDao dao = (ProcedureBookDao)SpringUtils.getBean("ProcedureBookDAO");
    	
    	List<ProcedureBook> procedures = dao.getByAppointmentNo(Integer.parseInt(appointmentNo));
    	StringBuilder sb = new StringBuilder();
    	
    	for(ProcedureBook f:procedures) {    		
    		sb.append(f.getProcedureName()).append(" ").append(f.getEye()).append(" ").append(f.getLocation());
    		sb.append("\n");
    	}
    	
    	try {
    		response.getWriter().print(sb.toString());
    	}catch(IOException e) {logger.error(e);}
    	
    	return null;
    }
}
