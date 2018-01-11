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

package org.oscarehr.PMmodule.web.admin;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceException;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.dao.ClientReferralDAO;
import org.oscarehr.PMmodule.dao.VacancyDao;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.BedReservedException;
import org.oscarehr.PMmodule.exception.FunctionalCentreDischargeException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.exception.ServiceRestrictionException;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.model.ProgramTeam;
import org.oscarehr.PMmodule.model.Vacancy;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.ClientRestrictionManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProgramQueueManager;
import org.oscarehr.PMmodule.service.VacancyTemplateManager;
import org.oscarehr.PMmodule.web.formbean.ProgramManagerViewFormBean;
import org.oscarehr.caisi_integrator.ws.ReferralWs;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Bed;
import org.oscarehr.common.model.BedDemographic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.JointAdmission;
import org.oscarehr.common.model.RoomDemographic;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.managers.BedDemographicManager;
import org.oscarehr.managers.BedManager;
import org.oscarehr.managers.RoomDemographicManager;
import org.oscarehr.managers.ScheduleManager;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.factory.annotation.Required;

import oscar.log.LogAction;

public class ProgramManagerViewAction extends DispatchAction {

	private static Logger logger = MiscUtils.getLogger();
	private ClientRestrictionManager clientRestrictionManager;
	private FacilityDao facilityDao = null;
	private CaseManagementManager caseManagementManager;
	private AdmissionManager admissionManager;
	private RoomDemographicManager roomDemographicManager;
	private BedDemographicManager bedDemographicManager;
	private BedManager bedManager;
	private ClientManager clientManager;
	private ProgramManager programManager;
	private ProgramManagerAction programManagerAction;
	private ProgramQueueManager programQueueManager;	
	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
	private ScheduleManager scheduleManager = SpringUtils.getBean(ScheduleManager.class);
	
	public void setFacilityDao(FacilityDao facilityDao) {
		this.facilityDao = facilityDao;
	}

	public void setProgramManagerAction(ProgramManagerAction programManagerAction) {
		this.programManagerAction = programManagerAction;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return view(mapping, form, request, response);
	}

	@SuppressWarnings("unchecked")
    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
		ProgramManagerViewFormBean formBean = (ProgramManagerViewFormBean) form;

        // find the program id
        String programId = request.getParameter("id");

        request.getSession().setAttribute("case_program_id",programId);

        if(request.getParameter("newVacancy")!=null && "true".equals(request.getParameter("newVacancy")))
			request.setAttribute("vacancyOrTemplateId", "");
		else
			request.setAttribute("vacancyOrTemplateId", formBean.getVacancyOrTemplateId());
		
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

        // check if the user is from program's staff
        request.setAttribute("userIsProgramProvider", new Boolean(programManager.getProgramProvider(providerNo, programId) != null));

        // need the queue to determine which tab to go to first
        List<ProgramQueue> queue = programQueueManager.getActiveProgramQueuesByProgramId(Long.valueOf(programId));
        request.setAttribute("queue", queue);

