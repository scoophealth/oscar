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
package oscar.oscarEncounter.oscarMeasurements.util;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.digester.Digester;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementTypesBean;
import oscar.oscarEncounter.oscarMeasurements.bean.EctValidationsBean;
import oscar.oscarEncounter.oscarMeasurements.bean.EctValidationsBeanHandler;
import oscar.oscarEncounter.oscarMeasurements.prop.EctFormProp;

//import com.ibatis.common.resources.Resources;
/* 
 * Author: Ivy Chan
 * Company: iConcept Technologes Inc. 
 * Created on: October 31, 2004
 */

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
            digester.addBeanPropertySetter("formProp/measurement/canPrefill", "canPrefill");
            
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
        	MiscUtils.getLogger().error("Error", exc);
        }
        return ret;
    }
    /*
    static public Vector getMeasurementsType(String formName, String demo) {
        
        boolean verdict = true;
        Vector measurementTypeVector = new Vector();
        
        try {
            
            String sql =   "SELECT mf.typeId, mt.type, mt.typeDisplayName, mt.typeDescription, " 
                            + "mt.measuringInstruction, mt.validation"
                            + " FROM measurementForm mf, measurementType mt WHERE mf.formName='" + formName 
                            + "' AND mf.typeId=mt.id";            
            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){                                      
                EctMeasurementTypesBean measurementTypes = new EctMeasurementTypesBean( rs.getInt("typeId"), 
                                                                                        oscar.Misc.getString(rs,("type"), 
                                                                                        oscar.Misc.getString(rs,("typeDisplayName"), 
                                                                                        oscar.Misc.getString(rs,("typeDescription"), 
                                                                                        oscar.Misc.getString(rs,("measuringInstruction"), 
                                                                                        oscar.Misc.getString(rs,("validation")); 
                measurementTypeVector.add(measurementTypes);                                                       
                MiscUtils.getLogger().debug("getMeasurementType() type: " + oscar.Misc.getString(rs,("typeId"));
            }
            
            rs.close();            
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return measurementTypeVector;
    }*/
    
    /**
     * Compare the form definition xml file with the measurementtype table in the database. 
     * If a measurment type found in the definition file but not in the database, add a new type to the measurementtype table
     * @param xmlpath the path of the form definition xml file
     * @author - Ivy Chan - iConcept Technologies Inc.
     */
    static public Vector checkMeasurmentTypes(InputStream is, String formName){

        EctFormProp formProp = getEctMeasurementsType(is);

        Vector measurementTypes = EctFormProp.getMeasurementTypes();

        for(int i=0; i<measurementTypes.size(); i++){
            EctMeasurementTypesBean mt = (EctMeasurementTypesBean) measurementTypes.elementAt(i);
            EctValidationsBean validation = (EctValidationsBean) mt.getValidationRules().elementAt(0);           

            if(!measurementTypeIsFound(mt, formName)){
                addMeasurementType(mt, formName);
            }
        }
        return measurementTypes;
    }
    
    static public boolean measurementTypeIsFound(EctMeasurementTypesBean mt, String formName){
        boolean verdict = true;
        try {
            
            String sql = "SELECT * from measurementType where type='"+ mt.getType() + "' AND measuringInstruction='"
                         + mt.getMeasuringInstrc() + "'";
            ResultSet rs = DBHandler.GetSQL(sql);
            if(!rs.next()){   
                
                verdict = false;
            }
            /*else{
                if(!measurementFrmIsAdded(formName, oscar.Misc.getString(rs,("id")))
                    add2MeasurementForm(formName, oscar.Misc.getString(rs,("id"));
            }*/
            rs.close();
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }
    
    static public boolean measurementTypeKeyIsFound(EctMeasurementTypesBean mt){
        boolean verdict = true;
        try {
            
            String sql = "SELECT * from measurementType where type='"+ mt.getType() + "' ";
            ResultSet rs = DBHandler.GetSQL(sql);
            if(!rs.next()){                   
                verdict = false;
            }
            rs.close();
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }
    
    
    /*
    static public boolean measurementFrmIsAdded(String formName, String typeId){
        boolean verdict = true;
        try {

            
            String sql = "SELECT * FROM measurementForm WHERE formName='" + formName + "' AND typeId='"+typeId+"'";
            ResultSet rs = DBHandler.GetSQL(sql);
            if(!rs.next()){            
                verdict = false;
            }
            rs.close();
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }*/
    
    static public void addMeasurementType(EctMeasurementTypesBean mt, String formName){
        try{
            
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
                DBHandler.RunSQL(sql);                
                /*sql = "SELECT * FROM measurementType ORDER BY id DESC LIMIT 1";
                ResultSet rs = DBHandler.GetSQL(sql);             
                if(rs.next()){
                    if(!measurementFrmIsAdded(formName, oscar.Misc.getString(rs,("id")))
                        add2MeasurementForm(formName, oscar.Misc.getString(rs,("id"));
                }*/
            }
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);         
        }
    }
    
    /*
    static public void add2MeasurementForm(String formName, String typeId){
        boolean verdict = true;
        try {

            
            String sql = "INSERT INTO measurementForm VALUES('" + formName + "','"+typeId+"')";
            DBHandler.RunSQL(sql);
            
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);           
        }        
    }*/    
        
}
