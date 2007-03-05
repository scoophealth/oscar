package oscar.oscarBilling.ca.on.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class JdbcBillingDxImpl {
	private static final Logger _logger = Logger
			.getLogger(JdbcBillingDxImpl.class);

	BillingONDataHelp dbObj = new BillingONDataHelp();

	public String getDxDescription(String dxCode) {
		String ret = "";
		String sql = "select description from diagnosticcode where diagnostic_code='"
				+ dxCode + "'";
		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			while (rs.next()) {
				ret = rs.getString("description");
			}
		} catch (SQLException e) {
			_logger.error("getDxDescription(sql = " + sql + ")");
		}
		return ret;
	}

}
