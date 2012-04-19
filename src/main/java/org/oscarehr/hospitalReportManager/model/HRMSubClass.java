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


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.oscarehr.common.model.AbstractModel;

@Entity
public class HRMSubClass extends AbstractModel<Integer> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String className;
	private String subClassName;
	private String subClassMnemonic;
	private String subClassDescription;
	private String sendingFacilityId;
	
	@ManyToOne
	@JoinColumn(name="hrmCategoryId")
	private HRMCategory hrmCategory;
	
	public HRMSubClass() {	}
	
	@Override
    public Integer getId() {
	    return id;
    }

	public String getClassName() {
    	return className;
    }

	public void setClassName(String className) {
    	this.className = className;
    }

	public String getSubClassName() {
    	return subClassName;
    }

	public void setSubClassName(String subClassName) {
    	this.subClassName = subClassName;
    }

	public HRMCategory getHrmCategory() {
    	return hrmCategory;
    }

	public void setHrmCategory(HRMCategory hrmCategory) {
    	this.hrmCategory = hrmCategory;
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

	public String getSendingFacilityId() {
    	return sendingFacilityId;
    }

	public void setSendingFacilityId(String sendingFacilityId) {
    	this.sendingFacilityId = sendingFacilityId;
    }


	

}
