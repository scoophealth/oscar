/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "providerLabRouting")
public class ProviderLabRoutingModel extends AbstractModel<Integer> implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "provider_no")
	private String providerNo;
	
	@Column(name = "lab_no")
	private Integer labNo;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "comment")
	private String comment;
	
	@Column(name = "timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@Column(name = "lab_type")
	private String labType;
	
	@Override
	public Integer getId() {
		return id;
	}
	
	public String getProviderNo() {
		return providerNo;
	}
	
	public void setProviderNo(String providerNo) {
		this.providerNo = StringUtils.trimToEmpty(providerNo);
	}
	
	public Integer getLabNo() {
		return labNo;
	}
	
	public void setLabNo(Integer labNo) {
		this.labNo = labNo;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = StringUtils.trimToNull(status);
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = StringUtils.trimToNull(comment);
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getLabType() {
		return labType;
	}
	
	public void setLabType(String labType) {
		this.labType = StringUtils.trimToNull(labType);
	}
	
	@PrePersist
	@PreUpdate
	protected void jpa_setTimestamp() {
		this.timestamp = new Date();
	}
	
}