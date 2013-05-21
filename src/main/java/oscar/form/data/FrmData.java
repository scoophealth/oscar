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


package oscar.form.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.EncounterFormDao;
import org.oscarehr.common.model.EncounterForm;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmData {
    private static final Logger _log = MiscUtils.getLogger();
    private static EncounterFormDao encounterFormDao=(EncounterFormDao)SpringUtils.getBean("encounterFormDao");

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

    public Form[] getForms() {

    	List<EncounterForm> results=encounterFormDao.findAll();

    	ArrayList<Form> forms = new ArrayList<Form>();
    	for (EncounterForm encounterForm : results)
    	{
            Form frm = new Form(encounterForm.getFormName(), encounterForm.getFormValue(), encounterForm.getFormTable());
            forms.add(frm);
    	}

    	return(forms.toArray(new Form[0]));
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
        ArrayList<PatientForm> forms = new ArrayList<PatientForm>();


        String sql = "SELECT ID, demographic_no, formCreated, formEdited FROM " + table
                    + " WHERE demographic_no=" + demoNo + " ORDER BY ID DESC";
        ResultSet rs = DBHandler.GetSQL(sql);
        while(rs.next()) {
            PatientForm frm = new PatientForm(oscar.Misc.getString(rs, "ID"), oscar.Misc.getString(rs, "demographic_no"),
                                UtilDateUtilities.DateToString(rs.getDate("formCreated"), "yy/MM/dd"), UtilDateUtilities.DateToString(rs.getDate("formEdited"), "yy/MM/dd"));
            forms.add(frm);
        }

        rs.close();
        PatientForm[] ret = {};
        ret = forms.toArray(ret);
        return ret;
    }

    public PatientForm getCurrentPatientForm(String demoNo, String studyNo) throws SQLException {
        PatientForm frm = null;


        String sql = "SELECT e.form_table from encounterForm e, study s where e.form_name = s.form_name and s.study_no = " + studyNo;
        String table = "";
        ResultSet rs = DBHandler.GetSQL(sql);
        while(rs.next()) {
            table = oscar.Misc.getString(rs, "form_table");
        }
        rs = null;

        sql = "SELECT ID, demographic_no, formCreated, formEdited FROM " + table + " WHERE demographic_no=" + demoNo + " ORDER BY ID DESC limit 0,1";
        rs = DBHandler.GetSQL(sql);
        while(rs.next()) {
            frm = new PatientForm(oscar.Misc.getString(rs, "ID"), oscar.Misc.getString(rs, "demographic_no"),
                                UtilDateUtilities.DateToString(rs.getDate("formCreated"), "yy/MM/dd"), UtilDateUtilities.DateToString(rs.getDate("formEdited"), "yy/MM/dd"));
        }

        rs.close();
        return frm;
    }

    public String[] getStudyNameLink(String studyNo) throws java.sql.SQLException {
        String[] ret = new String[2];


        String sql = "SELECT study_name, study_link FROM study WHERE study_no=" + studyNo;
        ResultSet rs = DBHandler.GetSQL(sql);
        while(rs.next()) {
            ret[0] = oscar.Misc.getString(rs, "study_name");
            ret[1] = oscar.Misc.getString(rs, "study_link");
        }

        rs.close();
        return ret;
    }

	//ret[0] = form path;   ret[1] = formId
    public String[] getShortcutFormValue(String demoNo, String formName) throws java.sql.SQLException {
        String[] ret = new String[2];
		String table = null;

		EncounterFormDao encounterFormDao=(EncounterFormDao) SpringUtils.getBean("encounterFormDao");
		List<EncounterForm> forms=encounterFormDao.findByFormName(formName);

		for (EncounterForm encounterForm : forms)
		{
            ret[0] = encounterForm.getFormValue();
            table = encounterForm.getFormTable();
		}


        String sql;
        ResultSet rs;

		ret[1] = "0";
        if (table.equals("form")) {
            String searchFormName = formName;
            if (searchFormName.equals("AR1")) searchFormName = "ar1_99_12"; // quick hack for ease of migration from old forms to new
            if (searchFormName.equals("AR2")) searchFormName = "ar2_99_08"; // ditto
            sql = "SELECT form_no FROM " + table + " WHERE demographic_no=" + demoNo +" AND form_name='" + searchFormName + "' order by form_no desc limit 0,1";
            rs = DBHandler.GetSQL(sql);
            while(rs.next()) {
                ret[1] = oscar.Misc.getString(rs, "form_no");
            }
            String[] xmlForm = ret.clone();

            if ( formName.equals("AR1")){
                //First check to see if there are records for 2005

                ret = getShortcutFormValue(demoNo, "AR2005");
                MiscUtils.getLogger().debug("ret[0] is: " + ret[0]);
                String[] foo = ret[0].split(".jsp");
                ret[0] = foo[0] + "pg1.jsp" + foo[1];
                MiscUtils.getLogger().debug("getShortcutFormValue forwarding new AR1 to: " + ret[0]);
                String[] first_pick =  ret.clone();

                //Check if AR has any forms
                if (ret[1].equals("0")){
                    ret = getShortcutFormValue(demoNo, "AR");
                    MiscUtils.getLogger().debug("ret[0] is: " + ret[0]);
                    foo = ret[0].split(".jsp");
                    ret[0] = foo[0] + "pg1.jsp" + foo[1];
                    MiscUtils.getLogger().debug("getShortcutFormValue forwarding new AR1 to: " + ret[0]);

                    if(ret[1].equals("0") && !xmlForm[1].equals("0")) { // Nothing found in AR but there are records in the old xml form
                        ret = xmlForm;
                    }else if (ret[1].equals("0")) {  // Nothing in either old form Go with new one
                        ret = first_pick;
                    }
                }

            }else if ( formName.equals("AR2")){
                ret = getShortcutFormValue(demoNo, "AR");
                String[] foo = ret[0].split(".jsp");
                ret[0] = foo[0] + "pg2.jsp" + foo[1];
                MiscUtils.getLogger().debug("getShortcutFormValue forwarding new AR2 to: " + ret[0]);
                String[] first_pick =  ret.clone();
                if (ret[1].equals("0")){
                   ret = getShortcutFormValue(demoNo, "AR");
                   foo = ret[0].split(".jsp");
                   ret[0] = foo[0] + "pg2.jsp" + foo[1];
                   MiscUtils.getLogger().debug("getShortcutFormValue forwarding new AR2 to: " + ret[0]);
                   if(ret[1].equals("0") && !xmlForm[1].equals("0")) { // Nothing found in AR but there are records in the old xml form
                        ret = xmlForm;
                   }else if (ret[1].equals("0")){  // Nothing in either old form Go with new one
                        ret = first_pick;
                   }
                }
            }

   
        } else if("".equals(table)){
        	rs = null;
            ret[1] = "0";
        } 
        
        else {
            sql = "SELECT ID FROM " + table + " WHERE demographic_no=" + demoNo +" order by formEdited desc limit 0,1";
            rs = DBHandler.GetSQL(sql);
            while(rs.next()) {
                ret[1] = oscar.Misc.getString(rs, "ID");
            }
        }

        if(rs != null)
        	rs.close();
        _log.debug("RETURNING "+ret[0]+" = "+ret[1]);
        return ret;
    }

    public String getResource() throws java.sql.SQLException {
        String ret = "";


        String sql = "SELECT value FROM property WHERE name='resource'";
        ResultSet rs = DBHandler.GetSQL(sql);
        while(rs.next()) {
            ret = oscar.Misc.getString(rs, "value");
        }

        rs.close();
        if(ret.compareTo("")==0)
            ret = "http://resource.oscarmcmaster.org/oscarResource/";
        return ret;

    }

    public String getResource(String name) throws java.sql.SQLException {
        String ret = "";


        String sql = "SELECT value FROM property WHERE name='" + name + "'";
        ResultSet rs = DBHandler.GetSQL(sql);
        while(rs.next()) {
            ret = oscar.Misc.getString(rs, "value");
        }

        rs.close();
        return ret;
    }

}
