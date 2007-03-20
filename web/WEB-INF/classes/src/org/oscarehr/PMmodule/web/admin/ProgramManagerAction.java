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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.BedCheckTime;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramAccess;
import org.oscarehr.PMmodule.model.ProgramClientStatus;
import org.oscarehr.PMmodule.model.ProgramFunctionalUser;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.model.ProgramTeam;
import org.oscarehr.PMmodule.web.BaseAction;

public class ProgramManagerAction extends BaseAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping, form, request, response);
	}

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("programs", programManager.getAllPrograms());

		logManager.log(getProviderNo(request), "read", "full program list", "", getIP(request));

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
			programForm.set("bedCheckTimes", bedCheckTimeManager.getBedCheckTimesByProgram(Integer.valueOf(id)));

			setEditAttributes(request, id);
		}

		return mapping.findForward("edit");
	}

	public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		programForm.set("program", new Program());

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

		logManager.log(getProviderNo(request), "write", "edit program - assign role", String.valueOf(program.getId()), getIP(request));
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

		logManager.log(getProviderNo(request), "write", "edit program - assign team", String.valueOf(program.getId()), getIP(request));
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

		logManager.log(getProviderNo(request), "write", "edit program - assign client to team", String.valueOf(program.getId()), getIP(request));

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

		int numQueue = programQueueManager.getProgramQueuesByProgramId(id).size();
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

		logManager.log(getProviderNo(request), "write", "delete program", String.valueOf(program.getId()), getIP(request));

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

		logManager.log(getProviderNo(request), "write", "edit program - delete access", String.valueOf(program.getId()), getIP(request));

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
		logManager.log(getProviderNo(request), "write", "edit program - delete function user", String.valueOf(program.getId()), getIP(request));

		this.setEditAttributes(request, String.valueOf(program.getId()));

		return edit(mapping, form, request, response);
	}

	public ActionForward delete_provider(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramProvider pp = (ProgramProvider) programForm.get("provider");

		if (pp.getId() != null && pp.getId().longValue() >= 0) {
			programManager.deleteProgramProvider(String.valueOf(pp.getId()));

			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
			saveMessages(request, messages);

			logManager.log(getProviderNo(request), "write", "edit program - delete provider", String.valueOf(program.getId()), getIP(request));
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

		logManager.log(getProviderNo(request), "write", "edit program - edit provider", String.valueOf(program.getId()), getIP(request));

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
		fullQueue.setStatus("removed");
		programQueueManager.saveProgramQueue(fullQueue);

		logManager.log(getProviderNo(request), "write", "edit program - queue removal", String.valueOf(program.getId()), getIP(request));

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

			for (Iterator iter = pp.getTeams().iterator(); iter.hasNext();) {
				ProgramTeam team = (ProgramTeam) iter.next();

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

		logManager.log(getProviderNo(request), "write", "edit program - assign team (removal)", String.valueOf(program.getId()), getIP(request));
		programForm.set("provider", new ProgramProvider());

		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");

		if (this.isCancelled(request)) {
			return list(mapping, form, request, response);
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
		
		//if a program has a client in it, you cannot make it inactive
		if(request.getParameter("program.programStatus").equals("inactive")) {
			//Admission ad = admissionManager.getAdmission(Long.valueOf(request.getParameter("id")));
			List admissions = admissionManager.getCurrentAdmissionsByProgramId(String.valueOf(program.getId()));
			if(admissions.size()>0){
				ActionMessages messages = new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.client_in_the_program", program.getName()));
				saveMessages(request, messages);
				setEditAttributes(request, String.valueOf(program.getId()));
				return mapping.findForward("edit");
			}	
			
			int numQueue = programQueueManager.getProgramQueuesByProgramId(String.valueOf(program.getId())).size();
			if (numQueue > 0) {
				ActionMessages messages = new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.client_in_the_queue", program.getName(), String.valueOf(numQueue)));
				saveMessages(request, messages);				
				setEditAttributes(request, String.valueOf(program.getId()));
				return mapping.findForward("edit");
			}
		}
				
				
		if (program.getMaxAllowed().intValue() < program.getNumOfMembers().intValue()) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.max_too_small", program.getName()));
			saveMessages(request, messages);
			setEditAttributes(request, String.valueOf(program.getId()));
			return mapping.findForward("edit");
		}

		if (!program.getType().equalsIgnoreCase("bed") && program.isHoldingTank()) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.invalid_holding_tank"));
			saveMessages(request, messages);
			setEditAttributes(request, String.valueOf(program.getId()));
			return mapping.findForward("edit");
		}

		programManager.saveProgram(program);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);

		logManager.log(getProviderNo(request), "write", "edit program", String.valueOf(program.getId()), getIP(request));

		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	public ActionForward save_access(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm) form;
		Program program = (Program) programForm.get("program");
		ProgramAccess access = (ProgramAccess) programForm.get("access");

		if (this.isCancelled(request)) {
			return list(mapping, form, request, response);
		}
		access.setProgramId(new Long(program.getId().longValue()));

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
			for (int x = 0; x < roles.length; x++) {
				access.getRoles().add(roleManager.getRole(roles[x]));
			}
		}

		programManager.saveProgramAccess(access);

		logManager.log(getProviderNo(request), "write", "access", String.valueOf(program.getId()), getIP(request));

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
		function.setProgramId(new Long(program.getId().longValue()));

		Long pid = programManager.getFunctionalUserByUserType(new Long(program.getId().longValue()), new Long(function.getUserTypeId()));

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

		logManager.log(getProviderNo(request), "write", "edit program - save function user", String.valueOf(program.getId()), getIP(request));

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
		provider.setProgramId(new Long(program.getId().longValue()));

		if (programManager.getProgramProvider(String.valueOf(provider.getProviderNo()), String.valueOf(program.getId())) != null) {
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

		logManager.log(getProviderNo(request), "write", "edit program - save provider", String.valueOf(program.getId()), getIP(request));
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

		logManager.log(getProviderNo(request), "write", "edit program - save team", String.valueOf(program.getId()), getIP(request));

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("program.saved", program.getName()));
		saveMessages(request, messages);
		programForm.set("team", new ProgramTeam());
		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}

	private void setEditAttributes(HttpServletRequest request, String programId) {
		request.setAttribute("id", programId);
		request.setAttribute("programName", programManager.getProgram(programId).getName());
		request.setAttribute("providers", programManager.getProgramProviders(programId));
		request.setAttribute("roles", roleManager.getRoles());
		request.setAttribute("functionalUserTypes", programManager.getFunctionalUserTypes());
		request.setAttribute("functional_users", programManager.getFunctionalUsers(programId));

		List teams = programManager.getProgramTeams(programId);

		for (Iterator i = teams.iterator(); i.hasNext();) {
			ProgramTeam team = (ProgramTeam) i.next();

			team.setProviders(programManager.getAllProvidersInTeam(Integer.valueOf(programId), team.getId()));
			team.setAdmissions(programManager.getAllClientsInTeam(Integer.valueOf(programId), team.getId()));
		}
		
		request.setAttribute("teams", teams);
		
		request.setAttribute("client_statuses", programManager.getProgramClientStatuses(new Integer(programId)));

		request.setAttribute("admissions", admissionManager.getCurrentAdmissionsByProgramId(programId));
		request.setAttribute("accesses", programManager.getProgramAccesses(programId));
		request.setAttribute("accessTypes", programManager.getAccessTypes());
		request.setAttribute("queue", programQueueManager.getProgramQueuesByProgramId(programId));
		
		request.setAttribute("bed_programs",programManager.getBedPrograms());
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

		logManager.log(getProviderNo(request), "write", "edit program - save status", String.valueOf(program.getId()), getIP(request));

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

		logManager.log(getProviderNo(request), "write", "edit program - assign client to status", String.valueOf(program.getId()), getIP(request));

		setEditAttributes(request, String.valueOf(program.getId()));

		return mapping.findForward("edit");
	}
	
}