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
@Table(name="mdsZLB")
public class MdsZLB extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	@Column(name="labID")
	private String labId;

	@Column(name="labIDVersion")
	private String labIdVersion;

	private String labAddress;

	private String primaryLab;

	private String primaryLabVersion;

	@Column(name="MDSLU")
	private String mdsLU;

	@Column(name="MDSLV")
	private String mdsLV;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getLabId() {
    	return labId;
    }

	public void setLabId(String labId) {
    	this.labId = labId;
    }

	public String getLabIdVersion() {
    	return labIdVersion;
    }

	public void setLabIdVersion(String labIdVersion) {
    	this.labIdVersion = labIdVersion;
    }

	public String getLabAddress() {
    	return labAddress;
    }

	public void setLabAddress(String labAddress) {
    	this.labAddress = labAddress;
    }

	public String getPrimaryLab() {
    	return primaryLab;
    }

	public void setPrimaryLab(String primaryLab) {
    	this.primaryLab = primaryLab;
    }

	public String getPrimaryLabVersion() {
    	return primaryLabVersion;
    }

	public void setPrimaryLabVersion(String primaryLabVersion) {
    	this.primaryLabVersion = primaryLabVersion;
    }

	public String getMdsLU() {
    	return mdsLU;
    }

	public void setMdsLU(String mdsLU) {
    	this.mdsLU = mdsLU;
    }

	public String getMdsLV() {
    	return mdsLV;
    }

	public void setMdsLV(String mdsLV) {
    	this.mdsLV = mdsLV;
    }


}
