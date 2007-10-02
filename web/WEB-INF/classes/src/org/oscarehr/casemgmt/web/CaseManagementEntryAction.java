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
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.ResourceBundle;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementTmpSave;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.web.formbeans.CaseManagementEntryFormBean;
import org.springframework.web.context.WebApplicationContext;

import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.util.UtilDateUtilities;

public class CaseManagementEntryAction extends BaseCaseManagementEntryAction
{

	private static Log log = LogFactory.getLog(CaseManagementEntryAction.class);
	
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return edit(mapping, form, request, response);
	}
	
	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("edit");
                if( request.getSession().getAttribute("userrole") == null ) {
                    response.sendError(response.SC_FORBIDDEN);
                    return null;
                }
                
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		
		request.setAttribute("change_flag", "false");
		request.setAttribute("from", "casemgmt");

		String demono = getDemographicNo(request);
		String providerNo = getProviderNo(request);
		Boolean restore = (Boolean) request.getAttribute("restore");
		String programId = (String)request.getSession().getAttribute("case_program_id");
				
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));		                                                
		
		/* process the request from other module */
		if (!"casemgmt".equalsIgnoreCase(request.getParameter("from"))) {

			// no demographic number, no page
			if (request.getParameter("demographicNo") == null
					|| "".equals(request.getParameter("demographicNo"))) {
				return mapping.findForward("NoDemoERR");
			}
			request.setAttribute("from", "");
		}
		
		/* prepare url for billing */
		if (request.getParameter("from") != null) {
			request.setAttribute("from", request.getParameter("from"));
		}

		String url = "";
		if ("casemgmt".equals(request.getAttribute("from"))) {
			String ss = (String) request.getSession().getAttribute("casemgmt_VlCountry");
			Properties oscarVariables = (Properties) request.getSession().getAttribute("oscarVariables");
			String province = "";
			if(oscarVariables != null) {
				province = ((String) oscarVariables.getProperty("billregion", "")).trim().toUpperCase();
			}
			
			EctSessionBean bean = (EctSessionBean) request.getSession().getAttribute("casemgmt_oscar_bean");

			if (bean.appointmentNo == null) {
				bean.appointmentNo = "0";
			}
			String bsurl = (String) request.getSession().getAttribute("casemgmt_oscar_baseurl");
			Date today = new Date();
			Calendar todayCal = Calendar.getInstance();
			todayCal.setTime(today);
			
			String Hour = Integer.toString(todayCal.get(Calendar.HOUR));
			String Min = Integer.toString(todayCal.get(Calendar.MINUTE));
			if ("BR".equals(ss)) {
				url = bsurl
						+ "/oscar/billing/procedimentoRealizado/init.do?appId="
						+ bean.appointmentNo;
			} else {
				//StringEncoderUtils.a();
				String default_view = "";
				if(oscarVariables != null) {
					default_view = oscarVariables.getProperty("default_view","");
				}
				url = bsurl
						+ "/billing.do?billRegion="
						+ java.net.URLEncoder.encode(province,"UTF-8")
						+ "&billForm="
						+ java.net.URLEncoder.encode(default_view,"UTF-8")
						+ "&hotclick="
						+ java.net.URLEncoder.encode("","UTF-8")
						+ "&appointment_no="
						+ bean.appointmentNo
						+ "&appointment_date="
						+ bean.appointmentDate
						+ "&start_time="
						+ Hour
						+ ":"
						+ Min
						+ "&demographic_name="
						+ java.net.URLEncoder.encode(bean.patientLastName + ","
								+ bean.patientFirstName,"UTF-8") + "&demographic_no="
						+ bean.demographicNo + "&providerview="
						+ bean.curProviderNo + "&user_no=" + bean.providerNo
						+ "&apptProvider_no=" + bean.curProviderNo
						+ "&bNewForm=1&status=t";
			}
			request.getSession().setAttribute("billing_url",url);
		}


		/* remove the remembered echart string */
		request.getSession().setAttribute("lastSavedNoteString", null);                                

		List issues;
		issues = caseManagementMgr.filterIssues(caseManagementMgr.getIssues(providerNo, demono),providerNo,programId);
                
		/*
		if(request.getSession().getAttribute("archiveView")!="true")
			issues = caseManagementMgr.filterIssues(caseManagementMgr.getIssues(providerNo, demono),providerNo,programId);
		else
			issues = caseManagementMgr.getIssues(providerNo, demono);
		*/
		
		cform.setDemoNo(demono);
		CaseManagementNote note = null;

                String nId = request.getParameter("noteId");
                String forceNote = request.getParameter("forceNote");
                if( forceNote == null ) 
                    forceNote = "false";
                
                log.debug("NoteId " + nId);
                CaseManagementTmpSave tmpsavenote = this.caseManagementMgr.restoreTmpSave(providerNo,demono,programId);
                if (request.getParameter("note_edit") != null && request.getParameter("note_edit").equals("new")) {		                                                                       
                        request.getSession().setAttribute("newNote","true");
                        request.getSession().setAttribute("issueStatusChanged","false");
                        note = new CaseManagementNote();
                        // note.setNote("test");
                        note.setProvider_no(providerNo);
                        Provider prov = new Provider();
                        prov.setProviderNo(providerNo);
                        note.setProvider(prov);
                        note.setDemographic_no(demono);     
                        
                        resetTemp(providerNo, demono, programId);

		}else if(tmpsavenote != null && !forceNote.equals("true")) {
                        log.debug("tempsavenote is NOT NULL");
                        if( tmpsavenote.getNote_id() > 0 ) {
                            request.getSession().setAttribute("newNote","false");
                            note = caseManagementMgr.getNote(String.valueOf(tmpsavenote.getNote_id())); 
                            log.debug("Restoring " + String.valueOf(note.getId()));
                        }
                        else {
                            request.getSession().setAttribute("newNote","true");
                            request.getSession().setAttribute("issueStatusChanged","false");
                            note = new CaseManagementNote();                        
                            note.setProvider_no(providerNo);
                            Provider prov = new Provider();
                            prov.setProviderNo(providerNo);
                            note.setProvider(prov);
                            note.setDemographic_no(demono);
                        }

                        note.setNote(tmpsavenote.getNote());
                
                }else if( nId != null && Integer.parseInt(nId) > 0 ) {
			request.getSession().setAttribute("newNote","false");			
			note = caseManagementMgr.getNote(nId);
                        log.debug("Using nId " + nId + " to fetch note");
			if(note.getHistory()== null || note.getHistory().equals("")) {
				//old note - we need to save the original in here
				note.setHistory(note.getNote());
				caseManagementMgr.saveNoteSimple(note);
			}

		} else {
                    //A hack to load last unsigned note when not specifying a particular note to edit
                    //if there is no unsigned note load a new one
                    if((note = getLastSaved(request,demono,providerNo)) == null) {
                        request.getSession().setAttribute("newNote","true");
                        request.getSession().setAttribute("issueStatusChanged","false");
                        note = new CaseManagementNote();
                        // note.setNote("test");
                        note.setProvider_no(providerNo);
                        Provider prov = new Provider();
                        prov.setProviderNo(providerNo);
                        note.setProvider(prov);
                        note.setDemographic_no(demono);                         
                    }                    
                }
		
		/* do the restore 
		if(restore != null && restore.booleanValue() == true) {
			String tmpsavenote = this.caseManagementMgr.restoreTmpSave(providerNo,demono,programId);
			if(tmpsavenote != null) {
				note.setNote(tmpsavenote);
			}
			
		}*/
                
                log.debug("Fetched Note " + String.valueOf(note.getId()));
		cform.setCaseNote(note);
		/* set issue checked list */
		CheckBoxBean[] checkedList = new CheckBoxBean[issues.size()];
		for (int i = 0; i < issues.size(); i++)
		{
			checkedList[i] = new CheckBoxBean();
			CaseManagementIssue iss = (CaseManagementIssue) issues.get(i);
			checkedList[i].setIssue(iss);
			checkedList[i].setUsed(caseManagementMgr.haveIssue(iss.getId(),
					demono));

		}
                
		Iterator itr = note.getIssues().iterator();
		while (itr.hasNext())
		{
			int id = ((CaseManagementIssue) itr.next()).getId().intValue();
			SetChecked(checkedList, id);
		}

		cform.setIssueCheckList(checkedList);
                
