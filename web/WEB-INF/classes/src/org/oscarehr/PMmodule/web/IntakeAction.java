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

package org.oscarehr.PMmodule.web;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.caisi.integrator.model.transfer.ClientTransfer;
import org.oscarehr.PMmodule.exception.IntegratorException;
import org.oscarehr.PMmodule.exception.IntegratorNotEnabledException;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.AgencyManager;
import org.oscarehr.PMmodule.service.BedCheckTimeManager;
import org.oscarehr.PMmodule.service.BedDemographicManager;
import org.oscarehr.PMmodule.service.BedManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.ConsentManager;
import org.oscarehr.PMmodule.service.FormsManager;
import org.oscarehr.PMmodule.service.GenericIntakeManager;
import org.oscarehr.PMmodule.service.IntakeAManager;
import org.oscarehr.PMmodule.service.IntakeCManager;
import org.oscarehr.PMmodule.service.IntegratorManager;
import org.oscarehr.PMmodule.service.LogManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProgramQueueManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.service.RoleManager;
import org.oscarehr.PMmodule.service.RoomDemographicManager;
import org.oscarehr.PMmodule.service.RoomManager;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.PMmodule.web.formbean.PreIntakeForm;
import org.oscarehr.PMmodule.web.utils.UserRoleUtils;
import org.oscarehr.casemgmt.service.CaseManagementManager;

import com.quatro.service.LookupManager;

public class IntakeAction extends BaseAction {

    private static Log log = LogFactory.getLog(IntakeAction.class);
    protected LookupManager lookupManager;
    protected CaseManagementManager caseManagementManager;
    protected AdmissionManager admissionManager;
    protected GenericIntakeManager genericIntakeManager;
    protected AgencyManager agencyManager;
    protected BedCheckTimeManager bedCheckTimeManager;
    protected RoomDemographicManager roomDemographicManager;
    protected BedDemographicManager bedDemographicManager;
    protected BedManager bedManager;
    protected ClientManager clientManager;
    protected ConsentManager consentManager;
    protected FormsManager formsManager;
    protected IntakeAManager intakeAManager;
    protected IntakeCManager intakeCManager;
    protected IntegratorManager integratorManager;
    protected LogManager logManager;
    protected ProgramManager programManager;
    protected ProviderManager providerManager;
    protected ProgramQueueManager programQueueManager;
    protected RoleManager roleManager;
    protected RoomManager roomManager;


    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("demographic", null);

