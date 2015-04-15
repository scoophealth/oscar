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
package org.oscarehr.sharingcenter.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.sharingcenter.dao.PatientPolicyConsentDao;
import org.oscarehr.sharingcenter.dao.PolicyDefinitionDao;
import org.oscarehr.sharingcenter.model.PolicyDefinitionDataObject;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ConsentCheckerServlet extends Action {

    private static final Logger LOGGER = MiscUtils.getLogger();
    private static final PatientPolicyConsentDao patientPolicyConsentDao = SpringUtils.getBean(PatientPolicyConsentDao.class);
    private static final PolicyDefinitionDao policyDefinitionDao = SpringUtils.getBean(PolicyDefinitionDao.class);

    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // Get request data
        int patientId = Integer.valueOf(request.getParameter("patient"));
        int domainId = Integer.valueOf(request.getParameter("domain"));
        int policyId = Integer.valueOf(request.getParameter("policy"));

        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "r", null)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
        
        // prepare response
        JSONObject policy = new JSONObject();
        try {
            PolicyDefinitionDataObject policyDef = policyDefinitionDao.getPolicyDefinition(policyId);

            if (policyDef != null) {
                policy.put("name", policyDef.getDisplayName());
                policy.put("url", policyDef.getPolicyDocUrl());
                policy.put("consent", patientPolicyConsentDao.isPatientConsentedToPolicy(patientId, policyId));
            }

        } catch (JSONException e) {
            LOGGER.error("Error creating the JSON object", e);
        }
        response.setContentType("application/json");
        response.getWriter().write(policy.toString());
        return null;
    }
}
