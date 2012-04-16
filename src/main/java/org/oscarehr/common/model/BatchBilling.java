/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the batch_billing database table.
 * 
 */
@Entity
@Table(name="batch_billing")
public class BatchBilling  extends AbstractModel<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String billingProviderNo;
	private Timestamp createDate;
	private Date lastbilled_date;
	private String creator;
	private int demographicNo;
	private String dxcode;
	private String serviceCode;
	private String billing_amount;

    public BatchBilling() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	@Column(name="billing_provider_no")
	public String getBillingProviderNo() {
		return this.billingProviderNo;
	}

	public void setBillingProviderNo(String billingProviderNo) {
		this.billingProviderNo = billingProviderNo;
	}


	@Column(name="create_date", nullable=false)
	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}


	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creatorProviderNo) {
		this.creator = creatorProviderNo;
	}


	@Column(name="demographic_no")
	public int getDemographicNo() {
		return this.demographicNo;
	}

	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}


	@Column(length=5)
	public String getDxcode() {
		return this.dxcode;
	}

	public void setDxcode(String dxcode) {
		this.dxcode = dxcode;
	}


	@Column(name="service_code", length=10)	
	public String getServiceCode() {
		return this.serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	@Column(name="lastbilled_date")
	@Temporal(TemporalType.DATE)	
	public Date getLastBilledDate() {
	    return lastbilled_date;
    }

	public void setLastBilledDate(Date lastbilled_date) {
	    this.lastbilled_date = lastbilled_date;
    }


	@Column(name="billing_amount")
	public String getBillingAmount() {
	    return billing_amount;
    }


	public void setBillingAmount(String billing_amount) {
	    this.billing_amount = billing_amount;
    }

}