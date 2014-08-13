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
package org.oscarehr.ws.rest.to.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="encounterNote")
public class NoteTo1 implements Serializable{
	
	private Integer noteId;
	private Boolean isSigned;
	private Boolean isEditable;
	private Date observationDate;
	private String revision;
	private Date updateDate;
	private String providerName;
	private String providerNo;
	private String status;
	private String programName;
	private String location;
	private String roleName;
	private Integer remoteFacilityId;
	private String uuid;
	private Boolean hasHistory;
	private Boolean locked;
	private String note;
	private boolean isDocument;
	private boolean isRxAnnotation;
	private boolean isEformData;
	private boolean isEncounterForm;
	private boolean isInvoice;
	private boolean isTicklerNote;
	private String encounterType;

	private ArrayList<String> editorNames;
	private ArrayList<String> issueDescriptions;

	//not controlled by note attributes / business logic like "editable".
	//use this for a category of notes - like integrator, group notes, etc
	private boolean isReadOnly;
	private boolean isGroupNote;
	private boolean isCpp;
	private String encounterTime;
	private String encounterTransportationTime;
	
	
	public Integer getNoteId() {
		return noteId;
	}

	public void setNoteId(Integer noteId) {
		this.noteId = noteId;
	}

	public Boolean getIsSigned() {
		return isSigned;
	}

	public void setIsSigned(Boolean isSigned) {
		this.isSigned = isSigned;
	}

	public Date getObservationDate() {
		return observationDate;
	}

	public void setObservationDate(Date observationDate) {
		this.observationDate = observationDate;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getRemoteFacilityId() {
		return remoteFacilityId;
	}

	public void setRemoteFacilityId(Integer remoteFacilityId) {
		this.remoteFacilityId = remoteFacilityId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Boolean getHasHistory() {
		return hasHistory;
	}

	public void setHasHistory(Boolean hasHistory) {
		this.hasHistory = hasHistory;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isDocument() {
		return isDocument;
	}

	public void setDocument(boolean isDocument) {
		this.isDocument = isDocument;
	}

	public boolean isRxAnnotation() {
		return isRxAnnotation;
	}

	public void setRxAnnotation(boolean isRxAnnotation) {
		this.isRxAnnotation = isRxAnnotation;
	}

	public boolean isEformData() {
		return isEformData;
	}

	public void setEformData(boolean isEformData) {
		this.isEformData = isEformData;
	}

	public boolean isEncounterForm() {
		return isEncounterForm;
	}

	public void setEncounterForm(boolean isEncounterForm) {
		this.isEncounterForm = isEncounterForm;
	}

	public boolean isInvoice() {
		return isInvoice;
	}

	public void setInvoice(boolean isInvoice) {
		this.isInvoice = isInvoice;
	}

	public boolean isTicklerNote() {
		return isTicklerNote;
	}

	public void setTicklerNote(boolean isTicklerNote) {
		this.isTicklerNote = isTicklerNote;
	}

	public String getEncounterType() {
		return encounterType;
	}

	public void setEncounterType(String encounterType) {
		this.encounterType = encounterType;
	}

	public ArrayList<String> getEditorNames() {
		return editorNames;
	}

	public void setEditorNames(ArrayList<String> editorNames) {
		this.editorNames = editorNames;
	}

	public ArrayList<String> getIssueDescriptions() {
		return issueDescriptions;
	}

	public void setIssueDescriptions(ArrayList<String> issueDescriptions) {
		this.issueDescriptions = issueDescriptions;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public boolean isGroupNote() {
		return isGroupNote;
	}

	public void setGroupNote(boolean isGroupNote) {
		this.isGroupNote = isGroupNote;
	}

	public boolean isCpp() {
		return isCpp;
	}

	public void setCpp(boolean isCpp) {
		this.isCpp = isCpp;
	}

	public String getEncounterTime() {
		return encounterTime;
	}

	public void setEncounterTime(String encounterTime) {
		this.encounterTime = encounterTime;
	}

	public String getEncounterTransportationTime() {
		return encounterTransportationTime;
	}

	public void setEncounterTransportationTime(String encounterTransportationTime) {
		this.encounterTransportationTime = encounterTransportationTime;
	}

	public Boolean isEditable() {
	    return isEditable;
    }

	public void setIsEditable(Boolean isEditable) {
	    this.isEditable = isEditable;
    } 
	
	public void setEditable(Boolean isEditable) {
	    this.isEditable = isEditable;
    } 

}
