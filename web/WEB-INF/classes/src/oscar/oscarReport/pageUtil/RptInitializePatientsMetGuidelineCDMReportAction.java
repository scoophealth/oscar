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


public class RptInitializePatientsMetGuidelineCDMReportAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
 
        RptInitializePatientsMetGuidelineCDMReportForm frm = (RptInitializePatientsMetGuidelineCDMReportForm) form;                       
        request.getSession().setAttribute("RptInitializePatientsMetGuidelineCDMReportForm", frm);
        String requestId = "";
        
        
        try{
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                                
                ArrayList reportMsg = new ArrayList();
                getNbPatientSeen(db, frm, reportMsg);                
                getMetGuidelinePercentage(db, frm, reportMsg);
                getPatientsMetAllSelectedGuideline(db, frm, reportMsg);
                MessageResources mr = getResources(request);
                String title = mr.getMessage("oscarReport.CDMReport.msgPercentageOfPatientWhoMetGuideline");
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
         
        
    
    private ArrayList getNbPatientSeen(DBHandler db, RptInitializePatientsMetGuidelineCDMReportForm frm, ArrayList messages){
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
                String msg = "There are " + Integer.toString(nbPatient) + " patients seen from " + startDateA + " to " + endDateA;
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
        return messages;
    }
    
    private ArrayList getMetGuidelinePercentage(DBHandler db, RptInitializePatientsMetGuidelineCDMReportForm frm, ArrayList metGLPercentageMsg){
        String[] startDateB = frm.getStartDateB();
        String[] endDateB = frm.getEndDateB(); 
        String[] idB = frm.getIdB();
        String[] guidelineB = frm.getGuidelineB();        
        String[] guidelineCheckbox = frm.getGuidelineCheckbox();
        
        if (guidelineCheckbox!=null){
            try{
                System.out.println("the length of guideline checkbox is "  + guidelineCheckbox.length);
                for(int i=0; i<guidelineCheckbox.length; i++){
                    int ctr = Integer.parseInt(guidelineCheckbox[i]);
                    System.out.println("the value of guildline Checkbox is: " + guidelineCheckbox[i]);
                    String startDate = startDateB[ctr];
                    String endDate = endDateB[ctr];                    
                    String guideline = guidelineB[ctr];
                    String measurementType = (String) frm.getValue("measurementType"+ctr);
                    String aboveBelow = (String) frm.getValue("aboveBelow"+ctr);
                    String sNumMInstrc = (String) frm.getValue("mNbInstrcs"+ctr);
                    int iNumMInstrc = Integer.parseInt(sNumMInstrc);                    
                    
                    for(int j=0; j<iNumMInstrc; j++){
                        double metGLPercentage = 0;
                        String mInstrc = (String) frm.getValue("mInstrcsCheckbox"+ctr+j);
                        if(mInstrc!=null){
                            String sql = "SELECT * FROM measurements WHERE dateObserved >'" + startDate + "'AND dateObserved <'" + endDate
                                         + "'AND type='"+ measurementType + "'AND measuringInstruction='"+ mInstrc 
                                         + "' AND dataField" + aboveBelow + "'" + guideline + "'";

                            System.out.println("SQL statement is " + sql);
                            ResultSet rs = db.GetSQL(sql);
                            rs.last();
                            double nbMetGL = rs.getRow();
                            rs.close();

                            sql = "SELECT * FROM measurements WHERE dateObserved >'" + startDate + "'AND dateObserved <'" + endDate +
                                  "'AND type='"+ measurementType + "'AND measuringInstruction='"+ mInstrc + "'";
                            System.out.println("SQL statement is" + sql);
                            rs = db.GetSQL(sql);
                            rs.last();
                            double nbGeneral = rs.getRow();
                            rs.close();

                            if(nbGeneral!=0){
                                metGLPercentage = (nbMetGL/nbGeneral) * 100;
                            }
                            String msg = "From " + startDate + "to " + endDate + ": " + measurementType + "--" + mInstrc + " "
                                               + metGLPercentage + "% " + aboveBelow + " " + guideline;
                            System.out.println(msg);
                            metGLPercentageMsg.add(msg);                     
                        }
                    }
                    
                    //percentage of patients who meet guideline for the same test with all measuring instruction
                        double metGLPercentage = 0;                        
                        String sql = "SELECT * FROM measurements WHERE dateObserved >'" + startDate + "'AND dateObserved <'" + endDate
                                     + "'AND type='"+ measurementType + "' AND dataField" + aboveBelow + "'" + guideline + "'";
                                     
                        System.out.println("SQL statement is " + sql);
                        ResultSet rs = db.GetSQL(sql);
                        rs.last();
                        double nbMetGL = rs.getRow();
                        rs.close();
                        
                        sql = "SELECT * FROM measurements WHERE dateObserved >'" + startDate + "'AND dateObserved <'" + endDate 
                              + "'AND type='"+ measurementType + "'";
                        System.out.println("SQL statement is" + sql);
                        rs = db.GetSQL(sql);
                        rs.last();
                        double nbGeneral = rs.getRow();
                        rs.close();

                        if(nbGeneral!=0){
                            metGLPercentage = (nbMetGL/nbGeneral) * 100;
                        }
                        String msg = "From " + startDate + " to " + endDate + ": " + measurementType + " "
                                           + metGLPercentage + "% " + aboveBelow + " " + guideline;
                        System.out.println(msg);
                        metGLPercentageMsg.add(msg); 
                        metGLPercentageMsg.add(""); 
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
    
    private ArrayList getPatientsMetAllSelectedGuideline(DBHandler db, RptInitializePatientsMetGuidelineCDMReportForm frm, ArrayList reportMsg){       
        String[] guidelineCheckbox = frm.getGuidelineCheckbox();
        String[] guidelineB = frm.getGuidelineB();        
        String startDate = frm.getStartDateA();
        String endDate = frm.getEndDateA();
        ArrayList patients = getPatients(db, startDate, endDate);
        int nbPatients = patients.size();
        int nbPatientsDoneAllTest = 0;
        int nbPatientsPassAllTest = 0;
        
        try{
            System.out.println("Number of Patients: " + nbPatients);
            for(int i=0; i<nbPatients; i++){
                
                String patient = (String) patients.get(i);
                boolean passAllTests = false;
                boolean doneAllTests = false;
                ArrayList testsDone = new ArrayList();
                ArrayList data = new ArrayList();
                
                String sql = "SELECT * FROM measurements WHERE demographicNo='" + patient + "' ORDER BY dateObserved DESC";
                db.RunSQL(sql);
                
                sql = "SELECT DISTINCT type , dataField FROM measurements WHERE demographicNo='" + patient + "'ORDER BY dateObserved DESC";
                ResultSet rs;                
                
                for(rs=db.GetSQL(sql);rs.next(); ){                    
                    testsDone.add(rs.getString("type"));
                    data.add(rs.getString("dataField"));
                }
                
                System.out.println("guidelineCheckbox length: " + guidelineCheckbox.length);
                System.out.println("tests done size: " + testsDone.size());
                
                if(guidelineCheckbox.length<=testsDone.size()){
                    
                    for(int j=0; j<guidelineCheckbox.length; j++){ 
                        
                        int ctr = Integer.parseInt(guidelineCheckbox[j]);
                        String guideline = guidelineB[ctr];
                        String measurementType = (String) frm.getValue("measurementType"+ctr);
                        String aboveBelow = (String) frm.getValue("aboveBelow"+ctr);
                        System.out.println("guidelineCheckbox loop: " + measurementType);
                        
                        for(int k=0; k<testsDone.size(); k++){  
                            doneAllTests=false;
                            String testDone = (String) testsDone.get(k);
                            System.out.println("testsDone loop: " + testDone);
                                                       
                            if(measurementType.compareTo(testDone)==0){ 
                                
                                System.out.println("measurementType is the same as testDone");
                                System.out.println("the guideline is " + guideline);
                                String tmp = (String) data.get(k);
                                System.out.println("the dataEntry is " + tmp);
                                if(getValidation(db, frm, ctr)==1){ 
                                    System.out.println("this is a numeric value");
                                    double dataEntry = Double.parseDouble((String) data.get(k));
                                    if(aboveBelow.compareTo(">")==0){                                        
                                        if(Double.parseDouble(guideline)>=dataEntry){
                                            passAllTests=true;
                                            System.out.println("Pass double test");
                                        }
                                        else{
                                            passAllTests=false;
                                        }
                                    }
                                    else if(aboveBelow.compareTo("<")==0){
                                        if(Double.parseDouble(guideline)<=dataEntry){
                                            passAllTests=true;
                                            System.out.println("Pass double test");
                                        }
                                        else{
                                            passAllTests=false;
                                        }
                                    }
                                }
                                else if(measurementType.compareTo("BP") == 0){
                                    String dataEntry = (String) data.get(k);                                    
                                    int slashIndex = guideline.indexOf("/");
                                    int dataSlashIndex = dataEntry.indexOf("/");
                                    
                                    if (slashIndex >= 0 && dataSlashIndex >=0){                                                                                                                                                                
                                        String systolic = guideline.substring(0, slashIndex);
                                        String diastolic = guideline.substring(slashIndex+1);
                                        String systolicData = dataEntry.substring(0, dataSlashIndex);
                                        String diastolicData = dataEntry.substring(dataSlashIndex+1);
                                        
                                        int iGuidelineSystolic = Integer.parseInt(systolic);
                                        int iGuidelineDiastolic = Integer.parseInt(diastolic); 
                                        int iDataSystolic = Integer.parseInt(systolicData);
                                        int iDataDiastolic = Integer.parseInt(diastolicData); 

                                        if(aboveBelow.compareTo("<")==0){
                                            if(iDataSystolic<=iGuidelineSystolic && iDataDiastolic<=iGuidelineDiastolic){
                                                passAllTests=true;
                                                System.out.println("pass this BP test");
                                            }
                                            else{
                                                passAllTests=false;
                                                System.out.println("fail this BP test");
                                            }
                                        }
                                        else if(aboveBelow.compareTo(">")==0){
                                            if(iDataSystolic>=iGuidelineSystolic && iDataDiastolic>=iGuidelineDiastolic){
                                                passAllTests=true;
                                                System.out.println("pass this BP test");
                                            }
                                            else{
                                                passAllTests=false;
                                                System.out.println("fail this BP test");
                                            }
                                        } 
                                    }
                                }
                                else{
                                    String dataEntry = (String) data.get(k);
                                    if(guideline=="YES"||guideline=="Yes"||guideline=="yes"||guideline=="Y" ){                                        
                                        if(dataEntry.compareTo("YES")==0 || dataEntry.compareTo("Yes")==0 
                                           || dataEntry.compareTo("Y")==0 || dataEntry.compareTo("Yes")==0){
                                               passAllTests=true;
                                               System.out.println("Pass yesno test");
                                        }
                                        else{
                                            passAllTests=false;
                                            System.out.println("fail yesno test");
                                        }
                                    }
                                    else if(guideline=="NO"||guideline=="No"||guideline=="no"||guideline=="N" ){
                                        if(dataEntry.compareTo("NO")==0 || dataEntry.compareTo("No")==0 
                                           || dataEntry.compareTo("N")==0 || dataEntry.compareTo("no")==0){
                                               passAllTests=true;
                                               System.out.println("Pass yesno test");
                                        }
                                        else{
                                            passAllTests=false;
                                             System.out.println("fail yesno test");
                                        }
                                    }
                                }
                                data.remove(k);
                                testsDone.remove(k);
                                k--;
                                doneAllTests=true; 
                                break;
                            }
                        }
                        if(!doneAllTests){
                            break;
                        }
                    }
                }
                else{
                    doneAllTests=false;
                }
                if(doneAllTests){
                    System.out.println("doneAllTest is true" );
                    nbPatientsDoneAllTest++;
                }
                if(passAllTests){
                    nbPatientsPassAllTest++;
                }
            }
            double passAllTestsPercentage = Math.round( ((double) nbPatientsPassAllTest/ (double) nbPatientsDoneAllTest) * 100);
            String msg = "There are " + nbPatientsDoneAllTest + " done all the selected tests and " 
                         + passAllTestsPercentage + "% pass all the tests";
            System.out.println(msg); 
            
            reportMsg.add(msg);
            reportMsg.add("/n");
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        
        return reportMsg;
    }

    
    private ArrayList getPatients(DBHandler db, String startDate, String endDate){

        ArrayList patients = new ArrayList();
        
        try{
            String sql = "SELECT DISTINCT demographicNo  FROM eChart WHERE timestamp > '" + startDate + "' AND timestamp < '" + endDate + "'";
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
    
    private int getValidation(DBHandler db, RptInitializePatientsMetGuidelineCDMReportForm frm, int ctr){
        String measurementType = (String) frm.getValue("measurementType"+ctr);
        
        try{
            String sql = "SELECT * FROM measurementType WHERE type='" + measurementType + "'";
            ResultSet rs;
            rs = db.GetSQL(sql);
            rs.next();
            String validation = rs.getString("validation");
            rs.close();
            sql = "SELECT * FROM validations WHERE id='" + validation + "'";
            rs = db.GetSQL(sql);
            rs.next();
            if(rs.getString("isNumeric")!=null){
                System.out.println("isNumeric: " + rs.getInt("isNumeric"));
                return rs.getInt("isNumeric");
            }
            else{
                return 0;
            }
            
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        
        return 0;
    }

    
    
}
