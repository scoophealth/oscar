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
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "patientLabRouting")
public class PatientLabRouting extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "lab_no")
	/** This is also referred to as segmentId in parts of the code... */
	private int labNo;

	@Column(name = "lab_type")
	private String labType;

	@Column(name = "demographic_no")
	private Integer demographicNo;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateModified = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date created = new Date();
	
	public int getLabNo() {
    	return labNo;
    }

	public void setLabNo(int labNo) {
    	this.labNo = labNo;
    }

	public String getLabType() {
    	return labType;
    }

	public void setLabType(String labType) {
    	this.labType = labType;
    }

	public Integer getDemographicNo() {
    	return demographicNo;
    }
	
	public void setDemographicNo(Integer demographicNo) {
    	this.demographicNo = demographicNo;
    }

	
	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	
	public Date getCreated() {
		return this.created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}

	@PrePersist
	@PreUpdate
	protected void jpa_setDateModified() {
		this.dateModified = new Date();
	}

	@Override
    public Integer getId() {
    	return id;
    }
}
