/**
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
package org.oscarehr.PMmodule.web;

import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.IntakeRequiredFieldsDao;

import com.quatro.service.LookupManager;

public class EditIntakeAction extends BaseAction {

    private static Logger logger = LogManager.getLogger(EditIntakeAction.class);
    protected LookupManager lookupManager;
    protected CaseManagementManager caseManagementManager;
    protected AdmissionManager admissionManager;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return view(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        // not doing anything
        return mapping.findForward("view");
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        // due to the nature of check boxes...
        // reset everything to not required
        // set only the boxes checked to required.

        try {
            IntakeRequiredFieldsDao.setAllNotRequired();

            @SuppressWarnings("unchecked")
            Enumeration<String> e = request.getParameterNames();
            while (e.hasMoreElements()) {
                String key = e.nextElement();
                String value = request.getParameter(key).toLowerCase().trim();

                if ("on".equals(value)) {
                    IntakeRequiredFieldsDao.setRequired(key);
                }
            }
        }
        catch (SQLException e) {
            logger.error("Unexpected error occurred.", e);
        }

        return mapping.findForward("view");
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
}
