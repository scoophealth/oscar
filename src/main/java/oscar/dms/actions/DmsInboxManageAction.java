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


package oscar.dms.actions;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.http.impl.cookie.DateUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.PMmodule.utility.UtilDateUtilities;
import org.oscarehr.common.dao.DocumentDao;
import org.oscarehr.common.dao.DocumentResultsDao;
import org.oscarehr.common.dao.InboxResultsDao;
import org.oscarehr.common.dao.ProviderInboxRoutingDao;
import org.oscarehr.common.dao.QueueDao;
import org.oscarehr.common.dao.QueueDocumentLinkDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProviderInboxItem;
import org.oscarehr.common.model.Queue;
import org.oscarehr.common.model.QueueDocumentLink;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarLab.ca.all.Hl7textResultsData;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.HRMResultsData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.oscarMDS.data.CategoryData;
import oscar.oscarMDS.data.PatientInfo;
import oscar.util.OscarRoleObjectPrivilege;

import com.quatro.dao.security.SecObjectNameDao;
import com.quatro.model.security.Secobjectname;

public class DmsInboxManageAction extends DispatchAction {
	
	private static Logger logger=MiscUtils.getLogger();

	private ProviderInboxRoutingDao providerInboxRoutingDAO = null;
	private QueueDocumentLinkDao queueDocumentLinkDAO = null;
	private SecObjectNameDao secObjectNameDao = null;
	private SecUserRoleDao secUserRoleDao = (SecUserRoleDao) SpringUtils.getBean("secUserRoleDao");
	private QueueDao queueDAO = (QueueDao) SpringUtils.getBean("queueDao");

	public void setProviderInboxRoutingDAO(ProviderInboxRoutingDao providerInboxRoutingDAO) {
		this.providerInboxRoutingDAO = providerInboxRoutingDAO;
	}

	public void setQueueDocumentLinkDAO(QueueDocumentLinkDao queueDocumentLinkDAO) {
		this.queueDocumentLinkDAO = queueDocumentLinkDAO;
	}

	public void setSecObjectNameDao(SecObjectNameDao secObjectNameDao) {
		this.secObjectNameDao = secObjectNameDao;
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		return null;
	}


	private void addQueueSecObjectName(String queuename, String queueid) {
		String q = "_queue.";
		if (queuename != null && queueid != null) {
			q += queueid;
			Secobjectname sbn = new Secobjectname();
			sbn.setObjectname(q);
			sbn.setDescription(queuename);
			sbn.setOrgapplicable(0);
			secObjectNameDao.saveOrUpdate(sbn);
		}
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

	public ActionForward previewPatientDocLab(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String demographicNo = request.getParameter("demog");
		String docs = request.getParameter("docs");
		String labs = request.getParameter("labs");
		String providerNo = request.getParameter("providerNo");
		String searchProviderNo = request.getParameter("searchProviderNo");
		String ackStatus = request.getParameter("ackStatus");
		ArrayList<EDoc> docPreview = new ArrayList<EDoc>();
		ArrayList<LabResultData> labPreview = new ArrayList<LabResultData>();

		if (docs.length() == 0) {
			// do nothing
		} else {
			String[] did = docs.split(",");
			List<String> didList = new ArrayList<String>();
			for (int i = 0; i < did.length; i++) {
				if (did[i].length() > 0) {
					didList.add(did[i]);
				}
			}
			if (didList.size() > 0) docPreview = EDocUtil.listDocsPreviewInbox(didList);

		}

		if (labs.length() == 0) {
			// do nothing
		} else {
			String[] labids = labs.split(",");
			List<String> ls = new ArrayList<String>();
			for (int i = 0; i < labids.length; i++) {
				if (labids.length > 0) ls.add(labids[i]);
			}

			if (ls.size() > 0) labPreview = Hl7textResultsData.getNotAckLabsFromLabNos(ls);
		}

		request.setAttribute("docPreview", docPreview);
		request.setAttribute("labPreview", labPreview);
		request.setAttribute("providerNo", providerNo);
		request.setAttribute("searchProviderNo", searchProviderNo);
		request.setAttribute("ackStatus", ackStatus);
		DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
		Demographic demographic = demographicManager.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographicNo);
		String demoName = "Not,Assigned";
		if (demographic != null) demoName = demographic.getFirstName() + "," + demographic.getLastName();
		request.setAttribute("demoName", demoName);
		return mapping.findForward("doclabPreview");
	}

