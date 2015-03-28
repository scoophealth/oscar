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


package oscar.dms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDocument;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.casemgmt.dao.CaseManagementNoteLinkDAO;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.common.dao.ConsultDocsDao;
import org.oscarehr.common.dao.CtlDocTypeDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DocumentDao;
import org.oscarehr.common.dao.IndivoDocsDao;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.common.model.CtlDocType;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Document;
import org.oscarehr.common.model.IndivoDocs;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.MyDateFormat;
import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.util.DateUtils;
import oscar.util.SqlUtilBaseS;
import oscar.util.UtilDateUtilities;

// all SQL statements here
public final class EDocUtil extends SqlUtilBaseS {

	private static ConsultDocsDao consultDocsDao = (ConsultDocsDao)SpringUtils.getBean("consultDocsDao");
	private static DocumentDao documentDao = (DocumentDao)SpringUtils.getBean(DocumentDao.class);
	private static IndivoDocsDao indivoDocsDao = (IndivoDocsDao)SpringUtils.getBean(IndivoDocsDao.class);

	private static Logger logger = MiscUtils.getLogger();

	public static final String PUBLIC = "public";
	public static final String PRIVATE = "private";
	public static final String SORT_DATE = "d.updatedatetime DESC, d.updatedatetime DESC";
	public static final String SORT_DESCRIPTION = "d.docdesc, d.updatedatetime DESC";
	public static final String SORT_DOCTYPE = "d.doctype, d.updatedatetime DESC";
	public static final String SORT_CREATOR = "d.doccreator, d.updatedatetime DESC";
	public static final String SORT_RESPONSIBLE = "d.responsible, d.updatedatetime DESC";
	public static final String SORT_OBSERVATIONDATE = "d.observationdate DESC, d.updatedatetime DESC";
	public static final String SORT_CONTENTTYPE = "d.contenttype, d.updatedatetime DESC";
	public static final String SORT_REVIEWER = "d.reviewer, d.updatedatetime DESC";
	public static final boolean ATTACHED = true;
	public static final boolean UNATTACHED = false;

	public static final String DMS_DATE_FORMAT = "yyyy/MM/dd";
	public static final String REVIEW_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

	private static ProgramManager programManager = (ProgramManager) SpringUtils.getBean("programManager");
	private static CaseManagementNoteLinkDAO caseManagementNoteLinkDao = (CaseManagementNoteLinkDAO) SpringUtils.getBean("CaseManagementNoteLinkDAO");
	private static ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	private static CtlDocTypeDao ctldoctypedao = (CtlDocTypeDao) SpringUtils.getBean("ctlDocTypeDao");
	private static DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");

	public static String getProviderName(String providerNo) {
		if(providerNo == null || providerNo.length() == 0) {
			return "";
		}
		Provider p = providerDao.getProvider(providerNo);
		if(p != null) {
			return p.getLastName().toUpperCase() + ", " + p.getFirstName().toUpperCase();
		}
		return "";
	}

	public static String getDemographicName(String demographicNo) {
		if(demographicNo == null || demographicNo.length() == 0) {
			return "";
		}
		Demographic d = demographicDao.getDemographic(demographicNo);
		if(d != null) {
			return d.getLastName().toUpperCase() + ", " + d.getFirstName().toUpperCase();
		}
		return "";
	}

	public static Provider getProvider(String providerNo) {
		if(providerNo == null || providerNo.length() == 0) {
			return null;
		}
		return providerDao.getProvider(providerNo);
	}

	public static ArrayList<String> getDoctypesByStatus(String module, String[] statuses) {
		ArrayList<String> doctypes = new ArrayList<String>();
		List<CtlDocType> result = ctldoctypedao.findByStatusAndModule(statuses, module);
		for (CtlDocType obj : result) {
			doctypes.add(obj.getDocType());
		}

		return doctypes;
	}
	
	public static ArrayList<String> getDoctypes(String module) {
		return getDoctypesByStatus(module,new String[]{ "A", "H", "I" });
	}

	public static ArrayList<String> getActiveDocTypes(String module) {
		return getDoctypesByStatus(module,new String[]{"A"});
	}

	public static String getDocStatus(String module, String doctype){
		List<CtlDocType> result = ctldoctypedao.findByDocTypeAndModule(doctype, module);
		String status = "";
		for(CtlDocType obj:result) {
			status = obj.getStatus();
		}
        return status;
   }

	public static void addCaseMgmtNoteLink(CaseManagementNoteLink cmnl) {
		caseManagementNoteLinkDao.save(cmnl);
		logger.debug("ADD CASEMGMT NOTE LINK : Id=" + cmnl.getId());
	}

