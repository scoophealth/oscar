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
package org.oscarehr.casemgmt.service.impl;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNote;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNoteCompositePk;
import org.oscarehr.casemgmt.common.EChartNoteEntry;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.casemgmt.service.NoteSelectionCriteria;
import org.oscarehr.casemgmt.service.NoteSelectionResult;
import org.oscarehr.casemgmt.service.NoteService;
import org.oscarehr.casemgmt.web.NoteDisplay;
import org.oscarehr.casemgmt.web.NoteDisplayIntegrator;
import org.oscarehr.casemgmt.web.NoteDisplayLocal;
import org.oscarehr.casemgmt.web.NoteDisplayNonNote;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.CaseManagementIssueNotesDao;
import org.oscarehr.common.dao.GroupNoteDao;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.GroupNoteLink;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import oscar.eform.EFormUtil;
import oscar.oscarEncounter.data.EctFormData;
import oscar.oscarEncounter.data.EctFormData.PatientForm;
import oscar.util.ConversionUtils;

/**
 * Default implementation of the notes service
 */
@Component
public class DefaultNoteService implements NoteService {

	private static Logger logger = Logger.getLogger(DefaultNoteService.class);

	@Autowired
	@Qualifier("caseManagementNoteDAO")
	private CaseManagementNoteDAO caseManagementNoteDao;

	@Autowired
	private GroupNoteDao groupNoteDao;

	@Autowired
	private BillingONCHeader1Dao billingONCHeader1Dao;

	@Autowired
	private CaseManagementManager caseManagementManager;

	@Autowired
	private CaseManagementIssueNotesDao cmeIssueNotesDao;

