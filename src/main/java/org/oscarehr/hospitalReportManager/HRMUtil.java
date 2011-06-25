package org.oscarehr.hospitalReportManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentSubClassDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.dao.HRMSubClassDao;
import org.oscarehr.hospitalReportManager.model.HRMCategory;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.hospitalReportManager.model.HRMDocumentSubClass;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.hospitalReportManager.model.HRMSubClass;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class HRMUtil {

	private static final Logger logger = MiscUtils.getLogger();
	
	public static final String DATE = "time_received";
	public static final String TYPE = " report_type";
	
	private static HRMDocumentDao hrmDocumentDao = (HRMDocumentDao) SpringUtils.getBean("HRMDocumentDao");
	private static HRMDocumentToDemographicDao hrmDocumentToDemographicDao = (HRMDocumentToDemographicDao) SpringUtils.getBean("HRMDocumentToDemographicDao");
	private static HRMSubClassDao hrmSubClassDao = (HRMSubClassDao) SpringUtils.getBean("HRMSubClassDao");
	private static HRMDocumentSubClassDao hrmDocumentSubClassDao = (HRMDocumentSubClassDao) SpringUtils.getBean("HRMDocumentSubClassDao");
	
	public HRMUtil() {
		
	}
	
	@SuppressWarnings("null")
    public static ArrayList<HashMap<String, ? extends Object>> listHRMDocuments(String sortBy, String demographicNo){
		ArrayList<HashMap<String, ? extends Object>> hrmdocslist = new ArrayList<HashMap<String, ?>>();
		
		List<HRMDocumentToDemographic> hrmDocResultsDemographic = hrmDocumentToDemographicDao.findByDemographicNo(demographicNo);
		List<HRMDocument> hrmDocumentsAll = new LinkedList<HRMDocument>();
		
		
		for (HRMDocumentToDemographic hrmDocResult : hrmDocResultsDemographic) {
			String id = hrmDocResult.getHrmDocumentId();
			List<HRMDocument> hrmDocuments = hrmDocumentDao.findById(Integer.parseInt(id));
			
			HRMCategory category = null;
			HRMSubClass thisReportSubClassMapping = null;
			List<HRMDocumentSubClass> subClassList = hrmDocumentSubClassDao.getSubClassesByDocumentId(hrmDocuments.get(0).getId());
			
			
			HRMReport report = HRMReportParser.parseReport(hrmDocuments.get(0).getReportFile());
			if (report.getFirstReportClass().equalsIgnoreCase("Diagnostic Imaging Report") || report.getFirstReportClass().equalsIgnoreCase("Cardio Respiratory Report")) {
				// We'll only care about the first one, as long as there is at least one
				if (subClassList != null && subClassList.size() > 0) {
					HRMDocumentSubClass firstSubClass = subClassList.get(0);
					thisReportSubClassMapping = hrmSubClassDao.findApplicableSubClassMapping(report.getFirstReportClass(), firstSubClass.getSubClass(), firstSubClass.getSubClassMnemonic(), report.getSendingFacilityId());
				}
			} else {
				// Medical records report
				String[] reportSubClass = report.getFirstReportSubClass().split("\\^");
				thisReportSubClassMapping = hrmSubClassDao.findApplicableSubClassMapping(report.getFirstReportClass(), reportSubClass[0], null, report.getSendingFacilityId());
			}
			
			if (thisReportSubClassMapping != null) {
				category = thisReportSubClassMapping.getHrmCategory();
			}
			
			
			HashMap<String, Object> curht = new HashMap<String, Object>();
			curht.put("id", hrmDocuments.get(0).getId());
			curht.put("time_received", hrmDocuments.get(0).getTimeReceived().toString());
			curht.put("report_type", hrmDocuments.get(0).getReportType());
			curht.put("report_status", hrmDocuments.get(0).getReportStatus());
			curht.put("category", category);
			
			hrmdocslist.add(curht);
			hrmDocumentsAll.addAll(hrmDocuments);
			
		}
		
		if (TYPE.equals(sortBy)) {
			Collections.sort(hrmDocumentsAll, HRMDocument.HRM_TYPE_COMPARATOR);
		}
		else { 
			Collections.sort(hrmDocumentsAll, HRMDocument.HRM_DATE_COMPARATOR) ;
		}
		
		
		return hrmdocslist;
		
	}
	
	 public static ArrayList<HashMap<String, ? extends Object>> listMappings(){
			ArrayList<HashMap<String, ? extends Object>> hrmdocslist = new ArrayList<HashMap<String, ?>>();
			
			List<HRMSubClass> hrmSubClasses = hrmSubClassDao.listAll();
			
			for (HRMSubClass hrmSubClass : hrmSubClasses) {
	
				HashMap<String, Object> curht = new HashMap<String, Object>();
				curht.put("id", hrmSubClass.getSendingFacilityId());
				curht.put("sub_class", hrmSubClass.getSubClassName());
				curht.put("class", hrmSubClass.getClassName());
				curht.put("category", hrmSubClass.getHrmCategory());
				
				hrmdocslist.add(curht);
				
			}
			
			return hrmdocslist;
			
		}
}
