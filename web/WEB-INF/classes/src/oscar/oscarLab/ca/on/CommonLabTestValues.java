/**
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
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
 *  Ontario, Canada   Creates a new instance of CommonLabTestValues
 *
 *
 * CommonLabTestValues.java
 *
 * Created on May 4, 2005, 3:27 PM
 */

package oscar.oscarLab.ca.on;

import java.sql.*;
import java.util.*;
import oscar.OscarProperties;
import oscar.oscarDB.*;
import oscar.oscarLab.ca.bc.PathNet.PathnetResultsData;
import oscar.util.*;

/**
 *
 * @author Jay Gallagher
 */
public class CommonLabTestValues {
   
   
   public CommonLabTestValues() {
   }
   
   public  String getReferenceRange(String minimum,String maximum){
      String retval ="";
      if (minimum != null && maximum != null){
         if (!minimum.equals("") && !maximum.equals("")){
            if (minimum.equals(maximum)){
               retval = minimum;
            }else{
               retval = minimum + " - " + maximum;
            }
         }
      }
      return retval;
   }
   
   
   
   
   public ArrayList findUniqueLabsForPatient(String demographic){
      OscarProperties op = OscarProperties.getInstance();
      String cml = op.getProperty("CML_LABS");
      String mds = op.getProperty("MDS_LABS");
      String pathnet = op.getProperty("PATHNET_LABS");
      ArrayList labs = new ArrayList();
      if( cml != null && cml.trim().equals("yes")){
         ArrayList cmlLabs = findUniqueLabsForPatientCML(demographic);
         labs.addAll(cmlLabs);
      }
      if (mds != null && mds.trim().equals("yes")){
         ArrayList mdsLabs = findUniqueLabsForPatientMDS(demographic);
         labs.addAll(mdsLabs);            
      }
      if (pathnet != null && pathnet.trim().equals("yes")){
         ArrayList pathLabs = findUniqueLabsForPatientExcelleris(demographic);
         labs.addAll(pathLabs);
      }
      return labs;
   }
   
   //Method returns unique test names for a patient
   //List is used to compile a cummalitive lab profile
   //Hashtable return in list
   //"testName" : Name of test eg. CHOL/HDL RATIO, CHOLESTEROL, CREATININE      
   //"labType" : Vendor of lab eg. MDS, CML, BCP(Excelleris)
   //"title" : Heading of lab group eg. CHEMISTRY, HEMATOLOGY
   public ArrayList findUniqueLabsForPatientCML(String demographic){
      //Need to check which labs are active
         ArrayList labList = new ArrayList();   
         String sql = "select  distinct p.lab_type, ltr.title, ltr.test_name "+
                      "from "+ 
                      "patientLabRouting p , "+
                      "labTestResults ltr, "+
                      "labPatientPhysicianInfo lpp "+            
                      "where p.lab_type = 'CML' "+ 
                      "and p.demographic_no = '"+demographic+"' "+ 
                      "and p.lab_no = ltr.labPatientPhysicianInfo_id "+
                      "and ltr.labPatientPhysicianInfo_id = lpp.id and  ltr.test_name is not null  and ltr.test_name != '' "+
                      "order by title,test_name ";
        
         System.out.println(sql);
      
         try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                  
            ResultSet rs = db.GetSQL(sql);      
            while(rs.next()){
               String testNam = rs.getString("test_name");
               String labType = rs.getString("lab_type"); 
               String title = rs.getString("title");
               Hashtable h = new Hashtable();
               h.put("testName", testNam);
               h.put("labType",labType);
               h.put("title",title);
               labList.add(h);
            }
            rs.close();
            db.CloseConn();        
         }catch(Exception e){
            System.out.println("exception in CommonLabTestValues.findValuesForTest():"+e);         
         } 
       
