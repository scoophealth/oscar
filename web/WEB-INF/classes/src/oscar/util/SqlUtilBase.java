/*
 * SqlUtilBase.java
 *
 * Created on July 11, 2006, 1:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.util;

import oscar.oscarDB.DBHandler;
import java.util.*;
import java.sql.*;

public class SqlUtilBase {
           //------------------private
   protected static void runSQL(String sql) {
       try {
           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
           db.RunSQL(sql);
           db.CloseConn();
       } catch (SQLException sqe) {
           sqe.printStackTrace();
       }
   }
   
   protected static String runSQLinsert(String sql) {
       try {
           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
           db.RunSQL(sql);
           sql = "SELECT LAST_INSERT_ID()";
           ResultSet rs = db.GetSQL(sql);
           rs.next();
           String lastID = rs.getString("LAST_INSERT_ID()");
           rs.close();
           return(lastID);
       } catch (SQLException sqe) { sqe.printStackTrace(); }
       return "";
   }
   
   protected static ResultSet getSQL(String sql) {
       ResultSet rs = null;
       try {
           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
           rs = db.GetSQL(sql);
           db.CloseConn();
       } catch (SQLException sqe) {
           sqe.printStackTrace();
       }
       return(rs);
   }
   
   protected static String rsGetString(ResultSet rs, String column) throws SQLException {
       //protects agianst null values;
       String thisStr = rs.getString(column);
       if (thisStr == null) return "";
       return thisStr;
   }
}
