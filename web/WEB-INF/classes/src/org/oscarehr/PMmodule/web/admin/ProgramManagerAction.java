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

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceException;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.BedCheckTime;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramAccess;
import org.oscarehr.PMmodule.model.ProgramClientRestriction;
import org.oscarehr.PMmodule.model.ProgramClientStatus;
import org.oscarehr.PMmodule.model.ProgramFunctionalUser;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.model.ProgramSignature;
import org.oscarehr.PMmodule.model.ProgramTeam;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.BedCheckTimeManager;
import org.oscarehr.PMmodule.service.ClientRestrictionManager;
import org.oscarehr.PMmodule.service.LogManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProgramQueueManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.service.RoleManager;
import org.oscarehr.PMmodule.web.BaseAction;
import org.oscarehr.caisi_integrator.ws.client.CachedDemographic;
import org.oscarehr.caisi_integrator.ws.client.CachedProvider;
import org.oscarehr.caisi_integrator.ws.client.DemographicWs;
import org.oscarehr.caisi_integrator.ws.client.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.ws.client.Referral;
import org.oscarehr.caisi_integrator.ws.client.ReferralWs;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.util.SessionConstants;
import org.springframework.beans.factory.annotation.Required;

public class ProgramManagerAction extends BaseAction {

	private static final Logger logger = org.apache.log4j.LogManager.getLogger(ProgramManagerAction.class);

	private ClientRestrictionManager clientRestrictionManager;
	private FacilityDao facilityDao = null;
	private AdmissionManager admissionManager;
	private BedCheckTimeManager bedCheckTimeManager;
	private LogManager logManager;
	private ProgramManager programManager;
	private ProviderManager providerManager;
	private ProgramQueueManager programQueueManager;
	private RoleManager roleManager;
	private CaisiIntegratorManager caisiIntegratorManager;

	public void setCaisiIntegratorManager(CaisiIntegratorManager caisiIntegratorManager) {
		this.caisiIntegratorManager = caisiIntegratorManager;
	}

