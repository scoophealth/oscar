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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.persistence.PersistenceException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;

public class SqlUtils {
	private static Logger logger = MiscUtils.getLogger();

	private enum DatabaseTypes {
		MYSQL, ORACLE, POSTGRESQL
	}

	private static java.sql.Date createAppropriateDate(Object value) {
		if (value == null) {
			return null;
		}
		String valueStr = ((String) value).trim();
		if (valueStr.length() == 0) {
			return null;
		}
		SimpleDateFormat sdf = DateUtils.getDateFormatter();
		Date result = null;
		try {
			result = new java.sql.Date(sdf.parse(valueStr).getTime());
		}
		catch (Exception exc) {
			result = null;
		}
		if (result == null) {
			// Maybe date has been returned as a timestamp?
			try {
				result = new java.sql.Date(java.sql.Timestamp.valueOf(valueStr).getTime());
			}
			catch (java.lang.IllegalArgumentException ex) {
				// Try date
				logger.info("date = " + valueStr);
				result = java.sql.Date.valueOf(valueStr);
			}
		}
		return result;
	}

	private static java.math.BigDecimal createAppropriateNumeric(Object value) {
		if (value == null) {
			return null;
		}
		String valueStr = ((String) value).trim();
		if (valueStr.length() == 0) {
			return null;
		}
		return new java.math.BigDecimal(valueStr);
	}

	/**
	 * this utility-method assigns a particular value to a place holder of a PreparedStatement. it tries to find the correct setXxx() value, accoring to the field-type information
	 * represented by "fieldType". quality: this method is bloody alpha (as you migth see :=)
	 */
	public static void fillPreparedStatement(PreparedStatement ps, int col, Object val, int fieldType) throws SQLException {
		try {
			logger.info("fillPreparedStatement( ps, " + col + ", " + val + ", " + fieldType + ")...");
			Object value = null;
			// Check for hard-coded NULL
			if (!("$null$".equals(val))) {
				value = val;
			}
			if (value != null) {
				switch (fieldType) {
				case FieldTypes.INTEGER:
					ps.setInt(col, Integer.parseInt((String) value));
					break;
				case FieldTypes.NUMERIC:
					ps.setBigDecimal(col, createAppropriateNumeric(value));
					break;
				case FieldTypes.CHAR:
					ps.setString(col, (String) value);
					break;
				case FieldTypes.DATE:
					ps.setDate(col, createAppropriateDate(value));
					break; // #checkme
				case FieldTypes.TIMESTAMP:
					ps.setTimestamp(col, java.sql.Timestamp.valueOf((String) value));
					break;
				case FieldTypes.DOUBLE:
					ps.setDouble(col, Double.valueOf((String) value).doubleValue());
					break;
				case FieldTypes.FLOAT:
					ps.setFloat(col, Float.valueOf((String) value).floatValue());
					break;
				case FieldTypes.LONG:
					ps.setLong(col, Long.parseLong(String.valueOf(value)));
					break;
				case FieldTypes.BLOB:
					FileHolder fileHolder = (FileHolder) value;
					try {
						ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
						ObjectOutputStream out = new ObjectOutputStream(byteOut);
						out.writeObject(fileHolder);
						out.flush();
						byte[] buf = byteOut.toByteArray();
						byteOut.close();
						out.close();
						ByteArrayInputStream bytein = new ByteArrayInputStream(buf);
						int byteLength = buf.length;
						ps.setBinaryStream(col, bytein, byteLength);
						// store fileHolder as a whole (this way we don't lose file meta-info!)
					}
					catch (IOException ioe) {
						MiscUtils.getLogger().error("Error", ioe);
						logger.info(ioe.toString());
						throw new SQLException("error storing BLOB in database - " + ioe.toString(), null, 2);
					}
					break;
				case FieldTypes.DISKBLOB:
					ps.setString(col, (String) value);
					break;
				default:
					ps.setObject(col, value); // #checkme
				}
			}
			else {
				switch (fieldType) {
				case FieldTypes.INTEGER:
					ps.setNull(col, java.sql.Types.INTEGER);
					break;
				case FieldTypes.NUMERIC:
					ps.setNull(col, java.sql.Types.NUMERIC);
					break;
				case FieldTypes.CHAR:
					ps.setNull(col, java.sql.Types.CHAR);
					break;
				case FieldTypes.DATE:
					ps.setNull(col, java.sql.Types.DATE);
					break;
				case FieldTypes.TIMESTAMP:
					ps.setNull(col, java.sql.Types.TIMESTAMP);
					break;
				case FieldTypes.DOUBLE:
					ps.setNull(col, java.sql.Types.DOUBLE);
					break;
				case FieldTypes.FLOAT:
					ps.setNull(col, java.sql.Types.FLOAT);
					break;
				case FieldTypes.BLOB:
					ps.setNull(col, java.sql.Types.BLOB);
					break;
				case FieldTypes.DISKBLOB:
					ps.setNull(col, java.sql.Types.CHAR);
					break;
				default:
					ps.setNull(col, java.sql.Types.OTHER);
				}
			}
		}
		catch (Exception e) {
			throw new SQLException("Field type seems to be incorrect - " + e.toString(), null, 1);
		}
	}

