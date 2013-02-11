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

package oscar.entities;

import java.util.Date;

import oscar.util.UtilMisc;

/**
 * BillHistory  represents an archive of a modification event on a specific line(BillingMaster Record) of a Bill
 * @author Joel Legris
 * @version 1.0
 */
public class BillHistory {
	
	private int id;
	private int billingMasterNo;
	private String practitioner_no = "";
	private String billingStatus = "";
	private Date archiveDate;
	private String billingtype = "";
	private String seqNum = "";
	private double amount;
	private double amountReceived;
	private String paymentTypeId;
	private String paymentTypeDesc;

	public BillHistory() {
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the billingMaster Number of the records which is being tracked
	 * @param billingMasterNo int
	 */
	public void setBillingMasterNo(int billingMasterNo) {
		this.billingMasterNo = billingMasterNo;
	}

	/**
	 * Sets the number of the provider who is responsible for initiating this audit event
	 * @param practitioner_no String
	 */
	public void setPractitioner_no(String practitioner_no) {

		this.practitioner_no = practitioner_no;
	}

	/**
	 * Set the status of the billingMaster record at the time of the event
	 * @param billingStatus String
	 */
	public void setBillingStatus(String billingStatus) {
		this.billingStatus = billingStatus;
	}

	/**
	 * Sets the Date of the event
	 * @param archiveDate Date
	 */
	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}

	public void setBillingtype(String billingtype) {

		this.billingtype = billingtype;
	}

	public void setSeqNum(String seqNum) {

		this.seqNum = seqNum;
	}

	public void setAmount(double amount) {
		this.amount = UtilMisc.toCurrencyDouble(amount);
	}

	public void setAmountReceived(double amountReceived) {
		this.amountReceived = UtilMisc.toCurrencyDouble(amountReceived);
	}

	public void setPaymentTypeId(String paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public void setPaymentTypeDesc(String paymentTypeDesc) {
		this.paymentTypeDesc = paymentTypeDesc;
	}

	public int getId() {
		return id;
	}

	public int getBillingMasterNo() {
		return billingMasterNo;
	}

	public String getPractitioner_no() {

		return practitioner_no;
	}

	public String getBillingStatus() {
		return billingStatus;
	}

	public Date getArchiveDate() {
		return archiveDate;
	}

	public String getBillingtype() {

		return billingtype;
	}

	public String getSeqNum() {

		return seqNum;
	}

	public double getAmount() {
		return amount;
	}

	public double getAmountReceived() {
		return amountReceived;
	}

	public String getPaymentTypeId() {
		return paymentTypeId;
	}

	public String getPaymentTypeDesc() {
		return paymentTypeDesc;
	}

}
