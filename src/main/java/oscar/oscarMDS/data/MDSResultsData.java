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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.ConsultDocsDao;
import org.oscarehr.common.dao.LabPatientPhysicianInfoDao;
import org.oscarehr.common.dao.LabTestResultsDao;
import org.oscarehr.common.dao.MdsMSHDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.dao.ProviderLabRoutingDao;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.common.model.LabPatientPhysicianInfo;
import org.oscarehr.common.model.MdsMSH;
import org.oscarehr.common.model.MdsZRG;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.common.model.ProviderLabRoutingModel;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.ConversionUtils;
import oscar.util.UtilDateUtilities;


public class MDSResultsData {
	
    static Logger logger = Logger.getLogger(MDSResultsData.class);
    
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
    	LabPatientPhysicianInfoDao dao = SpringUtils.getBean(LabPatientPhysicianInfoDao.class);
    	ConsultDocsDao consDao = SpringUtils.getBean(ConsultDocsDao.class);
    	
        labResults = new ArrayList<LabResultData>();
        ArrayList<LabResultData> attachedLabs = new ArrayList<LabResultData>();

        try {
        	for(Object[] co : consDao.findByConsultationIdAndType(ConversionUtils.fromIntString(consultationId), "L")) {
            	ConsultDocs cd = (ConsultDocs) co[0];
            	
                LabResultData lbData = new LabResultData(LabResultData.CML);
                lbData.labType = LabResultData.CML;
                lbData.labPatientId = "" + cd.getDocumentNo();
                attachedLabs.add(lbData);
            }
            
            LabResultData lbData = new LabResultData(LabResultData.CML);
            LabResultData.CompareId c = lbData.getComparatorId();
        	for(Object[] o : dao.findRoutings(ConversionUtils.fromIntString(demographicNo), "CML")) {
        		LabPatientPhysicianInfo lpp = (LabPatientPhysicianInfo) o[0];
        		PatientLabRouting r = (PatientLabRouting) o[1];

                lbData.labType = LabResultData.CML;
                lbData.labPatientId = "" + r.getId();
                lbData.segmentID = "" + lpp.getId();
                lbData.dateTime = lpp.getCollectionDate();
                lbData.setDateObj( UtilDateUtilities.getDateFromString(lbData.dateTime, "dd-MMM-yy") );

                if( attached && Collections.binarySearch(attachedLabs, lbData, c) >= 0 )
                    labResults.add(lbData);
                else if( !attached && Collections.binarySearch(attachedLabs, lbData, c) < 0 )
                    labResults.add(lbData);

                lbData = new LabResultData(LabResultData.CML);
            }
        }catch(Exception e){
            logger.error("exception in CMLPopulate", e);
        }

