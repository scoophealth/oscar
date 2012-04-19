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


package oscar.oscarBilling.ca.bc.pageUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public final class BillingViewForm
    extends ActionForm {
  private String amountReceived;
  private String messageNotes;
  private String recipientAddress;
  private String recipientCity;
  private String recipientName;
  private String recipientPostal;
  private String recipientProvince;
  String requestId;
  private String billStatus;
  private String billingNo;
  private String paymentMethod;
  private String billPatient;
  public String getRequestId() {
    return requestId;
  }

  public String getAmountReceived() {
    return amountReceived;
  }

  public String getRecipientProvince() {
    return recipientProvince;
  }

  public String getRecipientPostal() {
    return recipientPostal;
  }

  public String getRecipientName() {
    return recipientName;
  }

  public String getRecipientCity() {
    return recipientCity;
  }

  public String getRecipientAddress() {
    return recipientAddress;
  }

  public String getMessageNotes() {
    return messageNotes;
  }

  public String getBillStatus() {
    return billStatus;
  }

  public String getBillingNo() {
    return billingNo;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public String getBillPatient() {
    return billPatient;
  }

  public void setRequestId(String id) {
    this.requestId = id;
  }

  public void setAmountReceived(String amountReceived) {
    this.amountReceived = amountReceived;
  }

  public void setMessageNotes(String messageNotes) {
    this.messageNotes = messageNotes;
  }

  public void setRecipientAddress(String recipientAddress) {
    this.recipientAddress = recipientAddress;
  }

  public void setRecipientCity(String recipientCity) {
    this.recipientCity = recipientCity;
  }

  public void setRecipientName(String recipientName) {
    this.recipientName = recipientName;
  }

  public void setRecipientPostal(String recipientPostal) {
    this.recipientPostal = recipientPostal;
  }

  public void setRecipientProvince(String recipientProvince) {
    this.recipientProvince = recipientProvince;
  }

  public void setBillStatus(String billStatus) {
    this.billStatus = billStatus;
  }

  public void setBillingNo(String billingNo) {
    this.billingNo = billingNo;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public void setBillPatient(String billPatient) {
    this.billPatient = billPatient;
  }

  public void reset(ActionMapping mapping, HttpServletRequest request) {
     this.amountReceived = "";
   }

}