	public ActionForward prepareForIndexPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		try {
			if (session.getAttribute("userrole") == null)
				response.sendRedirect("../logout.jsp");
		} catch (Exception e) {
			MiscUtils.getLogger().error("error",e);
		}

		String providerNo = (String) session.getAttribute("user");
		String searchProviderNo = request.getParameter("searchProviderNo");
		Boolean searchAll = request.getParameter("searchProviderAll") != null;
		String status = request.getParameter("status");

		boolean providerSearch = !"-1".equals(searchProviderNo);

		if (status == null) {
			status = "N";
		} // default to new labs only
		else if ("-1".equals(status)) {
			status = "";
		}
		if (providerNo == null) {
			providerNo = "";
		}
		
		if( searchAll ) {
			searchProviderNo = request.getParameter("searchProviderAll");
		}
		else if (searchProviderNo == null) {
			searchProviderNo = providerNo;
		} // default to current provider
		MiscUtils.getLogger().debug("SEARCH " + searchProviderNo);
		String patientFirstName = request.getParameter("fname");
		String patientLastName = request.getParameter("lname");
		String patientHealthNumber = request.getParameter("hnum");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		if (patientFirstName == null) {
			patientFirstName = "";
		}
		if (patientLastName == null) {
			patientLastName = "";
		}
		if (patientHealthNumber == null) {
			patientHealthNumber = "";
		}
		boolean patientSearch = !"".equals(patientFirstName) || !"".equals(patientLastName)
				|| !"".equals(patientHealthNumber);
		try {
			CategoryData cData = new CategoryData(patientLastName, patientFirstName, patientHealthNumber,
					patientSearch, providerSearch, searchProviderNo, status);
			cData.populateCountsAndPatients();
			MiscUtils.getLogger().debug("LABS " + cData.getTotalLabs());
			request.setAttribute("patientFirstName", patientFirstName);
			request.setAttribute("patientLastName", patientLastName);
			request.setAttribute("patientHealthNumber", patientHealthNumber);
			request.setAttribute("patients", new ArrayList<PatientInfo>(cData.getPatients().values()));
			request.setAttribute("unmatchedDocs", cData.getUnmatchedDocs());
			request.setAttribute("unmatchedLabs", cData.getUnmatchedLabs());
			request.setAttribute("totalDocs", cData.getTotalDocs());
			request.setAttribute("totalLabs", cData.getTotalLabs());
			request.setAttribute("abnormalCount", cData.getAbnormalCount());
			request.setAttribute("normalCount", cData.getNormalCount());
			request.setAttribute("totalNumDocs", cData.getTotalNumDocs());
			request.setAttribute("providerNo", providerNo);
			request.setAttribute("searchProviderNo", searchProviderNo);
			request.setAttribute("ackStatus", status);
			request.setAttribute("categoryHash", cData.getCategoryHash());
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			return mapping.findForward("dms_index");
		} catch (SQLException e) {
			return mapping.findForward("error");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    public ActionForward prepareForContentPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		HttpSession session = request.getSession();
		try {
			if (session.getAttribute("userrole") == null) response.sendRedirect("../logout.jsp");
		} catch (Exception e) {
			logger.error("Error", e);
		}

		// can't use userrole from session, because it changes if provider A search for provider B's documents

		// oscar.oscarMDS.data.MDSResultsData mDSData = new oscar.oscarMDS.data.MDSResultsData();
		CommonLabResultData comLab = new CommonLabResultData();
		// String providerNo = request.getParameter("providerNo");
		String providerNo = (String) session.getAttribute("user");
		String searchProviderNo = request.getParameter("searchProviderNo");
		String ackStatus = request.getParameter("status");
		String demographicNo = request.getParameter("demographicNo"); // used when searching for labs by patient instead of provider
		String scannedDocStatus = request.getParameter("scannedDocument");
		Integer page = 0;
		try {
			page = Integer.parseInt(request.getParameter("page"));
			if (page > 0) {
				page--;
			}
		} catch (NumberFormatException nfe) {
			page = 0;
		}
		Integer pageSize = 20;
		try {
			String tmp = request.getParameter("pageSize");
			pageSize = Integer.parseInt(tmp);
		} catch (NumberFormatException nfe) {
			pageSize = 20;
		}
		scannedDocStatus = "I";

		String startDateStr = request.getParameter("startDate");
		String endDateStr = request.getParameter("endDate");



		String view = request.getParameter("view");
		if (view == null || "".equals(view)) {
			view = "all";
		}

		boolean mixLabsAndDocs = "normal".equals(view) || "all".equals(view);

		Date startDate = null;
		Date endDate = null;

		try {
			startDate = UtilDateUtilities.StringToDate(startDateStr);
			endDate = UtilDateUtilities.StringToDate(endDateStr);
		} catch (Exception e) {
			startDate = null;
			endDate = null;
		}

		logger.debug("Got dates: " + startDate + "-" + endDate + " out of " + startDateStr + "-" + endDateStr);
		
		Boolean isAbnormal = null;
		if ("abnormal".equals(view))
			isAbnormal = new Boolean(true);
		if ("normal".equals(view))
			isAbnormal = new Boolean(false);

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
		String patientFirstName = request.getParameter("fname");
		String patientLastName = request.getParameter("lname");
		String patientHealthNumber = request.getParameter("hnum");

		ArrayList<LabResultData> labdocs = new ArrayList<LabResultData>();

		if (!"labs".equals(view) && !"abnormal".equals(view)) {
			labdocs = inboxResultsDao.populateDocumentResultsData(searchProviderNo, demographicNo, patientFirstName,
					patientLastName, patientHealthNumber, ackStatus, true, page, pageSize, mixLabsAndDocs, isAbnormal);
		}
		if (!"documents".equals(view)) {
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
		Date newestLab = null;
		if (request.getParameter("newestDate") != null) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				newestLab = formatter.parse(request.getParameter("newestDate"));
			} catch (Exception e) {
				logger.error("Couldn't parse date + " + request.getParameter("newestDate"), e);
			}
		}

