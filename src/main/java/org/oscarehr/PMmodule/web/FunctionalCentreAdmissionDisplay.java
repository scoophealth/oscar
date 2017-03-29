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

package org.oscarehr.PMmodule.web;

import java.util.Date;

public class FunctionalCentreAdmissionDisplay {

	private Integer id;
 
	private Integer demographicNo;
	private String functionalCentreId;
	private String referralDate;
	private String admissionDate;
	private String serviceInitiationDate;
	private String dischargeDate = null;
	private boolean discharged = false;
	private String providerNo;
	private Date updateDate;
	private String dischargeReason;
	private String functionalCentreDescription;
	private String functionalCentre;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(Integer demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public String getFunctionalCentreId() {
    	return functionalCentreId;
    }

	public void setFunctionalCentreId(String functionalCentreId) {
    	this.functionalCentreId = functionalCentreId;
    }

	public String getReferralDate() {
    	return referralDate;
    }

	public void setReferralDate(String referralDate) {
    	this.referralDate = referralDate;
    }

	public String getAdmissionDate() {
    	return admissionDate;
    }

	public void setAdmissionDate(String admissionDate) {
    	this.admissionDate = admissionDate;
    }

	public String getServiceInitiationDate() {
    	return serviceInitiationDate;
    }	
	
	public void setServiceInitiationDate(String serviceInitiationDate) {
    	this.serviceInitiationDate = serviceInitiationDate;
    }
	
	public String getDischargeDate() {
    	return dischargeDate;
    }

	public void setDischargeDate(String dischargeDate) {
    	this.dischargeDate = dischargeDate;
    }	
	
	public boolean isDischarged() {
    	return discharged;
    }

	public void setDischarged(boolean discharged) {
    	this.discharged = discharged;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public Date getUpdateDate() {
    	return updateDate;
    }

	public void setUpdateDate(Date updateDate) {
    	this.updateDate = updateDate;
    }

	public String getDischargeReason() {
    	return dischargeReason;
    }

	public void setDischargeReason(String dischargeReason) {
    	this.dischargeReason = dischargeReason;
    }

	public String getFunctionalCentreDescription() {
    	return functionalCentreDescription;
    }

	public void setFunctionalCentreDescription(String functionalCentreDescription) {
    	this.functionalCentreDescription = functionalCentreDescription;
    }

	public String getFunctionalCentre() {
    	return functionalCentre;
    }

	public void setFunctionalCentre(String functionalCentre) {
    	this.functionalCentre = functionalCentre;
    }
	
}
