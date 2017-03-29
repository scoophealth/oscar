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
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This entity represents every time a provider fills out or updates a CDS form.
 * Note that every row / entry represents a change, so as an example if 
 * one provider answers the first 4 questions, then saves it. It will make a entry.
 * If another provider then updates it to answer another 2 questions, this should
 * make a 2nd row. This allows us to track who changed what on the form and when. 
 * As a result, these entities are non delete/update able, the expectation is to
 * make a new entity instead of updating an existing one.
 */
@Entity
public class CdsClientForm extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String cdsFormVersion=null;
	private String providerNo = null;
	private boolean signed=false;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created = new Date();
	
	private Integer facilityId=null;
	private Integer clientId=null;
	private Integer admissionId=null;
	
	@Temporal(TemporalType.DATE)
	private Date initialContactDate;

	@Temporal(TemporalType.DATE)
	private Date assessmentDate;

	@Temporal(TemporalType.DATE)
	private Date serviceInitiationDate;
	
	@Override
    public Integer getId() {
		return id;
	}

	public Date getCreated() {
		return created;
	}

	public String getCdsFormVersion() {
    	return cdsFormVersion;
    }

	public void setCdsFormVersion(String cdsFormVersion) {
    	this.cdsFormVersion = cdsFormVersion;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
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

	public Date getInitialContactDate() {
    	return initialContactDate;
    }

	public void setInitialContactDate(Date initialContactDate) {
    	this.initialContactDate = initialContactDate;
    }

	public Date getAssessmentDate() {
    	return assessmentDate;
    }

	public void setAssessmentDate(Date assessmentDate) {
    	this.assessmentDate = assessmentDate;
    }
	
	
	public Date getServiceInitiationDate() {
    	return serviceInitiationDate;
    }

	public void setServiceInitiationDate(Date serviceInitiationDate) {
    	this.serviceInitiationDate = serviceInitiationDate;
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
		CdsClientForm other = (CdsClientForm) obj;
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

	/* allow update cds form with correct admission date, referral date, service initialization date.
	@PreUpdate
	protected void jpaPreventUpdate()
	{
		throw(new UnsupportedOperationException("Update is not allowed for this type of item."));
	}
	*/
}
