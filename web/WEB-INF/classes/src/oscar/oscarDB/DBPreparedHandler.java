// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import org.oscarehr.util.DbConnectionFilter;

public class DBPreparedHandler {

    DBHandler db = null;
    ResultSet rs = null;
    Statement stmt = null;
    PreparedStatement preparedStmt = null;

    public DBPreparedHandler() {
    }
    
    public DBPreparedHandler(String dbDriver, String dbName, String dbUser, String dbPwd) throws SQLException {
    }

    public static Connection getConnection() throws SQLException {
        return DbConnectionFilter.getThreadLocalDbConnection();
    }

    public void init(String dbDriver, String dbUrl, String dbUser, String dbPwd) throws Exception, SQLException {
    }

    synchronized public void queryExecute(String preparedSQL, String[] param) throws SQLException {
        preparedStmt = getConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
            preparedStmt.setString((i + 1), param[i]);
            //System.out.println(param[i]);
        }
        preparedStmt.execute();
    }

    synchronized public int queryExecuteUpdate(String preparedSQL, String[] param) throws SQLException {
        preparedStmt = getConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
            preparedStmt.setString((i + 1), param[i]);
            //System.out.println(param[i]);
        }
        return(preparedStmt.executeUpdate());
    }

    synchronized public int queryExecuteUpdate(String preparedSQL, int[] param) throws SQLException {
        preparedStmt = getConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
            preparedStmt.setInt((i + 1), param[i]);
        }
        return(preparedStmt.executeUpdate());
    }
    synchronized public int queryExecuteUpdate(String preparedSQL, String[] param, Date[] dtparam,int[] intparam) throws SQLException {
        preparedStmt = getConnection().prepareStatement(preparedSQL);
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
        preparedStmt = getConnection().prepareStatement(preparedSQL);
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
        preparedStmt = getConnection().prepareStatement(preparedSQL);
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
        preparedStmt = getConnection().prepareStatement(preparedSQL);
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
        preparedStmt = getConnection().prepareStatement(preparedSQL);
        preparedStmt.setInt(1, param);
        rs = preparedStmt.executeQuery();
        return rs;
    }

    synchronized public ResultSet queryResults(String preparedSQL, int[] param) throws SQLException {
        preparedStmt = getConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
            preparedStmt.setInt((i + 1), param[i]);
        }
        return(preparedStmt.executeQuery());
    }

    synchronized public ResultSet queryResults(String preparedSQL, String param) throws SQLException {
        preparedStmt = getConnection().prepareStatement(preparedSQL);
        preparedStmt.setString(1, param);
        rs = preparedStmt.executeQuery();
        return rs;
    }

    synchronized public ResultSet queryResults_paged(String preparedSQL, String param, int iOffSet) throws SQLException {
        preparedStmt = getConnection().prepareStatement(preparedSQL);
        preparedStmt.setString(1, param);
        rs = preparedStmt.executeQuery();
        for(int i=1; i<=iOffSet; i++){
          if(rs.next()==false) break;
        }
        return rs;
    }

    synchronized public ResultSet queryResults(String preparedSQL, String[] param) throws SQLException {
        preparedStmt = getConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
            preparedStmt.setString((i + 1), param[i]);
        }
        rs = preparedStmt.executeQuery();
        return(rs);
    }

    synchronized public ResultSet queryResults_paged(String preparedSQL, String[] param, int iOffSet) throws SQLException {
        preparedStmt = getConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
           preparedStmt.setString((i + 1), param[i]);
        }
        rs = preparedStmt.executeQuery();
        for(int i=1; i<=iOffSet; i++){
            if(rs.next()==false) break;
        }
        return(rs);
    }
    
    synchronized public Object[] queryResultsCaisi(String preparedSQL, int param) throws SQLException {
        preparedStmt = getConnection().prepareStatement(preparedSQL);
        preparedStmt.setInt(1, param);
        rs = preparedStmt.executeQuery();
        return new Object[] {rs, preparedStmt};
    }

    synchronized public Object[] queryResultsCaisi(String preparedSQL, String param) throws SQLException {
        preparedStmt = getConnection().prepareStatement(preparedSQL);
        preparedStmt.setString(1, param);
        rs = preparedStmt.executeQuery();
        return new Object[] {rs, preparedStmt};
    }

    synchronized public Object[] queryResultsCaisi(String preparedSQL, String[] param) throws SQLException {
        preparedStmt = getConnection().prepareStatement(preparedSQL);
        for (int i = 0; i < param.length; i++) {
            preparedStmt.setString((i + 1), param[i]);
        }
        rs = preparedStmt.executeQuery();
        return new Object[] {rs, preparedStmt};
    }

    synchronized public Object[] queryResultsCaisi(String preparedSQL) throws SQLException {
        stmt = getConnection().createStatement();
        rs = stmt.executeQuery(preparedSQL);
        return new Object[] {rs, stmt};
    }

    synchronized public ResultSet queryResults(String preparedSQL) throws SQLException {
        stmt = getConnection().createStatement();
        rs = stmt.executeQuery(preparedSQL);
        return rs;
    }

    synchronized public ResultSet queryResults_paged(String preparedSQL, int iOffSet) throws SQLException {
        stmt = getConnection().createStatement();
        rs = stmt.executeQuery(preparedSQL);
        for(int i=1; i<=iOffSet; i++){
            if(rs.next()==false) break;
        }
        return rs;
    }

    // Don't forget to clean up!
    public void closePstmt() throws SQLException {
        if (stmt != null) {
            stmt.close();
            stmt = null;
        }
        else {
            preparedStmt.close();
            preparedStmt = null;
        }
    }

    public void closeConn() throws SQLException {
    }
    
    public String getString(ResultSet rs, String columnName) throws SQLException
    {
    	return oscar.Misc.getString(rs, columnName);
    }
    public String getString(ResultSet rs, int columnIndex) throws SQLException
    {
    	return oscar.Misc.getString(rs, columnIndex);
    }

}
