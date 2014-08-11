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

package org.oscarehr.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.model.Facility;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

public class OcanIarSubmitAction extends DispatchAction {

	Logger logger = MiscUtils.getLogger();
	
	public ActionForward submit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		Facility facility=loggedInInfo.getCurrentFacility();
		
		String assessmentid=request.getParameter("account");
		int submissionId_full = OcanReportUIBean.sendSubmissionToIAR(facility,OcanReportUIBean.generateOCANSubmission(facility.getId(),"FULL",assessmentid ));
		
		try {
			response.getWriter().println(submissionId_full);
		}catch(IOException e) {
			logger.error("Error",e);
		}
		
		int submissionId_self = OcanReportUIBean.sendSubmissionToIAR(facility,OcanReportUIBean.generateOCANSubmission(facility.getId(),"SELF",assessmentid));
		
		try {
			response.getWriter().println(submissionId_self);
		}catch(IOException e) {
			logger.error("Error:",e);
		}
		
		int submissionId_core = OcanReportUIBean.sendSubmissionToIAR(facility,OcanReportUIBean.generateOCANSubmission(facility.getId(),"CORE",assessmentid));
		
		try {
			response.getWriter().println(submissionId_core);
		}catch(IOException e) {
			logger.error("Error:",e);
		}
		
		return null;
	}
}
