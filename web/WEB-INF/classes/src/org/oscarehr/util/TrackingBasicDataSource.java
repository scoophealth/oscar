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
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

public class TrackingBasicDataSource extends BasicDataSource {
	
	public static final Logger logger=MiscUtils.getLogger();
	
    public static final Map<Connection, StackTraceElement[]> debugMap = Collections.synchronizedMap(new WeakHashMap<Connection, StackTraceElement[]>());
    // This is a fake weak hash set, the value is actually ignored, put null or what ever in it.
    private static ThreadLocal<WeakHashMap<Connection, Object>> connections = new ThreadLocal<WeakHashMap<Connection, Object>>();

    private static void trackConnection(Connection c)
    {
        debugMap.put(c, Thread.currentThread().getStackTrace());

        WeakHashMap<Connection, Object> map=connections.get();
        if (map==null)
        {
        	map=new WeakHashMap<Connection, Object>();
        	connections.set(map);
        }
        
        map.put(c, null);
        
        if (map.size()>5)
        {
        	String msg="Thread has more than 5 jdbc connection in use simultaniously.";
        	logger.warn(msg);
        	logger.debug(msg, new Exception(msg));
        }
    }
    
    public static void releaseThreadConnections()
    {
        WeakHashMap<Connection, Object> map=connections.get();
        if (map!=null)
        {
        	for (Connection c : map.keySet())
        	{
        		try {
	                if (!c.isClosed()) c.close();
                } catch (SQLException e) {
                	logger.error("Error closing jdbc connection.", e);
                }
        	}
        }
        
        connections.remove();
    }
    
	public Connection getConnection() throws SQLException {
		Connection c=super.getConnection();
		trackConnection(c);
	    return(new TrackingJdbcConnection(c));
    }

	public Connection getConnection(String username, String password) throws SQLException {
	    Connection c=super.getConnection(username, password);
		trackConnection(c);
	    return(new TrackingJdbcConnection(c));
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
			try
			{
				debugMap.remove(connection);
			}
			finally
			{
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
