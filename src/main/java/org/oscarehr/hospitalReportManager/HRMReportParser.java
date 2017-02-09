/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.hospitalReportManager;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.cxf.helpers.FileUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentSubClassDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToProviderDao;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.hospitalReportManager.model.HRMDocumentSubClass;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToProvider;
import org.oscarehr.hospitalReportManager.xsd.OmdCds;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.xml.sax.SAXException;

import oscar.OscarProperties;


public class HRMReportParser {

	private static Logger logger = MiscUtils.getLogger();
	
	private HRMReportParser() {}


	public static HRMReport parseReport(LoggedInInfo loggedInInfo, String hrmReportFileLocation) {
		OmdCds root = null;
		
		logger.debug("Parsing the Report in the location:"+hrmReportFileLocation);
		
		String fileData = null;
		if (hrmReportFileLocation != null) {
			
			try {
				//a lot of the parsers need to refer to a file and even when they provide functions like parse(String text)
				//it will not parse the same way because it will treat the text as a URL
				//so we take the lab and store them temporarily in a random filename in /tmp/oscar-sftp/
				File tmpXMLholder = new File(hrmReportFileLocation);
				
				//check the DOCUMENT_DIR
				if(!tmpXMLholder.exists()) {
					String place= OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
					tmpXMLholder = new File(place + File.separator + hrmReportFileLocation);
				}

				if(!tmpXMLholder.exists()) {
					logger.warn("unable to find the HRM report. checked " + hrmReportFileLocation + ", and in the document_dir");
					return null;
				}
				if (tmpXMLholder.exists()) fileData = FileUtils.getStringFromFile(tmpXMLholder);
				// Parse an XML document into a DOM tree.
				DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				// Create a SchemaFactory capable of understanding WXS schemas.


				//SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");//XMLConstants.W3C_XML_SCHEMA_NS_URI);
				SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

				// Load a WXS schema, represented by a Schema instance.
				Source schemaFile = new StreamSource(new File(SFTPConnector.OMD_directory + "report_manager_cds.xsd"));
				Schema schema = factory.newSchema(schemaFile); 

				JAXBContext jc = JAXBContext.newInstance("org.oscarehr.hospitalReportManager.xsd");
				Unmarshaller u = jc.createUnmarshaller();
				u.setSchema(schema);
				
				root = (OmdCds) u.unmarshal(tmpXMLholder);

				tmpXMLholder = null;

			} catch (SAXException e) {
				logger.error("SAX ERROR PARSING XML " + e);
			} catch (ParserConfigurationException e) {
				logger.error("PARSER ERROR PARSING XML " + e);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				logger.error("error",e);
				if(e.getLinkedException() != null) {
					SFTPConnector.notifyHrmError(loggedInInfo, e.getLinkedException().getMessage());
				} else {
					SFTPConnector.notifyHrmError(loggedInInfo, e.getMessage());
				}
				
			}

                        if (root!=null && hrmReportFileLocation!=null && fileData!=null)
                            return new HRMReport(root, hrmReportFileLocation, fileData);
		}

		return null;
	}

