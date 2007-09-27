package org.oscarehr.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import oscar.util.SqlUtils;

public class DbConnectionFilter implements javax.servlet.Filter
{
    private static ThreadLocal<Connection> dbConnection=new ThreadLocal<Connection>();
    
    public static Connection getThreadLocalDbConnection() throws SQLException
    {
        Connection c=dbConnection.get();
        if (c==null || c.isClosed())
        {
            c=SpringUtils.getDbConnection();
            dbConnection.set(c);
        }

        return(c);
    }
    
    
	public void init(FilterConfig filterConfig) throws ServletException
	{
        // nothing
	}

	public void doFilter(ServletRequest tmpRequest, ServletResponse tmpResponse, FilterChain chain) throws IOException, ServletException
	{
        try
        {
            chain.doFilter(tmpRequest, tmpResponse);
        }
        finally
        {
            releaseThreadLocalDbConnection();
        }
	}


    public static void releaseThreadLocalDbConnection() {
        Connection c=dbConnection.get();
        SqlUtils.closeResources(c, null, null);
        dbConnection.remove();
    }

	public void destroy()
	{
		// can't think of anything to do right now.
	}
}
