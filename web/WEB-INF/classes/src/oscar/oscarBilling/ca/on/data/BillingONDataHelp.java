/*
 * Created on Mar 14, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package oscar.oscarBilling.ca.on.data;
import java.sql.ResultSet;
import java.sql.SQLException;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
/**
 * @author yilee18
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class BillingONDataHelp {
	public synchronized int saveBillingRecord(String sql) {
		int ret = 0;
		try {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			boolean bDone = db.RunSQL(sql);

			/*
			 * if db_type = mysql return LAST_INSERT_ID() but if db_type =
			 * postgresql, return a prepared statement, since here we dont know
			 * which sequence will be used
			 */
			String db_type = OscarProperties.getInstance() != null
					? OscarProperties.getInstance().getProperty("db_type", "")
					: "";
			if (db_type.equals("") || db_type.equalsIgnoreCase("mysql")) {
				sql = "SELECT LAST_INSERT_ID()";
			} else if (db_type.equalsIgnoreCase("postgresql")) {
				sql = "SELECT CURRVAL('?')";
			} else {
				throw new SQLException("ERROR: Database " + db_type
						+ " unrecognized.");
			}
			ResultSet rs = db.GetSQL(sql);
			if (rs.next())
				ret = rs.getInt(1);
			rs.close();
			db.CloseConn();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}
	
	public synchronized boolean updateDBRecord(String sql) {
		boolean ret = false;
		try {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			ret = db.RunSQL(sql);

			db.CloseConn();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}
	
	public synchronized ResultSet searchDBRecord(String sql) {
		ResultSet ret = null;
		try {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			ret = db.GetSQL(sql);

			db.CloseConn();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}
}