	/**
	 * A simple and convenient method for retrieving object by criteria from the database. The ActiveRecord pattern is assumed whereby and object represents a row in the database.
	 * <p>
	 *
	 * @param qry
	 *            String
	 * @param classType
	 *            Class
	 * @return List
	 */
	public static List getBeanList(String qry, Class classType) {
		ArrayList rec = new ArrayList();
		int colCount = 0;
		ResultSet rs = null;

		try {

			rs = DBHandler.GetSQL(qry);
			ResultSetMetaData rsmd = rs.getMetaData();
			colCount = rsmd.getColumnCount();

			while (rs.next()) {
				int recordCount = 0; // used to check if an objects methods have been determined
				Object obj = null;
				Method method[] = null;
				Hashtable methodNameMap = new Hashtable(colCount);
				obj = classType.newInstance();
				Class cls = obj.getClass();
				method = cls.getDeclaredMethods();
				// iterate through each field in record and set data in the appropriate
				// object field. Each matching method name is to be placed in a list of method names
				// to be used in subsequent iterations. This will reduce the overhead in having to search those names needlessly
				for (int i = 0; i < colCount; i++) {
					String colName = rsmd.getColumnName(i + 1);
					Object value = getNewType(rs, i + 1);

					// if this is the first record, get list of method names in object
					// and perform method invocation

					if (recordCount == 0) {
						for (int j = 0; j < method.length; j++) {
							String methodName = method[j].getName();
							char[] b = { '_' };
							String columnCase = WordUtils.capitalize(colName, b);
							columnCase = org.apache.commons.lang.StringUtils.remove(columnCase, '_');
							columnCase = org.apache.commons.lang.StringUtils.capitalize(columnCase);

							if (methodName.equalsIgnoreCase("set" + colName)) {
								method[j].invoke(obj, new Object[] { value });
								methodNameMap.put(new Integer(j), methodName);
							}
							else if (methodName.equalsIgnoreCase("set" + columnCase)) {
								method[j].invoke(obj, new Object[] { value });
								methodNameMap.put(new Integer(j), methodName);
							}
						}
					}
					// else method names have been determined so perform invocations based on list
					else {
						for (Enumeration keys = methodNameMap.keys(); keys.hasMoreElements();) {
							Integer key = (Integer) keys.nextElement();
							MiscUtils.getLogger().debug(method[key.intValue()].getName() + " value  " + value.getClass().getName());
							method[key.intValue()].invoke(obj, new Object[] { value });
						}
					}
				}
				rec.add(obj);
				recordCount++;
			}
		}
		catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		catch (IllegalAccessException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		catch (InvocationTargetException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		catch (InstantiationException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
			}
			catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
			}
		}
		return rec;
	}

