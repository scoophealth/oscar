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

import java.util.ArrayList;

import oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem;

public class BillingSessionBean implements java.io.Serializable{
  private String apptProviderNo = null;
  private String patientName = null;
  private String providerView = null;
  private String billRegion = null;
  private String billForm = null;
  private String creator = null;
  private String patientNo = null;
  private String apptNo = null;
  private String apptDate = null;
  private String apptStart = null;
  private String apptStatus = null;
  private ArrayList<BillingItem> billitem;
  private String xml_billtype = null;
  private String xml_location = null;
  private String xml_starttime = null;
  private String xml_endtime = null;
  private String xml_appointment_date = null;
  private String xml_provider = null;
  private String xml_visittype = null;
  private String xml_vdate = null;
  private String refer1 = null;
  private String refer2 = null;
  private String refertype1 = null;
  private String refertype2 = null;
  private String dx1 = null;
  private String dx2 = null;
  private String dx3 = null;
  private String patientLastName = null;
  private String patientFirstName = null;
  private String patientDoB = null;
  private String patientAddress1 = null;
  private String patientAddress2 = null;
  private String patientPostal = null;
  private String patientSex = null;
  private String patientPHN = null;
  private String patientHCType = null;
  private String billingType = null;
  private String grandTotal = null;
  private String patientAge = null;
  private String billingPracNo = null;
  private String billingGroupNo = null;

  private String notes = "";
  private String encounter = "";
  private String icbc_claim_no = "";
  private String correspondenceCode = "";
  private String dependent = "";
  private String afterHours = "";
  private String timeCall = "";
  private String submissionCode = "";
  private String service_to_date = "";
  private String shortClaimNote = "";
  private String messageNotes = "";
  private String mva_claim_code = "";
  private String facilityNum = "";
  private String facilitySubNum = "";
  private String paymentType;
  private String paymentTypeName;
  private String endTimeHr = "";
  private String endTimeMin = "";
  private String startTimeHr = "";
  private String startTimeMin = "";
  private String wcbId="";

  public String getIcbc_claim_no() {
    this.icbc_claim_no = (null != this.icbc_claim_no) ? this.icbc_claim_no : "";
    if (icbc_claim_no.compareTo("") == 0 ||
        (this.billingType.compareTo("ICBC") != 0)) {
      return "00000000";
    }
    else {
      return this.icbc_claim_no;
    }
  }

  public void setIcbc_claim_no(String icbc) {
    this.icbc_claim_no = icbc;
  }

  public String getMva_claim_code() {
    return mva_claim_code;
  }

  public void setMva_claim_code(String mva_claim_code) {
    this.mva_claim_code = mva_claim_code;
  }

  public String getEncounter() {
    return this.encounter;
  }

  public void setEncounter(String encounter) {
    this.encounter = encounter;
  }

  public String getPatientAge() {
    return this.patientAge;
  }

  public void setPatientAge(String RHS) {
    this.patientAge = RHS;
  }

  public String getBillingPracNo() {
    return this.billingPracNo;
  }

  public void setBillingPracNo(String RHS) {
    this.billingPracNo = RHS;
  }

  public String getBillingGroupNo() {
    return this.billingGroupNo;
  }

  public void setBillingGroupNo(String RHS) {
    this.billingGroupNo = RHS;
  }

  public String getGrandtotal() {
    return this.grandTotal;
  }

  public void setGrandtotal(String grandTotal) {
    this.grandTotal = grandTotal;
  }

  public String getPatientLastName() {
    return this.patientLastName;
  }

  public void setPatientLastName(String RHS) {
    this.patientLastName = RHS;
  }

  public String getPatientFirstName() {
    return this.patientFirstName;
  }

  public void setPatientFirstName(String RHS) {
    this.patientFirstName = RHS;
  }

  public String getPatientDoB() {
    return this.patientDoB;
  }

  public void setPatientDoB(String RHS) {
    this.patientDoB = RHS;
  }

  public String getPatientAddress1() {
    return this.patientAddress1;
  }

  public void setPatientAddress1(String RHS) {
    this.patientAddress1 = RHS;
  }

  public String getPatientAddress2() {
    return this.patientAddress2;
  }

  public void setPatientAddress2(String RHS) {
    this.patientAddress2 = RHS;
  }

  public String getPatientPostal() {
    return this.patientPostal;
  }

  public void setPatientPostal(String RHS) {
    this.patientPostal = RHS;
  }

  public String getPatientSex() {
    return this.patientSex;
  }

  public void setPatientSex(String RHS) {
    this.patientSex = RHS;
  }

  public String getPatientPHN() {
    return this.patientPHN;
  }

  public void setPatientPHN(String RHS) {
    this.patientPHN = RHS;
  }

  public String getPatientHCType() {
    return this.patientHCType;
  }

