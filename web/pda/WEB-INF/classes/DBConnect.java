package bean;
import java.sql.*;
/*
 * $RCSfile: AbstractApplication.java,v1.0 $
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * Tom Zhu
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class DBConnect {
    private static Logger logger=MiscUtils.getLogger(); 
	
	
        //   String DBDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
 	//   String ConnStr = "jdbc:mysql://localhost/mydb?user=root&password=zt";
          String DBDriver = "org.gjt.mm.mysql.Driver";
// 	  String ConnStr = "jdbc:mysql://localhost/oscar_pda?user=root&password=liyi";
 	  String ConnStr = "jdbc:mysql://localhost/oscar_pcn?user=root&password=liyi";
	  Connection conn = null;
	  ResultSet rs = null;


  public DBConnect() {
      try {
               Class.forName(DBDriver);

      }catch(
        java.lang.ClassNotFoundException e) {
              logger.error("Unexpected error", e);
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
        logger.error("Unexpected error", e);
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
        logger.error("Unexpected error", e);
    }
    return rs;
  }

 
}

