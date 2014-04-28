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


package oscar.oscarEncounter.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class EctARRecord {

    public Properties getARRecord(int demographicNo, int existingID) throws SQLException {
        Properties properties = new Properties();
        
        if( existingID <= 0) {
            String s = "SELECT demographic_no, CONCAT(last_name, ', ', first_name) AS pName, sex, CONCAT(address, ', ', city, ', ', province, ' ', postal) AS address, phone, phone2, year_of_birth, month_of_birth, date_of_birth FROM demographic WHERE demographic_no = " + demographicNo;
            ResultSet resultset = DBHandler.GetSQL(s);
            if(resultset.next()) {
                java.util.Date date = UtilDateUtilities.calcDate(resultset.getString("year_of_birth"), resultset.getString("month_of_birth"), resultset.getString("date_of_birth"));
                properties.setProperty("demographic_no", resultset.getString("demographic_no"));
                properties.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(),"yyyy/MM/dd"));
                properties.setProperty("formEdited", UtilDateUtilities.DateToString(new Date(),"yyyy/MM/dd"));
                properties.setProperty("c_pName", resultset.getString("pName"));
                properties.setProperty("c_address", resultset.getString("address"));
                properties.setProperty("pg1_dateOfBirth", UtilDateUtilities.DateToString(date,"yyyy/MM/dd"));
                properties.setProperty("pg1_age", String.valueOf(UtilDateUtilities.calcAge(date)));
                properties.setProperty("pg1_homePhone", resultset.getString("phone"));
                properties.setProperty("pg1_workPhone", resultset.getString("phone2"));
                properties.setProperty("pg1_formDate", UtilDateUtilities.DateToString(new Date(),"yyyy/MM/dd"));
            }
            resultset.close();
        } else {
            String s1 = "SELECT * FROM formAR WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
            ResultSet resultset1 = DBHandler.GetSQL(s1);
            if(resultset1.next()) {
                ResultSetMetaData resultsetmetadata = resultset1.getMetaData();
                for(int k = 1; k <= resultsetmetadata.getColumnCount(); k++) {
                    String name = resultsetmetadata.getColumnName(k);
                    String value = null;
                    if(resultsetmetadata.getColumnTypeName(k).equalsIgnoreCase("TINY")) {
                        if(resultset1.getInt(k) == 1)
                            value = "checked='checked'";
                        else
                            value = "";
                    } else if(resultsetmetadata.getColumnTypeName(k).equalsIgnoreCase("date")) {
                        value = UtilDateUtilities.DateToString(resultset1.getDate(k),"yyyy/MM/dd");
                        if(value == null)
                            value = UtilDateUtilities.DateToString(new Date(),"yyyy/MM/dd");
                    } else {
                        value = resultset1.getString(k);
                    }
                    if(value != null) properties.setProperty(name, value);
                }

            }
            resultset1.close();
        }
        return properties;
    }

    public int saveARRecord(Properties properties, String formId) throws SQLException {
        String demographic_no = properties.getProperty("demographic_no");
        String temp = properties.getProperty("c_lastVisited");
        String page = temp.substring(0, 3).toLowerCase() + "_";
        

        String sqlDB = "SELECT * FROM formAR WHERE demographic_no=" + demographic_no + " AND ID=" + formId;
        ResultSet resultset = DBHandler.GetSQL(sqlDB);
        resultset.next();
        String sqlNew = "SELECT * FROM formAR WHERE demographic_no=" + demographic_no + " AND ID=0";
        ResultSet resultset1 = DBHandler.GetSQL(sqlNew, true);
        resultset1.moveToInsertRow();
        ResultSetMetaData resultsetmetadata = resultset1.getMetaData();

        for(int i = 1; i <= resultsetmetadata.getColumnCount(); i++) {
            String name = resultsetmetadata.getColumnName(i);
            if(name.equalsIgnoreCase("ID"))
                resultset1.updateNull(name);
            else if(name.equalsIgnoreCase("formEdited")) {
                java.util.Date date = new Date();
                resultset1.updateDate(name, new java.sql.Date(date.getTime()));
            } else if(name.equalsIgnoreCase("formCreated")) {
                java.util.Date date1 = UtilDateUtilities.StringToDate(properties.getProperty(name, null),"yyyy/MM/dd");
                resultset1.updateDate(name, new java.sql.Date(date1.getTime()));
            } else if(name.equalsIgnoreCase("demographic_no"))
                resultset1.updateString(name, properties.getProperty("demographic_no"));
            else if(name.equalsIgnoreCase("provider_no"))
                resultset1.updateString(name, properties.getProperty("provider_no"));
            else if(name.startsWith("c_") || name.startsWith(page) || page.equalsIgnoreCase("pg2_") && name.startsWith("ar2") || page.equalsIgnoreCase("pg3_") && name.startsWith("ar2")) {
                String value = properties.getProperty(name, null);
                if(resultsetmetadata.getColumnTypeName(i).equalsIgnoreCase("TINY")) {
                    if(value != null) {
                        if(value.equalsIgnoreCase("on"))
                            resultset1.updateInt(name, 1);
                        else
                            resultset1.updateInt(name, 0);
                    } else {
                        resultset1.updateInt(name, 0);
                    }
                } else if(resultsetmetadata.getColumnTypeName(i).equalsIgnoreCase("date")) {
                    java.util.Date date2 = UtilDateUtilities.StringToDate(value,"yyyy/MM/dd");
                    if(date2 == null)
                        resultset1.updateNull(name);
                    else
                        resultset1.updateDate(name, new java.sql.Date(date2.getTime()));
                } else if(value == null)
                    resultset1.updateNull(name);
                else
                    resultset1.updateString(name, value);// col does not begin with c_ or p#_
            } else if(!formId.equalsIgnoreCase("0")) {
                String value = resultset.getString(name);
                if(value != null)
                    resultset1.updateString(name, value);
                else
                    resultset1.updateNull(name);
            } else {
                resultset1.updateNull(name);
            }
        }

        resultset.close();
        resultset1.insertRow();
        resultset1.close();
        int j = 0;
        sqlNew = "SELECT LAST_INSERT_ID()";
        resultset1 = DBHandler.GetSQL(sqlNew);
        if(resultset1.next())
            j = resultset1.getInt(1);
        resultset1.close();
        return j;
    }

    public Properties getARPrintRecord(int demographicNo, int existingID) throws SQLException {
        Properties properties = new Properties();
        
        String s = "SELECT * FROM formAR WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
        ResultSet resultset = DBHandler.GetSQL(s);
        if(resultset.next()) {
            ResultSetMetaData resultsetmetadata = resultset.getMetaData();
            for(int k = 1; k <= resultsetmetadata.getColumnCount(); k++) {
                String name = resultsetmetadata.getColumnName(k);
                String value;
                if(resultsetmetadata.getColumnTypeName(k).equalsIgnoreCase("TINY")) {
                    if(resultset.getInt(k) == 1)
                        value = "X";
                    else
                        value = "";
                } else if(resultsetmetadata.getColumnTypeName(k).equalsIgnoreCase("date"))
                    value = UtilDateUtilities.DateToString(resultset.getDate(k),"yyyy/MM/dd");
                else
                    value = resultset.getString(k);
                if(value != null)
                    properties.setProperty(name, value);
            }

        }
        resultset.close();
        return properties;
    }
}
