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
@Table(name="labReportInformation")
public class LabReportInformation extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="location_id")
	private String locationId;

	@Column(name="print_date")
	private String printDate;

	@Column(name="print_time")
	private String printTime;

	@Column(name="total_BType")
	private String totalBType;

	@Column(name="total_CType")
	private String totalCType;

	@Column(name="total_DType")
	private String totalDType;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getLocationId() {
    	return locationId;
    }

	public void setLocationId(String locationId) {
    	this.locationId = locationId;
    }

	public String getPrintDate() {
    	return printDate;
    }

	public void setPrintDate(String printDate) {
    	this.printDate = printDate;
    }

	public String getPrintTime() {
    	return printTime;
    }

	public void setPrintTime(String printTime) {
    	this.printTime = printTime;
    }

	public String getTotalBType() {
    	return totalBType;
    }

	public void setTotalBType(String totalBType) {
    	this.totalBType = totalBType;
    }

	public String getTotalCType() {
    	return totalCType;
    }

	public void setTotalCType(String totalCType) {
    	this.totalCType = totalCType;
    }

	public String getTotalDType() {
    	return totalDType;
    }

	public void setTotalDType(String totalDType) {
    	this.totalDType = totalDType;
    }


}
