package oscar.form;

import oscar.oscarDB.*;
import oscar.oscarEncounter.data.*;
import oscar.util.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import java.lang.String;

public class FrmMentalHealthRecord  extends FrmRecord {
    public Properties getFormRecord(int demographicNo, int existingID)
            throws SQLException {
        Properties props = new Properties();

        if(existingID <= 0) {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			String sql = "SELECT demographic_no, CONCAT(last_name, ', ', first_name) AS pName, "
                + "sex, CONCAT(address, ', ', city, ', ', province, ' ', postal) AS address, "
                + "phone, year_of_birth, month_of_birth, date_of_birth, roster_status "
                + "FROM demographic WHERE demographic_no = " + demographicNo;
            ResultSet rs = db.GetSQL(sql);

            if(rs.next()) {
                java.util.Date dob = UtilDateUtilities.calcDate(rs.getString("year_of_birth"), rs.getString("month_of_birth"), rs.getString("date_of_birth"));

                props.setProperty("demographic_no", rs.getString("demographic_no"));
                props.setProperty("c_pName", rs.getString("pName"));
                props.setProperty("c_sex", rs.getString("sex"));
                props.setProperty("c_referralDate", UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy/MM/dd"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy/MM/dd"));
                //props.setProperty("formEdited", UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy/MM/dd"));
                props.setProperty("c_address", rs.getString("address"));
                props.setProperty("c_birthDate", UtilDateUtilities.DateToString(dob, "yyyy/MM/dd"));
                props.setProperty("c_homePhone", rs.getString("phone"));
                props.setProperty("demo_roster_status", rs.getString("roster_status"));
            }
            rs.close();
			db.CloseConn();

        } else {
            String sql = "SELECT * FROM formMentalHealth WHERE demographic_no = " +demographicNo +" AND ID = " +existingID;
			props = (new FrmRecordHelp()).getFormRecord(sql);

			// get roster_status from demographic table
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			ResultSet rs = null;
			sql = "SELECT roster_status FROM demographic WHERE demographic_no = " + demographicNo;
			rs = db.GetSQL(sql);
			if(rs.next()) {
				props.setProperty("demo_roster_status", rs.getString("roster_status"));
			}
			rs.close();
			db.CloseConn();
        }

        return props;
    }

    public Properties getFormCustRecord(Properties props, int provNo) throws SQLException {
		DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
		ResultSet rs = null;
		String sql = null;

        // from provider table
        sql = "SELECT CONCAT(last_name, ', ', first_name) AS provName "
              + "FROM provider WHERE provider_no = " + provNo;
        rs = db.GetSQL(sql);
        if(rs.next()) {
            props.setProperty("c_referredBy", rs.getString("provName"));
        }
        rs.close();
		db.CloseConn();

        return props;
	}

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formMentalHealth WHERE demographic_no=" +demographic_no +" AND ID=0";

		return ((new FrmRecordHelp()).saveFormRecord(props, sql));
	}

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException  {
        String sql = "SELECT * FROM formMentalHealth WHERE demographic_no = " +demographicNo +" AND ID = " +existingID ;
		return ((new FrmRecordHelp()).getPrintRecord(sql));
    }


    public String findActionValue(String submit) throws SQLException {
 		return ((new FrmRecordHelp()).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
 		return ((new FrmRecordHelp()).createActionURL(where, action, demoId, formId));
    }

}
