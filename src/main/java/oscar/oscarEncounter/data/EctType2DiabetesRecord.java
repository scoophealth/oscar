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

public class EctType2DiabetesRecord {
    public Properties getType2DiabetesRecord(int demographicNo, int existingID)
        throws SQLException    {
        Properties props = new Properties();
        
        if(existingID <= 0) {
            String sql = "SELECT demographic_no, CONCAT(last_name, ', ', first_name) AS pName, year_of_birth, month_of_birth, date_of_birth FROM demographic WHERE demographic_no = " +demographicNo;
            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next())
            {
                Date dob = UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"), oscar.Misc.getString(rs, "date_of_birth"));
                props.setProperty("demographic_no", oscar.Misc.getString(rs, "demographic_no"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                props.setProperty("formEdited", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                props.setProperty("birthDate", UtilDateUtilities.DateToString(dob, "yyyy/MM/dd"));
                props.setProperty("pName", oscar.Misc.getString(rs, "pName"));
            }
            rs.close();
        } else {
            String sql = "SELECT * FROM formType2Diabetes WHERE demographic_no = " +demographicNo +" AND ID = " +existingID;
            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next()) {
                ResultSetMetaData md = rs.getMetaData();
                for(int i = 1; i <= md.getColumnCount(); i++) {
                    String name = md.getColumnName(i);
                    String value;
                    if(md.getColumnTypeName(i).equalsIgnoreCase("TINY") && md.getScale(i) == 1) {
                        if(rs.getInt(i) == 1)
                            value = "checked='checked'";
                        else
                            value = "";
                    } else if(md.getColumnTypeName(i).equalsIgnoreCase("date"))
                        value = UtilDateUtilities.DateToString(rs.getDate(i), "yyyy/MM/dd");
                    else
                        value = oscar.Misc.getString(rs, i);
                    if(value != null)
                        props.setProperty(name, value);
                }

            }
            rs.close();
        }
        return props;
    }

    public int saveType2DiabetesRecord(Properties props) throws SQLException {

	/* if database = postgres, make a properties with ignore case */
	String db_type = oscar.OscarProperties.getInstance() .getProperty("db_type") .trim();
	if (db_type.equalsIgnoreCase("postgresql")) {
	    Properties temp = new Properties();
	    java.util.Enumeration varEnum = props.propertyNames();
	    while (varEnum.hasMoreElements()){
		String parameter = ((String) varEnum.nextElement());
		temp.setProperty(parameter.toLowerCase(), props.getProperty(parameter));
	    }
	    props = temp;
	}
        String demographic_no = props.getProperty("demographic_no");
        
        String sql = "SELECT * FROM formType2Diabetes WHERE demographic_no="+demographic_no+" AND ID=0";
        ResultSet rs = DBHandler.GetSQL(sql, true);
        rs.moveToInsertRow();
        ResultSetMetaData md = rs.getMetaData();

        for(int i = 1; i <= md.getColumnCount(); i++) {
            String name = md.getColumnName(i);
            if(name.equalsIgnoreCase("ID")) {
                rs.updateNull(name);
                continue;
            }
            String value = props.getProperty(name, null);
            if((md.getColumnTypeName(i).equalsIgnoreCase("TINY") && md.getScale(i) == 1)
	       || md.getColumnTypeName(i).trim().equalsIgnoreCase("numeric")
	       || md.getColumnTypeName(i).equalsIgnoreCase("int2")) {
                if(value != null) {
                    if(value.equalsIgnoreCase("on"))
                        rs.updateInt(name, 1);
                    else
                        rs.updateInt(name, Integer.parseInt(value));
                } else {
                    rs.updateInt(name, 0);
                }
                continue;
            }
            if(md.getColumnTypeName(i).equalsIgnoreCase("date")) {
                Date d;
                if(md.getColumnName(i).equalsIgnoreCase("formEdited"))  {
                    d = new Date();
                } else {
                    d = UtilDateUtilities.StringToDate(value, "yyyy/MM/dd");
                }
                if(d == null)
                    rs.updateNull(name);
                else
                    rs.updateDate(name, new java.sql.Date(d.getTime()));
                continue;
            }
            if(value == null)
                rs.updateNull(name);
            else
                rs.updateString(name, value);
        }

	
        int ret = 0;
	/* another fix */
	if (db_type.equalsIgnoreCase("postgresql")) {
	    ResultSet rs1 = DBHandler.GetSQL("select nextval('formtype2diabetes_numeric_se')");
	    rs1.next();
	    ret = rs1.getInt(1);
	    rs.updateInt("id", ret);	    
	    rs1.close();
	}
        rs.insertRow();
        rs.close();

	if (db_type.equalsIgnoreCase("mysql")) {
	    sql = "SELECT LAST_INSERT_ID()";
	    rs = DBHandler.GetSQL(sql);
	    if(rs.next())
		ret = rs.getInt(1);
	    rs.close();
	}
	return ret;
    }
}
