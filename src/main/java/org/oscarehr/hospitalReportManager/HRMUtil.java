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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.hospitalReportManager.dao.HRMCategoryDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.dao.HRMSubClassDao;
import org.oscarehr.hospitalReportManager.model.HRMCategory;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.hospitalReportManager.model.HRMDocumentSubClass;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.hospitalReportManager.model.HRMSubClass;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class HRMUtil {

	private static final Logger logger = MiscUtils.getLogger();
	
	public static final String DATE = "time_received";
	public static final String TYPE = " report_type";
	
	private static HRMDocumentDao hrmDocumentDao = (HRMDocumentDao) SpringUtils.getBean("HRMDocumentDao");
	private static HRMDocumentToDemographicDao hrmDocumentToDemographicDao = (HRMDocumentToDemographicDao) SpringUtils.getBean("HRMDocumentToDemographicDao");
	private static HRMSubClassDao hrmSubClassDao = (HRMSubClassDao) SpringUtils.getBean("HRMSubClassDao");
	private static HRMCategoryDao hrmCategoryDao = SpringUtils.getBean(HRMCategoryDao.class);
	
	public HRMUtil() {
		
	}
	
	@SuppressWarnings("null")
    public static ArrayList<HashMap<String, ? extends Object>> listHRMDocuments(LoggedInInfo loggedInInfo, String sortBy, boolean sortAsc, String demographicNo){
		
		ArrayList<HashMap<String, ? extends Object>> hrmdocslist = new ArrayList<HashMap<String, ?>>();
		
		List<HRMDocumentToDemographic> hrmDocResultsDemographic = hrmDocumentToDemographicDao.findByDemographicNo(demographicNo);
		for(HRMDocumentToDemographic matched : hrmDocResultsDemographic) {
			HRMDocument hrmDocument = hrmDocumentDao.find(Integer.parseInt(matched.getHrmDocumentId()));
			
			if(hrmDocument == null) {
				logger.warn("can't load HRMDocument " + matched.getHrmDocumentId());
				continue;
			}
			
			if(hrmDocument.getParentReport() != null) {
				//this is a child report. IE. it's been replaced by the parent..so skip
				continue;
			}
			
			HRMCategory category = null;
			//Provider provider = null;
			if(hrmDocument.getHrmCategoryId()!= null) {
				 category = hrmCategoryDao.find(hrmDocument.getHrmCategoryId());
			}
			
			HashMap<String, Object> curht = new HashMap<String, Object>();
			curht.put("id", hrmDocument.getId());
			curht.put("time_received", hrmDocument.getTimeReceived().toString());
			curht.put("report_date", hrmDocument.getReportDate() != null ? hrmDocument.getReportDate().toString() : "");
			curht.put("report_type", hrmDocument.getReportType());
			curht.put("report_status", hrmDocument.getReportStatus());
			curht.put("category", category!=null?category.getCategoryName():"");
			curht.put("description", hrmDocument.getDescription());
			if(!StringUtils.isEmpty(hrmDocument.getClassName()) && !StringUtils.isEmpty(hrmDocument.getSubClassName())) {
				String subClassName = "";
				if(hrmDocument.getSubClassName().indexOf("^") != -1) {
					subClassName = hrmDocument.getSubClassName().split("\\^")[1];
				}
				else {
					subClassName = hrmDocument.getSubClassName();
				}
				curht.put("class_subclass", hrmDocument.getClassName() + " " + subClassName);
			}
			if(!StringUtils.isEmpty(hrmDocument.getClassName()) && !hrmDocument.getAccompanyingSubClasses().isEmpty()) {
				for(HRMDocumentSubClass sc: hrmDocument.getAccompanyingSubClasses()) {
					if(sc.isActive()) {
						curht.put("class_subclass", hrmDocument.getClassName() + " " + sc.getSubClass() + ":" + sc.getSubClassMnemonic() + ":" + sc.getSubClassDescription());
					}
				}
			}
			
			hrmdocslist.add(curht);
		}
		
		
		//sort.
		if("report_name".equals(sortBy) ){
		      Collections.sort(hrmdocslist,new Comparator<HashMap<String, ? extends Object>>() {
	                 public int compare(HashMap<String, ? extends Object> o1, HashMap<String, ? extends Object> o2) {
	                	 return ((String)o1.get("report_type")).compareTo((String)o2.get("report_type"));
	                }
		      });

		} else if("report_date".equals(sortBy) ){
			 Collections.sort(hrmdocslist,new Comparator<HashMap<String, ? extends Object>>() {
                 public int compare(HashMap<String, ? extends Object> o1, HashMap<String, ? extends Object> o2) {
                	 return ((String)o1.get("report_date")).compareTo((String)o2.get("report_date"));
                }
	      });
		} else if("time_received".equals(sortBy) ){
			Collections.sort(hrmdocslist,new Comparator<HashMap<String, ? extends Object>>() {
                public int compare(HashMap<String, ? extends Object> o1, HashMap<String, ? extends Object> o2) {
               	 return ((String)o1.get("time_received")).compareTo((String)o2.get("time_received"));
               }
	      });
		} else if("category".equals(sortBy) ){
			Collections.sort(hrmdocslist,new Comparator<HashMap<String, ? extends Object>>() {
                public int compare(HashMap<String, ? extends Object> o1, HashMap<String, ? extends Object> o2) {
               	 return ((String)o1.get("category")).compareTo((String)o2.get("category"));
               }
	      });
		}
		
		if(!sortAsc) {
			Collections.reverse(hrmdocslist);
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
				curht.put("mnemonic", (hrmSubClass.getSubClassMnemonic() != null ? hrmSubClass.getSubClassMnemonic() : ""));
				curht.put("description", (hrmSubClass.getSubClassDescription() != null ? hrmSubClass.getSubClassDescription() : ""));
				curht.put("mappingId", hrmSubClass.getId());
				
				hrmdocslist.add(curht);
				
			}
			
			return hrmdocslist;
			
		}
}
