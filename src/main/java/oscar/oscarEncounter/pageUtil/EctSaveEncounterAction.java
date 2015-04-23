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
import java.util.Date;
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
import org.oscarehr.common.dao.EChartDao;
import org.oscarehr.common.dao.EncounterWindowDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.EChart;
import org.oscarehr.common.model.EncounterWindow;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarSurveillance.SurveillanceMaster;
import oscar.util.ConversionUtils;
import oscar.util.UtilDateUtilities;

public class EctSaveEncounterAction extends Action {
    static Logger log=MiscUtils.getLogger();
    AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
    OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	

	private String getLatestID(String demoNo) {
		EChartDao dao = SpringUtils.getBean(EChartDao.class);
		Integer maxId = dao.getMaxIdForDemographic(ConversionUtils.fromIntString(demoNo));

		if (maxId != null) {
			return maxId.toString();
		}
		return null;
	}


  //This function will compare the most current id in the echart with the
  // id that is stored in the session variable.  If the ID in the echart is
  // newer, then the user is working with a old (aka dirty) copy of the encounter
  private boolean isDirtyEncounter(String demographicNo, String userEChartID)  {
      String latestID = getLatestID(demographicNo);

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
	  
	  if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(httpservletrequest), "_demographic", "w", null)) {
			throw new SecurityException("missing required security object (_demographic)");
	  }
	  
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
    java.util.Date date = new Date();
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

          
        	  EChart e = new EChart();
        	  e.setTimestamp(date);
        	  e.setDemographicNo(Integer.parseInt(sessionbean.demographicNo));
        	  e.setProviderNo(sessionbean.providerNo);
        	  e.setSubject(sessionbean.subject);
        	  e.setSocialHistory(sessionbean.socialHistory);
        	  e.setFamilyHistory(sessionbean.familyHistory);
        	  e.setMedicalHistory(sessionbean.medicalHistory);
        	  e.setOngoingConcerns(sessionbean.ongoingConcerns);
        	  e.setReminders(sessionbean.reminders);
        	  e.setEncounter(sessionbean.encounter);
        	  
        	  EChartDao dao = SpringUtils.getBean(EChartDao.class);
        	  dao.persist(e);
            sessionbean.eChartId =String.valueOf(e.getId());
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
              
              if (httpservletrequest.getParameter("btnPressed").equals(
                  "Sign,Save and Exit")) {
            	  Appointment appt = appointmentDao.find(Integer.parseInt(sessionbean.appointmentNo));
            	  appointmentArchiveDao.archiveAppointment(appt);
                  if(appt != null) {
                  	appt.setStatus(as.signStatus());
                  	appt.setLastUpdateUser(sessionbean.providerNo);
                  	appointmentDao.merge(appt);
                  }
              }
              if (httpservletrequest.getParameter("btnPressed").equals(
                  "Verify and Sign")) {
            	  Appointment appt = appointmentDao.find(Integer.parseInt(sessionbean.appointmentNo));
            	  appointmentArchiveDao.archiveAppointment(appt);
            	  if(appt != null) {
                  	appt.setStatus(as.verifyStatus());
                  	appt.setLastUpdateUser(sessionbean.providerNo);
                  	appointmentDao.merge(appt);
                  }
              }
            }
          }
          
      }  //end of the synchronization block
    

    EncounterWindowDao encounterWindowDao  = SpringUtils.getBean(EncounterWindowDao.class);
    encounterWindowDao.remove(sessionbean.providerNo);
     
      
      EncounterWindow ew = new EncounterWindow();
      ew.setProviderNo(sessionbean.providerNo);
      ew.setRowOneSize(Integer.parseInt(httpservletrequest.getParameter("rowOneSize")));
      ew.setRowTwoSize(Integer.parseInt(httpservletrequest.getParameter("rowTwoSize")));
      ew.setRowThreeSize(Integer.parseInt(httpservletrequest.getParameter("rowThreeSize")));
      ew.setPresBoxSize(Integer.parseInt(httpservletrequest.getParameter("presBoxSize")));
      encounterWindowDao.persist(ew);
   
    String forward = null;

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
