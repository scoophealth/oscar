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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.oscarBilling.ca.bc.pageUtil;
import oscar.oscarDB.DBHandler;
import java.io.*;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.text.*;
import java.lang.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import oscar.oscarDB.DBHandler;
import oscar.oscarBilling.ca.bc.pageUtil.*;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;
import oscar.*;
import oscar.oscarBilling.ca.bc.MSP.*;
import oscar.oscarBilling.ca.bc.data.*;


public class BillingSaveBillingAction extends Action {
    
    
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
        
        if(request.getSession().getAttribute("user") == null  ){
            return (mapping.findForward("Logout"));
        }
        
        BillingSaveBillingForm frm = (BillingSaveBillingForm) form;
        
        oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean bean;
        bean = (oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean)request.getSession().getAttribute("billingSessionBean");
        //  oscar.oscarBilling.data.BillingStoreData bsd = new oscar.oscarBilling.data.BillingStoreDate();
        //  bsd.storeBilling(bean);
        oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
        String billStatus = as.billStatus(bean.getApptStatus());
        
        java.sql.ResultSet rs;
        GregorianCalendar now=new GregorianCalendar();
        int curYear = now.get(Calendar.YEAR);
        int curMonth = (now.get(Calendar.MONTH)+1);
        int curDay = now.get(Calendar.DAY_OF_MONTH);
        String curDate = String.valueOf(curYear) + "-" + String.valueOf(curMonth) + "-" + String.valueOf(curDay);
        String billingid = "";
        String dataCenterId = OscarProperties.getInstance().getProperty("dataCenterId");
        String billingMasterId = "";
        
        System.out.println("appointment_no: "+ bean.getApptNo());
        System.out.println("BillStatus:" + billStatus);
        String sql = "update appointment set status='" + billStatus + "' where appointment_no='" + bean.getApptNo() + "'";
        
        try {                        
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            db.RunSQL(sql);
            db.CloseConn();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("LLLOOK: APPT ERROR FOR demo:"+bean.getPatientName()+" date "+curDate);
            e.printStackTrace();
        }
        
        char billingAccountStatus = 'O';
        if (bean.getBillingType().equals("DONOTBILL")){
            bean.setBillingType("MSP");  //RESET this to MSP to get processed
            billingAccountStatus = 'N';           
        }else if  (bean.getBillingType().equals("WCB")){            
            billingAccountStatus = 'O';           
        }
        
        //TODO STILL NEED TO ADD EXTRA FIELDS for dotes and bill type
        String billingSQL = "insert into billing (billing_no,demographic_no, provider_no,appointment_no, demographic_name,hin,update_date, billing_date, total, status, dob, visitdate, visittype, provider_ohip_no, apptProvider_no, creator,billingtype)"
        + " values('\\N'," +
        "'" + bean.getPatientNo() + "'," +
        "'" + bean.getBillingProvider() + "', " +
        "'" + bean.getApptNo() + "'," +
        "'" + bean.getPatientName() + "'," +
        "'" + bean.getPatientPHN() + "'," +
        "'" + curDate + "'," +
        "'" + bean.getServiceDate() + "'," +
        "'" + bean.getGrandtotal() + "'," +
        "'" + billingAccountStatus + "'," +   //status
        "'" + bean.getPatientDoB() + "'," +
        "'" + bean.getAdmissionDate() + "'," +
        "'" + oscar.util.UtilMisc.mysqlEscape(bean.getVisitType()) + "'," +
        "'" + bean.getBillingPracNo() + "'," +
        "'" + bean.getApptProviderNo() + "'," +
        "'" + bean.getCreator() + "'," +
        "'"+bean.getBillingType()+"')";

        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            db.RunSQL(billingSQL);
            rs = db.GetSQL("SELECT LAST_INSERT_ID()");
            
            if (rs.next()){
                billingid = rs.getString(1);
            }
            rs.close();
            db.CloseConn();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
        
        
        ArrayList billItem = bean.getBillItem();
        char paymentMode = (bean.getEncounter().equals("E") && !bean.getBillingType().equals("ICBC") && !bean.getBillingType().equals("WCB")) ? 'E' : '0';
        