	@Autowired
	private ProgramProviderDAO programProviderDAO;
	
	
	@Override
	public NoteSelectionResult findNotes(LoggedInInfo loggedInInfo, NoteSelectionCriteria criteria) {
		logger.debug("LOOKING UP NOTES: " + criteria);

		List<EChartNoteEntry> entries = new ArrayList<EChartNoteEntry>();

		int demographicId = criteria.getDemographicId();
		String demoNo = "" + demographicId;
		long startTime = System.currentTimeMillis();
		long intTime = System.currentTimeMillis();

		//Gets some of the note data, no relationships, not the note/history..just enough
		List<Map<String, Object>> notes = caseManagementNoteDao.getRawNoteInfoMapByDemographic(demoNo);
		Map<String, Object> filteredNotes = new LinkedHashMap<String, Object>();

		//This gets rid of old revisions (better than left join on a computed subset of itself
		for (Map<String, Object> note : notes) {
			if (filteredNotes.get(note.get("uuid")) != null) {
				continue;
			}
			filteredNotes.put((String) note.get("uuid"), true);
			
			EChartNoteEntry e = new EChartNoteEntry();
			e.setId(note.get("id"));
			e.setDate((Date) note.get("observation_date"));
			e.setProviderNo((String) note.get("providerNo"));
			e.setProgramId(ConversionUtils.fromIntString(note.get("program_no")));
			e.setRole((String) note.get("reporter_caisi_role"));
			e.setType("local_note");
			entries.add(e);
		}

		logger.debug("FETCHED " + notes.size() + " NOTE META IN " + (System.currentTimeMillis() - intTime) + "ms");
		intTime = System.currentTimeMillis();

		List<CachedDemographicNote> remoteNotesInfo = getRemoteNoteIds(loggedInInfo, demographicId);
		if (remoteNotesInfo != null) {
			for (CachedDemographicNote note : remoteNotesInfo) {
				EChartNoteEntry e = new EChartNoteEntry();
				e.setId(note.getCachedDemographicNoteCompositePk());
				e.setDate(note.getObservationDate().getTime());
				e.setProviderNo(note.getObservationCaisiProviderId());
				e.setRole(note.getRole());
				e.setType("remote_note");
				entries.add(e);
			}
		}

		if (remoteNotesInfo != null) {
			logger.debug("FETCHED " + remoteNotesInfo.size() + " REMOTE NOTE META IN " + (System.currentTimeMillis() - intTime) + "ms");
		}
		intTime = System.currentTimeMillis();

		List<GroupNoteLink> groupNotesInfo = this.getGroupNoteIds(loggedInInfo, demographicId);
		if (groupNotesInfo != null) {
			for (GroupNoteLink note : groupNotesInfo) {
				EChartNoteEntry e = new EChartNoteEntry();
				e.setId(note.getNoteId());
				e.setDate(note.getCreated());
				//e.setProviderNo(note.get);
				//e.setRoleId(roleId)
				e.setType("group_note");
				entries.add(e);
			}
		}

		if (groupNotesInfo != null) {
			logger.debug("FETCHED " + groupNotesInfo.size() + " GROUP NOTES META IN " + (System.currentTimeMillis() - intTime) + "ms");
		}
		intTime = System.currentTimeMillis();

		String roleName = criteria.getUserRole() + "," + criteria.getUserName();
		ArrayList<HashMap<String, ? extends Object>> eForms = EFormUtil.listPatientEFormsNoData(loggedInInfo, demoNo, roleName);
		for (HashMap<String, ? extends Object> eform : eForms) {
			EChartNoteEntry e = new EChartNoteEntry();
			e.setId(eform.get("fdid"));

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = (String) eform.get("formDate") + " " + (String) eform.get("formTime");
			try {
				e.setDate(sdf.parse(date));
			} catch (ParseException e1) {
				logger.error("Unable to parse date " + date, e1);
			}
			e.setProviderNo((String) eform.get("providerNo"));
			e.setType("eform");
			entries.add(e);
		}

		logger.debug("FETCHED " + eForms.size() + " EFORMS META IN " + (System.currentTimeMillis() - intTime) + "ms");
		intTime = System.currentTimeMillis();

		ArrayList<PatientForm> allPatientForms = EctFormData.getGroupedPatientFormsFromAllTables(demographicId);
		for (PatientForm patientForm : allPatientForms) {
			EChartNoteEntry e = new EChartNoteEntry();
			e.setId(new String[] { patientForm.getFormName(), patientForm.getFormId() });
			SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
			try {
				e.setDate(sdf.parse(patientForm.getEdited()));
			} catch (ParseException e1) {
				logger.error("Unable to parse date" + patientForm.getEdited(), e1);
			}
			e.setType("encounter_form");
			entries.add(e);
		}

		logger.debug("FETCHED " + allPatientForms.size() + " FORMS IN " + (System.currentTimeMillis() - intTime) + "ms");
		intTime = System.currentTimeMillis();

		List<Map<String, Object>> bills = null;
		if (oscar.OscarProperties.getInstance().getProperty("billregion", "").equalsIgnoreCase("ON")) {
			bills = billingONCHeader1Dao.getInvoicesMeta(Integer.parseInt(demoNo));
			bills = filterInvoices(loggedInInfo,bills);
			for (Map<String, Object> h1 : bills) {
				EChartNoteEntry e = new EChartNoteEntry();
				e.setId(h1.get("id"));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = h1.get("billingDate") + " " + h1.get("billing_time");
				try {
					e.setDate(sdf.parse(date));
				} catch (ParseException e1) {
					logger.error("Unable to parse date " + date, e1);
				}
				e.setProviderNo((String) h1.get("provider_no"));
				//e.setProgramId(Integer.parseInt((String)note[3]));
				//e.setRoleId(Integer.parseInt((String)note[4]));
				e.setType("invoice");
				entries.add(e);
			}

			logger.debug("FETCHED " + bills.size() + " INVIOCES META IN " + (System.currentTimeMillis() - intTime) + "ms");
			intTime = System.currentTimeMillis();

		}

		//we now have this huge list
		//sort it by date or whatever
		if (criteria.isNoteSortSpecified()) {
			String sort = criteria.getNoteSort();
			if ("observation_date_desc".equals(sort)) {
				Collections.sort(entries, EChartNoteEntry.getDateComparatorDesc());
			} else if ("observation_date_asc".equals(sort)) {
				Collections.sort(entries, EChartNoteEntry.getDateComparator());
			}
		} else {
			Collections.sort(entries, EChartNoteEntry.getDateComparator());
		}

		logger.debug("SORTED " + entries.size() + " IN " + (System.currentTimeMillis() - intTime) + "ms");
		intTime = System.currentTimeMillis();

		String programId = criteria.getProgramId();

		//apply CAISI permission filter - local notes
		entries = caseManagementManager.filterNotes1(loggedInInfo.getLoggedInProviderNo(), entries, programId);
		logger.debug("FILTER NOTES (CAISI) " + (System.currentTimeMillis() - intTime) + "ms entries size "+entries.size());
		intTime = System.currentTimeMillis();

		//some notes are linkes to private documents, filter those out 
		entries = caseManagementManager.filterNotes2(loggedInInfo, entries);
		logger.debug("FILTER NOTES (CAISI) " + (System.currentTimeMillis() - intTime) + "ms entries size "+entries.size());
		intTime = System.currentTimeMillis();
		
		
		//TODO: role based filter for eforms?

		//apply provider filter
		entries = applyProviderFilter(entries, criteria.getProviders());
		logger.debug("FILTER NOTES PROVIDER " + (System.currentTimeMillis() - intTime) + "ms entries size "+entries.size());
		intTime = System.currentTimeMillis();

		//apply role filter
		entries = applyRoleFilter1(entries, criteria.getRoles());
		logger.debug("FILTER NOTES ROLES " + (System.currentTimeMillis() - intTime) + "ms entries size "+entries.size());
		intTime = System.currentTimeMillis();

		//apply issues filter
		entries = applyIssueFilter1(entries, criteria.getIssues());
		logger.debug("FILTER NOTES ISSUES " + (System.currentTimeMillis() - intTime) + "ms entries size "+entries.size());
		intTime = System.currentTimeMillis();

		NoteSelectionResult result = new NoteSelectionResult();
		
		List<EChartNoteEntry> slice = null;
		if(criteria.isSliceFromEndOfList()){
			slice = sliceFromEndOfList(criteria,entries,result);
		}else{
			slice = sliceFromStartOfList(criteria,entries,result);
		}
		logger.debug("CREATED SLICE OF SIZE  " + slice.size() + " IN " + (System.currentTimeMillis() - intTime) + "ms");
		intTime = System.currentTimeMillis();

		//we now have the slice we want to return
		ArrayList<NoteDisplay> notesToDisplay = new ArrayList<NoteDisplay>();

		if (slice.size() > 0) {
			//figure out what we need to retrieve
			List<Long> localNoteIds = new ArrayList<Long>();
			List<CachedDemographicNoteCompositePk> remoteNoteIds = new ArrayList<CachedDemographicNoteCompositePk>();
			List<Long> groupNoteIds = new ArrayList<Long>();
			List<Integer> invoiceIds = new ArrayList<Integer>();

			for (EChartNoteEntry entry : slice) {
				if (entry.getType().equals("local_note")) {
					localNoteIds.add((Long) entry.getId());
				} else if (entry.getType().equals("remote_note")) {
					remoteNoteIds.add((CachedDemographicNoteCompositePk) entry.getId());
				} else if (entry.getType().equals("invoice")) {
					invoiceIds.add((Integer) entry.getId());
				} else if (entry.getType().equals("group_note")) {
					groupNoteIds.add(((Integer) entry.getId()).longValue());
				}
			}

			List<CaseManagementNote> localNotes = caseManagementNoteDao.getNotes(localNoteIds);

			logger.debug("FETCHED " + localNotes.size() + " NOTES IN " + (System.currentTimeMillis() - intTime) + "ms");
			intTime = System.currentTimeMillis();

			List<CachedDemographicNote> remoteNotes = new ArrayList<CachedDemographicNote>();
			if (remoteNoteIds != null && remoteNoteIds.size() > 0) {
				try {
					remoteNotes = CaisiIntegratorManager.getLinkedNotes(loggedInInfo,remoteNoteIds);
				} catch (MalformedURLException e) {
					logger.error("Unable to load linked notes", e);
				}
			}

			logger.debug("FETCHED " + remoteNotes.size() + " REMOTE NOTES IN " + (System.currentTimeMillis() - intTime) + "ms");
			intTime = System.currentTimeMillis();

			List<CaseManagementNote> groupNotes = caseManagementNoteDao.getNotes(groupNoteIds);

			logger.debug("FETCHED " + groupNotes.size() + " GROUP NOTES IN " + (System.currentTimeMillis() - intTime) + "ms");
			intTime = System.currentTimeMillis();

			List<BillingONCHeader1> invoices = billingONCHeader1Dao.getInvoicesByIds(invoiceIds);

			logger.debug("FETCHED " + invoices.size() + " INVOICES IN " + (System.currentTimeMillis() - intTime) + "ms");
			intTime = System.currentTimeMillis();

			caseManagementManager.getEditors(localNotes);

			for (EChartNoteEntry entry : slice) {
				if (entry.getType().equals("local_note")) {
					notesToDisplay.add(new NoteDisplayLocal(loggedInInfo, findNote((Long) entry.getId(), localNotes)));
				} else if (entry.getType().equals("remote_note")) {
					notesToDisplay.add(new NoteDisplayIntegrator(loggedInInfo, findRemoteNote((CachedDemographicNoteCompositePk) entry.getId(), remoteNotes)));
				} else if (entry.getType().equals("eform")) {
					notesToDisplay.add(new NoteDisplayNonNote(findEform((String) entry.getId(), eForms)));
				} else if (entry.getType().equals("encounter_form")) {
					notesToDisplay.add(new NoteDisplayNonNote(findPatientForm((String[]) entry.getId(), allPatientForms)));
				} else if (entry.getType().equals("invoice")) {
					notesToDisplay.add(new NoteDisplayNonNote(findInvoice((Integer) entry.getId(), invoices)));
				} else if (entry.getType().equals("group_note")) {
					CaseManagementNote note = findNote(((Integer) entry.getId()).longValue(), groupNotes);
					NoteDisplayLocal disp = new NoteDisplayLocal(loggedInInfo, note);
					disp.setReadOnly(true);
					disp.setGroupNote(true);
					disp.setLocation(String.valueOf(note.getDemographic_no()));
					notesToDisplay.add(disp);
				}
			}

		}
		logger.debug("Total Time to load the notes=" + (System.currentTimeMillis() - startTime) + "ms.");
		result.getNotes().addAll(notesToDisplay);
		return result;
	}
	
