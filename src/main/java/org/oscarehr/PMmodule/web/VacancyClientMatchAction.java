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

package org.oscarehr.PMmodule.web;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.WaitlistDao;
import org.oscarehr.PMmodule.wlmatch.MatchBO;
import org.oscarehr.PMmodule.wlmatch.MatchingManager;
import org.oscarehr.PMmodule.wlmatch.VacancyDisplayBO;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;


public class VacancyClientMatchAction extends DispatchAction{
	private static final Logger logger = MiscUtils.getLogger();
	private WaitlistDao waitlistDao = SpringUtils.getBean(WaitlistDao.class);
	
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		DynaActionForm intakeForm = (DynaActionForm) form;
		MatchingManager matchingManager = new MatchingManager();
		double minPercentage = 0.0;
		try {
			minPercentage = Double.parseDouble(request.getParameter("percentage"));
		} catch (Exception e) {
			logger.info("Failed to get match cutoff!");
		}
		Integer vacancyId=Integer.parseInt(request.getParameter("vacancyId"));
		logger.info("vacancyID: " + vacancyId);
		//List<MatchBO> matchList= matchingManager.listTopMatches(vacancyID, 50);
		List<MatchBO> matchList= matchingManager.getClientMatchesWithMinMatchPercentage(vacancyId,minPercentage);
		logger.info(" VacancyClientMatchList: " + matchList.size());
		
		request.setAttribute("clientList", matchList);
		
		VacancyDisplayBO dis = matchingManager.getVacancyDisplay(vacancyId);
		intakeForm.set("template", dis.getVacancyTemplateName());
		
		intakeForm.set("vacancy", dis.getVacancyName());
		
		List<String> criteriaList = getCriteriaList(dis.getCriteriaSummary());
		intakeForm.set("criteriaList", criteriaList);
		
		int programId = waitlistDao.getProgramIdByVacancyId(vacancyId);
		intakeForm.set("programId", programId);
	    return mapping.findForward("success");
	}

	private List<String> getCriteriaList(String criteriaSummary) {
	    List<String> criteriaList = new ArrayList<String>();
		if(criteriaSummary != null && criteriaSummary.length() > 0){
			criteriaSummary = criteriaSummary.replaceAll(" AND ", "\n");
			StringTokenizer st = new StringTokenizer(criteriaSummary, "\n");
		     while (st.hasMoreTokens()) {
		    	 String token = st.nextToken();
		    	 if(token != null && token.trim().length() > 0){
		    		 criteriaList.add(token.trim());
		    	 }
		     }
		}
	    return criteriaList;
    }
}