  public void setPatientHCType(String RHS) {
    this.patientHCType = RHS;
  }

  public String getBillingType() {
    return this.billingType;
  }

  public void setBillingType(String RHS) {
    this.billingType = RHS;
  }

  public String getAdmissionDate() {
    return this.xml_vdate;
  }

  public void setAdmissionDate(String RHS) {
    this.xml_vdate = RHS;
  }

  public String getReferral1() {
    return this.refer1;
  }

  public void setReferral1(String RHS) {
    this.refer1 = RHS;
  }

  public String getReferral2() {
    return this.refer2;
  }

  public void setReferral2(String RHS) {
    this.refer2 = RHS;
  }

  public String getReferType1() {
    return this.refertype1;
  }

  public void setReferType1(String RHS) {
    this.refertype1 = RHS;
  }

  public String getReferType2() {
    return this.refertype2;
  }

  public void setReferType2(String RHS) {
    this.refertype2 = RHS;
  }

  public String getDx1() {
    return this.dx1;
  }

  public void setDx1(String RHS) {
    this.dx1 = RHS;
  }

  public String getDx2() {
    return this.dx2;
  }

  public void setDx2(String RHS) {
    this.dx2 = RHS;
  }

  public String getDx3() {
    return this.dx3;
  }

  public void setDx3(String RHS) {
    this.dx3 = RHS;
  }

  public String getBillType() {
    return this.xml_billtype;
  }

  public void setBillType(String RHS) {
    this.xml_billtype = RHS;
  }

  public String getVisitLocation() {
    return this.xml_location;
  }

  public void setVisitLocation(String RHS) {
    this.xml_location = RHS;
  }

  public String getStartTime() {
    return this.startTimeHr + this.startTimeMin;
  }

  public void setStartTime(String RHS) {
    this.xml_starttime = RHS;
  }

  public String getEndTime() {
    return this.endTimeHr + this.endTimeMin;
  }

  public void setEndTime(String RHS) {
    this.xml_endtime = RHS;
  }

  public String getServiceDate() {
    return this.xml_appointment_date;
  }

  public void setServiceDate(String RHS) {
    this.xml_appointment_date = RHS;
  }

  public String getBillingProvider() {
    return this.xml_provider;
  }

  public void setBillingProvider(String RHS) {
    this.xml_provider = RHS;
  }

  public String getVisitType() {
    return this.xml_visittype;
  }

  public void setVisitType(String RHS) {
    this.xml_visittype = RHS;
  }

  public ArrayList<BillingItem> getBillItem() {
    return this.billitem;
  }

  public void setBillItem(ArrayList<BillingItem> RHS) {
    this.billitem = RHS;
  }

  public String getApptProviderNo() {
    return this.apptProviderNo;
  }

  public void setApptProviderNo(String RHS) {
    this.apptProviderNo = RHS;
  }

  public String getPatientName() {
    return this.patientName;
  }

  public void setPatientName(String RHS) {
    this.patientName = RHS;
  }

  public String getProviderView() {
    return this.providerView;
  }

  public void setProviderView(String RHS) {
    this.providerView = RHS;
  }

  public String getBillRegion() {
    return this.billRegion;
  }

  public void setBillRegion(String RHS) {
    this.billRegion = RHS;
  }

  public String getBillForm() {
    return this.billForm;
  }

  public void setBillForm(String RHS) {
    this.billForm = RHS;
  }

  public String getCreator() {
    return this.creator;
  }

  public void setCreator(String RHS) {
    this.creator = RHS;
  }

  public String getPatientNo() {
    return this.patientNo;
  }

  public void setPatientNo(String RHS) {
    this.patientNo = RHS;
  }

  public String getApptNo() {
    return this.apptNo;
  }

  public void setApptNo(String RHS) {
    this.apptNo = RHS;
  }

  public String getApptDate() {
    return this.apptDate;
  }

  public void setApptDate(String RHS) {
    this.apptDate = RHS;
  }

  public String getApptStart() {
    return this.apptStart;
  }

  public void setApptStart(String RHS) {
    this.apptStart = RHS;
  }

  public String getApptStatus() {
    return this.apptStatus;
  }

  public void setApptStatus(String RHS) {
    this.apptStatus = RHS;
  }

  /**
   * Getter for property notes.
   * @return Value of property notes.
   */
  public String getNotes() {
    return notes;
  }

  /**
   * Setter for property notes.
   * @param notes New value of property notes.
   */
  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * Getter for property correspondenceCode.
   * @return Value of property correspondenceCode.
   */
  public String getCorrespondenceCode() {
    return correspondenceCode;
  }

  /**
   * Setter for property correspondenceCode.
   * @param correspondenceCode New value of property correspondenceCode.
   */
  public void setCorrespondenceCode(String correspondenceCode) {
    this.correspondenceCode = correspondenceCode;
  }

