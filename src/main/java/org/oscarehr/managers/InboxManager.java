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
package org.oscarehr.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.PMmodule.utility.UtilDateUtilities;
import org.oscarehr.common.dao.DocumentResultsDao;
import org.oscarehr.common.dao.InboxResultsDao;
import org.oscarehr.common.dao.QueueDocumentLinkDao;
import org.oscarehr.common.model.Queue;
import org.oscarehr.common.model.QueueDocumentLink;
import org.oscarehr.inbox.InboxManagerQuery;
import org.oscarehr.inbox.InboxManagerResponse;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.HRMResultsData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.OscarRoleObjectPrivilege;

@Service
public class InboxManager {
	
	private Logger logger = MiscUtils.getLogger();
	
	@Autowired
	private QueueDocumentLinkDao queueDocumentLinkDAO;
	@Autowired
	private SecUserRoleDao secUserRoleDao ;
	
	public static final String NORMAL = "normal";
	public static final String ALL = "all";
	public static final String ABNORMAL = "abnormal";
	public static final String LABS = "labs";
	public static final String DOCUMENTS = "documents";
	

	public InboxManagerResponse getInboxResults(LoggedInInfo loggedInInfo, InboxManagerQuery query) {
		CommonLabResultData comLab = new CommonLabResultData();
		String providerNo =  loggedInInfo.getLoggedInProviderNo();
		String searchProviderNo = query.getSearchProviderNo();
		String ackStatus = query.getStatus();
		String demographicNo = query.getDemographicNo()!=null?query.getDemographicNo().toString():null;
		String scannedDocStatus = query.getScannedDocStatus();
		Integer page = 0;
		page = query.getPage();
		//why it does this , I have no idea.
		if (page > 0) {
			page--;
		}
		Integer pageSize = query.getPageSize();
		
		scannedDocStatus = query.getScannedDocStatus();


		String view = query.getView();
		if (view == null || "".equals(view)) {
			view = ALL;
		}

		boolean mixLabsAndDocs = NORMAL.equals(view) || ALL.equals(view);

		Date startDate = query.getStartDate();
		Date endDate = query.getEndDate();

		
		//logger.info("Got dates: " + startDate + "-" + endDate + " out of " + startDateStr + "-" + endDateStr);
		
		Boolean isAbnormal = null;
		if (ABNORMAL.equals(view))
			isAbnormal = true;
		if (NORMAL.equals(view))
			isAbnormal = false;

		if (ackStatus == null) {
			ackStatus = "N";
			
		} // default to new labs only
		if (providerNo == null) {
			providerNo = "";
		}
		if (searchProviderNo == null) {
			searchProviderNo = providerNo;
		}
		String roleName = "";
		List<SecUserRole> roles = secUserRoleDao.getUserRoles(searchProviderNo);
		for (SecUserRole r : roles) {
			if (roleName.length() == 0) {
				roleName = r.getRoleName();

			} else {
				roleName += "," + r.getRoleName();
			}
		}
		roleName += "," + searchProviderNo;

		List<QueueDocumentLink> qd = queueDocumentLinkDAO.getQueueDocLinks();
		HashMap<String, String> docQueue = new HashMap<String, String>();
		for (QueueDocumentLink qdl : qd) {
			Integer i = qdl.getDocId();
			Integer n = qdl.getQueueId();
			docQueue.put(i.toString(), n.toString());
		}

		InboxResultsDao inboxResultsDao = (InboxResultsDao) SpringUtils.getBean("inboxResultsDao");
		String patientFirstName = query.getPatientFirstName();
		String patientLastName = query.getPatientLastName();
		String patientHealthNumber = query.getPatientHIN();

		ArrayList<LabResultData> labdocs = new ArrayList<LabResultData>();

		if (!LABS.equals(view) && !ABNORMAL.equals(view)) {
			labdocs = inboxResultsDao.populateDocumentResultsData(searchProviderNo, demographicNo, patientFirstName,
					patientLastName, patientHealthNumber, ackStatus, true, page, pageSize, mixLabsAndDocs, isAbnormal);
		}
		if (!DOCUMENTS.equals(view)) {
			labdocs.addAll(comLab.populateLabResultsData(loggedInInfo, searchProviderNo, demographicNo, patientFirstName,
					patientLastName, patientHealthNumber, ackStatus, scannedDocStatus, true, page, pageSize,
					mixLabsAndDocs, isAbnormal));
		}

		ArrayList<LabResultData> validlabdocs = new ArrayList<LabResultData>();

		DocumentResultsDao documentResultsDao = (DocumentResultsDao) SpringUtils.getBean("documentResultsDao");
		// check privilege for documents only
		for (LabResultData data : labdocs) {
			if (data.isDocument()) {
				String docid = data.getSegmentID();

				String queueid = docQueue.get(docid);
				if (queueid != null) {
					queueid = queueid.trim();

					int queueIdInt = Integer.parseInt(queueid);

					// if doc sent to default queue and no valid provider, do NOT include it
					if (queueIdInt == Queue.DEFAULT_QUEUE_ID && !documentResultsDao.isSentToValidProvider(docid) && isSegmentIDUnique(validlabdocs, data)) {
						// validlabdocs.add(data);
					}
					// if doc sent to default queue && valid provider, check if it's sent to this provider, if yes include it
					else if (queueIdInt == Queue.DEFAULT_QUEUE_ID && documentResultsDao.isSentToValidProvider(docid) && documentResultsDao.isSentToProvider(docid, searchProviderNo) && isSegmentIDUnique(validlabdocs, data)) {
						validlabdocs.add(data);
					}
					// if doc setn to non-default queue and valid provider, check if provider is in the queue or equal to the provider
					else if (queueIdInt != Queue.DEFAULT_QUEUE_ID && documentResultsDao.isSentToValidProvider(docid)) {
						Vector<Object> vec = OscarRoleObjectPrivilege.getPrivilegeProp("_queue." + queueid);
						if (OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) vec.get(0), (Vector) vec.get(1)) || documentResultsDao.isSentToProvider(docid, searchProviderNo)) {
							// labs is in provider's queue,do nothing
							if (isSegmentIDUnique(validlabdocs, data)) {
								validlabdocs.add(data);
							}
						}
					}
					// if doc sent to non default queue and no valid provider, check if provider is in the non default queue
					else if (!queueid.equals(Queue.DEFAULT_QUEUE_ID) && !documentResultsDao.isSentToValidProvider(docid)) {
						Vector<Object> vec = OscarRoleObjectPrivilege.getPrivilegeProp("_queue." + queueid);
						if (OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) vec.get(0), (Vector) vec.get(1))) {
							// labs is in provider's queue,do nothing
							if (isSegmentIDUnique(validlabdocs, data)) {
								validlabdocs.add(data);
							}

						}
					}
				}
			} else {// add lab
				if (isSegmentIDUnique(validlabdocs, data)) {
					validlabdocs.add(data);
				}
			}
		}

		// Find the oldest lab returned in labdocs, use that as the limit date for the HRM query
		Date oldestLab = null;
		Date newestLab = query.getNewestDate();
		
		for (LabResultData result : labdocs) {
			if (result.getDateObj() != null) {
				if (oldestLab == null || oldestLab.compareTo(result.getDateObj()) > 0)
					oldestLab = result.getDateObj();
				if (query.getNewestDate() != null && (newestLab == null || newestLab.compareTo(result.getDateObj()) < 0))
					newestLab = result.getDateObj();
			}
		}

		HRMResultsData hrmResult = new HRMResultsData();

		Collection<LabResultData> hrmDocuments = hrmResult.populateHRMdocumentsResultsData(loggedInInfo, searchProviderNo, ackStatus, newestLab, oldestLab);
		if (oldestLab == null) {
			for (LabResultData hrmDocument : hrmDocuments) {
				if (oldestLab == null || (hrmDocument.getDateObj() != null && oldestLab.compareTo(hrmDocument.getDateObj()) > 0))
					oldestLab = hrmDocument.getDateObj();
			}
		}

		labdocs.addAll(hrmDocuments);
		Collections.sort(labdocs);

		HashMap<String,LabResultData> labMap = new HashMap<String,LabResultData>();
		LinkedHashMap<String,ArrayList<String>> accessionMap = new LinkedHashMap<String,ArrayList<String>>();

		int accessionNumCount = 0;
		for (LabResultData result : labdocs) {
			if (startDate != null && startDate.after(result.getDateObj())) {
				continue;
			}

			if (endDate != null && endDate.before(result.getDateObj())) {
				continue;
			}

			String segmentId = result.getSegmentID();
			if (result.isDocument())
				segmentId += "d";
			else if (result.isHRM())
				segmentId += "h";

			labMap.put(segmentId, result);
			ArrayList<String> labNums = new ArrayList<String>();

			if (result.accessionNumber == null || result.accessionNumber.equals("")) {
				labNums.add(segmentId);
				accessionNumCount++;
				accessionMap.put("noAccessionNum" + accessionNumCount + result.labType, labNums);
			} else if (!accessionMap.containsKey(result.accessionNumber + result.labType)) {
				labNums.add(segmentId);
				accessionMap.put(result.accessionNumber + result.labType, labNums);

				// Different MDS Labs may have the same accession Number if they are seperated
				// by two years. So accession numbers are limited to matching only if their
				// labs are within one year of eachother
			} else {
				labNums = accessionMap.get(result.accessionNumber + result.labType);
				boolean matchFlag = false;
				for (int j = 0; j < labNums.size(); j++) {
					LabResultData matchingResult = labMap.get(labNums.get(j));

					Date dateA = result.getDateObj();
					Date dateB = matchingResult.getDateObj();
					int monthsBetween = 0;
					if (dateA == null || dateB == null) {
						monthsBetween = 5;
					} else if (dateA.before(dateB)) {
						monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
					} else {
						monthsBetween = UtilDateUtilities.getNumMonths(dateB, dateA);
					}

					if (monthsBetween < 4) {
						matchFlag = true;
						break;
					}
				}
				if (!matchFlag) {
					labNums.add(segmentId);
					accessionMap.put(result.accessionNumber + result.labType, labNums);
				}
			}
		}

		labdocs.clear();

		for (ArrayList<String> labNums : accessionMap.values()) {
			// must sort through in reverse to keep the labs in the correct order
			for (int j = labNums.size() - 1; j >= 0; j--) {
				labdocs.add(labMap.get(labNums.get(j)));
			}
		}
		logger.debug("labdocs.size()="+labdocs.size());

		/* find all data for the index.jsp page */
		Hashtable patientDocs = new Hashtable();
		Hashtable patientIdNames = new Hashtable();
		String patientIdNamesStr = "";
		Hashtable docStatus = new Hashtable();
		Hashtable docType = new Hashtable();
		Hashtable<String, List<String>> ab_NormalDoc = new Hashtable();

		for (int i = 0; i < labdocs.size(); i++) {
			LabResultData data = labdocs.get(i);

			List<String> segIDs = new ArrayList<String>();
			String labPatientId = data.getLabPatientId();
			if (labPatientId == null || labPatientId.equals("-1")) labPatientId = "-1";

			if (data.isAbnormal()) {
				List<String> abns = ab_NormalDoc.get(ABNORMAL);
				if (abns == null) {
					abns = new ArrayList<String>();
					abns.add(data.getSegmentID());
				} else {
					abns.add(data.getSegmentID());
				}
				ab_NormalDoc.put(ABNORMAL, abns);
			} else {
				List<String> ns = ab_NormalDoc.get(NORMAL);
				if (ns == null) {
					ns = new ArrayList<String>();
					ns.add(data.getSegmentID());
				} else {
					ns.add(data.getSegmentID());
				}
				ab_NormalDoc.put(NORMAL, ns);
			}
			if (patientDocs.containsKey(labPatientId)) {

				segIDs = (List) patientDocs.get(labPatientId);
				segIDs.add(data.getSegmentID());
				patientDocs.put(labPatientId, segIDs);
			} else {
				segIDs.add(data.getSegmentID());
				patientDocs.put(labPatientId, segIDs);
				patientIdNames.put(labPatientId, data.patientName);
				patientIdNamesStr += ";" + labPatientId + "=" + data.patientName;
			}
			docStatus.put(data.getSegmentID(), data.getAcknowledgedStatus());
			docType.put(data.getSegmentID(), data.labType);
		}

		Integer totalDocs = 0;
		Integer totalHL7 = 0;
		Hashtable<String, List<String>> typeDocLab = new Hashtable();
		Enumeration keys = docType.keys();
		while (keys.hasMoreElements()) {
			String keyDocLabId = ((String) keys.nextElement());
			String valType = (String) docType.get(keyDocLabId);

			if (valType.equalsIgnoreCase("DOC")) {
				if (typeDocLab.containsKey("DOC")) {
					List<String> docids = typeDocLab.get("DOC");
					docids.add(keyDocLabId);// add doc id to list
					typeDocLab.put("DOC", docids);
				} else {
					List<String> docids = new ArrayList<String>();
					docids.add(keyDocLabId);
					typeDocLab.put("DOC", docids);
				}
				totalDocs++;
			} else if (valType.equalsIgnoreCase("HL7")) {
				if (typeDocLab.containsKey("HL7")) {
					List<String> hl7ids = typeDocLab.get("HL7");
					hl7ids.add(keyDocLabId);
					typeDocLab.put("HL7", hl7ids);
				} else {
					List<String> hl7ids = new ArrayList<String>();
					hl7ids.add(keyDocLabId);
					typeDocLab.put("HL7", hl7ids);
				}
				totalHL7++;
			}
		}

		Hashtable patientNumDoc = new Hashtable();
		Enumeration patientIds = patientDocs.keys();
		String patientIdStr = "";
		Integer totalNumDocs = 0;
		while (patientIds.hasMoreElements()) {
			String key = (String) patientIds.nextElement();
			patientIdStr += key;
			patientIdStr += ",";
			List<String> val = (List<String>) patientDocs.get(key);
			Integer numDoc = val.size();
			patientNumDoc.put(key, numDoc);
			totalNumDocs += numDoc;
		}

		List<String> normals = ab_NormalDoc.get(NORMAL);
		List<String> abnormals = ab_NormalDoc.get(ABNORMAL);

		logger.debug("labdocs.size()="+labdocs.size());

		InboxManagerResponse response = new InboxManagerResponse();
		response.setPageNum(page);
		response.setDocType(docType);
		response.setPatientDocs(patientDocs);
		response.setProviderNo(providerNo);
		response.setSearchProviderNo(searchProviderNo);
		response.setPatientIdNames(patientIdNames);
		response.setDocStatus(docStatus);
		response.setPatientIdStr(patientIdStr);
		response.setTypeDocLab(typeDocLab);
		response.setDemographicNo(demographicNo!=null?Integer.parseInt(demographicNo):null);
		response.setAckStatus(ackStatus);
		response.setLabdocs(labdocs);
		response.setPatientNumDoc(patientNumDoc);
		response.setTotalDocs(totalDocs);
		response.setTotalHl7(totalHL7);
		response.setNormals(normals);
		response.setAbnormals(abnormals);
		response.setTotalNumDocs(totalNumDocs);
		response.setPatientIdNamesStr(patientIdNamesStr);
		response.setOldestLab(oldestLab);
	
		return response;
	}
	
	private boolean isSegmentIDUnique(ArrayList<LabResultData> doclabs, LabResultData data) {
		boolean unique = true;
		String sID = (data.segmentID).trim();
		for (int i = 0; i < doclabs.size(); i++) {
			LabResultData lrd = doclabs.get(i);
			if (sID.equals((lrd.segmentID).trim())) {
				unique = false;
				break;
			}
		}
		return unique;
	}
}


