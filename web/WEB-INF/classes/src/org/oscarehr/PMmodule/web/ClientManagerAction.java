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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.caisi.integrator.model.Client;
import org.caisi.integrator.model.transfer.ProgramTransfer;
import org.oscarehr.PMmodule.dao.ClientReferralDAO;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.AlreadyAdmittedException;
import org.oscarehr.PMmodule.exception.AlreadyQueuedException;
import org.oscarehr.PMmodule.exception.ClientAlreadyRestrictedException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.exception.ServiceRestrictionException;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Consent;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.DemographicExt;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.JointAdmission;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramClientRestriction;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.PMmodule.model.RoomDemographic;
import org.oscarehr.PMmodule.model.Demographic.ConsentGiven;
import org.oscarehr.PMmodule.service.ClientRestrictionManager;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.PMmodule.web.formbean.ClientManagerFormBean;
import org.oscarehr.PMmodule.web.formbean.ErConsentFormBean;
import org.oscarehr.PMmodule.web.utils.UserRoleUtils;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.survey.model.oscar.OscarFormInstance;
import org.springframework.beans.factory.annotation.Required;

import oscar.oscarDemographic.data.DemographicRelationship;

public class ClientManagerAction extends BaseAction {

    private static Log log = LogFactory.getLog(ClientManagerAction.class);

    protected ClientRestrictionManager clientRestrictionManager;
    protected CaseManagementManager caseManagementManager;
    private ClientReferralDAO clientReferralDAO;

    public void setClientReferralDAO(ClientReferralDAO clientReferralDAO) {
        this.clientReferralDAO = clientReferralDAO;
    }

    // Parameter
    public static final String ID = "id";

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm clientForm = (DynaActionForm) form;
        clientForm.set("view", new ClientManagerFormBean());