//Why are we caching over 31000 issues?               
//                System.out.println("Fetching all issues");
//		/* set new issue list */
//		List aInfo = caseManagementMgr.getAllIssueInfo();                
//                System.out.println("Got Issues and going to check for new ones");
//		List issueInfo = new ArrayList();
//		itr = aInfo.iterator();
//		while (itr.hasNext())
//		{
//			Issue iss = (Issue) itr.next();
//			if (!inCaseIssue(iss, issues))
//			{
//				LabelValueBean ll = new LabelValueBean();
//				ll.setValue(iss.getId().toString());
//				ll.setLabel(iss.getDescription());
//				issueInfo.add(ll);
//			}
//		}
//                
//                System.out.println("Caching issues " + issueInfo.size());
//		cform.setNewIssueList(issueInfo);
		//if (!note.isSigned())
		//	cform.setSign("off");
		//else
                
			cform.setSign("off");
		if (!note.isIncludeissue())
			cform.setIncludeIssue("off");
		else
			cform.setIncludeIssue("on");
              
                
                boolean passwd = caseManagementMgr.getEnabled();
                String chain = request.getParameter("chain");                               
		
                if( chain != null && chain.length() > 0 ) {
                    request.getSession().setAttribute("passwordEnabled", passwd);
                    return mapping.findForward(chain);   
                }
                
                request.setAttribute("passwordEnabled", passwd);
                               
                String ajax = request.getParameter("ajax");
		if( ajax != null && ajax.equalsIgnoreCase("true") ) {                  
                    request.getSession().setAttribute("caseManagementEntryForm", cform);
                    return mapping.findForward("issueList_ajax");
                }
                
		return mapping.findForward("view");
	}

        public void resetTemp(String providerNo, String demoNo, String programId) {
            try {
                    this.caseManagementMgr.deleteTmpSave(providerNo,demoNo,programId);
		}catch(Throwable e) {
                    log.warn(e);
		}     
        }

	public boolean noteSave(CaseManagementEntryFormBean cform, HttpServletRequest request) throws Exception {

                //we don't want to save empty notes!
                CaseManagementNote note = (CaseManagementNote) cform.getCaseNote();
                String noteTxt = note.getNote();
                if( noteTxt == null || noteTxt.equals("") )
                    return false;
                
		String providerNo = getProviderNo(request);
                Provider provider = getProvider(request);
		String userName = provider != null?provider.getFullName():"";
                

                CaseManagementCPP cpp = null;
                String chain = request.getParameter("chain");                
                if( chain == null )                       
                    cpp = this.caseManagementMgr.getCPP(getDemographicNo(request));                    

		String lastSavedNoteString = (String) request.getSession().getAttribute("lastSavedNoteString");

		/* get the checked issue save into note */
		List issuelist = new ArrayList();
		CheckBoxBean[] checkedlist = (CheckBoxBean[]) cform.getIssueCheckList();		

		String sign = (String) request.getParameter("sign");
		String includeIssue = (String) request.getParameter("includeIssue");
		if (sign == null || !sign.equals("on"))	{
			note.setSigning_provider_no("");
			note.setSigned(false);                        
			cform.setSign("off");
		} else {
			note.setProvider_no(providerNo);
			note.setSigning_provider_no(userName);
			note.setSigned(true);
		}
				
		if( provider != null )
                    note.setProvider(provider);
		
		WebApplicationContext ctx = this.getSpringContext();

		ProgramManager programManager= (ProgramManager)ctx.getBean("programManager");
		AdmissionManager admissionManager= (AdmissionManager)ctx.getBean("admissionManager");
		
		//agency manager not implemented yet. 
		note.setAgency_no("0");
	
		String role=null;
		String team=null;

		//if this is an update, don't overwrite the program id
		if(note.getProgram_no() == null || note.getProgram_no().equals("") ||!note.getProgram_no().equals("") ) {
			String programId = (String)request.getSession().getAttribute("case_program_id");			
			note.setProgram_no(programId);
		}

		try {
			role = String.valueOf((programManager.getProgramProvider(note.getProvider_no(),note.getProgram_no())).getRole().getId());
		}catch(Throwable e) {
			log.error(e);
			role = "0";
		}
		/*
		if(request.getSession().getAttribute("archiveView")!="true")
			note.setReporter_caisi_role(role);	
		else
			note.setReporter_caisi_role("1");
		*/
		note.setReporter_caisi_role(role);
		
		try {
			team = String.valueOf((admissionManager.getAdmission(note.getProgram_no(), Integer.valueOf(note.getDemographic_no()))).getTeamId());
		}catch(Throwable e) {
			log.error(e);
			team = "0";
		}
		note.setReporter_program_team(team);
	
		Set issueset = new HashSet();
		Set noteSet = new HashSet();
		String ongoing = "";
		for (int i = 0; i < checkedlist.length; i++)
		{
			if (!checkedlist[i].getIssue().isResolved())
				ongoing = ongoing
						+ checkedlist[i].getIssue().getIssue().getDescription()
						+ "\n";
			String ischecked = request.getParameter("issueCheckList[" + i
					+ "].checked");
			if (ischecked != null && ischecked.equalsIgnoreCase("on"))
			{
				checkedlist[i].setChecked("on");
				CaseManagementIssue iss = checkedlist[i].getIssue();
				iss.setNotes(noteSet);
				issueset.add(checkedlist[i].getIssue());
			} else
			{
				checkedlist[i].setChecked("off");
			}
			issuelist.add(checkedlist[i].getIssue());
		}

		note.setIssues(issueset);

		/* remove signature and the related issues from note */
		String noteString = note.getNote();
		noteString = removeSignature(noteString);
		noteString = removeCurrentIssue(noteString);
		note.setNote(noteString);

		/* add issues into notes */
		if (includeIssue == null || !includeIssue.equals("on"))	{
			/* set includeissue in note */
			note.setIncludeissue(false);
			cform.setIncludeIssue("off");
		} else {
			note.setIncludeissue(true);
			/* add the related issues to note */

			String issueString = "";
			issueString = createIssueString(issueset);
			// insert the string before signiture

			int index = noteString.indexOf("\n[[");
			if (index >= 0)	{
				String begString = noteString.substring(0, index);
				String endString = noteString.substring(index + 1);
				note.setNote(begString + issueString + endString);
			} else {
				note.setNote(noteString + issueString);
			}
		}
		
		/* save all issue changes for demographic */
		caseManagementMgr.saveAndUpdateCaseIssues(issuelist);
		//cpp.setOngoingConcerns(ongoing);
		
                //update appointment and add verify message to note if verified                
                EctSessionBean sessionBean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");                
                String verify = request.getParameter("verify");
                if( verify != null && verify.equalsIgnoreCase("on") ) {
                    ResourceBundle prop = ResourceBundle.getBundle("oscarResources", request.getLocale());
                    String message = "[" + prop.getString("oscarEncounter.class.EctSaveEncounterAction.msgVer") +
                                    " " +
                                    UtilDateUtilities.DateToString(new Date(), "dd-MMM-yyyy H:mm",request.getLocale()) + "]";
                    String n = note.getNote() + "\n" + message;
                    note.setNote(n);
                    
                    //only update appt if there is one
                    if( sessionBean.appointmentNo != null && !sessionBean.appointmentNo.equals("") )
                        caseManagementMgr.updateAppointment(sessionBean.appointmentNo, sessionBean.status, "verify");
                }
                else if( note.isSigned() ) {
                    //only update appt if there is one
                    if( sessionBean.appointmentNo != null && !sessionBean.appointmentNo.equals("") )
                        caseManagementMgr.updateAppointment(sessionBean.appointmentNo, sessionBean.status, "sign");
                }
		/*get access right*/
		//List accessRight=caseManagementMgr.getAccessRight(providerNo,getDemographicNo(request),(String)request.getSession().getAttribute("case_program_id"));
		String roleName=caseManagementMgr.getRoleName(providerNo,note.getProgram_no());
		/*
		 * if provider is a doctor or nurse,get all major and resolved medical
		 * issue for demograhhic and append them to CPP medical history
		 */
		//setCPPMedicalHistory(cpp, providerNo,accessRight);

		//caseManagementMgr.saveCPP(cpp, providerNo);
		
		if(note.getPassword() != null && note.getPassword().length()>0) {
			note.setLocked(true);
		} else {
			note.setLocked(false);
		}

                int revision;
                
                if( note.getRevision() != null ) {
                    revision = Integer.parseInt(note.getRevision());
                    ++revision;                   
                }
                else
                    revision = 1;
                
                note.setRevision(String.valueOf(revision));
                
                Date now = new Date();
                String observationDate = cform.getObservation_date();
                Date dateObserve;
                if( observationDate != null && !observationDate.equals("") ) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy H:mm");
                    dateObserve = formatter.parse(observationDate);
                }
                else {
                    dateObserve = now;
                }
                
                note.setObservation_date(dateObserve);
                note.setUpdate_date(now);
                if( note.getCreate_date() == null )
                    note.setCreate_date(now);
                
                
		/* save note including add signature */	
		String savedStr = caseManagementMgr.saveNote(cpp, note, providerNo, userName, lastSavedNoteString, roleName);
		/* remember the str written into echart */
		request.getSession().setAttribute("lastSavedNoteString", savedStr);
                
		
		try {
			this.caseManagementMgr.deleteTmpSave(providerNo,note.getDemographic_no(),note.getProgram_no());
		}catch(Throwable e) {
			log.warn(e);
		}               
                
                return true;
	}


	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)	throws Exception {                
                HttpSession session = request.getSession();
                if( session == null || session.getAttribute("userrole") == null )
                    return mapping.findForward("expired");                
                
                String providerNo = getProviderNo(request);
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		request.setAttribute("change_flag","false");
		
		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));
		

		request.setAttribute("from", request.getParameter("from"));	
		boolean saved = noteSave(cform, request);
		
		/* prepare the message */
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("note.saved"));
		saveMessages(request, messages);
                
                //are we in the new encounter and chaining actions?
                String chain = request.getParameter("chain");                
                if( chain != null ) {      
                    request.getSession().setAttribute("newNote",!saved);               
                    request.getSession().setAttribute("saveNote", new Boolean(true));  //tell CaseManagementView we have just saved note
                    return mapping.findForward(chain);
                }

		// this.caseManagementMgr.saveNote();
		return mapping.findForward("view");
	}



	public ActionForward saveAndExit(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception{
		log.debug("saveandexit");
                String providerNo = getProviderNo(request);
                if( request.getSession().getAttribute("userrole") == null )
                    return mapping.findForward("expired");                
                
		request.setAttribute("change_flag","false");
		
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("note.saved"));
		saveMessages(request, messages);

		noteSave(cform, request);
		cform.setMethod("view");
		return mapping.findForward("windowClose");
	}
	
	public ActionForward cancel(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception{
                if( request.getSession().getAttribute("userrole") == null )
                    return mapping.findForward("expired");
                
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		String providerNo = getProviderNo(request);
		CaseManagementNote note = (CaseManagementNote) cform.getCaseNote();
		
		try {
			this.caseManagementMgr.deleteTmpSave(providerNo,note.getDemographic_no(),note.getProgram_no());
		}catch(Throwable e) {
			log.warn(e);
		}
		
		return mapping.findForward("windowClose");
	}

	public ActionForward exit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("change_flag","false");
		return mapping.findForward("list");
	}

	public ActionForward addNewIssue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)	throws Exception {
		log.debug("addNewIssue");                
                
                if( request.getSession().getAttribute("userrole") == null )
                    return mapping.findForward("expired");
                                
		request.setAttribute("change_flag","true");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));
		
		
		request.setAttribute("from", request.getParameter("from"));
		//noteSave(cform, request);
		cform.setShowList("false");
		cform.setSearString("");
		return mapping.findForward("IssueSearch");
	}
        
        public ActionForward issueList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
            if( request.getSession().getAttribute("userrole") == null ) {
                response.sendError(response.SC_FORBIDDEN);
                return null;
            }
            
            String programId = (String)request.getSession().getAttribute("case_program_id");	
            CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;

            String demono = getDemographicNo(request);
            
            // get current providerNo
            String providerNo = getProviderNo(request);

            // get the issue list have search string
            String search = request.getParameter("issueSearch");

            List searchResults;
            searchResults = caseManagementMgr.searchIssues(providerNo, programId, search);            
            
            List filteredSearchResults = new ArrayList();

            //remove issues which we already have - we don't want duplicates
            List existingIssues;
            existingIssues = caseManagementMgr.filterIssues(caseManagementMgr.getIssues(providerNo, demono),providerNo,programId);
            
            Map existingIssuesMap = convertIssueListToMap(existingIssues);            
            for(Iterator iter=searchResults.iterator();iter.hasNext();) {
                    Issue issue = (Issue)iter.next();
                    if(existingIssuesMap.get(issue.getId()) == null) {
                            filteredSearchResults.add(issue);                            
                    }
            }
            
            CheckIssueBoxBean[] issueList = new CheckIssueBoxBean[filteredSearchResults.size()];
            for (int i = 0; i < filteredSearchResults.size(); i++)
            {
                    issueList[i] = new CheckIssueBoxBean();
                    issueList[i].setIssue((Issue) filteredSearchResults.get(i));

            }
            cform.setNewIssueCheckList(issueList);
                
            request.setAttribute("issueList", filteredSearchResults);
            
            return mapping.findForward("issueAutoCompletion");
        }

	public ActionForward issueSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		log.debug("issueSearch");
		String programId = (String)request.getSession().getAttribute("case_program_id");
		
		request.setAttribute("change_flag","true");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;

		request.setAttribute("from", request.getParameter("from"));
		cform.setShowList("true");
		
		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));
		

		// get current providerNo
		String providerNo = getProviderNo(request);
		
		// get the issue list have search string
		String search = (String) cform.getSearString();
		
		List searchResults;
		searchResults = caseManagementMgr.searchIssues(providerNo, programId, search);
		/*
		if(request.getSession().getAttribute("archiveView")!="true")
			searchResults = caseManagementMgr.searchIssues(providerNo, programId, search);
		else
			searchResults = caseManagementMgr.searchIssuesNoRolesConcerned(providerNo,programId,search);
		*/
		
		List filteredSearchResults = new ArrayList();
		
		//remove issues which we already have - we don't want duplicates
		List existingIssues;
		existingIssues = caseManagementMgr.filterIssues(caseManagementMgr.getIssues(providerNo, demono),providerNo,programId);
		/*
		if(request.getSession().getAttribute("archiveView")!="true")
			existingIssues = caseManagementMgr.filterIssues(caseManagementMgr.getIssues(providerNo, demono),providerNo,programId);
		else
			existingIssues = caseManagementMgr.getIssues(providerNo, demono);
		*/
		
		Map existingIssuesMap = convertIssueListToMap(existingIssues);		
		for(Iterator iter=searchResults.iterator();iter.hasNext();) {
			Issue issue = (Issue)iter.next();
			if(existingIssuesMap.get(issue.getId()) == null) {
				filteredSearchResults.add(issue);
			}
		}
		
		
		CheckIssueBoxBean[] issueList = new CheckIssueBoxBean[filteredSearchResults.size()];
		for (int i = 0; i < filteredSearchResults.size(); i++)
		{
			issueList[i] = new CheckIssueBoxBean();
			issueList[i].setIssue((Issue) filteredSearchResults.get(i));

		}
		cform.setNewIssueCheckList(issueList);
		
		if(request.getParameter("change_diagnosis") != null)
			request.setAttribute("change_diagnosis", request.getParameter("change_diagnosis"));
		if(request.getParameter("change_diagnosis_id") != null)
			request.setAttribute("change_diagnosis_id", request.getParameter("change_diagnosis_id"));
		

		return mapping.findForward("IssueSearch");
	}
	
        //we need to convert single issue into checkbox array so we can play nicely with CaseManagementEntryFormBean
        public ActionForward makeIssue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
            String programId = (String)request.getSession().getAttribute("case_program_id");
            //grab the issue we want to add
            String issueId = request.getParameter("newIssueId");
            String providerNo = getProviderNo(request);
            
            CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
            
            //check to see if this issue has already been associated with this demographic
            boolean issueExists = false;
            long lIssueId = Long.parseLong(issueId);
            CheckBoxBean[] existingCaseIssueList = cform.getIssueCheckList();
            for( int idx = 0; idx < existingCaseIssueList.length; ++idx ) {
                if( existingCaseIssueList[idx].getIssue().getIssue_id() == lIssueId ) {
                    issueExists = true;
                    break;
                }
            }
            
            //if issue hasn't been added, add it
            //if it has do nothing;
            if( !issueExists ) {
                CheckIssueBoxBean[] caseIssueList = new CheckIssueBoxBean[1];

                caseIssueList[0] = new CheckIssueBoxBean();
                Issue issue = caseManagementMgr.getIssue(issueId);
                caseIssueList[0].setIssue( issue );
                caseIssueList[0].setChecked(true);
                cform.setNewIssueCheckList(caseIssueList);

                return issueAdd(mapping, cform, request, response);
            }
            else
                return null;
        }

	public ActionForward issueAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)	throws Exception {
                if( request.getSession().getAttribute("userrole") == null )
                    return mapping.findForward("expired");
                
                
		String changeDiagnosis = request.getParameter("change_diagnosis");
		if(changeDiagnosis != null && changeDiagnosis.equalsIgnoreCase("true")) {
			return this.submitChangeDiagnosis(mapping, form, request, response);
		}
		log.debug("issueAdd");
		request.setAttribute("change_flag","true");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		request.setAttribute("from", request.getParameter("from"));
				

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge",getDemoAge(demono));
		request.setAttribute("demoDOB",getDemoDOB(demono));
		

		// add checked new issues to client's issue list
		// client's old issues
		CheckBoxBean[] oldList = (CheckBoxBean[]) cform.getIssueCheckList();
		// client's new issues
		CheckIssueBoxBean[] issueList = (CheckIssueBoxBean[]) cform
				.getNewIssueCheckList();
		int k = 0;
		for (int i = 0; i < issueList.length; i++)
		{
			if (issueList[i].isChecked())
				k++;
		}
		CheckBoxBean[] caseIssueList = new CheckBoxBean[oldList.length + k];
		for (int i = 0; i < oldList.length; i++)
		{
			caseIssueList[i] = new CheckBoxBean();
			caseIssueList[i].setChecked(oldList[i].getChecked());
			caseIssueList[i].setUsed(oldList[i].isUsed());
			caseIssueList[i].setIssue(oldList[i].getIssue());
		}
		k = 0;
		for (int i = 0; i < issueList.length; i++)
		{
			if (issueList[i].isChecked())
			{
				caseIssueList[oldList.length + k] = new CheckBoxBean();
				caseIssueList[oldList.length + k].setIssue(newIssueToCIssue(
						cform, issueList[i].getIssue()));
                                caseIssueList[oldList.length + k].setChecked("on");
				k++;
			}
		}

		cform.setIssueCheckList(caseIssueList);

                String ajax = request.getParameter("ajax");
                if( ajax != null && ajax.equalsIgnoreCase("true") ) {
                    request.setAttribute("caseManagementEntryForm", cform);
                    return mapping.findForward("issueList_ajax");
                }
                else
                    return mapping.findForward("view");
	}

	public ActionForward changeDiagnosis(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)	throws Exception {
		log.debug("changeDiagnosis");
                if( request.getSession().getAttribute("userrole") == null )
                    return mapping.findForward("expired");                
                
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		String inds = (String) cform.getDeleteId();
		
		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));
			
		request.setAttribute("from", request.getParameter("from"));
		request.setAttribute("change_diagnosis", new Boolean(true));
		request.setAttribute("change_diagnosis_id", inds);
		cform.setShowList("false");
		cform.setSearString("");
		return mapping.findForward("IssueSearch");	
	}
	
	public ActionForward submitChangeDiagnosis(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		log.debug("submitChangeDiagnosis");
                if( request.getSession().getAttribute("userrole") == null )
                    return mapping.findForward("expired");                
                
		request.setAttribute("change_flag","true");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		request.setAttribute("from", request.getParameter("from"));
	
		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge",getDemoAge(demono));
		request.setAttribute("demoDOB",getDemoDOB(demono));
		
		//get issue we're changing
		String strIndex = request.getParameter("change_diagnosis_id");
		int index = Integer.parseInt(strIndex);
		
		//change issue
		CheckBoxBean[] oldList = (CheckBoxBean[]) cform.getIssueCheckList();
		
		CheckIssueBoxBean[] issueList = (CheckIssueBoxBean[]) cform.getNewIssueCheckList();
		CheckIssueBoxBean substitution = null;
		
		//find the checked issue 
		for(CheckIssueBoxBean curr:issueList) {
			if(curr.isChecked()) {
				substitution = curr;
				break;
			}
		}
		
		if(substitution != null) {
			for(int x=0;x<oldList.length;x++) {
				if(x == index) {
					Issue iss = caseManagementMgr.getIssue(String.valueOf(substitution.getIssue().getId().longValue()));
					oldList[x].getIssue().setIssue(iss);
					oldList[x].getIssue().setIssue_id(substitution.getIssue().getId().longValue());
					this.caseManagementMgr.saveCaseIssue(oldList[x].getIssue());					
				}			
			}						
		}
		
		cform.setIssueCheckList(oldList);
		//updateIssueToConcern(cform);
		
		return mapping.findForward("view");
	}
	
        public ActionForward ajaxChangeDiagnosis(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		log.debug("ajaxChangeDiagnosis");		
		if( request.getSession().getAttribute("userrole") == null )
                    return mapping.findForward("expired");                
                
		//get issue we're changing
		String strIndex = request.getParameter("change_diagnosis_id");
                int idx = Integer.parseInt(strIndex);
		
		String substitution = request.getParameter("newIssueId");
                
                CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
                CheckBoxBean[] curIssues = cform.getIssueCheckList();
		
		if(substitution != null) {
						
                    Issue iss = caseManagementMgr.getIssue(substitution);
                    curIssues[idx].getIssue().setIssue(iss);
                    curIssues[idx].getIssue().setIssue_id(iss.getId());
                    this.caseManagementMgr.saveCaseIssue(curIssues[idx].getIssue());
                    
                    //update form with new issue list
                    Set issueset = new HashSet();
                    for( int i = 0; i < curIssues.length; ++i ) {
                        if( curIssues[i].getChecked().equalsIgnoreCase("on") )
                            issueset.add(curIssues[i].getIssue());
                    }
                    
                    cform.getCaseNote().setIssues(issueset);
		}
		
		cform.setIssueCheckList(curIssues);
		//updateIssueToConcern(cform);
		
                request.setAttribute("caseManagementEntryForm", cform);
		//request.setAttribute("issueCheckList", curIssues);
                return mapping.findForward("issueList_ajax");
	}
        
	public ActionForward issueDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("issueDelete");
                if( request.getSession().getAttribute("userrole") == null ) {
                    response.sendError(response.SC_FORBIDDEN);
                    return null;
                }
                
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		//noteSave(cform, request);
		request.setAttribute("change_flag","true");
		request.setAttribute("from", request.getParameter("from"));

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));
				
		CheckBoxBean[] oldList = (CheckBoxBean[]) cform.getIssueCheckList();
		
		String inds = (String) cform.getDeleteId();
		Integer ind = new Integer(inds);

		// delete the right issue
		CheckBoxBean[] caseIssueList = new CheckBoxBean[oldList.length - 1];
		int k = 0;

		if (ind.intValue() >= oldList.length)
		{
			log.error("issueDelete index error");
			return mapping.findForward("view");
		}
		for (int i = 0; i < oldList.length; i++)
		{

			if (i != ind.intValue())
			{
				caseIssueList[k] = new CheckBoxBean();
				caseIssueList[k].setChecked(oldList[i].getChecked());
				caseIssueList[k].setUsed(oldList[i].isUsed());
				caseIssueList[k].setIssue(oldList[i].getIssue());
				k++;
			}
			if (i == ind.intValue())
			{
				// delete from caseissue table
				CaseManagementIssue iss = oldList[i].getIssue();
				caseManagementMgr.deleteIssueById(iss);
			}
		}
		cform.setIssueCheckList(caseIssueList);
		// reset current concern in CPP
		//updateIssueToConcern(cform);
                
                String ajax = request.getParameter("ajax");
                if( ajax != null && ajax.equalsIgnoreCase("true") ) {
                    request.setAttribute("caseManagementEntryForm", cform);
                    return mapping.findForward("issueList_ajax");
                }
                else
                    return mapping.findForward("view");
	}

	public ActionForward issueChange(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception	{
		log.debug("issueChange");
                if( request.getSession().getAttribute("userrole") == null ) {
                    response.sendError(response.SC_FORBIDDEN);
                    return null;
                }
		request.setAttribute("from", request.getParameter("from"));
		request.setAttribute("change_flag", "true");
		request.getSession().setAttribute("issueStatusChanged", "true");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));
		

		//noteSave(cform, request);
		CheckBoxBean[] oldList = (CheckBoxBean[]) cform.getIssueCheckList();
		
		//CaseManagementCPP cpp = this.caseManagementMgr.getCPP(getDemographicNo(request));

		String inds = (String) cform.getLineId();

		Integer ind = new Integer(inds);
		List iss = new ArrayList();
		oldList[ind.intValue()].getIssue().setUpdate_date(new Date());
		iss.add(oldList[ind.intValue()].getIssue());
		caseManagementMgr.saveAndUpdateCaseIssues(iss);
		// reset current concern in CPP
		//updateIssueToConcern(cform);

		//get access right
		//List accessRight=caseManagementMgr.getAccessRight(providerNo,demono,(String)request.getSession().getAttribute("case_program_id"));
		
		// add medical history to CPP 
		//setCPPMedicalHistory(cpp, providerNo,accessRight);
		//caseManagementMgr.saveCPP(cpp, providerNo);
                
                String ajax = request.getParameter("ajax");
                if( ajax != null && ajax.equalsIgnoreCase("true") ) {
                    request.setAttribute("caseManagementEntryForm", cform);
                    return mapping.findForward("issueList_ajax");
                }
                else
                    return mapping.findForward("view");
	}

        public ActionForward notehistory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
            if( request.getSession().getAttribute("userrole") == null )
                    return mapping.findForward("expired");
            
            String demono = getDemographicNo(request);
            request.setAttribute("demoName", getDemoName(demono));            

            String noteid = (String) request.getParameter("noteId");
            
            List <CaseManagementNote> history = caseManagementMgr.getHistory(noteid);
            request.setAttribute("history", history);
            return mapping.findForward("showHistory");
        }

	public ActionForward history(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)	throws Exception {
		log.debug("history");
                if( request.getSession().getAttribute("userrole") == null )
                    return mapping.findForward("expired");

		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));

		String noteid = (String) request.getParameter("noteId");
		CaseManagementNote note = caseManagementMgr.getNote(noteid);
		
		request.setAttribute("history",note.getHistory());
			
		cform.setCaseNote_history(note.getHistory());
		return mapping.findForward("historyview");
	}

	public ActionForward autosave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){

		log.debug("autosave");
		
		String demographicNo = getDemographicNo(request);
		String programId = request.getParameter("programId");
		String providerNo = this.getProviderNo(request);
		String note = request.getParameter("note");
                String noteId = request.getParameter("note_id");
		
		if(note == null || note.length() == 0) {
			return null;
		}
		
		try {
			caseManagementMgr.tmpSave(providerNo, demographicNo, programId, noteId, note);
		}catch(Throwable e) {
			log.warn("AutoSave Error: " + e);
		}
		
		return null;
	}
	
	public ActionForward restore(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
                request.getSession().setAttribute("restoring", "true"); //tell CaseManagementView we're handling temp note
		request.setAttribute("restore", new Boolean(true));
		
		return edit(mapping, form, request, response);
	}
        
        public CaseManagementNote getLastSaved(HttpServletRequest request, String demono, String providerNo) {
            CaseManagementNote note = null;
            List notes = null;            
            
            notes = caseManagementMgr.getNotes(demono);
            notes = manageLockedNotes(notes,false,this.getUnlockedNotesMap(request));
            
            String programId = (String)request.getSession().getAttribute("case_program_id");
            
            if(programId == null || programId.length() == 0 ) {
                programId = "0";
            }
            
            notes = caseManagementMgr.filterNotes(notes, providerNo, programId);
            
            for( int idx = notes.size() - 1; idx >= 0; --idx ) {
                CaseManagementNote n = (CaseManagementNote)notes.get(idx);
                if( !n.isSigned() && n.getProvider_no().equals(providerNo)) {                    
                    note = n;        
                    request.getSession().setAttribute("newNote","false");
                    break;
                }
            }

            return note;
        }
        
        protected Map getUnlockedNotesMap(HttpServletRequest request) {
		Map map =  (Map)request.getSession().getAttribute("unlockedNoteMap");
		if(map == null) {
			map = new HashMap();
		}
		return map;
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
}
