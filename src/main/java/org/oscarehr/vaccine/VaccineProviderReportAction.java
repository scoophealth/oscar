/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.vaccine;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.GenericIntakeManager;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

public class VaccineProviderReportAction extends DispatchAction {
	private static Logger log = MiscUtils.getLogger();

	private ClientManager clientManager;
	private GenericIntakeManager genericIntakeManager;

	public void setGenericIntakeManager(GenericIntakeManager mgr) {
		this.genericIntakeManager = mgr;
	}
		
	protected void postMessage(HttpServletRequest request, String key, String val) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(key,val));
		saveMessages(request,messages);
	}
	
	protected void postMessage(HttpServletRequest request, String key) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(key));
		saveMessages(request,messages);
	}

	protected String getProviderNo(HttpServletRequest request) {
		return (String)request.getSession().getAttribute("user");
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return show_report(mapping,form,request,response);
	}
	
	/*
	 * Client Name
	 * DOB
	 * Health Card
	 * List of current Bed and Service programs (+contact info)
	 * list of current issues
	 * list of medications
	 * 
	 */
	public ActionForward show_report(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String clientId = request.getParameter("id");
			
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
        
		Demographic client = clientManager.getClientByDemographicNo(clientId);
		
		if(client == null) {
			postMessage(request,"client.missing");
			log.warn("client not found");
			return mapping.findForward("error");
		}
		
		String name = client.getFormattedName();
		String dob = client.getFormattedDob();
		String healthCard = client.getHin() + " " + client.getVer();
				
		
		request.setAttribute("demographicNo",clientId);
		request.setAttribute("client_name",name);
		request.setAttribute("client_dob",dob);
		request.setAttribute("client_healthCard",healthCard);
					
		//List allergies = this.caseManagementManager.getAllergies(clientId);
		//request.setAttribute("allergies",allergies);
		
		Intake quickIntake = genericIntakeManager.getMostRecentQuickIntake(Integer.parseInt(clientId), loggedInInfo.getCurrentFacility().getId());
		Map<String,String> answerMap = quickIntake.getAnswerKeyValues();
		String allergies = answerMap.get("Allergies");
		request.setAttribute("allergies", allergies);
		request.setAttribute("intakeMap", answerMap);
		

		return mapping.findForward("report");
	}

    public void setClientManager(ClientManager mgr) {
    	this.clientManager = mgr;
    }

}
