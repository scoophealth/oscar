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


package oscar.oscarMDS.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import oscar.oscarDB.DBHandler;
import oscar.oscarDB.DBPreparedHandler;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.UtilDateUtilities;


public class MDSResultsData {

    Logger logger = Logger.getLogger(MDSResultsData.class);

    public ArrayList<String> segmentID;
    public ArrayList<String> acknowledgedStatus;

    public ArrayList<String> healthNumber;
    public ArrayList<String> patientName;
    public ArrayList<String> sex;
    public ArrayList<String> resultStatus;
    public ArrayList<String> dateTime;
    public ArrayList<String> priority;
    public ArrayList<String> requestingClient;
    public ArrayList<String> discipline;
    public ArrayList<String> reportStatus;


    public ArrayList<LabResultData> labResults;

    /**
     *Lists labs predicated on relationship to patient's consultation
     */
    public ArrayList<LabResultData> populateCMLResultsData(String demographicNo, String consultationId, boolean attached) {
        String sql = "SELECT lpp.id, lpp.collection_date, lpp.accession_num, patientLabRouting.id AS labId FROM labPatientPhysicianInfo lpp, patientLabRouting"
                +" WHERE patientLabRouting.lab_type = 'CML' AND lpp.id = patientLabRouting.lab_no AND"
                +" patientLabRouting.demographic_no="+demographicNo;

        String attachQuery = "SELECT document_no FROM consultdocs, patientLabRouting WHERE patientLabRouting.id = consultdocs.document_no AND " +
                "consultdocs.requestId = " + consultationId + " AND consultdocs.doctype = 'L' AND consultdocs.deleted IS NULL ORDER BY document_no";

        labResults = new ArrayList<LabResultData>();
        ArrayList<LabResultData> attachedLabs = new ArrayList<LabResultData>();

        try {

            ResultSet rs = DBHandler.GetSQL(attachQuery);

            while(rs.next()) {
                LabResultData lbData = new LabResultData(LabResultData.CML);
                lbData.labType = LabResultData.CML;
                lbData.labPatientId = oscar.Misc.getString(rs, "document_no");
                attachedLabs.add(lbData);
            }
            rs.close();


            rs = DBHandler.GetSQL(sql);
            LabResultData lbData = new LabResultData(LabResultData.CML);
            LabResultData.CompareId c = lbData.getComparatorId();
            while(rs.next()){

                lbData.labType = LabResultData.CML;
                lbData.labPatientId = oscar.Misc.getString(rs, "labId");
                lbData.segmentID = oscar.Misc.getString(rs, "id");
                lbData.dateTime = oscar.Misc.getString(rs, "collection_date");
                lbData.setDateObj( UtilDateUtilities.getDateFromString(lbData.dateTime, "dd-MMM-yy") );

                if( attached && Collections.binarySearch(attachedLabs, lbData, c) >= 0 )
                    labResults.add(lbData);
                else if( !attached && Collections.binarySearch(attachedLabs, lbData, c) < 0 )
                    labResults.add(lbData);

                lbData = new LabResultData(LabResultData.CML);
            }
            rs.close();

        }catch(Exception e){
            logger.error("exception in CMLPopulate", e);

        }

        return labResults;

    }


