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
import java.sql.*;  
import java.io.*;

public class DBPreparedHandler  { 
  
  String connDriver=null; //"org.gjt.mm.mysql.Driver";
  String connURL=null;  //"jdbc:mysql://";
  String connUser=null; //"mysql";
  String connPwd=null; //"oscar";
  Connection conn = null;
  
  ResultSet rs = null;
  Statement stmt = null; 
  PreparedStatement preparedStmt = null;

	public DBPreparedHandler(String dbDriver,String dbName, String dbUser,String dbPwd ) throws SQLException{
    init(dbDriver,dbName,dbUser,dbPwd);
  }
  public void init(String dbDriver,String dbUrl, String dbUser,String dbPwd ) throws SQLException{
    connDriver=dbDriver;
    connURL=dbUrl;
    connUser=dbUser;
    connPwd=dbPwd;
    try{
      OpenConn(connDriver, connURL, connUser, connPwd);
    }catch(Exception e){
      e.printStackTrace(); 
    }
  }
  private void OpenConn(String dbDriver, String dbUrl,String dbUser,String dbPwd) throws Exception {
    try {
      Class.forName(dbDriver);
    }catch(java.lang.ClassNotFoundException e) {
      System.err.println(e.getMessage());
    }
    try {
      conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  

  synchronized public void queryExecute(String preparedSQL, String[] param)throws SQLException{
    preparedStmt = conn.prepareStatement(preparedSQL);
    for(int i=0;i<param.length;i++) {
      preparedStmt.setString((i+1), param[i]);
      //System.out.println(param[i]);
    }
    preparedStmt.execute();
  }

  synchronized public int queryExecuteUpdate(String preparedSQL, String[] param)throws SQLException{
    preparedStmt = conn.prepareStatement(preparedSQL);
    for(int i=0;i<param.length;i++) {
      preparedStmt.setString((i+1), param[i]);
      //System.out.println(param[i]);
    }
    return (preparedStmt.executeUpdate());
  }
  synchronized public int queryExecuteUpdate(String preparedSQL, int[] param)throws SQLException{
    preparedStmt = conn.prepareStatement(preparedSQL);
    for(int i=0;i<param.length;i++) {
      preparedStmt.setInt((i+1), param[i]);
    }
    return (preparedStmt.executeUpdate());
  }
  synchronized public int queryExecuteUpdate(String preparedSQL, String[] param, int [] intparam)throws SQLException{
    preparedStmt = conn.prepareStatement(preparedSQL);
    for(int i=0;i<param.length;i++) {
      preparedStmt.setString((i+1), param[i]);
    }
    for(int i=0;i<intparam.length;i++) {
      preparedStmt.setInt((param.length+i+1), intparam[i]);
    }
    return (preparedStmt.executeUpdate());
  }
  synchronized public int queryExecuteUpdate(String preparedSQL, int [] intparam, String[] param)throws SQLException{
    int i=0;
    preparedStmt = conn.prepareStatement(preparedSQL);
    for(i=0;i<intparam.length;i++) {
      preparedStmt.setInt((i+1), intparam[i]);
    }
    for(i=0;i<param.length;i++) {
      preparedStmt.setString((intparam.length+i+1), param[i]);
    }
    return (preparedStmt.executeUpdate());
  }


  synchronized public ResultSet queryResults(String preparedSQL, String[] param, int[] intparam) throws SQLException {
    int i=0;
    preparedStmt = conn.prepareStatement(preparedSQL);
    for(i=0;i<param.length;i++) {
      preparedStmt.setString((i+1), param[i]);
    }
    for(i=0;i<intparam.length;i++) {
      preparedStmt.setInt((param.length+i+1), intparam[i]);
    }
    rs=preparedStmt.executeQuery();
    return rs;
  }
  synchronized public ResultSet queryResults(String preparedSQL, int param) throws SQLException {
    preparedStmt = conn.prepareStatement(preparedSQL);
    preparedStmt.setInt(1, param);
    rs=preparedStmt.executeQuery();
    return rs;
  }
  synchronized public ResultSet queryResults(String preparedSQL, String param) throws SQLException {
    preparedStmt = conn.prepareStatement(preparedSQL);
    preparedStmt.setString(1, param);
    rs=preparedStmt.executeQuery();
    return rs;
  }
  synchronized public ResultSet queryResults(String preparedSQL, String[] param) throws SQLException {
    preparedStmt = conn.prepareStatement(preparedSQL);
    for(int i=0;i<param.length;i++) {
      preparedStmt.setString((i+1), param[i]);
    }
    rs = preparedStmt.executeQuery();
    return (rs);
  }
  synchronized public ResultSet queryResults(String preparedSQL) throws SQLException {
  	stmt = conn.createStatement();
    rs=stmt.executeQuery(preparedSQL);
    return rs;
  }
  

  // Don't forget to clean up!
  public void closePstmt() throws SQLException {
  	if(stmt!=null) {
  		stmt.close();
  		stmt = null;
  	}	else {
  		preparedStmt.close();
      preparedStmt = null;
    }    
  }
  public void closeConn() throws SQLException {
    conn.close();
    conn = null;
  }

}
