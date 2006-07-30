
package oscar.dms;

import oscar.oscarDB.DBHandler;
import java.util.*;
import java.sql.*;
import oscar.util.*;
import oscar.oscarTags.*;
//all SQL statements here
public class EDocUtil extends SqlUtilBase {
    public static final String PUBLIC = "='share'";
    public static final String PRIVATE = "!='share'";
    
    public static ArrayList getCurrentDocs(String tag) {
        //return TagUtil.getObjects(tag, "EDoc");
        return new ArrayList();
    }
    
    public static String getModuleName(String module, String moduleid) {
        String sql = "SELECT * FROM " + module + " WHERE " + module + "_no LIKE '" + moduleid + "'";
        ResultSet rs = getSQL(sql);
        String moduleName = "";
        try {
            if (rs.next()) {
                moduleName = rs.getString("first_name") + ", " + rs.getString("last_name");
            }
        } catch (SQLException sqe) {
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
                doctype = rs.getString("doctype");
                doctypes.add(doctype);
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
        return doctypes;
    }
    
    public static void addDocumentSQL(EDoc newDocument) {
        String documentSql = "INSERT INTO document (doctype, docdesc, docxml, docfilename, doccreator, updatedatetime, status, contenttype) " +
                "VALUES ('" + org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getType()) + "', '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getDescription()) + "', '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getXml()) + "', '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getFileName()) + "', '" + newDocument.getCreatorId() + "', '" + newDocument.getDateTimeStamp() + "', '" + newDocument.getStatus() + "', '" + newDocument.getContentType() + "');";
        String document_no = runSQLinsert(documentSql);
        char ctlStatus = 'A';
        if (!newDocument.getXml().equals("")) 
            ctlStatus = 'H';
        String ctlDocumentSql = "INSERT INTO ctl_document VALUES ('" + newDocument.getModule() + "', " + newDocument.getModuleId() + ", " + document_no + ", '" + ctlStatus + "');";
        runSQL(ctlDocumentSql);
    }
    
   public static void editDocumentSQL(EDoc newDocument) {
       String doctype = org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getType());
       String docDescription = org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getDescription());
       String docFileName = org.apache.commons.lang.StringEscapeUtils.escapeSql(newDocument.getFileName());
       String contentType = newDocument.getContentType();
       String editDocSql = "UPDATE document SET doctype='" + doctype + "', docdesc='" + docDescription + "', updatedatetime='" + getDmsDateTime() + "'";
       if (docFileName.length() > 0) {
           editDocSql = editDocSql + ", docfilename='" + docFileName + "', contenttype='" + newDocument.getContentType() + "'";
       }
       editDocSql = editDocSql + " WHERE document_no=" + newDocument.getDocId();
       System.out.println("doceditSQL: " + editDocSql);
       runSQL(editDocSql);
    }
    
/*
 document
+----------------+--------------+------+-----+---------+----------------+
| Field          | Type         | Null | Key | Default | Extra          |
+----------------+--------------+------+-----+---------+----------------+
| document_no    | int(6)       |      | PRI | NULL    | auto_increment |
| doctype        | varchar(20)  | YES  |     | NULL    |                |
| docdesc        | varchar(255) |      |     |         |                |
| docxml         | text         | YES  |     | NULL    |                |
| docfilename    | varchar(255) |      |     |         |                |
| doccreator     | varchar(30)  |      |     |         |                |
| updatedatetime | datetime     | YES  |     | NULL    |                |
| status         | char(1)      |      |     |         |                |
+----------------+--------------+------+-----+---------+----------------+

 *ctl_document
+-------------+-------------+------+-----+---------+-------+
| Field       | Type        | Null | Key | Default | Extra |
+-------------+-------------+------+-----+---------+-------+
| module      | varchar(30) |      |     |         |       |
| module_id   | int(6)      |      |     | 0       |       |
| document_no | int(6)      |      |     | 0       |       |
| status      | char(1)     | YES  |     | NULL    |       |
+-------------+-------------+------+-----+---------+-------+ 
 */
    
    public static ArrayList listDocs(String module, String moduleid, String typeCondition) {
        //type condition (i.e. "='share'" or "!='share'", etc...
        String sql = "SELECT DISTINCT c.module, c.module_id, d.doccreator, d.status, d.docdesc, d.docfilename, d.doctype, d.document_no, d.updatedatetime, d.contenttype FROM document d, ctl_document c WHERE d.status=c.status AND d.status != 'D' AND c.document_no=d.document_no and c.module='" + module + "' AND c.module_id='" + moduleid + "' AND d.doctype" + typeCondition + " ORDER BY d.updatedatetime DESC";
        ResultSet rs = getSQL(sql);
        ArrayList resultDocs = new ArrayList();
        try {
            while (rs.next()) {
                EDoc currentdoc = new EDoc();
                currentdoc.setModule(rsGetString(rs, "module"));
                currentdoc.setModuleId(rsGetString(rs, "module_id"));
                currentdoc.setDocId(rsGetString(rs, "document_no"));
                currentdoc.setDescription(rsGetString(rs, "docdesc"));
                currentdoc.setType(rsGetString(rs, "doctype"));
                currentdoc.setCreatorId(rsGetString(rs, "doccreator"));
                currentdoc.setDateTimeStamp(rsGetString(rs, "updatedatetime"));
                currentdoc.setFileName(rsGetString(rs, "docfilename"));
                currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
                currentdoc.setContentType(rsGetString(rs,"contenttype"));
                resultDocs.add(currentdoc);
            }
            rs.close();
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }

        return resultDocs;
    }
    
        public static EDoc getDoc(String documentNo) {
        String sql = "SELECT DISTINCT c.module, c.module_id, d.doccreator, d.status, d.docdesc, d.docfilename, d.doctype, d.document_no, d.updatedatetime FROM document d, ctl_document c WHERE d.status=c.status AND d.status != 'D' AND c.document_no=d.document_no AND c.document_no='" + documentNo + "' ORDER BY d.updatedatetime DESC";
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
                currentdoc.setStatus(rsGetString(rs, "status").charAt(0));
            }
            rs.close();
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
        return currentdoc;
    }
        
    public  String getDocumentName(String id){
       String filename = null;
       try {
          DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
          String sql = "select docfilename from document where document_no = '"+id+"'";
          ResultSet rs = db.GetSQL(sql);
          if (rs.next()){
              filename = rs.getString("docfilename");
          }
          rs.close();
          db.CloseConn();
       }catch(SQLException e) { 
           e.printStackTrace(); 
       }
       return filename;
    }
        
        
    
    public static void deleteDocument(String documentNo) {
        String nowDate = getDmsDateTime();
        String sql = "UPDATE document SET status='D', updatedatetime='" + nowDate + "' WHERE document_no=" + documentNo;
        runSQL(sql);
    }

    public static String getDmsDateTime() {
        String nowDateR = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd");
        String nowTimeR = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "HH:mm:ss");
        String dateTimeStamp = nowDateR + " " + nowTimeR;
        return dateTimeStamp;
    }

}
