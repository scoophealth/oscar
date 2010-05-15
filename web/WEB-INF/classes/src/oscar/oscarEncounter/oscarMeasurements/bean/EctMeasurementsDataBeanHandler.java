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
// * McMaster University
// * Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.oscarMeasurements.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarMeasurements.data.MeasurementTypes;

public class EctMeasurementsDataBeanHandler {
    private static Log log = LogFactory.getLog(EctMeasurementsDataBeanHandler.class);
    Vector measurementsDataVector = new Vector();
    
    public EctMeasurementsDataBeanHandler(String demo) {
        init(demo);
    }
    
    public EctMeasurementsDataBeanHandler(String demo, String type) {
        init(demo, type);
    }
    
    /*public boolean init(String demo) {
        boolean verdict = true;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql ="SELECT m.id, mt.type, mt.typeDisplayName, m.demographicNo, m.providerNo, m.dataField, m.measuringInstruction,"+
                        "m.comments, m.dateObserved, m.dateEntered , p.first_name AS provider_first, p.last_name AS provider_last," +
                        "v.isNumeric AS numericValidation, v.name AS validationName FROM measurements m, provider p, validations v," +
                        "measurementType mt WHERE m.demographicNo='" + demo + "' AND m.providerNo= p.provider_no AND m.type = mt.type " +
                        "AND mt.measuringInstruction = m.measuringInstruction AND mt.validation = v.id ORDER BY m.type ASC," +
                        "m.dateEntered DESC";
     
            log.debug(" EctMeasurementDataBeanHandler sql: " + sql);
            ResultSet rs;
            String canPlot = null;
            for(rs = db.GetSQL(sql); rs.next(); )
            {
                    if (rs.getInt("numericValidation")==1 || db.getString(rs,"validationName").compareTo("Blood Pressure")==0)
                        canPlot = "true";
                    else
                        canPlot = null;
                    //log.debug("canPlot value: " + canPlot);
                    EctMeasurementsDataBean data = new EctMeasurementsDataBean(rs.getInt("id"), db.getString(rs,"type"), db.getString(rs,"typeDisplayName"), db.getString(rs,"demographicNo"),
                                                                               db.getString(rs,"provider_first"), db.getString(rs,"provider_last"),
                                                                               db.getString(rs,"dataField"), db.getString(rs,"measuringInstruction"),
                                                                               db.getString(rs,"comments"), db.getString(rs,"dateObserved"),
                                                                               db.getString(rs,"dateEntered"), canPlot);
                    measurementsDataVector.add(data);
            }
     
            rs.close();
        }
        catch(SQLException e) {
            log.debug(e.getMessage());
            verdict = false;
        }
        return verdict;
    }*/
    
