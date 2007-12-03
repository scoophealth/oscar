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

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.BedReservedException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.exception.ServiceRestrictionException;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.model.ProgramTeam;
import org.oscarehr.PMmodule.service.ClientRestrictionManager;
import org.oscarehr.PMmodule.web.BaseAction;
import org.oscarehr.PMmodule.web.formbean.ProgramManagerViewFormBean;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.springframework.beans.factory.annotation.Required;

public class ProgramManagerViewAction extends BaseAction {

    private static final Log log = LogFactory.getLog(ProgramManagerViewAction.class);

    protected ClientRestrictionManager clientRestrictionManager;
    protected CaseManagementManager caseManagementManager;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return view(mapping, form, request, response);
    }

    @SuppressWarnings("unchecked")
    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ProgramManagerViewFormBean formBean = (ProgramManagerViewFormBean) form;

        // find the program id
        String programId = request.getParameter("id");

        if (programId == null) {
            programId = (String) request.getAttribute("id");
        }

        String demographicNo = request.getParameter("clientId");

        if (demographicNo != null) {
            request.setAttribute("clientId", demographicNo);
        }

        request.setAttribute("temporaryAdmission", programManager.getEnabled());

        // check role permission
        HttpSession se=request.getSession();
        String providerNo = (String)se.getAttribute("user");
        se.setAttribute("performAdmissions",new Boolean(caseManagementManager.hasAccessRight("perform admissions","access",providerNo,"",programId)));
		        
        // need the queue to determine which tab to go to first
        List<ProgramQueue> queue = programQueueManager.getActiveProgramQueuesByProgramId(Long.valueOf(programId));
        request.setAttribute("queue", queue);

        HashSet<Long> genderConflict = new HashSet<Long>();
        HashSet<Long> ageConflict = new HashSet<Long>();
        for (ProgramQueue programQueue : queue) {
            Demographic demographic=clientManager.getClientByDemographicNo(String.valueOf(programQueue.getClientId()));
            Program program=programManager.getProgram(programQueue.getProgramId());
            
            if (program.getManOrWoman()!=null && demographic.getSex()!=null)
            {
                if ("Man".equals(program.getManOrWoman()) && !"M".equals(demographic.getSex()))
                {
                    genderConflict.add(programQueue.getClientId());
                }
                if ("Woman".equals(program.getManOrWoman()) && !"F".equals(demographic.getSex()))
                {
                    genderConflict.add(programQueue.getClientId());
                }
                if ("Transgendered".equals(program.getManOrWoman()) && !"T".equals(demographic.getSex()))
                {
                    genderConflict.add(programQueue.getClientId());
                }
            }
            
            if (demographic.getAge()!=null)
            {
                int age=Integer.parseInt(demographic.getAge());
                if (age<program.getAgeMin() || age>program.getAgeMax()) ageConflict.add(programQueue.getClientId());
            }
        }
        request.setAttribute("genderConflict", genderConflict);
        request.setAttribute("ageConflict", ageConflict);

        if (formBean.getTab() == null || formBean.getTab().equals("")) {
            if (queue.size() > 0) {
                formBean.setTab("Queue");
            }
            else {
                formBean.setTab("General");
            }
        }

        Program program = programManager.getProgram(programId);
        request.setAttribute("program", program);

       
        if (formBean.getTab().equals("General")) {
            request.setAttribute("agency", programManager.getAgencyByProgram(programId));
        }

        if (formBean.getTab().equals("Service Restrictions")) {
            request.setAttribute("service_restrictions", clientRestrictionManager.getActiveRestrictionsForProgram(Integer.valueOf(programId), new Date()));
        }
        if (formBean.getTab().equals("Staff")) {
            request.setAttribute("providers", programManager.getProgramProviders(programId));
        }

        if (formBean.getTab().equals("Function User")) {
            request.setAttribute("functional_users", programManager.getFunctionalUsers(programId));
        }

        if (formBean.getTab().equals("Teams")) {
            List<ProgramTeam> teams = programManager.getProgramTeams(programId);

            for (ProgramTeam team : teams) {
                team.setProviders(programManager.getAllProvidersInTeam(Integer.valueOf(programId), team.getId()));
                team.setAdmissions(programManager.getAllClientsInTeam(Integer.valueOf(programId), team.getId()));
            }

            request.setAttribute("teams", teams);
        }

        if (formBean.getTab().equals("Clients")) {
            request.setAttribute("client_statuses", programManager.getProgramClientStatuses(new Integer(programId)));

            // request.setAttribute("admissions", admissionManager.getCurrentAdmissionsByProgramId(programId));
            // clients should be active
            List<Admission> admissions = new ArrayList<Admission>();
            List ads = admissionManager.getCurrentAdmissionsByProgramId(programId);
            for (Object ad1 : ads) {
                Admission admission = (Admission) ad1;
                Integer clientId = admission.getClientId();
                if (clientId > 0) {
                    Demographic client = clientManager.getClientByDemographicNo(Integer.toString(clientId));
                    if (client != null) {
                        String clientStatus = client.getPatientStatus();
                        if (clientStatus != null && clientStatus.equals("AC")) admissions.add(admission);
                    }
                }
            }
            request.setAttribute("admissions", admissions);

            request.setAttribute("program_name", program.getName());

            List<ProgramTeam> teams = programManager.getProgramTeams(programId);

            for (ProgramTeam team : teams) {
                team.setProviders(programManager.getAllProvidersInTeam(Integer.valueOf(programId), team.getId()));
                team.setAdmissions(programManager.getAllClientsInTeam(Integer.valueOf(programId), team.getId()));
            }

            request.setAttribute("teams", teams);

            List<Program> batchAdmissionPrograms = new ArrayList<Program>();

            for (Program bedProgram : programManager.getBedPrograms()) {
                if (bedProgram.isAllowBatchAdmission() && bedProgram.getProgramStatus().equals("active")) {
                    batchAdmissionPrograms.add(bedProgram);
                }
            }

            List<Program> batchAdmissionServicePrograms = new ArrayList<Program>();
            List servicePrograms;
            servicePrograms = programManager.getServicePrograms();
            for (Object serviceProgram1 : servicePrograms) {
                Program sp = (Program) serviceProgram1;
                if (sp.isAllowBatchAdmission() && sp.getProgramStatus().equals("active")) {
                    batchAdmissionServicePrograms.add(sp);
                }
            }

            // request.setAttribute("programs", batchAdmissionPrograms);
            request.setAttribute("bedPrograms", batchAdmissionPrograms);
            request.setAttribute("communityPrograms", programManager.getCommunityPrograms());
            request.setAttribute("allowBatchDischarge", program.isAllowBatchDischarge());
            request.setAttribute("servicePrograms", batchAdmissionServicePrograms);
        }

        if (formBean.getTab().equals("Access")) {
            request.setAttribute("accesses", programManager.getProgramAccesses(programId));
        }

        if (formBean.getTab().equals("Bed Check")) {
            formBean.setReservedBeds(bedManager.getBedsByProgram(Integer.valueOf(programId), true));
            request.setAttribute("bedDemographicStatuses", bedDemographicManager.getBedDemographicStatuses());
            request.setAttribute("communityPrograms", programManager.getCommunityPrograms());

            request.setAttribute("expiredReservations", bedDemographicManager.getExpiredReservations());
        }

        if (formBean.getTab().equals("Client Status")) {
            request.setAttribute("client_statuses", programManager.getProgramClientStatuses(new Integer(programId)));
        }

        logManager.log("view", "program", programId, request);

        request.setAttribute("id", programId);

        return mapping.findForward("view");
    }

    public ActionForward viewBedReservationChangeReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Integer reservedBedId = Integer.valueOf(request.getParameter("reservedBedId"));
        System.err.println(reservedBedId);

        // BedDemographicChange[] bedDemographicChanges = bedDemographicManager.getBedDemographicChanges(reservedBedId)
        request.setAttribute("bedReservationChanges", null);

        return mapping.findForward("viewBedReservationChangeReport");
    }

    public ActionForward viewBedCheckReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Integer programId = Integer.valueOf(request.getParameter("programId"));

        request.setAttribute("reservedBeds", bedManager.getBedsByProgram(programId, true));

        return mapping.findForward("viewBedCheckReport");
    }

    public ActionForward admit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String programId = request.getParameter("id");
        String clientId = request.getParameter("clientId");
        String queueId = request.getParameter("queueId");

        ProgramQueue queue = programQueueManager.getProgramQueue(queueId);
        Program fullProgram = programManager.getProgram(String.valueOf(programId));
        String dischargeNotes = request.getParameter("admission.dischargeNotes");
        String admissionNotes = request.getParameter("admission.admissionNotes");
        List<Long>  dependents = clientManager.getDependentsList(new Long(clientId));
        
        

        try {
            admissionManager.processAdmission(Integer.valueOf(clientId), getProviderNo(request), fullProgram, dischargeNotes, admissionNotes, queue
                    .isTemporaryAdmission(),dependents);
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.success"));
            saveMessages(request, messages);
        }
        catch (ProgramFullException e) {
            log.error(e);
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.full"));
            saveMessages(request, messages);
        }
        catch (AdmissionException e) {
            log.error(e);
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.error", e.getMessage()));
            saveMessages(request, messages);
        }
        catch (ServiceRestrictionException e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.service_restricted", e.getRestriction().getComments(), e.getRestriction()
                    .getProvider().getFormattedName()));
            saveMessages(request, messages);

            // store this for display
            ProgramManagerViewFormBean formBean = (ProgramManagerViewFormBean) form;

            formBean.setServiceRestriction(e.getRestriction());

            request.getSession().setAttribute("programId", programId);
            request.getSession().setAttribute("admission.dischargeNotes", dischargeNotes);
            request.getSession().setAttribute("admission.admissionNotes", admissionNotes);

            request.setAttribute("id", programId);

            request.setAttribute("hasOverridePermission", caseManagementManager.hasAccessRight("Service restriction override on admission", "access",
                    getProviderNo(request), clientId, programId));

            return mapping.findForward("service_restriction_error");
        }

        logManager.log("view", "admit to program", clientId, request);

        return view(mapping, form, request, response);
    }

    public ActionForward override_restriction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String programId = (String) request.getSession().getAttribute("programId");
        String clientId = request.getParameter("clientId");
        String queueId = request.getParameter("queueId");

        String dischargeNotes = (String) request.getSession().getAttribute("admission.dischargeNotes");
        String admissionNotes = (String) request.getSession().getAttribute("admission.admissionNotes");

        request.setAttribute("id", programId);

        if (isCancelled(request)
                || !caseManagementManager.hasAccessRight("Service restriction override on referral", "access", getProviderNo(request), clientId, programId)) {
            return view(mapping, form, request, response);
        }

        ProgramQueue queue = programQueueManager.getProgramQueue(queueId);
        Program fullProgram = programManager.getProgram(String.valueOf(programId));

        try {
            admissionManager.processAdmission(Integer.valueOf(clientId), getProviderNo(request), fullProgram, dischargeNotes, admissionNotes, queue
                    .isTemporaryAdmission(), true);
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.success"));
            saveMessages(request, messages);
        }
        catch (ProgramFullException e) {
            log.error(e);
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.full"));
            saveMessages(request, messages);
        }
        catch (AdmissionException e) {
            log.error(e);
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.error", e.getMessage()));
            saveMessages(request, messages);
        }
        catch (ServiceRestrictionException e) {
            throw new RuntimeException(e);
        }

        logManager.log("view", "override service restriction", clientId, request);

        logManager.log("view", "admit to program", clientId, request);

        return view(mapping, form, request, response);
    }

    public ActionForward assign_team_client(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String admissionId = request.getParameter("admissionId");
        String teamId = request.getParameter("teamId");
        String programName = request.getParameter("program_name");
        Admission ad = admissionManager.getAdmission(Long.valueOf(admissionId));

        ad.setTeamId(Integer.valueOf(teamId));

        admissionManager.saveAdmission(ad);

        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", programName));
        saveMessages(request, messages);

        logManager.log("write", "edit program - assign client to team", "", request);
        return view(mapping, form, request, response);
    }

    public ActionForward assign_status_client(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String admissionId = request.getParameter("admissionId");
        String statusId = request.getParameter("clientStatusId");
        String programName = request.getParameter("program_name");
        Admission ad = admissionManager.getAdmission(Long.valueOf(admissionId));

        ad.setClientStatusId(Integer.valueOf(statusId));

        admissionManager.saveAdmission(ad);

        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", programName));
        saveMessages(request, messages);

        logManager.log("write", "edit program - assign client to status", "", request);
        return view(mapping, form, request, response);
    }

    public ActionForward batch_discharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.info("do batch discharge");
        String type = request.getParameter("type");
        String admitToProgramId;
        if (type != null && type.equals("community")) {
            admitToProgramId = request.getParameter("batch_discharge_community_program");
        }
        else if (type != null && type.equals("bed")) {
            admitToProgramId = request.getParameter("batch_discharge_program");
        }
        else {
            admitToProgramId = "";
        }

        String message = "";

        // get clients
        Enumeration e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            if (name.startsWith("checked_") && request.getParameter(name).equals("on")) {
                String admissionId = name.substring(8);
                Admission admission = admissionManager.getAdmission(Long.valueOf(admissionId));
                if (admission == null) {
                    log.warn("admission #" + admissionId + " not found.");
                    continue;
                }

                // temporary admission will not allow batach discharge from bed program.
                if (admission.isTemporaryAdmission() && "bed".equals(type)) {
                    message += admission.getClient().getFormattedName()
                            + " is in this bed program temporarily. You cannot do batch discharge for this client!   \n";
                    continue;
                }

                // in case some clients maybe is already in the community program
                if (type != null) {
                    if (type.equals("community")) {
                        Integer clientId = admission.getClientId();
                        String program_type = admission.getProgramType();
                        // if discharged program is service program,
                        // then should check if the client is in one bed program
                        /*
                         * if(program_type.equals("Service")) { Admission admission_bed_program = admissionManager.getCurrentBedProgramAdmission(clientId);
                         * if(admission_bed_program!=null){ if(!admission_bed_program.isTemporaryAdmission()){ message +=
                         * admission.getClient().getFormattedName() + " is also in the bed program. You cannot do batch discharge for this client! \n";
                         * continue; } } }
                         */
                        // if the client is already in the community program, then cannot do batch discharge to the community program.
                        Admission admission_community_program = admissionManager.getCurrentCommunityProgramAdmission(clientId);
                        if (admission_community_program != null) {
                            message += admission.getClient().getFormattedName()
                                    + " is already in one community program. You cannot do batch discharge for this client! \n";
                            continue;
                        }
                    }
                }
                // lets see if there's room first
                if (!"service".equals(type)) {
                    Program programToAdmit = programManager.getProgram(admitToProgramId);
                    if (programToAdmit == null) {
                        message += "Admitting program not found!";
                        continue;
                    }
                    if (programToAdmit.getNumOfMembers() >= programToAdmit.getMaxAllowed()) {
                        message += "Program Full. Cannot admit " + admission.getClient().getFormattedName() + "\n";
                        continue;
                    }
                }
                admission.setDischargeDate(new Date());
                admission.setDischargeNotes("Batch discharge");
                admission.setAdmissionStatus(Admission.STATUS_DISCHARGED);
                admissionManager.saveAdmission(admission);

                // The service program can only be batch discharged, can not be admitted to another program.
                if (!"service".equals(type)) {
                    Admission newAdmission = new Admission();
                    newAdmission.setAdmissionDate(new Date());
                    newAdmission.setAdmissionNotes("Batch Admit");
                    newAdmission.setAdmissionStatus(Admission.STATUS_CURRENT);
                    newAdmission.setClientId(admission.getClientId());
                    newAdmission.setProgramId(Integer.valueOf(admitToProgramId));
                    newAdmission.setProviderNo(Long.valueOf(getProviderNo(request)));
                    newAdmission.setTeamId(0);
                    newAdmission.setAgencyId(admission.getAgencyId());

                    admissionManager.saveAdmission(newAdmission);
                }
            }
        }

        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", message));
        saveMessages(request, messages);

        return view(mapping, form, request, response);
    }

    public ActionForward reject_from_queue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String notes = request.getParameter("admission.admissionNotes");
        String programId = request.getParameter("id");
        String clientId = request.getParameter("clientId");
        String rejectionReason = request.getParameter("radioRejectionReason");

        log.debug("rejecting from queue: program_id=" + programId + ",clientId=" + clientId);

        programQueueManager.rejectQueue(programId, clientId, notes, rejectionReason);

        return view(mapping, form, request, response);
    }

    public ActionForward select_client_for_admit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String programId = request.getParameter("id");
        String clientId = request.getParameter("clientId");
        String queueId = request.getParameter("queueId");

        Program program = programManager.getProgram(String.valueOf(programId));
        ProgramQueue queue = programQueueManager.getProgramQueue(queueId);

        int numMembers = program.getNumOfMembers().intValue();
        int maxMem     = program.getMaxAllowed().intValue();
        int familySize = clientManager.getDependents(new Long(clientId)).size();
        //TODO: add warning if this admission ( w/ dependents) will exceed the maxMem 
        
        /*
         * If the user is currently enrolled in a bed program, we must warn the provider that this will also be a discharge
         */
        if (program.getType().equalsIgnoreCase("bed") && queue != null && !queue.isTemporaryAdmission()) {
            Admission currentAdmission = admissionManager.getCurrentBedProgramAdmission(Integer.valueOf(clientId));
            if (currentAdmission != null) {
                log.warn("client already in a bed program..doing a discharge/admit if proceeding");
                request.setAttribute("current_admission", currentAdmission);
                request.setAttribute("current_program", programManager.getProgram(String.valueOf(currentAdmission.getProgramId())));
            }
        }
        request.setAttribute("do_admit", Boolean.TRUE);

        return view(mapping, form, request, response);
    }

    public ActionForward select_client_for_reject(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("do_reject", Boolean.TRUE);

        return view(mapping, form, request, response);
    }

    public ActionForward saveReservedBeds(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ProgramManagerViewFormBean programManagerViewFormBean = (ProgramManagerViewFormBean) form;

        Bed[] reservedBeds = programManagerViewFormBean.getReservedBeds();

        for (int i = 0; i < reservedBeds.length; i++) {
            Bed reservedBed = reservedBeds[i];

            // detect check box false
            if (request.getParameter("reservedBeds[" + i + "].latePass") == null) {
                reservedBed.setLatePass(false);
            }

            // save bed
            try {
                bedManager.saveBed(reservedBed);

                BedDemographic bedDemographic = reservedBed.getBedDemographic();

                if (bedDemographic != null) {
                    // save bed demographic
                    bedDemographicManager.saveBedDemographic(bedDemographic);

                    Integer communityProgramId = reservedBed.getCommunityProgramId();

                    if (communityProgramId > 0) {
                        try {
                            // discharge to community program
                            admissionManager.processDischargeToCommunity(communityProgramId, bedDemographic.getId().getDemographicNo(), getProviderNo(request),
                                    "bed reservation ended - manually discharged", "0");
                        }
                        catch (AdmissionException e) {
                            ActionMessages messages = new ActionMessages();
                            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("discharge.failure", e.getMessage()));
                            saveMessages(request, messages);
                        }
                    }
                }
            }
            catch (BedReservedException e) {
                ActionMessages messages = new ActionMessages();
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.reserved.error", e.getMessage()));
                saveMessages(request, messages);
            }
        }

        return view(mapping, form, request, response);
    }

    @Required
    public void setClientRestrictionManager(ClientRestrictionManager clientRestrictionManager) {
        this.clientRestrictionManager = clientRestrictionManager;
    }

    @Required
    public void setCaseManagementManager(CaseManagementManager caseManagementManager) {
        this.caseManagementManager = caseManagementManager;
    }
}