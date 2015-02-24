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

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="billing_on_item_payment")
public class BillingOnItemPayment extends AbstractModel<Integer> implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="billing_on_item_id")
	private int billingOnItemId;

	@Column(name="billing_on_payment_id", nullable=false)
	private int billingOnPaymentId;

	@Column(name="ch1_id", nullable=false)
	private int ch1Id;

	@Column(precision=10, scale=2)
	private BigDecimal discount = new BigDecimal("0.00");

	@Column(precision=10, scale=2)
	private BigDecimal paid = new BigDecimal("0.00");
	
	@Column(precision=10, scale=2)
	private BigDecimal credit = new BigDecimal("0.00");

	@Column(name="payment_timestamp", nullable=false)
	private Timestamp paymentTimestamp;

	@Column(precision=10, scale=2)
	private BigDecimal refund = new BigDecimal("0.00");

	public BillingOnItemPayment() {
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBillingOnItemId() {
		return this.billingOnItemId;
	}

	public void setBillingOnItemId(int billingOnItemId) {
		this.billingOnItemId = billingOnItemId;
	}

	public int getBillingOnPaymentId() {
		return this.billingOnPaymentId;
	}

	public void setBillingOnPaymentId(int billingOnPaymentId) {
		this.billingOnPaymentId = billingOnPaymentId;
	}

	public int getCh1Id() {
		return this.ch1Id;
	}

	public void setCh1Id(int ch1Id) {
		this.ch1Id = ch1Id;
	}

	public BigDecimal getDiscount() {
		return this.discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getPaid() {
		return this.paid;
	}

	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}

	public Timestamp getPaymentTimestamp() {
		return this.paymentTimestamp;
	}

	public void setPaymentTimestamp(Timestamp paymentTimestamp) {
		this.paymentTimestamp = paymentTimestamp;
	}

	public BigDecimal getRefund() {
		return this.refund;
	}

	public void setRefund(BigDecimal refund) {
		this.refund = refund;
	}
	
	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}
}