	/**
	 * @return the new documentId
	 */
	public static String addDocumentSQL(EDoc newDocument) {
		Document doc = new Document();
		doc.setDoctype(newDocument.getType());
		doc.setDocClass(newDocument.getDocClass());
		doc.setDocSubClass(newDocument.getDocSubClass());
		doc.setDocdesc(newDocument.getDescription());
		doc.setDocxml(newDocument.getHtml());
		doc.setDocfilename(newDocument.getFileName());
		doc.setDoccreator(newDocument.getCreatorId());
		doc.setSource(newDocument.getSource());
		doc.setSourceFacility(newDocument.getSourceFacility());
		doc.setResponsible(newDocument.getResponsibleId());
		doc.setProgramId(newDocument.getProgramId());
		doc.setUpdatedatetime(newDocument.getDateTimeStampAsDate());
		doc.setStatus(newDocument.getStatus());
		doc.setContenttype(newDocument.getContentType());
		doc.setPublic1(Integer.parseInt(newDocument.getDocPublic()));
		doc.setObservationdate(MyDateFormat.getSysDate(newDocument.getObservationDate()));
		doc.setNumberofpages(newDocument.getNumberOfPages());
		doc.setAppointmentNo(newDocument.getAppointmentNo());
		documentDao.persist(doc);


		Integer document_no = doc.getId();
		String ctlDocumentSql = "INSERT INTO ctl_document (module,module_id,document_no,status) VALUES ('" + newDocument.getModule() + "', " + newDocument.getModuleId() + ", " + document_no + ", '" + newDocument.getStatus() + "'  )";

		logger.debug("in addDocumentSQL ,add ctl_document: " + ctlDocumentSql);
		runSQL(ctlDocumentSql);
		return document_no.toString();
	}

	//new method to let the user add a new DocumentType into the database
    public static void addDocTypeSQL(String docType, String module, String status){
    	CtlDocType ctldoctype = new CtlDocType();
    	ctldoctype.setDocType(docType);
    	ctldoctype.setModule(module);
    	ctldoctype.setStatus(status);
    	ctldoctypedao.persist(ctldoctype);
    }

    public static void changeDocTypeStatusSQL(String docType, String module, String status){
    	ctldoctypedao.changeDocType(docType, module, status);
    }

    /** new method to let the user add a new DocumentType into the database */
    public static void addDocTypeSQL(String docType, String module){
    	ctldoctypedao.addDocType(docType, module);
    }

	public static void detachDocConsult(String docNo, String consultId) {
		List<ConsultDocs> consultDocs = consultDocsDao.findByRequestIdDocumentNoAndDocumentType(Integer.parseInt(consultId), Integer.parseInt(docNo), "D");
    	for(ConsultDocs consultDoc:consultDocs) {
    		consultDoc.setDeleted("Y");
    		consultDocsDao.merge(consultDoc);
    	}
	}

	public static void attachDocConsult(String providerNo, String docNo, String consultId) {
		ConsultDocs consultDoc = new ConsultDocs();
    	consultDoc.setRequestId(Integer.parseInt(consultId));
    	consultDoc.setDocumentNo(Integer.parseInt(docNo));
    	consultDoc.setDocType("D");
    	consultDoc.setAttachDate(new Date());
    	consultDoc.setProviderNo(providerNo);
    	consultDocsDao.persist(consultDoc);
	}

	public static void editDocumentSQL(EDoc newDocument, boolean doReview) {

		Document doc = documentDao.find(Integer.parseInt(newDocument.getDocId()));
		if(doc != null) {
			doc.setDoctype(newDocument.getType());
			doc.setDocClass(newDocument.getDocClass());
			doc.setDocSubClass(newDocument.getDocSubClass());
			doc.setDocdesc(newDocument.getDescription());
			doc.setSource(newDocument.getSource());
			doc.setSourceFacility(newDocument.getSourceFacility());
			doc.setDocxml(newDocument.getHtml());
			doc.setResponsible(newDocument.getResponsibleId());
			doc.setPublic1(Integer.parseInt(newDocument.getDocPublic()));
			if(doReview) {
				doc.setReviewer(newDocument.getReviewerId());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				try {
					doc.setReviewdatetime(sdf.parse(newDocument.getReviewDateTime()));
				}catch(ParseException e) {
					logger.warn("error parsing date",e);
				}
			} else {
				doc.setReviewer(null);
				doc.setReviewdatetime(null);
				doc.setUpdatedatetime(newDocument.getDateTimeStampAsDate());
				doc.setObservationdate(MyDateFormat.getSysDate(newDocument.getObservationDate()));
			}
			if(newDocument.getFileName().length()>0) {
				doc.setDocfilename(newDocument.getFileName());
				doc.setContenttype(newDocument.getContentType());
			}
			documentDao.merge(doc);
		}
	}

	public static void indivoRegister(EDoc doc) {
		IndivoDocs id = new IndivoDocs();
		id.setOscarDocNo(Integer.parseInt(doc.getDocId()));
		id.setIndivoDocIdx(doc.getIndivoIdx());
		id.setDocType("document");
		id.setDateSent(new Date());
		if(doc.isInIndivo()) {
			id.setUpdate("U");
		}else {
			id.setUpdate("I");
		}
		indivoDocsDao.persist(id);
	}

