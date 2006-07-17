
package org.caisi.PMmodule.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.PMmodule.utility.Utility;

//##############################################################################

public class Database_Service extends DatabaseConnection
{
	private static Log log = LogFactory.getLog(Database_Service.class);

//##############################################################################

public Database_Service(DataSource dataSource)
{
	super(dataSource);
}
//############################################################################
public  List getTableQueryArrayList(DataSource dataSource, String queryStr)
{

    if(queryStr == null  ||  queryStr.equals("") )
    {
        return null;
    }
    List arr_result = new ArrayList();
    try
    {
    	arr_result = retrieveArrayListFromResultSet(dataSource, queryStr);
    }
    catch(Exception ex)
    {
    	
    }

    return arr_result;
}
//############################################################################
public  String[][] getTableQueryFromArrList(DataSource dataSource, String queryStr)
{
    if(dataSource == null  ||  queryStr == null  ||  queryStr.equals("") )
    {
        return null;
    }
	String[][] queryResult = null;
    
    try
    {
    	List arr_result = retrieveArrayListFromResultSet(dataSource, queryStr);
    	if(arr_result != null  &&  arr_result.size() > 0)
    	{
    		queryResult = Utility.convertArrayListTo2DStringArr(arr_result);
    	}
    }
    catch(Exception ex)
    {
    	
    }
    return queryResult;
}

//###############################################################################
public  ResultSetMetaData retrieveResultSetMetaData(DataSource dataSource, String dbTable)
{
    if(dbTable == null  ||  dbTable.equals(""))
    {
        return null;
    }

    String selectFromTable = " SELECT  * FROM " + dbTable + " ";
    ResultSet resultSet = null;
    try
	{
        resultSet = executeQuery(dataSource, selectFromTable);
  		ResultSetMetaData rsmd = resultSet.getMetaData();

        resultSet.close();
        return rsmd;
	}
    catch(Exception e)
  	{
       return null;
   	}
    finally
    {
        try
        {
        	
        }
        catch(Exception closeEx)
        {
            return null;
        }
    }

}

//############################################################################

public  String[] retrieveTableFieldNames(DataSource dataSource, String dbTable)
{
    if(dbTable == null  ||  dbTable.equals(""))
    {
        return null;
    }

    String selectFromTable = " SELECT TOP 1 " +
                             " * FROM " + dbTable + " ";

    ResultSet resultSet = null;
    String[] columnNames = null;
    int ncol = 0;
    try
	{
        resultSet = executeQuery(dataSource, selectFromTable);
        ResultSetMetaData rsmd = null;

        if(resultSet != null)
        {
      		  rsmd = resultSet.getMetaData();

            if(rsmd != null)
            {
	      	    ncol = rsmd.getColumnCount();

                columnNames = new String[ncol];

                for(int i=0; i < ncol; i++)
                {
                    columnNames[i] = rsmd.getColumnName(i+1);
                }
            }
        }
        resultSet.close();
	}
    catch(Exception e)
  	{
        return null;
   	}
    finally
    {
        try
        {
        	
        }
        catch(Exception closeEx)
        {
            return null;
        }
    }

    return columnNames;
}
//############################################################################
//############################################################################

public  String[][] retrieveTableFieldNamesAndDataTypes(DataSource dataSource, String dbTable)
{
    if(dbTable == null  ||  dbTable.equals(""))
    {
        return null;
    }


    String selectFromTable = " SELECT TOP 1 " +
                             " * FROM " + dbTable + " ";

    ResultSet resultSet = null;
    String[] columnNames = null;
    String[] columnDataTypes = null;
    String[][] columnNamesAndDataTypes = null;
    int ncol = 0;

    try
		{
        resultSet = executeQuery(dataSource, selectFromTable);
        ResultSetMetaData rsmd = null;

        if(resultSet != null)
        {
      		  rsmd = resultSet.getMetaData();

            if(rsmd != null)
            {
	      	      ncol = rsmd.getColumnCount();

                columnNames = new String[ncol];
                columnDataTypes = new String[ncol];
                columnNamesAndDataTypes = new String[ncol][2];//fieldName, fieldType
                for(int i=0; i < ncol; i++)
                {
                    columnNames[i] = rsmd.getColumnName(i+1);
                    columnDataTypes[i] = rsmd.getColumnTypeName(i+1);
                    columnNamesAndDataTypes[i][0] = columnNames[i];
                    columnNamesAndDataTypes[i][1] = columnDataTypes[i];
                }//end of for(int i=0; i < ncol; i++)
            }//end of if(rsmd != null)
        }//end of if(resultSet != null)
        resultSet.close();
	  }
    catch(Exception e)
  	{
        return null;
   	}
    finally
    {
        try
        {
        	
        	
        }
        catch(Exception closeEx)
        {
            return null;
        }
    }

    return columnNamesAndDataTypes;
}

//############################################################################

public  List retrieveArrayListFromResultSet(DataSource dataSource, String sqlStr)
{
    ResultSet resultSet = null;
    List arr_resultSet = new ArrayList();

    if(sqlStr == null  ||  sqlStr.equals("") )
    {
        return null;
    }

    try
	{
        resultSet = executeQuery(dataSource, sqlStr);

  		ResultSetMetaData rsmd = resultSet.getMetaData();
	  	int ncol = rsmd.getColumnCount();
  		while(resultSet.next())
  		{	
		    List arr_row = new ArrayList(ncol);
		    for (int colidx=1; colidx <= ncol; colidx++)
			{
		    	arr_row.add( resultSet.getObject(colidx) );
			}
      		arr_resultSet.add(arr_row);
		}
        resultSet.close();
        return arr_resultSet;
    }
    catch(Exception e)
  	{

        return null;
   	}
    finally
    {
    	
    }

}
//############################################################################

public  ResultSet retrieveResultSet(DataSource dataSource, String sqlStr)
{
    ResultSet resultSet = null;

    if(sqlStr == null  ||  sqlStr.equals("") )
    {
        return null;
    }

    try
	{
        resultSet = super.executeQuery(dataSource, sqlStr);

        return resultSet;
	}
    catch(Exception e)
  	{

        return null;
   	}
    finally
    {
        try
        {
        	
        	
        }
        catch(Exception closeEx)
        {
            return null;
        }
    }

}


//############################################################################

public ResultSet executeQuery(DataSource dataSource, String query)
{
    try
	{
        ResultSet resultSet = super.executeQuery(dataSource, query);
        return resultSet;
	}
    catch(Exception e)
  	{
        return null;
   	}
    finally
    {
        try
        {
        	
        	
        }
        catch(Exception closeEx)
        {
            return null;
        }
    }
}

//############################################################################

public  void executeUpdate(DataSource dataSource, String query) 
{

//    databaseConnection = new DatabaseConnection(request);

    try
	{
        super.executeUpdate(dataSource, query);
	}
    catch(Exception e)
  	{

   	}
    finally
    {
        try
        {
        	
        	
        }
        catch(Exception closeEx)
        {
        }
    }
}
//############################################################################

public boolean executeUpdate(DataSource dataSource, String query, boolean dummy) 
{
    try
	{
        if(super.executeUpdate(dataSource, query, true))
        {
        	return true;
        }
	}
    catch(Exception e)
  	{
    	return false;
   	}
    finally
    {
        try
        {
        	
        }
        catch(Exception closeEx)
        {
        }
    }
    return false;
}

//############################################################################

//################################################################################

//##############################################################################
//################################################################################

public  int colCount(DataSource dataSource, String tblName) 
{
  	int columns = 0;
  	String queryStr = "SELECT * FROM " + tblName;
  	
  	try
  	{
  		ResultSet rs = executeQuery(dataSource, queryStr);
  		ResultSetMetaData md = rs.getMetaData();
  		columns = md.getColumnCount();
  	}
  	catch(SQLException sqlEx)
  	{
//  		closeConnection();
  		return 0;
	}
	finally 
  	{
//  		closeConnection();
  	}  	
	
  	return columns;
}
//################################################################################

public  int rowCount(DataSource dataSource, String tblName)
{
  	int rows = 0;
  	String queryStr = "select * from " + tblName;
	try
	{
		ResultSet rs = executeQuery(dataSource, queryStr);
		while (rs.next())
		{
			rows++;
		}
	}
	catch(SQLException sqlEx)
	{
//		closeConnection();
		return 0;
	}
	finally 
	{
//		closeConnection();
	}  	
	return rows;
}
//################################################################################

//return a particular value depending table, column, and ID
public  String getValue(DataSource dataSource, 
		                String tblName, String colName, String idName, int id) 
{
  	String queryStr = "select " + colName + " from " + tblName + " where " + idName + "=" + id;
	String retValue = "";

	try
	{
   	    ResultSet rs = executeQuery(dataSource, queryStr);
   	    while (rs.next())
        {
   	        retValue = rs.getString(1);
   	    }
  	}
  	catch(SQLException sqlEx)
  	{
//  		closeConnection();
  		return null;
	}
	finally 
  	{
//  		closeConnection();
  	}  	
   	    

   	return retValue;
}
//################################################################################

public  boolean idExist(DataSource dataSource, String tblName, String colName, int id)
{
   	String queryStr = "select count(*) from " + tblName + " where " + colName + "=" + id;
   	boolean exist = false;

   	try   
   	{
	    ResultSet rs = executeQuery(dataSource, queryStr);
  	    while (rs.next())
        {
        	if(rs.getString(1).equals("1"))
        	{
		       exist = true;
        	}
  	    }
	}
	catch(SQLException sqlEx)
	{
//		closeConnection();
		return false;
	}
	finally 
	{
//		closeConnection();
	}  	
   	
    return exist;
}

//################################################################################

public synchronized  int nextID(DataSource dataSource, String tblName, String colName) 
{
  	String queryStr = "select max(" + colName + ") from " + tblName;
	int nextID = 0;
	try
	{
	     //find the maximum ID
	     ResultSet rs = executeQuery(dataSource, queryStr);
	     while (rs.next())
	     {
          nextID = Integer.parseInt(rs.getString(1));
	     }
  	}
	catch(SQLException sqlEx)
	{
//		closeConnection();
		return 0;
	}
	finally 
	{
//		closeConnection();
	}  	
	
	  return ++nextID;
}
//################################################################################

public  String[] getTableHeadings(DataSource dataSource, String tblName) 
{
    int columns = colCount(dataSource, tblName);
  	String[] tableHeadings = new String[columns];
  	String queryStr = "SELECT * FROM " + tblName;
  	try
  	{
	    ResultSet rs = executeQuery(dataSource, queryStr);
  	    ResultSetMetaData md = rs.getMetaData();

	    for(int i = 0; i < columns; i++)
	    {
	    	tableHeadings[i] = md.getColumnName(i + 1);
	    }
  	}
	catch(SQLException sqlEx)
	{
//		closeConnection();
		return null;
	}
	finally 
	{
//		closeConnection();
	}  	
    
    return tableHeadings;
}

//################################################################################

public  String[][] getColNamesAndTypes(DataSource dataSource, String tblName) 
{
    int columns = colCount(dataSource, tblName);
  	String[][] colNamesAndTypes = new String[columns][2];
  	String queryStr = "SELECT * FROM " + tblName;
  	try
  	{
	    ResultSet rs = executeQuery(dataSource, queryStr);
  	    ResultSetMetaData md = rs.getMetaData();

	    for(int i = 0; i < columns; i++)
	    {
	    	colNamesAndTypes[i][0] = md.getColumnName(i + 1);
	    	colNamesAndTypes[i][1] = md.getColumnTypeName(i + 1);
	    }
  	}
	catch(SQLException sqlEx)
	{
//		closeConnection();
		return null;
	}
	finally 
	{
//		closeConnection();
	}  	
    
    return colNamesAndTypes;
}

//################################################################################

public  String[][] getTableContents(DataSource dataSource, String tblName, String colName, String sort) 
{
    int columns = colCount(dataSource, tblName);
    int rows = rowCount(dataSource, tblName);
    String[][] tableContent = new String[rows][columns];
    String queryStr = " SELECT * FROM " + tblName + 
                      " ORDER BY " + colName + " " + sort;
    try
    {
   	    ResultSet rs = executeQuery(dataSource, queryStr);

	      rs.next(); // MySQL supports first() method
   	    for (int r = 0; r < rows; r++)
        {
        	for (int c = 0; c < columns; c++)
            {
       		     tableContent[r][c] = rs.getString(c + 1);
        	}
	        rs.next();
  	    }
  	}
	catch(SQLException sqlEx)
	{
//		closeConnection();
		return null;
	}
	finally 
	{
//		closeConnection();
	}  	

  	return tableContent;
}
//################################################################################
public void setAutoCommit(DataSource dataSource, boolean flag)
{
	super.setAutoCommit(dataSource, flag);
}
//################################################################################
public void rollback(DataSource dataSource)
{
	super.rollback(dataSource);
}

//################################################################################
public void commit(DataSource dataSource)
{
	super.commit(dataSource);
}
//################################################################################
public void closeConnection()
{
	super.closeConnection();
}
//################################################################################
public void closeStatement()
{
	super.closeStatement();
}
//##############################################################################################
}