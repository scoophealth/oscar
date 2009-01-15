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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementSearchBean;
import org.oscarehr.casemgmt.model.CaseManagementTmpSave;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.web.formbeans.CaseManagementViewFormBean;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.dx.model.DxResearch;
import org.oscarehr.util.SessionConstants;

import oscar.oscarRx.pageUtil.RxSessionBean;

public class CaseManagementViewAction extends BaseCaseManagementViewAction {

	private static Log log = LogFactory.getLog(CaseManagementViewAction.class);

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean) form;
		caseForm.setFilter_provider("");
		request.getSession().setAttribute("patientCppPrintPreview", "false");

		// prevent null pointer errors as both these variables are required in navigation.jsp
		request.getSession().setAttribute("casemgmt_newFormBeans", new ArrayList<Object>());
		request.getSession().setAttribute("casemgmt_msgBeans", new ArrayList<Object>());

		return view(mapping, form, request, response);
	}

	public ActionForward setViewType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return view(mapping, form, request, response);
	}

	public ActionForward setPrescriptViewType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return view(mapping, form, request, response);
	}

	public ActionForward setHideActiveIssues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return view(mapping, form, request, response);
	}

	public ActionForward saveAndExit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return save(mapping, form, request, response);
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();

		if (session.getAttribute("userrole") != null) {
			CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean) form;
			CaseManagementCPP cpp = caseForm.getCpp();
			cpp.setUpdate_date(new Date());
			// EncounterWindow ectWin = caseForm.getEctWin();
			String providerNo = getProviderNo(request);
			caseManagementMgr.saveCPP(cpp, providerNo);
                        System.out.println("Saved cpp for " + cpp.getDemographic_no());
			// caseManagementMgr.saveEctWin(ectWin);
		}
		else
			response.sendError(response.SC_FORBIDDEN);

		return null;
	}

	/* save CPP for patient */
	public ActionForward patientCPPSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("patientCPPSave");
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean) form;
		CaseManagementCPP cpp = caseForm.getCpp();
		cpp.setUpdate_date(new Date());
		cpp.setDemographic_no(caseForm.getDemographicNo());
		String providerNo = getProviderNo(request);
		caseManagementMgr.saveCPP(cpp, providerNo);
		addMessage(request, "cpp.saved");

		return view(mapping, form, request, response);
	}

	public ActionForward patientCppPrintPreview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("patientCPPSave");

		request.getSession().setAttribute("patientCppPrintPreview", "true");
		return view(mapping, form, request, response);
	}

	/* show case management view */
	/*
	 * Session variables : case_program_id casemgmt_DemoNo casemgmt_VlCountry casemgmt_msgBeans readonly
	 */
	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = System.currentTimeMillis();
		long beginning = start;
		long current = 0;
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean) form;
		log.debug("Starting VIEW");
		String tab = request.getParameter("tab");
		if (tab == null) {
			tab = CaseManagementViewFormBean.tabs[0];
		}
		HttpSession se = request.getSession();
		if (se.getAttribute("userrole") == null) return mapping.findForward("expired");

		String providerNo = getProviderNo(request);

		String demoNo = getDemographicNo(request);

		log.debug("is client in program");
		// need to check to see if the client is in our program domain
		// if not...don't show this screen!
		if (!caseManagementMgr.isClientInProgramDomain(providerNo, demoNo)) {
			return mapping.findForward("domain-error");
		}

		current = System.currentTimeMillis();
		log.debug("client in program " + String.valueOf(current - start));
		start = current;

		request.setAttribute("casemgmt_demoName", getDemoName(demoNo));
		request.setAttribute("casemgmt_demoAge", getDemoAge(demoNo));
		request.setAttribute("casemgmt_demoDOB", getDemoDOB(demoNo));
		request.setAttribute("demographicNo", demoNo);

		log.debug("client Image?");
		// get client image
		request.setAttribute("image_filename", getImageFilename(demoNo, request));

		current = System.currentTimeMillis();
		log.debug("client image " + String.valueOf(current - start));
		start = current;

		String programId = (String) request.getSession().getAttribute("case_program_id");

		if (programId == null || programId.length() == 0) {
			programId = "0";
		}

		log.debug("is there a tmp note?");
		// check to see if there is an unsaved note
		// if there is see if casemanagemententry has already handled it
		// if it has, disregard unsaved note; if it has not then set attribute
		CaseManagementTmpSave tmpsavenote = this.caseManagementMgr.restoreTmpSave(providerNo, demoNo, programId);
		if (tmpsavenote != null) {
			String restoring = (String) se.getAttribute("restoring");
			if (restoring == null)
				request.setAttribute("can_restore", new Boolean(true));
			else
				se.setAttribute("restoring", null);
		}

		current = System.currentTimeMillis();
		log.debug("tmp note " + String.valueOf(current - start));
		start = current;

		// fetch and set cpp display dimensions
		/*
		 * EncounterWindow ectWin = this.caseManagementMgr.getEctWin(providerNo); if (ectWin == null) { ectWin = new EncounterWindow(); ectWin.setProviderNo(providerNo);
		 * ectWin.setRowOneSize(EncounterWindow.NORMAL); ectWin.setRowTwoSize(EncounterWindow.NORMAL); }
		 * 
		 * caseForm.setEctWin(ectWin);
		 */

		log.debug("Get admission");
		String teamName = "";
		Admission admission = admissionMgr.getCurrentAdmission(programId, Integer.valueOf(demoNo));
		current = System.currentTimeMillis();
		log.debug("Get admission " + String.valueOf(current - start));
		start = current;

		if (admission != null) {
			log.debug("Get teams");
			List teams = programMgr.getProgramTeams(programId);
			current = System.currentTimeMillis();
			log.debug("Get teams " + String.valueOf(current - start));
			start = current;

			for (Iterator i = teams.iterator(); i.hasNext();) {
				log.debug("Searching teams");
				ProgramTeam team = (ProgramTeam) i.next();
				String id1 = Integer.toString(team.getId());
				String id2 = Integer.toString(admission.getTeamId());
				if (id1.equals(id2)) teamName = team.getName();
			}
		}
		request.setAttribute("teamName", teamName);

		String caisiLoaded = (String) request.getSession().getAttribute("caisiLoaded");

		if (caisiLoaded != null && caisiLoaded.equalsIgnoreCase("true")) {

			log.debug("Get program providers");
			List teamMembers = new ArrayList();
			List ps = programMgr.getProgramProviders(programId);
			current = System.currentTimeMillis();
			log.debug("Get program providers " + String.valueOf(current - start));
			start = current;

			for (Iterator j = ps.iterator(); j.hasNext();) {
				ProgramProvider pp = (ProgramProvider) j.next();
				log.debug("Get program provider teams");
				for (Iterator k = pp.getTeams().iterator(); k.hasNext();) {
					ProgramTeam pt = (ProgramTeam) k.next();
					if (pt.getName().equals(teamName)) {
						teamMembers.add(pp.getProvider().getFormattedName());
					}
				}
				current = System.currentTimeMillis();
				log.debug("Get program provider teams " + String.valueOf(current - start));
				start = current;

			}
			request.setAttribute("teamMembers", teamMembers);

			/* prepare new form list for patient */
			se.setAttribute("casemgmt_newFormBeans", this.caseManagementMgr.getEncounterFormBeans());

			/* prepare messenger list */
			se.setAttribute("casemgmt_msgBeans", this.caseManagementMgr.getMsgBeans(new Integer(demoNo)));

			// readonly access to define creat a new note button in jsp.
			se.setAttribute("readonly", new Boolean(this.caseManagementMgr.hasAccessRight("note-read-only", "access", providerNo, demoNo, (String) se
					.getAttribute("case_program_id"))));

		}
		/* Dx */
		List<DxResearch> dxList = this.caseManagementMgr.getDxByDemographicNo(demoNo);
		Map<String,DxResearch> dxMap = new HashMap<String,DxResearch>();
		for(DxResearch dx:dxList) {
			dxMap.put(dx.getCode(), dx);
		}
		request.setAttribute("dxMap",dxMap);

		Integer currentFacilityId = (Integer) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
				
		// UCF
		log.debug("Fetch Survey List");
		request.setAttribute("survey_list", surveyMgr.getAllForms(currentFacilityId,providerNo));
		current = System.currentTimeMillis();
		log.debug("Fetch Survey List " + String.valueOf(current - start));
		

		/* ISSUES */
		current = System.currentTimeMillis();
		log.debug("Prep work " + String.valueOf(current - start));
		start = current;
		if (tab.equals("Current Issues")) {
			List<CaseManagementIssue> issues = null;
			if (!caseForm.getHideActiveIssue().equals("true")) {
				log.debug("Get Issues");
				issues = caseManagementMgr.getIssues(providerNo, this.getDemographicNo(request));
				current = System.currentTimeMillis();
				log.debug("Get Issues " + String.valueOf(current - start));
				start = current;
			}
			else {
				log.debug("Get Active Issues");
				issues = caseManagementMgr.getActiveIssues(providerNo, this.getDemographicNo(request));
				current = System.currentTimeMillis();
				log.debug("Get Active Issues " + String.valueOf(current - start));
				start = current;
			}
			/*
			 * if(request.getSession().getAttribute("archiveView")!="true") request.setAttribute("Issues",caseManagementMgr.filterIssues(issues,providerNo,programId)); else
			 * request.setAttribute("Issues",issues);
			 */
			log.debug("Filter Issues");
			issues = caseManagementMgr.filterIssues(issues, providerNo, programId, currentFacilityId);
			current = System.currentTimeMillis();
			log.debug("FILTER ISSUES " + String.valueOf(current - start));
			start = current;

			request.setAttribute("Issues", issues);

			/* PROGRESS NOTES */
			List<CaseManagementNote> notes = null;

			log.debug("Get stale note date");
			// filter the notes by the checked issues and date if set
			UserProperty userProp = caseManagementMgr.getUserProperty(providerNo, UserProperty.STALE_NOTEDATE);
			request.setAttribute(UserProperty.STALE_NOTEDATE, userProp);
			current = System.currentTimeMillis();
			log.debug("Get stale note date " + String.valueOf(current - start));
			start = current;

			String[] checked_issues = request.getParameterValues("check_issue");
			if (checked_issues != null && checked_issues[0].trim().length() > 0) {
				// need to apply a filter
				log.debug("Get Notes with checked issues");
				request.setAttribute("checked_issues", checked_issues);
				notes = caseManagementMgr.getNotes(demoNo, checked_issues);
				notes = manageLockedNotes(notes, true, this.getUnlockedNotesMap(request));
				current = System.currentTimeMillis();
				log.debug("Get Notes with checked issues " + String.valueOf(current - start));
				start = current;
			}
			else {
				log.debug("Get Notes");
				notes = caseManagementMgr.getNotes(demoNo);
				notes = manageLockedNotes(notes, false, this.getUnlockedNotesMap(request));
				current = System.currentTimeMillis();
				log.debug("Get Notes " + String.valueOf(current - start));
				start = current;
			}

			log.debug("FETCHED " + notes.size() + " NOTES");

			// copy cpp notes
			/*
			 * HashMap issueMap = getCPPIssues(request, providerNo); Iterator<Map.Entry> iterator = issueMap.entrySet().iterator(); while( iterator.hasNext() ) { Map.Entry mapEntry
			 * = iterator.next(); String key = (String)mapEntry.getKey(); Issue value = (Issue)mapEntry.getValue(); List<CaseManagementNote>cppNotes =
			 * caseManagementMgr.getCPP(demoNo,value.getId(),userProp); String cppAdd = request.getContextPath() +
			 * "/CaseManagementEntry.do?hc=996633&method=issueNoteSave&providerNo=" + providerNo + "&demographicNo=" + demoNo + "&issue_id=" + value.getId() + "&noteId=";
			 * request.setAttribute(key,cppNotes); request.setAttribute(key+"add",cppAdd); }
			 */
			// apply role based access
			// if(request.getSession().getAttribute("archiveView")!="true")
			log.debug("Filter Notes");
			notes = caseManagementMgr.filterNotes(notes, providerNo, programId, currentFacilityId);
			current = System.currentTimeMillis();
			log.debug("FILTER NOTES " + String.valueOf(current - start));
			start = current;

			String resetFilter = request.getParameter("resetFilter");
			System.out.println("RESET FILTER " + resetFilter);
			if (resetFilter != null && resetFilter.equals("true")) {
				System.out.println("CASEMGMTVIEW RESET FILTER");
				caseForm.setFilter_providers(null);
				// caseForm.setFilter_provider("");
				caseForm.setFilter_roles(null);
				caseForm.setNote_sort(null);
			}

			// apply provider filter
			log.debug("Filter Notes Provider");
			Set providers = new HashSet();
			notes = applyProviderFilters(notes, providers, caseForm.getFilter_providers());
			current = System.currentTimeMillis();
			log.debug("FILTER NOTES PROVIDER " + String.valueOf(current - start));
			start = current;

			request.setAttribute("providers", providers);

			// apply if we are filtering on role
			log.debug("Filter on Role");
			List roles = roleMgr.getRoles();
			request.setAttribute("roles", roles);
			String[] roleId = caseForm.getFilter_roles();
			if (roleId != null && roleId.length > 0) notes = applyRoleFilter(notes, roleId);
			current = System.currentTimeMillis();
			log.debug("Filter on Role " + String.valueOf(current - start));
			start = current;

			log.debug("Pop notes with editors");
			this.caseManagementMgr.getEditors(notes);
			current = System.currentTimeMillis();
			log.debug("Pop notes with editors " + String.valueOf(current - start));
			start = current;

			/*
			 * people are changing the default sorting of notes so it's safer to explicity set it here, some one already changed it once and it reversed our sorting.
			 */
			log.debug("Apply sorting to notes");
			String noteSort = caseForm.getNote_sort();
			if (noteSort != null && noteSort.length() > 0) {
				request.setAttribute("Notes", sortNotes(notes, noteSort));
			}
			else {
				oscar.OscarProperties p = oscar.OscarProperties.getInstance();
				noteSort = p.getProperty("CMESort", "");
				if (noteSort.trim().equalsIgnoreCase("UP"))
					request.setAttribute("Notes", sortNotes(notes, "observation_date_asc"));
				else
					request.setAttribute("Notes", sortNotes(notes, "observation_date_desc"));
			}
			current = System.currentTimeMillis();
			log.debug("Apply sorting to notes " + String.valueOf(current - start));
			start = current;

			// request.setAttribute("surveys", surveyManager.getForms(demographicNo));

		}

		log.debug("Get CPP");
		CaseManagementCPP cpp = this.caseManagementMgr.getCPP(this.getDemographicNo(request));
		if (cpp == null) {
			cpp = new CaseManagementCPP();
			cpp.setDemographic_no(getDemographicNo(request));
		}
		request.setAttribute("cpp", cpp);
		caseForm.setCpp(cpp);
		current = System.currentTimeMillis();
		log.debug("Get CPP " + String.valueOf(current - start));
		start = current;

		/* get allergies */
		log.debug("Get Allergies");
		List allergies = this.caseManagementMgr.getAllergies(this.getDemographicNo(request));
		request.setAttribute("Allergies", allergies);
		current = System.currentTimeMillis();
		log.debug("Get Allergies " + String.valueOf(current - start));
		start = current;

		/* get prescriptions */
		if (tab.equals("Prescriptions")) {
			List prescriptions = null;
			if (caseForm.getPrescipt_view().equals("all")) {
				prescriptions = this.caseManagementMgr.getPrescriptions(this.getDemographicNo(request), true);
			}
			else {
				prescriptions = this.caseManagementMgr.getPrescriptions(this.getDemographicNo(request), false);
			}
			request.setAttribute("Prescriptions", prescriptions);

			// Setup RX bean start
			RxSessionBean bean = new RxSessionBean();
			bean.setProviderNo(providerNo);
			bean.setDemographicNo(Integer.parseInt(demoNo));
			request.getSession().setAttribute("RxSessionBean", bean);
			// set up RX end

		}

		/* tickler */
		if (tab != null && tab.equalsIgnoreCase("Ticklers")) {
			CustomFilter cf = new CustomFilter();
			cf.setDemographic_no(this.getDemographicNo(request));
			cf.setStatus("A");
			request.setAttribute("ticklers", ticklerManager.getTicklers(cf));
		}

		if (tab != null && tab.equalsIgnoreCase("Search")) {
			request.setAttribute("roles", roleMgr.getRoles());
			request.setAttribute("program_domain", programMgr.getProgramDomain(getProviderNo(request)));
		}

		/* set form value for e-chart */

		Locale vLocale = (Locale) se.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
		caseForm.setVlCountry(vLocale.getCountry());
		caseForm.setDemographicNo(getDemographicNo(request));

		se.setAttribute("casemgmt_DemoNo", demoNo);
		caseForm.setRootCompURL((String) se.getAttribute("casemgmt_oscar_baseurl"));
		se.setAttribute("casemgmt_VlCountry", vLocale.getCountry());

		// if we have just saved a note, remove saveNote flag
                String varName = "saveNote" + demoNo;
		Boolean saved = (Boolean) se.getAttribute(varName);
		if (saved != null && saved == true) {
			request.setAttribute("saveNote", saved);
			se.removeAttribute(varName);
		}
		current = System.currentTimeMillis();
		log.debug("VIEW Exiting " + String.valueOf(current - beginning));

		String useNewCaseMgmt = (String) request.getSession().getAttribute("newCaseManagement");
		String printPreview = (String) request.getSession().getAttribute("patientCppPrintPreview");
		if ("true".equals(printPreview)) {
			request.getSession().setAttribute("patientCppPrintPreview", "false");
			return mapping.findForward("clientHistoryPrintPreview");
		}
		else {

			if (useNewCaseMgmt != null && useNewCaseMgmt.equals("true"))
				return mapping.findForward("page.newcasemgmt.view");
			else
				return mapping.findForward("page.casemgmt.view");
		}
	}

	public ActionForward viewNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String nId = request.getParameter("noteId");
		CaseManagementNote note = this.caseManagementMgr.getNote(nId);
		request.setAttribute("noteStr", note.getNote());
		boolean raw = request.getParameter("raw").equalsIgnoreCase("true");
		request.setAttribute("raw", raw);
		return mapping.findForward("displayNote");
	}

	public ActionForward listNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("List Notes start");
		long beginning = System.currentTimeMillis();
		long start = beginning;
		long current = 0;
		String providerNo = getProviderNo(request);
		String demoNo = getDemographicNo(request);
		List notes = null;

		// set save url to be used by ajax editor
		String identUrl = request.getQueryString();
		request.setAttribute("identUrl", identUrl);

		// filter the notes by the checked issues
		// UserProperty userProp = caseManagementMgr.getUserProperty(providerNo, UserProperty.STALE_NOTEDATE);

		String[] codes = request.getParameterValues("issue_code");
		List<Issue> issues = caseManagementMgr.getIssueInfoByCode(providerNo, codes);
		StringBuffer checked_issues = new StringBuffer();
                StringBuffer cppIssues = new StringBuffer();
		String[] issueIds = new String[issues.size()];
		int idx = 0;
		for (Issue issue : issues) {
			checked_issues.append("&issue_id=" + String.valueOf(issue.getId()));
                        if( idx > 0 ) {
                            cppIssues.append(";");
                        }
                        cppIssues.append(issue.getId() + ";" + issue.getDescription());
			issueIds[idx] = String.valueOf(issue.getId());
                        idx++;
		}

		// set save Url
		String addUrl = request.getContextPath() + "/CaseManagementEntry.do?method=issueNoteSave&providerNo=" + providerNo + "&demographicNo=" + demoNo + 
				"&noteId=";
		request.setAttribute("addUrl", addUrl);
                request.setAttribute("cppIssue", cppIssues.toString());

		// set issueIds for retrieving history
		request.setAttribute("issueIds", StringUtils.join(issueIds, ","));

		// need to apply issue filter
		notes = caseManagementMgr.getNotes(demoNo, issueIds);
		notes = manageLockedNotes(notes, true, this.getUnlockedNotesMap(request));

		log.debug("FETCHED " + notes.size() + " NOTES filtered by " + StringUtils.join(issueIds, ","));
		log.debug("REFERER " + request.getRequestURL().toString() + "?" + request.getQueryString());

		String programId = (String) request.getSession().getAttribute("case_program_id");

		if (programId == null || programId.length() == 0) {
			programId = "0";
		}

		Integer currentFacilityId = request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID) != null ? (Integer) request.getSession().getAttribute(
				SessionConstants.CURRENT_FACILITY_ID) : 0;

		notes = caseManagementMgr.filterNotes(notes, providerNo, programId, currentFacilityId);
		this.caseManagementMgr.getEditors(notes);

		oscar.OscarProperties p = oscar.OscarProperties.getInstance();
		String noteSort = p.getProperty("CMESort", "");
		if (noteSort.trim().equalsIgnoreCase("UP"))
			request.setAttribute("Notes", sortNotes(notes, "observation_date_asc"));
		else
			request.setAttribute("Notes", sortNotes(notes, "observation_date_desc"));

		return mapping.findForward("listNotes");
	}

	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String programId = (String) request.getSession().getAttribute("case_program_id");

		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean) form;
		CaseManagementSearchBean searchBean = new CaseManagementSearchBean(this.getDemographicNo(request));
		searchBean.setSearchEncounterType(caseForm.getSearchEncounterType());
		searchBean.setSearchEndDate(caseForm.getSearchEndDate());
		searchBean.setSearchProgramId(caseForm.getSearchProgramId());
		searchBean.setSearchProviderNo(caseForm.getSearchProviderNo());
		searchBean.setSearchRoleId(caseForm.getSearchRoleId());
		searchBean.setSearchStartDate(caseForm.getSearchStartDate());
		searchBean.setSearchText(caseForm.getSearchText());
		List results = this.caseManagementMgr.search(searchBean);
		List filtered1 = manageLockedNotes(results, false, this.getUnlockedNotesMap(request));
		Integer currentFacilityId = (Integer) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
		List filteredResults = caseManagementMgr.filterNotes(filtered1, getProviderNo(request), programId, currentFacilityId);

		List sortedResults = this.sortNotes(filteredResults, caseForm.getNote_sort());
		request.setAttribute("search_results", sortedResults);
		return view(mapping, form, request, response);
	}

	public List sortNotes(List notes, String field) throws Exception {
		log.debug("Sorting notes by field: " + field);
		if (field == null || field.equals("") || field.equals("update_date")) {
			return notes;
		}

		if (field.equals("providerName")) {
			Collections.sort(notes, CaseManagementNote.getProviderComparator());
		}
		if (field.equals("programName")) {
			Collections.sort(notes, CaseManagementNote.getProgramComparator());
		}
		if (field.equals("roleName")) {
			Collections.sort(notes, CaseManagementNote.getRoleComparator());
		}
		if (field.equals("observation_date_asc")) {
			Collections.sort(notes, CaseManagementNote.noteObservationDateComparator);
			Collections.reverse(notes);
		}
		if (field.equals("observation_date_desc")) {
			Collections.sort(notes, CaseManagementNote.noteObservationDateComparator);
		}

		return notes;
	}

	// unlock a note temporarily - session
	/*
	 * show password
	 */
	public ActionForward unlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean) form;
		String noteId = request.getParameter("noteId");
		caseForm.setNoteId(Integer.parseInt(noteId));
		return mapping.findForward("unlockForm");
	}

	public ActionForward do_unlock_ajax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String password = request.getParameter("password");
		int noteId = Integer.parseInt(request.getParameter("noteId"));

		CaseManagementNote note = this.caseManagementMgr.getNote(request.getParameter("noteId"));
		this.caseManagementMgr.getEditors(note);
		request.setAttribute("Note", note);

		boolean success = caseManagementMgr.unlockNote(noteId, password);
		request.setAttribute("success", new Boolean(success));

		if (success) {
			Map unlockedNoteMap = this.getUnlockedNotesMap(request);
			unlockedNoteMap.put(new Long(noteId), new Boolean(success));
			request.getSession().setAttribute("unlockedNoteMap", unlockedNoteMap);
		}

		return mapping.findForward("unlock_ajax");

	}

	public ActionForward do_unlock(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean) form;

		String password = caseForm.getPassword();
		int noteId = caseForm.getNoteId();

		boolean success = caseManagementMgr.unlockNote(noteId, password);
		request.setAttribute("success", new Boolean(success));

		if (success) {
			Map unlockedNoteMap = this.getUnlockedNotesMap(request);
			unlockedNoteMap.put(new Long(noteId), new Boolean(success));
			request.getSession().setAttribute("unlockedNoteMap", unlockedNoteMap);
			return mapping.findForward("unlockSuccess");
		}
		else {
			return unlock(mapping, form, request, response);
		}

	}

	protected Map getUnlockedNotesMap(HttpServletRequest request) {
		Map map = (Map) request.getSession().getAttribute("unlockedNoteMap");
		if (map == null) {
			map = new HashMap();
		}
		return map;
	}

	protected List applyRoleFilter(List notes, String[] roleId) {

		// if no filter return everything
		if (Arrays.binarySearch(roleId, "a") >= 0) return notes;

		List filteredNotes = new ArrayList();

		for (Iterator iter = notes.listIterator(); iter.hasNext();) {
			CaseManagementNote note = (CaseManagementNote) iter.next();

			if (Arrays.binarySearch(roleId, note.getReporter_caisi_role()) >= 0) filteredNotes.add(note);
		}

		return filteredNotes;
	}

	/*
	 * This method extracts a unique list of providers, and optionally filters out all notes belonging to providerNo (arg2).
	 */
	protected List applyProviderFilter(List notes, Set providers, String providerNo) {
		boolean filter = false;
		List filteredNotes = new ArrayList();

		if (providerNo != null && providerNo.length() > 0) {
			filter = true;
		}

		for (Iterator iter = notes.iterator(); iter.hasNext();) {
			CaseManagementNote note = (CaseManagementNote) iter.next();
			providers.add(note.getProvider());
			if (!filter) {
				// no filter, add all
				filteredNotes.add(note);
			}
			else if (filter && note.getProviderNo().equals(providerNo)) {
				// correct provider
				filteredNotes.add(note);
			}
		}

		return filteredNotes;
	}

	protected List manageLockedNotes(List notes, boolean removeLockedNotes, Map unlockedNotesMap) {
		List notesNoLocked = new ArrayList();
		for (Iterator iter = notes.iterator(); iter.hasNext();) {
			CaseManagementNote note = (CaseManagementNote) iter.next();
			if (note.isLocked()) {
				if (unlockedNotesMap.get(note.getId()) != null) {
					note.setLocked(false);
				}
			}
			if (removeLockedNotes && !note.isLocked()) {
				notesNoLocked.add(note);
			}
		}
		if (removeLockedNotes) {
			return notesNoLocked;
		}
		return notes;
	}

	/*
	 * This method extracts a unique list of providers, and optionally filters out all notes belonging to providerNo (arg2).
	 */
	protected List applyProviderFilters(List notes, Set providers, String[] providerNo) {
		boolean filter = false;
		List filteredNotes = new ArrayList();

		if (providerNo != null && Arrays.binarySearch(providerNo, "a") < 0) {
			filter = true;
		}

		for (Iterator iter = notes.iterator(); iter.hasNext();) {
			CaseManagementNote note = (CaseManagementNote) iter.next();
			providers.add(note.getProvider());
			if (!filter) {
				// no filter, add all
				filteredNotes.add(note);

			}
			else {
				if (Arrays.binarySearch(providerNo, note.getProviderNo()) >= 0)
				// correct provider
					filteredNotes.add(note);
			}
		}

		return filteredNotes;
	}

	/*
	 * Retrieve CPP issuesIf not in session, load them
	 */
	protected HashMap getCPPIssues(HttpServletRequest request, String providerNo) {
		HashMap<String, Issue> issues = (HashMap<String, Issue>) request.getSession().getAttribute("CPPIssues");
		if (issues == null) {
			String[] issueCodes = { "SocHistory", "MedHistory", "Concerns", "Reminders" };
			issues = new HashMap<String, Issue>();
			for (String issue : issueCodes) {
				List<Issue> i = caseManagementMgr.getIssueInfoByCode(providerNo, issue);
				issues.put(issue, i.get(0));
			}

			request.getSession().setAttribute("CPPIssues", issues);
		}
		return issues;
	}

	public ActionForward addToDx(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String codingSystem = null;
		Properties dxProps = new Properties();
        try {
        	InputStream is = getClass().getResourceAsStream("/caisi_issues_dx.properties");   
        	dxProps.load(is);
        	codingSystem = dxProps.getProperty("coding_system");
        }catch(IOException e) {log.warn("Unable to load Dx properties file");}
        
		this.caseManagementMgr.saveToDx(getDemographicNo(request), request.getParameter("issue_code"),codingSystem);
		
		return view(mapping,form,request,response);
	}		
}
