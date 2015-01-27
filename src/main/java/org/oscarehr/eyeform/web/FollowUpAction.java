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
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.eyeform.dao.EyeformFollowUpDao;
import org.oscarehr.eyeform.model.EyeformFollowUp;
import org.oscarehr.util.SpringUtils;

public class FollowUpAction extends DispatchAction {

	static Logger logger = Logger.getLogger(FollowUpAction.class);
	static EyeformFollowUpDao dao = (EyeformFollowUpDao)SpringUtils.getBean(EyeformFollowUpDao.class);
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return form(mapping, form, request, response);
    }

    public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
    	request.setAttribute("providers",providerDao.getActiveProviders());
    	
    	
    	
        return mapping.findForward("form");
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {    	
    	DynaValidatorForm f = (DynaValidatorForm)form;
    	EyeformFollowUp data = (EyeformFollowUp)f.get("followup");
    	if(data.getId()!=null && data.getId()==0) {
    		data.setId(null);
    	}
	
    	dao.save(data);
    	
    	
    	return mapping.findForward("success");
    }
    
    public ActionForward getNoteText(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String appointmentNo = request.getParameter("appointmentNo");
    	
    	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
    	
    	
    	List<EyeformFollowUp> followUps = dao.getByAppointmentNo(Integer.parseInt(appointmentNo));
    	StringBuilder sb = new StringBuilder();
    	
    	for(EyeformFollowUp f:followUps) {
    		Provider p = providerDao.getProvider(f.getFollowupProvider());
    		sb.append(f.getType());
//    		if(f.getTimespan() > 0) {
    		if(!f.getTimespan().equals("0") || !f.getTimespan().equals("")){
    			sb.append(" ").append(f.getTimespan()).append(" ").append(f.getTimeframe());
    		}
    		sb.append(" Dr. ").append(p.getFormattedName()).append(" ").append(f.getUrgency());
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
    	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
    	
    	List<EyeformFollowUp> followUps = dao.getByAppointmentNo(appointmentNo);
    	StringBuilder sb = new StringBuilder();
    	
    	for(EyeformFollowUp f:followUps) {
    		Provider p = providerDao.getProvider(f.getFollowupProvider());
    		String type = "f/u:";
    		if(f.getType().equals("consult")) {
    			type="consult:";
    		}
    		sb.append(type);
//    		if(f.getTimespan() > 0) {
    		if(!f.getTimespan().equals("0") || !f.getTimespan().equals("")){
    			sb.append(" ").append(f.getTimespan()).append(" ").append(f.getTimeframe());
    		}
    		sb.append(" Dr. ").append(p.getFormattedName());
    		sb.append(" ").append(f.getComment());
    		sb.append("<br/>");
    	}
    	return sb.toString();
    }
}
