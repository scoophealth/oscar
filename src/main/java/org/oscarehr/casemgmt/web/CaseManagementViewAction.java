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

package org.oscarehr.casemgmt.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.caisi.model.CustomFilter;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramTeam;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.billing.CA.ON.dao.BillingClaimDAO;
import org.oscarehr.billing.CA.ON.model.BillingClaimHeader1;
import org.oscarehr.caisi_integrator.ws.CachedDemographicIssue;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNote;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNoteCompositePk;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.NoteIssue;
import org.oscarehr.casemgmt.common.Colour;
import org.oscarehr.casemgmt.common.EChartNoteEntry;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.casemgmt.model.CaseManagementSearchBean;
import org.oscarehr.casemgmt.model.CaseManagementTmpSave;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.casemgmt.web.formbeans.CaseManagementViewFormBean;
import org.oscarehr.common.dao.CaseManagementIssueNotesDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.dao.EncounterFormDao;
import org.oscarehr.common.dao.GroupNoteDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.GroupNoteLink;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.eyeform.EyeformInit;
import org.oscarehr.eyeform.dao.FollowUpDao;
import org.oscarehr.eyeform.dao.MacroDao;
import org.oscarehr.eyeform.dao.TestBookRecordDao;
import org.oscarehr.eyeform.model.EyeformFollowUp;
import org.oscarehr.eyeform.model.EyeformTestBook;
import org.oscarehr.eyeform.model.Macro;
import org.oscarehr.provider.web.CppPreferencesUIBean;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.eform.EFormUtil;
import oscar.oscarEncounter.data.EctFormData;
import oscar.oscarEncounter.data.EctFormData.PatientForm;
import oscar.oscarRx.pageUtil.RxSessionBean;
import oscar.util.OscarRoleObjectPrivilege;

/*
 * Updated by Eugene Petruhin on 21 jan 2009 while fixing missing "New Note" link
 */
public class CaseManagementViewAction extends BaseCaseManagementViewAction {

	private static final Integer QUICK_CHART = 20;
	private static final Integer FULL_CHART = -1;
	private static final Integer MAX_INVOICES = 20;
	private static Logger logger = MiscUtils.getLogger();
	private CaseManagementManager caseManagementManager = (CaseManagementManager) SpringUtils.getBean("caseManagementManager");
	private IssueDAO issueDao = (IssueDAO) SpringUtils.getBean("IssueDAO");
	private CaseManagementNoteDAO caseManagementNoteDao = (CaseManagementNoteDAO) SpringUtils.getBean("caseManagementNoteDAO");
	private SecUserRoleDao secUserRoleDao = (SecUserRoleDao) SpringUtils.getBean("secUserRoleDao");
	private GroupNoteDao groupNoteDao = (GroupNoteDao) SpringUtils.getBean("groupNoteDao");
	private DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private CaseManagementIssueNotesDao cmeIssueNotesDao = (CaseManagementIssueNotesDao)SpringUtils.getBean("caseManagementIssueNotesDao");
	private BillingClaimDAO billingClaimDao = (BillingClaimDAO)SpringUtils.getBean("billingClaimDAO");
	private EFormDataDao eFormDataDao = (EFormDataDao) SpringUtils.getBean("EFormDataDao");

	static {
		//temporary..need something generic;
		EyeformInit.init();
	}
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

			LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
			caseManagementMgr.saveCPP(cpp, loggedInInfo.loggedInProvider.getProviderNo());

