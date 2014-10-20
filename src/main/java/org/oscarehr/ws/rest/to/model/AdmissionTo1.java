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

import java.util.Date;

public class AdmissionTo1 {

	/*
	private Program program;
	
	private ProgramTeam team;
	
	private ProgramClientStatus clientStatus;
	
	private Demographic client;
	*/
	
	private Integer id;
	
	private String teamName;
	
	private String programName;
	
	private String programType;
	
 	private Date admissionDate;

	private boolean admissionFromTransfer;

 	private String admissionNotes;

	private String admissionStatus;

	private boolean automaticDischarge;

	private Integer clientId = null;

	private Integer clientStatusId = null;

	private Date dischargeDate;

	private boolean dischargeFromTransfer;

 	private String dischargeNotes;

	private Integer programId;

	private String providerNo;

	private String radioDischargeReason;

	private Integer teamId = null;

	private String tempAdmission;

	private String tempAdmitDischarge;

	private boolean temporaryAdmissionFlag;

	private Date lastUpdateDate = null;
	
	private DemographicTo1 demographic = null;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
	}

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public boolean isAdmissionFromTransfer() {
		return admissionFromTransfer;
	}

	public void setAdmissionFromTransfer(boolean admissionFromTransfer) {
		this.admissionFromTransfer = admissionFromTransfer;
	}

	public String getAdmissionNotes() {
		return admissionNotes;
	}

	public void setAdmissionNotes(String admissionNotes) {
		this.admissionNotes = admissionNotes;
	}

	public String getAdmissionStatus() {
		return admissionStatus;
	}

	public void setAdmissionStatus(String admissionStatus) {
		this.admissionStatus = admissionStatus;
	}

	public boolean isAutomaticDischarge() {
		return automaticDischarge;
	}

	public void setAutomaticDischarge(boolean automaticDischarge) {
		this.automaticDischarge = automaticDischarge;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getClientStatusId() {
		return clientStatusId;
	}

	public void setClientStatusId(Integer clientStatusId) {
		this.clientStatusId = clientStatusId;
	}

	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public boolean isDischargeFromTransfer() {
		return dischargeFromTransfer;
	}

	public void setDischargeFromTransfer(boolean dischargeFromTransfer) {
		this.dischargeFromTransfer = dischargeFromTransfer;
	}

	public String getDischargeNotes() {
		return dischargeNotes;
	}

	public void setDischargeNotes(String dischargeNotes) {
		this.dischargeNotes = dischargeNotes;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getRadioDischargeReason() {
		return radioDischargeReason;
	}

	public void setRadioDischargeReason(String radioDischargeReason) {
		this.radioDischargeReason = radioDischargeReason;
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public String getTempAdmission() {
		return tempAdmission;
	}

	public void setTempAdmission(String tempAdmission) {
		this.tempAdmission = tempAdmission;
	}

	public String getTempAdmitDischarge() {
		return tempAdmitDischarge;
	}

	public void setTempAdmitDischarge(String tempAdmitDischarge) {
		this.tempAdmitDischarge = tempAdmitDischarge;
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

	public DemographicTo1 getDemographic() {
		return demographic;
	}

	public void setDemographic(DemographicTo1 demographic) {
		this.demographic = demographic;
	}
	
	
	
}
