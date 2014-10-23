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
@Table(name="demographicPharmacy")
public class DemographicPharmacy extends AbstractModel<Integer>{
	
	public static final String ACTIVE = "1";
	
	public static final String INACTIVE = "0";
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="pharmacyID")
	private int pharmacyId;

	@Column(name="demographic_no")
	private int demographicNo;

	private String status;
	
	private int preferredOrder;

	@Temporal(TemporalType.TIMESTAMP)
	private Date addDate;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getPharmacyId() {
    	return pharmacyId;
    }

	public void setPharmacyId(int pharmacyId) {
    	this.pharmacyId = pharmacyId;
    }

	public int getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }

	public Date getAddDate() {
    	return addDate;
    }

	public void setAddDate(Date addDate) {
    	this.addDate = addDate;
    }

	@PrePersist
	public void beforePersist() {
		addDate = new Date();
	}

	/**
	 * @return the preferedOrder
	 */
    public int getPreferredOrder() {
	    return preferredOrder;
    }

	/**
	 * @param preferedOrder the preferedOrder to set
	 */
    public void setPreferredOrder(int preferedOrder) {
	    this.preferredOrder = preferedOrder;
    }


}
