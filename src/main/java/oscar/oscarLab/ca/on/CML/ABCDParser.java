/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of LabUploadAction
 *
 *
 * ABCDParse.java
 *
 * Created on March 24, 2005, 5:48 PM
 */

package oscar.oscarLab.ca.on.CML;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Logger;

import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarLab.ca.all.upload.ProviderLabRouting;

/**
 *
 * @author  root
 */
public class ABCDParser {
   Logger logger = Logger.getLogger("cmlFileManagement.ABCDLogger");
   
   Atype reportFile = null;
   ArrayList atypes = new ArrayList();
   /** Creates a new instance of ABCDParse */
   public ABCDParser() {   
   }
   
   public void save(Connection conn) throws SQLException{
      logger.info("Starting save");
      Hashtable htable = getProviderHash(conn);
      for ( int i = 0 ; i <  atypes.size(); i++){// for each report 
         Atype report = (Atype) atypes.get(i);
         logger.info("Saving Report for "+report.locationId+" printed on "+report.printDate+" "+report.printTime+ " # B "+report.totalBType+" #C "+report.totalCType+" #D "+report.totalDType);
         String id = report.save(conn, report.locationId,report.printDate,report.printTime,report.totalBType,report.totalCType,report.totalDType);//save AType to Database and return ID         
         logger.info("Report save to the database ID ="+id);
         if (null != id){
            for (int j = 0 ; j< report.btypes.size(); j++){
               Btype patient = (Btype) report.btypes.get(j);
               String patientId = patient.save(conn,id);
               
               addToProviderLabTable(patient.docNum,htable,patientId,conn);
               patientRouteReport(patientId,patient.pLastName,patient.pFirstName,patient.pSex,patient.pDOB,patient.pHealthNum,conn) ;
               
               for (int k = 0; k < patient.cdtypes.size(); k++ ){
                  Object labResult = patient.cdtypes.get(k);
                  //la
                  //Ctype labResult = (Ctype) patient.ctypes.get(k);
                  if (labResult instanceof Ctype){
                     ((Ctype) labResult).save(conn,patientId);
                  }else if (labResult instanceof Dtype){
                     ((Dtype) labResult).save(conn,patientId);
                  }
               }                              
            }
         }
         //report.btypes;
      }
   }
   //This method is to enter the lab into the providerLabTable
   private void addToProviderLabTable(String docNum, Hashtable htable, String labId,Connection conn) throws SQLException{
      
      String providerNo = (String) htable.get(docNum);

      if ( providerNo == null || providerNo.equals("null")){                   
         logger.info("Could not find provider "+docNum+" now trying provider "+docNum.substring(0,(docNum.length()-1))+"  for lab: "+labId); 
         providerNo = (String) htable.get(docNum.substring(0,(docNum.length()-1)));                
         
      }
      
      if ( providerNo == null || providerNo.equals("null")){ 
         logger.info("Could not find provider "+docNum.substring(0,(docNum.length()-1))+"either  for lab: "+labId); 
         logger.info("Setting Provider No to 0"); 
         providerNo = "0";
         
      }
     
      /*
      String sql = "insert into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES (?, ?, 'N','CML')";
        PreparedStatement pstmt = conn.prepareStatement(sql);                 
            pstmt.setString(1,providerNo ); // location_id
            pstmt.setString(2,labId );  // printDate                    
            pstmt.executeUpdate();                                      
            pstmt.close();   
      */
      ProviderLabRouting router = new ProviderLabRouting();
      router.route(labId, providerNo, conn, "CML");
   }
   
