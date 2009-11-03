package oscar.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import oscar.login.DBHelp;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

//	 Referenced classes of package oscar.form:
//	            FrmRecord, FrmRecordHelp

public class FrmBCClientChartChecklistRecord extends FrmRecord {

	public FrmBCClientChartChecklistRecord() {
		_dateFormat = "dd/MM/yyyy";
	}

	public Properties getFormRecord(int demographicNo, int existingID)
			throws SQLException {
		Properties props = new Properties();
		if (existingID <= 0) {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			String sql = "SELECT demographic_no, last_name, first_name, sex, address, city, province, postal, phone, phone2, year_of_birth, month_of_birth, date_of_birth, hin FROM demographic WHERE demographic_no = "
					+ demographicNo;
			ResultSet rs = db.GetSQL(sql);
			if (rs.next()) {
				java.util.Date date = UtilDateUtilities.calcDate(rs
						.getString("year_of_birth"), rs
						.getString("month_of_birth"), rs
						.getString("date_of_birth"));
				props.setProperty("demographic_no", rs
						.getString("demographic_no"));
				props.setProperty("formCreated", UtilDateUtilities
						.DateToString(UtilDateUtilities.Today(), _dateFormat));
				props.setProperty("formEdited", UtilDateUtilities.DateToString(
						UtilDateUtilities.Today(), _dateFormat));
				props.setProperty("c_surname", db.getString(rs,"last_name"));
				props.setProperty("c_givenName", db.getString(rs,"first_name"));
				props.setProperty("c_address", db.getString(rs,"address"));
				props.setProperty("c_city", db.getString(rs,"city"));
				props.setProperty("c_province", db.getString(rs,"province"));
				props.setProperty("c_postal", db.getString(rs,"postal"));
				props.setProperty("c_phn", db.getString(rs,"hin"));
				props.setProperty("pg1_dateOfBirth", UtilDateUtilities
						.DateToString(date, _dateFormat));
				props.setProperty("pg1_age", String.valueOf(UtilDateUtilities
						.calcAge(date)));
				props.setProperty("c_phone", db.getString(rs,"phone") + "  "
						+ db.getString(rs,"phone2"));
				props.setProperty("pg1_formDate", UtilDateUtilities
						.DateToString(UtilDateUtilities.Today(), _dateFormat));
			}
			sql = "select clinic_name from clinic";
			rs = db.GetSQL(sql);
			if (rs.next()) {
				props.setProperty("c_clinicName", db.getString(rs,"clinic_name"));
			}
			rs.close();
		} else {
			String sql = "SELECT * FROM formBCClientChartChecklist WHERE demographic_no = "
					+ demographicNo + " AND ID = " + existingID;
			FrmRecordHelp frh = new FrmRecordHelp();
			frh.setDateFormat(_dateFormat);
			props = frh.getFormRecord(sql);
			sql = "SELECT last_name, first_name, address, city, province, postal, phone,phone2, hin FROM demographic WHERE demographic_no = "
					+ demographicNo;
			DBHelp db = new DBHelp();
			ResultSet rs = db.searchDBRecord(sql);
			if (rs.next()) {
				props.setProperty("c_surname_cur", db.getString(rs,"last_name"));
				props
						.setProperty("c_givenName_cur", rs
								.getString("first_name"));
				props.setProperty("c_address_cur", db.getString(rs,"address"));
				props.setProperty("c_city_cur", db.getString(rs,"city"));
				props.setProperty("c_province_cur", db.getString(rs,"province"));
				props.setProperty("c_postal_cur", db.getString(rs,"postal"));
				props.setProperty("c_phn_cur", db.getString(rs,"hin"));
				props.setProperty("c_phone_cur", db.getString(rs,"phone") + "  "
						+ db.getString(rs,"phone2"));
			}
		}
		return props;
	}

	public int saveFormRecord(Properties props) throws SQLException {
		String demographic_no = props.getProperty("demographic_no");
		String sql = "SELECT * FROM formBCClientChartChecklist WHERE demographic_no="
				+ demographic_no + " AND ID=0";
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(_dateFormat);
		return frh.saveFormRecord(props, sql);
	}

	public Properties getPrintRecord(int demographicNo, int existingID)
			throws SQLException {
		String sql = "SELECT * FROM formBCClientChartChecklist WHERE demographic_no = "
				+ demographicNo + " AND ID = " + existingID;
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(_dateFormat);
		return frh.getPrintRecord(sql);
	}

	public String findActionValue(String submit) throws SQLException {
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(_dateFormat);
		return frh.findActionValue(submit);
	}

	public String createActionURL(String where, String action, String demoId,
			String formId) throws SQLException {
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(_dateFormat);
		return frh.createActionURL(where, action, demoId, formId);
	}

	private String _dateFormat;
}