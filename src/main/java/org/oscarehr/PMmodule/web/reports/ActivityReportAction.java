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

package org.oscarehr.PMmodule.web.reports;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.AdmissionSearchBean;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.web.formbean.ActivityReportFormBean;
import org.oscarehr.util.MiscUtils;

public class ActivityReportAction extends DispatchAction {
	private static Logger log = MiscUtils.getLogger();
	private  final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	private ProgramManager programManager;
	private AdmissionManager admissionManager;
	private ClientManager clientManager;
	
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}
	
	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}
	
	public void setClientManager(ClientManager mgr) {
		this.clientManager = mgr;
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return form(mapping,form,request,response);
	}
	
	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("programs",programManager.getPrograms());
		
		return mapping.findForward("form");
	}
	
	public ActionForward generate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm reportForm = (DynaActionForm)form;
		ActivityReportFormBean formBean = (ActivityReportFormBean)reportForm.get("form");
		
		//# of new admissions
		AdmissionSearchBean searchBean = new AdmissionSearchBean();
		searchBean.setProgramId(formBean.getProgramId());
		try {
			searchBean.setStartDate(formatter.parse(formBean.getStartDate()));
			searchBean.setEndDate(formatter.parse(formBean.getEndDate()));
		}catch(Exception e) {
			log.error("Error", e);
		}
		
		Map summaryMap = new LinkedHashMap();
		
		List programs = programManager.getPrograms();
		for(Iterator iter=programs.iterator();iter.hasNext();) {
			Program p = (Program)iter.next();
			
			//Don't report inactive program
			if(!p.isActive()) {
				continue;
			}
			searchBean.setProgramId(p.getId());
			List admissions = admissionManager.search(searchBean);
			int totalAdmissions = admissions.size();
			
			ClientReferral cr = new ClientReferral();
			cr.setProgramId(new Long(p.getId().longValue()));
			List referrals = clientManager.searchReferrals(cr);
			int totalReferrals = referrals.size();
			
			Long[] values = {new Long(totalAdmissions),new Long(totalReferrals)};
			summaryMap.put(p.getName(), values);
		}
		request.setAttribute("summary", summaryMap);
		
		return mapping.findForward("report");
	}
}
