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


package oscar.oscarLab.ca.on;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager;
import org.oscarehr.billing.CA.BC.dao.Hl7MshDao;
import org.oscarehr.caisi_integrator.ws.CachedDemographicLabResult;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.common.dao.CtlDocumentDao;
import org.oscarehr.common.dao.DocumentResultsDao;
import org.oscarehr.common.dao.Hl7TextMessageDao;
import org.oscarehr.common.dao.LabPatientPhysicianInfoDao;
import org.oscarehr.common.dao.MdsMSHDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.dao.ProviderLabRoutingDao;
import org.oscarehr.common.dao.QueueDocumentLinkDao;
import org.oscarehr.common.model.CtlDocument;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderLabRoutingModel;
import org.oscarehr.common.model.QueueDocumentLink;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.labs.LabIdAndType;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import oscar.OscarProperties;
import oscar.oscarDB.ArchiveDeletedRecords;
import oscar.oscarDB.DBPreparedHandler;
import oscar.oscarLab.ca.all.Hl7textResultsData;
import oscar.oscarLab.ca.all.upload.ProviderLabRouting;
import oscar.oscarLab.ca.bc.PathNet.PathnetResultsData;
import oscar.oscarMDS.data.MDSResultsData;
import oscar.oscarMDS.data.ReportStatus;
import oscar.util.ConversionUtils;

public class CommonLabResultData {

	private static Logger logger = MiscUtils.getLogger();

	public static final boolean ATTACHED = true;
	public static final boolean UNATTACHED = false;

	public static final String NOT_ASSIGNED_PROVIDER_NO = "0";
	
	private static PatientLabRoutingDao patientLabRoutingDao = SpringUtils.getBean(PatientLabRoutingDao.class);
	private static ProviderLabRoutingDao providerLabRoutingDao = SpringUtils.getBean(ProviderLabRoutingDao.class);
	private static QueueDocumentLinkDao queueDocumentLinkDao = SpringUtils.getBean(QueueDocumentLinkDao.class);
	
	
	public CommonLabResultData() {

	}

	public static String[] getLabTypes() {
		return new String[] { "MDS", "CML", "BCP", "HL7", "DOC", "Epsilon" };
	}

	//Populate Lab data for consultation request
	public ArrayList<LabResultData> populateLabResultsData(LoggedInInfo loggedInInfo, String demographicNo, String reqId, boolean attach) {
		return populateLabResultsData(loggedInInfo, demographicNo, reqId, attach, false);
	}
	
	//Populate Lab data for consultation response
	public ArrayList<LabResultData> populateLabResultsDataConsultResponse(LoggedInInfo loggedInInfo, String demographicNo, String respId, boolean attach) {
		return populateLabResultsData(loggedInInfo, demographicNo, respId, attach, true);
	}
	
	//Populate Lab data for consultation (private shared method)
	private ArrayList<LabResultData> populateLabResultsData(LoggedInInfo loggedInInfo, String demographicNo, String consultId, boolean attach, boolean isConsultResponse) {
		ArrayList<LabResultData> labs = new ArrayList<LabResultData>();
		oscar.oscarMDS.data.MDSResultsData mDSData = new oscar.oscarMDS.data.MDSResultsData();

		OscarProperties op = OscarProperties.getInstance();

		String cml = op.getProperty("CML_LABS");
		String mds = op.getProperty("MDS_LABS");
		String pathnet = op.getProperty("PATHNET_LABS");
		String hl7text = op.getProperty("HL7TEXT_LABS");
		String epsilon = op.getProperty("Epsilon_LABS");

		if (cml != null && cml.trim().equals("yes")) {
			ArrayList<LabResultData> cmlLabs = isConsultResponse ?
					mDSData.populateCMLResultsDataConsultResponse(demographicNo, consultId, attach) :
						mDSData.populateCMLResultsData(demographicNo, consultId, attach);
			labs.addAll(cmlLabs);
		}
		if (epsilon != null && epsilon.trim().equals("yes")) {
			ArrayList<LabResultData> cmlLabs = isConsultResponse ?
					mDSData.populateCMLResultsDataConsultResponse(demographicNo, consultId, attach) :
						mDSData.populateCMLResultsData(demographicNo, consultId, attach);
			labs.addAll(cmlLabs);
		}

		if (mds != null && mds.trim().equals("yes")) {
			ArrayList<LabResultData> mdsLabs = isConsultResponse ?
					mDSData.populateMDSResultsDataConsultResponse(demographicNo, consultId, attach) :
						mDSData.populateMDSResultsData(demographicNo, consultId, attach);
			labs.addAll(mdsLabs);
		}
		if (pathnet != null && pathnet.trim().equals("yes")) {
			PathnetResultsData pathData = new PathnetResultsData();
			ArrayList<LabResultData> pathLabs = isConsultResponse ?
					pathData.populatePathnetResultsDataConsultResponse(demographicNo, consultId, attach) :
						pathData.populatePathnetResultsData(demographicNo, consultId, attach);
			labs.addAll(pathLabs);
		}
		if (hl7text != null && hl7text.trim().equals("yes")) {
			ArrayList<LabResultData> hl7Labs = isConsultResponse ?
					Hl7textResultsData.populateHL7ResultsDataConsultResponse(demographicNo, consultId, attach) :
						Hl7textResultsData.populateHL7ResultsData(demographicNo, consultId, attach);
			labs.addAll(hl7Labs);
		}

		return labs;
	}


