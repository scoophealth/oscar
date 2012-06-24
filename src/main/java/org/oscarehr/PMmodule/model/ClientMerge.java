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

package org.oscarehr.PMmodule.model;

import java.io.Serializable;
import java.util.Calendar;

public class ClientMerge implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer clientId;
	private Integer mergedToClientId;
	private String providerNo;
	private boolean deleted;
	private Calendar lastUpdateDate;
	private String clientFirstName;
	private String clientLastName;
	private String mergedClientFirstName;
	private String mergedClientLastName;
	private String providerFirstName;
	private String providerLastName;
	
	
	private int hashCode = Integer.MIN_VALUE;
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Calendar getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Calendar lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public Integer getMergedToClientId() {
		return mergedToClientId;
	}
	public void setMergedToClientId(Integer mergedToClientId) {
		this.mergedToClientId = mergedToClientId;
	}
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	public boolean equals(Object obj) {
    	if (null == obj) return false;
    	if (!(obj instanceof org.oscarehr.PMmodule.model.ClientMerge)) return false;
    	else {
    		org.oscarehr.PMmodule.model.ClientMerge cmObj = (org.oscarehr.PMmodule.model.ClientMerge) obj;
    		if (null == this.getId() || null == cmObj.getId()) return false;
    		else return (this.getId().equals(cmObj.getId()));
    	}
    }

	public int hashCode() {
    	if (Integer.MIN_VALUE == this.hashCode) {
    		if (null == this.getId()) return super.hashCode();
    		else {
    			String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
    			this.hashCode = hashStr.hashCode();
    		}
    	}
    	return this.hashCode;
    }

	public String toString() {
    	return super.toString();
    }
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public String getClientFirstName() {
		return clientFirstName;
	}
	public void setClientFirstName(String clientFirstName) {
		this.clientFirstName = clientFirstName;
	}
	public String getClientLastName() {
		return clientLastName;
	}
	public void setClientLastName(String clientLastName) {
		this.clientLastName = clientLastName;
	}
	public String getMergedClientFirstName() {
		return mergedClientFirstName;
	}
	public void setMergedClientFirstName(String mergedClientFirstName) {
		this.mergedClientFirstName = mergedClientFirstName;
	}
	public String getMergedClientLastName() {
		return mergedClientLastName;
	}
	public void setMergedClientLastName(String mergedClientLastName) {
		this.mergedClientLastName = mergedClientLastName;
	}
	public String getProviderFirstName() {
		return providerFirstName;
	}
	public void setProviderFirstName(String providerFirstName) {
		this.providerFirstName = providerFirstName;
	}
	public String getProviderLastName() {
		return providerLastName;
	}
	public void setProviderLastName(String providerLastName) {
		this.providerLastName = providerLastName;
	}
	 public String getClientFormattedName() {
	        return getClientLastName() + ", " + getClientFirstName();
	    }
	 public String getMergedToClientFormattedName() {
	        return getMergedClientLastName() + ", " + getMergedClientFirstName();
	    }
	 public String getProviderFormattedName() {
	        return getProviderLastName()  + ", " + getProviderFirstName();
	    }
}
