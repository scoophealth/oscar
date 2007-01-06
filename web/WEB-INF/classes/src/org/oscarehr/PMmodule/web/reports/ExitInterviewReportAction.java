/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.web.reports;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.ConsentInterview;
import org.oscarehr.PMmodule.service.ConsentManager;

import com.Ostermiller.util.CSVPrinter;

public class ExitInterviewReportAction extends DispatchAction {

	private ConsentManager consentManager;
	
	public void setConsentManager(ConsentManager mgr) {
		this.consentManager = mgr;
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return form(mapping,form,request,response);
	}
	
	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("interviews", consentManager.getConsentInterviews());
		return mapping.findForward("form");
	}
	
	public ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		List interviews = consentManager.getConsentInterviews();
		
		try {
			response.setContentType("text/x-csv");
			response.addHeader("Content-Disposition", "attachement; filename=exit-interview-data.csv");
			
			CSVPrinter printer = new CSVPrinter(response.getOutputStream());
			printer.setAutoFlush(true);
			printer.println(new String[] {"Language","Language-Other","Language (Read)","Language (Read)-Other","Education","Review","Review-Comments","Pressure","Pressure-Comments","Information","Information-Comments","Followup","Followup-Comments","Comments","Comments-Comments"});
			
			for(Iterator iter = interviews.iterator();iter.hasNext();) {
				ConsentInterview interview = (ConsentInterview)iter.next();
				String data[] = new String[15];
				data[0] = interview.getLanguage();
				data[1] = interview.getLanguageOther();
				data[2] = interview.getLanguageRead();
				data[3] = interview.getLanguageReadOther();
				data[4] = interview.getEducation();
				data[5] = interview.getReview();
				data[6] = interview.getReviewOther();
				data[7] = interview.getPressure();
				data[8] = interview.getPressureOther();
				data[9] = interview.getInformation();
				data[10] = interview.getInformationOther();
				data[11] = interview.getFollowup();
				data[12] = interview.getFollowupOther();
				data[13] = interview.getComments();
				data[14] = interview.getCommentsOther();
				printer.println(data);
			}
			
			printer.close();
		}catch(Exception e) {
			log.error(e);
		}		
		
		return null;
		
	}
	
}
