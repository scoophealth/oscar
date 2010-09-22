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
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.dao.EncounterFormDao;
import org.oscarehr.common.model.EncounterForm;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class EctFormData {

    private static EncounterFormDao encounterFormDao=(EncounterFormDao)SpringUtils.getBean("encounterFormDao");

    public static Form[] getForms() {
		List<EncounterForm> results = encounterFormDao.findAll();
		Collections.sort(results, EncounterForm.BC_FIRST_COMPARATOR);
		
		ArrayList<Form> forms = new ArrayList<Form>();
		for (EncounterForm encounterForm : results) {
			Form frm = new Form(encounterForm.getFormName(), encounterForm.getFormValue(), encounterForm.getFormTable(), encounterForm.isHidden());
            forms.add(frm);
		}

		return (forms.toArray(new Form[0]));

	}

	public static class Form {
		private String formName;
		private String formPage;
		private String formTable;
		private boolean hidden;

		// Constructor
		public Form(String formName, String formPage, String formTable, boolean hidden) {
			this.formName = formName;
			this.formPage = formPage;
			this.formTable = formTable;
			this.hidden = hidden;
		}

		public Form() {
		}

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

		if (StringUtils.isEmpty(table)) {
			PatientForm[] ret = {};
			return ret;
		}

		ArrayList forms = new ArrayList();

		DBHandler db = new DBHandler();

		try {
			if (!table.equals("form")) {
				String sql = "SELECT ID, demographic_no, formCreated, formEdited FROM " + table + " WHERE demographic_no=" + demoNo + " ORDER BY ID DESC";

				ResultSet rs = db.GetSQL(sql);

				while (rs.next()) {
					PatientForm frm = new PatientForm(db.getString(rs, "ID"), db.getString(rs, "demographic_no"), UtilDateUtilities.DateToString(rs.getDate("formCreated"), "yy/MM/dd"), UtilDateUtilities.DateToString(rs.getTimestamp("formEdited"), "yy/MM/dd HH:mm:ss"));
					forms.add(frm);
				}
				rs.close();
			} else {
				String sql = "SELECT form_no, demographic_no, form_date from " + table + " where demographic_no=" + demoNo + " order by form_no desc";
				ResultSet rs = db.GetSQL(sql);

				while (rs.next()) {
					PatientForm frm = new PatientForm(db.getString(rs, "form_no"), db.getString(rs, "demographic_no"), UtilDateUtilities.DateToString(rs.getDate("form_date"), "yy/MM/dd"), UtilDateUtilities.DateToString(rs.getDate("form_date"), "yy/MM/dd"));
					forms.add(frm);
				}
				rs.close();
			}
		} catch (SQLException se) {
			PatientForm[] ret = {};
			return ret;
		} finally {
		}

		PatientForm[] ret = {};
		ret = (PatientForm[]) forms.toArray(ret);
		return ret;
	}

	public class PatientForm {
		private String formId;
		private String demoNo;
		private String created;
		private String edited;

		// Constructor
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

}