	private static Object getNewType(ResultSet rs, int colNum) {
		int type = 0;
		try {
			type = rs.getMetaData().getColumnType(colNum);
			switch (type) {
			case Types.LONGVARCHAR:
			case Types.CHAR:
			case Types.VARCHAR:
				return oscar.Misc.getString(rs, colNum);
			case Types.TINYINT:
			case Types.SMALLINT:
			case Types.INTEGER:
				return new Integer(rs.getInt(colNum));
			case Types.BIGINT:
				return new Long(rs.getLong(colNum));
			case Types.FLOAT:
			case Types.DECIMAL:
			case Types.REAL:
			case Types.DOUBLE:
			case Types.NUMERIC:
				return new Double(rs.getDouble(colNum));
				// case Types.B
			case Types.BIT:
				return new Boolean(rs.getBoolean(colNum));
			case Types.TIMESTAMP:
			case Types.DATE:
			case Types.TIME:
				return rs.getDate(colNum);
			default:
				return rs.getObject(colNum);
			}
		}
		catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return null;
	}

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
	 * @param type
	 *            int - a value of true indicates that the clause components are enclosed in quotes
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
	 * This method will return a string similar to "(?,?,?,?)". The intent is that this method will be used to build "in clauses" like select * from foo where x in (?,?,?) for
	 * prepared statements.
	 */
	public static String constructInClauseForPreparedStatements(int numberOfParameters) {
		if (numberOfParameters <= 0) throw (new IllegalArgumentException("Don't call this method if the numberOfParameters is <1 it doesn't make sense."));

		StringBuilder sb = new StringBuilder();
		sb.append('(');

		for (int i = 0; i < numberOfParameters; i++) {
			if (i > 0) sb.append(',');
			sb.append('?');
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

	public static String addOneDay(String oldDate) throws ParseException {
		SimpleDateFormat isoFormat = new SimpleDateFormat("dd-MMM-yyyy");
		java.util.Date date1 = isoFormat.parse(oldDate);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date1);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		date1 = calendar.getTime();

		SimpleDateFormat oracleFormat = new SimpleDateFormat("dd-MMM-yyyy");

		return (oracleFormat.format(date1));
	}

	private static DatabaseTypes getDatabaseType() {
		BasicDataSource basicDataSource = (BasicDataSource) SpringUtils.getBean("dataSource");
		String driverName = basicDataSource.getDriverClassName();

		if (driverName.startsWith("com.mysql.")) return (DatabaseTypes.MYSQL);
		if (driverName.startsWith("org.postgresql.")) return (DatabaseTypes.POSTGRESQL);
		if (driverName.startsWith("oracle.")) return (DatabaseTypes.ORACLE);
		else throw (new IllegalArgumentException("Need a new database driver type added : " + driverName));
	}

	/**
	 * This method will return the like condition for the appropriate database. As an example on mysql it will return "name like 'bob'" on postgres it would be "name ilike 'bob'"
	 * on oracle "regexp_like(name, 'bob', 'i')"
	 */
	public static String getCaseInsensitiveLike(String column, String pattern) {
		DatabaseTypes databaseType = getDatabaseType();

		if (databaseType == DatabaseTypes.MYSQL) return (column + " like '" + pattern + '\'');
		if (databaseType == DatabaseTypes.POSTGRESQL) return (column + " ilike '" + pattern + '\'');
		if (databaseType == DatabaseTypes.ORACLE) return ("regexp_like(" + column + ",'" + pattern + "','i')");
		else throw (new IllegalArgumentException("Need a new databaseType added : " + databaseType));
	}

	/**
	 * deprecated use jpa native queries instead
	 */
	public static List<Integer> selectIntList(String sqlCommand) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = DbConnectionFilter.getThreadLocalDbConnection();
			ps = c.prepareStatement(sqlCommand);
			rs = ps.executeQuery();

			ArrayList<Integer> al = new ArrayList<Integer>();

			while (rs.next())
				al.add(rs.getInt(1));

			return (al);
		}
		catch (SQLException e) {
			throw (new PersistenceException(e));
		}
		finally {
			closeResources(c, ps, rs);
		}
	}

	/**
	 * deprecated use jpa native queries instead
	 */
	public static List<String> selectStringList(String sqlCommand) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = DbConnectionFilter.getThreadLocalDbConnection();
			ps = c.prepareStatement(sqlCommand);
			rs = ps.executeQuery();

			ArrayList<String> al = new ArrayList<String>();

			while (rs.next())
				al.add(rs.getString(1));

			return (al);
		}
		catch (SQLException e) {
			throw (new PersistenceException(e));
		}
		finally {
			closeResources(c, ps, rs);
		}
	}

	public static int update(String sqlCommand) {
		Connection c = null;
		Statement s = null;
		try {
			c = DbConnectionFilter.getThreadLocalDbConnection();
			s = c.createStatement();
			return (s.executeUpdate(sqlCommand));
		}
		catch (SQLException e) {
			throw (new PersistenceException(e));
		}
		finally {
			closeResources(c, s, null);
		}
	}

	public static String getCurrentDatabaseName() {
		Connection c = null;
		try {
			c = DbConnectionFilter.getThreadLocalDbConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select database()");
			rs.next();
			return (rs.getString(1));
		}
		catch (SQLException e) {
			throw (new PersistenceException(e));
		}
		finally {
			closeResources(c, null, null);
		}
	}

}
