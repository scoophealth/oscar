package oscar.oscarBilling.ca.on.pageUtil;

import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import oscar.oscarBilling.ca.on.data.JdbcBilling3rdPartImpl;

public class Billing3rdPartPrep {
	private static final Logger _logger = Logger
			.getLogger(Billing3rdPartPrep.class);

	JdbcBilling3rdPartImpl dbObj = new JdbcBilling3rdPartImpl();

	// get 3rd billing data
	public Properties get3rdPartBillProp(String invNo) {
		Properties ret = new Properties();
		ret = dbObj.get3rdPartBillProp(invNo);
		return ret;
	}

	public Properties getLocalClinicAddr() {
		Properties ret = new Properties();
		ret = dbObj.getLocalClinicAddr();
		return ret;
	}

	public Properties get3rdPayMethod() {
		Properties ret = new Properties();
		ret = dbObj.get3rdPayMethod();
		return ret;
	}
        
        public Properties getGst(String invNo) throws SQLException{
                Properties ret = new Properties();
                ret = dbObj.getGstTotal(invNo);
                return ret;
        }

	public boolean updateKeyValue(String billingNo, String demoNo, String key,
			String value) {
		boolean ret = dbObj.updateKeyValue(billingNo, key, value);
		return ret;
	}

}