	/**
	 * Fetches all consult documents attached to specific consultation
	 */
	public static ArrayList<EDoc> listDocs(String demoNo, String consultationId, boolean attached) {
		String sql = "SELECT DISTINCT d.document_no, d.doccreator, d.source, d.sourceFacility, d.responsible, d.program_id, d.doctype, d.docdesc, d.observationdate, d.status, d.docfilename, d.contenttype, d.reviewer, d.reviewdatetime, d.appointment_no FROM document d, ctl_document c " + "WHERE d.status=c.status AND d.status != 'D' AND c.document_no=d.document_no AND " + "c.module='demographic' AND c.module_id = " + demoNo;

		String attachQuery = "SELECT d.document_no, d.doccreator, d.source, d.sourceFacility, d.responsible, d.program_id, d.doctype, d.docdesc, d.observationdate, d.status, d.docfilename, d.contenttype, d.reviewer, d.reviewdatetime, d.appointment_no FROM document d, consultdocs cd " + "WHERE d.document_no = cd.document_no AND " + "cd.requestId = " + consultationId + " AND cd.doctype = 'D' AND cd.deleted IS NULL";

		ArrayList<EDoc> resultDocs = new ArrayList<EDoc>();
		ArrayList<EDoc> attachedDocs = new ArrayList<EDoc>();

		try {
			ResultSet rs = getSQL(attachQuery);
			while (rs.next()) {
				EDoc currentdoc = new EDoc();
				currentdoc.setDocId(rsGetString(rs, "document_no"));
				currentdoc.setDescription(rsGetString(rs, "docdesc"));
				currentdoc.setFileName(rsGetString(rs, "docfilename"));
				currentdoc.setContentType(rsGetString(rs, "contenttype"));
				currentdoc.setCreatorId(rsGetString(rs, "doccreator"));
				currentdoc.setSource(rsGetString(rs, "source"));
				currentdoc.setSourceFacility(rsGetString(rs, "sourceFacility"));
				currentdoc.setResponsibleId(rsGetString(rs, "responsible"));
				String temp = rsGetString(rs, "program_id");
				if (temp != null && temp.length() > 0) currentdoc.setProgramId(Integer.valueOf(temp));
				temp = rsGetString(rs, "appointment_no");
				if (temp != null && temp.length() > 0) currentdoc.setAppointmentNo(Integer.valueOf(temp));
				currentdoc.setType(rsGetString(rs, "doctype"));
				currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
				currentdoc.setObservationDate(rsGetString(rs, "observationdate"));
				currentdoc.setReviewerId(rsGetString(rs, "reviewer"));
				currentdoc.setReviewDateTime(rsGetString(rs, "reviewdatetime"));
				currentdoc.setReviewDateTimeDate(rs.getTimestamp("reviewdatetime"));
				attachedDocs.add(currentdoc);
			}
			rs.close();

			if (!attached) {
				rs = getSQL(sql);
				while (rs.next()) {
					EDoc currentdoc = new EDoc();
					currentdoc.setDocId(rsGetString(rs, "document_no"));
					currentdoc.setDescription(rsGetString(rs, "docdesc"));
					currentdoc.setFileName(rsGetString(rs, "docfilename"));
					currentdoc.setContentType(rsGetString(rs, "contenttype"));
					currentdoc.setCreatorId(rsGetString(rs, "doccreator"));
					currentdoc.setSource(rsGetString(rs, "source"));
					currentdoc.setSourceFacility(rsGetString(rs, "sourceFacility"));
					currentdoc.setResponsibleId(rsGetString(rs, "responsible"));
					String temp = rsGetString(rs, "program_id");
					if (temp != null && temp.length() > 0) currentdoc.setProgramId(Integer.valueOf(temp));
					temp = rsGetString(rs, "appointment_no");
					if (temp != null && temp.length() > 0) currentdoc.setAppointmentNo(Integer.valueOf(temp));
					currentdoc.setType(rsGetString(rs, "doctype"));
					currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
					currentdoc.setObservationDate(rsGetString(rs, "observationdate"));
					currentdoc.setReviewerId(rsGetString(rs, "reviewer"));
					currentdoc.setReviewDateTime(rsGetString(rs, "reviewdatetime"));
					currentdoc.setReviewDateTimeDate(rs.getTimestamp("reviewdatetime"));

					if (!attachedDocs.contains(currentdoc)) resultDocs.add(currentdoc);
				}
				rs.close();
			} else resultDocs = attachedDocs;

		} catch (SQLException sqe) {
			logger.error("Error", sqe);
		}

		if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
			resultDocs = documentFacilityFiltering(resultDocs);
		}

