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


package oscar.oscarEncounter.oscarMeasurements.data;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementTypesBean;
import oscar.oscarEncounter.oscarMeasurements.bean.EctValidationsBean;
import oscar.oscarEncounter.oscarMeasurements.bean.EctValidationsBeanHandler;

/**
 * This class is used to export measurement types in the following format 
 * <measurement 
        type="NOSK"      
        typeDesc="Smoking"     
        typeDisplayName="Number of Cigarettes per day" 
        measuringInstrc="Cigarettes per day">  
        <validationRule
           name="Numeric Value: 0 to 300"
           maxValue="300"
           minValue="0" 
          isNumeric="1"/>   
    </measurement>
 * @author jay
 */
public class ExportMeasurementType {
    
    /** Creates a new instance of ExportMeasurementType */
    public ExportMeasurementType() {
    }
    
    private Element createXMLMeasurement(String type,String typeDesc,String typeDisplayName, String measuringInstrc){
        //Document doc = new Document();
        Element me = new Element("measurement");
        me.setAttribute("type",type);
        me.setAttribute("typeDesc",typeDesc);
        me.setAttribute("typeDisplayName",typeDisplayName);
        me.setAttribute("measuringInstrc",measuringInstrc);
         
        return me;
    }
    
    private Element createXMLValidation(String name, String maxValue, String minValue, String isDate, String isNumeric,String regularExp,String maxLength,String minLength){
        Element va = new Element("validationRule");
        
        addAttributeifValueNotNull(va,"name",name);
        addAttributeifValueNotNull(va,"maxValue",maxValue);
        addAttributeifValueNotNull(va,"minValue",minValue);
        addAttributeifValueNotNull(va,"isDate",isDate);
        addAttributeifValueNotNull(va,"isNumeric",isNumeric);
        addAttributeifValueNotNull(va,"regularExp",regularExp);
        addAttributeifValueNotNull(va,"maxLength",maxLength);
        addAttributeifValueNotNull(va,"minLength",minLength);
        
        return va;
    }
    
    private void addAttributeifValueNotNull(Element element,String attr, String value){
        if (value != null){
            element.setAttribute(attr,value);
        }
    }
    
    
    public Element exportElement (EctMeasurementTypesBean mtb){
        Element measurement = createXMLMeasurement(mtb.getType(),mtb.getTypeDesc(),mtb.getTypeDisplayName(),mtb.getMeasuringInstrc());
        
        EctValidationsBeanHandler valBeanHandler = new EctValidationsBeanHandler();
        EctValidationsBean v = valBeanHandler.getValidation(mtb.getValidationName());//(EctValidationsBean) validationRules.get(i);
        measurement.addContent(createXMLValidation(v.getName(),v.getMaxValue(),v.getMinValue(),v.getIsDate(),v.getIsNumeric(),v.getRegularExp(),v.getMaxLength(),v.getMinLength()));
        
        return measurement;      
    }
    public String export (EctMeasurementTypesBean mtb){
        Element measurement = createXMLMeasurement(mtb.getType(),mtb.getTypeDesc(),mtb.getTypeDisplayName(),mtb.getMeasuringInstrc());
        
        EctValidationsBeanHandler valBeanHandler = new EctValidationsBeanHandler();
        EctValidationsBean v = valBeanHandler.getValidation(mtb.getValidationName());//(EctValidationsBean) validationRules.get(i);
        measurement.addContent(createXMLValidation(v.getName(),v.getMaxValue(),v.getMinValue(),v.getIsDate(),v.getIsNumeric(),v.getRegularExp(),v.getMaxLength(),v.getMinLength()));
        
        XMLOutputter outp = new XMLOutputter();
        outp.setFormat(Format.getPrettyFormat());
        
        return outp.outputString(measurement);      
    }
           
}