        String billedAmount;
        if (bean.getBillingType().equals("MSP") || bean.getBillingType().equals("ICBC") || bean.getBillingType().equals("PRIV")) {
        
           for (int i=0; i < billItem.size(); i++){               
                if (paymentMode == 'E') {
                    billedAmount = "0000000";
                }else{
                    billedAmount = ((BillingBillingManager.BillingItem) billItem.get(i)).getDispPrice();                   
                }
                
                if (bean.getPatientHCType().trim().compareTo(bean.getBillRegion().trim()) == 0){


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
                           + "dx_expansion, service_location, referral_flag1, referral_no1, referral_flag2, referral_no2, time_call, service_start_time, service_end_time, birth_date, office_number, correspondence_code, claim_comment,mva_claim_code, icbc_claim_no,facility_no,facility_sub_no) "
                           + "values ('\\N',"
                           +"'"+ billingid+"',"
                           +"NOW(),"  //CURRENT_TIMESTAMP
                           +"'" + billingAccountStatus + "'," //status
                           +"'" + bean.getPatientNo() + "',"
                           +"'" + bean.getApptNo() + "',"
                           +"'C02',"
                           +"'"+dataCenterId+"',"
                           +"'" + bean.getBillingGroupNo() + "',"
                           +"'" + bean.getBillingPracNo() + "',"
                           +"'" + bean.getPatientPHN() + "',"
                           +"'" + bean.getPatientFirstName().substring(0,1) + " " + bean.getPatientLastName().substring(0,2) + "',"
                           +"'" + bean.getDependent() + "',"
                           +"'" + ((oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem)billItem.get(i)).getUnit() +"'," 
                           +"'" + bean.getVisitLocation().substring(0,2) + "',"
                           +"'00',"  //anatomical_area
                           +"'"+bean.getAfterHours()+"',"   //after_hours
                           +"'00',"  //new_program
                           +"'" + ((oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem)billItem.get(i)).getServiceCode()  + "',"
                           +"'" + ((oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem)billItem.get(i)).getDispLineTotal() + "',"
                           +"'" + paymentMode + "'," //payment_mode
                           +"'" +   convertDate8Char(bean.getServiceDate()) + "',"
                           +"'" + bean.getService_to_date() + "'," //service_to_day
                           +"'" + bean.getSubmissionCode() + "'," //submission_code
                           +"' ',"  //extended_submission_code
                           +"'" + bean.getDx1() + "',"
                           +"'" + bean.getDx2() + "',"
                           +"'" + bean.getDx3() + "',"
                           +"' '," //dx_expansion
                           +"'" + bean.getVisitType().substring(0,1) + "',"
                           +"'" + bean.getReferType1() + "',"
                           +"'" + bean.getReferral1()  + "',"
                           +"'" + bean.getReferType2() + "',"
                           +"'" + bean.getReferral2()  + "',"
                           +"'" + bean.getTimeCall()   +"',"    //time_call
                           +"'" + bean.getStartTime()  + "',"
                           +"'" + bean.getEndTime()    + "',"
                           +"'" + convertDate8Char(bean.getPatientDoB()) + "',"
                           +"'',"   //office number
                           +"'" + bean.getCorrespondenceCode()+"',"  //correspondence code
                           +"'" + bean.getShortClaimNote()    +"',"   //claim short comment
                           +"'" + bean.getMva_claim_code()    +"',"
                           +"'" + bean.getIcbc_claim_no()     +"',"
                           +"'" + bean.getFacilityNum()       +"',"
                           +"'" + bean.getFacilitySubNum()    +"'"
                           +")";
                       try {
                           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                           db.RunSQL(sql);
                           billingMasterId = getInsertId(db);
                           db.CloseConn();
                       } catch (SQLException e) {
                           System.out.println(e.getMessage());
                       }
                       System.out.println(sql);
                   }else{

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
                       "'"+ billingid+"'," +
                       "NOW()," +  //NOW
                       "'" + billingAccountStatus + "'," +
                       "'" + bean.getPatientNo() + "'," +
                       "'" + bean.getApptNo() + "'," +
                       "'C02'," +
                       "'"  +dataCenterId+"'," +
                       "'" + bean.getBillingGroupNo() + "'," +
                       "'"  + bean.getBillingPracNo() + "'," +
                       "'"  + "0000000000" + "'," + //phn
                       "'"  + "0000"+ "'," + //name_verify
                       "'"  + "00" + "'," + //dependent_num
                       "'"  + ((oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem)billItem.get(i)).getUnit() +"'," +
                       "'"  + bean.getVisitLocation().substring(0,2) + "'," +
                       "'00'," + //anatomical_area
                       "'"  + bean.getAfterHours() + "'," + //after_hour
                       "'00'," + //new_program
                       "'"  + ((oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem)billItem.get(i)).getServiceCode()  + "'," +
                       "'"  + ((oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem)billItem.get(i)).getDispLineTotal() + "'," +
                       "'"  + paymentMode + "'," + //payment_mode
                       "'"  +   convertDate8Char(bean.getServiceDate()) + "'," +
                       "'"  + bean.getService_to_date() + "'," + //service_to_date
                       "'"  + bean.getSubmissionCode() + "'," + //submission_code
                       "' '," + //extendednt_-submission_code
                       "'"  + bean.getDx1() + "'," +
                       "'"  + bean.getDx2() + "'," +
                       "'"  + bean.getDx3() + "'," + 
                       "' '," + //dx_expansion
                       "'"  + bean.getVisitType().substring(0,1) + "'," +
                       "'"  + bean.getReferType1() + "'," +
                       "'"  + bean.getReferral1()  + "'," +
                       "'"  + bean.getReferType2() + "'," +
                       "'"  + bean.getReferral2()  + "'," +
                       "'"  + bean.getTimeCall()   + "'," + //time_call
                       "'"  + bean.getStartTime() + "'," +
                       "'"  + bean.getEndTime()    + "'," +
                       "'"  + "00000000"           + "'," +  //birth_date
                       "'',"+   //office_number
                       " '" + bean.getCorrespondenceCode()+ "'," +  //correspondence code
                       "'"  + bean.getShortClaimNote()    + "'," +  //claim_comment
                       "'"  + bean.getPatientHCType()     + "'," +
                       "'"  + bean.getPatientPHN()        + "'," +
                       "'"  + convertDate8Char(bean.getPatientDoB()) + "'," +
                       "'"  + bean.getPatientFirstName()  + "'," +
                       "'"  + " " + "'," + //oin_second_name
                       "'"  + bean.getPatientLastName()   + "'," +
                       "'"  + bean.getPatientSex()        + "'," +
                       "'"  + bean.getPatientAddress1()   + "'," +
                       "'"  + bean.getPatientAddress2()   + "'," +
                       "''," + //oin_address3
                       "''," + //oin_address3
                       "'"  + bean.getPatientPostal()     +"',"  +
                       "'"  + bean.getMva_claim_code()    + "'," +
                       "'"  + bean.getIcbc_claim_no()     + "'," +
                       "'"  + bean.getFacilityNum()       + "'," +
                       "'"  + bean.getFacilitySubNum()    + "'"  +
                       ")";   

                       try {                                                
                           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                           db.RunSQL(sql);
                           billingMasterId = getInsertId(db);
                           db.CloseConn(); 
                       } catch (SQLException e) {
                           System.out.println(e.getMessage());
                       }
                       System.out.println(sql);                    
                   }                                
               
            }
           if (bean.getCorrespondenceCode().equals("N") || bean.getCorrespondenceCode().equals("B")){            
              try{
              MSPBillingNote n = new MSPBillingNote();
              n.addNote(billingMasterId,bean.getCreator(),bean.getNotes());
              } catch (SQLException e) {
                 System.out.println(e.getMessage());
              }
           }
           if (bean.getMessageNotes() != null || !bean.getMessageNotes().trim().equals("")){
              try{
              BillingNote n = new BillingNote();
              n.addNote(billingMasterId,bean.getCreator(),bean.getMessageNotes());
              } catch (SQLException e) {
                 System.out.println(e.getMessage());
              }
              
           }
           
        }
        //////////////
        if (null != request.getSession().getAttribute("WCBForm")) {
           WCBForm wcb = (WCBForm) request.getSession().getAttribute("WCBForm");
           String insertBillingMaster = 
            " INSERT INTO billingmaster (billing_no, createdate, payee_no, billingstatus, demographic_no, appointment_no,service_date) " +
            " VALUES ('"+billingid+"',NOW(),'"+wcb.getW_payeeno()+"','"+billingAccountStatus+"','"+bean.getPatientNo()+"','"+bean.getApptNo()+ "','"+convertDate8Char(bean.getServiceDate())+"')"; 
           
            wcb.setW_demographic(bean.getPatientNo());
            wcb.setW_providerno(bean.getBillingProvider());
            String billamt = "";

            try {
                String amnt = "0.00";
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                db.RunSQL(insertBillingMaster);
                rs = db.GetSQL("SELECT value FROM billingservice WHERE service_code='"+wcb.getW_feeitem()+"'");
                System.out.println("SELECT value FROM billingservice WHERE service_code='"+wcb.getW_feeitem()+"'");
                if (rs.next()) {
                    amnt = rs.getString("value");
                }
                //rs = db.GetSQL("SELECT value FROM billingservice WHERE service_code='"+ wcb.getW_extrafeeitem()+"'");
                //if (rs.next()) {
                //    amnt += rs.getDouble("value");
                //}
                
                //billamt = moneyFormat(String.valueOf(amnt));
                System.out.println("billamt"+amnt);
                db.RunSQL(wcb.SQL(billingid, amnt));
                                             
                
                if ( wcb.getW_extrafeeitem() != null && wcb.getW_extrafeeitem().trim().length() != 0 ){
                   System.out.println("Adding Second billing item");
                   String secondWCBBillingId = null;
                   String secondBillingAmt = "0.00" ;
                   db.RunSQL(billingSQL);
                   rs = db.GetSQL("SELECT LAST_INSERT_ID()");            
                   if (rs.next()){
                      secondWCBBillingId = rs.getString(1);
                   }
                   rs.close();
                   
                   String secondBillingMaster = " INSERT INTO billingmaster (billing_no, createdate, payee_no, billingstatus, demographic_no, appointment_no,service_date) " +
                    " VALUES ('"+secondWCBBillingId+"',NOW(),'"+wcb.getW_payeeno()+"','"+billingAccountStatus+"','"+bean.getPatientNo()+"','"+bean.getApptNo()+ "','"+convertDate8Char(bean.getServiceDate())+"')";
                   
                   db.RunSQL(secondBillingMaster);
                   rs = db.GetSQL("SELECT value FROM billingservice WHERE service_code='"+ wcb.getW_extrafeeitem()+"'");
                   if (rs.next()) {
                      secondBillingAmt = rs.getString("value");
                   }
                   db.RunSQL(wcb.secondSQLItem(secondWCBBillingId, secondBillingAmt));
        
                   
                }
                
                db.CloseConn();
            }
            catch (SQLException e) {
                System.err.println(e.getMessage());
            }            
            request.getSession().putValue("WCBForm", null);
        }

