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

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.dao.CriteriaDao;
import org.oscarehr.PMmodule.dao.CriteriaTypeOptionDao;
import org.oscarehr.PMmodule.dao.VacancyDao;
import org.oscarehr.PMmodule.dao.VacancyTemplateDao;
import org.oscarehr.PMmodule.model.Criteria;
import org.oscarehr.PMmodule.model.CriteriaSelectionOption;
import org.oscarehr.PMmodule.model.CriteriaType;
import org.oscarehr.PMmodule.model.CriteriaTypeOption;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramAccess;
import org.oscarehr.PMmodule.model.ProgramClientRestriction;
import org.oscarehr.PMmodule.model.ProgramClientStatus;
import org.oscarehr.PMmodule.model.ProgramFunctionalUser;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.model.ProgramSignature;
import org.oscarehr.PMmodule.model.ProgramTeam;
import org.oscarehr.PMmodule.model.Vacancy;
import org.oscarehr.PMmodule.model.VacancyTemplate;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientRestrictionManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProgramQueueManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.service.VacancyTemplateManager;
import org.oscarehr.PMmodule.utility.ProgramAccessCache;
import org.oscarehr.caisi_integrator.ws.CachedProvider;
import org.oscarehr.caisi_integrator.ws.DemographicTransfer;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.ws.Referral;
import org.oscarehr.caisi_integrator.ws.ReferralWs;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.dao.FunctionalCentreDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.BedCheckTime;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.FunctionalCentre;
import org.oscarehr.common.model.ProgramEncounterType;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.managers.BedCheckTimeManager;
import org.oscarehr.managers.ScheduleManager;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.match.IMatchManager;
import org.oscarehr.match.MatchManager;
import org.oscarehr.match.MatchManagerException;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.quatro.service.security.RolesManager;

import net.sf.json.JSONObject;
import oscar.log.LogAction;

public class ProgramManagerAction extends DispatchAction {

	private static final Logger logger = MiscUtils.getLogger();

	private ClientRestrictionManager clientRestrictionManager;
	private FacilityDao facilityDao = null;
	private AdmissionManager admissionManager;
	private BedCheckTimeManager bedCheckTimeManager;
	private ProgramManager programManager;
	private ProviderManager providerManager;
	private ProgramQueueManager programQueueManager;
	@Autowired
	private VacancyTemplateManager vacancyTemplateManager;
	//private RoleManager roleManager;
	private RolesManager roleManager;
	private FunctionalCentreDao functionalCentreDao;
	private static VacancyTemplateDao vacancyTemplateDAO = (VacancyTemplateDao) SpringUtils.getBean(VacancyTemplateDao.class);
	private static CriteriaDao criteriaDAO = SpringUtils.getBean(CriteriaDao.class);
	//private static CriteriaTypeDao criteriaTypeDAO = SpringUtils.getBean(CriteriaTypeDao.class);
	private static CriteriaTypeOptionDao criteriaTypeOptionDAO = SpringUtils.getBean(CriteriaTypeOptionDao.class);
	//private static CriteriaSelectionOptionDao criteriaSelectionOptionDAO = (CriteriaSelectionOptionDao) SpringUtils.getBean(CriteriaSelectionOptionDao.class);
	
	private TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
	
	private IMatchManager matchManager = new MatchManager();
	
	private ScheduleManager scheduleManager = SpringUtils.getBean(ScheduleManager.class);
	
	public void setFacilityDao(FacilityDao facilityDao) {
		this.facilityDao = facilityDao;
	}

