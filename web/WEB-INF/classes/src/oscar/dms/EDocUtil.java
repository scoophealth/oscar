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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.util.SpringUtils;

import oscar.MyDateFormat;
import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.oscarDB.DBPreparedHandlerParam;
import oscar.util.SqlUtilBaseS;
import oscar.util.UtilDateUtilities;

// all SQL statements here
public class EDocUtil extends SqlUtilBaseS {
    static Log log = LogFactory.getLog(EDocUtil.class);

    public static final String PUBLIC = "public";
    public static final String PRIVATE = "private";
    public static final String SORT_DATE = "d.updatedatetime DESC, d.updatedatetime DESC";
    public static final String SORT_DESCRIPTION = "d.docdesc, d.updatedatetime DESC";
    public static final String SORT_DOCTYPE = "d.doctype, d.updatedatetime DESC";
    public static final String SORT_CREATOR = "d.doccreator, d.updatedatetime DESC";
    public static final String SORT_OBSERVATIONDATE = "d.observationdate DESC, d.updatedatetime DESC";
    public static final String SORT_CONTENTTYPE = "d.contenttype, d.updatedatetime DESC";
    public static final boolean ATTACHED = true;
    public static final boolean UNATTACHED = false;

    public static final String DMS_DATE_FORMAT = "yyyy/MM/dd";

