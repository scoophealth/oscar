package oscar.appt;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import oscar.SxmlMisc;
import oscar.oscarBilling.ca.on.data.BillingONDataHelp;
import oscar.util.UtilDateUtilities;

public class JdbcApptImpl {
	private static final Logger _logger = Logger.getLogger(JdbcApptImpl.class);
	BillingONDataHelp dbObj = new BillingONDataHelp();

	public String getLocationFromSchedule(String apptDate, String provider_no) {
		String retval = getLocationFromSpec(apptDate, provider_no, "c");
		if (!"".equals(retval))
			return retval;
		retval = getLocationFromSpec(apptDate, provider_no, "b");
		if (!"".equals(retval))
			return retval;

		String sql = "select avail_hour from rschedule where provider_no='" + provider_no + "' ";
		sql += " and sdate<='" + apptDate + "' and edate>='" + apptDate + "'";
		// _logger.info("getLocationFromSchedule(sql = " + sql + ")");

		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			while (rs.next()) {
				retval = oscar.Misc.getString(rs,"avail_hour");
			}
		} catch (SQLException e) {
			_logger.error("getLocationFromSchedule(sql = " + sql + ")");
		}

		// get weekday number
		String[] temp = { "", "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN" };
		String strWeekday = UtilDateUtilities.DateToString(UtilDateUtilities.getDateFromString(apptDate, "yyyy-MM-dd"),
				"EEE");
		int n = 0;
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].equalsIgnoreCase(strWeekday)) {
				n = i;
				break;
			}
		}

		retval = SxmlMisc.getXmlContent(retval, "A" + n);
		retval = retval == null ? "" : retval;
		return retval;
	}

	// priority = c, reason = location
	private String getLocationFromSpec(String apptDate, String provider_no, String priority) {
		String retval = "";
		String sql = "select reason from scheduledate where sdate='" + apptDate + "' and provider_no='" + provider_no
				+ "' and priority='" + priority + "'";
		// _logger.info("getLocationFromSpec(sql = " + sql + ")");

		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			while (rs.next()) {
				retval = oscar.Misc.getString(rs,"reason");
			}
		} catch (SQLException e) {
			_logger.error("getLocationFromSpec(sql = " + sql + ")");
		}

		retval = retval == null ? "" : retval;
		return retval;
	}

	public String getPrevApptDate(String thisServiceDate) {
		String retval = "";
		String sql = "select appointment_date from appointment where appointment_date<'" + thisServiceDate + "' ";
		sql += " order by appointment_date desc limit 1";

		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			while (rs.next()) {
				retval = oscar.Misc.getString(rs,"appointment_date");
			}
		} catch (SQLException e) {
			_logger.error("getPrevApptDate(sql = " + sql + ")");
		}

		return retval;
	}

}
