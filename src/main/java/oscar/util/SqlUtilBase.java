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


/* A NON-STATIC VERSION OF SqlUtilBaseS (attempting to convert static to a non-static utility framework)
 * SqlUtilBase.java
 *
 * Created on July 11, 2006, 1:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class SqlUtilBase {
           //------------------private
   protected void runSQL(String sql) {
       try {
           
           DBHandler.RunSQL(sql);
       } catch (SQLException sqe) {
           MiscUtils.getLogger().error("Error", sqe);
       }
   }
   
   protected String runSQLinsert(String sql) {
       try {
           
           DBHandler.RunSQL(sql);
           sql = "SELECT LAST_INSERT_ID()";
           ResultSet rs = DBHandler.GetSQL(sql);
           rs.next();
           String lastID = oscar.Misc.getString(rs, "LAST_INSERT_ID()");
           rs.close();
           return(lastID);
       } catch (SQLException sqe) { MiscUtils.getLogger().error("Error", sqe); }
       return "";
   }
   
   protected ResultSet getSQL(String sql) {
       ResultSet rs = null;
       try {
           
           rs = DBHandler.GetSQL(sql);
       } catch (SQLException sqe) {
           MiscUtils.getLogger().error("Error", sqe);
       }
       return(rs);
   }
   
   protected String rsGetString(ResultSet rs, String column) throws SQLException {
       //protects agianst null values;
       String thisStr = oscar.Misc.getString(rs,column);
       if (thisStr == null) return "";
       return thisStr;
   }
}
