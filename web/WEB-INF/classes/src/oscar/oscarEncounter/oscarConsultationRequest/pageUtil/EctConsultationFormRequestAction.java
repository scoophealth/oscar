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
package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgStringQuote;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.OscarProperties;
import oscar.util.ParameterActionForward;

public class EctConsultationFormRequestAction extends Action {
   
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException {
      //EctSessionBean bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
      //if(bean == null)
      //    return mapping.findForward("eject");
      EctConsultationFormRequestForm frm = (EctConsultationFormRequestForm)form;
      String referalDate = frm.getReferalDate();
      String serviceId = frm.getService();
      String specId = frm.getSpecialist();
      String appointmentDate = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(frm.getAppointmentYear())))).append("/").append(frm.getAppointmentMonth()).append("/").append(frm.getAppointmentDay())));
      String appointmentHour = frm.getAppointmentHour();
      String appointmentPm = frm.getAppointmentPm();
      if(appointmentPm.equals("PM")) {
         appointmentHour = Integer.toString(Integer.parseInt(appointmentHour) + 12);
         System.out.println(String.valueOf(String.valueOf((new StringBuffer("\n\n\nlook at the time")).append(appointmentHour).append("\n\n\n"))));
      }
      String appointmentTime = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(appointmentHour)))).append(":").append(frm.getAppointmentMinute())));
      String reason = frm.getReasonForConsultation();
      String clinicalInfo = frm.getClinicalInformation();
      String currentMeds = frm.getCurrentMedications();
      String allergies = frm.getAllergies();
      String concurrentProblems = frm.getConcurrentProblems();
      String sendTo = frm.getSendTo();
      String submission = frm.getSubmission();
      String providerNo = frm.getProviderNo();
      String demographicNo = frm.getDemographicNo();
      String status = frm.getStatus();
      String statusText = frm.getAppointmentNotes();
      String urg = frm.getUrgency();
      String requestId = "";
      MsgStringQuote str = new MsgStringQuote();
      if(submission.equals("Submit Consultation Request") || submission.equals("Submit Consultation Request And Print Preview") || submission.equals("Submit And Fax")) {
         try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "insert into consultationRequests "
            +"(referalDate,serviceId,specId,appointmentDate,appointmentTime,reason,"
            +"clinicalInfo,currentMeds,allergies,providerNo,demographicNo,status,statusText,sendTo,concurrentProblems,urgency) "
            +" values ('"+str.q(referalDate)+"','"+str.q(serviceId)+"','"+str.q(specId)+"','"+str.q(appointmentDate)+"','"+str.q(appointmentTime)+"', "
            +"'"+str.q(reason)+"','"+str.q(clinicalInfo)+"','"+str.q(currentMeds)+"','"+str.q(allergies)+"','"+str.q(providerNo)+"','"+str.q(demographicNo)
            +"','"+str.q(status)+"','"+str.q(statusText)+"','"+str.q(sendTo)+"','"+str.q(concurrentProblems)+"','"+urg+"')";
            System.out.println(" sql statement "+sql);
            db.RunSQL(sql);
            
            /* select the correct db specific command */
            String db_type = OscarProperties.getInstance().getProperty("db_type").trim();
            String dbSpecificCommand;
            if (db_type.equalsIgnoreCase("mysql")) {
               dbSpecificCommand = "SELECT LAST_INSERT_ID()";
            } else if (db_type.equalsIgnoreCase("postgresql")){
               dbSpecificCommand = "SELECT CURRVAL('consultationrequests_numeric')";
            } else
               throw new SQLException("ERROR: Database " + db_type + " unrecognized.");
            
            ResultSet rs = db.GetSQL(dbSpecificCommand);
            if(rs.next())
               requestId = Integer.toString(rs.getInt(1));
            db.CloseConn();
         }
         catch(SQLException e) {
            System.out.println(e.getMessage());
         }
         request.setAttribute("transType", "2");
      } else
         if(submission.equals("Update Consultation Request") || submission.equals("Update Consultation Request And Print Preview") || submission.equals("Update And Fax")) {
            requestId = frm.getRequestId();
            System.out.println("request id = ".concat(String.valueOf(String.valueOf(frm.getRequestId()))));
            try {
               DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
               String sql = String.valueOf(String.valueOf((new StringBuffer("update consultationRequests set  serviceId = '")).append(str.q(serviceId)).append("', ").append(" specId = '").append(str.q(specId)).append("', ").append(" appointmentDate = '").append(str.q(appointmentDate)).append("', ").append(" appointmentTime = '").append(str.q(appointmentTime)).append("', ").append(" reason = '").append(str.q(reason)).append("', ").append(" clinicalInfo = '").append(str.q(clinicalInfo)).append("', ").append(" currentMeds = '").append(str.q(currentMeds)).append("', ").append(" allergies = '").append(str.q(allergies)).append("', ").append(" status = '").append(str.q(status)).append("', ").append(" statusText = '").append(str.q(statusText)).append("', ").append(" sendTo = '").append(str.q(sendTo)).append("', ").append(" urgency = '").append(urg).append("', ").append(" concurrentProblems = '").append(str.q(concurrentProblems)).append("' ").append(" where requestId = '").append(str.q(requestId)).append("'")));
               db.RunSQL(sql);
               db.CloseConn();
            }
            catch(SQLException e) {
               System.out.println(e.getMessage());
            }
            request.setAttribute("transType", "1");
         }
      frm.setRequestId("");
      request.setAttribute("teamVar",sendTo);
      if(submission.equals("Update Consultation Request And Print Preview") || submission.equals("Submit Consultation Request And Print Preview")){
         request.setAttribute("reqId", requestId);
         return mapping.findForward("print");
      } else if (submission.equals("Update And Fax") || submission.equals("Submit And Fax")) {
         request.setAttribute("reqId", requestId);
         return mapping.findForward("fax");
      } else {
         ParameterActionForward forward = new ParameterActionForward(mapping.findForward("success"));
         forward.addParameter("de", demographicNo);
         return forward;
      }
   }
}
