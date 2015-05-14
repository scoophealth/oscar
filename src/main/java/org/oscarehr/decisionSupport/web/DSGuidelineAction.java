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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.decisionSupport.model.DSCondition;
import org.oscarehr.decisionSupport.model.DSConsequence;
import org.oscarehr.decisionSupport.model.DSDemographicAccess;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.model.DSGuidelineFactory;
import org.oscarehr.decisionSupport.service.DSService;
import org.oscarehr.util.LoggedInInfo;

import oscar.oscarDemographic.data.DemographicData;

/**
 *
 * @author apavel
 */
public class DSGuidelineAction extends DispatchAction {

	private DSService dsService;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       return super.execute(mapping, form, request, response);
    }

    public DSGuidelineAction() {

    }


    public ActionForward unspecified(ActionMapping mapping, ActionForm  form,
           HttpServletRequest request, HttpServletResponse response)
           throws Exception {
           return list(mapping,form,request,response);
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
        String providerNo = request.getParameter("provider_no");
        List<DSGuideline> providerGuidelines = new ArrayList<DSGuideline>();
        if (providerNo != null)
            providerGuidelines = dsService.getDsGuidelinesByProvider(providerNo);
        request.setAttribute("guidelines", providerGuidelines);
        return mapping.findForward("guidelineList");
    }

    public ActionForward detail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
    	
        String guidelineId = request.getParameter("guidelineId");
        String demographicNo = request.getParameter("demographic_no");
        if (guidelineId == null) {
            response.getWriter().println("guidelineId cannot be null");
            return null;
        }
        DSGuidelineFactory factory = new DSGuidelineFactory();
        DSGuideline dsGuideline = dsService.findGuideline(Integer.parseInt(guidelineId));
        if (demographicNo == null) { //if just viewing details about guideline.
            request.setAttribute("guideline", dsGuideline);
            List<DSCondition> dsConditions = dsGuideline.getConditions();
            List<ConditionResult> conditionResults = new ArrayList<ConditionResult>();
            for (DSCondition dsCondition: dsConditions) {
                conditionResults.add(new ConditionResult(dsCondition, null, null));
            }
            request.setAttribute("conditionResults", conditionResults);
            return mapping.findForward("guidelineDetail");
        }
        List<DSCondition> dsConditions = dsGuideline.getConditions();
        List<ConditionResult> conditionResults = new ArrayList<ConditionResult>();
        for (DSCondition dsCondition: dsConditions) { //if viewing details about guideline in regards to patient
            DSGuideline testGuideline = factory.createBlankGuideline();
            //BeanUtils.copyProperties(dsCondition, testGuideline);
            ArrayList<DSCondition> testCondition = new ArrayList<DSCondition>();
            testCondition.add(dsCondition);
            testGuideline.setConditions(testCondition);
            testGuideline.setConsequences(new ArrayList<DSConsequence>());
            testGuideline.setTitle(dsGuideline.getTitle());
            testGuideline.setParsed(true); //supress parsing of xml, othewrise would overwrite the condition
            boolean result = testGuideline.evaluateBoolean(loggedInInfo, demographicNo);
            DSDemographicAccess demographicAccess = new DSDemographicAccess(loggedInInfo, demographicNo);
            String actualValues = demographicAccess.getDemogrpahicValues(dsCondition.getConditionType());
            conditionResults.add(new ConditionResult(dsCondition, result, actualValues));
        }
        DemographicData demographicData = new DemographicData();
        org.oscarehr.common.model.Demographic demographic = demographicData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographicNo);

        request.setAttribute("patientName", demographic.getFirstName() + " " + demographic.getLastName());
        request.setAttribute("guideline", dsGuideline);
        request.setAttribute("consequences", dsGuideline.evaluate(loggedInInfo, demographicNo));
        request.setAttribute("conditionResults", conditionResults);
        request.setAttribute("demographicAccess", new DSDemographicAccess(loggedInInfo, demographicNo));
        return mapping.findForward("guidelineDetail");

    }

    public void setDsService(DSService dsService) {
        this.dsService = dsService;
    }

    //for returning stuff
    public class ConditionResult {
        DSCondition condition;
        Boolean result;
        String actualValues;
        public ConditionResult(DSCondition condition, Boolean result, String actualValues) {
            this.condition = condition;
            this.result = result;
            this.actualValues = actualValues;
        }
        public void setCondition(DSCondition condition) { this.condition = condition; }
        public DSCondition getCondition() { return this.condition; }
        public void setResult(Boolean result) { this.result = result; }
        public Boolean getResult() { return this.result; }
        public void setActualValues(String actualValues) { this.actualValues = actualValues; }
        public String getActualValues() { return this.actualValues; }
    }
}
