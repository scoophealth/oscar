package oscar.oscarBilling.ca.bc.pageUtil;

/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import oscar.*;
import oscar.oscarBilling.ca.bc.MSP.*;
import oscar.oscarBilling.ca.bc.data.*;
import oscar.oscarDB.*;
import oscar.oscarDemographic.data.*;
import oscar.util.*;
import java.text.NumberFormat;

public class BillingReProcessBillAction
    extends Action {
  Misc misc = new Misc();
  MSPReconcile msp = new MSPReconcile();
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
      ServletException {
    if (request.getSession().getAttribute("user") == null) {
      return (mapping.findForward("Logout"));
    }

    BillingReProcessBillForm frm = (BillingReProcessBillForm) form;
    String dataCenterId = OscarProperties.getInstance().getProperty(
        "dataCenterId");
    String billingmasterNo = frm.getBillingmasterNo();
    String demographicNo = frm.getDemoNo();
    DemographicData demoD = new DemographicData();
    DemographicData.Demographic demo = demoD.getDemographic(demographicNo);

    oscar.oscarBilling.ca.bc.data.BillingFormData billform = new oscar.
        oscarBilling.ca.bc.data.BillingFormData();

    ///
    String providerNo = frm.getProvider_no(); //f
    String demographicFirstName = demo.getFirstName(); //d
    String demographicLastName = demo.getLastName(); //d
    String name_verify = demographicFirstName.substring(0, 1) + " " +
        demographicLastName.substring(0, 2); //d
    String billingGroupNo = billform.getGroupNo(providerNo);
    String practitionerNo = billform.getPracNo(providerNo); //p

    String hcNo = demo.getHIN().trim(); //d
    String dependentNo = frm.getDependentNo(); //f

    String visitLocation = frm.getLocationVisit(); //f
    String clarificationCode = visitLocation.substring(0, 2); //f
    String anatomicalArea = frm.getAnatomicalArea(); //f
    String afterHour = frm.getAfterHours(); //f
    String newProgram = frm.getNewProgram(); //f
    String billingUnit = frm.getBillingUnit(); ///f

    String billingServiceCode = frm.getService_code(); //f
    String billingServicePrice = frm.getBillingAmount(); //f
    String payment_mode = frm.getPaymentMode(); //f
    String serviceDate = frm.getServiceDate(); //f
    String serviceToDate = frm.getServiceToDay(); //f
    String submissionCode = frm.getSubmissionCode(); //f
    String exSubmissionCode = ""; //f
    String dxCode1 = frm.getDx1(); //f
    String dxCode2 = frm.getDx2(); //f
    String dxCode3 = frm.getDx3(); //f
    String dxExpansion = ""; //f
    String serviceLocation = frm.getServiceLocation();
    String referralFlag1 = frm.getReferalPracCD1(); //f
    String referralNo1 = frm.getReferalPrac1(); //f
    String referralFlag2 = frm.getReferalPracCD2(); //f
    String referralNo2 = frm.getReferalPrac2(); //f
    String timeCall = frm.getTimeCallRec(); //f
    String serviceStartTime = frm.getStartTime(); //f
    String serviceEndTime = frm.getFinishTime(); //f
    String birthDate = demo.getDob(); //d
    String correspondenceCode = frm.getCorrespondenceCode(); //f
    String claimComment = frm.getShortComment(); //f

    String billingStatus = frm.getStatus(); //f

    String facilityNum = frm.getFacilityNum();
    String facilitySubNum = frm.getFacilitySubNum();

    String originalMSPNumber = misc.forwardZero("", 20);

    String oinInsurerCode = frm.getInsurerCode(); //f
    String oinRegistrationNo = demo.getHIN(); //d
    String oinBirthdate = demo.getDob(); //d
    String oinFirstName = demo.getFirstName(); //d
    String oinSecondName = ""; //d
    String oinSurname = demo.getLastName(); //d
    String oinSexCode = demo.getSex(); //d
    String oinAddress = demo.getAddress(); //d
    String oinAddress2 = demo.getCity(); //d
    String oinAddress3 = ""; //d
    String oinAddress4 = ""; //d
    String oinPostalcode = demo.getPostal(); //d

    String hcType = demo.getHCType(); //d

    String messageNotes = frm.getMessageNotes();
    String billRegion = OscarProperties.getInstance().getProperty("billregion");
    String submit = frm.getSubmit();
    String secondSQL = null;

    if ( (submit.equals("Resubmit Bill") || submit.equals("Reprocess and Resubmit Bill")) || billingStatus.equals("O")) {
      billingStatus = "O";
      secondSQL = "update billing set status = 'O' where billing_no ='" + frm.getBillNumber() + "'";
    } else if (submit.equals("Settle Bill")) {
      billingStatus = "S";
    }

    if (hcType.equals(billRegion)) { //if its bc go on
      oinInsurerCode = "";
      oinRegistrationNo = "";
      oinBirthdate = "";
      oinFirstName = "";
      oinSecondName = "";
      oinSurname = "";
      oinSexCode = "";
      oinAddress = "";
      oinAddress2 = "";
      oinAddress3 = "";
      oinAddress4 = "";
      oinPostalcode = "";

    } else { //other provinces
      oinInsurerCode = hcType;
      hcNo = "000000000";
      name_verify = "0000";
    }

    if (submissionCode.equals("E")) {
      String seqNum = frm.getDebitRequestSeqNum();
      String dateRecieved = frm.getDebitRequestDate();
      try {
        dateRecieved = dateRecieved.trim();
        Integer.parseInt(dateRecieved);
      }catch (Exception e) {
        e.printStackTrace();
        dateRecieved = "";
      }

      originalMSPNumber = constructOriginalMSPNumber(dataCenterId, seqNum,dateRecieved);
    }
    /**
     * Check the bill type, if it has been changed by the user
     * we need to ensure that the correct fee code is associated
     * e.g. If bill is changed from MSP to Private, the correct private fee must be retrieved
     *
     */

    String persistedBillType = this.getPersistedBillType(billingmasterNo);
    if (persistedBillType != null) {
      if (!persistedBillType.equals(billingStatus)) {
        //if the bill status was changed to "Bill Patient
        //And the persisted bill status is anything but private
        if (msp.BILLPATIENT.equals(billingStatus) &&
            !msp.PAIDPRIVATE.equals(persistedBillType)) {
          //get the correct the Private code representation
          //and correct code amount if applicable
          //yes, this is lame. Private codes are simply the standard msp
          //code with the letter 'A' prepended. The current db design should really
          //have a 'fees' associative table
          //get the private fee data if it exists
          String[] privateCodeRecord = getServiceCodePrice(billingServiceCode,true);
          if (privateCodeRecord != null && privateCodeRecord.length == 1) {
            billingServiceCode = "A" + billingServiceCode;
            billingServicePrice = privateCodeRecord[0];

          }
        }
      }
    } else {
      throw new RuntimeException("BILLING BC - " + new java.util.Date().toString() + " - billingmaster_no " + billingmasterNo + " doesnt't seem to have a type");
    }

    //Multiply the bill amount by the units - Fixes bug where wrong amount being sent to MSP

    try{

      String[] codeRecord = getServiceCodePrice(billingServiceCode,false);
      String codePrice = "";
      if (codeRecord != null && codeRecord.length > 0) {
          codePrice = codeRecord[0];
          System.out.println("codePrice=" + codePrice);
      }

      if("E".equals(payment_mode)){
          codePrice = "0.00";
      }

      double dblBillAmount = Double.parseDouble(codePrice);
      double dblUnit = Double.parseDouble(billingUnit);
      double amtTemp = dblBillAmount * dblUnit;
      String fmtStr = NumberFormat.getCurrencyInstance().format(amtTemp);
      billingServicePrice = fmtStr.substring(1,fmtStr.length());
      billingServicePrice = billingServicePrice.replaceAll(",","");
    } catch(NumberFormatException e){
      e.printStackTrace();
      throw new RuntimeException("BC BILLING - Exception when attempting to multiply Bill Amount by Unit ");
    }


    String sql = "update billingmaster set "
        + "datacenter = '" + dataCenterId + "', "
        //TODO
        + "payee_no = '" + billingGroupNo + "', "
        + "practitioner_no = '" + practitionerNo + "', "
        + "phn = '" + hcNo + "', "
        + "name_verify = '" + UtilMisc.mysqlEscape(name_verify) + "', "
        + "dependent_num = '" + dependentNo + "', "
        + "billing_unit = '" + billingUnit + "', "
        + "clarification_code = '" + clarificationCode + "', "
        + "anatomical_area = '" + anatomicalArea + "', "
        + "after_hour = '" + afterHour + "', "
        + "new_program = '" + newProgram + "', "
        + "billing_code = '" + billingServiceCode + "', "
        + "bill_amount = '" + billingServicePrice + "', "
        + " payment_mode = '" + payment_mode + "', "
        + " service_date = '" + convertDate8Char(serviceDate) + "', "
        + " service_to_day = '" + serviceToDate + "', "
        + "submission_code = '" + submissionCode + "', "
        + "extended_submission_code = '" + exSubmissionCode + "', "
        + "dx_code1 = '" + dxCode1 + "', "
        + "dx_code2 = '" + dxCode2 + "', "
        + "dx_code3 = '" + dxCode3 + "', "
        + "dx_expansion = '" + dxExpansion + "', "
        + "service_location = '" + serviceLocation + "', "
        + "referral_flag1 = '" + referralFlag1 + "', "
        + "referral_no1 = '" + referralNo1 + "', "
        + "referral_flag2 = '" + referralFlag2 + "', "
        + "referral_no2 = '" + referralNo2 + "', "
        + "time_call = '" + timeCall + "', "
        + "service_start_time = '" + serviceStartTime + "', "
        + "service_end_time = '" + serviceEndTime + "', "
        + "birth_date = '" + birthDate + "', "
        + "correspondence_code = '" + correspondenceCode + "', "
        + "claim_comment = '" + claimComment + "', "
        + "original_claim = '" + originalMSPNumber + "',"
        + "facility_no = '" + facilityNum + "',"
        + "facility_sub_no = '" + facilitySubNum + "',"

        + "oin_insurer_code = '" + oinInsurerCode + "', "
        + "oin_registration_no = '" + UtilMisc.mysqlEscape(oinRegistrationNo) +
        "', "
        + "oin_birthdate = '" + UtilMisc.mysqlEscape(oinBirthdate) + "', "
        + "oin_first_name = '" + UtilMisc.mysqlEscape(oinFirstName) + "', "
        + "oin_second_name = '" + UtilMisc.mysqlEscape(oinSecondName) + "', "
        + "oin_surname = '" + UtilMisc.mysqlEscape(oinSurname) + "', "
        + "oin_sex_code = '" + UtilMisc.mysqlEscape(oinSexCode) + "', "
        + "oin_address = '" + UtilMisc.mysqlEscape(oinAddress) + "', "
        + "oin_address2 = '" + UtilMisc.mysqlEscape(oinAddress2) + "', "
        + "oin_address3 = '" + UtilMisc.mysqlEscape(oinAddress3) + "', "
        + "oin_address4 = '" + UtilMisc.mysqlEscape(oinAddress4) + "', "
        + "oin_postalcode = '" + UtilMisc.mysqlEscape(oinPostalcode) + "'  "
        + " where billingmaster_no  = '" + billingmasterNo + "'";

    String providerSQL = "update billing set provider_no = '" + providerNo +
        "' where billing_no ='" + frm.getBillNumber() + "'";

    System.out.println("\n" + sql + "\n");
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      db.RunSQL(sql);
      db.RunSQL(providerSQL);
      if (!StringUtils.isNullOrEmpty(billingStatus)) {  //What if billing status is null?? the status just doesn't get updated but everything else does??'
        msp.updateBillingStatus(frm.getBillNumber(), billingStatus,billingmasterNo);
      }
      BillingHistoryDAO dao = new BillingHistoryDAO();
      //If the adjustment amount field isn't empty, create an archive of the adjustment
      if (frm.getAdjAmount() != null && !"".equals(frm.getAdjAmount())) {
        double dblAdj = Math.abs(new Double(frm.getAdjAmount()).doubleValue());
        //if 1 this adjustment is a debit
        if ("1".equals(frm.getAdjType())) {
          dblAdj = dblAdj * -1.0;
        }
        dao.createBillingHistoryArchive(frm.getBillingmasterNo(), dblAdj,
                                        MSPReconcile.PAYTYPE_IA);
        msp.settleIfBalanced(frm.getBillingmasterNo());
      }
      else {
        dao.createBillingHistoryArchive(billingmasterNo);
      }
      if (secondSQL != null) {
        System.out.println(secondSQL);
        db.RunSQL(secondSQL);
      }

      if (correspondenceCode.equals("N") || correspondenceCode.equals("B")) {
        MSPBillingNote n = new MSPBillingNote();
        n.addNote(billingmasterNo,
                  (String) request.getSession().getAttribute("user"),
                  frm.getNotes());
      }

      if (messageNotes != null) {
        BillingNote n = new BillingNote();
        if (n.hasNote(billingmasterNo) || !messageNotes.trim().equals("")) {
          n.addNote(billingmasterNo,
                    (String) request.getSession().getAttribute("user"),
                    messageNotes);
        }
      }

      db.CloseConn();
    }
    catch (SQLException e3) {
      System.out.println(e3.getMessage());
    }

    request.setAttribute("billing_no", billingmasterNo);
    if (submit.equals("Reprocess and Resubmit Bill")) {
      request.setAttribute("close", "true");
    }
    return mapping.findForward("success");
  }


  private String[] getServiceCodePrice(String billingServiceCode,boolean usePrefix) {
    String prepend = usePrefix?"A":"";
    String[] privateCodeRecord = SqlUtils.getRow(
        "select value from billingservice where service_code = '" + prepend + billingServiceCode + "'");
    return privateCodeRecord;
  }

  /**
   * getPersistedBillType
   *
   * @param billingmasterNo String
   * @return String
   */
  private String getPersistedBillType(String billingmasterNo) {
    String qry = "select billingstatus from billingmaster where billingmaster.billingmaster_no = " +
        billingmasterNo;
    String row[] = SqlUtils.getRow(qry);
    String ret = null;
    if (row != null) {
      ret = row[0];
    }
    return ret;
  }

  /**
   * @todo THis belongs in a utility class
   * @param s String
   * @return String
   */
  public String convertDate8Char(String s) {
    String sdate = "00000000", syear = "", smonth = "", sday = "";
    System.out.println("s=" + s);
    if (s != null) {

      if (s.indexOf("-") != -1) {

        syear = s.substring(0, s.indexOf("-"));
        s = s.substring(s.indexOf("-") + 1);
        smonth = s.substring(0, s.indexOf("-"));
        if (smonth.length() == 1) {
          smonth = "0" + smonth;
        }
        s = s.substring(s.indexOf("-") + 1);
        sday = s;
        if (sday.length() == 1) {
          sday = "0" + sday;
        }

        System.out.println("Year" + syear + " Month" + smonth + " Day" + sday);
        sdate = syear + smonth + sday;

      }
      else {
        sdate = s;
      }
      System.out.println("sdate:" + sdate);
    }
    else {
      sdate = "00000000";

    }
    return sdate;
  }

  public String constructOriginalMSPNumber(String dataCenterNum, String seqNum,
                                           String dateRecieved) {
    String retval = "";

    retval = misc.forwardZero(dataCenterNum, 5) + misc.forwardZero(seqNum, 7) +
        misc.forwardZero(dateRecieved, 8);
    return retval;
  }

}
