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
package bean;
import java.sql.*;

public class DBConnect {
  static String DBDriver = ""; //"org.gjt.mm.mysql.Driver";
  static String ConnStr = "" ; //"jdbc:mysql://localhost/oscar?user=mysql&password=oscar";
  Connection conn = null;
  ResultSet rs = null;

  public static void init(String db_name, String db_driver,String db_uri,String db_username,String db_password ) 
  {
        DBDriver = db_driver ;
        ConnStr = db_uri + db_name + "?user=" + db_username + "&password=" + db_password ;
  }
  public DBConnect() {
      try {
        Class.forName(DBDriver);
      }catch(
        java.lang.ClassNotFoundException e) {
        System.err.println("DBconn (): " + e.getMessage());
      }
  }
  public boolean executeUpdate(String sql) {
    boolean insert = false;
    try {
         conn = DriverManager.getConnection(ConnStr);
         Statement stmt = conn.createStatement();
         int i= stmt.executeUpdate(sql);
         insert=true;
    }
    catch(SQLException ex) {
       System.err.println("aq.executeQuery: " + ex.getMessage());
    }
    return insert;
  }

  public ResultSet executeQuery(String sql) {
    rs = null;
    try {
         conn = DriverManager.getConnection(ConnStr);
         conn.setAutoCommit(true);
         Statement stmt = conn.createStatement();
         rs = stmt.executeQuery(sql);

    }
    catch(SQLException ex) {
       System.err.println("aq.executeQuery: " + ex.getMessage());
    }
    return rs;
  }
}

