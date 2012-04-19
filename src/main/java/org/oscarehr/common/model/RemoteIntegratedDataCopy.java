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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class RemoteIntegratedDataCopy extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "demographic_no")
	private Integer demographicNo;

	private String dataType;
	private String data;
	private String signature;
	
	private Integer facilityId;
	
	@Column(name="provider_no")
	private String providerNo;
		
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateDate;
	
	private boolean archived;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(Integer demographic_no) {
		this.demographicNo = demographic_no;
	}


	public Date getLastUpdateDate() {
    	return (lastUpdateDate);
    }

	public void setLastUpdateDate(Date lastUpdateDate) {
    	this.lastUpdateDate = lastUpdateDate;
    }
	
	public String getDataType() {
    	return dataType;
    }

	public void setDataType(String datatype) {
    	this.dataType = datatype;
    }

	public String getData() {
    	return data;
    }

	public void setData(String data) {
    	this.data = data;
    }
	
	public String getSignature() {
    	return signature;
    }

	public void setSignature(String signature) {
    	this.signature = signature;
    }

	public void setObject(Object obj){
		
		this.dataType = obj.getClass().getCanonicalName();
		
	}


	public Integer getFacilityId() {
    	return facilityId;
    }

	public void setFacilityId(Integer facilityId) {
    	this.facilityId = facilityId;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public boolean isArchived() {
    	return archived;
    }

	public void setArchived(boolean archived) {
    	this.archived = archived;
    }

	@PreUpdate
	@PrePersist
	protected void autoSetUpdateTime()
	{
		lastUpdateDate=new Date();
	}
}
