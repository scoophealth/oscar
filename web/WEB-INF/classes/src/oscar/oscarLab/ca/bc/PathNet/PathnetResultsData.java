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
 * Ontario, Canada   Creates a new instance of PathnetResultsData
 *
 *
 * PathnetResultsData.java
 *
 * Created on September 26, 2005, 11:31 AM
 */

package oscar.oscarLab.ca.bc.PathNet;

import java.sql.ResultSet;
import java.util.*;
import org.apache.log4j.Logger;
import oscar.oscarDB.*;
import oscar.oscarLab.ca.on.*;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Jay Gallagher
 */
public class PathnetResultsData {
    
    Logger logger = Logger.getLogger(PathnetResultsData.class);
    
    public PathnetResultsData() {
    }
    
    /**
     *Populates ArrayList with labs attached to a consultation request
     */
    public ArrayList populatePathnetResultsData(String demographicNo, String consultationId, boolean attached) {
        String sql = "SELECT m.message_id, patientLabRouting.id " +
                "FROM hl7_message m, patientLabRouting " +
                "WHERE patientLabRouting.lab_no = m.message_id "+
                "AND patientLabRouting.lab_type = 'BCP' AND patientLabRouting.demographic_no=" + demographicNo+" GROUP BY m.message_id";
        
        String attachQuery = "SELECT consultdocs.document_no FROM consultdocs, patientLabRouting " +
                "WHERE patientLabRouting.id = consultdocs.document_no AND " +
                "consultdocs.requestId = " + consultationId + " AND consultdocs.doctype = 'L' AND consultdocs.deleted IS NULL ORDER BY consultdocs.document_no";
        
        ArrayList labResults = new ArrayList();
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
            
            LabResultData lbData = new LabResultData(LabResultData.EXCELLERIS);
            LabResultData.CompareId c = lbData.getComparatorId();
            rs = db.GetSQL(sql);
            
            while(rs.next()){
                
                lbData.labType = LabResultData.EXCELLERIS;
                lbData.segmentID = rs.getString("message_id");
                lbData.labPatientId = rs.getString("id");
                lbData.dateTime = findPathnetObservationDate(lbData.segmentID);
                lbData.discipline = findPathnetDisipline(lbData.segmentID);
                
                if( attached && Collections.binarySearch(attachedLabs, lbData, c) >= 0 )
                    labResults.add(lbData);
                else if( !attached && Collections.binarySearch(attachedLabs, lbData, c) < 0 )
                    labResults.add(lbData);
                
                lbData = new LabResultData(LabResultData.EXCELLERIS);
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("exception in CMLPopulate:",e);
        }
        return labResults;
    }
    /////
    public ArrayList populatePathnetResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status) {
        if ( providerNo == null) { providerNo = ""; }
        if ( patientFirstName == null) { patientFirstName = ""; }
        if ( patientLastName == null) { patientLastName = ""; }
        if ( patientHealthNumber == null) { patientHealthNumber = ""; }
        if ( status == null ) { status = ""; }
        
        
        ArrayList labResults =  new ArrayList();
        String sql = "";
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            if ( demographicNo == null) {
              
                sql  ="select pid.message_id, pid.external_id as patient_health_num,  pid.patient_name as patientName, pid.sex as patient_sex ,pid.pid_id, orc.filler_order_number as accessionNum, orc.ordering_provider, msh.date_time_of_message as date, min(obr.result_status) as stat, providerLabRouting.status " +
                        "from hl7_message m, hl7_msh msh, hl7_pid pid, hl7_orc orc, hl7_obr obr, providerLabRouting " +
                        "where providerLabRouting.lab_no = pid.message_id and pid.message_id = msh.message_id and pid.pid_id = orc.pid_id and pid.pid_id = obr.pid_id  "+
                        " AND providerLabRouting.status like '%"+status+"%' AND providerLabRouting.provider_no like '"+(providerNo.equals("")?"%":providerNo)+"'" +
                        " AND providerLabRouting.lab_type = 'BCP' " +
                        " AND pid.patient_name like '"+patientLastName+"%^"+patientFirstName+"%' AND pid.external_id like '%"+patientHealthNumber+"%'" +
                        " GROUP BY pid.message_id";
                
            } else {
               
                sql= "select pid.message_id, pid.external_id as patient_health_num,  pid.patient_name as patientName, pid.sex as patient_sex,pid.pid_id, orc.filler_order_number as accessionNum, orc.ordering_provider, msh.date_time_of_message as date, min(obr.result_status) as stat "+
                        "from hl7_msh msh, hl7_pid pid, hl7_orc orc, hl7_obr obr, patientLabRouting "+
                        "where patientLabRouting.lab_no = pid.message_id   and pid.pid_id = orc.pid_id and pid.pid_id = obr.pid_id and msh.message_id = pid.message_id "+
                        "and patientLabRouting.lab_type = 'BCP' and patientLabRouting.demographic_no='"+demographicNo+"'" + 
                        " group by pid.message_id";
            }
            
            logger.info("s: "+sql);
            ResultSet rs = db.GetSQL(sql);
            logger.debug("after sql statement");
            while(rs.next()){
                LabResultData lbData = new LabResultData(LabResultData.EXCELLERIS);
                lbData.labType = LabResultData.EXCELLERIS;
                lbData.segmentID = rs.getString("message_id");
                
                if (demographicNo == null && !providerNo.equals("0")) {
                    lbData.acknowledgedStatus = rs.getString("status");
                } else {
                    lbData.acknowledgedStatus ="U";
                }
                
                ///if (findPathnetAdnormalResults(lbData.segmentID) > 0){
                ///   lbData.abn= true;
                ///}
                
                lbData.accessionNumber = justGetAccessionNumber(rs.getString("accessionNum"));
                
                lbData.healthNumber = rs.getString("patient_health_num");
                lbData.patientName = rs.getString("patientName");
                if(lbData.patientName != null){
                    lbData.patientName = lbData.patientName.replaceAll("\\^", " ");
                }
                lbData.sex = rs.getString("patient_sex");
                lbData.resultStatus = "0"; //TODO
                // solve lbData.resultStatus.add(rs.getString("abnormalFlag"));
                
                lbData.dateTime = rs.getString("date");
                
                //priority
                lbData.priority = "----";
                lbData.requestingClient = justGetDocName(rs.getString("ordering_provider"));
                lbData.reportStatus =  rs.getString("stat");
                
                if (lbData.reportStatus != null && lbData.reportStatus.equals("F")){
                    lbData.finalRes = true;
                }else{
                    lbData.finalRes = false;
                }
                //lbData.discipline = "Hem/Chem/Other";
                ///lbData.discipline = findPathnetDisipline(lbData.segmentID);
                //lbData.finalResultsCount = findNumOfFinalResults(lbData.segmentID);
                labResults.add(lbData);
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("exception in pathnetPopulate", e);
        }
        return labResults;
    }
    
    public String findPathnetObservationDate(String labId){
        String  ret = "";
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            //String sql = "select min(obr.observation_date_time) as d from hl7_pid pid, hl7_obr obr where obr.pid_id = pid.pid_id and pid.message_id = '"+labId+"'";
            String sql = "select max(obr.results_report_status_change) as d from hl7_pid pid, hl7_obr obr where obr.pid_id = pid.pid_id and pid.message_id = '"+labId+"'";
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                ret = rs.getString("d");
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("exception in pathnetResultsData",e);
        }
        return ret;
    }
    
    public int findNumOfFinalResults(String labId){
        int ret = 0;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql ="SELECT COUNT(*) FROM hl7_pid pid, hl7_obr obr, hl7_obx obx WHERE obx.observation_result_status = 'F' AND obx.obr_id = obr.obr_id AND obr.pid_id = pid.pid_id AND pid.message_id = '"+labId+"'";
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                ret = (rs.getInt(1));
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("exception in MDSResultsData:",e);
        }
        return ret;
    }
    
    public boolean isLabLinkedWithPatient(String labId){
        boolean ret = false;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            //String sql = "select min(obr.observation_date_time) as d from hl7_pid pid, hl7_obr obr, hl7_obx obx where obr.pid_id = pid.pid_id and obx.obr_id = obr.obr_id and pid.message_id = '"+labId+"'";
            String sql = "select demographic_no from patientLabRouting where lab_no = '"+labId+"' and lab_type  = 'BCP' ";
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
            logger.error("exception in isLabLinkedWithPatient",e);
            
        }
        return ret;
    }
    
    
    public String justGetAccessionNumber(String s){
        String[] ss = s.split("-");
        if (ss.length == 3)
            return ss[0];
        else
            return ss[1];
    }
    
    public String getMatchingLabs(String labId){
        String  ret = "";
        String accessionNum ="";
        String labDate = "";
        int monthsBetween = 0;
        ArrayList labs = new ArrayList();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            
            // find the accession number
            String sql = "select orc.filler_order_number, max(results_report_status_change) as date from hl7_orc orc, hl7_pid pid, hl7_obr obr where obr.pid_id=pid.pid_id and orc.pid_id = pid.pid_id and pid.message_id = '"+labId+"' GROUP BY pid.message_id";
            ResultSet rs = db.GetSQL(sql);
            if(rs.next()){
                accessionNum = justGetAccessionNumber(rs.getString("filler_order_number"));
                labDate = rs.getString("date");
            }
            
            //String sql = "select filler_order_number from hl7_orc orc, hl7_pid pid where orc.pid_id = pid.pid_id and pid.message_id = '"+labId+"'";
            sql = "SELECT DISTINCT pid.message_id, max(results_report_status_change) as date FROM  hl7_pid pid, hl7_orc orc, hl7_obr obr WHERE orc.filler_order_number like '%"+accessionNum+"%' AND orc.pid_id = pid.pid_id AND obr.pid_id = pid.pid_id GROUP BY pid.message_id ORDER BY obr.results_report_status_change";
            rs = db.GetSQL(sql);
            while (rs.next()){
                Date dateA = UtilDateUtilities.StringToDate(rs.getString("date"), "yyyy-MM-dd HH:mm:ss");
                Date dateB = UtilDateUtilities.StringToDate(labDate, "yyyy-MM-dd HH:mm:ss");
                if (dateA.before(dateB)){
                    monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
                }else{
                    monthsBetween = UtilDateUtilities.getNumMonths(dateB, dateA);
                }
                
                if (monthsBetween < 4){
                    
                    if (ret.equals(""))
                        ret = rs.getString("message_id");
                    else
                        ret = ret+","+rs.getString("message_id");
                    
                }
            }
            rs.close();
            db.CloseConn();
            
        }catch(Exception e){
            logger.error("exception in PathnetResultsData",e);
            return labId;
        }
        return ret;
    }
    
    public String justGetDocName(String s){
        String ret = s;
        int i = s.indexOf("^");
        if (i != -1){
            ret = s.substring(i+1).replaceAll("\\^", " ");
        }
        return ret;
    }
    
    public String findPathnetOrderingProvider(String labId){
        String  ret = "";
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select ordering_provider from hl7_orc orc, hl7_pid pid where orc.pid_id = pid.pid_id and pid.message_id = '"+labId+"'";
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                ret = justGetDocName(rs.getString("ordering_provider"));
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("exception in MDSResultsData",e);
        }
        return ret;
    }
    public String findPathnetStatus(String labId){
        String ret = "";
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select min(obr.result_status) as stat from hl7_pid pid, hl7_obr obr, hl7_obx obx where obr.pid_id = pid.pid_id and obx.obr_id = obr.obr_id and pid.message_id = '"+labId+"'";
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                ret = rs.getString("stat");
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("exception in MDSResultsData",e);
        }
        return ret;
    }
    
    
    public String findPathnetDisipline(String labId){
        StringBuffer ret = new StringBuffer();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select distinct diagnostic_service_sect_id from hl7_pid pid, hl7_obr obr where obr.pid_id = pid.pid_id and pid.message_id = '"+labId+"'";
            ResultSet rs = db.GetSQL(sql);
            boolean first = true;
            while(rs.next()){
                if (!first){ ret.append("/"); }
                ret.append(rs.getString("diagnostic_service_sect_id"));
                first = false;
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("exception in MDSResultsData",e);
        }
        return ret.toString();
    }
    
    
    public int findPathnetAdnormalResults(String labId){
        int count = 0;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select abnormal_flags from hl7_pid pid, hl7_obr obr, hl7_obx obx where obr.pid_id = pid.pid_id and obx.obr_id = obr.obr_id and ( obx.abnormal_flags = 'A' or obx.abnormal_flags = 'H' or obx.abnormal_flags = 'HH' or obx.abnormal_flags = 'L' )     and pid.message_id ='"+labId+"'";
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                count++;
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("exception in MDSResultsData",e);
        }
        return count;
    }
}
