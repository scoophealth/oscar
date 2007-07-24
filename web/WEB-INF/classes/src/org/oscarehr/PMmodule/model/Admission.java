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

package org.oscarehr.PMmodule.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * This is the object class that relates to the admission table. Any customizations belong here.
 */
public class Admission implements Serializable {

	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	
	public Admission () {
		// auto generated code... bah
	}

	/**
	 * Constructor for primary key
	 */
	public Admission (java.lang.Long id) {
		this.setId(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Admission(java.lang.Long id, java.lang.Long agencyId, java.lang.Long providerNo, java.lang.Integer clientId, java.lang.Integer programId) {
		this.setId(id);
		this.setAgencyId(agencyId);
		this.setProviderNo(providerNo);
		this.setClientId(clientId);
		this.setProgramId(programId);
	}

	/* [CONSTRUCTOR MARKER END] */

	private Program program;
	private org.oscarehr.PMmodule.model.ProgramTeam team;
	private org.oscarehr.PMmodule.model.ProgramClientStatus clientStatus;
	private org.oscarehr.PMmodule.model.Demographic client;
	private java.lang.Integer teamId;
	private java.lang.String teamName;
	private java.lang.Integer clientStatusId;
	private boolean temporaryAdmission;
	private java.lang.String dischargeNotes;
	private java.util.Date dischargeDate;
	private java.lang.String programName;
	private java.lang.String programType;
	private int hashCode = Integer.MIN_VALUE;
	private java.lang.Long id;
	private java.lang.Long agencyId;
	private java.lang.Long providerNo;
	private java.lang.String admissionStatus;
	private java.lang.Integer clientId;
	private java.util.Date admissionDate;
	private java.lang.String admissionNotes;
	private java.lang.String tempAdmission;
	private java.lang.Integer programId;
	private java.lang.String tempAdmitDischarge;
	private java.lang.String radioDischargeReason;
	
	public void setProgram(Program p) {
		this.program = p;
	}

	public Program getProgram() {
		return program;
	}
	
	public String getAdmissionDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(this.getAdmissionDate());		
	}

	/**
     * Return the value associated with the column: team_id
     */
    public org.oscarehr.PMmodule.model.ProgramTeam getTeam() {
    	return team;
    }

	/**
     * Set the value related to the column: team_id
     * @param team the team_id value
     */
    public void setTeam(org.oscarehr.PMmodule.model.ProgramTeam team) {
    	this.team = team;
    }

	/**
     * Return the value associated with the column: clientstatus_id
     */
    public org.oscarehr.PMmodule.model.ProgramClientStatus getClientStatus() {
    	return clientStatus;
    }

	/**
     * Set the value related to the column: clientstatus_id
     * @param clientStatus the clientstatus_id value
     */
    public void setClientStatus(org.oscarehr.PMmodule.model.ProgramClientStatus clientStatus) {
    	this.clientStatus = clientStatus;
    }

	/**
     * Return the value associated with the column: client_id
     */
    public org.oscarehr.PMmodule.model.Demographic getClient() {
    	return client;
    }

	/**
     * Set the value related to the column: client_id
     * @param client the client_id value
     */
    public void setClient(org.oscarehr.PMmodule.model.Demographic client) {
    	this.client = client;
    }

	/**
     * Return the value associated with the column: team_id
     */
    public java.lang.Integer getTeamId() {
    	return teamId;
    }

	/**
     * Return the value associated with the column: teamName
     */
    public java.lang.String getTeamName() {
    	return teamName;
    }

	/**
     * Set the value related to the column: teamName
     * @param teamName the teamName value
     */
    public void setTeamName(java.lang.String teamName) {
    	this.teamName = teamName;
    }

	/**
     * Return the value associated with the column: clientstatus_id
     */
    public java.lang.Integer getClientStatusId() {
    	return clientStatusId;
    }

	/**
     * Return the value associated with the column: temporary_admission_flag
     */
    public boolean isTemporaryAdmission() {
    	return temporaryAdmission;
    }

	/**
     * Set the value related to the column: temporary_admission_flag
     * @param temporaryAdmission the temporary_admission_flag value
     */
    public void setTemporaryAdmission(boolean temporaryAdmission) {
    	this.temporaryAdmission = temporaryAdmission;
    }

	/**
     * Return the value associated with the column: discharge_notes
     */
    public java.lang.String getDischargeNotes() {
    	return dischargeNotes;
    }

	/**
     * Set the value related to the column: discharge_notes
     * @param dischargeNotes the discharge_notes value
     */
    public void setDischargeNotes(java.lang.String dischargeNotes) {
    	this.dischargeNotes = dischargeNotes;
    }

	/**
     * Return the value associated with the column: discharge_date
     */
    public java.util.Date getDischargeDate() {
    	return dischargeDate;
    }

	/**
     * Set the value related to the column: discharge_date
     * @param dischargeDate the discharge_date value
     */
    public void setDischargeDate(java.util.Date dischargeDate) {
    	this.dischargeDate = dischargeDate;
    }

	/**
     * Set the value related to the column: team_id
     * @param teamId the team_id value
     */
    public void setTeamId(java.lang.Integer teamId) {
    	this.teamId = teamId;
    }

	/**
     * Set the value related to the column: clientstatus_id
     * @param clientStatusId the clientstatus_id value
     */
    public void setClientStatusId(java.lang.Integer clientStatusId) {
    	this.clientStatusId = clientStatusId;
    }

	/**
     * Return the value associated with the column: programName
     */
    public java.lang.String getProgramName() {
    	return programName;
    }

	/**
     * Set the value related to the column: programName
     * @param programName the programName value
     */
    public void setProgramName(java.lang.String programName) {
    	this.programName = programName;
    }

	/**
     * Return the value associated with the column: programType
     */
    public java.lang.String getProgramType() {
    	return programType;
    }

	/**
     * Set the value related to the column: programType
     * @param programType the programType value
     */
    public void setProgramType(java.lang.String programType) {
    	this.programType = programType;
    }

	/**
     * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="am_id"
     */
    public java.lang.Long getId() {
    	return id;
    }

	/**
     * Set the unique identifier of this class
     * @param id the new ID
     */
    public void setId(java.lang.Long id) {
    	this.id = id;
    	this.hashCode = Integer.MIN_VALUE;
    }

	/**
     * Return the value associated with the column: agency_id
     */
    public java.lang.Long getAgencyId() {
    	return agencyId;
    }

	/**
     * Set the value related to the column: agency_id
     * @param agencyId the agency_id value
     */
    public void setAgencyId(java.lang.Long agencyId) {
    	this.agencyId = agencyId;
    }

	/**
     * Return the value associated with the column: provider_no
     */
    public java.lang.Long getProviderNo() {
    	return providerNo;
    }

	/**
     * Set the value related to the column: provider_no
     * @param providerNo the provider_no value
     */
    public void setProviderNo(java.lang.Long providerNo) {
    	this.providerNo = providerNo;
    }

	/**
     * Return the value associated with the column: admission_status
     */
    public java.lang.String getAdmissionStatus() {
    	return admissionStatus;
    }

	/**
     * Set the value related to the column: admission_status
     * @param admissionStatus the admission_status value
     */
    public void setAdmissionStatus(java.lang.String admissionStatus) {
    	this.admissionStatus = admissionStatus;
    }

	/**
     * Return the value associated with the column: client_id
     */
    public java.lang.Integer getClientId() {
    	return clientId;
    }

	/**
     * Set the value related to the column: client_id
     * @param clientId the client_id value
     */
    public void setClientId(java.lang.Integer clientId) {
    	this.clientId = clientId;
    }

	/**
     * Return the value associated with the column: admission_date
     */
    public java.util.Date getAdmissionDate() {
    	return admissionDate;
    }

	/**
     * Set the value related to the column: admission_date
     * @param admissionDate the admission_date value
     */
    public void setAdmissionDate(java.util.Date admissionDate) {
    	this.admissionDate = admissionDate;
    }

	/**
     * Return the value associated with the column: admission_notes
     */
    public java.lang.String getAdmissionNotes() {
    	return admissionNotes;
    }

	/**
     * Set the value related to the column: admission_notes
     * @param admissionNotes the admission_notes value
     */
    public void setAdmissionNotes(java.lang.String admissionNotes) {
    	this.admissionNotes = admissionNotes;
    }

	/**
     * Return the value associated with the column: temp_admission
     */
    public java.lang.String getTempAdmission() {
    	return tempAdmission;
    }

	/**
     * Set the value related to the column: temp_admission
     * @param tempAdmission the temp_admission value
     */
    public void setTempAdmission(java.lang.String tempAdmission) {
    	this.tempAdmission = tempAdmission;
    }

	/**
     * Return the value associated with the column: program_id
     */
    public java.lang.Integer getProgramId() {
    	return programId;
    }

	/**
     * Set the value related to the column: program_id
     * @param programId the program_id value
     */
    public void setProgramId(java.lang.Integer programId) {
    	this.programId = programId;
    }

	/**
     * Return the value associated with the column: temp_admit_discharge
     */
    public java.lang.String getTempAdmitDischarge() {
    	return tempAdmitDischarge;
    }

	/**
     * Set the value related to the column: temp_admit_discharge
     * @param tempAdmitDischarge the temp_admit_discharge value
     */
    public void setTempAdmitDischarge(java.lang.String tempAdmitDischarge) {
    	this.tempAdmitDischarge = tempAdmitDischarge;
    }

	/**
     * Return the value associated with the column: radioDischargeReason
     */
    public java.lang.String getRadioDischargeReason() {
    	return radioDischargeReason;
    }

	/**
     * Set the value related to the column: radioDischargeReason
     * @param radioDischargeReason the radioDischargeReason value
     */
    public void setRadioDischargeReason(java.lang.String radioDischargeReason) {
    	this.radioDischargeReason = radioDischargeReason;
    }

	public boolean equals(Object obj) {
    	if (null == obj) return false;
    	if (!(obj instanceof org.oscarehr.PMmodule.model.Admission)) return false;
    	else {
    		org.oscarehr.PMmodule.model.Admission admission = (org.oscarehr.PMmodule.model.Admission) obj;
    		if (null == this.getId() || null == admission.getId()) return false;
    		else return (this.getId().equals(admission.getId()));
    	}
    }

	public int hashCode() {
    	if (Integer.MIN_VALUE == this.hashCode) {
    		if (null == this.getId()) return super.hashCode();
    		else {
    			String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
    			this.hashCode = hashStr.hashCode();
    		}
    	}
    	return this.hashCode;
    }

	public String toString() {
    	return super.toString();
    }

}