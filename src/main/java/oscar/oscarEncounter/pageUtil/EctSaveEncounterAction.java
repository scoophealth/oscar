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


package oscar.oscarEncounter.pageUtil;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.AppointmentArchiveDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarDB.DBHandler;
import oscar.oscarSurveillance.SurveillanceMaster;
import oscar.service.OscarSuperManager;
import oscar.util.UtilDateUtilities;

public class EctSaveEncounterAction extends Action {
    static Logger log=MiscUtils.getLogger();
    AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
    OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");


  private String getLatestID(String demoNo) throws
    SQLException  {

      String sql = "select MAX(eChartId) as maxID from eChart where demographicNo = " + demoNo;
      ResultSet rs = DBHandler.GetSQL(sql);
      String latestID = null;

      if (rs.next()) {
          latestID = oscar.Misc.getString(rs, "maxID");
      }
      rs.close();

      return latestID;
  }


  //This function will compare the most current id in the echart with the
  // id that is stored in the session variable.  If the ID in the echart is
  // newer, then the user is working with a old (aka dirty) copy of the encounter
  private boolean isDirtyEncounter(String demographicNo, String userEChartID)  {
      String latestID;
      try  {
        latestID = getLatestID(demographicNo);
      }
      catch (SQLException sqlexception) {
        MiscUtils.getLogger().debug(sqlexception.getMessage());
        return true;
      }

      //latestID should only be null if the assessed encounter is new, which
      // means that it can't be dirty
      if ( latestID == null || latestID.equals("") )  {
          return false;
      }
      // if the usrCopyID is null and the latestID isn't null, then
      // two people where probably trying to create a new encounter for
      // the same person at the same time.
      else if (userEChartID == null || userEChartID.equals(""))  {
          return true;
      }

      try  {
          Integer iLatestID = new Integer(latestID);
          Integer iUsrCopyID = new Integer(userEChartID);
          if ( iLatestID.longValue() > iUsrCopyID.longValue())  {
              return true;
          }
          else  {
              return false;
          }
      }
      catch  (NumberFormatException e) {
          // already handled the null/empy string case, so shouldn't ever get this
          // exception.
          MiscUtils.getLogger().error("Error", e);
          return true;
      }
 }

