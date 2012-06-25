
package oscar.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import oscar.SxmlMisc;
import oscar.login.DBHelp;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmBCARRecord extends FrmRecord {
    private String _dateFormat = "dd/MM/yyyy";

    public Properties getFormRecord(int demographicNo, int existingID) throws SQLException {
        Properties props = new Properties();

        if (existingID <= 0) {
            
            String sql = "SELECT demographic_no, last_name, first_name, sex, address, city, province, postal, phone, phone2, year_of_birth, month_of_birth, date_of_birth, hin, family_doctor FROM demographic WHERE demographic_no = "
                    + demographicNo;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                java.util.Date date = UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), rs
                        .getString("month_of_birth"), oscar.Misc.getString(rs, "date_of_birth"));
                props.setProperty("demographic_no", oscar.Misc.getString(rs, "demographic_no"));
                props
                        .setProperty("formCreated", UtilDateUtilities.DateToString(UtilDateUtilities.Today(),
                                _dateFormat));
                // props.setProperty("formEdited",
                // UtilDateUtilities.DateToString(UtilDateUtilities.Today(),_dateFormat));
                props.setProperty("c_surname", oscar.Misc.getString(rs, "last_name"));
                props.setProperty("c_givenName", oscar.Misc.getString(rs, "first_name"));
                props.setProperty("c_address", oscar.Misc.getString(rs, "address"));
                props.setProperty("c_city", oscar.Misc.getString(rs, "city"));
                props.setProperty("c_province", oscar.Misc.getString(rs, "province"));
                props.setProperty("c_postal", oscar.Misc.getString(rs, "postal"));
                props.setProperty("c_phn", oscar.Misc.getString(rs, "hin"));
                props.setProperty("pg1_dateOfBirth", UtilDateUtilities.DateToString(date, _dateFormat));
                props.setProperty("pg1_age", String.valueOf(UtilDateUtilities.calcAge(date)));
                props.setProperty("c_phone", oscar.Misc.getString(rs, "phone") + "  " + oscar.Misc.getString(rs, "phone2"));
                props.setProperty("pg1_formDate", UtilDateUtilities
                        .DateToString(UtilDateUtilities.Today(), _dateFormat));
                props.setProperty("pg2_formDate", UtilDateUtilities
                        .DateToString(UtilDateUtilities.Today(), _dateFormat));
                props.setProperty("pg3_formDate", UtilDateUtilities
                        .DateToString(UtilDateUtilities.Today(), _dateFormat));
                String rd = SxmlMisc.getXmlContent(oscar.Misc.getString(rs, "family_doctor"), "rd");
                rd = rd != null ? rd : "";
                props.setProperty("pg1_famPhy", rd);
            }
            rs.close();
        } else {
            String sql = "SELECT * FROM formBCAR WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
            FrmRecordHelp frh = new FrmRecordHelp();
            frh.setDateFormat(_dateFormat);
            props = (frh).getFormRecord(sql);

            sql = "SELECT last_name, first_name, address, city, province, postal, phone,phone2, hin FROM demographic WHERE demographic_no = "
                    + demographicNo;
            DBHelp db = new DBHelp();
            ResultSet rs = DBHelp.searchDBRecord(sql);
            if (rs.next()) {
                props.setProperty("c_surname_cur", oscar.Misc.getString(rs, "last_name"));
                props.setProperty("c_givenName_cur", oscar.Misc.getString(rs, "first_name"));
                props.setProperty("c_address_cur", oscar.Misc.getString(rs, "address"));
                props.setProperty("c_city_cur", oscar.Misc.getString(rs, "city"));
                props.setProperty("c_province_cur", oscar.Misc.getString(rs, "province"));
                props.setProperty("c_postal_cur", oscar.Misc.getString(rs, "postal"));
                props.setProperty("c_phn_cur", oscar.Misc.getString(rs, "hin"));
                props.setProperty("c_phone_cur", oscar.Misc.getString(rs, "phone") + "  " + oscar.Misc.getString(rs, "phone2"));
            }
        }
        return props;
    }

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formBCAR WHERE demographic_no=" + demographic_no + " AND ID=0";

        FrmRecordHelp frh = new FrmRecordHelp();
        frh.setDateFormat(_dateFormat);
        return ((frh).saveFormRecord(props, sql));
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException {
        String sql = "SELECT * FROM formBCAR WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
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
