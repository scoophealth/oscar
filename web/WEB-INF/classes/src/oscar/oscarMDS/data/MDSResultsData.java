/*
 *
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
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster Unviersity
 * Hamilton
 * Ontario, Canada
 */
package oscar.oscarMDS.data;

import oscar.oscarDB.*;
import java.util.*;
import java.sql.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import oscar.oscarLab.ca.on.*;
import oscar.util.UtilDateUtilities;


public class MDSResultsData {
   static Log log = LogFactory.getLog(MDSResultsData.class); 
    
   public ArrayList segmentID;
   public ArrayList acknowledgedStatus;
   
   public ArrayList healthNumber;
   public ArrayList patientName;
   public ArrayList sex;
   public ArrayList resultStatus;
   public ArrayList dateTime;
   public ArrayList priority;
   public ArrayList requestingClient;
   public ArrayList discipline;
   public ArrayList reportStatus;
   
   
   public ArrayList labResults;
     
   /**
    *Lists labs predicated on relationship to patient's consultation
    */
   public ArrayList populateCMLResultsData(String demographicNo, String consultationId, boolean attached) {
       String sql = "SELECT lpp.id, lpp.collection_date, patientLabRouting.id AS labId FROM labPatientPhysicianInfo lpp, patientLabRouting "
            +" WHERE patientLabRouting.lab_type = 'CML' AND lpp.id = patientLabRouting.lab_no AND " 
            +" patientLabRouting.demographic_no="+demographicNo;          
               
       String attachQuery = "SELECT document_no FROM consultdocs, patientLabRouting WHERE patientLabRouting.id = consultdocs.document_no AND " +
                            "consultdocs.requestId = " + consultationId + " AND consultdocs.doctype = 'L' AND consultdocs.deleted IS NULL ORDER BY document_no";
       
       labResults = new ArrayList();
       ArrayList attachedLabs = new ArrayList();
       
       try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs = db.GetSQL(attachQuery);
         
         while(rs.next()) {
             LabResultData lbData = new LabResultData(LabResultData.CML);
             lbData.labType = LabResultData.CML;
             lbData.labPatientId = rs.getString("document_no");
             attachedLabs.add(lbData);
         }
         rs.close();
         
         
         rs = db.GetSQL(sql);         
         LabResultData lbData = new LabResultData(LabResultData.CML);
         LabResultData.CompareId c = lbData.getComparatorId();
         while(rs.next()){            
            
            lbData.labType = LabResultData.CML;            
            lbData.labPatientId = rs.getString("labId");
            lbData.segmentID = rs.getString("id"); 
            lbData.dateTime = rs.getString("collection_date");
            lbData.setDateObj( UtilDateUtilities.getDateFromString(lbData.dateTime, "dd-MMM-yy") ); 
                        
            if( attached && Collections.binarySearch(attachedLabs, lbData, c) >= 0 )
                labResults.add(lbData);
            else if( !attached && Collections.binarySearch(attachedLabs, lbData, c) < 0 )
                labResults.add(lbData);
            
            lbData = new LabResultData(LabResultData.CML);
         }
         rs.close();
         db.CloseConn();                  
         
      }catch(Exception e){
         log.error("exception in populateCMLResultsData:"+sql,e);                  
         e.printStackTrace();
      }
      