		for (LabResultData result : labdocs) {
			if (result.getDateObj() != null) {
				if (oldestLab == null || oldestLab.compareTo(result.getDateObj()) > 0)
					oldestLab = result.getDateObj();
				if (request.getParameter("newestDate") != null && (newestLab == null || newestLab.compareTo(result.getDateObj()) < 0))
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
				List<String> abns = ab_NormalDoc.get("abnormal");
				if (abns == null) {
					abns = new ArrayList<String>();
					abns.add(data.getSegmentID());
				} else {
					abns.add(data.getSegmentID());
				}
				ab_NormalDoc.put("abnormal", abns);
			} else {
				List<String> ns = ab_NormalDoc.get("normal");
				if (ns == null) {
					ns = new ArrayList<String>();
					ns.add(data.getSegmentID());
				} else {
					ns.add(data.getSegmentID());
				}
				ab_NormalDoc.put("normal", ns);
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

		List<String> normals = ab_NormalDoc.get("normal");
		List<String> abnormals = ab_NormalDoc.get("abnormal");

		logger.debug("labdocs.size()="+labdocs.size());

		// set attributes
		request.setAttribute("pageNum", page);
		request.setAttribute("docType", docType);
		request.setAttribute("patientDocs", patientDocs);
		request.setAttribute("providerNo", providerNo);
		request.setAttribute("searchProviderNo", searchProviderNo);
		request.setAttribute("patientIdNames", patientIdNames);
		request.setAttribute("docStatus", docStatus);
		request.setAttribute("patientIdStr", patientIdStr);
		request.setAttribute("typeDocLab", typeDocLab);
		request.setAttribute("demographicNo", demographicNo);
		request.setAttribute("ackStatus", ackStatus);
		request.setAttribute("labdocs", labdocs);
		request.setAttribute("patientNumDoc", patientNumDoc);
		request.setAttribute("totalDocs", totalDocs);
		request.setAttribute("totalHL7", totalHL7);
		request.setAttribute("normals", normals);
		request.setAttribute("abnormals", abnormals);
		request.setAttribute("totalNumDocs", totalNumDocs);
		request.setAttribute("patientIdNamesStr", patientIdNamesStr);
		request.setAttribute("oldestLab", oldestLab != null ? DateUtils.formatDate(oldestLab, "yyyy-MM-dd HH:mm:ss") : null);

		return mapping.findForward("dms_page");
	}

	public ActionForward addNewQueue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		boolean success = false;
		try {
			String qn = request.getParameter("newQueueName");
			qn = qn.trim();
			if (qn != null && qn.length() > 0) {
				QueueDao queueDao = (QueueDao) SpringUtils.getBean("queueDao");
				success = queueDao.addNewQueue(qn);
				addQueueSecObjectName(qn, queueDao.getLastId());
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}

		HashMap<String, Boolean> hm = new HashMap<String, Boolean>();
		hm.put("addNewQueue", success);
		JSONObject jsonObject = JSONObject.fromObject(hm);
		try {
			response.getOutputStream().write(jsonObject.toString().getBytes());
		} catch (java.io.IOException ioe) {
			logger.error("Error", ioe);
		}
		return null;
	}

         public ActionForward isDocumentLinkedToDemographic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
                boolean success = false;
                String demoId = null;
                try {
                       String docId = request.getParameter("docId");
                       logger.debug("DocId:"+docId);
                       if (docId != null) {
                            docId = docId.trim();
                            if (docId.length() > 0) {
                                EDoc doc = EDocUtil.getDoc(docId);
                                demoId = doc.getModuleId();                                

                                if (demoId != null) {
                                    logger.debug("DemoId:"+demoId);
                                    Integer demographicId = Integer.parseInt(demoId);
                                    if (demographicId > 0) {
                                        logger.debug("Success true");
                                        success = true;
                                    }
                                }
                            }
                        }
                } catch (Exception e) {
                    logger.error("Error", e);
                }

                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("isLinkedToDemographic", success);
                hm.put("demoId", demoId);
                JSONObject jsonObject = JSONObject.fromObject(hm);
                try {
                    response.getOutputStream().write(jsonObject.toString().getBytes());
                } catch (java.io.IOException ioe) {
                    logger.error("Error", ioe);
                }

                return null;
	}
         
