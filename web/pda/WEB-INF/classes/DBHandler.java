/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */

package oscar;

import java.util.Hashtable; //import java.sql.Date;
//import java.text.DateFormat;
import java.util.Enumeration;
import java.sql.*; // communicate with database
import java.io.*;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class DBHandler {
	private static Logger logger = MiscUtils.getLogger();

	Hashtable theQueries = null; // contains prepackaged queries
	String currentOp = null;
	String driverName = null; // ="sun.jdbc.odbc.JdbcOdbcDriver";
	String dbUrl = null; // ="jdbc:odbc:PHONEBOOK";
	String theUser = null; // ="usr";
	String thePwd = null; // ="pwd";
	Connection conn = null;// db is our connection object
	ResultSet rs = null;// result will hold the recordset
	Statement stmt = null; // stmt will hold sql jdbc statements
	PreparedStatement pstmt = null;

	public DBHandler() {
	}

	// Logger lg;
	public DBHandler(String dbDriver, String dbName, String dbUser, String dbPwd, String[] qNames, String[] qVals) throws SQLException {
		// lg=new Logger();
		initDBHandler(dbDriver, dbName, dbUser, dbPwd, qNames, qVals, null);
	}

	public DBHandler(String dbDriver, String dbName, String dbUser, String dbPwd, String[] qNames, String[] qVals, String[] qTypes) throws SQLException {
		// lg=new Logger();
		initDBHandler(dbDriver, dbName, dbUser, dbPwd, qNames, qVals, qTypes);
	}

	public void initDBHandler(String dbDriver, String dbName, String dbUser, String dbPwd, String[] qNames, String[] qVals, String[] qTypes) throws SQLException {
		driverName = dbDriver;
		dbUrl = dbName;
		theUser = dbUser;
		thePwd = dbPwd;
		theQueries = new Hashtable();

		try {
			OpenConn(driverName, dbUrl, theUser, thePwd);
			// closeConn();
		} catch (Exception ex) {
			logger.error("Unexpected error", e);
		}
	}

	private void OpenConn(String jdbcdriver, String dsn, String uid, String pwd) throws Exception {
		try {
			Class.forName(jdbcdriver);
		} catch (java.lang.ClassNotFoundException e) {
			logger.error("Unexpected error", e);
		}
		try {
			conn = DriverManager.getConnection(dsn, uid, pwd);
		} catch (Exception e) {
			logger.error("Unexpected error", e);
		}
	}

	/*********************************************
	 * @sproc String for sql statement or stored procedure
	 */
	synchronized public void queryExecute(String sproc, String[] param) throws SQLException {
		// called from receiving page, not forwarding page. execute should return a boolean value
		// System.out.println(sproc);
		pstmt = conn.prepareStatement(sproc);
		for (int id = 0; id < param.length; id++) {
			pstmt.setString((id + 1), param[id]);
			System.out.println(param[id]);
		}
		pstmt.execute();
		// stmt = conn.createStatement();
		// stmt.execute(sproc);
	}

	synchronized public int queryExecuteUpdate(String sproc, String[] param) throws SQLException {
		// called from receiving page, not forwarding page. number of records should be returned
		// System.out.println(sproc);
		pstmt = conn.prepareStatement(sproc);
		for (int id = 0; id < param.length; id++) {
			pstmt.setString((id + 1), param[id]);
			// System.out.println(param[id]);
		}
		return (pstmt.executeUpdate());
	}

	synchronized public int queryExecuteUpdate(String sproc, int[] param) throws SQLException {
		// called from receiving page, not forwarding page. number of records should be returned
		// System.out.println(sproc);
		pstmt = conn.prepareStatement(sproc);
		for (int id = 0; id < param.length; id++) {
			pstmt.setInt((id + 1), param[id]);
		}
		return (pstmt.executeUpdate());
	}

	synchronized public int queryExecuteUpdate(String sproc, String[] param, int[] intparam) throws SQLException {
		int id = 0;
		pstmt = conn.prepareStatement(sproc);
		for (id = 0; id < param.length; id++) {
			pstmt.setString((id + 1), param[id]);
			System.out.println((id + 1) + " : " + param[id]);
		}
		for (id = 0; id < intparam.length; id++) {
			pstmt.setInt((param.length + id + 1), intparam[id]);
			System.out.println((id + 1) + " : " + intparam[id]);
		}
		return (pstmt.executeUpdate());
	}

	synchronized public int queryExecuteUpdate(String sproc, int[] intparam, String[] param) throws SQLException {
		int id = 0;
		pstmt = conn.prepareStatement(sproc);
		for (id = 0; id < intparam.length; id++) {
			pstmt.setInt((id + 1), intparam[id]);
			// System.out.println((id+1)+" : "+intparam[id]);
		}
		for (id = 0; id < param.length; id++) {
			pstmt.setString((intparam.length + id + 1), param[id]);
			// System.out.println((id+1)+" : "+param[id]);
		}
		return (pstmt.executeUpdate());
	}

	synchronized public ResultSet queryResults(String sproc, String[] param, int[] intparam) throws SQLException {
		int id = 0;
		pstmt = conn.prepareStatement(sproc);
		for (id = 0; id < param.length; id++) {
			pstmt.setString((id + 1), param[id]);
			// System.out.println((id+1)+" : "+param[id]);
		}
		for (id = 0; id < intparam.length; id++) {
			pstmt.setInt((param.length + id + 1), intparam[id]);
			// System.out.println((id+1)+" : "+intparam[id]);
		}
		rs = pstmt.executeQuery();
		return rs;
	}

	synchronized public ResultSet queryResults(String sproc, int param) throws SQLException {
		pstmt = conn.prepareStatement(sproc);
		pstmt.setInt(1, param);
		rs = pstmt.executeQuery();
		return rs;
	}

	synchronized public ResultSet queryResults(String sproc, String param) throws SQLException {
		pstmt = conn.prepareStatement(sproc);
		pstmt.setString(1, param);
		rs = pstmt.executeQuery();
		return rs;
	}

	synchronized public ResultSet queryResults(String sproc, String[] param) throws SQLException {
		pstmt = conn.prepareStatement(sproc);
		for (int id = 0; id < param.length; id++) {
			pstmt.setString((id + 1), param[id]);
		}
		rs = pstmt.executeQuery();
		return (rs);
	}

	synchronized public ResultSet queryResults(String sproc) throws SQLException {
		stmt = conn.createStatement();
		rs = stmt.executeQuery(sproc);
		return rs;
	}

	// Don't forget to clean up!
	public void closePstmt() throws SQLException {
		if (stmt != null) {
			stmt.close();
			stmt = null;
		} else {
			pstmt.close();
			pstmt = null;
		}
	}

	public void closeConn() throws SQLException {
		conn.close();
		conn = null;
	}

}
