package org.oscarehr.casemgmt.web;

import java.util.ArrayList;
import java.util.Date;
import org.oscarehr.casemgmt.dao.CaseManagementNoteLinkDAO;

import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import oscar.oscarRx.data.RxPrescriptionData;

public class NoteDisplayLocal implements NoteDisplay {
	private CaseManagementNote caseManagementNote;
	private boolean editable=false;
	private boolean readonly;
	private boolean groupNote;
	private String location;

	public NoteDisplayLocal(CaseManagementNote caseManagementNote)
	{
		this.caseManagementNote=caseManagementNote;

		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		if (loggedInInfo!=null) editable=!caseManagementNote.isSigned() || (loggedInInfo.loggedInProvider.getProviderNo().equals(caseManagementNote.getProviderNo()) && !caseManagementNote.isLocked());
	}

	public void setReadOnly(boolean ro) {
		readonly = ro;
	}

	public String getEncounterType() {
		return(caseManagementNote.getEncounter_type());
	}

	public boolean getHasHistory() {
		return(caseManagementNote.getHasHistory());
	}

	public String getLocation() {
		if(location != null) return location;
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

	public boolean isEformData() {
		return (caseManagementNote.isEformData());
	}

	public CaseManagementNoteLink getNoteLink() {
		CaseManagementNoteLinkDAO cmDao = (CaseManagementNoteLinkDAO)SpringUtils.getBean("CaseManagementNoteLinkDAO");
		return cmDao.getLastLinkByNote(caseManagementNote.getId());
	}

	public boolean isRxAnnotation(){
		System.out.println("in isRxAnnotation in notedisplaylocal.java, note id="+caseManagementNote.getId());
		return (caseManagementNote.isRxAnnotation());
	}

	public RxPrescriptionData.Prescription getRxFromAnnotation(CaseManagementNoteLink cmnl){
	return (caseManagementNote.getRxFromAnnotation(cmnl));
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

	public boolean isReadOnly() {return readonly;}

	public boolean isGroupNote() {
	return groupNote;
	}

	public void setGroupNote(boolean groupNote) {
	this.groupNote = groupNote;
	}

	public void setLocation(String location) {
	this.location= location;
	}
}