  /**
   * Getter for property dependent.
   * @return Value of property dependent.
   */
  public java.lang.String getDependent() {
    return dependent;
  }

  /**
   * Setter for property dependent.
   * @param dependent New value of property dependent.
   */
  public void setDependent(java.lang.String dependent) {
    this.dependent = dependent;
  }

  /**
   * Getter for property afterHours.
   * @return Value of property afterHours.
   */
  public java.lang.String getAfterHours() {
    return afterHours;
  }

  /**
   * Setter for property afterHours.
   * @param afterHours New value of property afterHours.
   */
  public void setAfterHours(java.lang.String afterHours) {
    this.afterHours = afterHours;
  }

  /**
   * Getter for property timeCall.
   * @return Value of property timeCall.
   */
  public java.lang.String getTimeCall() {
    return timeCall;
  }

  /**
   * Setter for property timeCall.
   * @param timeCall New value of property timeCall.
   */
  public void setTimeCall(java.lang.String timeCall) {
    this.timeCall = timeCall;
  }

  /**
   * Getter for property submissionCode.
   * @return Value of property submissionCode.
   */
  public java.lang.String getSubmissionCode() {
    return submissionCode;
  }

  /**
   * Setter for property submissionCode.
   * @param submissionCode New value of property submissionCode.
   */
  public void setSubmissionCode(java.lang.String submissionCode) {
    this.submissionCode = submissionCode;
  }

  /**
   * Getter for property service_to_date.
   * @return Value of property service_to_date.
   */
  public java.lang.String getService_to_date() {
    return service_to_date;
  }

  /**
   * Setter for property service_to_date.
   * @param service_to_date New value of property service_to_date.
   */
  public void setService_to_date(java.lang.String service_to_date) {
    this.service_to_date = service_to_date;
  }

  /**
   * Getter for property shortClaimNote.
   * @return Value of property shortClaimNote.
   */
  public java.lang.String getShortClaimNote() {
    return shortClaimNote;
  }

  /**
   * Setter for property shortClaimNote.
   * @param shortClaimNote New value of property shortClaimNote.
   */
  public void setShortClaimNote(java.lang.String shortClaimNote) {
    this.shortClaimNote = shortClaimNote;
  }

  /**
   * Getter for property messageNotes.
   * @return Value of property messageNotes.
   */
  public java.lang.String getMessageNotes() {
    return messageNotes;
  }

  /**
   * Setter for property messageNotes.
   * @param messageNotes New value of property messageNotes.
   */
  public void setMessageNotes(java.lang.String messageNotes) {
    this.messageNotes = messageNotes;
  }

  /**
   * Getter for property facilityNum.
   * @return Value of property facilityNum.
   */
  public java.lang.String getFacilityNum() {
    return facilityNum;
  }

  /**
   * Setter for property facilityNum.
   * @param facilityNum New value of property facilityNum.
   */
  public void setFacilityNum(java.lang.String facilityNum) {
    this.facilityNum = facilityNum;
  }

  /**
   * Getter for property facilitySubNum.
   * @return Value of property facilitySubNum.
   */
  public java.lang.String getFacilitySubNum() {
    return facilitySubNum;
  }

  /**
   * Setter for property facilitySubNum.
   * @param facilitySubNum New value of property facilitySubNum.
   */
  public void setFacilitySubNum(java.lang.String facilitySubNum) {
    this.facilitySubNum = facilitySubNum;
  }

  /**
   * setPaymentType
   *
   * @param type String
   */
  public void setPaymentType(String type) {
    this.paymentType = type;
  }

  public String getPaymentType() {
    return paymentType;
  }
  public String getPaymentTypeName() {
    return paymentTypeName;
  }

  public String getEndTimeHr() {
    return endTimeHr;
  }

  public String getEndTimeMin() {
    return endTimeMin;
  }

  public String getStartTimeHr() {
    return startTimeHr;
  }

  public String getStartTimeMin() {
    return startTimeMin;
  }

  public void setPaymentTypeName(String paymentTypeName) {
    this.paymentTypeName = paymentTypeName;
  }

  public void setEndTimeHr(String endTimeHr) {
    this.endTimeHr = endTimeHr;
  }

  public void setEndTimeMin(String endTimeMin) {
    this.endTimeMin = endTimeMin;
  }

  public void setStartTimeHr(String startTimeHr) {
    this.startTimeHr = startTimeHr;
  }

  public void setStartTimeMin(String startTimeMin) {
    this.startTimeMin = startTimeMin;
  }

    public String getWcbId() {
        return wcbId;
    }

    public void setWcbId(String wcbId) {
        this.wcbId = wcbId;
    }

}
