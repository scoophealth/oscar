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


package oscar.form;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmRourkeRecord extends FrmRecord {
    private static Logger logger=MiscUtils.getLogger(); 

	
    public Properties getFormRecord(LoggedInInfo loggedInInfo, int demographicNo, int existingID)
            throws SQLException    {
        Properties props = new Properties();

        if(existingID <= 0) {
			
            String sql = "SELECT demographic_no, CONCAT(last_name, ', ', first_name) AS pName, "
                + "year_of_birth, month_of_birth, date_of_birth, sex "
                + "FROM demographic WHERE demographic_no = " + demographicNo;
            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next()) {
                props.setProperty("demographic_no", oscar.Misc.getString(rs, "demographic_no"));
                props.setProperty("c_pName", oscar.Misc.getString(rs, "pName"));
                props.setProperty("formDate", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                //props.setProperty("formEdited", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                java.util.Date dob = UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"), oscar.Misc.getString(rs, "date_of_birth"));
                props.setProperty("c_birthDate", UtilDateUtilities.DateToString(dob, "yyyy/MM/dd"));
                //props.setProperty("age", String.valueOf(UtilDateUtilities.calcAge(dob)));
            }
            rs.close();
        } else {
            String sql = "SELECT * FROM formRourke WHERE demographic_no = " +demographicNo +" AND ID = " +existingID;
			props = (new FrmRecordHelp()).getFormRecord(sql);
        }

        return props;
    }

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formRourke WHERE demographic_no=" +demographic_no +" AND ID=0";

		return ((new FrmRecordHelp()).saveFormRecord(props, sql));
    }

//////////////new/ Done By Jay////
    public boolean isFemale(int demo){
	boolean retval = false;
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

    public Properties getGraph(int demographicNo, int existingID)  {
        Properties props = new Properties();

        
        ResultSet rs;
        String sql;

        if(existingID==0) {
            return props;
        }  else {
            sql = "SELECT c_pName, c_birthDate, c_birthWeight, c_headCirc, c_length, "
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

            
            	try {
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
                } catch (SQLException e) {

                	MiscUtils.getLogger().error("", e);
                }
            
            
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


    public String findActionValue(String submit) throws SQLException {
 		return ((new FrmRecordHelp()).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
 		return ((new FrmRecordHelp()).createActionURL(where, action, demoId, formId));
    }

}