   /////
   
   
   public void patientRouteReport(String labId, String lastName, String firstName, String sex, String dob, String hin, Connection conn) {                      
         
         String sql;                          
         String demo = "0";
                  //19481015
         String dobYear = "%";
         String dobMonth = "%";
         String dobDay = "%";
         String hinMod = new String(hin);
         
         if (hinMod.length() == 12){
            hinMod = hinMod.substring(0,10);
         }
         
         if (dob != null && dob.length() == 8){
            dobYear = dob.substring(0,4);
            dobMonth = dob.substring(4,6);
            dobDay = dob.substring(6,8); 
         }
         
         int count =0 ;
         try{
             
            if( OscarProperties.getInstance().getBooleanProperty("LAB_NOMATCH_NAMES","yes") ){
            sql = "select demographic_no from demographic where hin='"+hinMod+"' and " +     
		  " year_of_birth like '"+dobYear+"' and " +
                  " month_of_birth like '"+dobMonth+"' and "+
                  " date_of_birth like '"+dobDay+"' and " +
                  " sex like '"+sex+"%' ";      
            }else{
            sql = "select demographic_no from demographic where hin='"+hinMod+"' and " +
                  " last_name like '"+lastName.substring(0,1)+"%' and " +
                  " first_name like '"+firstName.substring(0,1)+"%' and " +
		  " year_of_birth like '"+dobYear+"' and " +
                  " month_of_birth like '"+dobMonth+"' and "+
                  " date_of_birth like '"+dobDay+"' and " +
                  " sex like '"+sex+"%' ";
            }
            
            
            MiscUtils.getLogger().debug(sql);
            PreparedStatement pstmt = conn.prepareStatement(sql);                                                                
            ResultSet rs = pstmt.executeQuery();            
            while(rs.next()){
               count ++;  
               demo = oscar.Misc.getString(rs,"demographic_no");
            }   
            rs.close();
            pstmt.close();
         }catch (SQLException sqlE){
        	 MiscUtils.getLogger().error("Error", sqlE);
         }
         
         try{ 
            if (count != 1){ 
               demo = "0";
               logger.info("Could not find patient for lab: "+labId+ "# of possible matches :"+count);
            }                                   
            sql = "insert into patientLabRouting (demographic_no, lab_no,lab_type) values ('"+demo+"', '"+labId+"','CML')";                                        
            PreparedStatement pstmt = conn.prepareStatement(sql);                                                                
            pstmt.executeUpdate();
            pstmt.close();                                                                                                                                      
         }catch (SQLException sqlE){
            MiscUtils.getLogger().debug("NO MATCHING PATIENT FOR LAB id ="+labId);
            MiscUtils.getLogger().error("Error", sqlE);
         }                                                    
    }    
   /////
   
   
   private Hashtable getProviderHash(Connection conn){
      logger.info("Init - provider Hash table");
      Hashtable htable = new Hashtable();
      try{ 
         String sql = "select provider_no, ohip_no from provider where ohip_no != '' ";
         PreparedStatement pstmt = conn.prepareStatement(sql);                                     
         pstmt.executeQuery();
         ResultSet rs = pstmt.executeQuery();
            
         while(rs.next()){
            String key = oscar.Misc.getString(rs,"ohip_no");
            String value = oscar.Misc.getString(rs,"provider_no");
            logger.info("Possible provider hashtable key "+key+" lab "+value);
            if ( key != null && value != null && !key.equals("null") && !value.equals("null")){
               htable.put(key, value);
               logger.info("Adding  to provider hashtable key "+key+" lab "+value);
            }
         }   
         pstmt.close();
      }catch (SQLException sqlE){
    	  MiscUtils.getLogger().error("Error", sqlE);
      }
      logger.info("provider hash table :"+htable);
      return htable;
   }
   
