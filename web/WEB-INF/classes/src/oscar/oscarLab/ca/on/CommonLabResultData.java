/**
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 * 
 *  Jason Gallagher
 * 
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   Creates a new instance of CommonLabResultData
 *

 *
 * CommonLabResultData.java
 *
 * Created on April 21, 2005, 4:25 PM
 */

package oscar.oscarLab.ca.on;

import java.sql.*;
import java.util.*;
import oscar.*;
import oscar.oscarDB.*;
import oscar.oscarLab.ca.bc.PathNet.*;
import oscar.oscarMDS.data.*;

/**
 *
 * @author Jay Gallagher
 */
public class CommonLabResultData {
   
   
   public CommonLabResultData() {
      
   }
   
   public static String[] getLabTypes(){
      return new String[] {"MDS","CML","BCP"};
   }
   
   public ArrayList populateLabResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status) {      
      ArrayList labs = new ArrayList();
      oscar.oscarMDS.data.MDSResultsData mDSData = new oscar.oscarMDS.data.MDSResultsData();    
      
      OscarProperties op = OscarProperties.getInstance();
      
      String cml = op.getProperty("CML_LABS");
      String mds = op.getProperty("MDS_LABS");
      String pathnet = op.getProperty("PATHNET_LABS");
      
      if( cml != null && cml.trim().equals("yes")){
         ArrayList cmlLabs = mDSData.populateCMLResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);
         labs.addAll(cmlLabs);
      }
      if (mds != null && mds.trim().equals("yes")){
         ArrayList mdsLabs = mDSData.populateMDSResultsData2(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);      
         labs.addAll(mdsLabs);            
      }
      if (pathnet != null && pathnet.trim().equals("yes")){
         PathnetResultsData pathData = new PathnetResultsData();
         ArrayList pathLabs = pathData.populatePathnetResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);            
         labs.addAll(pathLabs);
      }            
      return labs;
   }
   
   public static boolean updateReportStatus(Properties props, int labNo, int providerNo, char status, String comment, String labType) {
      
      try {
         DBPreparedHandler db = new DBPreparedHandler( props.getProperty("db_driver"), props.getProperty("db_uri")+props.getProperty("db_name"), props.getProperty("db_username"), props.getProperty("db_password") );
         // handles the case where this provider/lab combination is not already in providerLabRouting table
         String sql = "select id from providerLabRouting where status != 'A' and lab_type = '"+labType+"' and provider_no = '"+providerNo+"' and lab_no = '"+labNo+"'";
         
         ResultSet rs = db.queryResults(sql);
         
         if(rs.next()){  //
            String id = rs.getString("id");
            sql = "update providerLabRouting set status='"+status+"', comment=? where id = '"+id+"'";            
            db.queryExecute(sql, new String[] { comment });         
         }else{  
            sql = "insert ignore into providerLabRouting (provider_no, lab_no, status, comment,lab_type) values ('"+providerNo+"', '"+labNo+"', '"+status+"', ?,'"+labType+"')";
            db.queryExecute(sql, new String[] { comment });            
         }
         
         if ( providerNo != 0){            
            String recordsToDeleteSql = "select * from providerLabRouting where provider_no='0' and lab_no='"+labNo+"' and lab_type = '"+labType+"'";
            sql = "delete from providerLabRouting where provider_no='0' and lab_no=? and lab_type = '"+labType+"'";
            ArchiveDeletedRecords adr = new ArchiveDeletedRecords();
            adr.recordRowsToBeDeleted(recordsToDeleteSql, ""+providerNo,"providerLabRouting");
            db.queryExecute(sql, new String[] { Integer.toString(labNo) });            
         }
         db.closeConn();
         return true;
      }catch(Exception e){
         System.out.println("exception in MDSResultsData.updateReportStatus():"+e);
         e.printStackTrace();
         return false;
      }
   }
   
   
   
   
   public ArrayList getStatusArray(String labId, String labType){
      
      ArrayList statusArray = new ArrayList();
      
      String sql = "select provider.first_name, provider.last_name, provider.provider_no, providerLabRouting.status, providerLabRouting.comment, providerLabRouting.timestamp from provider, providerLabRouting where provider.provider_no = providerLabRouting.provider_no and providerLabRouting.lab_no='"+labId+"' and providerLabRouting.lab_type = '"+labType+"'";
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs = db.GetSQL(sql);
         System.out.println(sql);
         while(rs.next()){
             //statusArray.add( new ReportStatus(rs.getString("first_name")+" "+rs.getString("last_name"), rs.getString("provider_no"), descriptiveStatus(rs.getString("status")), rs.getString("comment"), rs.getString("timestamp") ) );
             statusArray.add( new ReportStatus(rs.getString("first_name")+" "+rs.getString("last_name"), rs.getString("provider_no"), descriptiveStatus(rs.getString("status")), rs.getString("comment"), rs.getTimestamp("timestamp").getTime() ) );
             
             
         }
         rs.close();
         db.CloseConn();
      }catch(Exception e){
         System.out.println("exception in CommonLabResultData.getStatusArray():"+e);
         e.printStackTrace();
         
      }
      return statusArray;
   }
   
   public String descriptiveStatus (String status) {
      switch (status.charAt(0)) {
          case 'A' : return "Acknowledged";
          case 'U' : return "N/A";
          default  : return "Not Acknowledged";
      }
  }
   
  public static String searchPatient(String labNo,String labType) {
     String retval = "0";
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         
         String sql = "select demographic_no from patientLabRouting where lab_no='"+labNo+"' and lab_type = '"+labType+"'";
         ResultSet rs = db.GetSQL(sql);
         db.CloseConn();
         if(rs.next()){
            retval = rs.getString("demographic_no");
         }
      }catch(Exception e){
         System.out.println("exception in CommonLabResultData.searchPatient():"+e);
         return "0";
      }
     return retval;
   } 
  
  public static boolean updatePatientLabRouting(String labNo, String demographicNo,String labType) {
      boolean result;
      
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         
         // delete old entries
         String sql = "delete from patientLabRouting where lab_no='"+labNo+"' and lab_type = '"+labType+"'";
         result = db.RunSQL(sql);
         
         // add new entries
         sql = "insert into patientLabRouting (lab_no, demographic_no,lab_type) values ('"+labNo+"', '"+demographicNo+"','"+labType+"')";
         result = db.RunSQL(sql);
         db.CloseConn();
         return result;
      }catch(Exception e){
         System.out.println("exception in CommonLabResultData.updateLabRouting():"+e);
         return false;
      }
   }
  
  
  public static boolean updateLabRouting(ArrayList flaggedLabs, String selectedProviders) {
      boolean result;
      
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         
         String[] providersArray = selectedProviders.split(",");
         String insertString = "";
         String deleteString = "";
         for (int i=0; i < flaggedLabs.size(); i++) {
            String[] strarr = (String[]) flaggedLabs.get(i);
            String lab = strarr[0];
            String labType = strarr[1];
            if (i != 0) {
               insertString = insertString + ", ";
               deleteString = deleteString + ", ";
            }
            for (int j=0; j < providersArray.length; j++) {
               if (j != 0) {
                  insertString = insertString + ", ";
               }
               insertString = insertString + "('" + providersArray[j] + "','" + lab+ "','N','"+labType+"')";
            }
            //deleteString = deleteString+"'"+lab+"'";
            // delete old entries
            String sql = "delete from providerLabRouting where provider_no='0' and lab_type= '"+labType+"' and lab_no = '"+lab+"'";
            result = db.RunSQL(sql);
         }
         
         
         
         // add new entries
         String sql = "insert ignore into providerLabRouting (provider_no, lab_no, status,lab_type) values "+insertString;
         result = db.RunSQL(sql);
         db.CloseConn();
         return result;
      }catch(Exception e){
         System.out.println("exception in CommonLabResultData.updateLabRouting():"+e);
         return false;
      }
   }
  
  ////
  public static boolean fileLabs(ArrayList flaggedLabs, String provider) {
      boolean result;    
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        
         for (int i=0; i < flaggedLabs.size(); i++) {
            String[] strarr = (String[]) flaggedLabs.get(i);
            String lab = strarr[0];
            String labType = strarr[1];      
            String sql = "update providerLabRouting set status = 'F' where provider_no='"+provider+"' and lab_type= '"+labType+"' and lab_no = '"+lab+"'";
            System.out.print(sql);
            db.RunSQL(sql);    
         }
         db.CloseConn();
      }catch(Exception e){
         System.out.println("exception in CommonLabResultData.updateLabRouting():"+e);
      }
      return true;
   }
  ////
  
  public String getDemographicNo(String labId,String labType){
      String demoNo = null;
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs = db.GetSQL("select demographic_no from patientLabRouting where lab_no = '"+labId+"' and lab_type = '"+labType+"'");                            
         if (rs.next()){                                    
            String d = rs.getString("demographic_no");
            System.out.println("dd "+d);
            if ( !"0".equals(d)){
               demoNo = d;
            }                        
         }         
         rs.close();
         db.CloseConn();
         
      }catch(Exception e){
         e.printStackTrace();
      }
      return demoNo;
   }
  
  public boolean isLabLinkedWithPatient(String labId,String labType){
      boolean ret = false;
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                 
         String sql = "select demographic_no from patientLabRouting where lab_no = '"+labId+"' and lab_type  = '"+labType+"' ";
         ResultSet rs = db.GetSQL(sql);
         if(rs.next()){
            String demo =  rs.getString("demographic_no");
            if(demo != null && !demo.trim().equals("0") ){
               ret = true;
            }                                                  
         }
         rs.close();
         db.CloseConn();
      }catch(Exception e){
         System.out.println("exception in isLabLinkedWithPatient:"+e);                  
         
      }
      return ret;
   }
   
  
}
