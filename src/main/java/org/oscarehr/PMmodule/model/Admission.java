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

package org.oscarehr.PMmodule.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This is the object class that relates to the admission table. Any customizations belong here.
 */
public class Admission implements Serializable {

	public static final String STATUS_CURRENT="current";
	public static final String STATUS_DISCHARGED="discharged";
	
	/* [CONSTRUCTOR MARKER BEGIN] */
	
	public Admission () {
		// auto generated code... bah
	}

	/**
	 * Constructor for primary key
	 */
	public Admission (Long id) {
		this.setId(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Admission(Long id, String providerNo, Integer clientId, Integer programId) {
		this.setId(id);
		this.setProviderNo(providerNo);
		this.setClientId(clientId);
		this.setProgramId(programId);
	}

	/* [CONSTRUCTOR MARKER END] */

	private Program program;
	private org.oscarehr.PMmodule.model.ProgramTeam team;
	private org.oscarehr.PMmodule.model.ProgramClientStatus clientStatus;
	private org.oscarehr.common.model.Demographic client;
	private Integer teamId;
	private String teamName;
	private Integer clientStatusId;
	private boolean temporaryAdmission;
	private String dischargeNotes;
	private java.util.Date dischargeDate;
	private String programName;
	private String programType;
	private int hashCode = Integer.MIN_VALUE;
	private Long id;
	private String providerNo;
	private String admissionStatus;
	private Integer clientId;
	private java.util.Date admissionDate;
	private String admissionNotes;
	private String tempAdmission;
	private Integer programId;
	private String tempAdmitDischarge;
	private String radioDischargeReason;
	private boolean dischargeFromTransfer=false;
	private boolean admissionFromTransfer=false;
	private boolean automaticDischarge=false;
	private Date lastUpdateDate = null;
	
	public boolean isDischargeFromTransfer() {
        return dischargeFromTransfer;
    }

    public void setDischargeFromTransfer(boolean dischargeFromTransfer) {
        this.dischargeFromTransfer = dischargeFromTransfer;
    }

    public boolean isAdmissionFromTransfer() {
        return admissionFromTransfer;
    }

    public void setAdmissionFromTransfer(boolean admissionFromTransfer) {
        this.admissionFromTransfer = admissionFromTransfer;
    }

    public void setProgram(Program p) {
		this.program = p;
	}

	public Program getProgram() {
		return program;
	}
	
	public GregorianCalendar getAdmissionCalendar()
	{
		GregorianCalendar cal=new GregorianCalendar();
		cal.setTime(admissionDate);
		return(cal);
	}
	
	public GregorianCalendar getDischargeCalendar()
	{
		if (dischargeDate==null) return(null);
		
		GregorianCalendar cal=new GregorianCalendar();
		cal.setTime(dischargeDate);
		return(cal);
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
    public org.oscarehr.common.model.Demographic getClient() {
    	return client;
    }

	/**
     * Set the value related to the column: client_id
     * @param client the client_id value
     */
    public void setClient(org.oscarehr.common.model.Demographic client) {
    	this.client = client;
    }

	/**
     * Return the value associated with the column: team_id
     */
    public Integer getTeamId() {
    	return teamId;
    }

	/**
     * Return the value associated with the column: teamName
     */
    public String getTeamName() {
    	return teamName;
    }

	/**
     * Set the value related to the column: teamName
     * @param teamName the teamName value
     */
    public void setTeamName(String teamName) {
    	this.teamName = teamName;
    }

	/**
     * Return the value associated with the column: clientstatus_id
     */
    public Integer getClientStatusId() {
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
    public String getDischargeNotes() {
    	return dischargeNotes;
    }

	/**
     * Set the value related to the column: discharge_notes
     * @param dischargeNotes the discharge_notes value
     */
    public void setDischargeNotes(String dischargeNotes) {
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
    public void setTeamId(Integer teamId) {
    	this.teamId = teamId;
    }

	/**
     * Set the value related to the column: clientstatus_id
     * @param clientStatusId the clientstatus_id value
     */
    public void setClientStatusId(Integer clientStatusId) {
    	this.clientStatusId = clientStatusId;
    }

	/**
     * Return the value associated with the column: programName
     */
    public String getProgramName() {
    	return programName;
    }

	/**
     * Set the value related to the column: programName
     * @param programName the programName value
     */
    public void setProgramName(String programName) {
    	this.programName = programName;
    }

	/**
     * Return the value associated with the column: programType
     */
    public String getProgramType() {
    	return programType;
    }

	/**
     * Set the value related to the column: programType
     * @param programType the programType value
     */
    public void setProgramType(String programType) {
    	this.programType = programType;
    }

	/**
     * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="native"
     *  column="am_id"
     */
    public Long getId() {
    	return id;
    }

	/**
     * Set the unique identifier of this class
     * @param id the new ID
     */
    public void setId(Long id) {
    	this.id = id;
    	this.hashCode = Integer.MIN_VALUE;
    }

	/**
     * Return the value associated with the column: provider_no
     */
    public String getProviderNo() {
    	return providerNo;
    }

	/**
     * Set the value related to the column: provider_no
     * @param providerNo the provider_no value
     */
    public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	/**
     * Return the value associated with the column: admission_status
     */
    public String getAdmissionStatus() {
    	return admissionStatus;
    }

	/**
     * Set the value related to the column: admission_status
     * @param admissionStatus the admission_status value
     */
    public void setAdmissionStatus(String admissionStatus) {
    	this.admissionStatus = admissionStatus;
    }

	/**
     * Return the value associated with the column: client_id
     */
    public Integer getClientId() {
    	return clientId;
    }

	/**
     * Set the value related to the column: client_id
     * @param clientId the client_id value
     */
    public void setClientId(Integer clientId) {
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
    public String getAdmissionNotes() {
    	return admissionNotes;
    }

	/**
     * Set the value related to the column: admission_notes
     * @param admissionNotes the admission_notes value
     */
    public void setAdmissionNotes(String admissionNotes) {
    	this.admissionNotes = admissionNotes;
    }

	/**
     * Return the value associated with the column: temp_admission
     */
    public String getTempAdmission() {
    	return tempAdmission;
    }

	/**
     * Set the value related to the column: temp_admission
     * @param tempAdmission the temp_admission value
     */
    public void setTempAdmission(String tempAdmission) {
    	this.tempAdmission = tempAdmission;
    }

	/**
     * Return the value associated with the column: program_id
     */
    public Integer getProgramId() {
    	return programId;
    }

	/**
     * Set the value related to the column: program_id
     * @param programId the program_id value
     */
    public void setProgramId(Integer programId) {
    	this.programId = programId;
    }

	/**
     * Return the value associated with the column: temp_admit_discharge
     */
    public String getTempAdmitDischarge() {
    	return tempAdmitDischarge;
    }

	/**
     * Set the value related to the column: temp_admit_discharge
     * @param tempAdmitDischarge the temp_admit_discharge value
     */
    public void setTempAdmitDischarge(String tempAdmitDischarge) {
    	this.tempAdmitDischarge = tempAdmitDischarge;
    }

	/**
     * Return the value associated with the column: radioDischargeReason
     */
    public String getRadioDischargeReason() {
    	return radioDischargeReason;
    }

	/**
     * Set the value related to the column: radioDischargeReason
     * @param radioDischargeReason the radioDischargeReason value
     */
    public void setRadioDischargeReason(String radioDischargeReason) {
    	this.radioDischargeReason = radioDischargeReason;
    }    
    
	public boolean getAutomaticDischarge() {
		return automaticDischarge;
	}

	public void setAutomaticDischarge(boolean automaticDischarge) {
		this.automaticDischarge = automaticDischarge;
	}

	@Override
    public boolean equals(Object obj) {
    	if (null == obj) return false;
    	if (!(obj instanceof org.oscarehr.PMmodule.model.Admission)) return false;
    	else {
    		org.oscarehr.PMmodule.model.Admission admission = (org.oscarehr.PMmodule.model.Admission) obj;
    		if (null == this.getId() || null == admission.getId()) return false;
    		else return (this.getId().equals(admission.getId()));
    	}
    }

	@Override
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

	@Override
    public String toString() {
    	return super.toString();
    }

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	

}
