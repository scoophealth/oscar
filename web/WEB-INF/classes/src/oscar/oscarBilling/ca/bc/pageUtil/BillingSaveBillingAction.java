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
package oscar.oscarBilling.ca.bc.pageUtil;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.OscarProperties;
import oscar.oscarBilling.ca.bc.MSP.MSPBillingNote;
import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;
import oscar.oscarBilling.ca.bc.data.BillingHistoryDAO;
import oscar.oscarBilling.ca.bc.data.BillingNote;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.data.EChartDAO;
import oscar.oscarEncounter.data.Echart;

public class BillingSaveBillingAction
    extends Action {
  private static Log log = LogFactory.getLog(BillingSaveBillingAction.class);
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
      ServletException {

    if (request.getSession().getAttribute("user") == null) {
      return (mapping.findForward("Logout"));
    }

    BillingSaveBillingForm frm = (BillingSaveBillingForm) form;

    oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean bean;
    bean = (oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean) request.
        getSession().getAttribute("billingSessionBean");
    //  oscar.oscarBilling.data.BillingStoreData bsd = new oscar.oscarBilling.data.BillingStoreDate();
    //  bsd.storeBilling(bean);
    
    
    
    
    
    
    
    oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
    String billStatus = as.billStatus(bean.getApptStatus());

    java.sql.ResultSet rs;
    GregorianCalendar now = new GregorianCalendar();
    int curYear = now.get(Calendar.YEAR);
    int curMonth = (now.get(Calendar.MONTH) + 1);
    int curDay = now.get(Calendar.DAY_OF_MONTH);
    String curDate = String.valueOf(curYear) + "-" + String.valueOf(curMonth) +
        "-" + String.valueOf(curDay);
    String billingid = "";
    ArrayList<String> billingIds = new ArrayList();
    String dataCenterId = OscarProperties.getInstance().getProperty(
        "dataCenterId");
    String billingMasterId = "";
   
    
    ///Update Appointment information
    log.debug("appointment_no: " + bean.getApptNo());
    log.debug("BillStatus:" + billStatus);
    String sql = "update appointment set status='" + billStatus +
        "' where appointment_no='" + bean.getApptNo() + "'";

    try {  
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      db.RunSQL(sql);
      db.CloseConn();

    }
    catch (SQLException e) {
      log.error(e.getMessage(),e);
      log.error("LLLOOK: APPT ERROR FOR demo:" + bean.getPatientName() +
                         " date " + curDate);
      e.printStackTrace();
    }
    ////End of updating appt information

    char billingAccountStatus = 'O';
    
    if (bean.getBillingType().equals("DONOTBILL")) {
      bean.setBillingType("MSP"); //RESET this to MSP to get processed
      billingAccountStatus = 'N';
    }else if (bean.getBillingType().equals("WCB")) {
      billingAccountStatus = 'O';
    }else if(MSPReconcile.BILLTYPE_PRI.equals(bean.getBillingType())){
     billingAccountStatus = 'P';
    }
    
    
    String billingSQL = insertIntoBilling(bean, curDate, billingAccountStatus);
    

    ArrayList billItem = bean.getBillItem();
    
    char paymentMode = (bean.getEncounter().equals("E") && !bean.getBillingType().equals("ICBC") && !bean.getBillingType().equals("Pri") && !bean.getBillingType().equals("WCB")) ? 'E' : '0';

    String billedAmount;
    if (bean.getBillingType().equals("MSP") || bean.getBillingType().equals("ICBC") || bean.getBillingType().equals("Pri")) {
      for (int i = 0; i < billItem.size(); i++) { 
        billingid = getInsertIdFromBilling(billingSQL);  
        log.debug("billing id "+billingid+"   sql "+billingSQL);
        billingIds.add(billingid);
        if (paymentMode == 'E') {
          billedAmount = "0.00";
        }
        else {
          billedAmount = ( (oscar.oscarBilling.ca.bc.pageUtil.
                            BillingBillingManager.BillingItem) billItem.get(i)).
              getDispLineTotal();
        }

        if (bean.getPatientHCType().trim().compareTo(bean.getBillRegion().trim()) ==
            0) {

          //			| billing_unit             | char(3)     | YES  |     | 000                  |                |
          //			| clarification_code       | char(2)     | YES  |     | 00                   |                |
          //			| anatomical_area          | char(2)     | YES  |     | NULL                 |                |
          //			| after_hour               | char(1)     | YES  |     | 0                    |                |
          //			| new_program              | char(2)     | YES  |     | 00                   |                |
          //			| billing_code             | varchar(5)  | YES  |     | 00000                |                |
          //			| bill_amount              | varchar(7)  | YES  |     | 0000000              |                |
          //			| payment_mode             | char(1)     | YES  |     | 0                    |                |
          //			| service_date             | varchar(8)  | YES  |     | 00000000             |                |
          //			| service_to_day           | char(2)     | YES  |     | 00                   |                |
          //			| submission_code          | char(1)     | YES  |     | 0                    |                |
          //			| extended_submission_code | char(1)     | YES  |     |                      |                |
          //			| dx_code1                 | varchar(5)  | YES  |     |                      |                |
          //			| dx_code2                 | varchar(5)  | YES  |     |                      |                |
          //			| dx_code3                 | varchar(5)  | YES  |     |                      |                |
          //			| dx_expansion             | varchar(15) | YES  |     |                      |                |
          //			| service_location         | char(1)     | YES  |     | 0                    |                |
          //			| referral_flag1           | char(1)     | YES  |     | 0                    |                |
          //			| referral_no1             | varchar(5)  | YES  |     | 00000                |                |
          //			| referral_flag2           | char(1)     | YES  |     | 0                    |                |
          //			| referral_no2             | varchar(5)  | YES  |     | 00000                |                |
          //			| time_call                | varchar(4)  | YES  |     | 0000                 |                |
          //			| service_start_time       | varchar(4)  | YES  |     | 0000                 |                |
          //			| service_end_time         | varchar(4)  | YES  |     | 0000                 |                |
          //			| birth_date               | varchar(8)  | YES  |     | 00000000             |                |
          //			| office_number            | varchar(7)  | YES  |     | 0000000              |                |
          //			| correspondence_code      | char(1)     | YES  |     | 0                    |                |
          //			| claim_comment            | varchar(20) | YES  |     | NULL                 |                |
          //			| mva_claim_code           | char(1)     | YES  |     | N                    |                |
          //			| icbc_claim_no            | varchar(8)  | YES  |     | 00000000             |                |
          //			| original_claim           | varchar(20) | YES  |     | 00000000000000000000 |                |
          //			| facility_no              | varchar(5)  | YES  |     | 00000                |                |
          //			| facility_sub_no          | varchar(5)  | YES  |     | 00000                |                |
          //			| filler_claim             | varchar(58) | YES  |     | NULL                 |                |
          //			| oin_insurer_code         | char(2)     | YES  |     |                      |                |
          //			| oin_registration_no      | varchar(12) | YES  |     |                      |                |
          //			| oin_birthdate            | varchar(8)  | YES  |     |                      |                |
          //			| oin_first_name           | varchar(12) | YES  |     |                      |                |
          //			| oin_second_name          | char(1)     | YES  |     |                      |                |
          //			| oin_surname              | varchar(18) | YES  |     |                      |                |
          //			| oin_sex_code             | char(1)     | YES  |     |                      |                |
          //			| oin_address              | varchar(25) | YES  |     |                      |                |
          //			| oin_address2             | varchar(25) | YES  |     |                      |                |
          //			| oin_address3             | varchar(25) | YES  |     |                      |                |
          //			| oin_address4             | varchar(25) | YES  |     |                      |                |
          //			| oin_postalcode           | varcha

          sql = "insert into billingmaster (billingmaster_no, billing_no, createdate, billingstatus,demographic_no, appointment_no, claimcode, datacenter, payee_no, practitioner_no, phn, name_verify, dependent_num,billing_unit,"
              + "clarification_code, anatomical_area, after_hour, new_program, billing_code, bill_amount, payment_mode, service_date, service_to_day, submission_code, extended_submission_code, dx_code1, dx_code2, dx_code3, "
              + "dx_expansion, service_location, referral_flag1, referral_no1, referral_flag2, referral_no2, time_call, service_start_time, service_end_time, birth_date, office_number, correspondence_code, claim_comment,mva_claim_code, icbc_claim_no,facility_no,facility_sub_no,paymentMethod) "
              + "values ('\\N',"
              + "'" + billingid + "',"
              + "NOW()," //CURRENT_TIMESTAMP
              + "'" + billingAccountStatus + "'," //status
              + "'" + bean.getPatientNo() + "',"
              + "'" + bean.getApptNo() + "',"
              + "'C02',"
              + "'" + dataCenterId + "',"
              + "'" + bean.getBillingGroupNo() + "',"
              + "'" + bean.getBillingPracNo() + "',"
              + "'" + bean.getPatientPHN() + "',"
              + "'" + oscar.util.UtilMisc.mysqlEscape(bean.getPatientFirstName().substring(0, 1) + " " + bean.getPatientLastName().substring(0, 2)) + "',"
              + "'" + bean.getDependent() + "',"
              + "'" + ( (oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem) billItem.get(i)).getUnit() + "',"
              + "'" + bean.getVisitLocation().substring(0, 2) + "',"
              + "'00'," //anatomical_area
              + "'" + bean.getAfterHours() + "'," //after_hours
              + "'00'," //new_program
              + "'" + ( (oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem) billItem.get(i)).getServiceCode() + "',"
              + "'" + billedAmount + "',"
              + "'" + paymentMode + "'," //payment_mode
              + "'" + convertDate8Char(bean.getServiceDate()) + "',"
              + "'" + bean.getService_to_date() + "'," //service_to_day
              + "'" + bean.getSubmissionCode() + "'," //submission_code
              + "' '," //extended_submission_code
              + "'" + bean.getDx1() + "',"
              + "'" + bean.getDx2() + "',"
              + "'" + bean.getDx3() + "',"
              + "' '," //dx_expansion
              + "'" + bean.getVisitType().substring(0, 1) + "',"
              + "'" + bean.getReferType1() + "',"
              + "'" + bean.getReferral1() + "',"
              + "'" + bean.getReferType2() + "',"
              + "'" + bean.getReferral2() + "',"
              + "'" + bean.getTimeCall() + "'," //time_call
              + "'" + bean.getStartTime() + "',"
              + "'" + bean.getEndTime() + "',"
              + "'" + convertDate8Char(bean.getPatientDoB()) + "',"
              + "''," //office number
              + "'" + bean.getCorrespondenceCode() + "'," //correspondence code
              + "'" + bean.getShortClaimNote() + "'," //claim short comment
              + "'" + bean.getMva_claim_code() + "',"
              + "'" + bean.getIcbc_claim_no() + "',"
              + "'" + bean.getFacilityNum() + "',"
              + "'" + bean.getFacilitySubNum() + "',"
              + bean.getPaymentType()
              + ")";
          try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            db.RunSQL(sql);

            //Store a record of this transaction
            billingMasterId = getLastInsertId(db);
            String status = new String(new char[] {billingAccountStatus});
            this.createBillArchive(billingMasterId);
            db.CloseConn();
          }
          catch (SQLException e) {
            log.error(e.getMessage(),e);
          }
          log.debug(sql);
        }
        else {

          //			| oin_insurer_code         | char(2)     | YES  |     |                      |                |
          //			| oin_registration_no      | varchar(12) | YES  |     |                      |                |
          //			| oin_birthdate            | varchar(8)  | YES  |     |                      |                |
          //			| oin_first_name           | varchar(12) | YES  |     |                      |                |
          //			| oin_second_name          | char(1)     | YES  |     |                      |                |
          //			| oin_surname              | varchar(18) | YES  |     |                      |                |
          //			| oin_sex_code             | char(1)     | YES  |     |                      |                |
          //			| oin_address              | varchar(25) | YES  |     |                      |                |
          //			| oin_address2             | varchar(25) | YES  |     |                      |                |
          //			| oin_address3             | varchar(25) | YES  |     |                      |                |
          //			| oin_address4             | varchar(25) | YES  |     |                      |                |
          //			| oin_postalcode           | varcha

          sql = "insert into billingmaster (billingmaster_no, billing_no, createdate, billingstatus,demographic_no, appointment_no, claimcode, datacenter, payee_no, practitioner_no, phn, name_verify, dependent_num,billing_unit,"
              + "clarification_code, anatomical_area, after_hour, new_program, billing_code, bill_amount, payment_mode, service_date, service_to_day, submission_code, extended_submission_code, dx_code1, dx_code2, dx_code3, "
              + "dx_expansion, service_location, referral_flag1, referral_no1, referral_flag2, referral_no2, time_call, service_start_time, service_end_time, birth_date, office_number, correspondence_code, claim_comment, "
              + "oin_insurer_code, oin_registration_no, oin_birthdate, oin_first_name, oin_second_name, oin_surname,oin_sex_code, oin_address, oin_address2, oin_address3, oin_address4, oin_postalcode, mva_claim_code, icbc_claim_no,facility_no,facility_sub_no) "
              + "values ('\\N'," +
              "'" + billingid + "'," +
              "NOW()," + //NOW
              "'" + billingAccountStatus + "'," +
              "'" + bean.getPatientNo() + "'," +
              "'" + bean.getApptNo() + "'," +
              "'C02'," +
              "'" + dataCenterId + "'," +
              "'" + bean.getBillingGroupNo() + "'," +
              "'" + bean.getBillingPracNo() + "'," +
              "'" + "0000000000" + "'," + //phn
              "'" + "0000" + "'," + //name_verify
              "'" + "00" + "'," + //dependent_num
              "'" +
              ( (oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.
                 BillingItem)
               billItem.get(i)).getUnit() + "'," +
              "'" + bean.getVisitLocation().substring(0, 2) + "'," +
              "'00'," + //anatomical_area
              "'" + bean.getAfterHours() + "'," + //after_hour
              "'00'," + //new_program
              "'" +
              ( (oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.
                 BillingItem)
               billItem.get(i)).getServiceCode() + "'," +
              "'" +
              ( (oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.
                 BillingItem)
               billItem.get(i)).getDispLineTotal() + "'," +
              "'" + paymentMode + "'," + //payment_mode
              "'" + convertDate8Char(bean.getServiceDate()) + "'," +
              "'" + bean.getService_to_date() + "'," + //service_to_date
              "'" + bean.getSubmissionCode() + "'," + //submission_code
              "' '," + //extendednt_-submission_code
              "'" + bean.getDx1() + "'," +
              "'" + bean.getDx2() + "'," +
              "'" + bean.getDx3() + "'," +
              "' '," + //dx_expansion
              "'" + bean.getVisitType().substring(0, 1) + "'," +
              "'" + bean.getReferType1() + "'," +
              "'" + bean.getReferral1() + "'," +
              "'" + bean.getReferType2() + "'," +
              "'" + bean.getReferral2() + "'," +
              "'" + bean.getTimeCall() + "'," + //time_call
              "'" + bean.getStartTime() + "'," +
              "'" + bean.getEndTime() + "'," +
              "'" + "00000000" + "'," + //birth_date
              "''," + //office_number
              " '" + bean.getCorrespondenceCode() + "'," + //correspondence code
              "'" + bean.getShortClaimNote() + "'," + //claim_comment
              "'" + bean.getPatientHCType() + "'," +
              "'" + bean.getPatientPHN() + "'," +
              "'" + convertDate8Char(bean.getPatientDoB()) + "'," +
              "'" + oscar.util.UtilMisc.mysqlEscape(bean.getPatientFirstName()) + "'," +
              "'" + " " + "'," + //oin_second_name
              "'" + oscar.util.UtilMisc.mysqlEscape(bean.getPatientLastName()) + "'," +
              "'" + bean.getPatientSex() + "'," +
              "'" + bean.getPatientAddress1() + "'," +
              "'" + bean.getPatientAddress2() + "'," +
              "''," + //oin_address3
              "''," + //oin_address3
              "'" + bean.getPatientPostal() + "'," +
              "'" + bean.getMva_claim_code() + "'," +
              "'" + bean.getIcbc_claim_no() + "'," +
              "'" + bean.getFacilityNum() + "'," +
              "'" + bean.getFacilitySubNum() + "'" +
              ")";

          try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            db.RunSQL(sql);
            billingMasterId = getLastInsertId(db);
            String status = new String(new char[] {billingAccountStatus});
        this.createBillArchive(billingMasterId);

            db.CloseConn();
          }
          catch (SQLException e) {
            log.error(e.getMessage(),e);
          }
          log.debug(sql);
        }

      }
      if (bean.getCorrespondenceCode().equals("N") || bean.getCorrespondenceCode().equals("B")) {
        try {
          MSPBillingNote n = new MSPBillingNote();
          n.addNote(billingMasterId, bean.getCreator(), bean.getNotes());
        }
        catch (SQLException e) {
          log.error(e.getMessage(),e);
        }
      }
      if (bean.getMessageNotes() != null ||
          !bean.getMessageNotes().trim().equals("")) {
        try {
          BillingNote n = new BillingNote();
          n.addNote(billingMasterId, bean.getCreator(), bean.getMessageNotes());
        }
        catch (SQLException e) {
          log.error(e.getMessage(),e);
        }

      }

    }
    //////////////
    if (bean.getBillingType().equals("WCB")) {
      //Keep in mind that the first billingId was set way up at the top
      //NOT ANY MORE  
      billingid = getInsertIdFromBilling(billingSQL);  
      billingIds.add(billingid);
      DBHandler db = null;
      String status = new String(new char[] {billingAccountStatus});
      WCBForm wcb = (WCBForm) request.getSession().getAttribute("WCBForm");
      wcb.setW_demographic(bean.getPatientNo());
      wcb.setW_providerno(bean.getBillingProvider());
      String insertBillingMaster = createBillingMasterInsertString(bean,
          billingid, billingAccountStatus, wcb.getW_payeeno());
      String amnt = getFeeByCode(wcb.getW_feeitem());
      try {
        //Save new bill in billingmaster table
        db = new DBHandler(DBHandler.OSCAR_DATA);
        db.RunSQL(insertBillingMaster);

        //Store an archive of this transaction
        billingMasterId = getLastInsertId(db);
         this.createBillArchive(billingMasterId);
      //  this.createBillArchive(billingMasterId, status);
/**
        //save extra fee item entry in wcb table
        if (wcb.isNotBilled()) {
          //if processing an existing WCB form, update values for first fee item
          String updateWCBSQL = createWCBUpdateSQL(billingid, amnt,
              wcb.getWcbFormId());
          db.RunSQL(updateWCBSQL);
        }
        else {
          //This form was created from the billing screen
          //Store a new WCB entry for the first fee item
          db.RunSQL(wcb.SQL(billingid, amnt));
        }
      **/

         //fixes bug where service location not being set

         //for some bizarre reason billing table stores location with trailing '|' e.g 'A|'
         //whereas WCB table stores it as single char.
         String serviceLocation = bean.getVisitType().substring(0);
         wcb.setW_servicelocation(serviceLocation);
         db.RunSQL(wcb.SQL(billingid, amnt));

        //If an extra fee item was declared on the WCB form, save it in
        //The billingmaster table as well
        if (wcb.getW_extrafeeitem() != null &&
            wcb.getW_extrafeeitem().trim().length() != 0) {
          log.debug("Adding Second billing item");
          String secondWCBBillingId = null;
          String secondBillingAmt = this.getFeeByCode(wcb.getW_extrafeeitem());
          //save entry in billing table
          db.RunSQL(billingSQL);
          secondWCBBillingId = this.getLastInsertId(db);
          billingIds.add(secondWCBBillingId);
          //Link new billing record to billing line in billingmaster table
          String secondBillingMaster = createBillingMasterInsertString(bean,
              secondWCBBillingId, billingAccountStatus, wcb.getW_payeeno());
          db.RunSQL(secondBillingMaster);
          //get most recent billingmaster id
          billingMasterId = getLastInsertId(db);

          //Store a record of this billingmaster Transaction
          status = new String(new char[] {billingAccountStatus});
           this.createBillArchive(billingMasterId);
          //this.createBillArchive(billingMasterId, status);

          //save extra fee item entry in wcb table
          /**
          if (wcb.isNotBilled()) {
            //if processing an existing WCB form, update values for second fee item
            String updateWCBSQL = createWCBUpdateSQL(secondWCBBillingId,
                secondBillingAmt, wcb.getWcbFormId());
            db.RunSQL(updateWCBSQL);
          }
          else {
            //This form was created from the billing screen
            //Store a new WCB entry for the second fee item
            db.RunSQL(wcb.secondSQLItem(secondWCBBillingId, secondBillingAmt));
          }**/
          db.RunSQL(wcb.secondSQLItem(secondWCBBillingId, secondBillingAmt));

          //Update patient echart with the clinical info from the WCB form
          updatePatientChartWithWCBInfo(wcb);
        }
      }
      catch (SQLException e) {
        log.error(e.getMessage(),e);
        e.printStackTrace();
      }

      finally {
        if (db != null) {
          try {
            db.CloseConn();
          }
          catch (SQLException ex) {
            log.error(ex.getMessage(),ex);  
            ex.printStackTrace();
          }
        }
      }
      request.getSession().setAttribute("WCBForm", null);
    }

    ////////////////////
    //      log.debug("Service count : "+ billItem.size());
    ActionForward af = mapping.findForward("success");
    if (frm.getSubmit().equals("Another Bill")) {
      bean.setBillForm("GP");
      af = mapping.findForward("anotherBill");

    }
    else if (frm.getSubmit().equals("Save & Print Receipt")) {
      StringBuffer stb = new StringBuffer();
      for(String s:billingIds){
          log.debug("String "+s);
          stb.append("billing_no="+s+"&");
      }
      log.debug("FULL STRING "+stb.toString());
      af = new ActionForward("/billing/CA/BC/billingView.do?"+stb.toString()+ "receipt=yes");
      af.setRedirect(true);
    }
    return af; //(mapping.findForward("success"));
  }

    private String getInsertIdFromBilling(final String billingSQL) {
        String billingId ="";
        try {
          DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
          db.RunSQL(billingSQL);
          java.sql.ResultSet rs = db.GetSQL("SELECT LAST_INSERT_ID()");
          if (rs.next()) {
            billingId = rs.getString(1);
          }
          rs.close();
          db.CloseConn();
        }
        catch (SQLException e) {
          log.error(e.getMessage(),e);
          e.printStackTrace();
        }
        return billingId;
    }

    private String insertIntoBilling(final oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean bean, final String curDate, final char billingAccountStatus) {

        //TODO STILL NEED TO ADD EXTRA FIELDS for dotes and bill type
        String billingSQL = "insert into billing (demographic_no, provider_no,appointment_no, demographic_name,hin,update_date, billing_date, total, status, dob, visitdate, visittype, provider_ohip_no, apptProvider_no, creator,billingtype)"
            + " values(" +
            "'" + bean.getPatientNo() + "'," +
            "'" + bean.getBillingProvider() + "', " +
            "'" + bean.getApptNo() + "'," +
            "'" + oscar.util.UtilMisc.mysqlEscape(bean.getPatientName()) + "'," +
            "'" + bean.getPatientPHN() + "'," +
            "'" + curDate + "'," +
            "'" + bean.getServiceDate() + "'," +
            "'" + bean.getGrandtotal() + "'," +
            "'" + billingAccountStatus + "'," + //status
            "'" + bean.getPatientDoB() + "'," +
            "'" + bean.getAdmissionDate() + "'," +
            "'" + oscar.util.UtilMisc.mysqlEscape(bean.getVisitType()) + "'," +
            "'" + bean.getBillingPracNo() + "'," +
            "'" + bean.getApptProviderNo() + "'," +
            "'" + bean.getCreator() + "'," +
            "'" + bean.getBillingType() + "')";
        return billingSQL;
    }

  private String createBillingMasterInsertString(BillingSessionBean bean,
                                                 String billingid,
                                                 char billingAccountStatus,
                                                 String payeeNo) {
    String insertBillingMaster =
        " INSERT INTO billingmaster (billing_no, createdate, payee_no, billingstatus, demographic_no, appointment_no,service_date) " +
        " VALUES ('" + billingid + "',NOW(),'" + payeeNo + "','" + billingAccountStatus + "','" + bean.getPatientNo() + "','" + bean.getApptNo() + "','" + convertDate8Char(bean.getServiceDate()) + "')";
    return insertBillingMaster;
  }

  private String getFeeByCode(String feeitem) {
    ResultSet rs = null;
    DBHandler db = null;
    String amnt = "0.00";
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = db.GetSQL("SELECT value FROM billingservice WHERE service_code='" +
                     feeitem + "'");
      if (rs.next()) {
        amnt = rs.getString("value");
      }
    }
    catch (SQLException ex) {
       log.error(ex.getMessage(),ex);
       ex.printStackTrace();
    }
    finally {
      if (db != null) {
        try {
          db.CloseConn();
        }
        catch (SQLException ex1) {
          log.error(ex1.getMessage(),ex1);  
          ex1.printStackTrace();
        }
      }
      if (rs != null) {
        try {
          rs.close();
        }
        catch (SQLException ex2) {
          log.error(ex2.getMessage(),ex2);  
          ex2.printStackTrace();
        }
      }
    }

    return amnt;
  }

  private void updatePatientChartWithWCBInfo(
      WCBForm wcb) {
    EChartDAO dao = new EChartDAO();
    Echart echart = dao.getMostRecentEchart(wcb.getDemographic());

    //if the patient doesn't have an echart than create  a new one
    if (echart == null) {
      echart = new Echart();
      echart.setDemographicNo(wcb.getDemographic());
      echart.setProviderNo(wcb.getW_pracno());
    }

    String wcbEchartEntry = "\n\n[WCB Clinical Info - CLAIM# - " + wcb.getW_wcbno() + " @ " + echart.getTimeStampToString() + "]\n" + wcb.getW_clinicinfo();
    echart.setEncounter(wcbEchartEntry);
    log.debug("wcbEchartEntry=" + wcbEchartEntry);
    dao.addEchartEntry(echart);
  }

  public String convertDate8Char(String s) {
    String sdate = "00000000", syear = "", smonth = "", sday = "";
    log.debug("s=" + s);
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

        log.debug("Year" + syear + " Month" + smonth + " Day" + sday);
        sdate = syear + smonth + sday;

      }
      else {
        sdate = s;
      }
      log.debug("sdate:" + sdate);
    }
    else {
      sdate = "00000000";

    }
    return sdate;
  }

  private String getLastInsertId(DBHandler db) throws SQLException {
    ResultSet rs = db.GetSQL("SELECT LAST_INSERT_ID()");
    String id = null;
    if (rs.next()) {
      id = rs.getString(1);
    }
    rs.close();
    return id;
  }

  String moneyFormat(String str) {
    String moneyStr = "0.00";
    try {
      moneyStr = new java.math.BigDecimal(str).movePointLeft(2).toString();
    }
    catch (Exception moneyException) {}
    return moneyStr;
  }

  /**
   * Adds a new entry into the billing_history table
   * @param newInvNo String
   */
  private void createBillArchive(String billingMasterNo) {
    BillingHistoryDAO dao = new BillingHistoryDAO();
    dao.createBillingHistoryArchive(billingMasterNo);
  }

}
