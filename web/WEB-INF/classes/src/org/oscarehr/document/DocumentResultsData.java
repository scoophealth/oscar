/*
 * Hl7textResultsData.java
 *
 * Created on June 19, 2007, 10:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.oscarehr.document;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.log4j.Logger;

import oscar.oscarDB.DBHandler;
import oscar.oscarLab.ca.on.LabResultData;

/**
 *
 * @author wrighd
 */
public class DocumentResultsData {
    
    Logger logger = Logger.getLogger(DocumentResultsData.class);
    
    /** Creates a new instance of Hl7textResultsData */
    public DocumentResultsData() {
    }
    
   
    
   
    
    /**
     *Populates ArrayList with labs attached to a consultation request
     */
    public ArrayList populateHL7ResultsData(String demographicNo, String consultationId, boolean attached) {
        String sql = "SELECT hl7.lab_no, hl7.obr_date, hl7.discipline, hl7.accessionNum, hl7.final_result_count, patientLabRouting.id " +
                "FROM hl7TextInfo hl7, patientLabRouting " +
                "WHERE patientLabRouting.lab_no = hl7.lab_no "+
                "AND patientLabRouting.lab_type = 'HL7' AND patientLabRouting.demographic_no=" + demographicNo+" GROUP BY hl7.lab_no";
        
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
                lbData.labPatientId = db.getString(rs,"document_no");
                attachedLabs.add(lbData);
            }
            rs.close();
            
            LabResultData lbData = new LabResultData(LabResultData.HL7TEXT);
            LabResultData.CompareId c = lbData.getComparatorId();
            rs = db.GetSQL(sql);
            
            while(rs.next()){
                
                lbData.segmentID = db.getString(rs,"lab_no");
                lbData.labPatientId = db.getString(rs,"id");
                lbData.dateTime = db.getString(rs,"obr_date");
                lbData.discipline = db.getString(rs,"discipline");
                lbData.accessionNumber = db.getString(rs,"accessionNum");
                lbData.finalResultsCount = rs.getInt("final_result_count");
                
                if( attached && Collections.binarySearch(attachedLabs, lbData, c) >= 0 )
                    labResults.add(lbData);
                else if( !attached && Collections.binarySearch(attachedLabs, lbData, c) < 0 )
                    labResults.add(lbData);
                
                lbData = new LabResultData(LabResultData.HL7TEXT);
            }
            rs.close();
            //db.CloseConn();
        }catch(Exception e){
            logger.error("exception in HL7Populate",e);
        }
        return labResults;
    }
    
    public ArrayList populateDocumentResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status) {
        
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
                
                sql = "select * "+//providerLabRouting.status " +
                        "from document doc, providerLabRouting " +
                        " where doc.document_no = providerLabRouting.lab_no "+
                        " AND providerLabRouting.status like '%"+status+"%' AND providerLabRouting.provider_no like '"+(providerNo.equals("")?"%":providerNo)+"'" +
                        " AND providerLabRouting.lab_type = 'DOC' " +
                        " ORDER BY doc.document_no DESC";
                
            } else {
                
                sql = "select info.lab_no, info.sex, info.health_no, info.result_status, info.obr_date, info.priority, info.requesting_client, info.discipline, info.last_name, info.first_name, info.report_status, info.accessionNum, info.final_result_count " +
                        "from hl7TextInfo info, patientLabRouting " +
                        " where info.lab_no = patientLabRouting.lab_no "+
                        " AND patientLabRouting.lab_type = 'HL7' AND patientLabRouting.demographic_no='"+demographicNo+"' ORDER BY info.lab_no DESC";
            }
            
            logger.info(sql);
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                
                //ocument_no | doctype | docdesc  | docxml | docfilename              | doccreator | program_id | updatedatetime      | status | contenttype | public1 | observationdate |

                
                LabResultData lbData = new LabResultData(LabResultData.DOCUMENT);
                lbData.labType = LabResultData.DOCUMENT;
                lbData.segmentID = db.getString(rs,"document_no");
                
                if (demographicNo == null && !providerNo.equals("0")) {
                    lbData.acknowledgedStatus = db.getString(rs,"status");
                } else {
                    lbData.acknowledgedStatus ="U";
                }
                
                lbData.healthNumber = "";//db.getString(rs,"health_no");
                lbData.patientName = "Not, Assigned";//db.getString(rs,"module_id");//+", "+db.getString(rs,"first_name");
                lbData.sex = "";//db.getString(rs,"sex");
                
                
                //BAD!!!! CODING APROACHING
                DBHandler dbh = new DBHandler(DBHandler.OSCAR_DATA);
                String sqlcd = "select * from ctl_document cd, demographic d where cd.module = 'demographic' and cd.module_id != '-1' and cd.module_id = d.demographic_no and cd.document_no = '"+lbData.segmentID+"'";
                ResultSet rscd= dbh.GetSQL(sqlcd);
                lbData.isMatchedToPatient = false;
                while(rscd.next()){
                     lbData.patientName = rscd.getString("last_name")+ ", "+rscd.getString("first_name");
                     lbData.healthNumber = rscd.getString("hin");
                     lbData.sex = rscd.getString("sex");
                     lbData.isMatchedToPatient = true;
                     lbData.setLabPatientId(Integer.toString(rscd.getInt("module_id")));
                }
                if(lbData.getPatientName().equalsIgnoreCase("Not, Assigned"))
                    lbData.setLabPatientId("-1");
                System.out.println("DOCU<ENT "+lbData.isMatchedToPatient());
                rscd.close();
                //dbh.CloseConn();
                //END BAD
                lbData.accessionNumber = "";//db.getString(rs,"accessionNum");
                
                lbData.resultStatus = "N";//;db.getString(rs,"result_status");
                if (lbData.resultStatus.equals("A"))
                    lbData.abn = true;
                
                lbData.dateTime = db.getString(rs,"observationdate");
                lbData.setDateObj(rs.getDate("observationdate"));
                
                //priority
                String priority = "";//db.getString(rs,"priority");
                
                if(priority != null && !priority.equals("")){
                    switch ( priority.charAt(0) ) {
                        case 'C' : lbData.priority = "Critical"; break;
                        case 'S' : lbData.priority = "Stat/Urgent"; break;
                        case 'U' : lbData.priority = "Unclaimed"; break;
                        case 'A' : lbData.priority = "ASAP"; break;
                        case 'L' : lbData.priority = "Alert"; break;
                        default: lbData.priority = "Routine"; break;
                    }
                }else{
                    lbData.priority = "----";
                }
                
                lbData.requestingClient = "";//db.getString(rs,"requesting_client");
                lbData.reportStatus =  "F";//db.getString(rs,"report_status");
                
                // the "C" is for corrected excelleris labs
                if (lbData.reportStatus != null && (lbData.reportStatus.equals("F") || lbData.reportStatus.equals("C"))){
                    lbData.finalRes = true;
                }else{
                    lbData.finalRes = false;
                }
                
                lbData.discipline = db.getString(rs,"doctype");
                if (lbData.discipline.trim().equals("")){
                    lbData.discipline = null;
                }
                
                lbData.finalResultsCount = 0;//rs.getInt("final_result_count");
                labResults.add(lbData);
            }
            rs.close();
            //db.CloseConn();
        }catch(Exception e){
            logger.error("exception in DOCPopulate:", e);
        }
        return labResults;
    }
    
}
