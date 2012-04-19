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


package org.oscarehr.hospitalReportManager.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.oscarehr.common.model.AbstractModel;

@Entity
public class HRMDocumentToProvider extends AbstractModel<Integer>  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String providerNo;
	private String hrmDocumentId;
	private Integer signedOff =0;
	private Date signedOffTimestamp;
	private Integer viewed = 0;
	
	@Override
    public Integer getId() {
	    return id;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public Integer getSignedOff() {
    	return signedOff;
    }

	public void setSignedOff(Integer signedOff) {
    	this.signedOff = signedOff;
    }

	public Date getSignedOffTimestamp() {
    	return signedOffTimestamp;
    }

	public void setSignedOffTimestamp(Date signedOffTimestamp) {
    	this.signedOffTimestamp = signedOffTimestamp;
    }

	public String getHrmDocumentId() {
    	return hrmDocumentId;
    }

	public void setHrmDocumentId(String hrmDocumentId) {
    	this.hrmDocumentId = hrmDocumentId;
    }

	public Integer getViewed() {
    	return viewed;
    }

	public void setViewed(Integer viewed) {
    	this.viewed = viewed;
    }
	
	
}
