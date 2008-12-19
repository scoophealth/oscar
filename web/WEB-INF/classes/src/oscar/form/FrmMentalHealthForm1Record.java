/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. This program is free
 * software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version. * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. * * You should have
 * received a copy of the GNU General Public License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * <OSCAR
 * TEAM> This software was written for the Department of Family Medicine McMaster Unviersity
 * Hamilton Ontario, Canada
 */
package oscar.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmMentalHealthForm1Record extends FrmRecord {
		
	public Properties getMentalHealthForm1Record(int demographicNo, int existingID, int providerNo, int programNo) throws SQLException {
        Properties props = new Properties();
        if (existingID <= 0) {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
            
            String sql0 = "SELECT name AS programName FROM program WHERE id='"+programNo+"'";
            ResultSet rs0 = db.GetSQL(sql0);
            if(rs0.next()) {
            	props.setProperty("programName",db.getString(rs0,"programName"));
            }
            rs0.close();  
            
            String sql = "SELECT demographic_no, CONCAT(CONCAT(last_name, ', '), first_name) AS clientName, year_of_birth, month_of_birth, date_of_birth, CONCAT(address,city,province,postal) AS address FROM demographic WHERE demographic_no = "
                    + demographicNo;
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
                Date dob = UtilDateUtilities.calcDate(db.getString(rs,"year_of_birth"), db.getString(rs,"month_of_birth"),
                        db.getString(rs,"date_of_birth"));
                props.setProperty("demographic_no", db.getString(rs,"demographic_no"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(UtilDateUtilities.Today(),
                        "yyyy/MM/dd"));
                //props.setProperty("formEdited",
                // UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy-MM-dd
                // HH:mm:ss"));
                props.setProperty("birthDate", UtilDateUtilities.DateToString(dob, "yyyy/MM/dd"));
                props.setProperty("clientName", db.getString(rs,"clientName"));
                props.setProperty("address",db.getString(rs,"address"));
            }
            rs.close();
            /*
            String sql1 = "SELECT CONCAT(CONCAT(last_name,', '),first_name) AS providerName FROM provider WHERE provider_no='"+providerNo+"'";
            ResultSet rs1 = db.GetSQL(sql1);
            if(rs1.next()) {
            	props.setProperty("providerName",rs1.getString("providerName"));
            }
            rs1.close();            
            */
             
            db.CloseConn();
        } else {
            String sql = "SELECT * FROM formMentalHealthForm1 WHERE demographic_no = " + demographicNo + " AND ID = "
                    + existingID;
            props = (new FrmRecordHelp()).getFormRecord(sql);
        }

        return props;
    }

	
	
	
	public Properties getFormRecord(int demographicNo, int existingID) throws SQLException {
        Properties props = new Properties();
        if (existingID <= 0) {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "SELECT demographic_no, CONCAT(CONCAT(last_name, ', '), first_name) AS clientName, year_of_birth, month_of_birth, date_of_birth FROM demographic WHERE demographic_no = "
                    + demographicNo;
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
                Date dob = UtilDateUtilities.calcDate(db.getString(rs,"year_of_birth"), db.getString(rs,"month_of_birth"),
                        db.getString(rs,"date_of_birth"));
                props.setProperty("demographic_no", db.getString(rs,"demographic_no"));
               // props.setProperty("formCreated", UtilDateUtilities.DateToString(UtilDateUtilities.Today(),
               //         "yyyy/MM/dd"));
                props.setProperty("formEdited",UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy-MM-dd HH:mm:ss"));
                props.setProperty("birthDate", UtilDateUtilities.DateToString(dob, "yyyy/MM/dd"));
                props.setProperty("clientName", db.getString(rs,"clientName"));
            }
            rs.close();
            db.CloseConn();
        } else {
            String sql = "SELECT * FROM formMentalHealthForm1 WHERE demographic_no = " + demographicNo + " AND ID = "
                    + existingID;
            props = (new FrmRecordHelp()).getFormRecord(sql);
        }

        //props.list(System.out);
        return props;
    }

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
      
        String sql = "SELECT * FROM formMentalHealthForm1 WHERE demographic_no=" + demographic_no + " AND ID=0";

        return ((new FrmRecordHelp()).saveFormRecord(props, sql));
    }

    public String findActionValue(String submit) throws SQLException {
        if (submit != null && submit.equalsIgnoreCase("print")) {
            return "print";
        } else if (submit != null && submit.equalsIgnoreCase("save")) {
            return "save";
        } else if (submit != null && submit.equalsIgnoreCase("exit")) {
            return "exit";
        } else {
            return "failure";
        }
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
        String temp = null;

        if (action.equalsIgnoreCase("print")) {
            temp = where + "?demoNo=" + demoId + "&formId=" + formId; // + "&study_no=" + studyId +
                                                                      // "&study_link" + studyLink;
        } else if (action.equalsIgnoreCase("save")) {
            temp = where + "?demographic_no=" + demoId + "&formId=" + formId; // "&study_no=" +
                                                                              // studyId +
                                                                              // "&study_link" +
                                                                              // studyLink; //+
        } else if (action.equalsIgnoreCase("exit")) {
            temp = where;
        } else {
            temp = where;
        }

        return temp;
    }

}