    public ArrayList<LabResultData> populateLabResultsData(LoggedInInfo loggedInInfo, String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, boolean isPaged, Integer page, Integer pageSize, boolean mixLabsAndDocs, Boolean isAbnormal) {

    		ArrayList<LabResultData> labs = new ArrayList<LabResultData>();
    		oscar.oscarMDS.data.MDSResultsData mDSData = new oscar.oscarMDS.data.MDSResultsData();

    		OscarProperties op = OscarProperties.getInstance();

    		String cml = op.getProperty("CML_LABS");
    		String mds = op.getProperty("MDS_LABS");
    		String pathnet = op.getProperty("PATHNET_LABS");
    		String hl7text = op.getProperty("HL7TEXT_LABS");


    		if(!isPaged && cml != null && cml.trim().equals("yes")){
    			ArrayList<LabResultData> cmlLabs = mDSData.populateCMLResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, null);
    			labs.addAll(cmlLabs);
    		}

    		if (!isPaged && mds != null && mds.trim().equals("yes")){
    			ArrayList<LabResultData> mdsLabs = mDSData.populateMDSResultsData2(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, null);
    			labs.addAll(mdsLabs);

    		}
    		if (!isPaged && pathnet != null && pathnet.trim().equals("yes")){
    			PathnetResultsData pathData = new PathnetResultsData();
    			ArrayList<LabResultData> pathLabs = pathData.populatePathnetResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status,null);
    			labs.addAll(pathLabs);
    		}

    		if (hl7text != null && hl7text.trim().equals("yes")){
    			if (isPaged) {
    		        ArrayList<LabResultData> hl7Labs = Hl7textResultsData.populateHl7ResultsData(providerNo, demographicNo, patientFirstName, patientLastName,
    		        												   patientHealthNumber, status, true, page, pageSize, mixLabsAndDocs, isAbnormal);
    		        labs.addAll(hl7Labs);
                }
                else {
                	ArrayList<LabResultData> hl7Labs = Hl7textResultsData.populateHl7ResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status,null);
    		        labs.addAll(hl7Labs);
                }

    		}
    		return labs;
    }

    public ArrayList<LabResultData> populateLabResultsData(LoggedInInfo loggedInInfo, String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String ackStatus, String docScanStatus, boolean isPaged, Integer page, Integer pageSize, boolean mixLabsAndDocs, Boolean isAbnormal) {
    		return populateLabResultsData(loggedInInfo, providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, ackStatus, isPaged, page, pageSize, mixLabsAndDocs, isAbnormal);
    }

	public ArrayList<LabResultData> populateLabResultsData(LoggedInInfo loggedInInfo, String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status) {
		return populateLabResultsData(loggedInInfo, providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, "I");
	}

	public ArrayList<LabResultData> populateLabResultsDataInboxIndexPage(LoggedInInfo loggedInInfo, String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, String scannedDocStatus) {
		ArrayList<LabResultData> labs = new ArrayList<LabResultData>();
		oscar.oscarMDS.data.MDSResultsData mDSData = new oscar.oscarMDS.data.MDSResultsData();

		OscarProperties op = OscarProperties.getInstance();

		String cml = op.getProperty("CML_LABS");
		String mds = op.getProperty("MDS_LABS");
		String pathnet = op.getProperty("PATHNET_LABS");
		String hl7text = op.getProperty("HL7TEXT_LABS");
		String epsilon = op.getProperty("Epsilon_LABS");

		if (scannedDocStatus != null && (scannedDocStatus.equals("N") || scannedDocStatus.equals("I") || scannedDocStatus.equals(""))) {

			if (epsilon != null && epsilon.trim().equals("yes")) {
				ArrayList<LabResultData> cmlLabs = mDSData.populateEpsilonResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, null);
				labs.addAll(cmlLabs);
			}

			if (cml != null && cml.trim().equals("yes")) {
				ArrayList<LabResultData> cmlLabs = mDSData.populateCMLResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, null);
				labs.addAll(cmlLabs);
			}
			if (mds != null && mds.trim().equals("yes")) {
				ArrayList<LabResultData> mdsLabs = mDSData.populateMDSResultsData2(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, null);
				labs.addAll(mdsLabs);
			}
			if (pathnet != null && pathnet.trim().equals("yes")) {
				PathnetResultsData pathData = new PathnetResultsData();
				ArrayList<LabResultData> pathLabs = pathData.populatePathnetResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status,null);
				labs.addAll(pathLabs);
			}
			if (hl7text != null && hl7text.trim().equals("yes")) {

				ArrayList<LabResultData> hl7Labs = Hl7textResultsData.populateHl7ResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status,null);
				labs.addAll(hl7Labs);
			}
		}

		if (scannedDocStatus != null && (scannedDocStatus.equals("O") || scannedDocStatus.equals("I") || scannedDocStatus.equals(""))) {

			DocumentResultsDao documentResultsDao = (DocumentResultsDao) SpringUtils.getBean("documentResultsDao");
			ArrayList<LabResultData> docs = documentResultsDao.populateDocumentResultsDataOfAllProviders(providerNo, demographicNo, status);
			labs.addAll(docs);
		}

		return labs;
	}

	// get documents that are specific provider to show in that provider's inbox
	public ArrayList<LabResultData> populateLabResultsData2(LoggedInInfo loggedInInfo, String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, String scannedDocStatus) {
		ArrayList<LabResultData> labs = new ArrayList<LabResultData>();
		labs = populateLabsData(loggedInInfo, providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, scannedDocStatus);
		labs.addAll(populateDocumentDataSpecificProvider(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, scannedDocStatus));
		return labs;
	}

	// return documents specific to this provider only, doesn't include documents that are not linked to any provider
	public ArrayList<LabResultData> populateDocumentDataSpecificProvider(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, String scannedDocStatus) {
		ArrayList<LabResultData> labs = new ArrayList<LabResultData>();
		if (scannedDocStatus != null && (scannedDocStatus.equals("O") || scannedDocStatus.equals("I") || scannedDocStatus.equals(""))) {
			DocumentResultsDao documentResultsDao = (DocumentResultsDao) SpringUtils.getBean("documentResultsDao");
			ArrayList<LabResultData> docs = documentResultsDao.populateDocumentResultsDataLinkToProvider(providerNo, demographicNo, status);
			return docs;
		}
		return labs;
	}

	public ArrayList<LabResultData> populateDocumentData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, String scannedDocStatus) {
		ArrayList<LabResultData> labs = new ArrayList<LabResultData>();
		if (scannedDocStatus != null && (scannedDocStatus.equals("O") || scannedDocStatus.equals("I") || scannedDocStatus.equals(""))) {
			DocumentResultsDao documentResultsDao = (DocumentResultsDao) SpringUtils.getBean("documentResultsDao");
			ArrayList<LabResultData> docs = documentResultsDao.populateDocumentResultsData(providerNo, demographicNo, status);
			return docs;
		}
		return labs;
	}

	public ArrayList<LabResultData> populateLabsData(LoggedInInfo loggedInInfo, String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, String scannedDocStatus) {
		ArrayList<LabResultData> result = new ArrayList<LabResultData>();
		List<Integer> ids = new ArrayList<Integer>();
		Integer parentId = ConversionUtils.fromIntString(demographicNo); 
		ids.add(parentId);
		
		DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
		ids.addAll(demographicManager.getMergedDemographicIds(loggedInInfo, parentId));
		
		for(Integer id : ids) { 
			result.addAll(pleaseRefactorMe(providerNo, id.toString(), patientFirstName, patientLastName, patientHealthNumber, status, scannedDocStatus));
		}
		
		return result;
	}

	private ArrayList<LabResultData> pleaseRefactorMe(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, String scannedDocStatus) {
	    ArrayList<LabResultData> labs = new ArrayList<LabResultData>();
		oscar.oscarMDS.data.MDSResultsData mDSData = new oscar.oscarMDS.data.MDSResultsData();

		OscarProperties op = OscarProperties.getInstance();

		String cml = op.getProperty("CML_LABS");
		String mds = op.getProperty("MDS_LABS");
		String pathnet = op.getProperty("PATHNET_LABS");
		String hl7text = op.getProperty("HL7TEXT_LABS");
		String epsilon = op.getProperty("Epsilon_LABS");
		String spire = op.getProperty("Spire_LABS");

		if (scannedDocStatus != null && (scannedDocStatus.equals("N") || scannedDocStatus.equals("I") || scannedDocStatus.equals(""))) {

			if (epsilon != null && epsilon.trim().equals("yes")) {
				ArrayList<LabResultData> cmlLabs = mDSData.populateEpsilonResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, null);
				labs.addAll(cmlLabs);
			}

			if (cml != null && cml.trim().equals("yes")) {
				ArrayList<LabResultData> cmlLabs = mDSData.populateCMLResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, null);
				labs.addAll(cmlLabs);
			}
			if (mds != null && mds.trim().equals("yes")) {
				ArrayList<LabResultData> mdsLabs = mDSData.populateMDSResultsData2(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, null);
				labs.addAll(mdsLabs);
			}
			if (pathnet != null && pathnet.trim().equals("yes")) {
				PathnetResultsData pathData = new PathnetResultsData();
				ArrayList<LabResultData> pathLabs = pathData.populatePathnetResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status,null);
				labs.addAll(pathLabs);
			}
			if (hl7text != null && hl7text.trim().equals("yes")) {

				ArrayList<LabResultData> hl7Labs = Hl7textResultsData.populateHl7ResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status,null);
				labs.addAll(hl7Labs);
			}
			
			if (spire != null && spire.trim().equals("yes")) {
				//SpireResultsData spireData = new SpireResultsData();
				//ArrayList<LabResultData> spireLabs = spireData.populateSpireResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);
				//labs.addAll(spireLabs);
			}
		}
		return labs;
    }

	public ArrayList<LabResultData> populateLabResultsData(LoggedInInfo loggedInInfo, String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status, String scannedDocStatus) {
		ArrayList<LabResultData> labs = new ArrayList<LabResultData>();
		labs = populateLabsData(loggedInInfo, providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, scannedDocStatus);
		labs.addAll(populateDocumentData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status, scannedDocStatus));

		return labs;
	}

	public static boolean updateReportStatus(int labNo, String providerNo, char status, String comment, String labType) {

		try {
			DBPreparedHandler db = new DBPreparedHandler();
			// handles the case where this provider/lab combination is not already in providerLabRouting table
			String sql = "select id, status from providerLabRouting where lab_type = '" + labType + "' and provider_no = '" + providerNo + "' and lab_no = '" + labNo + "'";

			ResultSet rs = db.queryResults(sql);
			boolean empty = true;
			while (rs.next()) {
				empty = false;
				String id = oscar.Misc.getString(rs, "id");
				if (!oscar.Misc.getString(rs, "status").equals("A")) {
					ProviderLabRoutingModel plr  = providerLabRoutingDao.find(Integer.parseInt(id));
					if(plr != null) {
						plr.setStatus(""+status);
						//we don't want to clobber existing comments when filing labs
						if( status != 'F' ) {
							plr.setComment(comment);
						}
						plr.setTimestamp(new Date());
						providerLabRoutingDao.merge(plr);
					}
				}
			} 
			if (empty) {
				ProviderLabRoutingModel p = new ProviderLabRoutingModel();
				p.setProviderNo(providerNo);
				p.setLabNo(labNo);
				p.setStatus(String.valueOf(status));
				p.setComment(comment);
				p.setLabType(labType);
				p.setTimestamp(new Date());
				providerLabRoutingDao.persist(p);
			}

			if (!"0".equals(providerNo)) {
				ProviderLabRoutingDao dao = SpringUtils.getBean(ProviderLabRoutingDao.class);
				List<ProviderLabRoutingModel> modelRecords = dao.findByLabNoAndLabTypeAndProviderNo(labNo, labType, providerNo);
				ArchiveDeletedRecords adr = new ArchiveDeletedRecords();
				adr.recordRowsToBeDeleted(modelRecords, "" + providerNo, "providerLabRouting");
				
				for(ProviderLabRoutingModel plr : providerLabRoutingDao.findByLabNoAndLabTypeAndProviderNo(labNo, labType, "0")) {
					providerLabRoutingDao.remove(plr.getId());
				}
				
			}
			return true;
		} catch (Exception e) {
			Logger l = Logger.getLogger(CommonLabResultData.class);
			l.error("exception in MDSResultsData.updateReportStatus()", e);
			return false;
		} finally {
			DbConnectionFilter.releaseThreadLocalDbConnection();
		}
	}

	public ArrayList<ReportStatus> getStatusArray(String labId, String labType) {
		ArrayList<ReportStatus> statusArray = new ArrayList<ReportStatus>();
		ProviderLabRoutingDao dao = SpringUtils.getBean(ProviderLabRoutingDao.class);
		for(Object[] i : dao.getProviderLabRoutings(ConversionUtils.fromIntString(labId), labType)) {
			Provider p = (Provider) i[0];
			ProviderLabRoutingModel m = (ProviderLabRoutingModel) i[1]; 
			statusArray.add(new ReportStatus(p.getFullName(), 
					p.getProviderNo(), 
					descriptiveStatus(m.getStatus()), 
					m.getComment(), 
					ConversionUtils.toTimestampString(m.getTimestamp()), 
					labId));
		}
		return statusArray;
	}

	public String descriptiveStatus(String status) {
		switch (status.charAt(0)) {
		case 'A':
			return "Acknowledged";
		case 'F':
			return "Filed but not acknowledged";
		case 'U':
			return "N/A";
		default:
			return "Not Acknowledged";
		}
	}

	public static String searchPatient(String labNo, String labType) {
		PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class);
		List<PatientLabRouting> routings = dao.findByLabNoAndLabType(ConversionUtils.fromIntString(labNo), labType);
		if (routings.isEmpty()) {
			return "0";
		}
		
		return routings.get(0).getDemographicNo().toString();
	}

	public static boolean updatePatientLabRouting(String labNo, String demographicNo, String labType) {
		boolean result = false;

		try {

			// update pateintLabRouting for labs with the same accession number
			CommonLabResultData data = new CommonLabResultData();
			String[] labArray = data.getMatchingLabs(labNo, labType).split(",");
			for (int i = 0; i < labArray.length; i++) {

				// delete old entries
				for(PatientLabRouting p:patientLabRoutingDao.findByLabNoAndLabType(Integer.parseInt(labArray[i]),labType)) {
					patientLabRoutingDao.remove(p.getId());
				}
				

				// add new entries
				PatientLabRouting plr = new PatientLabRouting();
				plr.setLabNo(Integer.parseInt(labArray[i]));
				plr.setDemographicNo(Integer.parseInt(demographicNo));
				plr.setLabType(labType);
				patientLabRoutingDao.persist(plr);
				

				// add labs to measurements table
				populateMeasurementsTable(labArray[i], demographicNo, labType);

			}

			return result;

		} catch (Exception e) {
			Logger l = Logger.getLogger(CommonLabResultData.class);
			l.error("exception in CommonLabResultData.updateLabRouting()", e);
			return false;
		}
	}

	public static boolean updateLabRouting(ArrayList<String[]> flaggedLabs, String selectedProviders) {
		boolean result;

		try {

			String[] providersArray = selectedProviders.split(",");

			CommonLabResultData data = new CommonLabResultData();
			ProviderLabRouting plr = new ProviderLabRouting();
			// MiscUtils.getLogger().info(flaggedLabs.size()+"--");
			for (int i = 0; i < flaggedLabs.size(); i++) {
				String[] strarr = flaggedLabs.get(i);
				String lab = strarr[0];
				String labType = strarr[1];

				// Forward all versions of the lab
				String matchingLabs = data.getMatchingLabs(lab, labType);
				String[] labIds = matchingLabs.split(",");
				// MiscUtils.getLogger().info(labIds.length+"labIds --");
				for (int k = 0; k < labIds.length; k++) {

					for (int j = 0; j < providersArray.length; j++) {
						plr.route(labIds[k], providersArray[j], DbConnectionFilter.getThreadLocalDbConnection(), labType);
					}

					// delete old entries
					for(ProviderLabRoutingModel p:providerLabRoutingDao.findByLabNoAndLabTypeAndProviderNo(Integer.parseInt(labIds[k]),labType,"0")) {
						providerLabRoutingDao.remove(p.getId());
					}
					
				}

			}

			return true;
		} catch (Exception e) {
			Logger l = Logger.getLogger(CommonLabResultData.class);
			l.error("exception in CommonLabResultData.updateLabRouting()", e);
			return false;
		}
	}

	// //
	public static boolean fileLabs(ArrayList<String[]> flaggedLabs, String provider) {

		CommonLabResultData data = new CommonLabResultData();

		for (int i = 0; i < flaggedLabs.size(); i++) {
			String[] strarr = flaggedLabs.get(i);
			String lab = strarr[0];
			String labType = strarr[1];
			String labs = data.getMatchingLabs(lab, labType);

			if (labs != null && !labs.equals("")) {
				String[] labArray = labs.split(",");
				for (int j = 0; j < labArray.length; j++) {
					updateReportStatus(Integer.parseInt(labArray[j]), provider, 'F', "", labType);
					removeFromQueue(Integer.parseInt(labArray[j]));
				}

			} else {
				updateReportStatus(Integer.parseInt(lab), provider, 'F', "", labType);
				removeFromQueue(Integer.parseInt(lab));
			}
		}
		return true;
	}
	
	
	private static void removeFromQueue(Integer lab_no) {
		List<QueueDocumentLink> queues = queueDocumentLinkDao.getQueueFromDocument(lab_no);
		
		for( QueueDocumentLink queue : queues ) {
			queueDocumentLinkDao.remove(queue.getId());
		}
	}

	// //

	public String getMatchingLabs(String lab_no, String lab_type) {
		String labs = null;
		if (lab_type.equals(LabResultData.HL7TEXT)) {
			labs = Hl7textResultsData.getMatchingLabs(lab_no);
		} else if (lab_type.equals(LabResultData.MDS)) {
			MDSResultsData data = new MDSResultsData();
			labs = data.getMatchingLabs(lab_no);
		} else if (lab_type.equals((LabResultData.EXCELLERIS))) {
			PathnetResultsData data = new PathnetResultsData();
			labs = data.getMatchingLabs(lab_no);
		} else if (lab_type.equals(LabResultData.CML)) {
			MDSResultsData data = new MDSResultsData();
			labs = data.getMatchingCMLLabs(lab_no);
		} else if (lab_type.equals(LabResultData.DOCUMENT)) {
			labs = lab_no;// one document is only linked to one patient.
		}else if (lab_type.equals(LabResultData.HRM)){
        		labs = lab_no;
        	}

		return labs;
	}

	public String getDemographicNo(String labId, String labType) {
		return searchPatient(labId, labType);
	}

	public boolean isDocLinkedWithPatient(String labId, String labType) {
		CtlDocumentDao dao = SpringUtils.getBean(CtlDocumentDao.class);
		List<CtlDocument> docList = dao.findByDocumentNoAndModule(ConversionUtils.fromIntString(labId), "demographic");
		if (docList.isEmpty()) {
			return false;
		}
		
		String mi = ConversionUtils.toIntString(docList.get(0).getId().getModuleId());
		return mi != null && !mi.trim().equals("-1");		
	}

	public boolean isLabLinkedWithPatient(String labId, String labType) {
		PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class);
		PatientLabRouting routing = dao.findDemographics(labType, ConversionUtils.fromIntString(labId));
		if (routing == null)
			return false;
		
		String demo = ConversionUtils.toIntString(routing.getDemographicNo());
		return demo != null && !demo.trim().equals("0");
	}

	public boolean isHRMLinkedWithPatient(String labId, String labType) {
		boolean ret = false;
		try {
			HRMDocumentToDemographicDao hrmDocumentToDemographicDao = (HRMDocumentToDemographicDao)  SpringUtils.getBean("HRMDocumentToDemographicDao");
			List<HRMDocumentToDemographic> docToDemo = hrmDocumentToDemographicDao.findByHrmDocumentId(labId);
			if(docToDemo != null && docToDemo.size() > 0){
				ret = true;
			}
		} catch (Exception e) {
			logger.error("exception in isLabLinkedWithPatient", e);

		}
		return ret;
	}


	public int getAckCount(String labId, String labType) {
		ProviderLabRoutingDao dao = SpringUtils.getBean(ProviderLabRoutingDao.class);
		return dao.findByStatusANDLabNoType(ConversionUtils.fromIntString(labId), labType, "A").size();
	}

	public static void populateMeasurementsTable(String labId, String demographicNo, String labType) {
		if (labType.equals(LabResultData.HL7TEXT)) {
			Hl7textResultsData.populateMeasurementsTable(labId, demographicNo);
		}
	}

	public static ArrayList<LabResultData> getRemoteLabs(LoggedInInfo loggedInInfo,Integer demographicId) {
		ArrayList<LabResultData> results = new ArrayList<LabResultData>();

		try {
			List<CachedDemographicLabResult> labResults  = null;
			try {
				if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
					DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());
					labResults = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility()).getLinkedCachedDemographicLabResults(demographicId);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unexpected error.", e);
				CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(),e);
			}

			if(CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
				labResults = IntegratorFallBackManager.getLabResults(loggedInInfo,demographicId);
			}

			for (CachedDemographicLabResult cachedDemographicLabResult : labResults) {
				results.add(toLabResultData(cachedDemographicLabResult));
			}
		} catch (Exception e) {
			logger.error("Error retriving remote labs", e);
		}

		return (results);
	}

	private static LabResultData toLabResultData(CachedDemographicLabResult cachedDemographicLabResult) throws IOException, SAXException, ParserConfigurationException {
		LabResultData result = new LabResultData();
		result.setRemoteFacilityId(cachedDemographicLabResult.getFacilityIdLabResultCompositePk().getIntegratorFacilityId());

		result.labType = cachedDemographicLabResult.getType();

		Document doc = XmlUtils.toDocument(cachedDemographicLabResult.getData());
		Node root = doc.getFirstChild();
		result.acknowledgedStatus = XmlUtils.getChildNodeTextContents(root, "acknowledgedStatus");
		result.accessionNumber = XmlUtils.getChildNodeTextContents(root, "accessionNumber");
		result.dateTime = XmlUtils.getChildNodeTextContents(root, "dateTime");
		result.discipline = XmlUtils.getChildNodeTextContents(root, "discipline");
		result.healthNumber = XmlUtils.getChildNodeTextContents(root, "healthNumber");
		result.labPatientId = XmlUtils.getChildNodeTextContents(root, "labPatientId");
		result.patientName = XmlUtils.getChildNodeTextContents(root, "patientName");
		result.priority = XmlUtils.getChildNodeTextContents(root, "priority");
		result.reportStatus = XmlUtils.getChildNodeTextContents(root, "reportStatus");
		result.requestingClient = XmlUtils.getChildNodeTextContents(root, "requestingClient");
		result.segmentID = XmlUtils.getChildNodeTextContents(root, "segmentID");
		result.sex = XmlUtils.getChildNodeTextContents(root, "sex");
		result.setAckCount(Integer.parseInt(XmlUtils.getChildNodeTextContents(root, "ackCount")));
		result.setMultipleAckCount(Integer.parseInt(XmlUtils.getChildNodeTextContents(root, "multipleAckCount")));

		return result;
	}
	
	public List<LabIdAndType> getCmlAndEpsilonLabResultsSince(Integer demographicNo, Date updateDate) {
		LabPatientPhysicianInfoDao labPatientPhysicianInfoDao = (LabPatientPhysicianInfoDao) SpringUtils.getBean("labPatientPhysicianInfoDao");
		
		//This case handles Epsilon and the old CML data
		List<Integer> ids = labPatientPhysicianInfoDao.getLabResultsSince(demographicNo,updateDate);
		List<LabIdAndType> results = new ArrayList<LabIdAndType>();
		
		for(Integer id:ids) {
			results.add(new LabIdAndType(id,"CML"));
		}
		return results;
	}
	
	public List<LabIdAndType> getMdsLabResultsSince(Integer demographicNo, Date updateDate) {
		MdsMSHDao mdsMSHDao = SpringUtils.getBean(MdsMSHDao.class);
		
		//This case handles old MDS data
		List<Integer> ids = mdsMSHDao.getLabResultsSince(demographicNo,updateDate);
		List<LabIdAndType> results = new ArrayList<LabIdAndType>();
		
		for(Integer id:ids) {
			results.add(new LabIdAndType(id,"MDS"));
		}
		return results;
	}

	public List<LabIdAndType> getPathnetResultsSince(Integer demographicNo, Date updateDate) {
		Hl7MshDao hl7MshDao = SpringUtils.getBean(Hl7MshDao.class);
		
		List<Integer> ids = hl7MshDao.getLabResultsSince(demographicNo,updateDate);
		List<LabIdAndType> results = new ArrayList<LabIdAndType>();
		
		for(Integer id:ids) {
			results.add(new LabIdAndType(id,"BCP"));
		}
		return results;
	}
	
	public List<LabIdAndType> getHl7ResultsSince(Integer demographicNo, Date updateDate) {
		Hl7TextMessageDao hl7TextMessageDao = SpringUtils.getBean(Hl7TextMessageDao.class);
		
		List<Integer> ids = hl7TextMessageDao.getLabResultsSince(demographicNo,updateDate);
		List<LabIdAndType> results = new ArrayList<LabIdAndType>();
		
		for(Integer id:ids) {
			results.add(new LabIdAndType(id,"HL7"));
		}
		return results;
	}
	
	public LabResultData getLab(LabIdAndType labIdAndType) {
		oscar.oscarMDS.data.MDSResultsData mDSData = new oscar.oscarMDS.data.MDSResultsData();
		PathnetResultsData pathData = new PathnetResultsData();
		List<LabResultData> resultsList = new ArrayList<LabResultData>();
		
		if("Epsilon".equals(labIdAndType.getLabType())) {
			resultsList.addAll(mDSData.populateEpsilonResultsData(null, null, null, null, null, null,labIdAndType.getLabId()));
		} else if("CML".equals(labIdAndType.getLabType())) {
			resultsList.addAll(mDSData.populateCMLResultsData(null, null, null, null, null, null,labIdAndType.getLabId()));
		} else if("BCP".equals(labIdAndType.getLabType())) {
			resultsList.addAll(pathData.populatePathnetResultsData(null, null, null, null, null, null, labIdAndType.getLabId()));
		} else if("MDS".equals(labIdAndType.getLabType())) {
			resultsList.addAll(mDSData.populateMDSResultsData2(null, null, null, null, null, null,labIdAndType.getLabId()));
		} else if("HL7".equals(labIdAndType.getLabType())) {
			resultsList.addAll(Hl7textResultsData.populateHl7ResultsData(null, null, null, null, null, null,labIdAndType.getLabId()));   
		}
		if(!resultsList.isEmpty()) {
			return resultsList.get(0);
		}
		return null;
	}
	
}
