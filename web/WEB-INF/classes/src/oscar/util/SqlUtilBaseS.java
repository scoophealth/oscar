/*
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
import oscar.oscarDB.DBPreparedHandler;
import oscar.oscarDB.DBPreparedHandlerParam;

public class SqlUtilBaseS {
           //------------------private
   protected static void runSQL(String sql) {
       try {
           
           DBHandler.RunSQL(sql);
       } catch (SQLException sqe) {
           MiscUtils.getLogger().error("Error", sqe);
       }
   }
   
   protected static String runSQLinsert(String sql) {
       try {
           
           DBHandler.RunSQL(sql);
           sql = "SELECT LAST_INSERT_ID()";
           ResultSet rs = DBHandler.GetSQL(sql);
           rs.next();
           String lastID = rs.getString("LAST_INSERT_ID()");
           rs.close();
           return(lastID);
       } catch (SQLException sqe) { MiscUtils.getLogger().error("Error", sqe); }
       return "";
   }
   
   protected static String runPreparedSqlInsert(String preparedSql, DBPreparedHandlerParam[] params) {
	   try {
		   DBPreparedHandler dbPre = new DBPreparedHandler();
		   dbPre.queryExecuteUpdate(preparedSql, params);
		   String lastID;
		   String strDbType = oscar.OscarProperties.getInstance().getProperty("db_type").trim();
		   if("oracle".equalsIgnoreCase(strDbType)){
			   
			   ResultSet rs = DBHandler.GetSQL("select HIBERNATE_SEQUENCE.NEXTVAL as nextval from dual");
			   rs.next();
			   int lastIDInt = rs.getInt("nextval") - 1;
			   lastID = String.valueOf(lastIDInt);			   
			   rs.close();
		   } else {
			   
			   String sql = "SELECT LAST_INSERT_ID()";
			   ResultSet rs = DBHandler.GetSQL(sql);
			   rs.next();
			   lastID = oscar.Misc.getString(rs, "LAST_INSERT_ID()");
			   rs.close();
		   }
           return(lastID);
       } catch (SQLException sqe) { MiscUtils.getLogger().error("Error", sqe); }
       return "";
   }
   
   protected static void runPreparedSql(String preparedSql,DBPreparedHandlerParam[] params) {
	   try {
		   DBPreparedHandler dbPre = new DBPreparedHandler();
		   dbPre.queryExecuteUpdate(preparedSql, params);
	   } catch (SQLException sqe) { 
		   MiscUtils.getLogger().error("Error", sqe); 
	   }       
   }
   
   protected static ResultSet getSQL(String sql) {
       ResultSet rs = null;
       try {
           
           rs = DBHandler.GetSQL(sql);
       } catch (SQLException sqe) {
           MiscUtils.getLogger().error("Error", sqe);
       }
       return(rs);
   }
   
   protected static String rsGetString(ResultSet rs, String column) throws SQLException {
       //protects agianst null values;
       String thisStr = oscar.Misc.getString(rs,column);
       if (thisStr == null) return "";
       return thisStr;
   }
}