	public static void addReportToInbox(LoggedInInfo loggedInInfo, HRMReport report) {
		
		if(report == null) {
			logger.info("addReportToInbox cannot continue, report parameter is null");
			return;
		}

		logger.debug("Adding Report to Inbox, for file:"+report.getFileLocation());
		
		HRMDocument document = new HRMDocument();

		File fileLocation = new File(report.getFileLocation());

		document.setReportFile(fileLocation.getName());
		document.setReportStatus(report.getResultStatus());
		document.setReportType(report.getFirstReportClass());
		document.setTimeReceived(new Date());

		String reportFileData = report.getFileData();

		String noMessageIdFileData = reportFileData.replaceAll("<MessageUniqueID>.*?</MessageUniqueID>", "<MessageUniqueID></MessageUniqueID>");
		String noTransactionInfoFileData = reportFileData.replaceAll("<TransactionInformation>.*?</TransactionInformation>", "<TransactionInformation></TransactionInformation>");
		String noDemograhpicInfoFileData = reportFileData.replaceAll("<Demographics>.*?</Demographics>", "<Demographics></Demographics").replaceAll("<MessageUniqueID>.*?</MessageUniqueID>", "<MessageUniqueID></MessageUniqueID>");

		String noMessageIdHash = DigestUtils.md5Hex(noMessageIdFileData);
		String noTransactionInfoHash = DigestUtils.md5Hex(noTransactionInfoFileData);
		String noDemographicInfoHash = DigestUtils.md5Hex(noDemograhpicInfoFileData);

		document.setReportHash(noMessageIdHash);
		document.setReportLessTransactionInfoHash(noTransactionInfoHash);
		document.setReportLessDemographicInfoHash(noDemographicInfoHash);

		document.setReportDate(HRMReportParser.getAppropriateDateFromReport(report));

		document.setDescription("");
		
		// We're going to check to see if there's a match in the database already for either of these
		// report hash matches = duplicate report for same recipient
		// no transaction info hash matches = duplicate report, but different recipient
		HRMDocumentDao hrmDocumentDao = (HRMDocumentDao) SpringUtils.getBean("HRMDocumentDao");
		List<Integer> exactMatchList = hrmDocumentDao.findByHash(noMessageIdHash);

		if (exactMatchList == null || exactMatchList.size() == 0) {
			List<HRMDocument> sameReportDifferentRecipientReportList = hrmDocumentDao.findByNoTransactionInfoHash(noTransactionInfoHash);

			if (sameReportDifferentRecipientReportList != null && sameReportDifferentRecipientReportList.size() > 0) {
				logger.info("Same Report Different Recipient, for file:"+report.getFileLocation());
				HRMReportParser.routeReportToProvider(sameReportDifferentRecipientReportList.get(0), report);
			} else {
				// New report
				hrmDocumentDao.persist(document);
				logger.debug("MERGED DOCUMENTS ID"+document.getId());


				HRMReportParser.routeReportToDemographic(report, document);
				HRMReportParser.doSimilarReportCheck(loggedInInfo, report, document);
				// Attempt a route to the provider listed in the report -- if they don't exist, note that in the record
				Boolean routeSuccess = HRMReportParser.routeReportToProvider(report, document.getId());
				if (!routeSuccess) {
					
					logger.info("Adding the provider name to the list of unidentified providers, for file:"+report.getFileLocation());
					
					// Add the provider name to the list of unidentified providers for this report
					document.setUnmatchedProviders((document.getUnmatchedProviders() != null ? document.getUnmatchedProviders() : "") + "|" + ((report.getDeliverToUserIdLastName()!=null)?report.getDeliverToUserIdLastName() + ", " + report.getDeliverToUserIdFirstName():report.getDeliverToUserId()) + " (" + report.getDeliverToUserId() + ")");
					hrmDocumentDao.merge(document);
					// Route this report to the "system" user so that a search for "all" in the inbox will come up with them
					HRMReportParser.routeReportToProvider(document.getId().toString(), "-1");
				}

				HRMReportParser.routeReportToSubClass(report, document.getId());
			}
		} else if (exactMatchList != null && exactMatchList.size() > 0) {
			// We've seen this one before.  Increment the counter on how many times we've seen it before
			
			logger.debug("We've seen this report before. Increment the counter on how many times we've seen it before, for file:"+report.getFileLocation());
			
			HRMDocument existingDocument = hrmDocumentDao.findById(exactMatchList.get(0)).get(0);
			existingDocument.setNumDuplicatesReceived((existingDocument.getNumDuplicatesReceived() != null ? existingDocument.getNumDuplicatesReceived() : 0) + 1);

			hrmDocumentDao.merge(existingDocument);
		}
	}

