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

import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmRourke2006Record extends FrmRecord {
    public Properties getFormRecord(LoggedInInfo loggedInInfo, int demographicNo, int existingID)
            throws SQLException    {
        Properties props = new Properties();
        
        String updated = "false";
        if(existingID <= 0) {			
            String sql = "SELECT demographic_no, CONCAT(last_name, ', ', first_name) AS pName, "
                + "year_of_birth, month_of_birth, date_of_birth, sex "
                + "FROM demographic WHERE demographic_no = " + demographicNo;
            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next()) {
                props.setProperty("demographic_no", oscar.Misc.getString(rs, "demographic_no"));
                props.setProperty("c_pName", oscar.Misc.getString(rs, "pName"));
                //props.setProperty("formDate", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(), "dd/MM/yyyy"));
                //props.setProperty("formEdited", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                java.util.Date dob = UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"), oscar.Misc.getString(rs, "date_of_birth"));
                props.setProperty("c_birthDate", UtilDateUtilities.DateToString(dob, "dd/MM/yyyy"));
                //props.setProperty("age", String.valueOf(UtilDateUtilities.calcAge(dob)));
            }
            rs.close();
        } else {
            String sql = "SELECT * FROM formRourke2006 WHERE demographic_no = " +demographicNo +" AND ID = " +existingID;
            FrmRecordHelp frmRec = new FrmRecordHelp();
            frmRec.setDateFormat("dd/MM/yyyy");
            props = frmRec.getFormRecord(sql);
            sql = "SELECT demographic_no, CONCAT(last_name, ', ', first_name) AS pName, "
                + "year_of_birth, month_of_birth, date_of_birth, sex "
                + "FROM demographic WHERE demographic_no = " + demographicNo;
            ResultSet rs = DBHandler.GetSQL(sql);
            
            if(rs.next()) {
                String rourkeVal = props.getProperty("c_pName","");
                String demoVal = oscar.Misc.getString(rs, "pName");
                
                if( !rourkeVal.equals(demoVal) ) {
                    props.setProperty("c_pName", demoVal);
                    updated = "true";
                }
                
                rourkeVal = props.getProperty("c_birthDate","");
                java.util.Date dob = UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"), oscar.Misc.getString(rs, "date_of_birth"));
                demoVal = UtilDateUtilities.DateToString(dob, "dd/MM/yyyy");
                
                if( !rourkeVal.equals(demoVal) ) {
                    props.setProperty("c_birthDate", demoVal);
                    updated = "true";
                }
            }            
        }
        props.setProperty("updated", updated);
        return props;
    }

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formRourke2006 WHERE demographic_no=" +demographic_no +" AND ID=0";
        FrmRecordHelp frmRec = new FrmRecordHelp();
        frmRec.setDateFormat("dd/MM/yyyy"); 
        
        return frmRec.saveFormRecord(props, sql);
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

    public Properties getGraph(int demographicNo, int existingID) {
        Properties props = new Properties();

        
        ResultSet rs;
        String sql;

        if(existingID==0) {
            return props;
        }  else {
            sql = "SELECT c_pName, c_birthDate, c_birthWeight, c_headCirc, c_length, "
                + "p1_date1w, p1_date2w, p1_date1m, p2_date2m, p2_date4m, p2_date6m, p3_date9m, p3_date12m, p3_date15m, p4_date18m, p4_date24m, "
                + "p1_hc1w, p1_hc2w, p1_hc1m, p2_hc2m, p2_hc4m, p2_hc6m, p3_hc9m, p3_hc12m, p3_hc15m, p4_hc18m, p4_hc24m, "                
                + "p1_wt1w, p1_wt2w, p1_wt1m, p2_wt2m, p2_wt4m, p2_wt6m, p3_wt9m, p3_wt12m, p3_wt15m, p4_wt18m, p4_wt24m, "                
                + "p1_ht1w, p1_ht2w, p1_ht1m, p2_ht2m, p2_ht4m, p2_ht6m, p3_ht9m, p3_ht12m, p3_ht15m, p4_ht18m, p4_ht24m, "
                + "ROUND((TO_DAYS(CURDATE()) - TO_DAYS(c_birthDate))/7) AS c_Age "
                + "FROM formRourke2006 "
                + "WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
             
            try {
	            rs = DBHandler.GetSQL(sql);

	            if(rs.next())           {
	                ResultSetMetaData md = rs.getMetaData();
	                String value;

	                for(int i=1; i<=md.getColumnCount(); i++)            {
	                    String name = md.getColumnName(i);

	                    if(md.getColumnTypeName(i).equalsIgnoreCase("date"))               {
	                        value = UtilDateUtilities.DateToString(rs.getDate(i), "dd/MM/yyyy");
	                    } else {
	                        value = oscar.Misc.getString(rs, i);
	                    }
	                    
	                    if(value!=null) {                        
	                        props.setProperty(name, value);
	                    }
	                }//end for

	            }//end if
	            rs.close();
            } catch (SQLException e) {
            	MiscUtils.getLogger().error("Error", e);
            }
        }
        return props;
    }

    public String findActionValue(String submit) throws SQLException {
 		return ((new FrmRecordHelp()).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
 		return ((new FrmRecordHelp()).createActionURL(where, action, demoId, formId));
    }

}
