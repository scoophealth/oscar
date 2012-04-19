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

import java.util.Date;

public class LabTest {

	private Long id;
	private Date date;
	private String name;
	private String description;
	private String codeType;
	private String code;
	private String codeValue;
	private String codeUnit;
	private String refRangeLow;
	private String refRangeHigh;
	private String refRangeText;
	private String flag;
	private String stat;
	private String notes;
	
	public Long getId() {
    	return id;
    }
	public void setId(Long id) {
    	this.id = id;
    }
	public Date getDate() {
    	return date;
    }
	public void setDate(Date date) {
    	this.date = date;
    }
	public String getName() {
    	return name;
    }
	public void setName(String name) {
    	this.name = name;
    }
	public String getDescription() {
    	return description;
    }
	public void setDescription(String description) {
    	this.description = description;
    }
	public String getCodeType() {
    	return codeType;
    }
	public void setCodeType(String codeType) {
    	this.codeType = codeType;
    }
	public String getCode() {
    	return code;
    }
	public void setCode(String code) {
    	this.code = code;
    }
	public String getCodeValue() {
    	return codeValue;
    }
	public void setCodeValue(String codeValue) {
    	this.codeValue = codeValue;
    }
	public String getCodeUnit() {
    	return codeUnit;
    }
	public void setCodeUnit(String codeUnit) {
    	this.codeUnit = codeUnit;
    }
	public String getRefRangeLow() {
    	return refRangeLow;
    }
	public void setRefRangeLow(String refRangeLow) {
    	this.refRangeLow = refRangeLow;
    }
	public String getRefRangeHigh() {
    	return refRangeHigh;
    }
	public void setRefRangeHigh(String refRangeHigh) {
    	this.refRangeHigh = refRangeHigh;
    }
	public String getRefRangeText() {
    	return refRangeText;
    }
	public void setRefRangeText(String refRangeText) {
    	this.refRangeText = refRangeText;
    }
	public String getFlag() {
    	return flag;
    }
	public void setFlag(String flag) {
    	this.flag = flag;
    }
	public String getStat() {
    	return stat;
    }
	public void setStat(String stat) {
    	this.stat = stat;
    }
	public String getNotes() {
    	return notes;
    }
	public void setNotes(String notes) {
    	this.notes = notes;
    }
	
	
}
