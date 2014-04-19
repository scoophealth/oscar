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
package org.oscarehr.common.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.oscarehr.PMmodule.model.Program;


/**
 * The persistent class for the admission database table.
 * 
 */
@Entity
@Table(name="admission")
public class Admission extends AbstractModel<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String STATUS_CURRENT="current";
	public static final String STATUS_DISCHARGED="discharged";
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="program_id", referencedColumnName="id", insertable=false, updatable=false)
	private Program program;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="team_id", insertable=false, updatable=false)
	private org.oscarehr.PMmodule.model.ProgramTeam team;
	
	
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="clientstatus_id", insertable=false, updatable=false, nullable=true)
	private org.oscarehr.PMmodule.model.ProgramClientStatus clientStatus;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="client_id", referencedColumnName="demographic_no", insertable=false, updatable=false)
	private org.oscarehr.common.model.Demographic client;
	
	
	@Transient
	private String TeamName;
	
	@Transient
	private String ProgramName;
	
	@Transient
	private String ProgramType;
	
	@Transient
	private int hashCode = Integer.MIN_VALUE;
				

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="am_id", unique=true, nullable=false)
	private Integer id;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="admission_date")
	private Date admissionDate;

	@Column(name="admission_from_transfer", nullable=false)
	private boolean admissionFromTransfer;

    @Lob()
	@Column(name="admission_notes")
	private String admissionNotes;

	@Column(name="admission_status", length=24)
	private String admissionStatus;

	@Column(name="automatic_discharge")
	private boolean automaticDischarge;

	@Column(name="client_id")
	private Integer clientId = null;

	@Column(name="clientstatus_id")
	private Integer clientStatusId = null;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="discharge_date")
	private Date dischargeDate;

	@Column(name="discharge_from_transfer", nullable=false)
	private boolean dischargeFromTransfer;

    @Lob()
	@Column(name="discharge_notes")
	private String dischargeNotes;

	@Column(name="program_id", nullable=false)
	private Integer programId;

	@Column(name="provider_no", nullable=false, length=6)
	private String providerNo;

	@Column(length=10)
	private String radioDischargeReason;

	@Column(name="team_id")
	private Integer teamId = null;

	@Column(name="temp_admission", length=1)
	private String tempAdmission;

	@Column(name="temp_admit_discharge", length=1)
	private String tempAdmitDischarge;

	@Column(name="temporary_admission_flag")
	private boolean temporaryAdmissionFlag;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateDate = null;
	
	public Admission () {
		// auto generated code... bah
	}
	
	/**
	 * Constructor for primary key
	 */
	public Admission (Long id) {
		Integer intId = id.intValue();
		this.setId(intId);
	}

	/**
	 * Constructor for required fields
	 */
	public Admission(Long id, String providerNo, Integer clientId, Integer programId) {
		Integer intId = id.intValue();
		this.setId(intId);
		this.setProviderNo(providerNo);
		this.setClientId(clientId);
		this.setProgramId(programId);
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer amId) {
		this.id = amId;
	}

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
    	return TeamName;
    }

	/**
     * Set the value related to the column: teamName
     * @param teamName the teamName value
     */
    public void setTeamName(String teamName) {
    	this.TeamName = teamName;
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
    	return temporaryAdmissionFlag;
    }

	/**
     * Set the value related to the column: temporary_admission_flag
     * @param temporaryAdmission the temporary_admission_flag value
     */
    public void setTemporaryAdmission(boolean temporaryAdmission) {
    	this.temporaryAdmissionFlag = temporaryAdmission;
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

    public String getFormattedDischargeDate() {
	Date d = getDischargeDate();
	if(d==null) {return "";}
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	return formatter.format(d);
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
    	return ProgramName;
    }

	/**
     * Set the value related to the column: programName
     * @param programName the programName value
     */
    public void setProgramName(String programName) {
    	this.ProgramName = programName;
    }

	/**
     * Return the value associated with the column: programType
     */
    public String getProgramType() {
    	return ProgramType;
    }

	/**
     * Set the value related to the column: programType
     * @param programType the programType value
     */
    public void setProgramType(String programType) {
    	this.ProgramType = programType;
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
    	if (!(obj instanceof org.oscarehr.common.model.Admission)) return false;
    	else {
    		org.oscarehr.common.model.Admission admission = (org.oscarehr.common.model.Admission) obj;
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
	
	@PostLoad
	public void postLoad() {		
		TeamName = team == null ? "" : team.getName();
		ProgramName = program.getName();
		ProgramType = program.getType();
		
	}

	public boolean isTemporaryAdmissionFlag() {
		return temporaryAdmissionFlag;
	}

	public void setTemporaryAdmissionFlag(boolean temporaryAdmissionFlag) {
		this.temporaryAdmissionFlag = temporaryAdmissionFlag;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	@PreUpdate
	@PrePersist
	protected void jpa_updateDate() {
		setLastUpdateDate(new Date());
	}
	
}
