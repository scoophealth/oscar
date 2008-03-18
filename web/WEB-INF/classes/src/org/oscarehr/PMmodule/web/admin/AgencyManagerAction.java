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

package org.oscarehr.PMmodule.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.caisi.integrator.model.transfer.AgencyTransfer;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.AgencyManager;
import org.oscarehr.PMmodule.service.GenericIntakeManager;
import org.oscarehr.PMmodule.web.BaseAction;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.util.SpringUtils;
import org.springframework.scheduling.timer.ScheduledTimerTask;

import com.quatro.service.LookupManager;

public class AgencyManagerAction extends BaseAction {

    private static final Log log = LogFactory.getLog(AgencyManagerAction.class);

    private static final String FORWARD_EDIT = "edit";
    private static final String FORWARD_VIEW = "view";
    private static final String FORWARD_EDIT_INTEGRATOR = "edit_integrator";
    private static final String FORWARD_VIEW_INTEGRATOR = "view_integrator";
    private static final String FORWARD_VIEW_COMMUNITY = "view_community";

    private static final String BEAN_AGENCY = "agency";

    protected LookupManager lookupManager;

    protected CaseManagementManager caseManagementManager;

    protected AdmissionManager admissionManager;

    protected GenericIntakeManager genericIntakeManager;

    protected AgencyManager agencyManager;


    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return view(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm agencyForm = (DynaActionForm) form;

        boolean integratorEnabled = integratorManager.isEnabled();

        request.setAttribute(BEAN_AGENCY, agencyManager.getLocalAgency());

        request.setAttribute("integrator_enabled", integratorEnabled);

        if (integratorEnabled) request.setAttribute("integrator_version", integratorManager.getIntegratorVersion());

        return mapping.findForward(FORWARD_VIEW);
    }

    public ActionForward view_integrator(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        boolean integratorEnabled = integratorManager.isEnabled();

        AgencyTransfer[] agencyList = integratorManager.getAgencies();

        request.setAttribute("agencies", agencyList);
        request.setAttribute(BEAN_AGENCY, agencyManager.getLocalAgency());
        request.setAttribute("integrator_enabled", integratorEnabled);

        if (integratorEnabled) request.setAttribute("integrator_version", integratorManager.getIntegratorVersion());

        return mapping.findForward(FORWARD_VIEW_INTEGRATOR);
    }

    public ActionForward view_community(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        boolean integratorEnabled = integratorManager.isEnabled();

        AgencyTransfer[] agencyList = integratorManager.getAgencies();

        request.setAttribute("agencies", agencyList);
        request.setAttribute(BEAN_AGENCY, agencyManager.getLocalAgency());
        request.setAttribute("integrator_enabled", integratorEnabled);

        if (integratorEnabled) request.setAttribute("integrator_version", integratorManager.getIntegratorVersion());

        return mapping.findForward(FORWARD_VIEW_COMMUNITY);
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm agencyForm = (DynaActionForm) form;

        Agency localAgency = agencyManager.getLocalAgency();

        agencyForm.set(BEAN_AGENCY, localAgency);

        request.setAttribute("id", localAgency.getId());
        request.setAttribute("integratorEnabled", localAgency.isIntegratorEnabled());
        request.setAttribute("integrator_enabled", integratorManager.isEnabled());

        return mapping.findForward(FORWARD_EDIT);
    }

    public ActionForward edit_integrator(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm agencyForm = (DynaActionForm) form;

        Agency localAgency = agencyManager.getLocalAgency();

        agencyForm.set(BEAN_AGENCY, localAgency);

        request.setAttribute("id", localAgency.getId());
        request.setAttribute("integratorEnabled", localAgency.isIntegratorEnabled());
        request.setAttribute("integrator_enabled", integratorManager.isEnabled());

        return mapping.findForward(FORWARD_EDIT_INTEGRATOR);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm agencyForm = (DynaActionForm) form;

        Agency agency = (Agency) agencyForm.get(BEAN_AGENCY);

        if (isCancelled(request)) {
            request.getSession().removeAttribute("agencyManagerForm");
            request.setAttribute("id", agency.getId());

            return view(mapping, form, request, response);
        }

        if (request.getParameter("agency.hic") == null) {
            agency.setHic(false);
        }

        agencyManager.saveLocalAgency(agency);

        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("agency.saved", agency.getName()));
        saveMessages(request, messages);

        request.setAttribute("id", agency.getId());
        request.setAttribute("integratorEnabled", agency.isIntegratorEnabled());

        logManager.log("write", "agency", agency.getId().toString(), request);

        return mapping.findForward(FORWARD_EDIT);
    }

    public ActionForward enable_integrator(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Agency agency = agencyManager.getLocalAgency();
        agency.setIntegratorEnabled(true);
        agencyManager.saveAgency(agency);

        request.setAttribute("id", agency.getId());
        request.setAttribute("integratorEnabled", agency.isIntegratorEnabled());

        return mapping.findForward(FORWARD_EDIT);
    }

    public ActionForward disable_integrator(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Agency agency = agencyManager.getLocalAgency();

        agency.setIntegratorEnabled(false);
        agencyManager.saveAgency(agency);

        request.setAttribute("id", agency.getId());
        request.setAttribute("integratorEnabled", agency.isIntegratorEnabled());

        return mapping.findForward(FORWARD_EDIT);
    }

    public ActionForward refresh_integrator(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        ScheduledTimerTask tt=(ScheduledTimerTask)SpringUtils.beanFactory.getBean("scheduledIntegratorUpdateTask");
        
        Thread t=new Thread(tt.getTimerTask());
        t.start();
        
        return view(mapping, form, request, response);
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