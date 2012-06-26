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
import java.util.Enumeration;

import org.oscarehr.util.MiscUtils;

import oscar.util.DateUtils;
import oscar.util.UtilDateUtilities;

/**
 *
 * Represents a Bill in the BC Billing module
 * @todo This class should be renamed since it represents any type of bill(ICBC,WCB,Private)
 * Furthermore, it is based on the MSPReconcile.Bill inner class which wasn't written to the Java Bean standard
 * (public accessors/modifiers and private members). Therefore, for backwards compatibility the members of this class are public.
 * This class needs to be refactored
 *
 * @author not attributable
 * @version 1.0
 */
public class MSPBill {
  public String serviceDateRange = "";
  public String billing_no = "";
  public String apptDoctorNo = "";
  public String apptNo = "";
  public String demoNo = "";
  public String demoName = "";
  public String userno = "";
  public String apptDate = "";
  public String apptTime = "";
  public String reason = "";
  public String billMasterNo = "";
  public String billingtype = "";
  public String demoSex = "";
  public String code = "";
  public String amount = "";
  public String dx1 = "";
  public String dx2 = "";
  public String dx3 = "";
  public String providerFirstName="";
  public String providerLastName="";
  public String updateDate="";
  public String billingUnit="";
  public String serviceDate="";
  public boolean WCB;
  private UtilDateUtilities ut;
  public String mvaCode;
  public String serviceLocation;
  public String demoDOB;
  public String demoPhone2;
  public String demoPhone;
  public String hin;
  public String serviceEndTime;
  public String serviceStartTime;
  public String serviceToDate;
  public String serviceStartDate;
  public String status;
  public String paymentDate;
  public String exp1="";
  public String exp2="";
  public String accountName;
  public String payeeName;
  public String provName;
  public String type;
  public String seqNum;
  public String acctInit;
  public String payeeNo;
  public String expString;
  public String expSum;
  public java.util.Hashtable explanations;
  public String accountNo;
  public String paymentMethod;
  public String paymentMethodName;
  public Date serviceDateDate;
  public String rejectionDate;
  public String adjustmentCode="";
  public String adjustmentCodeDesc="";
  public String amtOwing;
  public String adjustmentCodeAmt="";
  public MSPBill() {
    ut = new UtilDateUtilities();
  }

