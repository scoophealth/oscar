/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.eyeform.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.eyeform.dao.EyeformFollowUpDao;
import org.oscarehr.eyeform.dao.EyeformProcedureBookDao;
import org.oscarehr.eyeform.dao.EyeformTestBookDao;
import org.oscarehr.eyeform.model.EyeformFollowUp;
import org.oscarehr.eyeform.model.EyeformProcedureBook;
import org.oscarehr.eyeform.model.EyeformTestBook;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class PlanAction extends DispatchAction {

	Logger logger = MiscUtils.getLogger();

	protected EyeformFollowUpDao followUpDao = SpringUtils.getBean(EyeformFollowUpDao.class);
	protected EyeformProcedureBookDao procBookDao = SpringUtils.getBean(EyeformProcedureBookDao.class);
	protected EyeformTestBookDao testBookDao = SpringUtils.getBean(EyeformTestBookDao.class);
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

    	String strAppointmentNo = request.getParameter("followup.appointmentNo");
    	int appointmentNo = Integer.parseInt(strAppointmentNo);

    	//get all follow ups, procs, and tests for this appointment
    	List<EyeformFollowUp> followUps = followUpDao.getByAppointmentNo(appointmentNo);
    	request.setAttribute("followUps", followUps);
    	request.setAttribute("followup_num", followUps.size());

    	List<EyeformProcedureBook> procedures = procBookDao.getByAppointmentNo(appointmentNo);
    	request.setAttribute("procedures", procedures);
    	request.setAttribute("procedure_num", procedures.size());

    	List<EyeformTestBook> tests = testBookDao.getByAppointmentNo(appointmentNo);
    	request.setAttribute("tests", tests);
    	request.setAttribute("test_num", tests.size());

        return mapping.findForward("form");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String strAppointmentNo = request.getParameter("appointmentNo");
    	int appointmentNo = Integer.parseInt(strAppointmentNo);

    	request.setAttribute("providers",providerDao.getActiveProviders());
    	//get all follow ups, procs, and tests for this appointment
    	List<EyeformFollowUp> followUps = followUpDao.getByAppointmentNo(appointmentNo);
    	List<EyeformProcedureBook> procedures = procBookDao.getByAppointmentNo(appointmentNo);
    	List<EyeformTestBook> tests = testBookDao.getByAppointmentNo(appointmentNo);

    	request.setAttribute("followUps", followUps);
    	request.setAttribute("procedures", procedures);
    	request.setAttribute("tests", tests);

    	return mapping.findForward("edit");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		int demographicNo = Integer.parseInt(request.getParameter("followup.demographicNo"));
    	int appointmentNo = Integer.parseInt(request.getParameter("followup.appointmentNo"));

    	int maxFollowUp = Integer.parseInt(request.getParameter("followup_num"));
    	for(int x=1;x<=maxFollowUp;x++) {
    		String id = request.getParameter("followup_"+x+".id");
    		if(id != null) {
    			String timespan = request.getParameter("followup_"+x+".timespan");
    			//if(timespan.length() == 0) {
    			//	continue;
    			//}
//    			try {
//    				Integer.parseInt(timespan);
//    			}catch(NumberFormatException e) {
//    				timespan="0";
//    			}
    			EyeformFollowUp fu = new EyeformFollowUp();
    			if(id.length()>0 && Integer.parseInt(id)>0) {
    				fu = followUpDao.find(Integer.parseInt(id));
    			} else {
    				fu.setDate(new Date());
    				fu.setDemographicNo(demographicNo);
    				fu.setAppointmentNo(appointmentNo);
    			}
    			fu.setComment(request.getParameter("followup_"+x+".comment"));
    			fu.setFollowupProvider(request.getParameter("followup_"+x+".followupProvider"));
    			fu.setTimeframe(request.getParameter("followup_"+x+".timeframe"));
//    			fu.setTimespan(Integer.parseInt(timespan));
    			fu.setTimespan(timespan);
    			fu.setType(request.getParameter("followup_"+x+".type"));
    			fu.setUrgency(request.getParameter("followup_"+x+".urgency"));

    			if(fu.getId() == null)
    				followUpDao.persist(fu);
    			else
    				followUpDao.merge(fu);
    		}
    	}

    	//handle removes
    	String[] ids = request.getParameterValues("followup.delete");
    	if(ids != null) {
    		for(String id:ids) {
    			if(id.length()>0) {
        			int followUpId = Integer.parseInt(id);
        			followUpDao.remove(followUpId);    				
    			}
    		}
    	}


    	//PROCEDURES
    	int maxProcedure = Integer.parseInt(request.getParameter("procedure_num"));
    	for(int x=1;x<=maxProcedure;x++) {
    		String id = request.getParameter("procedure_"+x+".id");
    		if(id != null) {
    			EyeformProcedureBook proc = new EyeformProcedureBook();
    			if(id.length()>0 && Integer.parseInt(id)>0) {
    				proc = procBookDao.find(Integer.parseInt(id));
    			} else {
    				proc.setDate(new Date());
        			proc.setDemographicNo(demographicNo);
    				proc.setAppointmentNo(appointmentNo);

    			}
    			proc.setComment(request.getParameter("procedure_"+x+".comment"));
    			proc.setEye(request.getParameter("procedure_"+x+".eye"));
    			proc.setProcedureName(request.getParameter("procedure_"+x+".procedureName"));
    			proc.setLocation(request.getParameter("procedure_"+x+".location"));
    			proc.setUrgency(request.getParameter("procedure_"+x+".urgency"));
    			proc.setProvider(loggedInInfo.getLoggedInProviderNo());
    			if(proc.getId() == null)
    				procBookDao.persist(proc);
    			else
    				procBookDao.merge(proc);
    		}
    	}

    	//handle removes
    	ids = request.getParameterValues("procedure.delete");
    	if(ids != null) {
    		for(String id:ids) {
    			if(id.length()>0) {
        			int procedureId = Integer.parseInt(id);
        			procBookDao.remove(procedureId);    				
    			}
    		}
    	}

    	//TESTS
    	//PROCEDURES
    	int maxTest = Integer.parseInt(request.getParameter("test_num"));
    	for(int x=1;x<=maxTest;x++) {
    		String id = request.getParameter("test_"+x+".id");
    		if(id != null) {
    			EyeformTestBook test = new EyeformTestBook();
    			if(id.length()>0 && Integer.parseInt(id)>0) {
    				test = testBookDao.find(Integer.parseInt(id));
    			} else {
    				test.setDate(new Date());
        			test.setDemographicNo(demographicNo);
    				test.setAppointmentNo(appointmentNo);

    			}
    			test.setComment(request.getParameter("test_"+x+".comment"));
    			test.setEye(request.getParameter("test_"+x+".eye"));
    			test.setTestname(request.getParameter("test_"+x+".testname"));
    			test.setUrgency(request.getParameter("test_"+x+".urgency"));

    			test.setProvider(loggedInInfo.getLoggedInProviderNo());
    			if(test.getId() == null)
    				testBookDao.persist(test);
    			else
    				testBookDao.merge(test);
    		}
    	}

    	//handle removes
    	ids = request.getParameterValues("test.delete");
    	if(ids != null) {
    		for(String id:ids) {
    			if(id.length()>0) {
    				int testId = Integer.parseInt(id);
    				testBookDao.remove(testId);
    			}
    		}
    	}

    	return mapping.findForward("success");
    }


    public ActionForward save_edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	//try to get all the follow ups in the form
    	String[] ids = request.getParameterValues("followup.id");
    	if(ids != null) {
	    	for(String id:ids) {
	    		EyeformFollowUp followUp = followUpDao.find(Integer.parseInt(id));
	    		followUp.setType(request.getParameter("followup"+id+".type"));
//	    		followUp.setTimespan(Integer.parseInt(request.getParameter("followup"+id+".timespan")));
	    		followUp.setTimespan(request.getParameter("followup"+id+".timespan"));
	    		followUp.setFollowupProvider(request.getParameter("followup"+id+".followupProvider"));
	    		followUp.setUrgency(request.getParameter("followup"+id+".urgency"));
	    		followUp.setComment(request.getParameter("followup"+id+".comment"));
	    		followUp.setTimeframe(request.getParameter("followup"+id+".timeframe"));
//	    		if(followUp.getTimespan()==0) {
	    		if(followUp.getTimespan().equals("0") || followUp.getTimespan().equals("")) {
	    			followUpDao.remove(Integer.parseInt(id));
	    		} else {
	    			followUpDao.merge(followUp);
	    		}
	    	}
    	}

    	ids = request.getParameterValues("procedures.id");
    	if(ids != null) {
	    	for(String id:ids) {
	    		EyeformProcedureBook proc = procBookDao.find(Integer.parseInt(id));
	    		proc.setProcedureName(request.getParameter("proc"+id+".procedureName"));
	    		proc.setEye(request.getParameter("proc"+id+".eye"));
	    		proc.setUrgency(request.getParameter("proc"+id+".urgency"));
	    		proc.setLocation(request.getParameter("proc"+id+".location"));
	    		proc.setComment(request.getParameter("proc"+id+".comment"));
	    		if(proc.getProcedureName().equals("")) {
	    			procBookDao.remove(Integer.parseInt(id));
	    		} else {
	    			procBookDao.merge(proc);
	    		}
	    	}
    	}

    	ids = request.getParameterValues("tests.id");
    	if(ids != null) {
	    	for(String id:ids) {
	    		EyeformTestBook test = testBookDao.find(Integer.parseInt(id));
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
    	}

    	return mapping.findForward("success");
    }




    public static String printFollowUpTypeOptions(EyeformFollowUp followUp) {
    	StringBuilder sb = new StringBuilder();
    	String s1 = new String();
    	String s2 = new String();
    	if(followUp.getType().equals("followUp")) s1 = "selected=\"selected\"";
    	if(followUp.getType().equals("consult")) s2 = "selected=\"selected\"";

    	sb.append("<option value=\"followup\" "+s1+">Follow up</option>");
    	sb.append("<option value=\"consult\" "+s2+">Consult</option>");
    	return sb.toString();
    }

    public static String printFollowUpTimeFrameOptions(EyeformFollowUp followUp) {
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

    public static String printFollowUpProvidersOptions(EyeformFollowUp followUp) {
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

    public static String printFollowUpUrgency(EyeformFollowUp followUp) {
    	StringBuilder sb = new StringBuilder();
    	String s1 = new String();
    	String s2 = new String();
    	if(followUp.getUrgency().equals("routine")) s1 = "selected=\"selected\"";
    	if(followUp.getUrgency().equals("asap")) s2 = "selected=\"selected\"";

    	sb.append("<option value=\"routine\" "+s1+">routine</option>");
    	sb.append("<option value=\"asap\" "+s2+">ASAP</option>");
    	return sb.toString();
    }

    public static String printProcedureEye(EyeformProcedureBook proc) {
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

    public static String printProcedureUrgency(EyeformProcedureBook proc) {
    	StringBuilder sb = new StringBuilder();
    	String[] values = {"Routine","ASAP","Urgent"};

    	for(String val:values) {
    		if(proc.getEye().equals(val)) {
    			sb.append("<option value=\""+val+"\" selected=\"selected\">"+val+"</option>");
    		} else {
    			sb.append("<option value=\""+val+"\">"+val+"</option>");
    		}
    	}

    	return sb.toString();
    }

    public static String printTestEye(EyeformTestBook test ){
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

    public static String printTestUrgency(EyeformTestBook test) {
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
