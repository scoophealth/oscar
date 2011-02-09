package oscar.oscarBilling.ca.on.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class JdbcBillingErrorCodeImpl {
	private static final Logger _logger = Logger.getLogger(JdbcBillingErrorCodeImpl.class);
	BillingONDataHelp dbObj = new BillingONDataHelp();

	public Properties getBillingErrorCodeProp(String strServiceCode) {
		Properties ret = new Properties();
		String sql = "select * from billing_on_errorCode where code in (" + strServiceCode + ")";
		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			while (rs.next()) {
				ret.setProperty(rs.getString("code"), rs.getString("description"));
			}
		} catch (SQLException e) {
			_logger.error("getBillingErrorCodeProp(sql = " + sql + ")");
		}
		return ret;
	}

	public String getCodeDesc(String strServiceCode) {
		String ret = null;
		String sql = "select * from billing_on_errorCode where code ='" + strServiceCode + "'";
		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			while (rs.next()) {
				ret = rs.getString("description");
			}
		} catch (SQLException e) {
			_logger.error("getCodeDesc(sql = " + sql + ")");
		}
		return ret;
	}

}
