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

package org.oscarehr.consultations;

import java.io.Serializable;

public class ConsultationData implements Serializable {
	private static final long serialVersionUID = 2733207762515171377L;
	private String id;
	private String status;
	private Integer urgency;
	private String sendTo;
	private String patient;
	private String provider;
	private String serviceId;
	private String serviceDesc;
	private String consultant;
	private String referralDate;
	private String appointmentDate;
	private String appointmentTime;

	private String providerName;
	private String providerNo;
	private String specialistName;
	private String demographicNo;
	private String siteName;
	private boolean patientWillBook;
	private String followUpDate;

	public String getFollowUpDate() {
		return followUpDate;
	}

	public void setFollowUpDate(String followUpDate) {
		this.followUpDate = followUpDate;
	}

	public boolean getPatientWillBook() {
		return patientWillBook;
	}

	public void setPatientWillBook(boolean patientWillBook) {
		this.patientWillBook = patientWillBook;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getUrgency() {
		return urgency;
	}

	public void setUrgency(Integer urgency) {
		this.urgency = urgency;
	}


	public String getPatient() {
		return patient;
	}

	public void setPatient(String patient) {
		this.patient = patient;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}


	public String getSendTo() {
    	return sendTo;
    }

	public void setSendTo(String sendTo) {
    	this.sendTo = sendTo;
    }

	public String getServiceId() {
    	return serviceId;
    }

	public void setServiceId(String serviceId) {
    	this.serviceId = serviceId;
    }

	public String getServiceDesc() {
    	return serviceDesc;
    }

	public void setServiceDesc(String serviceDesc) {
    	this.serviceDesc = serviceDesc;
    }

	public String getConsultant() {
		return consultant;
	}

	public void setConsultant(String consultant) {
		this.consultant = consultant;
	}

	public String getReferralDate() {
		return referralDate;
	}

	public void setReferralDate(String referralDate) {
		this.referralDate = referralDate;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
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

	public String getSpecialistName() {
		return specialistName;
	}

	public void setSpecialistName(String specialistName) {
		this.specialistName = specialistName;
	}

	public String getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(String demographicNo) {
		this.demographicNo = demographicNo;
	}

}