/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package com.indivica.olis.queries;

import java.util.Date;

import com.indivica.olis.parameters.ZPD1;

public abstract class Query implements Cloneable {

	private Date queryExecutionDate;
	private String initiatingProviderNo;
	private String uuid;
	
	public String demographicNo;
	
	public String getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(String demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public abstract String getQueryHL7String();
	
	public abstract QueryType getQueryType();
	
	public abstract void setConsentToViewBlockedInformation(ZPD1 consentToViewBlockedInformation);
	
	 public Object clone() {
	    try {
	        return super.clone();
	    }
	    catch (CloneNotSupportedException e) {
	        throw new InternalError(e.toString());
	    }
	}	
	 
	 
	 public String getRequestingHICProviderNo() {
		 if(this instanceof Z04Query) {
			 return ((Z04Query)this).getRequestingHicIdNumber();
		 }
		 if(this instanceof Z01Query) {
			 return ((Z01Query)this).getRequestingHicIdNumber();
		 }
		 return null;
	 }

	public Date getQueryExecutionDate() {
		return queryExecutionDate;
	}

	public void setQueryExecutionDate(Date queryExecutionDate) {
		this.queryExecutionDate = queryExecutionDate;
	}

	public String getInitiatingProviderNo() {
		return initiatingProviderNo;
	}

	public void setInitiatingProviderNo(String initiatingProviderNo) {
		this.initiatingProviderNo = initiatingProviderNo;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	 
	
}
