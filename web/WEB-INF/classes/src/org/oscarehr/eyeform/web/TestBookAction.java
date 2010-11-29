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
import org.oscarehr.eyeform.dao.TestBookRecordDao;
import org.oscarehr.eyeform.model.TestBookRecord;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class TestBookAction extends DispatchAction {

	static Logger logger = Logger.getLogger(TestBookAction.class);
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	TestBookRecordDao dao = (TestBookRecordDao)SpringUtils.getBean("TestBookDAO");
    	
    	
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	TestBookRecord data = (TestBookRecord)f.get("data");
    	if(data.getId() != null && data.getId().intValue()>0) {
    		data = dao.find(data.getId()); 
    	}
    	
    	f.set("data", data);
    	        
        return mapping.findForward("form");
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {    	
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	TestBookRecord data = (TestBookRecord)f.get("data");
    	if(data.getId()!=null && data.getId()==0) {
    		data.setId(null);
    	}
    	TestBookRecordDao dao = (TestBookRecordDao)SpringUtils.getBean("TestBookDAO");
    	data.setProvider(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());	
    	dao.save(data);
    	
    	return mapping.findForward("success");
    }
    
    public ActionForward getNoteText(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String appointmentNo = request.getParameter("appointmentNo");
    	TestBookRecordDao dao = (TestBookRecordDao)SpringUtils.getBean("TestBookDAO");
    	
    	List<TestBookRecord> tests = dao.getByAppointmentNo(Integer.parseInt(appointmentNo));
    	StringBuilder sb = new StringBuilder();
    	
    	for(TestBookRecord f:tests) {    		
    		sb.append(f.getTestname()).append(" ").append(f.getEye()).append(" ").append(f.getUrgency());
    		sb.append("\n");
    	}
    	
    	try {
    		response.getWriter().print(sb.toString());
    	}catch(IOException e) {logger.error(e);}
    	
    	return null;
    }
}