	private static void routeReportToDemographic(HRMReport report, HRMDocument mergedDocument) {
		
		if(report == null) {
			logger.info("routeReportToDemographic cannot continue, report parameter is null");
			return;
		}
		

		logger.debug("Routing Report To Demographic, for file:"+report.getFileLocation());
		
		// Search the demographics on the system for a likely match and route it to them automatically
		DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");

		List<Demographic> matchingDemographicListByName = demographicDao.searchDemographic(report.getLegalName());

		if (matchingDemographicListByName.size() == 1) {
			// Found a match by name
			HRMReportParser.routeReportToDemographic(mergedDocument.getId().toString(), matchingDemographicListByName.get(0).getDemographicNo().toString());
		} else {
			for (Demographic d : matchingDemographicListByName) {

				if (report.getHCN().equalsIgnoreCase(d.getHin())) { // Check health card no.
					HRMReportParser.routeReportToDemographic(mergedDocument.getId().toString(), d.getDemographicNo().toString());
					return;
				} else if (report.getGender().equalsIgnoreCase(d.getSex()) && report.getDateOfBirthAsString().equalsIgnoreCase(d.getBirthDayAsString())) { // Check dob & sex
					HRMReportParser.routeReportToDemographic(mergedDocument.getId().toString(), d.getDemographicNo().toString());
					return;
				}
			}
		}
	}


	private static boolean hasSameStatus(HRMReport report, HRMReport loadedReport) {
		if(report.getResultStatus() != null) {
			return report.getResultStatus().equalsIgnoreCase(loadedReport.getResultStatus());
		}
		 
		return true;
	}
	private static void doSimilarReportCheck(LoggedInInfo loggedInInfo, HRMReport report, HRMDocument mergedDocument) {
		
		if(report == null) {
			logger.info("doSimilarReportCheck cannot continue, report parameter is null");
			return;
		}
		logger.debug("Identifying if this is a report that we received before, but was sent to the wrong demographic, for file:"+report.getFileLocation());
		
		HRMDocumentDao hrmDocumentDao = (HRMDocumentDao) SpringUtils.getBean("HRMDocumentDao");

		// Check #1: Identify if this is a report that we received before, but was sent to the wrong demographic
		List<Integer> parentReportList = hrmDocumentDao.findAllWithSameNoDemographicInfoHash(mergedDocument.getReportLessDemographicInfoHash());
		if (parentReportList != null && parentReportList.size() > 0) {
			for (Integer id : parentReportList) {
				if (id != null && id.intValue() != mergedDocument.getId().intValue()) {
					mergedDocument.setParentReport(id);
					hrmDocumentDao.merge(mergedDocument);
					return;
				}
			}
		}

		// Load all the reports for this demographic into memory -- check by name only
		List<HRMReport> thisDemoHrmReportList = HRMReportParser.loadAllReportsRoutedToDemographic(loggedInInfo, report.getLegalName());

		for (HRMReport loadedReport : thisDemoHrmReportList) {
			boolean hasSameReportContent = report.getFirstReportTextContent().equalsIgnoreCase(loadedReport.getFirstReportTextContent());
			boolean hasSameStatus = hasSameStatus(report,loadedReport);
			boolean hasSameClass = report.getFirstReportClass().equalsIgnoreCase(loadedReport.getFirstReportClass());
			boolean hasSameDate = false;

			hasSameDate = HRMReportParser.getAppropriateDateFromReport(report).equals(HRMReportParser.getAppropriateDateFromReport(loadedReport));

			Integer threshold = 0;

			if (hasSameReportContent)
				threshold += 100;
			else
				threshold += 10;

			if (hasSameStatus)
				threshold += 5;
			else
				threshold += 10;

			if (hasSameClass)
				threshold += 10;
			else
				threshold += 10;

			if (hasSameDate)
				threshold += 20;
			else
				threshold += 5;

			if (threshold >= 45) {
				// This is probably a changed report addressed to the same demographic, so set the parent id (as long as this isn't the same report) and we're done!
				if (loadedReport.getHrmParentDocumentId() != null && loadedReport.getHrmDocumentId().intValue() != mergedDocument.getId().intValue()) {
					mergedDocument.setParentReport(loadedReport.getHrmParentDocumentId());
					hrmDocumentDao.merge(mergedDocument);
					return;
				} else if (loadedReport.getHrmParentDocumentId() == null) {
					mergedDocument.setParentReport(loadedReport.getHrmDocumentId());
					hrmDocumentDao.merge(mergedDocument);
					return;
				}
			}
		}
	}


