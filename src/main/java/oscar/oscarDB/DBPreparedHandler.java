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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.oscarehr.util.DbConnectionFilter;

/**
 * deprecated Use JPA instead, no new code should be written against this class.
 */
@Deprecated
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
