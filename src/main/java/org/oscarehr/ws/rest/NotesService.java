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

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.casemgmt.service.NoteSelectionCriteria;
import org.oscarehr.casemgmt.service.NoteSelectionResult;
import org.oscarehr.casemgmt.service.NoteService;
import org.oscarehr.casemgmt.web.NoteDisplay;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.model.NoteSelectionTo1;
import org.oscarehr.ws.rest.to.model.NoteTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import oscar.OscarProperties;


@Path("/notes")
@Component("notesService")
public class NotesService extends AbstractServiceImpl {

	private static Logger logger = MiscUtils.getLogger();
	
	@Autowired
	private NoteService noteService; 
	
	@Autowired
	private ProgramManager2 programManager2;
	
	@Autowired
	private ProgramManager programMgr;
	
	@Autowired
	private CaseManagementManager caseManagementMgr;
	
	
	@POST
	@Path("/{demographicNo}/all")
	@Produces("application/json")
	@Consumes("application/json")
	public NoteSelectionTo1 getNotesWithFilter(@PathParam("demographicNo") Integer demographicNo ,@DefaultValue("20") @QueryParam("numToReturn") Integer numToReturn,@DefaultValue("0") @QueryParam("offset") Integer offset,JSONObject jsonobject){
		NoteSelectionTo1 returnResult = new NoteSelectionTo1();
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		logger.debug("The config "+jsonobject.toString());
	
		HttpSession se = loggedInInfo.session;
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
		} else if (!caseManagementMgr.isClientInProgramDomain(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo(), demoNo) && !caseManagementMgr.isClientReferredInProgramDomain(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo(), demoNo)) {
			logger.error("A domain error needs to be added to the returned result, remove this when fixed");
			return returnResult;
		}
		
		ProgramProvider pp = programManager2.getCurrentProgramInDomain(loggedInInfo.getLoggedInProvider().getProviderNo());
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
	
	
	private void processJsonArray( JSONObject jsonobject, String key, List<String> list){
		if( jsonobject != null && jsonobject.containsKey(key)){
			JSONArray arr = jsonobject.getJSONArray(key);
			for(int i =0; i < arr.size();i++){
				list.add(arr.getString(i));
			}
		}
	 
	}
	
	

}
