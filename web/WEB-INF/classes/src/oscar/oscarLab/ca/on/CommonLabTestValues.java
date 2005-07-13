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
import oscar.oscarDB.*;
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
                " and ltr.labPatientPhysicianInfo_id = lpp.id ";
         
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
               labList.add(h);
            }
            rs.close();
            db.CloseConn();        
         }catch(Exception e){
            System.out.println("exception in CommonLabTestValues.findValuesForTest():"+e);         
         }
      } else if ( labType != null && labType.equals("MDS")){
         //String sql = "select *   from mdsOBX x, mdsMSH m, patientLabRouting p where observationIden like '%^"+testName+"%' and  x.segmentID = m.segmentID and p.demographic_no = '"+demographicNo+"' and m.segmentID = p.lab_no";
         String sql = "select *   from mdsOBX x, mdsMSH m, patientLabRouting p where observationIden like '%^"+testName+"%' and  x.segmentID = m.segmentID and p.demographic_no = '"+demographicNo+"' and m.segmentID = p.lab_no";       
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
               String collDate = "";
               
               String sql2 = "select * from mdsZMN where segmentID = '"+segId+"' and reportName = '"+testNam+"'";
               ResultSet rs2 = db.GetSQL(sql2);                     
               
               if(rs2.next()){               
                  range = rs2.getString("referenceRange");  // mdsZMN referenceRange
                  units = rs2.getString("units"); //mdsZMN units
                  collDate = rs2.getString("dateTime"); //mdsOBX dateTime
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
