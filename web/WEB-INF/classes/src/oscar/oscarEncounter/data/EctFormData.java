// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class EctFormData {

    public Form[] getForms() throws java.sql.SQLException {
        ArrayList forms = new ArrayList();

        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        // Quick hack to display BC forms before other forms... probably should make this a little less BC-specific at some time in the future...
        String sql = "select * from encounterForm where form_name like 'BC%' order by form_name";
        ResultSet rs = db.GetSQL(sql);
        while(rs.next()) {
            Form frm = new Form(db.getString(rs,"form_name"), db.getString(rs,"form_value"), db.getString(rs,"form_table"), (!db.getString(rs,"hidden").equals("0")?false:true));
            forms.add(frm);
        }
        rs.close();
        sql = "select * from encounterForm where form_name not like 'BC%' order by hidden, form_name";
        rs = db.GetSQL(sql);
        while(rs.next()) {
            Form frm = new Form(db.getString(rs,"form_name"), db.getString(rs,"form_value"), db.getString(rs,"form_table"), (!db.getString(rs,"hidden").equals("0")?false:true));
            forms.add(frm);
        }
        rs.close();
        Form[] ret = {};
        ret = (Form[])forms.toArray(ret);
        return ret;
    }

    public class Form {
        private String formName;
        private String formPage;
        private String formTable;
        private boolean hidden;

        //Constructor
        public Form(String formName, String formPage, String formTable, boolean hidden) {
            this.formName = formName;
            this.formPage = formPage;
            this.formTable = formTable;
            this.hidden = hidden;
        }

        public Form(){}
        
        public String getFormName() {
            return formName;
        }
        public String getFormPage() {
            return formPage;
        }
        public String getFormTable() {
            return formTable;
        }
        
        public boolean isHidden() {
            return hidden;
        }
                
    }

    public PatientForm[] getPatientForms(String demoNo, String table) throws SQLException {
    	
    	if(StringUtils.isEmpty(table)){
    		PatientForm[] ret = {};
            return ret; 
    	}
    		
    	
        ArrayList forms = new ArrayList();

        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        
        try{
        if ( !table.equals("form") ) {
            String sql = "SELECT ID, demographic_no, formCreated, formEdited FROM " + table
                        + " WHERE demographic_no=" + demoNo + " ORDER BY ID DESC";
            
//            System.out.println(sql);

            ResultSet rs = db.GetSQL(sql);

            while(rs.next()) {
                PatientForm frm = new PatientForm(db.getString(rs,"ID"), db.getString(rs,"demographic_no"),
                                    UtilDateUtilities.DateToString(rs.getDate("formCreated"), "yy/MM/dd"), UtilDateUtilities.DateToString(rs.getTimestamp("formEdited"), "yy/MM/dd HH:mm:ss"));
                forms.add(frm);
            }
            rs.close();
        } else {
            String sql = "SELECT form_no, demographic_no, form_date from " + table + " where demographic_no=" + demoNo + " order by form_no desc";
            ResultSet rs = db.GetSQL(sql);

            while(rs.next()) {
                PatientForm frm = new PatientForm(db.getString(rs,"form_no"), db.getString(rs,"demographic_no"),
                                    UtilDateUtilities.DateToString(rs.getDate("form_date"), "yy/MM/dd"), UtilDateUtilities.DateToString(rs.getDate("form_date"), "yy/MM/dd"));
                forms.add(frm);
            }
            rs.close();
        }}catch(SQLException se){
        	PatientForm[] ret = {};
            return ret; 
        }finally{
        }
        

        PatientForm[] ret = {};
        ret = (PatientForm[])forms.toArray(ret);
        return ret;
    }

    public class PatientForm {
        private String formId;
        private String demoNo;
        private String created;
        private String edited;

        //Constructor
        public PatientForm(String formId, String demoNo, String created, String edited) {
            this.formId = formId;
            this.demoNo = demoNo;
            this.created = created;
            this.edited = edited;
        }

        public String getFormId() {
            return formId;
        }
        public String getDemoNo() {
            return demoNo;
        }
        public String getCreated() {
            return created;
        }
        public String getEdited() {
            return edited;
        }
    }

    public String getResource() throws java.sql.SQLException {
        String ret = "";

        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        String sql = "SELECT value FROM property WHERE name='resource'";
        ResultSet rs = db.GetSQL(sql);

        while(rs.next()) {
            ret = db.getString(rs,"value");
        }

        rs.close();
        return ret;
    }
    
    public String getFormNameByFormTable(String value){
        String formName = "";
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "SELECT form_name from encounterForm where form_value='" + value + "'";
            ResultSet rs = db.GetSQL(sql);
            if (rs.next())
                formName = db.getString(rs,"form_name");
            rs.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return formName;
    }

}
