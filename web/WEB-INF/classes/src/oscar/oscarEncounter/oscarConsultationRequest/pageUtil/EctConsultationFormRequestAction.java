// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
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
import oscar.util.UtilMisc;
import oscar.oscarEncounter.pageUtil.EctSessionBean;

public class EctConsultationFormRequestAction extends Action {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctSessionBean bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
        if(bean == null)
            return mapping.findForward("eject");
        EctConsultationFormRequestForm frm = (EctConsultationFormRequestForm)form;
        String referalDate = frm.getReferalDate();
        String serviceId = frm.getService();
        String specId = frm.getSpecialist();
        String appointmentDate = frm.getAppointmentYear() + "/" + frm.getAppointmentMonth() + "/" + frm.getAppointmentDay() ;
        String appointmentHour = frm.getAppointmentHour();
        String appointmentPm = frm.getAppointmentPm();
        if(appointmentPm.equals("PM"))
        {
            appointmentHour = Integer.toString(Integer.parseInt(appointmentHour) + 12);
        }
        String appointmentTime = appointmentHour + ":" + frm.getAppointmentMinute();
        String reason = frm.getReasonForConsultation();
        String clinicalInfo = frm.getClinicalInformation();
        String currentMeds = frm.getCurrentMedications();
        String allergies = frm.getAllergies();
        String concurrentProblems = frm.getConcurrentProblems();
        String sendTo = frm.getSendTo();
        String submission = frm.getSubmission();
        String providerNo = bean.providerNo;
        String demographicNo = bean.demographicNo;
        String status = frm.getStatus();
        String statusText = frm.getAppointmentNotes();
        String urg = frm.getUrgency();
        String requestId = "";
        if(submission.equals("Submit Consultation Request") || submission.equals("Submit Consultation Request And Print Preview"))
        {
            try
            {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = "insert into consultationRequests (referalDate,serviceId,specId,appointmentDate,appointmentTime,reason,clinicalInfo,currentMeds,allergies,providerNo,demographicNo,status,statusText,sendTo,concurrentProblems,urgency) values ('" + UtilMisc.charEscape(referalDate,'\'') + "','" + UtilMisc.charEscape(serviceId,'\'') + "','" + UtilMisc.charEscape(specId,'\'') + "','" + UtilMisc.charEscape(appointmentDate,'\'') + "','" + UtilMisc.charEscape(appointmentTime,'\'') + "','" + UtilMisc.charEscape(reason,'\'') + "','" + UtilMisc.charEscape(clinicalInfo,'\'') + "','" + UtilMisc.charEscape(currentMeds,'\'') + "','" + UtilMisc.charEscape(allergies,'\'') + "','" + UtilMisc.charEscape(providerNo,'\'') + "','" + UtilMisc.charEscape(demographicNo,'\'') + "','" + UtilMisc.charEscape(status,'\'') + "','" + UtilMisc.charEscape(statusText,'\'') + "','" + UtilMisc.charEscape(sendTo,'\'') + "','" + UtilMisc.charEscape(concurrentProblems,'\'') + "','" + urg + "')" ;
                db.RunSQL(sql);
                ResultSet rs = db.GetSQL("SELECT LAST_INSERT_ID() ");
                if(rs.next())
                    requestId = Integer.toString(rs.getInt(1));
                db.CloseConn();
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
            request.setAttribute("transType", "2");
        } else
        if(submission.equals("Update Consultation Request") || submission.equals("Update Consultation Request And Print Preview"))
        {
            requestId = bean.getConsultationRequestId();
            //System.out.println("request id = ".concat(String.valueOf(String.valueOf(bean.getConsultationRequestId()))));
            try
            {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = "update consultationRequests set  serviceId = '" + UtilMisc.charEscape(serviceId,'\'') + "', " + " specId = '" + UtilMisc.charEscape(specId,'\'') + "', " + " appointmentDate = '" + UtilMisc.charEscape(appointmentDate,'\'') + "', " + " appointmentTime = '" + UtilMisc.charEscape(appointmentTime,'\'') + "', " + " reason = '" + UtilMisc.charEscape(reason,'\'') + "', " + " clinicalInfo = '" + UtilMisc.charEscape(clinicalInfo,'\'') + "', " + " currentMeds = '" + UtilMisc.charEscape(currentMeds,'\'') + "', " + " allergies = '" + UtilMisc.charEscape(allergies,'\'') + "', " + " status = '" + UtilMisc.charEscape(status,'\'') + "', " + " statusText = '" + UtilMisc.charEscape(statusText,'\'') + "', " + " sendTo = '" + UtilMisc.charEscape(sendTo,'\'') + "', " + " urgency = '" + urg + "', " + " concurrentProblems = '" + UtilMisc.charEscape(concurrentProblems,'\'') + "' " + " where requestId = '" + UtilMisc.charEscape(requestId,'\'') + "'" ;
                db.RunSQL(sql);
                db.CloseConn();
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
            request.setAttribute("transType", "1");
        }
        bean.setConsultationRequestId("");
        if(submission.equals("Update Consultation Request And Print Preview") || submission.equals("Submit Consultation Request And Print Preview"))
        {
            request.setAttribute("reqId", requestId);
            return mapping.findForward("print");
        } else
        {
            return mapping.findForward("success");
        }
    }
}
