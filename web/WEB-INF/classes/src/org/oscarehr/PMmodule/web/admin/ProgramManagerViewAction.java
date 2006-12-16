package org.oscarehr.PMmodule.web.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.model.ProgramTeam;
import org.oscarehr.PMmodule.web.BaseAction;
import org.oscarehr.PMmodule.web.formbean.ProgramManagerViewFormBean;

public class ProgramManagerViewAction extends BaseAction {

	private static Log log = LogFactory.getLog(ProgramManagerViewAction.class);

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	return view(mapping, form, request, response);
    }

	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	ProgramManagerViewFormBean formBean = (ProgramManagerViewFormBean) form;
    
    	// find the id
    	String id = request.getParameter("id");
    
    	if (id == null) {
    		id = (String) request.getAttribute("id");
    	}
    
    	// queue
    	if (request.getParameter("clientId") != null) {
    		request.setAttribute("clientId", request.getParameter("clientId"));
    	}
    
    	// need the queue to determine which tab to go to first
    	List queue = programQueueManager.getActiveProgramQueuesByProgramId(id);
    	request.setAttribute("queue", queue);
    
    	if (formBean.getTab() == null || formBean.getTab().equals("")) {
    		if (queue.size() > 0) {
    			formBean.setTab("Queue");
    		} else {
    			formBean.setTab("General");
    		}
    	}
    
    	Program program = programManager.getProgram(id);
    	request.setAttribute("program", program);
    
    	if (formBean.getTab().equals("General")) {
    		request.setAttribute("agency", programManager.getAgencyByProgram(id));
    	}
    
    	if (formBean.getTab().equals("Staff")) {
    		request.setAttribute("providers", programManager.getProgramProviders(id));
    	}
    
    	if (formBean.getTab().equals("Function User")) {
    		request.setAttribute("functional_users", programManager.getFunctionalUsers(id));
    	}
    
    	if (formBean.getTab().equals("Teams")) {
    		List teams = programManager.getProgramTeams(id);
    
    		for (Iterator iter = teams.iterator(); iter.hasNext();) {
    			ProgramTeam team = (ProgramTeam) iter.next();
    			team.setProviders(programManager.getAllProvidersInTeam(Integer.valueOf(id), team.getId()));
    			team.setAdmissions(programManager.getAllClientsInTeam(Integer.valueOf(id), team.getId()));
    		}
    
    		request.setAttribute("teams", teams);
    	}
    
    	if (formBean.getTab().equals("Clients")) {
    		request.setAttribute("admissions", admissionManager.getCurrentAdmissionsByProgramId(id));
    		request.setAttribute("program_name", program.getName());
    		List teams = programManager.getProgramTeams(id);
    
    		for (Iterator iter = teams.iterator(); iter.hasNext();) {
    			ProgramTeam team = (ProgramTeam) iter.next();
    			team.setProviders(programManager.getAllProvidersInTeam(Integer.valueOf(id), team.getId()));
    			team.setAdmissions(programManager.getAllClientsInTeam(Integer.valueOf(id), team.getId()));
    		}
    		request.setAttribute("teams", teams);
    
    		List<Program> batchAdmissionPrograms = new ArrayList<Program>();
    		Program[] bedPrograms = programManager.getBedPrograms();
    
    		for (int x = 0; x < bedPrograms.length; x++) {
    			Program p = (Program) bedPrograms[x];
    
    			if (p.isAllowBatchAdmission()) {
    				batchAdmissionPrograms.add(p);
    			}
    		}
    
    		List communityPrograms = programManager.getCommunityPrograms();
    
    		request.setAttribute("communityPrograms", communityPrograms);
    		request.setAttribute("programs", batchAdmissionPrograms);
    		request.setAttribute("allowBatchDischarge", new Boolean(program.isAllowBatchDischarge()));
    	}
    
    	if (formBean.getTab().equals("Access")) {
    		request.setAttribute("accesses", programManager.getProgramAccesses(id));
    	}
    
    	if (formBean.getTab().equals("Bed Check")) {
    		request.setAttribute("reservedBeds", bedManager.getBedsByProgram(Integer.valueOf(id), true, null));
    	}
    
    	logManager.log(getProviderNo(request), "view", "program", id, request.getRemoteAddr());
    
    	request.setAttribute("id", id);
    
    	return mapping.findForward("view");
    }

	public ActionForward admit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String programId = request.getParameter("id");
		String clientId = request.getParameter("clientId");
		String queueId = request.getParameter("queueId");

		ProgramQueue queue = programQueueManager.getProgramQueue(queueId);

		Program fullProgram = programManager.getProgram(String.valueOf(programId));

		try {
			admissionManager.processAdmission(clientId, getProviderNo(request), fullProgram, request.getParameter("admission.dischargeNotes"), request.getParameter("admission.admissionNotes"), queue.isTemporaryAdmission());
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.success"));
			saveMessages(request, messages);
		} catch (ProgramFullException e) {
			log.error(e);
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.full"));
			saveMessages(request, messages);
		} catch (AdmissionException e) {
			log.error(e);
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.error", e.getMessage()));
			saveMessages(request, messages);
		}

		logManager.log(getProviderNo(request), "view", "admit to program", clientId, request.getRemoteAddr());

		return view(mapping, form, request, response);
	}

	public ActionForward assign_team_client(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String admissionId = request.getParameter("admissionId");
		String teamId = request.getParameter("teamId");
		String programName = request.getParameter("program_name");
		Admission ad = admissionManager.getAdmission(Long.valueOf(admissionId));

		ad.setTeamId(Long.valueOf(teamId));

		admissionManager.saveAdmission(ad);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", programName));
		saveMessages(request, messages);

		logManager.log(getProviderNo(request), "write", "edit program - assign client to team", "", getIP(request));
		return view(mapping, form, request, response);
	}

	public ActionForward batch_discharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		log.info("do batch discharge");
		String type = request.getParameter("type");
		String admitToProgramId = null;
		if (type != null && type.equals("community")) {
			admitToProgramId = request.getParameter("batch_discharge_community_program");
		} else {
			admitToProgramId = request.getParameter("batch_discharge_program");
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

				// lets see if there's room first
				Program programToAdmit = programManager.getProgram(admitToProgramId);
				if (programToAdmit == null) {
					message += "Admitting program not found!";
					continue;
				}
				if (programToAdmit.getNumOfMembers().intValue() >= programToAdmit.getMaxAllowed().intValue()) {
					message += "Program Full. Cannot admit " + admission.getClient().getFormattedName() + "\n";
					continue;
				}

				admission.setDischargeDate(new Date());
				admission.setDischargeNotes("Batch discharge");
				admission.setAdmissionStatus("discharged");
				admissionManager.saveAdmission(admission);

				Admission newAdmission = new Admission();
				newAdmission.setAdmissionDate(new Date());
				newAdmission.setAdmissionNotes("Batch Admit");
				newAdmission.setAdmissionStatus("current");
				newAdmission.setClientId(admission.getClientId());
				newAdmission.setProgramId(Long.valueOf(admitToProgramId));
				newAdmission.setProviderNo(Long.valueOf(getProviderNo(request)));
				newAdmission.setTeamId(new Long(0));
				newAdmission.setAgencyId(admission.getAgencyId());

				admissionManager.saveAdmission(newAdmission);
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

		log.debug("rejecting from queue: program_id=" + programId + ",clientId=" + clientId);

		programQueueManager.rejectQueue(programId, clientId, notes);

		return view(mapping, form, request, response);
	}

	public ActionForward select_client_for_admit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String programId = request.getParameter("id");
		String clientId = request.getParameter("clientId");
		String queueId = request.getParameter("queueId");

		Program program = programManager.getProgram(String.valueOf(programId));
		ProgramQueue queue = programQueueManager.getProgramQueue(queueId);
		/*
		 * If the user is currently enrolled in a bed program, we must warn the provider that this will also be a discharge
		 */
		if (program.getType().equalsIgnoreCase("bed") && queue != null && !queue.isTemporaryAdmission()) {
			Admission currentAdmission = admissionManager.getCurrentBedProgramAdmission(clientId);
			if (currentAdmission != null) {
				log.warn("client already in a bed program..doing a discharge/admit if proceeding");
				request.setAttribute("current_admission", currentAdmission);
				request.setAttribute("current_program", programManager.getProgram(String.valueOf(currentAdmission.getProgramId())));
			}
		}
		request.setAttribute("do_admit", new Boolean(true));

		return view(mapping, form, request, response);
	}

	public ActionForward select_client_for_reject(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("do_reject", new Boolean(true));

		return view(mapping, form, request, response);
	}
	
	public ActionForward unreserveBed(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	ProgramManagerViewFormBean formBean = (ProgramManagerViewFormBean) form;
		Integer bedId = Integer.valueOf(formBean.getBedId());
		
		try {
			BedDemographic bedDemographic = bedDemographicManager.getBedDemographicByBed(bedId);
			admissionManager.processDischargeToCommunity(Program.DEFAULT_COMMUNITY_PROGRAM_ID, bedDemographic.getId().getDemographicNo(), getProviderNo(request), "bed reservation ended - manually discharged");
			bedDemographicManager.deleteBedDemographic(bedDemographic);
		} catch (AdmissionException e) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("discharge.failure", e.getMessage()));
			saveMessages(request, messages);
		}
		
		return view(mapping, form, request, response);
	}
	
}