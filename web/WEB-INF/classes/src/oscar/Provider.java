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
package oscar;

import java.sql.*;
import java.io.*;

import org.oscarehr.util.DbConnectionFilter;

public class Provider {
    //String sDBDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
    //String sConnStr = "jdbc:odbc:oscar";

    Connection conn = null;// db is our connection object
    ResultSet result = null;// result will hold the recordset
    Statement stmt = null; // stmt will hold sql jdbc statements

    // default constructor
    public Provider() {
    }

    /*********************************************
    * @dsn String for ODBC Datasource name
    * @uid String for ODBC Datasource user name
    * @pwn String for ODBC Datasource password
    */
    public void OpenConn(String dsn, String uid, String pwd) throws Exception {
            conn = DbConnectionFilter.getThreadLocalDbConnection();
    }

    //} catch(SQLException ex) { 
    //System.err.println("aq.executeQuery: " + //ex.getMessage());
    //}

    /*********************************************
    * @sproc String for sql statement or stored
    *        procedure
    */
    synchronized public ResultSet getResults(String sproc) throws Exception {
        stmt = conn.createStatement();
        result = stmt.executeQuery(sproc);
        return result;
    }

    /*********************************************
    * @sproc String for sql statement or stored
    *        procedure
    */
    synchronized public void execute(String sproc) throws Exception {
        stmt = conn.createStatement();
        stmt.execute(sproc);
    }

    // Don't forget to clean up!
    public void CloseStmt() throws Exception {
        stmt.close();
        stmt = null;
    }

    // Don't forget to clean up!
    public void CloseConn() throws Exception {
        conn.close();
        conn = null;
    }
}
