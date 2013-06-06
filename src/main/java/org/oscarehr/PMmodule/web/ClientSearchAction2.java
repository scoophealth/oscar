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

package org.oscarehr.PMmodule.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.util.SpringUtils;

import oscar.log.LogAction;

import com.quatro.service.LookupManager;

public class ClientSearchAction2 extends DispatchAction {
	
	private LookupManager lookupManager;
    private ClientManager clientManager;
    private ProgramManager programManager;

    private CaseManagementNoteDAO caseManagementNoteDao = (CaseManagementNoteDAO) SpringUtils.getBean("caseManagementNoteDAO");

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return form(mapping,form,request,response);
	}
	
	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if(clientManager.isOutsideOfDomainEnabled()){ 
			request.getSession().setAttribute("outsideOfDomainEnabled","true");
		}else{
			request.getSession().setAttribute("outsideOfDomainEnabled","false");
		}
		
		List<Program> allBedPrograms = new ArrayList<Program>();
		Program[] allBedProgramsInArr = programManager.getBedPrograms();

		for(int i=0; i < allBedProgramsInArr.length; i++){
			allBedPrograms.add(allBedProgramsInArr[i]);
		}
		request.setAttribute("allBedPrograms", allBedPrograms);
		
		request.setAttribute("genders",lookupManager.LoadCodeList("GEN", true, null, null));
		
		return mapping.findForward("form");
	}

    public ActionForward attachForm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if(clientManager.isOutsideOfDomainEnabled()){
            request.getSession().setAttribute("outsideOfDomainEnabled","true");
        }else{
            request.getSession().setAttribute("outsideOfDomainEnabled","false");
        }

        List<Program> allBedPrograms = new ArrayList<Program>();
        Program[] allBedProgramsInArr = programManager.getBedPrograms();

        String noteId = request.getParameter("noteId");
        if(noteId==null||noteId.trim().length()==0||noteId.trim().equalsIgnoreCase("null")||noteId.trim().substring(0,1).equalsIgnoreCase("0")){
            String demographicNo = request.getParameter("demographicNo");
            if(demographicNo==null||demographicNo.trim().length()==0){
            	//don't do anything?
            }else{
                List<CaseManagementNote> notes = caseManagementNoteDao.getNotesByDemographic(demographicNo);
                if(notes!=null&&notes.size()>0) noteId = notes.get(notes.size()-1).getId()+"";
            }
        }
        if(noteId==null||noteId.trim().length()==0){
        	//don't do anything?
        }else{
            request.getSession().setAttribute("noteId",noteId);
            request.setAttribute("noteId",noteId);
        }

        for(int i=0; i < allBedProgramsInArr.length; i++){
            allBedPrograms.add(allBedProgramsInArr[i]);
        }
        request.setAttribute("allBedPrograms", allBedPrograms);

        request.setAttribute("genders",lookupManager.LoadCodeList("GEN", true, null, null));

        return mapping.findForward("attachSearch");
    }
	
	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		DynaActionForm searchForm = (DynaActionForm)form;
		ClientSearchFormBean formBean = (ClientSearchFormBean)searchForm.get("criteria");
		
		List<Program> allBedPrograms = new ArrayList<Program>();
		Program[] allBedProgramsInArr = programManager.getBedPrograms();

		for(int i=0; i < allBedProgramsInArr.length; i++){
			allBedPrograms.add(allBedProgramsInArr[i]);
		}
		request.setAttribute("allBedPrograms", allBedPrograms);
		
		formBean.setProgramDomain((List)request.getSession().getAttribute("program_domain"));
		
		/* do the search */
		request.setAttribute("clients",clientManager.search(formBean));		

		if(formBean.isSearchOutsideDomain()) {
			LogAction.log("read","out of domain client search","",request);
		}
		request.setAttribute("genders",lookupManager.LoadCodeList("GEN", true, null, null));
				
		return mapping.findForward("form");
	}

    public ActionForward attachSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        DynaActionForm searchForm = (DynaActionForm)form;
        ClientSearchFormBean formBean = (ClientSearchFormBean)searchForm.get("criteria");

        List<Program> allBedPrograms = new ArrayList<Program>();
        Program[] allBedProgramsInArr = programManager.getBedPrograms();

        for(int i=0; i < allBedProgramsInArr.length; i++){
            allBedPrograms.add(allBedProgramsInArr[i]);
        }
        request.setAttribute("allBedPrograms", allBedPrograms);

        formBean.setProgramDomain((List)request.getSession().getAttribute("program_domain"));

		/* do the search */
        request.setAttribute("clients",clientManager.search(formBean));

        if(formBean.isSearchOutsideDomain()) {
            LogAction.log("read","out of domain client search","",request);
        }
        request.setAttribute("genders",lookupManager.LoadCodeList("GEN", true, null, null));

        return mapping.findForward("attachSearch");
    }


    public void setLookupManager(LookupManager lookupManager) {
    	this.lookupManager = lookupManager;
    }

    public void setClientManager(ClientManager mgr) {
    	this.clientManager = mgr;
    }

    public void setProgramManager(ProgramManager mgr) {
    	this.programManager = mgr;
    }
	
}
