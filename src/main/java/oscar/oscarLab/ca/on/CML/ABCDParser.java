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


package oscar.oscarLab.ca.on.CML;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.oscarehr.common.dao.LabPatientPhysicianInfoDao;
import org.oscarehr.common.dao.LabReportInformationDao;
import org.oscarehr.common.dao.LabTestResultsDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.model.LabPatientPhysicianInfo;
import org.oscarehr.common.model.LabReportInformation;
import org.oscarehr.common.model.LabTestResults;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarLab.ca.all.upload.ProviderLabRouting;

/**
 *
 * @author  root
 */
public class ABCDParser {
   Logger logger = Logger.getLogger("cmlFileManagement.ABCDLogger");

   Atype reportFile = null;
   ArrayList<Atype> atypes = new ArrayList<Atype>();
   
   private PatientLabRoutingDao patientLabRoutingDao = SpringUtils.getBean(PatientLabRoutingDao.class);
   private LabReportInformationDao labReportInformationDao = SpringUtils.getBean(LabReportInformation.class);
   private LabPatientPhysicianInfoDao labPatientPhysicianInfoDao = SpringUtils.getBean(LabPatientPhysicianInfoDao.class);
   private LabTestResultsDao labTestResultsDao = SpringUtils.getBean(LabTestResultsDao.class);
   
   /** Creates a new instance of ABCDParse */
   public ABCDParser() {
   }

