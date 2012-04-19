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


package oscar.oscarBilling.pageUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.AppointmentArchiveDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarBilling.ca.bc.data.BillingHistoryDAO;
import oscar.oscarBilling.pageUtil.BillingBillingManager.BillingItem;
import oscar.oscarDB.DBHandler;
import oscar.service.OscarSuperManager;

public class BillingSaveBillingAction extends Action {

	AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
    OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");


  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
      ServletException {

    if (request.getSession().getAttribute("user") == null) {
      return (mapping.findForward("Logout"));
    }

    oscar.oscarBilling.pageUtil.BillingSessionBean bean;
    bean = (oscar.oscarBilling.pageUtil.BillingSessionBean) request.getSession().
        getAttribute("billingSessionBean");
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
    String dataCenterId = OscarProperties.getInstance().getProperty(
        "dataCenterId");

    //change appointment status
    MiscUtils.getLogger().debug("appointment_no: " + bean.getApptNo());
    MiscUtils.getLogger().debug("BillStatus:" + billStatus);
    OscarSuperManager oscarSuperManager = (OscarSuperManager)SpringUtils.getBean("oscarSuperManager");
    Appointment appt = appointmentDao.find(Integer.parseInt(bean.getApptNo()));
    appointmentArchiveDao.archiveAppointment(appt);
    oscarSuperManager.update("appointmentDao", "updatestatusc", new Object[]{billStatus,bean.getCreator(),bean.getApptNo()});

    String sql = "insert into billing (billing_no,demographic_no, provider_no,appointment_no, demographic_name,hin,update_date, billing_date, total, status, dob, visitdate, visittype, provider_ohip_no, apptProvider_no, creator)";
    sql = sql + " values('\\N','" + bean.getPatientNo() + "', '" +
        bean.getBillingProvider() + "', '" + bean.getApptNo() + "','" +
        bean.getPatientName() + "','" + bean.getPatientPHN() + "','" + curDate +
        "','" + bean.getServiceDate() + "','" + bean.getGrandtotal() +
        "','O','" + bean.getPatientDoB() + "','" + bean.getAdmissionDate() +
        "','" + oscar.util.UtilMisc.mysqlEscape(bean.getVisitType()) + "','" +
        bean.getBillingPracNo() + "','" + bean.getApptProviderNo() + "','" +
        bean.getCreator() + "')";
    try {

      DBHandler.RunSQL(sql);
      rs = DBHandler.GetSQL("SELECT LAST_INSERT_ID()");

      if (rs.next()) {
        billingid = rs.getString(1);
      }
      rs.close();

    }
    catch (SQLException e) {
      MiscUtils.getLogger().error("Error", e);
    }

    ArrayList<BillingItem> billItem = bean.getBillItem();
    for (int i = 0; i < billItem.size(); i++) {
      if (bean.getBillingType().compareTo("MSP") == 0) {
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
              + "dx_expansion, service_location, referral_flag1, referral_no1, referral_flag2, referral_no2, time_call, service_start_time, service_end_time, birth_date, office_number, correspondence_code, claim_comment) "
              + "values ('\\N',"
              + "'" + billingid + "',"
              + "CURRENT_TIMESTAMP(),"
              + "'O',"
              + "'" + bean.getPatientNo() + "',"
              + "'" + bean.getApptNo() + "',"
              + "'C02',"
              + "'" + dataCenterId + "',"
              + "'" + bean.getBillingGroupNo() + "',"
              + "'" + bean.getBillingPracNo() + "',"
              + "'" + bean.getPatientPHN() + "',"
              + "'" + bean.getPatientFirstName().substring(0, 1) + " " +
              bean.getPatientLastName().substring(0, 2) + "',"
              + "'" + "00" + "',"
              + "'" +
              (
               billItem.get(i)).getUnit() + "',"
              + "'" + bean.getVisitLocation().substring(0, 2) + "',"
              + "'00',"
              + "'0',"
              + "'00',"
              + "'" +
              (
               billItem.get(i)).getServiceCode() + "',"
              + "'" +
              (
               billItem.get(i)).getDispLineTotal() + "',"
              + "'0',"
              + "'" + convertDate8Char(bean.getServiceDate()) + "',"
              + "'" + "00" + "',"
              + "'" + "0" + "',"
              + "' ',"
              + "'" + bean.getDx1() + "',"
              + "'" + bean.getDx2() + "',"
              + "'" + bean.getDx3() + "',"
              + "' ',"
              + "'" + bean.getVisitType().substring(0, 1) + "',"
              + "'" + bean.getReferType1() + "',"
              + "'" + bean.getReferral1() + "',"
              + "'" + bean.getReferType2() + "',"
              + "'" + bean.getReferral2() + "',"
              + "'0000',"
              + "'" + bean.getStartTime() + "',"
              + "'" + bean.getEndTime() + "',"
              + "'" + convertDate8Char(bean.getPatientDoB()) + "',"
              + "'',"
              + "'0',"
              + "'')";
          try {


            DBHandler.RunSQL(sql);
            rs = DBHandler.GetSQL("SELECT LAST_INSERT_ID()");

            /**
             * @todo Don't forget to verify that this is the correct billingmaster_no
             */
            int billingMasterNo = -1;
            if (rs.next()) {
              billingMasterNo = rs.getInt(1);
            }

            //ensure that this insert action is audited
            this.createBillArchive(billingMasterNo++, "O");
          }
          catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
          }
          MiscUtils.getLogger().debug(sql);
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


          sql = "insert into billingmaster (billingmaster_no, billing_no, createdate, billingstatus,demographic_no, appointment_no, claimcode, datacenter, payee_no, practitioner_no, phn, name_verify, dependent_num,billing_unit,";
          sql = sql + "clarification_code, anatomical_area, after_hour, new_program, billing_code, bill_amount, payment_mode, service_date, service_to_day, submission_code, extended_submission_code, dx_code1, dx_code2, dx_code3, ";
          sql = sql + "dx_expansion, service_location, referral_flag1, referral_no1, referral_flag2, referral_no2, time_call, service_start_time, service_end_time, birth_date, office_number, correspondence_code, claim_comment, ";
          sql = sql + "oin_insurer_code, oin_registration_no, oin_birthdate, oin_first_name, oin_second_name, oin_surname,oin_sex_code, oin_address, oin_address2, oin_address3, oin_address4, oin_postalcode) ";
          sql = sql + "values ('\\N','" + billingid +
              "',CURRENT_TIMESTAMP(), 'O', '" + bean.getPatientNo() + "', '" +
              bean.getApptNo() + "','C02','" + dataCenterId + "', '" +
              bean.getBillingGroupNo() + "','" + bean.getBillingPracNo() +
              "','" + "0000000000" + "','" + "0000" + "','" + "00" + "','" +
              (
               billItem.get(i)).getUnit() + "',";
          sql = sql + "'" + bean.getVisitLocation().substring(0, 2) +
              "','00','0','00','" +
              (
               billItem.get(i)).getServiceCode() + "','" +
              (
               billItem.get(i)).getDispLineTotal() + "','0','" +
              convertDate8Char(bean.getServiceDate()) + "','" + "00" + "','" +
              "0" + "',' ','" + bean.getDx1() + "', '" + bean.getDx2() + "','" +
              bean.getDx3() + "',";
          sql = sql + "' ','" + bean.getVisitType().substring(0, 1) + "','" +
              bean.getReferType1() + "','" + bean.getReferral1() + "','" +
              bean.getReferType2() + "','" + bean.getReferral2() +
              "','0000', '" + bean.getStartTime() + "','" + bean.getEndTime() +
              "','" + "00000000" + "','', '0','',";
          sql = sql + "'" + bean.getPatientHCType() + "', '" +
              bean.getPatientPHN() + "', '" +
              convertDate8Char(bean.getPatientDoB()) + "','" +
              bean.getPatientFirstName() + "', '" + " " + "','" +
              bean.getPatientLastName() + "','" + bean.getPatientSex() + "', '" +
              bean.getPatientAddress1() + "', '" + bean.getPatientAddress2() +
              "', '', '', '" + bean.getPatientPostal() + "')";

          try {

            DBHandler.RunSQL(sql);
            rs = DBHandler.GetSQL("SELECT LAST_INSERT_ID()");
            /**
             * @todo Don't forget to verify that this is the correct billingmaster_no
             */
            int billingMasterNo = -1;
            if (rs.next()) {
              billingMasterNo = rs.getInt(1);
            }

            //ensure that this insert action is audited
            this.createBillArchive(billingMasterNo++, "O");
          }
          catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
          }
          MiscUtils.getLogger().debug(sql);
        }
      }
    }

    return (mapping.findForward("success"));
  }

  public String convertDate8Char(String s) {
    String sdate = "00000000", syear = "", smonth = "", sday = "";
    MiscUtils.getLogger().debug("s=" + s);
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

        MiscUtils.getLogger().debug("Year" + syear + " Month" + smonth + " Day" + sday);
        sdate = syear + smonth + sday;

      }
      else {
        sdate = s;
      }
      MiscUtils.getLogger().debug("sdate:" + sdate);
    }
    else {
      sdate = "00000000";

    }
    return sdate;
  }

  /**
   * Adds a new entry into the billing_history table
   * @param newInvNo String
   */
  private void createBillArchive(int billingMasterNo, String status) {
    BillingHistoryDAO dao = new BillingHistoryDAO();
    dao.createBillingHistoryArchive(String.valueOf(billingMasterNo));
  }

}
