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
import oscar.oscarEncounter.oscarMeasurements.pageUtil.EctValidation;
import oscar.OscarProperties;


public class RptInitializeFrequencyOfRelevantTestsCDMReportAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
 
        RptInitializeFrequencyOfRelevantTestsCDMReportForm frm = (RptInitializeFrequencyOfRelevantTestsCDMReportForm) form;                       
        request.getSession().setAttribute("RptInitializeFrequencyOfRelevantTestsCDMReportForm", frm);
        String requestId = "";

        try{
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);  
                if(!validate(frm, request)){                    
                    return (new ActionForward(mapping.getInput()));
                }
                
                ArrayList reportMsg = new ArrayList();
                getNbPatientSeen(db, frm, reportMsg, request);  
                getFrequenceOfTestPerformed(db, frm, reportMsg, request);
                MessageResources mr = getResources(request);
                String title = mr.getMessage("oscarReport.CDMReport.msgFrequencyOfRelevantTestsBeingPerformed");
                request.setAttribute("title", title);
                request.setAttribute("messages", reportMsg);
                
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

     /*****************************************************************************************
     * validate the input date
     *
     * @return boolean
     ******************************************************************************************/ 
    private boolean validate(RptInitializeFrequencyOfRelevantTestsCDMReportForm frm, HttpServletRequest request){
        EctValidation ectValidation = new EctValidation();                    
        ActionErrors errors = new ActionErrors();        
        String[] startDateD = frm.getStartDateD();
        String[] endDateD = frm.getEndDateD();         
        int[] exactly = frm.getExactly(); 
        int[] moreThan = frm.getMoreThan();         
        int[] lessThan = frm.getLessThan();
        String[] frequencyCheckbox = frm.getFrequencyCheckbox();
        boolean valid = true;
        
        if (frequencyCheckbox!=null){
       
            for(int i=0; i<frequencyCheckbox.length; i++){
                int ctr = Integer.parseInt(frequencyCheckbox[i]);                
                String startDate = startDateD[ctr];
                String endDate = endDateD[ctr];                                    
                String measurementType = (String) frm.getValue("measurementTypeD"+ctr);                                    
                
                if(!ectValidation.isDate(startDate)){                       
                    errors.add(startDate, new ActionError("errors.invalidDate", measurementType));
                    saveErrors(request, errors);
                    valid = false;
                }
                if(!ectValidation.isDate(endDate)){                       
                    errors.add(endDate, new ActionError("errors.invalidDate", measurementType));
                    saveErrors(request, errors);
                    valid = false;
                }
            }
        }
        return valid;
    }   

     /*****************************************************************************************
     * get the number of Patient seen during aspecific time period
     *
     * @return ArrayList which contain the result in String format
     ******************************************************************************************/  
    private int getNbPatientSeen(DBHandler db, RptInitializeFrequencyOfRelevantTestsCDMReportForm frm, ArrayList messages, HttpServletRequest request){
        String[] patientSeenCheckbox = frm.getPatientSeenCheckbox();
        String startDateA = frm.getStartDateA();
        String endDateA = frm.getEndDateA();
        int nbPatient = 0;
        if(patientSeenCheckbox!=null){
            try{
                String sql = "SELECT * FROM eChart WHERE timestamp >= '" + startDateA + "' AND timestamp <= '" + endDateA + "'";
                ResultSet rs;
                rs = db.GetSQL(sql);
                System.out.println("SQL Statement: " + sql);
                rs.last();
                nbPatient = rs.getRow();
                MessageResources mr = getResources(request);
                String msg = mr.getMessage("oscarReport.CDMReport.msgPatientSeen", Integer.toString(nbPatient), startDateA, endDateA); 
                System.out.println(msg);
                messages.add(msg);
                messages.add("");
                rs.close();

            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
        return nbPatient;
    }
    
     /*****************************************************************************************
     * get the Frequence of Test Performed during aspecific time period
     *
     * @return ArrayList which contain the result in String format
     ******************************************************************************************/      
    private ArrayList getFrequenceOfTestPerformed(DBHandler db, RptInitializeFrequencyOfRelevantTestsCDMReportForm frm, ArrayList percentageMsg, HttpServletRequest request){
        String[] startDateD = frm.getStartDateD();
        String[] endDateD = frm.getEndDateD();         
        int[] exactly = frm.getExactly(); 
        int[] moreThan = frm.getMoreThan();         
        int[] lessThan = frm.getLessThan();
        String[] frequencyCheckbox = frm.getFrequencyCheckbox();      
        MessageResources mr = getResources(request);
        
        if (frequencyCheckbox!=null){
            try{
                
                for(int i=0; i<frequencyCheckbox.length; i++){
                    int ctr = Integer.parseInt(frequencyCheckbox[i]);                   
                    String startDate = startDateD[ctr];
                    String endDate = endDateD[ctr];                    
                    int exact = exactly[ctr];
                    int more = moreThan[ctr];
                    int less = lessThan[ctr];
                                        
                    String measurementType = (String) frm.getValue("measurementTypeD"+ctr);                    
                    String sNumMInstrc = (String) frm.getValue("mNbInstrcsD"+ctr);
                    int iNumMInstrc = Integer.parseInt(sNumMInstrc);                    
                    ArrayList patients = getPatients(db, startDate, endDate);
                    int nbPatients = patients.size();
                    
                    for(int j=0; j<iNumMInstrc; j++){

                        double exactPercentage = 0;
                        double morePercentage = 0;
                        double lessPercentage = 0;
                        int nbExact =0;
                        int nbMore =0;
                        int nbLess =0;
                        
                        String mInstrc = (String) frm.getValue("mInstrcsCheckboxD"+ctr+j);
                        if(mInstrc!=null){
                            for(int k=0; k<nbPatients; k++){
                                String patient = (String) patients.get(k);                            

                                String sql = "SELECT * FROM measurements WHERE dateObserved >='" + startDate + "'AND dateObserved <='" + endDate
                                             + "'AND type='"+ measurementType + "'AND measuringInstruction='"+ mInstrc 
                                             + "' AND demographicNo=" + "'" + patient + "'";

                                System.out.println("SQL statement is " + sql);
                                ResultSet rs = db.GetSQL(sql);
                                rs.last();
                                int nbTest = rs.getRow();                              
                                rs.close();
                                
                                if(nbTest == exact){
                                    nbExact++;
                                }
                                if(nbTest > more){
                                    nbMore++;
                                }
                                if(nbTest < less){
                                    nbLess++;
                                }

                                if(nbPatients!=0){
                                    exactPercentage = Math.round( ((double) nbExact/ (double) nbPatients) * 100);
                                    morePercentage = Math.round( ((double) nbMore/ (double) nbPatients) * 100);
                                    lessPercentage = Math.round( ((double) nbLess/ (double) nbPatients) * 100);                                
                                }

                            }
                        
                            String[] param0 = {  startDate, 
                                                endDate,
                                                measurementType,
                                                mInstrc,
                                                Double.toString(exactPercentage),
                                                Integer.toString(exact)};
                            String msg = mr.getMessage("oscarReport.CDMReport.msgFrequencyOfRelevantTestsExact", param0);                              
                            System.out.println(msg);
                            percentageMsg.add(msg);
                            String[] param1 = {   startDate, 
                                                endDate,
                                                measurementType,
                                                mInstrc,
                                                Double.toString(morePercentage),
                                                Integer.toString(more)};
                            msg = mr.getMessage("oscarReport.CDMReport.msgFrequencyOfRelevantTestsMoreThan", param1); 
                            System.out.println(msg);
                            percentageMsg.add(msg); 
                            String[] param2 = {   startDate, 
                                                endDate,
                                                measurementType,
                                                mInstrc,
                                                Double.toString(lessPercentage),
                                                Integer.toString(less)};
                            msg = mr.getMessage("oscarReport.CDMReport.msgFrequencyOfRelevantTestsLessThan", param2); 
                            System.out.println(msg);
                            percentageMsg.add(msg); 
                            percentageMsg.add("");
                        }
                    }                  
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
        return percentageMsg;
    }

     /*****************************************************************************************
     * get the number of patients during aspecific time period
     *
     * @return ArrayList which contain the result in String format
     ******************************************************************************************/      
    private ArrayList getPatients(DBHandler db, String startDate, String endDate){

        ArrayList patients = new ArrayList();
        
        try{
            String sql = "SELECT DISTINCT demographicNo  FROM eChart WHERE timestamp >= '" + startDate + "' AND timestamp <= '" + endDate + "'";
            System.out.println("SQL Statement: " + sql);
            ResultSet rs;
            
            for(rs=db.GetSQL(sql); rs.next();){            
                String patient = rs.getString("demographicNo");
                patients.add(patient);                
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
       
        return patients;
    }        
}