  public ActionForward execute(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest httpservletrequest,
                               HttpServletResponse httpservletresponse) throws
      IOException, ServletException {
      log.debug("EctSaveEncounterAction Start");
    //UtilDateUtilities dateutilities = new UtilDateUtilities();
    EctSessionBean sessionbean = null;
    sessionbean = (EctSessionBean) httpservletrequest.getSession().getAttribute(
        "EctSessionBean");

    sessionbean.socialHistory = httpservletrequest.getParameter("shTextarea");
    sessionbean.familyHistory = httpservletrequest.getParameter("fhTextarea");
    sessionbean.medicalHistory = httpservletrequest.getParameter("mhTextarea");
    sessionbean.ongoingConcerns = httpservletrequest.getParameter("ocTextarea");
    sessionbean.reminders = httpservletrequest.getParameter("reTextarea");
    sessionbean.encounter = httpservletrequest.getParameter("enTextarea");
    sessionbean.subject = httpservletrequest.getParameter("subject");
    java.util.Date date = UtilDateUtilities.Today();
    sessionbean.eChartTimeStamp = date;

    if (!httpservletrequest.getParameter("btnPressed").equals("Exit")) {
      try {
        ResourceBundle prop = ResourceBundle.getBundle("oscarResources",
            httpservletrequest.getLocale());
        if (httpservletrequest.getParameter("btnPressed").equals(
            "Sign,Save and Bill")) {
          sessionbean.encounter = sessionbean.encounter + "\n" + "[" +
              prop.
              getString("oscarEncounter.class.EctSaveEncounterAction.msgSigned") +
              " " +
              UtilDateUtilities.DateToString(date,
                                             prop.getString("date.yyyyMMddHHmmss"),
                                             httpservletrequest.getLocale()) +
              " " +
              prop.getString(
              "oscarEncounter.class.EctSaveEncounterAction.msgSigBy") +
              " " + sessionbean.userName + "]";
        }
        if (httpservletrequest.getParameter("btnPressed").equals(
            "Sign,Save and Exit")) {
          sessionbean.encounter = sessionbean.encounter + "\n" + "[" +
              prop.
              getString("oscarEncounter.class.EctSaveEncounterAction.msgSigned") +
              " " +
              UtilDateUtilities.DateToString(date,
                                             prop.getString("date.yyyyMMddHHmmss"),
                                             httpservletrequest.getLocale()) +
              " " +
              prop.getString(
              "oscarEncounter.class.EctSaveEncounterAction.msgSigBy") +
              " " + sessionbean.userName + "]";
        }
        if (httpservletrequest.getParameter("btnPressed").equals(
            "Verify and Sign")) {
          sessionbean.encounter = sessionbean.encounter + "\n" + "[" +
              prop.
              getString(
              "oscarEncounter.class.EctSaveEncounterAction.msgVerAndSig") +
              " " +
              UtilDateUtilities.DateToString(date,
                                             prop.getString("date.yyyyMMddHHmmss"),
                                             httpservletrequest.getLocale()) +
              " " +
              prop.getString(
              "oscarEncounter.class.EctSaveEncounterAction.msgSigBy") +
              " " + sessionbean.userName + "]";
        }
        if (httpservletrequest.getParameter("btnPressed").equals("Split Chart")) {
          sessionbean.subject = prop.getString(
              "oscarEncounter.class.EctSaveEncounterAction.msgSplitChart");
        }
        sessionbean.template = "";
      }
      catch (Exception e) {
        MiscUtils.getLogger().error("Error", e);
      }

      //This code is synchronized to ensure that only one person is modifying the same patient
      // record at a time
      synchronized (this)  {
          //unfortunately, can't use the echart ID stored in the session bean, because it may
          // be overwritten when view split charts.
          String userEChartID = (String) httpservletrequest.getSession().getAttribute("eChartID");
          //make sure that user is trying to save the latest version of the encounter
          if ( isDirtyEncounter(sessionbean.demographicNo, userEChartID) )
          {
              //If it is an ajax submit, it should cause and exception, if it is a
              // regular submission, it should just forward on to an error page.
              if (httpservletrequest.getParameter("submitMethod") != null && httpservletrequest.getParameter("submitMethod").equals("ajax"))
              {
                  httpservletresponse.sendError(HttpServletResponse.SC_PRECONDITION_FAILED,
                    "Somebody else is currently modifying this encounter");
              }
              return actionmapping.findForward("concurrencyError");
          }

          try {
            String s = "insert into eChart (timeStamp, demographicNo,providerNo,subject,socialHistory,familyHistory,medicalHistory,ongoingConcerns,reminders,encounter) values (?,?,?,?,?,?,?,?,?,?)" ;
            PreparedStatement pstmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(s);
                pstmt.setTimestamp(1,new java.sql.Timestamp(date.getTime()));
                pstmt.setString(2,sessionbean.demographicNo);
                pstmt.setString(3,sessionbean.providerNo);
                pstmt.setString(4,sessionbean.subject);
                pstmt.setString(5,sessionbean.socialHistory);
                pstmt.setString(6,sessionbean.familyHistory);
                pstmt.setString(7,sessionbean.medicalHistory);
                pstmt.setString(8,sessionbean.ongoingConcerns);
                pstmt.setString(9,sessionbean.reminders);
                pstmt.setString(10,sessionbean.encounter);
                pstmt.executeUpdate();
                pstmt.close();
            sessionbean.eChartId = getLatestID(sessionbean.demographicNo);
            httpservletrequest.getSession().setAttribute("eChartID",sessionbean.eChartId);

            // add log here
            String ip = httpservletrequest.getRemoteAddr();
            LogAction.addLog( (String) httpservletrequest.getSession().getAttribute(
                "user"), LogConst.ADD, LogConst.CON_ECHART,
                             sessionbean.demographicNo, ip);

            //change the appt status
            if (sessionbean.status != null && !sessionbean.status.equals("")) {
              oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
              as.setApptStatus(sessionbean.status);
              OscarSuperManager oscarSuperManager = (OscarSuperManager)SpringUtils.getBean("oscarSuperManager");

              if (httpservletrequest.getParameter("btnPressed").equals(
                  "Sign,Save and Exit")) {
            	  Appointment appt = appointmentDao.find(Integer.parseInt(sessionbean.appointmentNo));
            	  appointmentArchiveDao.archiveAppointment(appt);
                  oscarSuperManager.update("appointmentDao", "updatestatusc", new Object[]{as.signStatus(),sessionbean.providerNo,sessionbean.appointmentNo});
              }
              if (httpservletrequest.getParameter("btnPressed").equals(
                  "Verify and Sign")) {
            	  Appointment appt = appointmentDao.find(Integer.parseInt(sessionbean.appointmentNo));
            	  appointmentArchiveDao.archiveAppointment(appt);
            	  oscarSuperManager.update("appointmentDao", "updatestatusc", new Object[]{as.verifyStatus(),sessionbean.providerNo,sessionbean.appointmentNo});
              }
            }
          }
          catch (SQLException sqlexception) {
            MiscUtils.getLogger().debug(sqlexception.getMessage());
          }
      }  //end of the synchronization block
    }

    try { // save enc. window sizes

      String s = "delete from encounterWindow where provider_no='" +
          sessionbean.providerNo + "'";
      DBHandler.RunSQL(s);
      s = "insert into encounterWindow (provider_no, rowOneSize, rowTwoSize, presBoxSize, rowThreeSize) values ('" +
          sessionbean.providerNo + "', '" +
          httpservletrequest.getParameter("rowOneSize") + "', '" +
          httpservletrequest.getParameter("rowTwoSize") + "', '" +
          httpservletrequest.getParameter("presBoxSize") + "', '" +
          httpservletrequest.getParameter("rowThreeSize") + "')";
      DBHandler.RunSQL(s);
    }
    catch (Exception e) {
     MiscUtils.getLogger().error("Error", e);
    }

    String forward = null;

    //billRegion=BC&billForm=GP&hotclick=&appointment_no=0&demographic_name=TEST%2CBILLING&demographic_no=10419&providerview=1&user_no=999998&apptProvider_no=none&appointment_date=2006-3-30&start_time=0:00&bNewForm=1&status=t')
    if (httpservletrequest.getParameter("btnPressed").equals(
        "Sign,Save and Bill")) {

      String billRegion = OscarProperties.getInstance().getProperty(
          "billregion");
      //
      oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean bean = new oscar.
          oscarBilling.ca.bc.pageUtil.BillingSessionBean();
      bean.setApptProviderNo(sessionbean.providerNo);
      bean.setPatientName(sessionbean.getPatientFirstName() + " " +
                          sessionbean.getPatientLastName());
      bean.setBillRegion(billRegion);
      Properties oscarVariables  = (Properties) httpservletrequest.getAttribute("oscarVariables");
      String formBill = "GP";

      if (oscarVariables != null ){
          formBill = oscarVariables.getProperty("default_view",formBill);
      }
      bean.setBillForm(formBill);
      bean.setPatientNo(sessionbean.demographicNo);
      bean.setApptNo(httpservletrequest.getParameter("appointment_no"));
      bean.setApptDate(sessionbean.appointmentDate);
      bean.setApptStatus(httpservletrequest.getParameter("status"));
      httpservletrequest.setAttribute("encounter", "true");
      httpservletrequest.getSession().setAttribute("billingSessionBean",bean);
      forward = "bill";
    }

    else if (httpservletrequest.getParameter("btnPressed").equals("Sign,Save and Exit")){
      forward = "success";
      SurveillanceMaster sMaster = SurveillanceMaster.getInstance();
      if(!SurveillanceMaster.surveysEmpty()){
         httpservletrequest.setAttribute("demoNo",sessionbean.demographicNo);
         log.debug("sending to surveillance");
         forward = "surveillance";
      }
    }
    else if(httpservletrequest.getParameter("btnPressed").equals("Verify and Sign")) {
      forward = "success";
    }
    else if (httpservletrequest.getParameter("btnPressed").equals("Save") || httpservletrequest.getParameter("btnPressed").equals("AutoSave")) {
      forward = "saveAndStay";
    }
    else if (httpservletrequest.getParameter("btnPressed").equals("Split Chart")) {
      forward = "splitchart";
    }
    else if (httpservletrequest.getParameter("btnPressed").equals("Exit")) {
      forward = "close";
    }
    else {
      forward = "failure";
    }
    return actionmapping.findForward(forward);
  }
}
