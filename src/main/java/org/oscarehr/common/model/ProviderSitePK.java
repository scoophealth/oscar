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

import javax.persistence.Column;


public class ProviderSitePK implements java.io.Serializable  {

	@Column(name="provider_no")
	private String providerNo;
	@Column(name="site_id")
	private int siteId;

	public ProviderSitePK() {

	}

	public ProviderSitePK(String providerNo, int siteId) {
		this.providerNo = providerNo;
		this.siteId = siteId;
	}

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public int getSiteId() {
    	return siteId;
    }

	public void setSiteId(int siteId) {
    	this.siteId = siteId;
    }


	public String toString() {
		return ("ProviderNo=" + providerNo + ", siteId=" + siteId);
	}

	@Override
	public int hashCode() {
		return (toString().hashCode());
	}

	@Override
	public boolean equals(Object o) {
		try {
			ProviderSitePK o1 = (ProviderSitePK) o;
			return ((providerNo.equals(o1.providerNo)) && (siteId == o1.siteId));
		} catch (RuntimeException e) {
			return (false);
		}
	}
}
