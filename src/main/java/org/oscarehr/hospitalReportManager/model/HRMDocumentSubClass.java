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
public class HRMDocumentSubClass extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer hrmDocumentId;
	private String subClass;
	private String subClassMnemonic;
	private String subClassDescription;
	private Date subClassDateTime;
	private boolean isActive;
	
	@Override
    public Integer getId() {
	    return id;
    }

	public Integer getHrmDocumentId() {
    	return hrmDocumentId;
    }

	public void setHrmDocumentId(Integer hrmDocumentId) {
    	this.hrmDocumentId = hrmDocumentId;
    }

	public String getSubClass() {
    	return subClass;
    }

	public void setSubClass(String subClass) {
    	this.subClass = subClass;
    }

	public String getSubClassMnemonic() {
    	return subClassMnemonic;
    }

	public void setSubClassMnemonic(String subClassMnemonic) {
    	this.subClassMnemonic = subClassMnemonic;
    }

	public String getSubClassDescription() {
    	return subClassDescription;
    }

	public void setSubClassDescription(String subClassDescription) {
    	this.subClassDescription = subClassDescription;
    }

	public Date getSubClassDateTime() {
    	return subClassDateTime;
    }

	public void setSubClassDateTime(Date subClassDateTime) {
    	this.subClassDateTime = subClassDateTime;
    }

	public boolean isActive() {
    	return isActive;
    }

	public void setActive(boolean isActive) {
    	this.isActive = isActive;
    }
	

}
