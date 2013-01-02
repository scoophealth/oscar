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
@Table(name="mdsZRG")
public class MdsZRG extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	private String reportSequence;

	@Column(name="reportGroupID")
	private String reportGroupId;

	private String reportGroupVersion;

	private String reportFlags;
	
	@Column(name="reportGroupDesc")
	private String reportGroupsDesc;

	private String MDSIndex;

	private String reportGroupHeading;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getReportSequence() {
    	return reportSequence;
    }

	public void setReportSequence(String reportSequence) {
    	this.reportSequence = reportSequence;
    }

	public String getReportGroupId() {
    	return reportGroupId;
    }

	public void setReportGroupId(String reportGroupId) {
    	this.reportGroupId = reportGroupId;
    }

	public String getReportGroupVersion() {
    	return reportGroupVersion;
    }

	public void setReportGroupVersion(String reportGroupVersion) {
    	this.reportGroupVersion = reportGroupVersion;
    }

	public String getReportFlags() {
    	return reportFlags;
    }

	public void setReportFlags(String reportFlags) {
    	this.reportFlags = reportFlags;
    }

	public String getReportGroupsDesc() {
    	return reportGroupsDesc;
    }

	public void setReportGroupsDesc(String reportGroupsDesc) {
    	this.reportGroupsDesc = reportGroupsDesc;
    }

	public String getMDSIndex() {
    	return MDSIndex;
    }

	public void setMDSIndex(String mDSIndex) {
    	MDSIndex = mDSIndex;
    }

	public String getReportGroupHeading() {
    	return reportGroupHeading;
    }

	public void setReportGroupHeading(String reportGroupHeading) {
    	this.reportGroupHeading = reportGroupHeading;
    }



}
