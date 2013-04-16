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
public class HRMDocumentToDemographic extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String demographicNo;
	private String hrmDocumentId;
	private Date timeAssigned;
	
	@Override
    public Integer getId() {
	    return id;
    }

	public String getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(String demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public Date getTimeAssigned() {
    	return timeAssigned;
    }

	public void setTimeAssigned(Date timeAssigned) {
    	this.timeAssigned = timeAssigned;
    }

	public String getHrmDocumentId() {
    	return hrmDocumentId;
    }

	public void setHrmDocumentId(String hrmDocumentId) {
    	this.hrmDocumentId = hrmDocumentId;
    }

}
