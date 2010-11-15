/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.casemgmt.dao.CaseManagementNoteLinkDAO;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.MyDateFormat;
import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.oscarDB.DBPreparedHandlerParam;
import oscar.util.SqlUtilBaseS;
import oscar.util.UtilDateUtilities;

// all SQL statements here
public final class EDocUtil extends SqlUtilBaseS {
    static Logger log=MiscUtils.getLogger();

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

    public static ArrayList getCurrentDocs(String tag) {
        // return TagUtil.getObjects(tag, "EDoc");
        return new ArrayList();
    }

    public static String getModuleName(String module, String moduleid) {
        String sql = "SELECT * FROM " + module + " WHERE " + module + "_no LIKE '" + moduleid + "'";
        ResultSet rs = getSQL(sql);
        String moduleName = "";
        try {
            if (rs.next()) {
                moduleName = oscar.Misc.getString(rs, "first_name") + ", " + oscar.Misc.getString(rs, "last_name");
            }
        }
        catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
        }
        return moduleName;
    }

    public static String getProviderInfo(String fieldName, String providerNo) {
        String sql = "SELECT * FROM provider WHERE provider_no='" + providerNo + "'";
        ResultSet rs = getSQL(sql);
        String info = "";
        try {
            if (rs.next()) info = rs.getString(fieldName);
        }
        catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
        }
        return info;
    }

    public static ArrayList getDoctypes(String module) {
        String sql = "SELECT * FROM ctl_doctype WHERE (status = 'A' OR status='H') AND module = '" + module + "'";
        ResultSet rs = getSQL(sql);
        ArrayList doctypes = new ArrayList();
        String doctype = "";
        try {
            while (rs.next()) {
                doctype = oscar.Misc.getString(rs, "doctype");
                doctypes.add(doctype);
            }
        }
        catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
        }
        return doctypes;
    }
 

    public static void addCaseMgmtNoteLink(CaseManagementNoteLink cmnl) {
		caseManagementNoteLinkDao.save(cmnl);
        MiscUtils.getLogger().debug("ADD CASEMGMT NOTE LINK : Id=" + cmnl.getId());
    }

    /**
     * @return the new documentId
     */
    public static String addDocumentSQL(EDoc newDocument) {
        String preparedSQL = "INSERT INTO document (doctype, docdesc, docxml, docfilename, doccreator, source, responsible, program_id, updatedatetime, status, contenttype, public1, observationdate,number_of_pages,appointment_no) VALUES (?,?,?, ?,?,?, ?,?,?, ?,?,?,?,?,?)";
        DBPreparedHandlerParam[] param = new DBPreparedHandlerParam[15];
        int counter = 0;
        param[counter++] = new DBPreparedHandlerParam(newDocument.getType());
        param[counter++] = new DBPreparedHandlerParam(newDocument.getDescription());
        param[counter++] = new DBPreparedHandlerParam(newDocument.getHtml());
        param[counter++] = new DBPreparedHandlerParam(newDocument.getFileName());
        param[counter++] = new DBPreparedHandlerParam(newDocument.getCreatorId());
	param[counter++] = new DBPreparedHandlerParam(newDocument.getSource());
	param[counter++] = new DBPreparedHandlerParam(newDocument.getResponsibleId());
        param[counter++] = new DBPreparedHandlerParam(newDocument.getProgramId());

        java.sql.Timestamp od1 = new java.sql.Timestamp(newDocument.getDateTimeStampAsDate().getTime());
        param[counter++] = new DBPreparedHandlerParam(od1);

        param[counter++] = new DBPreparedHandlerParam(String.valueOf(newDocument.getStatus()));
        param[counter++] = new DBPreparedHandlerParam(newDocument.getContentType());
        param[counter++] = new DBPreparedHandlerParam(newDocument.getDocPublic());
        java.sql.Date od2 = MyDateFormat.getSysDate(newDocument.getObservationDate());
        param[counter++] = new DBPreparedHandlerParam(od2);
        param[counter++] = new DBPreparedHandlerParam(newDocument.getNumberOfPages());
        param[counter++] = new DBPreparedHandlerParam(newDocument.getAppointmentNo());

        /*
         * String documentSql = "INSERT INTO document (doctype, docdesc, docxml, docfilename, doccreator, updatedatetime, status, contenttype, public1, observationdate) " + "VALUES ('" +
         * org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getType()) + "', '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getDescription()) + "', '" +
         * org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getHtml()) + "', '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getFileName()) + "', '" + newDocument.getCreatorId() + "', '" + newDocument.getDateTimeStamp() +
         * "', '" + newDocument.getStatus() + "', '" + newDocument.getContentType() + "', '" + newDocument.getDocPublic() + "', '" + newDocument.getObservationDate() + "')";
         *
         * String document_no = runSQLinsert(documentSql); MiscUtils.getLogger().debug("addDoc: " + documentSql);
         */
        
        runPreparedSql(preparedSQL, param);
        String document_no = getLastDocumentNo();
        String ctlDocumentSql = "INSERT INTO ctl_document (module,module_id,document_no,status) VALUES ('" + newDocument.getModule() + "', " + newDocument.getModuleId() + ", " + document_no + ", '" + newDocument.getStatus() + "'  )";
       
        MiscUtils.getLogger().debug("in addDocumentSQL ,add ctl_document: " + ctlDocumentSql);
        runSQL(ctlDocumentSql);
        return document_no;
    }

   
    public static void detachDocConsult(String docNo, String consultId) {
        String sql = "UPDATE consultdocs SET deleted = 'Y' WHERE requestId = " + consultId + " AND document_no = " + docNo + " AND doctype = 'D'";
        MiscUtils.getLogger().debug("detachDoc: " + sql);
        runSQL(sql);
    }

    public static void attachDocConsult(String providerNo, String docNo, String consultId) {
        String sql = "INSERT INTO consultdocs (requestId,document_no,doctype,attach_date, provider_no) VALUES(" + consultId + "," + docNo + ",'D', now(), '" + providerNo + "')";
        MiscUtils.getLogger().debug("attachDoc: " + sql);
        runSQL(sql);
    }

    public static void editDocumentSQL(EDoc newDocument, boolean doReview) {
        String doctype = org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getType());
        String docDescription = org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getDescription());
        String docFileName = org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getFileName());
        String html = org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getHtml());

	String editDocSql = "UPDATE document " +
		"SET doctype='" + doctype + "', " +
		"docdesc='" + docDescription + "', " +
		"source='" + newDocument.getSource() + "', " +
		"public1='" + newDocument.getDocPublic() + "', " +
		"responsible='" + newDocument.getResponsibleId() + "', " +
		"docxml='" + html + "'";
	if (doReview) {
	    editDocSql += ", reviewer='" + newDocument.getReviewerId() + "', " +
		    "reviewdatetime='" + newDocument.getReviewDateTime() + "'";
	} else {
	    editDocSql += ", reviewer=NULL, reviewdatetime=NULL, observationdate=?, updatedatetime=?";
	}
        if (docFileName.length() > 0) {
            editDocSql = editDocSql + ", docfilename='" + docFileName + "', contenttype='" + newDocument.getContentType() + "'";
        }
        editDocSql = editDocSql + " WHERE document_no=" + newDocument.getDocId();

	if (doReview) {
	    runSQL(editDocSql);
	} else {
	    DBPreparedHandlerParam[] param = new DBPreparedHandlerParam[2];
	    java.sql.Date od1 = MyDateFormat.getSysDate(newDocument.getObservationDate());
	    param[0] = new DBPreparedHandlerParam(od1);
	    java.sql.Timestamp od2 = new java.sql.Timestamp(new Date().getTime());
	    param[1] = new DBPreparedHandlerParam(od2);
	    runPreparedSql(editDocSql, param);
	}
    }

    public static void indivoRegister(EDoc doc) {
        StringBuilder sql = new StringBuilder("INSERT INTO indivoDocs (oscarDocNo, indivoDocIdx, docType, dateSent, `update`) VALUES(" + doc.getDocId() + ",'" + doc.getIndivoIdx() + "','document',now(),");

        if (doc.isInIndivo()) sql.append("'U')");
        else sql.append("'I')");

        runSQL(sql.toString());
    }

    /*
     * document +----------------+--------------+------+-----+---------+----------------+ | Field | Type | Null | Key | Default | Extra | +----------------+--------------+------+-----+---------+----------------+ | document_no | int(6) | | PRI | NULL |
     * auto_increment | | doctype | varchar(20) | YES | | NULL | | | docdesc | varchar(255) | | | | | | docxml | text | YES | | NULL | | | docfilename | varchar(255) | | | | | | doccreator | varchar(30) | | | | | | updatedatetime | datetime | YES | | NULL | | |
     * status | char(1) | | | | | +----------------+--------------+------+-----+---------+----------------+
     *
     * ctl_document +-------------+-------------+------+-----+---------+-------+ | Field | Type | Null | Key | Default | Extra | +-------------+-------------+------+-----+---------+-------+ | module | varchar(30) | | | | | | module_id | int(6) | | | 0 | | |
     * document_no | int(6) | | | 0 | | | status | char(1) | YES | | NULL | | +-------------+-------------+------+-----+---------+-------+
     */

    /**
     * Fetches all consult docs attached to specific consultation
     */
    public static ArrayList<EDoc> listDocs(String demoNo, String consultationId, boolean attached) {
        String sql = "SELECT DISTINCT d.document_no, d.doccreator, d.source, d.responsible, d.program_id, d.doctype, d.docdesc, d.observationdate, d.status, d.docfilename, d.contenttype, d.reviewer, d.reviewdatetime, d.appointment_no FROM document d, ctl_document c "
                + "WHERE d.status=c.status AND d.status != 'D' AND c.document_no=d.document_no AND " + "c.module='demographic' AND c.module_id = " + demoNo;

        String attachQuery = "SELECT d.document_no, d.doccreator, d.source, d.responsible, d.program_id, d.doctype, d.docdesc, d.observationdate, d.status, d.docfilename, d.contenttype, d.reviewer, d.reviewdatetime, d.appointment_no FROM document d, consultdocs cd " +
		"WHERE d.document_no = cd.document_no AND " + "cd.requestId = "+consultationId+" AND cd.doctype = 'D' AND cd.deleted IS NULL";

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
		currentdoc.setResponsibleId(rsGetString(rs, "responsible"));
                String temp = rsGetString(rs, "program_id");
                if (temp != null && temp.length()>0) currentdoc.setProgramId(Integer.valueOf(temp));
                temp = rsGetString(rs, "appointment_no");
                if (temp != null && temp.length()>0) currentdoc.setAppointmentNo(Integer.valueOf(temp));
                currentdoc.setType(rsGetString(rs, "doctype"));
                currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
                currentdoc.setObservationDate(rsGetString(rs, "observationdate"));
		currentdoc.setReviewerId(rsGetString(rs, "reviewer"));
		currentdoc.setReviewDateTime(rsGetString(rs, "reviewdatetime"));
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
		    currentdoc.setResponsibleId(rsGetString(rs, "responsible"));
                    String temp = rsGetString(rs, "program_id");
                    if (temp != null && temp.length()>0) currentdoc.setProgramId(Integer.valueOf(temp));
                    temp = rsGetString(rs, "appointment_no");
                    if (temp != null && temp.length()>0) currentdoc.setAppointmentNo(Integer.valueOf(temp));
                    currentdoc.setType(rsGetString(rs, "doctype"));
                    currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
                    currentdoc.setObservationDate(rsGetString(rs, "observationdate"));
		    currentdoc.setReviewerId(rsGetString(rs, "reviewer"));
		    currentdoc.setReviewDateTime(rsGetString(rs, "reviewdatetime"));

                    if (!attachedDocs.contains(currentdoc)) resultDocs.add(currentdoc);
                }
                rs.close();
            }
            else resultDocs = attachedDocs;

        }
        catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
        }

        if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
            resultDocs = documentFacilityFiltering(resultDocs);
        }

        return resultDocs;
    }

    public static ArrayList<EDoc> listDocs(String module, String moduleid, String docType, String publicDoc, String sort) {
        return  listDocs( module,  moduleid,  docType,  publicDoc,  sort, "active") ;
    }

    public static EDoc getEDocFromDocId(String docId){
         String sql = "SELECT DISTINCT c.module, c.module_id, d.doccreator, d.source, d.responsible, d.program_id, "
                + "d.status, d.docdesc, d.docfilename, d.doctype, d.document_no, d.updatedatetime, d.contenttype, d.observationdate, d.reviewer, d.reviewdatetime, d.appointment_no " +
		"FROM document d, ctl_document c WHERE "
                + " c.document_no=d.document_no AND c.document_no='" + docId + "'";
        sql = sql + " ORDER BY " + EDocUtil.SORT_OBSERVATIONDATE;//default sort

        ResultSet rs = getSQL(sql);
        EDoc currentdoc = new EDoc();
        try {
          if(rs.first()){
      
                currentdoc.setModule(rsGetString(rs, "module"));
                currentdoc.setModuleId(rsGetString(rs, "module_id"));
                currentdoc.setDocId(rsGetString(rs, "document_no"));
                currentdoc.setDescription(rsGetString(rs, "docdesc"));
                currentdoc.setType(rsGetString(rs, "doctype"));
                currentdoc.setCreatorId(rsGetString(rs, "doccreator"));
		currentdoc.setSource(rsGetString(rs, "source"));
		currentdoc.setResponsibleId(rsGetString(rs, "responsible"));
                String temp = rsGetString(rs, "program_id");
                if (temp != null && temp.length()>0) currentdoc.setProgramId(Integer.valueOf(temp));
                temp = rsGetString(rs, "appointment_no");
                if (temp != null && temp.length()>0) currentdoc.setAppointmentNo(Integer.valueOf(temp));
                currentdoc.setDateTimeStamp(rsGetString(rs, "updatedatetime"));
                currentdoc.setFileName(rsGetString(rs, "docfilename"));
                currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
                currentdoc.setContentType(rsGetString(rs, "contenttype"));
                currentdoc.setObservationDate(rsGetString(rs, "observationdate"));
		currentdoc.setReviewerId(rsGetString(rs, "reviewer"));
		currentdoc.setReviewDateTime(rsGetString(rs, "reviewdatetime"));
    
      
            rs.close();
          }
        }
        catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
        }

        return currentdoc;
    }
    public static ArrayList<EDoc> listDocsPreviewInbox(List<String> docIds){
     
        ArrayList<EDoc> resultDocs = new ArrayList<EDoc>();
        for(String docId:docIds){
            EDoc currentdoc = new EDoc();
            currentdoc=getEDocFromDocId(docId);
            resultDocs.add(currentdoc);
        }
        return resultDocs;
    }
    public static ArrayList<EDoc> listDocs(String module, String moduleid, String docType, String publicDoc, String sort, String viewstatus) {
        // sort must be not null
        // docType = null or = "all" to show all doctypes
        // select publicDoc and sorting from static variables for this class i.e. sort=EDocUtil.SORT_OBSERVATIONDATE
        // sql base (prefix) to avoid repetition in the if-statements
        String sql = "SELECT DISTINCT c.module, c.module_id, d.doccreator, d.source, d.responsible, d.program_id, d.status, d.docdesc, d.docfilename, d.doctype, d.document_no, d.updatedatetime, d.contenttype, d.observationdate, d.reviewer, d.reviewdatetime, d.appointment_no " +
		"FROM document d, ctl_document c WHERE c.document_no=d.document_no AND c.module='" + module + "'";
        // if-statements to select the where condition (suffix)
        if (publicDoc.equals(PUBLIC)) {
            if (docType==null || docType.equals("all") || docType.length()==0) sql = sql + " AND d.public1=1";
            else sql = sql + " AND d.public1=1 AND d.doctype='" + docType + "'";
        }
        else {
            if (docType==null || docType.equals("all") || docType.length()==0) sql = sql + " AND c.module_id='" + moduleid + "' AND d.public1=0";
            else sql = sql + " AND c.module_id='" + moduleid + "' AND d.public1=0 AND d.doctype='" + docType + "'";
        }

        if( viewstatus.equals("deleted") ) {
            sql += " AND d.status = 'D'";
        }
        else if( viewstatus.equals("active") ) {
            sql += " AND d.status != 'D'";
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
		currentdoc.setResponsibleId(rsGetString(rs, "responsible"));
                String temp = rsGetString(rs, "program_id");
                if (temp != null && temp.length()>0) currentdoc.setProgramId(Integer.valueOf(temp));
                temp = rsGetString(rs, "appointment_no");
                if (temp != null && temp.length()>0) currentdoc.setAppointmentNo(Integer.valueOf(temp));
                currentdoc.setDateTimeStamp(rsGetString(rs, "updatedatetime"));
                currentdoc.setFileName(rsGetString(rs, "docfilename"));
                currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
                currentdoc.setContentType(rsGetString(rs, "contenttype"));
                currentdoc.setObservationDate(rsGetString(rs, "observationdate"));
		currentdoc.setReviewerId(rsGetString(rs, "reviewer"));
		currentdoc.setReviewDateTime(rsGetString(rs, "reviewdatetime"));
                resultDocs.add(currentdoc);
            }
            rs.close();
        }
        catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
        }

        if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
            resultDocs = documentFacilityFiltering(resultDocs);
        }

        return resultDocs;
    }

    public ArrayList<EDoc> getUnmatchedDocuments(String creator, String responsible, Date startDate, Date endDate, boolean unmatchedDemographics){
       ArrayList<EDoc> list = new ArrayList();
       //boolean matchedDemographics = true;
       String sql= "SELECT DISTINCT c.module, c.module_id, d.doccreator, d.source, d.responsible, d.program_id, d.status, d.docdesc, d.docfilename, d.doctype, d.document_no, d.updatedatetime, d.contenttype, d.observationdate, d.appointment_no FROM document d, ctl_document c WHERE c.document_no=d.document_no AND c.module='demographic' and doccreator = ? and responsible = ? and updatedatetime >= ?  and updatedatetime <= ?";
       if (unmatchedDemographics){
           sql += " and c.module_id = -1 ";
       }
       /*else if (matchedDemographics){
           sql += " and c.module_id != -1 ";
       }*/
       try{
            java.sql.Date  sDate =  new java.sql.Date(startDate.getTime());
            java.sql.Date eDate  = new java.sql.Date(endDate.getTime());
            MiscUtils.getLogger().debug("Creator "+creator+" start "+sDate + " end "+eDate);

            Connection c=DbConnectionFilter.getThreadLocalDbConnection();
            PreparedStatement ps =  c.prepareStatement(sql);
            ps.setString(1,creator);
	    ps.setString(2,responsible);
            ps.setDate(3,new java.sql.Date(startDate.getTime()));
            ps.setDate(4,new java.sql.Date(endDate.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
               MiscUtils.getLogger().debug("DOCFILENAME "+rs.getString("docfilename"));
                EDoc currentdoc = new EDoc();
                currentdoc.setModule(rsGetString(rs, "module"));
                currentdoc.setModuleId(rsGetString(rs, "module_id"));
                currentdoc.setDocId(rsGetString(rs, "document_no"));
                currentdoc.setDescription(rsGetString(rs, "docdesc"));
                currentdoc.setType(rsGetString(rs, "doctype"));
                currentdoc.setCreatorId(rsGetString(rs, "doccreator"));
		currentdoc.setSource(rsGetString(rs, "source"));
		currentdoc.setResponsibleId(rsGetString(rs, "responsible"));
                String temp = rsGetString(rs, "program_id");
                if (temp != null && temp.length()>0) currentdoc.setProgramId(Integer.valueOf(temp));
                temp = rsGetString(rs, "appointment_no");
                if (temp != null && temp.length()>0) currentdoc.setAppointmentNo(Integer.valueOf(temp));
                currentdoc.setDateTimeStamp(rsGetString(rs, "updatedatetime"));
                currentdoc.setFileName(rsGetString(rs, "docfilename"));
                currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
                currentdoc.setContentType(rsGetString(rs, "contenttype"));
                currentdoc.setObservationDate(rsGetString(rs, "observationdate"));

               list.add(currentdoc);
            }
            rs.close();
       }catch(Exception e){
           MiscUtils.getLogger().error("Error", e);
       }
       //mysql> SELECT DISTINCT c.module, c.module_id, d.doccreator, d.source, d.program_id, d.status, d.docdesc, d.docfilename, d.doctype, d.document_no, d.updatedatetime, d.contenttype, d.observationdate FROM document d, ctl_document c WHERE c.document_no=d.document_no AND c.module='demographic' and module_id = -1
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

        log.debug("sql list: " + sql);
        ResultSet rs = getSQL(sql);
        ArrayList<EDoc> resultDocs = new ArrayList<EDoc>();
        try {
            while (rs.next()) {
                EDoc currentdoc = new EDoc();
                currentdoc.setType(rsGetString(rs, "doctype"));
                currentdoc.setFileName(rsGetString(rs, "docfilename"));
                currentdoc.setCreatorId(rsGetString(rs, "doccreator"));
		currentdoc.setSource(rsGetString(rs, "source"));
		currentdoc.setResponsibleId(rsGetString(rs, "responsible"));
                String temp = rsGetString(rs, "program_id");
                if (temp != null && temp.length()>0) currentdoc.setProgramId(Integer.valueOf(temp));
                temp = rsGetString(rs, "appointment_no");
                if (temp != null && temp.length()>0) currentdoc.setAppointmentNo(Integer.valueOf(temp));
                currentdoc.setDateTimeStamp(rsGetString(rs, "updatedatetime"));
                currentdoc.setContentType(rsGetString(rs, "contenttype"));
                currentdoc.setObservationDate(rsGetString(rs, "observationdate"));
		currentdoc.setReviewerId(rsGetString(rs, "reviewer"));
		currentdoc.setReviewDateTime(rsGetString(rs, "reviewdatetime"));
                resultDocs.add(currentdoc);
            }
            rs.close();
        }
        catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
        }

        if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
            resultDocs = documentFacilityFiltering(resultDocs);
        }

        return resultDocs;
    }

    public static ArrayList listModules() {
        String sql = "SELECT DISTINCT module FROM ctl_doctype";
        ResultSet rs = getSQL(sql);
        ArrayList modules = new ArrayList();
        try {
            while (rs.next()) {
                modules.add(rsGetString(rs, "module"));
            }
        }
        catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
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
                currentdoc.setCreatorId(rsGetString(rs, "doccreator"));
		currentdoc.setSource(rsGetString(rs, "source"));
		currentdoc.setResponsibleId(rsGetString(rs, "responsible"));
		currentdoc.setSource(rsGetString(rs, "source"));
                currentdoc.setDateTimeStamp(rsGetString(rs, "updatedatetime"));
                currentdoc.setFileName(rsGetString(rs, "docfilename"));
                currentdoc.setDocPublic(rsGetString(rs, "public1"));
                currentdoc.setObservationDate(rs.getDate("observationdate"));
		currentdoc.setReviewerId(rsGetString(rs, "reviewer"));
		currentdoc.setReviewDateTime(rsGetString(rs, "reviewdatetime"));
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
        }
        catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
        }
        return currentdoc;
    }

    public String getDocumentName(String id) {
        String filename = null;
        try {
            String sql = "select docfilename from document where document_no = '" + id + "'";
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                filename = oscar.Misc.getString(rs, "docfilename");
            }
            rs.close();
        }
        catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return filename;
    }

    public static void undeleteDocument(String documentNo) {
        // String nowDate = getDmsDateTime();
        // String sql = "UPDATE document SET status='D', updatedatetime='" + nowDate + "' WHERE document_no=" + documentNo;
        // runSQL(sql);


        try {
            String sql = "select status from ctl_document where document_no=" + documentNo;
            String status = "";
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                status = rs.getString("status");
            }
            rs.close();
            DBPreparedHandlerParam[] param = new DBPreparedHandlerParam[2];
            java.sql.Date od1 = MyDateFormat.getSysDate(getDmsDateTime());
            param[0] = new DBPreparedHandlerParam(status);
            param[1] = new DBPreparedHandlerParam(od1);

            String updateSql = "UPDATE document SET status=?, updatedatetime=? WHERE document_no=" + documentNo;

            runPreparedSql(updateSql, param);

        }
        catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
    }


    public static void deleteDocument(String documentNo) {
        // String nowDate = getDmsDateTime();
        // String sql = "UPDATE document SET status='D', updatedatetime='" + nowDate + "' WHERE document_no=" + documentNo;
        // runSQL(sql);

        DBPreparedHandlerParam[] param = new DBPreparedHandlerParam[1];
        java.sql.Date od1 = MyDateFormat.getSysDate(getDmsDateTime());
        param[0] = new DBPreparedHandlerParam(od1);

        String updateSql = "UPDATE document SET status='D', updatedatetime=? WHERE document_no=" + documentNo;

        runPreparedSql(updateSql, param);
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

    public static int addDocument(String demoNo, String docFileName, String docDesc, String docType, String contentType, String observationDate, String updateDateTime, String docCreator, String responsible) throws SQLException {
	return addDocument(demoNo, docFileName, docDesc, docType, contentType, observationDate, updateDateTime, docCreator, responsible, null, null);
    }

    public static int addDocument(String demoNo, String docFileName, String docDesc, String docType, String contentType, String observationDate, String updateDateTime, String docCreator, String responsible, String reviewer, String reviewDateTime) throws SQLException {
	return addDocument(demoNo, docFileName, docDesc, docType, contentType, observationDate, updateDateTime, docCreator, responsible, reviewer, reviewDateTime, null);
    }

    public static int addDocument(String demoNo, String docFileName, String docDesc, String docType, String contentType, String observationDate, String updateDateTime, String docCreator, String responsible, String reviewer, String reviewDateTime, String source) throws SQLException {
        String add_record_string1 = "insert into document (doctype,docdesc,docfilename,doccreator,responsible,updatedatetime,status,contenttype,public1,observationdate,reviewer,reviewdatetime, source) values (?,?,?,?,?,?,'A',?,0,?,?,?,?)";
        String add_record_string2 = "insert into ctl_document values ('demographic',?,?,'A')";
        int key = 0;

        Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
        PreparedStatement add_record = conn.prepareStatement(add_record_string1);

        add_record.setString(1, docType);
        add_record.setString(2, docDesc);
        add_record.setString(3, docFileName);
        add_record.setString(4, docCreator);
	add_record.setString(5, responsible);
        add_record.setString(6, updateDateTime);
        add_record.setString(7, contentType);
        add_record.setString(8, observationDate);
	add_record.setString(9, reviewer);
	add_record.setString(10, reviewDateTime);
	add_record.setString(11, source);

        add_record.executeUpdate();
        ResultSet rs = add_record.getGeneratedKeys();
        if (rs.next()) key = rs.getInt(1);
        add_record.close();
        rs.close();

        if (key > 0) {
            add_record = conn.prepareStatement(add_record_string2);
            add_record.setString(1, demoNo);
            add_record.setString(2, getLastDocumentNo());

            add_record.executeUpdate();
            rs = add_record.getGeneratedKeys();
            if (rs.next()) key = rs.getInt(1);
            add_record.close();
            rs.close();
        }
        return key;
    }

//private static String getLastDocumentNo() {
    public static String getLastDocumentNo() {
        String documentNo = null;
        try {
            String sql = "select max(document_no) from document";
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                documentNo = oscar.Misc.getString(rs, 1);
            }
            rs.close();
        }        catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return documentNo;
    }
