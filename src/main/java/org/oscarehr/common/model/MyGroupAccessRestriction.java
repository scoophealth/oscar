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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class MyGroupAccessRestriction extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String myGroupNo;
	
	private String providerNo;
	
	private String lastUpdateUser;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateDate;
	

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

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getMyGroupNo() {
    	return myGroupNo;
    }

	public void setMyGroupNo(String myGroupNo) {
    	this.myGroupNo = myGroupNo;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }
	
	@PrePersist @PreUpdate
	public void setupPreSave() {
		lastUpdateDate = new Date();
	}
	
}
