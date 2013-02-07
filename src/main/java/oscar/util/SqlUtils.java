/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class SqlUtils {
	private static Logger logger = MiscUtils.getLogger();




	/**
	 * Returns a List of String[] which contain the results of the specified arbitrary query.
	 *
	 * @param qry
	 *            String - The String SQL Query
	 * @return List - The List of String[] results or null if no results were yielded
	 */
	public static List<String[]> getQueryResultsList(String qry) {
		ArrayList<String[]> records = null;
		ResultSet rs = null;

		try {
			records = new ArrayList<String[]>();

			rs = DBHandler.GetSQL(qry);
			int cols = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				String[] record = new String[cols];
				for (int i = 0; i < cols; i++) {
					record[i] = oscar.Misc.getString(rs, i + 1);
				}
				records.add(record);
			}
		}
		catch (SQLException e) {
			records = null;
			MiscUtils.getLogger().error("Error", e);
		}

			if (rs != null) {
				try {
					rs.close();
				}
				catch (SQLException ex1) {MiscUtils.getLogger().error("Error", ex1);
				}
			}
			if (records != null) {
				records = records.isEmpty() ? null : records;
			}
			return records;
	}

	/**
	 * Returns a single row(the first row) from a quesry result Generally should only be used with queries that return a single result Returns null if there is no result
	 *
	 * @param qry
	 *            String
	 * @return String[]
	 */
	public static String[] getRow(String qry) {
		String ret[] = null;
		List<String[]> list = getQueryResultsList(qry);
		if (list != null) {
			ret = list.get(0);
		}
		return ret;
	}

	/**
	 * Returns a List of Map objects which contain the results of the specified arbitrary query. The key contains the field names of the table and the value, the field value of the
	 * record
	 *
	 * @param qry
	 *            String - The String SQL Query
	 * @return List - The List of String Map results or null if no results were yielded
	 */
	public static List<Properties> getQueryResultsMapList(String qry) {
		List<Properties> records = null;
		ResultSet rs = null;

		try {
			records = new ArrayList<Properties>();

			rs = DBHandler.GetSQL(qry);
			int cols = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				Properties record = new Properties();
				for (int i = 0; i < cols; i++) {
					String columnName = rs.getMetaData().getColumnName(i + 1);
					String cellValue = oscar.Misc.getString(rs, i + 1);
					if (columnName != null && !"".equals(columnName)) {

						cellValue = cellValue == null ? "" : cellValue;
						record.setProperty(columnName, cellValue);
					}
					else {
						MiscUtils.getLogger().debug("Empty key for value: " + cellValue);
					}
				}
				records.add(record);
			}
		}
		catch (SQLException e) {
			records = null;
			MiscUtils.getLogger().error("Error", e);
		}

			if (rs != null) {
				try {
					rs.close();
				}
				catch (SQLException ex1) {MiscUtils.getLogger().error("Error", ex1);
				}
			}

			return records;
	}

	/**
	 * Creates an 'in' clause segment of an sql query. This is handy in cases where the criteria of a query is dynamic/unknown
	 *
	 * @param criteria
	 *            String[] - he string array of criteria used to construct the query segment
	 * @param useQuotes
	 *            boolean - a value of true indicates that the clause components are enclosed in quotes
	 * @return String - The constructed sql 'in' clause String
	 */
	public static String constructInClauseString(String[] criteria, boolean useQuotes) {
		StringBuilder ret = new StringBuilder();
		String quote = useQuotes == true ? "'" : "";
		if (criteria.length != 0) {
			ret.append("in (");
			for (int i = 0; i < criteria.length; i++) {
				ret.append(quote + criteria[i] + quote);
				if (i < criteria.length - 1) {
					ret.append(",");
				}
			}
		}
		ret.append(")");
		return ret.toString();
	}

	/**
	 * This method will return a string similar to "(1,3,5,7)". The intent is that this method will be used to build "in clauses" like select * from foo where x in (1,3,5,7) for
	 * statements. This only works for primitives unless you pre-quote strings.
	 */
	public static String constructInClauseForStatements(Object[] items) {
		return(constructInClauseForStatements(items, false));
	}

	/**
	 * This method will return a string similar to "(1,3,5,7)". The intent is that this method will be used to build "in clauses" like select * from foo where x in (1,3,5,7) for
	 * statements. This only works for primitives unless you pre-quote strings.
	 */
	public static String constructInClauseForStatements(Object[] items, boolean quoteItems) {
		if (items.length <= 0) throw (new IllegalArgumentException("Don't call this method if the items for the in clause is <1 it doesn't make sense."));

		StringBuilder sb = new StringBuilder();
		sb.append('(');

		for (Object item : items) {
			if (sb.length() > 1) sb.append(',');
			if (quoteItems) sb.append('\'');
			sb.append(item);
			if (quoteItems) sb.append('\'');
		}

		sb.append(')');
		return (sb.toString());
	}


	/**
	 * This method will close the resources passed in. Pass in null for anything you don't want closed. All exceptions will be logged at WARN level but not rethrown. Note that if
	 * you retrieved the connection from something like hibernate/jpa you should not close the connection, let the entityManager / sessionManager do that, just close the statement
	 * and resultset.
	 */
	public static void closeResources(Connection c, Statement s, ResultSet rs) {
		closeResources(s, rs);

		if (c != null) {
			try {
				c.close();
			}
			catch (Exception e) {
				if (e.getMessage().toLowerCase().indexOf("closed") >= 0) {
					// I don't care.
				}
				else {
					logger.warn("Error closing Connection.", e);
				}
			}
		}
	}

	public static void closeResources(Session session, Statement s, ResultSet rs) {
		closeResources(s, rs);

		if (session != null) {
			session.close();
		}
	}

	public static void closeResources(Statement s, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException e) {
				logger.warn("Error closing ResultSet.", e);
			}
		}

		if (s != null) {
			try {
				s.close();
			}
			catch (SQLException e) {
				logger.warn("Error closing Statement.", e);
			}
		}
	}


}
