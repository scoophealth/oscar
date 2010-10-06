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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.EncounterFormDao;
import org.oscarehr.common.model.EncounterForm;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.SqlUtils;
import oscar.util.UtilDateUtilities;

public class EctFormData {

	private static Logger logger=MiscUtils.getLogger();
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

	public static ArrayList<PatientForm> getAllPatientFormsFromAllTables(Integer demographicId)
	{
		// grab all of the forms
		EncounterFormDao encounterFormDao=(EncounterFormDao)SpringUtils.getBean("encounterFormDao");
		List<EncounterForm> encounterForms = encounterFormDao.findAll();
		Collections.sort(encounterForms, EncounterForm.BC_FIRST_COMPARATOR);
		
		// grab all patient forms for all the above form types
		ArrayList<PatientForm> allResults=new ArrayList<PatientForm>();
		for (EncounterForm encounterForm : encounterForms) {
			String table = StringUtils.trimToNull(encounterForm.getFormTable());
			if (table!=null) {
				allResults.addAll(getPatientFormsAsArrayList(demographicId.toString(), encounterForm.getFormName(), table));
			}
		}

		return(allResults);
	}
	
	public static ArrayList<PatientForm> getPatientFormsAsArrayList(String demoNo, String formName, String table) {
		table=StringUtils.trimToNull(table);
		if (table==null) return(new ArrayList<PatientForm>());
		
		ArrayList<PatientForm> forms = new ArrayList<PatientForm>();

		Connection c=null;
		try {
			c=DbConnectionFilter.getThreadLocalDbConnection();
			
			if (!table.equals("form")) {
				String sql = "SELECT ID, demographic_no, formCreated, formEdited FROM " + table + " WHERE demographic_no=" + demoNo + " ORDER BY ID DESC";

				Statement s=c.createStatement();
				ResultSet rs = s.executeQuery(sql);

				while (rs.next()) {
					PatientForm frm = new PatientForm(formName, rs.getInt("ID"), rs.getInt("demographic_no"), rs.getDate("formCreated"), rs.getTimestamp("formEdited"));
					forms.add(frm);
				}
			} else {
				String sql = "SELECT form_no, demographic_no, form_date from " + table + " where demographic_no=" + demoNo + " order by form_no desc";

				Statement s=c.createStatement();
				ResultSet rs = s.executeQuery(sql);

				while (rs.next()) {
					PatientForm frm = new PatientForm(formName, rs.getInt("form_no"), rs.getInt("demographic_no"), rs.getDate("form_date"), rs.getDate("form_date"));
					forms.add(frm);
				}
			}
		} catch (SQLException e) {
			logger.error("Unexpected error.", e);
			throw(new PersistenceException(e));
		} finally {
			SqlUtils.closeResources(c, null, null);
		}
		
		return(forms);
	}
	
	public static PatientForm[] getPatientForms(String demoNo, String table) {
		return(getPatientFormsAsArrayList(demoNo, null, table).toArray(new PatientForm[0]));
	}

	/**
	 * Due to backwards compatability hack, leave all the getter methods as returning String,
	 * direct field access can be used to get native types. 
	 */
	public static class PatientForm {
		
		/**
		 * This comparator sorts PatientForm descending based on the created date
		 */
		public static final Comparator<PatientForm> CREATED_DATE_COMPARATOR=new Comparator<PatientForm>()
		{
			public int compare(PatientForm o1, PatientForm o2) {
				return(o2.created.compareTo(o1.created));
			}	
		};

		public Integer formId;
		public Integer demographicId;
		public Date created;
		public Date edited;
		public String formName;
		
		// Constructor
		public PatientForm(String formName, Integer formId, Integer demographicId, Date created, Date edited) {
			this.formName = formName;
			this.formId = formId;
			this.demographicId = demographicId;
			this.created = created;
			this.edited = edited;
		}

		public String getFormId() {
			return formId.toString();
		}

		public String getDemoNo() {
			return demographicId.toString();
		}

		public String getCreated() {
			return(UtilDateUtilities.DateToString(created, "yy/MM/dd"));
		}

		public String getEdited() {
			return(UtilDateUtilities.DateToString(edited, "yy/MM/dd HH:mm:ss"));
		}

		public String getFormName() {
        	return formName;
        }
				
	}
}
