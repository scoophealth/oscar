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
package oscar.oscarEncounter.pageUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import oscar.Misc;
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
        java.util.Date date = UtilDateUtilities.Today();
        if(httpservletrequest.getParameter("buttonPressed").equals("Sign,Save and Exit"))
            sessionbean.encounter = sessionbean.encounter + "\n" + "[Signed on " + UtilDateUtilities.DateToString(date, "yyyy-MM-dd HH:mm:ss") + " by " + sessionbean.userName + "]";
        sessionbean.template = "";
        try {
            DBHandler dbhandler = new DBHandler(DBHandler.OSCAR_DATA);
            String s = "insert into eChart (timeStamp, demographicNo,providerNo,socialHistory,familyHistory,medicalHistory,ongoingConcerns,reminders,encounter) values ('" + UtilDateUtilities.DateToString(date, "yyyyMMddHHmmss") + "'," + sessionbean.demographicNo + ",'" + sessionbean.providerNo + "','" + Misc.charEscape(sessionbean.socialHistory, '\'') + "','" + Misc.charEscape(sessionbean.familyHistory, '\'') + "','" + Misc.charEscape(sessionbean.medicalHistory, '\'') + "','" + Misc.charEscape(sessionbean.ongoingConcerns, '\'') + "','" + Misc.charEscape(sessionbean.reminders, '\'') + "','" + Misc.charEscape(sessionbean.encounter, '\'') + "')" ;
            dbhandler.RunSQL(s);
            dbhandler.CloseConn();
        } catch(SQLException sqlexception) {
            System.out.println(sqlexception.getMessage());
        }
        if(httpservletrequest.getParameter("buttonPressed").equals("Sign,Save and Exit"))
            return actionmapping.findForward("success");
        if(httpservletrequest.getParameter("buttonPressed").equals("Save"))
            return actionmapping.findForward("saveAndStay");
        else
            return actionmapping.findForward("failure");
    }
}
