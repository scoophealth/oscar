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


public class RptInitializeFrequencyOfRelevantTestsCDMReportAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
 
        RptInitializeFrequencyOfRelevantTestsCDMReportForm frm = (RptInitializeFrequencyOfRelevantTestsCDMReportForm) form;                       
        request.getSession().setAttribute("RptInitializeFrequencyOfRelevantTestsCDMReportForm", frm);
        MessageResources mr = getResources(request);
        ArrayList reportMsg = new ArrayList();
        ArrayList headings = new ArrayList();
        RptMeasurementsData mData = new RptMeasurementsData();
        String[] patientSeenCheckbox = frm.getPatientSeenCheckbox();
        String startDateA = frm.getStartDateA();
        String endDateA = frm.getEndDateA();
        int nbPatient = 0; 
        try{
                  
                if(!validate(frm, request)){                    
                    return (new ActionForward(mapping.getInput()));
                }
                
                
                addHeading(headings, request);
                if(patientSeenCheckbox!=null){
                    nbPatient = mData.getNbPatientSeen(startDateA, endDateA);  
                    String msg = mr.getMessage("oscarReport.CDMReport.msgPatientSeen", Integer.toString(nbPatient), startDateA, endDateA); 
                    MiscUtils.getLogger().debug(msg);
                    reportMsg.add(msg);
                    reportMsg.add("");
                }
                getFrequenceOfTestPerformed(frm, reportMsg, request);
                
                String title = mr.getMessage("oscarReport.CDMReport.msgFrequencyOfRelevantTestsBeingPerformed");
                request.setAttribute("title", title);
                //request.setAttribute("headings", headings);
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
     * add heading
     *
     * 
     ******************************************************************************************/ 
     private ArrayList addHeading(ArrayList headings, HttpServletRequest request){
         MessageResources mr = getResources(request);
         /*String hd = mr.getMessage("oscarReport.CDMReport.msgStartDate");
         MiscUtils.getLogger().debug(hd);
         headings.add(hd);
         hd = mr.getMessage("oscarReport.CDMReport.msgEndDate");
         MiscUtils.getLogger().debug(hd);
         headings.add(hd);
         hd = mr.getMessage("oscarReport.CDMReport.msgTest");
         MiscUtils.getLogger().debug(hd);
         headings.add(hd);
         hd = mr.getMessage("oscarReport.CDMReport.msgMeasuringInstruction");
         MiscUtils.getLogger().debug(hd);
         headings.add(hd);*/
         String hd = mr.getMessage("oscarReport.CDMReport.msgFrequency");
         MiscUtils.getLogger().debug(hd);
         headings.add(hd);
         hd = mr.getMessage("oscarReport.CDMReport.msgPercentage");
         MiscUtils.getLogger().debug(hd);
         headings.add(hd);         
         return headings;
     }
         
     /*****************************************************************************************
     * validate the input date
     *
     * @return boolean
     ******************************************************************************************/ 
    private boolean validate(RptInitializeFrequencyOfRelevantTestsCDMReportForm frm, HttpServletRequest request){
        EctValidation ectValidation = new EctValidation();                    
        ActionMessages errors = new ActionMessages();        
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
                    errors.add(startDate, new ActionMessage("errors.invalidDate", measurementType));
                    saveErrors(request, errors);
                    valid = false;
                }
                if(!ectValidation.isDate(endDate)){                       
                    errors.add(endDate, new ActionMessage("errors.invalidDate", measurementType));
                    saveErrors(request, errors);
                    valid = false;
                }
            }
        }
        return valid;
    }   


    
     /*****************************************************************************************
     * get the Frequence of Test Performed during aspecific time period
     *
     * @return ArrayList which contain the result in String format
     ******************************************************************************************/      
    private ArrayList getFrequenceOfTestPerformed(RptInitializeFrequencyOfRelevantTestsCDMReportForm frm, ArrayList percentageMsg, HttpServletRequest request){
        String[] startDateD = frm.getStartDateD();
        String[] endDateD = frm.getEndDateD();         
        int[] exactly = frm.getExactly(); 
        int[] moreThan = frm.getMoreThan();         
        int[] lessThan = frm.getLessThan();
        String[] frequencyCheckbox = frm.getFrequencyCheckbox();      
        MessageResources mr = getResources(request);
        RptMeasurementsData mData = new RptMeasurementsData();
        
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
                    ArrayList patients = mData.getPatientsSeen(startDate, endDate);
                    int nbPatients = patients.size();
                    
                    for(int j=0; j<iNumMInstrc; j++){

                        double exactPercentage = 0;
                        double morePercentage = 0;
                        double lessPercentage = 0;
                        int nbExact =0;
                        int nbMore =0;
                        int nbLess =0;
                        int nbTest =0;
                        
                        String mInstrc = (String) frm.getValue("mInstrcsCheckboxD"+ctr+j);
                        if(mInstrc!=null){
                            for(int k=0; k<nbPatients; k++){
                                String patient = (String) patients.get(k);                            

                                String sql = "SELECT COUNT(demographicNo) AS nbTest FROM measurements WHERE dateObserved >='" + startDate + "'AND dateObserved <='" + endDate
                                             + "'AND type='"+ measurementType + "'AND measuringInstruction='"+ mInstrc 
                                             + "' AND demographicNo=" + "'" + patient + "'";
                                
                                ResultSet rs = DBHandler.GetSQL(sql);  
                                if (rs.next())
                                    nbTest = rs.getInt("nbTest");                              
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
                                                Double.toString(nbExact) + "/" + Double.toString(nbPatients) +
                                                " (" + Double.toString(exactPercentage) + "%)",
                                                Integer.toString(exact)};
                            String msg = mr.getMessage("oscarReport.CDMReport.msgFrequencyOfRelevantTestsExact", param0);                              
                            MiscUtils.getLogger().debug(msg);
                            percentageMsg.add(msg);
                            String[] param1 = {   startDate, 
                                                endDate,
                                                measurementType,
                                                mInstrc,
                                                Double.toString(nbMore) + "/" + Double.toString(nbPatients) +
                                                " (" + Double.toString(morePercentage) + "%)",
                                                Integer.toString(more)};
                            msg = mr.getMessage("oscarReport.CDMReport.msgFrequencyOfRelevantTestsMoreThan", param1); 
                            MiscUtils.getLogger().debug(msg);
                            percentageMsg.add(msg); 
                            String[] param2 = {   startDate, 
                                                endDate,
                                                measurementType,
                                                mInstrc,
                                                Double.toString(nbLess) + "/" + Double.toString(nbPatients) +
                                                " (" + Double.toString(lessPercentage) + "%)",
                                                Integer.toString(less)};
                            msg = mr.getMessage("oscarReport.CDMReport.msgFrequencyOfRelevantTestsLessThan", param2); 
                            MiscUtils.getLogger().debug(msg);
                            percentageMsg.add(msg); 
                            percentageMsg.add("");
                        }
                    }                  
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
        return percentageMsg;
    }


}
