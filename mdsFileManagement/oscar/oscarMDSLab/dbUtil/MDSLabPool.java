/*
 * MDSLabPool.java
 *
 * Created on August 11, 2003, 10:30 PM
 */

package oscar.oscarMDSLab.dbUtil;
import java.sql.*;
import java.util.*;
import oscar.oscarMDSLab.dbUtil.ConnectionPool;


public class MDSLabPool extends ConnectionPool {
   private static MDSLabPool PoolS=null;
   
   private MDSLabPool(String driver, String url,String username, String password,
   int initialConnections,int maxConnections,boolean waitIfBusy) throws java.sql.SQLException {
      super(driver, url, username, password,initialConnections, maxConnections,waitIfBusy);
   }
   
   public static synchronized MDSLabPool getInstance(String driver, String url,
   String username, String password, int initialConnections,
   int maxConnections, boolean waitIfBusy) throws java.sql.SQLException {
      if(PoolS == null){
         PoolS = new MDSLabPool(driver, url, username, password,
         initialConnections, maxConnections, waitIfBusy);
      }
      return(PoolS);
   }
}