    public boolean init(String demo) {
        boolean verdict = true;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql ="SELECT mt.type, mt.typeDisplayName, mt.typeDescription, mt.measuringInstruction FROM measurements m," +
                    "measurementType mt WHERE m.demographicNo='" + demo + "' AND m.type = mt.type " +
                    "GROUP BY mt.type ORDER BY m.type ASC";
            
            log.debug(" EctMeasurementDataBeanHandler sql: " + sql);
            ResultSet rs;
            String canPlot = null;
            for(rs = db.GetSQL(sql); rs.next(); ) {
                EctMeasurementsDataBean data = new EctMeasurementsDataBean();
                data.setType(db.getString(rs,"type"));
                data.setTypeDisplayName(db.getString(rs,"typeDisplayName"));
                data.setTypeDescription(db.getString(rs,"typeDescription"));
                data.setMeasuringInstrc(db.getString(rs,"measuringInstruction"));
                //log.debug("Measurments: " + db.getString(rs,"type") + " " + db.getString(rs,"typeDisplayName") + " " + db.getString(rs,"typeDescription"));
                measurementsDataVector.add(data);
            }
            
            rs.close();
        } catch(SQLException e) {
            log.error(e.getMessage());
            verdict = false;
        }
        return verdict;
    }
    
    
    public boolean init(String demo, String type) {
        log.debug("Getting type "+type+" for demograph "+demo);
        boolean verdict = true;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            MeasurementTypes mt = MeasurementTypes.getInstance();
            EctMeasurementTypesBean mBean = mt.getByType(type);
            if ( mBean != null){
                
                /*String sql ="SELECT m.id,m.type, m.demographicNo, m.providerNo, m.dataField, m.measuringInstruction,"+
                        "m.comments, m.dateObserved, m.dateEntered , p.first_name AS provider_first, p.last_name AS provider_last," +
                        "v.isNumeric AS numericValidation, v.name AS validationName FROM measurements m, provider p, validations v" +
                        " WHERE m.demographicNo='" + demo + "' AND m.type = '" + type + "' AND m.providerNo= p.provider_no " +
                        "AND v.id = "+mBean.getValidation()+" GROUP BY m.id ORDER BY m.dateObserved DESC," +
                        "m.dateEntered DESC";
                */ 
                String sql ="SELECT m.id,m.type, m.demographicNo, m.providerNo, m.dataField, m.measuringInstruction,"+
                        "m.comments, m.dateObserved, m.dateEntered , p.first_name AS provider_first, p.last_name AS provider_last," +
                        "v.isNumeric AS numericValidation, v.name AS validationName FROM validations v, measurements m LEFT JOIN provider p" +
                        " ON m.providerNo= p.provider_no WHERE m.demographicNo='" + demo + "' AND m.type = '" + type + "'" +
                        "AND v.id = "+mBean.getValidation()+" GROUP BY m.id ORDER BY m.dateObserved DESC," +
                        "m.dateEntered DESC";
                
                log.debug("sql: " + sql);
                ResultSet rs;
                String canPlot = null;
                String firstName;
                String lastName;
                rs = db.GetSQL(sql);
                
                while( rs.next() ){
                    if (rs.getInt("numericValidation")==1 || db.getString(rs,"validationName").compareTo("Blood Pressure")==0)
                        canPlot = "true";
                    else
                        canPlot = null;
                    
                    firstName = db.getString(rs,"provider_first");
                    lastName = db.getString(rs,"provider_last");
                    if (firstName == null && lastName == null){
                        firstName = "Automatic";
                        lastName = "";
                    }
                    //log.debug("canPlot value: " + canPlot);
                    EctMeasurementsDataBean data = new EctMeasurementsDataBean(rs.getInt("id"), db.getString(rs,"type"), mBean.getTypeDisplayName(),mBean.getTypeDesc(), db.getString(rs,"demographicNo"),
                            firstName, lastName,
                            db.getString(rs,"dataField"), db.getString(rs,"measuringInstruction"),
                            db.getString(rs,"comments"), db.getString(rs,"dateObserved"),
                            db.getString(rs,"dateEntered"), canPlot,rs.getDate("dateObserved"),rs.getDate("dateEntered"));
                    measurementsDataVector.add(data);
                    
                }
                
                rs.close();
            }else{
                
                
            }
        } catch(SQLException e) {
            log.debug(e.getMessage());
            verdict = false;
        }
        return verdict;
    }
    
    
    
    public boolean init2(String demo, String type) {
        boolean verdict = true;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql ="SELECT m.id, mt.type, mt.typeDisplayName, mt.typeDescription, m.demographicNo, m.providerNo, m.dataField, m.measuringInstruction,"+
                    "m.comments, m.dateObserved, m.dateEntered , p.first_name AS provider_first, p.last_name AS provider_last," +
                    "v.isNumeric AS numericValidation, v.name AS validationName FROM measurements m, provider p, validations v," +
                    "measurementType mt WHERE m.demographicNo='" + demo + "' AND m.type = '" + type + "' AND m.providerNo= p.provider_no " +
                    "AND m.type = mt.type AND mt.validation = v.id GROUP BY m.id ORDER BY m.dateObserved DESC," +
                    "m.dateEntered DESC";
            log.debug("sql: " + sql);
            ResultSet rs;
            String canPlot = null;
            for(rs = db.GetSQL(sql); rs.next(); ) {
                if (rs.getInt("numericValidation")==1 || db.getString(rs,"validationName").compareTo("Blood Pressure")==0)
                    canPlot = "true";
                else
                    canPlot = null;
                //log.debug("canPlot value: " + canPlot);
                EctMeasurementsDataBean data = new EctMeasurementsDataBean(rs.getInt("id"), db.getString(rs,"type"), db.getString(rs,"typeDisplayName"), db.getString(rs,"typeDescription"), db.getString(rs,"demographicNo"),
                        db.getString(rs,"provider_first"), db.getString(rs,"provider_last"),
                        db.getString(rs,"dataField"), db.getString(rs,"measuringInstruction"),
                        db.getString(rs,"comments"), db.getString(rs,"dateObserved"),
                        db.getString(rs,"dateEntered"), canPlot,rs.getDate("dateObserved"),rs.getDate("dateEntered"));
                measurementsDataVector.add(data);
                
            }
            
            rs.close();
        } catch(SQLException e) {
            log.error(e.getMessage());
            verdict = false;
        }
        return verdict;
    }
    
    public Collection getMeasurementsDataVector(){
        return measurementsDataVector;
    }
    
    public static Hashtable getMeasurementDataById(String id){
        String sql = "SELECT mt.typeDisplayName, mt.typeDescription, m.dataField, m.measuringInstruction,  "
                +" m.comments, m.dateObserved, m.dateEntered , p.first_name AS provider_first, p.last_name AS provider_last "
                +" FROM measurements m LEFT JOIN provider p ON m.providerNo=p.provider_no, measurementType mt "
                +" WHERE m.id = '"+id+"' AND m.type = mt.type ";
        return getHashfromSQL(sql);
    }
    
    public static List<EctMeasurementsDataBean> getMeasurementObjectByType(String type, String demographicNo) {
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "SELECT m.id, m.type, m.demographicNo, m.providerNo, m.dataField, m.measuringInstruction, m.comments," +
                    " m.dateObserved, m.dateEntered, mt.typeDisplayName, mt.typeDescription, p.first_name AS provider_first," +
                    " p.last_name AS provider_last FROM measurements m LEFT JOIN provider p ON m.providerNo=p.provider_no," +
                    " measurementType mt WHERE m.type = mt.type AND m.type = '" + type + "' AND m.demographicNo = " + demographicNo;
            System.out.println("Measurements retreival sql: " + sql);
            ResultSet rs;
            
            ArrayList<EctMeasurementsDataBean> measurements = new ArrayList();
            for (rs = db.GetSQL(sql); rs.next(); ) {
                EctMeasurementsDataBean measurement = new EctMeasurementsDataBean();
                measurement.setId(Integer.parseInt(rsGetString(rs, "id")));
                measurement.setMeasuringInstrc(rsGetString(rs, "measuringInstruction"));
                measurement.setType(rsGetString(rs, "type"));
                measurement.setProviderFirstName(rsGetString(rs, "provider_first"));
                measurement.setProviderLastName(rsGetString(rs, "provider_last"));
                measurement.setDataField(rsGetString(rs, "dataField"));
                measurement.setComments(rsGetString(rs, "comments"));
                measurement.setDateObservedAsDate(rs.getDate("dateObserved"));
                measurement.setDateEnteredAsDate(rs.getDate("dateEntered"));
                measurements.add(measurement);
            }
            rs.close();
            return measurements;
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
        return null;
    }
    
    public static Hashtable getLast(String demo, String type) {
        String sql ="SELECT mt.typeDisplayName, mt.typeDescription, m.dataField, m.measuringInstruction,"+
                "m.comments, m.dateObserved, m.dateEntered , p.first_name AS provider_first, p.last_name AS provider_last " +
                "FROM measurements m, provider p, measurementType mt " +
                "WHERE m.demographicNo='" + demo + "' AND m.type = '" + type + 
                "' AND (m.providerNo = p.provider_no OR m.providerNo = '0') " +
                "AND m.type = mt.type GROUP BY m.id ORDER BY m.dateObserved DESC,m.dateEntered DESC LIMIT 1";
        return getHashfromSQL(sql);
    }
    
