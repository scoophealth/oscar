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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="labRequestReportLink")
public class LabRequestReportLink extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="request_table")
	private String requestTable;

	@Column(name="request_id")
	private Integer requestId;

	@Column(name="request_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestDate;

	@Column(name="report_table")
	private String reportTable;

	@Column(name="report_id")
	private int reportId;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getRequestTable() {
    	return requestTable;
    }

	public void setRequestTable(String requestTable) {
    	this.requestTable = requestTable;
    }

	public Integer getRequestId() {
    	return requestId;
    }

	public void setRequestId(Integer requestId) {
    	this.requestId = requestId;
    }

	public Date getRequestDate() {
    	return requestDate;
    }

	public void setRequestDate(Date requestDate) {
    	this.requestDate = requestDate;
    }

	public String getReportTable() {
    	return reportTable;
    }

	public void setReportTable(String reportTable) {
    	this.reportTable = reportTable;
    }

	public int getReportId() {
    	return reportId;
    }

	public void setReportId(int reportId) {
    	this.reportId = reportId;
    }


}
