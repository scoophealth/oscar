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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="mdsMSH")
public class MdsMSH extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	private String sendingApp;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;

	private String type;

	@Column(name="messageConID")
	private String controlId;

	@Column(name="processingID")
	private String processingId;

	@Column(name="versionID")
	private String versionId;

	private String acceptAckType;

	private String appAckType;

	@Column(name="demographic_no")
	private int demographicNo;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getSendingApp() {
    	return sendingApp;
    }

	public void setSendingApp(String sendingApp) {
    	this.sendingApp = sendingApp;
    }

	public Date getDateTime() {
    	return dateTime;
    }

	public void setDateTime(Date dateTime) {
    	this.dateTime = dateTime;
    }

	public String getType() {
    	return type;
    }

	public void setType(String type) {
    	this.type = type;
    }

	public String getControlId() {
    	return controlId;
    }

	public void setControlId(String controlId) {
    	this.controlId = controlId;
    }

	public String getProcessingId() {
    	return processingId;
    }

	public void setProcessingId(String processingId) {
    	this.processingId = processingId;
    }

	public String getVersionId() {
    	return versionId;
    }

	public void setVersionId(String versionId) {
    	this.versionId = versionId;
    }

	public String getAcceptAckType() {
    	return acceptAckType;
    }

	public void setAcceptAckType(String acceptAckType) {
    	this.acceptAckType = acceptAckType;
    }

	public String getAppAckType() {
    	return appAckType;
    }

	public void setAppAckType(String appAckType) {
    	this.appAckType = appAckType;
    }

	public int getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }

	@PrePersist
	protected void jpa_setDateTime() {
		this.dateTime = new Date();
	}

}
