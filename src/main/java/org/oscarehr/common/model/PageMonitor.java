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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class PageMonitor extends AbstractModel<Integer> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer	id;
	
	private String pageName;
        
        private String pageId;
	
	private String session;
	
	private String remoteAddr;
	
	private boolean locked;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	private int timeout;
	
	private String providerNo;
	
	private String providerName;
	
	@Transient
	private boolean self;
	
	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getPageName() {
    	return pageName;
    }

	public void setPageName(String pageName) {
    	this.pageName = pageName;
    }
        
        public String getPageId() {
            return pageId;
        }
        
        public void setPageId(String pageId) {
            this.pageId = pageId;
        }

	public String getSession() {
    	return session;
    }

	public void setSession(String session) {
    	this.session = session;
    }

	public String getRemoteAddr() {
    	return remoteAddr;
    }

	public void setRemoteAddr(String remoteAddr) {
    	this.remoteAddr = remoteAddr;
    }

	public Date getUpdateDate() {
    	return updateDate;
    }

	public void setUpdateDate(Date updateDate) {
    	this.updateDate = updateDate;
    }

	public boolean isLocked() {
    	return locked;
    }

	public void setLocked(boolean locked) {
    	this.locked = locked;
    }

	public int getTimeout() {
    	return timeout;
    }

	public void setTimeout(int timeout) {
    	this.timeout = timeout;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public String getProviderName() {
    	return providerName;
    }

	public void setProviderName(String providerName) {
    	this.providerName = providerName;
    }

	public boolean isSelf() {
    	return self;
    }

	public void setSelf(boolean self) {
    	this.self = self;
    }
	
	

}
