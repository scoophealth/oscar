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
import org.caisi.PMmodule.model.Program;
import org.caisi.PMmodule.model.ProgramProvider;
import org.caisi.PMmodule.model.ProgramTeam;
import org.caisi.PMmodule.model.Provider;
import org.caisi.PMmodule.service.LogManager;
import org.caisi.PMmodule.service.ProgramManager;
import org.caisi.PMmodule.service.ProviderManager;
import org.caisi.PMmodule.service.RoleManager;

public class StaffManagerAction extends DispatchAction {
	private static Log log = LogFactory.getLog(StaffManagerAction.class);

	private ProviderManager providerManager;
	private ProgramManager programManager;
	private LogManager logManager;
	private RoleManager roleManager;
	
	public void setRoleManager(RoleManager mgr) {
		this.roleManager = mgr;
	}
	public void setProviderManager(ProviderManager mgr) {
		this.providerManager = mgr;
	}
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
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
	
	public void setEditAttributes(HttpServletRequest request, Provider provider) {
		request.setAttribute("id",provider.getProviderNo());
		request.setAttribute("providerName",provider.getFormattedName());
		
		List pp = programManager.getProgramProvidersByProvider(provider.getProviderNo());
		for(Iterator iter=pp.iterator();iter.hasNext();) {
			ProgramProvider p = (ProgramProvider)iter.next();
			p.setProgramName(programManager.getProgramName(String.valueOf(p.getProgramId())));
		}
		request.setAttribute("programs",pp);
		
		List allPrograms = programManager.getProgramsByAgencyId("0");
		for(Iterator iter=allPrograms.iterator();iter.hasNext();) {
			Program p = (Program)iter.next();
			p.setTeamList(programManager.getProgramTeams(String.valueOf(p.getId())));
		}
		request.setAttribute("all_programs",allPrograms);
		request.setAttribute("roles",roleManager.getRoles());
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping,form,request,response);
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("providers",providerManager.getProviders());
		logManager.log(getProviderNo(request),"read","full provider list","",getIP(request));
		return mapping.findForward("list");
	}
	
	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm providerForm = (DynaActionForm)form;
		String id = request.getParameter("id");
		
		if(this.isCancelled(request)) {
			return list(mapping,form,request,response);
		}
		
		if(id != null) {
			Provider provider = providerManager.getProvider(id);

            if(provider == null) {
            	ActionMessages messages = new ActionMessages();
    			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("provider.missing"));
    			saveMessages(request,messages);

                return list(mapping,form,request,response);
            }
            providerForm.set("provider",provider);
            setEditAttributes(request,provider);
		}
		
		return mapping.findForward("edit");
	}
	
	public ActionForward assign_team(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm providerForm = (DynaActionForm)form;
		Provider provider = (Provider)providerForm.get("provider");
		ProgramProvider pp = (ProgramProvider)providerForm.get("program_provider");
		ProgramProvider existingPP = null;
		
		existingPP = programManager.getProgramProvider(provider.getProviderNo(),String.valueOf(pp.getProgramId()));
		String teamId = request.getParameter("teamId");
		ProgramTeam team = programManager.getProgramTeam(teamId);
		if(existingPP != null && team != null) {
			existingPP.getTeams().add(team);
			programManager.saveProgramProvider(existingPP);
		}
		
		setEditAttributes(request,providerManager.getProvider(provider.getProviderNo()));
		return mapping.findForward("edit");
	}
	
	public ActionForward remove_team(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm providerForm = (DynaActionForm)form;
		Provider provider = (Provider)providerForm.get("provider");
		ProgramProvider pp = (ProgramProvider)providerForm.get("program_provider");
		ProgramProvider existingPP = null;
		
		existingPP = programManager.getProgramProvider(provider.getProviderNo(),String.valueOf(pp.getProgramId()));
		String teamId = request.getParameter("teamId");
		if(existingPP != null && teamId != null && teamId.length()>0) {
			long team_id = Long.valueOf(teamId).longValue();
			for(Iterator iter=existingPP.getTeams().iterator();iter.hasNext();) {
				ProgramTeam temp = (ProgramTeam)iter.next();
				if(temp.getId().longValue() == team_id) {
					existingPP.getTeams().remove(temp);
					break;
				}
			}
			programManager.saveProgramProvider(existingPP);
		}
		
		setEditAttributes(request,providerManager.getProvider(provider.getProviderNo()));
		return mapping.findForward("edit");
	}
	
	public ActionForward assign_role(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm providerForm = (DynaActionForm)form;
		Provider provider = (Provider)providerForm.get("provider");
		ProgramProvider pp = (ProgramProvider)providerForm.get("program_provider");
		ProgramProvider existingPP = null;
		
		if( (existingPP = programManager.getProgramProvider(provider.getProviderNo(),String.valueOf(pp.getProgramId())) ) != null) {
			if(pp.getRoleId().longValue() == 0) {
				programManager.deleteProgramProvider(String.valueOf(existingPP.getId()));
			} else {
				existingPP.setRoleId(pp.getRoleId());
				programManager.saveProgramProvider(existingPP);
			}
		} else {
			pp.setProviderNo(Long.valueOf(provider.getProviderNo()));
			programManager.saveProgramProvider(pp);
		}
		
		setEditAttributes(request,providerManager.getProvider(provider.getProviderNo()));
		return mapping.findForward("edit");
	}

}
