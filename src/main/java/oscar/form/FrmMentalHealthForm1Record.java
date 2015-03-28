/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package oscar.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import org.oscarehr.util.LoggedInInfo;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmMentalHealthForm1Record extends FrmRecord {
	
	public Properties getFormRecord(LoggedInInfo loggedInInfo, int demographicNo, int existingID) throws SQLException {
        Properties props = new Properties();
        
        if (existingID <= 0) {            
            String demoProvider = "000000";
            String sql = "SELECT demographic_no, CONCAT(CONCAT(last_name, ', '), first_name) AS clientName, year_of_birth, month_of_birth, date_of_birth, CONCAT(address,' ',city,' ',province,' ',postal) AS clientAddress, provider_no FROM demographic WHERE demographic_no = "
                    + demographicNo;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                Date dob = UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"),
                        oscar.Misc.getString(rs, "date_of_birth"));
                props.setProperty("demographic_no", oscar.Misc.getString(rs, "demographic_no"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(),
                        "yyyy/MM/dd"));
                props.setProperty("formEdited",UtilDateUtilities.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                props.setProperty("birthDate", UtilDateUtilities.DateToString(dob, "yyyy/MM/dd"));
                props.setProperty("clientName", oscar.Misc.getString(rs, "clientName"));
                props.setProperty("demoProvider", oscar.Misc.getString(rs, "provider_no"));
                props.setProperty("clientAddress",oscar.Misc.getString(rs, "clientAddress"));
                demoProvider = oscar.Misc.getString(rs, "provider_no");
            }
            rs.close();
        } else {
            String sql = "SELECT * FROM formMentalHealthForm1 WHERE demographic_no = " + demographicNo + " AND ID = "
                    + existingID;            
            props = (new FrmRecordHelp()).getFormRecord(sql);            
        }

        return props;
    }

	public Properties getFormCustRecord(Properties props, String provNo) throws SQLException {
        String demoProvider = props.getProperty("demoProvider", "");
        
        ResultSet rs = null;
        String sql = null;

        if (!demoProvider.equals("")) {

            if (demoProvider.equals(provNo) ) {
                // from provider table
                sql = "SELECT CONCAT(last_name, ', ', first_name) AS provName, ohip_no "
                        + "FROM provider WHERE provider_no = '" + provNo + "'";
                rs = DBHandler.GetSQL(sql);

                if (rs.next()) {
                    String num = oscar.Misc.getString(rs, "ohip_no");
                    props.setProperty("reqProvName", oscar.Misc.getString(rs, "provName"));
                    props.setProperty("provName", oscar.Misc.getString(rs, "provName"));
                    props.setProperty("practitionerNo", "0000-" + num + "-00");
                }
                rs.close();
            } else {
                // from provider table
                sql = "SELECT CONCAT(last_name, ', ', first_name) AS provName, ohip_no FROM provider WHERE provider_no = '"
                        + provNo + "'";
                rs = DBHandler.GetSQL(sql);
                
                String num = "";
                if (rs.next()) {
                    num = oscar.Misc.getString(rs, "ohip_no");
                    props.setProperty("reqProvName", oscar.Misc.getString(rs, "provName"));                    
                    props.setProperty("practitionerNo", "0000-" + num + "-00");
                }
                rs.close();

                // from provider table
                sql = "SELECT CONCAT(last_name, ', ', first_name) AS provName, ohip_no FROM provider WHERE provider_no = "
                        + demoProvider;
                rs = DBHandler.GetSQL(sql);

                if (rs.next()) {
                    if( num.equals("") ) {
                        num = oscar.Misc.getString(rs, "ohip_no");
                        props.setProperty("practitionerNo", "0000-"+num+"-00");
                    }
                    props.setProperty("provName", oscar.Misc.getString(rs, "provName"));
                    
                }
                rs.close();
            }
        }
        

        return props;
    }

	public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formMentalHealthForm1 WHERE demographic_no=" + demographic_no + " AND ID=0";
        return ((new FrmRecordHelp()).saveFormRecord(props, sql));
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException {
        String sql = "SELECT * FROM formMentalHealthForm1 WHERE demographic_no = " + demographicNo + " AND ID = 0";
        return ((new FrmRecordHelp()).getPrintRecord(sql));
    }

    public String findActionValue(String submit) throws SQLException {
        return ((new FrmRecordHelp()).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
        return ((new FrmRecordHelp()).createActionURL(where, action, demoId, formId));
    }
	
}
