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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.casemgmt.dao.CaseManagementNoteLinkDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.common.dao.CaseManagementIssueNotesDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.CppUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarRx.data.RxPrescriptionData;

public class NoteDisplayLocal implements NoteDisplay {
	private CaseManagementIssueNotesDao caseManagementIssueNotesDao=(CaseManagementIssueNotesDao)SpringUtils.getBean("caseManagementIssueNotesDao");

	private CaseManagementNote caseManagementNote;
	private boolean editable = false;
	private boolean readonly;
	private boolean groupNote;
	private String location;
	private boolean isCpp = false;

	private List<CaseManagementIssue> caseManagementIssues;

	public NoteDisplayLocal(LoggedInInfo loggedInInfo, CaseManagementNote caseManagementNote) {
		this.caseManagementNote = caseManagementNote;
		if(caseManagementNote.getId() != null){
			this.caseManagementIssues=caseManagementIssueNotesDao.getNoteIssues(getNoteId());
			this.isCpp=calculateIsCpp();
		}
		if (loggedInInfo != null) editable = !caseManagementNote.isSigned() || (loggedInInfo.getLoggedInProviderNo().equals(caseManagementNote.getProviderNo()) && !caseManagementNote.isLocked());

		

	}

	public boolean containsIssue(String issueCode) {
		if(this.caseManagementIssues == null){
			return false;
		}
			
			
		for (CaseManagementIssue caseManagementIssue : this.caseManagementIssues) {
			if (caseManagementIssue.getIssue().getCode().equals(issueCode)) {
					return(true);
			}
		}
		return false;
	}

	private boolean calculateIsCpp(){
		if(this.caseManagementIssues == null){
			return false;
		}
		
		for (CaseManagementIssue caseManagementIssue : this.caseManagementIssues)
		{
			for (int cppIdx = 0; cppIdx < CppUtils.cppCodes.length; cppIdx++)
			{
				if (caseManagementIssue.getIssue().getCode().equals(CppUtils.cppCodes[cppIdx]))
				{
					return(true);
				}
			}
		}

		return(false);
	}

	public void setReadOnly(boolean ro) {
		readonly = ro;
	}

	public String getEncounterType() {
		return (caseManagementNote.getEncounter_type());
	}

	public boolean getHasHistory() {
		return (caseManagementNote.getHasHistory());
	}

	public String getLocation() {
		if (location != null) return location;
		return ("local");
	}

	public String getNote() {
		return (caseManagementNote.getNote());
	}

	public Integer getNoteId() {
		if(caseManagementNote.getId() == null){
			return null;
		}
		return (caseManagementNote.getId().intValue());
	}

	public Date getObservationDate() {
		return (caseManagementNote.getObservation_date());
	}

	public String getProgramName() {
		return (caseManagementNote.getProgramName());
	}

	public String getProviderName() {
		return (caseManagementNote.getProviderName());
	}

	public String getProviderNo() {
		return (caseManagementNote.getProviderNo());
	}

	public Integer getRemoteFacilityId() {
		return (null);
	}

	public String getRevision() {
		return (caseManagementNote.getRevision());
	}

	public String getRoleName() {
		return (caseManagementNote.getRoleName());
	}

	public String getStatus() {
		return (caseManagementNote.getStatus());
	}

	public Date getUpdateDate() {
		return (caseManagementNote.getUpdate_date());
	}

	public String getUuid() {
		return (caseManagementNote.getUuid());
	}

	public boolean isDocument() {
		return (caseManagementNote.isDocumentNote());
	}

	public boolean isEformData() {
		return (caseManagementNote.isEformData());
	}

	public CaseManagementNoteLink getNoteLink() {
		CaseManagementNoteLinkDAO cmDao = (CaseManagementNoteLinkDAO) SpringUtils.getBean("CaseManagementNoteLinkDAO");
		return cmDao.getLastLinkByNote(caseManagementNote.getId());
	}

	public boolean isRxAnnotation() {
		return (caseManagementNote.isRxAnnotation());
	}

	public RxPrescriptionData.Prescription getRxFromAnnotation(CaseManagementNoteLink cmnl) {
		return (caseManagementNote.getRxFromAnnotation(cmnl));
	}

	public boolean isEditable() {
		return (editable);
	}

	public boolean isLocked() {
		return (caseManagementNote.isLocked());
	}

	public boolean isSigned() {
		return (caseManagementNote.isSigned());
	}

	public ArrayList<String> getEditorNames() {
		ArrayList<String> editorNames = new ArrayList<String>();

		for (Provider provider : caseManagementNote.getEditors())
			editorNames.add(provider.getFormattedName());

		return (editorNames);
	}

	@Override
    public String getEncounterTime() {
		StringBuilder et = new StringBuilder();

		if(caseManagementNote.getHourOfEncounterTime()!=null) {
			et.append(caseManagementNote.getHourOfEncounterTime());
			et.append(":");
		}

		if(caseManagementNote.getMinuteOfEncounterTime()!=null) {
			et.append(caseManagementNote.getMinuteOfEncounterTime());
		}

		return et.toString();
	}

	@Override
    public String getEncounterTransportationTime() {
		StringBuilder et = new StringBuilder();

		if(caseManagementNote.getHourOfEncTransportationTime()!=null) {
			et.append(caseManagementNote.getHourOfEncTransportationTime());
			et.append(":");
		}

		if(caseManagementNote.getMinuteOfEncTransportationTime()!=null) {
			et.append(caseManagementNote.getMinuteOfEncTransportationTime());
		}

		return et.toString();
	}


	public ArrayList<String> getIssueDescriptions() {
		ArrayList<String> issueDescriptions = new ArrayList<String>();

		for (CaseManagementIssue issue : caseManagementNote.getIssues())
			issueDescriptions.add(issue.getIssue().getDescription());

		return (issueDescriptions);
	}

	public boolean isReadOnly() {
		return readonly;
	}

	public boolean isGroupNote() {
		return groupNote;
	}

	public void setGroupNote(boolean groupNote) {
		this.groupNote = groupNote;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isCpp() {
		return (isCpp);
	}

	public boolean isEncounterForm() {
	    return false;
    }

	public boolean isInvoice() {
	    return false;
    }
	public boolean isFreeDraw() {
		return false;
	}
	
	public boolean isTicklerNote() {
		return containsIssue("TicklerNote");
	}

	@Override
    public Integer getAppointmentNo() {
		return caseManagementNote.getAppointmentNo();
    }
}