	public void setFacilityDao(FacilityDao facilityDao) {
		this.facilityDao = facilityDao;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping, form, request, response);
	}

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		String searchStatus = (String) programForm.get("searchStatus");
		String searchType = (String) programForm.get("searchType");
		String searchFacilityId = (String) programForm.get("searchFacilityId");

		String providerNo = (String) request.getSession().getAttribute("user");
		String userrole = (String) request.getSession().getAttribute("userrole");

		List<Program> list = null;
		if ("".equals(searchStatus)) {
			// what is 'any' used for? Temporarily commented them out.
			// when click 'program list' on PMM, it will not display community programs, only display bed and service programs.
			// searchStatus = "Any";
			// searchType = "Any";
			// searchFacilityId = "0";

			if (userrole.indexOf("admin") != -1) {
				list = programManager.getAllPrograms();
			}
			else {
				list = programManager.getProgramDomain(providerNo);
			}
		}
		else {
			list = programManager.getAllPrograms(searchStatus, searchType, Integer.parseInt(searchFacilityId));
		}
		request.setAttribute("programs", list);
		request.setAttribute("facilities", facilityDao.findAll(true));

		programForm.set("searchStatus", searchStatus);
		programForm.set("searchType", searchType);
		programForm.set("searchFacilityId", searchFacilityId);

		logManager.log("read", "full program list", "", request);

		return mapping.findForward("list");
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;

		String id = request.getParameter("id");

		if (isCancelled(request)) {
			return list(mapping, form, request, response);
		}

		if (id != null) {
			Program program = programManager.getProgram(id);

			if (program == null) {
				ActionMessages messages = new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.missing"));
				saveMessages(request, messages);

				return list(mapping, form, request, response);
			}

			programForm.set("program", program);
			request.setAttribute("oldProgram", program);

			// request.setAttribute("programFirstSignature",programManager.getProgramFirstSignature(Integer.valueOf(id)));
			programForm.set("bedCheckTimes", bedCheckTimeManager.getBedCheckTimesByProgram(Integer.valueOf(id)));

			// programForm.set("programFirstSignature",programManager.getProgramFirstSignature(Integer.valueOf(id)));

			// List<ProgramSignature> pss = programManager.getProgramSignatures(Integer.valueOf(id));
			// programForm.set("programSignatures", (ProgramSignature[] ) pss.toArray(new ProgramSignature[pss.size()]));
			// request.setAttribute("programSignatures",programManager.getProgramSignatures(Integer.valueOf(id)));
		}

		setEditAttributes(request, id);
		request.setAttribute("service_restrictions", clientRestrictionManager.getActiveRestrictionsForProgram(Integer.valueOf(id), new Date()));
		request.setAttribute("disabled_service_restrictions", clientRestrictionManager.getDisabledRestrictionsForProgram(Integer.valueOf(id), new Date()));

		return mapping.findForward("edit");
	}

	public ActionForward programSignatures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		String programId = request.getParameter("programId");
		if (programId != null) {
			// List<ProgramSignature> pss = programManager.getProgramSignatures(Integer.valueOf(programId));
			// programForm.set("programSignatures", (ProgramSignature[] ) pss.toArray(new ProgramSignature[pss.size()]));
			request.setAttribute("programSignatures", programManager.getProgramSignatures(Integer.valueOf(programId)));
		}
		return mapping.findForward("programSignatures");
	}

	public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		programForm.set("program", new Program());

		setEditAttributes(request, null);

		return mapping.findForward("edit");
	}

	public ActionForward addBedCheckTime(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String programId = request.getParameter("id");
		String addTime = request.getParameter("addTime");

		BedCheckTime bedCheckTime = BedCheckTime.create(Integer.valueOf(programId), addTime);
		bedCheckTimeManager.addBedCheckTime(bedCheckTime);

		return edit(mapping, form, request, response);
	}

	public ActionForward assign_role(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramProvider provider = (ProgramProvider) programForm.get("provider");

		ProgramProvider pp = programManager.getProgramProvider(String.valueOf(provider.getId()));

		pp.setRoleId(provider.getRoleId());

		programManager.saveProgramProvider(pp);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		logManager.log("write", "edit program - assign role", String.valueOf(program.getId()), request);
		programForm.set("provider", new ProgramProvider());

		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward assign_team(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramProvider provider = (ProgramProvider) programForm.get("provider");

		ProgramProvider pp = programManager.getProgramProvider(String.valueOf(provider.getId()));

		ProgramTeam team = programManager.getProgramTeam(request.getParameter("teamId"));

		if (team != null) {
			pp.getTeams().add(team);
		}

		programManager.saveProgramProvider(pp);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		logManager.log("write", "edit program - assign team", String.valueOf(program.getId()), request);
		programForm.set("provider", new ProgramProvider());

		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward assign_team_client(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		Admission admission = (Admission) programForm.get("admission");

		Admission ad = admissionManager.getAdmission(admission.getId());

		ad.setTeamId(admission.getTeamId());

		admissionManager.saveAdmission(ad);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		logManager.log("write", "edit program - assign client to team", String.valueOf(program.getId()), request);

		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");

		if (id == null) {
			return list(mapping, form, request, response);
		}

		/*
		 * have to make sure 1) no clients 2) no queue
		 */
		Program program = programManager.getProgram(id);
		if (program == null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.missing", name));
			saveMessages(request, messages);
			return list(mapping, form, request, response);
		}

		int numAdmissions = admissionManager.getCurrentAdmissionsByProgramId(id).size();
		if (numAdmissions > 0) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.delete.admission", name, String.valueOf(numAdmissions)));
			saveMessages(request, messages);
			return list(mapping, form, request, response);
		}

		int numQueue = programQueueManager.getActiveProgramQueuesByProgramId(Long.valueOf(id)).size();
		if (numQueue > 0) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.delete.queue", name, String.valueOf(numQueue)));
			saveMessages(request, messages);
			return list(mapping, form, request, response);
		}

		programManager.removeProgram(id);
		programManager.deleteProgramProviderByProgramId(Long.valueOf(id));

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.deleted", name));
		saveMessages(request, messages);

		logManager.log("write", "delete program", String.valueOf(program.getId()), request);

		return list(mapping, form, request, response);
	}

	public ActionForward delete_access(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramAccess access = (ProgramAccess) programForm.get("access");

		programManager.deleteProgramAccess(String.valueOf(access.getId()));

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		logManager.log("write", "edit program - delete access", String.valueOf(program.getId()), request);

		this.setEditAttributes(request, String.valueOf(program.getId()));
		programForm.set("access", new ProgramAccess());

		return edit(mapping, form, request, response);
	}

	public ActionForward delete_function(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramFunctionalUser function = (ProgramFunctionalUser) programForm.get("function");

		programManager.deleteFunctionalUser(String.valueOf(function.getId()));

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);
		logManager.log("write", "edit program - delete function user", String.valueOf(program.getId()), request);

		this.setEditAttributes(request, String.valueOf(program.getId()));

		return edit(mapping, form, request, response);
	}

	public ActionForward delete_provider(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramProvider pp = (ProgramProvider) programForm.get("provider");

		if (pp.getId() != null && pp.getId() >= 0) {
			programManager.deleteProgramProvider(String.valueOf(pp.getId()));

			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
			saveMessages(request, messages);

			logManager.log("write", "edit program - delete provider", String.valueOf(program.getId()), request);
		}
		this.setEditAttributes(request, String.valueOf(program.getId()));
		programForm.set("provider", new ProgramProvider());

		return edit(mapping, form, request, response);
	}

	public ActionForward delete_team(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramTeam team = (ProgramTeam) programForm.get("team");

		if (programManager.getAllProvidersInTeam(program.getId(), team.getId()).size() > 0 || programManager.getAllClientsInTeam(program.getId(), team.getId()).size() > 0) {

			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.team.not_empty", program.getName()));
			saveMessages(request, messages);

			this.setEditAttributes(request, String.valueOf(program.getId()));
			return edit(mapping, form, request, response);
		}

		programManager.deleteProgramTeam(String.valueOf(team.getId()));

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		this.setEditAttributes(request, String.valueOf(program.getId()));
		programForm.set("function", new ProgramFunctionalUser());

		return edit(mapping, form, request, response);
	}

	public ActionForward edit_access(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramAccess access = (ProgramAccess) programForm.get("access");

		ProgramAccess pa = programManager.getProgramAccess(String.valueOf(access.getId()));

		if (pa == null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program_access.missing"));
			saveMessages(request, messages);
			setEditAttributes(request, String.valueOf(program.getId()));
			return edit(mapping, form, request, response);
		}
		programForm.set("access", pa);

		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward edit_function(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramFunctionalUser function = (ProgramFunctionalUser) programForm.get("function");

		ProgramFunctionalUser pfu = programManager.getFunctionalUser(String.valueOf(function.getId()));

		if (pfu == null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program_function.missing"));
			saveMessages(request, messages);
			setEditAttributes(request, String.valueOf(program.getId()));
			return edit(mapping, form, request, response);
		}
		programForm.set("function", pfu);
		request.setAttribute("providerName", pfu.getProvider().getFormattedName());

		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward edit_provider(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramProvider provider = (ProgramProvider) programForm.get("provider");

		ProgramProvider pp = programManager.getProgramProvider(String.valueOf(provider.getId()));

		if (pp == null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program_provider.missing"));
			saveMessages(request, messages);
			setEditAttributes(request, String.valueOf(program.getId()));
			return edit(mapping, form, request, response);
		}
		programForm.set("provider", pp);
		request.setAttribute("providerName", pp.getProvider().getFormattedName());

		logManager.log("write", "edit program - edit provider", String.valueOf(program.getId()), request);

		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward edit_team(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramTeam team = (ProgramTeam) programForm.get("team");

		ProgramTeam pt = programManager.getProgramTeam(String.valueOf(team.getId()));

		if (pt == null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program_team.missing"));
			saveMessages(request, messages);
			setEditAttributes(request, String.valueOf(program.getId()));
			return edit(mapping, form, request, response);
		}
		programForm.set("team", pt);
		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward removeBedCheckTime(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String removeId = request.getParameter("removeId");

		bedCheckTimeManager.removeBedCheckTime(Integer.valueOf(removeId));

		return edit(mapping, form, request, response);
	}

	public ActionForward remove_queue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramQueue queue = (ProgramQueue) programForm.get("queue");

		ProgramQueue fullQueue = programQueueManager.getProgramQueue(String.valueOf(queue.getId()));
		fullQueue.setStatus(ProgramQueue.STATUS_REMOVED);
		programQueueManager.saveProgramQueue(fullQueue);

		logManager.log("write", "edit program - queue removal", String.valueOf(program.getId()), request);

		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward remove_remote_queue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");

		Integer facilityId=(Integer)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
		
		Integer remoteReferralId=Integer.valueOf(request.getParameter("remoteReferralId"));
		
		try {
			ReferralWs referralWs = caisiIntegratorManager.getReferralWs(facilityId);
			referralWs.removeReferral(remoteReferralId);
		}
		catch (MalformedURLException e) {
			logger.error("Unexpected error", e);
		}
		catch (WebServiceException e) {
			logger.error("Unexpected error", e);
		}
		
		setEditAttributes(request, String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}

	public ActionForward remove_team(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramProvider provider = (ProgramProvider) programForm.get("provider");

		ProgramProvider pp = programManager.getProgramProvider(String.valueOf(provider.getId()));

		String teamId = request.getParameter("teamId");

		if (teamId != null && teamId.length() > 0) {
			long team_id = Long.valueOf(teamId);

			for (Object o : pp.getTeams()) {
				ProgramTeam team = (ProgramTeam) o;

				if (team.getId() == team_id) {
					pp.getTeams().remove(team);
					break;
				}
			}

			programManager.saveProgramProvider(pp);
		}

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		logManager.log("write", "edit program - assign team (removal)", String.valueOf(program.getId()), request);
		programForm.set("provider", new ProgramProvider());

		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward save_restriction_settings(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;

		Program program = (Program) programForm.get("program");
		Program realProgram = programManager.getProgram(program.getId());

		if (program.getMaxAllowed() < program.getNumOfMembers()) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.max_too_small", program.getName()));
			saveMessages(request, messages);
			setEditAttributes(request, String.valueOf(program.getId()));

			return edit(mapping, form, request, response);
		}

		Integer maxRestrictionDays = program.getMaximumServiceRestrictionDays();
		int defaultRestrictionDays = program.getDefaultServiceRestrictionDays();
		if (maxRestrictionDays != null && maxRestrictionDays != 0 && defaultRestrictionDays > maxRestrictionDays) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.default_restriction_exceeds_maximum", defaultRestrictionDays, maxRestrictionDays));
			saveMessages(request, messages);
			setEditAttributes(request, String.valueOf(program.getId()));

			return edit(mapping, form, request, response);
		}

		// copy over modified attributes
		realProgram.setDefaultServiceRestrictionDays(defaultRestrictionDays);
		if (maxRestrictionDays != null && maxRestrictionDays != 0) realProgram.setMaximumServiceRestrictionDays(maxRestrictionDays);

		// save program & sign the modification of the program
		programManager.saveProgram(realProgram);

		ProgramSignature programSignature = new ProgramSignature();
		programSignature.setProgramId(program.getId());
		programSignature.setProgramName(program.getName());
		String providerNo = (String) request.getSession().getAttribute("user");
		programSignature.setProviderId(providerNo);
		programSignature.setProviderName(providerManager.getProvider(providerNo).getFormattedName());
		programSignature.setCaisiRoleName(providerManager.getProvider(providerNo).getProviderType());
		Date now = new Date();
		programSignature.setUpdateDate(now);
		programManager.saveProgramSignature(programSignature);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		logManager.log("write", "edit program", String.valueOf(program.getId()), request);

		setEditAttributes(request, String.valueOf(program.getId()));

		return edit(mapping, form, request, response);
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;

		Program program = (Program) programForm.get("program");

		if (this.isCancelled(request)) {
			return list(mapping, form, request, response);
		}

		try {
			program.setFacilityId(Integer.parseInt(request.getParameter("program.facilityId")));
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}

		if (request.getParameter("program.allowBatchAdmission") == null) {
			program.setAllowBatchAdmission(false);
		}
		if (request.getParameter("program.allowBatchDischarge") == null) {
			program.setAllowBatchDischarge(false);
		}
		if (request.getParameter("program.hic") == null) {
			program.setHic(false);
		}
		if (request.getParameter("program.holdingTank") == null) {
			program.setHoldingTank(false);
		}
		if (request.getParameter("program.transgender") == null) program.setTransgender(false);
		if (request.getParameter("program.firstNation") == null) program.setFirstNation(false);
		if (request.getParameter("program.bedProgramAffiliated") == null) program.setBedProgramAffiliated(false);
		if (request.getParameter("program.alcohol") == null) program.setAlcohol(false);
		if (request.getParameter("program.physicalHealth") == null) program.setPhysicalHealth(false);
		if (request.getParameter("program.mentalHealth") == null) program.setMentalHealth(false);
		if (request.getParameter("program.housing") == null) program.setHousing(false);

		request.setAttribute("oldProgram", program);

		// if a program has a client in it, you cannot make it inactive
		if (request.getParameter("program.programStatus").equals("inactive")) {
			if (!("External".equals(request.getParameter("program.type")))) {
				// Admission ad = admissionManager.getAdmission(Long.valueOf(request.getParameter("id")));
				List admissions = admissionManager.getCurrentAdmissionsByProgramId(String.valueOf(program.getId()));
				if (admissions.size() > 0) {
					ActionMessages messages = new ActionMessages();
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.client_in_the_program", program.getName()));
					saveMessages(request, messages);
					setEditAttributes(request, String.valueOf(program.getId()));
					return mapping.findForward("edit");
				}
				int numQueue = programQueueManager.getActiveProgramQueuesByProgramId((long) program.getId()).size();
				if (numQueue > 0) {
					ActionMessages messages = new ActionMessages();
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.client_in_the_queue", program.getName(), String.valueOf(numQueue)));
					saveMessages(request, messages);
					setEditAttributes(request, String.valueOf(program.getId()));
					return mapping.findForward("edit");
				}
			}
		}

		if (!program.getType().equalsIgnoreCase("bed") && program.isHoldingTank()) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.invalid_holding_tank"));
			saveMessages(request, messages);
			setEditAttributes(request, String.valueOf(program.getId()));
			return mapping.findForward("edit");
		}

		saveProgram(request, program);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		logManager.log("write", "edit program", String.valueOf(program.getId()), request);

		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	private void saveProgram(HttpServletRequest request, Program program) {
		programManager.saveProgram(program);

		// if there were some changes happened to the program, then save the signature of this program
		Program oldProgram = new Program();
		oldProgram.setMaxAllowed(Integer.valueOf(request.getParameter("old_maxAllowed")));
		oldProgram.setName(request.getParameter("old_name"));
		oldProgram.setDescription(request.getParameter("old_descr"));
		oldProgram.setType(request.getParameter("old_type"));
		oldProgram.setAddress(request.getParameter("old_address"));
		oldProgram.setPhone(request.getParameter("old_phone"));
		oldProgram.setFax(request.getParameter("old_fax"));
		oldProgram.setUrl(request.getParameter("old_url"));
		oldProgram.setEmail(request.getParameter("old_email"));
		oldProgram.setEmergencyNumber(request.getParameter("old_emergencyNumber"));
		oldProgram.setLocation(request.getParameter("old_location"));
		oldProgram.setProgramStatus(request.getParameter("old_programStatus"));
		oldProgram.setBedProgramLinkId(Integer.valueOf(request.getParameter("old_bedProgramLinkId")));
		oldProgram.setManOrWoman(request.getParameter("old_manOrWoman"));
		oldProgram.setAbstinenceSupport(request.getParameter("old_abstinenceSupport"));
		oldProgram.setExclusiveView(request.getParameter("old_exclusiveView"));

		oldProgram.setHoldingTank(Boolean.valueOf(request.getParameter("old_holdingTank")));
		oldProgram.setAllowBatchAdmission(Boolean.valueOf(request.getParameter("old_allowBatchAdmission")));
		oldProgram.setAllowBatchDischarge(Boolean.valueOf(request.getParameter("old_allowBatchDischarge")));
		oldProgram.setHic(Boolean.valueOf(request.getParameter("old_hic")));
		oldProgram.setTransgender(Boolean.valueOf(request.getParameter("old_transgender")));
		oldProgram.setFirstNation(Boolean.valueOf(request.getParameter("old_firstNation")));
		oldProgram.setBedProgramAffiliated(Boolean.valueOf(request.getParameter("old_bedProgramAffiliated")));
		oldProgram.setAlcohol(Boolean.valueOf(request.getParameter("old_alcohol")));
		oldProgram.setPhysicalHealth(Boolean.valueOf(request.getParameter("old_physicalHealth")));
		oldProgram.setMentalHealth(Boolean.valueOf(request.getParameter("old_mentalHealth")));
		oldProgram.setHousing(Boolean.valueOf(request.getParameter("old_housing")));
		oldProgram.setHousing(Boolean.valueOf(request.getParameter("old_facility_id")));

		if (isChanged(program, oldProgram)) {
			ProgramSignature programSignature = new ProgramSignature();
			programSignature.setProgramId(program.getId());
			programSignature.setProgramName(program.getName());
			String providerNo = (String) request.getSession().getAttribute("user");
			programSignature.setProviderId(providerNo);
			programSignature.setProviderName(providerManager.getProvider(providerNo).getFormattedName());
			programSignature.setCaisiRoleName(providerManager.getProvider(providerNo).getProviderType());
			Date now = new Date();
			programSignature.setUpdateDate(now);

			programManager.saveProgramSignature(programSignature);
		}
	}

	public ActionForward save_access(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramAccess access = (ProgramAccess) programForm.get("access");

		if (this.isCancelled(request)) {
			return list(mapping, form, request, response);
		}
		access.setProgramId(program.getId().longValue());

		if (programManager.getProgramAccess(String.valueOf(access.getProgramId()), access.getAccessTypeId()) != null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.duplicate_access", program.getName()));
			saveMessages(request, messages);
			programForm.set("access", new ProgramAccess());
			setEditAttributes(request, String.valueOf(program.getId()));
			return mapping.findForward("edit");
		}

		String roles[] = request.getParameterValues("checked_role");
		if (roles != null) {
			if (access.getRoles() == null) {
				access.setRoles(new HashSet());
			}
			for (String role : roles) {
				access.getRoles().add(roleManager.getRole(role));
			}
		}

		programManager.saveProgramAccess(access);

		logManager.log("write", "access", String.valueOf(program.getId()), request);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);
		programForm.set("access", new ProgramAccess());
		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward save_function(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramFunctionalUser function = (ProgramFunctionalUser) programForm.get("function");

		if (this.isCancelled(request)) {
			return list(mapping, form, request, response);
		}
		function.setProgramId(program.getId().longValue());

		Long pid = programManager.getFunctionalUserByUserType(program.getId().longValue(), function.getUserTypeId());

		if (pid != null && function.getId().longValue() != pid.longValue()) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program_function.duplicate", program.getName()));
			saveMessages(request, messages);
			programForm.set("function", new ProgramFunctionalUser());
			setEditAttributes(request, String.valueOf(program.getId()));
			return mapping.findForward("edit");
		}
		programManager.saveFunctionalUser(function);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		logManager.log("write", "edit program - save function user", String.valueOf(program.getId()), request);

		programForm.set("function", new ProgramFunctionalUser());
		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward save_provider(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramProvider provider = (ProgramProvider) programForm.get("provider");

		if (this.isCancelled(request)) {
			return list(mapping, form, request, response);
		}
		provider.setProgramId(program.getId().longValue());

		if (programManager.getProgramProvider(provider.getProviderNo(), String.valueOf(program.getId())) != null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.provider.exists"));
			saveMessages(request, messages);
			programForm.set("provider", new ProgramProvider());
			setEditAttributes(request, String.valueOf(program.getId()));
			return mapping.findForward("edit");
		}

		programManager.saveProgramProvider(provider);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		logManager.log("write", "edit program - save provider", String.valueOf(program.getId()), request);
		programForm.set("provider", new ProgramProvider());
		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward save_team(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramTeam team = (ProgramTeam) programForm.get("team");

		if (this.isCancelled(request)) {
			return list(mapping, form, request, response);
		}
		team.setProgramId(program.getId());

		if (programManager.teamNameExists(program.getId(), team.getName())) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program_team.duplicate", team.getName()));
			saveMessages(request, messages);
			programForm.set("team", new ProgramTeam());
			setEditAttributes(request, String.valueOf(program.getId()));
			return mapping.findForward("edit");
		}

		programManager.saveProgramTeam(team);

		logManager.log("write", "edit program - save team", String.valueOf(program.getId()), request);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);
		programForm.set("team", new ProgramTeam());
		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	private void setEditAttributes(HttpServletRequest request, String programId) {
		Facility facility = (Facility) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY);

		if (programId != null) {
			request.setAttribute("id", programId);
			request.setAttribute("programName", programManager.getProgram(programId).getName());
			request.setAttribute("providers", programManager.getProgramProviders(programId));
			request.setAttribute("functional_users", programManager.getFunctionalUsers(programId));

			List teams = programManager.getProgramTeams(programId);

			for (Object team1 : teams) {
				ProgramTeam team = (ProgramTeam) team1;

				team.setProviders(programManager.getAllProvidersInTeam(Integer.valueOf(programId), team.getId()));
				team.setAdmissions(programManager.getAllClientsInTeam(Integer.valueOf(programId), team.getId()));
			}

			request.setAttribute("teams", teams);
			request.setAttribute("client_statuses", programManager.getProgramClientStatuses(new Integer(programId)));

			request.setAttribute("admissions", admissionManager.getCurrentAdmissionsByProgramId(programId));
			request.setAttribute("accesses", programManager.getProgramAccesses(programId));
			request.setAttribute("queue", programQueueManager.getActiveProgramQueuesByProgramId(Long.valueOf(programId)));

			if (facility.isIntegratorEnabled() && facility.isEnableIntegratedReferrals()) {
				request.setAttribute("remoteQueue", getRemoteQueue(facility.getId(), Integer.parseInt(programId)));
			}

			request.setAttribute("programFirstSignature", programManager.getProgramFirstSignature(Integer.valueOf(programId)));
		}

		request.setAttribute("roles", roleManager.getRoles());
		request.setAttribute("functionalUserTypes", programManager.getFunctionalUserTypes());

		request.setAttribute("accessTypes", programManager.getAccessTypes());
		request.setAttribute("bed_programs", programManager.getBedPrograms());

		request.setAttribute("facilities", facilityDao.findAll(true));
	}

	public static class RemoteQueueEntry {
		private Referral remoteReferral = null;
		private String clientName = null;
		private String providerName = null;

		public Referral getReferral() {
			return remoteReferral;
		}

		public void setReferral(Referral remoteReferral) {
			this.remoteReferral = remoteReferral;
		}

		public String getClientName() {
			return clientName;
		}

		public void setClientName(String clientName) {
			this.clientName = clientName;
		}

		public String getProviderName() {
			return providerName;
		}

		public void setProviderName(String providerName) {
			this.providerName = providerName;
		}

	}

	protected List<RemoteQueueEntry> getRemoteQueue(int facilityId, int programId) {
		try {
			DemographicWs demographicWs = caisiIntegratorManager.getDemographicWs(facilityId);
			ReferralWs referralWs = caisiIntegratorManager.getReferralWs(facilityId);
			List<Referral> remoteReferrals = referralWs.getReferralsToProgram(programId);

			ArrayList<RemoteQueueEntry> results = new ArrayList<RemoteQueueEntry>();
			for (Referral remoteReferral : remoteReferrals) {
				RemoteQueueEntry remoteQueueEntry = new RemoteQueueEntry();
				remoteQueueEntry.setReferral(remoteReferral);

				CachedDemographic cachedDemographic = demographicWs.getCachedDemographicByFacilityIdAndDemographicId(remoteReferral.getSourceIntegratorFacilityId(),
						remoteReferral.getSourceCaisiDemographicId());
				if (cachedDemographic != null) {
					remoteQueueEntry.setClientName(cachedDemographic.getLastName() + ", " +cachedDemographic.getFirstName());
				}
				else {
					remoteQueueEntry.setClientName("N/A");
				}

				FacilityIdStringCompositePk pk = new FacilityIdStringCompositePk();
				pk.setIntegratorFacilityId(remoteReferral.getSourceIntegratorFacilityId());
				pk.setCaisiItemId(remoteReferral.getSourceCaisiProviderId());
				CachedProvider cachedProvider = caisiIntegratorManager.getProvider(facilityId, pk);
				if (cachedProvider != null) {
					remoteQueueEntry.setProviderName(cachedProvider.getLastName() + ", " +cachedProvider.getFirstName());
				}
				else {
					remoteQueueEntry.setProviderName("N/A");
				}

				results.add(remoteQueueEntry);
			}
			return (results);
		}
		catch (MalformedURLException e) {
			logger.error("Unexpected Error.", e);
		}
		catch (WebServiceException e) {
			logger.error("Unexpected Error.", e);
		}

		return (null);
	}

	public ActionForward delete_status(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramClientStatus status = (ProgramClientStatus) programForm.get("client_status");

		if (programManager.getAllClientsInStatus(program.getId(), status.getId()).size() > 0) {

			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.status.not_empty", program.getName()));
			saveMessages(request, messages);

			this.setEditAttributes(request, String.valueOf(program.getId()));
			return edit(mapping, form, request, response);
		}

		programManager.deleteProgramClientStatus(String.valueOf(status.getId()));

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		this.setEditAttributes(request, String.valueOf(program.getId()));
		programForm.set("function", new ProgramFunctionalUser());

		return edit(mapping, form, request, response);
	}

	public ActionForward edit_status(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramClientStatus status = (ProgramClientStatus) programForm.get("client_status");

		ProgramClientStatus pt = programManager.getProgramClientStatus(String.valueOf(status.getId()));

		if (pt == null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program_status.missing"));
			saveMessages(request, messages);
			setEditAttributes(request, String.valueOf(program.getId()));
			return edit(mapping, form, request, response);
		}
		programForm.set("client_status", pt);
		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward save_status(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramClientStatus status = (ProgramClientStatus) programForm.get("client_status");

		if (this.isCancelled(request)) {
			return list(mapping, form, request, response);
		}
		status.setProgramId(program.getId());

		if (programManager.clientStatusNameExists(program.getId(), status.getName())) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program_status.duplicate", status.getName()));
			saveMessages(request, messages);
			programForm.set("client_status", new ProgramClientStatus());
			setEditAttributes(request, String.valueOf(program.getId()));
			return mapping.findForward("edit");
		}

		programManager.saveProgramClientStatus(status);

		logManager.log("write", "edit program - save status", String.valueOf(program.getId()), request);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);
		programForm.set("client_status", new ProgramClientStatus());
		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward assign_status_client(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		Admission admission = (Admission) programForm.get("admission");

		Admission ad = admissionManager.getAdmission(admission.getId());

		ad.setClientStatusId(admission.getClientStatusId());

		admissionManager.saveAdmission(ad);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		logManager.log("write", "edit program - assign client to status", String.valueOf(program.getId()), request);

		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward disable_restriction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;

		ProgramClientRestriction prc = (ProgramClientRestriction) programForm.get("restriction");
		clientRestrictionManager.disableClientRestriction(prc.getId());

		return edit(mapping, form, request, response);
	}

	public ActionForward enable_restriction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;

		ProgramClientRestriction prc = (ProgramClientRestriction) programForm.get("restriction");
		clientRestrictionManager.enableClientRestriction(prc.getId());

		return edit(mapping, form, request, response);
	}

	private boolean isChanged(Program program1, Program program2) {
		boolean changed = false;

		if (program1.getMaxAllowed().intValue() != program2.getMaxAllowed().intValue() || !program1.getName().equals(program2.getName())
				|| !program1.getType().equals(program2.getType()) || !program1.getDescription().equals(program2.getDescription())
				|| !program1.getAddress().equals(program2.getAddress()) || !program1.getPhone().equals(program2.getPhone()) || !program1.getFax().equals(program2.getFax())
				|| !program1.getUrl().equals(program2.getUrl()) || !program1.getEmail().equals(program2.getEmail())
				|| !program1.getEmergencyNumber().equals(program2.getEmergencyNumber()) || !program1.getLocation().equals(program2.getLocation())
				|| !program1.getProgramStatus().equals(program2.getProgramStatus()) || !program1.getBedProgramLinkId().equals(program2.getBedProgramLinkId())
				|| !program1.getManOrWoman().equals(program2.getManOrWoman()) || !program1.getAbstinenceSupport().equals(program2.getAbstinenceSupport())
				|| !program1.getExclusiveView().equals(program2.getExclusiveView()) || (program1.isHoldingTank() ^ program2.isHoldingTank())
				|| (program1.isAllowBatchAdmission() ^ program2.isAllowBatchAdmission()) || (program1.isAllowBatchDischarge() ^ program2.isAllowBatchDischarge())
				|| (program1.isHic() ^ program2.isHic()) || (program1.isTransgender() ^ program2.isTransgender()) || (program1.isFirstNation() ^ program2.isFirstNation())
				|| (program1.isBedProgramAffiliated() ^ program2.isBedProgramAffiliated()) || (program1.isAlcohol() ^ program2.isAlcohol())
				|| (program1.isPhysicalHealth() ^ program2.isPhysicalHealth()) || (program1.isMentalHealth() ^ program2.isMentalHealth())
				|| (program1.getFacilityId() != program2.getFacilityId()) || (program1.isHousing() ^ program2.isHousing()))

		changed = true;

		return changed;
	}

	@Required
	public void setClientRestrictionManager(ClientRestrictionManager clientRestrictionManager) {
		this.clientRestrictionManager = clientRestrictionManager;
	}

	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}

	public void setBedCheckTimeManager(BedCheckTimeManager bedCheckTimeManager) {
		this.bedCheckTimeManager = bedCheckTimeManager;
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

}