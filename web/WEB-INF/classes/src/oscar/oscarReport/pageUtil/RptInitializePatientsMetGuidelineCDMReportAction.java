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


public class RptInitializePatientsMetGuidelineCDMReportAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
 
        RptInitializePatientsMetGuidelineCDMReportForm frm = (RptInitializePatientsMetGuidelineCDMReportForm) form;                       
        request.getSession().setAttribute("RptInitializePatientsMetGuidelineCDMReportForm", frm);        
        
        
        try{
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                if(!validate(frm, request)){
                    System.out.println("the form is invalid");
                    return (new ActionForward(mapping.getInput()));
                }
                ArrayList reportMsg = new ArrayList();
                getNbPatientSeen(db, frm, reportMsg, request);                
                getMetGuidelinePercentage(db, frm, reportMsg, request);
                getPatientsMetAllSelectedGuideline(db, frm, reportMsg, request);
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
                                    
                db.CloseConn();
                
        }
        
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        
        return mapping.findForward("success");
    }
         

    
    /*****************************************************************************************
     * validate the input value
     *
     * @return boolean
     ******************************************************************************************/ 
    private boolean validate(RptInitializePatientsMetGuidelineCDMReportForm frm, HttpServletRequest request){
        EctValidation ectValidation = new EctValidation();                    
        ActionErrors errors = new ActionErrors();        
        String[] startDateB = frm.getStartDateB();
        String[] endDateB = frm.getEndDateB(); 
        String[] idB = frm.getIdB();
        String[] guidelineB = frm.getGuidelineB();        
        String[] guidelineCheckbox = frm.getGuidelineCheckbox();

        boolean valid = true;
        
        if (guidelineCheckbox!=null){ 
            for(int i=0; i<guidelineCheckbox.length; i++){
                int ctr = Integer.parseInt(guidelineCheckbox[i]);                
                String startDate = startDateB[ctr];
                String endDate = endDateB[ctr];                    
                String guideline = guidelineB[ctr];
                String measurementType = (String) frm.getValue("measurementType"+ctr);                
                String sNumMInstrc = (String) frm.getValue("mNbInstrcs"+ctr);                
                int iNumMInstrc = Integer.parseInt(sNumMInstrc);                     
                
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
                for(int j=0; j<iNumMInstrc; j++){
                    
                    String mInstrc = (String) frm.getValue("mInstrcsCheckbox"+ctr+j);
                    if(mInstrc!=null){
                        ResultSet rs = ectValidation.getValidationType(measurementType, mInstrc);                        
                        String regExp = null;
                        double dMax = 0;
                        double dMin = 0;
                        try{
                            if (rs.next()){
                                dMax = rs.getDouble("maxValue");
                                dMin = rs.getDouble("minValue");
                                regExp = rs.getString("regularExp");
                            }
                            
                            if(!ectValidation.isInRange(dMax, dMin, guideline)){                       
                                errors.add(guideline, new ActionError("errors.range", measurementType, 
                                           Double.toString(dMin), Double.toString(dMax)));
                                saveErrors(request, errors);
                                valid = false;                               
                            }                            
                            else if(!ectValidation.matchRegExp(regExp, guideline)){                        
                                errors.add(guideline,
                                new ActionError("errors.invalid", measurementType));
                                saveErrors(request, errors);
                                valid = false;
                            }
                            else if(!ectValidation.isValidBloodPressure(regExp, guideline)){                        
                                errors.add(guideline,
                                new ActionError("error.bloodPressure"));
                                saveErrors(request, errors);
                                valid = false;
                            }
                        }
                        catch(SQLException e)
                        {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        }
        return valid;
    }           
     /*****************************************************************************************
     * get the number of Patient Seen during a specific time period
     *
     * @return ArrayList which contain the result in String format
     ******************************************************************************************/    
    private ArrayList getNbPatientSeen(DBHandler db, RptInitializePatientsMetGuidelineCDMReportForm frm, ArrayList messages, HttpServletRequest request){
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
        return messages;
    }

     /*****************************************************************************************
     * get the number of Patient met the specific guideline during aspecific time period
     *
     * @return ArrayList which contain the result in String format
     ******************************************************************************************/  
    private ArrayList getMetGuidelinePercentage(DBHandler db, RptInitializePatientsMetGuidelineCDMReportForm frm, ArrayList metGLPercentageMsg, HttpServletRequest request){
        String[] startDateB = frm.getStartDateB();
        String[] endDateB = frm.getEndDateB(); 
        String[] idB = frm.getIdB();
        String[] guidelineB = frm.getGuidelineB();        
        String[] guidelineCheckbox = frm.getGuidelineCheckbox();
        RptCheckGuideline checkGuideline = new RptCheckGuideline();
        MessageResources mr = getResources(request);
        
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
                    String sql = "";
                    ResultSet rs;
                    int iNumMInstrc = Integer.parseInt(sNumMInstrc);                    
                    double metGLPercentage = 0;
                    double nbMetGL = 0;
                    
                    for(int j=0; j<iNumMInstrc; j++){
                        metGLPercentage = 0;
                        nbMetGL = 0;
                        String mInstrc = (String) frm.getValue("mInstrcsCheckbox"+ctr+j);
                        
                        if(mInstrc!=null){  
                            
                            sql = "SELECT * FROM measurements WHERE dateObserved >='" + startDate + "'AND dateObserved <='" + endDate +
                                  "'AND type='"+ measurementType + "'AND measuringInstruction='"+ mInstrc + "'";
                            System.out.println("SQL statement is" + sql);
                            rs = db.GetSQL(sql);
                            rs.last();
                            double nbGeneral = rs.getRow();
                            rs.close();
                            
                            if (measurementType.compareTo("BP")==0){
                                sql = "SELECT * FROM measurements WHERE dateObserved >='" + startDate + "'AND dateObserved <='" + endDate
                                         + "'AND type='"+ measurementType + "'AND measuringInstruction='"+ mInstrc 
                                         + "'";
                                System.out.println("SQL statement is " + sql);
                                rs = db.GetSQL(sql);
                                while(rs.next()){
                                    if(checkGuideline.isBloodPressureMetGuideline(rs.getString("dataField"), guideline, aboveBelow)){
                                        nbMetGL++;
                                    }
                                }
                                if(nbGeneral!=0){
                                    metGLPercentage = Math.round((nbMetGL/nbGeneral) * 100);
                                }
                                String[] param = {  startDate, 
                                                    endDate,
                                                    measurementType,
                                                    mInstrc,
                                                    Double.toString(metGLPercentage),
                                                    aboveBelow,
                                                    guideline};
                                String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsMetGuideline", param);    
                                System.out.println(msg);
                                metGLPercentageMsg.add(msg);           
                            }
                            else if (checkGuideline.getValidation(db, measurementType)==1)
                            {
                                sql = "SELECT * FROM measurements WHERE dateObserved >='" + startDate + "'AND dateObserved <='" + endDate
                                         + "'AND type='"+ measurementType + "'AND measuringInstruction='"+ mInstrc 
                                         + "' AND dataField" + aboveBelow + "'" + guideline + "'";
                                System.out.println("SQL statement is " + sql);
                                rs = db.GetSQL(sql);
                                rs.last();
                                nbMetGL = rs.getRow();
                                if(nbGeneral!=0){
                                    metGLPercentage = Math.round((nbMetGL/nbGeneral) * 100);
                                }
                                String[] param = {  startDate, 
                                                    endDate,
                                                    measurementType,
                                                    mInstrc,
                                                    Double.toString(metGLPercentage),
                                                    aboveBelow,
                                                    guideline};
                                String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsMetGuideline", param);                                 
                                System.out.println(msg);
                                metGLPercentageMsg.add(msg);           
                            }
                            else{
                                sql = "SELECT * FROM measurements WHERE dateObserved >='" + startDate + "'AND dateObserved <='" + endDate
                                         + "'AND type='"+ measurementType + "'AND measuringInstruction='"+ mInstrc 
                                         + "'";
                                System.out.println("SQL statement is " + sql);
                                rs = db.GetSQL(sql);
                                while(rs.next()){
                                    if(checkGuideline.isYesNoMetGuideline(rs.getString("dataField"), guideline)){
                                        nbMetGL++;
                                    }
                                }
                                if(nbGeneral!=0){
                                    metGLPercentage = Math.round((nbMetGL/nbGeneral) * 100);
                                }
                                String[] param = {  startDate,
                                                    endDate,
                                                    measurementType,
                                                    mInstrc,                                                
                                                    guideline,
                                                    Double.toString(metGLPercentage)};
                                String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsIs", param); 
                                System.out.println(msg);
                                metGLPercentageMsg.add(msg);           
                            }
                            rs.close();                                                                   
                        }
                    }
                    
                    //percentage of patients who meet guideline for the same test with all measuring instruction
                    
                        metGLPercentage = 0;
                        nbMetGL = 0;
                        sql = "SELECT * FROM measurements WHERE dateObserved >='" + startDate + "'AND dateObserved <='" + endDate 
                              + "'AND type='"+ measurementType + "'";
                        System.out.println("SQL statement is" + sql);
                        rs = db.GetSQL(sql);
                        rs.last();
                        double nbGeneral = rs.getRow();
                        rs.close();
                        
                        if (measurementType.compareTo("BP")==0){
                            sql = "SELECT * FROM measurements WHERE dateObserved >='" + startDate + "'AND dateObserved <='" + endDate
                                     + "'AND type='"+ measurementType + "'";
                            System.out.println("SQL statement is " + sql);
                            rs = db.GetSQL(sql);
                            while(rs.next()){
                                if(checkGuideline.isBloodPressureMetGuideline(rs.getString("dataField"), guideline, aboveBelow)){
                                    nbMetGL++;
                                }
                            }                                                        

                        if(nbGeneral!=0){
                                metGLPercentage = Math.round((nbMetGL/nbGeneral) * 100);
                            }
                            String[] param = {  startDate, 
                                                endDate,
                                                measurementType,
                                                "",
                                                Double.toString(metGLPercentage),
                                                aboveBelow,
                                                guideline};
                            String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsMetGuideline", param); 
                            System.out.println(msg);
                            metGLPercentageMsg.add(msg); 
                            metGLPercentageMsg.add(""); 
                        }
                        else if (checkGuideline.getValidation(db, measurementType)==1)
                        {
                            sql = "SELECT * FROM measurements WHERE dateObserved >='" + startDate + "'AND dateObserved <='" + endDate
                                     + "'AND type='"+ measurementType + "' AND dataField" + aboveBelow + "'" + guideline + "'";
                            System.out.println("SQL statement is " + sql);
                            rs = db.GetSQL(sql);
                            rs.last();
                            nbMetGL = rs.getRow();
                            

                            if(nbGeneral!=0){
                                metGLPercentage = Math.round((nbMetGL/nbGeneral) * 100);
                            }
                            String[] param = {  startDate, 
                                                endDate,
                                                measurementType,
                                                "",
                                                Double.toString(metGLPercentage),
                                                aboveBelow,
                                                guideline};
                            String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsMetGuideline", param); 
                            System.out.println(msg);
                            metGLPercentageMsg.add(msg); 
                            metGLPercentageMsg.add(""); 
                        }
                        
                        rs.close();
                        
                        
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

    

    /*****************************************************************************************
     * get the number of Patient met all the selected guideline during aspecific time period
     *
     * @return ArrayList which contain the result in String format
     ******************************************************************************************/     
    private ArrayList getPatientsMetAllSelectedGuideline(DBHandler db, RptInitializePatientsMetGuidelineCDMReportForm frm, ArrayList reportMsg, HttpServletRequest request){       
        String[] guidelineCheckbox = frm.getGuidelineCheckbox();
        String[] guidelineB = frm.getGuidelineB();        
        String startDate = frm.getStartDateA();
        String endDate = frm.getEndDateA();
        RptCheckGuideline checkGuideline = new RptCheckGuideline();
        ArrayList patients = checkGuideline.getPatients(db, startDate, endDate);
        int nbPatients = patients.size();
        double nbPatientsDoneAllTest = 0;
        double nbPatientsPassAllTest = 0;
        double passAllTestsPercentage = 0;
        MessageResources mr = getResources(request);
        
        if (guidelineCheckbox!=null){
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
                    System.out.println("testDone size: "+ testsDone.size());
                    if(guidelineCheckbox.length<=testsDone.size()){

                        for(int j=0; j<guidelineCheckbox.length; j++){ 

                            int ctr = Integer.parseInt(guidelineCheckbox[j]);
                            String guideline = guidelineB[ctr];
                            String measurementType = (String) frm.getValue("measurementType"+ctr);
                            String aboveBelow = (String) frm.getValue("aboveBelow"+ctr);
                            System.out.println("guideline: " + guideline);
                            for(int k=0; k<testsDone.size(); k++){  
                                doneAllTests=false;
                                String testDone = (String) testsDone.get(k);                            
                                System.out.println("testdone: " + testDone);                           
                                if(measurementType.compareTo(testDone)==0){                                 

                                    if(checkGuideline.getValidation(db, measurementType)==1){ 
                                        passAllTests = checkGuideline.isNumericValueMetGuideline(Double.parseDouble((String) data.get(k)), guideline, aboveBelow);                                    
                                    }
                                    else if(measurementType.compareTo("BP") == 0){
                                        passAllTests = checkGuideline.isBloodPressureMetGuideline((String) data.get(k), guideline, aboveBelow);                  
                                    }
                                    else{
                                        passAllTests = checkGuideline.isYesNoMetGuideline((String) data.get(k), guideline);
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
                        if(passAllTests){
                            nbPatientsPassAllTest++;
                        }
                    }
                }

                if(nbPatientsDoneAllTest!=0){
                    passAllTestsPercentage = Math.round( (nbPatientsPassAllTest/nbPatientsDoneAllTest) * 100);
                }
               
                String[] param = {  startDate, 
                                    endDate,
                                    Double.toString(nbPatientsDoneAllTest),
                                    Double.toString(passAllTestsPercentage)};
                String msg = mr.getMessage("oscarReport.CDMReport.msgPassAllSeletectedTest", param);                 
                System.out.println(msg); 

                reportMsg.add(msg);
                reportMsg.add("");
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
        
        return reportMsg;
    }
}
