/*
 * Hl7textResultsData.java
 *
 * Created on June 19, 2007, 10:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all;

import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;
import oscar.oscarDB.*;
import oscar.oscarLab.ca.on.*;

/**
 *
 * @author wrighd
 */
public class Hl7textResultsData {
    
    Logger logger = Logger.getLogger(Hl7textResultsData.class);
    
    /** Creates a new instance of Hl7textResultsData */
    public Hl7textResultsData() {
    }
       
    public String getMatchingLabs(String lab_no){
        String sql = "SELECT lab_no FROM hl7TextInfo WHERE accessionNum !='' AND accessionNum=(SELECT accessionNum FROM hl7TextInfo WHERE lab_no='"+lab_no+"') ORDER BY final_result_count";
        String ret = "";
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()) {
                if(ret.equals(""))
                    ret = rs.getString("lab_no");
                else
                    ret = ret+","+rs.getString("lab_no");
            }
            rs.close();
        }catch(Exception e){
            logger.error("Exception in HL7 getMatchingLabs: ", e);
        }
        if (ret.equals(""))
            return(lab_no);
        else
            return(ret);
    }
    
    /**
     *Populates ArrayList with labs attached to a consultation request
     */
    public ArrayList populateHL7ResultsData(String demographicNo, String consultationId, boolean attached) {
        String sql = "SELECT hl7.lab_no, hl7.obr_date, hl7.discipline, hl7.accessionNum, hl7.final_result_count, patientLabRouting.id " +
                "FROM hl7TextInfo hl7, patientLabRouting " +
                "WHERE patientLabRouting.lab_no = hl7.lab_no "+
                "AND patientLabRouting.lab_type = 'HL7' AND patientLabRouting.demographic_no=" + demographicNo;
        
        String attachQuery = "SELECT consultdocs.document_no FROM consultdocs, patientLabRouting " +
                "WHERE patientLabRouting.id = consultdocs.document_no AND " +
                "consultdocs.requestId = " + consultationId + " AND consultdocs.doctype = 'L' AND consultdocs.deleted IS NULL ORDER BY consultdocs.document_no";
        
        ArrayList labResults = new ArrayList();
        ArrayList attachedLabs = new ArrayList();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            
            ResultSet rs = db.GetSQL(attachQuery);
            while(rs.next()) {
                LabResultData lbData = new LabResultData(LabResultData.HL7TEXT);
                lbData.labPatientId = rs.getString("document_no");
                attachedLabs.add(lbData);
            }
            rs.close();
            
            LabResultData lbData = new LabResultData(LabResultData.HL7TEXT);
            LabResultData.CompareId c = lbData.getComparatorId();
            rs = db.GetSQL(sql);
            
            while(rs.next()){
                
                lbData.labType = LabResultData.HL7TEXT;
                lbData.segmentID = rs.getString("lab_no");
                lbData.labPatientId = rs.getString("id");
                lbData.dateTime = rs.getString("obr_date");
                lbData.discipline = rs.getString("discipline");
                lbData.accessionNumber = rs.getString("accessionNum");
                lbData.finalResultsCount = rs.getInt("final_result_count");                
                lbData.multiLabId = getMatchingLabs(lbData.segmentID);
                
                if( attached && Collections.binarySearch(attachedLabs, lbData, c) >= 0 )
                    labResults.add(lbData);
                else if( !attached && Collections.binarySearch(attachedLabs, lbData, c) < 0 )
                    labResults.add(lbData);
                
                lbData = new LabResultData(LabResultData.EXCELLERIS);
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("exception in HL7Populate",e);            
        }
        return labResults;
    }
    
    public ArrayList populateHl7ResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status) {
        
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
                // note to self: lab reports not found in the providerLabRouting table will not show up - need to ensure every lab is entered in providerLabRouting, with '0'
                // for the provider number if unable to find correct provider
                
                sql = "select info.lab_no, info.sex, info.health_no, info.result_status, info.obr_date, info.priority, info.requesting_client, info.discipline, info.last_name, info.first_name, info.report_status, info.accessionNum, info.final_result_count, providerLabRouting.status " +
                        "from hl7TextInfo info, providerLabRouting " +
                        " where info.lab_no = providerLabRouting.lab_no "+
                        " AND providerLabRouting.status like '%"+status+"%' AND providerLabRouting.provider_no like '"+(providerNo.equals("")?"%":providerNo)+"'" +
                        " AND providerLabRouting.lab_type = 'HL7' " +
                        " AND info.first_name like '"+patientFirstName+"%' AND info.last_name like '"+patientLastName+"%' AND info.health_no like '%"+patientHealthNumber+"%' ";
                
            } else {
                
                sql = "select info.lab_no, info.sex, info.health_no, info.result_status, info.obr_date, info.priority, info.requesting_client, info.discipline, info.last_name, info.first_name, info.report_status, info.accessionNum, info.final_result_count " +
                        "from hl7TextInfo info, patientLabRouting " +
                        " where info.lab_no = patientLabRouting.lab_no "+
                        " AND patientLabRouting.lab_type = 'HL7' AND patientLabRouting.demographic_no='"+demographicNo+"' ";
            }
            
            logger.info(sql);
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                LabResultData lbData = new LabResultData(LabResultData.HL7TEXT);
                lbData.labType = LabResultData.HL7TEXT;
                lbData.segmentID = rs.getString("lab_no");
                               
                if (demographicNo == null && !providerNo.equals("0")) {
                    lbData.acknowledgedStatus = rs.getString("status");
                } else {
                    lbData.acknowledgedStatus ="U";
                }
                
                lbData.accessionNumber = rs.getString("accessionNum");
                lbData.healthNumber = rs.getString("health_no");
                lbData.patientName = rs.getString("last_name")+" "+rs.getString("first_name");
                lbData.sex = rs.getString("sex");
                
                lbData.resultStatus = rs.getString("result_status");
                if (lbData.resultStatus.equals("A"))
                    lbData.abn = true;
                
                lbData.dateTime = rs.getString("obr_date");
                
                //priority
                lbData.priority = rs.getString("priority");
                if (lbData.priority.equals(""))
                    lbData.priority = "----";
                
                lbData.requestingClient = rs.getString("requesting_client");
                lbData.reportStatus =  rs.getString("report_status");
                
                if (lbData.reportStatus != null && lbData.reportStatus.equals("F")){
                    lbData.finalRes = true;
                }else{
                    lbData.finalRes = false;
                }
                lbData.discipline = rs.getString("discipline");
                lbData.finalResultsCount = rs.getInt("final_result_count");
                lbData.multiLabId = getMatchingLabs(lbData.segmentID);
                labResults.add(lbData);
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("exception in Hl7Populate:", e);
        }
        return labResults;
    }
    
}
