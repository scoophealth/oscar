package oscar.oscarLab.ca.on;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.hospitalReportManager.HRMReport;
import org.oscarehr.hospitalReportManager.HRMReportParser;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToProvider;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToProviderDao;
import org.oscarehr.util.SpringUtils;

public class HRMResultsData {

	Logger logger = Logger.getLogger(HRMResultsData.class);
	private HRMDocumentDao hrmDocumentDao = (HRMDocumentDao) SpringUtils.getBean("HRMDocumentDao");
	private HRMDocumentToProviderDao hrmDocumentToProviderDao = (HRMDocumentToProviderDao) SpringUtils.getBean("HRMDocumentToProviderDao");
	private HRMDocumentToDemographicDao hrmDocumentToDemographicDao = (HRMDocumentToDemographicDao) SpringUtils.getBean("HRMDocumentToDemographicDao");
	private DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");


	public HRMResultsData() {
	}

	public List<LabResultData> populateHRMdocumentsResultsData(String providerNo, Integer page, Integer pageSize){
		if(providerNo == null || "".equals(providerNo)){
			providerNo = "%";
		} else if (providerNo.equalsIgnoreCase("0")) {
			providerNo = "-1";
		}

		List<HRMDocumentToProvider> hrmDocResultsProvider = hrmDocumentToProviderDao.findByProviderNoLimit(providerNo, page, pageSize);


		ArrayList<LabResultData> labResults = new ArrayList<LabResultData>();

		for (HRMDocumentToProvider hrmDocResult : hrmDocResultsProvider) {
			String id = hrmDocResult.getHrmDocumentId();
			LabResultData lbData = new LabResultData(LabResultData.HRM);

			List<HRMDocument> hrmDocument = hrmDocumentDao.findById(Integer.parseInt(id));


			lbData.dateTime = hrmDocument.get(0).getTimeReceived().toString();
			lbData.acknowledgedStatus = "U";
			lbData.reportStatus = hrmDocument.get(0).getReportStatus();
			lbData.segmentID = hrmDocument.get(0).getId().toString();
			lbData.setDateObj(hrmDocument.get(0).getReportDate());
			lbData.patientName = "Not, Assigned";

			// check if patient is matched
			List<HRMDocumentToDemographic> hrmDocResultsDemographic = hrmDocumentToDemographicDao.findByHrmDocumentId(hrmDocument.get(0).getId().toString());
			HRMReport hrmReport = HRMReportParser.parseReport(hrmDocument.get(0).getReportFile());
			
			if (hrmDocResultsDemographic.size()>0) {
				Demographic demographic = demographicDao.getDemographic(hrmDocResultsDemographic.get(0).getDemographicNo());
				if (demographic != null) {
					lbData.patientName =  demographic.getLastName()+","+demographic.getFirstName();
					lbData.sex = demographic.getSex();
					lbData.healthNumber = demographic.getHin();
					lbData.isMatchedToPatient = true;
				}
			} else {
				lbData.sex = hrmReport.getGender();
				lbData.healthNumber = hrmReport.getHCN();
				lbData.patientName = hrmReport.getLegalName();
				
			}

			lbData.reportStatus = hrmReport.getResultStatus();
			lbData.priority = "----";
			lbData.requestingClient = "";
			lbData.discipline = "HRM";
			lbData.resultStatus = hrmReport.getResultStatus();
			
			
			labResults.add(lbData);

		}

		return labResults;
	}

	/*
	public List<LabResultData> populateHRMdocumentsResultsData(String providerNo){


		List<HRMDocumentToProvider> hrmDocResultsProvider = hrmDocumentToProviderDao.findByProviderNo(providerNo);


		ArrayList<LabResultData> labResults = new ArrayList<LabResultData>();

		for (HRMDocumentToProvider hrmDocResult : hrmDocResultsProvider) {
			String id = hrmDocResult.getHrmDocumentId();
			LabResultData lbData = new LabResultData(LabResultData.HRM);

			List<HRMDocument> hrmDocument = hrmDocumentDao.findById(Integer.parseInt(id));


			lbData.dateTime = hrmDocument.get(0).getTimeReceived().toString();
			lbData.acknowledgedStatus = "U";
			lbData.reportStatus = hrmDocument.get(0).getReportStatus();
			lbData.segmentID = hrmDocument.get(0).getId().toString();
			lbData.patientName = "Not, Assigned";

			// check if patient is matched
			List<HRMDocumentToDemographic> hrmDocResultsDemographic = hrmDocumentToDemographicDao.findByHrmDocumentId(hrmDocument.get(0).getId().toString());
			if (hrmDocResultsDemographic.size()>0) {
				Demographic demographic = demographicDao.getDemographic(hrmDocResultsDemographic.get(0).getDemographicNo());
				if (demographic != null) {
					lbData.patientName =  demographic.getLastName()+","+demographic.getFirstName();
					lbData.isMatchedToPatient = true;
				}
			} else {
				HRMReport hrmReport = HRMReportParser.parseReport(hrmDocument.get(0).getReportFile());
				lbData.sex = hrmReport.getGender();
				lbData.healthNumber = hrmReport.getHCN();
				lbData.patientName = hrmReport.getLegalName();
				lbData.reportStatus = hrmReport.getResultStatus();
				lbData.priority = "----";
				lbData.requestingClient = "";
				lbData.discipline = "HRM";
				lbData.resultStatus = hrmReport.getResultStatus();
				
			}

			labResults.add(lbData);

		}

		return labResults;
	}
*/
	
}
