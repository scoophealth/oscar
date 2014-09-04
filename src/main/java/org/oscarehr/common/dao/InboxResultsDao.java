/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.http.impl.cookie.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.springframework.transaction.annotation.Transactional;

import oscar.oscarLab.ca.on.LabResultData;

@Transactional
public class InboxResultsDao {

	Logger logger = Logger.getLogger(InboxResultsDao.class);

	@PersistenceContext
	protected EntityManager entityManager = null;

	/** Creates a new instance of Hl7textResultsData */
	public InboxResultsDao() {
	}

	/**
	 * Populates ArrayList with labs attached to a consultation request
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public ArrayList populateHL7ResultsData(String demographicNo, String consultationId, boolean attached) {
		String sql = "SELECT hl7.label, hl7.lab_no, hl7.obr_date, hl7.discipline, hl7.accessionNum, hl7.final_result_count, patientLabRouting.id "
				+ "FROM hl7TextInfo hl7, patientLabRouting "
				+ "WHERE patientLabRouting.lab_no = hl7.lab_no "
				+ "AND patientLabRouting.lab_type = 'HL7' AND patientLabRouting.demographic_no="
				+ demographicNo
				+ " GROUP BY hl7.lab_no";

		String attachQuery = "SELECT consultdocs.document_no FROM consultdocs, patientLabRouting "
				+ "WHERE patientLabRouting.id = consultdocs.document_no AND " + "consultdocs.requestId = "
				+ consultationId
				+ " AND consultdocs.doctype = 'L' AND consultdocs.deleted IS NULL ORDER BY consultdocs.document_no";

		ArrayList labResults = new ArrayList<LabResultData>();
		ArrayList attachedLabs = new ArrayList<LabResultData>();


		try {
			Query q = entityManager.createNativeQuery(attachQuery);

			List<Object[]> result = q.getResultList();
			for (Object[] r : result) {
				LabResultData lbData = new LabResultData(LabResultData.HL7TEXT);
				lbData.labPatientId = (String) r[0];
				attachedLabs.add(lbData);
			}

			LabResultData lbData = new LabResultData(LabResultData.HL7TEXT);
			LabResultData.CompareId c = lbData.getComparatorId();

			q = entityManager.createNativeQuery(sql);
			result = q.getResultList();
			for (Object[] r : result) {
				lbData.segmentID = (String) r[1];
				lbData.labPatientId = (String) r[6];
				lbData.dateTime = (String) r[2];
				lbData.discipline = (String) r[3];
				lbData.accessionNumber = (String) r[4];
				lbData.finalResultsCount = (Integer) r[5];
				lbData.label = (String) r[0];

				if (attached && Collections.binarySearch(attachedLabs, lbData, c) >= 0)
					labResults.add(lbData);
				else if (!attached && Collections.binarySearch(attachedLabs, lbData, c) < 0)
					labResults.add(lbData);

				lbData = new LabResultData(LabResultData.HL7TEXT);
			}
		} catch (Exception e) {
			logger.error("exception in HL7Populate", e);
		}


		return labResults;
	}

	@SuppressWarnings("unchecked")
	public boolean isSentToProvider(String docNo, String providerNo) {
		if (docNo != null && providerNo != null) {
			int dn = Integer.parseInt(docNo.trim());
			providerNo = providerNo.trim();
			String sql = "select * from providerLabRouting plr where plr.lab_type='DOC' and plr.lab_no=" + dn
					+ " and plr.provider_no='" + providerNo + "'";
			try {

				Query q = entityManager.createNativeQuery(sql);
				List<Object[]> rs = q.getResultList();

				logger.debug(sql);
				if (!rs.isEmpty()) {
					return true;
				} else
					return false;
			} catch (Exception e) {
				logger.error(e.toString());
				return false;
			}
		} else {
			return false;
		}
	}

	//retrieve all documents from database
	/**
	 * Wrapper function for non paged document queries.
	 */
	@SuppressWarnings("rawtypes")
    public ArrayList populateDocumentResultsData(String providerNo, String demographicNo, String patientFirstName,
			String patientLastName, String patientHealthNumber, String status) {
		return populateDocumentResultsData(providerNo, demographicNo, patientFirstName, patientLastName,
				patientHealthNumber, status, false, null, null, false, null);
	}

