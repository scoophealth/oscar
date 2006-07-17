
package org.caisi.PMmodule.dao;

//import org.gjt.mm.mysql.Driver; //mysql
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


//################################################################################

public class DatabaseConnection
{
	 Connection con = null;
	 Statement  stmt = null;
	 private static Log log = LogFactory.getLog(DatabaseConnection.class);

//################################################################################
public DatabaseConnection(DataSource dataSource)
{
	boolean conOK = makeConnection(dataSource);
}

//################################################################################
public boolean makeConnection(DataSource dataSource)
{
    if(dataSource != null)
    {
		try
		{
        	con = dataSource.getConnection();
        	createStatement();
 		}
		catch(SQLException sqlExcept1)
		{
			closeConnection();
    		return false;
		}
    }
    return true;
}
//################################################################################

//################################################################################

private  Statement createStatement() throws SQLException
{
    if(con != null  &&  !con.isClosed())
    {
         return con.createStatement();
    }
    else
    {
         return null;
    }
}
//################################################################################

public synchronized  ResultSet executeQuery(DataSource dataSource, String query)
throws SQLException
{
	if(con == null  ||  con.isClosed())
	{
		boolean conOK = makeConnection(dataSource);		
	}
	
	ResultSet rs = null;
	try
	{
		if(con != null  &&  !con.isClosed())
		{
			stmt = createStatement();
            rs = stmt.executeQuery(query);
        }
	}
	catch(SQLException sqlEx)
	{
//  		closeConnection();
  		return null;
	}
	finally 
  	{
//		closeStatement();		
//  		closeConnection();
  	}  	
    return rs;
	
    
}
//################################################################################

public synchronized  void executeUpdate(DataSource dataSource, String query) 
throws SQLException
{
	if(con == null  ||  con.isClosed())
	{
		boolean conOK = makeConnection(dataSource);		
	}
	
  	if(con != null  &&  !con.isClosed())
    {
  		try
  		{
  			setAutoCommit(dataSource, false);  
  			
  			stmt = createStatement();
  			stmt.executeUpdate(query);
  			
  			commit(dataSource);
  		}
  		catch(SQLException sqlEx)
  		{
  			rollback(dataSource);
  		}
  		finally 
  		{
//  			closeStatement();		
//  			closeConnection();
  		}  		
  	}
}
//################################################################################

public synchronized  boolean executeUpdate(DataSource dataSource, 
		                                   String updateStr, boolean dummy) 
throws SQLException
{
	if(con == null  ||  con.isClosed())
	{
		boolean conOK = makeConnection(dataSource);		
	}
	
  	if(con != null  &&  !con.isClosed())
    {
  		try
  		{
  			setAutoCommit(dataSource, false);  
  			
  			stmt = createStatement();
  			if(stmt.executeUpdate(updateStr) > 0)
  			{
  	  			commit(dataSource);
  				return true;
  			}
  		}
  		catch(SQLException sqlEx)
  		{
  			rollback(dataSource);
  			return false;
  		}
  		finally 
  		{
//  			closeStatement();		
//  			closeConnection();
  		}  		
  	}
  	return false;
}
//##########################################################################
public synchronized void setAutoCommit(DataSource dataSource, boolean flag)
{
	try
	{
		if(con == null  ||  con.isClosed())
		{
			boolean conOK = makeConnection(dataSource);		
		}
		if(con != null  &&  !con.isClosed())
		{
			con.setAutoCommit(flag);
		}
  	}
	catch(SQLException sqlEx)
	{
//		closeConnection();
	}

}
//################################################################################
public synchronized void rollback(DataSource dataSource)
{
	try
	{
		if(con == null  ||  con.isClosed())
		{
			boolean conOK = makeConnection(dataSource);		
		}
		if(con != null  &&  !con.isClosed())
		{
			con.rollback();
		}
  	}
	catch(SQLException sqlEx)
	{
//		closeConnection();
	}
}
//################################################################################
public synchronized void commit(DataSource dataSource)
{
	try
	{
		if(con == null  ||  con.isClosed())
		{
			boolean conOK = makeConnection(dataSource);		
		}
		if(con != null  &&  !con.isClosed())
		{
			con.commit();
		}
  	}
	catch(SQLException sqlEx)
	{
//		closeConnection();
	}
}

//################################################################################
public void closeConnection()
{
   try
   {
	   if(con != null  ||  !con.isClosed())
	   {
		   con.close();
	   }
   }
   catch(SQLException sqlEx)
   {
   }
}
//################################################################################
public void closeStatement()
{
   try
   {
	   if(stmt != null)
	   {
		   stmt.close();
		   stmt = null;
	   }
   }
   catch(SQLException sqlEx)
   {
	   
   }
}

//################################################################################

}

