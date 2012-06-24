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

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ClientLink extends AbstractModel<Integer> {

	/**
	 * As a special mention, the OSCAR_CAISI links are not actually currently saved locally due to
	 * privacy reasons, they are stored on the integrator. This may change in the future. This 
	 * DAO structure still holds valid though as we will have more datasources coming online 
	 * shortly which will also require linking. 
	 */
	public enum Type {
		HNR, OSCAR_CAISI
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;
	private Integer facilityId = null;
	private Integer clientId = null;
	@Enumerated(EnumType.STRING)
	private Type linkType = null;
	private Integer remoteLinkId = null;
	@Temporal(TemporalType.TIMESTAMP)
	private Date linkDate = null;
	/** this is the provider id who linked the client */
	private String linkProviderNo = null;
	@Temporal(TemporalType.TIMESTAMP)
	private Date unlinkDate = null;
	/** this is the provider id who unlinked the client */
	private String unlinkProviderNo = null;

	public Integer getClientId() {
		return clientId;
	}

	public Integer getFacilityId() {
    	return facilityId;
    }

	public void setFacilityId(Integer facilityId) {
    	this.facilityId = facilityId;
    }

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Type getLinkType() {
		return linkType;
	}

	public void setLinkType(Type linkType) {
		this.linkType = linkType;
	}

	public Integer getRemoteLinkId() {
		return remoteLinkId;
	}

	public void setRemoteLinkId(Integer remoteLinkId) {
		this.remoteLinkId = remoteLinkId;
	}

	public Date getLinkDate() {
		return linkDate;
	}

	public void setLinkDate(Date linkDate) {
		this.linkDate = linkDate;
	}

	public Date getUnlinkDate() {
		return unlinkDate;
	}

	public void setUnlinkDate(Date unlinkDate) {
		this.unlinkDate = unlinkDate;
	}

	public String getLinkProviderNo() {
		return linkProviderNo;
	}

	public void setLinkProviderNo(String linkProviderNo) {
		this.linkProviderNo = linkProviderNo;
	}

	public String getUnlinkProviderNo() {
		return unlinkProviderNo;
	}

	public void setUnlinkProviderNo(String unlinkProviderNo) {
		this.unlinkProviderNo = unlinkProviderNo;
	}

	public Integer getId() {
		return id;
	}
}
