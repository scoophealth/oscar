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

package org.oscarehr.casemgmt.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.caisi.model.CustomFilter;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramTeam;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementSearchBean;
import org.oscarehr.casemgmt.model.CaseManagementTmpSave;
import org.oscarehr.casemgmt.web.formbeans.CaseManagementViewFormBean;

import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.pageUtil.RxSessionBean;



public class CaseManagementViewAction extends BaseCaseManagementViewAction {

    private static Log log = LogFactory.getLog(CaseManagementViewAction.class);

	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean)form;
		caseForm.setFilter_provider("");
		return view(mapping,form,request,response);
	}
	
	public ActionForward setViewType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return view(mapping,form,request,response);
	}

	public ActionForward setPrescriptViewType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return view(mapping,form,request,response);
	}
	
	public ActionForward setHideActiveIssues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return view(mapping,form,request,response);
	}
        
        public ActionForward saveAndExit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
            return save(mapping, form, request, response);
        }
	
        public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
            
            HttpSession session = request.getSession();
            
           if( session.getAttribute("userrole") != null ) {            
                CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean)form;
                CaseManagementCPP cpp=caseForm.getCpp();
                cpp.setUpdate_date(new Date());
                String providerNo = getProviderNo(request);            
                caseManagementMgr.saveCPP(cpp,providerNo);
           }
           else
               response.sendError(response.SC_FORBIDDEN);
           
            return null;
        }

	/*save CPP for patient*/
	public ActionForward patientCPPSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("patientCPPSave");
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean)form;
		CaseManagementCPP cpp=caseForm.getCpp();
                cpp.setUpdate_date(new Date());
		String providerNo = getProviderNo(request);
		caseManagementMgr.saveCPP(cpp,providerNo);
		addMessage(request,"cpp.saved");
		
		return view(mapping,form,request,response);
	}
	
	/* show case management view */
	/*
	 * Session variables : 	case_program_id
	 * 						casemgmt_DemoNo
	 * 						casemgmt_VlCountry
	 * 						casemgmt_msgBeans
	 * 						readonly
	 */
	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
                long start = System.currentTimeMillis();
                long current = 0;
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean)form;
		String tab = request.getParameter("tab");
		if(tab == null) {
			tab = CaseManagementViewFormBean.tabs[0];
		}		
		HttpSession se=request.getSession();
                if( se.getAttribute("userrole") == null )
                    return mapping.findForward("expired");
		
		String providerNo = getProviderNo(request);                                
                
		String demoNo=getDemographicNo(request);
		
		//need to check to see if the client is in our program domain
		//if not...don't show this screen!
		if(!caseManagementMgr.isClientInProgramDomain(providerNo, demoNo)) {
			return mapping.findForward("domain-error");
		}
		
		request.setAttribute("casemgmt_demoName",getDemoName(demoNo));
		request.setAttribute("casemgmt_demoAge",getDemoAge(demoNo));
		request.setAttribute("casemgmt_demoDOB",getDemoDOB(demoNo));
		
		//get client image
		request.setAttribute("image_filename",this.getImageFilename(demoNo, request));

		String programId = (String)request.getSession().getAttribute("case_program_id");
                
		if(programId == null || programId.length() == 0 ) {
			programId = "0";
		}
                
                //check to see if there is an unsaved note
                //if there is see if casemanagemententry has already handled it
                //if it has, disregard unsaved note; if it has not then set attribute
		CaseManagementTmpSave tmpsavenote = this.caseManagementMgr.restoreTmpSave(providerNo,demoNo,programId);
		if(tmpsavenote != null) {
                        String restoring = (String)se.getAttribute("restoring");
                        if( restoring == null )
                            request.setAttribute("can_restore",new Boolean(true));
                        else
		           se.setAttribute("restoring", null);
                }
	
		String teamName = "";
		Admission admission = admissionMgr.getCurrentAdmission(programId, Integer.valueOf(demoNo));
		if(admission!=null){
		List teams = programMgr.getProgramTeams(programId);
		for(Iterator i = teams.iterator(); i.hasNext();) {
			ProgramTeam team = (ProgramTeam) i.next();
			String id1 = Integer.toString(team.getId());
			String id2 = Integer.toString(admission.getTeamId());
			if(id1.equals(id2))
				teamName = team.getName();
		}
		}		
		request.setAttribute("teamName", teamName);
		
		List teamMembers = new ArrayList();
		List ps = programMgr.getProgramProviders(programId);
		for(Iterator j = ps.iterator(); j.hasNext();){
			ProgramProvider pp = (ProgramProvider)j.next();
			for(Iterator k = pp.getTeams().iterator(); k.hasNext();){
				ProgramTeam pt = (ProgramTeam)k.next();
				if(pt.getName().equals(teamName)){
					teamMembers.add(pp.getProvider().getFormattedName());
				}
			}
		}
		request.setAttribute("teamMembers",teamMembers);
		
		/* ISSUES */
				
		if(tab.equals("Current Issues")) {
			List issues = null;
			if(!caseForm.getHideActiveIssue().equals("true")) {
				issues=caseManagementMgr.getIssues(providerNo,this.getDemographicNo(request));
			} else {
				issues=caseManagementMgr.getActiveIssues(providerNo,this.getDemographicNo(request));
			}
			/*
			if(request.getSession().getAttribute("archiveView")!="true")
				request.setAttribute("Issues",caseManagementMgr.filterIssues(issues,providerNo,programId));
			else
				request.setAttribute("Issues",issues);
			*/
                        current = System.currentTimeMillis();
                        log.debug("GET ISSUES " + String.valueOf(current-start));
                        start = current;
			request.setAttribute("Issues",caseManagementMgr.filterIssues(issues,providerNo,programId));
                        current = System.currentTimeMillis();
                        log.debug("FILTER ISSUES " + String.valueOf(current-start));
                        start = current;
			
			/* PROGRESS NOTES */			
			List notes = null;
			
			//filter the notes by the checked issues                        
			String[] checked_issues = request.getParameterValues("check_issue");
			if(checked_issues != null && checked_issues[0].trim().length() > 0 ) {                                
				//need to apply a filter
				request.setAttribute("checked_issues",checked_issues);
				notes = caseManagementMgr.getNotes(this.getDemographicNo(request),checked_issues);
				notes = manageLockedNotes(notes,true,this.getUnlockedNotesMap(request));
			} else {
				notes = caseManagementMgr.getNotes(this.getDemographicNo(request));
				notes = manageLockedNotes(notes,false,this.getUnlockedNotesMap(request));				
			}
			
			//apply role based access
			//if(request.getSession().getAttribute("archiveView")!="true")
                        current = System.currentTimeMillis();
                        log.debug("GET NOTES " + String.valueOf(current-start));
                        start = current;
			notes = caseManagementMgr.filterNotes(notes, providerNo, programId);
                        current = System.currentTimeMillis();
                        log.debug("FILTER NOTES " + String.valueOf(current-start));
                        start = current;
			
			//apply provider filter
			Set providers = new HashSet();
			notes = applyProviderFilters(notes,providers,caseForm.getFilter_providers());
                        current = System.currentTimeMillis();
                        log.debug("FILTER NOTES PROVIDER " + String.valueOf(current-start));
                        start = current;
                        
			request.setAttribute("providers", providers);
                        
                        //apply if we are filtering on role
                        List roles = roleMgr.getRoles();                        
                        request.setAttribute("roles", roles);
                        String[] roleId = caseForm.getFilter_roles();
                        if( roleId != null && roleId.length > 0 )
                            notes = applyRoleFilter(notes, roleId );
			    
                        /*
                         *Notes are by default sorted from the past to the most recent
                         *So we sort only if preference is set in form
                         *or site wide setting in oscar.properties
                         */
                        String noteSort = caseForm.getNote_sort();
                        if(noteSort != null && noteSort.length() > 0 ) {
                            request.setAttribute("Notes", sort_notes(notes, noteSort));
                        }
                        else {
                            oscar.OscarProperties p = oscar.OscarProperties.getInstance();
                            noteSort = p.getProperty("CMESort", "");
                            if( noteSort.trim().equalsIgnoreCase("UP") )
                                request.setAttribute("Notes", sort_notes(notes, "update_date_asc"));                            
                            else
                                request.setAttribute("Notes", notes);                            
                        }
			
			
			//UCF
			request.setAttribute("survey_list", surveyMgr.getAllForms());
			//request.setAttribute("surveys", surveyManager.getForms(demographicNo));
		}
		
		
		CaseManagementCPP cpp = this.caseManagementMgr.getCPP(this.getDemographicNo(request));
		if(cpp == null) {
			cpp = new CaseManagementCPP();
			cpp.setDemographic_no(getDemographicNo(request));
		}
		request.setAttribute("cpp",cpp);
		caseForm.setCpp(cpp);
		
		/* get allergies */
		List allergies = this.caseManagementMgr.getAllergies(this.getDemographicNo(request));
		request.setAttribute("Allergies",allergies);
		
		/* get prescriptions */
		if(tab.equals("Prescriptions")) {
			List prescriptions=null;
			if(caseForm.getPrescipt_view().equals("all")) {
				prescriptions = this.caseManagementMgr.getPrescriptions(this.getDemographicNo(request),true);			
			} else {
				prescriptions = this.caseManagementMgr.getPrescriptions(this.getDemographicNo(request),false);				
			}			
			request.setAttribute("Prescriptions",prescriptions);	
			
			// Setup RX bean start
	        RxSessionBean bean = new RxSessionBean();
	        bean.setProviderNo(providerNo);
	        bean.setDemographicNo(Integer.parseInt(demoNo));	        
	        request.getSession().setAttribute("RxSessionBean", bean);	        
	        // set up RX end
			
		}

		/* tickler */
		if(tab != null && tab.equalsIgnoreCase("Ticklers")) {
			CustomFilter cf = new CustomFilter();
			cf.setDemographic_no(this.getDemographicNo(request));
			cf.setStatus("A");
			request.setAttribute("ticklers",ticklerManager.getTicklers(cf));
		}
		
		if(tab != null && tab.equalsIgnoreCase("Search")) {
			request.setAttribute("roles",roleMgr.getRoles());
			request.setAttribute("program_domain", programMgr.getProgramDomain(getProviderNo(request)));			
		}
		
		
		/*set form value for e-chart*/
		
		Locale vLocale=(Locale) se.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
		caseForm.setVlCountry(vLocale.getCountry());
		caseForm.setDemographicNo(getDemographicNo(request));
		
		se.setAttribute("casemgmt_DemoNo",getDemographicNo(request));
		caseForm.setRootCompURL((String)se.getAttribute("casemgmt_oscar_baseurl"));
		se.setAttribute("casemgmt_VlCountry",vLocale.getCountry());
		
		/*prepare new form list for patient*/
		se.setAttribute("casemgmt_newFormBeans",this.caseManagementMgr.getEncounterFormBeans());
		
		/*prepare messenger list*/
		se.setAttribute("casemgmt_msgBeans", this.caseManagementMgr.getMsgBeans(new Integer(getDemographicNo(request))));
		
		//readonly access to define creat a new note button in jsp. 
		se.setAttribute("readonly",new Boolean(this.caseManagementMgr.hasAccessRight("note-read-only","access",providerNo,demoNo,(String)se.getAttribute("case_program_id"))));		                
                
		//if we have just saved a note, remove saveNote flag
                Boolean saved =  (Boolean)se.getAttribute("saveNote");
                if( saved != null && saved == true ) {
                    request.setAttribute("saveNote", saved);
                    se.removeAttribute("saveNote");
                }
                current = System.currentTimeMillis();
                log.debug("THE END " + String.valueOf(current-start));
                    
                String useNewCaseMgmt = (String)request.getSession().getAttribute("newCaseManagement");
                if( useNewCaseMgmt != null && useNewCaseMgmt.equals("true") )
                    return mapping.findForward("page.newcasemgmt.view");
                else
                    return mapping.findForward("page.casemgmt.view");
              
	}
	
	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String programId = (String)request.getSession().getAttribute("case_program_id");
		
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean)form;
		CaseManagementSearchBean searchBean = new CaseManagementSearchBean(this.getDemographicNo(request));
		searchBean.setSearchEncounterType(caseForm.getSearchEncounterType());
		searchBean.setSearchEndDate(caseForm.getSearchEndDate());
		searchBean.setSearchProgramId(caseForm.getSearchProgramId());
		searchBean.setSearchProviderNo(caseForm.getSearchProviderNo());
		searchBean.setSearchRoleId(caseForm.getSearchRoleId());
		searchBean.setSearchStartDate(caseForm.getSearchStartDate());
		searchBean.setSearchText(caseForm.getSearchText());
		List results = this.caseManagementMgr.search(searchBean);
		List filtered1 = manageLockedNotes(results,false,this.getUnlockedNotesMap(request));
		List filteredResults = caseManagementMgr.filterNotes(filtered1,getProviderNo(request),programId);
		
		List sortedResults = this.sort_notes(filteredResults, caseForm.getNote_sort());
		request.setAttribute("search_results",sortedResults);
		return view(mapping,form,request,response);
	}
	
	public List sort_notes(List notes,String field) throws Exception {
            log.debug("Sorting notes by field: " + field);
            if(field == null || field.equals("") || field.equals("update_date")) {
                    return notes;
            }

            if(field.equals("providerName")) {
                    Collections.sort(notes, CaseManagementNote.getProviderComparator());
            }
            if(field.equals("programName")) {
                    Collections.sort(notes, CaseManagementNote.getProgramComparator());
            }
            if(field.equals("roleName")) {
                    Collections.sort(notes, CaseManagementNote.getRoleComparator());
            }
            if(field.equals("update_date_asc")) {
                    Collections.reverse(notes);
            }
            
            return notes;
    }
            

    //unlock a note temporarily - session
    /*
      * show password
      */
    public ActionForward unlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean)form;
        String noteId = request.getParameter("noteId");
        caseForm.setNoteId(Integer.parseInt(noteId));
        return mapping.findForward("unlockForm");
    }

    public ActionForward do_unlock_ajax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String password = request.getParameter("password");
        int noteId = Integer.parseInt(request.getParameter("noteId"));

        CaseManagementNote note = this.caseManagementMgr.getNote(request.getParameter("noteId"));
        request.setAttribute("Note", note);

        boolean success = caseManagementMgr.unlockNote(noteId,password);
        request.setAttribute("success",new Boolean(success));

        if(success) {
            Map unlockedNoteMap = this.getUnlockedNotesMap(request);
            unlockedNoteMap.put(new Long(noteId),new Boolean(success));
            request.getSession().setAttribute("unlockedNoteMap",unlockedNoteMap);
        }

        return mapping.findForward("unlock_ajax");

    }
    public ActionForward do_unlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean)form;

        String password = caseForm.getPassword();
        int noteId = caseForm.getNoteId();

        boolean success = caseManagementMgr.unlockNote(noteId,password);
        request.setAttribute("success",new Boolean(success));

        if(success) {
            Map unlockedNoteMap = this.getUnlockedNotesMap(request);
            unlockedNoteMap.put(new Long(noteId),new Boolean(success));
            request.getSession().setAttribute("unlockedNoteMap",unlockedNoteMap);
            return mapping.findForward("unlockSuccess");
        } else {
            return unlock(mapping,form,request,response);
        }

    }

    protected Map getUnlockedNotesMap(HttpServletRequest request) {
        Map map =  (Map)request.getSession().getAttribute("unlockedNoteMap");
        if(map == null) {
            map = new HashMap();
        }
        return map;
    }

    protected List applyRoleFilter(List notes, String[] roleId ) {

        //if no filter return everything
        if( Arrays.binarySearch(roleId, "a") >= 0 )
            return notes;

        List filteredNotes = new ArrayList();

        for( Iterator iter = notes.listIterator(); iter.hasNext(); ) {
            CaseManagementNote note = (CaseManagementNote)iter.next();

            if( Arrays.binarySearch(roleId, note.getReporter_caisi_role()) >= 0 )
                filteredNotes.add(note);
        }

        return filteredNotes;
    }

    /*
      * This method extracts a unique list of providers, and optionally
      * filters out all notes belonging to providerNo (arg2).
      */
    protected List applyProviderFilter(List notes, Set providers, String providerNo) {
        boolean filter = false;
        List filteredNotes = new ArrayList();

        if(providerNo != null && providerNo.length()>0) {
            filter=true;
        }

        for(Iterator iter = notes.iterator();iter.hasNext();) {
            CaseManagementNote note = (CaseManagementNote)iter.next();
            providers.add(note.getProvider());
            if(!filter) {
                //no filter, add all
                filteredNotes.add(note);
            } else if(filter && note.getProvider_no().equals(providerNo)) {
                //correct provider
                filteredNotes.add(note);
            }
        }

        return filteredNotes;
    }

    protected List manageLockedNotes(List notes,boolean removeLockedNotes,Map unlockedNotesMap) {
        List notesNoLocked = new ArrayList();
        for(Iterator iter=notes.iterator();iter.hasNext();) {
            CaseManagementNote note = (CaseManagementNote)iter.next();
            if(note.isLocked()) {
                if(unlockedNotesMap.get(note.getId()) != null) {
                    note.setLocked(false);
                }
            }
            if(removeLockedNotes && !note.isLocked()) {
                notesNoLocked.add(note);
            }
        }
        if(removeLockedNotes) {
            return notesNoLocked;
        }
        return notes;
    }
    /*
      * This method extracts a unique list of providers, and optionally
      * filters out all notes belonging to providerNo (arg2).
      */
    protected List applyProviderFilters(List notes, Set providers, String[] providerNo) {
        boolean filter = false;
        List filteredNotes = new ArrayList();

        if(providerNo != null && Arrays.binarySearch(providerNo, "a") < 0) {
            filter = true;
        }

        for(Iterator iter = notes.iterator();iter.hasNext();) {
            CaseManagementNote note = (CaseManagementNote)iter.next();
            providers.add(note.getProvider());
            if(!filter) {
                //no filter, add all
                filteredNotes.add(note);

            } else {
                if( Arrays.binarySearch(providerNo, note.getProvider_no()) >=0)
                    //correct provider
                    filteredNotes.add(note);
            }
        }

        return filteredNotes;
    }
}
