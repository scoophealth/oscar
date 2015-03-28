
package oscar.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.login.DBHelp;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

//	 Referenced classes of package oscar.form:
//	            FrmRecord, FrmRecordHelp

public class FrmBCClientChartChecklistRecord extends FrmRecord {

	private ClinicDAO clinicDao = (ClinicDAO)SpringUtils.getBean("clinicDAO");


	public FrmBCClientChartChecklistRecord() {
		_dateFormat = "dd/MM/yyyy";
	}

	public Properties getFormRecord(LoggedInInfo loggedInInfo, int demographicNo, int existingID)
			throws SQLException {
		Properties props = new Properties();
		if (existingID <= 0) {

			String sql = "SELECT demographic_no, last_name, first_name, sex, address, city, province, postal, phone, phone2, year_of_birth, month_of_birth, date_of_birth, hin FROM demographic WHERE demographic_no = "
					+ demographicNo;
			ResultSet rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				java.util.Date date = UtilDateUtilities.calcDate(rs
						.getString("year_of_birth"), rs
						.getString("month_of_birth"), rs
						.getString("date_of_birth"));
				props.setProperty("demographic_no", rs
						.getString("demographic_no"));
				props.setProperty("formCreated", UtilDateUtilities
						.DateToString(new Date(), _dateFormat));
				props.setProperty("formEdited", UtilDateUtilities.DateToString(
						new Date(), _dateFormat));
				props.setProperty("c_surname", oscar.Misc.getString(rs, "last_name"));
				props.setProperty("c_givenName", oscar.Misc.getString(rs, "first_name"));
				props.setProperty("c_address", oscar.Misc.getString(rs, "address"));
				props.setProperty("c_city", oscar.Misc.getString(rs, "city"));
				props.setProperty("c_province", oscar.Misc.getString(rs, "province"));
				props.setProperty("c_postal", oscar.Misc.getString(rs, "postal"));
				props.setProperty("c_phn", oscar.Misc.getString(rs, "hin"));
				props.setProperty("pg1_dateOfBirth", UtilDateUtilities
						.DateToString(date, _dateFormat));
				props.setProperty("pg1_age", String.valueOf(UtilDateUtilities
						.calcAge(date)));
				props.setProperty("c_phone", oscar.Misc.getString(rs, "phone") + "  "
						+ oscar.Misc.getString(rs, "phone2"));
				props.setProperty("pg1_formDate", UtilDateUtilities
						.DateToString(new Date(), _dateFormat));
			}
			Clinic clinic = clinicDao.getClinic();
			if(clinic != null) {
				props.setProperty("c_clinicName", clinic.getClinicName());
			}
		} else {
			String sql = "SELECT * FROM formBCClientChartChecklist WHERE demographic_no = "
					+ demographicNo + " AND ID = " + existingID;
			FrmRecordHelp frh = new FrmRecordHelp();
			frh.setDateFormat(_dateFormat);
			props = frh.getFormRecord(sql);
			sql = "SELECT last_name, first_name, address, city, province, postal, phone,phone2, hin FROM demographic WHERE demographic_no = "
					+ demographicNo;
			ResultSet rs = DBHelp.searchDBRecord(sql);
			if (rs.next()) {
				props.setProperty("c_surname_cur", oscar.Misc.getString(rs, "last_name"));
				props
						.setProperty("c_givenName_cur", rs
								.getString("first_name"));
				props.setProperty("c_address_cur", oscar.Misc.getString(rs, "address"));
				props.setProperty("c_city_cur", oscar.Misc.getString(rs, "city"));
				props.setProperty("c_province_cur", oscar.Misc.getString(rs, "province"));
				props.setProperty("c_postal_cur", oscar.Misc.getString(rs, "postal"));
				props.setProperty("c_phn_cur", oscar.Misc.getString(rs, "hin"));
				props.setProperty("c_phone_cur", oscar.Misc.getString(rs, "phone") + "  "
						+ oscar.Misc.getString(rs, "phone2"));
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