        return labResults;
    }


 public ArrayList<LabResultData> populateEpsilonResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status) {
	 return populateLabResultsData("Epsilon", providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);
 }
 
 private ArrayList<LabResultData> populateLabResultsData(String labName, String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status) {
        //logger.info("populateCMLResultsData getting called now");
        if ( providerNo == null) { providerNo = ""; }
        if ( patientFirstName == null) { patientFirstName = ""; }
        if ( patientLastName == null) { patientLastName = ""; }
        if ( patientHealthNumber == null) { patientHealthNumber = ""; }
        if ( status == null ) { status = ""; }


        labResults =  new ArrayList<LabResultData>();
        try {
        	LabPatientPhysicianInfoDao dao = SpringUtils.getBean(LabPatientPhysicianInfoDao.class);
        	
        	List<Object[]> infos;
            if ( demographicNo == null) {
            	infos = dao.findByPatientName(status, labName, providerNo, patientLastName, patientFirstName, patientHealthNumber);
            } else {
            	infos = dao.findByDemographic(ConversionUtils.fromIntString(demographicNo), labName);
            }
            
            for(Object [] o : infos) {
            	LabPatientPhysicianInfo lpp = (LabPatientPhysicianInfo) o[0];
            	
                LabResultData lbData = new LabResultData(LabResultData.CML);
                lbData.labType = LabResultData.CML;
                lbData.segmentID = "" + lpp.getId();

                if (demographicNo == null && !providerNo.equals("0")) {
                	ProviderLabRoutingModel m = (ProviderLabRoutingModel) o[1]; 
                    lbData.acknowledgedStatus = m.getStatus();
                } else {
                    lbData.acknowledgedStatus ="U";
                }

                lbData.healthNumber = lpp.getPatientHin();
                lbData.patientName = lpp.getPatientFullName();
                lbData.sex = lpp.getPatientSex();
                lbData.resultStatus = "0"; //TODO
                // solve lbData.resultStatus.add(db.getString(rs,"abnormalFlag"));

                lbData.dateTime = lpp.getCollectionDate();
                lbData.setDateObj( UtilDateUtilities.getDateFromString(lbData.dateTime, "dd-MMM-yy") );

                //priority
                lbData.priority = "----";

                lbData.requestingClient = lpp.getDocName();
                lbData.reportStatus =  lpp.getLabStatus();
                lbData.accessionNumber = lpp.getAccessionNum();

                if (lbData.reportStatus != null && lbData.reportStatus.equals("F")){
                    lbData.finalRes = true;
                } else {
                	lbData.finalRes = false;
                }
                lbData.discipline = "Hem/Chem/Other";
                labResults.add(lbData);
            }
        }catch(Exception e){
            logger.error("exception in CMLPopulate", e);
        }
        
        return labResults;
 	}

    public ArrayList<LabResultData> populateCMLResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status) {
        return populateLabResultsData("CML", providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);
    }

    public int findCMLAdnormalResults(String labId){
        LabTestResultsDao dao = SpringUtils.getBean(LabTestResultsDao.class);
        return dao.findByAbnAndPhysicianId("A", ConversionUtils.fromIntString(labId)).size();
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

        try {

        	ProviderLabRoutingDao dao = SpringUtils.getBean(ProviderLabRoutingDao.class);        	
        	List<Object[]> searchResult;
            if (demographicNo == null) {
            	searchResult = dao.findMdsResultResultDataByManyThings(status, providerNo, patientLastName, patientFirstName, patientHealthNumber);
            } else {
            	searchResult = dao.findMdsResultResultDataByDemoId(demographicNo);
            }
            
            for(Object[] o : searchResult) {
            	Integer MSHsegmentID = (Integer) o[0];
            	String providerLabRoutingStatus = (String) o[2];
            	String mdsPIDpatientName = (String) o[3]; 
            	String mdsPIDhealthNumber = (String) o[4];
            	String mdsPIDsex = (String) o[5];
            	String abnormalFlag = (String) o[6]; // maxMdsZFR.abnormalFlag
            	Date mdsMSHdateTime = (Date) o[7];
                String mdsOBRquantityTiming = (String) o[8];
                String mdsPV1refDoctor = (String) o[9];
                String reportFormStatus = (String) o[10]; // minMdsZFR.reportFormStatus
                String mdsZRGreportGroupDesc = (String) o[11]; 
            	
                segmentID.add(Integer.toString(MSHsegmentID));
                if (demographicNo == null && !providerNo.equals("0")) {
                    acknowledgedStatus.add(providerLabRoutingStatus);
                } else {
                    acknowledgedStatus.add("U");
                }

                healthNumber.add(mdsPIDhealthNumber);
                patientName.add(beautifyName(mdsPIDpatientName));
                sex.add(mdsPIDsex);
                resultStatus.add(abnormalFlag);
                dateTime.add(ConversionUtils.toDateString(mdsMSHdateTime));

                switch ( mdsOBRquantityTiming.charAt(0) ) {
                    case 'C' : priority.add("Critical"); break;
                    case 'S' : priority.add("Stat\\Urgent"); break;
                    case 'U' : priority.add("Unclaimed"); break;
                    case 'A' : if ( mdsOBRquantityTiming.startsWith("AL") ) {
                        priority.add("Alert");
                    } else {
                        priority.add("ASAP");
                    }
                    break;
                    default: priority.add("Routine"); break;
                }

                requestingClient.add(ProviderData.beautifyProviderName(mdsPV1refDoctor));
                reportStatus.add(reportFormStatus);

                if ( mdsZRGreportGroupDesc.startsWith("MICRO") ) {
                    discipline.add("Microbiology");
                } else if ( mdsZRGreportGroupDesc.startsWith("DIAGNOSTIC IMAGING") ) {
                    discipline.add("Diagnostic Imaging");
                } else {
                    discipline.add("Hem/Chem/Other");
                }
            }
        }catch(Exception e){
            logger.error("exception in MDSResultsData", e);
        }
    }

    public ArrayList<LabResultData> populateMDSResultsData2(String demographicNo, String consultationId, boolean attached) {
        labResults = new ArrayList<LabResultData>();
        ArrayList<LabResultData> attachedLabs = new ArrayList<LabResultData>();
        try {
        	ConsultDocsDao cdDao = SpringUtils.getBean(ConsultDocsDao.class);
            for(Object[] o : cdDao.findByConsultationIdAndType(ConversionUtils.fromIntString(demographicNo), "L")) {
            	ConsultDocs cd = (ConsultDocs) o[0];
                LabResultData lbData = new LabResultData(LabResultData.EXCELLERIS);
                lbData.labPatientId = "" + cd.getDocumentNo();
                attachedLabs.add(lbData);
            }

            LabResultData lData = new LabResultData(LabResultData.MDS);
            LabResultData.CompareId c = lData.getComparatorId();
            
            PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class);
            for(Object[] o : dao.findResultsByDemographicAndLabType(ConversionUtils.fromIntString(demographicNo), "MDS")) {
            	PatientLabRouting p = (PatientLabRouting) o[0];
            	MdsMSH mdsMSH = (MdsMSH) o[1];
            	MdsZRG mdsZRG = (MdsZRG) o[2];
            	
                lData = new LabResultData(LabResultData.MDS);
                lData.segmentID = "" + mdsMSH.getId();
                lData.labPatientId = "" + p.getId();
                lData.dateTime = ConversionUtils.toTimestampString(mdsMSH.getDateTime());
                lData.setDateObj(mdsMSH.getDateTime());

                String reportGroupDesc = mdsZRG.getReportGroupsDesc();
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
        }catch(Exception e){
            logger.error("exception in MDSResultsData", e);
        }
        return labResults;
    }

    public ArrayList<LabResultData> populateMDSResultsData2(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status) {

        if ( providerNo == null) { providerNo = ""; }
        if ( patientFirstName == null) { patientFirstName = ""; }
        if ( patientLastName == null) { patientLastName = ""; }
        if ( patientHealthNumber == null) { patientHealthNumber = ""; }
        if ( status == null ) { status = ""; }

        labResults = new ArrayList<LabResultData>();
        String seqId = null;  //for debugging purposes

        try {
        	ProviderLabRoutingDao dao = SpringUtils.getBean(ProviderLabRoutingDao.class);        	
        	List<Object[]> searchResult;
            if (demographicNo == null) {
            	searchResult = dao.findMdsResultResultDataByManyThings(status, providerNo, patientLastName, patientFirstName, patientHealthNumber);
            } else {
            	searchResult = dao.findMdsResultResultDataByDemoId(demographicNo);
            }
            
            for(Object[] o : searchResult) {
            	Integer MSHsegmentID = (Integer) o[0]; 
            	String accessionNum = (String) o[1]; // mdsMSH.messageConID 
            	String providerLabRoutingStatus = (String) o[2];
            	String mdsPIDpatientName = (String) o[3]; 
            	String mdsPIDhealthNumber = (String) o[4];
            	String mdsPIDsex = (String) o[5];
            	String abnormalFlag = (String) o[6]; // maxMdsZFR.abnormalFlag
            	Date mdsMSHdateTime = (Date) o[7];
                String mdsOBRquantityTiming = (String) o[8];
                String mdsPV1refDoctor = (String) o[9];
                String reportFormStatus = (String) o[10]; // minMdsZFR.reportFormStatus
                String mdsZRGreportGroupDesc = (String) o[11]; 
            	
                LabResultData lData = new LabResultData(LabResultData.MDS);
                lData.segmentID = "" + MSHsegmentID;
                seqId = lData.segmentID;

                if (demographicNo == null && !providerNo.equals("0")) {
                    lData.acknowledgedStatus = providerLabRoutingStatus;
                } else {
                    lData.acknowledgedStatus = "U";
                }

                lData.healthNumber = mdsPIDhealthNumber;
                lData.patientName = beautifyName(mdsPIDpatientName);
                lData.sex = mdsPIDsex;
                lData.resultStatus = abnormalFlag;
                if(lData.resultStatus == null){
                    lData.resultStatus = "0";
                }
                lData.dateTime = ConversionUtils.toDateString(mdsMSHdateTime);
                lData.setDateObj(UtilDateUtilities.getDateFromString(lData.dateTime, "yyyy-MM-dd HH:mm:ss"));

                String quantityTimimg = mdsOBRquantityTiming;
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

                lData.requestingClient = ProviderData.beautifyProviderName(mdsPV1refDoctor);
                lData.reportStatus = reportFormStatus;

                if (lData.reportStatus != null && lData.reportStatus.equals("0")){
                    lData.finalRes = false;
                }else{
                    lData.finalRes = true;
                }


                if (  !lData.resultStatus.equals("0") ){
                    lData.abn = true;
                }
                String reportGroupDesc = mdsZRGreportGroupDesc;

                if ( reportGroupDesc != null && reportGroupDesc.startsWith("MICRO") ) {
                    lData.discipline = "Microbiology";
                } else if ( reportGroupDesc != null && reportGroupDesc.startsWith("DIAGNOSTIC IMAGING") ) {
                    lData.discipline = "Diagnostic Imaging";
                } else {
                    lData.discipline = "Hem/Chem/Other";
                }

                lData.accessionNumber = justGetAccessionNumber(accessionNum);

                // must reverse the order of the labs based on the final result count when
                // being sorted so subtract from a large integer
                lData.finalResultsCount = 100 - Integer.parseInt(accessionNum.substring(accessionNum.lastIndexOf("-")));
                //lData.multiLabId = getMatchingLabs(lData.segmentID);
                labResults.add(lData);
            }
        }catch(Exception e){
            logger.error("Error processing MDS lab, segment # "+seqId);
            logger.error("exception in MDSResultsData", e);

        }
        return labResults;
    }
    //////

    public String justGetAccessionNumber(String s){
        String[] ss = s.split("-");
        return ss[1];
    }

    public String findMDSAccessionNumber(String labId){
        MdsMSHDao dao = SpringUtils.getBean(MdsMSHDao.class);
        MdsMSH m = dao.find(ConversionUtils.fromIntString(labId));
        if (m != null) {
        	return justGetAccessionNumber(m.getControlId());
        }
        return "";
    }

    public String findCMLAccessionNumber(String labId){
        LabPatientPhysicianInfoDao dao = SpringUtils.getBean(LabPatientPhysicianInfoDao.class);
        LabPatientPhysicianInfo info = dao.find(ConversionUtils.fromIntString(labId));
        if (info != null) {
        	return info.getAccessionNum();
        }
        return "";
    }

    public String getMatchingCMLLabs(String labId){
        String  ret = "";
        int monthsBetween = 0;
        try {
        	LabPatientPhysicianInfoDao dao = SpringUtils.getBean(LabPatientPhysicianInfoDao.class);

            for(Object[] o : dao.findLabServiceDatesByLabId(ConversionUtils.fromIntString(labId))) {
            	Integer id = (Integer) o[0];
            	Date serviceDate = (Date) o[1];
            	Date labDate = (Date) o[2];
            	
                Date dateA = serviceDate;
                Date dateB = labDate;
                
                if (dateA.before(dateB)){
                    monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
                }else{
                    monthsBetween = UtilDateUtilities.getNumMonths(dateB, dateA);
                }

                if (monthsBetween < 4){
                        if (ret.equals(""))
                            ret = "" + id;
                        else
                            ret = ret + "," + id;
                }
            }

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
            MdsMSHDao dao = SpringUtils.getBean(MdsMSHDao.class);
            for(Object[] o : dao.findLabsByAccessionNumAndId(ConversionUtils.fromIntString(segmentID), "%"+accessionNum+"%")) {
            	MdsMSH a = (MdsMSH) o[0];
            	MdsMSH b = (MdsMSH) o[0];
            	
                //MDS labs recycle accessoin numbers every two years, accession
                //numbers for a lab should have lab dates within a year of eachother
                //even this is a large timespan
                Date dateA = a.getDateTime();
                Date dateB = b.getDateTime();
                if (dateA.before(dateB)){
                    monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
                }else{
                    monthsBetween = UtilDateUtilities.getNumMonths(dateB, dateA);
                }

                if (monthsBetween < 4){
                    if (ret.equals(""))
                        ret = "" + a.getId();
                    else
                        ret = ret + "," + a.getId();
                }
            }
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


}
