/*
 *  
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * Author: Ivy Chan
 * Company: iConcept Technologes Inc. 
 * Created on: October 31, 2004
 */
package oscar.oscarEncounter.oscarMeasurements.util;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarMeasurements.bean.*;
import oscar.oscarEncounter.oscarMeasurements.prop.*;

import org.apache.commons.digester.Digester;

//import com.ibatis.common.resources.Resources;

public class EctFindMeasurementTypeUtil {
    
    public EctFindMeasurementTypeUtil(){}
    
    static public EctFormProp getEctMeasurementsType(InputStream is) {
        EctFormProp ret = null;
        try {
            Digester digester = new Digester();
            digester.setValidating(false);
            
            digester.addObjectCreate("formProp", EctFormProp.class);
            digester.addObjectCreate("formProp/measurement", EctMeasurementTypesBean.class);
            digester.addBeanPropertySetter("formProp/measurement/type", "type");
            digester.addBeanPropertySetter("formProp/measurement/typeDesc", "typeDesc");
            digester.addBeanPropertySetter("formProp/measurement/typeDisplayName", "typeDisplayName");
            digester.addBeanPropertySetter("formProp/measurement/measuringInstrc", "measuringInstrc");
            
            digester.addObjectCreate("formProp/measurement/validationRule", EctValidationsBean.class);
            digester.addBeanPropertySetter("formProp/measurement/validationRule/name", "name");
            digester.addBeanPropertySetter("formProp/measurement/validationRule/regularExp", "regularExp");
            digester.addBeanPropertySetter("formProp/measurement/validationRule/minValue", "minValue");
            digester.addBeanPropertySetter("formProp/measurement/validationRule/maxValue", "maxValue");
	    digester.addBeanPropertySetter("formProp/measurement/validationRule/minLength", "minLength");
	    digester.addBeanPropertySetter("formProp/measurement/validationRule/maxLength", "maxLength");
            digester.addBeanPropertySetter("formProp/measurement/validationRule/isNumeric", "isNumeric");
            digester.addBeanPropertySetter("formProp/measurement/validationRule/isDate", "isDate");
            digester.addSetNext("formProp/measurement/validationRule", "addValidationRule");
            
            digester.addSetNext("formProp/measurement", "addMeasurementType");
                        
            //File input = new File(xmlPath);
            ret = (EctFormProp) digester.parse(is);
            digester.clear();
        } 
        catch (Exception exc) {
            exc.printStackTrace();
        }
        return ret;
    }
    
    /**
     * Compare the form definition xml file with the measurementtype table in the database. 
     * If a measurment type found in the definition file but not in the database, add a new type to the measurementtype table
     * @param xmlpath the path of the form definition xml file
     * @author - Ivy Chan - iConcept Technologies Inc.
     */
    static public Vector checkMeasurmentTypes(InputStream is){
        //System.out.println("in checkMeasurementTypes");
        EctFormProp formProp = getEctMeasurementsType(is);
        //System.out.println("xmlpath:" + xmlPath);
        Vector measurementTypes = formProp.getMeasurementTypes();
        //System.out.println("got measurementTypes");
        for(int i=0; i<measurementTypes.size(); i++){
            EctMeasurementTypesBean mt = (EctMeasurementTypesBean) measurementTypes.elementAt(i);
            EctValidationsBean validation = (EctValidationsBean) mt.getValidationRules().elementAt(0);           
            //System.out.println(mt.getType() + " " + validation.getName() + " " + validation.getRegularExp());
            if(!measurementTypeIsFound(mt)){
                addMeasurementType(mt);
            }
        }
        return measurementTypes;
    }
    
    static public boolean measurementTypeIsFound(EctMeasurementTypesBean mt){
        boolean verdict = true;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "SELECT * from measurementType where type='"+ mt.getType() + "' AND measuringInstruction='"
                         + mt.getMeasuringInstrc() + "'";
            ResultSet rs = db.GetSQL(sql);
            if(!rs.next()){            
                verdict = false;
            }
            rs.close();
            db.CloseConn();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
            verdict = false;
        }
        return verdict;
    }
    
    static public void addMeasurementType(EctMeasurementTypesBean mt){
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            //Find validation if not found add validation
            Vector validations = mt.getValidationRules();
            if(validations.size()>0){
                EctValidationsBean validation = (EctValidationsBean) validations.elementAt(0);  
                EctValidationsBeanHandler vHd = new EctValidationsBeanHandler();
                int validationId = vHd.findValidation(validation);
                if(validationId<0){
                    validationId = vHd.addValidation(validation);
                }
                String sql ="INSERT INTO measurementType(type, typeDisplayName, typeDescription, measuringInstruction, validation)" +
                            "VALUES('" + mt.getType() + "', '" + mt.getTypeDisplayName() + "', '" + mt.getTypeDesc() + "', '" +
                            mt.getMeasuringInstrc() + "', '" + validationId + "')";
                db.RunSQL(sql);
            }
            db.CloseConn();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());         
        }
    }
        
}