   public void parse(BufferedReader in) throws Exception{      
      logger.info("Begin parsing");
      String str;
      while ((str = in.readLine()) != null) {
         process(str);
      }
      in.close();    
      MiscUtils.getLogger().debug("sss "+reportFile.btypes.size());
   }
   
   
   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      // TODO code application logic here
   }
   
   public ArrayList d(String s){
      ArrayList arr = new ArrayList();      
      while (s.indexOf('^') != -1){
         int ind = s.indexOf('^');
         arr.add(s.substring(0,ind));
         s = s.substring(ind+1);
         
      }
      return arr;
   }
   
   void process(String str){ 
         if (str != null && !str.trim().equals("") ){
         
             ArrayList a = d(str);

             String lineType = (String) a.get(0);

             if(lineType.equals("A")){ 
                logger.info("Processing new report");
                reportFile = new Atype();
                reportFile.populate(a);
                atypes.add(reportFile);
             }                                                  

             if(reportFile != null){
                reportFile.read(lineType,a);
             }//ELSE need to throw an exception shouldn't happen
         }
   }
   
   ////
   class Atype {// line: Report information
      Btype labreport = null;
      public ArrayList btypes = new ArrayList();
      public void read(String lineType, ArrayList list){
         if(lineType.equals("B")){
            labreport = new Btype();
            labreport.populate(list);
            btypes.add(labreport);
         }else if(lineType.equals("A")){
            return;
         }
         labreport.process(lineType,list);
         
      }
      
      public String save(Connection conn, String locationId,String printDate,String printTime,String totalB,String totalC,String totalD) throws SQLException{
         String insertID = null;
         // Prepare a statement to insert a record
        String sql = "insert into labReportInformation (location_id,print_date,print_time,total_BType,total_CType,total_DType) values (?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);                 
            pstmt.setString(1,locationId ); // location_id
            pstmt.setString(2,printDate );  // printDate
            pstmt.setString(3,printTime );  // printTime
            pstmt.setString(4,totalB);      // total_BType
            pstmt.setString(5,totalC);      // total_CType 
            pstmt.setString(6,totalD);      // total_DType  
        
            pstmt.executeUpdate();
                          
            ResultSet rs = pstmt.getGeneratedKeys();
            
            if(rs.next()){
               insertID = oscar.Misc.getString(rs,1);       
            }
            pstmt.close();        
         return insertID;
      }
      
      public Atype(){
       
      }
      void populate(ArrayList lst){
         if (lst.size() == 7){ // must equal 7
            this.locationId = (String) lst.get(1);
            this.printDate = (String) lst.get(2);
            this.printTime = (String) lst.get(3);
            this.totalBType = (String) lst.get(4);
            this.totalCType = (String) lst.get(5);
            this.totalDType = (String) lst.get(6);
         }
      }
      
      String locationId = null; //  2. (e.g. 70 = CML Mississauga)
      String printDate  = null; //  3. YYYYMMDD
      String printTime  = null; //  4. HH:MM
      String totalBType = null; //  5. number of B-type lines (= # of reports)
      String totalCType = null; //  6. number of C-type lines
      String totalDType = null; //  7. number of D-type lines
   }
   
   class Btype { //line: Patient and Physician information
      void populate(ArrayList lst){
         if (lst.size() == 22){ // must equal 22
            this.accessionNum = (String) lst.get(1);
            this.physicianAccountNum = (String) lst.get(2);
            this.serviceDate = (String) lst.get(3);
            this.pFirstName = (String) lst.get(4);
            this.pLastName = (String) lst.get(5);
            this.pSex = (String) lst.get(6);
            this.pHealthNum = (String) lst.get(7);
            this.pDOB = (String) lst.get(8);
            this.status = (String) lst.get(9);
            this.docNum = (String) lst.get(10);
            this.docName = (String) lst.get(11);
            this.docAddr1 = (String) lst.get(12);
            this.docAddr2 = (String) lst.get(13);
            this.docAddr3 = (String) lst.get(14);
            this.docPostal = (String) lst.get(15);
            this.docRoute = (String) lst.get(16);
            this.comment1 = (String) lst.get(17);
            this.comment2 = (String) lst.get(18);
            this.pPhone = (String) lst.get(19);
            this.docPhone = (String) lst.get(20);
            this.collectionDate = (String) lst.get(21);
            MiscUtils.getLogger().debug("B "+pFirstName+" "+pLastName+" "+pHealthNum+" "+docRoute+docNum);
         }
      }
      
      public String save(Connection conn, String id) throws SQLException{
         String insertID = null;
         // Prepare a statement to insert a record
                           
        String sql = "insert into labPatientPhysicianInfo (labReportInfo_id,accession_num,physician_account_num,service_date,patient_first_name,patient_last_name,"
        + " patient_sex,patient_health_num,patient_dob,lab_status,doc_num,doc_name,doc_addr1,doc_addr2,doc_addr3,doc_postal,doc_route,comment1,comment2,patient_phone,"
        + "doc_phone,collection_date) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 
        PreparedStatement pstmt = conn.prepareStatement(sql);                 
            pstmt.setString(1,id); // location_id
            pstmt.setString(2,this.accessionNum);  
            pstmt.setString(3,this.physicianAccountNum); 
            pstmt.setString(4,this.serviceDate);  
            pstmt.setString(5,this.pFirstName);   
            pstmt.setString(6,this.pLastName);    
            pstmt.setString(7,this.pSex);    
            pstmt.setString(8,this.pHealthNum);    
            pstmt.setString(9,this.pDOB);    
            pstmt.setString(10,this.status);    
            pstmt.setString(11,this.docNum);    
            pstmt.setString(12,this.docName);    
            pstmt.setString(13,this.docAddr1);    
            pstmt.setString(14,this.docAddr2);    
            pstmt.setString(15,this.docAddr3);    
            pstmt.setString(16,this.docPostal);    
            pstmt.setString(17,this.docRoute);    
            pstmt.setString(18,this.comment1);    
            pstmt.setString(19,this.comment2);    
            pstmt.setString(20,this.pPhone);    
            pstmt.setString(21,this.docPhone);    
            pstmt.setString(22,this.collectionDate);    
                                
            pstmt.executeUpdate();
                          
            ResultSet rs = pstmt.getGeneratedKeys();
            
            if(rs.next()){
               insertID = oscar.Misc.getString(rs,1);       
            }
                     
         return insertID;
      }
      
      
      public void process(String lineType,ArrayList list){
         if(lineType.equals("C")){
            Ctype c = new Ctype();
            c.populate(list);
            ctypes.add(c);
            cdtypes.add(c);
         }else if(lineType.equals("D")){
            Dtype d = new Dtype();
            d.populate(list);
            dtypes.add(d);
            cdtypes.add(d);
         }
      }
      
      ArrayList ctypes= new ArrayList();
      ArrayList dtypes= new ArrayList();
      ArrayList cdtypes = new ArrayList();
      String accessionNum = null;        //  2. CML Accession number (minus first char)
      String physicianAccountNum = null; //  3. Physician Account number
      String serviceDate = null;         //  4. YYYYMMDD
      String pFirstName = null;          //  5. Patient: First name
      String pLastName = null;           //  6. Patient: Last name
      String pSex = null;                //  7. Sex F or M
      String pHealthNum = null;          //  8. Patient: Health number
      String pDOB = null;                //  9. Patient: Birth date
      String status = null;              // 10. Final or Partial F or P
      String docNum = null;              // 11. Physician: Number
      String docName = null;             // 12. Physician: Name
      String docAddr1 = null;            // 13. Physician: Address line 1
      String docAddr2 = null;            // 14. Physician: Address line 2
      String docAddr3 = null;            // 15. Physician: Address line 3
      String docPostal = null;           // 16. Physician: Postal code
      String docRoute = null;            // 17. Physician: Route number
      String comment1 = null;            // 18. Comment 1
      String comment2 = null;            // 19. Comment 2
      String pPhone = null;              // 20. Patient: Phone number
      String docPhone = null;            // 21. Physician: Phone number
      String collectionDate = null;      // 22. Collection date "DD MMM YY"
   }
   
   class Ctype { //line: Test results
      
      void populate(ArrayList lst){
         if (lst.size() == 12){ // must equal 12
            this.title = (String) lst.get(1);
            this.notUsed1 = (String) lst.get(2);
            this.notUsed2 = (String) lst.get(3);
            this.testName = (String) lst.get(4);
            this.abn = (String) lst.get(5);
            this.minimum = (String) lst.get(6);
            this.maximum = (String) lst.get(7);
            this.units = (String) lst.get(8);
            this.result = (String) lst.get(9);
            this.locationId = (String) lst.get(10);
            this.last = (String) lst.get(11);
            MiscUtils.getLogger().debug("C "+title+" "+testName+" "+abn+" "+result+ " ("+minimum+"-"+maximum+")");
         }
      }
      
      public String save(Connection conn, String id) throws SQLException{
         String insertID = null;
         // Prepare a statement to insert a record
        String sql = "insert into labTestResults (labPatientPhysicianInfo_id,title,notUsed1,notUsed2,test_name,abn,minimum,maximum,units,result,location_id,last,line_type) "
         + " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);                 
            pstmt.setString(1,id ); // location_id
            pstmt.setString(2,this.title);  // printDate
            pstmt.setString(3,this.notUsed1 );  // printTime
            pstmt.setString(4,this.notUsed2);      // total_BType
            pstmt.setString(5,this.testName);      // total_CType 
            pstmt.setString(6,this.abn);
            pstmt.setString(7,this.minimum);
            pstmt.setString(8,this.maximum);
            pstmt.setString(9,this.units);
            pstmt.setString(10,this.result);
            pstmt.setString(11,this.locationId);
            pstmt.setString(12,this.last);
            pstmt.setString(13,"C");
        
            pstmt.executeUpdate();
                          
            ResultSet rs = pstmt.getGeneratedKeys();
            
            if(rs.next()){
               insertID = oscar.Misc.getString(rs,1);       
            }
                     
         return insertID;
      }
           

      String title = null;       //  2. Title
      String notUsed1 = null;    //  3. Not used ?
      String notUsed2 = null;    //  4. Not used ?
      String testName = null;    //  5. Test name
      String abn  = null;     //  6. Normal/Abnormal N or A
      String minimum = null;     //  7. Minimum
      String maximum = null;     //  8. Maximum
      String units = null;       //  9. Units
      String result = null;      // 10. Result
      String locationId = null;  // 11. Location Id (Test performed at )
      String last = null;        // 12. Last Y or N
   }

   class Dtype { //line: Comments/Titles
      
      void populate(ArrayList lst){
         if (lst.size() == 6){ // must equal 6
            this.title = (String) lst.get(1);
            this.notUsed1 = (String) lst.get(2);
            this.description = (String) lst.get(3);
            this.locationId = (String) lst.get(4);
            this.last = (String) lst.get(5);
            MiscUtils.getLogger().debug("D "+title+" "+description);
         }else{
            MiscUtils.getLogger().debug("DTYPE INVALID");
         }
      }
      



      
      public String save(Connection conn, String id) throws SQLException{
         String insertID = null;
         // Prepare a statement to insert a record //labComments 
        String sql = "insert into labTestResults (labPatientPhysicianInfo_id,title,notUsed1,description,location_id,last,line_type) "
         + " values (?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);                 
            pstmt.setString(1,id ); 
            pstmt.setString(2,this.title);  
            pstmt.setString(3,this.notUsed1);  
            pstmt.setString(4,this.description);                 
            pstmt.setString(5,this.locationId);
            pstmt.setString(6,this.last);
            pstmt.setString(7,"D");
        
            pstmt.executeUpdate();
                          
            ResultSet rs = pstmt.getGeneratedKeys();
            
            if(rs.next()){
               insertID = oscar.Misc.getString(rs,1);       
            }
         pstmt.close();             
         return insertID;
      }
      
      String title = null;       // 2. Title
      String notUsed1 = null;    // 3. not used ?
      String description = null; // 4. Description/Comment
      String locationId = null;  // 5. Location Id
      String last = null;        // 6. Last Y or N
   }
   ////
   
}
