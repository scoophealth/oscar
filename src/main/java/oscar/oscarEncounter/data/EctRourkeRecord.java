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

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class EctRourkeRecord {
    private static Logger logger=MiscUtils.getLogger(); 

    public Properties getRourkeRecord(int demographicNo, int existingID)
            throws SQLException    {
        Properties props = new Properties();

        
        ResultSet rs;
        String sql;

        if(existingID <= 0) {
            sql = "SELECT demographic_no, CONCAT(last_name, ', ', first_name) AS pName, "
                + "year_of_birth, month_of_birth, date_of_birth, sex "
                + "FROM demographic WHERE demographic_no = " + demographicNo;

            rs = DBHandler.GetSQL(sql);

            if(rs.next())  {
                java.util.Date dob = UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"), oscar.Misc.getString(rs, "date_of_birth"));

                props.setProperty("demographic_no", oscar.Misc.getString(rs, "demographic_no"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                props.setProperty("formEdited", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                props.setProperty("c_birthDate", UtilDateUtilities.DateToString(dob, "yyyy/MM/dd"));
                props.setProperty("c_pName", oscar.Misc.getString(rs, "pName"));
                if(oscar.Misc.getString(rs, "sex").equalsIgnoreCase("M")) {
                    props.setProperty("c_male", "checked='checked'");
                } else {
                    props.setProperty("c_female", "checked='checked'");
                }
            }
            rs.close();
        } else {
            sql = "SELECT * FROM formRourke WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
            rs = DBHandler.GetSQL(sql);

            if(rs.next()) {
                ResultSetMetaData md = rs.getMetaData();
                for(int i=1; i<=md.getColumnCount(); i++) {
                    String name = md.getColumnName(i);
                    String value;

                    if(md.getColumnTypeName(i).equalsIgnoreCase("TINY"))
//                            && md.getPrecision(i)==1)
                    {
                        if(rs.getInt(i)==1) {
                            value = "checked='checked'";
                        } else {
                            value = "";
                        }
                    } else {
                        if(md.getColumnTypeName(i).equalsIgnoreCase("date")) {
                            value = UtilDateUtilities.DateToString(rs.getDate(i), "yyyy/MM/dd");
                        } else {
                            value = oscar.Misc.getString(rs, i);
                        }
                    }

                    if(value!=null) {
                        props.setProperty(name, value);
                    }
                }
            }
            rs.close();
        }
        return props;
    }

    public int saveRourkeRecord(Properties props, String formId) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String page = "p"+props.getProperty("c_lastVisited")+"_";

        
        String sqlDB = "SELECT * FROM formRourke WHERE demographic_no=" + demographic_no + " AND ID=" + formId;
        ResultSet rsDB = DBHandler.GetSQL(sqlDB);
        rsDB.next();

        String sqlPage="SELECT * FROM formRourke WHERE demographic_no=" + demographic_no + " AND ID=0";
        ResultSet rsPage = DBHandler.GetSQL(sqlPage, true);
        rsPage.moveToInsertRow();

        ResultSetMetaData md = rsPage.getMetaData();

        for(int i=1; i<=md.getColumnCount(); i++) {
            String name = md.getColumnName(i);

            if(name.equalsIgnoreCase("ID")) {
                rsPage.updateNull(name);
            } else if(name.equalsIgnoreCase("formEdited")) {
                java.util.Date d = new Date();
                rsPage.updateDate(name, new java.sql.Date(d.getTime()));
            } else if(name.equalsIgnoreCase("formCreated")) {
                java.util.Date d = UtilDateUtilities.StringToDate(props.getProperty(name, null), "yyyy/MM/dd");
                rsPage.updateDate(name, new java.sql.Date(d.getTime()));
            } else if(name.equalsIgnoreCase("demographic_no"))  {
                rsPage.updateString(name, props.getProperty("demographic_no"));
            } else if(name.equalsIgnoreCase("provider_no")) {
                rsPage.updateString(name, props.getProperty("provider_no"));
            } else if(name.startsWith("c_") || name.startsWith(page)) {
                String value = props.getProperty(name, null);
                if(md.getColumnTypeName(i).equalsIgnoreCase("TINY"))
//                        && md.getPrecision(i)==1)
                {
                    if(value!=null) {
                        if(value.equalsIgnoreCase("on"))  {
                            rsPage.updateInt(name, 1);
                        } else {
                            rsPage.updateInt(name, 0);
                        }
                    } else {
                        rsPage.updateInt(name, 0);
                    }
                } else {
                    if(md.getColumnTypeName(i).equalsIgnoreCase("date")) {
                        java.util.Date d = UtilDateUtilities.StringToDate(value, "yyyy/MM/dd");

                        if(d==null)   {
                            rsPage.updateNull(name);
                        } else {
                            rsPage.updateDate(name, new java.sql.Date(d.getTime()));
                        }
                    } else {
                        if(value==null) {
                            rsPage.updateNull(name);
                        } else {
                            rsPage.updateString(name, value);
                        }
                    }
                }
            }// col does not begin with c_ or p#_
            else if(!formId.equalsIgnoreCase("0"))  {
                String value = rsDB.getString(name);

                if(value!=null) {
                    rsPage.updateString(name, value);
                } else {
                    rsPage.updateNull(name);
                }
            } else {
                rsPage.updateNull(name);
            }
        }

        rsDB.close();
        rsPage.insertRow();
        rsPage.close();

        int ret = 0;

        sqlPage = "SELECT LAST_INSERT_ID()";
        rsPage = DBHandler.GetSQL(sqlPage);
        if(rsPage.next()) {
            ret = rsPage.getInt(1);
        }
        rsPage.close();
        return ret;
    }

    public Properties getGraph(int demographicNo, int existingID)  throws SQLException {
        Properties props = new Properties();

        
        ResultSet rs;
        String sql;

        if(existingID==0) {
            return props;
        }  else {
            sql = "SELECT c_pName, c_birthDate, c_birthWeight, c_headCirc, c_length, c_female,"
                + "p1_date1w, p1_date2w, p1_date1m, p1_date2m, "
                + "p2_date4m, p2_date6m, p2_date9m, p2_date12m, p3_date18m, p3_date2y, "
                + "p1_hc1w, p1_hc2w, p1_hc1m, p1_hc2m, "
                + "p2_hc4m, p2_hc6m, p2_hc9m, p2_hc12m, p3_hc18m, "
                + "p1_wt1w, p1_wt2w, p1_wt1m, p1_wt2m, "
                + "p2_wt4m, p2_wt6m, p2_wt9m, p2_wt12m, p3_wt18m, p3_wt2y, "
                + "p1_ht1w, p1_ht2w, p1_ht1m, p1_ht2m, "
                + "p2_ht4m, p2_ht6m, p2_ht9m, p2_ht12m, p3_ht18m, p3_ht2y "
                + "FROM formRourke "
                + "WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;

            rs = DBHandler.GetSQL(sql);

            if(rs.next())           {
                ResultSetMetaData md = rs.getMetaData();
                String value;

                for(int i=1; i<=md.getColumnCount(); i++)            {
                    String name = md.getColumnName(i);

                    if(md.getColumnTypeName(i).equalsIgnoreCase("date"))               {
                        value = UtilDateUtilities.DateToString(rs.getDate(i), "yyyy/MM/dd");
                    } else {
                        value = oscar.Misc.getString(rs, i);
                    }

                    if(i<=6) {
                        name = name.substring(2);
                    }  else {
                        name = name.substring(3);
                    }

                    if(value!=null) {
                        props.setProperty(name, value);
                    }
                }//end for

            }//end if
            rs.close();
        }
        return props;
    }

    public static double age(String dob, String today) {
        double age = -1;

        try {
            Date tToday = (oscar.util.UtilDateUtilities.StringToDate(today, "yyyy/MM/dd"));

            Date tDob = (oscar.util.UtilDateUtilities.StringToDate(dob, "yyyy/MM/dd"));

            age = (tToday.getTime() - tDob.getTime())/(1000*3600*24);
            double daysPerMonth = 30.4375;
            age = age/daysPerMonth; // the approximate number of days in a month
        } catch(Exception ex) {
            logger.error("", ex);
        }
        return age;
    }




//////////////new/ Done By Jay////
    public boolean isFemale(int demo){
        boolean retval = false;
        DBHandler db;
        ResultSet rs;
        String str = "M";
        try{
                
                rs = DBHandler.GetSQL("select sex from demographic where demographic_no = "+demo);
                if(rs.next()){
                        str = oscar.Misc.getString(rs, "sex");
                        if (str.equalsIgnoreCase("F")){
                                retval = true;
                        }
                }
        rs.close();
        }catch(Exception exc){MiscUtils.getLogger().error("Error", exc);}
        return retval;
    }
///////////////////////////////////
}
