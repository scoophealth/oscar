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
package org.oscarehr.ws.transfer_objects;

import java.util.Calendar;

/**
 * The purpose of this object is to transfer the ID of a piece of any piece of medical data in the oscar system.
 * 
 * In theory all pieces of medical data should have the following fields. In reality oscar is currently missing some of the fields. Expect null in some cases.
 */
public final class DataIdTransfer {
	
	private Integer facilityId;
	private Integer clinicId;
	private String creatorProviderId;
	private Integer ownerDemographicId;
	private String dataType;
	private String dataId;
	private Calendar createDate;

	public Integer getFacilityId() {
		return (facilityId);
	}

	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}

	public Integer getClinicId() {
		return (clinicId);
	}

	public void setClinicId(Integer clinicId) {
		this.clinicId = clinicId;
	}

	public String getCreatorProviderId() {
		return (creatorProviderId);
	}

	public void setCreatorProviderId(String creatorProviderId) {
		this.creatorProviderId = creatorProviderId;
	}

	public Integer getOwnerDemographicId() {
		return (ownerDemographicId);
	}

	public void setOwnerDemographicId(Integer ownerDemographicId) {
		this.ownerDemographicId = ownerDemographicId;
	}

	public String getDataType() {
		return (dataType);
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataId() {
		return (dataId);
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public Calendar getCreateDate() {
		return (createDate);
	}

	public void setCreateDate(Calendar createDate) {
		this.createDate = createDate;
	}
}
