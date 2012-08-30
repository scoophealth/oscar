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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.oscarehr.common.model.AbstractModel;

import oscar.entities.PrivateBillTransaction;

@Entity
@Table(name = "billing_private_transactions")
public class BillingPrivateTransactions extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "billingmaster_no", nullable = false)
	private int billingmasterNo;

	@Column(name = "amount_received", nullable = false, precision = 22, scale = 0)
	private double amountReceived;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date", nullable = false, length = 19)
	private Date creationDate;

	@Column(name = "payment_type_id", nullable = false)
	private int paymentTypeId;

	public BillingPrivateTransactions() {
	}

	public BillingPrivateTransactions(int billingmasterNo, double amountReceived, Date creationDate, int paymentTypeId) {
		this.billingmasterNo = billingmasterNo;
		this.amountReceived = amountReceived;
		this.creationDate = creationDate;
		this.paymentTypeId = paymentTypeId;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getBillingmasterNo() {
		return this.billingmasterNo;
	}

	public void setBillingmasterNo(int billingmasterNo) {
		this.billingmasterNo = billingmasterNo;
	}

	public double getAmountReceived() {
		return this.amountReceived;
	}

	public void setAmountReceived(double amountReceived) {
		this.amountReceived = amountReceived;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public int getPaymentTypeId() {
		return this.paymentTypeId;
	}

	public void setPaymentTypeId(int paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public PrivateBillTransaction toTx(PrivateBillTransaction target) {
		if (target == null)
			target = new PrivateBillTransaction();
		target.setAmount_received(getAmountReceived());
		target.setBillingmaster_no(getBillingmasterNo());
		target.setCreation_date(getCreationDate());
		target.setId(getId());
		target.setPayment_type(getPaymentTypeId());
		
		return target;
	}
}