      return labResults;
       
   }
   
   
   public ArrayList populateCMLResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status) {
      //log.debug("populateCMLResultsData getting called now");
      if ( providerNo == null) { providerNo = ""; }
      if ( patientFirstName == null) { patientFirstName = ""; }
      if ( patientLastName == null) { patientLastName = ""; }
      if ( patientHealthNumber == null) { patientHealthNumber = ""; }
      if ( status == null ) { status = ""; }
      
      
      labResults =  new ArrayList();
      // select lpp.patient_health_num, concat(lpp.patient_last_name,',',lpp.patient_first_name), lpp.patient_sex, lpp.doc_name, lpp.collection_date, lpp.lab_status from labPatientPhysicianInfo lpp;
      String sql = "";
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         if ( demographicNo == null) {
            // note to self: lab reports not found in the providerLabRouting table will not show up - need to ensure every lab is entered in providerLabRouting, with '0'
            // for the provider number if unable to find correct provider
            
            //sql = "select lpp.id, lpp.patient_health_num, concat(lpp.patient_last_name,',',lpp.patient_first_name) as patientName, lpp.patient_sex, lpp.doc_name, lpp.collection_date, lpp.lab_status, providerLabRouting.status "
            //+" from labPatientPhysicianInfo lpp, providerLabRouting "
            //+"where providerLabRouting.status like '%"+status+"%' AND providerLabRouting.provider_no like '"+(providerNo.equals("")?"%":providerNo)+"'" +
            //"AND lpp.patient_last_name like '"+patientLastName+"%' and lpp.patient_first_name like  '"+patientFirstName+"%' AND lpp.patient_health_num like '%"+patientHealthNumber+"%' "; //group by mdsMSH.segmentID";
            
            sql = "select lpp.id, lpp.patient_health_num, concat(lpp.patient_last_name,',',lpp.patient_first_name) as patientName, lpp.patient_sex, lpp.doc_name, lpp.collection_date, lpp.lab_status, providerLabRouting.status "
                 +" from labPatientPhysicianInfo lpp, providerLabRouting "
                 +" where providerLabRouting.status like '%"+status+"%' AND providerLabRouting.provider_no like '"+(providerNo.equals("")?"%":providerNo)+"'" 
                 +" AND providerLabRouting.lab_type = 'CML' "
                 +" AND lpp.patient_last_name like '"+patientLastName+"%' and lpp.patient_first_name like  '"+patientFirstName+"%' AND lpp.patient_health_num like '%"+patientHealthNumber+"%' and providerLabRouting.lab_no = lpp.id";
         } else {
                                   
            sql = "select lpp.id, lpp.patient_health_num, concat(lpp.patient_last_name,',',lpp.patient_first_name) as patientName, lpp.patient_sex, lpp.doc_name, lpp.collection_date, lpp.lab_status "
            +" from labPatientPhysicianInfo lpp, patientLabRouting "
            +" where patientLabRouting.lab_type = 'CML' and lpp.id = patientLabRouting.lab_no and patientLabRouting.demographic_no='"+demographicNo+"' "; //group by mdsMSH.segmentID";
         }
         
         
         log.debug(sql);
         ResultSet rs = db.GetSQL(sql);
         while(rs.next()){
            LabResultData lbData = new LabResultData(LabResultData.CML);
            
            lbData.labType = LabResultData.CML;
            
            lbData.segmentID = rs.getString("id");
            
            if (demographicNo == null && !providerNo.equals("0")) {
               lbData.acknowledgedStatus = rs.getString("status");
            } else {
               lbData.acknowledgedStatus ="U";
            }
            
            
            lbData.healthNumber = rs.getString("patient_health_num");
            lbData.patientName = rs.getString("patientName");
            lbData.sex = rs.getString("patient_sex");
            
            
            lbData.resultStatus = "0"; //TODO                        
            // solve lbData.resultStatus.add(rs.getString("abnormalFlag"));
            
            lbData.dateTime = rs.getString("collection_date");
            lbData.setDateObj( UtilDateUtilities.getDateFromString(lbData.dateTime, "dd-MMM-yy") );
            
            //priority
            lbData.priority = "----";
            
            lbData.requestingClient = rs.getString("doc_name");
            lbData.reportStatus =  rs.getString("lab_status");
            
            if (lbData.reportStatus != null && lbData.reportStatus.equals("F")){
               lbData.finalRes = true;
            }else{
               lbData.finalRes = false;
            }
            
            
            //if ( rs.getString("reportGroupDesc").startsWith("MICRO") ) {
            //   discipline.add("Microbiology");
            //} else if ( rs.getString("reportGroupDesc").startsWith("DIAGNOSTIC IMAGING") ) {
            //   discipline.add("Diagnostic Imaging");
            //} else {
               lbData.discipline = "Hem/Chem/Other";
            //}
            
            labResults.add(lbData);
         }
         rs.close();
         db.CloseConn();
      }catch(Exception e){
         log.error("exception in populateCMLResultsData:"+sql,e);                  
         e.printStackTrace();
      }
      
      return labResults;
   }
   
   public int findCMLAdnormalResults(String labId){
      int count = 0;
      String sql = null;
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                  
         sql = "select id from labTestResults where abn = 'A' and labPatientPhysicianInfo_id = '"+labId+"'";
         
         ResultSet rs = db.GetSQL(sql);
         while(rs.next()){
         count++;
         }
         rs.close();
         db.CloseConn();
      }catch(Exception e){
         log.error("exception in MDSResultsData:"+sql,e);                  
      }
      return count;
   }
   
   public void populateMDSResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status) {
      
      if ( providerNo == null) { providerNo = ""; }
      if ( patientFirstName == null) { patientFirstName = ""; }
      if ( patientLastName == null) { patientLastName = ""; }
      if ( patientHealthNumber == null) { patientHealthNumber = ""; }
      if ( status == null ) { status = ""; }
      
      segmentID = new ArrayList();
      acknowledgedStatus = new ArrayList();
      
      healthNumber = new ArrayList();
      patientName = new ArrayList();
      sex = new ArrayList();
      resultStatus = new ArrayList();
      dateTime  = new ArrayList();
      priority = new ArrayList();
      requestingClient = new ArrayList();
      discipline = new ArrayList();
      reportStatus = new ArrayList();
      
      String sql = "";
      
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         if ( demographicNo == null) {
            // note to self: lab reports not found in the providerLabRouting table will not show up - need to ensure every lab is entered in providerLabRouting, with '0'
            // for the provider number if unable to find correct provider
            sql = "SELECT mdsMSH.segmentID, providerLabRouting.status, mdsPID.patientName, mdsPID.healthNumber, " +
            "mdsPID.sex, max(mdsZFR.abnormalFlag) as abnormalFlag, mdsMSH.dateTime, mdsOBR.quantityTiming, mdsPV1.refDoctor, " +
            "min(mdsZFR.reportFormStatus) as reportFormStatus, mdsZRG.reportGroupDesc " +
            "FROM mdsMSH,mdsPID,providerLabRouting,mdsPV1,mdsZFR,mdsOBR,mdsZRG where " +
            "mdsMSH.segmentID=mdsPID.segmentID AND mdsMSH.segmentID=providerLabRouting.lab_no " +
            "AND mdsMSH.segmentID=mdsPV1.segmentID AND mdsMSH.segmentID=mdsZFR.segmentID " +
            "AND mdsMSH.segmentID=mdsOBR.segmentID AND mdsMSH.segmentID=mdsZRG.segmentID " +
            "AND providerLabRouting.status like '%"+status+"%' AND providerLabRouting.provider_no like '"+(providerNo.equals("")?"%":providerNo)+"'" +
            "AND mdsPID.patientName like '"+patientLastName+"%^"+patientFirstName+"%^%' AND mdsPID.healthNumber like '%"+patientHealthNumber+"%' group by mdsMSH.segmentID";
         } else {
            sql = "SELECT mdsMSH.segmentID, mdsPID.patientName, mdsPID.healthNumber, " +
            "mdsPID.sex, max(mdsZFR.abnormalFlag) as abnormalFlag, mdsMSH.dateTime, mdsOBR.quantityTiming, mdsPV1.refDoctor, " +
            "min(mdsZFR.reportFormStatus) as reportFormStatus, mdsZRG.reportGroupDesc " +
            "FROM mdsMSH,mdsPID,patientLabRouting,mdsPV1,mdsZFR,mdsOBR,mdsZRG where " +
            "mdsMSH.segmentID=mdsPID.segmentID AND mdsMSH.segmentID=patientLabRouting.lab_no " +
            "AND mdsMSH.segmentID=mdsPV1.segmentID AND mdsMSH.segmentID=mdsZFR.segmentID " +
            "AND mdsMSH.segmentID=mdsOBR.segmentID AND mdsMSH.segmentID=mdsZRG.segmentID " +
            "AND patientLabRouting.demographic_no='"+demographicNo+"' group by mdsMSH.segmentID";
         }
         
         ResultSet rs = db.GetSQL(sql);
         while(rs.next()){
            segmentID.add(Integer.toString(rs.getInt("segmentID")));
            if (demographicNo == null && !providerNo.equals("0")) {
               acknowledgedStatus.add(rs.getString("status"));
            } else {
               acknowledgedStatus.add("U");
            }
            
            healthNumber.add(rs.getString("healthNumber"));
            patientName.add(beautifyName(rs.getString("patientName")));
            sex.add(rs.getString("sex"));
            resultStatus.add(rs.getString("abnormalFlag"));
            dateTime.add(rs.getString("dateTime"));
            
            switch ( rs.getString("quantityTiming").charAt(0) ) {
               case 'C' : priority.add("Critical"); break;
               case 'S' : priority.add("Stat\\Urgent"); break;
               case 'U' : priority.add("Unclaimed"); break;
               case 'A' : if ( rs.getString("quantityTiming").startsWith("AL") ) {
                  priority.add("Alert");
               } else {
                  priority.add("ASAP");
               }
               break;
               default: priority.add("Routine"); break;
            }
            
            requestingClient.add(ProviderData.beautifyProviderName(rs.getString("refDoctor")));
            reportStatus.add(rs.getString("reportFormStatus"));
            
            if ( rs.getString("reportGroupDesc").startsWith("MICRO") ) {
               discipline.add("Microbiology");
            } else if ( rs.getString("reportGroupDesc").startsWith("DIAGNOSTIC IMAGING") ) {
               discipline.add("Diagnostic Imaging");
            } else {
               discipline.add("Hem/Chem/Other");
            }
         }
         rs.close();
         db.CloseConn();
      }catch(Exception e){
         log.error("exception in populateMDSResultsData:"+sql,e);
      }
   }
   
