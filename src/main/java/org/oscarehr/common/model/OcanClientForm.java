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
import javax.persistence.PreUpdate;
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
public class OcanClientForm extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String ocanFormVersion=null;
	private String providerNo = null;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date created = new Date();
	
	private Integer facilityId=null;
	private Integer clientId=null;
	
	private String lastName;
	private String firstName;
	
	private String dateOfBirth;
	
	private Date startDate;
	private Date completionDate;
	
	private String assessmentStatus;
	
	private String providerName;
	
	public OcanClientForm() {		
	}
	
	public Integer getId() {
		return id;
	}

	public Date getCreated() {
		return created;
	}

	public String getOcanFormVersion() {
    	return ocanFormVersion;
    }

	public void setOcanFormVersion(String cdsFormVersion) {
    	this.ocanFormVersion = cdsFormVersion;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
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
		OcanClientForm other = (OcanClientForm) obj;
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

	@PreUpdate
	protected void jpaPreventUpdate()
	{
		throw(new UnsupportedOperationException("Update is not allowed for this type of item."));
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setCreated(Date created) {
		this.created = created;
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

	public String getAssessmentStatus() {
		return assessmentStatus;
	}

	public void setAssessmentStatus(String assessmentStatus) {
		this.assessmentStatus = assessmentStatus;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

}
