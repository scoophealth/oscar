package org.oscarehr.casemgmt.service;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class CommunityNoteManager {

	private static Logger log = Logger.getLogger(CommunityNoteManager.class);
	private static CaseManagementManager cMan = (CaseManagementManager)SpringUtils.getBean("caseManagementManager");

	public static List<CaseManagementNote> getRemoteNotes(int demographicId, String programId, List<String> issues) {
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		List<CaseManagementNote> notes = new ArrayList<CaseManagementNote>();

//		List<CachedDemographicIssue> transfers = new ArrayList<CachedDemographicIssue>();
//
//		// get demographic role
//		Role role = cMan.getProviderRole(loggedInInfo.loggedInProvider.getProviderNo(), programId, loggedInInfo.currentFacility.getId());
//		if (role == null || (!role.getName().equals("doctor") && !role.getName().equals("nurse"))) // not a doctor or nurse
//		{
//			log.debug("returning no notes because of role not being a doctor/nurse (" + role.getName() + ")");
//			return notes;
//		}
//
//		for (String issue : issues) {
//			CachedDemographicIssue trans = new CachedDemographicIssue();
//			trans.setDemographicId(demographicId);
//			trans.setFacilityId(loggedInInfo.currentFacility.getId());
//			trans.setIssueCode(issue);
//			transfers.add(trans);
//			log.debug("CachedDemographicIssue created for " + issue);
//		}
//		log.debug("looking for remoteNotes");
//		List<NoteTransfer> remoteNotes = CaisiIntegratorManager.getRemoteNotes(demographicId, transfers);
//		log.debug("found " + remoteNotes.size() + " note transfers");
//
//		for (NoteTransfer rNote : remoteNotes) {
//			CaseManagementNote note = new CaseManagementNote();
//			note.setRemote(true);
//			note.setFacilityName(rNote.getFacilityName());
//			note.setNote(rNote.getNote());
//			note.setEncounter_type(rNote.getEncounterType());
//			note.setProgramName(rNote.getProgramName());
//			note.setObservation_date(rNote.getObservationDate());
//			note.setUpdate_date(rNote.getUpdateDate());
//			note.setLocked(false);
//			note.setRoleName(rNote.getRole());
//
//			Provider provider = new Provider();
//			StringTokenizer toke = new StringTokenizer(rNote.getObservationCaisiProviderName(), "||");
//			if (toke.hasMoreTokens()) {
//				provider.setFirstName(toke.nextToken());
//				provider.setLastName(toke.nextToken());
//			} else {
//				// what to do? this would be problematic
//			}
//			note.setProvider(provider);
//			note.setSigned(rNote.getSigningCaisiProviderName() != null);
//			log.debug("adding note to list");
//			notes.add(note);
//		}
//
		return notes;
	}

}
