/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.util;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

public class TrackingBasicDataSource extends BasicDataSource {

	public static final int MAX_CONNECTION_WARN_SIZE = 2;
	public static final Logger logger = MiscUtils.getLogger();

	public static final Map<Connection, StackTraceElement[]> debugMap = Collections.synchronizedMap(new WeakHashMap<Connection, StackTraceElement[]>());
	private static final ThreadLocal<HashSet<Connection>> connections = new ThreadLocal<HashSet<Connection>>();

	private static Connection trackConnection(Connection c) {
		c = new TrackingJdbcConnection(c);

		debugMap.put(c, Thread.currentThread().getStackTrace());

		HashSet<Connection> threadConnections = connections.get();
		if (threadConnections == null) {
			threadConnections = new HashSet<Connection>();
			connections.set(threadConnections);
		}

		threadConnections.add(c);

		if (threadConnections.size() > MAX_CONNECTION_WARN_SIZE) {
			String msg = "Thread is currently using " + threadConnections.size() + " separate jdbc connections, it souldn't need more than " + MAX_CONNECTION_WARN_SIZE;
			logger.warn(msg);
			logger.debug(msg, new Exception(msg));
		}

		return (c);
	}

	public static void releaseThreadConnections() {
		HashSet<Connection> threadConnections = connections.get();
		if (threadConnections != null && threadConnections.size() > 0) {

			threadConnections = new HashSet<Connection>(threadConnections);
			for (Connection c : threadConnections) {
				try {
					if (!c.isClosed()) {
						c.close();
					}
				} catch (SQLException e) {
					logger.error("Error closing jdbc connection.", e);
				}
			}
		}

		connections.remove();
	}

	public static void logDebugMapToError() {
		String divider="------------------------------";

		HashMap<Connection, StackTraceElement[]> connectionMap = new HashMap<Connection, StackTraceElement[]>(debugMap);
		for (Map.Entry<Connection, StackTraceElement[]> entry : connectionMap.entrySet()) {
			String key = entry.getKey().hashCode() + ":" + entry.getKey().toString();
			String value = (Arrays.toString(entry.getValue())).replace(",", "\n");
			logger.error(divider+'\n'+key + '\n' + value+'\n'+divider);
		}
	}

	public Connection getConnection() throws SQLException {
		try {
			Connection c = trackConnection(super.getConnection());
			return (c);
		} catch (RuntimeException e) {
			logger.error("Error:", e);
			logDebugMapToError();
			throw (e);
		}
	}

	public Connection getConnection(String username, String password) throws SQLException {
		try {
			Connection c = trackConnection(super.getConnection(username, password));
			return (c);
		} catch (RuntimeException e) {
			logger.error("Error:", e);
			logDebugMapToError();
			throw (e);
		}
	}

	public static class TrackingJdbcConnection implements Connection {

		private Connection connection;

		public TrackingJdbcConnection(Connection connection) {
			this.connection = connection;
		}

		public void clearWarnings() throws SQLException {
			connection.clearWarnings();
		}

		public void close() throws SQLException {
			try {
				debugMap.remove(this);

				HashSet<Connection> map = connections.get();
				if (map != null) {
					map.remove(this);
				}
			} finally {
				connection.close();
			}
		}

		public void commit() throws SQLException {
			connection.commit();
		}

		public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
			return connection.createArrayOf(typeName, elements);
		}

		public Blob createBlob() throws SQLException {
			return connection.createBlob();
		}

		public Clob createClob() throws SQLException {
			return connection.createClob();
		}

		public NClob createNClob() throws SQLException {
			return connection.createNClob();
		}

		public SQLXML createSQLXML() throws SQLException {
			return connection.createSQLXML();
		}

		public Statement createStatement() throws SQLException {
			return connection.createStatement();
		}

		public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
			return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
			return connection.createStatement(resultSetType, resultSetConcurrency);
		}

		public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
			return connection.createStruct(typeName, attributes);
		}

		public boolean getAutoCommit() throws SQLException {
			return connection.getAutoCommit();
		}

		public String getCatalog() throws SQLException {
			return connection.getCatalog();
		}

		public Properties getClientInfo() throws SQLException {
			return connection.getClientInfo();
		}

		public String getClientInfo(String name) throws SQLException {
			return connection.getClientInfo(name);
		}

		public int getHoldability() throws SQLException {
			return connection.getHoldability();
		}

		public DatabaseMetaData getMetaData() throws SQLException {
			return connection.getMetaData();
		}

		public int getTransactionIsolation() throws SQLException {
			return connection.getTransactionIsolation();
		}

		public Map<String, Class<?>> getTypeMap() throws SQLException {
			return connection.getTypeMap();
		}

		public SQLWarning getWarnings() throws SQLException {
			return connection.getWarnings();
		}

		public boolean isClosed() throws SQLException {
			return connection.isClosed();
		}

		public boolean isReadOnly() throws SQLException {
			return connection.isReadOnly();
		}

		public boolean isValid(int timeout) throws SQLException {
			return connection.isValid(timeout);
		}

		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return connection.isWrapperFor(iface);
		}

		public String nativeSQL(String sql) throws SQLException {
			return connection.nativeSQL(sql);
		}

		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
			return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
			return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
		}

		public CallableStatement prepareCall(String sql) throws SQLException {
			return connection.prepareCall(sql);
		}

		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
			return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
			return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
		}

		public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
			return connection.prepareStatement(sql, autoGeneratedKeys);
		}

		public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
			return connection.prepareStatement(sql, columnIndexes);
		}

		public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
			return connection.prepareStatement(sql, columnNames);
		}

		public PreparedStatement prepareStatement(String sql) throws SQLException {
			return connection.prepareStatement(sql);
		}

		public void releaseSavepoint(Savepoint savepoint) throws SQLException {
			connection.releaseSavepoint(savepoint);
		}

		public void rollback() throws SQLException {
			connection.rollback();
		}

		public void rollback(Savepoint savepoint) throws SQLException {
			connection.rollback(savepoint);
		}

		public void setAutoCommit(boolean autoCommit) throws SQLException {
			connection.setAutoCommit(autoCommit);
		}

		public void setCatalog(String catalog) throws SQLException {
			connection.setCatalog(catalog);
		}

		public void setClientInfo(Properties properties) throws SQLClientInfoException {
			connection.setClientInfo(properties);
		}

		public void setClientInfo(String name, String value) throws SQLClientInfoException {
			connection.setClientInfo(name, value);
		}

		public void setHoldability(int holdability) throws SQLException {
			connection.setHoldability(holdability);
		}

		public void setReadOnly(boolean readOnly) throws SQLException {
			connection.setReadOnly(readOnly);
		}

		public Savepoint setSavepoint() throws SQLException {
			return connection.setSavepoint();
		}

		public Savepoint setSavepoint(String name) throws SQLException {
			return connection.setSavepoint(name);
		}

		public void setTransactionIsolation(int level) throws SQLException {
			connection.setTransactionIsolation(level);
		}

		public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
			connection.setTypeMap(map);
		}

		public <T> T unwrap(Class<T> iface) throws SQLException {
			return connection.unwrap(iface);
		}
	}
}
