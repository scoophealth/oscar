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
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.model.CaseManagementTmpSave;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementPrintPdf;
import org.oscarehr.casemgmt.web.CaseManagementViewAction.IssueDisplay;
import org.oscarehr.casemgmt.web.formbeans.CaseManagementEntryFormBean;
import org.oscarehr.common.model.DxAssociation;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SessionConstants;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;
import org.springframework.web.context.WebApplicationContext;

import oscar.OscarProperties;
import oscar.dms.EDocUtil;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.oscarSurveillance.SurveillanceMaster;
import oscar.service.OscarSuperManager;
import oscar.util.UtilDateUtilities;

import com.lowagie.text.DocumentException;

/*
 * Updated by Eugene Petruhin on 12 and 13 jan 2009 while fixing #2482832 & #2494061
 * Updated by Eugene Petruhin on 21 jan 2009 while fixing missing "New Note" link
 */
public class CaseManagementEntryAction extends BaseCaseManagementEntryAction {

	private static Logger logger = MiscUtils.getLogger();

	private CaseManagementNoteDAO caseManagementNoteDao = (CaseManagementNoteDAO) SpringUtils.getBean("caseManagementNoteDAO");
	private CaseManagementIssueDAO caseManagementIssueDao = (CaseManagementIssueDAO) SpringUtils.getBean("caseManagementIssueDAO");
	private IssueDAO issueDao = (IssueDAO) SpringUtils.getBean("IssueDAO");

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return edit(mapping, form, request, response);
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("Edit Starts");
		long start = System.currentTimeMillis();
		long beginning = start;
		long current = 0;
		if (request.getSession().getAttribute("userrole") == null) {
			response.sendError(response.SC_FORBIDDEN);
			return null;
		}

		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		cform.setChain("");
		request.setAttribute("change_flag", "false");
		request.setAttribute("from", "casemgmt");

		logger.debug("Get demo and provider no");
		String demono = getDemographicNo(request);
		Integer demographicNo = Integer.parseInt(demono);
		String providerNo = getProviderNo(request);
		current = System.currentTimeMillis();
		logger.debug("Get demo and provider no " + String.valueOf(current - start));
		start = current;

