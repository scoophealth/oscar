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
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "consultationRequests")
public class ConsultationRequest extends AbstractModel<Integer> implements Serializable {

	private static final String ACTIVE_MARKER = "1";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "requestId")
	private Integer id;

	@Column(name = "referalDate")
	@Temporal(TemporalType.DATE)
	private Date referralDate;

	private Integer serviceId;

	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="specId")
	private ProfessionalSpecialist professionalSpecialist;

	@Temporal(TemporalType.DATE)
	private Date appointmentDate;	
	@Temporal(TemporalType.TIME)
	private Date appointmentTime;

	@Column(name = "reason")
	private String reasonForReferral;

	private String clinicalInfo;
	private String currentMeds;
	private String allergies;
	private String providerNo;

	@Column(name = "demographicNo")
	private Integer demographicId;

	private String status = ACTIVE_MARKER;
	private String statusText;
	private String sendTo;
	private String concurrentProblems;
	private String urgency;
	private String appointmentInstructions;
	private boolean patientWillBook;	
	
	@Column(name = "site_name")
	private String siteName;
        
    @Temporal(TemporalType.DATE)
    private Date followUpDate;
    @Column(name = "signature_img")
    private String signatureImg;
    private String letterheadName;
    private String letterheadAddress;
    private String letterheadPhone;
    private String letterheadFax;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;
    
    private Integer fdid = null;
    private String source;
    
    @ManyToOne(fetch=FetchType.EAGER, targetEntity=LookupListItem.class)
    @JoinColumn(name="appointmentInstructions", referencedColumnName="value", insertable = false, updatable = false)
    private LookupListItem lookupListItem;
    
	@Override
    public Integer getId() {
	    return(id);
    }

	public Date getReferralDate() {
    	return referralDate;
    }

	public void setReferralDate(Date referralDate) {
    	this.referralDate = referralDate;
    }

	public Integer getServiceId() {
    	return serviceId;
    }

	public void setServiceId(Integer serviceId) {
    	this.serviceId = serviceId;
    }

	public Date getAppointmentDate() {
    	return appointmentDate;
    }

	public void setAppointmentDate(Date appointmentDate) {
    	this.appointmentDate = appointmentDate;
    }

	public Date getAppointmentTime() {
    	return appointmentTime;
    }

	public void setAppointmentTime(Date appointmentTime) {
    	this.appointmentTime = appointmentTime;
    }

	public String getReasonForReferral() {
    	return reasonForReferral;
    }

	public void setReasonForReferral(String reasonForReferral) {
    	this.reasonForReferral = StringUtils.trimToNull(reasonForReferral);
    }

	public String getClinicalInfo() {
    	return clinicalInfo;
    }

	public void setClinicalInfo(String clinicalInfo) {
    	this.clinicalInfo = StringUtils.trimToNull(clinicalInfo);
    }

	public String getCurrentMeds() {
    	return currentMeds;
    }

	public void setCurrentMeds(String currentMeds) {
    	this.currentMeds = StringUtils.trimToNull(currentMeds);
    }

	public String getAllergies() {
    	return allergies;
    }

	public void setAllergies(String allergies) {
    	this.allergies = StringUtils.trimToNull(allergies);
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = StringUtils.trimToNull(providerNo);
    }

	public Integer getDemographicId() {
    	return demographicId;
    }

	public void setDemographicId(Integer demographicId) {
    	this.demographicId = demographicId;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = StringUtils.trimToNull(status);
    }

	public String getStatusText() {
    	return statusText;
    }

	public void setStatusText(String statusText) {
    	this.statusText = StringUtils.trimToNull(statusText);
    }

	public String getSendTo() {
    	return sendTo;
    }

	public void setSendTo(String sendTo) {
    	this.sendTo = StringUtils.trimToNull(sendTo);
    }

	public String getConcurrentProblems() {
    	return concurrentProblems;
    }

	public void setConcurrentProblems(String concurrentProblems) {
    	this.concurrentProblems = StringUtils.trimToNull(concurrentProblems);
    }

	public String getUrgency() {
    	return urgency;
    }

	public void setUrgency(String urgency) {
    	this.urgency = StringUtils.trimToNull(urgency);
    }
	
	public String getSiteName() {
    	return siteName;
    }

	public void setSiteName(String siteName) {
    	this.siteName = siteName;
    }

	public boolean isPatientWillBook() {
    	return patientWillBook;
    }

	public void setPatientWillBook(boolean patientWillBook) {
    	this.patientWillBook = patientWillBook;
    }

    /**
     * @return the followUpDate
     */
    public Date getFollowUpDate() {
        return followUpDate;
    }

    /**
     * @param followUpDate the followUpDate to set
     */
    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }

    /**
     * @return the professionalSpecialist
     */
    public ProfessionalSpecialist getProfessionalSpecialist() {
        return professionalSpecialist;
}

    /**
     * @param professionalSpecialist the professionalSpecialist to set
     */
    public void setProfessionalSpecialist(ProfessionalSpecialist professionalSpecialist) {
        this.professionalSpecialist = professionalSpecialist;
    }

    public Integer getSpecialistId() {
    	if(professionalSpecialist != null)
    		return this.professionalSpecialist.getId();
    	else
    		return null;
    }

	public String getSignatureImg() {
	    return signatureImg;
    }

	public void setSignatureImg(String signatureImg) {
	    this.signatureImg = signatureImg;
    }

	public String getLetterheadName() {
	    return letterheadName;
    }

	public void setLetterheadName(String letterheadName) {
	    this.letterheadName = letterheadName;
    }

	public String getLetterheadAddress() {
	    return letterheadAddress;
    }

	public void setLetterheadAddress(String letterheadAddress) {
	    this.letterheadAddress = letterheadAddress;
    }

	public String getLetterheadPhone() {
	    return letterheadPhone;
    }

	public void setLetterheadPhone(String letterheadPhone) {
	    this.letterheadPhone = letterheadPhone;
    }

	public String getLetterheadFax() {
	    return letterheadFax;
    }

	public void setLetterheadFax(String letterheadFax) {
	    this.letterheadFax = letterheadFax;
    }
	
	
	public Integer getFdid() {
		return fdid;
	}

	public void setFdid(Integer fdid) {
		this.fdid = fdid;
	}
	
	

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@PrePersist
	@PreUpdate
	protected void jpa_updateLastDateUpdated() {
		lastUpdateDate = new Date();
	}
	
	/**
	 * returns the appointment instructions value. 
	 * This can be a display value or select list value 
	 * if the Lookup List interface is used. 
	 * If the table contains a hash key it most likely is a 
	 * primary key association in the LookupListItem table.
	 */
	public String getAppointmentInstructions() {
		return appointmentInstructions;
	}

	public void setAppointmentInstructions(String appointmentInstructions) {
		this.appointmentInstructions = appointmentInstructions;
	}

	/**
	 * Returns the display label of the Appointment Instruction if
	 * the Lookup List interface is being used.
	 * Empty string otherwise.
	 */
	@Transient
	public String getAppointmentInstructionsLabel() {
		if( lookupListItem != null ) {
			return lookupListItem.getLabel();
		}
		return "";
	}

	/**
	 * This will be bound if the Appointment Instructions
	 * value is found as a unique match in the LookupListItem
	 * table. 
	 */
	public LookupListItem getLookupListItem() {
		return lookupListItem;
	}

	public void setLookupListItem(LookupListItem lookupListItem) {
		this.lookupListItem = lookupListItem;
	}


}
