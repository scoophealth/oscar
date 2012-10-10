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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="labTestResults")
public class LabTestResults extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="labPatientPhysicianInfo_id")
	private int labPatientPhysicianInfoId;

	@Column(name="line_type")
	private String lineType;

	private String title;

	private String notUsed1;

	private String notUsed2;

	@Column(name="test_name")
	private String testName;

	private String abn;

	private String minimum;

	private String maximum;

	private String units;

	private String result;

	private String description;

	@Column(name="location_id")
	private String locationId;

	private String last;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getLabPatientPhysicianInfoId() {
    	return labPatientPhysicianInfoId;
    }

	public void setLabPatientPhysicianInfoId(int labPatientPhysicianInfoId) {
    	this.labPatientPhysicianInfoId = labPatientPhysicianInfoId;
    }

	public String getLineType() {
    	return lineType;
    }

	public void setLineType(String lineType) {
    	this.lineType = lineType;
    }

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNotUsed1() {
    	return notUsed1;
    }

	public void setNotUsed1(String notUsed1) {
    	this.notUsed1 = notUsed1;
    }

	public String getNotUsed2() {
    	return notUsed2;
    }

	public void setNotUsed2(String notUsed2) {
    	this.notUsed2 = notUsed2;
    }

	public String getTestName() {
    	return testName;
    }

	public void setTestName(String testName) {
    	this.testName = testName;
    }

	public String getAbn() {
    	return abn;
    }

	public void setAbn(String abn) {
    	this.abn = abn;
    }

	public String getMinimum() {
    	return minimum;
    }

	public void setMinimum(String minimum) {
    	this.minimum = minimum;
    }

	public String getMaximum() {
    	return maximum;
    }

	public void setMaximum(String maximum) {
    	this.maximum = maximum;
    }

	public String getUnits() {
    	return units;
    }

	public void setUnits(String units) {
    	this.units = units;
    }

	public String getResult() {
    	return result;
    }

	public void setResult(String result) {
    	this.result = result;
    }

	public String getDescription() {
    	return description;
    }

	public void setDescription(String description) {
    	this.description = description;
    }

	public String getLocationId() {
    	return locationId;
    }

	public void setLocationId(String locationId) {
    	this.locationId = locationId;
    }

	public String getLast() {
    	return last;
    }

	public void setLast(String last) {
    	this.last = last;
    }


}