	public void setFunctionalCentreDao(FunctionalCentreDao functionalCentreDao) {
    	this.functionalCentreDao = functionalCentreDao;
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
			} else {
				list = programManager.getProgramDomain(providerNo);
			}
		} else {
			list = programManager.getAllPrograms(searchStatus, searchType, Integer.parseInt(searchFacilityId));
		}
		request.setAttribute("programs", list);
		request.setAttribute("facilities", facilityDao.findAll(true));

		programForm.set("searchStatus", searchStatus);
		programForm.set("searchType", searchType);
		programForm.set("searchFacilityId", searchFacilityId);

		LogAction.log("read", "full program list", "", request);

		return mapping.findForward("list");
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;

		String id = request.getParameter("id");
		
		request.setAttribute("view.tab", request.getParameter("view.tab"));
		if(request.getParameter("newVacancy")!=null && "true".equals(request.getParameter("newVacancy")))
			request.setAttribute("vacancyOrTemplateId", "");
		else
			request.setAttribute("vacancyOrTemplateId", programForm.get("vacancyOrTemplateId"));
		
		if (isCancelled(request)) {
			return list(mapping, form, request, response);
		}

		if (!StringUtils.isEmpty(id)) {
			Program program = programManager.getProgram(id);
			
			if (program == null) {
				ActionMessages messages = new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.missing"));
				saveMessages(request, messages);

				return list(mapping, form, request, response);
			}

			programForm.set("program", program);
			request.setAttribute("oldProgram", program);

			List<FunctionalCentre> functionalCentres = functionalCentreDao.findAll();
			Collections.sort(functionalCentres, FunctionalCentre.ACCOUNT_ID_COMPARATOR);
			request.setAttribute("functionalCentres", functionalCentres);

			// request.setAttribute("programFirstSignature",programManager.getProgramFirstSignature(Integer.valueOf(id)));
			programForm.set("bedCheckTimes", bedCheckTimeManager.getBedCheckTimesByProgram(Integer.valueOf(id)));

			// programForm.set("programFirstSignature",programManager.getProgramFirstSignature(Integer.valueOf(id)));

			// List<ProgramSignature> pss = programManager.getProgramSignatures(Integer.valueOf(id));
			// programForm.set("programSignatures", (ProgramSignature[] ) pss.toArray(new ProgramSignature[pss.size()]));
			// request.setAttribute("programSignatures",programManager.getProgramSignatures(Integer.valueOf(id)));
		}

		setEditAttributes(request, id);
		
		if (id != null && id!="") {
			request.setAttribute("service_restrictions", clientRestrictionManager.getActiveRestrictionsForProgram(Integer.valueOf(id), new Date()));
			request.setAttribute("disabled_service_restrictions", clientRestrictionManager.getDisabledRestrictionsForProgram(Integer.valueOf(id), new Date()));
		}
		return mapping.findForward("edit");
	}

	public ActionForward programSignatures(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		//DynaActionForm programForm = (DynaActionForm) form;
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
		
		List<FunctionalCentre> functionalCentres = functionalCentreDao.findAll();
		Collections.sort(functionalCentres, FunctionalCentre.ACCOUNT_ID_COMPARATOR);
		request.setAttribute("functionalCentres", functionalCentres);

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

		programManager.saveProgramProvider(LoggedInInfo.getLoggedInInfoFromSession(request),pp);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		LogAction.log("write", "edit program - assign role", String.valueOf(program.getId()), request);
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

		programManager.saveProgramProvider(LoggedInInfo.getLoggedInInfoFromSession(request),pp);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		LogAction.log("write", "edit program - assign team", String.valueOf(program.getId()), request);
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

		LogAction.log("write", "edit program - assign client to team", String.valueOf(program.getId()), request);

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

		LogAction.log("write", "delete program", String.valueOf(program.getId()), request);

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

		LogAction.log("write", "edit program - delete access", String.valueOf(program.getId()), request);

		this.setEditAttributes(request, String.valueOf(program.getId()));
		programForm.set("access", new ProgramAccess());

		ProgramAccessCache.setAccessMap(program.getId());
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
		LogAction.log("write", "edit program - delete function user", String.valueOf(program.getId()), request);

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

			LogAction.log("write", "edit program - delete provider", String.valueOf(program.getId()), request);
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

		ProgramAccessCache.setAccessMap(program.getId());
		
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

		LogAction.log("write", "edit program - edit provider", String.valueOf(program.getId()), request);

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

		LogAction.log("write", "edit program - queue removal", String.valueOf(program.getId()), request);

		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward remove_remote_queue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
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

			programManager.saveProgramProvider(LoggedInInfo.getLoggedInInfoFromSession(request),pp);
		}

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		LogAction.log("write", "edit program - assign team (removal)", String.valueOf(program.getId()), request);
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
		programSignature.setCaisiRoleName("n/a");
		Date now = new Date();
		programSignature.setUpdateDate(now);
		programManager.saveProgramSignature(programSignature);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		LogAction.log("write", "edit program", String.valueOf(program.getId()), request);

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
		} catch (NumberFormatException e) {
			MiscUtils.getLogger().error("Error", e);
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
		if (request.getParameter("program.bedProgramLinkId") == null) program.setBedProgramLinkId(0);
		if (request.getParameter("program.alcohol") == null) program.setAlcohol(false);
		if (request.getParameter("program.physicalHealth") == null) program.setPhysicalHealth(false);
		if (request.getParameter("program.mentalHealth") == null) program.setMentalHealth(false);
		if (request.getParameter("program.enableOCAN") == null) program.setEnableOCAN(false);
		if (request.getParameter("program.housing") == null) program.setHousing(false);
		if (request.getParameter("program.enableEncounterTime") == null) program.setEnableEncounterTime(false);
		if (request.getParameter("program.enableEncounterTransportationTime") == null) program.setEnableEncounterTransportationTime(false);

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

		ProgramAccessCache.setAccessMap(program.getId());
		
		LogAction.log("write", "edit program", String.valueOf(program.getId()), request);

		setEditAttributes(request, String.valueOf(program.getId()));

		return edit(mapping, form, request, response);
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
		oldProgram.setBedProgramLinkId(getParameterAsInteger(request,"old_bedProgramLinkId",0));
		oldProgram.setManOrWoman(request.getParameter("old_manOrWoman"));
		oldProgram.setAbstinenceSupport(request.getParameter("old_abstinenceSupport"));
		oldProgram.setExclusiveView(request.getParameter("old_exclusiveView"));

		oldProgram.setHoldingTank(getParameterAsBoolean(request,"old_holdingTank"));
		oldProgram.setAllowBatchAdmission(getParameterAsBoolean(request,"old_allowBatchAdmission"));
		oldProgram.setAllowBatchDischarge(getParameterAsBoolean(request,"old_allowBatchDischarge"));
		oldProgram.setHic(getParameterAsBoolean(request,"old_hic"));
		oldProgram.setTransgender(getParameterAsBoolean(request,"old_transgender"));
		oldProgram.setFirstNation(getParameterAsBoolean(request,"old_firstNation"));
		oldProgram.setBedProgramAffiliated(getParameterAsBoolean(request,"old_bedProgramAffiliated"));
		oldProgram.setAlcohol(getParameterAsBoolean(request,"old_alcohol"));
		oldProgram.setPhysicalHealth(getParameterAsBoolean(request,"old_physicalHealth"));
		oldProgram.setMentalHealth(getParameterAsBoolean(request,"old_mentalHealth"));
		oldProgram.setEnableOCAN(getParameterAsBoolean(request,"old_enableOCAN"));
		oldProgram.setHousing(getParameterAsBoolean(request,"old_housing"));
		oldProgram.setFacilityId(getParameterAsInteger(request,"old_facility_id",0));
		oldProgram.setEnableEncounterTime(getParameterAsBoolean(request,"old_enableEncounterTime"));
		oldProgram.setEnableEncounterTransportationTime(getParameterAsBoolean(request,"old_enableEncounterTransportationTime"));

		if (isChanged(program, oldProgram)) {
			ProgramSignature programSignature = new ProgramSignature();
			programSignature.setProgramId(program.getId());
			programSignature.setProgramName(program.getName());
			String providerNo = (String) request.getSession().getAttribute("user");
			programSignature.setProviderId(providerNo);
			programSignature.setProviderName(providerManager.getProvider(providerNo).getFormattedName());
			programSignature.setCaisiRoleName("n/a");
			Date now = new Date();
			programSignature.setUpdateDate(now);

			programManager.saveProgramSignature(programSignature);
		}
	}
	
	public ActionForward viewVacancyTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String templateId = request.getParameter("templateId");
		
		request.setAttribute("templateId", templateId);
		
		List<Criteria> criterias = criteriaDAO.getCriteriaByTemplateId(Integer.valueOf(templateId));
		request.setAttribute("criterias",criterias);
		
		return mapping.findForward("edit");
	}

	public ActionForward chooseTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		
		programForm.set("program", program);
		
		String templateId = request.getParameter("requiredVacancyTemplateId");
		
		request.setAttribute("view.tab", "vacancy_add");
		
		VacancyTemplate vt = vacancyTemplateDAO.getVacancyTemplate(Integer.valueOf(templateId));
		
		request.setAttribute("selectedTemplate",vt);
		
		setEditAttributes(request, String.valueOf(program.getId()));
		
		return mapping.findForward("edit");
	}
	
	public ActionForward save_vacancy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		
		if (this.isCancelled(request)) {
			return list(mapping, form, request, response);
		}
				
		HashMap<String,String[]> parameters=new HashMap(request.getParameterMap());
		
		Integer templateId = null;
		String templateId_str = request.getParameter("requiredVacancyTemplateId");
		if(!StringUtils.isBlank(templateId_str))
			templateId = Integer.valueOf(templateId_str);
				
		String dateClosed = parameters.get("dateClosed")[0];
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", request.getLocale());
		Date dateClosedFormatted = new Date();
		if (!StringUtils.isBlank(dateClosed)) {			
			try {
				dateClosedFormatted = formatter.parse(dateClosed);					
			} catch (Exception e){
				logger.warn("warn", e);
			}
		}
		
		Vacancy vacancy = new Vacancy();
		String vacancyId = request.getParameter("vacancyId");
		if(!StringUtils.isBlank(vacancyId)) {
			vacancy = VacancyTemplateManager.getVacancyById(Integer.valueOf(vacancyId));	
			vacancy.setStatus(parameters.get("vacancyStatus")[0]);
			vacancy.setReasonClosed(parameters.get("reasonClosed")[0]);	
			vacancy.setDateClosed(dateClosedFormatted);
			VacancyTemplateManager.saveVacancy(vacancy);

			Facility f = loggedInInfo.getCurrentFacility();
			if(f.getAssignNewVacancyTicklerProvider() != null && f.getAssignNewVacancyTicklerProvider().length()>0 
				&& f.getAssignNewVacancyTicklerDemographic() != null && f.getAssignNewVacancyTicklerDemographic()> 0) {
				createWaitlistNotificationTickler(loggedInInfo,f,vacancy,loggedInInfo.getLoggedInProviderNo());
			}
		} else {		
			vacancy.setTemplateId(templateId);				
			vacancy.setName(request.getParameter("vacancyName"));			
			vacancy.setDateCreated(new Date());			
			vacancy.setWlProgramId(program.getId());
			vacancy.setStatus(parameters.get("vacancyStatus")[0]);
			vacancy.setReasonClosed(parameters.get("reasonClosed")[0]);	
			vacancy.setDateClosed(dateClosedFormatted);
			VacancyTemplateManager.saveVacancy(vacancy);	
			
			Facility f = loggedInInfo.getCurrentFacility();
			if(f.getAssignNewVacancyTicklerProvider() != null && f.getAssignNewVacancyTicklerProvider().length()>0 
				&& f.getAssignNewVacancyTicklerDemographic() != null && f.getAssignNewVacancyTicklerDemographic()> 0) {
				createWaitlistNotificationTickler(loggedInInfo,f,vacancy,loggedInInfo.getLoggedInProviderNo());
			}
				
			List<Criteria> criteriaList = VacancyTemplateManager.getRefinedCriteriasByTemplateId(templateId);
			for(Criteria c : criteriaList) {
				CriteriaType type = VacancyTemplateManager.getCriteriaTypeById(c.getCriteriaTypeId());
				Criteria newCriteria = new Criteria();			
				newCriteria.setVacancyId(vacancy.getId());			
				newCriteria.setMatchScoreWeight(1.0); //???
						
				if(c.getCanBeAdhoc()==1) { //mandatory and not changeable, disabled in the form and the value not contained in request. But it should keep same value in new criteria.
                                	newCriteria.setCanBeAdhoc(c.getCanBeAdhoc());
                                	newCriteria.setCriteriaTypeId(c.getCriteriaTypeId());
                                	newCriteria.setCriteriaValue(c.getCriteriaValue());
                                	newCriteria.setRangeEndValue(c.getRangeEndValue());
                                	newCriteria.setRangeStartValue(c.getRangeStartValue());
                                	VacancyTemplateManager.saveCriteria(newCriteria);
                                	continue;
                        	}

				String required = type.getFieldName().toLowerCase().replaceAll(" ","_")+"Required";
				if(request.getParameter(required) == null) 
					newCriteria.setCanBeAdhoc(0);
				else
					newCriteria.setCanBeAdhoc(Integer.valueOf(request.getParameter(required)));
				
				String targetName = "targetOf"+ type.getFieldName().toLowerCase().replaceAll(" ","_");
				String[] answers = parameters.get(targetName);
				
				saveTemplateOrVacancy(parameters, answers, type, newCriteria, request);			
			}	
				
			//Call Match Manager
			//TODO do the testing
			try {
		        matchManager.processEvent(vacancy, IMatchManager.Event.VACANCY_CREATED);
	        } catch (MatchManagerException e) {
	        	log.error("Match manager failed", e);
	        }
		}

		setEditAttributes(request, String.valueOf(program.getId()));
		return edit(mapping, form, request, response);		
		
	}
	
    private void createWaitlistWithdrawnNotificationTickler(LoggedInInfo loggedInInfo,Facility facility, Vacancy vacancy, String creatorProviderNo) {
        Tickler t = new Tickler();
        t.setCreator(creatorProviderNo);
        t.setDemographicNo(facility.getVacancyWithdrawnTicklerDemographic());
        t.setMessage("vacancy=["+vacancy.getName()+"] withdrawn");      
        t.setProgramId(vacancy.getWlProgramId());
        t.setServiceDate(new Date());     
        t.setTaskAssignedTo(facility.getVacancyWithdrawnTicklerProvider());
        t.setUpdateDate(new Date());
     
        ticklerManager.addTickler(loggedInInfo,t);
    }

	
	private void createWaitlistNotificationTickler(LoggedInInfo loggedInInfo,Facility facility, Vacancy vacancy, String creatorProviderNo) {
		Tickler t = new Tickler();
		t.setCreator(creatorProviderNo);
		t.setDemographicNo(facility.getAssignNewVacancyTicklerDemographic());
		t.setMessage("New vacancy=["+vacancy.getName()+"]");
		t.setProgramId(vacancy.getWlProgramId());
		t.setServiceDate(new Date());
		t.setTaskAssignedTo(facility.getAssignNewVacancyTicklerProvider());
		t.setUpdateDate(new Date());
		
		ticklerManager.addTickler(loggedInInfo,t);
	}
	
	public ActionForward save_vacancy_template(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		
		if (this.isCancelled(request)) {
			return list(mapping, form, request, response);
		}
				
		HashMap<String,String[]> parameters=new HashMap(request.getParameterMap());
		
		//save tmeplate
		String vacancyTemplateId = request.getParameter("vacancyOrTemplateId");
		VacancyTemplate vacancyTemplate=VacancyTemplateManager.createVacancyTemplate(vacancyTemplateId);
		vacancyTemplate.setName(request.getParameter("templateName"));
		if (request.getParameter("templateActive") == null) {
			vacancyTemplate.setActive(false);
		}		
		vacancyTemplate.setWlProgramId(Integer.parseInt(request.getParameter("programId")));
		VacancyTemplateManager.saveVacancyTemplate(vacancyTemplate);	
		
		//Save Criteria
		//List<CriteriaType> typeList = VacancyTemplateManager.getAllCriteriaTypes();
		List<CriteriaType> typeList = VacancyTemplateManager.getAllCriteriaTypesByWlProgramId(Integer.parseInt(request.getParameter("programId")));
		for(CriteriaType type : typeList) {
			Criteria criteria = new Criteria();		
			criteria.setTemplateId(vacancyTemplate.getId());			
			String required = type.getFieldName().toLowerCase().replaceAll(" ","_")+"Required";
			criteria.setCanBeAdhoc(request.getParameter(required)==null?0:Integer.valueOf(request.getParameter(required)));
			String targetName = "targetOf"+ type.getFieldName().toLowerCase().replaceAll(" ","_");
			String[] answers = parameters.get(targetName);
			
			saveTemplateOrVacancy(parameters, answers, type, criteria, request);
			
		}	
		
		setEditAttributes(request, String.valueOf(program.getId()));
		
		return edit(mapping, form, request, response);		
	}
	
	private void saveTemplateOrVacancy(HashMap<String, String[]> parameters, String[] answers, CriteriaType type, Criteria criteria, HttpServletRequest request) {
		
		if(type.getFieldType().equalsIgnoreCase("select_multiple") || type.getFieldType().equalsIgnoreCase("select_multiple_narrowing")) {
			
			saveCriteria(criteria, answers);
			
		} else if(type.getFieldType().equalsIgnoreCase("select_one_range")) {
			
			String sourceName = "sourceOf" + type.getFieldName().toLowerCase().replaceAll(" ","_");
			String[] singleAnswers = parameters.get(sourceName);
			String answer = "";
			if(singleAnswers!=null && singleAnswers.length>0)
				answer = singleAnswers[0];
			criteria.setCriteriaValue(answer);
			
			String minName = type.getFieldName().toLowerCase().replaceAll(" ","_")+"Minimum";
			String maxName = type.getFieldName().toLowerCase().replaceAll(" ","_")+"Maximum";
			if(!StringUtils.isBlank(request.getParameter(minName)))
				criteria.setRangeStartValue(Integer.valueOf(request.getParameter(minName)));
			if(!StringUtils.isBlank(request.getParameter(maxName)))
				criteria.setRangeEndValue(Integer.valueOf(request.getParameter(maxName)));
			
			criteria.setCriteriaTypeId(type.getId()); 	
			
			VacancyTemplateManager.saveCriteria(criteria);
		
		} else if(type.getFieldType().equalsIgnoreCase("select_one")) {
			
			String sourceName = "sourceOf" + type.getFieldName().toLowerCase().replaceAll(" ","_");
			String[] singleAnswers = parameters.get(sourceName);
			String answer = "";
			if(singleAnswers!=null && singleAnswers.length>0)
				answer = singleAnswers[0];
			criteria.setCriteriaValue(answer);
			
			criteria.setCriteriaTypeId(type.getId()); 	
			
			VacancyTemplateManager.saveCriteria(criteria);
			
		} else if(type.getFieldType().equalsIgnoreCase("number")) {
			
			String numberName = type.getFieldName().toLowerCase().replaceAll(" ","_") + "Number";
			String[] numberAnswers = parameters.get(numberName);
			String number = "";
			if(numberAnswers!=null && numberAnswers.length>0)
				number = numberAnswers[0];
			criteria.setCriteriaValue(number);
			
			criteria.setCriteriaTypeId(type.getId());
			
			VacancyTemplateManager.saveCriteria(criteria);
			
		} else {
			//do nothing for now
		}
}
	
	private void saveCriteria(Criteria criteria, String [] paramValues) {	
		if(paramValues==null || paramValues.length<1) 
			return;
		for(int i=0; i<paramValues.length; i++) {		
			CriteriaTypeOption option = criteriaTypeOptionDAO.getByValue(paramValues[i]);
			if(option!=null) {
				criteria.setCriteriaTypeId(option.getCriteriaTypeId());
			}
			
			//criteria.setMatchScoreWeight(Double.parseDouble("0.5"));
			VacancyTemplateManager.saveCriteria(criteria);
			
			//Save criteria_selection_option
			CriteriaSelectionOption selectedOption = new CriteriaSelectionOption();
			selectedOption.setCriteriaId(criteria.getId());
			//selectedOption.setOptionValue(String.valueOf(option.getId()));
			selectedOption.setOptionValue(option.getOptionValue());
			VacancyTemplateManager.saveCriteriaSelectedOption(selectedOption);	
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
				//access.getRoles().add()
			}
		}

		programManager.saveProgramAccess(access);

		LogAction.log("write", "access", String.valueOf(program.getId()), request);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);
		programForm.set("access", new ProgramAccess());
		setEditAttributes(request, String.valueOf(program.getId()));

		ProgramAccessCache.setAccessMap(program.getId());
		
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

		LogAction.log("write", "edit program - save function user", String.valueOf(program.getId()), request);

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

		programManager.saveProgramProvider(LoggedInInfo.getLoggedInInfoFromSession(request),provider);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		LogAction.log("write", "edit program - save provider", String.valueOf(program.getId()), request);
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

		LogAction.log("write", "edit program - save team", String.valueOf(program.getId()), request);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);
		programForm.set("team", new ProgramTeam());
		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	private void setEditAttributes(HttpServletRequest request, String programId) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if (!StringUtils.isEmpty(programId)) {
			request.setAttribute("id", programId);
			request.setAttribute("programName", programManager.getProgram(programId).getName());
			request.setAttribute("providers", programManager.getProgramProviders(programId));
			request.setAttribute("functional_users", programManager.getFunctionalUsers(programId));

			List<ProgramTeam> teams = programManager.getProgramTeams(programId);

			for (ProgramTeam team : teams) {
				team.setProviders(programManager.getAllProvidersInTeam(Integer.valueOf(programId), team.getId()));
				team.setAdmissions(programManager.getAllClientsInTeam(Integer.valueOf(programId), team.getId()));
			}

			request.setAttribute("teams", teams);
			request.setAttribute("client_statuses", programManager.getProgramClientStatuses(new Integer(programId)));

			//this can be pretty big
			if(request.getAttribute("view.tab") != null && request.getAttribute("view.tab").equals("Clients"))
				request.setAttribute("admissions", admissionManager.getCurrentAdmissionsByProgramId(programId));
			
			request.setAttribute("accesses", programManager.getProgramAccesses(programId));
			request.setAttribute("queue", programQueueManager.getActiveProgramQueuesByProgramId(Long.valueOf(programId)));

			if (CaisiIntegratorManager.isEnableIntegratedReferrals(loggedInInfo.getCurrentFacility())) {
				request.setAttribute("remoteQueue", getRemoteQueue(loggedInInfo, Integer.parseInt(programId)));
			}

			request.setAttribute("programFirstSignature", programManager.getProgramFirstSignature(Integer.valueOf(programId)));
			
			request.setAttribute("myGroups", scheduleManager.getMyGroupsByProgramNo(Integer.parseInt(programId)));
			
			request.setAttribute("encounterTypes", programManager.getCustomEncounterTypes(loggedInInfo, Integer.parseInt(programId)));
			request.setAttribute("encTypes", programManager.getNonGlobalEncounterTypes(loggedInInfo));
			
		}

		request.setAttribute("roles", roleManager.getRoles());
		request.setAttribute("functionalUserTypes", programManager.getFunctionalUserTypes());

		request.setAttribute("accessTypes", programManager.getAccessTypes());
		request.setAttribute("bed_programs", programManager.getBedPrograms());

		request.setAttribute("facilities", facilityDao.findAll(true));
		
		request.setAttribute("allMyGroups", scheduleManager.getMyGroups());
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

	protected List<RemoteQueueEntry> getRemoteQueue(LoggedInInfo loggedInInfo, int programId) {
		try {
			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());
			ReferralWs referralWs = CaisiIntegratorManager.getReferralWs(loggedInInfo, loggedInInfo.getCurrentFacility());
			List<Referral> remoteReferrals = referralWs.getReferralsToProgram(programId);

			ArrayList<RemoteQueueEntry> results = new ArrayList<RemoteQueueEntry>();
			for (Referral remoteReferral : remoteReferrals) {
				RemoteQueueEntry remoteQueueEntry = new RemoteQueueEntry();
				remoteQueueEntry.setReferral(remoteReferral);

				DemographicTransfer demographicTransfer = demographicWs.getDemographicByFacilityIdAndDemographicId(remoteReferral.getSourceIntegratorFacilityId(), remoteReferral.getSourceCaisiDemographicId());
				if (demographicTransfer != null) {
					remoteQueueEntry.setClientName(demographicTransfer.getLastName() + ", " + demographicTransfer.getFirstName());
				} else {
					remoteQueueEntry.setClientName("N/A");
				}

				FacilityIdStringCompositePk pk = new FacilityIdStringCompositePk();
				pk.setIntegratorFacilityId(remoteReferral.getSourceIntegratorFacilityId());
				pk.setCaisiItemId(remoteReferral.getSourceCaisiProviderId());
				CachedProvider cachedProvider = CaisiIntegratorManager.getProvider(loggedInInfo, loggedInInfo.getCurrentFacility(),pk);
				if (cachedProvider != null) {
					remoteQueueEntry.setProviderName(cachedProvider.getLastName() + ", " + cachedProvider.getFirstName());
				} else {
					remoteQueueEntry.setProviderName("N/A");
				}

				results.add(remoteQueueEntry);
			}
			return (results);
		} catch (MalformedURLException e) {
			logger.error("Unexpected Error.", e);
		} catch (WebServiceException e) {
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

		LogAction.log("write", "edit program - save status", String.valueOf(program.getId()), request);

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

		if(admission.getClientStatusId() != null && admission.getClientStatusId().intValue() == 0) {
			ad.setClientStatusId(null);
		} else {
			ad.setClientStatusId(admission.getClientStatusId());
		}

		admissionManager.saveAdmission(ad);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		LogAction.log("write", "edit program - assign client to status", String.valueOf(program.getId()), request);

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
	
	public ActionForward activeTmplStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		String vacancyId = (String)programForm.get("vacancyOrTemplateId");
		try {
			int templateId = Integer.parseInt(vacancyId);
			VacancyTemplate vacTmpl = vacancyTemplateDAO.getVacancyTemplate(templateId);
			if (vacTmpl != null) {
				vacTmpl.setActive(true);
				try {
					vacancyTemplateDAO.mergeVacancyTemplate(vacTmpl);
				} catch (Exception e) {
					logger.debug(e.toString());
				}
			}
		} catch (Exception e) {
			logger.debug(e.toString());
		}
		
		return edit(mapping, form, request, response);
	}
	
	public ActionForward inactiveTmplStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		String vacancyId = (String)programForm.get("vacancyOrTemplateId");
		try {
			int templateId = Integer.parseInt(vacancyId);
			VacancyTemplate vacTmpl = vacancyTemplateDAO.getVacancyTemplate(templateId);
			if (vacTmpl != null) {
				vacTmpl.setActive(false);
				try {
					vacancyTemplateDAO.mergeVacancyTemplate(vacTmpl);
				} catch (Exception e) {
					logger.debug(e.toString());
				}
			}
		} catch (Exception e) {
			logger.debug(e.toString());
		}
		
		return edit(mapping, form, request, response);
	}



    
	public ActionForward saveVacancyStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String vacancyId = request.getParameter("vacancyId");
		String status = request.getParameter("status");
		boolean success=true;
		String error = "";
		
		VacancyDao vacancyDao = SpringUtils.getBean(VacancyDao.class);
		Vacancy vacancy = vacancyDao.find(Integer.parseInt(vacancyId));
		
		if(vacancy != null) {
			vacancy.setStatus(status);
			vacancy.setStatusUpdateUser(loggedInInfo.getLoggedInProviderNo());
			vacancy.setStatusUpdateDate(new Date());
			vacancyDao.merge(vacancy);
			
            Facility f = loggedInInfo.getCurrentFacility();
            if(status.equals("Withdrawn") && f.getVacancyWithdrawnTicklerProvider() != null && f.getVacancyWithdrawnTicklerProvider().length()>0 
                    && f.getVacancyWithdrawnTicklerDemographic() != null && f.getVacancyWithdrawnTicklerDemographic()> 0) {
                    createWaitlistWithdrawnNotificationTickler(loggedInInfo,f,vacancy,loggedInInfo.getLoggedInProviderNo());
            }
            
		} else {
			error="Vacancy not found";
			success=false;
		}
		
		JSONObject obj = new JSONObject();
		obj.put("success", success);
		obj.put("error", error);
		
		try {
			response.getWriter().print(obj.toString());
		}catch(IOException e) {
			MiscUtils.getLogger().warn("error writing json",e);
		}
		return null;
	}
	
	
	
	public ActionForward saveScheduleGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");

		String[] vals = request.getParameterValues("checked_group");
		scheduleManager.replaceMyGroupProgram(program.getId(),vals);
		
		
		LogAction.log("write", "scheduleprogram", String.valueOf(program.getId()), request);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);
		//programForm.set("access", new ProgramAccess());
		setEditAttributes(request, String.valueOf(program.getId()));

		
		return mapping.findForward("edit");
	}
	
	private boolean isChanged(Program program1, Program program2) {
		boolean changed = false;

		if (   !eq(program1.getName(), program2.getName())
                    || !eq(program1.getType(), program2.getType())
                    || !eq(program1.getDescription(), program2.getDescription())
                    || !eq(program1.getAddress(), program2.getAddress())
                    || !eq(program1.getPhone(), program2.getPhone())
                    || !eq(program1.getFax(), program2.getFax())
                    || !eq(program1.getUrl(), program2.getUrl())
                    || !eq(program1.getEmail(), program2.getEmail())
                    || !eq(program1.getEmergencyNumber(), program2.getEmergencyNumber())
                    || !eq(program1.getLocation(), program2.getLocation())
                    || !eq(program1.getProgramStatus(), program2.getProgramStatus())
                    || !eq(program1.getManOrWoman(), program2.getManOrWoman())
                    || !eq(program1.getAbstinenceSupport(), program2.getAbstinenceSupport())
                    || !eq(program1.getExclusiveView(), program2.getExclusiveView())
                    || !eq(program1.getBedProgramLinkId(), program2.getBedProgramLinkId())
                    || !eq(program1.getMaxAllowed(), program2.getMaxAllowed())
                    || (program1.isHoldingTank() ^ program2.isHoldingTank())
                    || (program1.isAllowBatchAdmission() ^ program2.isAllowBatchAdmission())
                    || (program1.isAllowBatchDischarge() ^ program2.isAllowBatchDischarge())
                    || (program1.isHic() ^ program2.isHic())
                    || (program1.isTransgender() ^ program2.isTransgender())
                    || (program1.isFirstNation() ^ program2.isFirstNation())
                    || (program1.isBedProgramAffiliated() ^ program2.isBedProgramAffiliated())
                    || (program1.isAlcohol() ^ program2.isAlcohol())
                    || (program1.isPhysicalHealth() ^ program2.isPhysicalHealth())
                    || (program1.isMentalHealth() ^ program2.isMentalHealth())
                    || (program1.getFacilityId() != program2.getFacilityId())
                    || (program1.isHousing() ^ program2.isHousing()))

		changed = true;

		return changed;
	}

        private boolean eq(String s1, String s2) {
            return ((s1==null && s2==null) || s1.equals(s2));
        }

        private boolean eq(Integer in1, Integer in2) {
            return ((in1==null && in2==null) || in1.equals(in2));
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

	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}

	public void setProgramQueueManager(ProgramQueueManager mgr) {
		this.programQueueManager = mgr;
	}

	public void setProviderManager(ProviderManager mgr) {
		this.providerManager = mgr;
	}

	public void setRolesManager(RolesManager mgr) {
		this.roleManager = mgr;
	}

	/**
     * @return the vacancyTemplateManager
     */
    public VacancyTemplateManager getVacancyTemplateManager() {
    	return vacancyTemplateManager;
    }

	/**
     * @param vacancyTemplateManager the vacancyTemplateManager to set
     */
    public void setVacancyTemplateManager(VacancyTemplateManager vacancyTemplateManager) {
    	this.vacancyTemplateManager = vacancyTemplateManager;
    }

	protected Integer getParameterAsInteger(HttpServletRequest request, String name, Integer defaultVal) {
		String param = request.getParameter(name);
		if(!(param==null || param.equals("null") || param.equals(""))) {
			return Integer.valueOf(param);
		}
		return defaultVal;
	}
	
	protected Boolean getParameterAsBoolean(HttpServletRequest request, String name, Boolean defaultVal) {
		String param = request.getParameter(name);
		if(param != null) {
			return Boolean.valueOf(param);
		}
		return defaultVal;
	}
	
	protected Boolean getParameterAsBoolean(HttpServletRequest request, String name) {
		return getParameterAsBoolean(request,name,false);
	}
	
	
	public ActionForward delete_encounterType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramEncounterType encounterType = (ProgramEncounterType) programForm.get("encounterType");

		
		programManager.deleteCustomEncounterType(encounterType.getId());

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		LogAction.log("write", "edit program - delete encounter type", String.valueOf(program.getId()), request);

		this.setEditAttributes(request, String.valueOf(program.getId()));
		programForm.set("encounterType", new ProgramEncounterType());

		return edit(mapping, form, request, response);
	}

	public ActionForward save_encounterType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramEncounterType programEncounterType = (ProgramEncounterType) programForm.get("encounterType");

		if (this.isCancelled(request)) {
			return list(mapping, form, request, response);
		}
		programEncounterType.getId().setProgramId(program.getId().intValue());

		if (programManager.findCustomEncounterType(programEncounterType.getId()) != null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.duplicate_encounterType", program.getName()));
			saveMessages(request, messages);
			programForm.set("encounterType", new ProgramEncounterType());
			setEditAttributes(request, String.valueOf(program.getId()));
			return mapping.findForward("edit");
		}

		programManager.saveCustomEncounterType(programEncounterType);

		LogAction.log("write", "encounter_type", String.valueOf(program.getId()), request);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);
		programForm.set("encounterType", new ProgramEncounterType());
		setEditAttributes(request, String.valueOf(program.getId()));

		
		return mapping.findForward("edit");
	}

	
}
