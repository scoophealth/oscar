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
package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.PrintStream;
import java.util.*;
import java.lang.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.validator.*;
import org.apache.commons.validator.*;
import oscar.oscarDB.DBHandler;


public class EctValidation{

    public String regCharacterExp = "^[0-9a-zA-Z]*$";        

    public EctValidation(){
    }
    

    public String getRegCharacterExp(){
        return this.regCharacterExp;
    }
              
    public ResultSet getValidationType(String inputType, String mInstrc){
        ResultSet rs = null;
        try{
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                
                String sql = "SELECT validation FROM measurementType WHERE type = '"+ inputType + "'AND measuringInstruction='" + mInstrc + "'"; 
                rs = db.GetSQL(sql);
                if (rs.next()){
                    String validation = rs.getString("validation");                
                    rs.close();

                    sql = "SELECT * FROM validations where id=" + validation;
                    rs = db.GetSQL(sql);
                }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return rs;
    }
    
    public boolean matchRegExp(String regExp, String inputValue){

        boolean validation = true; 
        org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
        
        System.out.println("matchRegExp function is called.");
        
        if (!gValidator.isBlankOrNull(regExp) && !gValidator.isBlankOrNull(inputValue)){
            System.out.println("both the regExp and inputValue is not blank nor null.");
            if(!inputValue.matches(regExp)){
                System.out.println("Regexp not matched");                                            
                validation=false;
                
            }

        }
        return validation;
     }
    
        
    public boolean isInRange(double dMax, double dMin, String inputValue){

        boolean validation = true;
        org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
        
        if ((dMax!=0) || (dMin!=0)){
            if(gValidator.isDouble(inputValue)){
                double dValue = Double.parseDouble(inputValue);
                String min = Double.toString(dMin);
                String max = Double.toString(dMax);
                if (!gValidator.isInRange(dValue, dMin, dMax)){                                       
                    validation=false;
                }
            }
            else if(!gValidator.isBlankOrNull(inputValue)){                                
                validation=false;
            }
        }
        return validation;
    }

    
    public boolean isInteger(String inputValue){

        boolean validation = true;
        org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
        
        
        if(!gValidator.isInt(inputValue)){                                                 
            validation=false;
        }
        
       
        return validation;
    }
    
    public boolean isDate(String inputValue){

        boolean validation = true;
        String datePattern1 = "yyyy-mm-dd";
        String datePattern2 = "yyyy-m-d";        
        org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
        
        
        if(!gValidator.isDate(inputValue, datePattern1, false) || !gValidator.isDate(inputValue, datePattern2, false)){                                                 
            validation=false;
        }
               
        return validation;
    }

     public boolean isValidBloodPressure(String regExp, String inputValue){
         
         boolean validation = true;    
         
         if(matchRegExp(regExp, inputValue)){
            System.out.println("/");
            int slashIndex = inputValue.indexOf("/");
            System.out.println(slashIndex);
            if (slashIndex >= 0){
                String systolic = inputValue.substring(0, slashIndex);
                String diastolic = inputValue.substring(slashIndex+1);
                System.out.println("The systolic value is " + systolic);
                System.out.println("The diastolic value is " + diastolic);
                int iSystolic = Integer.parseInt(systolic);
                int iDiastolic = Integer.parseInt(diastolic);
                if( iDiastolic > iSystolic){                    
                    validation=false;
                }
                else if (iDiastolic > 300 || iSystolic > 300){
                    validation =false;
                }
            }
        }
        return validation;
     }
     
     
}