       return labList;
   }
   
   //Method returns unique test names for a patient
   //List is used to compile a cummalitive lab profile
   //Hashtable return in list
   //"testName" : Name of test eg. CHOL/HDL RATIO, CHOLESTEROL, CREATININE      
   //"labType" : Vendor of lab eg. MDS, CML, BCP(Excelleris)
   //"title" : Heading of lab group eg. CHEMISTRY, HEMATOLOGY
   public ArrayList findUniqueLabsForPatientMDS(String demographic){
      //Need to check which labs are active
         ArrayList labList = new ArrayList();   
         String sql = "select p.lab_type, x.observationIden " +
                      "from mdsOBX x, mdsMSH m, patientLabRouting p " +
                      " where p.demographic_no = '"+demographic+"' " +
                      "and m.segmentID = p.lab_no " +
                      "and x.segmentID = m.segmentID  "; 
                      
         System.out.println(sql);
      
         try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                  
            ResultSet rs = db.GetSQL(sql);      
            while(rs.next()){
               String testNam = "Unknown";rs.getString("observationIden").substring(1,rs.getString("observationIden").indexOf('^'));
               String labType = rs.getString("lab_type"); 
               String title = "";//TODO:rs.getString("title");
               
               try{   
                 String obserIden = rs.getString("observationIden");//.substring(rs.getString("observationIden").indexOf('^'),rs.getString("observationIden").indexOf('^',rs.getString("observationIden").indexOf('^')));  //reportname or observationIden
                 int first = rs.getString("observationIden").indexOf('^');
                 int second = rs.getString("observationIden").indexOf('^',first+1);
                 testNam = rs.getString("observationIden").substring(first+1,second);
               }catch(Exception e){
                   e.printStackTrace();
               }
               
               
               Hashtable h = new Hashtable();
               h.put("testName", testNam);
               h.put("labType",labType);
               h.put("title",title);
               labList.add(h);
            }
            rs.close();
            db.CloseConn();        
         }catch(Exception e){
            System.out.println("exception in CommonLabTestValues.findValuesForTest():"+e); 
            e.printStackTrace();
         } 
       
       return labList;
   }
   public ArrayList findUniqueLabsForPatientExcelleris(String demographic){
       ArrayList labList = new ArrayList();   
         String sql = "select distinct p.lab_type,x.observation_identifier "+
                      "from patientLabRouting p, hl7_msh m ,hl7_pid pi, hl7_obr r,hl7_obx x  " +
                      "where p.demographic_no = '"+demographic+"' " +
                      "and p.lab_no = m.message_id " +
                      "and pi.message_id = m.message_id " +
                      "and r.pid_id = pi.pid_id " +
                      "and r.obr_id = x.obr_id";
         
         System.out.println(sql);
      
         try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                  
            ResultSet rs = db.GetSQL(sql);      
            while(rs.next()){
               String testNam = rs.getString("observation_identifier").substring(1+rs.getString("observation_identifier").indexOf('^'));
               String labType = rs.getString("lab_type"); 
               String title = "";//TODO:rs.getString("title");
               Hashtable h = new Hashtable();
               h.put("testName", testNam);
               h.put("labType",labType);
               h.put("title",title);
               labList.add(h);
            }
            rs.close();
            db.CloseConn();        
         }catch(Exception e){
            System.out.println("exception in CommonLabTestValues.findValuesForTest():"+e);  
            e.printStackTrace();
         } 
       
       return labList;
   }
   
   /**Returns hashtable with the following characteristics
   //first field is testName, 
   //second field is abn : abnormal or normal, A or N
   //third field is result
   //fourth field is unit
   //fifth field is range
   //sixth field is date : collection Date
   */
   public ArrayList findValuesForTest(String labType, String demographicNo, String testName){
      ArrayList labList = new ArrayList();
      if ( labType != null && labType.equals("CML")){
         String sql = "select p.lab_no , p.lab_type, ltr.title, ltr.test_name, ltr.result,ltr.abn, ltr.minimum, ltr.maximum, ltr.units, lpp.collection_date " +
                "from patientLabRouting p , labTestResults ltr, labPatientPhysicianInfo lpp " +
                " where p.lab_type = 'CML' " +  
                " and p.demographic_no = '"+demographicNo+"' " +
                " and p.lab_no = ltr.labPatientPhysicianInfo_id " +
                " and ltr.test_name = '"+testName+"' " +
                " and ltr.labPatientPhysicianInfo_id = lpp.id order by lpp.collection_date";
         
         System.out.println(sql);
      
         try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                  
            ResultSet rs = db.GetSQL(sql);      
            while(rs.next()){
               String testNam = rs.getString("test_name");
               String abn = rs.getString("abn"); 
               String result = rs.getString("result");
               String range = getReferenceRange(rs.getString("minimum"),rs.getString("maximum"));
               String units = rs.getString("units");
               String collDate = rs.getString("collection_date");
               Hashtable h = new Hashtable();
               h.put("testName", testNam);
               h.put("abn",abn);
               h.put("result",result);
               h.put("range",range);
               h.put("units",units);
               h.put("collDate",collDate);
               h.put("collDateDate",UtilDateUtilities.getDateFromString(collDate, "dd-MMM-yy"));
               labList.add(h);
            }
            rs.close();
            db.CloseConn();        
         }catch(Exception e){
            System.out.println("exception in CommonLabTestValues.findValuesForTest():"+e); 
            e.printStackTrace();
         }
      } else if ( labType != null && labType.equals("MDS")){
         //String sql = "select *   from mdsOBX x, mdsMSH m, patientLabRouting p where observationIden like '%^"+testName+"%' and  x.segmentID = m.segmentID and p.demographic_no = '"+demographicNo+"' and m.segmentID = p.lab_no";
         String sql = "select *   from mdsOBX x, mdsMSH m, patientLabRouting p where observationIden like '%^"+testName+"%' and  x.segmentID = m.segmentID and p.demographic_no = '"+demographicNo+"' and m.segmentID = p.lab_no order by dateTime";       
         System.out.println(sql);
      
         try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                  
            ResultSet rs = db.GetSQL(sql);      
            while(rs.next()){
               
               String testNam = rs.getString("observationIden").substring(1,rs.getString("observationIden").indexOf('^'));  //reportname or observationIden
               System.out.println("testNam "+testNam);
               String abn = rs.getString("abnormalFlags");            //abnormalFlags from mdsOBX
               String result = rs.getString("observationValue");     //mdsOBX observationValue
               String segId = rs.getString("segmentID");
               String range = "";
               String units = "";
               String collDate = rs.getString("dateTime"); //mdsOBX dateTime
               
               String sql2 = "select * from mdsZMN where segmentID = '"+segId+"' and reportName = '"+testNam+"'";
               ResultSet rs2 = db.GetSQL(sql2);                     
               
               if(rs2.next()){               
                  range = rs2.getString("referenceRange");  // mdsZMN referenceRange
                  units = rs2.getString("units"); //mdsZMN units
               }
               rs2.close();
               Hashtable h = new Hashtable();
               h.put("testName", testNam);
               h.put("abn",abn);
               h.put("result",result);
               h.put("range",range);
               h.put("units",units);
               h.put("collDate",collDate);
               h.put("collDateDate",UtilDateUtilities.getDateFromString(collDate, "yyyy-MM-dd HH:mm:ss"));
               labList.add(h);
            }
            rs.close();
            db.CloseConn();        
         }catch(Exception e){
            System.out.println("exception in CommonLabTestValues.findValuesForTest():"+e); 
            e.printStackTrace();
         }
      }else if ( labType != null && labType.equals("BCP")){
          String sql = "select * from patientLabRouting p, hl7_msh m ,hl7_pid pi, hl7_obr r,hl7_obx x  where p.demographic_no = '"+demographicNo+"' and x.observation_identifier like '%^"+testName+"' and p.lab_no = m.message_id and pi.message_id = m.message_id and r.pid_id = pi.pid_id and r.obr_id = x.obr_id order by r.observation_date_time";
          System.out.println(sql);
          try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                  
            ResultSet rs = db.GetSQL(sql);      
            while(rs.next()){
                // |   |  | 
               String testNam = rs.getString("observation_identifier").substring(rs.getString("observation_identifier").indexOf('^')+1);
               System.out.println("testNam "+testNam);
               String abn = rs.getString("abnormal_flags");            //abnormalFlags from mdsOBX
               String result = rs.getString("observation_results");     //mdsOBX observationValue
               String segId = rs.getString("lab_no");
               String range = rs.getString("reference_range");
               String units = rs.getString("units");
               String collDate = rs.getString("observation_date_time");
                
               Hashtable h = new Hashtable();
               h.put("testName", testNam);
               h.put("abn",abn);
               h.put("result",result);
               h.put("range",range);
               h.put("units",units);
               h.put("collDate",collDate);
               h.put("collDateDate",UtilDateUtilities.getDateFromString(collDate, "yyyy-MM-dd HH:mm:ss"));
               labList.add(h);
            }
            rs.close();
            db.CloseConn();        
         }catch(Exception e){
            System.out.println("exception in CommonLabTestValues.findValuesForTest():"+e);       
            e.printStackTrace();
         }     
          
      }
   return labList;
   }
   
   
   
   
   
   /**Returns hashtable with the following characteristics
   //first field is testName, 
   //second field is abn : abnormal or normal, A or N
   //third field is result
   //fourth field is unit
   //fifth field is range
   //sixth field is date : collection Date
   */
   public ArrayList findValuesForDemographic(String demographicNo){
      ArrayList labList = new ArrayList();
      
         String sql = "select p.lab_no , p.lab_type, ltr.title, ltr.test_name, ltr.result,ltr.abn, ltr.minimum, ltr.maximum, ltr.units, lpp.collection_date " +
                "from patientLabRouting p , labTestResults ltr, labPatientPhysicianInfo lpp " +
                " where p.lab_type = 'CML' " +  
                " and p.demographic_no = '"+demographicNo+"' " +
                " and p.lab_no = ltr.labPatientPhysicianInfo_id " +                
                " and ltr.labPatientPhysicianInfo_id = lpp.id and test_name != \"\" ";
         
         System.out.println(sql);     
         try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                  
            ResultSet rs = db.GetSQL(sql);      
            while(rs.next()){
               String testNam = rs.getString("test_name");
               String abn = rs.getString("abn"); 
               String result = rs.getString("result");
               String range = getReferenceRange(rs.getString("minimum"),rs.getString("maximum"));
               String units = rs.getString("units");
               String collDate = UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(rs.getString("collection_date"),"dd-MMM-yy"),"yyyy-MM-dd");
               System.out.println("This went in "+rs.getString("collection_date")+" this came out "+UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(rs.getString("collection_date"),"dd-MMM-yy"),"yyyy-MM-dd"));
               //rs.getString("collection_date");
               Hashtable h = new Hashtable();                                                            
               h.put("testName", testNam);
               h.put("abn",abn);
               h.put("result",result);
               h.put("range",range);
               h.put("units",units);
               h.put("collDate",collDate);
               labList.add(h);
            }
            rs.close();
            db.CloseConn();        
         }catch(Exception e){
            e.printStackTrace();
            System.out.println("exception in CommonLabTestValues.findValuesForTest():"+e);         
         }
         
      
         sql = null;
         sql = "select *   from mdsOBX x, mdsMSH m, patientLabRouting p where x.segmentID = m.segmentID and p.demographic_no = '"+demographicNo+"' and m.segmentID = p.lab_no";       
         System.out.println(sql);
      
         try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                  
            ResultSet rs = db.GetSQL(sql);      
            while(rs.next()){
               
               //String testNam = rs.getString("observationIden").substring(1,rs.getString("observationIden").indexOf('^'));  //reportname or observationIden
                
               String obserIden = rs.getString("observationIden").substring(1,rs.getString("observationIden").indexOf('^'));  //reportname or observationIden
               int first = rs.getString("observationIden").indexOf('^');
               int second = rs.getString("observationIden").substring(first+1).indexOf('^');
               System.out.println("first "+first+" second "+second);
               String testNam = rs.getString("observationIden").substring(first+1,second+first+1);
               
               
               System.out.println("testNam "+testNam);
               String abn = rs.getString("abnormalFlags");            //abnormalFlags from mdsOBX
               String result = rs.getString("observationValue");     //mdsOBX observationValue
               String segId = rs.getString("segmentID");
               String range = "";
               String units = "";               
               //String collDate = rs.getString("dateTime");
               
               String collDate = UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(rs.getString("dateTime"),"yyyy-MM-dd hh:mm:ss"),"yyyy-MM-dd");
               
               //<LabResults testDate="2004-11-17 16:26:18
               
               String sql2 = "select * from mdsZMN where segmentID = '"+segId+"' and resultMnemonic = '"+obserIden+"'";
               
               System.out.println(sql2);
               ResultSet rs2 = db.GetSQL(sql2);                     
               
               if(rs2.next()){               
                  range = rs2.getString("referenceRange");  // mdsZMN referenceRange
                  units = rs2.getString("units"); //mdsZMN units
                  //collDate = rs2.getString("dateTime"); //mdsOBX dateTime
               }
               rs2.close();
               Hashtable h = new Hashtable();
               h.put("testName", testNam);
               h.put("abn",abn);
               h.put("result",result);
               h.put("range",range);
               h.put("units",units);
               h.put("collDate",collDate);
               labList.add(h);
            }
            rs.close();
            db.CloseConn();        
         }catch(Exception e){
            System.out.println("exception in CommonLabTestValues.findValuesForTest():"+e);         
         }
      
   return labList;
   }
   
   
}
