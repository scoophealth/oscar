/*
 * PathNetInfo.java
 *
 * Created on July 28, 2004, 11:36 AM
 */

package oscar.oscarLab.ca.bc.PathNet;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author  root
 */
public class PathNetInfo {
   
   /** Creates a new instance of PathNetInfo */
   public PathNetInfo() {
   }
                
   
   public int getPendingLabCount() throws SQLException{
      int pendingLabs = 0;
      String select_pending = "SELECT count(hl7_pid.pid_id) as `count` " +
                              "FROM hl7_pid left join hl7_link on hl7_pid.pid_id=hl7_link.pid_id " +
                              "WHERE hl7_link.status='P' OR hl7_link.status is null";      
      ResultSet rsLab = DBHandler.GetSQL(select_pending);
      if(rsLab.next()){
         pendingLabs = rsLab.getInt("count");
      }
      rsLab.close();
      return pendingLabs;
   }
    
   public int getNotSignedLabCount(String providerNo) throws SQLException{
      String select_not_signed = "SELECT count(hl7_link.pid_id) as `count` " +
                              "FROM hl7_link, demographic  " +
                              "WHERE demographic.provider_no= '"+providerNo+"' " +
                              "AND demographic.demographic_no=hl7_link.demographic_no " +
                              "AND (hl7_link.status='N' OR hl7_link.status='A')";
      int notSigned = 0;
      ResultSet rsLab = DBHandler.GetSQL(select_not_signed);
      if(rsLab.next()){
         notSigned = rsLab.getInt("count");
      }
      rsLab.close();
      return notSigned;
   }
   
   public String getLabTab(String providerNo){
      String retval = "";
      try{  
         String labText = "Lab" + ((getPendingLabCount()>0)? " *" : "");
         String labTab = "<font size=\"2\" " + ((getNotSignedLabCount(providerNo)>0)? "color='red'" : "") + ">" + labText + "</font>";
         retval = labTab;
      }catch(Exception e){
         MiscUtils.getLogger().error("Error", e);
         retval = "Lab";
      }
      return retval;
   }
}
