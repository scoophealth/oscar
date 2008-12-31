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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreUpdate;

@Entity
public class ClientLink {

	public enum Type {
		HNR, OSCAR_CAISI
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id = null;
	private Integer clientId = null;
	private Type linkType = null;
	private Integer remoteLinkId = null;
	private Date creationDate = new Date();
	private String creatorProviderId = null;
	private boolean active=true;

	public Integer getClientId() {
		return clientId;
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatorProviderId() {
		return creatorProviderId;
	}

	public void setCreatorProviderId(String creatorProviderId) {
		this.creatorProviderId = creatorProviderId;
	}

	public Integer getId() {
		return id;
	}

	public boolean isActive() {
    	return active;
    }

	public void setActive(boolean active) {
    	this.active = active;
    }

	@PreUpdate
	protected void jpa_preventUpdate()
	{
		throw(new IllegalArgumentException("Update is not allowed for this type of item."));
	}
}
