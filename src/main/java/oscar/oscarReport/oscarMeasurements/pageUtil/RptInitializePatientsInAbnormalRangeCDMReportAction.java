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


public class RptInitializePatientsInAbnormalRangeCDMReportAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
 
        RptInitializePatientsInAbnormalRangeCDMReportForm frm = (RptInitializePatientsInAbnormalRangeCDMReportForm) form;                       
        request.getSession().setAttribute("RptInitializePatientsInAbnormalRangeCDMReportForm", frm);        
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
                
                getInAbnormalRangePercentage(frm, reportMsg, request);                
                String title = mr.getMessage("oscarReport.CDMReport.msgPercentageOfPatientInAbnormalRange");
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
    private boolean validate(RptInitializePatientsInAbnormalRangeCDMReportForm frm, HttpServletRequest request){
        EctValidation ectValidation = new EctValidation();                    
        ActionMessages errors = new ActionMessages();        
        String[] startDateC = frm.getStartDateC();
        String[] endDateC = frm.getEndDateC();         
        String[] upperBound = frm.getUpperBound(); 
        String[] lowerBound = frm.getLowerBound();         
        String[] abnormalCheckbox = frm.getAbnormalCheckbox();
        boolean valid = true;
        
        if (abnormalCheckbox!=null){
       
            for(int i=0; i<abnormalCheckbox.length; i++){
                int ctr = Integer.parseInt(abnormalCheckbox[i]);                
                String startDate = startDateC[ctr];
                String endDate = endDateC[ctr];                    
                String upper = upperBound[ctr];
                String lower = lowerBound[ctr];
                String measurementType = (String) frm.getValue("measurementTypeC"+ctr);                    
                String sNumMInstrc = (String) frm.getValue("mNbInstrcsC"+ctr);
                int iNumMInstrc = Integer.parseInt(sNumMInstrc);                                                        
                String upperMsg = "The upper bound value of "+ measurementType;
                String lowerMsg = "The lower bound value of " + measurementType;
                
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
                    
                    String mInstrc = (String) frm.getValue("mInstrcsCheckboxC"+ctr+j);
                    if(mInstrc!=null){
                        ResultSet rs = ectValidation.getValidationType(measurementType, mInstrc);
                        String msg = null;
                        String regExp = null;
                        double dMax = 0;
                        double dMin = 0;
                        try{
                            if (rs.next()){
                                dMax = rs.getDouble("maxValue");
                                dMin = rs.getDouble("minValue");
                                regExp = rs.getString("regularExp");
                            }
                            
                            if(!ectValidation.isInRange(dMax, dMin, upper)){                       
                                errors.add(upper, new ActionMessage("errors.range", upperMsg, 
                                           Double.toString(dMin), Double.toString(dMax)));
                                saveErrors(request, errors);
                                valid = false;                               
                            }
                            else if(!ectValidation.isInRange(dMax, dMin, lower)){                       
                                errors.add(lower, new ActionMessage("errors.range", lowerMsg, 
                                           Double.toString(dMin), Double.toString(dMax)));
                                saveErrors(request, errors);
                                valid = false;
                            }
                            else if(!ectValidation.matchRegExp(regExp, upper)){                        
                                errors.add(upper,
                                new ActionMessage("errors.invalid", upperMsg));
                                saveErrors(request, errors);
                                valid = false;
                            }
                            else if(!ectValidation.matchRegExp(regExp, lower)){                        
                                errors.add(lower,
                                new ActionMessage("errors.invalid", lowerMsg));
                                saveErrors(request, errors);
                                valid = false;
                            }
                            else if(!ectValidation.isValidBloodPressure(regExp, upper)){                        
                                errors.add(upper,
                                new ActionMessage("error.bloodPressure"));
                                saveErrors(request, errors);
                                valid = false;
                            }
                            else if(!ectValidation.isValidBloodPressure(regExp, lower)){                        
                                errors.add(lower,
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
     * get the number of Patient in the abnormal range during aspecific time period
     *
     * @return ArrayList which contain the result in String format
     ******************************************************************************************/      
    private ArrayList getInAbnormalRangePercentage(RptInitializePatientsInAbnormalRangeCDMReportForm frm, ArrayList metGLPercentageMsg, HttpServletRequest request){
        String[] startDateC = frm.getStartDateC();
        String[] endDateC = frm.getEndDateC();         
        String[] upperBound = frm.getUpperBound(); 
        String[] lowerBound = frm.getLowerBound();         
        String[] abnormalCheckbox = frm.getAbnormalCheckbox();        
        RptCheckGuideline checkGuideline = new RptCheckGuideline();
        MessageResources mr = getResources(request);
        
        if (abnormalCheckbox!=null){
            try{
                MiscUtils.getLogger().debug("the length of abnormal range checkbox is "  + abnormalCheckbox.length);

                for(int i=0; i<abnormalCheckbox.length; i++){
                    int ctr = Integer.parseInt(abnormalCheckbox[i]);
                    MiscUtils.getLogger().debug("the value of abnormal range Checkbox is: " + abnormalCheckbox[i]);
                    String startDate = startDateC[ctr];
                    String endDate = endDateC[ctr];                    
                    String upper = upperBound[ctr];
                    String lower = lowerBound[ctr];
                    String measurementType = (String) frm.getValue("measurementTypeC"+ctr);                    
                    String sNumMInstrc = (String) frm.getValue("mNbInstrcsC"+ctr);
                    int iNumMInstrc = Integer.parseInt(sNumMInstrc);                                        
                    double nbMetGL = 0;                    
                    double metGLPercentage = 0;
                    ResultSet rs;
                    String sql = "";
                    
                    
                    for(int j=0; j<iNumMInstrc; j++){
                        metGLPercentage = 0;                        
                        nbMetGL = 0;
                        
                        String mInstrc = (String) frm.getValue("mInstrcsCheckboxC"+ctr+j);
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
                                        if(checkGuideline.isBloodPressureMetGuideline(rsData.getString("dataField"), upper, "<") &&
                                        checkGuideline.isBloodPressureMetGuideline(rsData.getString("dataField"), lower, ">")){
                                            nbMetGL++;
                                        }
                                    }
                                    nbGeneral++;
                                }                                
                                if(nbGeneral!=0){
                                    MiscUtils.getLogger().debug("the total number of patients seen: " + nbGeneral + " nb of them pass the test: " + nbMetGL);
                                    metGLPercentage = Math.round(nbMetGL/nbGeneral* 100);
                                }                                
                                String[] param = {  startDate, 
                                                    endDate,
                                                    measurementType,
                                                    mInstrc,
                                                    lower,
                                                    upper,
                                                    "("+nbMetGL+"/"+ nbGeneral+")"+Double.toString(metGLPercentage)};
                                String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsInAbnormalRange", param);                                 
                                MiscUtils.getLogger().debug(msg);
                                metGLPercentageMsg.add(msg); 
                            }
                            else if (checkGuideline.getValidation(measurementType)==1)
                            {
                                while(rs.next()){
                                    sql =   "SELECT dataField FROM measurements WHERE dateEntered = '" + rs.getString("max(dateEntered)") 
                                            + "' AND demographicNo = '" + rs.getString("demographicNo") + "' AND type='"+ measurementType + "' AND measuringInstruction='"+ mInstrc
                                            + "' AND dataField <" + "'" + upper + "' AND dataField >" + "'" + lower + "'";
                                    ResultSet rsData = DBHandler.GetSQL(sql);
                                    rsData.last();
                                    if(rsData.getRow()>0)
                                        nbMetGL++;
                                    nbGeneral++;
                                }
                                
                                if(nbGeneral!=0){
                                    metGLPercentage = Math.round(nbMetGL/nbGeneral* 100);
                                }                                
                                String[] param = {  startDate, 
                                                    endDate,
                                                    measurementType,
                                                    mInstrc,
                                                    lower,
                                                    upper,
                                                    "("+nbMetGL+"/"+ nbGeneral+")"+Double.toString(metGLPercentage)};
                                String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsInAbnormalRange", param); 
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
                                        if(checkGuideline.isYesNoMetGuideline(rsData.getString("dataField"), upper)){
                                            nbMetGL++;
                                        }
                                    }
                                    nbGeneral++;
                                }                                
                                if(nbGeneral!=0){
                                    metGLPercentage = Math.round(nbMetGL/nbGeneral* 100);
                                }
                                String[] param = {  startDate,
                                                    endDate,
                                                    measurementType,
                                                    mInstrc,                                                
                                                    upper,
                                                    "("+nbMetGL+"/"+ nbGeneral+")"+Double.toString(metGLPercentage)};
                                String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsIs", param); 
                                MiscUtils.getLogger().debug(msg);
                                metGLPercentageMsg.add(msg); 
                            }
                                                                                                                
                            rs.close();
                            

                                                
                        }
                    }
                    
                    //percentage of patients who are in abnormal range for the same test with all measuring instruction
                    metGLPercentage = 0; 
                    nbMetGL = 0;

                    sql = "SELECT demographicNo, max(dateEntered) FROM measurements WHERE dateObserved >='" + startDate + "' AND dateObserved <='" + endDate
                     + "' AND type='"+ measurementType + "' group by demographicNo";
                    MiscUtils.getLogger().debug("SQL statement is" + sql);
                    rs = DBHandler.GetSQL(sql);                            
                    double nbGeneral = 0;   

                    if (measurementType.compareTo("BP")==0){                                
                        MiscUtils.getLogger().debug("SQL statement is " + sql);
                        rs = DBHandler.GetSQL(sql);
                        while(rs.next()){
                            sql =   "SELECT dataField FROM measurements WHERE dateEntered = '" + rs.getString("max(dateEntered)") + 
                                    "' AND demographicNo = '" + rs.getString("demographicNo") 
                                    + "' AND type='"+ measurementType + "'";
                            ResultSet rsData =  DBHandler.GetSQL(sql);
                            if (rsData.next()){
                                if(checkGuideline.isBloodPressureMetGuideline(rsData.getString("dataField"), upper, "<") &&
                                checkGuideline.isBloodPressureMetGuideline(rsData.getString("dataField"), lower, ">")){
                                    nbMetGL++;
                                }
                            }
                            nbGeneral++;
                        }                                
                        if(nbGeneral!=0){
                            MiscUtils.getLogger().debug("the total number of patients seen: " + nbGeneral + " nb of them pass the test: " + nbMetGL);
                            metGLPercentage = Math.round(nbMetGL/nbGeneral* 100);
                        }                                
                        String[] param = {  startDate, 
                                            endDate,
                                            measurementType,
                                            "",
                                            lower,
                                            upper,
                                            "("+nbMetGL+"/"+ nbGeneral+")"+Double.toString(metGLPercentage)};
                        String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsInAbnormalRange", param);                                 
                        MiscUtils.getLogger().debug(msg);
                        metGLPercentageMsg.add(msg); 
                    }
                    else if (checkGuideline.getValidation(measurementType)==1)
                    {
                        while(rs.next()){
                            sql =   "SELECT dataField FROM measurements WHERE dateEntered = '" + rs.getString("max(dateEntered)") 
                                    + "' AND demographicNo = '" + rs.getString("demographicNo") + "' AND type='"+ measurementType 
                                    + "' AND dataField <" + "'" + upper + "' AND dataField >" + "'" + lower + "'";
                            ResultSet rsData = DBHandler.GetSQL(sql);
                            rsData.last();
                            if(rsData.getRow()>0)
                                nbMetGL++;
                            nbGeneral++;
                        }

                        if(nbGeneral!=0){
                            metGLPercentage = Math.round(nbMetGL/nbGeneral* 100);
                        }                                
                        String[] param = {  startDate, 
                                            endDate,
                                            measurementType,
                                            "",
                                            lower,
                                            upper,
                                            "("+nbMetGL+"/"+ nbGeneral+")"+Double.toString(metGLPercentage)};
                        String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsInAbnormalRange", param); 
                        MiscUtils.getLogger().debug(msg);
                        metGLPercentageMsg.add(msg); 
                    }
                    else{
                        while(rs.next()){
                            sql =   "SELECT dataField FROM measurements WHERE dateEntered = '" + rs.getString("max(dateEntered)") + 
                                    "' AND demographicNo = '" + rs.getString("demographicNo") 
                                    + "' AND type='"+ measurementType + "'";
                            ResultSet rsData =  DBHandler.GetSQL(sql);
                            if (rsData.next()){
                                if(checkGuideline.isYesNoMetGuideline(rsData.getString("dataField"), upper)){
                                    nbMetGL++;
                                }
                            }
                            nbGeneral++;
                        }                                
                        if(nbGeneral!=0){
                            metGLPercentage = Math.round(nbMetGL/nbGeneral* 100);
                        }
                        String[] param = {  startDate,
                                            endDate,
                                            measurementType,
                                            "",                                                
                                            upper,
                                            "("+nbMetGL+"/"+ nbGeneral+")"+Double.toString(metGLPercentage)};
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
             
}
