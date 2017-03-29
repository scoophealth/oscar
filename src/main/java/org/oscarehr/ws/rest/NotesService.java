/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.ws.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.casemgmt.dao.CaseManagementNoteLinkDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.casemgmt.service.NoteSelectionCriteria;
import org.oscarehr.casemgmt.service.NoteSelectionResult;
import org.oscarehr.casemgmt.service.NoteService;
import org.oscarehr.casemgmt.web.CaseManagementEntryAction;
import org.oscarehr.casemgmt.web.NoteDisplay;
import org.oscarehr.casemgmt.web.NoteDisplayLocal;
import org.oscarehr.common.dao.CaseManagementIssueNotesDao;
import org.oscarehr.common.model.CaseManagementTmpSave;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.EncounterUtil;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.conversion.CaseManagementIssueConverter;
import org.oscarehr.ws.rest.conversion.IssueConverter;
import org.oscarehr.ws.rest.to.AbstractSearchResponse;
import org.oscarehr.ws.rest.to.GenericRESTResponse;
import org.oscarehr.ws.rest.to.TicklerNoteResponse;
import org.oscarehr.ws.rest.to.model.CaseManagementIssueTo1;
import org.oscarehr.ws.rest.to.model.IssueTo1;
import org.oscarehr.ws.rest.to.model.NoteExtTo1;
import org.oscarehr.ws.rest.to.model.NoteIssueTo1;
import org.oscarehr.ws.rest.to.model.NoteSelectionTo1;
import org.oscarehr.ws.rest.to.model.NoteTo1;
import org.oscarehr.ws.rest.to.model.TicklerNoteTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oscar.OscarProperties;
import oscar.oscarEncounter.pageUtil.EctSessionBean;


@Path("/notes")
@Component("notesService")
public class NotesService extends AbstractServiceImpl {

	public static String cppCodes[] = {"OMeds", "SocHistory", "MedHistory", "Concerns", "FamHistory", "Reminders", "RiskFactors","OcularMedication","TicklerNote"};
	
	private static Logger logger = MiscUtils.getLogger();
	
	private static ConcurrentHashMap<String, ConcurrentHashMap<String, Long>> editList = new ConcurrentHashMap<String, ConcurrentHashMap<String, Long>>();
	
	@Autowired
	private NoteService noteService; 
	
	@Autowired
	private ProgramManager2 programManager2;
	
	@Autowired
	private ProgramManager programMgr;
	
	@Autowired
	private CaseManagementManager caseManagementMgr;
	
	@Autowired
	private ProviderManager providerMgr;
	
	@Autowired
	private IssueDAO issueDao;
	
	@Autowired
	private SecurityInfoManager securityInfoManager;
	
	
	@POST
	@Path("/{demographicNo}/all")
	@Produces("application/json")
	@Consumes("application/json")
	public NoteSelectionTo1 getNotesWithFilter(@PathParam("demographicNo") Integer demographicNo ,@DefaultValue("20") @QueryParam("numToReturn") Integer numToReturn,@DefaultValue("0") @QueryParam("offset") Integer offset,JSONObject jsonobject){
		NoteSelectionTo1 returnResult = new NoteSelectionTo1();
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		logger.debug("The config "+jsonobject.toString());
	
		HttpSession se = loggedInInfo.getSession();
		if (se.getAttribute("userrole") == null) {
			logger.error("An Error needs to be added to the returned result, remove this when fixed");
			return returnResult;
		}
		
		String demoNo = ""+demographicNo;

		logger.debug("is client in program");
		// need to check to see if the client is in our program domain
		// if not...don't show this screen!
		String roles = (String) se.getAttribute("userrole");
		if (OscarProperties.getInstance().isOscarLearning() && roles != null && roles.indexOf("moderator") != -1) {
			logger.info("skipping domain check..provider is a moderator");
		} else if (!caseManagementMgr.isClientInProgramDomain(loggedInInfo.getLoggedInProviderNo(), demoNo) && !caseManagementMgr.isClientReferredInProgramDomain(loggedInInfo.getLoggedInProviderNo(), demoNo)) {
			logger.error("A domain error needs to be added to the returned result, remove this when fixed");
			return returnResult;
		}
		
		ProgramProvider pp = programManager2.getCurrentProgramInDomain(getLoggedInInfo(),loggedInInfo.getLoggedInProviderNo());
		String programId = null;
		
		if(pp !=null && pp.getProgramId() != null){
			programId = ""+pp.getProgramId();
		}else{
			programId = String.valueOf(programMgr.getProgramIdByProgramName("OSCAR")); //Default to the oscar program if provider hasn't been assigned to a program
		}
		
		NoteSelectionCriteria criteria = new NoteSelectionCriteria();
		
		criteria.setMaxResults(numToReturn);
		criteria.setFirstResult(offset);
		
		criteria.setDemographicId(demographicNo);
		criteria.setUserRole((String) se.getAttribute("userrole"));
		criteria.setUserName((String) se.getAttribute("user"));
		
		// Note order is not user selectable in this version yet
		criteria.setNoteSort("observation_date_desc");  
		criteria.setSliceFromEndOfList(false);
				

		if (programId != null && !programId.trim().isEmpty()) {
			criteria.setProgramId(programId);
		}
		
		processJsonArray(jsonobject, "filterRoles", criteria.getRoles());
		
		processJsonArray(jsonobject, "filterProviders", criteria.getProviders());
		
		processJsonArray(jsonobject, "filterIssues", criteria.getIssues());
		
		if (logger.isDebugEnabled()) {
			logger.debug("SEARCHING FOR NOTES WITH CRITERIA: " + criteria);
		}
		
		NoteSelectionResult result = noteService.findNotes(loggedInInfo,criteria);
		
		if (logger.isDebugEnabled()) {
			logger.debug("FOUND: " + result);
			for(NoteDisplay nd : result.getNotes()) {
				logger.debug("   " + nd.getClass().getSimpleName() + " " + nd.getNoteId() + " " + nd.getNote());
			}
		}
		
		
		
		returnResult.setMoreNotes(result.isMoreNotes());
		List<NoteTo1> noteList = returnResult.getNotelist();
		for(NoteDisplay nd : result.getNotes()) {
			NoteTo1 note = new NoteTo1();
			note.setNoteId(nd.getNoteId());
			
			note.setIsSigned(nd.isSigned());
			note.setIsEditable(nd.isEditable());
			note.setObservationDate(nd.getObservationDate());
			note.setRevision(nd.getRevision());
			note.setUpdateDate(nd.getUpdateDate());
			note.setProviderName(nd.getProviderName());
			note.setProviderNo(nd.getProviderNo());
			note.setStatus(nd.getStatus());
			note.setProgramName(nd.getProgramName());
			note.setLocation(nd.getLocation());
			note.setRoleName(nd.getRoleName());
			note.setRemoteFacilityId(nd.getRemoteFacilityId());
			note.setUuid(nd.getUuid());
			note.setHasHistory(nd.getHasHistory());
			note.setLocked(nd.isLocked());
			note.setNote(nd.getNote());
			note.setDocument(nd.isDocument());
			note.setRxAnnotation(nd.isRxAnnotation());
			note.setEformData(nd.isEformData());
			note.setEncounterForm(nd.isEncounterForm());
			note.setInvoice(nd.isInvoice());
			note.setTicklerNote(nd.isTicklerNote());
			note.setEncounterType(nd.getEncounterType());
			note.setEditorNames(nd.getEditorNames());
			note.setIssueDescriptions(nd.getIssueDescriptions());
			note.setReadOnly(nd.isReadOnly());
			note.setGroupNote(nd.isGroupNote());
			note.setCpp(nd.isCpp());
			note.setEncounterTime(nd.getEncounterTime());	
			note.setEncounterTransportationTime(nd.getEncounterTransportationTime());
			
			noteList.add(note);
		}
		logger.debug("returning note list size "+noteList.size() +"  numToReturn was "+numToReturn+" offset "+offset );
		
		return returnResult;
	}
	
	
	
