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
import java.util.Collections;
import java.util.Comparator;
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
import org.oscarehr.PMmodule.dao.FacilityDAO;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Facility;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramTeam;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.web.BaseAction;
import org.oscarehr.PMmodule.web.formbean.StaffEditProgramContainer;
import org.oscarehr.PMmodule.web.formbean.StaffManagerViewFormBean;

public class StaffManagerAction extends BaseAction {
	private static Log log = LogFactory.getLog(StaffManagerAction.class);
	
	private FacilityDAO facilityDAO=null;
	
	public void setFacilityDAO(FacilityDAO facilityDAO) {
        this.facilityDAO = facilityDAO;
    }
	
    public ActionForward add_to_facility(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        
        int facilityId=Integer.parseInt(request.getParameter("facility_id"));
        int providerId=Integer.parseInt(request.getParameter("id"));

        ProviderDao.addProviderToFacility(providerId, facilityId);
        
        return edit(mapping,form,request,response);
    }
        
    public ActionForward remove_from_facility(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        
        int facilityId=Integer.parseInt(request.getParameter("facility_id"));
        int providerId=Integer.parseInt(request.getParameter("id"));

        ProviderDao.removeProviderFromFacility(providerId, facilityId);
        
        return edit(mapping,form,request,response);
    }
        
    public void setEditAttributes(HttpServletRequest request, Provider provider) {
		request.setAttribute("id",provider.getProviderNo());
		request.setAttribute("providerName",provider.getFormattedName());
		
		/* programs the provider is already a staff member of */
		List pp = programManager.getProgramProvidersByProvider(provider.getProviderNo());
		for(Iterator iter=pp.iterator();iter.hasNext();) {
			ProgramProvider p = (ProgramProvider)iter.next();
			String name = programManager.getProgramName(String.valueOf(p.getProgramId()));
			if(name == null) {
				log.warn("Program doesn't have a name?");
				name="";
			}
			p.setProgramName(name);
		}
		request.setAttribute("programs",sortProgramProviders(pp));
		
		List allPrograms = programManager.getProgramsByAgencyId("0");
		List allProgramsInContainer = new ArrayList();
		for(Iterator iter=allPrograms.iterator();iter.hasNext();) {
			Program p = (Program)iter.next();
			StaffEditProgramContainer container = new StaffEditProgramContainer(p,programManager.getProgramTeams(String.valueOf(p.getId())));
			allProgramsInContainer.add(container);
		}
		request.setAttribute("all_programs",allProgramsInContainer);
		request.setAttribute("roles",roleManager.getRoles());
		
		List<Facility> allFacilities=facilityDAO.getFacilities();
        request.setAttribute("all_facilities",allFacilities);
        
        List<Integer> providerFacilities=ProviderDao.getFacilityIds(Integer.parseInt(provider.getProvider_no()));
        request.setAttribute("providerFacilities",providerFacilities);
	}
	
	protected List sortProgramProviders(List pps) {
		Collections.sort(pps,new Comparator() {
			public int compare(Object o1, Object o2) {
				ProgramProvider pp1  = (ProgramProvider)o1;
				ProgramProvider pp2  = (ProgramProvider)o2;
				
				return pp1.getProgramName().compareTo(pp2.getProgramName());
			}
		}
		);
		return pps;
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping,form,request,response);
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm providerForm = (DynaActionForm)form;
		StaffManagerViewFormBean formBean = (StaffManagerViewFormBean)providerForm.get("view");
		
		//request.setAttribute("providers",providerManager.getProviders());
		//changed to get all active providers
		request.setAttribute("providers",providerManager.getActiveProviders());
		
		logManager.log("read","full provider list","",request);
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
		providerForm.set("program_provider",new ProgramProvider());
		return mapping.findForward("edit");
	}
	
	public ActionForward remove_team(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm providerForm = (DynaActionForm)form;
		Provider provider = (Provider)providerForm.get("provider");
		ProgramProvider pp = (ProgramProvider)providerForm.get("program_provider");
		ProgramProvider existingPP = null;
		
		existingPP = programManager.getProgramProvider(provider.getProviderNo(),String.valueOf(pp.getProgramId()));
		String teamId = request.getParameter("teamId");
		if (existingPP != null && teamId != null && teamId.length() > 0) {
			long team_id = Long.valueOf(teamId);
			for (Iterator iter = existingPP.getTeams().iterator(); iter.hasNext();) {
				ProgramTeam temp = (ProgramTeam) iter.next();
				if (temp.getId() == team_id) {
					existingPP.getTeams().remove(temp);
					break;
				}
			}
			programManager.saveProgramProvider(existingPP);
		}
		
		setEditAttributes(request,providerManager.getProvider(provider.getProviderNo()));
		providerForm.set("program_provider",new ProgramProvider());
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
			pp.setProviderNo(provider.getProviderNo());
			programManager.saveProgramProvider(pp);
		}
		
		setEditAttributes(request,providerManager.getProvider(provider.getProviderNo()));
		providerForm.set("program_provider",new ProgramProvider());
		return mapping.findForward("edit");
	}

	public ActionForward remove_entry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm providerForm = (DynaActionForm)form;
		Provider provider = (Provider)providerForm.get("provider");
		ProgramProvider pp = (ProgramProvider)providerForm.get("program_provider");
		
		programManager.deleteProgramProvider(String.valueOf(pp.getId()));
		
		setEditAttributes(request,providerManager.getProvider(provider.getProviderNo()));
		providerForm.set("program_provider",new ProgramProvider());
		return mapping.findForward("edit");
	}
}
