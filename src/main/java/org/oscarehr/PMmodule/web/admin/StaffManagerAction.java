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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramTeam;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.web.formbean.StaffEditProgramContainer;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.dao.SecRoleDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.log.LogAction;

public class StaffManagerAction extends DispatchAction {
	private static Logger log = MiscUtils.getLogger();
	
	private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
    

	private FacilityDao facilityDao=null;

    private ProgramManager programManager;

    private ProviderManager providerManager;

    private SecRoleDao secRoleDao;


    public void setSecRoleDao(SecRoleDao secRoleDao) {
    	this.secRoleDao = secRoleDao;
    }


	public void setFacilityDao(FacilityDao facilityDao) {
        this.facilityDao = facilityDao;
    }

    public ActionForward add_to_facility(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        int facilityId=Integer.parseInt(request.getParameter("facility_id"));
        String providerId=request.getParameter("id");

        ProviderDao.addProviderToFacility(providerId, facilityId);

        return edit(mapping,form,request,response);
    }

    public ActionForward remove_from_facility(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        int facilityId=Integer.parseInt(request.getParameter("facility_id"));
        String providerId=request.getParameter("id");

        ProviderDao.removeProviderFromFacility(providerId, facilityId);

        return edit(mapping,form,request,response);
    }

    public void setEditAttributes(HttpServletRequest request, Provider provider) {
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
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

		List<Program> allPrograms = programManager.getCommunityPrograms(loggedInInfo.getCurrentFacility().getId());
		List<StaffEditProgramContainer> allProgramsInContainer = new ArrayList<StaffEditProgramContainer>();
		for(Program p : allPrograms) {
			StaffEditProgramContainer container = new StaffEditProgramContainer(p,programManager.getProgramTeams(String.valueOf(p.getId())));
			allProgramsInContainer.add(container);
		}
		request.setAttribute("all_programs",allProgramsInContainer);
	//	request.setAttribute("roles",roleManager.getRoles());
		request.setAttribute("roles", secRoleDao.findAll());

		List<Facility> allFacilities=facilityDao.findAll(true);
        request.setAttribute("all_facilities",allFacilities);

        List<Integer> providerFacilities=providerDao.getFacilityIds(provider.getProviderNo());
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
		//DynaActionForm providerForm = (DynaActionForm)form;
		//StaffManagerViewFormBean formBean = (StaffManagerViewFormBean)providerForm.get("view");

		//request.setAttribute("providers",providerManager.getProviders());
		//changed to get all active providers
		request.setAttribute("providers",providerManager.getActiveProviders());

        request.setAttribute("facilities",facilityDao.findAll(true));

        //show programs which can be assigned to the provider
        request.setAttribute("programs",programManager.getAllPrograms("Any", "Any", 0));

		LogAction.log("read","full provider list","",request);
		return mapping.findForward("list");
	}

	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm providerForm = (DynaActionForm)form;

		String facilityId = (String)providerForm.get("facilityId");
		if(facilityId==null) facilityId="0";
		String programId = (String)providerForm.get("programId");
		if(programId==null) programId="0";
        if(facilityId.equals("0")){
        	providerForm.set("programId", "0");
        	programId="0";
        }

		request.setAttribute("facilities",facilityDao.findAll(true));
        if(facilityId.equals("0")==false) request.setAttribute("programs",programManager.getAllPrograms("Any", "Any", Integer.valueOf(facilityId)));

		request.setAttribute("providers",providerManager.getActiveProviders(facilityId, programId));

		LogAction.log("read","full provider list","",request);
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
			programManager.saveProgramProvider(LoggedInInfo.getLoggedInInfoFromSession(request),existingPP);
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
			programManager.saveProgramProvider(LoggedInInfo.getLoggedInInfoFromSession(request),existingPP);
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
				programManager.saveProgramProvider(LoggedInInfo.getLoggedInInfoFromSession(request),existingPP);
			}
		} else {
			pp.setProviderNo(provider.getProviderNo());
			programManager.saveProgramProvider(LoggedInInfo.getLoggedInInfoFromSession(request),pp);
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

    public void setProgramManager(ProgramManager mgr) {
    	this.programManager = mgr;
    }

    public void setProviderManager(ProviderManager mgr) {
    	this.providerManager = mgr;
    }

}
