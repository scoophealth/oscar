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

import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.oscarehr.util.MiscUtils;

import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementTypesBean;
import oscar.oscarEncounter.oscarMeasurements.bean.EctValidationsBean;
import oscar.oscarEncounter.oscarMeasurements.util.EctFindMeasurementTypeUtil;

/**
 *
 * @author jay
 */
public class ImportMeasurementTypes {
    
    private static Logger log = MiscUtils.getLogger();
    
    /**
     * Creates a new instance of ImportMeasurementTypes
     */
    public ImportMeasurementTypes() {
    }
    
    
    
    
    
    public void importMeasurements(InputStream is) throws Exception{
        SAXBuilder parser = new SAXBuilder();
           Document doc = parser.build(is);        
           Element root = doc.getRootElement();
           importMeasurements(root);
    
    }
    
    public void importMeasurements(Element root){
           
           boolean measurementReInitNeeded = false;
           //MAKE SURE ALL MEASUREMENTS HAVE BEEN INITIALIZED
           List meas = root.getChildren("measurement");
           
           log.debug("measurement size "+meas.size());
           for (int i = 0; i < meas.size(); i++){
               Element e = (Element) meas.get(i);
               EctMeasurementTypesBean mtb = new EctMeasurementTypesBean();
               mtb.setType(e.getAttributeValue("type"));
               mtb.setTypeDesc(e.getAttributeValue("typeDesc"));
               mtb.setTypeDisplayName(e.getAttributeValue("typeDisplayName"));
               mtb.setMeasuringInstrc(e.getAttributeValue("measuringInstrc"));
               Element v = e.getChild("validationRule");
               EctValidationsBean vb = new EctValidationsBean();
               vb.setName(v.getAttributeValue("name"));
               vb.setMaxValue(v.getAttributeValue("maxValue"));
               vb.setMinValue(v.getAttributeValue("minValue"));
               vb.setIsDate(v.getAttributeValue("isDate"));
               vb.setIsNumeric(v.getAttributeValue("isNumeric"));
               vb.setRegularExp(v.getAttributeValue("regularExp"));
               vb.setMaxLength(v.getAttributeValue("maxLength"));
               vb.setMinLength(v.getAttributeValue("minLength"));
               mtb.addValidationRule(vb);
               if(!EctFindMeasurementTypeUtil.measurementTypeKeyIsFound(mtb)){
                  log.debug("Needed to add"+mtb.getType());
                  EctFindMeasurementTypeUtil.addMeasurementType(mtb, "");
                  measurementReInitNeeded = true;
               }else{
                  log.debug("Didn't Need to add"+mtb.getType());
               }
               //TODO: check about isTrue
               
           }  
           if(measurementReInitNeeded){
                MeasurementTypes.getInstance().reInit();
           }
           //return measurementReInitNeeded;
    }
    
}
