package org.oscarehr.casemgmt.service;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.caisi.model.Role;
import org.jboss.logging.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.IssueTransfer;
import org.oscarehr.caisi_integrator.ws.NoteTransfer;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

public class CommunityNoteManager {

	private static Logger log = Logger.getLogger(CommunityNoteManager.class);
	private CaseManagementManager cMan = (CaseManagementManager)SpringUtils.getBean("caseManagementManager");

	public List<CaseManagementNote> getRemoteNotes(int facilityId, int demographicId, String providerNo, String programId, List<String> issues)
	{
		List<CaseManagementNote> notes = new ArrayList<CaseManagementNote>();
		try
		{
			List<IssueTransfer> transfers = new ArrayList<IssueTransfer>();
			
			// get demographic role
			Role role = cMan.getProviderRole(providerNo, programId, facilityId);
			if(role == null || (!role.getName().equals("doctor") && !role.getName().equals("nurse"))) // not a doctor or nurse 
			{
				log.debug("returning no notes because of role not being a doctor/nurse (" + role.getName() + ")");
				return notes;
			}
			
			for(String issue: issues)
			{
				IssueTransfer trans = new IssueTransfer();
				trans.setDemographicId(demographicId);
				trans.setFacilityId(facilityId);
				trans.setIssueCode(issue);
				transfers.add(trans);
				log.debug("IssueTransfer created for " + issue);
			}
			log.debug("looking for remoteNotes");
			List<NoteTransfer> remoteNotes = CaisiIntegratorManager.getRemoteNotes(facilityId, demographicId, transfers);
			log.debug("found " + remoteNotes.size() + " note transfers");
			
			for(NoteTransfer rNote: remoteNotes)
			{
				CaseManagementNote note = new CaseManagementNote();
				note.setRemote(true);
				note.setFacilityName(rNote.getFacilityName());
				note.setNote(rNote.getNote());
				note.setEncounter_type(rNote.getEncounterType());
				note.setProgramName(rNote.getProgramName());
				note.setObservation_date(rNote.getObservationDate());
				note.setUpdate_date(rNote.getUpdateDate());
				note.setLocked(false);
				note.setRoleName(rNote.getRole());
				
				Provider provider = new Provider();
				StringTokenizer toke = new StringTokenizer(rNote.getObservationCaisiProviderName(), "||");
				if(toke.hasMoreTokens())
				{
					provider.setFirstName(toke.nextToken());
					provider.setLastName(toke.nextToken());
				}
				else
				{
					// what to do?  this would be problematic
				}
				note.setProvider(provider);
				note.setSigned(rNote.getSigningCaisiProviderName() != null);
				log.debug("adding note to list");
				notes.add(note);
			}
		}
		catch(MalformedURLException mue)
		{
			log.error("error", mue);
		}
		return notes;
	}

}