        return mapping.findForward("pre-intake");
    }

    public ActionForward do_intake(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm preIntakeForm = (DynaActionForm) form;
        PreIntakeForm formBean = (PreIntakeForm) preIntakeForm.get("form");
        request.getSession().setAttribute("demographic", null);
        boolean doLocalSearch = false;


        Demographic[] results = new Demographic[0];

        /* here we want to switch to the integrator, if available */
        Demographic d = new Demographic();
        d.setFirstName(formBean.getFirstName());
        d.setLastName(formBean.getLastName());
        d.setYearOfBirth(formBean.getYearOfBirth());
        d.setMonthOfBirth(String.valueOf(formBean.getMonthOfBirth()));
        d.setDateOfBirth(String.valueOf(formBean.getDayOfBirth()));
        d.setHin(formBean.getHealthCardNumber());
        d.setVer(formBean.getHealthCardVersion());

        try {
            Collection<ClientTransfer> demographicCollection = integratorManager.matchClient(d);
            log.debug("integrator found " + demographicCollection.size() + " match(es)");
        } catch (IntegratorNotEnabledException e) {
            log.info(e);
            doLocalSearch = true;
        } catch (Throwable e) {
            log.error(e);
            doLocalSearch = true;
        }
        if (doLocalSearch) {
            ClientSearchFormBean searchBean = new ClientSearchFormBean();
            searchBean.setFirstName(formBean.getFirstName());
            searchBean.setLastName(formBean.getLastName());
            searchBean.setSearchOutsideDomain(true);
            searchBean.setSearchUsingSoundex(true);

            boolean allowOnlyOptins = UserRoleUtils.hasRole(request, UserRoleUtils.Roles.external);

            List resultList = clientManager.search(searchBean, allowOnlyOptins);
            results = (Demographic[]) resultList.toArray(new Demographic[resultList.size()]);
            log.debug("local search found " + results.length + " match(es)");

        }

        if (results != null && results.length > 0) {
            request.setAttribute("localSearch", new Boolean(doLocalSearch));
            request.setAttribute("clients", results);
            return mapping.findForward("pre-intake");
        }

        return new_client(mapping, form, request, response);
    }

    /*
      * There can be a new client in 2 scenerios
      * 1) new client button was clicked.
      * 2) new client, but they are being linked to a record already
      * 		existing on the integrator; in which case the session variable
      * 		'demographic' will be set.
      *
      */
    public ActionForward new_client(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm preIntakeForm = (DynaActionForm) form;
        PreIntakeForm formBean = (PreIntakeForm) preIntakeForm.get("form");

        /* no matches */
        if (formBean.getDemographicId().equals("")) {
            return mapping.findForward(getIntakeForward());
        }


        Demographic demographic = null;
        if (!integratorManager.getLocalAgency().getIntegratorUsername().equals(formBean.getAgencyId())) {
            //integrator
            try {
                demographic = integratorManager.getDemographic(formBean.getAgencyId(), Long.valueOf(formBean.getDemographicId()));
            } catch (IntegratorException e) {
                log.error(e);
            }
        } else {
            //local...can this even happen?
            //demographic = clientManager.getClientByDemographicNo(formBean.getDemographicId());
        }

        request.getSession().setAttribute("demographic", demographic);

        return mapping.findForward(getIntakeForward());
    }

    /*
      * This is just updating the intake form on a client since
      * he/she are already in the local database.
      */
    public ActionForward update_client(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm preIntakeForm = (DynaActionForm) form;
        PreIntakeForm formBean = (PreIntakeForm) preIntakeForm.get("form");

        String demographicNo = formBean.getDemographicId();

        log.debug("update intake for client " + demographicNo);

        request.setAttribute("demographicNo", demographicNo);

        return mapping.findForward(getIntakeForward());
    }


    private String getIntakeForward() {
        String value = "intakea";

        if (intakeAManager.isNewClientForm()) {
            value = "intakea";
        }
        if (intakeCManager.isNewClientForm()) {
            value = "intakec";
        }
        return value;
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

    public void setBedCheckTimeManager(BedCheckTimeManager bedCheckTimeManager) {
        this.bedCheckTimeManager = bedCheckTimeManager;
    }

    public void setBedDemographicManager(BedDemographicManager demographicBedManager) {
    	this.bedDemographicManager = demographicBedManager;
    }

    public void setRoomDemographicManager(RoomDemographicManager roomDemographicManager) {
    	this.roomDemographicManager = roomDemographicManager;
    }

    public void setBedManager(BedManager bedManager) {
    	this.bedManager = bedManager;
    }

    public void setClientManager(ClientManager mgr) {
    	this.clientManager = mgr;
    }

    public void setConsentManager(ConsentManager mgr) {
    	this.consentManager = mgr;
    }

    public void setFormsManager(FormsManager mgr) {
    	this.formsManager = mgr;
    }

    public void setIntakeAManager(IntakeAManager mgr) {
    	this.intakeAManager = mgr;
    }

    public void setIntakeCManager(IntakeCManager mgr) {
    	this.intakeCManager = mgr;
    }

    public void setIntegratorManager(IntegratorManager mgr) {
    	this.integratorManager = mgr;
    }

    public void setLogManager(LogManager mgr) {
    	this.logManager = mgr;
    }

    public void setProgramManager(ProgramManager mgr) {
    	this.programManager = mgr;
    }

    public void setProgramQueueManager(ProgramQueueManager mgr) {
    	this.programQueueManager = mgr;
    }

    public void setProviderManager(ProviderManager mgr) {
    	this.providerManager = mgr;
    }

    public void setRoleManager(RoleManager mgr) {
    	this.roleManager = mgr;
    }

    public void setRoomManager(RoomManager roomManager) {
    	this.roomManager = roomManager;
    }
}
