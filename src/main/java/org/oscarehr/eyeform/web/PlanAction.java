package org.oscarehr.eyeform.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.eyeform.dao.FollowUpDao;
import org.oscarehr.eyeform.dao.ProcedureBookDao;
import org.oscarehr.eyeform.dao.TestBookRecordDao;
import org.oscarehr.eyeform.model.FollowUp;
import org.oscarehr.eyeform.model.ProcedureBook;
import org.oscarehr.eyeform.model.TestBookRecord;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class PlanAction extends DispatchAction {

	protected FollowUpDao followUpDao = (FollowUpDao)SpringUtils.getBean("FollowUpDAO");
	protected ProcedureBookDao procBookDao = (ProcedureBookDao)SpringUtils.getBean("ProcedureBookDAO");
	protected TestBookRecordDao testBookDao = (TestBookRecordDao)SpringUtils.getBean("TestBookDAO");
	static ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {    	
    	request.setAttribute("providers",providerDao.getActiveProviders());    	    
        return mapping.findForward("form");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String strAppointmentNo = request.getParameter("appointmentNo");
    	int appointmentNo = Integer.parseInt(strAppointmentNo);
    	
    	request.setAttribute("providers",providerDao.getActiveProviders());    	
    	//get all follow ups, procs, and tests for this appointment
    	List<FollowUp> followUps = followUpDao.getByAppointmentNo(appointmentNo);
    	List<ProcedureBook> procedures = procBookDao.getByAppointmentNo(appointmentNo);
    	List<TestBookRecord> tests = testBookDao.getByAppointmentNo(appointmentNo);
    	
    	request.setAttribute("followUps", followUps);
    	request.setAttribute("procedures", procedures);
    	request.setAttribute("tests", tests);    	
    	
    	return mapping.findForward("edit");
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
       	DynaValidatorForm f = (DynaValidatorForm)form;
    	FollowUp followUp = (FollowUp)f.get("followup");
    	ProcedureBook proc = (ProcedureBook)f.get("proc");
    	TestBookRecord test = (TestBookRecord)f.get("test");
    	
    	int appointmentNo = followUp.getAppointmentNo();
    	
    	followUp.setId(null);
    	proc.setId(null);
    	test.setId(null);
    	
    	
    	proc.setProvider(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
    	proc.setAppointmentNo(appointmentNo);
    	proc.setDemographicNo(followUp.getDemographicNo());
    	test.setProvider(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
    	test.setAppointmentNo(appointmentNo);
    	test.setDemographicNo(followUp.getDemographicNo());
    	
    	
    	if(followUp.getTimespan()>0)
    		followUpDao.save(followUp);
    	if(proc.getProcedureName().length()>0) 
    		procBookDao.save(proc);
    	if(test.getTestname().length()>0) {
    		testBookDao.save(test);
    	}
    	
    	
    	
    	return mapping.findForward("success");    	
    }

 
    public ActionForward save_edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	//try to get all the follow ups in the form
    	String[] ids = request.getParameterValues("followup.id");
    	for(String id:ids) {
    		FollowUp followUp = followUpDao.find(Integer.parseInt(id));
    		followUp.setType(request.getParameter("followup"+id+".type"));
    		followUp.setTimespan(Integer.parseInt(request.getParameter("followup"+id+".timespan")));
    		followUp.setFollowupProvider(request.getParameter("followup"+id+".followupProvider"));
    		followUp.setUrgency(request.getParameter("followup"+id+".urgency"));
    		followUp.setComment(request.getParameter("followup"+id+".comment"));
    		followUp.setTimeframe(request.getParameter("followup"+id+".timeframe"));
    		if(followUp.getTimespan()==0) {
    			followUpDao.remove(Integer.parseInt(id));    			
    		} else {
    			followUpDao.merge(followUp);
    		}
    	}
    	
    	ids = request.getParameterValues("procedures.id");
    	for(String id:ids) {
    		ProcedureBook proc = procBookDao.find(Integer.parseInt(id));
    		proc.setProcedureName(request.getParameter("proc"+id+".procedureName"));
    		proc.setEye(request.getParameter("proc"+id+".eye"));
    		proc.setLocation(request.getParameter("proc"+id+".location"));
    		proc.setComment(request.getParameter("proc"+id+".comment"));
    		if(proc.getProcedureName().equals("")) {
    			procBookDao.remove(Integer.parseInt(id));
    		} else {
    			procBookDao.merge(proc);
    		}
    	}
    	
    	ids = request.getParameterValues("tests.id");
    	for(String id:ids) {
    		TestBookRecord test = testBookDao.find(Integer.parseInt(id));
    		test.setTestname(request.getParameter("test"+id+".testname"));
    		test.setEye(request.getParameter("test"+id+".eye"));
    		test.setUrgency(request.getParameter("test"+id+".urgency"));
    		test.setComment(request.getParameter("test"+id+".comment"));
    		if(test.getTestname().equals("")) {
    			testBookDao.remove(Integer.parseInt(id));
    		} else {
    			testBookDao.merge(test);
    		}
    	}
    	
    	return mapping.findForward("success");
    }
    
    
    
    
    public static String printFollowUpTypeOptions(FollowUp followUp) {
    	StringBuilder sb = new StringBuilder();
    	String s1 = new String();
    	String s2 = new String();
    	if(followUp.getType().equals("followUp")) s1 = "selected=\"selected\"";
    	if(followUp.getType().equals("consult")) s2 = "selected=\"selected\"";
    	
    	sb.append("<option value=\"followup\" "+s1+">Follow up</option>");
    	sb.append("<option value=\"consult\" "+s2+">Consult</option>");
    	return sb.toString();
    }
    
    public static String printFollowUpTimeFrameOptions(FollowUp followUp) {
    	StringBuilder sb = new StringBuilder();
    	String s1 = new String();
    	String s2 = new String();
    	String s3 = new String();
    	if(followUp.getTimeframe().equals("days")) s1 = "selected=\"selected\"";
    	if(followUp.getTimeframe().equals("weeks")) s2 = "selected=\"selected\"";
    	if(followUp.getTimeframe().equals("months")) s3 = "selected=\"selected\"";
    	
    	sb.append("<option value=\"days\" "+s1+">days</option>");
    	sb.append("<option value=\"weeks\" "+s2+">weeks</option>");
    	sb.append("<option value=\"months\" "+s3+">months</option>");
    	return sb.toString();
    }
    
    public static String printFollowUpProvidersOptions(FollowUp followUp) {
    	List<Provider> providers = providerDao.getActiveProviders();
    	StringBuilder sb = new StringBuilder();
    	for(Provider p:providers) {
    		if(p.getProviderNo().equals(followUp.getFollowupProvider())) {
    			sb.append("<option value=\""+p.getProviderNo()+"\" selected=\"selected\">"+p.getFormattedName()+"</option>");
    		} else {
    			sb.append("<option value=\""+p.getProviderNo()+"\">"+p.getFormattedName()+"</option>");sb.append("");
    		}
    	}
    	return sb.toString();
    }
    
    public static String printFollowUpUrgency(FollowUp followUp) {
    	StringBuilder sb = new StringBuilder();
    	String s1 = new String();
    	String s2 = new String();
    	if(followUp.getUrgency().equals("routine")) s1 = "selected=\"selected\"";
    	if(followUp.getUrgency().equals("asap")) s2 = "selected=\"selected\"";
    	
    	sb.append("<option value=\"routine\" "+s1+">routine</option>");
    	sb.append("<option value=\"asap\" "+s2+">ASAP</option>");
    	return sb.toString();
    }
    
    public static String printProcedureEye(ProcedureBook proc) {
    	StringBuilder sb = new StringBuilder();
    	String[] values = {"OU","OD","OS","OD then OS","OS then OD"};
    	
    	for(String val:values) {
    		if(proc.getEye().equals(val)) {
    			sb.append("<option value=\""+val+"\" selected=\"selected\">"+val+"</option>");
    		} else {
    			sb.append("<option value=\""+val+"\">"+val+"</option>");
    		}
    	}
    	
    	return sb.toString();
    }
 
    public static String printTestEye(TestBookRecord test ){ 	
		StringBuilder sb = new StringBuilder();
    	String[] values = {"OU","OD","OS","n/a"};
    	
    	for(String val:values) {
    		if(test.getEye().equals(val)) {
    			sb.append("<option value=\""+val+"\" selected=\"selected\">"+val+"</option>");
    		} else {
    			sb.append("<option value=\""+val+"\">"+val+"</option>");
    		}
    	}
    	
    	return sb.toString();
    }
    
    public static String printTestUrgency(TestBookRecord test) {    	
		StringBuilder sb = new StringBuilder();
    	String[] values = {"routine","ASAP","PTNV"};
    	
    	for(String val:values) {
    		if(test.getUrgency().equals(val)) {
    			sb.append("<option value=\""+val+"\" selected=\"selected\">"+val+"</option>");
    		} else {
    			sb.append("<option value=\""+val+"\">"+val+"</option>");
    		}
    	}
    	
    	return sb.toString();
    }
}
