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
//used by eforms for writing measurements
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.MeasurementDao.SearchCriteria;
import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.common.model.Validations;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.oscarMeasurements.pageUtil.EctValidation;
import oscar.util.ConversionUtils;
import oscar.util.UtilDateUtilities;

public class WriteNewMeasurements {
	
	private static MeasurementTypeDao measurementTypeDao = SpringUtils.getBean(MeasurementTypeDao.class);
	private static MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);

    public static ActionMessages addMeasurements(ArrayList names, ArrayList values, String demographicNo, String providerNo) {
            //must be called on the same eform object because it retrieves some of its properties
        Vector measures = new Vector();
        for (int i=0; i<names.size(); i++) {
            if ((values.get(i) == null) || ((String) values.get(i)).equals("")) continue;
            String name = (String) names.get(i);
            int typeStart = name.indexOf("m$");
            int fieldStart = name.indexOf("#");
            if ((typeStart < 0) || (fieldStart < 0)) continue;
            typeStart += 2;
            fieldStart++;

            String type = name.substring(typeStart, fieldStart-1);
            String field = name.substring(fieldStart);
            //--------type (i.e. vital signs) and field (i.e. BP) are extracted
            int index;
            index = getMeasurement(measures, type);  //checks if exists in our ArrayList
            
            if (index >= 0) {
                Hashtable curmeasure = (Hashtable) measures.get(index);
                curmeasure.put(field, values.get(i));
            } else {
                Hashtable measure = new Hashtable();
                measure.put("type", type);
                measure.put(field, values.get(i));
                measures.addElement(measure);
            }
        }
        //--------All measurement values are organized in Hashtables in an arraylist
        //--------validation......
        preProcess(measures);
        ActionMessages errors = new ActionMessages();
        //errors.add("value",
           //new ActionError("errors.range", "value", "2", "3"));
        errors = validate(measures, demographicNo);
        if (errors.isEmpty()) {
            write(measures, demographicNo, providerNo);
        }
        return errors;
    }
    
    static private int getMeasurement(Vector measures, String type) {
        for (int i=0; i<measures.size(); i++) {
            Hashtable curmeasure = (Hashtable) measures.get(i);
            if (((String)curmeasure.get("type")).equals(type)) {
                return i;
            }
        }
        return -1;
    }
    static private void preProcess(Vector measures) {
        //fills in required values
       
        for (int i=0; i<measures.size(); i++) {
            Hashtable curmeasure = (Hashtable) measures.get(i);
            String type = (String) curmeasure.get("type");
            String measuringInst = (String)curmeasure.get("measuringInstruction");
            String comments = (String)curmeasure.get("comments");
            String dateObserved = (String)curmeasure.get("dateObserved");
            java.util.Date now = new Date();
            String dateEntered = UtilDateUtilities.DateToString(now, "yyyy-MM-dd HH:mm:ss");
            String sql;
            if (measuringInst == null || measuringInst.equals("")) {
            	List<MeasurementType> tmp = measurementTypeDao.findByType(type);
            	if(tmp.size() > 0) {
            		 measuringInst = tmp.get(0).getMeasuringInstruction();
                     curmeasure.put("measuringInstruction", measuringInst);
            	} else {            		 
                     continue;
            	}
            
            }
            if (comments == null) {
                curmeasure.put("comments", "");
            }
            if (dateObserved == null || dateObserved.equals("")) {
                curmeasure.put("dateObserved", dateEntered);
            }
            curmeasure.put("dateEntered", dateEntered);
        }
       
    }
    
    static private ActionMessages validate(Vector measures, String demographicNo) {
        ActionMessages errors = new ActionMessages();
        EctValidation ectValidation = new EctValidation();
        List<Validations> vs;
        boolean valid = true;
        for (int i=0; i<measures.size(); i++) {
            Hashtable measure = (Hashtable) measures.get(i);
            String inputType = (String) measure.get("type");
            String inputValue = (String) measure.get("value");
            String dateObserved = (String) measure.get("dateObserved");
            String comments = (String) measure.get("comments");
            String mInstrc, regCharExp;
            String regExp = null;
            Double dMax = 0.0;
            Double dMin = 0.0;
            Integer iMax = 0;
            Integer iMin = 0;
            org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
            if (GenericValidator.isBlankOrNull(inputValue)) {
                measures.removeElementAt(i);
                i--;
                continue;
            }
            mInstrc = (String) measure.get("measuringInstruction");
            vs = ectValidation.getValidationType(inputType, mInstrc);
            regCharExp = ectValidation.getRegCharacterExp();
            if (!vs.isEmpty()) {
    			Validations v = vs.iterator().next();
    			dMax = v.getMaxValue();
    			dMin = v.getMinValue();
    			iMax = v.getMaxLength();
    			iMin = v.getMinLength();
    			regExp = v.getRegularExp();
    		}
            else {
                //if type with instruction does not exist
                errors.add(inputType, 
                new ActionMessage("errors.oscarEncounter.Measurements.cannotFindType", inputType, mInstrc));
                valid=false;
                continue;
            }

            if(!ectValidation.isInRange(dMax, dMin, inputValue)){
                errors.add(inputType,
                new ActionMessage("errors.range", inputType, Double.toString(dMin), Double.toString(dMax)));
                valid=false;
            }
            if(!ectValidation.maxLength(iMax, inputValue)){
                errors.add(inputType,
                new ActionMessage("errors.maxlength", inputType, Integer.toString(iMax)));
                valid=false;
            }
            if(!ectValidation.minLength(iMin, inputValue)){
                errors.add(inputType,
                new ActionMessage("errors.minlength", inputType, Integer.toString(iMin)));
                valid=false;
            }
            if(!ectValidation.matchRegExp(regExp, inputValue)){
                errors.add(inputType,
                new ActionMessage("errors.invalid", inputType));
                valid=false;
            }
            if(!ectValidation.isValidBloodPressure(regExp, inputValue)){
                errors.add(inputType,
                new ActionMessage("error.bloodPressure"));
                valid=false;
            }
            if(!ectValidation.isDate(dateObserved)&&inputValue.compareTo("")!=0){
                errors.add(inputType,
                new ActionMessage("errors.invalidDate", inputType));
                valid=false;
            }
            
            if (!valid) continue;
            inputValue = org.apache.commons.lang.StringEscapeUtils.escapeSql(inputValue);
            inputType = org.apache.commons.lang.StringEscapeUtils.escapeSql(inputType);
            mInstrc = org.apache.commons.lang.StringEscapeUtils.escapeSql(mInstrc);
            comments = org.apache.commons.lang.StringEscapeUtils.escapeSql(comments);

            //Find if the same data has already been entered into the system
            MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);
            SearchCriteria sc = new SearchCriteria();
            sc.setDemographicNo(demographicNo);
            sc.setDataField(inputValue);
            sc.setMeasuringInstrc(mInstrc);
            sc.setComments(comments);
            sc.setDateObserved(ConversionUtils.fromDateString(dateObserved));
            List<Measurement> ms = dao.find(sc);
            
            if(!ms.isEmpty()) {
                measures.remove(i);
                i--;
                continue;
            }
        }
        
        return errors;   
    }

    static public void write(Vector measures, String demographicNo, String providerNo) {
       
            
        for (int i=0; i<measures.size(); i++) {
            Hashtable measure = (Hashtable) measures.get(i);

            String inputValue = (String) measure.get("value");
            String inputType = (String) measure.get("type");
            String mInstrc = (String) measure.get("measuringInstruction");
            String comments = (String) measure.get("comments");
            String dateObserved = (String) measure.get("dateObserved");
            String dateEntered = (String) measure.get("dateEntered");
            //write....
            Measurement m = new Measurement();
            m.setType(inputType);
            m.setDemographicId(Integer.parseInt(demographicNo));
            m.setProviderNo(providerNo);
            m.setDataField(inputValue);
            m.setMeasuringInstruction(mInstrc);
            m.setComments(comments);
            m.setDateObserved(ConversionUtils.fromTimestampString(dateObserved));
            dao.persist(m);
           
        }
        
    }
    
    static public void write(Hashtable measure, String demographicNo, String providerNo) { 
        String inputValue = (String) measure.get("value");
        String inputType = (String) measure.get("type");
        String mInstrc = (String) measure.get("measuringInstruction");
        String comments = (String) measure.get("comments");
        String dateObserved = (String) measure.get("dateObserved");
        String dateEntered = (String) measure.get("dateEntered");
        //write....
        Measurement m = new Measurement();
        m.setType(inputType);
        m.setDemographicId(Integer.parseInt(demographicNo));
        m.setProviderNo(providerNo);
        m.setDataField(inputValue);
        m.setMeasuringInstruction(mInstrc);
        m.setComments(comments);
        m.setDateObserved(ConversionUtils.fromTimestampString(dateObserved));
        dao.persist(m);
    }
    
     public void write(final String followUpType, final String followUpValue, final String demographicNo, final String providerNo,final java.util.Date dateObserved,final String comment ) {        
        Hashtable measure = new Hashtable();
        measure.put("value",followUpValue);
        measure.put("type",followUpType);
        measure.put("measuringInstruction","");
        measure.put("comments",  comment == null ? "":comment  );
        measure.put("dateObserved",UtilDateUtilities.DateToString(dateObserved, "yyyy-MM-dd HH:mm:ss"));
        measure.put("dateEntered",UtilDateUtilities.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        write(measure,demographicNo,providerNo);
    }
}
