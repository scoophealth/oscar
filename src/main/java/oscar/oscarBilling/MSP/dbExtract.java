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


package oscar.oscarBilling.MSP;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

public class dbExtract implements Serializable {

    private Connection con = null;
    private Statement stmt = null;
    private Statement stmt2 = null;
    private Statement stmt3 = null;
    private int numUpdate;
    private Statement prepStmt = null;
    PreparedStatement prep = null;
    ResultSet resultSet = null;
    ResultSet resultSet2 = null;
    ResultSet resultSet3 = null;

    public dbExtract() {
    }

    public void openConnection() {
        try {

            //establish connection with the specified username, password and url
            con = DbConnectionFilter.getThreadLocalDbConnection();
            stmt = con.createStatement();
            stmt2 = con.createStatement();
        }
        catch (SQLException e) {
            MiscUtils.getLogger().debug("Cannot get connection ");
            MiscUtils.getLogger().debug("Exception is: " + e);
        }

    }

    public ResultSet executeQuery(String sql) {
        try {
            String SQLString = sql;
            // Execute sql
            // statement
            resultSet = stmt.executeQuery(SQLString);
            return resultSet;
        }
        catch (SQLException e) {
            MiscUtils.getLogger().debug("Cannot get connection ");
            MiscUtils.getLogger().debug("Exception is: " + e);
            return resultSet;
        }
    }

    public ResultSet executeQuery2(String sql) {
        try {
            String SQLString = sql;
            // Execute sql
            // statement
            resultSet2 = stmt2.executeQuery(SQLString);
            return resultSet2;
        }
        catch (SQLException e) {
            MiscUtils.getLogger().debug("Cannot get connection ");
            MiscUtils.getLogger().debug("Exception is: " + e);
            return resultSet2;
        }
    }

    public ResultSet executeQuery3(String sql) {
        try {
            String SQLString = sql;
            // Execute sql
            // statement
            resultSet3 = stmt3.executeQuery(SQLString);
            return resultSet3;
        }
        catch (SQLException e) {
            MiscUtils.getLogger().debug("Cannot get connection ");
            MiscUtils.getLogger().debug("Exception is: " + e);
            return resultSet3;
        }
    }

    public void closeConnection() {
        try {
            if ((con != null) && (stmt != null)) {
                con.close();
                stmt.close();
            }

        }
        catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }

    } //closeConnection ends
}
