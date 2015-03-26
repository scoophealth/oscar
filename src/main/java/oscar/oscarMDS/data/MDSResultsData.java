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
import org.oscarehr.common.dao.ConsultResponseDocDao;
import org.oscarehr.common.dao.LabPatientPhysicianInfoDao;
import org.oscarehr.common.dao.LabTestResultsDao;
import org.oscarehr.common.dao.MdsMSHDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.dao.ProviderLabRoutingDao;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.common.model.ConsultResponseDoc;
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
	private ConsultDocsDao consultDocsDao = SpringUtils.getBean(ConsultDocsDao.class);
	private ConsultResponseDocDao consultResponseDocDao = SpringUtils.getBean(ConsultResponseDocDao.class);
	private LabPatientPhysicianInfoDao labPPIDao = SpringUtils.getBean(LabPatientPhysicianInfoDao.class);
	private PatientLabRoutingDao PLRDao = SpringUtils.getBean(PatientLabRoutingDao.class);
	
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
	 *Lists CML labs predicated on relationship to patient's consultation
	 */
	//Consult Request list labs
	public ArrayList<LabResultData> populateCMLResultsData(String demographicNo, String consultationId, boolean attached) {
		List<LabResultData> attachedLabs = new ArrayList<LabResultData>();
		for(Object[] co : consultDocsDao.findLabs(ConversionUtils.fromIntString(consultationId))) {
			ConsultDocs cd = (ConsultDocs) co[0];
			LabResultData lbData = new LabResultData(LabResultData.CML);
			lbData.labType = LabResultData.CML;
			lbData.labPatientId = String.valueOf(cd.getDocumentNo());
			attachedLabs.add(lbData);
		}
		return populateCMLResultsData(demographicNo, attached, attachedLabs);
	}
	
	//Consult Response list labs
	public ArrayList<LabResultData> populateCMLResultsDataConsultResponse(String demographicNo, String consultationId, boolean attached) {
		List<LabResultData> attachedLabs = new ArrayList<LabResultData>();
		for(Object[] co : consultResponseDocDao.findLabs(ConversionUtils.fromIntString(consultationId))) {
			ConsultResponseDoc cd = (ConsultResponseDoc) co[0];
			LabResultData lbData = new LabResultData(LabResultData.CML);
			lbData.labType = LabResultData.CML;
			lbData.labPatientId = String.valueOf(cd.getDocumentNo());
			attachedLabs.add(lbData);
		}
		return populateCMLResultsData(demographicNo, attached, attachedLabs);
	}
	
	
	//Consult List Labs private shared method
	private ArrayList<LabResultData> populateCMLResultsData(String demographicNo, boolean attached, List<LabResultData> attachedLabs) {
		labResults = new ArrayList<LabResultData>();
		try {
			LabResultData lbData = new LabResultData(LabResultData.CML);
			LabResultData.CompareId c = lbData.getComparatorId();
			for(Object[] o : labPPIDao.findRoutings(ConversionUtils.fromIntString(demographicNo), "CML")) {
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
		} catch(Exception e){
			logger.error("exception in CMLPopulate", e);
		}
		return labResults;
	}
	/**
	 * End Lists CML labs related to consultation
	 */
	
	
	public ArrayList<LabResultData> populateEpsilonResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, Integer labNo) {
		return populateLabResultsData("Epsilon", providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status , labNo);
	}

	private ArrayList<LabResultData> populateLabResultsData(String labName, String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, Integer labNo) {
		//logger.info("populateCMLResultsData getting called now");
		if ( providerNo == null) { providerNo = ""; }
		if ( patientFirstName == null) { patientFirstName = ""; }
		if ( patientLastName == null) { patientLastName = ""; }
		if ( patientHealthNumber == null) { patientHealthNumber = ""; }
		if ( status == null ) { status = ""; }


		labResults =  new ArrayList<LabResultData>();
		try {
			List<Object[]> infos = new ArrayList<Object[]>();
			if(labNo != null && labNo.intValue()>0) {
				LabPatientPhysicianInfo lppi = labPPIDao.find(labNo);
				infos.add(new Object[]{lppi});
			} else {
				if ( demographicNo == null) {
					infos = labPPIDao.findByPatientName(status, labName, providerNo, patientLastName, patientFirstName, patientHealthNumber);
				} else {
					infos = labPPIDao.findByDemographic(ConversionUtils.fromIntString(demographicNo), labName);
				}
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

	public ArrayList<LabResultData> populateCMLResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, Integer labNo) {
		return populateLabResultsData("CML", providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, labNo);
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

	/**
	 *Lists MDS labs predicated on relationship to patient's consultation
	 */
	//Consult Request list labs
	public ArrayList<LabResultData> populateMDSResultsData(String demographicNo, String consultationId, boolean attached) {
		List<LabResultData> attachedLabs = new ArrayList<LabResultData>();
		for(Object[] o : consultDocsDao.findLabs(ConversionUtils.fromIntString(consultationId))) {
			ConsultDocs cd = (ConsultDocs) o[0];
			LabResultData lbData = new LabResultData(LabResultData.EXCELLERIS);
			lbData.labPatientId = "" + cd.getDocumentNo();
			attachedLabs.add(lbData);
		}
		List<Object[]> labsMDS = PLRDao.findResultsByDemographicAndLabType(ConversionUtils.fromIntString(demographicNo), "MDS");
		return populateMDSResultsData(attachedLabs, labsMDS, attached);
	}
	
	//Consult Response list labs
	public ArrayList<LabResultData> populateMDSResultsDataConsultResponse(String demographicNo, String consultationId, boolean attached) {
		List<LabResultData> attachedLabs = new ArrayList<LabResultData>();
		for(Object[] o : consultResponseDocDao.findLabs(ConversionUtils.fromIntString(consultationId))) {
			ConsultDocs cd = (ConsultDocs) o[0];
			LabResultData lbData = new LabResultData(LabResultData.EXCELLERIS);
			lbData.labPatientId = "" + cd.getDocumentNo();
			attachedLabs.add(lbData);
		}
		List<Object[]> labsMDS = PLRDao.findResultsByDemographicAndLabType(ConversionUtils.fromIntString(demographicNo), "MDS");
		return populateMDSResultsData(attachedLabs, labsMDS, attached);
	}
	
	//Consult list labs private shared method
	private ArrayList<LabResultData> populateMDSResultsData(List<LabResultData> attachedLabs, List<Object[]> labsMDS, boolean attached) {
		labResults = new ArrayList<LabResultData>();
		try {
			LabResultData lData = new LabResultData(LabResultData.MDS);
			LabResultData.CompareId c = lData.getComparatorId();
			
			for(Object[] o : labsMDS) {
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
	/**
	 * End Lists CML labs related to consultation
	 */
	

	public ArrayList<LabResultData> populateMDSResultsData2(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, Integer labNo) {

		if ( providerNo == null) { providerNo = ""; }
		if ( patientFirstName == null) { patientFirstName = ""; }
		if ( patientLastName == null) { patientLastName = ""; }
		if ( patientHealthNumber == null) { patientHealthNumber = ""; }
		if ( status == null ) { status = ""; }

		labResults = new ArrayList<LabResultData>();
		String seqId = null;  //for debugging purposes

		try {
			ProviderLabRoutingDao dao = SpringUtils.getBean(ProviderLabRoutingDao.class);			
			List<Object[]> searchResult = null;
				
			if(labNo != null && labNo.intValue()>0 && demographicNo != null) {
				searchResult = dao.findMdsResultResultDataByDemographicNoAndLabNo(Integer.parseInt(demographicNo), labNo);
			} else {
				if (demographicNo == null) {
					searchResult = dao.findMdsResultResultDataByManyThings(status, providerNo, patientLastName, patientFirstName, patientHealthNumber);
				} else {
					searchResult = dao.findMdsResultResultDataByDemoId(demographicNo);
				}
			}
			
			Integer MSHsegmentID = null; 
			String accessionNum = null; 
			String providerLabRoutingStatus = null;
			String mdsPIDpatientName = null; 
			String mdsPIDhealthNumber = null;
			String mdsPIDsex = null;
			String abnormalFlag = null; // maxMdsZFR.abnormalFlag
			Date mdsMSHdateTime = null;
			String mdsOBRquantityTiming = null;
			String mdsPV1refDoctor = null;
			String reportFormStatus = null; // minMdsZFR.reportFormStatus
			String mdsZRGreportGroupDesc = null; 
			
			
			for(Object[] o : searchResult) {
				if( demographicNo == null ) {
					MSHsegmentID = (Integer) o[0]; 
					accessionNum = String.valueOf(o[1]); // mdsMSH.messageConID
					providerLabRoutingStatus = String.valueOf(o[2]);
					mdsPIDpatientName = (String) o[3]; 
					mdsPIDhealthNumber = (String) o[4];
					mdsPIDsex = (String) o[5];
					abnormalFlag = (String) o[6]; // maxMdsZFR.abnormalFlag
					mdsMSHdateTime = (Date) o[7];
					mdsOBRquantityTiming = (String) o[8];
					mdsPV1refDoctor = (String) o[9];
					reportFormStatus = (String) o[10]; // minMdsZFR.reportFormStatus
					mdsZRGreportGroupDesc = (String) o[11];
				}
				else {
					MSHsegmentID = (Integer) o[0]; 
					accessionNum = String.valueOf(o[1]); // mdsMSH.messageConID		
					mdsPIDpatientName = (String) o[2]; 
					mdsPIDhealthNumber = (String) o[3];
					mdsPIDsex = (String) o[4];
					abnormalFlag = (String) o[5]; // maxMdsZFR.abnormalFlag
					mdsMSHdateTime = (Date) o[6];
					mdsOBRquantityTiming = (String) o[7];
					mdsPV1refDoctor = (String) o[8];
					reportFormStatus = (String) o[9]; // minMdsZFR.reportFormStatus
					mdsZRGreportGroupDesc = (String) o[10];
				}
				
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
				lData.dateTime = ConversionUtils.toDateString(mdsMSHdateTime,ConversionUtils.DEFAULT_TS_PATTERN);
				
				
				lData.setDateObj(UtilDateUtilities.getDateFromString(lData.dateTime, "yyyy-MM-dd HH:mm:ss"));
				
				if( lData.getDateObj() == null ) {				
					lData.setDateObj(UtilDateUtilities.getDateFromString(lData.dateTime, "yyyy-MM-dd"));
				}
				
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
		if( ss.length > 1 )
			return ss[1];
		else
			return ss[0];
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
		LabPatientPhysicianInfo info = labPPIDao.find(ConversionUtils.fromIntString(labId));
		if (info != null) {
			return info.getAccessionNum();
		}
		return "";
	}

	public String getMatchingCMLLabs(String labId){
		String  ret = "";
		int monthsBetween = 0;
		try {
			for(Object[] o : labPPIDao.findLabServiceDatesByLabId(ConversionUtils.fromIntString(labId))) {
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