	public ActionForward isLabLinkedToDemographic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		boolean success = false;
		String demoId = null;
		try {
			String qn = request.getParameter("labid");
			if (qn != null) {
				qn = qn.trim();
				if (qn.length() > 0) {
					CommonLabResultData c = new CommonLabResultData();
					demoId = c.getDemographicNo(qn, "HL7");
					if (demoId != null && !demoId.equals("0")) success = true;
				}
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}

		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("isLinkedToDemographic", success);
		hm.put("demoId", demoId);
		JSONObject jsonObject = JSONObject.fromObject(hm);
		try {
			response.getOutputStream().write(jsonObject.toString().getBytes());
		} catch (java.io.IOException ioe) {
			logger.error("Error", ioe);
		}
		return null;
	}

	public ActionForward updateDocStatusInQueue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String docid = request.getParameter("docid");
		if (docid != null) {
			if (!queueDocumentLinkDAO.setStatusInactive(Integer.parseInt(docid))) {
				logger.error("failed to set status in queue document link to be inactive");
			}
		}
		return null;
	}

	// return a hastable containing queue id to queue name, a hashtable of queue id and a list of document nos.
	// forward to documentInQueus.jsp
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public ActionForward getDocumentsInQueues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		try {
			if (session.getAttribute("userrole") == null) response.sendRedirect("../logout.jsp");
		} catch (Exception e) {
			logger.error("Error", e);
		}
		String providerNo = (String) session.getAttribute("user");
		String searchProviderNo = request.getParameter("searchProviderNo");
		String ackStatus = request.getParameter("status");

		if (ackStatus == null) {
			ackStatus = "N";
		} // default to new labs only
		if (providerNo == null) {
			providerNo = "";
		}
		if (searchProviderNo == null) {
			searchProviderNo = providerNo;
		}

		StringBuilder roleName = new StringBuilder();
		List<SecUserRole> roles = secUserRoleDao.getUserRoles(searchProviderNo);
		for (SecUserRole r : roles) {
			if (roleName.length() != 0) {
				roleName.append(',');
			}
			roleName.append(r.getRoleName());
		}
		roleName.append("," + searchProviderNo);

		String patientIdNamesStr = "";
		List<QueueDocumentLink> qs = queueDocumentLinkDAO.getActiveQueueDocLink();
		HashMap<Integer, List<Integer>> queueDocNos = new HashMap<Integer, List<Integer>>();
		HashMap<Integer, String> docType = new HashMap<Integer, String>();
		HashMap<Integer, List<Integer>> patientDocs = new HashMap<Integer, List<Integer>>();
		DocumentDao documentDao = (DocumentDao) SpringUtils.getBean("documentDao");
		Demographic demo = new Demographic();
		List<Integer> docsWithPatient = new ArrayList<Integer>();
		HashMap<Integer, String> patientIdNames = new HashMap<Integer, String>();// lbData.patientName = demo.getLastName()+ ", "+demo.getFirstName();
		List<Integer> patientIds = new ArrayList<Integer>();
		Integer demoNo;
		HashMap<Integer, String> docStatus = new HashMap<Integer, String>();
		String patientIdStr = "";
		StringBuilder patientIdBud = new StringBuilder();
		HashMap<String, List<Integer>> typeDocLab = new HashMap<String, List<Integer>>();
		List<Integer> ListDocIds = new ArrayList<Integer>();
		for (QueueDocumentLink q : qs) {
			int qid = q.getQueueId();
			List<Object> vec = OscarRoleObjectPrivilege.getPrivilegeProp("_queue." + qid);
			// if queue is not default and provider doesn't have access to it,continue
			if (qid != Queue.DEFAULT_QUEUE_ID && !OscarRoleObjectPrivilege.checkPrivilege(roleName.toString(), (Properties) vec.get(0), (List) vec.get(1))) {
				continue;
			}
			int docid = q.getDocId();
			ListDocIds.add(docid);
			docType.put(docid, "DOC");
			demo = documentDao.getDemoFromDocNo(Integer.toString(docid));
			if (demo == null) demoNo = -1;
			else demoNo = demo.getDemographicNo();
			if (!patientIds.contains(demoNo)) patientIds.add(demoNo);
			if (!patientIdNames.containsKey(demoNo)) {
				if (demoNo == -1) {
					patientIdNames.put(demoNo, "Not, Assigned");
					patientIdNamesStr += ";" + demoNo + "=" + "Not, Assigned";
				} else {
					patientIdNames.put(demoNo, demo.getLastName() + ", " + demo.getFirstName());
					patientIdNamesStr += ";" + demoNo + "=" + demo.getLastName() + ", " + demo.getFirstName();
				}

			}
			List<ProviderInboxItem> providers = providerInboxRoutingDAO.getProvidersWithRoutingForDocument("DOC", docid);
			if (providers.size() > 0) {
				ProviderInboxItem pii = providers.get(0);
				docStatus.put(docid, pii.getStatus());
			}
			if (patientDocs.containsKey(demoNo)) {
				docsWithPatient = patientDocs.get(demoNo);
				docsWithPatient.add(docid);
				patientDocs.put(demoNo, docsWithPatient);
			} else {
				docsWithPatient = new ArrayList<Integer>();
				docsWithPatient.add(docid);
				patientDocs.put(demoNo, docsWithPatient);
			}
			if (queueDocNos.containsKey(qid)) {

				List<Integer> ds = queueDocNos.get(qid);
				ds.add(docid);
				queueDocNos.put(qid, ds);

			} else {
				List<Integer> ds = new ArrayList<Integer>();
				ds.add(docid);
				queueDocNos.put(qid, ds);
			}
		}
		Integer dn = 0;
		for (int i = 0; i < patientIds.size(); i++) {
			dn = patientIds.get(i);
			patientIdBud.append(dn);
			if (i != patientIds.size() - 1) patientIdBud.append(",");
		}
		patientIdStr = patientIdBud.toString();
		typeDocLab.put("DOC", ListDocIds);
		List<Integer> normals = ListDocIds;// assume all documents are normal
		List<Integer> abnormals = new ArrayList<Integer>();
		request.setAttribute("typeDocLab", typeDocLab);
		request.setAttribute("docStatus", docStatus);
		request.setAttribute("patientDocs", patientDocs);
		request.setAttribute("patientIdNames", patientIdNames);
		request.setAttribute("docType", docType);
		request.setAttribute("patientIds", patientIds);
		request.setAttribute("patientIdStr", patientIdStr);
		request.setAttribute("normals", normals);
		request.setAttribute("abnormals", abnormals);
		request.setAttribute("queueDocNos", queueDocNos);
		request.setAttribute("patientIdNamesStr", patientIdNamesStr);
		request.setAttribute("queueIdNames", queueDAO.getHashMapOfQueues());
		request.setAttribute("providerNo", providerNo);
		request.setAttribute("searchProviderNo", searchProviderNo);
		return mapping.findForward("document_in_queues");

	}
}
