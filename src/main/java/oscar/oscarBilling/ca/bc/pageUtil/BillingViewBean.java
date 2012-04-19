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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.oscarehr.util.MiscUtils;

import oscar.entities.PaymentType;
import oscar.oscarBilling.ca.bc.MSP.MSPBillingNote;
import oscar.oscarBilling.ca.bc.data.BillRecipient;
import oscar.oscarBilling.ca.bc.data.BillingNote;
import oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem;
import oscar.oscarDB.DBHandler;
import oscar.util.SqlUtils;

public class BillingViewBean {
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
  private String billingMasterNo = null;
  private String billingNo = null;
  private String subTotal;
  private String billStatus;
  private String billNotes;
  private String paymentMethod;
  private String defaultPayeeFirstName;
  private String defaultPayeeLastName;
  public void loadBilling(String billing_no) {
    try {

      ResultSet rs;
      String sql;

      sql = "select bi.billing_date, bi.visitdate, bi.apptProvider_no, bi.creator, bi.provider_no,bi.provider_ohip_no, b.billingmaster_no, b.billing_no, b.createdate, b.billingstatus,b.demographic_no, b.appointment_no, b.claimcode, b.datacenter, b.payee_no, b.practitioner_no, b.phn, b.name_verify, b.dependent_num,b.billing_unit,";
      sql = sql + "b.clarification_code, b.anatomical_area, b.after_hour, b.new_program, b.billing_code, b.bill_amount, b.payment_mode, b.service_date, b.service_to_day, b.submission_code, b.extended_submission_code, b.dx_code1, b.dx_code2, b.dx_code3, ";
      sql = sql + "b.dx_expansion, b.service_location, b.referral_flag1, b.referral_no1, b.referral_flag2, b.referral_no2, b.time_call, b.service_start_time, b.service_end_time, b.birth_date, b.office_number, b.correspondence_code, b.claim_comment,b.paymentMethod ";
      sql = sql + "from billingmaster b, billing bi where bi.billing_no=b.billing_no and b.billing_no='" +
          billing_no + "'";
      MiscUtils.getLogger().debug(sql);
      rs = DBHandler.GetSQL(sql);

      while (rs.next()) {

        this.patientNo = rs.getString("demographic_no");
        this.creator = rs.getString("creator");
        this.apptProviderNo = rs.getString("apptProvider_no");
        this.xml_provider = rs.getString("provider_no");
        this.refer1 = rs.getString("referral_no1");
        this.refer2 = rs.getString("referral_no2");
        this.refertype1 = rs.getString("referral_flag1");
        this.refertype2 = rs.getString("referral_flag2");
        this.dx1 = rs.getString("dx_code1");
        this.dx2 = rs.getString("dx_code2");
        this.dx3 = rs.getString("dx_code3");
        this.billRegion = "BC";
        this.xml_location = rs.getString("clarification_code");
        this.xml_visittype = rs.getString("service_location");
        this.billingType = rs.getString("billingstatus").compareTo("O") == 0 ?
            "MSP" : rs.getString("billingstatus");
        this.xml_appointment_date = rs.getString("billing_date");
        this.xml_vdate = rs.getString("visitdate");
        this.xml_starttime = rs.getString("service_start_time");
        this.xml_endtime = rs.getString("service_end_time");
        this.billingMasterNo = rs.getString("billingmaster_no");
        this.billingNo = rs.getString("billing_no");
        this.paymentMethod = rs.getString("paymentMethod");
        this.billingPracNo = rs.getString("payee_no");

      }
      //setBillItem(billingItemsArray);
      rs.close();

    }
    catch (SQLException e) {
      MiscUtils.getLogger().error("Error", e);
    }

  }

  public List<BillRecipient> getBillRecipient(String billingNo) {
    return SqlUtils.getBeanList(
        "select * from bill_recipients where billingNo = " +
        billingNo, BillRecipient.class);

  }