		if (CaisiIntegratorManager.isEnableIntegratedReferrals(loggedInInfo.getCurrentFacility())) {
			request.setAttribute("remoteQueue", programManagerAction.getRemoteQueue(loggedInInfo,Integer.parseInt(programId)));
		}

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
                if ("Transgender".equals(program.getManOrWoman()) && !"T".equals(demographic.getSex()))
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
            if (queue!=null && queue.size() > 0) {
                formBean.setTab("Queue");
            }
            else {
                formBean.setTab("General");
            }
        }

        Program program = programManager.getProgram(programId);
        request.setAttribute("program", program);
        Facility facility=facilityDao.find(program.getFacilityId());
        if(facility!=null) request.setAttribute("facilityName", facility.getName());

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
            List<Admission> ads = admissionManager.getCurrentAdmissionsByProgramId(programId);
            for (Admission admission : ads) {
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
                if (bedProgram.isAllowBatchAdmission() && bedProgram.isActive()) {
                    batchAdmissionPrograms.add(bedProgram);
                }
            }

            List<Program> batchAdmissionServicePrograms = new ArrayList<Program>();
            List<Program> servicePrograms;
            servicePrograms = programManager.getServicePrograms();
            for (Program sp : servicePrograms) {
                if (sp.isAllowBatchAdmission() && sp.isActive()) {
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
        
        if (formBean.getTab().equals("Encounter Types")) {
        	request.setAttribute("encounterTypes", programManager.getCustomEncounterTypes(loggedInInfo, Integer.parseInt(programId)));
        }

        if (formBean.getTab().equals("Bed Check")) {
        	
        	Integer[] bedClientIds = null;
        	Boolean[] isFamilyDependents = null;
        	JointAdmission clientsJadm = null;
        	Bed[] beds = bedManager.getBedsByProgram(Integer.valueOf(programId), true);
        	beds = bedManager.addFamilyIdsToBeds(clientManager, beds);
            if(beds != null  &&  beds.length > 0){
	        	bedClientIds = bedManager.getBedClientIds(beds);
	        	
	    		if(clientManager != null  &&  bedClientIds != null  &&  bedClientIds.length > 0){
	    			isFamilyDependents = new Boolean[beds.length];
	    			for(int i=0; i < bedClientIds.length; i++){
	    				clientsJadm = clientManager.getJointAdmission(Integer.valueOf(bedClientIds[i].toString()));
	    				
	    	    		if(clientsJadm != null  &&  clientsJadm.getHeadClientId() != null) {
	    	    			isFamilyDependents[i] = new Boolean(true);
	    	    		}else{
	    	    			isFamilyDependents[i] = new Boolean(false);
	    	    		}
	    			}
	    		}
            }
    		request.setAttribute("bedDemographicStatuses", bedDemographicManager.getBedDemographicStatuses());
    		formBean.setReservedBeds(beds);
    		request.setAttribute("isFamilyDependents", isFamilyDependents);
            request.setAttribute("communityPrograms", programManager.getCommunityPrograms());
            request.setAttribute("expiredReservations", bedDemographicManager.getExpiredReservations());
        }

        if (formBean.getTab().equals("Client Status")) {
            request.setAttribute("client_statuses", programManager.getProgramClientStatuses(new Integer(programId)));
        }

        if (formBean.getTab().equals("Schedule")) {
        	request.setAttribute("myGroups", scheduleManager.getMyGroupsByProgramNo(Integer.parseInt(programId)));
        }
        
        LogAction.log("view", "program", programId, request);

        request.setAttribute("id", programId);

        return mapping.findForward("view");
    }

	public ActionForward remove_remote_queue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		Integer remoteReferralId = Integer.valueOf(request.getParameter("remoteReferralId"));

		try {
			ReferralWs referralWs = CaisiIntegratorManager.getReferralWs(loggedInInfo, loggedInInfo.getCurrentFacility());
			referralWs.removeReferral(remoteReferralId);
		} catch (MalformedURLException e) {
			logger.error("Unexpected error", e);
		} catch (WebServiceException e) {
			logger.error("Unexpected error", e);
		}

		return view(mapping, form, request, response);
	}

	public ActionForward viewBedReservationChangeReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		Integer reservedBedId = Integer.valueOf(request.getParameter("reservedBedId"));
		logger.debug(reservedBedId);

		// BedDemographicChange[] bedDemographicChanges = bedDemographicManager.getBedDemographicChanges(reservedBedId)
		request.setAttribute("bedReservationChanges", null);

		return mapping.findForward("viewBedReservationChangeReport");
	}

	public ActionForward viewBedCheckReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		Integer programId = Integer.valueOf(request.getParameter("programId"));

		Bed[] beds = bedManager.getBedsByProgram(programId, true);
		beds = bedManager.addFamilyIdsToBeds(clientManager, beds);
		request.setAttribute("reservedBeds", beds);
		return mapping.findForward("viewBedCheckReport");
	}

	public ActionForward admit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, FunctionalCentreDischargeException {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String programId = request.getParameter("id");
		String clientId = request.getParameter("clientId");
		String queueId = request.getParameter("queueId");

		ProgramQueue queue = programQueueManager.getProgramQueue(queueId);
		Program fullProgram = programManager.getProgram(String.valueOf(programId));
		String dischargeNotes = request.getParameter("admission.dischargeNotes");
		String admissionNotes = request.getParameter("admission.admissionNotes");
		String formattedAdmissionDate = request.getParameter("admissionDate");
		Date admissionDate = oscar.util.DateUtils.toDate(formattedAdmissionDate);
		List<Integer> dependents = clientManager.getDependentsList(new Integer(clientId));

		try {
			admissionManager.processAdmission(loggedInInfo, Integer.valueOf(clientId), loggedInInfo.getLoggedInProviderNo(), fullProgram, dischargeNotes, admissionNotes, queue.isTemporaryAdmission(), dependents, admissionDate);
			
			//change vacancy status to filled after one patient is admitted to associated program in that vacancy.
	    	Vacancy vacancy = VacancyTemplateManager.getVacancyByName(queue.getVacancyName());
	    	if(vacancy!=null) {
	    		vacancy.setStatus("Filled");	    	
	    		VacancyTemplateManager.saveVacancy(vacancy);
	    	}
	    	
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.success"));
			saveMessages(request, messages);
		} catch (ProgramFullException e) {
			logger.error("Error", e);
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.full"));
			saveMessages(request, messages);
		} catch (AdmissionException e) {
			logger.error("Error", e);
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.error", e.getMessage()));
			saveMessages(request, messages);
		} catch (ServiceRestrictionException e) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.service_restricted", e.getRestriction().getComments(), e.getRestriction().getProvider().getFormattedName()));
			saveMessages(request, messages);

			// store this for display
			ProgramManagerViewFormBean formBean = (ProgramManagerViewFormBean) form;

			formBean.setServiceRestriction(e.getRestriction());

			request.getSession().setAttribute("programId", programId);
			request.getSession().setAttribute("admission.dischargeNotes", dischargeNotes);
			request.getSession().setAttribute("admission.admissionNotes", admissionNotes);

			request.setAttribute("id", programId);

			request.setAttribute("hasOverridePermission", caseManagementManager.hasAccessRight("Service restriction override on admission", "access", loggedInInfo.getLoggedInProviderNo(), clientId, programId));

			return mapping.findForward("service_restriction_error");
		}

		LogAction.log("view", "admit to program", clientId, request);

		return view(mapping, form, request, response);
	}

	public ActionForward override_restriction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, FunctionalCentreDischargeException {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
		String programId = (String) request.getSession().getAttribute("programId");
		String clientId = request.getParameter("clientId");
		String queueId = request.getParameter("queueId");

		String dischargeNotes = (String) request.getSession().getAttribute("admission.dischargeNotes");
		String admissionNotes = (String) request.getSession().getAttribute("admission.admissionNotes");

		request.setAttribute("id", programId);

		if (isCancelled(request) || !caseManagementManager.hasAccessRight("Service restriction override on referral", "access", loggedInInfo.getLoggedInProviderNo(), clientId, programId)) {
			return view(mapping, form, request, response);
		}

		ProgramQueue queue = programQueueManager.getProgramQueue(queueId);
		Program fullProgram = programManager.getProgram(String.valueOf(programId));

		try {
			admissionManager.processAdmission(loggedInInfo, Integer.valueOf(clientId), loggedInInfo.getLoggedInProviderNo(), fullProgram, dischargeNotes, admissionNotes, queue.isTemporaryAdmission(), true);
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.success"));
			saveMessages(request, messages);
		} catch (ProgramFullException e) {
			logger.error("Error", e);
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.full"));
			saveMessages(request, messages);
		} catch (AdmissionException e) {
			logger.error("Error", e);
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.error", e.getMessage()));
			saveMessages(request, messages);
		} catch (ServiceRestrictionException e) {
			throw new RuntimeException(e);
		}

		LogAction.log("view", "override service restriction", clientId, request);

		LogAction.log("view", "admit to program", clientId, request);

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

		LogAction.log("write", "edit program - assign client to team", "", request);
		return view(mapping, form, request, response);
	}

	public ActionForward assign_status_client(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String admissionId = request.getParameter("admissionId");
		String statusId = request.getParameter("clientStatusId");
		String programName = request.getParameter("program_name");
		Admission ad = admissionManager.getAdmission(Long.valueOf(admissionId));

		ad.setClientStatusId(Integer.valueOf(statusId));

		if(statusId != null && "0".equals(statusId)) {
			ad.setClientStatusId(null);
		} 
		admissionManager.saveAdmission(ad);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", programName));
		saveMessages(request, messages);

		LogAction.log("write", "edit program - assign client to status", "", request);
		return view(mapping, form, request, response);
	}

	public ActionForward batch_discharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		logger.info("do batch discharge");
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String type = request.getParameter("type");
		String admitToProgramId;
		if (type != null && type.equalsIgnoreCase("community")) {
			admitToProgramId = request.getParameter("batch_discharge_community_program");
		} else if (type != null && type.equalsIgnoreCase("bed")) {
			admitToProgramId = request.getParameter("batch_discharge_program");
		} else {
			logger.warn("Invalid program type for batch discharge");
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
					logger.warn("admission #" + admissionId + " not found.");
					continue;
				}

				// temporary admission will not allow batach discharge from bed program.
				if (admission.isTemporaryAdmission() && "bed".equals(type)) {
					message += admission.getClient().getFormattedName() + " is in this bed program temporarily. You cannot do batch discharge for this client!   \n";
					continue;
				}

				// in case some clients maybe is already in the community program
				if (type != null) {
					if (type.equals("community")) {
						Integer clientId = admission.getClientId();
						
						// if discharged program is service program,
						// then should check if the client is in one bed program
						/*
						 * if(program_type.equals("Service")) { Admission admission_bed_program = admissionManager.getCurrentBedProgramAdmission(clientId); if(admission_bed_program!=null){ if(!admission_bed_program.isTemporaryAdmission()){ message +=
						 * admission.getClient().getFormattedName() + " is also in the bed program. You cannot do batch discharge for this client! \n"; continue; } } }
						 */
						// if the client is already in the community program, then cannot do batch discharge to the community program.
						Admission admission_community_program = admissionManager.getCurrentCommunityProgramAdmission(clientId);
						if (admission_community_program != null) {
							message += admission.getClient().getFormattedName() + " is already in one community program. You cannot do batch discharge for this client! \n";
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
					newAdmission.setProviderNo(loggedInInfo.getLoggedInProviderNo());
//					newAdmission.setTeamId(0);

					admissionManager.saveAdmission(newAdmission);
				}
			}
		}

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", message));
		saveMessages(request, messages);

		return view(mapping, form, request, response);
	}
	
	private void createWaitlistRejectionNotificationTickler(LoggedInInfo loggedInInfo, Facility facility, String clientId, Integer vacancyId, String creatorProviderNo) {
		if(vacancyId == null)
			return;
		VacancyDao vacancyDao = SpringUtils.getBean(VacancyDao.class);
		Vacancy vacancy = vacancyDao.find(vacancyId);
		if(vacancy == null)
			return;
		
		Demographic d = demographicDao.getDemographic(clientId);
		Tickler t = new Tickler();
		t.setCreator(creatorProviderNo);
		t.setDemographicNo(Integer.parseInt(clientId));
		t.setMessage("Client=["+d.getFormattedName()+"] rejected from vacancy=["+vacancy.getName()+"]");
		t.setProgramId(vacancy.getWlProgramId());
		t.setServiceDate(new Date());
		t.setTaskAssignedTo(facility.getAssignRejectedVacancyApplicant());
		t.setUpdateDate(new Date());
		
		ticklerManager.addTickler(loggedInInfo,t);
	}

	public ActionForward reject_from_queue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String notes = request.getParameter("admission.admissionNotes");
		String programId = request.getParameter("id");
		String clientId = request.getParameter("clientId");
		String rejectionReason = request.getParameter("radioRejectionReason");

		List<Integer> dependents = clientManager.getDependentsList(new Integer(clientId));

		logger.debug("rejecting from queue: program_id=" + programId + ",clientId=" + clientId);

		ProgramQueue queue = this.programQueueManager.getActiveProgramQueue(programId,clientId);
		
		programQueueManager.rejectQueue(programId, clientId, notes, rejectionReason);

		//TODO: WL notification
		ClientReferralDAO clientReferralDao = SpringUtils.getBean(ClientReferralDAO.class);
		Facility facility = loggedInInfo.getCurrentFacility();
		if(facility.getAssignRejectedVacancyApplicant() != null && facility.getAssignRejectedVacancyApplicant().length()>0) {
			Integer vacancyId = null;
			if(queue!=null) {
				ClientReferral referral = clientReferralDao.getClientReferral(queue.getReferralId());
				if(referral != null) {
					vacancyId = referral.getVacancyId();
				}
			}
			createWaitlistRejectionNotificationTickler(loggedInInfo, facility,clientId,vacancyId, loggedInInfo.getLoggedInProviderNo());
		}
		if (dependents != null) {
			for (Integer l : dependents) {
				logger.debug("rejecting from queue: program_id=" + programId + ",clientId=" + l.intValue());
				programQueueManager.rejectQueue(programId, l.toString(), notes, rejectionReason);
			}
		}

		return view(mapping, form, request, response);
	}

	public ActionForward select_client_for_admit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String programId = request.getParameter("id");
		String clientId = request.getParameter("clientId");
		String queueId = request.getParameter("queueId");

		Program program = programManager.getProgram(String.valueOf(programId));
		ProgramQueue queue = programQueueManager.getProgramQueue(queueId);

		//int numMembers = program.getNumOfMembers().intValue();
		//int maxMem = program.getMaxAllowed().intValue();
		//int familySize = clientManager.getDependents(new Long(clientId)).size();
		// TODO: add warning if this admission ( w/ dependents) will exceed the maxMem

		/*
		 * If the user is currently enrolled in a bed program, we must warn the provider that this will also be a discharge
		 */
		if (program.getType().equalsIgnoreCase("bed") && queue != null && !queue.isTemporaryAdmission()) {
			Admission currentAdmission = admissionManager.getCurrentBedProgramAdmission(Integer.valueOf(clientId));
			if (currentAdmission != null) {
				logger.warn("client already in a bed program..doing a discharge/admit if proceeding");
				request.setAttribute("current_admission", currentAdmission);
				Program currentProgram = programManager.getProgram(String.valueOf(currentAdmission.getProgramId()));
				request.setAttribute("current_program", currentProgram);

				request.setAttribute("sameFacility", program.getFacilityId() == currentProgram.getFacilityId());
			}
		}
		request.setAttribute("do_admit", Boolean.TRUE);
		Date referralDate = queue.getReferralDate();
		String referralDateString = oscar.util.DateUtils.getDate(referralDate,"yyyy-MM-dd");		
		request.setAttribute("referralDate",referralDateString);
		return view(mapping, form, request, response);
	}

	public ActionForward select_client_for_reject(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("do_reject", Boolean.TRUE);

		return view(mapping, form, request, response);
	}

	public ActionForward saveReservedBeds(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FunctionalCentreDischargeException {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		ProgramManagerViewFormBean programManagerViewFormBean = (ProgramManagerViewFormBean) form;

		ActionMessages messages = new ActionMessages();
		Bed[] reservedBeds = programManagerViewFormBean.getReservedBeds();
		List<Integer> familyList = new ArrayList<Integer>();
		boolean isClientDependent = false;
		boolean isClientFamilyHead = false;

		for (int i = 0; reservedBeds != null && i < reservedBeds.length; i++) {
			Bed reservedBed = reservedBeds[i];

			// detect check box false
			if (request.getParameter("reservedBeds[" + i + "].latePass") == null) {
				reservedBed.setLatePass(false);
			}
			// save bed
			try {
				BedDemographic bedDemographic = reservedBed.getBedDemographic();
				// Since bed_check.jsp blocked dependents to have Community programs displayed in dropdown,
				// so reservedBed for dependents have communityProgramId == 0
				// When changed to Community program --> how about room_demographic update???
				if (bedDemographic != null) {
					Integer clientId = bedDemographic.getId().getDemographicNo();

					if (clientId != null) {
						isClientDependent = clientManager.isClientDependentOfFamily(clientId);
						isClientFamilyHead = clientManager.isClientFamilyHead(clientId);
					}

					if (clientId == null || isClientDependent) {// Forbid saving of this particular bedDemographic record when client is dependent of family
						// bedManager.saveBed(reservedBed);//should not save bed if dependent
					} else {// client can be family head or independent

						if (isClientFamilyHead) {
							familyList.clear();
							List<JointAdmission> dependentList = clientManager.getDependents(Integer.valueOf(clientId.toString()));
							familyList.add(clientId);
							for (int j = 0; dependentList != null && j < dependentList.size(); j++) {
								familyList.add(Integer.valueOf(dependentList.get(j).getClientId().toString()));
							}

							for (int k = 0; familyList != null && k < familyList.size(); k++) {
								bedDemographic.getId().setDemographicNo(familyList.get(k));

								BedDemographic dependentBD = bedDemographicManager.getBedDemographicByDemographic(familyList.get(k), loggedInInfo.getCurrentFacility().getId());

								if (dependentBD != null) {
									bedDemographic.getId().setBedId(dependentBD.getId().getBedId());
								}
								bedManager.saveBed(reservedBed);

								// save bed demographic
								bedDemographicManager.saveBedDemographic(bedDemographic);

								Integer communityProgramId = reservedBed.getCommunityProgramId();

								if (communityProgramId > 0) {
									try {
										// discharge to community program
										admissionManager.processDischargeToCommunity(loggedInInfo, communityProgramId, bedDemographic.getId().getDemographicNo(), loggedInInfo.getLoggedInProviderNo(), "bed reservation ended - manually discharged", "0", null, false);
									} catch (AdmissionException e) {

										messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("discharge.failure", e.getMessage()));
										saveMessages(request, messages);
									}
								}
							}

						} else {// client is indpendent
							bedManager.saveBed(reservedBed);

							// save bed demographic
							bedDemographicManager.saveBedDemographic(bedDemographic);

							Integer communityProgramId = reservedBed.getCommunityProgramId();

							if (communityProgramId > 0) {
								try {
									// discharge to community program
									admissionManager.processDischargeToCommunity(loggedInInfo, communityProgramId, bedDemographic.getId().getDemographicNo(), loggedInInfo.getLoggedInProviderNo(), "bed reservation ended - manually discharged", "0", null, false);
								} catch (AdmissionException e) {

									messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("discharge.failure", e.getMessage()));
									saveMessages(request, messages);
								}
							}
						}
					}
				}
			} catch (BedReservedException e) {

				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.reserved.error", e.getMessage()));
				saveMessages(request, messages);
			}
		}// end of for (int i=0; i < reservedBeds.length; i++)

		return view(mapping, form, request, response);
	}

	public ActionForward switch_beds(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		/*
		 * (1)Check whether both clients are from same program //??? probably not necessary ??? (1.1)If not, disallow bed switching
		 * 
		 * (2)Check whether both beds are from same room: (2.1)If beds are from same room: you can switch beds in any circumstances
		 * 
		 * (2.2)If beds are from different rooms: (2.2.1)Only 2 indpendent clients can switch beds between different rooms. (2.2.2)If either client is a dependent, disallow switching beds of different rooms ???(2.2.3)If 2 clients are family heads, allow
		 * switching beds with different rooms with conditions: (2.2.3.1)all dependents have to switch together ???
		 * 
		 * (3)Save changes to the 'bed' table ??? <- not implemented yet
		 */
		ProgramManagerViewFormBean formBean = (ProgramManagerViewFormBean) form;
		ActionMessages messages = new ActionMessages();
		//Bed[] reservedBeds = formBean.getReservedBeds();
		Bed bed1 = null;
		Bed bed2 = null;
		Integer client1 = null;
		Integer client2 = null;
		boolean isSameRoom = false;
		boolean isFamilyHead1 = false;
		boolean isFamilyHead2 = false;
		boolean isFamilyDependent1 = false;
		boolean isFamilyDependent2 = false;
		boolean isIndependent1 = false;
		boolean isIndependent2 = false;
		Integer bedDemographicStatusId1 = null;
		boolean latePass1 = false;
		Date reservationEnd1 = null;
		Date assignEnd1 = null;
		Date today = new Date();
		// List<Integer> familyList = new ArrayList<Integer>();

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		String switchBed1 = formBean.getSwitchBed1();
		String switchBed2 = formBean.getSwitchBed2();

		if (bedManager == null || bedDemographicManager == null || roomDemographicManager == null) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.check.error"));
			saveMessages(request, messages);
			return view(mapping, form, request, response);
		}
		if (switchBed1 == null || switchBed1.length() <= 0) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.check.error"));
			saveMessages(request, messages);
			return view(mapping, form, request, response);
		}


		bed1 = bedManager.getBed(Integer.valueOf(switchBed1));
		bed2 = bedManager.getBed(Integer.valueOf(switchBed2));

		if (bed1 == null || bed2 == null) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.check.error"));
			saveMessages(request, messages);
			return view(mapping, form, request, response);
		}
		BedDemographic bedDemographic1 = bedDemographicManager.getBedDemographicByBed(bed1.getId());
		BedDemographic bedDemographic2 = bedDemographicManager.getBedDemographicByBed(bed2.getId());

		if (bedDemographic1 == null || bedDemographic2 == null) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.check.error"));
			saveMessages(request, messages);
			return view(mapping, form, request, response);
		}
		client1 = bedDemographic1.getId().getDemographicNo();
		client2 = bedDemographic2.getId().getDemographicNo();

		bedDemographicStatusId1 = bedDemographic1.getBedDemographicStatusId();
		latePass1 = bedDemographic1.isLatePass();
		reservationEnd1 = bedDemographic1.getReservationEnd();


		// Check whether both beds are from same room:
		if (bed1.getRoomId().intValue() == bed2.getRoomId().intValue()) {
			isSameRoom = true;
		}

		if (isSameRoom) {// you can switch beds in same room for any client combination
			bedDemographicManager.deleteBedDemographic(bedDemographic1);
			bedDemographicManager.deleteBedDemographic(bedDemographic2);

			bedDemographic1.getId().setDemographicNo(client2);
			bedDemographic1.setBedDemographicStatusId(bedDemographic2.getBedDemographicStatusId());
			bedDemographic1.setLatePass(bedDemographic2.isLatePass());
			bedDemographic1.setReservationStart(today);
			bedDemographic1.setReservationEnd(bedDemographic2.getReservationEnd());
			bedDemographic2.getId().setDemographicNo(client1);
			bedDemographic2.setBedDemographicStatusId(bedDemographicStatusId1);
			bedDemographic2.setLatePass(latePass1);
			bedDemographic2.setReservationStart(today);
			bedDemographic2.setReservationEnd(reservationEnd1);

			bedDemographicManager.saveBedDemographic(bedDemographic1);
			bedDemographicManager.saveBedDemographic(bedDemographic2);
		} else {// beds are from different rooms
			isFamilyHead1 = clientManager.isClientFamilyHead(client1);
			isFamilyHead2 = clientManager.isClientFamilyHead(client2);
			isFamilyDependent1 = clientManager.isClientDependentOfFamily(client1);
			isFamilyDependent2 = clientManager.isClientDependentOfFamily(client2);




			RoomDemographic roomDemographic1 = roomDemographicManager.getRoomDemographicByDemographic(client1, loggedInInfo.getCurrentFacility().getId());
			RoomDemographic roomDemographic2 = roomDemographicManager.getRoomDemographicByDemographic(client2, loggedInInfo.getCurrentFacility().getId());

			if (roomDemographic1 == null || roomDemographic2 == null) {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.check.error"));
				saveMessages(request, messages);
				return view(mapping, form, request, response);
			}

			if (!isFamilyHead1 && !isFamilyDependent1) {
				isIndependent1 = true;
			}
			if (!isFamilyHead2 && !isFamilyDependent2) {
				isIndependent2 = true;
			}


			// Check whether both clients are indpendents
			if (isIndependent1 && isIndependent2) {
				// Can switch beds and rooms
				bedDemographicManager.deleteBedDemographic(bedDemographic1);
				bedDemographicManager.deleteBedDemographic(bedDemographic2);

				bedDemographic1.getId().setDemographicNo(client2);
				bedDemographic1.setBedDemographicStatusId(bedDemographic2.getBedDemographicStatusId());
				bedDemographic1.setLatePass(bedDemographic2.isLatePass());
				bedDemographic1.setReservationStart(today);
				bedDemographic1.setReservationEnd(bedDemographic2.getReservationEnd());
				bedDemographic2.getId().setDemographicNo(client1);
				bedDemographic2.setBedDemographicStatusId(bedDemographicStatusId1);
				bedDemographic2.setLatePass(latePass1);
				bedDemographic2.setReservationStart(today);
				bedDemographic2.setReservationEnd(reservationEnd1);

				bedDemographicManager.saveBedDemographic(bedDemographic1);
				bedDemographicManager.saveBedDemographic(bedDemographic2);

				roomDemographicManager.deleteRoomDemographic(roomDemographic1);
				roomDemographicManager.deleteRoomDemographic(roomDemographic2);

				assignEnd1 = roomDemographic1.getAssignEnd();
				roomDemographic1.getId().setDemographicNo(client2);
				roomDemographic1.setAssignStart(today);
				roomDemographic1.setAssignEnd(roomDemographic2.getAssignEnd());
				roomDemographic2.getId().setDemographicNo(client1);
				roomDemographic2.setAssignStart(today);
				roomDemographic2.setAssignEnd(assignEnd1);

				roomDemographicManager.saveRoomDemographic(roomDemographic1);
				roomDemographicManager.saveRoomDemographic(roomDemographic2);
			} else {
				if (isFamilyDependent1 || isFamilyDependent2) {// if either client is dependent or both are
					// do not allow bed switching
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.check.dependent_disallowed"));
					saveMessages(request, messages);
					return view(mapping, form, request, response);
				}
				if (isFamilyHead1 || isFamilyHead2) {// if either clients are family head
					// very complicated!!!
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.check.familyHead_switch"));
					saveMessages(request, messages);
					return view(mapping, form, request, response);
				}
			}
		}
		return view(mapping, form, request, response);
	}

	@Required
	public void setClientRestrictionManager(ClientRestrictionManager clientRestrictionManager) {
		this.clientRestrictionManager = clientRestrictionManager;
	}

	public void setCaseManagementManager(CaseManagementManager caseManagementManager) {
		this.caseManagementManager = caseManagementManager;
	}

	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
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

	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}

	public void setProgramQueueManager(ProgramQueueManager mgr) {
		this.programQueueManager = mgr;
	}
}
