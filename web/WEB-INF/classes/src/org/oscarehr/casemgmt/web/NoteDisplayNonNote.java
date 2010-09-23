package org.oscarehr.casemgmt.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.data.EctFormData.PatientForm;
import oscar.oscarRx.data.RxPrescriptionData.Prescription;

/**
 * The echart seems to have non-note items in the note list. As a result this class will hold non-note items. A constructor can be made for each type of non-note item.
 */
public class NoteDisplayNonNote implements NoteDisplay {

	private static ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");

	private Integer noteId;
	private Date date;
	private String note;
	private Provider provider;
	private boolean isEformData = false;
	private boolean isEncounterForm = false;
	private String linkInfo;

	public NoteDisplayNonNote(Hashtable<String, Object> eform) {
		date = (Date) eform.get("formDateAsDate");
		note = eform.get("formName") + " : " + eform.get("formSubject");
		provider = providerDao.getProvider((String) eform.get("providerNo"));
		isEformData = true;
		noteId = new Integer((String) eform.get("fdid"));
	}

	public NoteDisplayNonNote(PatientForm patientForm) {
		date = patientForm.created;
		note = patientForm.formName;
		noteId = patientForm.formId;
		linkInfo = patientForm.formName;
		isEncounterForm = true;
	}

	public ArrayList<String> getEditorNames() {
		return (new ArrayList<String>());
	}

	public String getEncounterType() {
		return "";
	}

	public boolean getHasHistory() {
		return false;
	}

	public ArrayList<String> getIssueDescriptions() {
		return (new ArrayList<String>());
	}

	public String getLocation() {
		return null;
	}

	public String getNote() {
		return note;
	}

	public Integer getNoteId() {
		return noteId;
	}

	public CaseManagementNoteLink getNoteLink() {
		return null;
	}

	public Date getObservationDate() {
		return date;
	}

	public String getProgramName() {
		return null;
	}

	public String getProviderName() {
		if (provider != null) return (provider.getFormattedName());
		else return ("");
	}

	public String getProviderNo() {
		if (provider != null) return (provider.getProviderNo());
		else return ("");
	}

	public Integer getRemoteFacilityId() {
		return null;
	}

	public String getRevision() {
		return null;
	}

	public String getRoleName() {
		return null;
	}

	public Prescription getRxFromAnnotation(CaseManagementNoteLink cmnl) {
		return null;
	}

	public String getStatus() {
		return null;
	}

	public Date getUpdateDate() {
		return date;
	}

	public String getUuid() {
		return null;
	}

	public boolean isCpp() {
		return false;
	}

	public boolean isDocument() {
		return false;
	}

	public boolean isEditable() {
		return false;
	}

	public boolean isEformData() {
		return isEformData;
	}

	public boolean isGroupNote() {
		return false;
	}

	public boolean isLocked() {
		return false;
	}

	public boolean isReadOnly() {
		return false;
	}

	public boolean isRxAnnotation() {
		return false;
	}

	public boolean isSigned() {
		return false;
	}

	public boolean isEncounterForm() {
		return isEncounterForm;
	}

	public String getLinkInfo() {
		return linkInfo;
	}

}