    private static ProgramManager programManager = (ProgramManager) SpringUtils.beanFactory.getBean("programManager");

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
            sqe.printStackTrace();
        }
        return moduleName;
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
            sqe.printStackTrace();
        }
        return doctypes;
    }

    public static void addDocumentSQL(EDoc newDocument) {

        String preparedSQL = "INSERT INTO document (doctype, docdesc, docxml, docfilename, doccreator, program_id, updatedatetime, status, contenttype, public1, observationdate) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        DBPreparedHandlerParam[] param = new DBPreparedHandlerParam[11];
        int counter = 0;
        param[counter++] = new DBPreparedHandlerParam(org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getType()));
        param[counter++] = new DBPreparedHandlerParam(org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getDescription()));
        param[counter++] = new DBPreparedHandlerParam(org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getHtml()));
        param[counter++] = new DBPreparedHandlerParam(org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getFileName()));
        param[counter++] = new DBPreparedHandlerParam(newDocument.getCreatorId());
        param[counter++] = new DBPreparedHandlerParam(newDocument.getProgramId());

        java.sql.Date od1 = MyDateFormat.getSysDate(newDocument.getDateTimeStamp());
        param[counter++] = new DBPreparedHandlerParam(od1);

        param[counter++] = new DBPreparedHandlerParam(String.valueOf(newDocument.getStatus()));
        param[counter++] = new DBPreparedHandlerParam(newDocument.getContentType());
        param[counter++] = new DBPreparedHandlerParam(newDocument.getDocPublic());
        java.sql.Date od2 = MyDateFormat.getSysDate(newDocument.getObservationDate());
        param[counter++] = new DBPreparedHandlerParam(od2);

        /*
         * String documentSql = "INSERT INTO document (doctype, docdesc, docxml, docfilename, doccreator, updatedatetime, status, contenttype, public1, observationdate) " + "VALUES ('" +
         * org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getType()) + "', '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getDescription()) + "', '" +
         * org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getHtml()) + "', '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getFileName()) + "', '" + newDocument.getCreatorId() + "', '" + newDocument.getDateTimeStamp() +
         * "', '" + newDocument.getStatus() + "', '" + newDocument.getContentType() + "', '" + newDocument.getDocPublic() + "', '" + newDocument.getObservationDate() + "')";
         * 
         * String document_no = runSQLinsert(documentSql); System.out.println("addDoc: " + documentSql);
         */
        runPreparedSql(preparedSQL, param);
        String document_no = getLastDocumentNo();
        String ctlDocumentSql = "INSERT INTO ctl_document VALUES ('" + newDocument.getModule() + "', " + newDocument.getModuleId() + ", " + document_no + ", '" + newDocument.getStatus() + "')";
        System.out.println("add ctl_document: " + ctlDocumentSql);
        runSQL(ctlDocumentSql);
    }

    public static void detachDocConsult(String docNo, String consultId) {
        String sql = "UPDATE consultdocs SET deleted = 'Y' WHERE requestId = " + consultId + " AND document_no = " + docNo + " AND doctype = 'D'";
        System.out.println("detachDoc: " + sql);
        runSQL(sql);
    }

    public static void attachDocConsult(String providerNo, String docNo, String consultId) {
        String sql = "INSERT INTO consultdocs (requestId,document_no,doctype,attach_date, provider_no) VALUES(" + consultId + "," + docNo + ",'D', now(), '" + providerNo + "')";
        System.out.println("attachDoc: " + sql);
        runSQL(sql);
    }

    public static void editDocumentSQL(EDoc newDocument) {
        String doctype = org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getType());
        String docDescription = org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getDescription());
        String docFileName = org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getFileName());
        String html = org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getHtml());
        System.out.println("obs date: " + newDocument.getObservationDate());

        /*
         * String editDocSql = "UPDATE document SET doctype='" + doctype + "', docdesc='" + docDescription + "', updatedatetime='" + getDmsDateTime() + "', public1='" + newDocument.getDocPublic() + "', observationdate='" + newDocument.getObservationDate() +
         * "', docxml='" + html + "'"; if (docFileName.length() > 0) { editDocSql = editDocSql + ", docfilename='" + docFileName + "', contenttype='" + newDocument.getContentType() + "'"; } editDocSql = editDocSql + " WHERE document_no=" +
         * newDocument.getDocId(); System.out.println("doceditSQL: " + editDocSql); runSQL(editDocSql);
         */
        String editDocSql = "UPDATE document SET doctype='" + doctype + "', docdesc='" + docDescription + "', updatedatetime=?, public1='" + newDocument.getDocPublic() + "', observationdate=?, docxml='" + html + "'";
        if (docFileName.length() > 0) {
            editDocSql = editDocSql + ", docfilename='" + docFileName + "', contenttype='" + newDocument.getContentType() + "'";
        }
        editDocSql = editDocSql + " WHERE document_no=" + newDocument.getDocId();

        DBPreparedHandlerParam[] param = new DBPreparedHandlerParam[2];
        java.sql.Date od1 = MyDateFormat.getSysDate(getDmsDateTime());
        param[0] = new DBPreparedHandlerParam(od1);
        java.sql.Date od2 = MyDateFormat.getSysDate(newDocument.getObservationDate());
        param[1] = new DBPreparedHandlerParam(od2);
        System.out.println("doceditSQL: " + editDocSql);
        runPreparedSql(editDocSql, param);

    }

    public static void indivoRegister(EDoc doc) {
        StringBuffer sql = new StringBuffer("INSERT INTO indivoDocs (oscarDocNo, indivoDocIdx, docType, dateSent, `update`) VALUES(" + doc.getDocId() + ",'" + doc.getIndivoIdx() + "','document',now(),");

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
    public static ArrayList listDocs(String demoNo, String consultationId, boolean attached, Integer currentFacilityId) {
        String sql = "SELECT DISTINCT d.document_no, d.doccreator, d.program_id, d.doctype, d.docdesc, d.observationdate, d.status, d.docfilename, d.contenttype FROM document d, ctl_document c "
                + "WHERE d.status=c.status AND d.status != 'D' AND c.document_no=d.document_no AND " + "c.module='demographic' AND c.module_id = " + demoNo;

        String attachQuery = "SELECT d.document_no, d.doccreator, d.program_id, d.doctype, d.docdesc, d.observationdate, d.status, d.docfilename, d.contenttype FROM document d, consultdocs cd WHERE d.document_no = cd.document_no AND " + "cd.requestId = "
                + consultationId + " AND cd.doctype = 'D' AND cd.deleted IS NULL";

        ArrayList resultDocs = new ArrayList();
        ArrayList attachedDocs = new ArrayList();

        try {
            ResultSet rs = getSQL(attachQuery);
            while (rs.next()) {
                EDoc currentdoc = new EDoc();
                currentdoc.setDocId(rsGetString(rs, "document_no"));
                currentdoc.setDescription(rsGetString(rs, "docdesc"));
                currentdoc.setFileName(rsGetString(rs, "docfilename"));
                currentdoc.setContentType(rsGetString(rs, "contenttype"));
                currentdoc.setCreatorId(rsGetString(rs, "doccreator"));
                String temp = rsGetString(rs, "program_id");
                if (temp != null && temp.length()>0) currentdoc.setProgramId(Integer.valueOf(temp));
                currentdoc.setType(rsGetString(rs, "doctype"));
                currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
                currentdoc.setObservationDate(rsGetString(rs, "observationdate"));
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
                    String temp = rsGetString(rs, "program_id");
                    if (temp != null && temp.length()>0) currentdoc.setProgramId(Integer.valueOf(temp));
                    currentdoc.setType(rsGetString(rs, "doctype"));
                    currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
                    currentdoc.setObservationDate(rsGetString(rs, "observationdate"));

                    if (!attachedDocs.contains(currentdoc)) resultDocs.add(currentdoc);
                }
                rs.close();
            }
            else resultDocs = attachedDocs;

        }
        catch (SQLException sqe) {
            sqe.printStackTrace();
        }

        if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
            resultDocs = documentFacilityFiltering(currentFacilityId, resultDocs);
        }

        return resultDocs;
    }

    public static ArrayList<EDoc> listDocs(String module, String moduleid, String docType, String publicDoc, String sort, Integer currentFacilityId) {
        // sort must be not null
        // docType = null or = "all" to show all doctypes
        // select publicDoc and sorting from static variables for this class i.e. sort=EDocUtil.SORT_OBSERVATIONDATE
        // sql base (prefix) to avoid repetition in the if-statements
        String sql = "SELECT DISTINCT c.module, c.module_id, d.doccreator, d.program_id, d.status, d.docdesc, d.docfilename, d.doctype, d.document_no, d.updatedatetime, d.contenttype, d.observationdate FROM document d, ctl_document c WHERE d.status=c.status AND d.status != 'D' AND c.document_no=d.document_no AND c.module='"
                + module + "'";
        // if-statements to select the where condition (suffix)
        if (publicDoc.equals(PUBLIC)) {
            if ((docType == null) || (docType.equals("all")) || (docType.equals(""))) sql = sql + " AND d.public1=1";
            else sql = sql + " AND d.public1=1 AND d.doctype='" + docType + "'";
        }
        else {
            if ((docType == null) || (docType.equals("all")) || (docType.equals(""))) sql = sql + " AND c.module_id='" + moduleid + "' AND d.public1=0";
            else sql = sql + " AND c.module_id='" + moduleid + "' AND d.public1=0 AND d.doctype='" + docType + "'";
        }
        sql = sql + " ORDER BY " + sort;
        log.debug("sql list: " + sql);
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
                String temp = rsGetString(rs, "program_id");
                if (temp != null && temp.length()>0) currentdoc.setProgramId(Integer.valueOf(temp));
                currentdoc.setDateTimeStamp(rsGetString(rs, "updatedatetime"));
                currentdoc.setFileName(rsGetString(rs, "docfilename"));
                currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
                currentdoc.setContentType(rsGetString(rs, "contenttype"));
                currentdoc.setObservationDate(rsGetString(rs, "observationdate"));
                resultDocs.add(currentdoc);
            }
            rs.close();
        }
        catch (SQLException sqe) {
            sqe.printStackTrace();
        }

        if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
            resultDocs = documentFacilityFiltering(currentFacilityId, resultDocs);
        }

        return resultDocs;
    }

    private static ArrayList<EDoc> documentFacilityFiltering(Integer currentFacilityId, List<EDoc> eDocs) {
        ArrayList<EDoc> results = new ArrayList<EDoc>();

        for (EDoc eDoc : eDocs) {
            Integer programId = eDoc.getProgramId();
            if (programManager.hasAccessBasedOnFacility(currentFacilityId, programId)) results.add(eDoc);
        }

        return results;
    }

    public static ArrayList<EDoc> listDemoDocs(String moduleid, Integer currentFacilityId) {
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
                String temp = rsGetString(rs, "program_id");
                if (temp != null && temp.length()>0) currentdoc.setProgramId(Integer.valueOf(temp));
                currentdoc.setDateTimeStamp(rsGetString(rs, "updatedatetime"));
                currentdoc.setContentType(rsGetString(rs, "contenttype"));
                currentdoc.setObservationDate(rsGetString(rs, "observationdate"));
                resultDocs.add(currentdoc);
            }
            rs.close();
        }
        catch (SQLException sqe) {
            sqe.printStackTrace();
        }

        if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
            resultDocs = documentFacilityFiltering(currentFacilityId, resultDocs);
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
            sqe.printStackTrace();
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
                currentdoc.setDateTimeStamp(rsGetString(rs, "updatedatetime"));
                currentdoc.setFileName(rsGetString(rs, "docfilename"));
                currentdoc.setDocPublic(rsGetString(rs, "public1"));
                currentdoc.setObservationDate(rs.getDate("observationdate"));
                currentdoc.setHtml(rsGetString(rs, "docxml"));
                currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
                currentdoc.setContentType(rsGetString(rs, "contenttype"));

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
            sqe.printStackTrace();
        }
        return currentdoc;
    }

    public String getDocumentName(String id) {
        String filename = null;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select docfilename from document where document_no = '" + id + "'";
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
                filename = oscar.Misc.getString(rs, "docfilename");
            }
            rs.close();
            db.CloseConn();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return filename;
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

    public static int addDocument(String demoNo, String docFileName, String docDesc, String docType, String contentType, String observationDate, String updateDateTime, String docCreator) throws SQLException {
        String add_record_string1 = "insert into document (doctype,docdesc,docfilename,doccreator,updatedatetime,status,contenttype,public1,observationdate) values (?,?,?,?,?,'A',?,0,?)";
        String add_record_string2 = "insert into ctl_document values ('demographic',?,?,'A')";
        int key = 0;

        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        Connection conn = db.GetConnection();
        PreparedStatement add_record = conn.prepareStatement(add_record_string1);

        add_record.setString(1, docType);
        add_record.setString(2, docDesc);
        add_record.setString(3, docFileName);
        add_record.setString(4, docCreator);
        add_record.setString(5, updateDateTime);
        add_record.setString(6, contentType);
        add_record.setString(7, observationDate);

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
        db.CloseConn();
        return key;
    }

    private static String getLastDocumentNo() {
        String documentNo = null;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select max(document_no) from document";
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
                documentNo = oscar.Misc.getString(rs, 1);
            }
            rs.close();
            db.CloseConn();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return documentNo;
    }

    public byte[] getFile(String fpath) {
        byte[] fdata = null;
        try {
            // first we get length of file and allocate mem for file
            File file = new File(fpath);
            long length = file.length();
            fdata = new byte[(int) length];
            // System.out.println("Size of file is " + length);

            // now we read file into array buffer
            FileInputStream fis = new FileInputStream(file);
            fis.read(fdata);
            fis.close();

        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        return fdata;
    }

}