		return resultDocs;
	}

	public static ArrayList<EDoc> listDocs(String module, String moduleid, String docType, String publicDoc, String sort) {
		return listDocs(module, moduleid, docType, publicDoc, sort, "active");
	}

	public static EDoc getEDocFromDocId(String docId) {
		String sql = "SELECT DISTINCT c.module, c.module_id, d.doccreator, d.source, d.sourceFacility, d.responsible, d.program_id, "
                           + "d.status, d.docdesc, d.docfilename, d.doctype, d.document_no, d.updatedatetime, d.contenttype, d.observationdate, "
                           + "d.docClass, d.docSubClass, d.reviewer, d.reviewdatetime, d.appointment_no " + "FROM document d, ctl_document c "
                           + "WHERE c.document_no=d.document_no AND c.document_no='" + docId + "'";
		sql = sql + " ORDER BY " + EDocUtil.SORT_OBSERVATIONDATE;// default sort

		ResultSet rs = getSQL(sql);
		EDoc currentdoc = new EDoc();
		try {
			if (rs.first()) {

				currentdoc.setModule(rsGetString(rs, "module"));
				currentdoc.setModuleId(rsGetString(rs, "module_id"));
				currentdoc.setDocId(rsGetString(rs, "document_no"));
				currentdoc.setDescription(rsGetString(rs, "docdesc"));
				currentdoc.setType(rsGetString(rs, "doctype"));
				currentdoc.setDocClass(rsGetString(rs, "docClass"));
				currentdoc.setDocSubClass(rsGetString(rs, "docSubClass"));
				currentdoc.setCreatorId(rsGetString(rs, "doccreator"));
				currentdoc.setSource(rsGetString(rs, "source"));
				currentdoc.setSourceFacility(rsGetString(rs, "sourceFacility"));
				currentdoc.setResponsibleId(rsGetString(rs, "responsible"));
				String temp = rsGetString(rs, "program_id");
				if (temp != null && temp.length() > 0) currentdoc.setProgramId(Integer.valueOf(temp));
				temp = rsGetString(rs, "appointment_no");
				if (temp != null && temp.length() > 0) currentdoc.setAppointmentNo(Integer.valueOf(temp));

				currentdoc.setDateTimeStampAsDate(rs.getTimestamp("updatedatetime"));
				currentdoc.setDateTimeStamp(rsGetString(rs, "updatedatetime"));
				currentdoc.setFileName(rsGetString(rs, "docfilename"));
				currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
				currentdoc.setContentType(rsGetString(rs, "contenttype"));
				currentdoc.setObservationDate(rsGetString(rs, "observationdate"));
				currentdoc.setReviewerId(rsGetString(rs, "reviewer"));
				currentdoc.setReviewDateTime(rsGetString(rs, "reviewdatetime"));
				currentdoc.setReviewDateTimeDate(rs.getTimestamp("reviewdatetime"));

				rs.close();
			}
		} catch (SQLException sqe) {
			logger.error("Error", sqe);
		}

		return currentdoc;
	}

	public static ArrayList<EDoc> listDocsPreviewInbox(List<String> docIds) {

		ArrayList<EDoc> resultDocs = new ArrayList<EDoc>();
		for (String docId : docIds) {
			EDoc currentdoc = new EDoc();
			currentdoc = getEDocFromDocId(docId);
			resultDocs.add(currentdoc);
		}
		return resultDocs;
	}

	public static ArrayList<EDoc> listDocs(String module, String moduleid, String docType, String publicDoc, String sort, String viewstatus) {
		return listDocsSince(module,moduleid,docType,publicDoc,sort,viewstatus,null);
	}
	
	public static ArrayList<EDoc> listDocsSince(String module, String moduleid, String docType, String publicDoc, String sort, String viewstatus, Date since) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		// sort must be not null
		// docType = null or = "all" to show all doctypes
		// select publicDoc and sorting from static variables for this class i.e. sort=EDocUtil.SORT_OBSERVATIONDATE
		// sql base (prefix) to avoid repetition in the if-statements
		String sql = "SELECT DISTINCT c.module, c.module_id, d.doccreator, d.source, d.sourceFacility, d.docClass, d.docSubClass,d.responsible, d.program_id, d.status, d.docdesc, d.docfilename, d.doctype, d.document_no, d.updatedatetime, d.contenttype, d.observationdate, d.reviewer, d.reviewdatetime, d.appointment_no " + "FROM document d, ctl_document c WHERE c.document_no=d.document_no AND c.module='" + module + "'";
		// if-statements to select the where condition (suffix)
		if (publicDoc.equals(PUBLIC)) {
			if (docType == null || docType.equals("all") || docType.length() == 0) sql = sql + " AND d.public1=1";
			else sql = sql + " AND d.public1=1 AND d.doctype='" + docType + "'";
		} else {
			if (docType == null || docType.equals("all") || docType.length() == 0) sql = sql + " AND c.module_id='" + moduleid + "' AND d.public1=0";
			else sql = sql + " AND c.module_id='" + moduleid + "' AND d.public1=0 AND d.doctype='" + docType + "'";
		}

		if (viewstatus.equals("deleted")) {
			sql += " AND d.status = 'D'";
		} else if (viewstatus.equals("active")) {
			sql += " AND d.status != 'D'";
		}
		
		if(since != null) {
			sql += " AND d.updatedatetime > '" +  formatter.format(since) + "' ";
		}

		sql = sql + " ORDER BY " + sort;
		ResultSet rs = getSQL(sql);
		ArrayList<EDoc> resultDocs = new ArrayList<EDoc>();
		try {
			while (rs.next()) {
				EDoc currentdoc = new EDoc();
				currentdoc.setModule(rsGetString(rs, "module"));
				currentdoc.setModuleId(rsGetString(rs, "module_id"));
				currentdoc.setDocId(rsGetString(rs, "document_no"));
				currentdoc.setDescription(rsGetString(rs, "docdesc"));
				currentdoc.setType(rsGetString(rs, "doctype"));
				currentdoc.setCreatorId(rsGetString(rs, "doccreator"));
				currentdoc.setSource(rsGetString(rs, "source"));
				currentdoc.setSourceFacility(rsGetString(rs, "sourceFacility"));
				currentdoc.setResponsibleId(rsGetString(rs, "responsible"));
                                currentdoc.setDocClass(rsGetString(rs, "docClass"));
				currentdoc.setDocSubClass(rsGetString(rs, "docSubClass"));
				String temp = rsGetString(rs, "program_id");
				if (temp != null && temp.length() > 0) currentdoc.setProgramId(Integer.valueOf(temp));
				temp = rsGetString(rs, "appointment_no");
				if (temp != null && temp.length() > 0) currentdoc.setAppointmentNo(Integer.valueOf(temp));
				currentdoc.setDateTimeStampAsDate(rs.getTimestamp("updatedatetime"));
				currentdoc.setDateTimeStamp(rsGetString(rs, "updatedatetime"));
				currentdoc.setFileName(rsGetString(rs, "docfilename"));
				currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
				currentdoc.setContentType(rsGetString(rs, "contenttype"));
				currentdoc.setObservationDate(rsGetString(rs, "observationdate"));
				currentdoc.setReviewerId(rsGetString(rs, "reviewer"));
				currentdoc.setReviewDateTime(rsGetString(rs, "reviewdatetime"));
				currentdoc.setReviewDateTimeDate(rs.getTimestamp("reviewdatetime"));
				resultDocs.add(currentdoc);
			}
			rs.close();
		} catch (SQLException sqe) {
			logger.error("Error", sqe);
		}

		if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
			resultDocs = documentFacilityFiltering(resultDocs);
		}

		return resultDocs;
	}
	
	public static ArrayList<Integer> listDemographicIdsSince(Date since) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		String sql = "SELECT DISTINCT c.module_id " + "FROM document d, ctl_document c WHERE c.document_no=d.document_no AND c.module='demographic'";
		
		if(since != null) {
			sql += " AND d.updatedatetime > '" +  formatter.format(since) + "' ";
		}

		ResultSet rs = getSQL(sql);
		ArrayList<Integer> resultDocs = new ArrayList<Integer>();
		try {
			while (rs.next()) {
				resultDocs.add(Integer.parseInt(rsGetString(rs, "module_id")));
			}
			rs.close();
		} catch (SQLException sqe) {
			logger.error("Error", sqe);
		}

		return resultDocs;
	}

	public ArrayList<EDoc> getUnmatchedDocuments(String creator, String responsible, Date startDate, Date endDate, boolean unmatchedDemographics) {
		ArrayList<EDoc> list = new ArrayList<EDoc>();
		// boolean matchedDemographics = true;
		String sql = "SELECT DISTINCT c.module, c.module_id, d.doccreator, d.source, d.sourceFacility, d.responsible, d.program_id, d.status, d.docdesc, d.docfilename, d.doctype, d.document_no, d.updatedatetime, d.contenttype, d.observationdate, d.appointment_no FROM document d, ctl_document c WHERE c.document_no=d.document_no AND c.module='demographic' and doccreator = ? and responsible = ? and updatedatetime >= ?  and updatedatetime <= ?";
		if (unmatchedDemographics) {
			sql += " and c.module_id = -1 ";
		}
		/*
		 * else if (matchedDemographics){ sql += " and c.module_id != -1 "; }
		 */
		try {
			java.sql.Date sDate = new java.sql.Date(startDate.getTime());
			java.sql.Date eDate = new java.sql.Date(endDate.getTime());
			logger.debug("Creator " + creator + " start " + sDate + " end " + eDate);

			Connection c = DbConnectionFilter.getThreadLocalDbConnection();
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, creator);
			ps.setString(2, responsible);
			ps.setDate(3, new java.sql.Date(startDate.getTime()));
			ps.setDate(4, new java.sql.Date(endDate.getTime()));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				logger.debug("DOCFILENAME " + rs.getString("docfilename"));
				EDoc currentdoc = new EDoc();
				currentdoc.setModule(rsGetString(rs, "module"));
				currentdoc.setModuleId(rsGetString(rs, "module_id"));
				currentdoc.setDocId(rsGetString(rs, "document_no"));
				currentdoc.setDescription(rsGetString(rs, "docdesc"));
				currentdoc.setType(rsGetString(rs, "doctype"));
				currentdoc.setCreatorId(rsGetString(rs, "doccreator"));
				currentdoc.setSource(rsGetString(rs, "source"));
				currentdoc.setSourceFacility(rsGetString(rs, "sourceFacility"));
				currentdoc.setResponsibleId(rsGetString(rs, "responsible"));
				String temp = rsGetString(rs, "program_id");
				if (temp != null && temp.length() > 0) currentdoc.setProgramId(Integer.valueOf(temp));
				temp = rsGetString(rs, "appointment_no");
				if (temp != null && temp.length() > 0) currentdoc.setAppointmentNo(Integer.valueOf(temp));
				currentdoc.setDateTimeStamp(rsGetString(rs, "updatedatetime"));
				currentdoc.setFileName(rsGetString(rs, "docfilename"));
				currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
				currentdoc.setContentType(rsGetString(rs, "contenttype"));
				currentdoc.setObservationDate(rsGetString(rs, "observationdate"));

				list.add(currentdoc);
			}
			rs.close();
		} catch (Exception e) {
			logger.error("Error", e);
		}
		// mysql> SELECT DISTINCT c.module, c.module_id, d.doccreator, d.source, d.program_id, d.status, d.docdesc, d.docfilename, d.doctype, d.document_no, d.updatedatetime, d.contenttype, d.observationdate FROM document d, ctl_document c WHERE
		// c.document_no=d.document_no AND c.module='demographic' and module_id = -1
		return list;
	}

	private static ArrayList<EDoc> documentFacilityFiltering(List<EDoc> eDocs) {
		ArrayList<EDoc> results = new ArrayList<EDoc>();

		for (EDoc eDoc : eDocs) {
			Integer programId = eDoc.getProgramId();
			if (programManager.hasAccessBasedOnCurrentFacility(programId)) results.add(eDoc);
		}

		return results;
	}

	public static ArrayList<EDoc> listDemoDocs(String moduleid) {
		String sql = "SELECT d.*, p.first_name, p.last_name FROM document d, provider p, ctl_document c " + "WHERE d.doccreator=p.provider_no AND d.document_no = c.document_no " + "AND c.module='demographic' AND c.module_id=" + moduleid;

		logger.debug("sql list: " + sql);
		ResultSet rs = getSQL(sql);
		ArrayList<EDoc> resultDocs = new ArrayList<EDoc>();
		try {
			while (rs.next()) {
				EDoc currentdoc = new EDoc();
				currentdoc.setDocId(rsGetString(rs, "document_no"));
				currentdoc.setType(rsGetString(rs, "doctype"));
				currentdoc.setFileName(rsGetString(rs, "docfilename"));
				currentdoc.setCreatorId(rsGetString(rs, "doccreator"));
				currentdoc.setSource(rsGetString(rs, "source"));
				currentdoc.setSourceFacility(rsGetString(rs, "sourceFacility"));
				currentdoc.setResponsibleId(rsGetString(rs, "responsible"));
				currentdoc.setDocClass(rsGetString(rs, "docClass"));
				currentdoc.setDocSubClass(rsGetString(rs, "docSubClass"));
				String temp = rsGetString(rs, "program_id");
				if (temp != null && temp.length() > 0) currentdoc.setProgramId(Integer.valueOf(temp));
				temp = rsGetString(rs, "appointment_no");
				if (temp != null && temp.length() > 0) currentdoc.setAppointmentNo(Integer.valueOf(temp));
				currentdoc.setDateTimeStamp(rsGetString(rs, "updatedatetime"));
				currentdoc.setContentType(rsGetString(rs, "contenttype"));
				currentdoc.setObservationDate(rsGetString(rs, "observationdate"));
				currentdoc.setReviewerId(rsGetString(rs, "reviewer"));
				currentdoc.setReviewDateTime(rsGetString(rs, "reviewdatetime"));
				currentdoc.setReviewDateTimeDate(rs.getTimestamp("reviewdatetime"));
				resultDocs.add(currentdoc);
			}
			rs.close();
		} catch (SQLException sqe) {
			logger.error("Error", sqe);
		}

		if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
			resultDocs = documentFacilityFiltering(resultDocs);
		}

		return resultDocs;
	}

	public static ArrayList<String> listModules() {
		String sql = "SELECT DISTINCT module FROM ctl_doctype";
		ResultSet rs = getSQL(sql);
		ArrayList<String> modules = new ArrayList<String>();
		try {
			while (rs.next()) {
				modules.add(rsGetString(rs, "module"));
			}
		} catch (SQLException sqe) {
			logger.error("Error", sqe);
		}
		return modules;
	}

	public static EDoc getDoc(String documentNo) {
		String sql = "SELECT DISTINCT c.module, c.module_id, d.* FROM document d, ctl_document c WHERE d.status=c.status AND d.status != 'D' AND " + "c.document_no=d.document_no AND c.document_no='" + documentNo + "' ORDER BY d.updatedatetime DESC";

		String indivoSql = "SELECT indivoDocIdx FROM indivoDocs i WHERE i.oscarDocNo = ? and i.docType = 'document' limit 1";
		boolean myOscarEnabled = OscarProperties.getInstance().getProperty("MY_OSCAR", "").trim().equalsIgnoreCase("YES");
		ResultSet rs2;

		ResultSet rs = getSQL(sql);
		EDoc currentdoc = new EDoc();
		try {
			while (rs.next()) {
				currentdoc.setModule(rsGetString(rs, "module"));
				currentdoc.setModuleId(rsGetString(rs, "module_id"));
				currentdoc.setDocId(rsGetString(rs, "document_no"));
				currentdoc.setDescription(rsGetString(rs, "docdesc"));
				currentdoc.setType(rsGetString(rs, "doctype"));
                                currentdoc.setDocClass(rsGetString(rs, "docClass"));
                                currentdoc.setDocSubClass(rsGetString(rs, "docSubClass"));
				currentdoc.setCreatorId(rsGetString(rs, "doccreator"));
				currentdoc.setResponsibleId(rsGetString(rs, "responsible"));
				currentdoc.setSource(rsGetString(rs, "source"));
				currentdoc.setSourceFacility(rsGetString(rs, "sourceFacility"));
				currentdoc.setDateTimeStampAsDate(rs.getTimestamp("updatedatetime"));
				currentdoc.setDateTimeStamp(rsGetString(rs, "updatedatetime"));
				currentdoc.setFileName(rsGetString(rs, "docfilename"));
				currentdoc.setDocPublic(rsGetString(rs, "public1"));
				currentdoc.setObservationDate(rs.getDate("observationdate"));
				currentdoc.setReviewerId(rsGetString(rs, "reviewer"));
				currentdoc.setReviewDateTime(rsGetString(rs, "reviewdatetime"));
				currentdoc.setReviewDateTimeDate(rs.getTimestamp("reviewdatetime"));
				currentdoc.setHtml(rsGetString(rs, "docxml"));
				currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
				currentdoc.setContentType(rsGetString(rs, "contenttype"));
				currentdoc.setNumberOfPages(rs.getInt("number_of_pages"));

				if (myOscarEnabled) {
					String tmp = indivoSql.replaceFirst("\\?", oscar.Misc.getString(rs, "document_no"));
					rs2 = getSQL(tmp);

					if (rs2.next()) {
						currentdoc.setIndivoIdx(rsGetString(rs2, "indivoDocIdx"));
						if (currentdoc.getIndivoIdx().length() > 0) currentdoc.registerIndivo();
					}
					rs2.close();
				}
			}
			rs.close();
		} catch (SQLException sqe) {
			logger.error("Error", sqe);
		}
		return currentdoc;
	}

	public String getDocumentName(String id) {
		Document d = documentDao.find(Integer.parseInt(id));
		if(d != null) {
			return d.getDocfilename();
		}
		return null;
	}

	public static void undeleteDocument(String documentNo) {

		try {
			String sql = "select status from ctl_document where document_no=" + documentNo;
			String status = "";
			ResultSet rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				status = rs.getString("status");
			}
			rs.close();

			Document d = documentDao.find(Integer.parseInt(documentNo));
			if(d != null) {
				d.setStatus(status.toCharArray()[0]);
				d.setUpdatedatetime( MyDateFormat.getSysDate(getDmsDateTime()));
				documentDao.merge(d);
			}

		} catch (SQLException e) {
			logger.error("Error", e);
		}
	}

	public static void deleteDocument(String documentNo) {
		Document d = documentDao.find(Integer.parseInt(documentNo));
		if(d != null) {
			d.setStatus('D');
			d.setUpdatedatetime(MyDateFormat.getSysDate(getDmsDateTime()));
			documentDao.merge(d);
		}
	}

	public static String getDmsDateTime() {
		String nowDateR = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy/MM/dd");
		String nowTimeR = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "HH:mm:ss");
		String dateTimeStamp = nowDateR + " " + nowTimeR;
		return dateTimeStamp;
	}

	public static Date getDmsDateTimeAsDate() {
		return UtilDateUtilities.now();
	}

	public static int addDocument(String demoNo, String docFileName, String docDesc, String docType, String docClass, String docSubClass, String contentType, String observationDate, String updateDateTime, String docCreator, String responsible) throws SQLException {
		return addDocument(demoNo, docFileName, docDesc, docType, docClass, docSubClass, contentType, observationDate, updateDateTime, docCreator, responsible, null, null, null);
	}

	public static int addDocument(String demoNo, String docFileName, String docDesc, String docType, String docClass, String docSubClass, String contentType, String observationDate, String updateDateTime, String docCreator, String responsible, String reviewer, String reviewDateTime) throws SQLException {
		return addDocument(demoNo, docFileName, docDesc, docType, docClass, docSubClass, contentType, observationDate, updateDateTime, docCreator, responsible, reviewer, reviewDateTime, null, null);
	}

	public static int addDocument(String demoNo, String docFileName, String docDesc, String docType, String docClass, String docSubClass, String contentType, String observationDate, String updateDateTime, String docCreator, String responsible, String reviewer, String reviewDateTime, String source) throws SQLException {
		return addDocument(demoNo, docFileName, docDesc, docType, docClass, docSubClass, contentType, observationDate, updateDateTime, docCreator, responsible, reviewer, reviewDateTime, source, null);
	}

	public static int addDocument(String demoNo, String docFileName, String docDesc, String docType, String docClass, String docSubClass, String contentType, String observationDate, String updateDateTime, String docCreator, String responsible, String reviewer, String reviewDateTime, String source, String sourceFacility) throws SQLException {

		Document doc = new Document();
		doc.setDoctype(docType);
		doc.setDocClass(docClass);
		doc.setDocSubClass(docSubClass);
		doc.setDocdesc(docDesc);
		doc.setDocfilename(docFileName);
		doc.setDoccreator(docCreator);
		doc.setResponsible(responsible);
		doc.setUpdatedatetime(MyDateFormat.getSysDate(updateDateTime));
		doc.setStatus('A');
		doc.setContenttype(contentType);
		doc.setPublic1(0);
		doc.setObservationdate(MyDateFormat.getSysDate(observationDate));
		doc.setReviewer(reviewer);
		doc.setReviewdatetime(MyDateFormat.getSysDate(reviewDateTime));
		doc.setSource(source);
		doc.setSourceFacility(sourceFacility);
		documentDao.persist(doc);


		int key=0;
		if (doc.getDocumentNo() > 0) {
			String add_record_string2 = "insert into ctl_document values ('demographic',?,?,'A')";
			Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
			PreparedStatement add_record = conn.prepareStatement(add_record_string2);

			add_record.setString(1, demoNo);
			add_record.setString(2, doc.getDocumentNo().toString());

			add_record.executeUpdate();
			ResultSet rs = add_record.getGeneratedKeys();
			if (rs.next()) key = rs.getInt(1);
			add_record.close();
			rs.close();
		}
		return key;
	}

	// private static String getLastDocumentNo() {
	public static String getLastDocumentNo() {
		String documentNo = null;
		try {
			String sql = "select max(document_no) from document";
			ResultSet rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				documentNo = oscar.Misc.getString(rs, 1);
			}
			rs.close();
		} catch (SQLException e) {
			logger.error("Error", e);
		}
		return documentNo;
	}

	public static String getLastDocumentDesc() {
		String docDesc = null;
		try {
			String docNumber = EDocUtil.getLastDocumentNo();

			String sql = "select docdesc from document where document_no=" + docNumber;
			ResultSet rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
				docDesc = oscar.Misc.getString(rs, 1);
			}
			rs.close();
		} catch (SQLException e) {
			logger.error("Error", e);
		}

		return docDesc;
	}

	public static byte[] getFile(String fpath) {
		byte[] fdata = null;
		FileInputStream fis=null;
		try {
			// first we get length of file and allocate mem for file
			File file = new File(fpath);
			long length = file.length();
			fdata = new byte[(int) length];

			// now we read file into array buffer
			fis = new FileInputStream(file);
			fis.read(fdata);

		} catch (NullPointerException ex) {
			logger.error("Error", ex);
		} catch (FileNotFoundException ex) {
			logger.error("Error", ex);
		} catch (IOException ex) {
			logger.error("Error", ex);
		}
		finally
		{
			try {
	            if (fis!=null) fis.close();
            } catch (IOException e) {
	            logger.error("error", e);
            }
		}

		return fdata;
	}

	// add for inbox manager

	public static boolean getDocReviewFlag(String docId) {
		boolean flag = false;
		return flag;
	}

	public static boolean getDocUrgentFlag(String docId) {
		boolean flag = false;
		return flag;
	}

	// get noteId from tableId
	public static Long getNoteIdFromDocId(Long docId) {
		Long noteId = 0L;
		CaseManagementNoteLink cmnLink = caseManagementNoteLinkDao.getLastLinkByTableId(CaseManagementNoteLink.DOCUMENT, docId);
		if (cmnLink != null) noteId = cmnLink.getNoteId();
		return noteId;
	}

	// get tableId from noteId when table_name is document
	public static Long getTableIdFromNoteId(Long noteId) {
		Long tableId = 0L;
		CaseManagementNoteLink cmnLink = caseManagementNoteLinkDao.getLastLinkByNote(noteId);
		if (cmnLink != null && cmnLink.getTableName().equals(CaseManagementNoteLink.DOCUMENT)) {
			tableId = cmnLink.getTableId();
		}
		return tableId;
	}

	// get document from its note
	public static EDoc getDocFromNote(Long noteId) {
		EDoc doc = new EDoc();
		Long docIdL = getTableIdFromNoteId(noteId);
		if (docIdL > 0L) {
			Integer docId = docIdL.intValue();
			String getDocSql = "select document_no, docfilename, status from document where document_no=" + docId;
			ResultSet rs = getSQL(getDocSql);
			try {
				if (rs.first()) {
					doc.setDocId(rs.getString("document_no"));
					doc.setFileName(rs.getString("docfilename"));
					doc.setStatus(rs.getString("status").charAt(0));
				}
			} catch (SQLException ex) {
				logger.error("Error", ex);
			}
		}
		return doc;
	}

	public static ArrayList<EDoc> getRemoteDocuments(Integer demographicId) {
		ArrayList<EDoc> results = new ArrayList<EDoc>();

		try {

			List<CachedDemographicDocument> remoteDocuments  = null;
			try {
				if (!CaisiIntegratorManager.isIntegratorOffline()){
					DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
					remoteDocuments = CaisiIntegratorManager.getDemographicWs().getLinkedCachedDemographicDocuments(demographicId);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unexpected error.", e);
				CaisiIntegratorManager.checkForConnectionError(e);
			}

			if(CaisiIntegratorManager.isIntegratorOffline()){
				MiscUtils.getLogger().debug("getting fall back documents for "+demographicId);
				remoteDocuments = IntegratorFallBackManager.getRemoteDocuments(demographicId);
			}


			for (CachedDemographicDocument remoteDocument : remoteDocuments) {
				results.add(toEDoc(remoteDocument));
			}
		} catch (Exception e) {
			logger.error("Error retriving integrated documents.", e);
		}

		logger.debug("retreived remote documents, document count="+results.size());

		return(results);
	}

	private static EDoc toEDoc(CachedDemographicDocument remoteDocument) {
		EDoc eDoc=new EDoc();

		eDoc.setRemoteFacilityId(remoteDocument.getFacilityIntegerPk().getIntegratorFacilityId());

		eDoc.setAppointmentNo(remoteDocument.getAppointmentNo());
		eDoc.setContentType(remoteDocument.getContentType());
		eDoc.setCreatorId(remoteDocument.getDocCreator());
		eDoc.setDateTimeStamp(DateUtils.formatDate(remoteDocument.getUpdateDateTime(), null));
		eDoc.setDateTimeStampAsDate(MiscUtils.toDate(remoteDocument.getUpdateDateTime()));
		eDoc.setDescription(remoteDocument.getDescription());
		eDoc.setDocId(remoteDocument.getFacilityIntegerPk().getCaisiItemId().toString());
		eDoc.setDocPublic(""+remoteDocument.getPublic1());
		eDoc.setFileName(remoteDocument.getDocFilename());
		eDoc.setHtml(remoteDocument.getDocXml());
		eDoc.setModule("demographic");
		eDoc.setModuleId(""+remoteDocument.getCaisiDemographicId());
		eDoc.setNumberOfPages(remoteDocument.getNumberOfPages());
		eDoc.setObservationDate(MiscUtils.toDate(remoteDocument.getObservationDate()));
		eDoc.setProgramId(remoteDocument.getProgramId());
		eDoc.setResponsibleId(remoteDocument.getResponsible());
		eDoc.setReviewDateTimeDate(MiscUtils.toDate(remoteDocument.getReviewDateTime()));
		eDoc.setReviewDateTime(DateUtils.formatDate(remoteDocument.getReviewDateTime(), null));
		eDoc.setReviewerId(remoteDocument.getReviewer());
		eDoc.setSource(remoteDocument.getSource());
		eDoc.setStatus(remoteDocument.getStatus()!=null&&remoteDocument.getStatus().length()>0?remoteDocument.getStatus().charAt(0):' ');
		eDoc.setType(remoteDocument.getContentType());

	    return(eDoc);
    }

	public static void subtractOnePage(String docId) {
		Document doc = documentDao.find(Integer.parseInt(docId));
		doc.setNumberofpages(doc.getNumberofpages()-1);

		documentDao.merge(doc);
	}
}
