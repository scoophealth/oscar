/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package oscar.oscarEncounter.oscarMeasurements.util;
//used by eforms for writing measurements
import org.apache.struts.action.*;
import oscar.oscarEncounter.oscarMeasurements.pageUtil.*;
import oscar.oscarEncounter.oscarMeasurements.bean.*;
import oscar.oscarDB.DBHandler;
import java.sql.*;
import java.util.*;
import oscar.util.*;

public class WriteNewMeasurements {
    public static ActionErrors addMeasurements(ArrayList names, ArrayList values, String demographicNo, String providerNo) {
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
        ActionErrors errors = new ActionErrors();
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
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = null;
            for (int i=0; i<measures.size(); i++) {
                Hashtable curmeasure = (Hashtable) measures.get(i);
                String type = (String) curmeasure.get("type");
                String measuringInst = (String)curmeasure.get("measuringInstruction");
                String comments = (String)curmeasure.get("comments");
                String dateObserved = (String)curmeasure.get("dateObserved");
                java.util.Date now = UtilDateUtilities.now();
                String dateEntered = UtilDateUtilities.DateToString(now, "yyyy-MM-dd HH:mm:ss");
                String sql;
                if (measuringInst == null || measuringInst.equals("")) {
                    sql = "SELECT measuringInstruction FROM measurementType WHERE type='" + type + "'";
                    rs = db.GetSQL(sql);
                    if (rs.next()) {
                        measuringInst = rs.getString("measuringInstruction");
                        curmeasure.put("measuringInstruction", measuringInst);
                        rs.close();
                    } else {
                        measures.remove(i);
                        i--;
                        rs.close();
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
            db.CloseConn();
        } catch (SQLException sqe) { sqe.printStackTrace(); }
    }
    
    static private ActionErrors validate(Vector measures, String demographicNo) {
        ActionErrors errors = new ActionErrors();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            EctValidation ectValidation = new EctValidation();
            ResultSet rs;
            boolean valid = true;
            for (int i=0; i<measures.size(); i++) {
                Hashtable measure = (Hashtable) measures.get(i);
                String inputType = (String) measure.get("type");
                String inputValue = (String) measure.get("value");
                String dateObserved = (String) measure.get("dateObserved");
                String comments = (String) measure.get("comments");
                String dateEntered = (String) measure.get("dateEntered");
                String mInstrc, regCharExp;
                String msg = null;
                String regExp = null;
                double dMax = 0;
                double dMin = 0;
                int iMax = 0;
                int iMin = 0;
                org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
                if (gValidator.isBlankOrNull(inputValue)) {
                    measures.removeElementAt(i);
                    i--;
                    continue;
                }
                mInstrc = (String) measure.get("measuringInstruction");
                rs = ectValidation.getValidationType(inputType, mInstrc);
                regCharExp = ectValidation.getRegCharacterExp();
                if (rs.next()){
                    dMax = rs.getDouble("maxValue");
                    dMin = rs.getDouble("minValue");
                    iMax = rs.getInt("maxLength");
                    iMin = rs.getInt("minLength");
                    regExp = rs.getString("regularExp");
                }
                else {
                    //if type with instruction does not exist
                    errors.add(inputType, 
                    new ActionError("errors.oscarEncounter.Measurements.cannotFindType", inputType, mInstrc));
                    valid=false;
                    continue;
                }
                rs.close();

                if(!ectValidation.isInRange(dMax, dMin, inputValue)){
                    errors.add(inputType,
                    new ActionError("errors.range", inputType, Double.toString(dMin), Double.toString(dMax)));
                    valid=false;
                }
                if(!ectValidation.maxLength(iMax, inputValue)){
                    errors.add(inputType,
                    new ActionError("errors.maxlength", inputType, Integer.toString(iMax)));
                    valid=false;
                }
                if(!ectValidation.minLength(iMin, inputValue)){
                    errors.add(inputType,
                    new ActionError("errors.minlength", inputType, Integer.toString(iMin)));
                    valid=false;
                }
                if(!ectValidation.matchRegExp(regExp, inputValue)){
                    errors.add(inputType,
                    new ActionError("errors.invalid", inputType));
                    valid=false;
                }
                if(!ectValidation.isValidBloodPressure(regExp, inputValue)){
                    errors.add(inputType,
                    new ActionError("error.bloodPressure"));
                    valid=false;
                }
                if(!ectValidation.isDate(dateObserved)&&inputValue.compareTo("")!=0){
                    errors.add(inputType,
                    new ActionError("errors.invalidDate", inputType));
                    valid=false;
                }
                
                if (!valid) continue;
                inputValue = org.apache.commons.lang.StringEscapeUtils.escapeSql(inputValue);
                inputType = org.apache.commons.lang.StringEscapeUtils.escapeSql(inputType);
                mInstrc = org.apache.commons.lang.StringEscapeUtils.escapeSql(mInstrc);
                comments = org.apache.commons.lang.StringEscapeUtils.escapeSql(comments);

                //Find if the same data has already been entered into the system
                String sql = "SELECT * FROM measurements WHERE demographicNo='"+demographicNo+ "' AND dataField='"+inputValue
                +"' AND measuringInstruction='" + mInstrc + "' AND comments='" + comments
                + "' AND dateObserved='" + dateObserved + "'";
                rs = db.GetSQL(sql);
                if(rs.next()) {
                    measures.remove(i);
                    i--;
                    continue;
                }
            }
        } catch (SQLException sqe) { sqe.printStackTrace(); }
        return errors;   
    }

    static private void write(Vector measures, String demographicNo, String providerNo) {
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            for (int i=0; i<measures.size(); i++) {
                Hashtable measure = (Hashtable) measures.get(i);

                String inputValue = (String) measure.get("value");
                String inputType = (String) measure.get("type");
                String mInstrc = (String) measure.get("measuringInstruction");
                String comments = (String) measure.get("comments");
                String dateObserved = (String) measure.get("dateObserved");
                String dateEntered = (String) measure.get("dateEntered");
                //write....
                String sql = "INSERT INTO measurements"
                +"(type, demographicNo, providerNo, dataField, measuringInstruction, comments, dateObserved, dateEntered)"
                +" VALUES ('"+inputType+"','"+demographicNo+"','"+providerNo+"','"+inputValue+"','"
                + mInstrc+"','"+comments+"','"+dateObserved+"','"+dateEntered+"')";
                System.out.println("SQL measure ====" + sql);
                db.RunSQL(sql);
            }
            db.CloseConn();
        }
        catch(SQLException e) { e.printStackTrace(); }
    }
}
