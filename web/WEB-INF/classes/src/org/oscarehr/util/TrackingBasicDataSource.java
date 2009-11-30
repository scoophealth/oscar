package org.oscarehr.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.DelegatingConnection;
import org.apache.log4j.Logger;

public class TrackingBasicDataSource extends BasicDataSource {
	
	public static final int MAX_CONNECTION_WARN_SIZE=2;
	public static final Logger logger=MiscUtils.getLogger();
	
    public static final Map<Connection, StackTraceElement[]> debugMap = Collections.synchronizedMap(new WeakHashMap<Connection, StackTraceElement[]>());
    private static final ThreadLocal<HashSet<Connection>> connections = new ThreadLocal<HashSet<Connection>>();

    private static Connection trackConnection(Connection c)
    {
    	c=new TrackingJdbcConnection(c);
    	
    	debugMap.put(c, Thread.currentThread().getStackTrace());

    	HashSet<Connection> threadConnections=connections.get();
        if (threadConnections==null)
        {
        	threadConnections=new HashSet<Connection>();
        	connections.set(threadConnections);
        }
        
        threadConnections.add(c);
        
        if (threadConnections.size()>MAX_CONNECTION_WARN_SIZE)
        {
        	String msg="Thread is currently using "+threadConnections.size()+" separate jdbc connections, it souldn't need more than "+MAX_CONNECTION_WARN_SIZE;
        	logger.warn(msg);
        	logger.debug(msg, new Exception(msg));
        }

        return(c);
    }
    
    public static void releaseThreadConnections()
    {
    	HashSet<Connection> threadConnections=connections.get();
        if (threadConnections!=null && threadConnections.size()>0)
        {

        	threadConnections=new HashSet<Connection>(threadConnections);
        	for (Connection c : threadConnections)
        	{
        		try {
	                if (!c.isClosed())
	                {
	                	c.close();
	                }
                } catch (SQLException e) {
                	logger.error("Error closing jdbc connection.", e);
                }
        	}
        }
        
        connections.remove();
    }
    
	public Connection getConnection() throws SQLException {
		Connection c=trackConnection(super.getConnection());
	    return(c);
    }

	public Connection getConnection(String username, String password) throws SQLException {
	    Connection c=trackConnection(super.getConnection(username, password));
	    return(c);
    }
	

	public static class TrackingJdbcConnection extends DelegatingConnection {

		public TrackingJdbcConnection(Connection connection) {    	
			super(connection);
	    }

		public void close() throws SQLException {
			try
			{
				debugMap.remove(this);
				
				HashSet<Connection> map=connections.get();
				if (map!=null)
				{
					map.remove(this);
				}
			}
			finally
			{
				try {
	                super.close();
                } catch (Exception e) {
                	logger.debug("Already closed.", e);
                }
			}
	    }		
	}
}
