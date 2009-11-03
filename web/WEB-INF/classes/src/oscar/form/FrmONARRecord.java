/*
 * Copyright (c) 2005- Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. This program is free
 * software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version. * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. * * You should have
 * received a copy of the GNU General Public License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. 
 * 
 * Yi Li
 */
package oscar.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import oscar.OscarProperties;
import oscar.login.DBHelp;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmONARRecord extends FrmRecord {
    public Properties getFormRecord(int demographicNo, int existingID) throws SQLException {
        Properties props = new Properties();

        if (existingID <= 0) {
            DBHelp db = new DBHelp();
            String sql = "SELECT demographic_no, last_name, first_name, sex, "
                    + "address, city, province, postal, phone, phone2, "
                    + "year_of_birth, month_of_birth, date_of_birth, hin, chart_no FROM demographic WHERE demographic_no = "
                    + demographicNo;
            ResultSet rs = db.searchDBRecord(sql);
            if (rs.next()) {
                java.util.Date date = UtilDateUtilities.calcDate(db.getString(rs,"year_of_birth"), rs
                        .getString("month_of_birth"), db.getString(rs,"date_of_birth"));
                props.setProperty("demographic_no", db.getString(rs,"demographic_no"));
                props.setProperty("formCreated", UtilDateUtilities
                        .DateToString(UtilDateUtilities.Today(), "yyyy/MM/dd"));
                // props.setProperty("formEdited",
                // UtilDateUtilities.DateToString(UtilDateUtilities.Today(),"yyyy/MM/dd"));
                props.setProperty("c_lastName", db.getString(rs,"last_name"));
                props.setProperty("c_firstName", db.getString(rs,"first_name"));
                props.setProperty("c_address", db.getString(rs,"address"));
                props.setProperty("c_city", db.getString(rs,"city"));
                props.setProperty("c_province", db.getString(rs,"province"));
                props.setProperty("c_postal", db.getString(rs,"postal"));
                props.setProperty("c_hin", db.getString(rs,"hin"));
                props.setProperty("c_fileNo", db.getString(rs,"chart_no"));
                props.setProperty("pg1_dateOfBirth", UtilDateUtilities.DateToString(date, "yyyy/MM/dd"));
                props.setProperty("pg1_age", String.valueOf(UtilDateUtilities.calcAge(db.getString(rs,"year_of_birth"), rs
                        .getString("month_of_birth"), db.getString(rs,"date_of_birth"))));
                props.setProperty("pg1_homePhone", db.getString(rs,"phone"));
                props.setProperty("pg1_workPhone", db.getString(rs,"phone2"));
                props.setProperty("pg1_formDate", UtilDateUtilities.DateToString(UtilDateUtilities.Today(),
                        "yyyy/MM/dd"));
            }
            rs.close();
        } else {
            String sql = "SELECT * FROM formONAR WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
            props = (new FrmRecordHelp()).getFormRecord(sql);
        }

        return props;
    }

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formONAR WHERE demographic_no=" + demographic_no + " AND ID=0";

        return ((new FrmRecordHelp()).saveFormRecord(props, sql));
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException {
        String sql = "SELECT * FROM formONAR WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
        return ((new FrmRecordHelp()).getPrintRecord(sql));
    }

    public String findActionValue(String submit) throws SQLException {
        return ((new FrmRecordHelp()).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
        return ((new FrmRecordHelp()).createActionURL(where, action, demoId, formId));
    }

    public boolean isSendToPing(String demoNo) throws SQLException {
        boolean ret = false;
        if ("yes".equalsIgnoreCase(OscarProperties.getInstance().getProperty("PHR", ""))) {

            String demographic_no = demoNo;
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select email from demographic where demographic_no=" + demographic_no;
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
                if (db.getString(rs,"email") != null && db.getString(rs,"email").length() > 5
                        && db.getString(rs,"email").matches(".*@.*"))
                    ret = true;
            }

            rs.close();
        }
        return ret;
    }

}