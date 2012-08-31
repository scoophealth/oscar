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
package org.oscarehr.billing.CA.BC.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name = "billing_trayfees")
public class BillingTrayFee extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "billingServiceNo", nullable = false, length = 10)
	private String billingServiceNo;

	@Column(name = "billingServiceTrayNo", nullable = false, length = 10)
	private String billingServiceTrayNo;

	public BillingTrayFee() {
	}

	public BillingTrayFee(String billingServiceNo, String billingServiceTrayNo) {
		this.billingServiceNo = billingServiceNo;
		this.billingServiceTrayNo = billingServiceTrayNo;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBillingServiceNo() {
		return this.billingServiceNo;
	}

	public void setBillingServiceNo(String billingServiceNo) {
		this.billingServiceNo = billingServiceNo;
	}

	public String getBillingServiceTrayNo() {
		return this.billingServiceTrayNo;
	}

	public void setBillingServiceTrayNo(String billingServiceTrayNo) {
		this.billingServiceTrayNo = billingServiceTrayNo;
	}

}