	@POST
	@Path("/{demographicNo}/tmpSave")
	@Consumes("application/json")
	@Produces("application/json")
	public NoteTo1 tmpSaveNote(@PathParam("demographicNo") Integer demographicNo ,NoteTo1 note){
		logger.debug("autosave "+note);

		LoggedInInfo loggedInInfo = getLoggedInInfo();//  LoggedInInfo.loggedInInfo.get();
		String providerNo=loggedInInfo.getLoggedInProvider().getProviderNo();

		
		String programId = getProgram(loggedInInfo,providerNo);
		String noteStr = note.getNote();
		String noteId  = ""+note.getNoteId();
		
		try{  
			Integer.parseInt(noteId);
		}catch(Exception e){
			noteId = null;
		}
		
		/* NOT SURE HOW TO HANDLE LOCKS YET!!
		//compare locks and see if they are the same
		CasemgmtNoteLock casemgmtNoteLockSession = (CasemgmtNoteLock)request.getSession().getAttribute("casemgmtNoteLock"+demographicNo);
		try {
			//if other window has acquired lock don't save
			CasemgmtNoteLock casemgmtNoteLock = casemgmtNoteLockDao.find(casemgmtNoteLockSession.getId());
			if( !casemgmtNoteLock.getSessionId().equals(casemgmtNoteLockSession.getSessionId()) ) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}
		}
		catch(Exception e ) {
			//Exception thrown if other window has saved and exited so lock is gone
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;

		}		
		*/
		if (noteStr == null || noteStr.length() == 0) {
			return null;
		}		
		
		//delete from tmp save and then add another
		try {
			caseManagementMgr.deleteTmpSave(providerNo, ""+demographicNo, programId);
			caseManagementMgr.tmpSave(providerNo, ""+demographicNo, programId, noteId, noteStr);
		} catch (Exception e) {
			logger.error("AutoSave Error: ", e);
		}

		return note;
	}
	
	private String getProgram(LoggedInInfo loggedInInfo,String providerNo){
		ProgramProvider pp = programManager2.getCurrentProgramInDomain(loggedInInfo,providerNo);
		String programId = null;
		
		if(pp !=null && pp.getProgramId() != null){
			programId = ""+pp.getProgramId();
		}else{
			programId = String.valueOf(programMgr.getProgramIdByProgramName("OSCAR")); //Default to the oscar program if provider hasn't been assigned to a program
		}
		return programId;
	}
	
	
	@POST
	@Path("/{demographicNo}/save")
	@Consumes("application/json")
	@Produces("application/json")
	public NoteTo1 saveNote(@PathParam("demographicNo") Integer demographicNo ,NoteTo1 note) throws Exception{
		logger.debug("saveNote "+note);
		LoggedInInfo loggedInInfo = getLoggedInInfo(); //LoggedInInfo.loggedInInfo.get();
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		Provider provider = loggedInInfo.getLoggedInProvider();
		String userName = provider != null ? provider.getFullName() : "";

		String demo = ""+demographicNo;
		
		CaseManagementNote caseMangementNote  = new CaseManagementNote();
		
		caseMangementNote.setDemographic_no(demo);
		caseMangementNote.setProvider(provider);
		caseMangementNote.setProviderNo(providerNo);
		
		if(note.getUuid() != null && !note.getUuid().trim().equals("")){
			caseMangementNote.setUuid(note.getUuid());
		}
		
		String noteTxt = note.getNote();
		noteTxt = org.apache.commons.lang.StringUtils.trimToNull(noteTxt);
		if (noteTxt == null || noteTxt.equals("")) return null;

		caseMangementNote.setNote(noteTxt);
		
		CaseManagementCPP cpp = this.caseManagementMgr.getCPP(demo);
		if (cpp == null) {
			cpp = new CaseManagementCPP();
			cpp.setDemographic_no(demo);
		}
		logger.debug("enc TYPE " +note.getEncounterType());
		caseMangementNote.setEncounter_type(note.getEncounterType());
		
		//caseMangementNote.setHourOfEncounterTime(note.getEncounterTime());
		logger.debug("this is what the encounter time was "+note.getEncounterTime());
		/*String hourOfEncounterTime = request.getParameter("hourOfEncounterTime");
		if (hourOfEncounterTime != null && hourOfEncounterTime != "") {
			note.setHourOfEncounterTime(Integer.valueOf(hourOfEncounterTime));
		}

		String minuteOfEncounterTime = request.getParameter("minuteOfEncounterTime");
		if (minuteOfEncounterTime != null && minuteOfEncounterTime != "") {
			note.setMinuteOfEncounterTime(Integer.valueOf(minuteOfEncounterTime));
		}*/

		logger.debug("this is what the encounter time was "+note.getEncounterTransportationTime());
		/*
		String hourOfEncTransportationTime = request.getParameter("hourOfEncTransportationTime");
		if (hourOfEncTransportationTime != null && hourOfEncTransportationTime != "") {
			note.setHourOfEncTransportationTime(Integer.valueOf(hourOfEncTransportationTime));
		}

		String minuteOfEncTransportationTime = request.getParameter("minuteOfEncTransportationTime");
		if (minuteOfEncTransportationTime != null && minuteOfEncTransportationTime != "") {
			note.setMinuteOfEncTransportationTime(Integer.valueOf(minuteOfEncTransportationTime));
		}
		*/
		//Need to check some how that if a note is signed that it must stay signed, currently this is done in the interface where the save button is not available.
		if(note.getIsSigned()){
			caseMangementNote.setSigning_provider_no(providerNo);
			caseMangementNote.setSigned(true);
		} else {
			caseMangementNote.setSigning_provider_no("");
			caseMangementNote.setSigned(false);
		}
		
		caseMangementNote.setProviderNo(providerNo);
		if (provider != null) caseMangementNote.setProvider(provider);

		//note.getPro
		String programIdString = getProgram(loggedInInfo,providerNo); //might not to convert it.
		Integer programId = null;
		try {
			programId = Integer.parseInt(programIdString);
		} catch (Exception e) {
			logger.warn("Error parsing programId:" + programIdString, e);
		}
		caseMangementNote.setProgram_no(programIdString);
		
		List<CaseManagementIssue> issuelist = new ArrayList<CaseManagementIssue>();
		
		for(CaseManagementIssueTo1 i:note.getAssignedIssues()) {
			if(!i.isUnchecked()) {
				CaseManagementIssue cmi = caseManagementMgr.getIssueByIssueCode(demo, i.getIssue().getCode());
				if(cmi != null) {
					//update
				} else {
					//new one
					cmi = new CaseManagementIssue();
					Issue is = issueDao.getIssue(i.getIssue_id());
					cmi.setIssue_id(is.getId());
					cmi.setIssue(is);
					cmi.setProgram_id(programManager2.getCurrentProgramInDomain(getLoggedInInfo(), getLoggedInInfo().getLoggedInProviderNo()).getProgramId().intValue());
					cmi.setType(is.getRole());
					cmi.setDemographic_no(Integer.valueOf(demo));
				}
				
				cmi.setAcute(i.isAcute());
				cmi.setCertain(i.isCertain());
				cmi.setMajor(i.isMajor());
				cmi.setResolved(i.isResolved());
				cmi.setUpdate_date(new Date());
				
				issuelist.add(cmi);
				caseManagementMgr.saveCaseIssue(cmi);
			} 
		}
		
		note.setIssues(new HashSet<CaseManagementIssue>(issuelist));
		caseMangementNote.setIssues(new HashSet<CaseManagementIssue>(issuelist));

		
		String ongoing = new String();
		//ongoing = saveCheckedIssues_newCme(request, demo, note, issuelist, checkedlist, issueset, noteSet, ongoing);
		
	
		// remove signature and the related issues from note 
		String noteString = note.getNote();
		// noteString = removeSignature(noteString);
		// noteString = removeCurrentIssue(noteString);
		caseMangementNote.setNote(noteString);
		
		/* Not sure how to handle this
		// add issues into notes 
		String includeIssue = request.getParameter("includeIssue");
		if (includeIssue == null || !includeIssue.equals("on")) {
			// set includeissue in note 
			note.setIncludeissue(false);
			sessionFrm.setIncludeIssue("off");
		} else {
			note.setIncludeissue(true);
			// add the related issues to note

			String issueString = new String();
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
		*/
		
		// update appointment and add verify message to note if verified
		
		boolean verify = false;
		if(note.getIsVerified()!=null && note.getIsVerified()){
			verify = true;
		}
		
		
		// update password
		/*
		String passwd = cform.getCaseNote().getPassword();
		if (passwd != null && passwd.trim().length() > 0) {
			note.setPassword(passwd);
			note.setLocked(true);
		}
		 */
		Date now = new Date();
		
		Date observationDate = note.getObservationDate();
		if (observationDate != null && !observationDate.equals("")) {
			if (observationDate.getTime() > now.getTime()) {
				//request.setAttribute("DateError", props.getString("oscarEncounter.futureDate.Msg"));
				caseMangementNote.setObservation_date(now);
			} else{
				caseMangementNote.setObservation_date(observationDate);
			}
		} else if (note.getObservationDate() == null) {
			caseMangementNote.setObservation_date(now);
		}
		
		caseMangementNote.setUpdate_date(now);
		
		/* Currently not available from this method
		// Checks whether the user can set the program via the UI - if so, make sure that they can't screw it up if they do
				if (OscarProperties.getInstance().getBooleanProperty("note_program_ui_enabled", "true")) {
					String noteProgramNo = request.getParameter("_note_program_no");
					String noteRoleId = request.getParameter("_note_role_id");

					if (noteProgramNo != null && noteRoleId != null && noteProgramNo.trim().length() > 0 && noteRoleId.trim().length() > 0) {
						if (noteProgramNo.equalsIgnoreCase("-2") || noteRoleId.equalsIgnoreCase("-2")) {
							throw new Exception("Patient is not admitted to any programs user has access to. [roleId=-2, programNo=-2]");
						} else if (!noteProgramNo.equalsIgnoreCase("-1") && !noteRoleId.equalsIgnoreCase("-1")) {
							note.setProgram_no(noteProgramNo);
							note.setReporter_caisi_role(noteRoleId);
						}
					} else {
								throw new Exception("Missing role id or program number. [roleId=" + noteRoleId + ", programNo=" + noteProgramNo + "]");
					}
				}
		 	*/
		
		
		if (note.getAppointmentNo() != null) {
			caseMangementNote.setAppointmentNo(note.getAppointmentNo());
		}
		
		
		// Save annotation 

		CaseManagementNote annotationNote = null;// (CaseManagementNote) session.getAttribute(attrib_name);

		//String ongoing = null; // figure out this
		String lastSavedNoteString = null;
		String user = loggedInInfo.getLoggedInProvider().getProviderNo();
		String remoteAddr = 	""; // Not sure how to get this	
		caseMangementNote = caseManagementMgr.saveCaseManagementNote(loggedInInfo, caseMangementNote,issuelist, cpp, ongoing,verify, loggedInInfo.getLocale(),now,annotationNote,userName,user,remoteAddr, lastSavedNoteString) ;
			
		caseManagementMgr.getEditors(caseMangementNote);
		
			
		
		note.setNoteId(Integer.parseInt(""+caseMangementNote.getId()));
		note.setUuid(caseMangementNote.getUuid());
		note.setUpdateDate(caseMangementNote.getUpdate_date());
		note.setObservationDate(caseMangementNote.getObservation_date());
		logger.error("note should return like this " + note.getNote() );
		return note;
		
		
		/*
		//update lock to new note id
		casemgmtNoteLockSession.setNoteId(note.getId());
		logger.info("UPDATING NOTE ID in LOCK");
		casemgmtNoteLockDao.merge(casemgmtNoteLockSession);
		session.setAttribute("casemgmtNoteLock"+demo, casemgmtNoteLockSession);	
		*/


		/*
		String sessionFrmName = "caseManagementEntryForm" + demo;
		CaseManagementEntryFormBean sessionFrm = (CaseManagementEntryFormBean) session.getAttribute(sessionFrmName);
		
		CasemgmtNoteLock casemgmtNoteLockSession = (CasemgmtNoteLock)session.getAttribute("casemgmtNoteLock"+demo);				
		
		try {
			
			if(casemgmtNoteLockSession == null) {
				throw new Exception("SESSION CASEMANAGEMENT NOTE LOCK OBJECT IS NULL");
			}
			
			CasemgmtNoteLock casemgmtNoteLock = casemgmtNoteLockDao.find(casemgmtNoteLockSession.getId());
			//if other window has acquired lock we reject save									
			if( !casemgmtNoteLock.getSessionId().equals(casemgmtNoteLockSession.getSessionId()) || !request.getRequestedSessionId().equals(casemgmtNoteLockSession.getSessionId()) ) {
				logger.info("DO NOT HAVE LOCK FOR " + demo + " PROVIDER " + providerNo + " CONTINUE SAVING LOCAL SESSION " + request.getRequestedSessionId() + " LOCAL IP " + request.getRemoteAddr() + " LOCK SESSION " + casemgmtNoteLockSession.getSessionId() + " LOCK IP " + casemgmtNoteLockSession.getIpAddress());
				return -1L;
			}
		}
		catch(Exception e ) {
			//Exception thrown if other window has saved and exited so lock is gone
			logger.error("Lock not found for " + demo + " provider " + providerNo + " IP " + request.getRemoteAddr(), e);
			return -1L;
		}
		String lastSavedNoteString = (String) session.getAttribute("lastSavedNoteString");
		
		String strBeanName = "casemgmt_oscar_bean" + demo;
		EctSessionBean sessionBean = (EctSessionBean) session.getAttribute(strBeanName);

		return note.getId();
		*/
		
		
	}
	
	
	
