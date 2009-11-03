/*
 * Copyright (c) 2005- <OSCAR TEAM>
 */
package oscar.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmInvoiceRecord extends FrmRecord {
    private String _dateFormat = "yyyy/MM/dd";

    public Properties getFormRecord(int demographicNo, int existingID) throws SQLException {
        Properties props = new Properties();

        if (existingID <= 0) {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "SELECT demographic_no, last_name, first_name, sex, address, city, province, postal, phone, phone2, year_of_birth, month_of_birth, date_of_birth, hin, ver, hc_type FROM demographic WHERE demographic_no = "
                    + demographicNo;
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
                java.util.Date date = UtilDateUtilities.calcDate(db.getString(rs,"year_of_birth"), rs
                        .getString("month_of_birth"), db.getString(rs,"date_of_birth"));
                props.setProperty("demographic_no", db.getString(rs,"demographic_no"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(UtilDateUtilities.Today(),
                        _dateFormat));
                props.setProperty("patientName", db.getString(rs,"first_name") + " " + db.getString(rs,"last_name"));
                props.setProperty("patientSex", db.getString(rs,"sex"));
                props.setProperty("dateOfBirth", UtilDateUtilities.DateToString(date, _dateFormat));
                //props.setProperty("c_surname", db.getString(rs,"last_name"));
                props.setProperty("c_address", db.getString(rs,"address"));
                props.setProperty("c_address2", db.getString(rs,"city") + "," + db.getString(rs,"province") + "   "
                        + db.getString(rs,"postal"));
                //props.setProperty("c_province", db.getString(rs,"province"));
                //props.setProperty("c_postal", db.getString(rs,"postal"));
                props.setProperty("c_phn", db.getString(rs,"hin") + db.getString(rs,"ver") + "(" + db.getString(rs,"hc_type")
                        + ")");
                props.setProperty("c_phone", db.getString(rs,"phone") + "  " + db.getString(rs,"phone2"));
                props.setProperty("date_invoice", UtilDateUtilities.DateToString(UtilDateUtilities.Today(),
                        _dateFormat));
                props.setProperty("date_signature", UtilDateUtilities.DateToString(UtilDateUtilities.Today(),
                        _dateFormat));
            }
            rs.close();
        } else {
            String sql = "SELECT * FROM formInvoice WHERE demographic_no = " + demographicNo + " AND ID = "
                    + existingID;
            FrmRecordHelp frh = new FrmRecordHelp();
            frh.setDateFormat(_dateFormat);
            props = (frh).getFormRecord(sql);
        }

        return props;
    }

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formInvoice WHERE demographic_no=" + demographic_no + " AND ID=0";

        FrmRecordHelp frh = new FrmRecordHelp();
        frh.setDateFormat(_dateFormat);
        return ((frh).saveFormRecord(props, sql));
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException {
        String sql = "SELECT * FROM formInvoice WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
        FrmRecordHelp frh = new FrmRecordHelp();
        frh.setDateFormat(_dateFormat);
        return ((frh).getPrintRecord(sql));
    }

    public String findActionValue(String submit) throws SQLException {
        FrmRecordHelp frh = new FrmRecordHelp();
        frh.setDateFormat(_dateFormat);
        return ((frh).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
        FrmRecordHelp frh = new FrmRecordHelp();
        frh.setDateFormat(_dateFormat);
        return ((frh).createActionURL(where, action, demoId, formId));
    }

}
