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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class RptCheckGuideline{
    
    public RptCheckGuideline(){
    }


    /*****************************************************************************************
     * get the number of Patient during aspecific time period
     *
     * @return ArrayList which contain the result in String format
     ******************************************************************************************/ 
    public ArrayList getPatients(DBHandler db, String startDate, String endDate){

        ArrayList patients = new ArrayList();
        
        try{
            String sql = "SELECT DISTINCT demographicNo  FROM eChart WHERE timestamp >= '" + startDate + "' AND timestamp <= '" + endDate + "'";
            MiscUtils.getLogger().debug("SQL Statement: " + sql);
            ResultSet rs;
            
            for(rs=DBHandler.GetSQL(sql); rs.next();){            
                String patient = rs.getString("demographicNo");
                patients.add(patient);                
            }
            rs.close();
        }
        catch(SQLException e)
        {
            MiscUtils.getLogger().error("Error", e);
        }
       
        return patients;
    }

     /*****************************************************************************************
     * Check the measurementType is a numeric value
     *
     * @return 0 when it is false, 1 otherwise
     ******************************************************************************************/
    public int getValidation(String measurementType){        
        
        try{
            String sql = "SELECT * FROM measurementType WHERE type='" + measurementType + "'";
            ResultSet rs;
            rs = DBHandler.GetSQL(sql);
            rs.next();
            String validation = rs.getString("validation");
            rs.close();
            sql = "SELECT * FROM validations WHERE id='" + validation + "'";
            rs = DBHandler.GetSQL(sql);
            rs.next();
            if(rs.getString("isNumeric")!=null){
                MiscUtils.getLogger().debug("isNumeric: " + rs.getInt("isNumeric"));
                return rs.getInt("isNumeric");
            }
            else{
                return 0;
            }
            
        }
        catch(SQLException e)
        {
            MiscUtils.getLogger().error("Error", e);
        }
        
        return 0;
    }

    /*****************************************************************************************
     * Check if the data stored met guideline
     *
     * @return boolean
     ******************************************************************************************/	
    public boolean isNumericValueMetGuideline(double dataEntry, String guideline, String aboveBelow){
        
        MiscUtils.getLogger().debug("this is a numeric value");
        boolean passAllTests = false;
        
        if(aboveBelow.compareTo(">")==0){                                        
            if(dataEntry>=Double.parseDouble(guideline)){
                passAllTests=true;
                MiscUtils.getLogger().debug("Pass double test");
            }
            else{
                passAllTests=false;
            }
        }
        else if(aboveBelow.compareTo("<")==0){
            if(dataEntry<=Double.parseDouble(guideline)){
                passAllTests=true;
                MiscUtils.getLogger().debug("Pass double test");
            }
            else{
                passAllTests=false;
            }
        }
        return passAllTests;
    }
    
     /*****************************************************************************************
     * Check if the the blood pressure stored met guideline
     *
     * @return boolean
     ******************************************************************************************/       
    public boolean isBloodPressureMetGuideline(String dataEntry, String guideline, String aboveBelow){
         
        MiscUtils.getLogger().debug("this is blood pressure");
        
        int slashIndex = guideline.indexOf("/");
        int dataSlashIndex = dataEntry.indexOf("/");
        boolean passAllTests = false;
        
        if (slashIndex >= 0 && dataSlashIndex >=0){                                                                                                                                                                
            String systolic = guideline.substring(0, slashIndex);
            String diastolic = guideline.substring(slashIndex+1);
            String systolicData = dataEntry.substring(0, dataSlashIndex);
            String diastolicData = dataEntry.substring(dataSlashIndex+1);

            int iGuidelineSystolic = Integer.parseInt(systolic);
            int iGuidelineDiastolic = Integer.parseInt(diastolic); 
            int iDataSystolic = Integer.parseInt(systolicData);
            int iDataDiastolic = Integer.parseInt(diastolicData); 

            MiscUtils.getLogger().debug("guideline Systolic: " + iGuidelineSystolic + " dataSystolic: " + iDataSystolic);
            MiscUtils.getLogger().debug("guideline Diastolic: " + iGuidelineDiastolic + " dataDiastolic: " + iDataDiastolic);
            if(aboveBelow.compareTo("<")==0){
                if(iDataSystolic<=iGuidelineSystolic && iDataDiastolic<=iGuidelineDiastolic){
                    passAllTests=true;
                    MiscUtils.getLogger().debug("pass this BP test");
                }
                else{
                    passAllTests=false;
                    MiscUtils.getLogger().debug("fail this BP test");
                }
            }
            else if(aboveBelow.compareTo(">")==0){
                if(iDataSystolic>=iGuidelineSystolic && iDataDiastolic>=iGuidelineDiastolic){
                    passAllTests=true;
                    MiscUtils.getLogger().debug("pass this BP test");
                }
                else{
                    passAllTests=false;
                    MiscUtils.getLogger().debug("fail this BP test");
                }
            } 
        }
        return passAllTests;
    }    

     /*****************************************************************************************
     * Check if the yes/no question met guideline 
     *
     * @return boolean
     ******************************************************************************************/       
    public boolean isYesNoMetGuideline(String dataEntry, String guideline){
        boolean passAllTests = false;
        
        MiscUtils.getLogger().debug("this is yes/no question");
        if(guideline.compareTo("YES")==0 || guideline.compareTo("yes")==0 
               || guideline.compareTo("Y")==0 || guideline.compareTo("Yes")==0){                                        
            if(dataEntry.compareTo("YES")==0 || dataEntry.compareTo("yes")==0 
               || dataEntry.compareTo("Y")==0 || dataEntry.compareTo("Yes")==0){
                   passAllTests=true;
                   MiscUtils.getLogger().debug("Pass yesno test");
            }
            else{
                passAllTests=false;
                MiscUtils.getLogger().debug("fail yesno test");
            }
        }
        else if(guideline.compareTo("NO")==0 || guideline.compareTo("No")==0 
               || guideline.compareTo("N")==0 || guideline.compareTo("no")==0){
            if(dataEntry.compareTo("NO")==0 || dataEntry.compareTo("No")==0 
               || dataEntry.compareTo("N")==0 || dataEntry.compareTo("no")==0){
                   passAllTests=true;
                   MiscUtils.getLogger().debug("Pass yesno test");
            }
            else{
                passAllTests=false;
                 MiscUtils.getLogger().debug("fail yesno test");
            }
        }
        return passAllTests;
    }
}
