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
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.caisi.model.CustomFilter;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramTeam;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.caisi_integrator.ws.CachedDemographicIssue;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNote;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.CachedProgram;
import org.oscarehr.caisi_integrator.ws.CachedProvider;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.ws.NoteIssue;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementSearchBean;
import org.oscarehr.casemgmt.model.CaseManagementTmpSave;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.casemgmt.web.formbeans.CaseManagementViewFormBean;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.dx.model.DxResearch;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarRx.pageUtil.RxSessionBean;

/*
 * Updated by Eugene Petruhin on 21 jan 2009 while fixing missing "New Note" link
 */
public class CaseManagementViewAction extends BaseCaseManagementViewAction {

	private static Log log = LogFactory.getLog(CaseManagementViewAction.class);
	private CaseManagementManager caseManagementManager=(CaseManagementManager)SpringUtils.getBean("caseManagementManager");
	private IssueDAO issueDao = (IssueDAO)SpringUtils.getBean("IssueDAO");
	private CaseManagementNoteDAO caseManagementNoteDao = (CaseManagementNoteDAO)SpringUtils.getBean("caseManagementNoteDAO");
	private SecUserRoleDao secUserRoleDao=(SecUserRoleDao)SpringUtils.getBean("secUserRoleDao");
	private ProgramDao programDao = (ProgramDao)SpringUtils.getBean("programDao");
	private ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean) form;
		caseForm.setFilter_provider("");
		request.setAttribute("patientCppPrintPreview", "false");

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
                       // System.out.println("Saved cpp for " + cpp.getDemographic_no());
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

		request.setAttribute("patientCppPrintPreview", "true");
		return view(mapping, form, request, response);
	}

	/* show case management view */
	/*
	 * Session variables : case_program_id casemgmt_DemoNo casemgmt_VlCountry casemgmt_msgBeans readonly
	 */
	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
                //response.setCharacterEncoding("UTF-8");
		long start = System.currentTimeMillis();
		long beginning = start;
		long current = 0;
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean) form;
		boolean useNewCaseMgmt=false;
		String useNewCaseMgmtString = (String) request.getSession().getAttribute("newCaseManagement");		
		if (useNewCaseMgmtString!=null) useNewCaseMgmt=Boolean.parseBoolean(useNewCaseMgmtString);
		
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
		ClientImage img = clientImageMgr.getClientImage(Integer.parseInt(demoNo));
		if(img != null) {
			request.setAttribute("image_exists", "true");
		}
		
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

		if (OscarProperties.getInstance().isCaisiLoaded() && !useNewCaseMgmt) {

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

		// UCF
		log.debug("Fetch Survey List");
		request.setAttribute("survey_list", surveyMgr.getAllFormsForCurrentProviderAndCurrentFacility());
		current = System.currentTimeMillis();
		log.debug("Fetch Survey List " + String.valueOf(current - start));
		

		/* ISSUES */
		if (tab.equals("Current Issues")) {
			if (useNewCaseMgmt) viewCurrentIssuesTab_newCme(request, caseForm, demoNo, programId);
			else viewCurrentIssuesTab_oldCme(request, caseForm, demoNo, programId);
		} // end Current Issues Tab

		log.debug("Get CPP");
		current = System.currentTimeMillis();
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
			List<Drug> prescriptions = null;
			boolean viewAll=caseForm.getPrescipt_view().equals("all");
			String demographicId=getDemographicNo(request);
			request.setAttribute("isIntegratorEnabled", LoggedInInfo.loggedInInfo.get().currentFacility.isIntegratorEnabled());			
			prescriptions = this.caseManagementMgr.getPrescriptions(Integer.parseInt(demographicId), viewAll);
			
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

		String printPreview = (String) request.getAttribute("patientCppPrintPreview");
		if ("true".equals(printPreview)) {
			request.setAttribute("patientCppPrintPreview", "false");
			return mapping.findForward("clientHistoryPrintPreview");
		}
		else {

			if (useNewCaseMgmt)
				return mapping.findForward("page.newcasemgmt.view");
			else
				return mapping.findForward("page.casemgmt.view");
		}
	}

	public static class IssueDisplay
	{
		public boolean writeAccess=true;
		public String codeType=null;
		public String code=null;
		public String description=null;
		public String location=null;
		public String acute=null;
		public String certain=null;
		public String major=null;
		public String resolved=null;
		public String role=null;
		public String priority=null;
		public String getCodeType() {
        	return codeType;
        }
		public String getCode() {
        	return code;
        }
		public String getDescription() {
        	return description;
        }
		public String getLocation() {
        	return location;
        }
		public String getAcute() {
        	return acute;
        }
		public String getCertain() {
        	return certain;
        }
		public String getMajor() {
        	return major;
        }
		public String getResolved() {
        	return resolved;
        }
		public String getRole() {
        	return role;
        }
		public String getPriority() {
        	return priority;
        }
		public boolean isWriteAccess() {
        	return writeAccess;
        }
		
		public void setWriteAccess(boolean writeAccess) {
        	this.writeAccess = writeAccess;
        }
		public void setCodeType(String codeType) {
        	this.codeType = codeType;
        }
		public void setCode(String code) {
        	this.code = code;
        }
		public void setDescription(String description) {
        	this.description = description;
        }
		public void setLocation(String location) {
        	this.location = location;
        }
		public void setAcute(String acute) {
        	this.acute = acute;
        }
		public void setCertain(String certain) {
        	this.certain = certain;
        }
		public void setMajor(String major) {
        	this.major = major;
        }
		public void setResolved(String resolved) {
        	this.resolved = resolved;
        }
		public void setRole(String role) {
        	this.role = role;
        }
		public void setPriority(String priority) {
        	this.priority = priority;
        }
		public String toString()
		{
			return(ReflectionToStringBuilder.toString(this));
		}
	}
	
	public static class NoteDisplay
	{
		public Integer remoteFacilityId=null;
		public String uuid=null;
		public Integer noteId=null;
		public boolean editable=false;
		public boolean hasHistory=false;
		public boolean locked=false;
		public Date observationDate=null;
		public String provider=null;
		public String status=null;
		public String program=null;
		public String location=null;
		public String role=null;
		public String note=null;
		public Integer getNoteId() {
        	return noteId;
        }
		public boolean isEditable() {
        	return editable;
        }
		public Date getObservationDate() {
        	return observationDate;
        }
		public String getProvider() {
        	return provider;
        }
		public String getStatus() {
        	return status;
        }
		public String getProgram() {
        	return program;
        }
		public String getLocation() {
        	return location;
        }
		public String getRole() {
        	return role;
        }
		public Integer getRemoteFacilityId() {
        	return remoteFacilityId;
        }
		public String getUuid() {
        	return uuid;
        }
		public boolean getHasHistory()
		{
			return(hasHistory);
		}
		public boolean isLocked() {
        	return locked;
        }
		public String getNote() {
        	return note;
        }
		
	}
	
    private void viewCurrentIssuesTab_oldCme(HttpServletRequest request, CaseManagementViewFormBean caseForm, String demoNo, String programId) throws InvocationTargetException,
            IllegalAccessException, Exception {
	    long startTime = System.currentTimeMillis();

	    LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		String providerNo=loggedInInfo.loggedInProvider.getProviderNo();
		int demographicNo=Integer.parseInt(demoNo);
		boolean hideInactiveIssues=Boolean.parseBoolean(caseForm.getHideActiveIssue());
		
		ArrayList<CheckBoxBean> checkBoxBeanList = new ArrayList<CheckBoxBean>();
		addLocalIssues(checkBoxBeanList, demographicNo, hideInactiveIssues, null);
		addRemoteIssues(checkBoxBeanList, demographicNo, hideInactiveIssues);
		
       	request.setAttribute("Issues", checkBoxBeanList);
 	log.debug("Get issues time : " + (System.currentTimeMillis()-startTime));
    	
	    log.debug("Get stale note date");
	    startTime = System.currentTimeMillis();
	    // filter the notes by the checked issues and date if set
	    UserProperty userProp = caseManagementMgr.getUserProperty(providerNo, UserProperty.STALE_NOTEDATE);
	    request.setAttribute(UserProperty.STALE_NOTEDATE, userProp);
	    log.debug("Get stale note date " + (System.currentTimeMillis()-startTime));

	    /* PROGRESS NOTES */
		startTime = System.currentTimeMillis();
	    String[] checkedIssues=request.getParameterValues("check_issue");

	    // extract just the codes for local usage
	    ArrayList<String> checkedCodeList=new ArrayList<String>();
	    if (checkedIssues != null) {
			for (String s : checkedIssues) {
				String[] temp = s.split("\\.");
				if (temp.length == 2) checkedCodeList.add(temp[1]);
				else log.warn("Unexpected parameter, wrong format : " + s);
			}
		}	
	    
	    ArrayList<NoteDisplay> notesToDisplay=new ArrayList<NoteDisplay>();

	    // deal with local notes
	    startTime = System.currentTimeMillis();
	    Collection<CaseManagementNote> localNotes = caseManagementNoteDao.findNotesByDemographicAndIssueCode(demographicNo, checkedCodeList.toArray(new String[0]));
	    localNotes = manageLockedNotes(localNotes, true, this.getUnlockedNotesMap(request));
	    localNotes = caseManagementMgr.filterNotes(localNotes, programId);                        

	    caseManagementMgr.getEditors(localNotes);

	    addLocalNotes(notesToDisplay, localNotes);
	    log.debug("FETCHED " + localNotes.size() + " NOTES in time : "+(System.currentTimeMillis()-startTime));
	    
	    // deal with remote notes
		startTime = System.currentTimeMillis();
	    addRemoteNotes(notesToDisplay, demographicNo, checkedCodeList);
	    log.debug("Get remote notes. time="+(System.currentTimeMillis()-startTime));

	    // not sure what everything else is after this
	    String resetFilter = request.getParameter("resetFilter");
	    log.debug("RESET FILTER " + resetFilter);
	    if (resetFilter != null && resetFilter.equals("true")) {
	    	log.debug("CASEMGMTVIEW RESET FILTER");
	    	caseForm.setFilter_providers(null);
	    	// caseForm.setFilter_provider("");
	    	caseForm.setFilter_roles(null);
	    	caseForm.setNote_sort(null);
	    }

	    // apply if we are filtering on role
	    log.debug("Filter on Role");
	    startTime = System.currentTimeMillis();
	    List roles = roleMgr.getRoles();
	    request.setAttribute("roles", roles);
	    String[] roleId = caseForm.getFilter_roles();
	    notesToDisplay = applyRoleFilter(notesToDisplay, roleId);
	    log.debug("Filter on Role " + (System.currentTimeMillis()-startTime));
	                
	    // filter providers
	    notesToDisplay = applyProviderFilter(notesToDisplay, caseForm.getFilter_providers());
	    
	    // set providers to display
	    HashSet<LabelValueBean> providers=new HashSet<LabelValueBean>();
	    for (NoteDisplay tempNote : notesToDisplay)
	    {
	    	String tempProvider=tempNote.getProvider();
	    	providers.add(new LabelValueBean(tempProvider,tempProvider));
	    }
	    request.setAttribute("providers", providers);
	    
	    /*
	     * people are changing the default sorting of notes so it's safer to explicity set it here, some one already changed it once and it reversed our sorting.
	     */
	    log.debug("Apply sorting to notes");
	    startTime = System.currentTimeMillis();
	    String noteSort = caseForm.getNote_sort();
	    if (noteSort != null && noteSort.length() > 0) {
	    	notesToDisplay=sortNotes(notesToDisplay, noteSort);
	    }
	    else {
	    	oscar.OscarProperties p = oscar.OscarProperties.getInstance();
	    	noteSort = p.getProperty("CMESort", "");
	    	if (noteSort.trim().equalsIgnoreCase("UP"))
	    		notesToDisplay=sortNotes(notesToDisplay, "observation_date_asc");
	    	else
	    		notesToDisplay=sortNotes(notesToDisplay, "observation_date_desc");
	    }

	    request.setAttribute("Notes", notesToDisplay);
	    log.debug("Apply sorting to notes " + (System.currentTimeMillis()-startTime));
    }

	private void viewCurrentIssuesTab_newCme(HttpServletRequest request, CaseManagementViewFormBean caseForm, String demoNo, String programId) throws InvocationTargetException,
	        IllegalAccessException, Exception {
	    LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		String providerNo=loggedInInfo.loggedInProvider.getProviderNo();

		long startTime;
		
		log.debug("Get stale note date");
		// filter the notes by the checked issues and date if set
		startTime = System.currentTimeMillis();
		UserProperty userProp = caseManagementMgr.getUserProperty(providerNo, UserProperty.STALE_NOTEDATE);
		request.setAttribute(UserProperty.STALE_NOTEDATE, userProp);
		log.debug("Get stale note date " + (System.currentTimeMillis() - startTime));

		/* PROGRESS NOTES */
		List<CaseManagementNote> notes = null;
		// here we might have a checked/unchecked issue that is remote and has no issue_id (they're all zero).
		String[] checkedIssues = request.getParameterValues("check_issue");
		if (checkedIssues != null && checkedIssues[0].trim().length() > 0) {
			// need to apply a filter
			log.debug("Get Notes with checked issues");
			startTime = System.currentTimeMillis();
			request.setAttribute("checked_issues", checkedIssues);
			notes = caseManagementMgr.getNotes(demoNo, checkedIssues);
			notes = manageLockedNotes(notes, true, this.getUnlockedNotesMap(request));
			log.debug("Get Notes with checked issues " + (System.currentTimeMillis() - startTime));
		} else { // get all notes
			log.debug("Get Notes");
			startTime = System.currentTimeMillis();
			notes = caseManagementMgr.getNotes(demoNo);
			notes = manageLockedNotes(notes, false, this.getUnlockedNotesMap(request));
			log.debug("Get Notes " + (System.currentTimeMillis() - startTime));
		}

		log.debug("FETCHED " + notes.size() + " NOTES");

		// copy cpp notes
		/*
		 * HashMap issueMap = getCPPIssues(request, providerNo); Iterator<Map.Entry> iterator = issueMap.entrySet().iterator(); while( iterator.hasNext() ) { Map.Entry mapEntry = iterator.next(); String key = (String)mapEntry.getKey(); Issue value =
		 * (Issue)mapEntry.getValue(); List<CaseManagementNote>cppNotes = caseManagementMgr.getCPP(demoNo,value.getId(),userProp); String cppAdd = request.getContextPath() + "/CaseManagementEntry.do?hc=996633&method=issueNoteSave&providerNo=" + providerNo
		 * + "&demographicNo=" + demoNo + "&issue_id=" + value.getId() + "&noteId="; request.setAttribute(key,cppNotes); request.setAttribute(key+"add",cppAdd); }
		 */
		// apply role based access
		// if(request.getSession().getAttribute("archiveView")!="true")
		startTime = System.currentTimeMillis();
		String resetFilter = request.getParameter("resetFilter");
		log.debug("RESET FILTER " + resetFilter);
		if (resetFilter != null && resetFilter.equals("true")) {
			log.debug("CASEMGMTVIEW RESET FILTER");
			caseForm.setFilter_providers(null);
			// caseForm.setFilter_provider("");
			caseForm.setFilter_roles(null);
			caseForm.setNote_sort(null);
		}

		log.debug("Filter Notes");

		// filter notes based on role and program/provider mappings
		notes = caseManagementMgr.filterNotes(notes, programId);
		log.debug("FILTER NOTES " + (System.currentTimeMillis() - startTime));

		// apply provider filter
		log.debug("Filter Notes Provider");
		startTime = System.currentTimeMillis();
		Set providers = new HashSet();
		notes = applyProviderFilters(notes, providers, caseForm.getFilter_providers());
		log.debug("FILTER NOTES PROVIDER " + (System.currentTimeMillis() - startTime));

		request.setAttribute("providers", providers);

		// apply if we are filtering on role
		log.debug("Filter on Role");
		startTime = System.currentTimeMillis();
		List roles = roleMgr.getRoles();
		request.setAttribute("roles", roles);
		String[] roleId = caseForm.getFilter_roles();
		if (roleId != null && roleId.length > 0) notes = applyRoleFilter(notes, roleId);
		log.debug("Filter on Role " + (System.currentTimeMillis() - startTime));

		// this is a local filter and does not apply to remote notes
		log.debug("Pop notes with editors");
		startTime = System.currentTimeMillis();
		this.caseManagementMgr.getEditors(notes);
		log.debug("Pop notes with editors " + (System.currentTimeMillis() - startTime));

		/*
		 * people are changing the default sorting of notes so it's safer to explicity set it here, some one already changed it once and it reversed our sorting.
		 */
		log.debug("Apply sorting to notes");
		startTime = System.currentTimeMillis();
		String noteSort = caseForm.getNote_sort();
		if (noteSort != null && noteSort.length() > 0) {
			request.setAttribute("Notes", sortNotes(notes, noteSort));
		} else {
			oscar.OscarProperties p = oscar.OscarProperties.getInstance();
			noteSort = p.getProperty("CMESort", "");
			if (noteSort.trim().equalsIgnoreCase("UP")) request.setAttribute("Notes", sortNotes(notes, "observation_date_asc"));
			else request.setAttribute("Notes", sortNotes(notes, "observation_date_desc"));
		}
		log.debug("Apply sorting to notes " + (System.currentTimeMillis() - startTime));

		// request.setAttribute("surveys", surveyManager.getForms(demographicNo));
	}
	
	private List sortNotes(List notes, String field) throws Exception {
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

	private List applyRoleFilter(List notes, String[] roleId) {

		// if no filter return everything
		if (Arrays.binarySearch(roleId, "a") >= 0) return notes;

		List filteredNotes = new ArrayList();

		for (Iterator iter = notes.listIterator(); iter.hasNext();) {
			CaseManagementNote note = (CaseManagementNote) iter.next();

			if (Arrays.binarySearch(roleId, note.getReporter_caisi_role()) >= 0) filteredNotes.add(note);
		}

		return filteredNotes;
	}

	private List<CaseManagementNote> manageLockedNotes(List<CaseManagementNote> notes, boolean removeLockedNotes, Map unlockedNotesMap) {
		List<CaseManagementNote> notesNoLocked = new ArrayList<CaseManagementNote>();
		for (CaseManagementNote note : notes) {
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

	private List applyProviderFilters(List notes, Set providers, String[] providerNo) {
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
	
    private static boolean hasRole(List<SecUserRole> roles, String role)
    {
    	if (roles==null) return(false);
    	
    	for (SecUserRole roleTmp : roles)
    	{
    		if (roleTmp.getRoleName().equals(role)) return(true);
    	}
    	
    	return(false);
    }
    
	private void addRemoteNotes(ArrayList<NoteDisplay> notesToDisplay, int demographicNo, ArrayList<String> issueCodesToDisplay) {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		List<SecUserRole> roles=secUserRoleDao.getUserRoles(loggedInInfo.loggedInProvider.getProviderNo());

		if (!loggedInInfo.currentFacility.isIntegratorEnabled()) return;

		try {
			List<CachedDemographicNote> linkedNotes = CaisiIntegratorManager.getLinkedNotes(demographicNo);

			for (CachedDemographicNote cachedDemographicNote : linkedNotes) {
				try {
					
					// filter on issues to display
					if (hasIssueToBeDisplayed(cachedDemographicNote, issueCodesToDisplay)) {
						
						// filter on role based access
						if (hasRole(roles, cachedDemographicNote.getRole())) {
							notesToDisplay.add(getNoteToDisplay(cachedDemographicNote));
						}
					}
				} catch (Exception e) {
					log.error("Unexpected error.", e);
				}
			}
		} catch (Exception e) {
			log.error("Unexpected error.", e);
		}
    }

	private boolean hasIssueToBeDisplayed(CachedDemographicNote cachedDemographicNote, ArrayList<String> issueCodesToDisplay) {
		// no issue selected means display all
		if (issueCodesToDisplay==null || issueCodesToDisplay.size()==0) return(true);
		
		for (NoteIssue noteIssue : cachedDemographicNote.getIssues())
		{
			// yes I know this is flawed in that it's ignoreing the code type.
			// right now we don't support code type properly on the caisi side.
			if (issueCodesToDisplay.contains(noteIssue.getIssueCode())) return(true);
		}
		
		return(false);
	}

	private NoteDisplay getNoteToDisplay(CachedDemographicNote cachedDemographicNote) throws MalformedURLException {
    	NoteDisplay noteDisplay=new NoteDisplay();
    	
    	noteDisplay.editable=false;
    	noteDisplay.observationDate=cachedDemographicNote.getObservationDate();
    	
    	CachedFacility cachedFacility=CaisiIntegratorManager.getRemoteFacility(cachedDemographicNote.getCachedDemographicNoteCompositePk().getIntegratorFacilityId());
    	if (cachedFacility!=null) noteDisplay.location="remote: "+cachedFacility.getName();
    	else noteDisplay.location="remote: name unavailable";
    	
    	noteDisplay.remoteFacilityId=cachedDemographicNote.getCachedDemographicNoteCompositePk().getIntegratorFacilityId();
    	noteDisplay.uuid=cachedDemographicNote.getCachedDemographicNoteCompositePk().getUuid();
    	
    	FacilityIdIntegerCompositePk programPk=new FacilityIdIntegerCompositePk();
    	programPk.setIntegratorFacilityId(cachedDemographicNote.getCachedDemographicNoteCompositePk().getIntegratorFacilityId());
    	programPk.setCaisiItemId(cachedDemographicNote.getCaisiProgramId());
    	CachedProgram remoteProgram=CaisiIntegratorManager.getRemoteProgram(programPk);
    	if (remoteProgram!=null) noteDisplay.program=remoteProgram.getName();
    	else noteDisplay.program="Unavailable";
    	
    	FacilityIdStringCompositePk providerPk=new FacilityIdStringCompositePk();
    	providerPk.setIntegratorFacilityId(cachedDemographicNote.getCachedDemographicNoteCompositePk().getIntegratorFacilityId());
    	providerPk.setCaisiItemId(cachedDemographicNote.getObservationCaisiProviderId());
    	CachedProvider remoteProvider=CaisiIntegratorManager.getProvider(providerPk);
    	if (remoteProvider!=null) noteDisplay.provider=remoteProvider.getLastName()+", "+remoteProvider.getFirstName();
    	else noteDisplay.provider="Unavailable";
    	
    	noteDisplay.role=cachedDemographicNote.getRole();
    	noteDisplay.status=cachedDemographicNote.getSigningCaisiProviderId()!=null?"Signed":"Unsigned";
    	noteDisplay.note=cachedDemographicNote.getNote();
    	
    	return(noteDisplay);
	}

	private void addLocalNotes(ArrayList<NoteDisplay> notesToDisplay, Collection<CaseManagementNote> localNotes) {	    
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		
		for (CaseManagementNote localNote : localNotes)
	    {
	    	NoteDisplay noteDisplay=new NoteDisplay();
	    	
	    	noteDisplay.editable=!localNote.isSigned() || (loggedInInfo.loggedInProvider.getProviderNo().equals(localNote.getProviderNo()) && !localNote.isLocked());
	    	noteDisplay.observationDate=localNote.getObservation_date();
	    	noteDisplay.location="local";
	    	noteDisplay.noteId=localNote.getId().intValue();
	    	noteDisplay.hasHistory=localNote.getHasHistory();
	    	noteDisplay.locked=localNote.isLocked();
	    	
	    	try
	    	{
		    	Program program=programDao.getProgram(Integer.parseInt(localNote.getProgram_no()));
		    	noteDisplay.program=program.getName();
	    	}
	    	catch (Exception e)
	    	{
	    		log.error("Error, note is missing program_no, or program_no is invalid. ProgramNo="+localNote.getProgram_no());
	    		noteDisplay.program="Error, not available.";
	    	}
	    	
	    	Provider provider=providerDao.getProvider(localNote.getProviderNo());
	    	noteDisplay.provider=provider.getFormattedName();
	    	
	    	noteDisplay.role=localNote.getRoleName();
	    	noteDisplay.status=localNote.isSigned()?"Signed":"Unsigned";
	    	noteDisplay.note=localNote.getNote();
	    	
	    	notesToDisplay.add(noteDisplay);
	    }
    }

	protected void addRemoteIssues(ArrayList<CheckBoxBean> checkBoxBeanList, int demographicNo, boolean hideInactiveIssues) {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();

		if (!loggedInInfo.currentFacility.isIntegratorEnabled()) return;

		try {
			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
			List<CachedDemographicIssue> remoteIssues = demographicWs.getLinkedCachedDemographicIssuesByDemographicId(demographicNo);

			for (CachedDemographicIssue cachedDemographicIssue : remoteIssues) {
				try {
						IssueDisplay issueDisplay=null;
					
					if (!hideInactiveIssues) issueDisplay=getIssueToDisplay(cachedDemographicIssue); 
					else if (!cachedDemographicIssue.isResolved()) issueDisplay=getIssueToDisplay(cachedDemographicIssue);
					
					if (issueDisplay!=null)
					{
						CheckBoxBean checkBoxBean=new CheckBoxBean();
						checkBoxBean.setIssueDisplay(issueDisplay);
			        	checkBoxBean.setUsed(caseManagementNoteDao.haveIssue(issueDisplay.getCode(), demographicNo));
						checkBoxBeanList.add(checkBoxBean);
					}
			} catch (Exception e) {
					log.error("Unexpected error.", e);
				}
			}
		} catch (Exception e) {
			log.error("Unexpected error.", e);
		}
	}

	private IssueDisplay getIssueToDisplay(CachedDemographicIssue cachedDemographicIssue) throws MalformedURLException {
		IssueDisplay issueDisplay = new IssueDisplay();

		issueDisplay.writeAccess=true;
		issueDisplay.acute = cachedDemographicIssue.isAcute() ? "acute" : "chronic";
		issueDisplay.certain = cachedDemographicIssue.isCertain() ? "certain" : "uncertain";
		issueDisplay.code = cachedDemographicIssue.getFacilityDemographicIssuePk().getIssueCode();
		issueDisplay.codeType = "ICD10"; // temp hard coded hack till issue is resolved

		Issue issue = null;
		// temp hard coded icd hack till issue is resolved
		if ("ICD10".equalsIgnoreCase(OscarProperties.getInstance().getProperty("COMMUNITY_ISSUE_CODETYPE").toUpperCase())) {
			issue = issueDao.findIssueByCode(cachedDemographicIssue.getFacilityDemographicIssuePk().getIssueCode());
		}

		if (issue != null) {
			issueDisplay.description = issue.getDescription();
			issueDisplay.priority = issue.getPriority();
			issueDisplay.role = issue.getRole();
		} else {
			issueDisplay.description = "Not Available";
			issueDisplay.priority = "Not Available";
			issueDisplay.role = "Not Available";
		}

		Integer remoteFacilityId = cachedDemographicIssue.getFacilityDemographicIssuePk().getIntegratorFacilityId();
		CachedFacility remoteFacility = CaisiIntegratorManager.getRemoteFacility(remoteFacilityId);
		if (remoteFacility != null) issueDisplay.location = "remote: " + remoteFacility.getName();
		else issueDisplay.location = "remote: name unavailable";

		issueDisplay.major = cachedDemographicIssue.isMajor() ? "major" : "not major";
		issueDisplay.resolved = cachedDemographicIssue.isResolved() ? "resolved" : "unresolved";

		return (issueDisplay);
	}

	protected void addLocalIssues(ArrayList<CheckBoxBean> checkBoxBeanList, Integer demographicNo, boolean hideInactiveIssues, Integer programId) {
		List<CaseManagementIssue> localIssues = caseManagementManager.getIssues(demographicNo, hideInactiveIssues?false:null);

		for (CaseManagementIssue cmi : localIssues)
		{
				CheckBoxBean checkBoxBean=new CheckBoxBean();
			
			checkBoxBean.setIssue(cmi);
			
		IssueDisplay issueDisplay = getIssueDisplay(programId, cmi);
			checkBoxBean.setIssueDisplay(issueDisplay);
			
        	checkBoxBean.setUsed(caseManagementNoteDao.haveIssue(cmi.getIssue().getCode(), demographicNo));
			
			checkBoxBeanList.add(checkBoxBean);
		}
	}

	protected IssueDisplay getIssueDisplay(Integer programId, CaseManagementIssue cmi) {
	    IssueDisplay issueDisplay=new IssueDisplay();
	    
	    if (programId!=null) issueDisplay.writeAccess=cmi.isWriteAccess(programId);
	    
	    issueDisplay.acute=cmi.isAcute()?"acute":"chronic";
	    issueDisplay.certain=cmi.isCertain()?"certain":"uncertain";
	    
	    long issueId=cmi.getIssue_id();
	    Issue issue=issueDao.getIssue(issueId);
	    
	    issueDisplay.code=issue.getCode();
	    issueDisplay.codeType=OscarProperties.getInstance().getProperty("COMMUNITY_ISSUE_CODETYPE").toUpperCase();
	    issueDisplay.description=issue.getDescription();
	    issueDisplay.location="local";
	    issueDisplay.major=cmi.isMajor()?"major":"not major";
	    issueDisplay.priority=issue.getPriority();
	    issueDisplay.resolved=cmi.isResolved()?"resolved":"unresolved";
	    issueDisplay.role=issue.getRole();
	    return issueDisplay;
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
		Collection notes = null;

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
                        cppIssues.append(issue.getId()+";"+issue.getCode()+";"+issue.getDescription());
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
		notes = caseManagementMgr.getActiveNotes(demoNo, issueIds);
		notes = manageLockedNotes(notes, true, this.getUnlockedNotesMap(request));

		log.debug("FETCHED " + notes.size() + " NOTES filtered by " + StringUtils.join(issueIds, ","));
		log.debug("REFERER " + request.getRequestURL().toString() + "?" + request.getQueryString());

		String programId = (String) request.getSession().getAttribute("case_program_id");

		if (programId == null || programId.length() == 0) {
			programId = "0";
		}

		notes = caseManagementMgr.filterNotes(notes, programId);
		this.caseManagementMgr.getEditors(notes);
		
		List lcme = new ArrayList();
		for (Object obj : notes) {
		    CaseManagementNote cmn = (CaseManagementNote)obj;
		    lcme.addAll(caseManagementMgr.getExtByNote(cmn.getId()));
		}
		request.setAttribute("NoteExts", lcme);
        request.setAttribute("Notes", notes);
		/*
		oscar.OscarProperties p = oscar.OscarProperties.getInstance();
		String noteSort = p.getProperty("CMESort", "");
		if (noteSort.trim().equalsIgnoreCase("UP"))
			request.setAttribute("Notes", sortNotes(notes, "observation_date_asc"));
		else
			request.setAttribute("Notes", sortNotes(notes, "observation_date_desc"));
                 */

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
		List results = caseManagementMgr.search(searchBean);
		Collection filtered1 = manageLockedNotes(results, false, this.getUnlockedNotesMap(request));
		List filteredResults = caseManagementMgr.filterNotes(filtered1, programId);

		List sortedResults = sortNotes_old(filteredResults, caseForm.getNote_sort());
		request.setAttribute("search_results", sortedResults);
		return view(mapping, form, request, response);
	}
	
	private List sortNotes_old(Collection<CaseManagementNote> notes, String field) throws Exception {
		log.debug("Sorting notes by field: " + field);
		
		ArrayList<CaseManagementNote> resultsSorted=new ArrayList<CaseManagementNote>(notes);
		
		if (field == null || field.equals("") || field.equals("update_date")) {
			return resultsSorted;
		}

		if (field.equals("providerName")) {
			Collections.sort(resultsSorted, CaseManagementNote.getProviderComparator());
		}
		if (field.equals("programName")) {
			Collections.sort(resultsSorted, CaseManagementNote.getProgramComparator());
		}
		if (field.equals("roleName")) {
			Collections.sort(resultsSorted, CaseManagementNote.getRoleComparator());
		}
		if (field.equals("observation_date_asc")) {
			Collections.sort(resultsSorted, CaseManagementNote.noteObservationDateComparator);
			Collections.reverse(resultsSorted);
		}
		if (field.equals("observation_date_desc")) {
			Collections.sort(resultsSorted, CaseManagementNote.noteObservationDateComparator);
		}

		return resultsSorted;
	}
	
	private ArrayList sortNotes(ArrayList<NoteDisplay> notes, String field) throws Exception {
		log.debug("Sorting notes by field: " + field);
		
		if (field == null || field.equals("") || field.equals("update_date")) {
			return notes;
		}

		if (field.equals("providerName")) {
			Collections.sort(notes, noteProviderComparator);
		}
		if (field.equals("programName")) {
			Collections.sort(notes, noteRoleComparator);
		}
		if (field.equals("observation_date_asc")) {
			Collections.sort(notes, noteObservationDateComparator);
			Collections.reverse(notes);
		}
		if (field.equals("observation_date_desc")) {
			Collections.sort(notes, noteObservationDateComparator);
		}

		return notes;
	}

	public static Comparator<NoteDisplay> noteProviderComparator = new Comparator<NoteDisplay>() {
		public int compare(NoteDisplay note1, NoteDisplay note2) {
			if (note1 == null || note2 == null) {
				return 0;
			}

			return note1.provider.compareTo(note2.provider);
		}
	};

	public static Comparator<NoteDisplay> noteProgramComparator = new Comparator<NoteDisplay>() {
		public int compare(NoteDisplay note1, NoteDisplay note2) {
			if (note1 == null || note1.program == null || note2 == null || note2.program == null) {
				return 0;
			}
			return note1.program.compareTo(note2.program);
		}
	};

	public static Comparator<NoteDisplay> noteRoleComparator = new Comparator<NoteDisplay>() {
		public int compare(NoteDisplay note1, NoteDisplay note2) {
			if (note1 == null || note2 == null) {
				return 0;
			}
			return note1.role.compareTo(note2.role);
		}
	};

	public static Comparator<NoteDisplay> noteObservationDateComparator = new Comparator<NoteDisplay>() {
		public int compare(NoteDisplay note1, NoteDisplay note2) {
			if (note1 == null || note2 == null) {
				return 0;
			}

			return note2.observationDate.compareTo(note1.observationDate);
		}
	};
	
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

	private ArrayList<NoteDisplay> applyRoleFilter(ArrayList<NoteDisplay> notes, String[] roleId) {

		if (roleId==null || hasRole(roleId, "a")) return(notes);

		ArrayList<NoteDisplay> filteredNotes = new ArrayList<NoteDisplay>();

		for (NoteDisplay note : notes) {
			if (hasRole(roleId, note.getRole())) filteredNotes.add(note);
		}

		return filteredNotes;
	}

	private static boolean hasRole(String[] roleId, String role)
	{
		for (String s : roleId)
		{
			if (s.equals(role)) return(true);
		}
		
		return(false);
	}
	
	
	private Collection<CaseManagementNote> manageLockedNotes(Collection<CaseManagementNote> notes, boolean removeLockedNotes, Map unlockedNotesMap) {
		List<CaseManagementNote> notesNoLocked = new ArrayList<CaseManagementNote>();
		for (CaseManagementNote note : notes) {
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

	private ArrayList<NoteDisplay> applyProviderFilter(ArrayList<NoteDisplay> notes, String[] providerName) {
		ArrayList<NoteDisplay> filteredNotes = new ArrayList<NoteDisplay>();

		// no list, or empty list, or list of no providers
		if (providerName == null || providerName.length==0 || providerName[0].length()==0) return(notes);

		for (NoteDisplay note : notes) {
			String tempName=note.getProvider();
			
			for (String temp : providerName)
			{
				if (tempName.equals(temp)) filteredNotes.add(note);
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
			String[] issueCodes = { "SocHistory", "MedHistory", "Concerns", "Reminders", "FamHistory", "RiskFactors" };
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
        
		this.caseManagementMgr.saveToDx(getDemographicNo(request), request.getParameter("issue_code"),codingSystem,false);
		
		return view(mapping,form,request,response);
	}		
}