public ArrayList populateMDSResultsData2(String demographicNo, String consultationId, boolean attached) {   
    String sql = "SELECT patientLabRouting.id, mdsMSH.segmentID, mdsMSH.dateTime, mdsZRG.reportGroupDesc " +            
            "FROM " +    
            "patientLabRouting "+
            "LEFT JOIN mdsMSH on patientLabRouting.lab_no = mdsMSH.segmentID "+            
  	    "LEFT JOIN mdsZRG on patientLabRouting.lab_no = mdsZRG.segmentID "+
            "WHERE patientLabRouting.lab_type = 'MDS' " +
            "AND patientLabRouting.demographic_no=" + demographicNo;
    
    String attachQuery = "SELECT document_no FROM consultdocs, patientLabRouting WHERE patientLabRouting.id = consultdocs.document_no AND " +
                            "consultdocs.requestId = " + consultationId + " AND consultdocs.doctype = 'L' AND consultdocs.deleted IS NULL";
        
    labResults = new ArrayList();
    ArrayList attachedLabs = new ArrayList();
    
    try {
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
    
        ResultSet rs = db.GetSQL(attachQuery);
        while(rs.next()) {            
            LabResultData lbData = new LabResultData(LabResultData.EXCELLERIS);            
            lbData.labPatientId = rs.getString("document_no");
            attachedLabs.add(lbData);
        }
        rs.close();
        
        LabResultData lData = new LabResultData(LabResultData.MDS);            
        LabResultData.CompareId c = lData.getComparatorId();
        rs = db.GetSQL(sql);
         
        while(rs.next()) {            
            lData = new LabResultData(LabResultData.MDS);
            lData.segmentID = Integer.toString(rs.getInt("segmentID"));
            lData.labPatientId = rs.getString("id");
                        
            lData.dateTime = rs.getString("dateTime");
            lData.setDateObj(UtilDateUtilities.getDateFromString(lData.dateTime, "yyyy-MM-dd HH:mm:ss"));                                    
            
            String reportGroupDesc = rs.getString("reportGroupDesc");            
            if ( reportGroupDesc != null && reportGroupDesc.startsWith("MICRO") ) {
               lData.discipline = "Microbiology";
            } else if ( reportGroupDesc != null && reportGroupDesc.startsWith("DIAGNOSTIC IMAGING") ) {
               lData.discipline = "Diagnostic Imaging";
            } else {
               lData.discipline = "Hem/Chem/Other";
            }
            
            if( attached && Collections.binarySearch(attachedLabs, lData, c) >= 0 )
                labResults.add(lData);
            else if( !attached && Collections.binarySearch(attachedLabs, lData, c) < 0 )
                labResults.add(lData);                        
            
            lData = new LabResultData(LabResultData.MDS);
         }
         rs.close();
         db.CloseConn();
      }catch(Exception e){         
         log.error("exception in MDSResultsData:"+sql,e);
         e.printStackTrace();
      }
      return labResults;
}
   //////
   
   public ArrayList populateMDSResultsData2(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status) {
      //System.out.println("populateMDSResultsData2 top");
      if ( providerNo == null) { providerNo = ""; }
      if ( patientFirstName == null) { patientFirstName = ""; }
      if ( patientLastName == null) { patientLastName = ""; }
      if ( patientHealthNumber == null) { patientHealthNumber = ""; }
      if ( status == null ) { status = ""; }
      
      labResults = new ArrayList();            
      String sql = "";
      String seqId = null;  //for debugging purposes
      
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         //log.debug("populateMDSResultsData2 demo"+demographicNo);
         if ( demographicNo == null) {
            // note to self: lab reports not found in the providerLabRouting table will not show up - need to ensure every lab is entered in providerLabRouting, with '0'
            // for the provider number if unable to find correct provider
            sql = "SELECT mdsMSH.segmentID, providerLabRouting.status, mdsPID.patientName, mdsPID.healthNumber, " +
            "mdsPID.sex, max(mdsZFR.abnormalFlag) as abnormalFlag, mdsMSH.dateTime, mdsOBR.quantityTiming, mdsPV1.refDoctor, " +
            "min(mdsZFR.reportFormStatus) as reportFormStatus, mdsZRG.reportGroupDesc " +
            "FROM " +    
            "providerLabRouting "+
            "LEFT JOIN mdsMSH on providerLabRouting.lab_no = mdsMSH.segmentID "+
            "LEFT JOIN mdsPID on providerLabRouting.lab_no = mdsPID.segmentID "+
            "LEFT JOIN mdsPV1 on providerLabRouting.lab_no = mdsPV1.segmentID "+
            "LEFT JOIN mdsZFR on providerLabRouting.lab_no = mdsZFR.segmentID "+
            "LEFT JOIN mdsOBR on providerLabRouting.lab_no = mdsOBR.segmentID "+              
  	    "LEFT JOIN mdsZRG on providerLabRouting.lab_no = mdsZRG.segmentID "+
            "WHERE " +        
            "providerLabRouting.lab_type = 'MDS' " +
            "AND providerLabRouting.status like '%"+status+"%' AND providerLabRouting.provider_no like '"+(providerNo.equals("")?"%":providerNo)+"'" +
            "AND mdsPID.patientName like '"+patientLastName+"%^"+patientFirstName+"%^%' AND mdsPID.healthNumber like '%"+patientHealthNumber+"%' group by mdsMSH.segmentID";
         } else {
            sql = "SELECT mdsMSH.segmentID, mdsPID.patientName, mdsPID.healthNumber, " +
            "mdsPID.sex, max(mdsZFR.abnormalFlag) as abnormalFlag, mdsMSH.dateTime, mdsOBR.quantityTiming, mdsPV1.refDoctor, " +
            "min(mdsZFR.reportFormStatus) as reportFormStatus, mdsZRG.reportGroupDesc " +
            
            "FROM " +    
            "patientLabRouting "+
            "LEFT JOIN mdsMSH on patientLabRouting.lab_no = mdsMSH.segmentID "+
            "LEFT JOIN mdsPID on patientLabRouting.lab_no = mdsPID.segmentID "+
            "LEFT JOIN mdsPV1 on patientLabRouting.lab_no = mdsPV1.segmentID "+
            "LEFT JOIN mdsZFR on patientLabRouting.lab_no = mdsZFR.segmentID "+
            "LEFT JOIN mdsOBR on patientLabRouting.lab_no = mdsOBR.segmentID "+              
  	    "LEFT JOIN mdsZRG on patientLabRouting.lab_no = mdsZRG.segmentID "+
            "WHERE " +        
            "patientLabRouting.lab_type = 'MDS' " +
            "AND patientLabRouting.demographic_no='"+demographicNo+"' group by mdsMSH.segmentID";
            
         }
         
         log.debug("sql "+sql);
         ResultSet rs = db.GetSQL(sql);
         while(rs.next()){
            LabResultData lData = new LabResultData(LabResultData.MDS);
            lData.segmentID = Integer.toString(rs.getInt("segmentID"));
            seqId = lData.segmentID;
            
            if (demographicNo == null && !providerNo.equals("0")) {
               lData.acknowledgedStatus = rs.getString("status");
            } else {
               lData.acknowledgedStatus = "U";
            }
            
            lData.healthNumber = rs.getString("healthNumber");
            lData.patientName = beautifyName(rs.getString("patientName"));
            lData.sex = rs.getString("sex");
            lData.resultStatus = rs.getString("abnormalFlag");
            if(lData.resultStatus == null){
                lData.resultStatus = "0";
            }
            lData.dateTime = rs.getString("dateTime");
            lData.setDateObj(UtilDateUtilities.getDateFromString(lData.dateTime, "yyyy-MM-dd HH:mm:ss"));
            
            String quantityTimimg = rs.getString("quantityTiming");
            if(quantityTimimg != null){
                switch ( quantityTimimg.charAt(0) ) {
                   case 'C' : lData.priority = "Critical"; break;
                   case 'S' : lData.priority = "Stat\\Urgent"; break;
                   case 'U' : lData.priority = "Unclaimed"; break;
                   case 'A' : if ( quantityTimimg.startsWith("AL") ) {
                      lData.priority = "Alert";
                   } else {
                      lData.priority = "ASAP";
                   }
                   break;
                   default: lData.priority = "Routine"; break;
                }
            }else{
               lData.priority = "Routine"; 
            }
            
            lData.requestingClient = ProviderData.beautifyProviderName(rs.getString("refDoctor"));
            lData.reportStatus = rs.getString("reportFormStatus");
            
            if (lData.reportStatus != null && lData.reportStatus.equals("0")){    
               lData.finalRes = false;
            }else{
               lData.finalRes = true;
            }
            
            
            if (  !lData.resultStatus.equals("0") ){
               lData.abn = true;
            }
            String reportGroupDesc = rs.getString("reportGroupDesc");
            
            if ( reportGroupDesc != null && reportGroupDesc.startsWith("MICRO") ) {
               lData.discipline = "Microbiology";
            } else if ( reportGroupDesc != null && reportGroupDesc.startsWith("DIAGNOSTIC IMAGING") ) {
               lData.discipline = "Diagnostic Imaging";
            } else {
               lData.discipline = "Hem/Chem/Other";
            }
            labResults.add(lData); 
         }
         rs.close();
         db.CloseConn();
      }catch(Exception e){
         log.error("Error processing MDS lab, segment # "+seqId); 
         log.error("exception in MDSResultsData:"+sql,e);
         e.printStackTrace();
      }
      return labResults;
   }
   //////
   
   
   private String beautifyName(String name) {
      try {
         return name.substring(0, name.indexOf("^")) + ", "
         + name.substring(name.indexOf("^") + 1).replace('^', ' ');
      } catch (IndexOutOfBoundsException e) {
         return name.replace('^', ' ');
      }
   }
   
   
   public static boolean updateReportStatus(Properties props, int labNo, int providerNo, char status, String comment) {
      String sql = null;
      try {
         DBPreparedHandler db = new DBPreparedHandler( props.getProperty("db_driver"), props.getProperty("db_uri")+props.getProperty("db_name"), props.getProperty("db_username"), props.getProperty("db_password") );
         // handles the case where this provider/lab combination is not already in providerLabRouting table
         sql = "insert ignore into providerLabRouting (provider_no, lab_no, status, comment) values ('"+providerNo+"', '"+labNo+"', '"+status+"', ?)";
         if ( db.queryExecuteUpdate(sql, new String[] { comment }) == 0 ) {
            // handles the case where it is
            sql = "update providerLabRouting set status='"+status+"', comment=? where provider_no='"+providerNo+"' and lab_no='"+labNo+"'";
             db.queryExecute(sql, new String[] { comment });
         } else {
            sql = "delete from providerLabRouting where provider_no='0' and lab_no=?";
            db.queryExecute(sql, new String[] { Integer.toString(labNo) });
         }
         db.closeConn();
         return true;
      }catch(Exception e){
         log.error("exception in MDSResultsData.updateReportStatus():"+sql,e);
         e.printStackTrace();
         return false;
      }
   }
   
   public static boolean updateLabRouting(String[] flaggedLabs, String selectedProviders) {
      boolean result;
      String sql = null;
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         
         String[] providersArray = selectedProviders.split(",");
         String insertString = "";
         String deleteString = "";
         for (int i=0; i < flaggedLabs.length; i++) {
            if (i != 0) {
               insertString = insertString + ", ";
               deleteString = deleteString + ", ";
            }
            for (int j=0; j < providersArray.length; j++) {
               if (j != 0) {
                  insertString = insertString + ", ";
               }
               insertString = insertString + "('" + providersArray[j] + "','" + flaggedLabs[i] + "','N')";
            }
            deleteString = deleteString+"'"+flaggedLabs[i]+"'";
         }
         
         // delete old entries
         sql = "delete from providerLabRouting where provider_no='0' and lab_no in ("+deleteString+")";
         result = db.RunSQL(sql);
         
         // add new entries
         sql = "insert ignore into providerLabRouting (provider_no, lab_no, status) values "+insertString;
         result = db.RunSQL(sql);
         db.CloseConn();
         return result;
      }catch(Exception e){
         log.error("exception in MDSResultsData.updateLabRouting():"+sql,e);
         return false;
      }
   }
   
   public static String searchPatient(String labNo) {
      String sql = null;
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         
         sql = "select demographic_no from patientLabRouting where lab_no='"+labNo+"'";
         ResultSet rs = db.GetSQL(sql);
         db.CloseConn();
         rs.next();
         return rs.getString("demographic_no");
      }catch(Exception e){
         log.error("exception in MDSResultsData.searchPatient():"+sql,e);
         return "0";
      }
   }
   
   public static boolean updatePatientLabRouting(String labNo, String demographicNo) {
      boolean result;
      String sql = null;
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         
         // delete old entries
         sql = "delete from patientLabRouting where lab_no='"+labNo+"'";
         result = db.RunSQL(sql);
         
         // add new entries
         sql = "insert into patientLabRouting (lab_no, demographic_no) values ('"+labNo+"', '"+demographicNo+"')";
         result = db.RunSQL(sql);
         db.CloseConn();
         return result;
      }catch(Exception e){
         log.error("exception in MDSResultsData.updateLabRouting():"+sql,e);
         return false;
      }
   }
   
}
