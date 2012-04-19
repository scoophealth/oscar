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


package oscar.oscarDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.oscarehr.util.DbConnectionFilter;

import oscar.OscarProperties;

/**
 * deprecated Use JPA instead, no new code should be written against this class.
 */
public final class DBPreparedHandler {
    
    ResultSet rs = null;
    Statement stmt = null;
    PreparedStatement preparedStmt = null;

    synchronized public void procExecute(String procName, String[] param) throws SQLException {
    	String sql = "{call " + procName ;
    	if (param != null && param.length > 0) {
	    	String prms = "";
	    	for(int i=0;i< param.length;i++)
	    	{
	    		prms += "?,";
	    	}
	    	if (!prms.equals("")) sql += "(" + prms.substring(0,prms.length()-1) + ")";
    	}
    	
    	sql += "}";
    	CallableStatement   stmt = DbConnectionFilter.getThreadLocalDbConnection().prepareCall(sql);
    	if (param != null && param.length > 0) {
	        for (int i = 0; i < param.length; i++) {
	            stmt.setString((i + 1), param[i]);

	        }
    	}
        stmt.execute();
    }

    synchronized public void queryExecute(String preparedSQL, String[] param) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
            preparedStmt.setString((i + 1), param[i]);

        }
        preparedStmt.execute();
    }
    synchronized public int queryExecuteUpdate(String preparedSQL) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        return(preparedStmt.executeUpdate());
    }
    synchronized public int queryExecuteInsertReturnId(String preparedSQL) throws SQLException {
    	return queryExecuteInsertReturnId(preparedSQL, null);
    }
    synchronized public int queryExecuteInsertReturnId(String preparedSQL, DBPreparedHandlerParam[] params) throws SQLException {
    	Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
    	boolean ac = conn.getAutoCommit();
		conn.setAutoCommit(false);
    	try {
    		if (params == null) {
    			queryExecuteUpdate(preparedSQL);
    		}
    		else 
    		{
    			queryExecuteUpdate(preparedSQL,params);
    		}
    		OscarProperties prop = OscarProperties.getInstance();
    		String db_type = prop.getProperty("db_type", "mysql").trim();
	        String sql = "";
	        if (db_type.equalsIgnoreCase("mysql")) {
	               sql= "SELECT LAST_INSERT_ID()";
	         } else if (db_type.equalsIgnoreCase("postgresql")) {
	               sql = "SELECT CURRVAL('messagetbl_int_seq')";
	         } else if (db_type.equalsIgnoreCase("oracle")) {
	        	   sql = "SELECT HIBERNATE_SEQUENCE.CURRVAL FROM DUAL";
	         }
	         else 
	               throw new java.sql.SQLException("ERROR: Database type: " + db_type + " unrecognized");
	        ResultSet rs = queryResults(sql);
	        int id = 0;
	        if(rs.next()){
	            id = rs.getInt(1);
	         }
	         conn.commit();
	         rs.close();
	         return id;
    	}
    	catch(SQLException ex) 
    	{
    		conn.rollback();
    		throw ex;
    	}
    	finally
    	{
    		conn.setAutoCommit(ac);
    	}
    }

    synchronized public int queryExecuteUpdate(String preparedSQL, String[] param) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
            preparedStmt.setString((i + 1), param[i]);

        }
        return(preparedStmt.executeUpdate());
    }

    synchronized public int queryExecuteUpdate(String preparedSQL, int[] param) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
            preparedStmt.setInt((i + 1), param[i]);
        }
        return(preparedStmt.executeUpdate());
    }
    synchronized public int queryExecuteUpdate(String preparedSQL, DBPreparedHandlerParam[] params) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < params.length; i++) {
        	DBPreparedHandlerParam param = params[i];
        	
        	if (param==null) preparedStmt.setObject(i+1, null);
        	else if(DBPreparedHandlerParam.PARAM_STRING.equals(param.getParamType())){
                    preparedStmt.setString(i+1, param.getStringValue());
        	}else if (DBPreparedHandlerParam.PARAM_DATE.equals(param.getParamType())){
                    preparedStmt.setDate(i+1, param.getDateValue());
        	}else if (DBPreparedHandlerParam.PARAM_INT.equals(param.getParamType())){
                    preparedStmt.setInt(i+1,param.getIntValue());
        	}else if (DBPreparedHandlerParam.PARAM_TIMESTAMP.equals(param.getParamType())){
                    preparedStmt.setTimestamp(i+1,param.getTimestampValue());
                }
        }
        return(preparedStmt.executeUpdate());
    }

    synchronized public int queryExecuteUpdate(String preparedSQL, String[] param, Date[] dtparam,int[] intparam) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        int idx = 1;
        for (int i = 0; i < param.length; i++) {
            preparedStmt.setString((idx++), param[i]);
        }
        for (int i = 0; i < dtparam.length; i++) {
            preparedStmt.setDate((idx++), dtparam[i]);
        }
        for (int i = 0; i < intparam.length; i++) {
            preparedStmt.setInt((idx++), intparam[i]);
        }
        return(preparedStmt.executeUpdate());
    }

    synchronized public int queryExecuteUpdate(String preparedSQL, String[] param, int[] intparam) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
            preparedStmt.setString((i + 1), param[i]);
        }
        for (int i = 0; i < intparam.length; i++) {
            preparedStmt.setInt((param.length + i + 1), intparam[i]);
        }
        return(preparedStmt.executeUpdate());
    }

    synchronized public int queryExecuteUpdate(String preparedSQL, int[] intparam, String[] param) throws SQLException {
        int i = 0;
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        for (i = 0; i < intparam.length; i++) {
            preparedStmt.setInt((i + 1), intparam[i]);
        }
        for (i = 0; i < param.length; i++) {
            preparedStmt.setString((intparam.length + i + 1), param[i]);
        }
        return(preparedStmt.executeUpdate());
    }

    synchronized public ResultSet queryResults(String preparedSQL, String[] param, int[] intparam) throws SQLException {
        int i = 0;
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        for (i = 0; i < param.length; i++) {
            preparedStmt.setString((i + 1), param[i]);
        }
        for (i = 0; i < intparam.length; i++) {
            preparedStmt.setInt((param.length + i + 1), intparam[i]);
        }
        rs = preparedStmt.executeQuery();
        return rs;
    }

    synchronized public ResultSet queryResults(String preparedSQL, int param) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        preparedStmt.setInt(1, param);
        rs = preparedStmt.executeQuery();
        return rs;
    }

    synchronized public ResultSet queryResults(String preparedSQL, int[] param) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
            preparedStmt.setInt((i + 1), param[i]);
        }
        return(preparedStmt.executeQuery());
    }

    synchronized public ResultSet queryResults(String preparedSQL, String param) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        preparedStmt.setString(1, param);
        rs = preparedStmt.executeQuery();
        return rs;
    }

    synchronized public ResultSet queryResults_paged(String preparedSQL, String param, int iOffSet) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        preparedStmt.setString(1, param);
        rs = preparedStmt.executeQuery();
        for(int i=1; i<=iOffSet; i++){
          if(rs.next()==false) break;
        }
        return rs;
    }

    synchronized public ResultSet queryResults(String preparedSQL, String[] param) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
            preparedStmt.setString((i + 1), param[i]);
        }
        rs = preparedStmt.executeQuery();
        return(rs);
    }

    synchronized public ResultSet queryResults_paged(String preparedSQL, String[] param, int iOffSet) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
           preparedStmt.setString((i + 1), param[i]);
        }
        rs = preparedStmt.executeQuery();
        for(int i=1; i<=iOffSet; i++){
            if(rs.next()==false) break;
        }
        return(rs);
    }
    synchronized public ResultSet queryResults(String preparedSQL, DBPreparedHandlerParam[] param) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
           if(param[i].getParamType().equals(DBPreparedHandlerParam.PARAM_STRING)){
               preparedStmt.setString((i + 1), param[i].getStringValue());
           }
           else if(param[i].getParamType().equals(DBPreparedHandlerParam.PARAM_DATE)){
               preparedStmt.setDate((i + 1), param[i].getDateValue());
           }
           else if(param[i].getParamType().equals(DBPreparedHandlerParam.PARAM_INT)){
               preparedStmt.setInt((i + 1), param[i].getIntValue());
           }
        }
        rs = preparedStmt.executeQuery();
        return(rs);
    }
    
    synchronized public ResultSet queryResults_paged(String preparedSQL, DBPreparedHandlerParam[] param, int iOffSet) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
           if(param[i].getParamType().equals(DBPreparedHandlerParam.PARAM_STRING)){
               preparedStmt.setString((i + 1), param[i].getStringValue());
           }
           else if(param[i].getParamType().equals(DBPreparedHandlerParam.PARAM_DATE)){
               preparedStmt.setDate((i + 1), param[i].getDateValue());
           }
           else if(param[i].getParamType().equals(DBPreparedHandlerParam.PARAM_INT)){
               preparedStmt.setInt((i + 1), param[i].getIntValue());
           }
        }
        rs = preparedStmt.executeQuery();
        for(int i=1; i<=iOffSet; i++){
            if(rs.next()==false) break;
        }
        return(rs);
    }

    synchronized public Object[] queryResultsCaisi(String preparedSQL, int param) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        preparedStmt.setInt(1, param);
        rs = preparedStmt.executeQuery();
        return new Object[] {rs, preparedStmt};
    }

    synchronized public Object[] queryResultsCaisi(String preparedSQL, String param) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        preparedStmt.setString(1, param);
        rs = preparedStmt.executeQuery();
        return new Object[] {rs, preparedStmt};
    }

    synchronized public Object[] queryResultsCaisi(String preparedSQL, String[] param) throws SQLException {
        preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
            preparedStmt.setString((i + 1), param[i]);
        }
        rs = preparedStmt.executeQuery();
        return new Object[] {rs, preparedStmt};
    }

    synchronized public Object[] queryResultsCaisi(String preparedSQL) throws SQLException {
        stmt = DbConnectionFilter.getThreadLocalDbConnection().createStatement();
        rs = stmt.executeQuery(preparedSQL);
        return new Object[] {rs, stmt};
    }

    synchronized public ResultSet queryResults(String preparedSQL) throws SQLException {
        stmt = DbConnectionFilter.getThreadLocalDbConnection().createStatement();
        rs = stmt.executeQuery(preparedSQL);
        return rs;
    }

    synchronized public ResultSet queryResults_paged(String preparedSQL, int iOffSet) throws SQLException {
        stmt = DbConnectionFilter.getThreadLocalDbConnection().createStatement();
        rs = stmt.executeQuery(preparedSQL);
        for(int i=1; i<=iOffSet; i++){
            if(rs.next()==false) break;
        }
        return rs;
    }

    public synchronized String getNewProviderNo()
    {
    	try {
	    	String pno = oscar.Misc.getRandomNumber(6);
	    	String sql = "select count(*) from provider where provider_no= '" + pno + "'";
	    	ResultSet rs = queryResults(sql);
	    	while (rs.next()) {
	    		if (rs.getInt(1)> 0) {
	    			do {
	    				pno = oscar.Misc.getRandomNumber(6);
	    			}while(pno != null && pno.startsWith("0"));
	    			sql = "select count(*) from prvider where provider_no= '" + pno + "'";
	    			rs= queryResults(sql);
	    		}
	    	}
	    	return pno;
    	}
    	catch(Exception ex)
    	{
    		return "";
    	}
    }

}
