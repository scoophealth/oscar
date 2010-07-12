package oscar.oscarBilling.ca.on.data;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class BillingStatusData {
	private static final Logger _logger = Logger.getLogger(BillingStatusData.class);

	public BillingStatusData() {
	}

	public ArrayList getBills(String statusType, String providerNo, String startDate, String endDate, String demoNo) {
		ArrayList list = new ArrayList();

		String providerQuery = "";
		String startDateQuery = "";
		String endDateQuery = "";
		String demoQuery = "";

		if (providerNo != null && !providerNo.trim().equalsIgnoreCase("all")) {
			providerQuery = " and provider_no = '" + providerNo + "'";
		}

		if (startDate != null && !startDate.trim().equalsIgnoreCase("")) {
			startDateQuery = " and billing_date >= '" + startDate + "' ";
		}

		if (endDate != null && !endDate.trim().equalsIgnoreCase("")) {
			endDateQuery = " and billing_date <= '" + endDate + "' ";
		}
		if (demoNo != null && !demoNo.trim().equalsIgnoreCase("")) {
			demoQuery = " and demographic_no = '" + demoNo + "' ";
		}

		String sql = "select * from billing_on_cheader1 where status = '" + StringEscapeUtils.escapeSql(statusType)
				+ "' " + providerQuery + startDateQuery + endDateQuery + demoQuery;
		MiscUtils.getLogger().debug("bill status query " + sql);
		try {
			BillingONDataHelp db = new BillingONDataHelp();
			ResultSet rs = db.searchDBRecord(sql);
			while (rs.next()) {
				Hashtable h = new Hashtable();
				h.put("billing_no", "" + rs.getInt("id"));
				h.put("demographic_no", rs.getString("demographic_no"));
				h.put("status", rs.getString("status"));
				h.put("provider_no", rs.getString("provider_no"));
				h.put("demographic_name", rs.getString("demographic_name"));
				h.put("billing_date", rs.getString("billing_date"));
				h.put("billing_time", rs.getString("billing_time"));
				h.put("total", rs.getString("total"));
				h.put("clinic", rs.getString("clinic"));
				list.add(h);
			}
		} catch (Exception e) {
			_logger.error("getBills(sql = " + sql + ")");
		}
		return list;
	}
}
