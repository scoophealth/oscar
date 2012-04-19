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


package oscar.oscarReport.oscarMeasurements.pageUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarMeasurements.pageUtil.EctValidation;
import oscar.oscarReport.oscarMeasurements.data.RptMeasurementsData;


public class RptInitializePatientsMetGuidelineCDMReportAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
 
        RptInitializePatientsMetGuidelineCDMReportForm frm = (RptInitializePatientsMetGuidelineCDMReportForm) form;                       
        request.getSession().setAttribute("RptInitializePatientsMetGuidelineCDMReportForm", frm);        
        MessageResources mr = getResources(request);
        RptMeasurementsData mData = new RptMeasurementsData();
        String[] patientSeenCheckbox = frm.getPatientSeenCheckbox();
        String startDateA = frm.getStartDateA();
        String endDateA = frm.getEndDateA();
        
        ArrayList reportMsg = new ArrayList();
        
        try{
                
                if(!validate(frm, request)){
                    MiscUtils.getLogger().debug("the form is invalid");
                    return (new ActionForward(mapping.getInput()));
                }
                
                if(patientSeenCheckbox!=null){
                    int nbPatient = mData.getNbPatientSeen(startDateA, endDateA);  
                    String msg = mr.getMessage("oscarReport.CDMReport.msgPatientSeen", Integer.toString(nbPatient), startDateA, endDateA); 
                    MiscUtils.getLogger().debug(msg);
                    reportMsg.add(msg);
                    reportMsg.add("");
                }
                
                getMetGuidelinePercentage(frm, reportMsg, request);
                //getPatientsMetAllSelectedGuideline(db, frm, reportMsg, request);
               
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
                
        }
        
        catch(SQLException e)
        {
            MiscUtils.getLogger().error("Error", e);
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
        ActionMessages errors = new ActionMessages();        
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
                    errors.add(startDate, new ActionMessage("errors.invalidDate", measurementType));
                    saveErrors(request, errors);
                    valid = false;
                }
                if(!ectValidation.isDate(endDate)){                       
                    errors.add(endDate, new ActionMessage("errors.invalidDate", measurementType));
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
                                errors.add(guideline, new ActionMessage("errors.range", measurementType, 
                                           Double.toString(dMin), Double.toString(dMax)));
                                saveErrors(request, errors);
                                valid = false;                               
                            }                            
                            else if(!ectValidation.matchRegExp(regExp, guideline)){                        
                                errors.add(guideline,
                                new ActionMessage("errors.invalid", measurementType));
                                saveErrors(request, errors);
                                valid = false;
                            }
                            else if(!ectValidation.isValidBloodPressure(regExp, guideline)){                        
                                errors.add(guideline,
                                new ActionMessage("error.bloodPressure"));
                                saveErrors(request, errors);
                                valid = false;
                            }
                        }
                        catch(SQLException e)
                        {
                            MiscUtils.getLogger().error("Error", e);
                        }
                    }
                }
            }
        }
        return valid;
    }           
 

     /*****************************************************************************************
     * get the number of Patient met the specific guideline during aspecific time period
     *
     * @return ArrayList which contain the result in String format
     ******************************************************************************************/  
    private ArrayList getMetGuidelinePercentage(RptInitializePatientsMetGuidelineCDMReportForm frm, ArrayList metGLPercentageMsg, HttpServletRequest request){
        String[] startDateB = frm.getStartDateB();
        String[] endDateB = frm.getEndDateB(); 
        String[] idB = frm.getIdB();
        String[] guidelineB = frm.getGuidelineB();        
        String[] guidelineCheckbox = frm.getGuidelineCheckbox();
        RptCheckGuideline checkGuideline = new RptCheckGuideline();
        MessageResources mr = getResources(request);
        
        if (guidelineCheckbox!=null){
            try{
                MiscUtils.getLogger().debug("the length of guideline checkbox is "  + guidelineCheckbox.length);
                for(int i=0; i<guidelineCheckbox.length; i++){
                    int ctr = Integer.parseInt(guidelineCheckbox[i]);
                    MiscUtils.getLogger().debug("the value of guildline Checkbox is: " + guidelineCheckbox[i]);
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
                            sql = "SELECT demographicNo, max(dateEntered) FROM measurements WHERE dateObserved >='" + startDate + "' AND dateObserved <='" + endDate
                                 + "' AND type='"+ measurementType + "'AND measuringInstruction='"+ mInstrc 
                                 + "' group by demographicNo";
                            MiscUtils.getLogger().debug("SQL statement is" + sql);
                            rs = DBHandler.GetSQL(sql);                            
                            double nbGeneral = 0;                            
                                                        
                            if (measurementType.compareTo("BP")==0){
                                                                
                                MiscUtils.getLogger().debug("SQL statement is " + sql);
                                rs = DBHandler.GetSQL(sql);
                                while(rs.next()){
                                    sql =   "SELECT dataField FROM measurements WHERE dateEntered = '" + rs.getString("max(dateEntered)") + 
                                            "' AND demographicNo = '" + rs.getString("demographicNo") 
                                            + "' AND type='"+ measurementType + "'AND measuringInstruction='"+ mInstrc + "'";
                                    ResultSet rsData =  DBHandler.GetSQL(sql);
                                    if (rsData.next()){
                                        if(checkGuideline.isBloodPressureMetGuideline(rsData.getString("dataField"), guideline, aboveBelow)){
                                            nbMetGL++;
                                        }
                                    }
                                    nbGeneral++;
                                }
                                if(nbGeneral!=0){
                                    metGLPercentage = Math.round((nbMetGL/nbGeneral) * 100);
                                }
                                String[] param = {  startDate, 
                                                    endDate,
                                                    measurementType,
                                                    mInstrc,
                                                    "("+nbMetGL+"/"+ nbGeneral+") "+Double.toString(metGLPercentage),
                                                    aboveBelow,
                                                    guideline};
                                String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsMetGuideline", param);    
                                MiscUtils.getLogger().debug(msg);
                                metGLPercentageMsg.add(msg);           
                            }
                            else if (checkGuideline.getValidation(measurementType)==1)
                            {
                                while(rs.next()){
                                    sql =   "SELECT dataField FROM measurements WHERE dateEntered = '" + rs.getString("max(dateEntered)") 
                                            + "' AND demographicNo = '" + rs.getString("demographicNo") + "' AND type='"+ measurementType + "' AND measuringInstruction='"+ mInstrc
                                            + "' AND dataField" + aboveBelow + "'" + guideline + "'";
                                    ResultSet rsData = DBHandler.GetSQL(sql);
                                    rsData.last();
                                    if(rsData.getRow()>0)
                                        nbMetGL++;
                                    nbGeneral++;
                                }
                                                                
                                if(nbGeneral!=0){
                                    metGLPercentage = Math.round((nbMetGL/nbGeneral) * 100);
                                }
                                String[] param = {  startDate, 
                                                    endDate,
                                                    measurementType,
                                                    mInstrc,
                                                    "("+nbMetGL+"/"+ nbGeneral+") "+Double.toString(metGLPercentage),
                                                    aboveBelow,
                                                    guideline};
                                                    
                                String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsMetGuideline", param);                                 
                                MiscUtils.getLogger().debug(msg);
                                metGLPercentageMsg.add(msg);           
                            }
                            else{                                
                                while(rs.next()){
                                    sql =   "SELECT dataField FROM measurements WHERE dateEntered = '" + rs.getString("max(dateEntered)") + 
                                            "' AND demographicNo = '" + rs.getString("demographicNo") 
                                            + "' AND type='"+ measurementType + "'AND measuringInstruction='"+ mInstrc + "'";
                                    ResultSet rsData =  DBHandler.GetSQL(sql);
                                    if (rsData.next()){
                                        if(checkGuideline.isYesNoMetGuideline(rsData.getString("dataField"), guideline)){
                                            nbMetGL++;
                                        }
                                    }
                                    nbGeneral++;
                                }
                                if(nbGeneral!=0){
                                    metGLPercentage = Math.round((nbMetGL/nbGeneral) * 100);
                                }
                                String[] param = {  startDate,
                                                    endDate,
                                                    measurementType,
                                                    mInstrc,                                                
                                                    guideline,
                                                    "("+nbMetGL+"/"+ nbGeneral+") "+Double.toString(metGLPercentage)};
                                String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsIs", param); 
                                MiscUtils.getLogger().debug(msg);
                                metGLPercentageMsg.add(msg);           
                            }
                            rs.close();                                                                   
                        }
                    }
                    
                    //percentage of patients who meet guideline for the same test with all measuring instruction
                    
                        metGLPercentage = 0;
                        nbMetGL = 0;
                        sql = "SELECT demographicNo, max(dateEntered) FROM measurements WHERE dateObserved >='" + startDate + "'AND dateObserved <='" + endDate
                                 + "' AND type='"+ measurementType +  "' group by demographicNo";
                            MiscUtils.getLogger().debug("SQL statement is" + sql);
                            rs = DBHandler.GetSQL(sql);                            
                            double nbGeneral = 0;                            
                                                        
                            if (measurementType.compareTo("BP")==0){
                                                                
                                MiscUtils.getLogger().debug("SQL statement is " + sql);
                                rs = DBHandler.GetSQL(sql);
                                while(rs.next()){
                                    sql =   "SELECT dataField FROM measurements WHERE dateEntered = '" + rs.getString("max(dateEntered)") + 
                                            "' AND demographicNo = '" + rs.getString("demographicNo") + "' AND type='"+ measurementType + "'";
                                    ResultSet rsData =  DBHandler.GetSQL(sql);
                                    if (rsData.next()){
                                        if(checkGuideline.isBloodPressureMetGuideline(rsData.getString("dataField"), guideline, aboveBelow)){
                                            nbMetGL++;
                                        }
                                    }
                                    nbGeneral++;
                                }
                                if(nbGeneral!=0){
                                    metGLPercentage = Math.round((nbMetGL/nbGeneral) * 100);
                                }
                                String[] param = {  startDate, 
                                                    endDate,
                                                    measurementType,
                                                    "",
                                                    "("+nbMetGL+"/"+ nbGeneral+") "+Double.toString(metGLPercentage),
                                                    aboveBelow,
                                                    guideline};
                                String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsMetGuideline", param);    
                                MiscUtils.getLogger().debug(msg);
                                metGLPercentageMsg.add(msg);           
                            }
                            else if (checkGuideline.getValidation(measurementType)==1)
                            {
                                while(rs.next()){
                                    sql =   "SELECT dataField FROM measurements WHERE dateEntered = '" + rs.getString("max(dateEntered)") 
                                            + "' AND demographicNo = '" + rs.getString("demographicNo") + "' AND type='"+ measurementType
                                            + "' AND dataField" + aboveBelow + "'" + guideline + "'";
                                    ResultSet rsData = DBHandler.GetSQL(sql);
                                    rsData.last();
                                    if(rsData.getRow()>0)
                                        nbMetGL++;
                                    nbGeneral++;
                                }
                                                                
                                if(nbGeneral!=0){
                                    metGLPercentage = Math.round((nbMetGL/nbGeneral) * 100);
                                }
                                String[] param = {  startDate, 
                                                    endDate,
                                                    measurementType,
                                                    "",
                                                    "("+nbMetGL+"/"+ nbGeneral+") "+Double.toString(metGLPercentage),
                                                    aboveBelow,
                                                    guideline};
                                String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsMetGuideline", param);                                 
                                MiscUtils.getLogger().debug(msg);
                                metGLPercentageMsg.add(msg);           
                            }
                            else{                                
                                while(rs.next()){
                                    sql =   "SELECT dataField FROM measurements WHERE dateEntered = '" + rs.getString("max(dateEntered)") + 
                                            "' AND demographicNo = '" + rs.getString("demographicNo") + "' AND type='"+ measurementType + "'";
                                    ResultSet rsData =  DBHandler.GetSQL(sql);
                                    if (rsData.next()){
                                        if(checkGuideline.isYesNoMetGuideline(rsData.getString("dataField"), guideline)){
                                            nbMetGL++;
                                        }
                                    }
                                    nbGeneral++;
                                }
                                if(nbGeneral!=0){
                                    metGLPercentage = Math.round((nbMetGL/nbGeneral) * 100);
                                }
                                String[] param = {  startDate,
                                                    endDate,
                                                    measurementType,
                                                    "",                                                
                                                    guideline,
                                                    "("+nbMetGL+"/"+ nbGeneral+") "+Double.toString(metGLPercentage)};
                                String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsIs", param); 
                                MiscUtils.getLogger().debug(msg);
                                metGLPercentageMsg.add(msg);           
                            }
                            rs.close();                                                                                           
                        
                }
            }
            catch(SQLException e)
            {
                MiscUtils.getLogger().error("Error", e);
            }
        }
        else{
            MiscUtils.getLogger().debug("guideline checkbox is null");
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
        RptMeasurementsData mData = new RptMeasurementsData();
        ArrayList patients = mData.getPatientsSeen(startDate, endDate);
        int nbPatients = patients.size();
        double nbPatientsDoneAllTest = 0;
        double nbPatientsPassAllTest = 0;
        double passAllTestsPercentage = 0;
        MessageResources mr = getResources(request);
        
        
        if (guidelineCheckbox!=null){
            try{
                MiscUtils.getLogger().debug("Number of Patients: " + nbPatients);
                for(int i=0; i<nbPatients; i++){

                    String patient = (String) patients.get(i);
                    boolean passAllTests = false;
                    boolean doneAllTests = false;
                    ArrayList testsDone = new ArrayList();
                    ArrayList mInstrc = new ArrayList();
                    ArrayList data = new ArrayList();
                    
                    ResultSet rs;
                    ResultSet rsData;
                    
                    String sql = "SELECT DISTINCT type, measuringInstruction FROM measurements WHERE demographicNo='" + patient + "'";                              

                    for(rs=DBHandler.GetSQL(sql);rs.next(); ){ 
                        sql =   "SELECT dataField FROM measurements WHERE demographicNo='"+patient+"' AND type='"+ rs.getString("type") 
                                + "' ORDER BY dateEntered DESC LIMIT 1";
                        rsData = DBHandler.GetSQL(sql);
                        if(rsData.next()){
                            testsDone.add(rs.getString("type"));
                            data.add(rsData.getString("dataField"));
                        }
                    }                
                    MiscUtils.getLogger().debug("guidelineCheckbox length: " + guidelineCheckbox.length);
                    MiscUtils.getLogger().debug("testDone size: "+ testsDone.size());
                    if(guidelineCheckbox.length<=testsDone.size()){

                        for(int j=0; j<guidelineCheckbox.length; j++){ 

                            int ctr = Integer.parseInt(guidelineCheckbox[j]);
                            String guideline = guidelineB[ctr];
                            String measurementType = (String) frm.getValue("measurementType"+ctr);
                            String aboveBelow = (String) frm.getValue("aboveBelow"+ctr);
                            MiscUtils.getLogger().debug("guideline: " + guideline);
                            for(int k=0; k<testsDone.size(); k++){  
                                doneAllTests=false;
                                String testDone = (String) testsDone.get(k);                            
                                MiscUtils.getLogger().debug("testdone: " + testDone);                           
                                if(measurementType.compareTo(testDone)==0){                                 

                                    if(checkGuideline.getValidation(measurementType)==1){ 
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
                        MiscUtils.getLogger().debug("doneAllTest is true" );
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
                MiscUtils.getLogger().debug(msg); 

                reportMsg.add(msg);
                reportMsg.add("");
            }
            catch(SQLException e)
            {
                MiscUtils.getLogger().error("Error", e);
            }
        }
        
        return reportMsg;
    }
}