        return edit(mapping, form, request, response);
    }

    public ActionForward admit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm clientForm = (DynaActionForm) form;

        Admission admission = (Admission) clientForm.get("admission");
        Program program = (Program) clientForm.get("program");
        String demographicNo = request.getParameter("id");

        Program fullProgram = programManager.getProgram(String.valueOf(program.getId()));

        try {
            admissionManager.processAdmission(Integer.valueOf(demographicNo), getProviderNo(request), fullProgram, admission.getDischargeNotes(), admission.getAdmissionNotes(), admission.isTemporaryAdmission());
        }
        catch (ProgramFullException e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.error", "Program is full."));
            saveMessages(request, messages);
        }
        catch (AdmissionException e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.error", e.getMessage()));
            saveMessages(request, messages);
        }
        catch (ServiceRestrictionException e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.service_restricted", e.getRestriction().getComments(), e.getRestriction().getProvider().getFormattedName()));
            saveMessages(request, messages);
        }

        logManager.log("write", "admit", demographicNo, request);

        setEditAttributes(form, request, demographicNo);
        return mapping.findForward("edit");
    }

    public ActionForward admit_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm clientForm = (DynaActionForm) form;

        Program program = (Program) clientForm.get("program");
        String demographicNo = request.getParameter("id");
        setEditAttributes(form, request, demographicNo);

        program = programManager.getProgram(program.getId());

        /*
         * If the user is currently enrolled in a bed program, we must warn the provider that this will also be a discharge
         */
        if (program.getType().equalsIgnoreCase("bed")) {
            Admission currentAdmission = admissionManager.getCurrentBedProgramAdmission(Integer.valueOf(demographicNo));
            if (currentAdmission != null) {
                request.setAttribute("current_admission", currentAdmission);
                request.setAttribute("current_program", programManager.getProgram(currentAdmission.getProgramId()));
            }
        }
        request.setAttribute("do_admit", new Boolean(true));

        return mapping.findForward("edit");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm clientForm = (DynaActionForm) form;
        Admission admission = (Admission) clientForm.get("admission");
        admission.setDischargeNotes("");
        admission.setRadioDischargeReason("");

        clientForm.set("view", new ClientManagerFormBean());
        return edit(mapping, form, request, response);
    }

    public ActionForward discharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm clientForm = (DynaActionForm) form;

        Admission admission = (Admission) clientForm.get("admission");
        Program p = (Program) clientForm.get("program");
        String id = request.getParameter("id");
        List<Long> dependents = clientManager.getDependentsList(new Long(id));

        boolean success = true;

        try {
            admissionManager.processDischarge(p.getId(), new Integer(id), admission.getDischargeNotes(), admission.getRadioDischargeReason(), dependents);
        }
        catch (AdmissionException e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("discharge.failure", e.getMessage()));
            saveMessages(request, messages);
            success = false;
        }

        if (success) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("discharge.success"));
            saveMessages(request, messages);
            logManager.log("write", "discharge", id, request);
        }

        setEditAttributes(form, request, id);
        admission.setDischargeNotes("");
        admission.setRadioDischargeReason("");
        return mapping.findForward("edit");
    }

    public ActionForward discharge_community(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm clientForm = (DynaActionForm) form;

        Admission admission = (Admission) clientForm.get("admission");
        Program program = (Program) clientForm.get("program");
        String clientId = request.getParameter("id");
        List<Long> dependents = clientManager.getDependentsList(new Long(clientId));

        ActionMessages messages = new ActionMessages();

        try {
            admissionManager.processDischargeToCommunity(program.getId(), new Integer(clientId), getProviderNo(request), admission.getDischargeNotes(), admission.getRadioDischargeReason(), dependents);
            logManager.log("write", "discharge", clientId, request);

            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("discharge.success"));
            saveMessages(request, messages);
        }
        catch (AdmissionException e) {
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("discharge.failure", e.getMessage()));
            saveMessages(request, messages);
        }

        setEditAttributes(form, request, clientId);
        admission.setDischargeNotes("");
        admission.setRadioDischargeReason("");

        return mapping.findForward("edit");
    }

    public ActionForward discharge_community_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");

        setEditAttributes(form, request, id);

        request.setAttribute("do_discharge", new Boolean(true));
        request.setAttribute("community_discharge", new Boolean(true));
        return mapping.findForward("edit");
    }

    public ActionForward nested_discharge_community_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("nestedReason", "true");
        return discharge_community_select_program(mapping, form, request, response);
    }

    public ActionForward discharge_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        DynaActionForm clientForm = (DynaActionForm) form;
        Program program = (Program) clientForm.get("program");
        request.setAttribute("programId", String.valueOf(program.getId()));
        setEditAttributes(form, request, id);

        request.setAttribute("do_discharge", new Boolean(true));

        return mapping.findForward("edit");
    }

    public ActionForward nested_discharge_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("nestedReason", "true");
        setEditAttributes(form, request, request.getParameter("id"));
        request.setAttribute("do_discharge", new Boolean(true));
        return mapping.findForward("edit");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");

        if (id == null || id.equals("")) {
            Object o = request.getAttribute("demographicNo");

            if (o instanceof String) {
                id = (String) o;
            }

            if (o instanceof Long) {
                id = String.valueOf((Long) o);
            }
        }

        setEditAttributes(form, request, id);

        logManager.log("read", "pmm client record", id, request);

        String roles = (String) request.getSession().getAttribute("userrole");

        // for Vaccine Provider
        if (roles.indexOf("Vaccine Provider") != -1) {
            return new ActionForward("/VaccineProviderReport.do?id=" + id, true);
        }

        // for ERModule
        if (roles.indexOf(UserRoleUtils.Roles.er_clerk.name()) != -1) {
            Map<?, ?> consentMap = (Map<?, ?>) request.getSession().getAttribute("er_consent_map");

            if (consentMap == null) {
                return mapping.findForward("consent");
            }

            if (consentMap.get(id) == null) {
                return mapping.findForward("consent");
            }

            request.getSession().setAttribute("er_consent_map", consentMap);
            return mapping.findForward("er-redirect");
        }

        Demographic demographic = clientManager.getClientByDemographicNo(id);
        request.getSession().setAttribute("clientGender", demographic.getSex());
        request.getSession().setAttribute("clientAge", demographic.getAge());

        return mapping.findForward("edit");
    }

    public ActionForward getLinks(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");

        if (id != null && integratorManager.isEnabled()) {
            Client client = integratorManager.getClient(integratorManager.getLocalAgency().getIntegratorUsername(), Long.valueOf(id));
            request.setAttribute("client", client);
        }
        return mapping.findForward("links");
    }

    public ActionForward refer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm clientForm = (DynaActionForm) form;
        ClientReferral referral = (ClientReferral) clientForm.get("referral");
        Program p = (Program) clientForm.get("program");

        long clientId = Long.parseLong(request.getParameter("id"));
        long agencyId = p.getAgencyId();
        int programId = p.getId();
        long providerId = Long.valueOf(getProviderNo(request));

        referral.setAgencyId(agencyId);
        referral.setSourceAgencyId(-1l);
        referral.setClientId(clientId);
        referral.setProgramId((long) programId);
        referral.setProviderNo(providerId);
        referral.setReferralDate(new Date());
        referral.setProgramType(p.getType());

        if (agencyId == 0) {
            referToLocalAgencyProgram(request, clientForm, referral, p);
        }
        else {
            referToRemoteAgencyProgram(request, referral);
        }

        setEditAttributes(form, request, String.valueOf(clientId));
        clientForm.set("program", new Program());
        clientForm.set("referral", new ClientReferral());

        return mapping.findForward("edit");
    }

    private void referToRemoteAgencyProgram(HttpServletRequest request, ClientReferral referral) {

        referral.setStatus(ClientReferral.STATUS_UNKNOWN);

        boolean referred = integratorManager.makeReferral(referral);

        ActionMessages messages = new ActionMessages();
        if (referred) {
            clientReferralDAO.saveClientReferral(referral);
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.success"));
        }
        else {
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.failed"));
        }
        saveMessages(request, messages);
    }

    private void referToLocalAgencyProgram(HttpServletRequest request, DynaActionForm clientForm, ClientReferral referral, Program p) {
        Program program = programManager.getProgram(p.getId());

        referral.setStatus(ClientReferral.STATUS_ACTIVE);

        boolean success = true;
        try {
            clientManager.processReferral(referral);
        }
        catch (AlreadyAdmittedException e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.already_admitted"));
            saveMessages(request, messages);
            success = false;
        }
        catch (AlreadyQueuedException e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.already_referred"));
            saveMessages(request, messages);
            success = false;
        }
        catch (ServiceRestrictionException e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.service_restricted", e.getRestriction().getComments(), e.getRestriction().getProvider().getFormattedName()));
            saveMessages(request, messages);

            // store this for display
            clientForm.set("serviceRestriction", e.getRestriction());

            // going to need this in case of override
            clientForm.set("referral", referral);

            // store permission
            request.setAttribute("hasOverridePermission", caseManagementManager.hasAccessRight("Service restriction override on referral", "access", getProviderNo(request), String.valueOf(referral.getClientId()), "" + program.getId()));

            // jump to service restriction error page to allow overrides, etc.
            // return mapping.findForward("service_restriction_error");
        }

        if (success) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.success"));
            saveMessages(request, messages);
        }

        logManager.log("write", "referral", String.valueOf(referral.getClientId()), request);
    }

    public ActionForward refer_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm clientForm = (DynaActionForm) form;
        Program p = (Program) clientForm.get("program");
        String id = request.getParameter("id");
        setEditAttributes(form, request, id);

        long agencyId = p.getAgencyId();
        long programId = p.getId();
        if (agencyId == 0) {
            Program program = programManager.getProgram(programId);
            p.setName(program.getName());
            request.setAttribute("program", program);
        }
        else {
            ProgramTransfer programTransfer = integratorManager.getProgramByAgencyAndId(agencyId, programId);
            p.setName(programTransfer.getName());
            p.mergeFromProgramTransfer(programTransfer);
            request.setAttribute("program", p);
        }

        request.setAttribute("do_refer", true);
        request.setAttribute("temporaryAdmission", programManager.getEnabled());

        return mapping.findForward("edit");
    }

    public ActionForward service_restrict(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm clientForm = (DynaActionForm) form;
        ProgramClientRestriction restriction = (ProgramClientRestriction) clientForm.get("serviceRestriction");
        Integer days = (Integer) clientForm.get("serviceRestrictionLength");

        Program p = (Program) clientForm.get("program");
        String id = request.getParameter("id");

        restriction.setProgramId(p.getId());
        restriction.setDemographicNo(Integer.valueOf(id));
        restriction.setStartDate(new Date());
        restriction.setProviderNo(getProviderNo(request));
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + days);
        restriction.setEndDate(cal.getTime());
        restriction.setEnabled(true);

        boolean success;
        try {
            clientRestrictionManager.saveClientRestriction(restriction);
            success = true;
        }
        catch (ClientAlreadyRestrictedException e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("restrict.already_restricted"));
            saveMessages(request, messages);
            success = false;
        }

        if (success) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("restrict.success"));
            saveMessages(request, messages);
        }
        clientForm.set("program", new Program());
        clientForm.set("serviceRestriction", new ProgramClientRestriction());
        clientForm.set("serviceRestrictionLength", null);

        request.setAttribute("serviceRestrictions", clientRestrictionManager.getActiveRestrictionsForClient(Integer.valueOf(id), new Date()));

        setEditAttributes(form, request, id);
        logManager.log("write", "service_restriction", id, request);

        return mapping.findForward("edit");
    }

    public ActionForward restrict_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm clientForm = (DynaActionForm) form;
        Program p = (Program) clientForm.get("program");
        String id = request.getParameter("id");
        setEditAttributes(form, request, id);

        Program program = programManager.getProgram(p.getId());
        p.setName(program.getName());

        request.setAttribute("do_restrict", true);
        request.setAttribute("can_restrict", caseManagementManager.hasAccessRight("Create service restriction", "access", getProviderNo(request), id, "" + p.getId()));
        request.setAttribute("program", program);

        return mapping.findForward("edit");
    }

    public ActionForward override_restriction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm clientForm = (DynaActionForm) form;
        ProgramClientRestriction restriction = (ProgramClientRestriction) clientForm.get("serviceRestriction");

        ClientReferral referral = (ClientReferral) clientForm.get("referral");

        if (isCancelled(request) || !caseManagementManager.hasAccessRight("Service restriction override on referral", "access", getProviderNo(request), "" + restriction.getDemographicNo(), "" + restriction.getProgramId())) {
            clientForm.set("referral", new ClientReferral());
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.cancelled"));
            saveMessages(request, messages);

            setEditAttributes(form, request, "" + referral.getClientId());

            return mapping.findForward("edit");
        }

        boolean success = true;
        try {
            clientManager.processReferral(referral, true);
        }
        catch (AlreadyAdmittedException e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.already_admitted"));
            saveMessages(request, messages);
            success = false;
        }
        catch (AlreadyQueuedException e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.already_referred"));
            saveMessages(request, messages);
            success = false;
        }
        catch (ServiceRestrictionException e) {
            throw new RuntimeException("service restriction encountered during override");
        }

        if (success) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.success"));
            saveMessages(request, messages);
        }

        clientForm.set("program", new Program());
        clientForm.set("referral", new ClientReferral());
        setEditAttributes(form, request, "" + referral.getClientId());
        logManager.log("write", "referral", "" + referral.getClientId(), request);

        return mapping.findForward("edit");
    }

    public ActionForward refreshBedDropDownForReservation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        DynaActionForm clientForm = (DynaActionForm) form;

        BedDemographic bedDemographic = (BedDemographic) clientForm.get("bedDemographic");
        Date today = DateTimeFormatUtils.getToday();
        String roomId = request.getParameter("roomId");
        request.setAttribute("roomId", roomId);

        request.setAttribute("isRefreshRoomDropDown", "Y");

        // retrieve an array of beds associated with this roomId
        Bed[] unreservedBeds = bedManager.getCurrentPlusUnreservedBedsByRoom(Integer.valueOf(roomId), bedDemographic.getId().getBedId(), false);

        request.setAttribute("unreservedBeds", unreservedBeds);

        return edit(mapping, form, request, response);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return edit(mapping, form, request, response);
    }

    public ActionForward saveBedReservation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		//When room has beds assigned to it --> should not let client select room only.
		//When room has no beds assigned to it --> allow clients to select room only.

    	ActionMessages messages = new ActionMessages();
        DynaActionForm clientForm = (DynaActionForm) form;
        BedDemographic bedDemographic = (BedDemographic) clientForm.get("bedDemographic");
        Date today = DateTimeFormatUtils.getToday();
        String roomId = request.getParameter("roomId");
        bedDemographic.setReservationStart(today);
        bedDemographic.setRoomId(Integer.valueOf(roomId));
        
        Integer bedId = bedDemographic.getBedId();
        Integer demographicNo = bedDemographic.getId().getDemographicNo();
        boolean isBedSelected = (bedDemographic.getBedId() != null && bedDemographic.getBedId().intValue() != 0);
	   
		boolean isFamilyHead = false; 
		boolean isFamilyDependent = false;
		int familySize = 0; 
		RoomDemographic roomDemographic = null;
		
		//if room has bed --> must be assigned as room/bed combo or 
		//if room has no bed --> must be assigned as room only.
		boolean isRoomAssignedWithBeds = roomManager.isRoomAssignedWithBeds(bedDemographic.getRoomId());
		
		if(isRoomAssignedWithBeds  &&  (bedId == null  ||  bedId.intValue() == 0) ){//if assignedBed==1 && no bed assigned --> display error
		    messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.reservation.room_bed"));
		    saveMessages(request, messages);
		    return edit(mapping, clientForm, request, response);
		}
		
		//get dependents to be saved, removed from  'room_demographic'  &  'bed_demographic'  tables.
		List<JointAdmission> dependentList = clientManager.getDependents(new Long(demographicNo));
		JointAdmission clientsJadm = clientManager.getJointAdmission(new Long(demographicNo));
		
		if(dependentList != null  &&  dependentList.size() > 0){
			//condition met then demographicNo must be familyHead
			familySize = dependentList.size() + 1;
			isFamilyHead = true;
		}
			
		if (clientsJadm != null  &&  clientsJadm.getHeadClientId() != null) {
			isFamilyDependent = true;
		}
		System.out.println("ClientManagerAction.saveBedReservation(): isFamilyHead = " + isFamilyHead); 
		System.out.println("ClientManagerAction.saveBedReservation(): isFamilyDependent = " + isFamilyDependent);  
		
		if(!isFamilyHead  &&  isFamilyDependent){//when client is dependent of a family -> do not attempt to assign.
			//Display message notifying that the client cannot be saved (assign or unassign)  a room or a bed
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.reservation.dependent_disallowed"));
			saveMessages(request, messages);
			return edit(mapping, clientForm, request, response);

		}else {//check whether client is familyHead or independent client
			//create roomDemographic from bedDemographic
			roomDemographic = getRoomDemographicManager().getRoomDemographicByDemographic(demographicNo);
			if(roomDemographic == null) {//demographicNo (familyHead or independent) has no record in 'room_demographic'
				roomDemographic = RoomDemographic.create(demographicNo, bedDemographic.getProviderNo());
			}
			roomDemographic.setRoomDemographicFromBedDemographic(bedDemographic);
			// detect check box false
			if (request.getParameter("bedDemographic.latePass") == null) {
				bedDemographic.setLatePass(false);
			}
			//when client is familyHead, all family + dependents must be assigned or unassigned together
			if(isFamilyHead  &&  !isFamilyDependent){
					// Conditions: 
					//(1)Check whether the familySize is less than or equal to the (roomCapacity - roomOccupancy) currently.
					//   (i.e.) [roomCapacity] - [roomOccupancy] - [familySize] >= 0
					//(1.1)roomCapacity is either max number of clients a room can accomodate or number of beds assigned to this room
				    //    with the beds taking precedence!
					//   >> if familyHead choose room with bed, then  roomCapacity = [total number of unreserved beds]
					//   >> if familyHead choose room only,     then  roomCapacity = Capacity set for that particular room
					//(1.2)roomOccupancy = [number of all reserved beds] - [number of family members if occupying beds] or
					//     roomOccupancy = [number of all assigned clients] - [number of family members if amongst assigned]
					//(2)If less than --> display message to notify that there is not enough space in room.
					//(3)If greater or equal :
					//(3.1)for room/bed --> Delete all records in 'bed_demographic' & 'room_demographic' if any --> 
					//                      then save room/bed for all family members one at a time.
					//(3.2)for room only --> Delete all records in 'room_demographic' if any --> then save room for all family
					//                       members one at a time.

				
					int roomCapacity = 0;
					int roomOccupancy = 0;
					Room room = getRoomManager().getRoom(bedDemographic.getRoomId());
					Integer[] dependentIds = new Integer[dependentList.size()];
					List<Integer> unreservedBedIdList = new ArrayList<Integer>();
					List<Integer> dependentsBedIdList = new ArrayList<Integer>();
					List<Integer> availableBedIdList = new ArrayList<Integer>();
					List<Integer> correctedAvailableBedIdList = new ArrayList<Integer>();
					int numberOfFamilyMembersAssignedRoomBed = 0;
					Bed[] bedReservedInRoom = null;
					Bed[] bedUnReservedInRoom = null;
					List rdsByRoom = null;
					
					if(familySize > 1){
						for(int i=0; i < dependentList.size(); i++ ){
							dependentIds[i] = new Integer(((JointAdmission)dependentList.get(i)).getClientId().intValue());
						}
					}
					
					
					if(bedDemographic.getRoomId().intValue() == 0){//unassigning whole family
						//unassign family head first 
						getRoomDemographicManager().saveRoomDemographic(roomDemographic);
						if(isBedSelected) {
							bedDemographicManager.saveBedDemographic(bedDemographic);
						}
						else {
							// if only select room without bed, delete previous selected bedId in 'bed_demographic' table
							getRoomDemographicManager().cleanUpBedTables(roomDemographic);
						}

						for(int i=0; dependentIds != null  &&  i < dependentIds.length; i++){
							roomDemographic.getId().setDemographicNo(dependentIds[i]);
							bedDemographic.getId().setDemographicNo(dependentIds[i]);
							getRoomDemographicManager().saveRoomDemographic(roomDemographic);
							if(isBedSelected) {
								bedDemographicManager.saveBedDemographic(bedDemographic);
							}
							else {
								// if only select room without bed, delete previous selected bedId in 'bed_demographic' table
								getRoomDemographicManager().cleanUpBedTables(roomDemographic);
							}
						}
						
					}else{
					
					
						if(bedId == null  ||  bedId.intValue()==0){//assign room only
							if(room != null){
								roomCapacity = room.getOccupancy().intValue();
							}
						}else{//roomCapacity = total number of beds assigned to room
							Bed[] bedAssignedToRoom = bedManager.getBedsByRoom(bedDemographic.getRoomId());
							if(bedAssignedToRoom != null  &&   bedAssignedToRoom.length > 0){
								roomCapacity = bedAssignedToRoom.length;
							}
						}
	
						//roomOccupancy = [number of all assigned clients] - [number of family members if amongst room assigned]  or
						//roomOccupancy = [number of all reserved beds] - [number of family members if occupying beds]	   
						//bedIdList     = [id of all unreserved beds] + [id beds previously occupied by family members]
						if(bedId == null  ||  bedId.intValue()==0){//assign room only
							int numberOfFamilyMembersAssignedRoom = 0;
							
							rdsByRoom = roomDemographicManager.getRoomDemographicByRoom(bedDemographic.getRoomId());
							
							if(rdsByRoom != null  &&  !rdsByRoom.isEmpty()){
								for(int i=0; i < rdsByRoom.size(); i++){
									int rdsClientId = ((RoomDemographic)(rdsByRoom.get(i))).getId().getDemographicNo().intValue();
									if(demographicNo.intValue() ==  rdsClientId){
										numberOfFamilyMembersAssignedRoom++;
									}
									for(int j=0; j < dependentIds.length; j++){
										
										if(dependentIds[j].intValue() == rdsClientId ){
											numberOfFamilyMembersAssignedRoom++;
										}
									}
								}
								roomOccupancy = rdsByRoom.size() - numberOfFamilyMembersAssignedRoom;
							}
						}else{//assign room/bed combination
							
							BedDemographic bd = null;
							
							//unreservedBedIdList = [id of all unreserved beds] + [id beds previously occupied by family members]
							bedUnReservedInRoom = bedManager.getReservedBedsByRoom(bedDemographic.getRoomId(), false);
							if(bedUnReservedInRoom != null  &&  bedUnReservedInRoom.length > 0){
								for(int i=0; i < bedUnReservedInRoom.length; i++){
									unreservedBedIdList.add( ((Bed)bedUnReservedInRoom[i]).getId() );
								}
							}
						
							bedReservedInRoom = bedManager.getReservedBedsByRoom(bedDemographic.getRoomId(), true);
							if(bedReservedInRoom != null  &&  bedReservedInRoom.length > 0){
								
								for(int i=0; i < bedReservedInRoom.length; i++){
									
									int bedReservedInRoomId = ((Bed)(bedReservedInRoom[i])).getId().intValue();
									bd = bedDemographicManager.getBedDemographicByBed(bedReservedInRoomId);
									int bdClientId = bd.getId().getDemographicNo().intValue();
									
									if(demographicNo.intValue() ==  bdClientId){
										dependentsBedIdList.add(bd.getId().getBedId());
										numberOfFamilyMembersAssignedRoomBed++;
									}else{
										for(int j=0; j < dependentIds.length; j++){
											if(dependentIds[j].intValue() == bdClientId ){
												dependentsBedIdList.add(bd.getId().getBedId());
												numberOfFamilyMembersAssignedRoomBed++;
											}
										}
									}
								}//end for loop
								roomOccupancy = bedReservedInRoom.length - numberOfFamilyMembersAssignedRoomBed;
							}
							
							if(!unreservedBedIdList.isEmpty()){
								availableBedIdList.addAll(unreservedBedIdList);
							}
							if(!dependentsBedIdList.isEmpty()){
								availableBedIdList.addAll(dependentsBedIdList);
							}
						}//end of assign room/bed combination
	
						//Check whether the familySize is less than or equal to the (roomCapacity - roomOccupancy) currently
						
						if( roomCapacity > 0  &&  roomOccupancy >= 0  &&  familySize > 0  &&  
						    (roomCapacity - roomOccupancy - familySize >= 0 ) ) {
							Integer clientId = null;
							
							//assigning for familyHead only
							getRoomDemographicManager().saveRoomDemographic(roomDemographic);
	
							if(isBedSelected) {
								BedDemographic bdHeadDelete = bedDemographicManager.getBedDemographicByDemographic(bedDemographic.getId().getDemographicNo());
								if(bdHeadDelete != null){
									bedDemographicManager.deleteBedDemographic(bdHeadDelete);
								}
								for(int i=0; i < availableBedIdList.size(); i++){
									if( ((Integer)bedDemographic.getId().getBedId()).intValue()  !=  ((Integer)availableBedIdList.get(i)).intValue() ){
										correctedAvailableBedIdList.add(availableBedIdList.get(i));
									}
								}
								bedDemographicManager.saveBedDemographic(bedDemographic);
							}
							else {
								// if only select room without bed, delete previous selected bedId in 'bed_demographic' table
								getRoomDemographicManager().cleanUpBedTables(roomDemographic);
							}
							//Assign for each dependent member of family
							for(int i=0; i < dependentList.size(); i++){
								//clienId is each dependent
								clientId = new Integer(((JointAdmission)dependentList.get(i)).getClientId().intValue());
								
								if( clientId != null ){
									roomDemographic = getRoomDemographicManager().getRoomDemographicByDemographic( clientId  );
									bedDemographic.getId().setDemographicNo(clientId); //change to dependent member
								
									//assigning both room & bed (different ones) for all dependents
									if( isBedSelected  &&  correctedAvailableBedIdList.size() >= dependentList.size()){
	
										BedDemographic bdDependent = bedDemographicManager.getBedDemographicByDemographic(bedDemographic.getId().getDemographicNo());
										bedDemographic.getId().setBedId(correctedAvailableBedIdList.get(i));
										
										if (roomDemographic == null) {
											roomDemographic = RoomDemographic.create( clientId, bedDemographic.getProviderNo());
										}
										roomDemographic.setRoomDemographicFromBedDemographic(bedDemographic);
										// detect check box false
										if (request.getParameter("bedDemographic.latePass") == null) {
											bedDemographic.setLatePass(false);
										}
	
										getRoomDemographicManager().saveRoomDemographic(roomDemographic);
										if(bdDependent != null){
											bedDemographicManager.deleteBedDemographic(bdDependent);
										}
										bedDemographicManager.saveBedDemographic(bedDemographic);
		
									}else if(!isBedSelected){//assigning room only for all dependents
										
										if (roomDemographic != null) {
											roomDemographic.setRoomDemographicFromBedDemographic(bedDemographic);
										}
										else {
											roomDemographic = RoomDemographic.create( clientId, bedDemographic.getProviderNo());
											roomDemographic.setRoomDemographicFromBedDemographic(bedDemographic);
										}
										// detect check box false
										if (request.getParameter("bedDemographic.latePass") == null) {
											bedDemographic.setLatePass(false);
										}
										getRoomDemographicManager().saveRoomDemographic(roomDemographic);
										
										// if only select room without bed, delete previous selected bedId in 'bed_demographic' table
										getRoomDemographicManager().cleanUpBedTables(roomDemographic);
									}
								
								}//end of if( clientId != null )
	
							}//end for loop
							
						}else{//if(roomCapacity - roomOccupancy - familySize < 0 )
							String occupancy = "0";
							String available = "0";
							//Display message notifying that the roomCapacity is deficient ...
							if( isBedSelected ){
								if(bedReservedInRoom != null){
									occupancy = String.valueOf(bedReservedInRoom.length);
								}
								if(availableBedIdList != null){
									available = String.valueOf(availableBedIdList.size());
								}
						        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.reservation.bedsCapacity_exceeded", occupancy, available ));
						        saveMessages(request, messages);
	
							}else{
								if(rdsByRoom != null){
									occupancy = String.valueOf(rdsByRoom.size());
								}
								if(roomCapacity > 0){
									available = "" + (roomCapacity - rdsByRoom.size());
								}
								
						        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.reservation.roomCapacity_exceeded", occupancy, available ));
						        saveMessages(request, messages);
							}
					        return edit(mapping, clientForm, request, response);
						}//end of if(roomCapacity - roomOccupancy - familySize < 0 )
						
					}//end of if(roomId != 0) -> (i.e.) assigning instead of unassigning

			}else{ //when client is independent -> just assign/unassign either room/bed or room only.

				getRoomDemographicManager().saveRoomDemographic(roomDemographic);

				if(isBedSelected) {
					bedDemographicManager.saveBedDemographic(bedDemographic);
				}
				else {
					// if only select room without bed, delete previous selected bedId in 'bed_demographic' table
					getRoomDemographicManager().cleanUpBedTables(roomDemographic);
				}
			}//end of isIndependentClient
			
		}//end of  isFamilyHead  ||  isIndependentClient

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.reservation.success"));
        saveMessages(request, messages);

        return edit(mapping, clientForm, request, response);
    }

    public ActionForward save_survey(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm clientForm = (DynaActionForm) form;
        OscarFormInstance formInstance = (OscarFormInstance) clientForm.get("form");

        ClientManagerFormBean formBean = (ClientManagerFormBean) clientForm.get("view");

        formInstance.setFormId(0);

        String clientId = (String) request.getAttribute("clientId");
        if (clientId == null) {
            clientId = request.getParameter("id");
        }

        formBean.setTab("Forms");

        setEditAttributes(form, request, clientId);
        return mapping.findForward("edit");
    }

    public ActionForward save_joint_admission(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JointAdmission jadmission = new JointAdmission();

        String headClientId = request.getParameter("headClientId");
        String clientId = request.getParameter("dependentClientId");
        String type = request.getParameter("type");
        Long headInteger = new Long(headClientId);
        Long clientInteger = new Long(clientId);

        System.out.println("headClientId " + headClientId + " clientId " + clientId);

        jadmission.setAdmissionDate(new Date());
        jadmission.setHeadClientId(headInteger);
        jadmission.setArchived(false);
        jadmission.setClientId(clientInteger);
        jadmission.setProviderNo((String) request.getSession().getAttribute("user"));
        jadmission.setTypeId(new Long(type));
        System.out.println(jadmission.toString());
        clientManager.saveJointAdmission(jadmission);
        setEditAttributes(form, request, (String) request.getParameter("clientId"));

        return mapping.findForward("edit");
    }

    public ActionForward remove_joint_admission(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String clientId = request.getParameter("dependentClientId");
        clientManager.removeJointAdmission(new Long(clientId), (String) request.getSession().getAttribute("user"));
        setEditAttributes(form, request, (String) request.getParameter("clientId"));
        return mapping.findForward("edit");
    }

    public ActionForward search_programs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm clientForm = (DynaActionForm) form;

        Program criteria = (Program) clientForm.get("program");
        request.setAttribute("programs", programManager.search(criteria));

        ProgramUtils.addProgramRestrictions(request);

        List<ProgramTransfer> remotePrograms = getRemoteProgramsFiltered(criteria);
        if (remotePrograms != null) request.setAttribute("remotePrograms", remotePrograms);

        return mapping.findForward("search_programs");
    }

    /**
     * @return null if integrator is disabled or upon error.
     */
    private List<ProgramTransfer> getRemoteProgramsFiltered(Program criteria) {
        ProgramTransfer[] remotePrograms = integratorManager.getOtherAgenciesPrograms();
        if (remotePrograms == null) return(null);

        ArrayList<ProgramTransfer> remoteProgramsFiltered = new ArrayList<ProgramTransfer>();

        for (ProgramTransfer programTransfer : remotePrograms) {
            if (matchCriteria(criteria, programTransfer)) {
                remoteProgramsFiltered.add(programTransfer);
            }
        }

        return(remoteProgramsFiltered);
    }

    /**
     * @param criteria
     * @param programTransfer
     * @return true if the program matches the criteria, false otherwise
     */
    private boolean matchCriteria(Program criteria, ProgramTransfer programTransfer) {

        String temp = StringUtils.trimToNull(criteria.getName());
        if (temp != null) if (!programTransfer.getName().equalsIgnoreCase(temp)) return(false);

        temp = StringUtils.trimToNull(criteria.getType());
        if (temp != null) if (!programTransfer.getType().equalsIgnoreCase(temp)) return(false);

        temp = StringUtils.trimToNull(criteria.getManOrWoman());
        if (temp != null) if (!programTransfer.getManOrWoman().equalsIgnoreCase(temp)) return(false);

        if (criteria.isTransgender()) if (!programTransfer.isTransgender()) return(false);

        if (criteria.isFirstNation()) if (!programTransfer.isFirstNation()) return(false);

        if (criteria.isBedProgramAffiliated()) if (!programTransfer.isBedProgramAffiliated()) return(false);

        if (criteria.isAlcohol()) if (!programTransfer.isAlcohol()) return(false);

        temp = StringUtils.trimToNull(criteria.getAbstinenceSupport());
        if (temp != null) if (!programTransfer.getAbstinenceSupport().equalsIgnoreCase(temp)) return(false);

        if (criteria.isPhysicalHealth()) if (!programTransfer.isPhysicalHealth()) return(false);

        if (criteria.isMentalHealth()) if (!programTransfer.isMentalHealth()) return(false);

        if (criteria.isHousing()) if (!programTransfer.isHousing()) return(false);

        return(true);
    }

    public ActionForward submit_erconsent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm clientForm = (DynaActionForm) form;
        ErConsentFormBean consentFormBean = (ErConsentFormBean) clientForm.get("erconsent");
        boolean success = true;

        String demographicNo = request.getParameter("id");

        // save consent to session
        Map<String, ErConsentFormBean> consentMap = (Map) request.getSession().getAttribute("er_consent_map");

        if (consentMap == null) {
            consentMap = new HashMap<String, ErConsentFormBean>();
        }
        consentMap.put(demographicNo, consentFormBean);

        request.getSession().setAttribute("er_consent_map", consentMap);

        List<?> programDomain = providerManager.getProgramDomain(getProviderNo(request));
        if (programDomain.size() > 0) {
            boolean doAdmit = true;
            boolean doRefer = true;
            ProgramProvider program = (ProgramProvider) programDomain.get(0);
            // refer/admin client to service program associated with this user
            ClientReferral referral = new ClientReferral();
            referral.setAgencyId((long) 0);
            referral.setClientId(new Long(demographicNo));
            referral.setNotes("ER Automated referral\nConsent Type: " + consentFormBean.getConsentType() + "\nReason: " + consentFormBean.getConsentReason());
            referral.setProgramId(program.getProgramId().longValue());
            referral.setProviderNo(Long.valueOf(getProviderNo(request)));
            referral.setReferralDate(new Date());
            referral.setSourceAgencyId((long) 0);
            referral.setStatus(ClientReferral.STATUS_ACTIVE);

            Admission currentAdmission = admissionManager.getCurrentAdmission(String.valueOf(program.getProgramId()), Integer.valueOf(demographicNo));
            if (currentAdmission != null) {
                referral.setStatus(ClientReferral.STATUS_REJECTED);
                referral.setCompletionNotes("Client currently admitted");
                referral.setCompletionDate(new Date());
                ActionMessages messages = new ActionMessages();
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.already_admitted"));
                saveMessages(request, messages);
                doAdmit = false;
            }
            ProgramQueue queue = programQueueManager.getActiveProgramQueue(String.valueOf(program.getId()), demographicNo);
            if (queue != null) {
                referral.setStatus(ClientReferral.STATUS_REJECTED);
                referral.setCompletionNotes("Client already in queue");
                referral.setCompletionDate(new Date());
                ActionMessages messages = new ActionMessages();
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.already_referred"));
                saveMessages(request, messages);
                doRefer = false;
            }
            if (doRefer) {
                clientManager.saveClientReferral(referral);
            }

            if (doAdmit) {
                String admissionNotes = "ER Automated admission\nConsent Type: " + consentFormBean.getConsentType() + "\nReason: " + consentFormBean.getConsentReason();
                try {
                    admissionManager.processAdmission(Integer.valueOf(demographicNo), getProviderNo(request), programManager.getProgram(String.valueOf(program.getProgramId())), null, admissionNotes);
                }
                catch (Exception e) {
                    ActionMessages messages = new ActionMessages();
                    messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.error", e.getMessage()));
                    saveMessages(request, messages);
                    success = false;
                }
            }
        }

        clientForm.set("erconsent", new ErConsentFormBean());
        request.setAttribute("id", demographicNo);
        if (success) {
            return mapping.findForward("er-redirect");
        }
        else {
            return mapping.findForward("search");
        }
    }

    public ActionForward survey(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm clientForm = (DynaActionForm) form;
        OscarFormInstance formInstance = (OscarFormInstance) clientForm.get("form");

        if (request.getAttribute("survey_saved") != null) {
            setEditAttributes(form, request, (String) request.getAttribute("clientId"));
            return mapping.findForward("edit");
        }

        String clientId = request.getParameter("id");
        String formId = String.valueOf(formInstance.getFormId());

        formInstance.setFormId(0);

        return new ActionForward("/PMmodule/Forms/SurveyExecute.do?method=survey&formId=" + formId + "&clientId=" + clientId);
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return edit(mapping, form, request, response);
    }

    public ActionForward view_referral(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String referralId = request.getParameter("referralId");
        ClientReferral referral = clientManager.getClientReferral(referralId);
        Agency agency = agencyManager.getAgency("" + referral.getAgencyId());
        Demographic client = clientManager.getClientByDemographicNo("" + referral.getClientId());

        Long providerNo = referral.getProviderNo();
        Provider provider = providerManager.getProvider("" + providerNo);
        DynaActionForm clientForm = (DynaActionForm) form;

        clientForm.set("referral", referral);
        clientForm.set("agency", agency);
        clientForm.set("client", client);

        clientForm.set("provider", provider);

        return mapping.findForward("view_referral");
    }

    public ActionForward view_admission(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String admissionId = request.getParameter("admissionId");
        Admission admission = admissionManager.getAdmission(Long.valueOf(admissionId));
        Agency agency = agencyManager.getAgency("" + admission.getAgencyId());
        Demographic client = clientManager.getClientByDemographicNo("" + admission.getClientId());
        Long providerNo = admission.getProviderNo();
        Provider provider = providerManager.getProvider("" + providerNo);

        DynaActionForm clientForm = (DynaActionForm) form;
        clientForm.set("admission", admission);
        clientForm.set("client", client);
        clientForm.set("agency", agency);
        clientForm.set("provider", provider);

        return mapping.findForward("view_admission");
    }

    public ActionForward update_sharing_opting(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String sharingOptinChecked = request.getParameter("state");
        if (sharingOptinChecked != null) {
            int id = Integer.parseInt(request.getParameter("id"));

            if ("true".equals(sharingOptinChecked)) {
                String value = Demographic.ConsentGiven.CIRCLE_OF_CARE.name();
                clientManager.saveDemographicExt(id, Demographic.CONSENT_GIVEN_KEY, value);
                clientManager.saveDemographicExt(id, Demographic.METHOD_OBTAINED_KEY, Demographic.MethodObtained.EXPLICIT.name());
                logManager.log("update", "DataSharingOpting:" + value, String.valueOf(id), request);
            }
            else if ("false".equals(sharingOptinChecked)) {
                String value = Demographic.ConsentGiven.NONE.name();
                clientManager.saveDemographicExt(id, Demographic.CONSENT_GIVEN_KEY, value);
                clientManager.saveDemographicExt(id, Demographic.METHOD_OBTAINED_KEY, Demographic.MethodObtained.EXPLICIT.name());
                logManager.log("update", "DataSharingOpting:" + value, String.valueOf(id), request);
            }
            else throw(new IllegalStateException("Unexpected state, sharingOptinCheckbox state = " + sharingOptinChecked));

        }

        return(unspecified(mapping, form, request, response));
    }

    private boolean isInDomain(long programId, List<?> programDomain) {
        for (int x = 0; x < programDomain.size(); x++) {
            ProgramProvider p = (ProgramProvider) programDomain.get(x);

            if (p.getProgramId().longValue() == programId) {
                return true;
            }
        }

        return false;
    }

    private void setEditAttributes(ActionForm form, HttpServletRequest request, String demographicNo) {
        DynaActionForm clientForm = (DynaActionForm) form;

        ClientManagerFormBean tabBean = (ClientManagerFormBean) clientForm.get("view");

        request.setAttribute("id", demographicNo);
        request.setAttribute("client", clientManager.getClientByDemographicNo(demographicNo));

        DemographicExt demographicExtConsent = clientManager.getDemographicExt(Integer.parseInt(demographicNo), Demographic.CONSENT_GIVEN_KEY);
        DemographicExt demographicExtConsentMethod = clientManager.getDemographicExt(Integer.parseInt(demographicNo), Demographic.METHOD_OBTAINED_KEY);

        ConsentGiven consentGiven = ConsentGiven.NONE;
        if (demographicExtConsent != null) consentGiven = ConsentGiven.valueOf(demographicExtConsent.getValue());

        Demographic.MethodObtained methodObtained = Demographic.MethodObtained.IMPLICIT;
        if (demographicExtConsentMethod != null) methodObtained = Demographic.MethodObtained.valueOf(demographicExtConsentMethod.getValue());

        request.setAttribute("consentStatus", consentGiven.name());
        request.setAttribute("consentMethod", methodObtained.name());
        boolean consentStatusChecked = Demographic.ConsentGiven.ALL == consentGiven || Demographic.ConsentGiven.CIRCLE_OF_CARE == consentGiven;
        request.setAttribute("consentCheckBoxState", consentStatusChecked ? "checked=\"checked\"" : "");

        String providerNo = getProviderNo(request);

        // program domain
        List<Program> programDomain = new ArrayList<Program>();

        for (Iterator<?> i = providerManager.getProgramDomain(providerNo).iterator(); i.hasNext();) {
            ProgramProvider programProvider = (ProgramProvider) i.next();
            programDomain.add(programManager.getProgram(programProvider.getProgramId()));
        }

        request.setAttribute("programDomain", programDomain);

        // check role permission
        HttpSession se = request.getSession();
        List admissions = admissionManager.getCurrentAdmissions(Integer.valueOf(demographicNo));
        for (Iterator it = admissions.iterator(); it.hasNext();) {
            Admission admission = (Admission) it.next();
            String inProgramId = String.valueOf(admission.getProgramId());
            String inProgramType = admission.getProgramType();
            if ("service".equalsIgnoreCase(inProgramType)) {
                se.setAttribute("performDischargeService", new Boolean(caseManagementManager.hasAccessRight("perform discharges", "access", providerNo, demographicNo, inProgramId)));
                se.setAttribute("performAdmissionService", new Boolean(caseManagementManager.hasAccessRight("perform admissions", "access", providerNo, demographicNo, inProgramId)));

            }
            else if ("bed".equalsIgnoreCase(inProgramType)) {
                se.setAttribute("performDischargeBed", new Boolean(caseManagementManager.hasAccessRight("perform discharges", "access", providerNo, demographicNo, inProgramId)));
                se.setAttribute("performAdmissionBed", new Boolean(caseManagementManager.hasAccessRight("perform admissions", "access", providerNo, demographicNo, inProgramId)));
                se.setAttribute("performBedAssignments", new Boolean(caseManagementManager.hasAccessRight("perform bed assignments", "access", providerNo, demographicNo, inProgramId)));

            }
        }

        // tab override - from survey module
        String tabOverride = (String) request.getAttribute("tab.override");

        if (tabOverride != null && tabOverride.length() > 0) {
            tabBean.setTab(tabOverride);
        }

        if (tabBean.getTab().equals("Summary")) {
            /* survey module */
            request.setAttribute("survey_list", surveyManager.getAllForms());
            request.setAttribute("surveys", surveyManager.getForms(demographicNo));

            // request.setAttribute("admissions", admissionManager.getCurrentAdmissions(Integer.valueOf(demographicNo)));
            // only allow bed/service programs show up.(not external program)
            List currentAdmissionList = admissionManager.getCurrentAdmissions(Integer.valueOf(demographicNo));
            List bedServiceList = new ArrayList();
            for (Iterator ad = currentAdmissionList.iterator(); ad.hasNext();) {
                Admission admission1 = (Admission) ad.next();
                if ("External".equalsIgnoreCase(programManager.getProgram(admission1.getProgramId()).getType())) {
                    continue;
                }
                bedServiceList.add(admission1);
            }
            request.setAttribute("admissions", bedServiceList);

            Intake mostRecentQuickIntake = genericIntakeManager.getMostRecentQuickIntake(Integer.valueOf(demographicNo));
            request.setAttribute("mostRecentQuickIntake", mostRecentQuickIntake);

            Consent consent = consentManager.getMostRecentConsent(Long.valueOf(demographicNo));
            request.setAttribute("consent", consent);

            if (consent == null) {
                DemographicExt remote_consent = clientManager.getDemographicExt(new Integer(demographicNo), "consent_st");

                if (remote_consent != null) {
                    request.setAttribute("remote_consent", remote_consent);
                    request.setAttribute("remote_consent_exclusions", clientManager.getDemographicExt(new Integer(demographicNo), "consent_ex"));

                    DemographicExt remoteConsentAgency = clientManager.getDemographicExt(new Integer(demographicNo), "consent_ag");
                    if (remoteConsentAgency != null) {
                        request.setAttribute("remote_consent_agency", remoteConsentAgency);
                        request.setAttribute("remote_consent_agency_name", Agency.getAgencyName(Long.parseLong(remoteConsentAgency.getValue())));
                    }

                    request.setAttribute("remote_consent_date", clientManager.getDemographicExt(new Integer(demographicNo), "consent_dt"));
                }
            }

            request.setAttribute("referrals", clientManager.getActiveReferrals(demographicNo));
        }

        /* history */
        if (tabBean.getTab().equals("History")) {
            request.setAttribute("admissionHistory", admissionManager.getAdmissions(Integer.valueOf(demographicNo)));
            request.setAttribute("referralHistory", clientManager.getReferrals(demographicNo));
        }

        List<?> currentAdmissions = admissionManager.getCurrentAdmissions(Integer.valueOf(demographicNo));

        for (int x = 0; x < currentAdmissions.size(); x++) {
            Admission admission = (Admission) currentAdmissions.get(x);

            if (isInDomain(admission.getProgramId().longValue(), providerManager.getProgramDomain(providerNo))) {
                request.setAttribute("isInProgramDomain", Boolean.TRUE);
                break;
            }
        }

        /* bed reservation view */
        BedDemographic bedDemographic = bedDemographicManager.getBedDemographicByDemographic(Integer.valueOf(demographicNo));
        request.setAttribute("bedDemographic", bedDemographic);
        
        RoomDemographic roomDemographic = getRoomDemographicManager().getRoomDemographicByDemographic(Integer.valueOf(demographicNo));

		if(roomDemographic != null){
			Integer roomIdInt = roomDemographic.getId().getRoomId();
			Room room = null;
			if(roomIdInt != null){
				room = getRoomManager().getRoom(roomIdInt);
			}
			if(room != null){
				roomDemographic.setRoom(room);
			}
		}
		request.setAttribute("roomDemographic", roomDemographic);
		
        if (tabBean.getTab().equals("Bed/Room Reservation")) {

            boolean isRefreshRoomDropDown = false;
            if (request.getAttribute("isRefreshRoomDropDown") != null && request.getAttribute("isRefreshRoomDropDown").equals("Y")) {
                isRefreshRoomDropDown = true;
            }
            else {
                isRefreshRoomDropDown = false;
            }

            String roomId = request.getParameter("roomId");
            if (roomDemographic != null && roomId == null) {
                roomId = roomDemographic.getId().getRoomId().toString();
            }

            // set bed program id
            Admission bedProgramAdmission = admissionManager.getCurrentBedProgramAdmission(Integer.valueOf(demographicNo));
            Integer bedProgramId = null;
            if(bedProgramAdmission != null){
            	bedProgramId = (bedProgramAdmission != null) ? bedProgramAdmission.getProgramId() : null;
            }
            request.setAttribute("bedProgramId", bedProgramId);

            Bed reservedBed = null;

            if (bedDemographic == null) {
                bedDemographic = BedDemographic.create(Integer.valueOf(demographicNo), bedDemographicManager.getDefaultBedDemographicStatus(), providerNo);

                if (roomDemographic != null) {
                    bedDemographic.setReservationStart(roomDemographic.getAssignStart());
                    bedDemographic.setReservationEnd(roomDemographic.getAssignEnd());
                }

                reservedBed = null;

            }
            else {

                reservedBed = bedManager.getBed(bedDemographic.getBedId());
            }

            if (isRefreshRoomDropDown) {
                bedDemographic.setRoomId(Integer.valueOf(roomId));
            }

            clientForm.set("bedDemographic", bedDemographic);

            Room[] availableRooms = getRoomManager().getAvailableRooms(null, bedProgramId, Boolean.TRUE, demographicNo);
            
            request.setAttribute("availableRooms", availableRooms);

            if ((isRefreshRoomDropDown && roomId != null) || (reservedBed == null && !"0".equals(roomId))) {
                request.setAttribute("roomId", roomId);
            }
            else if (reservedBed != null) {
                request.setAttribute("roomId", reservedBed.getRoomId().toString());
            }
            else {
                request.setAttribute("roomId", "0");
            }
            request.setAttribute( "isAssignedBed", String.valueOf(getRoomManager().isAssignedBed((String)request.getAttribute("roomId"), availableRooms ) ) ); 
            										
            // retrieve an array of beds associated with this roomId
            Bed[] unreservedBeds = null;

            if (isRefreshRoomDropDown && request.getAttribute("unreservedBeds") != null) {
                unreservedBeds = (Bed[]) request.getAttribute("unreservedBeds");

            }
            else if (reservedBed != null) {

                // unreservedBeds = bedManager.getBedsByRoomProgram(availableRooms, bedProgramId, false);
                unreservedBeds = bedManager.getCurrentPlusUnreservedBedsByRoom(reservedBed.getRoomId(), bedDemographic.getId().getBedId(), false);
            }

            clientForm.set("unreservedBeds", unreservedBeds);

            // set bed demographic statuses
            clientForm.set("bedDemographicStatuses", bedDemographicManager.getBedDemographicStatuses());
        }
        /* forms */
        if (tabBean.getTab().equals("Forms")) {
            request.setAttribute("quickIntakes", genericIntakeManager.getQuickIntakes(Integer.valueOf(demographicNo)));
            request.setAttribute("indepthIntakes", genericIntakeManager.getIndepthIntakes(Integer.valueOf(demographicNo)));
            request.setAttribute("programIntakes", genericIntakeManager.getProgramIntakes(Integer.valueOf(demographicNo)));
            request.setAttribute("programsWithIntake", genericIntakeManager.getProgramsWithIntake(Integer.valueOf(demographicNo)));

            /* survey module */
            request.setAttribute("survey_list", surveyManager.getAllForms());
            request.setAttribute("surveys", surveyManager.getForms(demographicNo));
        }

        /* refer */
        if (tabBean.getTab().equals("Refer")) {
            request.setAttribute("referrals", clientManager.getActiveReferrals(demographicNo));
        }

        /* service restrictions */
        if (tabBean.getTab().equals("Service Restrictions")) {
            request.setAttribute("serviceRestrictions", clientRestrictionManager.getActiveRestrictionsForClient(Integer.valueOf(demographicNo), new Date()));
        }

        /* discharge */
        if (tabBean.getTab().equals("Discharge")) {
            request.setAttribute("communityPrograms", programManager.getCommunityPrograms());
            request.setAttribute("serviceAdmissions", admissionManager.getCurrentServiceProgramAdmission(Integer.valueOf(demographicNo)));
            request.setAttribute("temporaryAdmissions", admissionManager.getCurrentTemporaryProgramAdmission(Integer.valueOf(demographicNo)));
            request.setAttribute("current_bed_program", admissionManager.getCurrentBedProgramAdmission(Integer.valueOf(demographicNo)));
            request.setAttribute("current_community_program", admissionManager.getCurrentCommunityProgramAdmission(Integer.valueOf(demographicNo)));
        }

        /* Relations */
        DemographicRelationship demoRelation = new DemographicRelationship();
        ArrayList<Hashtable> relList = demoRelation.getDemographicRelationshipsWithNamePhone(demographicNo);
        List<JointAdmission> list = clientManager.getDependents(new Long(demographicNo));
        JointAdmission clientsJadm = clientManager.getJointAdmission(new Long(demographicNo));
        int familySize = list.size() + 1;
        if (familySize > 1) {
            request.setAttribute("groupHead", "yes");
        }
        if (clientsJadm != null) {
            request.setAttribute("dependentOn", clientsJadm.getHeadClientId());
            List depList = clientManager.getDependents(clientsJadm.getHeadClientId());
            familySize = depList.size() + 1;
            Demographic headClientDemo = clientManager.getClientByDemographicNo("" + clientsJadm.getHeadClientId());
            request.setAttribute("groupName", headClientDemo.getFormattedName() + " Group");
        }

        if (relList != null && relList.size() > 0) {
            for (Hashtable h : relList) {
                String demographic = (String) h.get("demographicNo");
                Long demoLong = new Long(demographic);
                JointAdmission demoJadm = clientManager.getJointAdmission(demoLong);
                System.out.println("DEMO JADM: " + demoJadm);

                // IS PERSON JOINTLY ADMITTED WITH ME, They will either have the same HeadClient or be my headClient
                if (clientsJadm != null && clientsJadm.getHeadClientId().longValue() == demoLong) { // they're my head client
                    h.put("jointAdmission", "head");
                }
                else if (demoJadm != null && clientsJadm != null && clientsJadm.getHeadClientId().longValue() == demoJadm.getHeadClientId().longValue()) {
                    // They depend on the same person i do!
                    h.put("jointAdmission", "dependent");
                }
                else if (demoJadm != null && demoJadm.getHeadClientId().longValue() == new Long(demographicNo).longValue()) {
                    // They depend on me
                    h.put("jointAdmission", "dependent");
                }
                // Can this person be added to my depended List
                if (clientsJadm == null && demoJadm == null && clientManager.getDependents(demoLong).size() == 0) {
                    // yes if - i am not dependent on anyone
                    // - this person is not dependent on someone
                    // - this person is not a head of a family already
                    h.put("dependentable", "yes");
                }
                if (demoJadm != null) { // DEPENDS ON SOMEONE
                    h.put("dependentOn", demoJadm.getHeadClientId());
                    if (demoJadm.getHeadClientId().longValue() == new Long(demographicNo).longValue()) {
                        h.put("dependent", demoJadm.getTypeId());
                    }
                }
                else if (clientsJadm != null && clientsJadm.getHeadClientId().longValue() == demoLong) { // HEAD PERSON WON'T DEPEND ON ANYONE
                    h.put("dependent", new Long(0));
                }
            }
            request.setAttribute("relations", relList);
            request.setAttribute("relationSize", familySize);

        }
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