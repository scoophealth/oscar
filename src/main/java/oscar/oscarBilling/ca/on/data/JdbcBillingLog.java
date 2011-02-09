package oscar.oscarBilling.ca.on.data;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import oscar.util.UtilDateUtilities;

public class JdbcBillingLog {
	private static final Logger _logger = Logger.getLogger(JdbcBillingLog.class);
	BillingONDataHelp dbObj = new BillingONDataHelp();

	public boolean addBillingLog(String providerNo, String action, String comment, String object) {
		boolean retval = false;
		String createdatetime = UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss");
		String sql = "insert into billing_on_proc values(\\N, '" + providerNo + "','" + action + "','"
				+ StringEscapeUtils.escapeSql(comment) + "','" + object + "','" + createdatetime + "')";
		_logger.info("addBillingLog(sql = " + sql + ")");

		retval = dbObj.updateDBRecord(sql);

		if (retval) {
			_logger.error("addBillingLog(sql = " + sql + ")");
		}
		return retval;
	}

}
