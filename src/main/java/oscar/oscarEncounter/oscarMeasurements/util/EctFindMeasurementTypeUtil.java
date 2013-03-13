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
import java.util.Vector;

import org.apache.commons.digester.Digester;
import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

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
    
	private static MeasurementTypeDao dao = SpringUtils.getBean(MeasurementTypeDao.class);

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
                        
            ret = (EctFormProp) digester.parse(is);
            digester.clear();
        } 
        catch (Exception exc) {
        	MiscUtils.getLogger().error("Error", exc);
        }
        return ret;
    }
 
   
    /**
     * Compare the form definition xml file with the measurementtype table in the database. 
     * If a measurment type found in the definition file but not in the database, add a new type to the measurementtype table
     */
    static public Vector checkMeasurmentTypes(InputStream is, String formName){

        EctFormProp formProp = getEctMeasurementsType(is);

        Vector measurementTypes = EctFormProp.getMeasurementTypes();

        for(int i=0; i<measurementTypes.size(); i++){
            EctMeasurementTypesBean mt = (EctMeasurementTypesBean) measurementTypes.elementAt(i);
            
            if(!measurementTypeIsFound(mt, formName)){
                addMeasurementType(mt, formName);
            }
        }
        return measurementTypes;
    }
    
    static public boolean measurementTypeIsFound(EctMeasurementTypesBean mt, String formName){
        boolean verdict = true;
        if(dao.findByTypeAndMeasuringInstruction(mt.getType(), mt.getMeasuringInstrc()).size() == 0) {
        	verdict = false;
        }
       
        return verdict;
    }
    
    static public boolean measurementTypeKeyIsFound(EctMeasurementTypesBean mt){
        boolean verdict = true;
        if(dao.findByType(mt.getType()).size() == 0) {
        	verdict=false;
        }
       
        return verdict;
    }
    
    
    static public void addMeasurementType(EctMeasurementTypesBean mt, String formName){
        
        //Find validation if not found add validation
        Vector validations = mt.getValidationRules();
        if(validations.size()>0){
            EctValidationsBean validation = (EctValidationsBean) validations.elementAt(0);  
            EctValidationsBeanHandler vHd = new EctValidationsBeanHandler();
            int validationId = vHd.findValidation(validation);
            if(validationId<0){
                validationId = vHd.addValidation(validation);
            }
            MeasurementType m = new MeasurementType();
            m.setType(mt.getType());
            m.setTypeDisplayName(mt.getTypeDisplayName());
            m.setTypeDescription(mt.getTypeDesc());
            m.setMeasuringInstruction(mt.getMeasuringInstrc());
            m.setValidation(String.valueOf(validationId));
            dao.persist(m);            
            
        }
       
    }
        
}
