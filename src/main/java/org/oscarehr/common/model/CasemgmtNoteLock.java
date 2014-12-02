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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * The persistent class for the casemgmt_note_lock database table.
 * 
 */
@Entity
@Table(name="casemgmt_note_lock")
public class CasemgmtNoteLock extends AbstractModel<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Long id;

	@Column(name="ip_address", length=64)
	private String ipAddress;

	@Column(name="note_id")
	private Long noteId;
	
	@Column(name="provider_no", length=6)
	private String providerNo;
	
	@Column(name="demographic_no")
	Integer demographicNo;
	
	@Column(name="session_id")
	private String sessionId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="lock_acquired")
	Date lockAcquired;
	
	@Transient
	private boolean locked = false;
	
	@Transient
	private boolean lockedBySameUser = false;

    public CasemgmtNoteLock() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Long getNoteId() {
		return this.noteId;
	}

	public void setNoteId(Long noteId) {
		this.noteId = noteId;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	/**
	 * @return the demographicNo
	 */
	public Integer getDemographicNo() {
		return demographicNo;
	}

	/**
	 * @param demographicNo the demographicNo to set
	 */
	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}

	/**
	 * @return the lockedBySameUser
	 */
	public boolean isLockedBySameUser() {
		return lockedBySameUser;
	}

	/**
	 * @param lockedBySameUser the lockedBySameUser to set
	 */
	public void setLockedBySameUser(boolean lockedBySameUser) {
		this.lockedBySameUser = lockedBySameUser;
	}

	/**
	 * @return the lockAcquired
	 */
	public Date getLockAcquired() {
		return lockAcquired;
	}

	/**
	 * @param lockAcquired the lockAcquired to set
	 */	
	public void setLockAcquired(Date lockAcquired) {
		this.lockAcquired = lockAcquired;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}