	@POST
	@Path("/{demographicNo}/saveIssueNote")
	@Consumes("application/json")
	@Produces("application/json")
	public NoteIssueTo1 saveIssueNote(@PathParam("demographicNo") Integer demographicNo ,NoteIssueTo1 noteIssue) throws Exception{
		NoteTo1 note = noteIssue.getEncounterNote();
		NoteExtTo1 noteExt = noteIssue.getGroupNoteExt();
		IssueTo1 issue = noteIssue.getIssue();
		//List<IssueTo1> assignedIssues = noteIssue.getAssignedIssues();
		List<CaseManagementIssueTo1> assignedCMIssues = noteIssue.getAssignedCMIssues();
		
		LoggedInInfo loggedInInfo = getLoggedInInfo(); //LoggedInInfo.loggedInInfo.get();
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		Provider provider = loggedInInfo.getLoggedInProvider();
		String userName = provider != null ? provider.getFullName() : "";

		String demo = ""+demographicNo;
		String noteId = String.valueOf(note.getNoteId());
		
		String programId = getProgram(loggedInInfo,providerNo);
				
		CaseManagementNote caseMangementNote  = new CaseManagementNote();
		boolean newNote = false;
		
		// we don't want to try to remove an issue from a new note so we test here
		if(note.getNoteId()==null || note.getNoteId()==0){		
			newNote = true;
		}else{
			boolean extChanged = true; //false
			// if note has not changed don't save
			caseManagementMgr.getNote(noteId);
			if ( note.getNote().equals(note.getNote()) && issue.isIssueChange() && !extChanged && note.isArchived() ) return null;
		}
		
		caseMangementNote.setDemographic_no(demo);

		if(!newNote) {
			if (note.isArchived() ){
				caseMangementNote.setArchived(true);
			}
			note.setRevision(Integer.parseInt(note.getRevision())+1 + "");	
		}
		
		
		if(note.getUuid() != null && !note.getUuid().trim().equals("")){
			caseMangementNote.setUuid(note.getUuid());
		}
		
		String noteTxt = note.getNote();
		noteTxt = org.apache.commons.lang.StringUtils.trimToNull(noteTxt);
		if (noteTxt == null || noteTxt.equals("")) return null;

		caseMangementNote.setNote(noteTxt);
	
		CaseManagementCPP cpp = this.caseManagementMgr.getCPP(demo);
		if (cpp == null) {
			cpp = new CaseManagementCPP();
			cpp.setDemographic_no(demo);
		}
		
		if(note.isCpp() && note.getSummaryCode()!=null){
		cpp = copyNote2cpp(cpp, note.getNote(), note.getSummaryCode());
		}
		
		ProgramManager programManager = (ProgramManager) SpringUtils.getBean("programManager");
		AdmissionManager admissionManager = (AdmissionManager) SpringUtils.getBean("admissionManager");

		String role = null;
		String team = null;

		try {
			role = String.valueOf((programManager.getProgramProvider(providerNo, programId)).getRole().getId());
		} catch (Exception e) {
			logger.error("Error", e);
			role = "0";
		}

		caseMangementNote.setReporter_caisi_role(role);

		try {
			team = String.valueOf((admissionManager.getAdmission(programId, demographicNo)).getTeamId());
		} catch (Exception e) {
			logger.error("Error", e);
			team = "0";
		}
		caseMangementNote.setReporter_program_team(team);		
				
		//Need to check some how that if a note is signed that it must stay signed, currently this is done in the interface where the save button is not available.
		if(note.getIsSigned()){
			caseMangementNote.setSigning_provider_no(providerNo);
			caseMangementNote.setSigned(true);
		} else {
			caseMangementNote.setSigning_provider_no("");
			caseMangementNote.setSigned(false);
		}
		
		caseMangementNote.setProviderNo(providerNo);
		if (provider != null) caseMangementNote.setProvider(provider);

		
		caseMangementNote.setProgram_no(programId);
		
		//this code basically updates the CPP note with which issues were removed
		if(!newNote) {
			List<String> removedIssueNames = new ArrayList<String>();
			for(CaseManagementIssueTo1 cmit : assignedCMIssues) {
				if(cmit.isUnchecked() && cmit.getId() != null && cmit.getId().longValue()>0) {
					//we want to remove this association, and append to the note
					removedIssueNames.add(cmit.getIssue().getDescription());
				}
			}
			
			if(!removedIssueNames.isEmpty()) {
				String text =  new SimpleDateFormat("dd-MMM-yyyy").format(new Date()) + " " + "Removed following issue(s)" + ":\n" + StringUtils.join(removedIssueNames, ",");
				caseMangementNote.setNote(caseMangementNote.getNote() + "\n" + text);
			}
		}
				
		//String issue_id = issues.getId().toString();
		//String ongoing = new String();
	
		List<CaseManagementIssue> issuelist = new ArrayList<CaseManagementIssue>();
		
		for(CaseManagementIssueTo1 i:assignedCMIssues) {
			if(!i.isUnchecked()) {
				CaseManagementIssue cmi = caseManagementMgr.getIssueByIssueCode(demo, i.getIssue().getCode());
				if (cmi==null) {
					//new one
					cmi = new CaseManagementIssue();
					Issue is = issueDao.getIssue(i.getIssue_id());
					cmi.setIssue_id(is.getId());
					cmi.setIssue(is);
					cmi.setProgram_id(programManager2.getCurrentProgramInDomain(getLoggedInInfo(), getLoggedInInfo().getLoggedInProviderNo()).getProgramId().intValue());
					cmi.setType(is.getRole());
					cmi.setDemographic_no(Integer.valueOf(demo));
				}
				cmi.setAcute(i.isAcute());
				cmi.setCertain(i.isCertain());
				cmi.setMajor(i.isMajor());
				cmi.setResolved(i.isResolved());
				
				issuelist.add(cmi);
				caseManagementMgr.saveCaseIssue(cmi);
			} 
		}
	//	caseMangementNote.setIssues(new HashSet<CaseManagementIssue>(issuelist));
		
		
		//this is actually just the issue for the main note
		//translate summary codes
		String issueCode = note.getSummaryCode();//set temp
		if("ongoingconcerns".equals(issueCode)){
			issueCode = "Concerns";
		}else if("medhx".equals(issueCode)){
			issueCode = "MedHistory";
		}else if("reminders".equals(issueCode)){
			issueCode = "Reminders"; 
		}else if("othermeds".equals(issueCode)){
			issueCode = "OMeds";
		}else if("sochx".equals(issueCode)){
			issueCode = "SocHistory";
		}else if("famhx".equals(issueCode)){
			issueCode = "FamHistory";
		}else if("riskfactors".equals(issueCode)){
			issueCode = "RiskFactors";
		}

		Issue cppIssue = caseManagementMgr.getIssueInfoByCode(issueCode);				
		
		CaseManagementIssue cIssue;
		cIssue = caseManagementMgr.getIssueByIssueCode(demo, issueCode);	
		
		//no issue existing for this type of CPP note..create and save it
		if( cIssue == null ) {
			Date creationDate = new Date();

			cIssue = new CaseManagementIssue();
			cIssue.setAcute(false);
			cIssue.setCertain(false);
			cIssue.setDemographic_no(Integer.valueOf(demo));
			cIssue.setIssue_id(cppIssue.getId());
			cIssue.setMajor(false);
			cIssue.setProgram_id(Integer.parseInt(programId));
			cIssue.setResolved(false);
			cIssue.setType(cppIssue.getRole()); 
			cIssue.setUpdate_date(creationDate);
			
			caseManagementMgr.saveCaseIssue(cIssue);
		}

		//save the associations
		issuelist.add(cIssue);	
		
		note.setIssues(new HashSet<CaseManagementIssue>(issuelist));
		caseMangementNote.setIssues(new HashSet<CaseManagementIssue>(issuelist));
		
		// remove signature and the related issues from note 
		//String noteString = note.getNote();
		// noteString = removeSignature(noteString);
		// noteString = removeCurrentIssue(noteString);
		//caseMangementNote.setNote(noteString);
		

		// update appointment and add verify message to note if verified
		boolean verify = false;

		Date now = new Date();
		
		Date observationDate = note.getObservationDate();
		if (observationDate != null && !observationDate.equals("")) {
			if (observationDate.getTime() > now.getTime()) {
				//request.setAttribute("DateError", props.getString("oscarEncounter.futureDate.Msg"));
				caseMangementNote.setObservation_date(now);
			} else{
				caseMangementNote.setObservation_date(observationDate);
			}
		} else if (note.getObservationDate() == null) {
			caseMangementNote.setObservation_date(now);
		}
		
		caseMangementNote.setUpdate_date(now);
		if(note.getAppointmentNo() != null) {
			caseMangementNote.setAppointmentNo(note.getAppointmentNo());
		}
		
		
		//update positions
		/*
		 * There's a few cases to handle, but basically when user is adding, editing, or archiving,
		 * we go and set the positions so it's always 1,2,..,n across the group note. Archived notes,
		 * and older notes (not the latest based on uuid/id) have positions set to 0
		 */
		String[] strIssueId = { String.valueOf(cppIssue.getId()) };
		List<CaseManagementNote> curCPPNotes = this.caseManagementMgr.getActiveNotes(demo, strIssueId);
		Collections.sort(curCPPNotes,CaseManagementNote.getPositionComparator());
		
		
		if(note.isArchived()) {
			//this one will basically assign 1,2,3,..,n to the group and ignore the one to be archived..setting it's position to 0
			int positionToAssign=1;
			for(int x=0;x<curCPPNotes.size();x++) {
				if(curCPPNotes.get(x).getUuid().equals(note.getUuid())) {
					curCPPNotes.get(x).setPosition(0);
					caseManagementMgr.updateNote(curCPPNotes.get(x));
					continue;
				}
				curCPPNotes.get(x).setPosition(positionToAssign);
				caseManagementMgr.updateNote(curCPPNotes.get(x));
				positionToAssign++;
			}
			
		} else {
			List<CaseManagementNote> curCPPNotes2 = new ArrayList<CaseManagementNote>();
			for(CaseManagementNote cn:curCPPNotes) {
				if(!cn.getUuid().equals(note.getUuid())) {
					curCPPNotes2.add(cn);
				} else {
					cn.setPosition(0);
					caseManagementMgr.updateNote(cn);
				}
			}
			//we make a fake CaseManagementNoteEntry into curCPPNotes, and insert it into desired location. 
			//we then just set the positions to 1,2,...,n ignoring the fake one, but still incrementing the positionToAssign variable
			//when the new note is saved.it will have the missing position.
			int positionToAssign=1;
			CaseManagementNote xn = new CaseManagementNote();
			xn.setId(-1L);
			curCPPNotes2.add(note.getPosition()-1,xn);
			for(int x=0;x<curCPPNotes2.size();x++) {
				if(curCPPNotes2.get(x).getId() != -1L) {
					//update the note
					curCPPNotes2.get(x).setPosition(positionToAssign);
					caseManagementMgr.updateNote(curCPPNotes2.get(x));
				} 
				if(curCPPNotes2.get(x).getId() != -1L && curCPPNotes2.get(x).getUuid().equals(note.getUuid())) {
					curCPPNotes2.get(x).setPosition(0);
					caseManagementMgr.updateNote(curCPPNotes2.get(x));
					positionToAssign--;
				}
				positionToAssign++;
			}
		}
		if(!note.isArchived()) {
			caseMangementNote.setPosition(note.getPosition());
		}
		
		/*
		 * 
		 * update_date, observation_date, 
		 * demographic_no, provider_no, note, signed, 
		 * include_issue_innote, signing_provider_no, encounter_type, billing_code, 
		 * program_no, reporter_caisi_role, reporter_program_team, history,
		 *  uuid, password, locked, archived, appointmentNo, hourOfEncounterTime, minuteOfEncounterTime, hourOfEncTransportationTime, minuteOfEncTransportationTime
		 * 
		 */
		
		// Save annotation 
		CaseManagementNote annotationNote = null;// (CaseManagementNote) session.getAttribute(attrib_name);
		//logger.error(noteIssue.getAnnotation_attrib());
		
		//String ongoing = null; // figure out this
		String lastSavedNoteString = null;
		String user = loggedInInfo.getLoggedInProvider().getProviderNo();
		String remoteAddr = 	""; // Not sure how to get this	
		
		//caseMangementNote = caseManagementMgr.saveCaseManagementNote(caseMangementNote,issuelist, cpp, ongoing,verify, loggedInInfo.getLocale(),now,annotationNote,userName,user,remoteAddr, lastSavedNoteString) ;
		
		String savedStr = caseManagementMgr.saveNote(cpp, caseMangementNote, providerNo, userName, null, note.getRoleName());
		caseManagementMgr.saveCPP(cpp, providerNo);
		
		caseManagementMgr.getEditors(caseMangementNote);
		
		
		note.setNoteId(Integer.parseInt(""+caseMangementNote.getId()));
		note.setUuid(caseMangementNote.getUuid());
		note.setUpdateDate(caseMangementNote.getUpdate_date());
		note.setObservationDate(caseMangementNote.getObservation_date());
		logger.debug("note should return like this " + note.getNote() );
		
		
		long newNoteId =  Long.valueOf(note.getNoteId());
		
		logger.debug("ISSUES LIST START for note " + newNoteId);
		CaseManagementIssueNotesDao cmeIssueNotesDao = (CaseManagementIssueNotesDao) SpringUtils.getBean("caseManagementIssueNotesDao");
		List<CaseManagementIssue> issuesList = cmeIssueNotesDao.getNoteIssues(note.getNoteId());
		for (CaseManagementIssue issueItem : issuesList) {
			logger.debug("ISSUES LIST " + issueItem + " for note " + newNoteId);
		}
		
		if(note.getNoteId()!=0){
			caseManagementMgr.addNewNoteLink(newNoteId);
		}
			
		/* save extra fields */
		CaseManagementNoteExt cme = new CaseManagementNoteExt();
		
		if(noteExt.getStartDate()!=null){
			cme.setNoteId(newNoteId);
			cme.setKeyVal(NoteExtTo1.STARTDATE);	
			cme.setDateValue(noteExt.getStartDate());
			caseManagementMgr.saveNoteExt(cme);
		}
		
		if(noteExt.getResolutionDate()!=null){
			cme.setNoteId(newNoteId);
			cme.setKeyVal(NoteExtTo1.RESOLUTIONDATE);	
			cme.setDateValue(noteExt.getResolutionDate());
			caseManagementMgr.saveNoteExt(cme);
		}
		
		if(noteExt.getProcedureDate()!=null){
			cme.setNoteId(newNoteId);
			cme.setKeyVal(NoteExtTo1.PROCEDUREDATE);
			cme.setDateValue(noteExt.getProcedureDate());
			caseManagementMgr.saveNoteExt(cme);
		}
			
		if(noteExt.getAgeAtOnset()!=null){
			cme.setNoteId(newNoteId);
			cme.setKeyVal(NoteExtTo1.AGEATONSET);
			cme.setDateValue((Date) null);
			cme.setValue(noteExt.getAgeAtOnset());
			caseManagementMgr.saveNoteExt(cme);
		}
		
		if(noteExt.getTreatment()!=null){
			cme.setNoteId(newNoteId);
			cme.setKeyVal(NoteExtTo1.TREATMENT);
			cme.setDateValue((Date) null);
			cme.setValue(noteExt.getTreatment());
			caseManagementMgr.saveNoteExt(cme);
		}
		
		if(noteExt.getProblemStatus()!=null){
			cme.setNoteId(newNoteId);
			cme.setKeyVal(NoteExtTo1.PROBLEMSTATUS);
			cme.setDateValue((Date) null);
			cme.setValue(noteExt.getProblemStatus());
			caseManagementMgr.saveNoteExt(cme);
		}
		
		if(noteExt.getExposureDetail()!=null){
			cme.setNoteId(newNoteId);
			cme.setKeyVal(NoteExtTo1.EXPOSUREDETAIL);
			cme.setDateValue((Date) null);
			cme.setValue(noteExt.getExposureDetail());
			caseManagementMgr.saveNoteExt(cme);
		}
		
		if(noteExt.getRelationship()!=null){
			cme.setNoteId(newNoteId);
			cme.setKeyVal(NoteExtTo1.RELATIONSHIP);
			cme.setDateValue((Date) null);
			cme.setValue(noteExt.getRelationship());
			caseManagementMgr.saveNoteExt(cme);
		}
			
		if(noteExt.getLifeStage()!=null){
			cme.setNoteId(newNoteId);
			cme.setKeyVal(NoteExtTo1.LIFESTAGE);
			cme.setDateValue((Date) null);
			cme.setValue(noteExt.getLifeStage());
			caseManagementMgr.saveNoteExt(cme);			
		}
		
		if(noteExt.getHideCpp()!=null){
			cme.setNoteId(newNoteId);
			cme.setKeyVal(NoteExtTo1.HIDECPP);
			cme.setDateValue((Date) null);
			cme.setValue(noteExt.getHideCpp());
			caseManagementMgr.saveNoteExt(cme);			
		}
		
		if(noteExt.getProblemDesc()!=null){
			cme.setNoteId(newNoteId);
			cme.setKeyVal(NoteExtTo1.PROBLEMDESC);
			cme.setDateValue((Date) null);
			cme.setValue(noteExt.getProblemDesc());
			caseManagementMgr.saveNoteExt(cme);
		}
		
		/* save extra fields */				
		noteIssue.setEncounterNote(note);	
		noteIssue.setGroupNoteExt(noteExt);
		
		return noteIssue;
	}
	
	
	
