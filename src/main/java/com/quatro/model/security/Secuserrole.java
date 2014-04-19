/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 */

package com.quatro.model.security;

import java.util.Date;


public class Secuserrole implements java.io.Serializable {

	// Fields

	private Integer id;
	private String providerNo;
	private String roleName;
	private String orgcd;
	private Integer activeyn;
	// added extra 
	private String roleName_desc;
	private String orgcd_desc;
	private String providerName;
	private String providerLName;
	private String providerFName;
		
	// added more
	private String fullName;
	private String userName;
	private String lastUpdateUser;
	private Date lastUpdateDate;
	
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}


	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}


	public String getLastUpdateUser() {
		return lastUpdateUser;
	}


	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}


	/** default constructor */
	public Secuserrole() {
		
	}

	
	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProviderNo() {
		return this.providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getOrgcd() {
		return this.orgcd;
	}

	public void setOrgcd(String orgcd) {
		this.orgcd = orgcd;
	}

	public Integer getActiveyn() {
		return this.activeyn;
	}

	public void setActiveyn(Integer activeyn) {
		this.activeyn = activeyn;
	}

	public String getOrgcd_desc() {
		return orgcd_desc;
	}

	public void setOrgcd_desc(String orgcd_desc) {
		this.orgcd_desc = orgcd_desc;
	}

	public String getRoleName_desc() {
		return roleName_desc;
	}

	public void setRoleName_desc(String roleName_desc) {
		this.roleName_desc = roleName_desc;
	}

	public String getProviderName() {
		if(providerName == null || providerName.length() <= 0){
			if(providerFName != null)
				providerName = providerLName + ", " + providerFName;
			else
				providerName = providerLName;
		}
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getProviderFName() {
		return providerFName;
	}

	public void setProviderFName(String providerFName) {
		this.providerFName = providerFName;
	}

	public String getProviderLName() {
		return providerLName;
	}

	public void setProviderLName(String providerLName) {
		this.providerLName = providerLName;
	}
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
