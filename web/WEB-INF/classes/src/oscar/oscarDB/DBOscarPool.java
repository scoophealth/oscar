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
import java.util.*;


public class DBOscarPool extends DBConnectionPool {
    private static DBOscarPool PoolS=null;

    private DBOscarPool(String driver, String url,String username, String password,
        int initialConnections,int maxConnections,boolean waitIfBusy) throws java.sql.SQLException
    {
        super(driver, url, username, password,initialConnections, maxConnections,waitIfBusy);
    }

    public static synchronized DBOscarPool getInstance(String driver, String url,
        String username, String password, int initialConnections,
        int maxConnections, boolean waitIfBusy) throws java.sql.SQLException
    {
        if(PoolS == null){
            PoolS = new DBOscarPool(driver, url, username, password,
            initialConnections, maxConnections, waitIfBusy);
        }
        return(PoolS);
    }
}
