package oscar.oscarEncounter.pageUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
//import oscar.Misc;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.util.*;

public class EctSaveEncounterAction extends Action {

    public ActionForward perform(ActionMapping actionmapping, ActionForm actionform, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException  {
        //UtilDateUtilities dateutilities = new UtilDateUtilities();
        EctSessionBean sessionbean = null;
        sessionbean = (EctSessionBean)httpservletrequest.getSession().getAttribute("EctSessionBean");
        sessionbean.socialHistory = httpservletrequest.getParameter("shTextarea");
        sessionbean.familyHistory = httpservletrequest.getParameter("fhTextarea");
        sessionbean.medicalHistory = httpservletrequest.getParameter("mhTextarea");
        sessionbean.ongoingConcerns = httpservletrequest.getParameter("ocTextarea");
        sessionbean.reminders = httpservletrequest.getParameter("reTextarea");
        sessionbean.encounter = httpservletrequest.getParameter("enTextarea");
        sessionbean.subject = httpservletrequest.getParameter("subject");
        java.util.Date date = UtilDateUtilities.Today();
        if(httpservletrequest.getParameter("buttonPressed").equals("Sign,Save and Exit"))
            sessionbean.encounter = sessionbean.encounter + "\n" + "[Signed on " + UtilDateUtilities.DateToString(date, "yyyy-MM-dd HH:mm:ss") + " by " + sessionbean.userName + "]";
        if(httpservletrequest.getParameter("buttonPressed").equals("Verify and Sign"))
            sessionbean.encounter = sessionbean.encounter + "\n" + "[Verified and Signed on " + UtilDateUtilities.DateToString(date, "yyyy-MM-dd HH:mm:ss") + " by " + sessionbean.userName + "]";
        if(httpservletrequest.getParameter("buttonPressed").equals("Split Chart"))
            sessionbean.subject = "SPLIT CHART";
        sessionbean.template = "";
        try {
            DBHandler dbhandler = new DBHandler(DBHandler.OSCAR_DATA);
            String s = "insert into eChart (timeStamp, demographicNo,providerNo,subject,socialHistory,familyHistory,medicalHistory,ongoingConcerns,reminders,encounter) values ('" + UtilDateUtilities.DateToString(date, "yyyyMMddHHmmss") + "'," + sessionbean.demographicNo + ",'" + sessionbean.providerNo + "','" +  UtilMisc.charEscape(sessionbean.subject, '\'') + "','" + UtilMisc.charEscape(sessionbean.socialHistory, '\'') + "','" + UtilMisc.charEscape(sessionbean.familyHistory, '\'') + "','" + UtilMisc.charEscape(sessionbean.medicalHistory, '\'') + "','" + UtilMisc.charEscape(sessionbean.ongoingConcerns, '\'') + "','" + UtilMisc.charEscape(sessionbean.reminders, '\'') + "','" + UtilMisc.charEscape(sessionbean.encounter, '\'') + "')" ;
            dbhandler.RunSQL(s);

            //change the appt status
            if (sessionbean.status != null && !sessionbean.status.equals("")) {
                oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
                as.setApptStatus(sessionbean.status);
                if (httpservletrequest.getParameter("buttonPressed").equals("Sign,Save and Exit") ) {
                    s = "update appointment set status='" + as.signStatus() + "' where appointment_no=" + sessionbean.appointmentNo;
                    dbhandler.RunSQL(s);
                }
                if (httpservletrequest.getParameter("buttonPressed").equals("Verify and Sign") ) {
                    s = "update appointment set status='" + as.verifyStatus() + "' where appointment_no=" + sessionbean.appointmentNo;
                    dbhandler.RunSQL(s);
                }
            }
            dbhandler.CloseConn();
        } catch(SQLException sqlexception) {
            System.out.println(sqlexception.getMessage());
        }
        if(httpservletrequest.getParameter("buttonPressed").equals("Sign,Save and Exit") 
                || httpservletrequest.getParameter("buttonPressed").equals("Verify and Sign"))
            return actionmapping.findForward("success");
        if(httpservletrequest.getParameter("buttonPressed").equals("Save"))
            return actionmapping.findForward("saveAndStay");
        if(httpservletrequest.getParameter("buttonPressed").equals("Split Chart"))
            return actionmapping.findForward("splitchart");
        else
            return actionmapping.findForward("failure");
    }
}