        ////////////////////
        //      System.out.println("Service count : "+ billItem.size());
        ActionForward af = mapping.findForward("success");
        if (frm.getSubmit().equals("Another Bill")){
           af = mapping.findForward("anotherBill");
        } else if ( frm.getSubmit().equals("Save & Print Receipt")){
           af = new ActionForward("/billing/CA/BC/billingView.do?billing_no="+billingid+"&receipt=yes");           
        }
        return af;//(mapping.findForward("success"));
    }
    
    public String convertDate8Char(String s){
        String sdate = "00000000", syear="", smonth="", sday="";
        System.out.println("s=" + s);
        if (s != null){
            
            if (s.indexOf("-") != -1){
                
                syear = s.substring(0, s.indexOf("-"));
                s = s.substring(s.indexOf("-")+1);
                smonth = s.substring(0, s.indexOf("-"));
                if (smonth.length() == 1)  {
                    smonth = "0" + smonth;
                }
                s = s.substring(s.indexOf("-")+1);
                sday = s;
                if (sday.length() == 1)  {
                    sday = "0" + sday;
                }
                
                
                System.out.println("Year" + syear + " Month" + smonth + " Day" + sday);
                sdate = syear + smonth + sday;
                
            }else{
                sdate = s;
            }
            System.out.println("sdate:" + sdate);
        }else{
            sdate="00000000";
            
        }
        return sdate;
    }
    
    private String getInsertId(DBHandler db) throws SQLException{       
       ResultSet rs = db.GetSQL("SELECT LAST_INSERT_ID()");            
       String id = null;
       if (rs.next()){
          id = rs.getString(1);
       }
       rs.close();              
       return id;
    }
    
    String moneyFormat(String str){       
        String moneyStr = "0.00";
        try{             
            moneyStr = new java.math.BigDecimal(str).movePointLeft(2).toString();
        }catch (Exception moneyException) {}
    return moneyStr;
    }
}

