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


package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.validator.GenericValidator;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class EctValidation{

    public String regCharacterExp = "^[\\w\\s,.?]*$";        

    public EctValidation(){
    }
    

    public String getRegCharacterExp(){
        return this.regCharacterExp;
    }
              
    public ResultSet getValidationType(String inputType, String mInstrc){
        ResultSet rs = null;
        try{
                                
                String sql = "SELECT validation FROM measurementType WHERE type = '"+ inputType + "'AND measuringInstruction='" + mInstrc + "'"; 
                rs = DBHandler.GetSQL(sql);
                if (rs.next()){
                    String validation = oscar.Misc.getString(rs, "validation");                
                    rs.close();

                    sql = "SELECT * FROM validations where id=" + validation;
                    rs = DBHandler.GetSQL(sql);
                }
        }
        catch(SQLException e){
            MiscUtils.getLogger().error("Error", e);
        }
        
        return rs;
    }
    
    public boolean matchRegExp(String regExp, String inputValue){

        boolean validation = true; 
        org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
        
        MiscUtils.getLogger().debug("matchRegExp function is called.");
        
        if (!GenericValidator.isBlankOrNull(regExp) && !GenericValidator.isBlankOrNull(inputValue)){
            MiscUtils.getLogger().debug("both the regExp and inputValue is not blank nor null.");
            if(!inputValue.matches(regExp)){
                MiscUtils.getLogger().debug("Regexp not matched");                                            
                validation=false;
                
            }

        }
        return validation;
     }
    
     
    public boolean isInRange(double dMax, double dMin, String inputValue){

        boolean validation = true;
        org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
        
        if ((dMax!=0) || (dMin!=0)){
            if(GenericValidator.isDouble(inputValue)){
                double dValue = Double.parseDouble(inputValue);
                
                if (!GenericValidator.isInRange(dValue, dMin, dMax)){                                       
                    validation=false;
                }
            }
            else if(!GenericValidator.isBlankOrNull(inputValue)){                                
                validation=false;
            }
        }
        return validation;
    }
    

    public boolean maxLength(int iMax, String inputValue){

        boolean validation = true;
        org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
        
        if (iMax!=0){            
            if(!GenericValidator.maxLength(inputValue, iMax)){                
                    validation=false;
                }                       
        }
        return validation;
    }

    public boolean minLength(int iMin, String inputValue){

        boolean validation = true;
        org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
        
        if (iMin!=0){            
            if(!GenericValidator.minLength(inputValue, iMin)){                
                    validation=false;
                }                       
        }
        return validation;
    }    
    
    public boolean isInteger(String inputValue){

        boolean validation = true;
        org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
        
        
        if(!GenericValidator.isInt(inputValue)){                                                 
            validation=false;
        }
        
       
        return validation;
    }
    
    public boolean isDate(String inputValue){

        boolean validation = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        
        try {
			Date d = sdf.parse(inputValue);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			if(c.get(Calendar.YEAR) > 9999) { // to prevent date parsing of more than 4-digit year
				validation = false;
			}
		} catch (ParseException e) {
			validation = false;
		}
               
        return validation;
    }

     public boolean isValidBloodPressure(String regExp, String inputValue){
         
         boolean validation = true;    
         
         if(inputValue.split("/").length >1 && regExp.isEmpty()){
        	 // this field is not blood pressure, no need to validate
        	 return validation;
         }
         
         if(matchRegExp(regExp, inputValue)){
            MiscUtils.getLogger().debug("/");
            int slashIndex = inputValue.indexOf("/");
            MiscUtils.getLogger().debug(slashIndex);
            if (slashIndex >= 0){
                String systolic = inputValue.substring(0, slashIndex);
                String diastolic = inputValue.substring(slashIndex+1);
                MiscUtils.getLogger().debug("The systolic value is " + systolic);
                MiscUtils.getLogger().debug("The diastolic value is " + diastolic);

                int iSystolic = 0;
                int iDiastolic = 0;

                try {
                	iSystolic = Integer.parseInt(systolic);
                	iDiastolic = Integer.parseInt(diastolic);
                }
                catch (NumberFormatException e) {
                	MiscUtils.getLogger().error("Wrong input for blood pressure");
                	validation =  false;
                }
                if( iDiastolic > iSystolic){                    
                    validation = false;
                }
                else if (iDiastolic > 300 || iSystolic > 300){
                    validation = false;
                }
            }
        }
        return validation;
     }
     
     /*****************************************************************************************
     * find the css path from the database.
     * Use commented code to find css path from properties file
     *
     * @return String
     ******************************************************************************************/
    public String getCssPath(String inputGroupName){
        String cssLocation = null;
        try {
            
            String sql = "SELECT * from measurementGroupStyle where groupName = '" + inputGroupName + "'";
            MiscUtils.getLogger().debug("Sql Statement: " + sql);
            ResultSet rs;
            rs = DBHandler.GetSQL(sql);
            if(rs.next()){
                String cssId = oscar.Misc.getString(rs, "cssID");
                rs.close();   
                
                sql = "SELECT * from measurementCSSLocation where cssID = '" + cssId + "'";
                rs = DBHandler.GetSQL(sql);
                if(rs.next()){
                    String place = "StreamStyleSheet.do?cssfilename="; // Streams by default

                    // Use the following commented code in place of the above line to allow the
                    // option of using the oscarMeasurement_css property to form the css path.
                    // If using this code, also uncomment the line in oscar.login.Startup.java
                    // that checks and sets that property.
                    /*
                     * String downloadMethod = OscarProperties.getInstance().getProperty("oscarMeasurement_css_download_method");
                     * String place = "";
                     * if (downloadMethod == null || !(downloadMethod.equalsIgnoreCase("stream"))) {
                     *    place = OscarProperties.getInstance().getProperty("oscarMeasurement_css");
                     *    if(!place.endsWith("/"))
                     *       place = new StringBuilder(place).insert(place.length(),"/").toString();
                     * } else {
                     *    place = "StreamStyleSheet.do?cssfilename=";
                     * }
                     */

                    cssLocation = place+oscar.Misc.getString(rs, "location");
                }
            }
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);            
        }

        return cssLocation;
    }
    
     /*****************************************************************************************
     * find the css name from the database
     *
     * @return String
     ******************************************************************************************/
    public String getCssName(String inputGroupName){
        String cssName = null;
        try {
            
            String sql = "SELECT * from measurementGroupStyle where groupName = '" + inputGroupName + "'";
            MiscUtils.getLogger().debug("Sql Statement: " + sql);
            ResultSet rs;
            rs = DBHandler.GetSQL(sql);
            if(rs.next()){
                String cssId = oscar.Misc.getString(rs, "cssID");
                rs.close();   
                
                sql = "SELECT * from measurementCSSLocation where cssID = '" + cssId + "'";
                rs = DBHandler.GetSQL(sql);
                if(rs.next()){                    
                    cssName = oscar.Misc.getString(rs, "location");
                }
            }
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);            
        }

        return cssName;
    }
}