	private static List<HRMReport> loadAllReportsRoutedToDemographic(LoggedInInfo loggedInInfo, String legalName) {
		DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
		HRMDocumentToDemographicDao hrmDocumentToDemographicDao = (HRMDocumentToDemographicDao) SpringUtils.getBean("HRMDocumentToDemographicDao");
		HRMDocumentDao hrmDocumentDao = (HRMDocumentDao) SpringUtils.getBean("HRMDocumentDao");

		List<Demographic> matchingDemographicListByName = demographicDao.searchDemographic(legalName);

		List<HRMReport> allRoutedReports = new LinkedList<HRMReport>();

		for (Demographic d : matchingDemographicListByName) {
			List<HRMDocumentToDemographic> matchingHrmDocumentList = hrmDocumentToDemographicDao.findByDemographicNo(d.getDemographicNo().toString());
			for (HRMDocumentToDemographic matchingHrmDocument : matchingHrmDocumentList) {
				HRMDocument hrmDocument = hrmDocumentDao.find(Integer.parseInt(matchingHrmDocument.getHrmDocumentId()));

				HRMReport hrmReport = HRMReportParser.parseReport(loggedInInfo, hrmDocument.getReportFile());
				hrmReport.setHrmDocumentId(hrmDocument.getId());
				hrmReport.setHrmParentDocumentId(hrmDocument.getParentReport());
				allRoutedReports.add(hrmReport);
			}
		}

		return allRoutedReports;

	}


	public static void routeReportToSubClass(HRMReport report, Integer reportId) {
		if(report == null) {
			logger.info("routeReportToSubClass cannot continue, report parameter is null");
			return;
		}
		
		logger.debug("Routing Report To SubClass, for file:"+report.getFileLocation());
		
		HRMDocumentSubClassDao hrmDocumentSubClassDao = (HRMDocumentSubClassDao) SpringUtils.getBean("HRMDocumentSubClassDao");

		if (report.getFirstReportClass().equalsIgnoreCase("Diagnostic Imaging Report") || report.getFirstReportClass().equalsIgnoreCase("Cardio Respiratory Report")) {
			List<List<Object>> subClassList = report.getAccompanyingSubclassList();

			boolean firstSubClass = true;
			
			for (List<Object> subClass : subClassList) {
				HRMDocumentSubClass newSubClass = new HRMDocumentSubClass();

				newSubClass.setSubClass((String) subClass.get(0));
				newSubClass.setSubClassMnemonic((String) subClass.get(1));
				newSubClass.setSubClassDescription((String) subClass.get(2));
				newSubClass.setSubClassDateTime((Date) subClass.get(3));

				if (firstSubClass) {
					newSubClass.setActive(true);
					firstSubClass = false;
				}
				newSubClass.setHrmDocumentId(reportId);

				hrmDocumentSubClassDao.merge(newSubClass);
			}
		} else {
			// There aren't subclasses on a Medical Records Report
		}
	}

	public static Date getAppropriateDateFromReport(HRMReport report) {
		if (report.getFirstReportClass().equalsIgnoreCase("Diagnostic Imaging Report") || report.getFirstReportClass().equalsIgnoreCase("Cardio Respiratory Report")) {
			return ((Date) (report.getAccompanyingSubclassList().get(0).get(3)));
		}

		// Medical Records Report
		return report.getFirstReportEventTime().getTime();
	}