	@SuppressWarnings({ "unchecked" })
	public ArrayList<LabResultData> populateDocumentResultsData(String providerNo, String demographicNo, String patientFirstName,
			String patientLastName, String patientHealthNumber, String status, boolean isPaged, Integer page,
			Integer pageSize, boolean mixLabsAndDocs, Boolean isAbnormal) {
		if (providerNo == null) {
			providerNo = "";
		}
		boolean searchProvider = !"-1".equals(providerNo);
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
		if (status == null) {
			status = "";
		}

		ArrayList<LabResultData> labResults = new ArrayList<LabResultData>();
		String sql = "";

		int idLoc = 0;
		int docNoLoc = 1;
		int statusLoc = 2;
		int docTypeLoc = 3;
		int lastNameLoc = 4;
		int firstNameLoc = 5;
		int hinLoc = 6;
		int sexLoc = 7;
		int moduleLoc = 8;
		int obsDateLoc = 9;
		
		int priorityLoc = 10;
		int requestingClientLoc = 11;
		int disciplineLoc = 12;
		int accessionNumLoc = 13;
		int finalResultCountLoc = 14;
		
		try {

			// Get documents by demographic
			//if (demographicNo != null && !"".equals(demographicNo)) {
			// Get mix from labs
			boolean isUnclaimedQuery = false;
			if (mixLabsAndDocs) {
				if (demographicNo == null && "0".equals(providerNo)) {
					UnclaimedInboxQueryBuilder unclaimedQuery = new UnclaimedInboxQueryBuilder();
					unclaimedQuery.setPage(page);
					unclaimedQuery.setPaged(isPaged);
					unclaimedQuery.setPageSize(pageSize);
					unclaimedQuery.setMixLabsAndDocs(true);
					sql = unclaimedQuery.buildQuery();
					isUnclaimedQuery = true;
				} else if ("0".equals(demographicNo) || "0".equals(providerNo)) {
					sql = " SELECT X.id, X.lab_no as document_no, X.status, X.lab_type as doctype, d.last_name, d.first_name, hin, sex, d.demographic_no as module_id, doc.observationdate "
							+ " FROM document doc, "
							+ " (SELECT plr.id, plr.lab_type, plr.lab_no, plr.status "
							+ "  FROM patientLabRouting plr2, providerLabRouting plr, hl7TextInfo info "
							+ "  WHERE plr.lab_no = plr2.lab_no "
							+ (searchProvider ? " AND plr.provider_no = '" + providerNo + "' " : " ")
							+ "    AND plr.status like '%"
							+ status
							+ "%' "
							+ "    AND plr.lab_type = 'HL7'   "
							+ "    AND plr2.lab_type = 'HL7' "
							+ "    AND plr2.demographic_no = '0' "
							+ "    AND info.lab_no = plr.lab_no "
							+ (isAbnormal != null && isAbnormal ? "AND info.result_status = 'A'" : isAbnormal != null
									&& !isAbnormal ? "AND (info.result_status IS NULL OR info.result_status != 'A')" : "")
							+ " UNION "
							+ " SELECT plr.id, plr.lab_type, plr.lab_no, plr.status "
							+ " FROM providerLabRouting plr  "
							+ " WHERE plr.lab_type = 'DOC' AND plr.status like '%"
							+ status
							+ "%' "
							+ (searchProvider ? " AND plr.provider_no = '" + providerNo + "' " : " ")
							+ " ORDER BY id DESC "
							+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "")
							+ " ) AS X "
							+ " LEFT JOIN demographic d "
							+ " ON d.demographic_no = -1 "
							+ " WHERE X.lab_type = 'DOC' AND doc.document_no = X.lab_no ";

				} else if (demographicNo != null && !"".equals(demographicNo)) {
					sql = " SELECT plr.id, doc.document_no, plr.status, d.last_name, d.first_name, hin, sex, d.demographic_no as module_id, doc.observationdate, plr.lab_type as doctype "
							+ " FROM demographic d, providerLabRouting plr, document doc, "
							+ " (SELECT * FROM "
							+ " (SELECT DISTINCT plr.id, plr.lab_type  FROM providerLabRouting plr, ctl_document cd "
							+ " WHERE 	" + " (cd.module_id = '"
							+ demographicNo
							+ "' "
							+ "	AND cd.document_no = plr.lab_no"
							+ "	AND plr.lab_type = 'DOC'  	"
							+ "	AND plr.status like '%"
							+ status
							+ "%' "
							+ (searchProvider ? " AND plr.provider_no = '" + providerNo + "' )" : " )")
							+ " ORDER BY id DESC) AS Y"
							+ " UNION"
							+ " SELECT * FROM"
							+ " (SELECT DISTINCT plr.id, plr.lab_type  FROM providerLabRouting plr, patientLabRouting plr2"
							+ " WHERE"
							+ "	plr.lab_type = 'HL7' AND plr2.lab_type = 'HL7'"
							+ "	AND plr.status like '%"
							+ status
							+ "%' "
							+ (searchProvider ? " AND plr.provider_no = '" + providerNo + "' " : " ")
							+ " 	AND plr.lab_no = plr2.lab_no AND plr2.demographic_no = '"
							+ demographicNo
							+ "'"
							+ " ORDER BY id DESC) AS Z"
							+ " ORDER BY id DESC "
							+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "")
							+ " ) AS X "
							+ " WHERE X.lab_type = 'DOC' and X.id = plr.id and doc.document_no = plr.lab_no and d.demographic_no = '"
							+ demographicNo + "' ";
				} else if (patientSearch) { // N arg
					sql = " SELECT plr.id, doc.document_no, plr.status, d.last_name, d.first_name, hin, sex, d.demographic_no as module_id, doc.observationdate, plr.lab_type as doctype "
							+ " FROM demographic d, providerLabRouting plr, document doc,  "
							+ " (SELECT * FROM "
							+ "	 (SELECT * FROM  "
							+ "		(SELECT DISTINCT plr.id, plr.lab_type, d.demographic_no "
							+ "			FROM providerLabRouting plr, ctl_document cd, demographic d "
							+ "			WHERE 	 "
							+ "			(d.first_name like '%"
							+ patientFirstName
							+ "%' AND d.last_name like '%"
							+ patientLastName
							+ "%' AND d.hin like '%"
							+ patientHealthNumber
							+ "%' "
							+ "		AND cd.module_id = d.demographic_no 	AND cd.document_no = plr.lab_no	AND plr.lab_type = 'DOC' "
							+ "				AND plr.status like '%"
							+ status
							+ "%' "
							+ (searchProvider ? " AND plr.provider_no = '" + providerNo + "' " : " ")
							+ "		) ORDER BY id DESC) AS Y "
							+ " 	UNION "
							+ "	SELECT * FROM "
							+ "		(SELECT DISTINCT plr.id, plr.lab_type, d.demographic_no "
							+ "		FROM providerLabRouting plr, patientLabRouting plr2, demographic d"
							+ (isAbnormal != null ? ", hl7TextInfo info " : " ")
							+ "		WHERE d.first_name like '%"
							+ patientFirstName
							+ "%' AND d.last_name like '%"
							+ patientLastName
							+ "%' AND d.hin like '%"
							+ patientHealthNumber
							+ "%' "
							+ "		AND	plr.lab_type = 'HL7' AND plr2.lab_type = 'HL7' "
							+ (isAbnormal != null ? " AND plr.lab_no = info.lab_no AND (info.result_status IS NULL OR info.result_status != 'A') "
									: " ")
							+ "				AND plr.status like '%"
							+ status
							+ "%' "
							+ (searchProvider ? " AND plr.provider_no = '" + providerNo + "' " : " ")
							+ " 	AND plr.lab_no = plr2.lab_no AND plr2.demographic_no = d.demographic_no ORDER BY id DESC) AS Z "
							+ " 			ORDER BY id DESC) AS X "
							+ " 	  ) AS Z  "
							+ " WHERE Z.lab_type = 'DOC' and Z.id = plr.id and doc.document_no = plr.lab_no and d.demographic_no = Z.demographic_no "
							+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "");

				} else {
					sql = " SELECT doc.document_no as id, plr.status, last_name, first_name, hin, sex, module_id, observationdate, plr.lab_type as doctype, doc.document_no"
							+ " FROM document doc, "
							+ " 	(SELECT DISTINCT plr.* FROM providerLabRouting plr"
							+ (isAbnormal != null ? ", hl7TextInfo info " : "")
							+ " 	WHERE plr.status like '%"
							+ status
							+ "%' "
							+ (searchProvider ? " AND plr.provider_no = '" + providerNo + "' " : "")
							// The only time abnormal matters for documents is when we are looking for normal documents since there are no abnormal documents.
							+ (isAbnormal != null ? "     AND (plr.lab_type = 'DOC' OR (plr.lab_no = info.lab_no AND (info.result_status IS NULL OR info.result_status != 'A'))) "
									: " ")
							+ " 	ORDER BY id DESC "
							+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "")
							+ "     ) AS plr"
							+ " LEFT JOIN "
							+ "(SELECT module_id, document_no FROM ctl_document cd "
							+ "WHERE cd.module = 'demographic' AND cd.module_id != '-1') AS Y "
							+ "ON plr.lab_type = 'DOC' AND plr.lab_no = Y.document_no"
							+ " LEFT JOIN "
							+ "(SELECT demographic_no, first_name, last_name, hin, sex "
							+ "FROM demographic d) AS Z "
							+ "ON Y.module_id = Z.demographic_no "
							+ "WHERE doc.document_no = plr.lab_no AND plr.lab_type = 'DOC'";
				}
			} else { // Don't mix labs and docs.
				if ("0".equals(demographicNo) || "0".equals(providerNo)) {
					sql = " SELECT id, document_no, status, demographic_no as module_id, observationdate, doctype, last_name, first_name, hin, sex"
							+ " FROM "
							+ " (SELECT plr.id, doc.document_no, plr.status, observationdate, plr.lab_type as doctype"
							+ " FROM providerLabRouting plr, document doc"
							+ " WHERE plr.lab_type = 'DOC' "
							+ " AND plr.status like '%"
							+ status
							+ "%'  "
							+ " AND plr.provider_no = '0' "
							+ " AND doc.document_no = plr.lab_no"
							+ " ORDER BY id DESC 	"
							+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "")
							+ ") as X"
							+ " LEFT JOIN demographic d" + " ON d.demographic_no = -1";
				} else if (demographicNo != null && !"".equals(demographicNo)) {
					sql = "SELECT plr.id, doc.document_no, plr.status, last_name, first_name, hin, sex, module_id, observationdate, plr.lab_type as doctype "
							+ "FROM ctl_document cd, demographic d, providerLabRouting plr, document doc "
							+ "WHERE d.demographic_no = '"
							+ demographicNo
							+ "' "
							+ "	AND cd.module_id = d.demographic_no "
							+ "	AND cd.document_no = plr.lab_no "
							+ "	AND plr.lab_type = 'DOC' "
							+ "	AND plr.status like '%"
							+ status
							+ "%' "
							+ (searchProvider ? " AND plr.provider_no = '" + providerNo + "' " : "")
							+ "	AND doc.document_no = cd.document_no "
							+ " ORDER BY id DESC "
							+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "");
				} else if (patientSearch) {
					sql = "SELECT plr.id, doc.document_no, plr.status, last_name, first_name, hin, sex, module_id, observationdate, plr.lab_type as doctype "
							+ "FROM ctl_document cd, demographic d, providerLabRouting plr, document doc "
							+ "WHERE d.first_name like '%"
							+ patientFirstName
							+ "%' AND d.last_name like '%"
							+ patientLastName
							+ "%' AND d.hin like '%"
							+ patientHealthNumber
							+ "%' "
							+ "	AND cd.module_id = d.demographic_no "
							+ "	AND cd.document_no = plr.lab_no "
							+ "	AND plr.lab_type = 'DOC' "
							+ "	AND plr.status like '%"
							+ status
							+ "%' "
							+ (searchProvider ? " AND plr.provider_no = '" + providerNo + "' " : "")
							+ "	AND doc.document_no = cd.document_no "
							+ " ORDER BY id DESC "
							+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "");
				} else {
					sql = " SELECT * "
							+ " FROM (SELECT plr.id, doc.document_no, plr.status, last_name, first_name, hin, sex, module_id, observationdate, plr.lab_type as doctype "
							+ " FROM ctl_document cd, demographic d, providerLabRouting plr, document doc "
							+ " WHERE (cd.module_id = d.demographic_no) "
							+ " 	AND cd.document_no = plr.lab_no "
							+ " 	AND plr.lab_type = 'DOC' "
							+ "	AND plr.status like '%"
							+ status
							+ "%'  "
							+ (searchProvider ? " AND plr.provider_no = '" + providerNo + "' " : "")
							+ " 	AND doc.document_no = cd.document_no  "
							+ " UNION "
							+ " SELECT X.id, X.lab_no as document_no, X.status, last_name, first_name, hin, sex, X.module_id, X.observationdate, X.lab_type as doctype "
							+ " FROM (SELECT plr.id, plr.lab_no, plr.status, plr.lab_type, cd.module_id, observationdate "
							+ " FROM ctl_document cd, providerLabRouting plr, document d "
							+ " WHERE plr.lab_type = 'DOC' " + "	AND plr.status like '%" + status + "%'  "
							+ (searchProvider ? " AND plr.provider_no = '" + providerNo + "' " : "")
							+ " AND plr.lab_no = cd.document_no " + " AND cd.module_id = -1 "
							+ " AND d.document_no = cd.document_no " + " ) AS X " + " LEFT JOIN demographic d "
							+ " ON d.demographic_no = -1) AS X " + " ORDER BY id DESC "
							+ (isPaged ? "	LIMIT " + (page * pageSize) + "," + pageSize : "");
				}
			}

			logger.debug(sql);

			Query q = entityManager.createNativeQuery(sql);
			configureScalars(q, isUnclaimedQuery);
			
			List<Object[]> result = q.getResultList();
			for (Object[] r : result) {
				
				if (logger.isDebugEnabled()) {
					logger.debug("idLoc = " + idLoc + ", docNoLoc = " + docNoLoc + ", statusLoc = " + statusLoc + ", docTypeLoc = " + docTypeLoc 
							+ ", lastNameLoc = " + lastNameLoc + ", firstNameLoc = " + firstNameLoc + ", hinLoc = " + hinLoc + ", sexLoc = " 
							+ sexLoc + ", moduleLoc = " + moduleLoc + ", obsDateLoc = " + obsDateLoc + ", priorityLoc = " + priorityLoc 
							+ ", requestingClientLoc = " + requestingClientLoc + ", disciplineLoc = " + disciplineLoc + ", accessionNumLoc = "
							+ accessionNumLoc + ", finalResultCountLoc = " + finalResultCountLoc);
					logger.debug(Arrays.toString(r));
				}
				
				LabResultData lbData = new LabResultData(LabResultData.DOCUMENT);
				lbData.labType = LabResultData.DOCUMENT;
				String labType = getStringValue(r[docTypeLoc]);
				if (labType == null) {
					lbData.labType = LabResultData.DOCUMENT;
				} else {
					lbData.labType = labType;
				}


				lbData.segmentID = getStringValue(r[docNoLoc]);

				if (demographicNo == null && !providerNo.equals("0")) {
					lbData.acknowledgedStatus = getStringValue(r[statusLoc]);
				} else {
					lbData.acknowledgedStatus = "U";
				}

				lbData.healthNumber = "";
				lbData.patientName = "Not, Assigned";
				lbData.sex = "";

				lbData.isMatchedToPatient = r[lastNameLoc] != null;

				if (lbData.isMatchedToPatient) {
					lbData.patientName = getStringValue(r[lastNameLoc]) + ", " + getStringValue(r[firstNameLoc]);
					lbData.healthNumber = getStringValue(r[hinLoc]);
					lbData.sex = getStringValue(r[sexLoc]);
					lbData.setLabPatientId(getStringValue(r[moduleLoc]));

				}else {
					lbData.patientName = "Not, Assigned";
				}

				logger.debug("DOCUMENT " + lbData.isMatchedToPatient());
				lbData.accessionNumber = "";
				lbData.resultStatus = "N";
				
				// status seem to matter only for labs - so A for documents
				// means acknowledged, not abnormal :)
				if (!LabResultData.DOCUMENT.equals(lbData.labType)) {
					String statusString = getStringValue(r[statusLoc]);
					if (statusString != null) {
						lbData.resultStatus = statusString;
					}
	
					if (lbData.resultStatus.equals("A")) {
						lbData.abn = true;
					}
				}

				lbData.dateTime = getStringValue(r[obsDateLoc]);
				
				String obsDateString = getStringValue(r[obsDateLoc]);
				if (obsDateString != null) {
					lbData.setDateObj(DateUtils.parseDate(obsDateString, new String[] {
							"yyyy-MM-dd"
					}));
				}

				String priority = "";
				if (isUnclaimedQuery) {
					priority = getStringValue(r[priorityLoc]);
				}
				if (priority != null && !priority.equals("")) {
					switch (priority.charAt(0)) {
						case 'C':
							lbData.priority = "Critical";
							break;
						case 'S':
							lbData.priority = "Stat/Urgent";
							break;
						case 'U':
							lbData.priority = "Unclaimed";
							break;
						case 'A':
							lbData.priority = "ASAP";
							break;
						case 'L':
							lbData.priority = "Alert";
							break;
						default:
							lbData.priority = "Routine";
							break;
					}
				} else {
					lbData.priority = "----";
				}

				lbData.requestingClient = "";
				if (isUnclaimedQuery) {
					lbData.requestingClient = getStringValue(r[requestingClientLoc]);
				}

				lbData.reportStatus = "F";

				// the "C" is for corrected excelleris labs
				if (lbData.reportStatus != null && (lbData.reportStatus.equals("F") || lbData.reportStatus.equals("C"))) {
					lbData.finalRes = true;
				} else if (lbData.reportStatus != null && lbData.reportStatus.equals("X")){
					lbData.cancelledReport = true;
				} else{
					lbData.finalRes = false;
				}

				if (!isUnclaimedQuery) {
					lbData.discipline = getStringValue(r[docTypeLoc]);
					if (lbData.discipline != null && lbData.discipline.trim().equals("")) {
						lbData.discipline = null;
					}
				} else {
					lbData.discipline = getStringValue(r[disciplineLoc]);
				}

				if (!isUnclaimedQuery) {
					lbData.finalResultsCount = 0;
				} else { 
					try {
						lbData.finalResultsCount = Integer.parseInt(String.valueOf(r[finalResultCountLoc]));
					} catch (Exception e ) {
						// swallow
					}
				}
				
				if (isUnclaimedQuery) {
					lbData.accessionNumber = getStringValue(r[accessionNumLoc]);
				}
				
				boolean isDuplicateFound = false;
				// unclaimed search might contain duplicate records, for example 
				// with provider_no set to "" and "0", in case we already found one 
				// unclaimed provider for this result. This bug must be fixed on the
				// parsing side, this is just a patch to avoid duplicate entries
				// displayed for the same lab / doc
				if (isUnclaimedQuery && lbData.getSegmentID() != null) {
					for(LabResultData l : labResults) {
						if (lbData.getSegmentID() == null || lbData.labType == null) {
							continue;
						}

						if (lbData.getSegmentID().equals(l.getSegmentID())
								&& lbData.labType.equals(l.labType)) {
							logger.info("Duplicate result data found, skipping - " + Arrays.toString(r));
							isDuplicateFound = true;
							break;
						}
					}
				}
				
				if (!isDuplicateFound) {
					labResults.add(lbData);
				}
			}

		} catch (Exception e) {
			logger.error("exception in DOCPopulate:", e);
		}
		return labResults;
	}

	private void configureScalars(Query q, boolean isExtended) {
		if (!(q instanceof QueryImpl)) {
			throw new IllegalStateException("Invalid query type. Expected EJB Query implementation instance");
		}
		
		QueryImpl ejbQuery = (QueryImpl) q;
		if (!(ejbQuery.getHibernateQuery() instanceof SQLQuery)) {
			throw new IllegalStateException("Invalid query type. Expected to retrieve SQL query instance");
		}
		
	    SQLQuery sql = (SQLQuery) ejbQuery.getHibernateQuery();
	    sql.addScalar("id", Hibernate.INTEGER);
	    sql.addScalar("document_no", Hibernate.INTEGER);
	    sql.addScalar("status", Hibernate.CHARACTER);
	    sql.addScalar("doctype", Hibernate.STRING);
	    sql.addScalar("last_name", Hibernate.STRING);
	    sql.addScalar("first_name", Hibernate.STRING);
	    sql.addScalar("hin", Hibernate.STRING);
	    sql.addScalar("sex", Hibernate.STRING);
	    sql.addScalar("module_id", Hibernate.INTEGER);
	    sql.addScalar("observationdate", Hibernate.DATE);
	    
	    if (!isExtended) {
	    	return;
	    }
	    
	    for(String column : new String[] {"priority", "requesting_client", "discipline", "accessionNum", "final_result_count"}) {
	    	sql.addScalar(column, Hibernate.STRING);
	    }
	    
    }
	
	private String getStringValue(Object value) {
		return value != null ? value.toString() : null;
	}
}