  public boolean isWCB() {
    return billingtype.equals("WCB");
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getApptDate() {
    return apptDate;
  }

  public void setApptDate(String apptDate) {
    this.apptDate = apptDate;
  }

  public String getApptDoctorNo() {
    return apptDoctorNo;
  }

  public void setApptDoctorNo(String apptDoctorNo) {
    this.apptDoctorNo = apptDoctorNo;
  }

  public String getApptNo() {
    return apptNo;
  }

  public void setApptNo(String apptNo) {
    this.apptNo = apptNo;
  }

  public String getApptTime() {
    return apptTime;
  }

  public void setApptTime(String apptTime) {
    this.apptTime = apptTime;
  }

  public String getBilling_no() {
    return billing_no;
  }

  public void setBilling_no(String billing_no) {
    this.billing_no = billing_no;
  }

  public String getBillingtype() {
    return billingtype;
  }

  public void setBillingtype(String billingtype) {
    this.billingtype = billingtype;
  }

  public void setBillingUnit(String billingUnit) {
    this.billingUnit = billingUnit;
  }

  public void setBillMasterNo(String billMasterNo) {
    this.billMasterNo = billMasterNo;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setDemoName(String demoName) {
    this.demoName = demoName;
  }

  public void setDemoNo(String demoNo) {
    this.demoNo = demoNo;
  }

  public void setDx1(String dx1) {
    this.dx1 = dx1;
  }

  public void setDx2(String dx2) {
    this.dx2 = dx2;
  }

  public void setDx3(String dx3) {
    this.dx3 = dx3;
  }

  public void setProviderLastName(String providerLastName) {
    this.providerLastName = providerLastName;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public void setServiceDate(String serviceDate) {
    this.serviceDate = serviceDate;
  }

  public void setUpdateDate(String updateDate) {
    this.updateDate = updateDate;
  }

  public void setUserno(String userno) {
    this.userno = userno;
  }

  public void setWCB(boolean WCB) {
    this.WCB = WCB;
  }

  public String getUserno() {
    return userno;
  }

  public String getUpdateDate() {
    return updateDate;
  }

  public String getServiceDate() {
    return serviceDate;
  }

  public String getReason() {
    return reason;
  }

  public String getProviderLastName() {
    return providerLastName;
  }

  public String getProviderFirstName() {
    return providerFirstName;
  }

  public String getDx3() {
    return dx3;
  }

  public String getDx2() {
    return dx2;
  }

  public String getDx1() {
    return dx1;
  }

  public String getDemoNo() {
    return demoNo;
  }

  public String getDemoName() {
    return demoName;
  }

  public String getCode() {
    return code;
  }

  public String getBillMasterNo() {
    return billMasterNo;
  }

  public String getBillingUnit() {
    return billingUnit;
  }

  public String getDemoSex() {
    return demoSex;
  }

  public void setDemoSex(String demoSex) {
    this.demoSex = demoSex;
  }

  public void setDemoPhone2(String demoPhone2) {
    this.demoPhone2 = demoPhone2;
  }

  public String getDemoPhone2() {
    return demoPhone2;
  }

  public String getDemoDOB() {
    return demoDOB;
  }

  public void setDemoDOB(String demoDOB) {
    this.demoDOB = demoDOB;
  }

  public void setExplanations(java.util.Hashtable explanations) {
    this.explanations = explanations;
  }

  public void setMvaCode(String mvaCode) {
    this.mvaCode = mvaCode;
  }

  public void setProviderFirstName(String providerFirstName) {
    this.providerFirstName = providerFirstName;
  }

  public String getMvaCode() {
    return mvaCode;
  }

  public java.util.Hashtable getExplanations() {
    return explanations;
  }

  public String getServiceLocation() {
    return serviceLocation;
  }

  public void setServiceLocation(String serviceLocation) {
    this.serviceLocation = serviceLocation;
  }

  public void setUt(UtilDateUtilities ut) {
    this.ut = ut;
  }

  public UtilDateUtilities getUt() {
    return ut;
  }

  public String getDemoPhone() {
    return demoPhone;
  }

  public void setDemoPhone(String demoPhone) {
    this.demoPhone = demoPhone;
  }

  public String getHin() {
    return hin;
  }

  public void setHin(String hin) {
    this.hin = hin;
  }

  public String getServiceEndTime() {
    return serviceEndTime;
  }

  public void setServiceEndTime(String serviceEndTime) {
    this.serviceEndTime = serviceEndTime;
  }

  public String getServiceStartTime() {
    return serviceStartTime;
  }

  public void setServiceStartTime(String serviceStartTime) {
    this.serviceStartTime = serviceStartTime;
  }

  public void setServiceToDate(String serviceToDate) {
    this.serviceToDate = serviceToDate;
  }

  public String getServiceToDate() {
    return serviceToDate;
  }

  public String getServiceStartDate() {
    return serviceStartDate;
  }

  public void setServiceStartDate(String serviceStartDate) {
    this.serviceStartDate = serviceStartDate;
  }

  /**
   * Returns an int value representing the date range of a bill's age in days
   * since its initial service date
   * @return int
   */
  public String getServiceDateRange() {
    String ret = "0";
    java.util.Date dt = null;
    try {
      java.text.DateFormat formatter = new java.text.SimpleDateFormat(
          "yyyyMMdd");
      dt = formatter.parse(this.serviceDate);
    }
    catch (Exception e) {
      MiscUtils.getLogger().error("Error", e);
    }
    oscar.util.DateUtils ut = new oscar.util.DateUtils();
    long daysOld = DateUtils.getDifDays(new Date(), dt);
    if (daysOld >= 0 && daysOld <= 30) {
      ret = "1";
    }
    else if (daysOld >= 31 && daysOld <= 60) {
      ret = "2";
    }
    else if (daysOld >= 61 && daysOld <= 90) {
      ret = "3";
    }
    else if (daysOld > 91) {
      ret = "4";
    }
    return ret;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(String paymentDate) {
    this.paymentDate = paymentDate;
  }

  public void setServiceDateRange(String serviceDateRange) {
    this.serviceDateRange = serviceDateRange;
  }

  public String getExp1() {
    return exp1;
  }

  public String getExp2() {
    return exp2;
  }

  public void setExp1(String exp1) {
    this.exp1 = exp1;
  }

  public void setExp2(String exp2) {
    this.exp2 = exp2;
  }

  public void setPayeeName(String payeeName) {
    this.payeeName = payeeName;
  }

  public String getPayeeName() {
    return payeeName;
  }

  public String getProvName() {
    return provName;
  }

  public void setProvName(String provName) {
    this.provName = provName;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public String getSeqNum() {
    return seqNum;
  }

  public void setSeqNum(String seqNum) {
    this.seqNum = seqNum;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getAcctInit() {
    return acctInit;
  }

  public void setAcctInit(String acctInit) {
    this.acctInit = acctInit;
  }

  /**
   * Returns a concatenated string of explanation codes for a specific bill
   * @return String
   */
  public String getExpString() {
    return this.expString;
  }

  public void setExpString(String expString) {
    this.expString = expString;
  }

  /**
   * Returns a formatted summary of the explanation code for a specific rejected bill
   * @todo This really ought to go into a presentation class
   * @return String
   */
  public String getExpSum() {
    String summary = "";
    try{
      if(this.explanations != null){
        Enumeration e = this.explanations.keys();
        while (e.hasMoreElements()) {
          String code = (String) e.nextElement();
          String desc = (String)this.explanations.get(code);
          summary += code + ":" + desc + "\n";
        }
      }
      else{
        MiscUtils.getLogger().debug("null sum=" + this.hashCode());
      }
    }catch(Exception e){
      MiscUtils.getLogger().error("Error", e);
    }
    return summary;
  }

  public void setExpSum(String expSum) {
    this.expSum = expSum;
  }

  public String getAccountNo() {
    return accountNo;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public void setAccountNo(String accountNo) {
    this.accountNo = accountNo;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public String getPaymentMethodName() {
    return paymentMethodName;
  }

  public String getRejectionDate() {
    return rejectionDate;
  }

  public Date getServiceDateDate() {
    return serviceDateDate;
  }

  public String getPayeeNo() {
    return payeeNo;
  }

  public String getAdjustmentCode() {
    return adjustmentCode;
  }

  public String getAdjustmentCodeDesc() {
    return adjustmentCodeDesc;
  }

  public String getAmtOwing() {
    return amtOwing;
  }

  public String getAdjustmentCodeAmt() {
    return adjustmentCodeAmt;
  }

  public void setPaymentMethodName(String paymentMethodName) {
    this.paymentMethodName = paymentMethodName;
  }

  public void setRejectionDate(String rejectionDate) {
    this.rejectionDate = rejectionDate;
  }

  public void setServiceDateDate(Date serviceDateDate) {
    this.serviceDateDate = serviceDateDate;
  }

  public void setPayeeNo(String payeeNo) {
    this.payeeNo = payeeNo;
  }

  public void setAdjustmentCode(String adjustmentCode) {
    this.adjustmentCode = adjustmentCode;
  }

  public void setAdjustmentCodeDesc(String adjustmentCodeDesc) {
    this.adjustmentCodeDesc = adjustmentCodeDesc;
  }

  public void setAmtOwing(String amtOwing) {
    this.amtOwing = amtOwing;
  }

  public void setAdjustmentCodeAmt(String adjustmentCodeAmt) {
    this.adjustmentCodeAmt = adjustmentCodeAmt;
  }
}
