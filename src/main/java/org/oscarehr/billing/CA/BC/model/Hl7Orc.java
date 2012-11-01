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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="hl7_orc")
public class Hl7Orc extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="orc_id")
	private Integer id;
	
	@Column(name="pid_id")
	private int pidId;
	
	@Column(name="order_control")
	private String orderControl;
	
	@Column(name="placer_order_number1")
	private String placerOrderNumber1;
	
	@Column(name="filler_order_number")
	private String fillerOrderNumber;
	
	@Column(name="placer_order_number2")
	private String placerOrderNumber2;
	
	@Column(name="order_status")
	private String orderStatus;
	
	@Column(name="response_flag")
	private String responseFlag;
	
	@Column(name="quantity_timing")
	private String quantityTiming;
	
	private String parent;
	
	@Column(name="date_time_of_transaction")
	@Temporal(TemporalType.TIMESTAMP)
	private Date datetimeOfTransaction;
	
	@Column(name="entered_by")
	private String enteredBy;
	
	@Column(name="verified_by")
	private String verifiedBy;
	
	@Column(name="ordering_provider")
	private String orderingProvider;
	
	@Column(name="enterer_location")
	private String entererLocation;
	
	@Column(name="callback_phone_number")
	private String callbackPhoneNumber;
	
	@Column(name="order_effective_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderEffectiveDateTime;
	
	@Column(name="order_control_code_reason")
	private String orderControlCodeReason;
	
	@Column(name="entering_organization")
	private String enteringOrganization;
	
	@Column(name="entering_device")
	private String enteringDevice;
	
	@Column(name="action_by")
	private String actionBy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getPidId() {
		return pidId;
	}

	public void setPidId(int pidId) {
		this.pidId = pidId;
	}

	public String getOrderControl() {
		return orderControl;
	}

	public void setOrderControl(String orderControl) {
		this.orderControl = orderControl;
	}

	public String getPlacerOrderNumber1() {
		return placerOrderNumber1;
	}

	public void setPlacerOrderNumber1(String placerOrderNumber1) {
		this.placerOrderNumber1 = placerOrderNumber1;
	}

	public String getFillerOrderNumber() {
		return fillerOrderNumber;
	}

	public void setFillerOrderNumber(String fillerOrderNumber) {
		this.fillerOrderNumber = fillerOrderNumber;
	}

	public String getPlacerOrderNumber2() {
		return placerOrderNumber2;
	}

	public void setPlacerOrderNumber2(String placerOrderNumber2) {
		this.placerOrderNumber2 = placerOrderNumber2;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getResponseFlag() {
		return responseFlag;
	}

	public void setResponseFlag(String responseFlag) {
		this.responseFlag = responseFlag;
	}

	public String getQuantityTiming() {
		return quantityTiming;
	}

	public void setQuantityTiming(String quantityTiming) {
		this.quantityTiming = quantityTiming;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Date getDatetimeOfTransaction() {
		return datetimeOfTransaction;
	}

	public void setDatetimeOfTransaction(Date datetimeOfTransaction) {
		this.datetimeOfTransaction = datetimeOfTransaction;
	}

	public String getEnteredBy() {
		return enteredBy;
	}

	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}

	public String getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public String getOrderingProvider() {
		return orderingProvider;
	}

	public void setOrderingProvider(String orderingProvider) {
		this.orderingProvider = orderingProvider;
	}

	public String getEntererLocation() {
		return entererLocation;
	}

	public void setEntererLocation(String entererLocation) {
		this.entererLocation = entererLocation;
	}

	public String getCallbackPhoneNumber() {
		return callbackPhoneNumber;
	}

	public void setCallbackPhoneNumber(String callbackPhoneNumber) {
		this.callbackPhoneNumber = callbackPhoneNumber;
	}

	public Date getOrderEffectiveDateTime() {
		return orderEffectiveDateTime;
	}

	public void setOrderEffectiveDateTime(Date orderEffectiveDateTime) {
		this.orderEffectiveDateTime = orderEffectiveDateTime;
	}

	public String getOrderControlCodeReason() {
		return orderControlCodeReason;
	}

	public void setOrderControlCodeReason(String orderControlCodeReason) {
		this.orderControlCodeReason = orderControlCodeReason;
	}

	public String getEnteringOrganization() {
		return enteringOrganization;
	}

	public void setEnteringOrganization(String enteringOrganization) {
		this.enteringOrganization = enteringOrganization;
	}

	public String getEnteringDevice() {
		return enteringDevice;
	}

	public void setEnteringDevice(String enteringDevice) {
		this.enteringDevice = enteringDevice;
	}

	public String getActionBy() {
		return actionBy;
	}

	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}
	
	
}
