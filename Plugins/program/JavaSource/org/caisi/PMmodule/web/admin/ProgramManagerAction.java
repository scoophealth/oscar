package org.caisi.PMmodule.web.admin;

import java.util.HashSet;
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
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.caisi.PMmodule.model.Admission;
import org.caisi.PMmodule.model.Program;
import org.caisi.PMmodule.model.ProgramAccess;
import org.caisi.PMmodule.model.ProgramBedLog;
import org.caisi.PMmodule.model.ProgramFunctionalUser;
import org.caisi.PMmodule.model.ProgramProvider;
import org.caisi.PMmodule.model.ProgramQueue;
import org.caisi.PMmodule.model.ProgramTeam;
import org.caisi.PMmodule.model.Provider;
import org.caisi.PMmodule.service.AdmissionManager;
import org.caisi.PMmodule.service.LogManager;
import org.caisi.PMmodule.service.ProgramManager;
import org.caisi.PMmodule.service.ProgramQueueManager;
import org.caisi.PMmodule.service.RoleManager;
import org.caisi.PMmodule.web.formbean.ProgramManagerViewFormBean;


public class ProgramManagerAction extends DispatchAction {
	private static Log log = LogFactory.getLog(ProgramManagerAction.class);

	private ProgramManager programManager;
	private AdmissionManager admissionManager;
	private RoleManager roleManager;
	private ProgramQueueManager programQueueManager;
	private LogManager logManager;
	
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}

	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}
	
	public void setRoleManager(RoleManager mgr) {
		this.roleManager = mgr;
	}
	
	public void setProgramQueueManager(ProgramQueueManager mgr) {
		this.programQueueManager = mgr;
	}
	
	public void setLogManager(LogManager mgr) {
		this.logManager = mgr;
	}
	
	public String getProviderNo(HttpServletRequest request) {
		Provider p =  (Provider)request.getSession().getAttribute("provider");
		return p.getProviderNo();
	}
	
	public String getIP(HttpServletRequest request) {
		return request.getRemoteAddr();
	}
	
	public void setEditAttributes(HttpServletRequest request, String programId) {
		request.setAttribute("id",programId);
		request.setAttribute("programName",programManager.getProgram(programId).getName());
		request.setAttribute("providers",programManager.getProgramProviders(programId));
		request.setAttribute("roles",roleManager.getRoles());
		request.setAttribute("functionalUserTypes",programManager.getFunctionalUserTypes());
		request.setAttribute("functional_users",programManager.getFunctionalUsers(programId));
		List teams = programManager.getProgramTeams(programId);
		request.setAttribute("teams",teams);
		for(Iterator iter=teams.iterator();iter.hasNext();) {
			ProgramTeam team = (ProgramTeam)iter.next();
			team.setProviders(  programManager.getAllProvidersInTeam(Long.valueOf(programId),team.getId()));
			team.setAdmissions(programManager.getAllClientsInTeam(Long.valueOf(programId),team.getId()));
		}
		request.setAttribute("admissions",admissionManager.getCurrentAdmissionsByProgramId(programId));
		request.setAttribute("accesses",programManager.getProgramAccesses(programId));
		request.setAttribute("accessTypes",programManager.getAccessTypes());
		request.setAttribute("queue",programQueueManager.getProgramQueuesByProgramId(programId));
		
		request.setAttribute("bedlog_statuses",programManager.getBedLogStatuses(programId));
		request.setAttribute("bedlog_checktimes",programManager.getBedLogCheckTimes(programId));
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping,form,request,response);
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("programs",programManager.getAllPrograms());
		logManager.log(getProviderNo(request),"read","full program list","",getIP(request));
		return mapping.findForward("list");
	}
	
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		
		if(id == null) {
			return list(mapping,form,request,response);
		}
		
		/* have to make sure
		 * 1) no clients
		 * 2) no queue
		 */
		Program program = programManager.getProgram(id);
		if(program == null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.missing",name));
			saveMessages(request,messages);			
			return list(mapping,form,request,response);
		}
		
		int numAdmissions = admissionManager.getCurrentAdmissionsByProgramId(id).size();
		if(numAdmissions > 0) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.delete.admission",name,String.valueOf(numAdmissions)));
			saveMessages(request,messages);			
			return list(mapping,form,request,response);			
		}
		
		int numQueue = programQueueManager.getProgramQueuesByProgramId(id).size();
		if(numQueue > 0) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.delete.queue",name,String.valueOf(numQueue)));
			saveMessages(request,messages);			
			return list(mapping,form,request,response);			
		}		
		
		programManager.removeProgram(id);
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.deleted",name));
		saveMessages(request,messages);

		logManager.log(getProviderNo(request),"write","delete program",String.valueOf(program.getId()),getIP(request));
		return list(mapping,form,request,response);
	}
	
	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		String id = request.getParameter("id");
		
		if(this.isCancelled(request)) {
			return list(mapping,form,request,response);
		}
		
		if(id != null) {
			Program program = programManager.getProgram(id);

            if(program == null) {
            	ActionMessages messages = new ActionMessages();
    			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.missing"));
    			saveMessages(request,messages);

                return list(mapping,form,request,response);
            }
            programForm.set("program",program);
            programForm.set("bedlog",programManager.getBedLogByProgramId(program.getId().longValue()));
            request.setAttribute("id",id);
            setEditAttributes(request,id);
		}
		
		return mapping.findForward("edit");
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		
		if(this.isCancelled(request)) {
			return list(mapping,form,request,response);
		}
		
		if(program.getMaxAllowed().intValue() < program.getNumOfMembers().intValue()) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.max_too_small",program.getName()));
			saveMessages(request,messages);	
			setEditAttributes(request,String.valueOf(program.getId()));
			return mapping.findForward("edit");
		}
		
		if(!program.getType().equalsIgnoreCase("bed") && program.isHoldingTank()) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.invalid_holding_tank"));
			saveMessages(request,messages);	
			setEditAttributes(request,String.valueOf(program.getId()));
			return mapping.findForward("edit");
		}
		
		programManager.saveProgram(program);
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);
		
		logManager.log(getProviderNo(request),"write","edit program",String.valueOf(program.getId()),getIP(request));
		
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	public ActionForward save_provider(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramProvider provider = (ProgramProvider)programForm.get("provider");
		
		if(this.isCancelled(request)) {
			return list(mapping,form,request,response);
		}
		provider.setProgramId(new Long(program.getId().longValue()));
		//provider.setTeamId(new Long(0));
		
		if(programManager.getProgramProvider(String.valueOf(provider.getProviderNo()),String.valueOf(program.getId())) != null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.provider.exists"));
			saveMessages(request,messages);
			programForm.set("provider",new ProgramProvider());
			setEditAttributes(request,String.valueOf(program.getId()));
			return mapping.findForward("edit");
		}
		
		programManager.saveProgramProvider(provider);
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);
		
		logManager.log(getProviderNo(request),"write","edit program - save provider",String.valueOf(program.getId()),getIP(request));
		programForm.set("provider",new ProgramProvider());
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	public ActionForward assign_team(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramProvider provider = (ProgramProvider)programForm.get("provider");
		
		ProgramProvider pp = programManager.getProgramProvider(String.valueOf(provider.getId()));
		
		//pp.setTeamId(provider.getTeamId());
		ProgramTeam team  = programManager.getProgramTeam(request.getParameter("teamId"));
		if(team != null) {
			pp.getTeams().add(team);
			programManager.saveProgramProvider(pp);	
		}
		
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);
		
		logManager.log(getProviderNo(request),"write","edit program - assign team",String.valueOf(program.getId()),getIP(request));
		programForm.set("provider",new ProgramProvider());
		
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	public ActionForward remove_team(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramProvider provider = (ProgramProvider)programForm.get("provider");
		
		ProgramProvider pp = programManager.getProgramProvider(String.valueOf(provider.getId()));
		
		String teamId = request.getParameter("teamId");
		if(teamId != null && teamId.length() > 0) {
			long team_id = Long.valueOf(teamId).longValue();
			for(Iterator iter=pp.getTeams().iterator();iter.hasNext();) {
				ProgramTeam temp = (ProgramTeam)iter.next();
				if(temp.getId().longValue() == team_id) {
					pp.getTeams().remove(temp);
					break;
				}
			}
			programManager.saveProgramProvider(pp);	
		}
		
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);
		
		logManager.log(getProviderNo(request),"write","edit program - assign team (removal)",String.valueOf(program.getId()),getIP(request));
		programForm.set("provider",new ProgramProvider());
		
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	public ActionForward assign_role(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramProvider provider = (ProgramProvider)programForm.get("provider");
		
		ProgramProvider pp = programManager.getProgramProvider(String.valueOf(provider.getId()));
		
		pp.setRoleId(provider.getRoleId());
		
		programManager.saveProgramProvider(pp);
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);
		
		logManager.log(getProviderNo(request),"write","edit program - assign role",String.valueOf(program.getId()),getIP(request));
		programForm.set("provider",new ProgramProvider());
		
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	public ActionForward assign_team_client(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		Admission admission = (Admission)programForm.get("admission");
		
		Admission ad = admissionManager.getAdmission(admission.getId());
		
		ad.setTeamId(admission.getTeamId());
		
		admissionManager.saveAdmission(ad);
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);
		
		logManager.log(getProviderNo(request),"write","edit program - assign client to team",String.valueOf(program.getId()),getIP(request));
		
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	public ActionForward delete_provider(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramProvider pp = (ProgramProvider)programForm.get("provider");
	
		if(pp.getId() != null && pp.getId().longValue() >= 0) {
			programManager.deleteProgramProvider(String.valueOf(pp.getId()));
			
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
			saveMessages(request,messages);
			
			logManager.log(getProviderNo(request),"write","edit program - delete provider",String.valueOf(program.getId()),getIP(request));
		}	
		this.setEditAttributes(request,String.valueOf(program.getId()));
		programForm.set("provider",new ProgramProvider());
		
		return edit(mapping,form,request,response);
	}

	public ActionForward edit_provider(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramProvider provider = (ProgramProvider)programForm.get("provider");
		
		ProgramProvider pp = programManager.getProgramProvider(String.valueOf(provider.getId()));

        if(pp == null) {
        	ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program_provider.missing"));
			saveMessages(request,messages);
			setEditAttributes(request,String.valueOf(program.getId()));
			return edit(mapping,form,request,response);
        }
        programForm.set("provider",pp);
        request.setAttribute("providerName",pp.getProvider().getFormattedName());
  
        logManager.log(getProviderNo(request),"write","edit program - edit provider",String.valueOf(program.getId()),getIP(request));
		
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	
	public ActionForward save_function(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramFunctionalUser function = (ProgramFunctionalUser)programForm.get("function");
		
		if(this.isCancelled(request)) {
			return list(mapping,form,request,response);
		}
		function.setProgramId(new Long(program.getId().longValue()));
		
		Long pid = programManager.getFunctionalUserByUserType(new Long(program.getId().longValue()),new Long(function.getUserTypeId()));
		
		if(pid != null && function.getId().longValue() != pid.longValue()) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program_function.duplicate",program.getName()));
			saveMessages(request,messages);
			programForm.set("function",new ProgramFunctionalUser());
			setEditAttributes(request,String.valueOf(program.getId()));
			return mapping.findForward("edit");
		}
		programManager.saveFunctionalUser(function);
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);
		
		logManager.log(getProviderNo(request),"write","edit program - save function user",String.valueOf(program.getId()),getIP(request));
		
		programForm.set("function",new ProgramFunctionalUser());
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	public ActionForward delete_function(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramFunctionalUser function = (ProgramFunctionalUser)programForm.get("function");
		
		programManager.deleteFunctionalUser(String.valueOf(function.getId()));
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);
		logManager.log(getProviderNo(request),"write","edit program - delete function user",String.valueOf(program.getId()),getIP(request));
		
		this.setEditAttributes(request,String.valueOf(program.getId()));
		return edit(mapping,form,request,response);
	}

	public ActionForward edit_function(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramFunctionalUser function = (ProgramFunctionalUser)programForm.get("function");
		
		ProgramFunctionalUser pfu = programManager.getFunctionalUser(String.valueOf(function.getId()));

        if(pfu == null) {
        	ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program_function.missing"));
			saveMessages(request,messages);
			setEditAttributes(request,String.valueOf(program.getId()));
			return edit(mapping,form,request,response);
        }
        programForm.set("function",pfu);
        request.setAttribute("providerName",pfu.getProvider().getFormattedName());
  
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	
	public ActionForward save_team(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramTeam team = (ProgramTeam)programForm.get("team");
		
		if(this.isCancelled(request)) {
			return list(mapping,form,request,response);
		}
		team.setProgramId(new Long(program.getId().longValue()));
		
		if(programManager.teamNameExists(new Long(program.getId().longValue()),team.getName())) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program_team.duplicate",team.getName()));
			saveMessages(request,messages);
			programForm.set("team",new ProgramTeam());
			setEditAttributes(request,String.valueOf(program.getId()));
			return mapping.findForward("edit");
		}
		
		programManager.saveProgramTeam(team);
		
		logManager.log(getProviderNo(request),"write","edit program - save team",String.valueOf(program.getId()),getIP(request));
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);
		programForm.set("team",new ProgramTeam());
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	public ActionForward delete_team(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramTeam team = (ProgramTeam)programForm.get("team");
		
		if(programManager.getAllProvidersInTeam(new Long(program.getId().longValue()),team.getId()).size() > 0
				|| programManager.getAllClientsInTeam(new Long(program.getId().longValue()),team.getId()).size() > 0) {
			
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.team.not_empty",program.getName()));
			saveMessages(request,messages);
			
			this.setEditAttributes(request,String.valueOf(program.getId()));
			return edit(mapping,form,request,response);
		}
		
		programManager.deleteProgramTeam(String.valueOf(team.getId()));
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);
		
		this.setEditAttributes(request,String.valueOf(program.getId()));
		programForm.set("function",new ProgramFunctionalUser());
		return edit(mapping,form,request,response);
	}

	public ActionForward edit_team(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramTeam team = (ProgramTeam)programForm.get("team");
		
		ProgramTeam pt = programManager.getProgramTeam(String.valueOf(team.getId()));

        if(pt == null) {
        	ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program_team.missing"));
			saveMessages(request,messages);
			//programForm.set("team",new ProgramTeam());
			setEditAttributes(request,String.valueOf(program.getId()));
			return edit(mapping,form,request,response);
        }
        programForm.set("team",pt);
        //programForm.set("team",new ProgramTeam());
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	
	public ActionForward save_access(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramAccess access = (ProgramAccess)programForm.get("access");
		
		if(this.isCancelled(request)) {
			return list(mapping,form,request,response);
		}
		access.setProgramId(new Long(program.getId().longValue()));
		
		if(programManager.getProgramAccess(String.valueOf(access.getProgramId()),access.getAccessTypeId()) != null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.duplicate_access",program.getName()));
			saveMessages(request,messages);
			programForm.set("access",new ProgramAccess());
			setEditAttributes(request,String.valueOf(program.getId()));
			return mapping.findForward("edit");	
		}
		
		String roles[] = request.getParameterValues("checked_role");
		if(roles != null) {
			if(access.getRoles() == null) {
				access.setRoles(new HashSet());
			}
			for(int x=0;x<roles.length;x++) {
				access.getRoles().add(roleManager.getRole(roles[x]));
			}
		}

		programManager.saveProgramAccess(access);
		
		logManager.log(getProviderNo(request),"write","access",String.valueOf(program.getId()),getIP(request));
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);
		programForm.set("access",new ProgramAccess());
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	public ActionForward delete_access(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramAccess access = (ProgramAccess)programForm.get("access");
		
		programManager.deleteProgramAccess(String.valueOf(access.getId()));
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);

		logManager.log(getProviderNo(request),"write","edit program - delete access",String.valueOf(program.getId()),getIP(request));		
		
		this.setEditAttributes(request,String.valueOf(program.getId()));
		programForm.set("access",new ProgramAccess());
		return edit(mapping,form,request,response);
	}

	public ActionForward edit_access(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramAccess access = (ProgramAccess)programForm.get("access");
		
		ProgramAccess pa = programManager.getProgramAccess(String.valueOf(access.getId()));

        if(pa == null) {
        	ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program_access.missing"));
			saveMessages(request,messages);
			setEditAttributes(request,String.valueOf(program.getId()));
			return edit(mapping,form,request,response);
        }
        programForm.set("access",pa);
      
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}

	public ActionForward remove_queue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramQueue queue = (ProgramQueue)programForm.get("queue");
		
		ProgramQueue fullQueue = programQueueManager.getProgramQueue(String.valueOf(queue.getId()));
		fullQueue.setStatus("removed");
		programQueueManager.saveProgramQueue(fullQueue);
		//programQueueManager.removeProgramQueue(String.valueOf(queue.getId()));

		logManager.log(getProviderNo(request),"write","edit program - queue removal",String.valueOf(program.getId()),getIP(request));
		
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	public ActionForward save_bedlog(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramBedLog bedlog = (ProgramBedLog)programForm.get("bedlog");
		bedlog.setProgramId(new Long(program.getId().longValue()));
		programManager.saveBedLog(bedlog);
		
       	ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);
		
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	public ActionForward bedlog_add_checktime(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramBedLog bedlog = (ProgramBedLog)programForm.get("bedlog");
		ProgramManagerViewFormBean formBean = (ProgramManagerViewFormBean)programForm.get("view");
      	
		String hour = formBean.getBedLogHour();
		String minute = formBean.getBedLogMinute();
		String ampm = formBean.getBedLogAmPm();
		
		String time = hour + ":" + minute + " " + ampm;
		
		bedlog.addToCheckTimes(time);
		programManager.saveBedLog(bedlog);
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);

		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	public ActionForward bedlog_remove_checktime(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramBedLog bedlog = (ProgramBedLog)programForm.get("bedlog");
		ProgramManagerViewFormBean formBean = (ProgramManagerViewFormBean)programForm.get("view");
      	
		String time = formBean.getBedLogTime();
		
		bedlog.getCheckTimes().remove(time);
		
		programManager.saveBedLog(bedlog);
		formBean.setBedLogTime("");
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);

		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	public ActionForward bedlog_add_status(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramBedLog bedlog = (ProgramBedLog)programForm.get("bedlog");
		ProgramManagerViewFormBean formBean = (ProgramManagerViewFormBean)programForm.get("view");
      	
		bedlog.addToStatuses(formBean.getBedLogStatus());
		
		programManager.saveBedLog(bedlog);
		
		formBean.setBedLogStatus("");
      	ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);

		
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
	
	public ActionForward bedlog_remove_status(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm programForm = (DynaActionForm)form;
		Program program = (Program)programForm.get("program");
		ProgramBedLog bedlog = (ProgramBedLog)programForm.get("bedlog");
		ProgramManagerViewFormBean formBean = (ProgramManagerViewFormBean)programForm.get("view");
      	
		System.out.println("removing " + formBean.getBedLogStatus());
		
		bedlog.getStatuses().remove(formBean.getBedLogStatus());
		
		programManager.saveBedLog(bedlog);
		
		formBean.setBedLogStatus("");
      	ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",program.getName()));
		saveMessages(request,messages);

		
		setEditAttributes(request,String.valueOf(program.getId()));
		return mapping.findForward("edit");
	}
}
