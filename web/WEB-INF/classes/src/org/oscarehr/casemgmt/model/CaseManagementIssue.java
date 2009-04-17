 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/



package org.oscarehr.casemgmt.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.caisi.model.BaseObject;

public class CaseManagementIssue extends BaseObject {
	protected Long id;
	protected String demographic_no;
	protected long issue_id;
	protected boolean acute;
	//protected boolean medical_diagnosis;
	protected boolean certain;
	protected boolean major;
	//protected boolean active;
	protected boolean resolved;
	protected String type;
	protected Date update_date;
	protected Set notes = new HashSet();
	protected Issue issue;
	protected Integer program_id=null;
	
	protected boolean writeAccess;
	
	protected int hashCode = Integer.MIN_VALUE;
	
	public String toString() {
		return "CaseManagementIssue: id=" + id;
	}
	
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
        
        /*
         *Copy constructor performs copy
         */
        public CaseManagementIssue(CaseManagementIssue cMgmtIssue) {
            
            this.setId(0L); //so hibernate will think it a non persisted obj
            this.setDemographic_no(cMgmtIssue.getDemographic_no());
            this.setIssue_id(cMgmtIssue.getIssue_id());
            this.setAcute(cMgmtIssue.isAcute());
            this.setCertain(cMgmtIssue.isCertain());
            this.setMajor(cMgmtIssue.isMajor());
            this.setResolved(cMgmtIssue.isResolved());
            this.setType(cMgmtIssue.getType());
            this.setUpdate_date(cMgmtIssue.getUpdate_date());
            this.setNotes(cMgmtIssue.getNotes());
            this.setIssue(cMgmtIssue.getIssue());
            this.setWriteAccess(cMgmtIssue.isWriteAccess());
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


	public boolean isWriteAccess() {
		return writeAccess;
	}


	public void setWriteAccess(boolean writeAccess) {
		this.writeAccess = writeAccess;
	}


    public Integer getProgram_id() {
        return program_id;
    }


    public void setProgram_id(Integer program_id) {
        this.program_id = program_id;
    }
	
}