	protected CaseManagementCPP copyNote2cpp(CaseManagementCPP cpp, String note, String code) {
		//TODO: change this back to a loop
		StringBuilder text = new StringBuilder();
		Date d = new Date();
		String separator = "\n-----[[" + d + "]]-----\n";
	
			if (code.equals("othermeds")) {
				text.append(cpp.getFamilyHistory());
				text.append(separator);
				text.append(note);
				cpp.setFamilyHistory(text.toString());
				
			} else if (code.equals("sochx")) {
				text.append(cpp.getSocialHistory());
				text.append(separator);
				text.append(note);
				cpp.setSocialHistory(text.toString());
				
			} else if (code.equals("medhx")) {
				text.append(cpp.getMedicalHistory());
				text.append(separator);
				text.append(note);
				cpp.setMedicalHistory(text.toString());
				
			} else if (code.equals("ongoingconcerns")) {
				text.append(cpp.getOngoingConcerns());
				text.append(separator);
				text.append(note);
				cpp.setOngoingConcerns(text.toString());
				
			} else if (code.equals("reminders")) {
				text.append(cpp.getReminders());
				text.append(separator);
				text.append(note);
				cpp.setReminders(text.toString());
				
			} else if (code.equals("famhx")) {
				text.append(cpp.getFamilyHistory());
				text.append(separator);
				text.append(note);
				cpp.setFamilyHistory(text.toString());
				
			} else if (code.equals("riskfactors")) {
				text.append(cpp.getRiskFactors());
				text.append(separator);
				text.append(note);
				cpp.setRiskFactors(text.toString());
				
			}
		

		return cpp;
	}
	
	
	
