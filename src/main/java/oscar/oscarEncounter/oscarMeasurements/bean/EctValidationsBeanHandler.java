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


package oscar.oscarEncounter.oscarMeasurements.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class EctValidationsBeanHandler {
    
    Vector validationsVector = new Vector();
 
    public EctValidationsBeanHandler() {
        init();
    }
    
    public boolean init() {
        
        boolean verdict = true;
        try {
            
            String sql = "SELECT name,id FROM validations ORDER BY name";
            ResultSet rs;
            for(rs = DBHandler.GetSQL(sql); rs.next(); )
            {
                EctValidationsBean validation = new EctValidationsBean(oscar.Misc.getString(rs, "name"), rs.getInt("id"));
                validationsVector.add(validation);
            }

            rs.close();
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }

    public Collection getValidationsVector(){
        return validationsVector;
    }
    
    public int findValidation(EctValidationsBean validation){
        int validationId = -1;
        String regularExp = " IS NULL";
        String minValue = " IS NULL";
        String maxValue = " IS NULL";
        String minLength = " IS NULL";
        String maxLength = " IS NULL";
        String isNumeric = " IS NULL";
        String isDate = " IS NULL";
        
        if(validation.getRegularExp()!=null)
            regularExp = "='" + validation.getRegularExp() + "'";

        if(validation.getMinValue()!=null)
            minValue = "='" + validation.getMinValue() + "'";

        if(validation.getMaxValue()!=null)
            maxValue = "='" + validation.getMaxValue() + "'";

        if(validation.getMinLength()!=null)
            minLength = "='" + validation.getMinLength() + "'";

        if(validation.getMaxLength()!=null)
            maxLength = "='" + validation.getMaxLength() + "'";

        if(validation.getIsNumeric()!=null)
            isNumeric = "='" + validation.getIsNumeric() + "'";
        
        if(validation.getIsDate()!=null)
            isDate = "='" + validation.getIsDate() + "'";
        
        try{
                        
            String sql ="SELECT * FROM validations WHERE regularExp" + regularExp
                        + " AND minValue" + minValue 
                        + " AND maxValue" + maxValue
                        + " AND minLength" + minLength
                        + " AND maxLength" + maxLength
                        + " AND isNumeric" + isNumeric
                        + " AND isDate" + isDate;

            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next()){
                validationId = rs.getInt("id");

            }
            rs.close();
        }
        
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            validationId = -1;
        }
        
        return validationId;
    }
    
    public int addValidation(EctValidationsBean validation){
        int validationId = -1;
        
        try{
                        
            String regularExp = null;
            String minValue = null;
            String maxValue = null;
            String minLength = null;
            String maxLength = null;
            String isNumeric = null;
            String isDate = null;
            
            if(validation.getRegularExp()!=null)
                regularExp = "'" + validation.getRegularExp() + "'";
            
            if(validation.getMinValue()!=null)
                minValue = "'" + validation.getMinValue() + "'";

            if(validation.getMaxValue()!=null)
                maxValue = "'" + validation.getMaxValue() + "'";
            
            if(validation.getMinLength()!=null)
                minLength = "'" + validation.getMinLength() + "'";
            
            if(validation.getMaxLength()!=null)
                maxLength = "'" + validation.getMaxLength() + "'";
            
            if(validation.getIsNumeric()!=null)
                isNumeric = "'" + validation.getIsNumeric() + "'";
            
            if(validation.getIsDate()!=null)
                isDate = "'" + validation.getIsDate() + "'";
            
            String sql ="INSERT INTO validations(`name`, `regularExp`, `minValue`, `maxValue`, `minLength`, `maxLength`, `isNumeric`, `isDate`) VALUES('"            
                        + validation.getName() + "', "
                        + regularExp + "," 
                        + minValue + "," 
                        + maxValue + "," 
                        + minLength + "," 
                        + maxLength + "," 
                        + isNumeric + ","
                        + isDate + ")";

            DBHandler.RunSQL(sql);
            sql = "SELECT id FROM validations ORDER BY id DESC LIMIT 1";
            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next()){
                validationId = rs.getInt("id");
            }
        }
        
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            validationId = -1;
        }
        
        return validationId;
    }
    
    public EctValidationsBean getValidation(String val){   
        EctValidationsBean validation = new EctValidationsBean();
        try{
                        

            String sql ="SELECT * FROM  validations WHERE name = '"+StringEscapeUtils.escapeSql(val)+"'";             
            ResultSet rs = DBHandler.GetSQL(sql);
            
            if (rs.next()){
                validation.setName(val);
                validation.setRegularExp(oscar.Misc.getString(rs, "regularExp")); 
                validation.setMinValue(oscar.Misc.getString(rs, "minValue"));
                validation.setMaxValue(oscar.Misc.getString(rs, "maxValue"));
                validation.setMinLength(oscar.Misc.getString(rs, "minLength"));
                validation.setMaxLength(oscar.Misc.getString(rs, "maxLength"));
                validation.setIsNumeric(oscar.Misc.getString(rs, "isNumeric"));
                validation.setIsDate(oscar.Misc.getString(rs, "isDate"));
            }
        }catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        
        return validation;
    }
}
