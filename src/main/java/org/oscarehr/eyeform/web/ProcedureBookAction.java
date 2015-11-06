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
import org.oscarehr.eyeform.dao.EyeformProcedureBookDao;
import org.oscarehr.eyeform.model.EyeformProcedureBook;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class ProcedureBookAction extends DispatchAction {

	private static Logger logger = Logger.getLogger(ProcedureBookAction.class);
	private static EyeformProcedureBookDao procedureBookDao = SpringUtils.getBean(EyeformProcedureBookDao.class);
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	
    	
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	EyeformProcedureBook data = (EyeformProcedureBook)f.get("data");
    	if(data.getId() != null && data.getId().intValue()>0) {
    		data = procedureBookDao.find(data.getId()); 
    	}
    	
    	f.set("data", data);
    	        
        return mapping.findForward("form");
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {    	
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		DynaValidatorForm f = (DynaValidatorForm)form;
    	EyeformProcedureBook data = (EyeformProcedureBook)f.get("data");
    	if(data.getId()!=null && data.getId()==0) {
    		data.setId(null);
    	}
    	data.setProvider(loggedInInfo.getLoggedInProviderNo());	
    	procedureBookDao.save(data);
    	
    	return mapping.findForward("success");
    }

    public ActionForward getNoteText(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String appointmentNo = request.getParameter("appointmentNo");
    	
    	List<EyeformProcedureBook> procedures = procedureBookDao.getByAppointmentNo(Integer.parseInt(appointmentNo));
    	StringBuilder sb = new StringBuilder();
    	
    	for(EyeformProcedureBook f:procedures) {    		
    		sb.append("book procedure: ").append(f.getProcedureName()).append(" ").append(f.getEye()).append(" ").append(f.getLocation());
    		sb.append(" ").append(f.getComment());
    		sb.append("\n");
    	}
    	
    	try {
    		response.getWriter().print(sb.toString());
    	}catch(IOException e) {logger.error(e);}
    	
    	return null;
    }
    
    public ActionForward getTicklerText(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String appointmentNo = request.getParameter("appointmentNo");
    	
    	String text = getTicklerText(Integer.parseInt(appointmentNo));
    	
    	try {
    		response.getWriter().print(text);
    	}catch(IOException e) {logger.error(e);}
    	
    	return null;
    }
    
    public static String getTicklerText(int appointmentNo) {
    	
    	List<EyeformProcedureBook> procedures = procedureBookDao.getByAppointmentNo(appointmentNo);
    	StringBuilder sb = new StringBuilder();
    	
    	for(EyeformProcedureBook f:procedures) {
    		String style = new String();
    		if(f.getUrgency().equals("URGENT") || f.getUrgency().equals("ASAP")) {
    			style = "style=\"color:red;\"";
    		}
    		sb.append("<span "+style+" title=\""+f.getComment()+"\">proc:").append(f.getProcedureName()).append(" ").append(f.getEye()).append(" ").append(f.getLocation()).append(" ").append(f.getUrgency()).append(" ").append(f.getComment()).append("</span>");
    		sb.append("<br/>");
    	}
    	return sb.toString();
    }
}