	public static boolean routeReportToProvider(HRMReport report, Integer reportId) {
		if(report == null) {
			logger.info("routeReportToProvider cannot continue, report parameter is null");
			return false;
		}
		
		logger.debug("Routing Report to Provider, for file:"+report.getFileLocation());
		
		HRMDocumentToProviderDao hrmDocumentToProviderDao = (HRMDocumentToProviderDao) SpringUtils.getBean("HRMDocumentToProviderDao");
		ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao"); 

		String providerNo = report.getDeliverToUserId().substring(1); // We have to remove the first "D"
		//		String providerLastName = report.getDeliverToUserIdLastName();
		//		String providerFirstName = report.getDeliverToUserIdFirstName();

		Provider sendToProvider = providerDao.getProviderByPractitionerNo(providerNo);
		List<Provider> sendToProviderList = new LinkedList<Provider>();
		//		if (sendToProvider == null) {
		//			// Check to see if there's a match with first and last name
		//			List<Provider> potentialProviderMatchList = providerDao.getProviderLikeFirstLastName(providerFirstName, providerLastName);
		//			if (potentialProviderMatchList != null && potentialProviderMatchList.size() >= 1) {
		//				for (Provider p : potentialProviderMatchList)
		//					sendToProviderList.add(p);
		//			}
		//		} else {
		if (sendToProvider != null) {	
			sendToProviderList.add(sendToProvider);
		}
		//		}

		for (Provider p : sendToProviderList) {
						
			List<HRMDocumentToProvider> existingHRMDocumentToProviders =  hrmDocumentToProviderDao.findByHrmDocumentIdAndProviderNoList(reportId.toString(), p.getProviderNo());
			
			if (existingHRMDocumentToProviders == null || existingHRMDocumentToProviders.size() == 0) {	
				HRMDocumentToProvider providerRouting = new HRMDocumentToProvider();
				providerRouting.setHrmDocumentId(reportId.toString());
	
				providerRouting.setProviderNo(p.getProviderNo());
				providerRouting.setSignedOff(0);
	
				hrmDocumentToProviderDao.merge(providerRouting);
			}	
		}

		return sendToProviderList.size() > 0;

	}

	public static void setDocumentParent(String reportId, String childReportId) {
		HRMDocumentDao hrmDocumentDao = (HRMDocumentDao) SpringUtils.getBean("HRMDocumentDao");
		try {
			HRMDocument childDocument = hrmDocumentDao.find(childReportId);
			childDocument.setParentReport(Integer.parseInt(reportId));

			hrmDocumentDao.merge(childDocument);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Can't set HRM document parent", e);
		}
	}

	public static void routeReportToProvider(HRMDocument originalDocument, HRMReport newReport) {
		routeReportToProvider(newReport, originalDocument.getId());
	}

	public static void routeReportToProvider(String reportId, String providerNo) {
		HRMDocumentToProviderDao hrmDocumentToProviderDao = (HRMDocumentToProviderDao) SpringUtils.getBean("HRMDocumentToProviderDao");
		HRMDocumentToProvider providerRouting = new HRMDocumentToProvider();

		providerRouting.setHrmDocumentId(reportId);
		providerRouting.setProviderNo(providerNo);

		hrmDocumentToProviderDao.merge(providerRouting);

	}

	public static void signOffOnReport(String providerRoutingId, Integer signOffStatus) {
		HRMDocumentToProviderDao hrmDocumentToProviderDao = (HRMDocumentToProviderDao) SpringUtils.getBean("HRMDocumentToProviderDao");
		HRMDocumentToProvider providerRouting = hrmDocumentToProviderDao.find(providerRoutingId);

		if (providerRouting != null) {
			providerRouting.setSignedOff(signOffStatus);
			providerRouting.setSignedOffTimestamp(new Date());
			hrmDocumentToProviderDao.merge(providerRouting);
		}
	}

	public static void routeReportToDemographic(String reportId, String demographicNo) {
		HRMDocumentToDemographicDao hrmDocumentToDemographicDao = (HRMDocumentToDemographicDao) SpringUtils.getBean("HRMDocumentToDemographicDao");

		HRMDocumentToDemographic demographicRouting = new HRMDocumentToDemographic();
		demographicRouting.setDemographicNo(demographicNo);
		demographicRouting.setHrmDocumentId(reportId);
		demographicRouting.setTimeAssigned(new Date());

		hrmDocumentToDemographicDao.merge(demographicRouting);

	}
}
