/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
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
	
	//the full history is not captured here...but if signedOff=1, the provider that did it will be listed here
	//and if the signOff=0 and signOffProvider!=null , then this holds the value of the provider who did that sign-off revocation.
	//the full solution would be to archive this table, or setup a 1-many relationship to capture full history. 
	//all sign off/revocation is captured in the audit log as well.
	private String signOffProvider;
	
	
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

	public String getSignOffProvider() {
		return signOffProvider;
	}

	public void setSignOffProvider(String signOffProvider) {
		this.signOffProvider = signOffProvider;
	}
	
	
}
