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
package oscar.oscarReport.pageUtil;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.validator.*;
import org.apache.commons.validator.*;
import org.apache.struts.util.MessageResources;
import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgStringQuote;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.OscarProperties;


public class RptInitializePatientsInAbnormalRangeCDMReportAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
 
        RptInitializePatientsInAbnormalRangeCDMReportForm frm = (RptInitializePatientsInAbnormalRangeCDMReportForm) form;                       
        request.getSession().setAttribute("RptInitializePatientsInAbnormalRangeCDMReportForm", frm);
        String requestId = "";

        try{
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                                
                int nbPatient = getNbPatientSeen(db, frm);                
                ArrayList messages = getInAbnormalRangePercentage(db, frm);
                MessageResources mr = getResources(request);
                String title = mr.getMessage("oscarReport.CDMReport.msgPercentageOfPatientInAbnormalRange");
                request.setAttribute("title", title);
                request.setAttribute("messages", messages);
                
                /* select the correct db specific command */
                String db_type = OscarProperties.getInstance().getProperty("db_type").trim();
                String dbSpecificCommand;
                if (db_type.equalsIgnoreCase("mysql")) {
                    dbSpecificCommand = "SELECT LAST_INSERT_ID()";
                } 
                else if (db_type.equalsIgnoreCase("postgresql")){
                    dbSpecificCommand = "SELECT CURRVAL('consultationrequests_numeric')";
                }
                else
                    throw new SQLException("ERROR: Database " + db_type + " unrecognized.");

                    
                ResultSet rs = db.GetSQL(dbSpecificCommand);
                if(rs.next())
                    requestId = Integer.toString(rs.getInt(1));
                db.CloseConn();
                
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return mapping.findForward("success");
    }
     
    private int getNbPatientSeen(DBHandler db, RptInitializePatientsInAbnormalRangeCDMReportForm frm){
        String[] patientSeenCheckbox = frm.getPatientSeenCheckbox();
        String startDateA = frm.getStartDateA();
        String endDateA = frm.getEndDateA();
        int nbPatient = 0;
        if(patientSeenCheckbox!=null){
            try{
                String sql = "SELECT * FROM eChart WHERE timestamp > '" + startDateA + "' AND timestamp < '" + endDateA + "'";
                ResultSet rs;
                rs = db.GetSQL(sql);
                System.out.println("SQL Statement: " + sql);
                rs.last();
                nbPatient = rs.getRow();
                System.out.println("There are " + Integer.toString(nbPatient) + " patients seen from " + startDateA + " to " + endDateA);
                rs.close();

            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
        return nbPatient;
    }    
    
    private ArrayList getInAbnormalRangePercentage(DBHandler db, RptInitializePatientsInAbnormalRangeCDMReportForm frm){
        String[] startDateC = frm.getStartDateC();
        String[] endDateC = frm.getEndDateC();         
        String[] upperBound = frm.getUpperBound(); 
        String[] lowerBound = frm.getLowerBound();         
        String[] abnormalCheckbox = frm.getAbnormalCheckbox();
        ArrayList metGLPercentageMsg = new ArrayList();
        if (abnormalCheckbox!=null){
            try{
                System.out.println("the length of abnormal range checkbox is "  + abnormalCheckbox.length);

                for(int i=0; i<abnormalCheckbox.length; i++){
                    int ctr = Integer.parseInt(abnormalCheckbox[i]);
                    System.out.println("the value of abnormal range Checkbox is: " + abnormalCheckbox[i]);
                    String startDate = startDateC[ctr];
                    String endDate = endDateC[ctr];                    
                    String upper = upperBound[ctr];
                    String lower = lowerBound[ctr];
                    String measurementType = (String) frm.getValue("measurementTypeC"+ctr);                    
                    String sNumMInstrc = (String) frm.getValue("mNbInstrcsC"+ctr);
                    int iNumMInstrc = Integer.parseInt(sNumMInstrc);                    
                    
                    for(int j=0; j<iNumMInstrc; j++){
                        double metGLPercentage = 0;
                        String mInstrc = (String) frm.getValue("mInstrcsCheckboxC"+ctr+j);
                        if(mInstrc!=null){
                            String sql = "SELECT * FROM measurements WHERE dateObserved >'" + startDate + "'AND dateObserved <'" + endDate
                                         + "'AND type='"+ measurementType + "'AND measuringInstruction='"+ mInstrc 
                                         + "' AND dataField <" + "'" + upper + "' AND dataField >" + "'" + lower + "'";

                            System.out.println("SQL statement is " + sql);
                            ResultSet rs = db.GetSQL(sql);
                            rs.last();
                            int nbMetGL = rs.getRow();
                            rs.close();

                            sql = "SELECT * FROM measurements WHERE dateObserved >'" + startDate + "'AND dateObserved <'" + endDate +
                                  "'AND type='"+ measurementType + "'AND measuringInstruction='"+ mInstrc + "'";
                            System.out.println("SQL statement is" + sql);
                            rs = db.GetSQL(sql);
                            rs.last();
                            int nbGeneral = rs.getRow();
                            rs.close();

                            if(nbGeneral!=0){
                                metGLPercentage = Math.round((double)nbMetGL/(double)nbGeneral) * 100;
                            }
                            String msg = "From " + startDate + " to " + endDate + ": " + measurementType + "--" + mInstrc + " "
                                               + metGLPercentage + "% " + " < " + upper + " > " + lower;
                            System.out.println(msg);
                            metGLPercentageMsg.add(msg);                     
                        }
                    }
                    
                    //percentage of patients who meet guideline for the same test with all measuring instruction
                        double metGLPercentage = 0;                        
                        String sql = "SELECT * FROM measurements WHERE dateObserved >'" + startDate + "'AND dateObserved <'" + endDate
                                     + "'AND type='"+ measurementType + "' AND dataField <" + "'" + upper + "' AND dataField >" + "'" + lower + "'";
                                     
                        System.out.println("SQL statement is " + sql);
                        ResultSet rs = db.GetSQL(sql);
                        rs.last();
                        int nbMetGL = rs.getRow();
                        rs.close();
                        
                        sql = "SELECT * FROM measurements WHERE dateObserved >'" + startDate + "'AND dateObserved <'" + endDate 
                              + "'AND type='"+ measurementType + "'";
                        System.out.println("SQL statement is" + sql);
                        rs = db.GetSQL(sql);
                        rs.last();
                        int nbGeneral = rs.getRow();
                        rs.close();

                        if(nbGeneral!=0){
                            metGLPercentage = Math.round((double) nbMetGL/ (double) nbGeneral) * 100;
                        }
                        String msg = "From " + startDate + " to " + endDate + ": " + measurementType + " "
                                           + metGLPercentage + "% " + " < " + upper + " > " + lower;
                        System.out.println(msg);
                        metGLPercentageMsg.add(msg); 
                }
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
        else{
            System.out.println("guideline checkbox is null");
        }
        return metGLPercentageMsg;
    }
             
}