	private String getString(JSONObject jsonobject,String key){
		if(jsonobject.containsKey(key)){
			return jsonobject.getString(key); 
		}
		return null;
	}
	
	@POST
	@Path("/{demographicNo}/getCurrentNote")
	@Consumes("application/json")
	@Produces("application/json")
	public NoteTo1 getCurrentNote(@PathParam("demographicNo") Integer demographicNo ,JSONObject jsonobject){
		logger.debug("getCurrentNote "+jsonobject);
		LoggedInInfo loggedInInfo =  getLoggedInInfo(); //LoggedInInfo.loggedInInfo.get();

		String providerNo=loggedInInfo.getLoggedInProviderNo();

		
		HttpSession session = loggedInInfo.getSession();
		if (session.getAttribute("userrole") == null) {
//			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}

//		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
//		cform.setChain("");
//		request.setAttribute("change_flag", "false");
//		request.setAttribute("from", "casemgmt");

		

		String programIdString = getProgram(loggedInInfo,providerNo);
		Integer programId = null;
		try {
			programId = Integer.parseInt(programIdString);
		} catch (Exception e) {
			logger.warn("Error parsing programId:" + programIdString, e);
		}

///////Not sure what this is about??		
//		/* process the request from other module */
//		if (!"casemgmt".equalsIgnoreCase(request.getParameter("from"))) {
//
//			// no demographic number, no page
//			if (request.getParameter("demographicNo") == null || "".equals(request.getParameter("demographicNo"))) {
//				return mapping.findForward("NoDemoERR");
//			}
//			request.setAttribute("from", "");
//		}


		

		CaseManagementNote note = null;

		String nId = getString(jsonobject,"noteId");// request.getParameter("noteId");
		String forceNote = getString(jsonobject,"forceNote");//request.getParameter("forceNote");
		if (forceNote == null) forceNote = "false";

		logger.debug("NoteId " + nId);

		CaseManagementTmpSave tmpsavenote = this.caseManagementMgr.restoreTmpSave(providerNo, ""+demographicNo, programIdString);
		

		logger.debug("Get Note for editing");
		String strBeanName = "casemgmt_oscar_bean" + demographicNo;
		EctSessionBean bean = (EctSessionBean) loggedInInfo.getSession().getAttribute(strBeanName);
		String encType = getString(jsonobject,"encType");
		
		logger.debug("Encounter Type : "+encType);
		
		

		// create a new note
		if (getString(jsonobject,"note_edit") != null && getString(jsonobject,"note_edit").equals("new")) {
			logger.debug("NEW NOTE GENERATED");
//			request.setAttribute("newNoteIdx", request.getParameter("newNoteIdx"));

			note = new CaseManagementNote();
			note.setProviderNo(providerNo);
			Provider prov = new Provider();
			prov.setProviderNo(providerNo);
			note.setProvider(prov);
			note.setDemographic_no(""+demographicNo);

//////This adds the note text i think
//			if (!OscarProperties.getInstance().isPropertyActive("encounter.empty_new_note")) {
//				this.insertReason(request, note);
//			} else {
//				note.setNote("");
//				note.setEncounter_type("");
//			}

			

			if (encType == null || encType.equals("")) {
				note.setEncounter_type("");
			} else {
				note.setEncounter_type(encType);
			}
			if (bean.encType != null && bean.encType.length() > 0) {
				note.setEncounter_type(bean.encType);
			}

//			resetTemp(providerNo, ""+demographicNo, programIdString);

		}
		// get the last temp note?
		else if (tmpsavenote != null && !forceNote.equals("true")) {
			logger.debug("tempsavenote is NOT NULL == noteId :"+tmpsavenote.getNoteId());
			if (tmpsavenote.getNoteId() > 0) {
//				session.setAttribute("newNote", "false");
				note = caseManagementMgr.getNote(String.valueOf(tmpsavenote.getNoteId()));
				logger.debug("Restoring " + String.valueOf(note.getId()));
			} else {
				logger.debug("creating new note");
//				session.setAttribute("newNote", "true");
//				session.setAttribute("issueStatusChanged", "false");
				note = new CaseManagementNote();
				note.setProviderNo(providerNo);
				Provider prov = new Provider();
				prov.setProviderNo(providerNo);
				note.setProvider(prov);
				note.setDemographic_no(""+demographicNo);
			}
			
			note.setNote(tmpsavenote.getNote());
			logger.debug("Setting note to " + note.getNote());

		}
		// get an existing non-temp note?
		else if (nId != null && Integer.parseInt(nId) > 0) {
			logger.debug("Using nId " + nId + " to fetch note");
//			session.setAttribute("newNote", "false");
			note = caseManagementMgr.getNote(nId);

			if (note.getHistory() == null || note.getHistory().equals("")) {
				// old note - we need to save the original in here
				note.setHistory(note.getNote());

				caseManagementMgr.saveNoteSimple(note);
//				addNewNoteLink(Long.parseLong(nId));
			}

		}
		// no note specified, get last unsigned
		else {
			logger.debug("in empty else");
			// A hack to load last unsigned note when not specifying a particular note to edit
			// if there is no unsigned note load a new one
			
			Map unlockedNotesMap = null; //NEED THIS ??
			if ((note = caseManagementMgr.getLastSaved(""+programId, ""+demographicNo, providerNo,unlockedNotesMap)) == null) {
//				session.setAttribute("newNote", "true");
//				//session.setAttribute("issueStatusChanged", "false");
			
				//String encType 
				String apptDate = getString(jsonobject,"apptDate");
				String reason = getString(jsonobject,"reason");
				String appointmentNo = getString(jsonobject,"appointmentNo");
				note = caseManagementMgr.makeNewNote(providerNo, ""+demographicNo, encType, appointmentNo,loggedInInfo.getLocale());
				//note = caseManagementMgr.makeNewNote(providerNo, ""+demographicNo, bean, encType, apptDate, reason,loggedInInfo.locale);
//				note = this.makeNewNote(providerNo, demono, request);				
			}
		}
		

		/*
		 * do the restore if(restore != null && restore.booleanValue() == true) { String tmpsavenote = this.caseManagementMgr.restoreTmpSave(providerNo,demono,programId); if(tmpsavenote != null) { note.setNote(tmpsavenote); } }
		 */
		logger.debug("note ?" +note);
		logger.debug("Set Encounter Type: " + note.getEncounter_type());
		logger.debug("Fetched Note " + String.valueOf(note.getId()));

		logger.debug("Populate Note with editors");
		this.caseManagementMgr.getEditors(note);
		

		// put the new/retrieved not in the form object for rendering on page
		/* set issue checked list */

		// get issues for current demographic, based on provider rights


//		cform.setSign("off");
//		if (!note.isIncludeissue()) cform.setIncludeIssue("off");
//		else cform.setIncludeIssue("on");

//		boolean passwd = caseManagementMgr.getEnabled();
//		String chain = request.getParameter("chain");

		

//		LogAction.addLog((String) session.getAttribute("user"), LogConst.EDIT, LogConst.CON_CME_NOTE, String.valueOf(note.getId()), request.getRemoteAddr(), demono, note.getAuditString());

		//check to see if someone else is editing note in this chart
//		String ipAddress = request.getRemoteAddr();
//		CasemgmtNoteLock casemgmtNoteLock;
//		Long note_id = note.getId() != null && note.getId() >= 0 ? note.getId() : 0L;
//		casemgmtNoteLock = isNoteEdited(note_id, demographicNo, providerNo, ipAddress, request.getRequestedSessionId());
		
//		if( casemgmtNoteLock.isLocked() ) {
//			note = makeNewNote(providerNo, demono, request);
//			cform.setCaseNote(note);
//		}
		
//		session.setAttribute("casemgmtNoteLock"+demono, casemgmtNoteLock);		
		
		

		/*
		 ///Is it a specific thats being requested to edit

	      //YES  -- > load that note

	      //NO
	            //check to see if a note is in tmp-save? 
	                  //YES -->> load that tmp save note
	                  //NO 

	                        //Is there an unsigned note?
	                                //YES -->> load that unsigned save note
	                                //NO
	                                   //Is it a new note? What type?  -->> load the new note (ie visit note, tele note etc)
		 */
		
		NoteTo1 returnNote = new NoteTo1();
		
		NoteDisplay nd = new NoteDisplayLocal(loggedInInfo,note);
		
		returnNote.setNoteId(nd.getNoteId());
		
		returnNote.setIsSigned(nd.isSigned());
		returnNote.setIsEditable(nd.isEditable());
		returnNote.setObservationDate(nd.getObservationDate());
		returnNote.setRevision(nd.getRevision());
		returnNote.setUpdateDate(nd.getUpdateDate());
		returnNote.setProviderName(nd.getProviderName());
		returnNote.setProviderNo(nd.getProviderNo());
		returnNote.setStatus(nd.getStatus());
		returnNote.setProgramName(nd.getProgramName());
		returnNote.setLocation(nd.getLocation());
		returnNote.setRoleName(nd.getRoleName());
		returnNote.setRemoteFacilityId(nd.getRemoteFacilityId());
		returnNote.setUuid(nd.getUuid());
		returnNote.setHasHistory(nd.getHasHistory());
		returnNote.setLocked(nd.isLocked());
		returnNote.setNote(nd.getNote());
		returnNote.setDocument(nd.isDocument());
		returnNote.setRxAnnotation(nd.isRxAnnotation());
		returnNote.setEformData(nd.isEformData());
		returnNote.setEncounterForm(nd.isEncounterForm());
		returnNote.setInvoice(nd.isInvoice());
		returnNote.setTicklerNote(nd.isTicklerNote());
		returnNote.setEncounterType(nd.getEncounterType());
		returnNote.setEditorNames(nd.getEditorNames());
		returnNote.setIssueDescriptions(nd.getIssueDescriptions());
		returnNote.setReadOnly(nd.isReadOnly());
		returnNote.setGroupNote(nd.isGroupNote());
		returnNote.setCpp(nd.isCpp());
		returnNote.setEncounterTime(nd.getEncounterTime());	
		returnNote.setEncounterTransportationTime(nd.getEncounterTransportationTime());
		returnNote.setAppointmentNo(nd.getAppointmentNo());
		
		return returnNote;
	}
	
	
	private void processJsonArray( JSONObject jsonobject, String key, List<String> list){
		if( jsonobject != null && jsonobject.containsKey(key)){
			JSONArray arr = jsonobject.getJSONArray(key);
			for(int i =0; i < arr.size();i++){
				list.add(arr.getString(i));
			}
		}
	 
	}
	
	@POST
	@Path("/getIssueNote/{noteId}")	
	@Produces("application/json")
	public NoteIssueTo1 getIssueNote(@PathParam("noteId") Integer noteId){
		
		
		//get all note values NoteDisplay nd = new NoteDisplayLocal(loggedInInfo,note);
		CaseManagementNote casemgmtNote = null;
		casemgmtNote = caseManagementMgr.getNote(String.valueOf(noteId));
		
		NoteTo1 note = new NoteTo1();
		note.setNoteId(noteId);
		note.setIsSigned(casemgmtNote.isSigned());
		note.setRevision(casemgmtNote.getRevision());
		note.setUpdateDate(casemgmtNote.getUpdate_date());
		note.setProviderName(casemgmtNote.getProviderName());
		note.setProviderNo(casemgmtNote.getProviderNo());
		note.setStatus(casemgmtNote.getStatus());
		note.setProgramName(casemgmtNote.getProgramName());
		note.setRoleName(casemgmtNote.getRoleName());
		note.setUuid(casemgmtNote.getUuid());
		note.setHasHistory(casemgmtNote.getHasHistory());
		note.setLocked(casemgmtNote.isLocked());
		note.setNote(casemgmtNote.getNote());
		note.setRxAnnotation(casemgmtNote.isRxAnnotation());
		note.setEncounterType(casemgmtNote.getEncounter_type());
		//note.setEditorNames(casemgmtNote.getEditors());		
		//note.setIssueDescriptions(casemgmtNote.get);
		note.setPosition(casemgmtNote.getPosition());
		
		//note.getIssueDescriptions(casemgmtNote.getIssues());
		note.setAppointmentNo(casemgmtNote.getAppointmentNo());	
		
		
		//get all note extra values	
		List<CaseManagementNoteExt> lcme = new ArrayList<CaseManagementNoteExt>();
		lcme.addAll(caseManagementMgr.getExtByNote( Long.valueOf(noteId) ));

		NoteExtTo1 noteExt = new NoteExtTo1();
		noteExt.setNoteId( Long.valueOf(noteId) );
		
		for(CaseManagementNoteExt l : lcme){
			logger.debug("NOTE EXT KEY:" +l.getKeyVal() + l.getValue());
			
			if(l.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)){
				noteExt.setStartDate(l.getDateValueStr());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)){
				noteExt.setResolutionDate(l.getDateValueStr());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.PROCEDUREDATE)){
				noteExt.setProcedureDate(l.getDateValueStr());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.AGEATONSET)){
				noteExt.setAgeAtOnset(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.TREATMENT)){
				noteExt.setTreatment(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.PROBLEMSTATUS)){
				noteExt.setProblemStatus(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.EXPOSUREDETAIL)){
				noteExt.setExposureDetail(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.RELATIONSHIP)){
				noteExt.setRelationship(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.LIFESTAGE)){
				noteExt.setLifeStage(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.HIDECPP)){
				noteExt.setHideCpp(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.PROBLEMDESC)){
				noteExt.setProblemDesc(l.getValue());
			}
			
		}
		
		//assigned issues..remove the CPP one.
		List<CaseManagementIssue> rawCmeIssues = new ArrayList<CaseManagementIssue>(casemgmtNote.getIssues());
		List<CaseManagementIssue> cmeIssues = new ArrayList<CaseManagementIssue>();
		
		for(CaseManagementIssue cmei:rawCmeIssues) {
			if(!isCppCode(cmei)) {
				cmeIssues.add(cmei);
			}
		}
		
		//set NoteIssue to return
		NoteIssueTo1 noteIssue = new NoteIssueTo1();
		noteIssue.setEncounterNote(note);	
		noteIssue.setGroupNoteExt(noteExt);	
		noteIssue.setAssignedCMIssues(new CaseManagementIssueConverter().getAllAsTransferObjects(getLoggedInInfo(), cmeIssues));
		
		return noteIssue;
		
	}
	
	private boolean isCppCode(CaseManagementIssue cmeIssue) {
		return Arrays.asList(cppCodes).contains(cmeIssue.getIssue().getCode());
	}
	
	@POST
	@Path("/getGroupNoteExt/{noteId}")	
	@Produces("application/json")
	public NoteExtTo1 getGroupNoteExt(@PathParam("noteId") Long noteId){
		
		List<CaseManagementNoteExt> lcme = new ArrayList<CaseManagementNoteExt>();
		lcme.addAll(caseManagementMgr.getExtByNote(noteId));

		NoteExtTo1 noteExt = new NoteExtTo1();
		noteExt.setNoteId(noteId);
		
		for(CaseManagementNoteExt l : lcme){
			logger.debug("NOTE EXT KEY:" +l.getKeyVal() + l.getValue());
			
			if(l.getKeyVal().equals(CaseManagementNoteExt.STARTDATE)){
				noteExt.setStartDate(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.RESOLUTIONDATE)){
				noteExt.setResolutionDate(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.PROCEDUREDATE)){
				noteExt.setProcedureDate(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.AGEATONSET)){
				noteExt.setAgeAtOnset(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.TREATMENT)){
				noteExt.setTreatment(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.PROBLEMSTATUS)){
				noteExt.setProblemStatus(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.EXPOSUREDETAIL)){
				noteExt.setExposureDetail(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.RELATIONSHIP)){
				noteExt.setRelationship(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.LIFESTAGE)){
				noteExt.setLifeStage(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.HIDECPP)){
				noteExt.setHideCpp(l.getValue());
			}else if(l.getKeyVal().equals(CaseManagementNoteExt.PROBLEMDESC)){
				noteExt.setProblemDesc(l.getValue());
			}
			
		}
		
		return noteExt;
	}
	
	//TODO
	@POST
	@Path("/getIssueId/{issueCode}")	
	@Produces("application/json")
	public IssueTo1 getIssueId(@PathParam("issueCode") String issueCode){
		
		//translate summary codes
		if("ongoingconcerns".equals(issueCode)){
			issueCode = "Concerns";
		}else if("medhx".equals(issueCode)){
			issueCode = "MedHistory";
		}else if("reminders".equals(issueCode)){
			issueCode = "Reminders"; 
		}else if("othermeds".equals(issueCode)){
			issueCode = "OMeds";
		}else if("sochx".equals(issueCode)){
			issueCode = "SocHistory";
		}else if("famhx".equals(issueCode)){
			issueCode = "FamHistory";
		}else if("riskfactors".equals(issueCode)){
			issueCode = "RiskFactors";
		}
		
		Issue issues = caseManagementMgr.getIssueInfoByCode(issueCode);
		
		IssueTo1 issueId = new IssueTo1();
		issueId.setId(issues.getId());
		
		return issueId;
	}
	
	@POST
	@Path("/getIssueById/{issueId}")	
	@Produces("application/json")
	public IssueTo1 getIssueId(@PathParam("issueId") int issueId){
		
		Issue issue = caseManagementMgr.getIssue(String.valueOf(issueId));
		
		IssueTo1 issueTo = new IssueConverter().getAsTransferObject(getLoggedInInfo(), issue);
		
		return issueTo;
	}

	@GET
	@Path("/ticklerGetNote/{ticklerNo}")
	@Produces("application/json")
	//{"ticklerNote":{"editor":"oscardoc, doctor","note":"note 2","noteId":6,"observationDate":"2014-09-13T13:18:41-04:00","revision":2}}
	public TicklerNoteResponse ticklerGetNote(@PathParam("ticklerNo") Integer ticklerNo ){

		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_tickler", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_eChart", "r", null)) {
			throw new RuntimeException("Access Denied");
		}
		
		TicklerNoteResponse response = new TicklerNoteResponse();
		CaseManagementNoteLink link = caseManagementMgr.getLatestLinkByTableId(CaseManagementNoteLink.TICKLER, Long.valueOf(ticklerNo));
		
		if(link != null) {
			Long noteId = link.getNoteId();
			
			CaseManagementNote note = caseManagementMgr.getNote(noteId.toString());
			
			if(note != null) {
				TicklerNoteTo1 tNote = new TicklerNoteTo1();
				tNote.setNoteId(note.getId().intValue());
				tNote.setNote(note.getNote());
				tNote.setRevision(note.getRevision());
				tNote.setObservationDate(note.getObservation_date());
				tNote.setEditor(providerMgr.getProvider(note.getProviderNo()).getFormattedName());
				response.setTicklerNote(tNote);
			}
		}
		return response;
	}
	
	@POST
	@Path("/ticklerSaveNote")
	@Produces("application/json")
	@Consumes("application/json")
	public GenericRESTResponse ticklerSaveNote(JSONObject json){
		
		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_tickler", "w", null)) {
			throw new RuntimeException("Access Denied");
		}
		if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_eChart", "w", null)) {
			throw new RuntimeException("Access Denied");
		}

		logger.info("The config "+json.toString());
		
		String strNote = json.getString("note");
		Integer noteId = json.getInt("noteId");
		
		logger.info("want to save note id " + noteId + " with value " + strNote);
		
		JSONObject tickler = json.getJSONObject("tickler");
		Integer ticklerId = tickler.getInt("id");
		Integer demographicNo = tickler.getInt("demographicNo");
		
		logger.info("tickler id " + ticklerId + ", demographicNo " + demographicNo);
		
		Date creationDate = new Date();
		LoggedInInfo loggedInInfo=this.getLoggedInInfo();
		Provider loggedInProvider = loggedInInfo.getLoggedInProvider();
		
		
		String revision = "1";
		String history = strNote;
		String uuid = null;
		
		if(noteId != null  && noteId.intValue()>0) {
			CaseManagementNote existingNote = caseManagementMgr.getNote(String.valueOf(noteId));
			
			revision = String.valueOf(Integer.valueOf(existingNote.getRevision()).intValue() + 1);
			history = strNote + "\n" + existingNote.getHistory();
			uuid = existingNote.getUuid();
		}
		
		CaseManagementNote cmn = new CaseManagementNote();
		cmn.setAppointmentNo(0);
		cmn.setArchived(false);
		cmn.setCreate_date(creationDate);
		cmn.setDemographic_no(String.valueOf(demographicNo));
		cmn.setEncounter_type(EncounterUtil.EncounterType.FACE_TO_FACE_WITH_CLIENT.getOldDbValue());
		cmn.setNote(strNote);
		cmn.setObservation_date(creationDate);
		
		cmn.setProviderNo(loggedInProvider.getProviderNo());
		cmn.setRevision(revision);
		cmn.setSigned(true);
		cmn.setSigning_provider_no(loggedInProvider.getProviderNo());
		cmn.setUpdate_date(creationDate);
		cmn.setHistory(history);
		//just doing this because the other code does it.
		cmn.setReporter_program_team("null");
		cmn.setUuid(uuid);
		
		
		ProgramProvider pp = programManager2.getCurrentProgramInDomain(getLoggedInInfo(),getLoggedInInfo().getLoggedInProviderNo());
		if(pp != null) {
			cmn.setProgram_no(String.valueOf(pp.getProgramId()));
		} else {
			List<ProgramProvider> ppList = programManager2.getProgramDomain(getLoggedInInfo(),getLoggedInInfo().getLoggedInProviderNo());
			if(ppList != null && ppList.size()>0) {
				cmn.setProgram_no(String.valueOf(ppList.get(0).getProgramId()));
			}
			
		}
		
		//weird place for it , but for now.
		CaseManagementEntryAction.determineNoteRole(cmn,loggedInProvider.getProviderNo(),String.valueOf(demographicNo));
		
		caseManagementMgr.saveNoteSimple(cmn);

		logger.info("note id is " + cmn.getId());
		
		
		//save link, so we know what tickler this note is linked to
		CaseManagementNoteLink link = new CaseManagementNoteLink();
		link.setNoteId(cmn.getId());
		link.setTableId(ticklerId.longValue());
		link.setTableName(CaseManagementNoteLink.TICKLER);
		
		CaseManagementNoteLinkDAO caseManagementNoteLinkDao = (CaseManagementNoteLinkDAO) SpringUtils.getBean("CaseManagementNoteLinkDAO");
		caseManagementNoteLinkDao.save(link);
		
		
		
		Issue issue = this.issueDao.findIssueByTypeAndCode("system", "TicklerNote");
		if(issue == null) {
			logger.warn("missing TicklerNote issue, please run all database updates");
			return null;
		}
		
		CaseManagementIssue cmi = caseManagementMgr.getIssueById(demographicNo.toString(), issue.getId().toString());
		
		if(cmi == null) {
		//save issue..this will make it a "cpp looking" issue in the eChart
			cmi = new CaseManagementIssue();
			cmi.setAcute(false);
			cmi.setCertain(false);
			cmi.setDemographic_no(demographicNo);
			cmi.setIssue_id(issue.getId());
			cmi.setMajor(false);
			cmi.setProgram_id(Integer.parseInt(cmn.getProgram_no()));
			cmi.setResolved(false);
			cmi.setType(issue.getRole());
			cmi.setUpdate_date(creationDate);
			
			caseManagementMgr.saveCaseIssue(cmi);
			
		}

		cmn.getIssues().add(cmi);
		caseManagementMgr.updateNote(cmn);
		
		 
		
		return new GenericRESTResponse();
	}

	
	@POST
	@Path("/searchIssues")
	@Produces("application/json")
	@Consumes("application/json")
	public AbstractSearchResponse<IssueTo1> search(JSONObject json,@QueryParam("startIndex") Integer startIndex,@QueryParam("itemsToReturn") Integer itemsToReturn ) {
		AbstractSearchResponse<IssueTo1> response = new AbstractSearchResponse<IssueTo1>();
		
		//if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_demographic", "r", null)) {
		//	throw new RuntimeException("Access Denied");
		//}
		
		String term = json.getString("term");
		
		if(json.getString("term").length() >= 1) {
		
			ProgramProvider pp = programManager2.getCurrentProgramInDomain(getLoggedInInfo(),getLoggedInInfo().getLoggedInProviderNo());
			
			CaseManagementManager caseManagementManager = SpringUtils.getBean(CaseManagementManager.class);
			
			//change to get count, and get the slice
			Integer issuesCount = caseManagementManager.searchIssuesCount(getLoggedInInfo().getLoggedInProviderNo(), (pp!=null)?String.valueOf(pp.getProgramId()):null, term);
			
			List<Issue> issues = caseManagementManager.searchIssues(getLoggedInInfo().getLoggedInProviderNo(), (pp!=null)?String.valueOf(pp.getProgramId()):null, term, startIndex, itemsToReturn);
			
			List<IssueTo1> results = new IssueConverter().getAllAsTransferObjects(getLoggedInInfo(), issues);
			
			response.setContent(results);
			response.setTotal(issuesCount);
		}
				
		
		return response;
	}
	
	
	@POST
	@Path("/setEditingNoteFlag")
	@Produces("application/json")
	public GenericRESTResponse setEditingNoteFlag(@QueryParam("noteUUID") String noteUUID, @QueryParam("userId") String providerNo) {
		GenericRESTResponse resp = new GenericRESTResponse(false, "Parameter error");
		if (noteUUID==null || noteUUID.trim().isEmpty() || providerNo==null || providerNo.trim().isEmpty()) return resp;
		
		ConcurrentHashMap<String, Long> noteList = editList.get(noteUUID);
		if (noteList==null) {
			noteList = new ConcurrentHashMap<String, Long>();
			editList.put(noteUUID, noteList);
		}
		clearDanglingFlags();
		
		resp.setSuccess(true);
		resp.setMessage(null);
		
		if (!noteList.containsKey(providerNo)) { // only check for other editing user when initializing flag
			for (String key : noteList.keySet()) {
				if (key!=providerNo) {
					resp.setSuccess(false);
					break;
				}
			}
		}
		noteList.put(providerNo, new Date().getTime());
		editList.put(noteUUID, noteList);
		return resp;
	}
	
	private void clearDanglingFlags() {
		long now = new Date().getTime();
		String[] noteUUIDs = editList.keySet().toArray(new String[editList.keySet().size()]);
		for (String uuid : noteUUIDs) {
			ConcurrentHashMap<String, Long> noteList = editList.get(uuid);
			String[] providerNos = noteList.keySet().toArray(new String[noteList.keySet().size()]);
			for (String providerNo : providerNos) {
				Long editTime = noteList.get(providerNo);
				if (now-editTime>=360000) noteList.remove(providerNo); //remove flag due 6 min (should be renewed/removed within 5 min)
			}
			if (noteList.isEmpty()) editList.remove(uuid);
			else editList.put(uuid, noteList);
		}
	}
	

	@POST
	@Path("/checkEditNoteNew")
	@Produces("application/json")
	public GenericRESTResponse checkEditNoteNew(@QueryParam("noteUUID") String noteUUID, @QueryParam("userId") String providerNo) {
		GenericRESTResponse resp = new GenericRESTResponse(true, null);
		if (noteUUID==null || noteUUID.trim().isEmpty() || providerNo==null || providerNo.trim().isEmpty()) return resp;
		
		ConcurrentHashMap<String, Long> noteList = editList.get(noteUUID);
		if (noteList==null) return resp;
		if (noteList.size()==1 && noteList.containsKey(providerNo)) return resp;
		
		long myEditTime = 0;
		if (noteList.containsKey(providerNo)) myEditTime = noteList.get(providerNo);
		for (String key : noteList.keySet()) {
			if (key!=providerNo) {
				if (noteList.get(key)>myEditTime) {
					resp.setSuccess(false);
					break;
				}
			}
		}
		return resp;  //true = no new edit, false = warn about new edit
	}

	@POST
	@Path("/removeEditingNoteFlag")
	public void removeEditingNoteFlag(@QueryParam("noteUUID") String noteUUID, @QueryParam("userId") String providerNo) {
		if (noteUUID==null || noteUUID.trim().isEmpty() || providerNo==null || providerNo.trim().isEmpty()) return;
		
		ConcurrentHashMap<String, Long> noteList = editList.get(noteUUID);
		if (noteList!=null && noteList.containsKey(providerNo)) noteList.remove(providerNo);
		if (noteList.isEmpty()) editList.remove(noteUUID);
		else editList.put(noteUUID, noteList);
	}
}
