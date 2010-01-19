package org.oscarehr.casemgmt.web;

import java.util.ArrayList;
import java.util.Date;

import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;

public class NoteDisplayLocal implements NoteDisplay {
	private CaseManagementNote caseManagementNote;
	private boolean editable=false;
	
	public NoteDisplayLocal(CaseManagementNote caseManagementNote)
	{
		this.caseManagementNote=caseManagementNote;
		
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		if (loggedInInfo!=null) editable=!caseManagementNote.isSigned() || (loggedInInfo.loggedInProvider.getProviderNo().equals(caseManagementNote.getProviderNo()) && !caseManagementNote.isLocked());
	}

	public String getEncounterType() {
	    return(caseManagementNote.getEncounter_type());
    }

	public boolean getHasHistory() {
	    return(caseManagementNote.getHasHistory());
    }

	public String getLocation() {
	    return("local");
    }

	public String getNote() {
	    return(caseManagementNote.getNote());
    }

	public Integer getNoteId() {
	    return(caseManagementNote.getId().intValue());
    }

	public Date getObservationDate() {
	    return(caseManagementNote.getObservation_date());
    }

	public String getProgramName() {
	    return(caseManagementNote.getProgramName());
    }

	public String getProviderName() {
	    return(caseManagementNote.getProviderName());
    }

	public String getProviderNo() {
	    return(caseManagementNote.getProviderNo());
    }

	public Integer getRemoteFacilityId() {
	    return(null);
    }

	public String getRevision() {
	    return(caseManagementNote.getRevision());
    }

	public String getRoleName() {
	    return(caseManagementNote.getRoleName());
    }

	public String getStatus() {
	    return(caseManagementNote.getStatus());
    }

	public Date getUpdateDate() {
	    return(caseManagementNote.getUpdate_date());
    }

	public String getUuid() {
	    return(caseManagementNote.getUuid());
    }

	public boolean isDocument() {
	    return(caseManagementNote.isDocumentNote());
    }

	public boolean isEditable() {
		return(editable);
    }

	public boolean isLocked() {
	    return(caseManagementNote.isLocked());
    }

	public boolean isSigned() {
	    return(caseManagementNote.isSigned());
    }

	public ArrayList<String> getEditorNames() {
		ArrayList<String> editorNames=new ArrayList<String>();
		
		for (Provider provider : caseManagementNote.getEditors()) editorNames.add(provider.getFormattedName());
		
		return(editorNames);
	}

	public ArrayList<String> getIssueDescriptions() {
		ArrayList<String> issueDescriptions=new ArrayList<String>();
		
		for (CaseManagementIssue issue : caseManagementNote.getIssues()) issueDescriptions.add(issue.getIssue().getDescription());
		
		return(issueDescriptions);
    }
}
