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
package oscar.oscarEncounter.pageUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;
import oscar.util.UtilMisc;
import oscar.OscarProperties;

public class EctSaveEncounterAction
    extends Action {

  public ActionForward execute(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest httpservletrequest,
                               HttpServletResponse httpservletresponse) throws
      IOException, ServletException {

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
        e.printStackTrace();
      }
      try {
        DBHandler dbhandler = new DBHandler(DBHandler.OSCAR_DATA);
        String s = "insert into eChart (timeStamp, demographicNo,providerNo,subject,socialHistory,familyHistory,medicalHistory,ongoingConcerns,reminders,encounter) values ('" +
            UtilDateUtilities.DateToString(date, "yyyy-MM-dd HH:mm:ss") + "'," +
            sessionbean.demographicNo + ",'" + sessionbean.providerNo + "','" +
            UtilMisc.charEscape(sessionbean.subject, '\'') + "','" +
            UtilMisc.charEscape(sessionbean.socialHistory, '\'') + "','" +
            UtilMisc.charEscape(sessionbean.familyHistory, '\'') + "','" +
            UtilMisc.charEscape(sessionbean.medicalHistory, '\'') + "','" +
            UtilMisc.charEscape(sessionbean.ongoingConcerns, '\'') + "','" +
            UtilMisc.charEscape(sessionbean.reminders, '\'') + "','" +
            UtilMisc.charEscape(sessionbean.encounter, '\'') + "')";
        dbhandler.RunSQL(s);
        // add log here
        String ip = httpservletrequest.getRemoteAddr();
        LogAction.addLog( (String) httpservletrequest.getSession().getAttribute(
            "user"), LogConst.ADD, LogConst.CON_ECHART,
                         sessionbean.demographicNo, ip);

        //change the appt status
        if (sessionbean.status != null && !sessionbean.status.equals("")) {
          oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
          as.setApptStatus(sessionbean.status);
          if (httpservletrequest.getParameter("btnPressed").equals(
              "Sign,Save and Exit")) {
            s = "update appointment set status='" + as.signStatus() +
                "' where appointment_no=" + sessionbean.appointmentNo;
            dbhandler.RunSQL(s);
          }
          if (httpservletrequest.getParameter("btnPressed").equals(
              "Verify and Sign")) {
            s = "update appointment set status='" + as.verifyStatus() +
                "' where appointment_no=" + sessionbean.appointmentNo;
            dbhandler.RunSQL(s);
          }
        }
        dbhandler.CloseConn();
      }
      catch (SQLException sqlexception) {
        System.out.println(sqlexception.getMessage());
      }
    }

    try { // save enc. window sizes
      DBHandler dbhandler = new DBHandler(DBHandler.OSCAR_DATA);
      String s = "delete from encounterWindow where provider_no='" +
          sessionbean.providerNo + "'";
      dbhandler.RunSQL(s);
      s = "insert into encounterWindow (provider_no, rowOneSize, rowTwoSize, presBoxSize, rowThreeSize) values ('" +
          sessionbean.providerNo + "', '" +
          httpservletrequest.getParameter("rowOneSize") + "', '" +
          httpservletrequest.getParameter("rowTwoSize") + "', '" +
          httpservletrequest.getParameter("presBoxSize") + "', '" +
          httpservletrequest.getParameter("rowThreeSize") + "')";
      dbhandler.RunSQL(s);
      dbhandler.CloseConn();
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }

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
      bean.setBillForm("GP");
      bean.setPatientNo(sessionbean.demographicNo);
      bean.setApptNo(httpservletrequest.getParameter("appointment_no"));
      bean.setApptDate(sessionbean.appointmentDate);
      httpservletrequest.setAttribute("encounter", "true");
      httpservletrequest.getSession().setAttribute("billingSessionBean",bean);
      return actionmapping.findForward("bill");
    }
    if (httpservletrequest.getParameter("btnPressed").equals(
        "Sign,Save and Exit")
        ||
        httpservletrequest.getParameter("btnPressed").equals("Verify and Sign")) {
      return actionmapping.findForward("success");
    }
    if (httpservletrequest.getParameter("btnPressed").equals("Save")) {
      return actionmapping.findForward("saveAndStay");
    }
    if (httpservletrequest.getParameter("btnPressed").equals("Split Chart")) {
      return actionmapping.findForward("splitchart");
    }
    if (httpservletrequest.getParameter("btnPressed").equals("Exit")) {
      return actionmapping.findForward("close");
    }
    else {
      return actionmapping.findForward("failure");
    }
  }
}
