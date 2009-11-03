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
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class EctMMSERecord {
    public Properties getMMSERecord(int demographicNo, int existingID)  throws SQLException  {
        Properties props = new Properties();

        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        ResultSet rs;
        String sql;

        if(existingID <= 0) {
            sql = "SELECT demographic_no, CONCAT(last_name, ', ', first_name) AS pName, "
                + "sex, year_of_birth, month_of_birth, date_of_birth "
                + "FROM demographic WHERE demographic_no = " + demographicNo;

            rs = db.GetSQL(sql);

            if(rs.next()) {
                java.util.Date dob = UtilDateUtilities.calcDate(db.getString(rs,"year_of_birth"), db.getString(rs,"month_of_birth"), db.getString(rs,"date_of_birth"));

                props.setProperty("demographic_no", db.getString(rs,"demographic_no"));
                props.setProperty("pName", db.getString(rs,"pName"));
                props.setProperty("formDate", UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy/MM/dd"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy/MM/dd"));
                props.setProperty("formEdited", UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy/MM/dd"));
                props.setProperty("sex", db.getString(rs,"sex"));
                props.setProperty("age", String.valueOf(UtilDateUtilities.calcAge(dob)));
            }

            rs.close();
        } else {
            sql = "SELECT * FROM formMMSE WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;

            rs = db.GetSQL(sql);

            if(rs.next())
            {
                ResultSetMetaData md = rs.getMetaData();

                for(int i=1; i<=md.getColumnCount(); i++)
                {
                    String name = md.getColumnName(i);

                    String value;

                    if(md.getColumnTypeName(i).equalsIgnoreCase("TINY"))
                    {
                        if(rs.getInt(i)==1)
                        {
                            value = "checked='checked'";
                        }
                        else
                        {
                            value = "";
                        }
                    }
                    else
                    {
                        if(md.getColumnTypeName(i).equalsIgnoreCase("date"))
                        {
                            value = UtilDateUtilities.DateToString(rs.getDate(i), "yyyy/MM/dd");
                        }
                        else
                        {
                            value = db.getString(rs,i);
                        }
                    }

                    if(value!=null)
                    {
                        props.setProperty(name, value);
                    }
                }
            }

            rs.close();
        }
        return props;
    }

    public int saveMMSERecord(Properties props)  throws SQLException  {
        String demographic_no = props.getProperty("demographic_no");

        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        String sql="SELECT * FROM formMMSE WHERE demographic_no=" + demographic_no + " AND ID=0";
        ResultSet rs = db.GetSQL(sql, true);

        rs.moveToInsertRow();

        ResultSetMetaData md = rs.getMetaData();

        for(int i=1; i<=md.getColumnCount(); i++)
        {
            String name = md.getColumnName(i);

            if(name.equalsIgnoreCase("ID"))
            {
                rs.updateNull(name);
            }
            else
            {
                String value = props.getProperty(name, null);

                if(md.getColumnTypeName(i).equalsIgnoreCase("TINY"))
                {
                    if(value!=null)
                    {
                        if(value.equalsIgnoreCase("on"))
                        {
                            rs.updateInt(name, 1);
                        }
                        else
                        {
                            rs.updateInt(name, 0);
                        }
                    }
                    else
                    {
                        rs.updateInt(name, 0);
                    }
                }
                else
                {
                    if(md.getColumnTypeName(i).equalsIgnoreCase("date"))
                    {
                        java.util.Date d;

                        if(md.getColumnName(i).equalsIgnoreCase("formEdited"))
                        {
                            d = UtilDateUtilities.Today();
                        }
                        else
                        {
                            d = UtilDateUtilities.StringToDate(value, "yyyy/MM/dd");
                        }

                        if(d==null)
                        {
                            rs.updateNull(name);
                        }
                        else
                        {
                            rs.updateDate(name, new java.sql.Date(d.getTime()));
                        }
                    }
                    else
                    {
                        if(value==null)
                        {
                            rs.updateNull(name);
                        }
                        else
                        {
                            rs.updateString(name, value);
                        }
                    }
                }
            }
        }

        rs.insertRow();

        rs.close();

        int ret = 0;

        sql = "SELECT LAST_INSERT_ID()";
        rs = db.GetSQL(sql);
        if(rs.next())
        {
            ret = rs.getInt(1);
        }
        rs.close();
        return ret;
    }
}