   public void save(Connection conn) throws SQLException{
      logger.info("Starting save");
      HashMap<String,String> htable = getProviderHash(conn);
      for ( int i = 0 ; i <  atypes.size(); i++){// for each report
         Atype report = atypes.get(i);
         logger.info("Saving Report for "+report.locationId+" printed on "+report.printDate+" "+report.printTime+ " # B "+report.totalBType+" #C "+report.totalCType+" #D "+report.totalDType);
         String id = report.save(conn, report.locationId,report.printDate,report.printTime,report.totalBType,report.totalCType,report.totalDType);//save AType to Database and return ID
         logger.info("Report save to the database ID ="+id);
         if (null != id){
            for (int j = 0 ; j< report.btypes.size(); j++){
               Btype patient = report.btypes.get(j);
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
   private void addToProviderLabTable(String docNum, HashMap<String,String> htable, String labId,Connection conn) throws SQLException{

      String providerNo = htable.get(docNum);

      if ( providerNo == null || providerNo.equals("null")){
         logger.info("Could not find provider "+docNum+" now trying provider "+docNum.substring(0,(docNum.length()-1))+"  for lab: "+labId);
         providerNo = htable.get(docNum.substring(0,(docNum.length()-1)));

      }

      if ( providerNo == null || providerNo.equals("null")){
         logger.info("Could not find provider "+docNum.substring(0,(docNum.length()-1))+"either  for lab: "+labId);
         logger.info("Setting Provider No to 0");
         providerNo = "0";

      }


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

        
        if (count != 1){
           demo = "0";
           logger.info("Could not find patient for lab: "+labId+ "# of possible matches :"+count);
        }
        
        PatientLabRouting p = new PatientLabRouting();
        p.setDemographicNo(Integer.parseInt(demo));
        p.setLabNo(Integer.parseInt(labId));
        p.setLabType("CML");
        patientLabRoutingDao.persist(p);
         
    }
   /////


   private HashMap<String,String> getProviderHash(Connection conn){
      logger.info("Init - provider Hash table");
      HashMap<String,String> htable = new HashMap<String,String>();
      try{
         String sql = "select provider_no, ohip_no from provider where ohip_no != '' ";
         PreparedStatement pstmt = conn.prepareStatement(sql);
         pstmt.executeQuery();
         ResultSet rs = pstmt.executeQuery();

         while(rs.next()){
            String key = oscar.Misc.getString(rs,"ohip_no");
            String value = oscar.Misc.getString(rs,"provider_no");
            logger.info("Possible provider HashMap key "+key+" lab "+value);
            if ( key != null && value != null && !key.equals("null") && !value.equals("null")){
               htable.put(key, value);
               logger.info("Adding  to provider HashMap key "+key+" lab "+value);
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


   public ArrayList<String> d(String s){
      ArrayList<String> arr = new ArrayList<String>();
      while (s.indexOf('^') != -1){
         int ind = s.indexOf('^');
         arr.add(s.substring(0,ind));
         s = s.substring(ind+1);

      }
      return arr;
   }

   void process(String str){
         if (str != null && !str.trim().equals("") ){

             ArrayList<String> a = d(str);

             String lineType = a.get(0);

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
      public ArrayList<Btype> btypes = new ArrayList<Btype>();
      public void read(String lineType, ArrayList<String> list){
         if(lineType.equals("B")){
            labreport = new Btype();
            labreport.populate(list);
            btypes.add(labreport);
         }else if(lineType.equals("A")){
            return;
         }
         labreport.process(lineType,list);

      }

      public String save(Connection conn, String locationId,String printDate,String printTime,String totalB,String totalC,String totalD) {
    	  LabReportInformation l = new LabReportInformation();
    	  l.setLocationId(locationId);
    	  l.setPrintDate(printDate);
    	  l.setPrintTime(printTime);
    	  l.setTotalBType(totalB);
    	  l.setTotalCType(totalC);
    	  l.setTotalDType(totalD);
    	  labReportInformationDao.persist(l);
        
    	  return String.valueOf(l.getId());
      }

      public Atype(){

      }
      void populate(ArrayList<String> lst){
         if (lst.size() == 7){ // must equal 7
            this.locationId = lst.get(1);
            this.printDate = lst.get(2);
            this.printTime = lst.get(3);
            this.totalBType = lst.get(4);
            this.totalCType = lst.get(5);
            this.totalDType = lst.get(6);
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
      void populate(ArrayList<String> lst){
         if (lst.size() == 22){ // must equal 22
            this.accessionNum =  lst.get(1);
            this.physicianAccountNum =  lst.get(2);
            this.serviceDate =  lst.get(3);
            this.pFirstName =  lst.get(4);
            this.pLastName =  lst.get(5);
            this.pSex =  lst.get(6);
            this.pHealthNum =  lst.get(7);
            this.pDOB =  lst.get(8);
            this.status =  lst.get(9);
            this.docNum =  lst.get(10);
            this.docName =  lst.get(11);
            this.docAddr1 =  lst.get(12);
            this.docAddr2 =  lst.get(13);
            this.docAddr3 =  lst.get(14);
            this.docPostal =  lst.get(15);
            this.docRoute =  lst.get(16);
            this.comment1 =  lst.get(17);
            this.comment2 =  lst.get(18);
            this.pPhone =  lst.get(19);
            this.docPhone =  lst.get(20);
            this.collectionDate =  lst.get(21);
            MiscUtils.getLogger().debug("B "+pFirstName+" "+pLastName+" "+pHealthNum+" "+docRoute+docNum);
         }
      }

      public String save(Connection conn, String id) {
       
         LabPatientPhysicianInfo l = new LabPatientPhysicianInfo();
         l.setLabReportInfoId(Integer.parseInt(id));
         l.setAccessionNum(this.accessionNum);
         l.setPhysicianAccountNum(this.physicianAccountNum);
         l.setServiceDate(this.serviceDate);
         l.setPatientFirstName(this.pFirstName);
         l.setPatientLastName(this.pLastName);
         l.setPatientSex(this.pSex);
         l.setPatientHin(this.pHealthNum);
         l.setPatientDob(this.pDOB);
         l.setLabStatus(this.status);
         l.setDocNum(this.docNum);
         l.setDocName(this.docName);
         l.setDocAddress1(this.docAddr1);
         l.setDocAddress2(this.docAddr2);
         l.setDocAddress3(this.docAddr3);
         l.setDocPostal(this.docPostal);
         l.setDocRoute(this.docRoute);
         l.setComment1(this.comment1);
         l.setComment2(this.comment2);
         l.setPatientPhone(this.pPhone);
         l.setDocPhone(this.docPhone);
         l.setCollectionDate(this.collectionDate);
         
         labPatientPhysicianInfoDao.persist(l);
         
         return String.valueOf(l.getId());
 
      }


      public void process(String lineType,ArrayList<String> list){
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

      ArrayList<Ctype> ctypes= new ArrayList<Ctype>();
      ArrayList<Dtype> dtypes= new ArrayList<Dtype>();
      ArrayList <ParentType> cdtypes = new ArrayList<ParentType>();
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

   class Ctype implements ParentType{ //line: Test results

      void populate(ArrayList<String> lst){
         if (lst.size() == 12){ // must equal 12
            this.title =  lst.get(1);
            this.notUsed1 =  lst.get(2);
            this.notUsed2 =  lst.get(3);
            this.testName =  lst.get(4);
            this.abn =  lst.get(5);
            this.minimum =  lst.get(6);
            this.maximum =  lst.get(7);
            this.units =  lst.get(8);
            this.result =  lst.get(9);
            this.locationId =  lst.get(10);
            this.last =  lst.get(11);
            MiscUtils.getLogger().debug("C "+title+" "+testName+" "+abn+" "+result+ " ("+minimum+"-"+maximum+")");
         }
      }

      public String save(Connection conn, String id) {
    	  LabTestResults l = new LabTestResults();
    	  l.setLabPatientPhysicianInfoId(Integer.parseInt(id));
    	  l.setTitle(this.title);
    	  l.setNotUsed1(this.notUsed1);
    	  l.setNotUsed2(this.notUsed2);
    	  l.setTestName(this.testName);
    	  l.setAbn(this.abn);
    	  l.setMinimum(this.minimum);
    	  l.setMaximum(this.maximum);
    	  l.setUnits(this.units);
    	  l.setResult(this.result);
    	  l.setLocationId(this.locationId);
    	  l.setLast(this.last);
    	  l.setLineType("C");
    	  labTestResultsDao.persist(l);
         
    	return String.valueOf(l.getId());
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

   class Dtype implements ParentType { //line: Comments/Titles

      void populate(ArrayList<String> lst){
         if (lst.size() == 6){ // must equal 6
            this.title =  lst.get(1);
            this.notUsed1 =  lst.get(2);
            this.description =  lst.get(3);
            this.locationId =  lst.get(4);
            this.last =  lst.get(5);
            MiscUtils.getLogger().debug("D "+title+" "+description);
         }else{
            MiscUtils.getLogger().debug("DTYPE INVALID");
         }
      }




      public String save(Connection conn, String id) {
    	  LabTestResults l = new LabTestResults();
    	  l.setLabPatientPhysicianInfoId(Integer.parseInt(id));
    	  l.setTitle(this.title);
    	  l.setNotUsed1(this.notUsed1);
    	  l.setDescription(this.description);
    	  l.setLocationId(this.locationId);
    	  l.setLast(this.last);
    	  l.setLineType("D");
    	
    	  labTestResultsDao.persist(l);
    	  
    	  return l.getId().toString();
        
      }

      String title = null;       // 2. Title
      String notUsed1 = null;    // 3. not used ?
      String description = null; // 4. Description/Comment
      String locationId = null;  // 5. Location Id
      String last = null;        // 6. Last Y or N
   }
   ////

}

interface ParentType {
	//for type safety
}