 public ArrayList<LabResultData> populateEpsilonResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, Integer labNo) {
        //logger.info("populateCMLResultsData getting called now");
        if ( providerNo == null) { providerNo = ""; }
        if ( patientFirstName == null) { patientFirstName = ""; }
        if ( patientLastName == null) { patientLastName = ""; }
        if ( patientHealthNumber == null) { patientHealthNumber = ""; }
        if ( status == null ) { status = ""; }


        labResults =  new ArrayList<LabResultData>();
        String sql = "";
        try {

        	if(labNo != null && labNo.intValue()>0) {
        		sql = "select lpp.id, lpp.patient_health_num, concat(lpp.patient_last_name,',',lpp.patient_first_name) as patientName, lpp.patient_sex, lpp.doc_name, lpp.collection_date, lpp.lab_status, lpp.accession_num from labPatientPhysicianInfo lpp where lpp.id = " + labNo;
        	} else {
	            if ( demographicNo == null) {
	                 sql = "select lpp.id, lpp.patient_health_num, concat(lpp.patient_last_name,',',lpp.patient_first_name) as patientName, lpp.patient_sex, lpp.doc_name, lpp.collection_date, lpp.lab_status, lpp.accession_num, providerLabRouting.status "
	                        +" from labPatientPhysicianInfo lpp, providerLabRouting "
	                        +" where providerLabRouting.status like '%"+status+"%' AND providerLabRouting.provider_no like '"+(providerNo.equals("")?"%":providerNo)+"'"
	                        +" AND providerLabRouting.lab_type = 'Epsilon' "
	                        +" AND lpp.patient_last_name like '"+patientLastName+"%' and lpp.patient_first_name like '"+patientFirstName+"%' AND lpp.patient_health_num like '%"+patientHealthNumber+"%' and providerLabRouting.lab_no = lpp.id";
	            } else {
	
	                sql = "select lpp.id, lpp.patient_health_num, concat(lpp.patient_last_name,',',lpp.patient_first_name) as patientName, lpp.patient_sex, lpp.doc_name, lpp.collection_date, lpp.lab_status, lpp.accession_num "
	                        +" from labPatientPhysicianInfo lpp, patientLabRouting "
	                        +" where patientLabRouting.lab_type = 'Epsilon' and lpp.id = patientLabRouting.lab_no and patientLabRouting.demographic_no='"+demographicNo+"' "; //group by mdsMSH.segmentID";
	            }
        	}

            logger.debug(sql);
            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                LabResultData lbData = new LabResultData(LabResultData.CML);

                lbData.labType = LabResultData.CML;

                lbData.segmentID = oscar.Misc.getString(rs,"id");

                if (demographicNo == null && !providerNo.equals("0")) {
                	try {
						lbData.acknowledgedStatus = oscar.Misc.getString(rs, "status");
					}catch(SQLException e) {
						//the load by labNo version doesn't care about inbox status.
						lbData.acknowledgedStatus ="U";
					}
                } else {
                    lbData.acknowledgedStatus ="U";
                }


                lbData.healthNumber = oscar.Misc.getString(rs,"patient_health_num");
                lbData.patientName = oscar.Misc.getString(rs,"patientName");
                lbData.sex = oscar.Misc.getString(rs,"patient_sex");


                lbData.resultStatus = "0"; //TODO
                // solve lbData.resultStatus.add(db.getString(rs,"abnormalFlag"));


                lbData.dateTime = oscar.Misc.getString(rs,"collection_date");
                lbData.setDateObj( UtilDateUtilities.getDateFromString(lbData.dateTime, "dd-MMM-yy") );

                //priority
                lbData.priority = "----";

                lbData.requestingClient = oscar.Misc.getString(rs,"doc_name");
                lbData.reportStatus =  oscar.Misc.getString(rs,"lab_status");
                lbData.accessionNumber = oscar.Misc.getString(rs,"accession_num");

                if (lbData.reportStatus != null && lbData.reportStatus.equals("F")){
                    lbData.finalRes = true;
                }else{
                    lbData.finalRes = false;
                }

                lbData.discipline = "Hem/Chem/Other";

                labResults.add(lbData);
            }
            rs.close();
        }catch(Exception e){
            logger.error("exception in CMLPopulate", e);

        }

        return labResults;

}

    public ArrayList<LabResultData> populateCMLResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, Integer labNo) {
        //logger.info("populateCMLResultsData getting called now");
        if ( providerNo == null) { providerNo = ""; }
        if ( patientFirstName == null) { patientFirstName = ""; }
        if ( patientLastName == null) { patientLastName = ""; }
        if ( patientHealthNumber == null) { patientHealthNumber = ""; }
        if ( status == null ) { status = ""; }


        labResults =  new ArrayList<LabResultData>();
        String sql = "";
        try {

        	if(labNo != null && labNo.intValue()>0) {
        		sql = "select lpp.id, lpp.patient_health_num, concat(lpp.patient_last_name,',',lpp.patient_first_name) as patientName, lpp.patient_sex, lpp.doc_name, lpp.collection_date, lpp.lab_status, lpp.accession_num from labPatientPhysicianInfo lpp where lpp.id = " + labNo;
        	} else {
	            if ( demographicNo == null) {
	                // note to self: lab reports not found in the providerLabRouting table will not show up - need to ensure every lab is entered in providerLabRouting, with '0'
	                // for the provider number if unable to find correct provider
	
	              
	                sql = "select lpp.id, lpp.patient_health_num, concat(lpp.patient_last_name,',',lpp.patient_first_name) as patientName, lpp.patient_sex, lpp.doc_name, lpp.collection_date, lpp.lab_status, lpp.accession_num, providerLabRouting.status "
	                        +" from labPatientPhysicianInfo lpp, providerLabRouting "
	                        +" where providerLabRouting.status like '%"+status+"%' AND providerLabRouting.provider_no like '"+(providerNo.equals("")?"%":providerNo)+"'"
	                        +" AND providerLabRouting.lab_type = 'CML' "
	                        +" AND lpp.patient_last_name like '"+patientLastName+"%' and lpp.patient_first_name like '"+patientFirstName+"%' AND lpp.patient_health_num like '%"+patientHealthNumber+"%' and providerLabRouting.lab_no = lpp.id";
	            } else {
	
	                sql = "select lpp.id, lpp.patient_health_num, concat(lpp.patient_last_name,',',lpp.patient_first_name) as patientName, lpp.patient_sex, lpp.doc_name, lpp.collection_date, lpp.lab_status, lpp.accession_num "
	                        +" from labPatientPhysicianInfo lpp, patientLabRouting "
	                        +" where patientLabRouting.lab_type = 'CML' and lpp.id = patientLabRouting.lab_no and patientLabRouting.demographic_no='"+demographicNo+"' "; //group by mdsMSH.segmentID";
	            }
        	}


            logger.debug(sql);
            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                LabResultData lbData = new LabResultData(LabResultData.CML);

                lbData.labType = LabResultData.CML;

                lbData.segmentID = oscar.Misc.getString(rs, "id");

                if (demographicNo == null && !providerNo.equals("0")) {
                	try {
						lbData.acknowledgedStatus = oscar.Misc.getString(rs, "status");
					}catch(SQLException e) {
						//the load by labNo version doesn't care about inbox status.
						lbData.acknowledgedStatus ="U";
					}
                } else {
                    lbData.acknowledgedStatus ="U";
                }


                lbData.healthNumber = oscar.Misc.getString(rs, "patient_health_num");
                lbData.patientName = oscar.Misc.getString(rs, "patientName");
                lbData.sex = oscar.Misc.getString(rs, "patient_sex");


                lbData.resultStatus = "0"; //TODO
                // solve lbData.resultStatus.add(oscar.Misc.getString(rs,"abnormalFlag"));

                lbData.dateTime = oscar.Misc.getString(rs, "collection_date");
                //lbData.setDateObj( UtilDateUtilities.getDateFromString(lbData.dateTime, "dd-MMM-yy") );
                lbData.setDateObj( UtilDateUtilities.getDateFromString(lbData.dateTime, "yyyy-MM-dd") );

                //priority
                lbData.priority = "----";

                lbData.requestingClient = oscar.Misc.getString(rs, "doc_name");
                lbData.reportStatus =  oscar.Misc.getString(rs, "lab_status");
                lbData.accessionNumber = oscar.Misc.getString(rs, "accession_num");

                if (lbData.reportStatus != null && lbData.reportStatus.equals("F")){
                    lbData.finalRes = true;
                }else{
                    lbData.finalRes = false;
                }

                //if ( oscar.Misc.getString(rs,"reportGroupDesc").startsWith("MICRO") ) {
                //   discipline.add("Microbiology");
                //} else if ( oscar.Misc.getString(rs,"reportGroupDesc").startsWith("DIAGNOSTIC IMAGING") ) {
                //   discipline.add("Diagnostic Imaging");
                //} else {
                lbData.discipline = "Hem/Chem/Other";
                //}

                labResults.add(lbData);
            }
            rs.close();
        }catch(Exception e){
            logger.error("exception in CMLPopulate", e);

        }

        return labResults;
    }

    public int findCMLAdnormalResults(String labId){
        int count = 0;
        try {


            String sql = "select id from labTestResults where abn = 'A' and labPatientPhysicianInfo_id = '"+labId+"'";

            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                count++;
            }
            rs.close();
        }catch(Exception e){
            logger.error("exception in MDSResultsData", e);
        }
        return count;
    }

    public void populateMDSResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status) {

        if ( providerNo == null) { providerNo = ""; }
        if ( patientFirstName == null) { patientFirstName = ""; }
        if ( patientLastName == null) { patientLastName = ""; }
        if ( patientHealthNumber == null) { patientHealthNumber = ""; }
        if ( status == null ) { status = ""; }

        segmentID = new ArrayList<String>();
        acknowledgedStatus = new ArrayList<String>();

        healthNumber = new ArrayList<String>();
        patientName = new ArrayList<String>();
        sex = new ArrayList<String>();
        resultStatus = new ArrayList<String>();
        dateTime  = new ArrayList<String>();
        priority = new ArrayList<String>();
        requestingClient = new ArrayList<String>();
        discipline = new ArrayList<String>();
        reportStatus = new ArrayList<String>();

        String sql = "";

        try {

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
                        "AND providerLabRouting.status like '%"+status+"%' AND providerLabRouting.provider_no like '"+(providerNo.equals("")?"%":providerNo)+"' " +
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

            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                segmentID.add(Integer.toString(rs.getInt("segmentID")));
                if (demographicNo == null && !providerNo.equals("0")) {
                    acknowledgedStatus.add(oscar.Misc.getString(rs, "status"));
                } else {
                    acknowledgedStatus.add("U");
                }

                healthNumber.add(oscar.Misc.getString(rs, "healthNumber"));
                patientName.add(beautifyName(oscar.Misc.getString(rs, "patientName")));
                sex.add(oscar.Misc.getString(rs, "sex"));
                resultStatus.add(oscar.Misc.getString(rs, "abnormalFlag"));
                dateTime.add(oscar.Misc.getString(rs, "dateTime"));

                switch ( oscar.Misc.getString(rs, "quantityTiming").charAt(0) ) {
                    case 'C' : priority.add("Critical"); break;
                    case 'S' : priority.add("Stat\\Urgent"); break;
                    case 'U' : priority.add("Unclaimed"); break;
                    case 'A' : if ( oscar.Misc.getString(rs, "quantityTiming").startsWith("AL") ) {
                        priority.add("Alert");
                    } else {
                        priority.add("ASAP");
                    }
                    break;
                    default: priority.add("Routine"); break;
                }

                requestingClient.add(ProviderData.beautifyProviderName(oscar.Misc.getString(rs, "refDoctor")));
                reportStatus.add(oscar.Misc.getString(rs, "reportFormStatus"));

                if ( oscar.Misc.getString(rs, "reportGroupDesc").startsWith("MICRO") ) {
                    discipline.add("Microbiology");
                } else if ( oscar.Misc.getString(rs, "reportGroupDesc").startsWith("DIAGNOSTIC IMAGING") ) {
                    discipline.add("Diagnostic Imaging");
                } else {
                    discipline.add("Hem/Chem/Other");
                }
            }
            rs.close();
        }catch(Exception e){
            logger.error("exception in MDSResultsData", e);
        }
    }

    public ArrayList<LabResultData> populateMDSResultsData2(String demographicNo, String consultationId, boolean attached) {
        String sql = "SELECT patientLabRouting.id, mdsMSH.segmentID, mdsMSH.dateTime, mdsZRG.reportGroupDesc " +
                "FROM " +
                "patientLabRouting "+
                "LEFT JOIN mdsMSH on patientLabRouting.lab_no = mdsMSH.segmentID "+
                "LEFT JOIN mdsZRG on patientLabRouting.lab_no = mdsZRG.segmentID "+
                "WHERE patientLabRouting.lab_type = 'MDS' " +
                "AND patientLabRouting.demographic_no=" + demographicNo;

        String attachQuery = "SELECT document_no FROM consultdocs, patientLabRouting WHERE patientLabRouting.id = consultdocs.document_no AND " +
                "consultdocs.requestId = " + consultationId + " AND consultdocs.doctype = 'L' AND consultdocs.deleted IS NULL";

        labResults = new ArrayList<LabResultData>();
        ArrayList<LabResultData> attachedLabs = new ArrayList<LabResultData>();

        try {


            ResultSet rs = DBHandler.GetSQL(attachQuery);
            while(rs.next()) {
                LabResultData lbData = new LabResultData(LabResultData.EXCELLERIS);
                lbData.labPatientId = oscar.Misc.getString(rs, "document_no");
                attachedLabs.add(lbData);
            }
            rs.close();

            LabResultData lData = new LabResultData(LabResultData.MDS);
            LabResultData.CompareId c = lData.getComparatorId();
            rs = DBHandler.GetSQL(sql);

            while(rs.next()) {
                lData = new LabResultData(LabResultData.MDS);
                lData.segmentID = Integer.toString(rs.getInt("segmentID"));
                lData.labPatientId = oscar.Misc.getString(rs, "id");

                lData.dateTime = oscar.Misc.getString(rs, "dateTime");
                lData.setDateObj(UtilDateUtilities.getDateFromString(lData.dateTime, "yyyy-MM-dd HH:mm:ss"));

                String reportGroupDesc = oscar.Misc.getString(rs, "reportGroupDesc");
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
        }catch(Exception e){
            logger.error("exception in MDSResultsData", e);
        }
        return labResults;
    }
    //////

    public ArrayList<LabResultData> populateMDSResultsData2(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, Integer labNo) {

        if ( providerNo == null) { providerNo = ""; }
        if ( patientFirstName == null) { patientFirstName = ""; }
        if ( patientLastName == null) { patientLastName = ""; }
        if ( patientHealthNumber == null) { patientHealthNumber = ""; }
        if ( status == null ) { status = ""; }

        labResults = new ArrayList<LabResultData>();
        String sql = "";
        String seqId = null;  //for debugging purposes

        try {

        	if(labNo != null && labNo.intValue()>0) {
        		//i know this SQL is kinda dumb, but really, I'm not messing with all the joins..just adding a condition on the primaryKey of mdsMSH
        		sql = "SELECT mdsMSH.segmentID, mdsMSH.messageConID AS accessionNum, mdsPID.patientName, mdsPID.healthNumber, " +
                        "mdsPID.sex, max(mdsZFR.abnormalFlag) as abnormalFlag, mdsMSH.dateTime, mdsOBR.quantityTiming, mdsPV1.refDoctor, " +
                        "min(mdsZFR.reportFormStatus) as reportFormStatus, mdsZRG.reportGroupDesc " +
                        "FROM patientLabRouting "+
                        "LEFT JOIN mdsMSH on patientLabRouting.lab_no = mdsMSH.segmentID "+
                        "LEFT JOIN mdsPID on patientLabRouting.lab_no = mdsPID.segmentID "+
                        "LEFT JOIN mdsPV1 on patientLabRouting.lab_no = mdsPV1.segmentID "+
                        "LEFT JOIN mdsZFR on patientLabRouting.lab_no = mdsZFR.segmentID "+
                        "LEFT JOIN mdsOBR on patientLabRouting.lab_no = mdsOBR.segmentID "+
                        "LEFT JOIN mdsZRG on patientLabRouting.lab_no = mdsZRG.segmentID "+
                        "WHERE " +
                        "patientLabRouting.lab_type = 'MDS' " +
                        "AND patientLabRouting.demographic_no='"+demographicNo+"' and mdsMSH.segmentID="+labNo+" group by mdsMSH.segmentID";
        	} else {

	            if ( demographicNo == null) {
	                // note to self: lab reports not found in the providerLabRouting table will not show up - need to ensure every lab is entered in providerLabRouting, with '0'
	                // for the provider number if unable to find correct provider
	                sql = "SELECT mdsMSH.segmentID, mdsMSH.messageConID AS accessionNum, providerLabRouting.status, mdsPID.patientName, mdsPID.healthNumber, " +
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
	                        "AND providerLabRouting.status like '%"+status+"%' AND providerLabRouting.provider_no like '"+(providerNo.equals("")?"%":providerNo)+"' " +
	                        "AND mdsPID.patientName like '"+patientLastName+"%^"+patientFirstName+"%^%' AND mdsPID.healthNumber like '%"+patientHealthNumber+"%' group by mdsMSH.segmentID";
	            } else {
	                sql = "SELECT mdsMSH.segmentID, mdsMSH.messageConID AS accessionNum, mdsPID.patientName, mdsPID.healthNumber, " +
	                        "mdsPID.sex, max(mdsZFR.abnormalFlag) as abnormalFlag, mdsMSH.dateTime, mdsOBR.quantityTiming, mdsPV1.refDoctor, " +
	                        "min(mdsZFR.reportFormStatus) as reportFormStatus, mdsZRG.reportGroupDesc " +
	                        "FROM patientLabRouting "+
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
        	}
        	
            logger.debug("sql "+sql);
            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                LabResultData lData = new LabResultData(LabResultData.MDS);
                lData.segmentID = Integer.toString(rs.getInt("segmentID"));
                seqId = lData.segmentID;

                if (demographicNo == null && !providerNo.equals("0")) {
                    lData.acknowledgedStatus = oscar.Misc.getString(rs, "status");
                } else {
                    lData.acknowledgedStatus = "U";
                }

                lData.healthNumber = oscar.Misc.getString(rs, "healthNumber");
                lData.patientName = beautifyName(oscar.Misc.getString(rs, "patientName"));
                lData.sex = oscar.Misc.getString(rs, "sex");
                lData.resultStatus = oscar.Misc.getString(rs, "abnormalFlag");
                if(lData.resultStatus == null){
                    lData.resultStatus = "0";
                }
                lData.dateTime = oscar.Misc.getString(rs, "dateTime");
                lData.setDateObj(UtilDateUtilities.getDateFromString(lData.dateTime, "yyyy-MM-dd HH:mm:ss"));

                String quantityTimimg = oscar.Misc.getString(rs, "quantityTiming");
                if(quantityTimimg != null){
                    switch ( quantityTimimg.charAt(0) ) {
                        case 'C' : lData.priority = "Critical"; break;
                        case 'S' : lData.priority = "Stat/Urgent"; break;
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

                lData.requestingClient = ProviderData.beautifyProviderName(oscar.Misc.getString(rs, "refDoctor"));
                lData.reportStatus = oscar.Misc.getString(rs, "reportFormStatus");

                if (lData.reportStatus != null && lData.reportStatus.equals("0")){
                    lData.finalRes = false;
                }else{
                    lData.finalRes = true;
                }


                if (  !lData.resultStatus.equals("0") ){
                    lData.abn = true;
                }
                String reportGroupDesc = oscar.Misc.getString(rs, "reportGroupDesc");

                if ( reportGroupDesc != null && reportGroupDesc.startsWith("MICRO") ) {
                    lData.discipline = "Microbiology";
                } else if ( reportGroupDesc != null && reportGroupDesc.startsWith("DIAGNOSTIC IMAGING") ) {
                    lData.discipline = "Diagnostic Imaging";
                } else {
                    lData.discipline = "Hem/Chem/Other";
                }

                //lData.accessionNumber = findMDSAccessionNumber(lData.segmentID);
                String accessionNum = oscar.Misc.getString(rs, "accessionNum");
                lData.accessionNumber = justGetAccessionNumber(accessionNum);

                // must reverse the order of the labs based on the final result count when
                // being sorted so subtract from a large integer
                lData.finalResultsCount = 100 - Integer.parseInt(accessionNum.substring(accessionNum.lastIndexOf("-")));
                //lData.multiLabId = getMatchingLabs(lData.segmentID);
                labResults.add(lData);
            }
            rs.close();
        }catch(Exception e){
            logger.error("Error processing MDS lab, segment # "+seqId);
            logger.error("exception in MDSResultsData", e);

        }
        return labResults;
    }
    //////

    public String justGetAccessionNumber(String s){
        String[] ss = s.split("-");
        if( ss.length > 1 )
        	return ss[1];
        else
        	return ss[0];
    }

    public String findMDSAccessionNumber(String labId){
        String  ret = "";
        try {

            String sql ="select messageConID from mdsMSH where segmentID = '"+labId+"'";
            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                ret = justGetAccessionNumber(oscar.Misc.getString(rs, "messageConID"));
            }
            rs.close();
        }catch(Exception e){
            logger.error("exception in MDSResultsData", e);
        }
        return ret;
    }

    public String findCMLAccessionNumber(String labId){
        String  ret = "";
        try {

            String sql ="select accession_num from labPatientPhysicianInfo where id = '"+labId+"'";
            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                ret = oscar.Misc.getString(rs, "accession_num");
            }
            rs.close();
        }catch(Exception e){
            logger.error("exception in findCMLAccessionNumber", e);
        }
        return ret;
    }

    public String getMatchingCMLLabs(String labId){
        String  ret = "";
        //String accessionNum = findCMLAccessionNumber(labId);
        int monthsBetween = 0;
        try {

            //String sql = "select filler_order_number from hl7_orc orc, hl7_pid pid where orc.pid_id = pid.pid_id and pid.message_id = '"+labId+"'";
            String sql = "SELECT DISTINCT lpp.id, lpp.service_date, lpp2.service_date as labDate from labPatientPhysicianInfo lpp, labPatientPhysicianInfo lpp2, labReportInformation tr where lpp.accession_num = lpp2.accession_num and lpp2.id='"+labId+"' and tr.id=lpp.labReportInfo_id order by tr.print_date, tr.print_time";
            ResultSet rs = DBHandler.GetSQL(sql);

            while (rs.next()){
                Date dateA = UtilDateUtilities.StringToDate(oscar.Misc.getString(rs, "service_date"), "yyyyMMdd");
                Date dateB = UtilDateUtilities.StringToDate(oscar.Misc.getString(rs, "labDate"), "yyyyMMdd");
                if (dateA.before(dateB)){
                    monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
                }else{
                    monthsBetween = UtilDateUtilities.getNumMonths(dateB, dateA);
                }

                if (monthsBetween < 4){

                        if (ret.equals(""))
                            ret = oscar.Misc.getString(rs, "id");
                        else
                            ret = ret+","+oscar.Misc.getString(rs, "id");

                }
            }
            rs.close();

        }catch(Exception e){
            logger.error("exception in getMatchingCMLLabs",e);
            return labId;
        }
        return ret;
    }

    public String getMatchingLabs(String labId){
        String  ret = "";
        String accessionNum = findMDSAccessionNumber(labId);
        int monthsBetween = 0;

        try {

            String sql ="select a.segmentID, a.messageConID, a.dateTime, b.dateTime as labDate from mdsMSH a, mdsMSH b where a.messageConID like '%"+accessionNum+"%' and b.segmentID='"+labId+"' order by a.messageConID";
            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){

                //MDS labs recycle accessoin numbers every two years, accession
                //numbers for a lab should have lab dates within a year of eachother
                //even this is a large timespan
                Date dateA = UtilDateUtilities.StringToDate(oscar.Misc.getString(rs, "dateTime"), "yyyy-MM-dd hh:mm:ss");
                Date dateB = UtilDateUtilities.StringToDate(oscar.Misc.getString(rs, "labDate"), "yyyy-MM-dd hh:mm:ss");
                if (dateA.before(dateB)){
                    monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
                }else{
                    monthsBetween = UtilDateUtilities.getNumMonths(dateB, dateA);
                }

                if (monthsBetween < 4){
                    if (ret.equals(""))
                        ret = oscar.Misc.getString(rs, "segmentID");
                    else
                        ret = ret+","+oscar.Misc.getString(rs, "segmentID");
                }
            }
            rs.close();

        }catch(Exception e){
            logger.error("exception in MDSResultsData", e);
            return labId;
        }
        return ret;
    }

    private String beautifyName(String name) {
        try {
            return name.substring(0, name.indexOf("^")) + ", "
                    + name.substring(name.indexOf("^") + 1).replace('^', ' ');
        } catch (IndexOutOfBoundsException e) {
            return name.replace('^', ' ');
        }
    }


    public static boolean updateReportStatus(Properties props, int labNo, int providerNo, char status, String comment) {

        try {
            DBPreparedHandler db = new DBPreparedHandler();
            // handles the case where this provider/lab combination is not already in providerLabRouting table
            String sql = "insert ignore into providerLabRouting (provider_no, lab_no, status, comment) values ('"+providerNo+"', '"+labNo+"', '"+status+"', ?)";
            if ( db.queryExecuteUpdate(sql, new String[] { comment }) == 0 ) {
                // handles the case where it is
                sql = "update providerLabRouting set status='"+status+"', comment=? where provider_no='"+providerNo+"' and lab_no='"+labNo+"'";
                db.queryExecute(sql, new String[] { comment });
            } else {
                sql = "delete from providerLabRouting where provider_no='0' and lab_no=?";
                db.queryExecute(sql, new String[] { Integer.toString(labNo) });
            }
            return true;
        }catch(Exception e){
            Logger l = Logger.getLogger(MDSResultsData.class);
            l.error("exception in MDSResultsData.updateReportStatus()", e);

            return false;
        }
    }

    public static boolean updateLabRouting(String[] flaggedLabs, String selectedProviders) {
        boolean result;

        try {


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
            String sql = "delete from providerLabRouting where provider_no='0' and lab_no in ("+deleteString+")";
            result = DBHandler.RunSQL(sql);

            // add new entries
            sql = "insert ignore into providerLabRouting (provider_no, lab_no, status) values "+insertString;
            result = DBHandler.RunSQL(sql);
            return result;
        }catch(Exception e){
            Logger l = Logger.getLogger(MDSResultsData.class);
            l.error("exception in MDSResultsData.updateLabRouting()", e);
            return false;
        }
    }

    public static String searchPatient(String labNo) {
        try {


            String sql = "select demographic_no from patientLabRouting where lab_no='"+labNo+"'";
            ResultSet rs = DBHandler.GetSQL(sql);
            rs.next();
            return oscar.Misc.getString(rs, "demographic_no");
        }catch(Exception e){
            Logger l = Logger.getLogger(MDSResultsData.class);
            l.error("exception in MDSResultsData.searchPatient()", e);
            return "0";
        }
    }

    public static boolean updatePatientLabRouting(String labNo, String demographicNo) {
        boolean result;

        try {


            // delete old entries
            String sql = "delete from patientLabRouting where lab_no='"+labNo+"'";
            result = DBHandler.RunSQL(sql);

            // add new entries
            sql = "insert into patientLabRouting (lab_no, demographic_no,created) values ('"+labNo+"', '"+demographicNo+"',now())";
            result = DBHandler.RunSQL(sql);
            return result;
        }catch(Exception e){
            Logger l = Logger.getLogger(MDSResultsData.class);
            l.error("exception in MDSResultsData.updateLabRouting()", e);
            return false;
        }
    }

}
