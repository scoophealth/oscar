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

import java.io.*;
import java.sql.*;

public class DBHandler {
    public static String IDDF_DATA = "iddf";
    
    public static String  OSCAR_DATA = "oscar";
    private static String  connDriver = "org.gjt.mm.mysql.Driver";
    private static String  connURL = "jdbc:mysql://"; //change to "jdbc:mysql:///"
    private static String  connUser = "root";
    private static String  connPwd = "oscar";
    
    private int     connInitialConnections = 1;
    private int     connMaxConnections = 100;
    private boolean connWaitIfBusy = true;

    private Connection conn;
    private DBConnectionPool pool;

    public static void init(String db_name, String db_driver,String db_uri,String db_username,String db_password ) 
    {
        OSCAR_DATA = db_name ;
        connDriver = db_driver ;
        connURL = db_uri ;
        connUser = db_username ;
        connPwd = db_password ;
    }

    public DBHandler(String dbName) throws SQLException
    {
        //this("localhost:3306/", dbName);
        this("", dbName);
    }
    public DBHandler(String host, String dbName) throws SQLException
    {
        if(dbName.compareTo(IDDF_DATA)==0)
        {
            pool = DBIddfPool.getInstance(connDriver, connURL + host + dbName, connUser,
                    connPwd, connInitialConnections, connMaxConnections, connWaitIfBusy);
            conn = pool.getConnection();
        }
        else
        {
            pool = DBOscarPool.getInstance(connDriver, connURL + host + dbName, connUser,
                    connPwd, connInitialConnections, connMaxConnections, connWaitIfBusy);

            conn = pool.getConnection();
        }
    }
    synchronized public java.sql.ResultSet GetSQL(String SQLStatement) throws SQLException
    {
        return this.GetSQL(SQLStatement, false);
    }

    synchronized public java.sql.ResultSet GetSQL(String SQLStatement, boolean updatable) throws SQLException
    {
        Statement stmt;
        ResultSet rs = null;

        if(updatable)
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        }
        else
        {
            stmt = conn.createStatement();
        }

//        System.err.println(stmt.getResultSetConcurrency());

        rs = stmt.executeQuery(SQLStatement);

//        System.err.println(rs.getConcurrency());

        return rs;
    }

    synchronized public boolean RunSQL(String SQLStatement) throws SQLException
    {
        boolean b = false;

        Statement stmt;
        stmt = conn.createStatement();
        b = stmt.execute(SQLStatement);

        return b;
    }

    public void CloseConn() throws SQLException
    {
        if(!conn.isClosed())
        {
            pool.free(conn);
        }
    }
}