//    public static Hashtable getLast(String demo, String type) {
//        try {
//            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
//            String sql ="SELECT mt.typeDisplayName, mt.typeDescription, m.dataField, m.measuringInstruction,"+
//                        "m.comments, m.dateObserved, m.dateEntered , p.first_name AS provider_first, p.last_name AS provider_last " +
//                        "FROM measurements m, provider p, measurementType mt " +
//                        "WHERE m.demographicNo='" + demo + "' AND m.type = '" + type + "' AND m.providerNo= p.provider_no " +
//                        "AND m.type = mt.type GROUP BY m.id ORDER BY m.dateObserved DESC LIMIT 1";
//            ResultSet rs = db.GetSQL(sql);
//            if (rs.next()) {
//                Hashtable data = new Hashtable();
//                data.put("type", rsGetString(rs, "typeDisplayName"));
//                data.put("typeDisplayName", rsGetString(rs, "typeDisplayName"));
//                data.put("typeDescription", rsGetString(rs, "typeDescription"));
//                data.put("value", rsGetString(rs, "dataField"));
//                data.put("measuringInstruction", rsGetString(rs, "measuringInstruction"));
//                data.put("comments", rsGetString(rs, "comments"));
//                data.put("dateObserved", rsGetString(rs, "dateObserved"));
//                data.put("dateObserved_date", rs.getDate("dateObserved"));
//                data.put("dateEntered", rsGetString(rs, "dateEntered"));
//                data.put("dateEntered_date", rs.getDate("dateEntered"));
//                data.put("provider_first", rsGetString(rs, "provider_first"));
//                data.put("provider_last", rsGetString(rs, "provider_last"));
//                rs.close();
//                return data;
//
//            } else {
//                rs.close();
//                return null;
//            }
//        }
//        catch(SQLException e) {
//            log.debug(e.getMessage());
//            return null;
//        }
//    }
//
    
    
    private static Hashtable getHashfromSQL(String sql){
        Hashtable data = null;
        log.debug(sql);
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
                data = new Hashtable();
                data.put("type", rsGetString(rs, "typeDisplayName"));
                data.put("typeDisplayName", rsGetString(rs, "typeDisplayName"));
                data.put("typeDescription", rsGetString(rs, "typeDescription"));
                data.put("value", rsGetString(rs, "dataField"));
                data.put("measuringInstruction", rsGetString(rs, "measuringInstruction"));
                data.put("comments", rsGetString(rs, "comments"));
                data.put("dateObserved", rsGetString(rs, "dateObserved"));
                data.put("dateObserved_date", rs.getDate("dateObserved"));
                data.put("dateEntered", rsGetString(rs, "dateEntered"));
                data.put("dateEntered_date", rs.getDate("dateEntered"));
                data.put("provider_first", rsGetString(rs, "provider_first"));
                data.put("provider_last", rsGetString(rs, "provider_last"));
            }
            rs.close();
        } catch(SQLException e) {
            log.error(e.getMessage());
        }
        return data;
    }
    
    private static String rsGetString(ResultSet rs, String column) throws SQLException {
        //protects agianst null values;
        String thisStr = oscar.Misc.getString(rs,column);
        if (thisStr == null) return "";
        return thisStr;
    }
}

