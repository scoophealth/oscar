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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.AgencyManager;
import org.oscarehr.PMmodule.service.GenericIntakeManager;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.oscarehr.PMmodule.web.BaseAction;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.survey.SurveyReport;
import org.oscarehr.survey.SurveyReportEntry;
import org.oscarehr.surveymodel.SurveyDocument.Survey;

import com.quatro.service.LookupManager;

public class BaseUCFReport extends BaseAction {

    protected SurveyManager surveyManager;
    protected LookupManager lookupManager;
    protected CaseManagementManager caseManagementManager;
    protected AdmissionManager admissionManager;
    protected GenericIntakeManager genericIntakeManager;
    protected AgencyManager agencyManager;

    public void setSurveyManager(SurveyManager mgr) {
        this.surveyManager = mgr;
    }

	protected String getFormId() {
		return "6";
	}
	
	protected List getClients() {
		return clientManager.getClients();
	}
	
	protected SurveyReport getConfiguration() {
		SurveyReport config = new SurveyReport();
		SurveyReportEntry entry = new SurveyReportEntry();
		
		//get average age
		entry.setOperation(SurveyReport.OPERATION_AVERAGE);
		entry.setPageNumber(1);
		entry.setSectionId(0);
		entry.setQuestionId(1);
		
		config.getEntries().add(entry);
		
		return config;
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return report(mapping,form,request,response);
	}
	public ActionForward report(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		//get clients
		List clients = getClients();
		
		//list of OscarFormInstance
		List forms = surveyManager.getCurrentForms(getFormId(), clients);
		
		//get configuration
		SurveyReport config = getConfiguration();
		
		//getSurvey
		Survey survey = surveyManager.getFormModel(getFormId());
		
		//Generate the report
		
		
		return mapping.findForward("report");
	}

    public void setLookupManager(LookupManager lookupManager) {
    	this.lookupManager = lookupManager;
    }

    public void setCaseManagementManager(CaseManagementManager caseManagementManager) {
    	this.caseManagementManager = caseManagementManager;
    }

    public void setAdmissionManager(AdmissionManager mgr) {
    	this.admissionManager = mgr;
    }

    public void setGenericIntakeManager(GenericIntakeManager genericIntakeManager) {
        this.genericIntakeManager = genericIntakeManager;
    }

    public void setAgencyManager(AgencyManager mgr) {
    	this.agencyManager = mgr;
    }
}
