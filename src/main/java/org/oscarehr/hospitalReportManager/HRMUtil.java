/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.hospitalReportManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

import oscar.oscarLab.ca.on.HRMResultsData;

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
		
		HashMap<String,ArrayList<Integer>> duplicateLabIds=new HashMap<String, ArrayList<Integer>>();
		HashMap<String,HRMDocument> docsToDisplay=filterDuplicates(hrmDocResultsDemographic, duplicateLabIds);
		
		for (Map.Entry<String, HRMDocument> entry : docsToDisplay.entrySet()) {
			String duplicateKey=entry.getKey();
			HRMDocument hrmDocument = entry.getValue();
			
			HRMCategory category = null;
			HRMSubClass thisReportSubClassMapping = null;
			List<HRMDocumentSubClass> subClassList = hrmDocumentSubClassDao.getSubClassesByDocumentId(hrmDocument.getId());
			
			
			HRMReport report = HRMReportParser.parseReport(hrmDocument.getReportFile());
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
			curht.put("id", hrmDocument.getId());
			curht.put("time_received", hrmDocument.getTimeReceived().toString());
			curht.put("report_type", hrmDocument.getReportType());
			curht.put("report_status", hrmDocument.getReportStatus());
			curht.put("category", category);
			
			StringBuilder duplicateLabIdQueryString=new StringBuilder();
			ArrayList<Integer> duplicateIdList=duplicateLabIds.get(duplicateKey);
        	if (duplicateIdList!=null)
        	{
				for (Integer duplicateLabIdTemp : duplicateIdList)
            	{
            		if (duplicateLabIdQueryString.length()>0) duplicateLabIdQueryString.append(',');
            		duplicateLabIdQueryString.append(duplicateLabIdTemp);
            	}
			}
        	curht.put("duplicateLabIds", duplicateLabIdQueryString.toString());
			
			hrmdocslist.add(curht);
			hrmDocumentsAll.add(hrmDocument);
			
		}
		
		if (TYPE.equals(sortBy)) {
			Collections.sort(hrmDocumentsAll, HRMDocument.HRM_TYPE_COMPARATOR);
		}
		else { 
			Collections.sort(hrmDocumentsAll, HRMDocument.HRM_DATE_COMPARATOR) ;
		}
		
		
		return hrmdocslist;
		
	}
	
	 private static HashMap<String,HRMDocument> filterDuplicates(List<HRMDocumentToDemographic> hrmDocumentToDemographics, HashMap<String,ArrayList<Integer>> duplicateLabIds) {
		 
		HashMap<String,HRMDocument> docsToDisplay = new HashMap<String,HRMDocument>();
		HashMap<String,HRMReport> labReports=new HashMap<String,HRMReport>();

		 for (HRMDocumentToDemographic hrmDocumentToDemographic : hrmDocumentToDemographics)
		 {
			String id = hrmDocumentToDemographic.getHrmDocumentId();
			List<HRMDocument> hrmDocuments = hrmDocumentDao.findById(Integer.parseInt(id));

			for (HRMDocument hrmDocument : hrmDocuments)
			{
				HRMReport hrmReport = HRMReportParser.parseReport(hrmDocument.getReportFile());
				if (hrmReport == null) continue;
				hrmReport.setHrmDocumentId(hrmDocument.getId());
				String duplicateKey=hrmReport.getSendingFacilityId()+':'+hrmReport.getSendingFacilityReportNo()+':'+hrmReport.getDeliverToUserId();
	
				// if no duplicate
				if (!docsToDisplay.containsKey(duplicateKey))
				{
					docsToDisplay.put(duplicateKey,hrmDocument);
					labReports.put(duplicateKey, hrmReport);
				}
				else // there exists an entry like this one
				{
					HRMReport previousHrmReport=labReports.get(duplicateKey);
					
					logger.debug("Duplicate report found : previous="+previousHrmReport.getHrmDocumentId()+", current="+hrmReport.getHrmDocumentId());
					
					Integer duplicateIdToAdd;
					
					// if the current entry is newer than the previous one then replace it, other wise just keep the previous entry
					if (HRMResultsData.isNewer(hrmReport, previousHrmReport))
					{
						HRMDocument previousHRMDocument = docsToDisplay.get(duplicateKey);
						duplicateIdToAdd=previousHRMDocument.getId();
						
						docsToDisplay.put(duplicateKey,hrmDocument);
						labReports.put(duplicateKey, hrmReport);
					}
					else
					{
						duplicateIdToAdd=hrmDocument.getId();
					}
	
					ArrayList<Integer> duplicateIds=duplicateLabIds.get(duplicateKey);
					if (duplicateIds==null)
					{
						duplicateIds=new ArrayList<Integer>();
						duplicateLabIds.put(duplicateKey, duplicateIds);
					}
					
					duplicateIds.add(duplicateIdToAdd);						
				}
			}
		}
		 
		 return(docsToDisplay);
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
				curht.put("mnemonic", (hrmSubClass.getSubClassMnemonic() != null ? hrmSubClass.getSubClassMnemonic() : ""));
				curht.put("description", (hrmSubClass.getSubClassDescription() != null ? hrmSubClass.getSubClassDescription() : ""));
				curht.put("mappingId", hrmSubClass.getId());
				
				hrmdocslist.add(curht);
				
			}
			
			return hrmdocslist;
			
		}
}
