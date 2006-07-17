package org.caisi.casemgmt.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.caisi.model.BaseObject;

public class CaseManagementIssue extends BaseObject {
	private Long id;
	private String demographic_no;
	private long issue_id;
	private boolean acute;
	//private boolean medical_diagnosis;
	private boolean certain;
	private boolean major;
	//private boolean active;
	private boolean resolved;
	private String type;
	private Date update_date;
	private Set notes = new HashSet();
	private Issue issue;
	
	private int hashCode = Integer.MIN_VALUE;
	
	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof CaseManagementIssue)) return false;
		else {
			CaseManagementIssue mObj = (CaseManagementIssue) obj;
			if (null == this.getId() || null == mObj.getId()) return false;
			else return (this.getId().equals(mObj.getId()));
		}
	}


	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}
	
	public CaseManagementIssue() {
		update_date = new Date();
	}
	/*public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}*/
	public boolean isAcute() {
		return acute;
	}
	public void setAcute(boolean acute) {
		this.acute = acute;
	}
	public boolean isCertain() {
		return certain;
	}
	public void setCertain(boolean certain) {
		this.certain = certain;
	}
	public String getDemographic_no() {
		return demographic_no;
	}
	public void setDemographic_no(String demographic_no) {
		this.demographic_no = demographic_no;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Issue getIssue() {
		return issue;
	}
	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	public long getIssue_id() {
		return issue_id;
	}
	public void setIssue_id(long issue_id) {
		this.issue_id = issue_id;
	}
	
	public boolean isMajor() {
		return major;
	}
	public void setMajor(boolean major) {
		this.major = major;
	}
	/*public boolean isMedical_diagnosis() {
		return medical_diagnosis;
	}
	public void setMedical_diagnosis(boolean medical_diagnosis) {
		this.medical_diagnosis = medical_diagnosis;
	}*/
	public Set getNotes() {
		return notes;
	}
	public void setNotes(Set notes) {
		this.notes = notes;
	}
	public boolean isResolved() {
		return resolved;
	}
	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	
}
