package oscar.form;

import oscar.oscarDB.*;
import oscar.oscarEncounter.data.*;
import oscar.util.* ;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Date;
import java.util.*;
import java.sql.*;
import java.io.*;
import java.lang.String;

public class FrmARRecord extends FrmRecord {
    public Properties getFormRecord(int demographicNo, int existingID)
            throws SQLException    {
        Properties props = new Properties();

        if(existingID <= 0) {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "SELECT demographic_no, CONCAT(last_name, ', ', first_name) AS pName, sex, CONCAT(address, ', ', city, ', ', province, ' ', postal) AS address, phone, phone2, year_of_birth, month_of_birth, date_of_birth FROM demographic WHERE demographic_no = " + demographicNo;
            ResultSet rs = db.GetSQL(sql);
            if(rs.next()) {
                java.util.Date date = UtilDateUtilities.calcDate(rs.getString("year_of_birth"), rs.getString("month_of_birth"), rs.getString("date_of_birth"));
                props.setProperty("demographic_no", rs.getString("demographic_no"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(UtilDateUtilities.Today(),"yyyy/MM/dd"));
                //props.setProperty("formEdited", UtilDateUtilities.DateToString(UtilDateUtilities.Today(),"yyyy/MM/dd"));
                props.setProperty("c_pName", rs.getString("pName"));
                props.setProperty("c_address", rs.getString("address"));
                props.setProperty("pg1_dateOfBirth", UtilDateUtilities.DateToString(date,"yyyy/MM/dd"));
                props.setProperty("pg1_age", String.valueOf(UtilDateUtilities.calcAge(date)));
                props.setProperty("pg1_homePhone", rs.getString("phone"));
                props.setProperty("pg1_workPhone", rs.getString("phone2"));
                props.setProperty("pg1_formDate", UtilDateUtilities.DateToString(UtilDateUtilities.Today(),"yyyy/MM/dd"));
            }
            rs.close();
			db.CloseConn();
        } else {
            String sql = "SELECT * FROM formAR WHERE demographic_no = " +demographicNo +" AND ID = " +existingID;
			props = (new FrmRecordHelp()).getFormRecord(sql);
        }

        return props;
    }

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formAR WHERE demographic_no=" +demographic_no +" AND ID=0";

		return ((new FrmRecordHelp()).saveFormRecord(props, sql));
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException  {
        String sql = "SELECT * FROM formAR WHERE demographic_no = " +demographicNo +" AND ID = " +existingID ;
		return ((new FrmRecordHelp()).getPrintRecord(sql));
    }

    public String findActionValue(String submit) throws SQLException {
 		return ((new FrmRecordHelp()).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
 		return ((new FrmRecordHelp()).createActionURL(where, action, demoId, formId));
    }

}
