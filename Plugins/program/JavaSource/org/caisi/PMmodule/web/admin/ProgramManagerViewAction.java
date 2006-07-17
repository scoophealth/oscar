package org.caisi.PMmodule.web.admin;

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
import org.caisi.PMmodule.exception.ProgramFullException;
import org.caisi.PMmodule.model.Admission;
import org.caisi.PMmodule.model.Program;
import org.caisi.PMmodule.model.ProgramTeam;
import org.caisi.PMmodule.model.Provider;
import org.caisi.PMmodule.service.AdmissionManager;
import org.caisi.PMmodule.service.LogManager;
import org.caisi.PMmodule.service.ProgramManager;
import org.caisi.PMmodule.service.ProgramQueueManager;

public class ProgramManagerViewAction extends DispatchAction {
	private static Log log = LogFactory.getLog(ProgramManagerViewAction.class);

	private ProgramManager programManager;
	private AdmissionManager admissionManager;
	private ProgramQueueManager programQueueManager;
	private LogManager logManager;
	
	public String getProviderNo(HttpServletRequest request) {
		Provider p =  (Provider)request.getSession().getAttribute("provider");
		return p.getProviderNo();
	}
	
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}
	
	
	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}
	
	public void setProgramQueueManager(ProgramQueueManager mgr) {
		this.programQueueManager = mgr;
	}
	
	public void setLogManager(LogManager mgr) {
		this.logManager = mgr;
	}
	
	public String getIP(HttpServletRequest request) {
		return request.getRemoteAddr();
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return view(mapping,form,request,response);
	}

	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		if(id == null) {
			id = (String)request.getAttribute("id");
		}
		if(id == null) {
			//return list(mapping,form,request,response);
		}
		if(request.getParameter("clientId") != null) {
			request.setAttribute("clientId",request.getParameter("clientId"));
		}
		Program program = programManager.getProgram(id); 
		request.setAttribute("program",program);
		request.setAttribute("program_name",program.getName());
		request.setAttribute("agency",programManager.getAgencyByProgram(id));
		
		request.setAttribute("providers",programManager.getProgramProviders(id));
		request.setAttribute("functional_users",programManager.getFunctionalUsers(id));
		request.setAttribute("teams",programManager.getProgramTeams(id));
		request.setAttribute("admissions",admissionManager.getCurrentAdmissionsByProgramId(id));
		request.setAttribute("accesses",programManager.getProgramAccesses(id));
		
		request.setAttribute("queue",programQueueManager.getActiveProgramQueuesByProgramId(id));
		
		List teams = programManager.getProgramTeams(id);
		request.setAttribute("teams",teams);
		for(Iterator iter=teams.iterator();iter.hasNext();) {
			ProgramTeam team = (ProgramTeam)iter.next();
			team.setProviders(  programManager.getAllProvidersInTeam(Long.valueOf(id),team.getId()));
			team.setAdmissions(programManager.getAllClientsInTeam(Long.valueOf(id),team.getId()));
		}
	
		request.setAttribute("bedlog",programManager.getBedLogByProgramId(Long.valueOf(id).longValue()));
		
		logManager.log(getProviderNo(request),"view","program",id,request.getRemoteAddr());
		
		request.setAttribute("id",id);
		return mapping.findForward("view");
	}

	public ActionForward select_client_for_admit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String programId = request.getParameter("id");
		String clientId = request.getParameter("clientId");
		
		Program program = programManager.getProgram(String.valueOf(programId));
		/*
		 * If the user is currently enrolled in a bed program,
		 * we must warn the provider that this will also be a discharge
		 */
		if(program.getType().equalsIgnoreCase("bed")) {
			Admission currentAdmission = admissionManager.getCurrentBedProgramAdmission(programManager,clientId);
			if(currentAdmission != null) {
				log.warn("client already in a bed program..doing a discharge/admit if proceeding");
				request.setAttribute("current_admission",currentAdmission);
				request.setAttribute("current_program",programManager.getProgram(String.valueOf(currentAdmission.getProgramId())));
			}
		}
		request.setAttribute("do_admit",new Boolean(true));
		
		return view(mapping,form,request,response);
	}
	
	public ActionForward reject_from_queue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String programId = request.getParameter("id");
		String clientId = request.getParameter("clientId");
		
		log.debug("rejecting from queue: program_id=" + programId + ",clientId=" + clientId);

		programQueueManager.rejectQueue(programId,clientId);
		
		return view(mapping,form,request,response);
	}
	
	public ActionForward admit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String programId = request.getParameter("id");
		String clientId = request.getParameter("clientId");
		
		Program fullProgram = programManager.getProgram(String.valueOf(programId));

		try {
			admissionManager.processAdmission(clientId,getProviderNo(request),fullProgram,programManager,request.getParameter("admission.dischargeNotes"),request.getParameter("admission.admissionNotes"));
		}catch(ProgramFullException e) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("admit.full"));
			saveMessages(request,messages);									
		}
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("admit.success"));
		saveMessages(request,messages);			
		
		logManager.log(getProviderNo(request),"view","admit to program",clientId,request.getRemoteAddr());
		
		
		return view(mapping,form,request,response);
	}
	
	public ActionForward assign_team_client(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String admissionId = request.getParameter("admissionId");
		String teamId = request.getParameter("teamId");
		String programName = request.getParameter("program_name");
		Admission ad = admissionManager.getAdmission(Long.valueOf(admissionId));
		
		ad.setTeamId(Long.valueOf(teamId));
		
		admissionManager.saveAdmission(ad);
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("program.saved",programName));
		saveMessages(request,messages);
		
		logManager.log(getProviderNo(request),"write","edit program - assign client to team","",getIP(request));
		
		return view(mapping,form,request,response);
	}
}