  /**
   * Updates the paymentMethod of the specified bill with the supplied paymentMethod code and payee number
   * @param billingNo String - The uid of the bill to be updated
   * @param paymentMethod String - The paymentMethod code
   */
  public void updateBill(String billingNo,String payeeNo) {

    try {

      @SuppressWarnings("unchecked")
    List<String[]> billingMasterNos = SqlUtils.getQueryResultsList(
          "select billingmaster_no from billingmaster where billing_no = " +
          billingNo);

      if (billingMasterNos != null) {
        for (int i = 0; i < billingMasterNos.size(); i++) {
          String[] values = billingMasterNos.get(i);
          String billingMasternum = values[0];
          DBHandler.RunSQL("update billingmaster set payee_no = '" + payeeNo + "' " +
                    " where billingmaster_no = " + billingMasternum + "");

        }
      }
    }
    catch (Exception e) {
      MiscUtils.getLogger().error("Error", e);
    }
  }

  public String getMessageNotes() {
    return new BillingNote().getNote(this.getBillingMasterNo());
  }

  public String getMessageNotesByBillingNo(String billingNo) {

    ResultSet rs = null;
    StringBuilder res = new StringBuilder();
    try {

      rs = DBHandler.GetSQL(
          "select billingmaster_no from billingmaster where billing_no = " +
          billingNo);
      ArrayList<String> billingMasterNos = new ArrayList<String>();
      while (rs.next()) {
        billingMasterNos.add(rs.getString(1));
        res.append(new BillingNote().getNote(this.getBillingMasterNo()));
      }
    }
    catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
    }

    return res.toString();
  }

  public String getMSPBillingNote() {
    return new MSPBillingNote().getNote(this.getBillingMasterNo());
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
    return this.xml_starttime;
  }

  public void setStartTime(String RHS) {
    this.xml_starttime = RHS;
  }

  public String getEndTime() {
    return this.xml_endtime;
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
   * Getter for property billingMasterNo.
   * @return Value of property billingMasterNo.
   */
  public java.lang.String getBillingMasterNo() {
    return billingMasterNo;
  }

  /**
   * Setter for property billingMasterNo.
   * @param billingMasterNo New value of property billingMasterNo.
   */
  public void setBillingMasterNo(java.lang.String billingMasterNo) {
    this.billingMasterNo = billingMasterNo;
  }

  /**
   * Getter for property billingNo.
   * @return Value of property billingNo.
   */
  public java.lang.String getBillingNo() {
    return billingNo;
  }

  public String getSubTotal() {
    return subTotal;
  }

  public String getBillStatus() {
    return billStatus;
  }

  public String getBillNotes() {
    return billNotes;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public String getDefaultPayeeFirstName() {
    return defaultPayeeFirstName;
  }

  public String getDefaultPayeeLastName() {
    return defaultPayeeLastName;
  }

  /**
   * Setter for property billingNo.
   * @param billingNo New value of property billingNo.
   */
  public void setBillingNo(java.lang.String billingNo) {
    this.billingNo = billingNo;
  }

  public void setSubTotal(String subTotal) {
    this.subTotal = subTotal;
  }

  public void setBillStatus(String billStatus) {
    this.billStatus = billStatus;
  }

  public void setBillNotes(String billNotes) {
    this.billNotes = billNotes;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public void setDefaultPayeeFirstName(String defaultPayeeFirstName) {
    this.defaultPayeeFirstName = defaultPayeeFirstName;
  }

  public void setDefaultPayeeLastName(String defaultPayeeLastName) {
    this.defaultPayeeLastName = defaultPayeeLastName;
  }

  public List<PaymentType> getPaymentTypes() {
    ArrayList<PaymentType> types = new ArrayList<PaymentType>();
    String sql = "select * from billing_payment_type";

    ResultSet rs = null;
    try {

      rs = DBHandler.GetSQL(sql);

      while (rs.next()) {
        PaymentType tp = new PaymentType();
        tp.setId(rs.getString(1));
        tp.setPaymentType(rs.getString(2));
        types.add(tp);
      }
    }
    catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
    }
    finally {
      try {
        rs.close();
      }
      catch (SQLException ex1) {MiscUtils.getLogger().error("Error", ex1);
      }
    }

    return types;
  }

  /**
   * calculateSubtotal
   */
  public double calculateSubtotal() {
    double ret = 0.0;
    for (Iterator<BillingItem> iter = this.billitem.iterator(); iter.hasNext(); ) {
      BillingItem billingItem = iter.next();
      ret += billingItem.price;
    }
    return ret;
  }
}
