/*
 * 
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for 
 * Centre for Research on Inner City Health, St. Michael's Hospital, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.common.model;

import java.util.Date;
import java.util.zip.CRC32;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This object is to help support tracking which fields in the demographic object have been 
 * marked as "valid" by an end user. The original intent is because only "validated" information
 * is allowed to be sent to the HNR and part of the scope of work was to track 
 * who validated which fields and when. To keep track of what is validated and to help nullify the
 * validation upon change of data, we're going to use a CRC32 on the data being validated. The reason
 * is because there's currently no reliable way of hooking into when some of this data is updated
 * so short of making a copy of all the data when validation occurs... this seemed like the only 
 * other reasonable alternative. 
 */
@Entity
public class HnrDataValidation {

	public enum Type {
		PICTURE, HC_INFO, OTHER
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id = null;
	private Integer facilityId = null;
	private Integer clientId = null;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created = null;
	/** this is the provider id who validated this piece of data */
	private String validatorProviderNo = null;
	private boolean valid = false;
	@Enumerated(EnumType.STRING)
	private Type validationType=null;
	private Long validationCrc=null;

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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getValidatorProviderNo() {
		return validatorProviderNo;
	}

	public void setValidatorProviderNo(String validatorProviderNo) {
		this.validatorProviderNo = validatorProviderNo;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Integer getId() {
		return id;
	}

	public Type getValidationType() {
    	return validationType;
    }

	public void setValidationType(Type validationType) {
    	this.validationType = validationType;
    }

	public Long getValidationCrc() {
    	return validationCrc;
    }

	public void setValidationCrc(Long validationCrc) {
    	this.validationCrc = validationCrc;
    }

	public void setValidationCrc(byte[] b) {
    	CRC32 crc32=new CRC32();
    	crc32.update(b);
    	setValidationCrc(crc32.getValue());
    }
	
	public boolean isMatchingCrc(byte[] b) {
    	CRC32 crc32=new CRC32();
    	crc32.update(b);
    	
    	return(crc32.equals(validationCrc));
	}

	@PreRemove
	protected void jpa_preventDelete() {
		throw (new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}

	@PreUpdate
	protected void jpa_preventUpdate() {
		throw (new UnsupportedOperationException("Update is not allowed for this type of item."));
	}
}
