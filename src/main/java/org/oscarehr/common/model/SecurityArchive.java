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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class SecurityArchive extends AbstractModel<Integer> {

	public SecurityArchive() {
	}
	
	public SecurityArchive(Security s) {
		setSecurityNo(s.getId());
		setUserName(s.getUserName());
		setPassword(s.getPassword());
		setProviderNo(s.getProviderNo());
		setPin(s.getPin());
		setBExpireset(s.getBExpireset());
		setDateExpiredate(s.getDateExpiredate());
		setBLocallockset(s.getBLocallockset());
		setBRemotelockset(s.getBRemotelockset());
		setForcePasswordReset(s.isForcePasswordReset());
		setStorageVersion(s.getStorageVersion());
		setPasswordUpdateDate(s.getPasswordUpdateDate());
		setPinUpdateDate(s.getPinUpdateDate());
		setLastUpdateUser(s.getLastUpdateUser());
		setLastUpdateDate(s.getLastUpdateDate());
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "security_no")
	private Integer securityNo;
	@Column(name = "user_name",nullable=false)
	private String userName;
	
	@Column(name = "password",nullable=false)
	private String password;
	
	@Column(name = "provider_no",nullable=false)
	private String providerNo;

	@Column(name = "pin")
	private String pin;
	
	@Column(name = "b_ExpireSet")
	private Integer BExpireset;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_ExpireDate")
	private Date dateExpiredate;

	@Column(name = "b_LocalLockSet")
	private Integer BLocallockset;

	@Column(name = "b_RemoteLockSet")
	private Integer BRemotelockset;


	@Column(name="forcePasswordReset")
	private Boolean forcePasswordReset;

	private int storageVersion = 2;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date passwordUpdateDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date pinUpdateDate;
	
	private String lastUpdateUser;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateDate;
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getSecurityNo() {
		return securityNo;
	}


	public void setSecurityNo(Integer securityNo) {
		this.securityNo = securityNo;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getProviderNo() {
		return providerNo;
	}


	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}


	public String getPin() {
		return pin;
	}


	public void setPin(String pin) {
		this.pin = pin;
	}


	public Integer getBExpireset() {
		return BExpireset;
	}


	public void setBExpireset(Integer bExpireset) {
		BExpireset = bExpireset;
	}


	public Date getDateExpiredate() {
		return dateExpiredate;
	}


	public void setDateExpiredate(Date dateExpiredate) {
		this.dateExpiredate = dateExpiredate;
	}


	public Integer getBLocallockset() {
		return BLocallockset;
	}


	public void setBLocallockset(Integer bLocallockset) {
		BLocallockset = bLocallockset;
	}


	public Integer getBRemotelockset() {
		return BRemotelockset;
	}


	public void setBRemotelockset(Integer bRemotelockset) {
		BRemotelockset = bRemotelockset;
	}


	public Boolean getForcePasswordReset() {
		return forcePasswordReset;
	}


	public void setForcePasswordReset(Boolean forcePasswordReset) {
		this.forcePasswordReset = forcePasswordReset;
	}

	public int getStorageVersion() {
		return storageVersion;
	}

	public void setStorageVersion(int storageVersion) {
		this.storageVersion = storageVersion;
	}

	public Date getPasswordUpdateDate() {
		return passwordUpdateDate;
	}

	public void setPasswordUpdateDate(Date passwordUpdateDate) {
		this.passwordUpdateDate = passwordUpdateDate;
	}

	public Date getPinUpdateDate() {
		return pinUpdateDate;
	}

	public void setPinUpdateDate(Date pinUpdateDate) {
		this.pinUpdateDate = pinUpdateDate;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	
}