	private List<Map<String, Object>> filterInvoices(LoggedInInfo loggedInInfo, List<Map<String, Object>> bills) {
		List<Map<String, Object>> filtered = new ArrayList<Map<String, Object>>();
		
		List<ProgramProvider> ppList = programProviderDAO.getProgramDomain(loggedInInfo.getLoggedInProviderNo());
		List<Integer> programIdsUserCanAccess = new ArrayList<Integer>();
		for(ProgramProvider pp:ppList) {
			programIdsUserCanAccess.add(pp.getProgramId().intValue());
		}
		
		for(Map<String, Object> h:bills) {
			Integer programNo = (Integer)h.get("programNo");
			if(programNo != null) {
				if(programIdsUserCanAccess.contains(programNo)) {
					filtered.add(h);
				} else {
					continue;
				}
			} else {
				filtered.add(h);
			}
			
		}
		return filtered;
	}
	
	private static List<EChartNoteEntry> sliceFromStartOfList(NoteSelectionCriteria criteria,List<EChartNoteEntry> entries,NoteSelectionResult result){
		List<EChartNoteEntry> slice = new ArrayList<EChartNoteEntry>();
		int numToReturn = criteria.getMaxResults();
		int offset = criteria.getFirstResult();
		int startingPoint = offset;
		int endingPoint = offset+numToReturn;
		
		if(offset < 0){
			startingPoint = 0;
		}
		
		if(endingPoint > (entries.size())){
			endingPoint = entries.size() ;
		}
		logger.error("number of entries "+entries.size()+" starting "+startingPoint+" endpoint "+endingPoint+" offset "+offset+" numToReturn "+numToReturn);
		for (int x = startingPoint ; x < endingPoint; x++) {
			slice.add(entries.get(x));
		}
		logger.error("slice "+slice.size() +" max size "+ criteria.getMaxResults());
		if(slice.size() == criteria.getMaxResults() ){
			result.setMoreNotes(true);
		}
		return slice;
	}
	
	
	private static List<EChartNoteEntry> sliceFromEndOfList(NoteSelectionCriteria criteria,List<EChartNoteEntry> entries,NoteSelectionResult result){
		List<EChartNoteEntry> slice = new ArrayList<EChartNoteEntry>();
		int numToReturn = criteria.getMaxResults();
		int offset = criteria.getFirstResult();
		
		if (offset <= 0) {
			//this is the first fetch, we want the last items up to numToReturn
			int endOfTheList = entries.size();
			int startingPoint = endOfTheList - numToReturn;
			if (startingPoint < 0) startingPoint = 0;
			for (int x = startingPoint; x < endOfTheList; x++) {
				slice.add(entries.get(x));
			}
		} else {
			if (entries.size() >= offset) {
				int endingPoint = entries.size() - offset;
				int startingPoint = endingPoint - numToReturn;
				if (startingPoint < 0) startingPoint = 0;
				for (int x = startingPoint; x < endingPoint; x++) {
					slice.add(entries.get(x));
				}
			}
			result.setMoreNotes(true);
		}
		return slice;
	}

	
	