public static String getLastDocumentDesc() {
        String docDesc=null;
        try {
            String docNumber=EDocUtil.getLastDocumentNo();

            String sql = "select docdesc from document where document_no="+docNumber;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                 docDesc= oscar.Misc.getString(rs, 1);
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }

        return docDesc;
    }

    public static String getLastNoteId() {
        String noteId = null;
        try {
            
            String sql = "select max(note_id) from casemgmt_note";
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                noteId = oscar.Misc.getString(rs, 1);
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return noteId;
    }

    public byte[] getFile(String fpath) {
        byte[] fdata = null;
        try {
            // first we get length of file and allocate mem for file
            File file = new File(fpath);
            long length = file.length();
            fdata = new byte[(int) length];

            // now we read file into array buffer
            FileInputStream fis = new FileInputStream(file);
            fis.read(fdata);
            fis.close();

        }        catch (NullPointerException ex) {MiscUtils.getLogger().error("Error", ex);
        }        catch (FileNotFoundException ex) {MiscUtils.getLogger().error("Error", ex);
        }        catch (IOException ex) {MiscUtils.getLogger().error("Error", ex);
        }

        return fdata;
    }

	//add for inbox manager

    public static boolean getDocReviewFlag(String docId){
    	boolean flag=false;
    	try {
            
            String sql = "select reviewed_flag from doc_manager where id="+docId;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                String reviewed = oscar.Misc.getString(rs, "reviewed_flag");
                if (reviewed!=null&&"1".equalsIgnoreCase(reviewed.trim()))
                	flag=true;
            }
            rs.close();
        }
        catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
    	return flag;
    }

    public static boolean getDocUrgentFlag(String docId){
    	boolean flag=false;
    	try {
            
            String sql = "select urgent from doc_manager where id="+docId;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                String urgent = oscar.Misc.getString(rs, "urgent");
                if (urgent!=null&&"1".equalsIgnoreCase(urgent.trim()))
                	flag=true;
            }
            rs.close();
        }
        catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
    	return flag;
    }

       //get noteId from tableId
    public static Long getNoteIdFromDocId(Long docId){
		Long noteId = 0L;
		CaseManagementNoteLink cmnLink = caseManagementNoteLinkDao.getLastLinkByTableId(CaseManagementNoteLink.DOCUMENT, docId);
		if (cmnLink!=null) noteId = cmnLink.getNoteId();
		return noteId;
    }

    //get tableId from noteId when table_name is document
    public static Long getTableIdFromNoteId(Long noteId){
		Long tableId = 0L;
		CaseManagementNoteLink cmnLink = caseManagementNoteLinkDao.getLastLinkByNote(noteId);
		if (cmnLink!=null && cmnLink.getTableName().equals(CaseManagementNoteLink.DOCUMENT)) {
			tableId = cmnLink.getTableId();
		}
		return tableId;
    }

    //get document from its note
    public static EDoc getDocFromNote(Long noteId) {
		EDoc doc = new EDoc();
		Long docIdL = getTableIdFromNoteId(noteId);
		if (docIdL>0L) {
			Integer docId = docIdL.intValue();
			String getDocSql = "select document_no, docfilename, status from document where document_no=" + docId;
			ResultSet rs = getSQL(getDocSql);
			try {
				if (rs.first()) {
					doc.setDocId(rs.getString("document_no"));
					doc.setFileName(rs.getString("docfilename"));
					doc.setStatus(rs.getString("status").charAt(0));
				}
			} catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
			}
		}
		return doc;
    }


}

