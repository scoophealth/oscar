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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class IntegratorProgressItem extends AbstractModel<Integer> {
	 
	public static final String STATUS_COMPLETED = "completed";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private int demographicNo;

	private int integratorProgressId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateUpdated;

	private String status;
	
	public IntegratorProgressItem() {
		
	}
	
	public IntegratorProgressItem(int integratorProgressId) {
		this();
		this.integratorProgressId = integratorProgressId;
	}
	
	public IntegratorProgressItem(int integratorProgressId, Integer demographicNo) {
		this(integratorProgressId);
		this.demographicNo = demographicNo;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}

	public int getIntegratorProgressId() {
		return integratorProgressId;
	}

	public void setIntegratorProgressId(int integratorProgressId) {
		this.integratorProgressId = integratorProgressId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	@PrePersist
	@PreUpdate
	protected void jpa_updateTS() {
		dateUpdated = new Date();
	}
	
}