		String programIdString = (String) request.getSession().getAttribute("case_program_id");
		Integer programId = null;
		try {
			programId = Integer.parseInt(programIdString);
		} catch (Exception e) {
			logger.warn("Error parsing programId:" + programIdString, e);
		}

		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));

		/* process the request from other module */
		if (!"casemgmt".equalsIgnoreCase(request.getParameter("from"))) {

			// no demographic number, no page
			if (request.getParameter("demographicNo") == null || "".equals(request.getParameter("demographicNo"))) {
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
			if (oscarVariables != null) {
				province = ((String) oscarVariables.getProperty("billregion", "")).trim().toUpperCase();
			}

			String strBeanName = "casemgmt_oscar_bean" + demono;
			EctSessionBean bean = (EctSessionBean) request.getSession().getAttribute(strBeanName);

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
				url = bsurl + "/oscar/billing/procedimentoRealizado/init.do?appId=" + bean.appointmentNo;
			} else {
				// StringEncoderUtils.a();
				String default_view = "";
				if (oscarVariables != null) {
					default_view = oscarVariables.getProperty("default_view", "");
				}
				url = bsurl + "/billing.do?billRegion=" + java.net.URLEncoder.encode(province, "UTF-8") + "&billForm=" + java.net.URLEncoder.encode(default_view, "UTF-8") + "&hotclick=" + java.net.URLEncoder.encode("", "UTF-8") + "&appointment_no=" + bean.appointmentNo + "&appointment_date=" + bean.appointmentDate + "&start_time=" + Hour + ":" + Min + "&demographic_name=" + java.net.URLEncoder.encode(bean.patientLastName + "," + bean.patientFirstName, "UTF-8") + "&demographic_no=" + bean.demographicNo
				        + "&providerview=" + bean.curProviderNo + "&user_no=" + bean.providerNo + "&apptProvider_no=" + bean.curProviderNo + "&bNewForm=1&status=t";
			}
			request.getSession().setAttribute("billing_url", url);
		}

		/* remove the remembered echart string */
		request.getSession().setAttribute("lastSavedNoteString", null);

		logger.debug("Get Issues and filter them");
		current = System.currentTimeMillis();
		logger.debug("Get Issues and filter them " + String.valueOf(current - start));
		start = current;

		cform.setDemoNo(demono);
		CaseManagementNote note = null;

		String nId = request.getParameter("noteId");
		String forceNote = request.getParameter("forceNote");
		if (forceNote == null) forceNote = "false";

		logger.debug("NoteId " + nId);

		String maxTmpSave = oscar.OscarProperties.getInstance().getProperty("maxTmpSave", "");
		logger.debug("maxTmpSave " + maxTmpSave);
		// set date 2 weeks in past so we retrieve more recent saved notes
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -14);
		Date twoWeeksAgo = cal.getTime();
		logger.debug("Get tmp note");
		CaseManagementTmpSave tmpsavenote;
		if (maxTmpSave.equalsIgnoreCase("off")) {
			tmpsavenote = this.caseManagementMgr.restoreTmpSave(providerNo, demono, programIdString);
		} else {
			tmpsavenote = this.caseManagementMgr.restoreTmpSave(providerNo, demono, programIdString, twoWeeksAgo);
		}
		current = System.currentTimeMillis();
		logger.debug("Get tmp note " + String.valueOf(current - start));
		start = current;

		logger.debug("Get Note for editing");

		// create a new note
		if (request.getParameter("note_edit") != null && request.getParameter("note_edit").equals("new")) {
			logger.debug("NEW NOTE GENERATED");
			request.getSession().setAttribute("newNote", "true");
			request.getSession().setAttribute("issueStatusChanged", "false");
			request.setAttribute("newNoteIdx", request.getParameter("newNoteIdx"));

			note = new CaseManagementNote();
			note.setProviderNo(providerNo);
			Provider prov = new Provider();
			prov.setProviderNo(providerNo);
			note.setProvider(prov);
			note.setDemographic_no(demono);

			this.insertReason(request, note);

			resetTemp(providerNo, demono, programIdString);

		}
		// get the last temp note?
		else if (tmpsavenote != null && !forceNote.equals("true")) {
			logger.debug("tempsavenote is NOT NULL");
			if (tmpsavenote.getNote_id() > 0) {
				request.getSession().setAttribute("newNote", "false");
				request.setAttribute("noteId", String.valueOf(tmpsavenote.getNote_id()));
				note = caseManagementMgr.getNote(String.valueOf(tmpsavenote.getNote_id()));
				logger.debug("Restoring " + String.valueOf(note.getId()));
			} else {
				request.getSession().setAttribute("newNote", "true");
				request.getSession().setAttribute("issueStatusChanged", "false");
				note = new CaseManagementNote();
				note.setProviderNo(providerNo);
				Provider prov = new Provider();
				prov.setProviderNo(providerNo);
				note.setProvider(prov);
				note.setDemographic_no(demono);
			}

			note.setNote(tmpsavenote.getNote());

		}
		// get an existing non-temp note?
		else if (nId != null && Integer.parseInt(nId) > 0) {
			logger.debug("Using nId " + nId + " to fetch note");
			request.getSession().setAttribute("newNote", "false");
			note = caseManagementMgr.getNote(nId);

			if (note.getHistory() == null || note.getHistory().equals("")) {
				// old note - we need to save the original in here
				note.setHistory(note.getNote());

				caseManagementMgr.saveNoteSimple(note);
				addNewNoteLink(Long.parseLong(nId));
			}

		}
		// no note specified, get last unsigned
		else {
			// A hack to load last unsigned note when not specifying a particular note to edit
			// if there is no unsigned note load a new one
			if ((note = getLastSaved(request, demono, providerNo)) == null) {
				request.getSession().setAttribute("newNote", "true");
				request.getSession().setAttribute("issueStatusChanged", "false");
				note = new CaseManagementNote();
				note.setProviderNo(providerNo);
				Provider prov = new Provider();
				prov.setProviderNo(providerNo);
				note.setProvider(prov);
				note.setDemographic_no(demono);

				this.insertReason(request, note);
			}
		}
		current = System.currentTimeMillis();
		logger.debug("Get note to edit " + String.valueOf(current - start));
		start = current;

		/*
		 * do the restore if(restore != null && restore.booleanValue() == true) { String tmpsavenote = this.caseManagementMgr.restoreTmpSave(providerNo,demono,programId); if(tmpsavenote != null) { note.setNote(tmpsavenote); } }
		 */
		logger.debug("Set Encounter Type: " + note.getEncounter_type());
		logger.debug("Fetched Note " + String.valueOf(note.getId()));

		logger.debug("Populate Note with editors");
		this.caseManagementMgr.getEditors(note);
		current = System.currentTimeMillis();
		logger.debug("Populate Note with editors " + String.valueOf(current - start));
		start = current;

		// put the new/retrieved not in the form object for rendering on page
		cform.setCaseNote(note);
		/* set issue checked list */

		// get issues for current demographic, based on provider rights

		Boolean useNewCaseMgmt = new Boolean((String) request.getSession().getAttribute("newCaseManagement"));

		CheckBoxBean[] checkedList = null;
		if (useNewCaseMgmt) {

			CaseManagementViewAction caseManagementViewAction = new CaseManagementViewAction();
			ArrayList<CheckBoxBean> checkBoxBeanList = new ArrayList<CheckBoxBean>();
			caseManagementViewAction.addLocalIssues(checkBoxBeanList, demographicNo, false, programId);
			caseManagementViewAction.addRemoteIssues(checkBoxBeanList, demographicNo, false);

			checkedList = checkBoxBeanList.toArray(new CheckBoxBean[checkBoxBeanList.size()]);
			/*
			 * List<CaseManagementIssue> issues = caseManagementMgr.filterIssues(caseManagementMgr.getIssues(Integer.parseInt(demono)), programIdString); checkedList = new CheckBoxBean[issues.size()]; // set issue checked list
			 * log.debug("Set Checked Issues " + String.valueOf(current-start)); List allNotes = this.caseManagementMgr.getNotes(demono); for (int i = 0; i < issues.size(); i++) { checkedList[i] = new CheckBoxBean(); CaseManagementIssue iss =
			 * issues.get(i); checkedList[i].setIssue(iss); checkedList[i].setUsed(haveIssue(iss.getId(), allNotes)); current = System.currentTimeMillis(); log.debug("Set Checked Issues " + String.valueOf(current-start)); start = current; }
			 */
			Iterator itr = note.getIssues().iterator();
			while (itr.hasNext()) {
				int id = ((CaseManagementIssue) itr.next()).getId().intValue();
				SetChecked(checkedList, id);
			}

		} else // old CME
		{
			CaseManagementViewAction caseManagementViewAction = new CaseManagementViewAction();
			ArrayList<CheckBoxBean> checkBoxBeanList = new ArrayList<CheckBoxBean>();
			caseManagementViewAction.addLocalIssues(checkBoxBeanList, demographicNo, false, programId);
			caseManagementViewAction.addRemoteIssues(checkBoxBeanList, demographicNo, false);
			caseManagementViewAction.addGroupIssues(checkBoxBeanList, demographicNo, false);

			checkedList = checkBoxBeanList.toArray(new CheckBoxBean[0]);

			for (CaseManagementIssue cmi : note.getIssues()) {
				setChecked_oldCme(checkedList, cmi);
			}
		}

		current = System.currentTimeMillis();

		cform.setIssueCheckList(checkedList);

		cform.setSign("off");
		if (!note.isIncludeissue()) cform.setIncludeIssue("off");
		else cform.setIncludeIssue("on");

		boolean passwd = caseManagementMgr.getEnabled();
		String chain = request.getParameter("chain");

		current = System.currentTimeMillis();
		logger.debug("The End of Edit " + String.valueOf(current - beginning));
		start = current;

		LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.EDIT, LogConst.CON_CME_NOTE, String.valueOf(note.getId()), request.getRemoteAddr(), demono, note.getAuditString());

		String frmName = "caseManagementEntryForm" + demono;
		logger.debug("Setting session form - " + frmName + " - " + String.valueOf(cform != null));
		request.getSession().setAttribute(frmName, cform);

		ActionForward fwd, finalFwd = null;
		if (chain != null && chain.length() > 0) {
			request.getSession().setAttribute("passwordEnabled", passwd);
			fwd = mapping.findForward(chain);
		} else {
			request.setAttribute("passwordEnabled", passwd);

			String ajax = request.getParameter("ajax");
			if (ajax != null && ajax.equalsIgnoreCase("true")) {
				fwd = mapping.findForward("issueList_ajax");
			} else {
				fwd = mapping.findForward("view");
			}
		}

		if (fwd != null) {
			StringBuilder path = new StringBuilder(fwd.getPath());

			if (path.indexOf("?") == -1) path.append("?");
			else path.append("&");

			path.append("demographicNo=" + demono);
			String noteBody = request.getParameter("noteBody");

			if (noteBody != null) path.append("&noteBody=" + noteBody);

			finalFwd = new ActionForward(path.toString());
		}
		return finalFwd;
	}

	private void setChecked_oldCme(CheckBoxBean[] checkedList, CaseManagementIssue cmi) {
		for (CheckBoxBean cbb : checkedList) {
			if (cbb.getIssueDisplay().code.equals(cmi.getIssue().getCode())) {
				cbb.setChecked("on");
				return;
			}
		}
	}

	public void resetTemp(String providerNo, String demoNo, String programId) {
		try {
			this.caseManagementMgr.deleteTmpSave(providerNo, demoNo, programId);
		} catch (Throwable e) {
			logger.warn(e);
		}
	}

	public ActionForward issueNoteSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String strNote = request.getParameter("value");
		// strNote = strNote.trim();
		logger.debug("Saving: " + strNote);
		strNote = org.apache.commons.lang.StringUtils.trimToNull(strNote);
		if (strNote == null || strNote.equals("")) return null;

		String providerNo = getProviderNo(request);
		Provider provider = getProvider(request);
		String userName = provider != null ? provider.getFullName() : "";

		String demo = getDemographicNo(request);

		String noteId = request.getParameter("noteId");
		logger.debug("SAVING NOTE " + noteId + " STRING: " + strNote);
		String issueChange = request.getParameter("issueChange");
		String archived = request.getParameter("archived");

		CaseManagementNote note;
		boolean newNote = false;
		// we don't want to try to remove an issue from a new note so we test here
		if (noteId.equals("0")) {

			note = new CaseManagementNote();
			note.setDemographic_no(demo);
			newNote = true;
		} else {
			note = this.caseManagementMgr.getNote(noteId);
			// if note has not changed don't save
			if (strNote.equals(note.getNote()) && !issueChange.equals("true") && (archived == null || archived.equalsIgnoreCase("false"))) return null;
		}

		note.setNote(strNote);
		note.setProviderNo(providerNo);
		note.setSigning_provider_no(providerNo);
		note.setSigned(true);

		if (provider != null) note.setProvider(provider);

		String logAction = "";
		if (archived == null || archived.equalsIgnoreCase("false")) {
			note.setArchived(false);
		} else {
			note.setArchived(true);
			logger.debug("Setting archived to true");
			logAction = LogConst.ARCHIVE;
		}

		logger.debug("Note archived " + note.isArchived());
		String programId = (String) request.getSession().getAttribute("case_program_id");
		note.setProgram_no(programId);

		WebApplicationContext ctx = this.getSpringContext();

		ProgramManager programManager = (ProgramManager) ctx.getBean("programManager");
		AdmissionManager admissionManager = (AdmissionManager) ctx.getBean("admissionManager");

		String role = null;
		String team = null;

		try {
			role = String.valueOf((programManager.getProgramProvider(note.getProviderNo(), note.getProgram_no())).getRole().getId());
		} catch (Throwable e) {
			logger.error(e);
			role = "0";
		}

		note.setReporter_caisi_role(role);

		try {
			team = String.valueOf((admissionManager.getAdmission(note.getProgram_no(), Integer.valueOf(note.getDemographic_no()))).getTeamId());
		} catch (Throwable e) {
			logger.error(e);
			team = "0";
		}
		note.setReporter_program_team(team);

		// update note issues
		String sessionFrmName = "caseManagementEntryForm" + demo;
		CaseManagementEntryFormBean sessionFrm = (CaseManagementEntryFormBean) request.getSession().getAttribute(sessionFrmName);
		Set issueSet = new HashSet();
		Set noteSet = new HashSet();
		String[] issue_id = request.getParameterValues("issue_id");
		CheckBoxBean[] existingCaseIssueList = sessionFrm.getIssueCheckList();
		ArrayList<CheckBoxBean> caseIssueList = new ArrayList<CheckBoxBean>();

		// copy existing issues for sessionfrm
		for (int idx = 0; idx < existingCaseIssueList.length; ++idx) {
			caseIssueList.add(existingCaseIssueList[idx]);
		}

		// first we check if any notes have been removed
		Set<CaseManagementIssue> noteIssues = note.getIssues();
		Iterator<CaseManagementIssue> iter = noteIssues.iterator();
		CaseManagementIssue cIssue;
		boolean issueExists;
		StringBuilder issueNames = new StringBuilder();
		int j;

		// we need the defining issue as originally passed in
		String reloadQuery = request.getParameter("reloadUrl");
		String[] params = reloadQuery.split("&");
		String cppStrIssue = "";
		for (int p = 0; p < params.length; ++p) {
			String[] keyVal = params[p].split("=");
			if (keyVal[0].equalsIgnoreCase("issue_code")) {
				cppStrIssue = keyVal[1];
				break;
			}
		}

		boolean removed = false;

		// we've removed all issues so record that
		ResourceBundle props = ResourceBundle.getBundle("oscarResources");
		if (issue_id == null) {
			while (iter.hasNext()) {
				cIssue = iter.next();
				issueNames.append(cIssue.getIssue().getDescription() + "\n");
			}

			strNote += "\n" + new SimpleDateFormat("dd-MMM-yyyy", request.getLocale()).format(new Date()) + " " + props.getString("oscarEncounter.removedIssue.Msg") + ":\n" + issueNames.toString();
			note.setNote(strNote);
			removed = true;
		} else {
			// check to see if we have removed any issues
			while (iter.hasNext()) {
				cIssue = iter.next();
				issueExists = false;
				for (j = 0; j < issue_id.length; ++j) {
					if (Long.parseLong(issue_id[j]) == cIssue.getIssue_id()) {
						issueExists = true;
						break;
					}
				}

				if (!issueExists) {
					issueNames.append(cIssue.getIssue().getDescription() + "\n");
					if (cIssue.getIssue().getCode().equalsIgnoreCase(cppStrIssue)) {
						removed = true;
					}
				}
			}

			// if we have removed an issue add it to message body
			if (issueNames.length() > 0) {
				strNote += "\n" + new SimpleDateFormat("dd-MMM-yyyy", request.getLocale()).format(new Date()) + " " + props.getString("oscarEncounter.removedIssue.Msg") + ":\n" + issueNames.toString();
				note.setNote(strNote);
			}

			for (int idx = 0; idx < issue_id.length; ++idx) {
				cIssue = this.caseManagementMgr.getIssueById(demo, issue_id[idx]);
				if (cIssue == null) {
					Issue issue = this.caseManagementMgr.getIssue(issue_id[idx]);
					cIssue = this.newIssueToCIssue(demo, issue, Integer.parseInt(programId));
					cIssue.setNotes(noteSet);

					// we have a new issue so add it to sessionfrm list
					CheckBoxBean checkbox = new CheckBoxBean();
					checkbox.setIssue(cIssue);
					checkbox.setChecked("off");
					checkbox.setUsed(true);
					caseIssueList.add(checkbox);
				}
				issueSet.add(cIssue);

			} // end for

			CheckBoxBean[] newCheckBox = new CheckBoxBean[caseIssueList.size()];
			newCheckBox = caseIssueList.toArray(newCheckBox);
			sessionFrm.setIssueCheckList(newCheckBox);

		}
		note.setIssues(issueSet);

		// now we can update the order of the notes if necessary
		// if the note has been removed or archived we move notes up in the order
		// else we check if position has changed and move the note down
		int position = -1;
		int newPos = Integer.parseInt(request.getParameter("position"));

		List<Issue> cppIssue = caseManagementMgr.getIssueInfoByCode(providerNo, cppStrIssue);
		List<CaseManagementNote> curCPPNotes = new ArrayList<CaseManagementNote>();
		if (cppIssue.size() > 0) {
			String[] strIssueId = { String.valueOf(cppIssue.get(0).getId()) };
			curCPPNotes = this.caseManagementMgr.getActiveNotes(demo, strIssueId);
		}

		CaseManagementNote curNote;
		long nId = Long.parseLong(noteId);
		int numNotes = curCPPNotes.size();
		// Alas we have to cycle through to make sure an ordering has been set
		// this is for legacy data
		for (int idx = 1; idx < curCPPNotes.size(); ++idx) {
			curNote = curCPPNotes.get(idx);
			if (curNote.getPosition() == 0) {
				curNote.setPosition(idx);

				if (curNote.getId() == nId) {
					note.setPosition(idx);
				}

				this.caseManagementMgr.updateNote(curNote);
			}
		}

		if (removed || note.isArchived()) {
			position = note.getPosition();
			for (CaseManagementNote c : curCPPNotes) {
				if (c.getId() == nId) {
					continue;
				} else if (position < c.getPosition()) {
					newPos = c.getPosition() - 1;
					c.setPosition(newPos);
					this.caseManagementMgr.updateNote(c);
				}
			}
		} else if ((newPos != note.getPosition() && !(newPos == numNotes && note.getPosition() == (numNotes - 1))) || newNote) {
			for (CaseManagementNote c : curCPPNotes) {
				if (c.getId() != nId) {
					if (newNote && c.getPosition() >= newPos) {
						position = c.getPosition() + 1;
						c.setPosition(position);
						this.caseManagementMgr.updateNote(c);
					} else if ((!newNote && newPos < note.getPosition()) && c.getPosition() >= newPos && c.getPosition() < note.getPosition()) {
						position = c.getPosition() + 1;
						c.setPosition(position);
						this.caseManagementMgr.updateNote(c);
					} else if ((!newNote && newPos > note.getPosition()) && c.getPosition() <= newPos && c.getPosition() > note.getPosition()) {
						position = c.getPosition() - 1;
						c.setPosition(position);
						this.caseManagementMgr.updateNote(c);
					}

				}
			}

			if (newPos == numNotes && !newNote) {
				--newPos;
			}
			note.setPosition(newPos);
		}

		/*
		 * Remove linked issue(s) and insert message into note
		 * 
		 * if( removeIssue.equals("true") ) { issue_id = request.getParameterValues("issue_id"); issueSet = note.getIssues(); StringBuilder issueNames = new StringBuilder(); for( int idx = 0; idx < issue_id.length; ++idx ) { for(Iterator iter =
		 * issueSet.iterator();iter.hasNext();) { CaseManagementIssue cIssue = (CaseManagementIssue)iter.next(); if( cIssue.getIssue_id() == Long.parseLong(issue_id[idx]) ) { issueSet.remove(cIssue); issueNames.append(cIssue.getIssue().getDescription() +
		 * "\n"); break; } } } //Force hibernate to save rather than update Set tmpIssues = new HashSet(issueSet); note.setIssues(tmpIssues); strNote += "\n" + new SimpleDateFormat("dd-MMM-yyyy").format(new Date()) + " Removed following issue(s):\n" +
		 * issueNames.toString(); note.setNote(strNote); }
		 */

		int revision;

		if (note.getRevision() != null) {
			revision = Integer.parseInt(note.getRevision());
			++revision;
		} else revision = 1;

		note.setRevision(String.valueOf(revision));
		Date now = new Date();
		if (note.getObservation_date() == null) {
			note.setObservation_date(now);
		}

		note.setUpdate_date(now);
		if (note.getCreate_date() == null) note.setCreate_date(now);

		/* save note including add signature */
		String lastSavedNoteString = (String) request.getSession().getAttribute("lastSavedNoteString");
		String roleName = caseManagementMgr.getRoleName(providerNo, note.getProgram_no());
		CaseManagementCPP cpp = this.caseManagementMgr.getCPP(demo);
		if (cpp == null) {
			cpp = new CaseManagementCPP();
			cpp.setDemographic_no(demo);
		}
		cpp = copyNote2cpp(cpp, note);
		String savedStr = caseManagementMgr.saveNote(cpp, note, providerNo, userName, lastSavedNoteString, roleName);
		addNewNoteLink(note.getId());
		logger.debug("Saved note " + savedStr);
		caseManagementMgr.saveCPP(cpp, providerNo);
		/* remember the str written into echart */
		request.getSession().setAttribute("lastSavedNoteString", savedStr);

		/* save extra fields */
		CaseManagementNoteExt cme = new CaseManagementNoteExt();
		cme.setNoteId(note.getId());
		String[] names = { "startdate", "resolutiondate", "proceduredate", "ageatonset", "problemstatus", "treatment", "exposuredetail", "relationship" };
		String[] keys = { cme.STARTDATE, cme.RESOLUTIONDATE, cme.PROCEDUREDATE, cme.AGEATONSET, cme.PROBLEMSTATUS, cme.TREATMENT, cme.EXPOSUREDETAIL, cme.RELATIONSHIP };
		for (int i = 0; i < names.length; i++) {
			String val = request.getParameter(names[i]);
			if (filled(val)) {
				cme.setKeyVal(keys[i]);
				if (i <= 2) {
					cme.setDateValue(val);
				} else {
					if (cme.getDateValue() != null) cme.setDateValue("");
					cme.setValue(val);
				}
				caseManagementMgr.saveNoteExt(cme);
			}
		}

		/* Save annotation */
		String attrib_name = request.getParameter("annotation_attrib");
		HttpSession se = request.getSession();
		if (attrib_name != null) {
			CaseManagementNote cmn = (CaseManagementNote) se.getAttribute(attrib_name);
			if (cmn != null) {
				caseManagementMgr.saveNoteSimple(cmn);
				CaseManagementNoteLink cml = new CaseManagementNoteLink();
				cml.setTableName(cml.CASEMGMTNOTE);
				cml.setTableId(note.getId());
				cml.setNoteId(cmn.getId());
				caseManagementMgr.saveNoteLink(cml);
				LogAction.addLog(providerNo, LogConst.ANNOTATE, LogConst.CON_CME_NOTE, String.valueOf(cmn.getId()), request.getRemoteAddr(), demo, cmn.getNote());
				se.removeAttribute(attrib_name);
			}
		}
		caseManagementMgr.getEditors(note);

		if (newNote) {
			logAction = LogConst.ADD;
		} else if (note.isArchived()) {
			logAction = LogConst.ARCHIVE;
		} else {
			logAction = LogConst.UPDATE;
		}

		LogAction.addLog((String) request.getSession().getAttribute("user"), logAction, LogConst.CON_CME_NOTE, String.valueOf(note.getId()), request.getRemoteAddr(), demo, note.getAuditString());

		ActionForward forward = mapping.findForward("listCPPNotes");
		StringBuilder path = new StringBuilder(forward.getPath());
		path.append("?" + reloadQuery);
		return new ActionForward(path.toString());
	}

	private long noteSave(CaseManagementEntryFormBean cform, HttpServletRequest request) throws Exception {
		WebApplicationContext ctx = getSpringContext();
		ProgramManager programManager = (ProgramManager) ctx.getBean("programManager");
		AdmissionManager admissionManager = (AdmissionManager) ctx.getBean("admissionManager");

		// we don't want to save empty notes!

		String noteTxt = cform.getCaseNote_note();
		noteTxt = org.apache.commons.lang.StringUtils.trimToNull(noteTxt);
		if (noteTxt == null || noteTxt.equals("")) return -1;

		String demo = getDemographicNo(request);

		String sessionFrmName = "caseManagementEntryForm" + demo;
		CaseManagementEntryFormBean sessionFrm = (CaseManagementEntryFormBean) request.getSession().getAttribute(sessionFrmName);

		CaseManagementNote note = sessionFrm.getCaseNote();
		note.setNote(noteTxt);

		String providerNo = getProviderNo(request);
		Provider provider = getProvider(request);
		String userName = provider != null ? provider.getFullName() : "";

		CaseManagementCPP cpp = this.caseManagementMgr.getCPP(demo);
		if (cpp == null) {
			cpp = new CaseManagementCPP();
			cpp.setDemographic_no(demo);
		}

		boolean inCaisi = OscarProperties.getInstance().isCaisiLoaded();

		String lastSavedNoteString = (String) request.getSession().getAttribute("lastSavedNoteString");

		// bug fix - encounter type was not being updated.
		String encounterType = request.getParameter("caseNote.encounter_type");
		if (encounterType != null) {
			note.setEncounter_type(encounterType);
		}

		String sign = (String) request.getParameter("sign");
		if (sign == null) {
			note.setSigning_provider_no("");
			note.setSigned(false);
			sessionFrm.setSign("off");
		} else if (sign.equalsIgnoreCase("persist")) {

			if (note.isSigned()) {
				note.setSigning_provider_no(providerNo);
				note.setSigned(true);
			} else {
				note.setSigning_provider_no("");
				note.setSigned(false);
				sessionFrm.setSign("off");
			}
		} else if (sign.equalsIgnoreCase("on")) {
			note.setSigning_provider_no(providerNo);
			note.setSigned(true);
		} else {
			note.setSigning_provider_no("");
			note.setSigned(false);
			sessionFrm.setSign("off");
		}

		note.setProviderNo(providerNo);
		if (provider != null) note.setProvider(provider);

		String role = null;
		String team = null;

		// if this is an update, don't overwrite the program id
		if (note.getProgram_no() == null || note.getProgram_no().equals("") || !note.getProgram_no().equals("")) {
			String programId = (String) request.getSession().getAttribute("case_program_id");
			note.setProgram_no(programId);
		}

		try {
			role = String.valueOf((programManager.getProgramProvider(note.getProviderNo(), note.getProgram_no())).getRole().getId());
		} catch (Throwable e) {
			// log.error(e);
			role = "0";
		}
		/*
		 * if(request.getSession().getAttribute("archiveView")!="true") note.setReporter_caisi_role(role); else note.setReporter_caisi_role("1");
		 */
		note.setReporter_caisi_role(role);

		try {
			team = String.valueOf((admissionManager.getAdmission(note.getProgram_no(), Integer.valueOf(note.getDemographic_no()))).getTeamId());
		} catch (Throwable e) {
			logger.error(e);
			team = "0";
		}
		note.setReporter_program_team(team);

		/* get the checked issue save into note */
		// this goes into the database casemgmt_issue table
		List<CaseManagementIssue> issuelist = new ArrayList<CaseManagementIssue>();

		CheckBoxBean[] checkedlist = sessionFrm.getIssueCheckList();

		// this gets attached to the CaseManagementNote object
		Set<CaseManagementIssue> issueset = new HashSet<CaseManagementIssue>();
		// wherever this is populated, it's not here...
		Set<CaseManagementNote> noteSet = new HashSet<CaseManagementNote>();
		String ongoing = "";
		Boolean useNewCaseMgmt = new Boolean((String) request.getSession().getAttribute("newCaseManagement"));
		if (useNewCaseMgmt) {
			ongoing = saveCheckedIssues_newCme(request, demo, note, issuelist, checkedlist, issueset, noteSet, ongoing);
		} else {
			ongoing = saveCheckedIssues_oldCme(request, demo, issuelist, checkedlist, issueset, noteSet, ongoing);
		}

		sessionFrm.setIssueCheckList(checkedlist);
		note.setIssues(issueset);

		/* remove signature and the related issues from note */
		String noteString = note.getNote();
		// noteString = removeSignature(noteString);
		noteString = removeCurrentIssue(noteString);
		note.setNote(noteString);

		/* add issues into notes */
		String includeIssue = (String) request.getParameter("includeIssue");
		if (includeIssue == null || !includeIssue.equals("on")) {
			/* set includeissue in note */
			note.setIncludeissue(false);
			sessionFrm.setIncludeIssue("off");
		} else {
			note.setIncludeissue(true);
			/* add the related issues to note */

			String issueString = "";
			issueString = createIssueString(issueset);
			// insert the string before signiture

			int index = noteString.indexOf("\n[[");
			if (index >= 0) {
				String begString = noteString.substring(0, index);
				String endString = noteString.substring(index + 1);
				note.setNote(begString + issueString + endString);
			} else {
				note.setNote(noteString + issueString);
			}
		}

		/* save all issue changes for demographic */
		caseManagementMgr.saveAndUpdateCaseIssues(issuelist);
		if (inCaisi) cpp.setOngoingConcerns(ongoing);

		// update appointment and add verify message to note if verified
		String strBeanName = "casemgmt_oscar_bean" + demo;
		EctSessionBean sessionBean = (EctSessionBean) request.getSession().getAttribute(strBeanName);
		String verify = request.getParameter("verify");
		OscarSuperManager oscarSuperManager = (OscarSuperManager) SpringUtils.getBean("oscarSuperManager");

		Date now = new Date();
		String roleName = caseManagementMgr.getRoleName(providerNo, note.getProgram_no());

		if (verify != null && verify.equalsIgnoreCase("on")) {
			String message = caseManagementMgr.getSignature(providerNo, userName, roleName, request.getLocale(), caseManagementMgr.SIGNATURE_VERIFY);

			String n = note.getNote() + "\n" + message;
			note.setNote(n);

			// only update appt if there is one
			if (sessionBean.appointmentNo != null && !sessionBean.appointmentNo.equals("")) {
				String apptStatus = updateApptStatus(sessionBean.status, "verify");
				oscarSuperManager.update("appointmentDao", "archive_appt", new Object[] { sessionBean.appointmentNo });
				oscarSuperManager.update("appointmentDao", "updatestatusc", new Object[] { apptStatus, providerNo, sessionBean.appointmentNo });
			}

		} else if (note.isSigned()) {
			String message = caseManagementMgr.getSignature(providerNo, userName, roleName, request.getLocale(), caseManagementMgr.SIGNATURE_SIGNED);

			String n = note.getNote() + "\n" + message;
			note.setNote(n);

			// only update appt if there is one
			if (sessionBean.appointmentNo != null && !sessionBean.appointmentNo.equals("")) {
				String apptStatus = updateApptStatus(sessionBean.status, "sign");
				oscarSuperManager.update("appointmentDao", "archive_appt", new Object[] { sessionBean.appointmentNo });
				oscarSuperManager.update("appointmentDao", "updatestatusc", new Object[] { apptStatus, providerNo, sessionBean.appointmentNo });
			}
		}

		// PLACEHOLDER FOR DX CHECK
		/*
		 * If an issue is checked, new , and certain - we want to check dx associations. if found in dx associations. we want to make an entry into dx.
		 */
		if (note.isSigned()) {
			for (CaseManagementIssue cmIssue : issueset) {
				if (cmIssue.isCertain()) {
					DxAssociation assoc = dxDao.findAssociation(cmIssue.getIssue().getType(), cmIssue.getIssue().getCode());
					if (assoc != null) {
						// we found a match. Let's add them to registry
						this.caseManagementMgr.saveToDx(getDemographicNo(request), assoc.getDxCode(), assoc.getDxCodeType(), true);

					}
				}
			}
		}

		/*
		 * if provider is a doctor or nurse,get all major and resolved medical issue for demograhhic and append them to CPP medical history
		 */
		if (inCaisi) {
			/* get access right */
			List accessRight = caseManagementMgr.getAccessRight(providerNo, getDemographicNo(request), (String) request.getSession().getAttribute("case_program_id"));
			setCPPMedicalHistory(cpp, providerNo, accessRight);
			cpp.setUpdate_date(now);
			caseManagementMgr.saveCPP(cpp, providerNo);
		}

		// update password
		String passwd = cform.getCaseNote().getPassword();
		if (passwd != null && passwd.trim().length() > 0) {
			note.setPassword(passwd);
			note.setLocked(true);
		}

		int revision;

		if (note.getRevision() != null) {
			revision = Integer.parseInt(note.getRevision());
			++revision;
		} else revision = 1;

		note.setRevision(String.valueOf(revision));

		String observationDate = cform.getObservation_date();
		ResourceBundle props = ResourceBundle.getBundle("oscarResources", request.getLocale());
		if (observationDate != null && !observationDate.equals("")) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy H:mm", request.getLocale());
			Date dateObserve = formatter.parse(observationDate);
			if (dateObserve.getTime() > now.getTime()) {
				request.setAttribute("DateError", props.getString("oscarEncounter.futureDate.Msg"));
				note.setObservation_date(now);
			} else note.setObservation_date(dateObserve);
		} else if (note.getObservation_date() == null) {
			note.setObservation_date(now);
		}

		note.setUpdate_date(now);
		boolean newNote = false;
		if (note.getCreate_date() == null) {
			note.setCreate_date(now);
			newNote = true;
		}

		/* save note including add signature */
		String savedStr = caseManagementMgr.saveNote(cpp, note, providerNo, userName, lastSavedNoteString, roleName);
		addNewNoteLink(note.getId());
		/* remember the str written into echart */
		request.getSession().setAttribute("lastSavedNoteString", savedStr);
		caseManagementMgr.getEditors(note);
		cform.setCaseNote(note);

		try {
			this.caseManagementMgr.deleteTmpSave(providerNo, note.getDemographic_no(), note.getProgram_no());
		} catch (Throwable e) {
			logger.warn(e);
		}

		String logAction;
		if (newNote) {
			logAction = LogConst.ADD;
		} else {
			logAction = LogConst.UPDATE;
		}
		LogAction.addLog((String) request.getSession().getAttribute("user"), logAction, LogConst.CON_CME_NOTE, "" + Long.valueOf(note.getId()).intValue(), request.getRemoteAddr(), demo, note.getAuditString());

		return note.getId();
	}

	private String updateApptStatus(String status, String type) {
		oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
		as.setApptStatus(status);

		if (type.equalsIgnoreCase("sign")) status = as.signStatus();
		if (type.equalsIgnoreCase("verify")) status = as.verifyStatus();

		return status;
	}

	private String saveCheckedIssues_oldCme(HttpServletRequest request, String demo, List<CaseManagementIssue> issuelist, CheckBoxBean[] checkedlist, Set<CaseManagementIssue> issueset, Set noteSet, String ongoing) {

		int demographicNo = Integer.parseInt(demo);

		for (int i = 0; i < checkedlist.length; i++) {
			CheckBoxBean checkBoxBean = checkedlist[i];
			IssueDisplay issueDisplay = checkBoxBean.getIssueDisplay();

			if (issueDisplay.resolved != null && issueDisplay.resolved.equals("unresolved")) {
				ongoing = ongoing + issueDisplay.getDescription() + "\n";
			}

			boolean isChecked = WebUtils.isChecked(request, "issueCheckList[" + i + "].checked");

			CaseManagementIssue caseManagementIssue = null;
			caseManagementIssue = caseManagementIssueDao.getIssuebyIssueCode(demo, issueDisplay.code);
			if (caseManagementIssue == null && isChecked) {
				Issue issue = issueDao.findIssueByCode(issueDisplay.code);
				if (issue != null) {
					caseManagementIssue = new CaseManagementIssue();
					caseManagementIssue.setDemographic_no(demo);
					caseManagementIssue.setIssue_id(issue.getId());
					caseManagementIssue.setType(issue.getRole());
					caseManagementIssue.setUpdate_date(new Date());

					// Should not save duplicated issue for one demographic
					if (caseManagementIssueDao.getIssuebyId(demo, String.valueOf(issue.getId())) == null) {
						caseManagementIssueDao.saveIssue(caseManagementIssue);
					}
					// reload to materliase generated fields.
					caseManagementIssue = caseManagementIssueDao.getIssuebyId(demo, String.valueOf(issue.getId()));
				}
			}

			if (caseManagementIssue == null) continue;
			else copyIssueDisplayToCaseManagementIssue(caseManagementIssue, issueDisplay);

			if (isChecked) {
				checkBoxBean.setChecked("on");
				checkBoxBean.setUsed(true);
				caseManagementIssue.setNotes(noteSet);

				issueset.add(caseManagementIssue);
			} else {
				checkBoxBean.setChecked("off");
				boolean isLocal = "local".equals(issueDisplay.location);
				if (!isLocal) {
					checkBoxBean.setUsed(false);
				} else {
					checkBoxBean.setUsed(caseManagementNoteDao.haveIssue(issueDisplay.code, demographicNo));
				}
			}

			if (!containsIssue(issuelist, caseManagementIssue)) issuelist.add(caseManagementIssue);
		}
		return ongoing;
	}

	private boolean containsIssue(List<CaseManagementIssue> issuelist, CaseManagementIssue issue) {
		for (CaseManagementIssue tempIssue : issuelist) {
			if (tempIssue.getId() != null && tempIssue.getId().equals(issue.getId())) return (true);
		}

		return (false);
	}

	private void copyIssueDisplayToCaseManagementIssue(CaseManagementIssue caseManagementIssue, IssueDisplay issueDisplay) {
		caseManagementIssue.setAcute("acute".equals(issueDisplay.acute));
		caseManagementIssue.setCertain("certain".equals(issueDisplay.certain));
		caseManagementIssue.setMajor("major".equals(issueDisplay.major));
		caseManagementIssue.setResolved("resolved".equals(issueDisplay.resolved));
	}

	private String saveCheckedIssues_newCme(HttpServletRequest request, String demo, CaseManagementNote note, List issuelist, CheckBoxBean[] checkedlist, Set issueset, Set noteSet, String ongoing) {
		/*
		 * for (int i = 0; i < checkedlist.length; i++) { if (!checkedlist[i].getIssue().isResolved()) ongoing = ongoing + checkedlist[i].getIssue().getIssue().getDescription() + "\n"; String ischecked = request.getParameter("issueCheckList[" + i +
		 * "].checked"); CaseManagementIssue iss = checkedlist[i].getIssue(); if (ischecked != null && ischecked.equalsIgnoreCase("on")) { checkedlist[i].setChecked("on"); checkedlist[i].setUsed(true); iss.setNotes(noteSet);
		 * issueset.add(checkedlist[i].getIssue()); } else { checkedlist[i].setChecked("off"); checkedlist[i].setUsed(caseManagementMgr.haveIssue(iss.getId(), note.getId(), demo)); checkedlist[i].setUsed(false); }
		 * 
		 * issuelist.add(checkedlist[i].getIssue()); } return ongoing;
		 */

		int demographicNo = Integer.parseInt(demo);

		for (int i = 0; i < checkedlist.length; i++) {
			CheckBoxBean checkBoxBean = checkedlist[i];
			IssueDisplay issueDisplay = checkBoxBean.getIssueDisplay();

			if (issueDisplay.resolved != null && issueDisplay.resolved.equals("unresolved")) {
				ongoing = ongoing + issueDisplay.getDescription() + "\n";
			}

			boolean isChecked = WebUtils.isChecked(request, "issueCheckList[" + i + "].checked");

			CaseManagementIssue caseManagementIssue = null;
			caseManagementIssue = caseManagementIssueDao.getIssuebyIssueCode(demo, issueDisplay.code);
			if (caseManagementIssue == null && isChecked) {
				Issue issue = issueDao.findIssueByCode(issueDisplay.code);
				if (issue != null) {
					caseManagementIssue = new CaseManagementIssue();
					caseManagementIssue.setDemographic_no(demo);
					caseManagementIssue.setIssue_id(issue.getId());
					caseManagementIssue.setType(issue.getRole());
					caseManagementIssue.setUpdate_date(new Date());

					// Should not save duplicated issue for one demographic
					if (caseManagementIssueDao.getIssuebyId(demo, String.valueOf(issue.getId())) == null) {
						caseManagementIssueDao.saveIssue(caseManagementIssue);
					}
					// reload to materliase generated fields.
					caseManagementIssue = caseManagementIssueDao.getIssuebyId(demo, String.valueOf(issue.getId()));
				}
			}

			if (caseManagementIssue == null) continue;
			else copyIssueDisplayToCaseManagementIssue(caseManagementIssue, issueDisplay);

			if (isChecked) {
				checkBoxBean.setChecked("on");
				checkBoxBean.setUsed(true);
				caseManagementIssue.setNotes(noteSet);

				issueset.add(caseManagementIssue);
			} else {
				checkBoxBean.setChecked("off");
				boolean isLocal = "local".equals(issueDisplay.location);
				if (!isLocal) {
					checkBoxBean.setUsed(false);
				} else {
					checkBoxBean.setUsed(caseManagementNoteDao.haveIssue(issueDisplay.code, demographicNo));
				}
			}

			if (!containsIssue(issuelist, caseManagementIssue)) issuelist.add(caseManagementIssue);
		}
		return ongoing;

	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("userrole") == null) return mapping.findForward("expired");

		// String providerNo = getProviderNo(request);
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		request.setAttribute("change_flag", "false");

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));

		request.setAttribute("from", request.getParameter("from"));
		long noteId = noteSave(cform, request);
		/*
		 * CaseManagementNote preNote=new CaseManagementNote(); if(cform.getNoteId()!=null) { Long nId=Long.parseLong(cform.getNoteId()); preNote.setId(nId); }
		 */

		/* prepare the message */
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("note.saved"));
		saveMessages(request, messages);

		// are we in the new encounter and chaining actions?
		String chain = request.getParameter("chain");
		if (chain != null && !chain.equals("")) {
			String varName = "newNote";
			request.getSession().setAttribute(varName, false);
			varName = "saveNote" + demono;
			request.getSession().setAttribute(varName, new Boolean(true)); // tell CaseManagementView we have just saved note
			ActionForward fwd = mapping.findForward(chain);
			StringBuilder path = new StringBuilder(fwd.getPath());
			ResourceBundle props = ResourceBundle.getBundle("oscarResources");
			if (request.getAttribute("DateError") != null) {
				if (path.indexOf("?") == -1) path.append("?");
				else path.append("&");

				path.append("DateError=" + props.getString("oscarEncounter.futureDate.Msg"));
			}

			ActionForward forward = new ActionForward();
			forward.setPath(path.toString());
			return forward;
		}

		// this.caseManagementMgr.saveNote();
		return mapping.findForward("view");
	}

	public ActionForward ajaxsave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getSession().getAttribute("userrole") == null) return mapping.findForward("expired");

		String noteTxt = request.getParameter("noteTxt");
		noteTxt = org.apache.commons.lang.StringUtils.trimToNull(noteTxt);
		if (noteTxt == null || noteTxt.equals("")) return null;

		logger.debug("Saving Note" + request.getParameter("nId"));
		logger.debug("Text -- " + noteTxt);
		String demo = getDemographicNo(request);
		String providerNo = getProviderNo(request);
		Provider provider = getProvider(request);

		CaseManagementNote note;
		String history;
		String noteId = request.getParameter("nId");
		boolean newNote;
		Date now = new Date();
		if (noteId.substring(0, 1).equals("0")) {
			note = new CaseManagementNote();
			note.setDemographic_no(demo);
			history = "";
			newNote = true;
		} else {
			note = this.caseManagementMgr.getNote(request.getParameter("nId"));
			history = note.getHistory();
			history = "---------History Record---------" + history;
			newNote = false;
		}

		String observationDate = request.getParameter("obsDate");
		ResourceBundle props = ResourceBundle.getBundle("oscarResources");
		if (observationDate != null && !observationDate.equals("")) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy H:mm", request.getLocale());
			Date dateObserve = formatter.parse(observationDate);
			if (dateObserve.getTime() > now.getTime()) {
				request.setAttribute("DateError", props.getString("oscarEncounter.futureDate.Msg"));
				note.setObservation_date(now);
			} else note.setObservation_date(dateObserve);
		} else if (note.getObservation_date() == null) {
			note.setObservation_date(now);
		}

		history = noteTxt + "[[" + now + "]]" + history;
		note.setNote(noteTxt);
		note.setHistory(history);

		if (note.isSigned()) {
			note.setSigning_provider_no(providerNo);
			note.setSigned(true);
		} else {
			note.setSigning_provider_no("");
			note.setSigned(false);
		}

		note.setProviderNo(providerNo);
		if (provider != null) note.setProvider(provider);

		String programId = (String) request.getSession().getAttribute("case_program_id");
		note.setProgram_no(programId);

		WebApplicationContext ctx = this.getSpringContext();
		ProgramManager programManager = (ProgramManager) ctx.getBean("programManager");
		AdmissionManager admissionManager = (AdmissionManager) ctx.getBean("admissionManager");

		String role = null;
		try {
			role = String.valueOf((programManager.getProgramProvider(note.getProviderNo(), note.getProgram_no())).getRole().getId());
		} catch (Throwable e) {
			logger.error(e);
			role = "0";
		}

		note.setReporter_caisi_role(role);

		String team = null;
		try {
			team = String.valueOf((admissionManager.getAdmission(note.getProgram_no(), Integer.valueOf(note.getDemographic_no()))).getTeamId());
		} catch (Throwable e) {
			logger.error(e);
			team = "0";
		}

		note.setReporter_program_team(team);

		String sessionName = "caseManagementEntryForm" + demo;
		CaseManagementEntryFormBean sessionFrm = (CaseManagementEntryFormBean) request.getSession().getAttribute(sessionName);
		List issuelist = new ArrayList();
		Set issueset = new HashSet();
		Set noteSet = new HashSet();

		int numIssues = Integer.parseInt(request.getParameter("numIssues"));
		// CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;

		CheckBoxBean[] checkedlist = (CheckBoxBean[]) sessionFrm.getIssueCheckList();
		for (int i = 0; i < numIssues; i++) {
			String ischecked = request.getParameter("issue" + i);
			if (ischecked != null && ischecked.equalsIgnoreCase("on")) {
				checkedlist[i].setChecked("on");
				CaseManagementIssue iss = checkedlist[i].getIssue();
				iss.setNotes(noteSet);
				issueset.add(checkedlist[i].getIssue());
			} else {
				checkedlist[i].setChecked("off");
			}
			issuelist.add(checkedlist[i].getIssue());
		}

		note.setIssues(issueset);
		note.setIncludeissue(false);
		caseManagementMgr.saveAndUpdateCaseIssues(issuelist);
		sessionFrm.setIssueCheckList(checkedlist);

		int revision;

		if (note.getRevision() != null) {
			revision = Integer.parseInt(note.getRevision());
			++revision;
		} else revision = 1;

		note.setRevision(String.valueOf(revision));

		note.setUpdate_date(now);
		if (note.getCreate_date() == null) note.setCreate_date(now);

		note.setEncounter_type(request.getParameter("encType"));

		// check if previous note is doc note.

		Long prevNoteId = note.getId();

		this.caseManagementMgr.saveNoteSimple(note);
		this.caseManagementMgr.getEditors(note);

		if (prevNoteId != null) {
			addNewNoteLink(prevNoteId);
		}

		try {
			this.caseManagementMgr.deleteTmpSave(providerNo, note.getDemographic_no(), note.getProgram_no());
		} catch (Throwable e) {
			logger.warn(e);
		}

		request.getSession().setAttribute(sessionName, sessionFrm);
		CaseManagementEntryFormBean newform = new CaseManagementEntryFormBean();
		newform.setCaseNote(note);
		newform.setIssueCheckList(checkedlist);
		request.setAttribute("caseManagementEntryForm", newform);
		String varName = "newNote";
		request.getSession().setAttribute(varName, false);
		request.setAttribute("ajaxsave", note.getId());
		request.setAttribute("origNoteId", noteId);

		String logAction;
		if (newNote) {
			logAction = LogConst.ADD;
		} else {
			logAction = LogConst.UPDATE;
		}

		LogAction.addLog((String) request.getSession().getAttribute("user"), logAction, LogConst.CON_CME_NOTE, String.valueOf(note.getId()), request.getRemoteAddr(), demo, note.getAuditString());

		return mapping.findForward("issueList_ajax");
		/*
		 * CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		 * 
		 * String oldId = cform.getCaseNote().getId() == null ? request.getParameter("newNoteIdx") : String.valueOf(cform.getCaseNote().getId()); long newId = noteSave(cform, request);
		 * 
		 * if( newId > -1 ) { log.debug("OLD ID " + oldId + " NEW ID " + String.valueOf(newId)); cform.setMethod("view"); request.getSession().setAttribute("newNote", false); request.getSession().setAttribute("caseManagementEntryForm", cform);
		 * request.setAttribute("caseManagementEntryForm", cform); request.setAttribute("ajaxsave", newId); request.setAttribute("origNoteId", oldId); return mapping.findForward("issueList_ajax"); }
		 * 
		 * return null;
		 */
	}

	// add new note link if note is document or rx note
	private void addNewNoteLink(Long noteId) {
		CaseManagementNote cmn = caseManagementMgr.getNote(noteId.toString());
		List<CaseManagementNote> cmnList = caseManagementMgr.getNotesByUUID(cmn.getUuid());
		Long firstNoteId;
		Long lastNoteId;
		List<Long> noteIdList = new ArrayList();
		for (CaseManagementNote note : cmnList) {
			noteIdList.add(note.getId());
		}
		if (noteIdList.size() > 0) {
			Collections.sort(noteIdList);
			firstNoteId = noteIdList.get(0);
			lastNoteId = noteIdList.get(noteIdList.size() - 1);
			if (firstNoteId != lastNoteId) {
				CaseManagementNote firstNote = caseManagementMgr.getNote(firstNoteId.toString());
				if (firstNote.isDocumentNote()) {
					Long tableId = EDocUtil.getTableIdFromNoteId(firstNote.getId());
					CaseManagementNoteLink cmnl = new CaseManagementNoteLink();
					cmnl.setNoteId(lastNoteId);
					cmnl.setTableName(cmnl.DOCUMENT);
					cmnl.setTableId(tableId);
					caseManagementMgr.saveNoteLink(cmnl);
				} else if (firstNote.isRxAnnotation()) {

					CaseManagementNoteLink latestLink = caseManagementMgr.getLatestLinkByNote(firstNote.getId());

					CaseManagementNoteLink cmnl = new CaseManagementNoteLink();
					cmnl.setNoteId(lastNoteId);
					cmnl.setTableName(cmnl.DRUGS);
					cmnl.setTableId(latestLink.getTableId());
					caseManagementMgr.saveNoteLink(cmnl);
					// EDocUtil.addCaseMgmtNoteLink(cmnl);
				}
			}
		}
	}

	public ActionForward saveAndExit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("saveandexit");
		String providerNo = getProviderNo(request);
		String demoNo = getDemographicNo(request);
		if (request.getSession().getAttribute("userrole") == null) return mapping.findForward("expired");

		request.setAttribute("change_flag", "false");

		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("note.saved"));
		saveMessages(request, messages);

		long noteId = noteSave(cform, request);
		if (noteId == -1) {
			return mapping.findForward("windowClose");
		}

		cform.setMethod("view");
		String error = (String) request.getAttribute("DateError");
		if (error != null) {

			String varName = "newNote";
			request.getSession().setAttribute(varName, false);
			varName = "saveNote" + demoNo;
			request.getSession().setAttribute(varName, new Boolean(true)); // tell CaseManagementView we have just saved note
			ActionForward fwd = mapping.findForward("list");
			StringBuilder path = new StringBuilder(fwd.getPath());

			if (path.indexOf("?") == -1) path.append("?");
			else path.append("&");

			path.append("DateError=" + error);

			ActionForward forward = new ActionForward();
			forward.setPath(path.toString());
			return forward;
		}

		String toBill = request.getParameter("toBill");
		if (toBill != null && toBill.equalsIgnoreCase("true")) {
			String region = cform.getBillRegion();
			String appointmentNo = cform.getAppointmentNo();
			String name = this.getDemoName(demoNo);
			String date = cform.getAppointmentDate();
			String start_time = cform.getStart_time();
			String apptProvider = cform.getApptProvider();
			String providerview = cform.getProviderview();
			String defaultView = oscar.OscarProperties.getInstance().getProperty("default_view", "");

			Set setIssues = cform.getCaseNote().getIssues();
			Iterator iter = setIssues.iterator();
			StringBuilder dxCodes = new StringBuilder();
			String strDxCode;
			int dxNum = 0;
			while (iter.hasNext()) {
				CaseManagementIssue cIssue = (CaseManagementIssue) iter.next();
				dxCodes.append("&dxCode");
				strDxCode = String.valueOf(cIssue.getIssue().getCode());
				if (strDxCode.length() > 3) {
					strDxCode = strDxCode.substring(0, 3);
				}

				if (dxNum > 0) {
					dxCodes.append(String.valueOf(dxNum));
				}

				dxCodes.append("=" + strDxCode);
				++dxNum;
			}

			String url = "/billing.do?billRegion=" + region + "&billForm=" + defaultView + "&hotclick=&appointment_no=" + appointmentNo + "&demographic_name=" + java.net.URLEncoder.encode(name, "utf-8") + "&amp;status=t&demographic_no=" + demoNo + "&providerview=" + providerview + "&user_no=" + providerNo + "&apptProvider_no=" + apptProvider + "&appointment_date=" + date + "&start_time=" + start_time + "&bNewForm=1" + dxCodes.toString();
			logger.debug("BILLING URL " + url);
			ActionForward forward = new ActionForward();
			forward.setPath(url);
			return forward;
		}

		String chain = request.getParameter("chain");

		SurveillanceMaster sMaster = SurveillanceMaster.getInstance();
		if (!sMaster.surveysEmpty()) {
			request.setAttribute("demoNo", demoNo);
			if (chain != null && !chain.equals("")) {
				request.setAttribute("proceedURL", chain);
			}
			logger.debug("sending to surveillance");
			return mapping.findForward("surveillance");
		}

		if (chain != null && !chain.equals("")) {
			ActionForward fwd = new ActionForward();
			fwd.setPath(chain);
			return fwd;
		}

		return mapping.findForward("windowClose");
	}

	public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getSession().getAttribute("userrole") == null) return mapping.findForward("expired");

		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		String providerNo = getProviderNo(request);

		String programNo = (String) request.getSession().getAttribute("case_program_id");

		String demo = cform.getDemographicNo();

		try {
			logger.debug("CANCEL P:" + providerNo + " D:" + demo + " PROG:" + programNo);
			this.caseManagementMgr.deleteTmpSave(providerNo, demo, programNo);
		} catch (Throwable e) {
			logger.warn(e);
		}

		return mapping.findForward("windowClose");
	}

	public ActionForward exit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("change_flag", "false");
		return mapping.findForward("list");
	}

	public ActionForward addNewIssue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("addNewIssue");

		if (request.getSession().getAttribute("userrole") == null) return mapping.findForward("expired");

		request.setAttribute("change_flag", "true");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		String demono = getDemographicNo(request);
		String sessionFrmName = "caseManagementEntryForm" + demono;
		CaseManagementEntryFormBean sessionFrm = (CaseManagementEntryFormBean) request.getSession().getAttribute(sessionFrmName);
		CaseManagementNote note = sessionFrm.getCaseNote();
		String noteTxt = cform.getCaseNote_note();
		noteTxt = org.apache.commons.lang.StringUtils.trimToNull(noteTxt);
		note.setNote(noteTxt);

		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));

		request.setAttribute("from", request.getParameter("from"));

		// noteSave(cform, request);
		cform.setShowList("false");
		cform.setSearString("");
		return mapping.findForward("IssueSearch");
	}

	public ActionForward issueList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getSession().getAttribute("userrole") == null) {
			response.sendError(response.SC_FORBIDDEN);
			return null;
		}

		String programId = (String) request.getSession().getAttribute("case_program_id");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;

		String demono = request.getParameter("amp;demographicNo");

		// get current providerNo
		// String providerNo = request.getParameter("amp;providerNo");
		String providerNo = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();

		// get the issue list have search string
		String search = request.getParameter("issueSearch");

		List searchResults;
		searchResults = caseManagementMgr.searchIssues(providerNo, programId, search);

		// remove issues which we already have - we don't want duplicates unless asked for
		List existingIssues = caseManagementMgr.filterIssues(caseManagementMgr.getIssues(Integer.parseInt(demono)), programId);
		List filteredSearchResults;

		if (request.getParameter("amp;all") != null) {
			filteredSearchResults = new ArrayList(searchResults);
		} else {
			filteredSearchResults = new ArrayList();
			Map existingIssuesMap = convertIssueListToMap(existingIssues);
			for (Iterator iter = searchResults.iterator(); iter.hasNext();) {
				Issue issue = (Issue) iter.next();
				if (existingIssuesMap.get(issue.getId()) == null) {
					filteredSearchResults.add(issue);
				}
			}

		}

		CheckIssueBoxBean[] issueList = new CheckIssueBoxBean[filteredSearchResults.size()];
		for (int i = 0; i < filteredSearchResults.size(); i++) {
			issueList[i] = new CheckIssueBoxBean();
			issueList[i].setIssue((Issue) filteredSearchResults.get(i));

		}
		cform.setNewIssueCheckList(issueList);

		request.setAttribute("issueList", filteredSearchResults);

		return mapping.findForward("issueAutoCompletion");
	}

	public ActionForward issueSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("issueSearch");
		String programId = (String) request.getSession().getAttribute("case_program_id");

		request.setAttribute("change_flag", "true");
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
		String search = cform.getSearString();

		List searchResults;
		searchResults = caseManagementMgr.searchIssues(providerNo, programId, search);

		List filteredSearchResults = new ArrayList();

		// remove issues which we already have - we don't want duplicates
		List existingIssues = caseManagementMgr.filterIssues(caseManagementMgr.getIssues(Integer.parseInt(demono)), programId);
		Map existingIssuesMap = convertIssueListToMap(existingIssues);
		for (Iterator iter = searchResults.iterator(); iter.hasNext();) {
			Issue issue = (Issue) iter.next();
			if (existingIssuesMap.get(issue.getId()) == null) {
				filteredSearchResults.add(issue);
			}
		}

		CheckIssueBoxBean[] issueList = new CheckIssueBoxBean[filteredSearchResults.size()];
		for (int i = 0; i < filteredSearchResults.size(); i++) {
			Issue issue = (Issue) filteredSearchResults.get(i);
			issueList[i] = new CheckIssueBoxBean();
			issueList[i].setIssue(issue);
		}
		logger.debug("Community issue reconciliation complete");
		String sessionFrmName = "caseManagementEntryForm" + demono;
		CaseManagementEntryFormBean sessionFrm = (CaseManagementEntryFormBean) request.getSession().getAttribute(sessionFrmName);
		sessionFrm.setNewIssueCheckList(issueList);
		sessionFrm.setShowList("true");

		if (request.getParameter("change_diagnosis") != null) request.setAttribute("change_diagnosis", request.getParameter("change_diagnosis"));
		if (request.getParameter("change_diagnosis_id") != null) request.setAttribute("change_diagnosis_id", request.getParameter("change_diagnosis_id"));

		return mapping.findForward("IssueSearch");
	}

	// we need to convert single issue into checkbox array so we can play nicely with CaseManagementEntryFormBean
	public ActionForward makeIssue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// String programId = (String) request.getSession().getAttribute("case_program_id");
		// grab the issue we want to add
		String issueId = request.getParameter("newIssueId");
		// String providerNo = getProviderNo(request);

		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		String sessionFrmName = "caseManagementEntryForm" + this.getDemographicNo(request);
		CaseManagementEntryFormBean sessionFrm = (CaseManagementEntryFormBean) request.getSession().getAttribute(sessionFrmName);

		// check to see if this issue has already been associated with this demographic
		boolean issueExists = false;
		long lIssueId = Long.parseLong(issueId);
		CheckBoxBean[] existingCaseIssueList = sessionFrm.getIssueCheckList();
		for (int idx = 0; idx < existingCaseIssueList.length; ++idx) {
			if (existingCaseIssueList[idx].getIssue().getIssue_id() == lIssueId) {
				issueExists = true;
				break;
			}
		}

		// if issue hasn't been added, add it
		// if it has do nothing;
		if (!issueExists) {
			CheckIssueBoxBean[] caseIssueList = new CheckIssueBoxBean[1];

			caseIssueList[0] = new CheckIssueBoxBean();
			Issue issue = caseManagementMgr.getIssue(issueId);
			caseIssueList[0].setIssue(issue);
			caseIssueList[0].setChecked(true);
			sessionFrm.setNewIssueCheckList(caseIssueList);

			return issueAdd(mapping, cform, request, response);
		} else return null;
	}

	public ActionForward issueAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getSession().getAttribute("userrole") == null) return mapping.findForward("expired");

		String changeDiagnosis = request.getParameter("change_diagnosis");
		if (changeDiagnosis != null && changeDiagnosis.equalsIgnoreCase("true")) {
			return submitChangeDiagnosis(mapping, form, request, response);
		}
		logger.debug("issueAdd");
		request.setAttribute("change_flag", "true");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		request.setAttribute("from", request.getParameter("from"));

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));

		String sessionFrmName = "caseManagementEntryForm" + demono;
		CaseManagementEntryFormBean sessionFrm = (CaseManagementEntryFormBean) request.getSession().getAttribute(sessionFrmName);

		// add checked new issues to client's issue list
		// client's old issues
		CheckBoxBean[] oldList = (CheckBoxBean[]) sessionFrm.getIssueCheckList();
		// client's new issues
		CheckIssueBoxBean[] issueList = (CheckIssueBoxBean[]) sessionFrm.getNewIssueCheckList();
		int k = 0;
		if (issueList != null) {
			for (int i = 0; i < issueList.length; i++) {

				// duplicated issues should be discarded.
				if (caseManagementIssueDao.getIssuebyId(demono, String.valueOf(issueList[i].getIssue().getId())) != null) {
					continue;
				}

				if (issueList[i].isChecked()) k++;
			}
		}

		CheckBoxBean[] caseIssueList = new CheckBoxBean[oldList.length + k];
		for (int i = 0; i < oldList.length; i++) {
			caseIssueList[i] = new CheckBoxBean();
			caseIssueList[i].setChecked(oldList[i].getChecked());
			caseIssueList[i].setUsed(oldList[i].isUsed());
			caseIssueList[i].setIssue(oldList[i].getIssue());
			caseIssueList[i].setIssueDisplay(oldList[i].getIssueDisplay());
		}
		k = 0;

		String programIdStr = (String) request.getSession().getAttribute(SessionConstants.CURRENT_PROGRAM_ID);
		Integer programId = null;
		if (programIdStr != null) programId = Integer.valueOf(programIdStr);

		Properties dxProps = new Properties();
		try {
			InputStream is = getClass().getResourceAsStream("/caisi_issues_dx.properties");
			dxProps.load(is);
		} catch (IOException e) {
			logger.warn("Unable to load Dx properties file");
		}

		if (issueList != null) {
			CaseManagementViewAction caseManagementViewAction = new CaseManagementViewAction();

			for (int i = 0; i < issueList.length; i++) {
				if (issueList[i].isChecked()) {
					if (caseManagementIssueDao.getIssuebyId(demono, String.valueOf(issueList[i].getIssue().getId())) != null) {
						continue;
					}
					caseIssueList[oldList.length + k] = new CheckBoxBean();
					CaseManagementIssue cmi = newIssueToCIssue(sessionFrm, issueList[i].getIssue(), programId);
					caseIssueList[oldList.length + k].setIssue(cmi);
					caseIssueList[oldList.length + k].setChecked("on");
					IssueDisplay issueDisplay = caseManagementViewAction.getIssueDisplay(programId, cmi);
					caseIssueList[oldList.length + k].setIssueDisplay(issueDisplay);

					// should issue be automagically added to Dx? check config file
					if (dxProps != null && dxProps.get(issueList[i].getIssue().getCode()) != null) {
						String codingSystem = dxProps.getProperty("coding_system");
						if (caseIssueList[oldList.length + k].getIssue().isCertain()) {
							logger.info("adding to Dx");
							this.caseManagementMgr.saveToDx(getDemographicNo(request), issueList[i].getIssue().getCode(), codingSystem, false);
							caseIssueList[oldList.length + k].getIssue().setMajor(true);
						}
					}

					k++;

				}
			}
		}
		cform.setIssueCheckList(caseIssueList);
		sessionFrm.setIssueCheckList(caseIssueList);

		String ajax = request.getParameter("ajax");
		if (ajax != null && ajax.equalsIgnoreCase("true")) {
			request.setAttribute("caseManagementEntryForm", sessionFrm);
			return mapping.findForward("issueList_ajax");
		} else return mapping.findForward("view");
	}

	public ActionForward changeDiagnosis(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("changeDiagnosis");
		if (request.getSession().getAttribute("userrole") == null) return mapping.findForward("expired");

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
		logger.debug("submitChangeDiagnosis");
		if (request.getSession().getAttribute("userrole") == null) return mapping.findForward("expired");

		request.setAttribute("change_flag", "true");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		request.setAttribute("from", request.getParameter("from"));

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));

		// get issue we're changing
		String strIndex = request.getParameter("change_diagnosis_id");
		int index = Integer.parseInt(strIndex);

		// change issue
		CheckBoxBean[] oldList = cform.getIssueCheckList();

		CheckIssueBoxBean[] issueList = cform.getNewIssueCheckList();
		CheckIssueBoxBean substitution = null;
		String origIssueDesc = null;
		String newIssueDesc = null;
		// find the checked issue
		for (CheckIssueBoxBean curr : issueList) {
			if (curr.isChecked()) {
				substitution = curr;
				break;
			}
		}

		if (substitution != null) {
			for (int x = 0; x < oldList.length; x++) {
				if (x == index) {

					Issue oldIssue = oldList[x].getIssue().getIssue();
					origIssueDesc = oldIssue.getDescription();

					Issue newIssue = caseManagementMgr.getIssue(String.valueOf(substitution.getIssue().getId().longValue()));
					newIssueDesc = newIssue.getDescription();

					oldList[x].getIssue().setIssue(newIssue);
					oldList[x].getIssue().setIssue_id(substitution.getIssue().getId().longValue());
					oldList[x].getIssue().setType(newIssue.getType());
					oldList[x].getIssueDisplay().setCode(newIssue.getCode());
					oldList[x].getIssueDisplay().setCodeType(newIssue.getType());
					oldList[x].getIssueDisplay().setDescription(newIssue.getDescription());

					caseManagementMgr.saveCaseIssue(oldList[x].getIssue());
				}
			}
		}

		cform.setIssueCheckList(oldList);
		if (substitution != null && origIssueDesc != null) this.caseManagementMgr.changeIssueInCPP(demono, origIssueDesc, newIssueDesc);
		// updateIssueToConcern(cform);

		return mapping.findForward("view");
	}

	public ActionForward ajaxChangeDiagnosis(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("ajaxChangeDiagnosis");
		if (request.getSession().getAttribute("userrole") == null) return mapping.findForward("expired");

		// get issue we're changing
		String strIndex = request.getParameter("change_diagnosis_id");
		int idx = Integer.parseInt(strIndex);

		String substitution = request.getParameter("newIssueId");
		String sessionFrmName = "caseManagementEntryForm" + getDemographicNo(request);
		CaseManagementEntryFormBean sessionFrm = (CaseManagementEntryFormBean) request.getSession().getAttribute(sessionFrmName);

		CheckBoxBean[] curIssues = sessionFrm.getIssueCheckList();

		if (substitution != null) {

			Issue iss = caseManagementMgr.getIssue(substitution);
			curIssues[idx].getIssue().setIssue(iss);
			curIssues[idx].getIssue().setIssue_id(iss.getId());
			this.caseManagementMgr.saveCaseIssue(curIssues[idx].getIssue());

			// update form with new issue list
			Set issueset = new HashSet();
			for (int i = 0; i < curIssues.length; ++i) {
				if (curIssues[i].getChecked().equalsIgnoreCase("on")) issueset.add(curIssues[i].getIssue());
			}

			sessionFrm.getCaseNote().setIssues(issueset);
		}

		sessionFrm.setIssueCheckList(curIssues);
		request.setAttribute("caseManagementEntryForm", sessionFrm);
		// updateIssueToConcern(cform);

		// request.setAttribute("issueCheckList", curIssues);
		return mapping.findForward("issueList_ajax");
	}

	public ActionForward issueDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("issueDelete");
		if (request.getSession().getAttribute("userrole") == null) {
			response.sendError(response.SC_FORBIDDEN);
			return null;
		}

		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		String demono = getDemographicNo(request);
		String sessionFrmName = "caseManagementEntryForm" + demono;
		CaseManagementEntryFormBean sessionFrm = (CaseManagementEntryFormBean) request.getSession().getAttribute(sessionFrmName);

		// noteSave(cform, request);
		request.setAttribute("change_flag", "true");
		request.setAttribute("from", request.getParameter("from"));

		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));

		CheckBoxBean[] oldList = (CheckBoxBean[]) sessionFrm.getIssueCheckList();

		String inds = (String) cform.getDeleteId();
		Integer ind = new Integer(inds);

		// delete the right issue
		CheckBoxBean[] caseIssueList = new CheckBoxBean[oldList.length - 1];
		int k = 0;
		CaseManagementIssue iss = null;

		if (ind.intValue() >= oldList.length) {
			logger.error("issueDelete index error");
			return mapping.findForward("view");
		}
		for (int i = 0; i < oldList.length; i++) {

			if (i != ind.intValue()) {
				caseIssueList[k] = new CheckBoxBean();
				caseIssueList[k].setChecked(oldList[i].getChecked());
				caseIssueList[k].setUsed(oldList[i].isUsed());
				caseIssueList[k].setIssue(oldList[i].getIssue());
				caseIssueList[k].setIssueDisplay(oldList[i].getIssueDisplay());
				k++;
			}
			if (i == ind.intValue()) {
				// delete from caseissue table
				iss = oldList[i].getIssue();
				caseManagementMgr.deleteIssueById(iss);
			}
		}
		cform.setIssueCheckList(caseIssueList);
		sessionFrm.setIssueCheckList(caseIssueList);

		if (OscarProperties.getInstance().isCaisiLoaded() && iss != null) {
			// reset current concern in CPP
			// updateIssueToConcern(cform);
			caseManagementMgr.removeIssueFromCPP(demono, iss);
		}

		// added by Eugene Petrhin in order to guarantee the DB consistency if user hits Cancel afterwards
		if (sessionFrm.getCaseNote().getId() != null) {
			noteSave(cform, request);
		}

		String ajax = request.getParameter("ajax");
		if (ajax != null && ajax.equalsIgnoreCase("true")) {
			request.setAttribute("caseManagementEntryForm", sessionFrm);
			return mapping.findForward("issueList_ajax");
		} else return mapping.findForward("view");
	}

	public ActionForward issueChange(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("issueChange");
		if (request.getSession().getAttribute("userrole") == null) {
			response.sendError(response.SC_FORBIDDEN);
			return null;
		}
		request.setAttribute("from", request.getParameter("from"));
		request.setAttribute("change_flag", "true");
		request.getSession().setAttribute("issueStatusChanged", "true");
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		String demono = getDemographicNo(request);
		String sessionFrmName = "caseManagementEntryForm" + demono;
		CaseManagementEntryFormBean sessionFrm = (CaseManagementEntryFormBean) request.getSession().getAttribute(sessionFrmName);

		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));

		// noteSave(cform, request);
		CheckBoxBean[] oldList = (CheckBoxBean[]) sessionFrm.getIssueCheckList();

		String inds = (String) cform.getLineId();

		Integer ind = new Integer(inds);
		List iss = new ArrayList();
		oldList[ind.intValue()].getIssue().setUpdate_date(new Date());
		iss.add(oldList[ind.intValue()].getIssue());
		caseManagementMgr.saveAndUpdateCaseIssues(iss);

		if (OscarProperties.getInstance().isCaisiLoaded()) {
			String providerNo = this.getProviderNo(request);

			// get access right
			List accessRight = caseManagementMgr.getAccessRight(providerNo, demono, (String) request.getSession().getAttribute("case_program_id"));

			// add medical history to CPP
			CaseManagementCPP cpp = this.caseManagementMgr.getCPP(getDemographicNo(request));
			setCPPMedicalHistory(cpp, providerNo, accessRight);
			cpp.setUpdate_date(new Date());
			caseManagementMgr.saveCPP(cpp, providerNo);
		}

		String ajax = request.getParameter("ajax");
		if (ajax != null && ajax.equalsIgnoreCase("true")) {
			request.setAttribute("caseManagementEntryForm", sessionFrm);
			return mapping.findForward("issueList_ajax");
		} else return mapping.findForward("view");
	}

	public ActionForward notehistory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getSession().getAttribute("userrole") == null) return mapping.findForward("expired");

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));

		String noteid = (String) request.getParameter("noteId");

		List<CaseManagementNote> history = caseManagementMgr.getHistory(noteid);
		request.setAttribute("history", history);
		ResourceBundle props = ResourceBundle.getBundle("oscarResources");
		request.setAttribute("title", props.getString("oscarEncounter.noteHistory.title"));
		return mapping.findForward("showHistory");
	}

	public ActionForward issuehistory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getSession().getAttribute("userrole") == null) return mapping.findForward("expired");

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		String issueIds = request.getParameter("issueIds");

		List<CaseManagementNote> history = new ArrayList<CaseManagementNote>();
		List<CaseManagementNote> temp = caseManagementMgr.getIssueHistory(issueIds, demono);

		ArrayList<Boolean> current = new ArrayList<Boolean>(history.size());
		for (Iterator<CaseManagementNote> iter = temp.listIterator(); iter.hasNext();) {
			CaseManagementNote historyNote = iter.next();
			CaseManagementNote recentNote = caseManagementMgr.getMostRecentNote(historyNote.getUuid());

			if (!recentNote.isLocked()) {
				history.add(historyNote);
				if (recentNote.getUpdate_date().compareTo(historyNote.getUpdate_date()) > 0) {
					current.add(new Boolean(false));
				} else current.add(new Boolean(true));
			}
		}

		request.setAttribute("history", history);
		request.setAttribute("current", current);

		StringBuilder title = new StringBuilder();
		String arrIssues[] = issueIds.split(",");
		ResourceBundle props = ResourceBundle.getBundle("oscarResources");
		if (arrIssues != null) {
			for (int idx = 0; idx < arrIssues.length; ++idx) {
				String tempArrIssue=StringUtils.trimToNull(arrIssues[idx]);
				if (tempArrIssue==null) continue;
				Issue i = this.caseManagementMgr.getIssue(tempArrIssue);
				title.append(i.getDescription());
				if (idx < arrIssues.length - 1) title.append(", ");
			}
		}
		title.append(" " + props.getString("oscarEncounter.history.title"));
		request.setAttribute("title", title.toString());

		return mapping.findForward("showHistory");
	}

	public ActionForward history(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("history");
		if (request.getSession().getAttribute("userrole") == null) return mapping.findForward("expired");

		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("demoDOB", getDemoDOB(demono));

		String noteid = (String) request.getParameter("noteId");
		CaseManagementNote note = caseManagementMgr.getNote(noteid);

		request.setAttribute("history", note.getHistory());

		cform.setCaseNote_history(note.getHistory());

		LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_CME_NOTE, noteid, request.getRemoteAddr(), note.getAuditString());

		return mapping.findForward("historyview");
	}

	public ActionForward autosave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("autosave");

		String demographicNo = getDemographicNo(request);
		String programId = request.getParameter("programId");
		String providerNo = this.getProviderNo(request);
		String note = request.getParameter("note");
		String noteId = request.getParameter("note_id");

		if (note == null || note.length() == 0) {
			return null;
		}

		try {
			caseManagementMgr.tmpSave(providerNo, demographicNo, programId, noteId, note);
		} catch (Throwable e) {
			logger.warn("AutoSave Error: " + e);
		}

		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		cform.getCaseNote().setNote(note);

		response.setStatus(response.SC_OK);
		return null;
	}

	public ActionForward restore(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.getSession().setAttribute("restoring", "true"); // tell CaseManagementView we're handling temp note
		request.setAttribute("restore", new Boolean(true));

		return edit(mapping, form, request, response);
	}

	public ActionForward cleanup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String demoNo = this.getDemographicNo(request);
		String sessionFrmName = "caseManagementEntryForm" + demoNo;
		String strBeanName = "casemgmt_oscar_bean" + demoNo;

		request.getSession().setAttribute(sessionFrmName, null);
		request.getSession().setAttribute(strBeanName, null);

		return null;
	}

	public String[] getIssueIds(List<Issue> issues) {
		String[] issueIds = new String[issues.size()];
		int idx = 0;
		for (Issue i : issues) {
			issueIds[idx] = String.valueOf(i.getId());
			++idx;
		}
		return issueIds;
	}

	public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf"); // octet-stream
		response.setHeader("Content-Disposition", "attachment; filename=\"Encounter-" + UtilDateUtilities.getToday("yyyy-MM-dd.hh.mm.ss") + ".pdf\"");
		doPrint(request, response.getOutputStream());
		return null;
	}

	public void doPrint(HttpServletRequest request, OutputStream os) throws IOException, DocumentException {
		String ids = request.getParameter("notes2print");

		String demono = getDemographicNo(request);
		request.setAttribute("demoName", getDemoName(demono));
		request.setAttribute("demoSex", getDemoSex(demono));
		request.setAttribute("demoAge", getDemoAge(demono));
		request.setAttribute("mrp", getMRP(request));
		String dob = getDemoDOB(demono);
		dob = convertDateFmt(dob, request);
		request.setAttribute("demoDOB", dob);
		String providerNo = getProviderNo(request);

		String[] noteIds;
		if (ids.length() > 0) noteIds = ids.split(",");
		else noteIds = (String[]) Array.newInstance(String.class, 0);

		List<CaseManagementNote> notes = new ArrayList<CaseManagementNote>();
		for (int idx = 0; idx < noteIds.length; ++idx)
			notes.add(this.caseManagementMgr.getNote(noteIds[idx]));

		// we're not guaranteed any ordering of notes given to us, so sort by observation date
		oscar.OscarProperties p = oscar.OscarProperties.getInstance();
		String noteSort = p.getProperty("CMESort", "");
		if (noteSort.trim().equalsIgnoreCase("UP")) {
			Collections.sort(notes, CaseManagementNote.noteObservationDateComparator);
			Collections.reverse(notes);
		} else Collections.sort(notes, CaseManagementNote.noteObservationDateComparator);

		List<CaseManagementNote> issueNotes;
		List<CaseManagementNote> tmpNotes;
		HashMap<String, List<CaseManagementNote>> cpp = null;
		if (request.getParameter("printCPP").equalsIgnoreCase("true")) {
			cpp = new HashMap<String, List<CaseManagementNote>>();
			String[] issueCodes = { "OMeds", "SocHistory", "MedHistory", "Concerns", "Reminders", "FamHistory", "RiskFactors" };
			for (int j = 0; j < issueCodes.length; ++j) {
				List<Issue> issues = caseManagementMgr.getIssueInfoByCode(providerNo, issueCodes[j]);
				String[] issueIds = getIssueIds(issues);// = new String[issues.size()];
				tmpNotes = caseManagementMgr.getNotes(demono, issueIds);
				issueNotes = new ArrayList<CaseManagementNote>();
				for (int k = 0; k < tmpNotes.size(); ++k) {
					if (!tmpNotes.get(k).isLocked()) {
						issueNotes.add(tmpNotes.get(k));
					}
				}
				cpp.put(issueCodes[j], issueNotes);
			}
		}
		String demoNo = null;
		List<CaseManagementNote> othermeds = null;
		if (request.getParameter("printRx").equalsIgnoreCase("true")) {
			demoNo = demono;
			if (cpp == null) {
				List<Issue> issues = caseManagementMgr.getIssueInfoByCode(providerNo, "OMeds");
				String[] issueIds = getIssueIds(issues);// new String[issues.size()];
				othermeds = caseManagementMgr.getNotes(demono, issueIds);
			} else {
				othermeds = cpp.get("OMeds");
			}
		}

		CaseManagementPrintPdf printer = new CaseManagementPrintPdf(request, os);
		printer.printDocHeaderFooter();
		printer.printCPP(cpp);
		printer.printRx(demoNo, othermeds);
		printer.printNotes(notes);
		printer.finish();
	}

	public CaseManagementNote getLastSaved(HttpServletRequest request, String demono, String providerNo) {
		CaseManagementNote note = null;
		List notes = null;

		// UserProperty prop = caseManagementMgr.getUserProperty(providerNo, UserProperty.STALE_NOTEDATE);
		notes = caseManagementMgr.getNotes(demono);
		notes = manageLockedNotes(notes, false, this.getUnlockedNotesMap(request));

		String programId = (String) request.getSession().getAttribute("case_program_id");

		if (programId == null || programId.length() == 0) {
			programId = "0";
		}

		notes = caseManagementMgr.filterNotes(notes, programId);

		for (int idx = notes.size() - 1; idx >= 0; --idx) {
			CaseManagementNote n = (CaseManagementNote) notes.get(idx);
			if (!n.isSigned() && n.getProviderNo().equals(providerNo)) {
				note = n;
				request.getSession().setAttribute("newNote", "false");
				break;
			}
		}

		return note;
	}

	protected Map getUnlockedNotesMap(HttpServletRequest request) {
		Map map = (Map) request.getSession().getAttribute("unlockedNoteMap");
		if (map == null) {
			map = new HashMap();
		}
		return map;
	}

	private List manageLockedNotes(List notes, boolean removeLockedNotes, Map unlockedNotesMap) {
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
	 * Grab family physician or MRP for demo
	 */
	protected String getMRP(HttpServletRequest request) {
		String strBeanName = "casemgmt_oscar_bean" + this.getDemographicNo(request);
		oscar.oscarEncounter.pageUtil.EctSessionBean bean = (oscar.oscarEncounter.pageUtil.EctSessionBean) request.getSession().getAttribute(strBeanName);
		if (bean == null) return new String("");

		if (bean.familyDoctorNo.equals("")) return new String("");

		oscar.oscarEncounter.data.EctProviderData.Provider prov = new oscar.oscarEncounter.data.EctProviderData().getProvider(bean.familyDoctorNo);
		String name = prov.getFirstName() + " " + prov.getSurname();
		return name;
	}

	/*
	 * Insert encounter reason for new note
	 */
	protected void insertReason(HttpServletRequest request, CaseManagementNote note) {
		String strBeanName = "casemgmt_oscar_bean" + note.getDemographic_no();
		oscar.oscarEncounter.pageUtil.EctSessionBean bean = (oscar.oscarEncounter.pageUtil.EctSessionBean) request.getSession().getAttribute(strBeanName);

		if (bean != null) {
			String encounterText = "";
			String apptDate = convertDateFmt(bean.appointmentDate, request);
			if (bean.eChartTimeStamp == null) {
				encounterText = "\n[" + UtilDateUtilities.DateToString(bean.currentDate, "dd-MMM-yyyy", request.getLocale()) + " .: " + bean.reason + "] \n";
				// encounterText +="\n["+bean.appointmentDate+" .: "+bean.reason+"] \n";
			} else { // if(bean.currentDate.compareTo(bean.eChartTimeStamp)>0){
				// log.debug("2curr Date "+ oscar.util.UtilDateUtilities.DateToString(oscar.util.UtilDateUtilities.now(),"yyyy",java.util.Locale.CANADA) );
				// encounterText +="\n__________________________________________________\n["+dateConvert.DateToString(bean.currentDate)+" .: "+bean.reason+"]\n";
				encounterText = "\n[" + ("".equals(bean.appointmentDate) ? UtilDateUtilities.getToday("dd-MMM-yyyy") : apptDate) + " .: " + bean.reason + "]\n";
			} /*
			 * else if((bean.currentDate.compareTo(bean.eChartTimeStamp) == 0) && (bean.reason != null || bean.subject != null ) && !bean.reason.equals(bean.subject) ){ //encounterText
			 * +="\n__________________________________________________\n["+dateConvert.DateToString(bean.currentDate)+" .: "+bean.reason+"]\n"; encounterText ="\n["+apptDate+" .: "+bean.reason+"]\n"; }
			 */
			// log.debug("eChartTimeStamp" + bean.eChartTimeStamp+" bean.currentDate " + dateConvert.DateToString(bean.currentDate));//" diff "+bean.currentDate.compareTo(bean.eChartTimeStamp));
			// if (!bean.oscarMsg.equals("")) {
			// encounterText += "\n\n" + bean.oscarMsg;
			// }

			note.setNote(encounterText);
			String encType = request.getParameter("encType");

			if (encType == null || encType.equals("")) {
				note.setEncounter_type("");
			} else {
				note.setEncounter_type(encType);
			}
		}

	}

	protected String convertDateFmt(String strOldDate, HttpServletRequest request) {
		String strNewDate = "";
		if (strOldDate != null && strOldDate.length() > 0) {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", request.getLocale());
			try {

				Date tempDate = fmt.parse(strOldDate);
				strNewDate = new SimpleDateFormat("dd-MMM-yyyy", request.getLocale()).format(tempDate);

			} catch (ParseException ex) {
				MiscUtils.getLogger().error("Error", ex);
			}
		}

		return strNewDate;
	}

	protected CaseManagementCPP copyNote2cpp(CaseManagementCPP cpp, CaseManagementNote note) {
		Set<CaseManagementIssue> issueSet = note.getIssues();
		StringBuilder text = new StringBuilder();
		Date d = new Date();
		String separator = "\n-----[[" + d + "]]-----\n";
		for (CaseManagementIssue issue : issueSet) {
			String code = issue.getIssue().getCode();
			if (code.equals("OMeds")) {
				text.append(cpp.getFamilyHistory());
				text.append(separator);
				text.append(note.getNote());
				cpp.setFamilyHistory(text.toString());
				break;
			} else if (code.equals("SocHistory")) {
				text.append(cpp.getSocialHistory());
				text.append(separator);
				text.append(note.getNote());
				cpp.setSocialHistory(text.toString());
				break;
			} else if (code.equals("MedHistory")) {
				text.append(cpp.getMedicalHistory());
				text.append(separator);
				text.append(note.getNote());
				cpp.setMedicalHistory(text.toString());
				break;
			} else if (code.equals("Concerns")) {
				text.append(cpp.getOngoingConcerns());
				text.append(separator);
				text.append(note.getNote());
				cpp.setOngoingConcerns(text.toString());
				break;
			} else if (code.equals("Reminders")) {
				text.append(cpp.getReminders());
				text.append(separator);
				text.append(note.getNote());
				cpp.setReminders(text.toString());
				break;
			} else if (code.equals("FamHistory")) {
				text.append(cpp.getFamilyHistory());
				text.append(separator);
				text.append(note.getNote());
				cpp.setFamilyHistory(text.toString());
				break;
			} else if (code.equals("RiskFactors")) {
				text.append(cpp.getRiskFactors());
				text.append(separator);
				text.append(note.getNote());
				cpp.setRiskFactors(text.toString());
				break;
			}
		}

		return cpp;
	}

	/*
	 * Retrieve CPP issuesIf not in session, load them
	 */
	protected HashMap getCPPIssues(HttpServletRequest request, String providerNo) {
		HashMap<String, Issue> issues = (HashMap<String, Issue>) request.getSession().getAttribute("CPPIssues");
		if (issues == null) {
			String[] issueCodes = { "SocHistory", "MedHistory", "Concerns", "Reminders", "FamHistory" };
			issues = new HashMap<String, Issue>();
			for (String issue : issueCodes) {
				List<Issue> i = caseManagementMgr.getIssueInfoByCode(providerNo, issue);
				issues.put(issue, i.get(0));
			}

			request.getSession().setAttribute("CPPIssues", issues);
		}
		return issues;
	}

	boolean filled(String s) {
		return (s != null && s.length() > 0);
	}

	public boolean haveIssue(Long issid, List allNotes) {

		Iterator itr = allNotes.iterator();
		while (itr.hasNext()) {
			CaseManagementNote note = (CaseManagementNote) itr.next();
			Set issues = note.getIssues();
			Iterator its = issues.iterator();
			while (its.hasNext()) {
				CaseManagementIssue iss = (CaseManagementIssue) its.next();
				if (iss.getId().intValue() == issid.intValue()) return true;
			}
		}
		return false;
	}
}
