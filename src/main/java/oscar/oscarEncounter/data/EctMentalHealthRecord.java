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

public class EctMentalHealthRecord
{
    public Properties getMentalHealthRecord(int demographicNo, int existingID, int provNo)
            throws SQLException    {
        Properties props = new Properties();

        
        ResultSet rs;
        String sql;

        if(existingID <= 0)        {
            sql = "SELECT demographic_no, CONCAT(last_name, ', ', first_name) AS pName, "
                + "sex, CONCAT(address, ', ', city, ', ', province, ' ', postal) AS address, "
                + "phone, year_of_birth, month_of_birth, date_of_birth "
                + "FROM demographic WHERE demographic_no = " + demographicNo;

            rs = DBHandler.GetSQL(sql);

            if(rs.next())    {
                java.util.Date dob = UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"), oscar.Misc.getString(rs, "date_of_birth"));

                props.setProperty("demographic_no", oscar.Misc.getString(rs, "demographic_no"));
                props.setProperty("c_pName", oscar.Misc.getString(rs, "pName"));
                props.setProperty("c_sex", oscar.Misc.getString(rs, "sex"));
                props.setProperty("c_referralDate", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                props.setProperty("formEdited", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                props.setProperty("c_address", oscar.Misc.getString(rs, "address"));
                props.setProperty("c_birthDate", UtilDateUtilities.DateToString(dob, "yyyy/MM/dd"));
                props.setProperty("c_homePhone", oscar.Misc.getString(rs, "phone"));
            }
            rs.close();

            // from provider table
            sql = "SELECT CONCAT(last_name, ', ', first_name) AS provName "
                + "FROM provider WHERE provider_no = " + provNo;

            rs = DBHandler.GetSQL(sql);

            if(rs.next()) {
                props.setProperty("c_referredBy", oscar.Misc.getString(rs, "provName"));
            }
            rs.close();
        } else {
            sql = "SELECT * FROM formMentalHealth WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;

            rs = DBHandler.GetSQL(sql);

            if(rs.next()) {
                ResultSetMetaData md = rs.getMetaData();

                for(int i=1; i<=md.getColumnCount(); i++) {
                    String name = md.getColumnName(i);

                    String value = null;

                    if(md.getColumnTypeName(i).equalsIgnoreCase("TINY")) {
                        if(rs.getInt(i)==1)   {
                            value = "checked='checked'";
                        }   else  {
                            value = "";
                        }
                    } else  {
                        if(md.getColumnTypeName(i).equalsIgnoreCase("date")) {
                            value = UtilDateUtilities.DateToString(rs.getDate(i), "yyyy/MM/dd");
                            if(value==null)  {
                                value = UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd");
                            }
                        }  else  {
                            value = oscar.Misc.getString(rs, i);
                        }
                    }

                    if(value!=null)  {
                        props.setProperty(name, value);
                    }
                }
            }
            rs.close();
        }
        return props;
    }

    public int saveMentalHealthRecord(Properties props, String formId) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String temp = props.getProperty("c_lastVisited");
        String page = temp.substring(0,1).toLowerCase()+"_";

        

        String sqlDB = "SELECT * FROM formMentalHealth WHERE demographic_no=" + demographic_no + " AND ID=" + formId;
        ResultSet rsDB = DBHandler.GetSQL(sqlDB);
        rsDB.next();

        String sqlNew="SELECT * FROM formMentalHealth WHERE demographic_no=" + demographic_no + " AND ID=0";
        ResultSet rsNew = DBHandler.GetSQL(sqlNew, true);
        rsNew.moveToInsertRow();

        ResultSetMetaData md = rsNew.getMetaData();

        for(int i=1; i<=md.getColumnCount(); i++)     {
            String name = md.getColumnName(i);

            if(name.equalsIgnoreCase("ID"))  {
                rsNew.updateNull(name);
            } else if(name.equalsIgnoreCase("formEdited")) {
                java.util.Date d = new Date();
                rsNew.updateDate(name, new java.sql.Date(d.getTime()));
            } else if(name.equalsIgnoreCase("formCreated"))  {
                java.util.Date d = UtilDateUtilities.StringToDate(props.getProperty(name, null), "yyyy/MM/dd");
                rsNew.updateDate(name, new java.sql.Date(d.getTime()));
            }
            else if(name.equalsIgnoreCase("demographic_no"))
            {
                rsNew.updateString(name, props.getProperty("demographic_no"));
            }
            else if(name.equalsIgnoreCase("provider_no"))
            {
                rsNew.updateString(name, props.getProperty("provider_no"));
            }
            else if(name.startsWith("c_") || name.startsWith(page))
            {
                String value = props.getProperty(name, null);

                if(md.getColumnTypeName(i).equalsIgnoreCase("TINY"))
                {
                    if(value!=null)
                    {
                        if(value.equalsIgnoreCase("on"))
                        {
                            rsNew.updateInt(name, 1);
                        }
                        else
                        {
                            rsNew.updateInt(name, 0);
                        }
                    }
                    else
                    {
                        rsNew.updateInt(name, 0);
                    }
                }
                else
                {
                    if(md.getColumnTypeName(i).equalsIgnoreCase("date"))
                    {
                        java.util.Date d = UtilDateUtilities.StringToDate(value, "yyyy/MM/dd");

                        if(d==null)
                        {
                            rsNew.updateNull(name);
                        }
                        else
                        {
                            rsNew.updateDate(name, new java.sql.Date(d.getTime()));
                        }
                    }
                    else
                    {
                        if(value==null)
                        {
                            rsNew.updateNull(name);
                        }
                        else
                        {
                            rsNew.updateString(name, value);
                        }
                    }
                }
            }// col does not begin with c_ or p#_
            else if(!formId.equalsIgnoreCase("0"))
            {
                String value = rsDB.getString(name);

                if(value!=null)
                {
                    rsNew.updateString(name, value);
                }
                else
                {
                    rsNew.updateNull(name);
                }
            }
            else
            {
                rsNew.updateNull(name);
            }
        }

        rsDB.close();
        rsNew.insertRow();
        rsNew.close();

        int ret = 0;

        sqlNew = "SELECT LAST_INSERT_ID()";
        rsNew = DBHandler.GetSQL(sqlNew);
        if(rsNew.next())
        {
            ret = rsNew.getInt(1);
        }
        rsNew.close();
        return ret;
    }
}
