package oscar.form.data;

import oscar.oscarDB.*;
import oscar.util.*;
import java.util.*;
import java.sql.*;

public class FrmData {
    public class Form {
        private String formName;
        private String formPage;
        private String formTable;

        public Form(String formName, String formPage, String formTable) {
            this.formName = formName;
            this.formPage = formPage;
            this.formTable = formTable;
        }

        public String getFormName() {       return formName;        }
        public String getFormPage() {       return formPage;        }
        public String getFormTable() {      return formTable;       }
    }

    public Form[] getForms() throws java.sql.SQLException {
        ArrayList forms = new ArrayList();

        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        String sql = "SELECT * from encounterForm";
        ResultSet rs = db.GetSQL(sql);
        while(rs.next()) {
            Form frm = new Form(rs.getString("form_name"), rs.getString("form_value"), rs.getString("form_table"));
            forms.add(frm);
        }

        rs.close();
        db.CloseConn();

        Form[] ret = {};
        ret = (Form[])forms.toArray(ret);
        return ret;
    }

    public class PatientForm {
        private String formId;
        private String demoNo;
        private String created;
        private String edited;

        public PatientForm(String formId, String demoNo, String created, String edited) {
            this.formId = formId;
            this.demoNo = demoNo;
            this.created = created;
            this.edited = edited;
        }

        public String getFormId() {            return formId;        }
        public String getDemoNo() {            return demoNo;        }
        public String getCreated() {            return created;        }
        public String getEdited() {            return edited;        }
    }

    public PatientForm[] getPatientForms(String demoNo, String table) throws SQLException {
        ArrayList forms = new ArrayList();

        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        String sql = "SELECT ID, demographic_no, formCreated, formEdited FROM " + table
                    + " WHERE demographic_no=" + demoNo + " ORDER BY ID DESC";
        ResultSet rs = db.GetSQL(sql);
        while(rs.next()) {
            PatientForm frm = new PatientForm(rs.getString("ID"), rs.getString("demographic_no"),
                                UtilDateUtilities.DateToString(rs.getDate("formCreated"), "yy/MM/dd"), UtilDateUtilities.DateToString(rs.getDate("formEdited"), "yy/MM/dd"));
            forms.add(frm);
        }

        rs.close();
        db.CloseConn();

        PatientForm[] ret = {};
        ret = (PatientForm[])forms.toArray(ret);
        return ret;
    }

    public PatientForm getCurrentPatientForm(String demoNo, String studyNo) throws SQLException {
        PatientForm frm = null;

        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        String sql = "SELECT e.form_table from encounterForm e, study s where e.form_name = s.form_name and s.study_no = " + studyNo;
        String table = "";
        ResultSet rs = db.GetSQL(sql);
        while(rs.next()) {
            table = rs.getString("form_table");
        }
        rs = null;

        sql = "SELECT ID, demographic_no, formCreated, formEdited FROM " + table + " WHERE demographic_no=" + demoNo + " ORDER BY ID DESC limit 0,1";
        rs = db.GetSQL(sql);
        while(rs.next()) {
            frm = new PatientForm(rs.getString("ID"), rs.getString("demographic_no"),
                                UtilDateUtilities.DateToString(rs.getDate("formCreated"), "yy/MM/dd"), UtilDateUtilities.DateToString(rs.getDate("formEdited"), "yy/MM/dd"));
        }

        rs.close();
        db.CloseConn();

        return frm;
    }

    public String[] getStudyNameLink(String studyNo) throws java.sql.SQLException {
        String[] ret = new String[2];

        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        String sql = "SELECT study_name, study_link FROM study WHERE study_no=" + studyNo;
        ResultSet rs = db.GetSQL(sql);
        while(rs.next()) {
            ret[0] = rs.getString("study_name");
            ret[1] = rs.getString("study_link");
        }

        rs.close();
        db.CloseConn();
        return ret;
    }

	//ret[0] = form path;   ret[1] = formId
    public String[] getShortcutFormValue(String demoNo, String formName) throws java.sql.SQLException {
        String[] ret = new String[2];
		String table = null;

        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        String sql = "SELECT form_value, form_table FROM encounterForm WHERE form_name='" + formName + "'";
        ResultSet rs = db.GetSQL(sql);
        while(rs.next()) {
            ret[0] = rs.getString("form_value");
            table = rs.getString("form_table");
        }

	ret[1] = "0";
        if (table.equals("form")) {
            sql = "SELECT form_no FROM " + table + " WHERE demographic_no=" + demoNo +" AND form_name='" + formName + "' order by form_no desc limit 0,1";
            rs = db.GetSQL(sql);
            while(rs.next()) {
                ret[1] = rs.getString("form_no");
            }
            if ( ret[1].equals("0") && formName.equals("AR1") ) { // quick hack for ease of migration from old forms to new
                ret = getShortcutFormValue(demoNo, "AR");
                System.out.println("ret[0] is: " + ret[0]);                
                String[] foo = ret[0].split(".jsp");
                ret[0] = foo[0] + "pg1.jsp" + foo[1];
                System.out.println("getShortcutFormValue forwarding new AR1 to: " + ret[0]);
            }
            if ( ret[1].equals("0") && formName.equals("AR2") ) { // ditto
                ret = getShortcutFormValue(demoNo, "AR");
                String[] foo = ret[0].split(".jsp");
                ret[0] = foo[0] + "pg2.jsp" + foo[1];
                System.out.println("getShortcutFormValue forwarding new AR2 to: " + ret[0]);
            }
        } else {
            sql = "SELECT ID FROM " + table + " WHERE demographic_no=" + demoNo +" order by formEdited desc limit 0,1";
            rs = db.GetSQL(sql);
            while(rs.next()) {
                ret[1] = rs.getString("ID");
            }
        }

        rs.close();
        db.CloseConn();
        return ret;
    }

    public String getResource() throws java.sql.SQLException {
        String ret = "";

        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        String sql = "SELECT value FROM property WHERE name='resource'";
        ResultSet rs = db.GetSQL(sql);
        while(rs.next()) {
            ret = rs.getString("value");
        }

        rs.close();
        db.CloseConn();
        return ret;
    }

    public String getResource(String name) throws java.sql.SQLException {
        String ret = "";

        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        String sql = "SELECT value FROM property WHERE name='" + name + "'";
        ResultSet rs = db.GetSQL(sql);
        while(rs.next()) {
            ret = rs.getString("value");
        }

        rs.close();
        db.CloseConn();
        return ret;
    }

}