	private BillingONCHeader1 findInvoice(Integer id, List<BillingONCHeader1> invoices) {
		for (BillingONCHeader1 invoice : invoices) {
			if (id.equals(invoice.getId())) {
				invoices.remove(invoice);
				return invoice;
			}
		}
		return null;
	}

	private PatientForm findPatientForm(String[] id, List<PatientForm> forms) {
		for (PatientForm form : forms) {
			if (id[0].equals(form.getFormName()) && id[1].equals(form.getFormId())) {
				forms.remove(form);
				return form;
			}
		}
		return null;
	}

	private Map<String, ? extends Object> findEform(String id, ArrayList<HashMap<String, ? extends Object>> eforms) {
		for (HashMap<String, ? extends Object> eform : eforms) {
			if (id.equals(eform.get("fdid"))) {
				eforms.remove(eform);
				return eform;
			}
		}
		return null;
	}

	private List<CachedDemographicNote> getRemoteNoteIds(LoggedInInfo loggedInInfo, int demographicNo) {
		if (loggedInInfo == null) {
			return null;
		}

		if (!loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
			return null;
		}

		List<CachedDemographicNote> linkedNotes = null;
		try {
			if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())) {
				linkedNotes = CaisiIntegratorManager.getLinkedNotesMetaData(loggedInInfo, demographicNo);
			}
		} catch (Exception e) {
			logger.error("Unexpected error.", e);

			CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(),e);
		}

		if (CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())) {
			// TODO: No idea how this works
			// linkedNotes = IntegratorFallBackManager.getLinkedNotes(demographicNo);
		}

		if (linkedNotes == null) {
			return null;
		}

		return linkedNotes;

	}

	private List<GroupNoteLink> getGroupNoteIds(LoggedInInfo loggedInInfo, int demographicNo) {
		if (loggedInInfo == null || !loggedInInfo.getCurrentFacility().isEnableGroupNotes()) {
			return new ArrayList<GroupNoteLink>();
		}

		return groupNoteDao.findLinksByDemographic(demographicNo);
	}

	private List<EChartNoteEntry> applyProviderFilter(List<EChartNoteEntry> notes, List<String> providerNo) {
		boolean skipFilter = false;
		List<EChartNoteEntry> filteredNotes = new ArrayList<EChartNoteEntry>();

		if (providerNo != null && (providerNo.contains("a") || providerNo.isEmpty())) {
			skipFilter = true;
			Collections.sort(providerNo);
		}

		for (Iterator<EChartNoteEntry> iter = notes.iterator(); iter.hasNext();) {
			EChartNoteEntry note = iter.next();
			if (!note.getType().equals("local_note") && !note.getType().equals("eform") && !note.getType().equals("encounter_form") && !note.getType().equals("invoice")) {
				filteredNotes.add(note);
				continue;
			}
			if (skipFilter) {
				filteredNotes.add(note);
			} else {
				if (note.getProviderNo() == null) {
					continue;
				}

				if (providerNo.contains(note.getProviderNo())) {
					filteredNotes.add(note);
				}
			}
		}

		return filteredNotes;
	}

	private List<EChartNoteEntry> applyRoleFilter1(List<EChartNoteEntry> notes, List<String> roleId) {
		if (roleId.isEmpty() || roleId.contains("a")) {
			return notes;
		}
		Collections.sort(roleId);

		List<EChartNoteEntry> filteredNotes = new ArrayList<EChartNoteEntry>();
		for (Iterator<EChartNoteEntry> iter = notes.listIterator(); iter.hasNext();) {
			EChartNoteEntry note = iter.next();
			if (!note.getType().equals("local_note")) {
				filteredNotes.add(note);
				continue;
			}

			if (roleId.contains(note.getRole())) {
				filteredNotes.add(note);
			}
		}

		return filteredNotes;
	}

	private List<EChartNoteEntry> applyIssueFilter1(List<EChartNoteEntry> notes, List<String> issueId) {
		if (issueId.isEmpty() || issueId.contains("a")) {
			return notes;
		}

		List<EChartNoteEntry> filteredNotes = new ArrayList<EChartNoteEntry>();

		List<Integer> noteIds = cmeIssueNotesDao.getNoteIdsWhichHaveIssues(issueId.toArray(new String[] {}));

		//Integer
		if (noteIds != null) {
			for (EChartNoteEntry note : notes) {
				if (!note.getType().equals("local_note")) {
					filteredNotes.add(note);
					continue;
				}
				Integer tmp = ((Long) note.getId()).intValue();
				if (noteIds.contains(tmp)) {
					filteredNotes.add(note);
				}
			}
		}

		return filteredNotes;
	}

	private CaseManagementNote findNote(Long id, List<CaseManagementNote> notes) {
		for (CaseManagementNote note : notes) {
			if (id.equals(note.getId())) {
				notes.remove(note);
				return note;
			}
		}
		return null;
	}

	private CachedDemographicNote findRemoteNote(CachedDemographicNoteCompositePk id, List<CachedDemographicNote> notes) {
		for (CachedDemographicNote note : notes) {
			if (id.getIntegratorFacilityId().equals(note.getCachedDemographicNoteCompositePk().getIntegratorFacilityId()) && id.getUuid().equals(note.getCachedDemographicNoteCompositePk().getUuid())) {
				return note;
			}
		}
		return null;
	}

}
