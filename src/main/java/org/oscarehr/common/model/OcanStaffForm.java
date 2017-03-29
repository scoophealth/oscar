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

package org.oscarehr.common.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This entity represents every time a provider fills out or updates a OCAN form.
 * Note that every row / entry represents a change, so as an example if 
 * one provider answers the first 4 questions, then saves it. It will make a entry.
 * If another provider then updates it to answer another 2 questions, this should
 * make a 2nd row. This allows us to track who changed what on the form and when. 
 * As a result, these entities are non delete/update able, the expectation is to
 * make a new entity instead of updating an existing one.
 */
@Entity
public class OcanStaffForm extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
 
	private Integer assessmentId;
	
	private String ocanFormVersion=null;
	private String ocanType=null;
	public static final String FORM_TYPE_OCAN="OCAN";
	public static final String FORM_TYPE_CBI="CBI";
	
	private String providerNo = null;
	private String clientFormProviderNo = null;
	private boolean signed=false;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date clientFormCreated;
	
	private Integer facilityId=null;
	private Integer clientId=null;
	private Integer admissionId=null;
	private Integer clientAge=null;
	
	private String lastName;  //current last name
	private String lastNameAtBirth="";
	private String firstName;
	private String addressLine1="";
	private String addressLine2="";
	private String city="";
	private String province;
	private String postalCode="";
	private String phoneNumber="";
	private String email="";
	private String hcNumber="";
	private String hcVersion="";
	private String dateOfBirth;
	private String estimatedAge="";
	private String clientDateOfBirth;
	private String gender;
	
	private String assessmentStatus;
	private Date startDate;
	private Date completionDate;
	private Date clientStartDate;
	private Date clientCompletionDate;
	

	private String reasonForAssessment;
	
	private String providerName;
	private String clientFormProviderName;
	
	private int submissionId;
	
	private String consent;
	
	private Date referralDate;
	private Date admissionDate;
	private Date serviceInitDate;
	private Date dischargeDate;
	
	public OcanStaffForm() {
		province = "ON";
		setAssessmentStatus("In Progress");
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(Integer assessmentId) {
		this.assessmentId = assessmentId;
	}

	public Date getCreated() {
		return created;
	}

	
	public Date getClientFormCreated() {
		return clientFormCreated;
	}

	public String getOcanFormVersion() {
    	return ocanFormVersion;
    }

	public void setOcanFormVersion(String cdsFormVersion) {
    	this.ocanFormVersion = cdsFormVersion;
    }

	public String getOcanType() {
		return ocanType;
	}

	public void setOcanType(String ocanType) {
		this.ocanType = ocanType;
	}

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	
	public String getClientFormProviderNo() {
		return clientFormProviderNo;
	}

	public void setClientFormProviderNo(String clientFormProviderNo) {
		this.clientFormProviderNo = clientFormProviderNo;
	}

	public boolean isSigned() {
    	return signed;
    }

	public void setSigned(boolean signed) {
    	this.signed = signed;
    }

	public Integer getFacilityId() {
    	return facilityId;
    }

	public void setFacilityId(Integer facilityId) {
    	this.facilityId = facilityId;
    }

	public Integer getClientId() {
    	return clientId;
    }

	public void setClientId(Integer clientId) {
    	this.clientId = clientId;
    }

	public Integer getAdmissionId() {
    	return admissionId;
    }

	public void setAdmissionId(Integer admissionId) {
    	this.admissionId = admissionId;
    }

	public Integer getClientAge() {
    	return clientAge;
    }

	public void setClientAge(Integer clientAge) {
    	this.clientAge = clientAge;
    }

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		OcanStaffForm other = (OcanStaffForm) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}

	@PreRemove
	protected void jpaPreventDelete()
	{
		throw(new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}
/*
	@PreUpdate
	protected void jpaPreventUpdate()
	{
		throw(new UnsupportedOperationException("Update is not allowed for this type of item."));
	}
*/
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLastNameAtBirth() {
    	return lastNameAtBirth;
    }

	public void setLastNameAtBirth(String lastNameAtBirth) {
    	this.lastNameAtBirth = lastNameAtBirth;
    }

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHcNumber() {
		return hcNumber;
	}

	public void setHcNumber(String hcNumber) {
		this.hcNumber = hcNumber;
	}

	public String getHcVersion() {
		return hcVersion;
	}

	public void setHcVersion(String hcVersion) {
		this.hcVersion = hcVersion;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getClientDateOfBirth() {
		return clientDateOfBirth;
	}

	public void setClientDateOfBirth(String clientDateOfBirth) {
		this.clientDateOfBirth = clientDateOfBirth;
	}
		
	public String getEstimatedAge() {
    	return estimatedAge;
    }

	public void setEstimatedAge(String estimatedAge) {
    	this.estimatedAge = estimatedAge;
    }

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setClientFormCreated(Date clientFormCreated) {
		this.clientFormCreated = clientFormCreated;
	}
	
	public String getAssessmentStatus() {
		return assessmentStatus;
	}

	public void setAssessmentStatus(String assessmentStatus) {
		this.assessmentStatus = assessmentStatus;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
	
	public Date getClientStartDate() {
		return clientStartDate;
	}

	public void setClientStartDate(Date clientStartDate) {
		this.clientStartDate = clientStartDate;
	}

	public Date getClientCompletionDate() {
		return clientCompletionDate;
	}

	public void setClientCompletionDate(Date clientCompletionDate) {
		this.clientCompletionDate = clientCompletionDate;
	}

	public String getFormattedStartDate() {
		Date d = getStartDate();
		if(d==null) {return "";}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(d);
	}
	
	public String getFormattedCompletionDate() {
		Date d = getCompletionDate();
		if(d==null) {return "";}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(d);
	}
	public String getFormattedClientStartDate() {
		Date d = getClientStartDate();
		if(d==null) {return "";}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(d);
	}
	
	public String getFormattedClientCompletionDate() {
		Date d = getClientCompletionDate();
		if(d==null) {return "";}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(d);
	}	
		
	public String getReasonForAssessment() {
		return reasonForAssessment;
	}

	public void setReasonForAssessment(String reasonForAssessment) {
		this.reasonForAssessment = reasonForAssessment;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}


	public String getClientFormProviderName() {
		return clientFormProviderName;
	}

	public void setClientFormProviderName(String clientFormProviderName) {
		this.clientFormProviderName = clientFormProviderName;
	}
	


	public int getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(int submissionId) {
		this.submissionId = submissionId;
	}
	

	public String getConsent() {
		return consent;
	}

	public void setConsent(String consent) {
		this.consent = consent;
	}

	public Date getReferralDate() {
    	return referralDate;
    }

	public void setReferralDate(Date referralDate) {
    	this.referralDate = referralDate;
    }

	public Date getAdmissionDate() {
    	return admissionDate;
    }

	public void setAdmissionDate(Date admissionDate) {
    	this.admissionDate = admissionDate;
    }

	public Date getServiceInitDate() {
    	return serviceInitDate;
    }

	public void setServiceInitDate(Date serviceInitDate) {
    	this.serviceInitDate = serviceInitDate;
    }

	public Date getDischargeDate() {
    	return dischargeDate;
    }

	public void setDischargeDate(Date dischargeDate) {
    	this.dischargeDate = dischargeDate;
    }
}