			// caseManagementMgr.saveEctWin(ectWin);
		} else response.sendError(HttpServletResponse.SC_FORBIDDEN);

		return null;
	}

	/* save CPP for patient */
	public ActionForward patientCPPSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("patientCPPSave");
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean) form;
		CaseManagementCPP cpp = caseForm.getCpp();
		cpp.setUpdate_date(new Date());
		cpp.setDemographic_no(caseForm.getDemographicNo());

		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		caseManagementMgr.saveCPP(cpp, loggedInInfo.loggedInProvider.getProviderNo());
		addMessage(request, "cpp.saved");

		return view(mapping, form, request, response);
	}

	public ActionForward patientCppPrintPreview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("patientCPPSave");

		request.setAttribute("patientCppPrintPreview", "true");
		return view(mapping, form, request, response);
	}

	public ActionForward viewNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// response.setCharacterEncoding("UTF-8");
		long start = System.currentTimeMillis();
		long beginning = start;
		long current = 0;
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean) form;
		boolean useNewCaseMgmt = false;
		String useNewCaseMgmtString = (String) request.getSession().getAttribute("newCaseManagement");
		if (useNewCaseMgmtString != null) useNewCaseMgmt = Boolean.parseBoolean(useNewCaseMgmtString);

		logger.debug("Starting VIEW");
		String tab = request.getParameter("tab");
		if (tab == null) {
			tab = CaseManagementViewFormBean.tabs[0];
		}
		HttpSession se = request.getSession();
		if (se.getAttribute("userrole") == null) return mapping.findForward("expired");

		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();

		String demoNo = getDemographicNo(request);

		logger.debug("is client in program");
		// need to check to see if the client is in our program domain
		// if not...don't show this screen!
		String roles = (String) se.getAttribute("userrole");
		if (OscarProperties.getInstance().isOscarLearning() && roles != null && roles.indexOf("moderator") != -1) {
			logger.info("skipping domain check..provider is a moderator");
		} else if (!caseManagementMgr.isClientInProgramDomain(loggedInInfo.loggedInProvider.getProviderNo(), demoNo)) {
			return mapping.findForward("domain-error");
		}
		String programId = (String) request.getSession().getAttribute("case_program_id");

		viewCurrentIssuesTab_newCmeNotes(request, caseForm, demoNo, programId);

		return mapping.findForward("ajaxDisplayNotes");
	}

	/* show case management view */
	/*
	 * Session variables : case_program_id casemgmt_DemoNo casemgmt_VlCountry casemgmt_msgBeans readonly
	 */
	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// response.setCharacterEncoding("UTF-8");
		long start = System.currentTimeMillis();
		long beginning = start;
		long current = 0;
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean) form;
		boolean useNewCaseMgmt = false;
		String useNewCaseMgmtString = (String) request.getSession().getAttribute("newCaseManagement");
		if (useNewCaseMgmtString != null) useNewCaseMgmt = Boolean.parseBoolean(useNewCaseMgmtString);

		logger.debug("Starting VIEW");
		String tab = request.getParameter("tab");
		if (tab == null) {
			tab = CaseManagementViewFormBean.tabs[0];
		}
		HttpSession se = request.getSession();
		if (se.getAttribute("userrole") == null) return mapping.findForward("expired");

		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();

		String demoNo = getDemographicNo(request);

		logger.debug("is client in program");
		// need to check to see if the client is in our program domain
		// if not...don't show this screen!
		String roles = (String) se.getAttribute("userrole");
		if (OscarProperties.getInstance().isOscarLearning() && roles != null && roles.indexOf("moderator") != -1) {
			logger.info("skipping domain check..provider is a moderator");
		} else if (!caseManagementMgr.isClientInProgramDomain(loggedInInfo.loggedInProvider.getProviderNo(), demoNo)) {
			return mapping.findForward("domain-error");
		}

		current = System.currentTimeMillis();
		logger.debug("client in program " + String.valueOf(current - start));
		start = current;

		request.setAttribute("casemgmt_demoName", getDemoName(demoNo));
		request.setAttribute("casemgmt_demoAge", getDemoAge(demoNo));
		request.setAttribute("casemgmt_demoDOB", getDemoDOB(demoNo));
		request.setAttribute("demographicNo", demoNo);

		logger.debug("client Image?");
		// get client image
		ClientImage img = clientImageMgr.getClientImage(Integer.parseInt(demoNo));
		if (img != null) {
			request.setAttribute("image_exists", "true");
		}

		current = System.currentTimeMillis();
		logger.debug("client image " + String.valueOf(current - start));
		start = current;

		String programId = (String) request.getSession().getAttribute("case_program_id");

		if (programId == null || programId.length() == 0) {
			programId = "0";
		}

		logger.debug("is there a tmp note?");
		// check to see if there is an unsaved note
		// if there is see if casemanagemententry has already handled it
		// if it has, disregard unsaved note; if it has not then set attribute
		CaseManagementTmpSave tmpsavenote = this.caseManagementMgr.restoreTmpSave(loggedInInfo.loggedInProvider.getProviderNo(), demoNo, programId);
		if (tmpsavenote != null) {
			String restoring = (String) se.getAttribute("restoring");
			if (restoring == null) request.setAttribute("can_restore", new Boolean(true));
			else se.setAttribute("restoring", null);
		}

		current = System.currentTimeMillis();
		logger.debug("tmp note " + String.valueOf(current - start));
		start = current;

		logger.debug("Get admission");
		String teamName = "";
		Admission admission = admissionMgr.getCurrentAdmission(programId, Integer.valueOf(demoNo));
		current = System.currentTimeMillis();
		logger.debug("Get admission " + String.valueOf(current - start));
		start = current;

		if (admission != null) {
			logger.debug("Get teams");
			List teams = programMgr.getProgramTeams(programId);
			current = System.currentTimeMillis();
			logger.debug("Get teams " + String.valueOf(current - start));
			start = current;

			for (Iterator i = teams.iterator(); i.hasNext();) {
				logger.debug("Searching teams");
				ProgramTeam team = (ProgramTeam) i.next();
				String id1 = Integer.toString(team.getId());
				String id2 = Integer.toString(admission.getTeamId());
				if (id1.equals(id2)) teamName = team.getName();
			}
		}
		request.setAttribute("teamName", teamName);

		if (OscarProperties.getInstance().isCaisiLoaded() && !useNewCaseMgmt) {

			logger.debug("Get program providers");
			List<String> teamMembers = new ArrayList<String>();
			List<ProgramProvider> ps = programMgr.getProgramProviders(programId);
			current = System.currentTimeMillis();
			logger.debug("Get program providers " + String.valueOf(current - start));
			start = current;

			for (Iterator<ProgramProvider> j = ps.iterator(); j.hasNext();) {
				ProgramProvider pp = j.next();
				logger.debug("Get program provider teams");
				for (Iterator<ProgramTeam> k = pp.getTeams().iterator(); k.hasNext();) {
					ProgramTeam pt = k.next();
					if (pt.getName().equals(teamName)) {
						teamMembers.add(pp.getProvider().getFormattedName());
					}
				}
				current = System.currentTimeMillis();
				logger.debug("Get program provider teams " + String.valueOf(current - start));
				start = current;

			}
			request.setAttribute("teamMembers", teamMembers);

			/* prepare new form list for patient */
			EncounterFormDao encounterFormDao=(EncounterFormDao)SpringUtils.getBean("encounterFormDao");
			se.setAttribute("casemgmt_newFormBeans", encounterFormDao.findAll());

			/* prepare messenger list */
			se.setAttribute("casemgmt_msgBeans", this.caseManagementMgr.getMsgBeans(new Integer(demoNo)));

			// readonly access to define creat a new note button in jsp.
			se.setAttribute("readonly", new Boolean(this.caseManagementMgr.hasAccessRight("note-read-only", "access", loggedInInfo.loggedInProvider.getProviderNo(), demoNo, (String) se.getAttribute("case_program_id"))));

		}
		/* Dx */
		List<Dxresearch> dxList = this.caseManagementMgr.getDxByDemographicNo(demoNo);
		Map<String, Dxresearch> dxMap = new HashMap<String, Dxresearch>();
		for (Dxresearch dx : dxList) {
			dxMap.put(dx.getDxresearchCode(), dx);
		}
		request.setAttribute("dxMap", dxMap);

		// UCF
		logger.debug("Fetch Survey List");
		request.setAttribute("survey_list", surveyMgr.getAllFormsForCurrentProviderAndCurrentFacility());
		current = System.currentTimeMillis();
		logger.debug("Fetch Survey List " + String.valueOf(current - start));

		/* ISSUES */
		if (tab.equals("Current Issues")) {
			if (useNewCaseMgmt) viewCurrentIssuesTab_newCme(request, caseForm, demoNo, programId);
			else viewCurrentIssuesTab_oldCme(request, caseForm, demoNo, programId);
		} // end Current Issues Tab

		logger.debug("Get CPP");
		current = System.currentTimeMillis();
		CaseManagementCPP cpp = this.caseManagementMgr.getCPP(this.getDemographicNo(request));
		if (cpp == null) {
			cpp = new CaseManagementCPP();
			cpp.setDemographic_no(getDemographicNo(request));
		}
		request.setAttribute("cpp", cpp);
		caseForm.setCpp(cpp);
		current = System.currentTimeMillis();
		logger.debug("Get CPP " + String.valueOf(current - start));
		start = current;

		/* get allergies */
		logger.debug("Get Allergies");
		List allergies = this.caseManagementMgr.getAllergies(this.getDemographicNo(request));
		request.setAttribute("Allergies", allergies);
		current = System.currentTimeMillis();
		logger.debug("Get Allergies " + String.valueOf(current - start));
		start = current;

		/* get prescriptions */
		if (tab.equals("Prescriptions")) {
			List<Drug> prescriptions = null;
			boolean viewAll = caseForm.getPrescipt_view().equals("all");
			String demographicId = getDemographicNo(request);
			request.setAttribute("isIntegratorEnabled", LoggedInInfo.loggedInInfo.get().currentFacility.isIntegratorEnabled());
			prescriptions = caseManagementMgr.getPrescriptions(Integer.parseInt(demographicId), viewAll);

			request.setAttribute("Prescriptions", prescriptions);

			// Setup RX bean start
			RxSessionBean bean = new RxSessionBean();
			bean.setProviderNo(loggedInInfo.loggedInProvider.getProviderNo());
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

		//load up custom JavaScript

		//1. try from Properties
		String customCmeJs = OscarProperties.getInstance().getProperty("cme_js");
		if(customCmeJs == null || customCmeJs.length()==0) {
			request.setAttribute("cme_js", "default");
		} else {
			request.setAttribute("cme_js", customCmeJs);
		}

		//2. Override from provider preferences?

		//3. Override based on appointment type?

		logger.debug("VIEW Exiting " + String.valueOf(current - beginning));


		String printPreview = (String) request.getAttribute("patientCppPrintPreview");
		if ("true".equals(printPreview)) {
			request.setAttribute("patientCppPrintPreview", "false");
			return mapping.findForward("clientHistoryPrintPreview");
		} else {

			if (useNewCaseMgmt) {
				String fwdName = request.getParameter("ajaxview");
				if( fwdName == null || fwdName.equals("") || fwdName.equalsIgnoreCase("null")) {
					return mapping.findForward("page.newcasemgmt.view");
				}
				else {
					return mapping.findForward(fwdName);
				}
			}
			else return mapping.findForward("page.casemgmt.view");
		}
	}

	public static class IssueDisplay {
		public boolean writeAccess = true;
		public String codeType = null;
		public String code = null;
		public String description = null;
		public String location = null;
		public String acute = null;
		public String certain = null;
		public String major = null;
		public String resolved = null;
		public String role = null;
		public String priority = null;
		public Integer sortOrderId = null;
		
		
		public Integer getSortOrderId() {
        	return sortOrderId;
        }

		public void setSortOrderId(Integer sortOrderId) {
        	this.sortOrderId = sortOrderId;
        }

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

		public String toString() {
			return (ReflectionToStringBuilder.toString(this));
		}
	}

	private void viewCurrentIssuesTab_oldCme(HttpServletRequest request, CaseManagementViewFormBean caseForm, String demoNo, String programId) throws Exception {
		long startTime = System.currentTimeMillis();

		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		String providerNo = loggedInInfo.loggedInProvider.getProviderNo();
		int demographicNo = Integer.parseInt(demoNo);
		boolean hideInactiveIssues = Boolean.parseBoolean(caseForm.getHideActiveIssue());

		ArrayList<CheckBoxBean> checkBoxBeanList = new ArrayList<CheckBoxBean>();
		// addLocalIssues(checkBoxBeanList, demographicNo, hideInactiveIssues, null);
		addLocalIssues(checkBoxBeanList, demographicNo, hideInactiveIssues, Integer.valueOf(programId));
		addRemoteIssues(checkBoxBeanList, demographicNo, hideInactiveIssues);
		addGroupIssues(checkBoxBeanList, demographicNo, hideInactiveIssues);

		sortIssues(checkBoxBeanList);
		request.setAttribute("Issues", checkBoxBeanList);
		logger.debug("Get issues time : " + (System.currentTimeMillis() - startTime));

		logger.debug("Get stale note date");
		startTime = System.currentTimeMillis();
		// filter the notes by the checked issues and date if set
		UserProperty userProp = caseManagementMgr.getUserProperty(providerNo, UserProperty.STALE_NOTEDATE);
		request.setAttribute(UserProperty.STALE_NOTEDATE, userProp);
		UserProperty userProp2 = caseManagementMgr.getUserProperty(providerNo, UserProperty.STALE_FORMAT);
		request.setAttribute(UserProperty.STALE_FORMAT, userProp2);
		logger.debug("Get stale note date " + (System.currentTimeMillis() - startTime));

		/* PROGRESS NOTES */
		startTime = System.currentTimeMillis();
		String[] checkedIssues = request.getParameterValues("check_issue");

		// extract just the codes for local usage
		ArrayList<String> checkedCodeList = new ArrayList<String>();
		if (checkedIssues != null) {
			for (String s : checkedIssues) {
				String[] temp = s.split("\\.");
				if (temp.length == 2) checkedCodeList.add(temp[1]);
				else logger.warn("Unexpected parameter, wrong format : " + s);
			}
		}

		ArrayList<NoteDisplay> notesToDisplay = new ArrayList<NoteDisplay>();

		// deal with local notes
		startTime = System.currentTimeMillis();
		Collection<CaseManagementNote> localNotes = caseManagementNoteDao.findNotesByDemographicAndIssueCode(demographicNo, checkedCodeList.toArray(new String[0]));
		//show locked notes anyway: localNotes = manageLockedNotes(localNotes, true, this.getUnlockedNotesMap(request));
	    localNotes = manageLockedNotes(localNotes, false, this.getUnlockedNotesMap(request));
	    localNotes = caseManagementMgr.filterNotes(localNotes, programId);

		caseManagementMgr.getEditors(localNotes);

		for (CaseManagementNote noteTemp : localNotes)
			notesToDisplay.add(new NoteDisplayLocal(noteTemp));
		logger.debug("FETCHED " + localNotes.size() + " NOTES in time : " + (System.currentTimeMillis() - startTime));

		// deal with remote notes
		startTime = System.currentTimeMillis();
		addRemoteNotes(notesToDisplay, demographicNo, checkedCodeList, programId);
		addGroupNotes(notesToDisplay, Integer.parseInt(demoNo), null);
		logger.debug("Get remote notes. time=" + (System.currentTimeMillis() - startTime));

		// not sure what everything else is after this
		String resetFilter = request.getParameter("resetFilter");
		logger.debug("RESET FILTER " + resetFilter);
		if (resetFilter != null && resetFilter.equals("true")) {
			logger.debug("CASEMGMTVIEW RESET FILTER");
			caseForm.setFilter_providers(null);
			// caseForm.setFilter_provider("");
			caseForm.setFilter_roles(null);
			caseForm.setNote_sort(null);
		}

		// apply if we are filtering on role
		logger.debug("Filter on Role");
		startTime = System.currentTimeMillis();
		List roles = roleMgr.getRoles();
		request.setAttribute("roles", roles);
		String[] roleId = caseForm.getFilter_roles();
		notesToDisplay = applyRoleFilter(notesToDisplay, roleId);
		logger.debug("Filter on Role " + (System.currentTimeMillis() - startTime));

		// filter providers
		notesToDisplay = applyProviderFilter(notesToDisplay, caseForm.getFilter_providers());

		// set providers to display
		HashSet<LabelValueBean> providers = new HashSet<LabelValueBean>();
		for (NoteDisplay tempNote : notesToDisplay) {
			String tempProvider = tempNote.getProviderName();
			providers.add(new LabelValueBean(tempProvider, tempProvider));
		}
		request.setAttribute("providers", providers);

		/*
		 * people are changing the default sorting of notes so it's safer to explicity set it here, some one already changed it once and it reversed our sorting.
		 */
		logger.debug("Apply sorting to notes");
		startTime = System.currentTimeMillis();
		String noteSort = caseForm.getNote_sort();
		if (noteSort != null && noteSort.length() > 0) {
			notesToDisplay = sortNotes(notesToDisplay, noteSort);
		} else {
			oscar.OscarProperties p = oscar.OscarProperties.getInstance();
			noteSort = p.getProperty("CMESort", "");
			if (noteSort.trim().equalsIgnoreCase("UP")) notesToDisplay = sortNotes(notesToDisplay, "observation_date_asc");
			else notesToDisplay = sortNotes(notesToDisplay, "observation_date_desc");
		}

		request.setAttribute("Notes", notesToDisplay);
		logger.debug("Apply sorting to notes " + (System.currentTimeMillis() - startTime));
	}

	private void sortIssues(ArrayList<CheckBoxBean> checkBoxBeanList) {
		Comparator<CheckBoxBean> cbbComparator = new Comparator<CheckBoxBean>() {
			public int compare(CheckBoxBean o1, CheckBoxBean o2) {
				if (o1.getIssueDisplay() != null && o2.getIssueDisplay() != null && o1.getIssueDisplay().code != null) {
					return (o1.getIssueDisplay().code.compareTo(o2.getIssueDisplay().code));
				} else return (0);
			}
		};

		Collections.sort(checkBoxBeanList, cbbComparator);
	}

	public void sortIssuesByOrderId(ArrayList<CheckBoxBean> checkBoxBeanList) {
		Comparator<CheckBoxBean> cbbComparator = new Comparator<CheckBoxBean>() {
			public int compare(CheckBoxBean o1, CheckBoxBean o2) {
				if (o1.getIssueDisplay() != null && o2.getIssueDisplay() != null && o1.getIssueDisplay().sortOrderId != null && o2.getIssueDisplay().sortOrderId != null) {
					return (o1.getIssueDisplay().sortOrderId.compareTo(o2.getIssueDisplay().sortOrderId));
				} else return (0);
			}
		};

		Collections.sort(checkBoxBeanList, cbbComparator);
	}
	/**
	 * New CME
	 */
	private void viewCurrentIssuesTab_newCmeNotes(HttpServletRequest request, CaseManagementViewFormBean caseForm, String demoNo, String programId) throws Exception {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		String providerNo = loggedInInfo.loggedInProvider.getProviderNo();
		int demographicId=Integer.parseInt(demoNo);

		long startTime;
		startTime = System.currentTimeMillis();

		/* PROGRESS NOTES */
		List<CaseManagementNote> notes = null;
		// here we might have a checked/unchecked issue that is remote and has no issue_id (they're all zero).
		String[] checkedIssues = request.getParameterValues("check_issue");

		if (request.getParameter("offset") != null && request.getParameter("numToReturn") != null) {
			Integer offset = Integer.parseInt(request.getParameter("offset"));
			Integer numToReturn = Integer.parseInt(request.getParameter("numToReturn"));
			if (offset > 0) request.setAttribute("moreNotes", true);
			notes = caseManagementMgr.getNotesWithLimit(demoNo, offset, numToReturn);
		} else {
			notes = caseManagementMgr.getNotes(demoNo);
		}

		logger.debug("FETCHED " + notes.size() + " NOTES");

		startTime = System.currentTimeMillis();
		String resetFilter = request.getParameter("resetFilter");
		logger.debug("RESET FILTER " + resetFilter);
		if (resetFilter != null && resetFilter.equals("true")) {
			logger.debug("CASEMGMTVIEW RESET FILTER");
			caseForm.setFilter_providers(null);
			caseForm.setFilter_roles(null);
			caseForm.setNote_sort(null);
			caseForm.setIssues(null);
		}

		logger.debug("Filter Notes");

		// filter notes based on role and program/provider mappings
		notes = caseManagementMgr.filterNotes(notes, programId);
		logger.debug("FILTER NOTES " + (System.currentTimeMillis() - startTime));

		// apply provider filter
		logger.debug("Filter Notes Provider");
		startTime = System.currentTimeMillis();
		notes = applyProviderFilters(notes, caseForm.getFilter_providers());
		logger.debug("FILTER NOTES PROVIDER " + (System.currentTimeMillis() - startTime));

		// apply if we are filtering on role
		logger.debug("Filter on Role");
		startTime = System.currentTimeMillis();
		String[] roleId = caseForm.getFilter_roles();
		if (roleId != null && roleId.length > 0) notes = applyRoleFilter(notes, roleId);
		logger.debug("Filter on Role " + (System.currentTimeMillis() - startTime));

		// apply if we are filtering on issues
		logger.debug("Filter on issues");
		startTime = System.currentTimeMillis();

		if (checkedIssues != null && checkedIssues.length > 0) notes = applyIssueFilter(notes, checkedIssues);
		logger.debug("Filter on issue " + (System.currentTimeMillis() - startTime));


		// this is a local filter and does not apply to remote notes
		logger.debug("Pop notes with editors");
		startTime = System.currentTimeMillis();
		this.caseManagementMgr.getEditors(notes);
		logger.debug("Pop notes with editors " + (System.currentTimeMillis() - startTime));

		ArrayList<NoteDisplay> notesToDisplay = new ArrayList<NoteDisplay>();
		for (CaseManagementNote noteTemp : notes) {
			notesToDisplay.add(new NoteDisplayLocal(noteTemp));
		}

		if (request.getParameter("offset") == null || request.getParameter("offset").equalsIgnoreCase("0")) {
			addRemoteNotes(notesToDisplay, demographicId, null, programId);
			addGroupNotes(notesToDisplay, demographicId, null);

			// add eforms to notes list as single line items
			String roleName = (String) request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
			ArrayList<HashMap<String, ? extends Object>> eForms = EFormUtil.listPatientEForms(EFormUtil.DATE, EFormUtil.CURRENT, demoNo, roleName);

			// add forms to notes list as single line items
			ArrayList<PatientForm> allPatientForms=EctFormData.getGroupedPatientFormsFromAllTables(demographicId);

			for (HashMap<String, ? extends Object> eform : eForms) {
				notesToDisplay.add(new NoteDisplayNonNote(eform));
			}

			// add forms to notes list as single line items
			//ArrayList<PatientForm> allPatientForms=EctFormData.getGroupedPatientFormsFromAllTables(demographicId);
			for (PatientForm patientForm : allPatientForms) {
				notesToDisplay.add(new NoteDisplayNonNote(patientForm));
			}

			if( oscar.OscarProperties.getInstance().getProperty("billregion","").equalsIgnoreCase("ON") ) {
				fetchInvoices(notesToDisplay, demoNo);
			}
		}

		// sort the notes
		String noteSort = oscar.OscarProperties.getInstance().getProperty("CMESort", "");
		if (noteSort.trim().equalsIgnoreCase("UP")) notesToDisplay = sortNotes(notesToDisplay, "observation_date_asc");
		else notesToDisplay = sortNotes(notesToDisplay, "observation_date_desc");

		request.setAttribute("notesToDisplay", notesToDisplay);
	}

	/**
	 * New CME
	 */
	private void viewCurrentIssuesTab_newCme(HttpServletRequest request, CaseManagementViewFormBean caseForm, String demoNo, String programId) throws Exception {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		String providerNo = loggedInInfo.loggedInProvider.getProviderNo();
		int demographicId=Integer.parseInt(demoNo);

		long startTime;
		startTime = System.currentTimeMillis();

		logger.debug("Get stale note date");
		// filter the notes by the checked issues and date if set
		UserProperty userProp = caseManagementMgr.getUserProperty(providerNo, UserProperty.STALE_NOTEDATE);
		request.setAttribute(UserProperty.STALE_NOTEDATE, userProp);
		UserProperty userProp2 = caseManagementMgr.getUserProperty(providerNo, UserProperty.STALE_FORMAT);
		request.setAttribute(UserProperty.STALE_FORMAT, userProp2);
		logger.debug("Get stale note date " + (System.currentTimeMillis() - startTime));

		//List<CaseManagementIssue> issues = cmeIssueDao.getIssuesByDemographic(demoNo);
		ArrayList<CheckBoxBean> checkBoxBeanList = new ArrayList<CheckBoxBean>();
		// addLocalIssues(checkBoxBeanList, demographicNo, hideInactiveIssues, null);
		addLocalIssues(checkBoxBeanList, demographicId, false, Integer.valueOf(programId));
		addRemoteIssues(checkBoxBeanList, demographicId, false);
		addGroupIssues(checkBoxBeanList, demographicId, false);
		sortIssues(checkBoxBeanList);
		request.setAttribute("cme_issues", checkBoxBeanList);

		Set providers = new HashSet(caseManagementMgr.getAllEditors(demoNo));
		request.setAttribute("providers", providers);

		List roles = roleMgr.getRoles();
		request.setAttribute("roles", roles);



	}

	private void fetchInvoices(ArrayList<NoteDisplay>notes, String demographic_no) {
		List<BillingClaimHeader1>bills = billingClaimDao.getInvoices(demographic_no, MAX_INVOICES);

		for( BillingClaimHeader1 h1 : bills ) {
			notes.add(new NoteDisplayNonNote(h1));
		}
	}

	private List<CaseManagementNote> applyRoleFilter(List<CaseManagementNote> notes, String[] roleId) {

		// if no filter return everything
		if (Arrays.binarySearch(roleId, "a") >= 0) return notes;

		List<CaseManagementNote> filteredNotes = new ArrayList<CaseManagementNote>();

		for (Iterator<CaseManagementNote> iter = notes.listIterator(); iter.hasNext();) {
			CaseManagementNote note = iter.next();

			if (Arrays.binarySearch(roleId, note.getReporter_caisi_role()) >= 0) filteredNotes.add(note);
		}

		return filteredNotes;
	}

	private List<CaseManagementNote> applyIssueFilter(List<CaseManagementNote> notes, String[] issueId) {

		// if no filter return everything
		if (Arrays.binarySearch(issueId, "a") >= 0) return notes;

		List<CaseManagementNote> filteredNotes = new ArrayList<CaseManagementNote>();

		for (Iterator<CaseManagementNote> iter = notes.listIterator(); iter.hasNext();) {
			CaseManagementNote note = iter.next();
			List<CaseManagementIssue> issues = cmeIssueNotesDao.getNoteIssues((Integer.valueOf(note.getId().toString())));
			for(CaseManagementIssue issue: issues) {
				if (Arrays.binarySearch(issueId, String.valueOf(issue.getId())) >= 0) {
					filteredNotes.add(note);
					break;
				}
			}
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

	private List applyProviderFilters(List<CaseManagementNote> notes, String[] providerNo) {
		boolean filter = false;
		List<CaseManagementNote> filteredNotes = new ArrayList<CaseManagementNote>();

		if (providerNo != null && Arrays.binarySearch(providerNo, "a") < 0) {
			filter = true;
		}

		for (Iterator<CaseManagementNote> iter = notes.iterator(); iter.hasNext();) {
			CaseManagementNote note = iter.next();
			if (!filter) {
				// no filter, add all
				filteredNotes.add(note);

			} else {
				if (Arrays.binarySearch(providerNo, note.getProviderNo()) >= 0)
				// correct provider
				    filteredNotes.add(note);
			}
		}

		return filteredNotes;
	}

	private static boolean hasRole(List<SecUserRole> roles, String role) {
		if (roles == null) return (false);

		logger.debug("Note Role : " + role);

		for (SecUserRole roleTmp : roles) {
			logger.debug("Provider Roles : " + roleTmp.getRoleName());
			if (roleTmp.getRoleName().equals(role)) return (true);
		}

		return (false);
	}

	private void addGroupNotes(ArrayList<NoteDisplay> notesToDisplay, int demographicNo, ArrayList<String> issueCodesToDisplay) {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		List<SecUserRole> roles = secUserRoleDao.getUserRoles(loggedInInfo.loggedInProvider.getProviderNo());

		if (!loggedInInfo.currentFacility.isEnableGroupNotes()) return;

		List<GroupNoteLink> noteLinks = groupNoteDao.findLinksByDemographic(demographicNo);
		for (GroupNoteLink noteLink : noteLinks) {
			try {

				int orginalNoteId = noteLink.getNoteId();
				CaseManagementNote note = this.caseManagementNoteDao.getNote(Long.valueOf(orginalNoteId));

				// filter on issues to display
				// if (issueCodesToDisplay==null || hasIssueToBeDisplayed(note, issueCodesToDisplay)) {
				// filter on role based access
				String roleName = this.roleMgr.getRole(note.getReporter_caisi_role()).getRoleName();
				if (hasRole(roles, roleName)) {
					String originaldemo = note.getDemographic_no();

					note.setDemographic_no(String.valueOf(demographicNo));
					NoteDisplayLocal disp = new NoteDisplayLocal(note);
					disp.setReadOnly(true);
					disp.setGroupNote(true);
					Demographic origDemographic = demographicDao.getDemographic(originaldemo);
					disp.setLocation(String.valueOf(origDemographic.getDemographicNo()));
					notesToDisplay.add(disp);
				}
				// }
			} catch (Exception e) {
				logger.error("Unexpected error.", e);
			}
		}

	}

	private void addRemoteNotes(ArrayList<NoteDisplay> notesToDisplay, int demographicNo, ArrayList<String> issueCodesToDisplay, String programId) {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();

		if (!loggedInInfo.currentFacility.isIntegratorEnabled()) return;
		List<CachedDemographicNote> linkedNotes  = null;
		try {
			if (!CaisiIntegratorManager.isIntegratorOffline()){
			   linkedNotes = CaisiIntegratorManager.getLinkedNotes(demographicNo);
			}
		} catch (Exception e) {
			logger.error("Unexpected error.", e);
			CaisiIntegratorManager.checkForConnectionError(e);
		}

		if(CaisiIntegratorManager.isIntegratorOffline()){
		   linkedNotes = IntegratorFallBackManager.getLinkedNotes(demographicNo);
		}

		if (linkedNotes == null) return;
		for (CachedDemographicNote cachedDemographicNote : linkedNotes) {
			try {

				// filter on issues to display
				if (issueCodesToDisplay == null || hasIssueToBeDisplayed(cachedDemographicNote, issueCodesToDisplay)) {
					// filter on role based access
					if (caseManagementMgr.hasRole(cachedDemographicNote, programId)) {
						notesToDisplay.add(new NoteDisplayIntegrator(cachedDemographicNote));
					}
				}
			} catch (Exception e) {
				logger.error("Unexpected error.", e);
			}
		}

	}

	private boolean hasIssueToBeDisplayed(CachedDemographicNote cachedDemographicNote, ArrayList<String> issueCodesToDisplay) {
		// no issue selected means display all
		if (issueCodesToDisplay == null || issueCodesToDisplay.size() == 0) return (true);

		for (NoteIssue noteIssue : cachedDemographicNote.getIssues()) {
			// yes I know this is flawed in that it's ignoreing the code type.
			// right now we don't support code type properly on the caisi side.
			if (issueCodesToDisplay.contains(noteIssue.getIssueCode())) return (true);
		}

		return (false);
	}

	protected void addGroupIssues(ArrayList<CheckBoxBean> checkBoxBeanList, int demographicNo, boolean hideInactiveIssues) {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();

		if (!loggedInInfo.currentFacility.isEnableGroupNotes()) return;

		try {
			// get all the issues for which we have group notes for
			List<GroupNoteLink> links = this.groupNoteDao.findLinksByDemographic(demographicNo);
			for (GroupNoteLink link : links) {
				int noteId = link.getNoteId();
				List<CaseManagementIssue> issues = this.caseManagementMgr.getIssuesByNote(noteId);
			}
			/*
			 * for (CachedDemographicIssue cachedDemographicIssue : remoteIssues) { try { IssueDisplay issueDisplay=null;
			 *
			 * if (!hideInactiveIssues) issueDisplay=getIssueToDisplay(cachedDemographicIssue); else if (!cachedDemographicIssue.isResolved()) issueDisplay=getIssueToDisplay(cachedDemographicIssue);
			 *
			 * if (issueDisplay!=null) { CheckBoxBean checkBoxBean=new CheckBoxBean(); checkBoxBean.setIssueDisplay(issueDisplay); checkBoxBean.setUsed(caseManagementNoteDao.haveIssue(issueDisplay.getCode(), demographicNo));
			 * checkBoxBeanList.add(checkBoxBean); } } catch (Exception e) { log.error("Unexpected error.", e); } }
			 */
		} catch (Exception e) {
			logger.error("Unexpected error.", e);
		}
	}

	protected void addRemoteIssues(ArrayList<CheckBoxBean> checkBoxBeanList, int demographicNo, boolean hideInactiveIssues) {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();

		if (!loggedInInfo.currentFacility.isIntegratorEnabled()) return;

		try {


			List<CachedDemographicIssue> remoteIssues  = null;
			try {
				if (!CaisiIntegratorManager.isIntegratorOffline()){
				   DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
				   remoteIssues = demographicWs.getLinkedCachedDemographicIssuesByDemographicId(demographicNo);
				}
			} catch (Exception e) {
				logger.error("Unexpected error.", e);
				CaisiIntegratorManager.checkForConnectionError(e);
			}

			if(CaisiIntegratorManager.isIntegratorOffline()){
			   remoteIssues = IntegratorFallBackManager.getRemoteDemographicIssues(demographicNo);
			}


			for (CachedDemographicIssue cachedDemographicIssue : remoteIssues) {
				try {
					IssueDisplay issueDisplay = null;

					if (!hideInactiveIssues) issueDisplay = getIssueToDisplay(cachedDemographicIssue);
					else if (!cachedDemographicIssue.isResolved()) issueDisplay = getIssueToDisplay(cachedDemographicIssue);

					if (issueDisplay != null) {
						if (existsIssueWithSameAttributes(issueDisplay, checkBoxBeanList)) continue;

						CheckBoxBean checkBoxBean = new CheckBoxBean();
						checkBoxBean.setIssueDisplay(issueDisplay);
						checkBoxBean.setUsed(caseManagementNoteDao.haveIssue(issueDisplay.getCode(), demographicNo));
						checkBoxBeanList.add(checkBoxBean);
					}
				} catch (Exception e) {
					logger.error("Unexpected error.", e);
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error.", e);
		}
	}

	private static boolean existsIssueWithSameAttributes(IssueDisplay issueDisplay, ArrayList<CheckBoxBean> checkBoxBeanList) {
		// must iterate through all items, can't stop at first hit
		for (CheckBoxBean cbb : checkBoxBeanList) {
			IssueDisplay existingIssueDisplay = cbb.getIssueDisplay();
			if (hasSameAttributes(existingIssueDisplay, issueDisplay)) return (true);
		}

		return (false);
	}

	public static boolean hasSameAttributes(IssueDisplay issueDisplay1, IssueDisplay issueDisplay2) {
		if (issueDisplay1.code != null && !issueDisplay1.code.equals(issueDisplay2.code)) return (false);
		if (issueDisplay1.acute != null && !issueDisplay1.acute.equals(issueDisplay2.acute)) return (false);
		if (issueDisplay1.certain != null && !issueDisplay1.certain.equals(issueDisplay2.certain)) return (false);
		if (issueDisplay1.major != null && !issueDisplay1.major.equals(issueDisplay2.major)) return (false);
		if (issueDisplay1.priority != null && !issueDisplay1.priority.equals(issueDisplay2.priority)) return (false);
		if (issueDisplay1.resolved != null && !issueDisplay1.resolved.equals(issueDisplay2.resolved)) return (false);

		return (true);
	}

	private IssueDisplay getIssueToDisplay(CachedDemographicIssue cachedDemographicIssue) throws MalformedURLException {
		IssueDisplay issueDisplay = new IssueDisplay();

		issueDisplay.writeAccess = true;
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
		List<CaseManagementIssue> localIssues = caseManagementManager.getIssues(demographicNo, hideInactiveIssues ? false : null);

		for (CaseManagementIssue cmi : localIssues) {
			CheckBoxBean checkBoxBean = new CheckBoxBean();

			checkBoxBean.setIssue(cmi);

			IssueDisplay issueDisplay = getIssueDisplay(programId, cmi);
			checkBoxBean.setIssueDisplay(issueDisplay);

			checkBoxBean.setUsed(caseManagementNoteDao.haveIssue(cmi.getIssue().getCode(), demographicNo));

			checkBoxBeanList.add(checkBoxBean);
		}
	}

	protected IssueDisplay getIssueDisplay(Integer programId, CaseManagementIssue cmi) {
		IssueDisplay issueDisplay = new IssueDisplay();

		if (programId != null) issueDisplay.writeAccess = cmi.isWriteAccess(programId);

		issueDisplay.acute = cmi.isAcute() ? "acute" : "chronic";
		issueDisplay.certain = cmi.isCertain() ? "certain" : "uncertain";

		long issueId = cmi.getIssue_id();
		Issue issue = issueDao.getIssue(issueId);

		issueDisplay.code = issue.getCode();
		issueDisplay.codeType = OscarProperties.getInstance().getProperty("COMMUNITY_ISSUE_CODETYPE").toUpperCase();
		issueDisplay.description = issue.getDescription();
		issueDisplay.location = "local";
		issueDisplay.major = cmi.isMajor() ? "major" : "not major";
		issueDisplay.priority = issue.getPriority();
		issueDisplay.resolved = cmi.isResolved() ? "resolved" : "unresolved";
		issueDisplay.role = issue.getRole();
		issueDisplay.sortOrderId = issue.getSortOrderId();
		
		return issueDisplay;
	}

	public ActionForward viewNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String nId = request.getParameter("noteId");
		CaseManagementNote note = this.caseManagementMgr.getNote(nId);
		request.setAttribute("noteStr", note.getNote());
		boolean raw = request.getParameter("raw").equalsIgnoreCase("true");
		request.setAttribute("raw", raw);
		return mapping.findForward("displayNote");
	}

	public ActionForward listNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("List Notes start");

		String providerNo = getProviderNo(request);
		String demoNo = getDemographicNo(request);
		Collection<CaseManagementNote> notes = null;

		String appointmentNo = request.getParameter("appointment_no");

		String[] codes = request.getParameterValues("issue_code");
		com.quatro.service.security.SecurityManager securityManager = new com.quatro.service.security.SecurityManager();
		//these are the ones on the right nav bar. - generic implementation of view access
		if(codes != null && codes.length > 0) {
			if(!securityManager.hasReadAccess("_"  + codes[0], request.getSession().getAttribute("userrole") + "," + request.getSession().getAttribute("user"))) {
				return null;
			}
		}

		String roleName = (String) request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");

		boolean a = true;
		if (codes[0].equalsIgnoreCase("OMeds")) {
			a = hasPrivilege("_newCasemgmt.otherMeds", roleName);
			if (!a) {
				return mapping.findForward("success"); // The link of Other Meds won't show up on new CME screen.
			}
		} else if (codes[0].equalsIgnoreCase("RiskFactors")) {
			a = hasPrivilege("_newCasemgmt.riskFactors", roleName);
			if (!a) {
				return mapping.findForward("success"); // The link of Risk Factors won't show up on new CME screen.
			}
		} else if (codes[0].equalsIgnoreCase("FamHistory")) {
			a = hasPrivilege("_newCasemgmt.familyHistory", roleName);
			if (!a) {
				return mapping.findForward("success"); // The link of Family History won't show up on new CME screen.
			}
		} else if (codes[0].equalsIgnoreCase("MedHistory")) {
			a = hasPrivilege("_newCasemgmt.medicalHistory", roleName);
			if (!a) {
				return mapping.findForward("success"); // The link of Medical History won't show up on new CME screen.
			}
		}

		// set save url to be used by ajax editor
		String identUrl = request.getQueryString();
		request.setAttribute("identUrl", identUrl);

		// filter the notes by the checked issues
		// UserProperty userProp = caseManagementMgr.getUserProperty(providerNo, UserProperty.STALE_NOTEDATE);

		List<Issue> issues = caseManagementMgr.getIssueInfoByCode(providerNo, codes);
		StringBuilder checked_issues = new StringBuilder();
		StringBuilder cppIssues = new StringBuilder();
		String[] issueIds = new String[issues.size()];
		int idx = 0;
		for (Issue issue : issues) {
			checked_issues.append("&issue_id=" + String.valueOf(issue.getId()));
			if (idx > 0) {
				cppIssues.append(";");
			}
			cppIssues.append(issue.getId() + ";" + issue.getCode() + ";" + issue.getDescription());
			issueIds[idx] = String.valueOf(issue.getId());
			idx++;
		}

		// set save Url
		String addUrl = request.getContextPath() + "/CaseManagementEntry.do?method=issueNoteSave&providerNo=" + providerNo + "&demographicNo=" + demoNo + "&appointmentNo="+appointmentNo + "&noteId=";
		request.setAttribute("addUrl", addUrl);
		request.setAttribute("cppIssue", cppIssues.toString());

		// set issueIds for retrieving history
		request.setAttribute("issueIds", StringUtils.join(issueIds, ","));

		// need to apply issue filter
		notes = caseManagementMgr.getActiveNotes(demoNo, issueIds);
		notes = manageLockedNotes(notes, true, this.getUnlockedNotesMap(request));

		logger.debug("FETCHED " + notes.size() + " NOTES filtered by " + StringUtils.join(issueIds, ","));
		logger.debug("REFERER " + request.getRequestURL().toString() + "?" + request.getQueryString());

		String programId = (String) request.getSession().getAttribute("case_program_id");

		if (programId == null || programId.length() == 0) {
			programId = "0";
		}

		notes = caseManagementMgr.filterNotes(notes, programId);
		this.caseManagementMgr.getEditors(notes);

		List<CaseManagementNoteExt> lcme = new ArrayList<CaseManagementNoteExt>();
		for (Object obj : notes) {
			CaseManagementNote cmn = (CaseManagementNote) obj;
			lcme.addAll(caseManagementMgr.getExtByNote(cmn.getId()));
		}
		request.setAttribute("NoteExts", lcme);
		request.setAttribute("Notes", notes);
		/*
		 * oscar.OscarProperties p = oscar.OscarProperties.getInstance(); String noteSort = p.getProperty("CMESort", ""); if (noteSort.trim().equalsIgnoreCase("UP")) request.setAttribute("Notes", sortNotes(notes, "observation_date_asc")); else
		 * request.setAttribute("Notes", sortNotes(notes, "observation_date_desc"));
		 */

		boolean isJsonRequest = request.getParameter("json") != null && request.getParameter("json").equalsIgnoreCase("true");
        if (isJsonRequest) {
        	HashMap<String, Object> hashMap = new HashMap<String, Object>();

        	List<HashMap<String, Object>> notesList = new ArrayList<HashMap<String, Object>>();
        	for (Object cmn : notes)
        		notesList.add((HashMap<String, Object>) ((CaseManagementNote) cmn).getMap());

        	hashMap.put("Items", notesList);
        	hashMap.put("RightURL", addUrl);
        	hashMap.put("Issues", issues);

        	JsonConfig config = new JsonConfig();
        	config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());
        	JSONObject json = JSONObject.fromObject(hashMap, config);
        	response.getOutputStream().write(json.toString().getBytes());
        	return null;
        }

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

	private List sortNotes_old(Collection<CaseManagementNote> notes, String field) {
		logger.debug("Sorting notes by field: " + field);

		ArrayList<CaseManagementNote> resultsSorted = new ArrayList<CaseManagementNote>(notes);

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

	private ArrayList<NoteDisplay> sortNotes(ArrayList<NoteDisplay> notes, String field) {
		logger.debug("Sorting notes by field: " + field);

		if (field == null || field.equals("") || field.equals("update_date")) {
			return notes;
		}

		if (field.equals("providerName")) {
			Collections.sort(notes, NoteDisplay.noteProviderComparator);
		}
		if (field.equals("programName")) {
			Collections.sort(notes, NoteDisplay.noteRoleComparator);
		}
		if (field.equals("observation_date_asc")) {
			Collections.sort(notes, NoteDisplay.noteObservationDateComparator);
			Collections.reverse(notes);
		}
		if (field.equals("observation_date_desc")) {
			Collections.sort(notes, NoteDisplay.noteObservationDateComparator);
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
		} else {
			return unlock(mapping, form, request, response);
		}

	}

	public ActionForward run_macro_script(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MacroDao macroDao = (MacroDao)SpringUtils.getBean("MacroDAO");
		Macro macro = macroDao.find(Integer.parseInt(request.getParameter("id")));
		logger.info("loaded macro " + macro.getLabel());
		StringBuilder sb = new StringBuilder();

		//impression text
		sb.append("var noteTa = document.getElementById('caseNote_note"+request.getParameter("noteId")+"');");
		sb.append("var noteTaVal = noteTa.value;");
		sb.append("noteTaVal = noteTaVal + '"+macro.getImpression()+"';");
		sb.append("noteTa.value = noteTaVal;");

		//checkboxes
		if(macro.getDischargeFlag().equals("dischargeFlag")) {
			sb.append("jQuery(\"#ack1\").attr(\"checked\",true);");
		}
		if(macro.getStatFlag().equals("statFlag")) {
			sb.append("jQuery(\"#ack2\").attr(\"checked\",true);");
		}
		if(macro.getOptFlag().equals("optFlag")) {
			sb.append("jQuery(\"#ack3\").attr(\"checked\",true);");
		}

		//send tickler
		if(macro.getTicklerRecipient().length()>0) {
			sb.append("saveNoteAndSendTickler();");
		} else {
			sb.append("saveEyeformNote();");
		}

		//billing

		response.getWriter().println(sb.toString());

		return null;
	}

	public ActionForward run_macro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MacroDao macroDao = (MacroDao)SpringUtils.getBean("MacroDAO");
		Macro macro = macroDao.find(Integer.parseInt(request.getParameter("id")));
		logger.info("loaded macro " + macro.getLabel());

		StringBuilder sb = new StringBuilder();


		//follow up - need to add it, then force a reload
		int followUpNo = macro.getFollowupNo();
		String followUpUnit = macro.getFollowupUnit();
		String followUpDr = macro.getFollowupDoctorId();
		if(followUpDr.length()>0) {
			EyeformFollowUp f = new EyeformFollowUp();
			f.setAppointmentNo(Integer.parseInt(request.getParameter("appointmentNo")));
			f.setDate(new Date());
			f.setDemographicNo(Integer.parseInt(request.getParameter("demographicNo")));
			f.setProvider(LoggedInInfo.loggedInInfo.get().loggedInProvider);
			f.setTimeframe(followUpUnit);
			f.setTimespan(followUpNo);
			f.setType("followup");
			f.setUrgency("routine");
			f.setFollowupProvider(followUpDr);
			FollowUpDao dao = (FollowUpDao)SpringUtils.getBean("FollowUpDAO");
	    	dao.save(f);
		}

		//tests
		TestBookRecordDao testDao = (TestBookRecordDao)SpringUtils.getBean("TestBookDAO");
		String[] tests = macro.getTestRecords().split("\n");
		for(String test:tests) {
			String[] parts = test.trim().split("\\|");
			if(parts.length==4) {
				EyeformTestBook rec = new EyeformTestBook();
				rec.setAppointmentNo(Integer.parseInt(request.getParameter("appointmentNo")));
				rec.setComment(parts[3]);
				rec.setDate(new Date());
				rec.setDemographicNo(Integer.parseInt(request.getParameter("demographicNo")));
				rec.setEye(parts[1]);
				rec.setProvider(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
				//rec.setStatus(null);
				rec.setTestname(parts[0]);
				rec.setUrgency(parts[2]);
				testDao.save(rec);
			}
		}


		return null;
	}

	protected Map getUnlockedNotesMap(HttpServletRequest request) {
		Map map = (Map) request.getSession().getAttribute("unlockedNoteMap");
		if (map == null) {
			map = new HashMap();
		}
		return map;
	}

	private ArrayList<NoteDisplay> applyRoleFilter(ArrayList<NoteDisplay> notes, String[] roleId) {

		if (roleId == null || hasRole(roleId, "a")) return (notes);

		ArrayList<NoteDisplay> filteredNotes = new ArrayList<NoteDisplay>();

		for (NoteDisplay note : notes) {
			if (hasRole(roleId, note.getRoleName())) filteredNotes.add(note);
		}

		return filteredNotes;
	}

	private static boolean hasRole(String[] roleId, String role) {
		for (String s : roleId) {
			if (s.equals(role)) return (true);
		}

		return (false);
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
		if (providerName == null || providerName.length == 0 || providerName[0].length() == 0) return (notes);

		for (NoteDisplay note : notes) {
			String tempName = note.getProviderName();

			for (String temp : providerName) {
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
		} catch (IOException e) {
			logger.warn("Unable to load Dx properties file");
		}

		this.caseManagementMgr.saveToDx(getDemographicNo(request), request.getParameter("issue_code"), codingSystem, false);

		return view(mapping, form, request, response);
	}

	public boolean hasPrivilege(String objectName, String roleName) {
		Vector v = OscarRoleObjectPrivilege.getPrivilegeProp(objectName);
		return OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (Vector) v.get(1));
	}

	public static String getNoteColour(NoteDisplay noteDisplay) {
		// set all colours
		String blackColour = "000000";
		String documentColour = "color:#" + blackColour + ";background-color:#" + Colour.getInstance().documents + ";";
		String diseaseColour = "color:#" + blackColour + ";background-color:#" + Colour.getInstance().disease + ";";
		String eFormsColour = "color:#" + blackColour + ";background-color:#" + Colour.getInstance().eForms + ";";
		String formsColour = "color:#" + blackColour + ";background-color:#" + Colour.getInstance().forms + ";";
		String labsColour = "color:#" + blackColour + ";background-color:#" + Colour.getInstance().labs + ";";
		String measurementsColour = "color:#" + blackColour + ";background-color:#" + Colour.getInstance().measurements + ";";
		String messagesColour = "color:#" + blackColour + ";background-color:#" + Colour.getInstance().messages + ";";
		String preventionColour = "color:#" + blackColour + ";background-color:#" + Colour.getInstance().prevention + ";";
		String ticklerColour = "color:#" + blackColour + ";background-color:#" + Colour.getInstance().tickler + ";";
		String rxColour = "color:#" + blackColour + ";background-color:#" + Colour.getInstance().rx + ";";
		String invoiceColour = "color:#" + blackColour + ";background-color:#" + Colour.getInstance().invoices + ";";

		String bgColour = "color:#000000;background-color:#CCCCFF;";

		if (noteDisplay.isCpp()) {
			bgColour = "color:#FFFFFF;background-color:#" + getCppColour(noteDisplay) + ";";
		} else if (noteDisplay.isDocument()) {
			bgColour = documentColour;
		} else if (noteDisplay.isRxAnnotation()) {
			bgColour = rxColour;
		} else if (noteDisplay.isEformData()) {
			bgColour = eFormsColour;
		} else if (noteDisplay.isEncounterForm()) {
			bgColour = formsColour;
		} else if (noteDisplay.isInvoice()) {
			bgColour = invoiceColour;
		}


		return (bgColour);
	}

	private static String getCppColour(NoteDisplay noteDisplay) {
		Colour colour = Colour.getInstance();

		if (noteDisplay.containsIssue("OMeds")) return (colour.omed);
		else if (noteDisplay.containsIssue("FamHistory")) return (colour.familyHistory);
		else if (noteDisplay.containsIssue("RiskFactors")) return (colour.riskFactors);
		else if (noteDisplay.containsIssue("SocHistory")) return (colour.socialHistory);
		else if (noteDisplay.containsIssue("MedHistory")) return (colour.medicalHistory);
		else if (noteDisplay.containsIssue("Concerns")) return (colour.ongoingConcerns);
		else if (noteDisplay.containsIssue("Reminders")) return (colour.reminders);
		else return colour.prevention;

	}

	public static CaseManagementNote getLatestCppNote(String demographicNo, long issueId, int appointmentNo, boolean filterByAppointment) {
		CaseManagementManager caseManagementMgr = (CaseManagementManager)SpringUtils.getBean("caseManagementManager");
		Collection<CaseManagementNote> notes = caseManagementMgr.getActiveNotes(demographicNo, new String[] {String.valueOf(issueId)});
		List<CaseManagementNote> filteredNotes = new ArrayList<CaseManagementNote>();

		if(notes.size()==0) {
			return null;
		}
		if(filterByAppointment) {
			for(CaseManagementNote note:notes) {
				if(note.getAppointmentNo() == appointmentNo) {
					filteredNotes.add(note);
				}
			}
			if(filteredNotes.size()==0) {
				return null;
			}
		} else {
			filteredNotes.addAll(notes);
		}
		return filteredNotes.iterator().next();
	}

	public static String getCppAdditionalData(Long noteId, String issueCode,List<CaseManagementNoteExt> noteExts,CppPreferencesUIBean prefsBean) {
		if(prefsBean.getEnable()== null || !prefsBean.getEnable().equals("on")) {
			return new String();
		}
		String issueCodeArr[] = issueCode.split(";");
		StringBuilder sb = new StringBuilder();
		if(issueCodeArr[1].equals("SocHistory")) {
			if(prefsBean.getSocialHxStartDate().equals("on")) {
				sb.append("Start Date:"+getNoteExt(noteId, "Start Date",noteExts));
			}
			if(prefsBean.getSocialHxResDate().equals("on")) {
				if(sb.length()>0) {sb.append(" ");}
				sb.append("Resolution Date:"+getNoteExt(noteId, "Resolution Date",noteExts));
			}
		}
		if(issueCodeArr[1].equals("Reminders")) {
			if(prefsBean.getRemindersStartDate().equals("on")) {
				sb.append("Start Date:"+getNoteExt(noteId, "Start Date",noteExts));
			}
			if(prefsBean.getRemindersResDate().equals("on")) {
				if(sb.length()>0) {sb.append(" ");}
				sb.append("Resolution Date:"+getNoteExt(noteId, "Resolution Date",noteExts));
			}
		}
		if(issueCodeArr[1].equals("Concerns")) {
			if(prefsBean.getOngoingConcernsStartDate().equals("on")) {
				sb.append("Start Date:"+getNoteExt(noteId, "Start Date",noteExts));
			}
			if(prefsBean.getOngoingConcernsResDate().equals("on")) {
				if(sb.length()>0) {sb.append(" ");}
				sb.append("Resolution Date:"+getNoteExt(noteId, "Resolution Date",noteExts));
			}
			if(prefsBean.getOngoingConcernsProblemStatus().equals("on")) {
				if(sb.length()>0) {sb.append(" ");}
				sb.append("Status:"+getNoteExt(noteId, "Problem Status",noteExts));
			}
		}
		if(issueCodeArr[1].equals("MedHistory")) {
			if(prefsBean.getMedHxStartDate().equals("on")) {
				sb.append("Start Date:"+getNoteExt(noteId, "Start Date",noteExts));
			}
			if(prefsBean.getMedHxResDate().equals("on")) {
				if(sb.length()>0) {sb.append(" ");}
				sb.append("Resolution Date:"+getNoteExt(noteId, "Resolution Date",noteExts));
			}
			if(prefsBean.getMedHxProcedureDate().equals("on")) {
				if(sb.length()>0) {sb.append(" ");}
				sb.append("Procedure Date:"+getNoteExt(noteId, "Procedure Date",noteExts));
			}
			if (prefsBean.getMedHxTreatment().equals("on")) {
				if (sb.length() > 0) {
					sb.append(" ");
				}
				sb.append("Treatment:" + getNoteExt(noteId, "Treatment", noteExts));
			}
		}
		if(sb.length()>0) {
			sb.insert(0, " (");
			sb.append(")");
		}
		return sb.toString();
	}

	static String getNoteExt(Long noteId, String key, List<CaseManagementNoteExt> lcme) {
		for (CaseManagementNoteExt cme : lcme) {
		    if (cme.getNoteId().equals(noteId) && cme.getKeyVal().equals(key) ) {
				String val = null;

				if (key.contains(" Date")) {
				    val = oscar.util.UtilDateUtilities.DateToString(cme.getDateValue(),"yyyy-MM-dd");
				} else {
				    val = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(cme.getValue());
				}
				return val;
		    }
		}
		return "";
	}


	public ActionForward viewNotesOpt(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// response.setCharacterEncoding("UTF-8");
		long start = System.currentTimeMillis();
		CaseManagementViewFormBean caseForm = (CaseManagementViewFormBean) form;

		HttpSession se = request.getSession();
		if (se.getAttribute("userrole") == null) return mapping.findForward("expired");


		String demoNo = getDemographicNo(request);

		logger.debug("is client in program");
		// need to check to see if the client is in our program domain
		// if not...don't show this screen!
		String roles = (String) se.getAttribute("userrole");
		if (OscarProperties.getInstance().isOscarLearning() && roles != null && roles.indexOf("moderator") != -1) {
			logger.info("skipping domain check..provider is a moderator");
		} else if (!caseManagementMgr.isClientInProgramDomain(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo(), demoNo)) {
			return mapping.findForward("domain-error");
		}
		String programId = (String) request.getSession().getAttribute("case_program_id");

		viewCurrentIssuesTab_newCmeNotesOpt(request, caseForm, demoNo, programId);

		return mapping.findForward("ajaxDisplayNotes");
	}


	private void viewCurrentIssuesTab_newCmeNotesOpt(HttpServletRequest request, CaseManagementViewFormBean caseForm, String demoNo, String programId) throws Exception {
		List<EChartNoteEntry> entries = new ArrayList<EChartNoteEntry>();

		int demographicId=Integer.parseInt(demoNo);
		long startTime = System.currentTimeMillis();
		long intTime = System.currentTimeMillis();

		//Gets some of the note data, no relationships, not the note/history..just enough
		List<Map<String,Object>> notes = this.caseManagementNoteDao.getRawNoteInfoMapByDemographic(demoNo);
		Map<String,Object> filteredNotes = new LinkedHashMap<String,Object>();

		//This gets rid of old revisions (better than left join on a computed subset of itself
		Integer programNo;
		for(Map<String,Object> note:notes) {
			if(filteredNotes.get(note.get("uuid"))!=null)
				continue;
			filteredNotes.put((String)note.get("uuid"),true);
			EChartNoteEntry e = new EChartNoteEntry();
			e.setId(note.get("id"));
			e.setDate((Date)note.get("observation_date"));
			e.setProviderNo((String)note.get("providerNo"));
			
			programNo = note.get("program_no").equals("") ? null : Integer.parseInt((String)note.get("program_no"));
			e.setProgramId(programNo);
			e.setRole((String)note.get("reporter_caisi_role"));
			e.setType("local_note");
			entries.add(e);

		}
		logger.info("FETCHED " + notes.size() + " NOTE META IN " + (System.currentTimeMillis()-intTime) + "ms");
		intTime = System.currentTimeMillis();

		List<CachedDemographicNote> remoteNotesInfo = getRemoteNoteIds(demographicId);
		if(remoteNotesInfo != null) {
			for(CachedDemographicNote note:remoteNotesInfo) {
				EChartNoteEntry e = new EChartNoteEntry();
				e.setId(note.getCachedDemographicNoteCompositePk());
				e.setDate(note.getObservationDate().getTime());
				e.setProviderNo(note.getObservationCaisiProviderId());
				e.setRole(note.getRole());
				e.setType("remote_note");
				entries.add(e);
			}
		}

		if(remoteNotesInfo != null)
			logger.info("FETCHED " + remoteNotesInfo.size() + " REMOTE NOTE META IN " + (System.currentTimeMillis()-intTime) + "ms");
		intTime = System.currentTimeMillis();

		List<GroupNoteLink> groupNotesInfo = this.getGroupNoteIds(demographicId);
		if(groupNotesInfo != null) {
			for(GroupNoteLink note:groupNotesInfo) {
				EChartNoteEntry e = new EChartNoteEntry();
				e.setId(note.getNoteId());
				e.setDate(note.getCreated());
				//e.setProviderNo(note.get);
				//e.setRoleId(roleId)
				e.setType("group_note");
				entries.add(e);
			}
		}

		if(groupNotesInfo != null)
			logger.info("FETCHED " + groupNotesInfo.size() + " GROUP NOTES META IN " + (System.currentTimeMillis()-intTime) + "ms");
		intTime = System.currentTimeMillis();


		String roleName = (String) request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
		ArrayList<HashMap<String, ? extends Object>> eForms = EFormUtil.listPatientEFormsNoData(demoNo, roleName);
		for(HashMap<String, ? extends Object> eform:eForms) {
			EChartNoteEntry e = new EChartNoteEntry();
			e.setId(eform.get("fdid"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			e.setDate(sdf.parse((String)eform.get("formDate") + " " + (String)eform.get("formTime")));
			e.setProviderNo((String)eform.get("providerNo"));
			e.setType("eform");
			entries.add(e);
		}

		logger.info("FETCHED " + eForms.size() + " EFORMS META IN " + (System.currentTimeMillis()-intTime) + "ms");
		intTime = System.currentTimeMillis();

		ArrayList<PatientForm> allPatientForms=EctFormData.getGroupedPatientFormsFromAllTables(demographicId);
		for (PatientForm patientForm : allPatientForms) {
			EChartNoteEntry e = new EChartNoteEntry();
			e.setId(new String[]{patientForm.getFormName(),patientForm.getFormId()});
			SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
			e.setDate(sdf.parse(patientForm.getEdited()));
			//e.setProviderNo(patientForm.get);
			//e.setProgramId(Integer.parseInt((String)note[3]));
			//e.setRoleId(Integer.parseInt((String)note[4]));
			e.setType("encounter_form");
			entries.add(e);
		}

		logger.info("FETCHED " + allPatientForms.size() + " FORMS IN " + (System.currentTimeMillis()-intTime) + "ms");
		intTime = System.currentTimeMillis();


		List<Map<String,Object>>bills = null;
		if( oscar.OscarProperties.getInstance().getProperty("billregion","").equalsIgnoreCase("ON") ) {
			bills= billingClaimDao.getInvoicesMeta(demoNo);
			for( Map<String,Object> h1 : bills ) {
				EChartNoteEntry e = new EChartNoteEntry();
				e.setId(h1.get("id"));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				e.setDate(sdf.parse(h1.get("billing_date") + " " + h1.get("billing_time")));
				e.setProviderNo((String)h1.get("provider_no"));
				//e.setProgramId(Integer.parseInt((String)note[3]));
				//e.setRoleId(Integer.parseInt((String)note[4]));
				e.setType("invoice");
				entries.add(e);
			}

			logger.info("FETCHED " + bills.size() + " INVIOCES META IN " + (System.currentTimeMillis()-intTime) + "ms");
			intTime = System.currentTimeMillis();

		}



		//we now have this huge list
		//sort it by date or whatever
		if(request.getParameter("note_sort") != null && request.getParameter("note_sort").length()>0) {
			String sort = request.getParameter("note_sort");
			if("observation_date_desc".equals(sort)) {
				Collections.sort(entries, EChartNoteEntry.getDateComparatorDesc());
			} else if("observation_date_asc".equals(sort)) {
				Collections.sort(entries, EChartNoteEntry.getDateComparator());
			}
		} else {
			Collections.sort(entries, EChartNoteEntry.getDateComparator());
		}

		logger.info("SORTED " + entries.size() + " IN " + (System.currentTimeMillis()-intTime) + "ms");
		intTime = System.currentTimeMillis();


		//apply CAISI permission filter - local notes
		entries = caseManagementMgr.filterNotes1(entries, programId);
		logger.info("FILTER NOTES (CAISI) " + (System.currentTimeMillis() - intTime) + "ms");
		intTime = System.currentTimeMillis();

		//TODO: role based filter for eforms?

		//apply provider filter
		entries = applyProviderFilter(entries, caseForm.getFilter_providers());
		logger.info("FILTER NOTES PROVIDER " + (System.currentTimeMillis() - intTime) + "ms");
		intTime = System.currentTimeMillis();

		//apply role filter
		entries = applyRoleFilter1(entries, caseForm.getFilter_roles());
		logger.info("FILTER NOTES ROLES " + (System.currentTimeMillis() - intTime) + "ms");
		intTime = System.currentTimeMillis();

		//apply issues filter
		String[] checkedIssues = request.getParameterValues("issues");
		entries = applyIssueFilter1(entries,checkedIssues);
		logger.info("FILTER NOTES ISSUES " + (System.currentTimeMillis() - intTime) + "ms");
		intTime = System.currentTimeMillis();


		List<EChartNoteEntry> slice = new ArrayList<EChartNoteEntry>();

		int numToReturn = 20;
		if (request.getParameter("numToReturn") != null && request.getParameter("numToReturn").length()>0) {
			numToReturn = Integer.parseInt(request.getParameter("numToReturn"));
		}

		if (request.getParameter("offset") == null || request.getParameter("offset").equalsIgnoreCase("0")) {
			//this is the first fetch, we want the last items up to numToReturn
			int endOfTheList = entries.size();
			int startingPoint = endOfTheList-numToReturn;
			if(startingPoint<0)
				startingPoint=0;
			for(int x=startingPoint;x<endOfTheList;x++){
				slice.add(entries.get(x));
			}
		} else {
			int offset = Integer.parseInt(request.getParameter("offset"));
			if(entries.size() >= offset) {
				int endingPoint = entries.size()-offset;
				int startingPoint = endingPoint-numToReturn;
				if(startingPoint < 0)
					startingPoint = 0;
				for(int x=startingPoint;x<endingPoint;x++){
					slice.add(entries.get(x));
				}
			}
			request.setAttribute("moreNotes", true);
		}

		logger.info("CREATED SLICE OF SIZE  " + slice.size() + " IN " + (System.currentTimeMillis()-intTime) + "ms");
		intTime = System.currentTimeMillis();


		//we now have the slice we want to return
		ArrayList<NoteDisplay> notesToDisplay = new ArrayList<NoteDisplay>();

		if(slice.size() > 0) {
			//figure out what we need to retrieve
			List<Long> localNoteIds = new ArrayList<Long>();
			List<CachedDemographicNoteCompositePk> remoteNoteIds = new ArrayList<CachedDemographicNoteCompositePk>();
			List<Long> groupNoteIds = new ArrayList<Long>();
			List<Integer> invoiceIds = new ArrayList<Integer>();

			for(EChartNoteEntry entry:slice) {
				if(entry.getType().equals("local_note")) {
					localNoteIds.add((Long)entry.getId());
				}
				else if(entry.getType().equals("remote_note")) {
					remoteNoteIds.add((CachedDemographicNoteCompositePk)entry.getId());
				}
				else if(entry.getType().equals("invoice")) {
					invoiceIds.add((Integer)entry.getId());
				}
				else if(entry.getType().equals("group_note")) {
					groupNoteIds.add(((Integer)entry.getId()).longValue());
				}
			}

			List<CaseManagementNote> localNotes = caseManagementNoteDao.getNotes(localNoteIds);

			logger.info("FETCHED " + localNotes.size() + " NOTES IN " + (System.currentTimeMillis()-intTime) + "ms");
			intTime = System.currentTimeMillis();

			List<CachedDemographicNote> remoteNotes =new ArrayList<CachedDemographicNote>();
			if(remoteNoteIds != null && remoteNoteIds.size()>0)
				remoteNotes = CaisiIntegratorManager.getLinkedNotes(remoteNoteIds);

			logger.info("FETCHED " + remoteNotes.size() + " REMOTE NOTES IN " + (System.currentTimeMillis()-intTime) + "ms");
			intTime = System.currentTimeMillis();

			List<CaseManagementNote> groupNotes = caseManagementNoteDao.getNotes(groupNoteIds);

			logger.info("FETCHED " + groupNotes.size() + " GROUP NOTES IN " + (System.currentTimeMillis()-intTime) + "ms");
			intTime = System.currentTimeMillis();

			List<BillingClaimHeader1> invoices = billingClaimDao.getInvoicesByIds(invoiceIds);

			logger.info("FETCHED " + invoices.size() + " INVOICES IN " + (System.currentTimeMillis()-intTime) + "ms");
			intTime = System.currentTimeMillis();

			this.caseManagementMgr.getEditors(localNotes);
			for(EChartNoteEntry entry:slice) {
				if(entry.getType().equals("local_note")) {
					notesToDisplay.add(new NoteDisplayLocal(findNote((Long)entry.getId(),localNotes)));
				}
				else if(entry.getType().equals("remote_note")) {
					notesToDisplay.add(new NoteDisplayIntegrator(findRemoteNote((CachedDemographicNoteCompositePk)entry.getId(),remoteNotes)));
				}
				else if(entry.getType().equals("eform")) {
					notesToDisplay.add(new NoteDisplayNonNote(findEform((String)entry.getId(),eForms)));
				}
				else if(entry.getType().equals("encounter_form")) {
					notesToDisplay.add(new NoteDisplayNonNote(findPatientForm((String[])entry.getId(),allPatientForms)));
				}
				else if(entry.getType().equals("invoice")) {
					notesToDisplay.add(new NoteDisplayNonNote(findInvoice((Integer)entry.getId(),invoices)));
				}
				else if(entry.getType().equals("group_note")) {
					CaseManagementNote note = findNote(((Integer)entry.getId()).longValue(),groupNotes);
					NoteDisplayLocal disp = new NoteDisplayLocal(note);
					disp.setReadOnly(true);
					disp.setGroupNote(true);
					disp.setLocation(String.valueOf(note.getDemographic_no()));
					notesToDisplay.add(disp);
				}
			}

		}
		logger.info("Total Time to load the notes=" + (System.currentTimeMillis()-startTime) + "ms.");
		request.setAttribute("notesToDisplay", notesToDisplay);
	}

	public CaseManagementNote findNote(Long id, List<CaseManagementNote> notes) {
		for(CaseManagementNote note:notes) {
			if(id.equals(note.getId())) {
				notes.remove(note);
				return note;
			}
		}
		return null;
	}

	public CachedDemographicNote findRemoteNote(CachedDemographicNoteCompositePk id, List<CachedDemographicNote> notes) {
		for(CachedDemographicNote note:notes) {
			if(id.getIntegratorFacilityId().equals(note.getCachedDemographicNoteCompositePk().getIntegratorFacilityId()) && id.getUuid().equals(note.getCachedDemographicNoteCompositePk().getUuid())) {
				//notes.remove(note);
				return note;
			}
		}
		return null;
	}

	public HashMap<String, ? extends Object> findEform(String id, ArrayList<HashMap<String, ? extends Object>> eforms) {
		for(HashMap<String, ? extends Object> eform:eforms) {
			if(id.equals(eform.get("fdid"))) {
				eforms.remove(eform);
				return eform;
			}
		}
		return null;
	}

	public PatientForm findPatientForm(String[] id, List<PatientForm> forms) {
		for(PatientForm form:forms) {
			if(id[0].equals(form.getFormName()) && id[1].equals(form.getFormId())) {
				forms.remove(form);
				return form;
			}
		}
		return null;
	}

	public BillingClaimHeader1 findInvoice(Integer id, List<BillingClaimHeader1> invoices) {
		for(BillingClaimHeader1 invoice:invoices) {
			if(id.equals(invoice.getId())) {
				invoices.remove(invoice);
				return invoice;
			}
		}
		return null;
	}

	private List<CachedDemographicNote> getRemoteNoteIds(int demographicNo) {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();

		if (!loggedInInfo.currentFacility.isIntegratorEnabled()) return null;
		List<CachedDemographicNote> linkedNotes  = null;
		try {
			if (!CaisiIntegratorManager.isIntegratorOffline()){
			   linkedNotes = CaisiIntegratorManager.getLinkedNotesMetaData(demographicNo);
			}
		} catch (Exception e) {
			logger.error("Unexpected error.", e);
			CaisiIntegratorManager.checkForConnectionError(e);
		}

		if(CaisiIntegratorManager.isIntegratorOffline()){
			//TODO: No idea how this works
		  // linkedNotes = IntegratorFallBackManager.getLinkedNotes(demographicNo);
		}

		if (linkedNotes == null) return null;

		return linkedNotes;

	}

	private List<EChartNoteEntry> applyProviderFilter(List<EChartNoteEntry> notes, String[] providerNo) {
		boolean filter = false;
		List<EChartNoteEntry> filteredNotes = new ArrayList<EChartNoteEntry>();

		if (providerNo != null && Arrays.binarySearch(providerNo, "a") < 0) {
			filter = true;
			Arrays.sort(providerNo);
		}

		for (Iterator<EChartNoteEntry> iter = notes.iterator(); iter.hasNext();) {
			EChartNoteEntry note = iter.next();
			if(!note.getType().equals("local_note") && !note.getType().equals("eform") && !note.getType().equals("encounter_form")
					&& !note.getType().equals("invoice")) {
				filteredNotes.add(note);
				continue;
			}
			if (!filter) {
				filteredNotes.add(note);

			} else {
				if(note.getProviderNo()==null) continue;
				if (Arrays.binarySearch(providerNo, note.getProviderNo()) >= 0)
				filteredNotes.add(note);
			}
		}

		return filteredNotes;
	}

	private List<EChartNoteEntry> applyRoleFilter1(List<EChartNoteEntry> notes, String[] roleId) {

		if(roleId == null || roleId.length==0) {
			return notes;
		}
		// if no filter return everything
		if (Arrays.binarySearch(roleId, "a") >= 0) {
			return notes;
		}

		Arrays.sort(roleId);

		List<EChartNoteEntry> filteredNotes = new ArrayList<EChartNoteEntry>();

		for (Iterator<EChartNoteEntry> iter = notes.listIterator(); iter.hasNext();) {
			EChartNoteEntry note = iter.next();
			if(!note.getType().equals("local_note")) {
				filteredNotes.add(note);
				continue;
			}

			if (Arrays.binarySearch(roleId, note.getRole()) >= 0)
				filteredNotes.add(note);
		}

		return filteredNotes;
	}

	private List<EChartNoteEntry> applyIssueFilter1(List<EChartNoteEntry> notes, String[] issueId) {

		if(issueId == null || issueId.length==0)
			return notes;

		// if no filter return everything
		if (Arrays.binarySearch(issueId, "a") >= 0)
			return notes;

		List<EChartNoteEntry> filteredNotes = new ArrayList<EChartNoteEntry>();

		List<Integer> noteIds = cmeIssueNotesDao.getNoteIdsWhichHaveIssues(issueId);

		//Integer
		if(noteIds != null) {
			for(EChartNoteEntry note:notes) {
				if(!note.getType().equals("local_note")) {
					filteredNotes.add(note);
					continue;
				}
				Integer tmp =((Long)note.getId()).intValue();
				if(noteIds.contains(tmp)) {
					filteredNotes.add(note);
				}
			}
		}


		return filteredNotes;
	}

	private List<GroupNoteLink> getGroupNoteIds(int demographicNo) {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();

		if (!loggedInInfo.currentFacility.isEnableGroupNotes()) return new ArrayList<GroupNoteLink>();

		return groupNoteDao.findLinksByDemographic(demographicNo);

	}
}
