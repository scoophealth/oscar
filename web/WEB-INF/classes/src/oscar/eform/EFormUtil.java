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

package oscar.eform;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.casemgmt.dao.CaseManagementNoteLinkDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.eform.data.EForm;
import oscar.eform.data.EFormBase;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;
import oscar.util.UtilMisc;

public class EFormUtil {
	//for sorting....
	public static final String NAME = "form_name";
	public static final String SUBJECT = "subject";
	public static final String DATE = "form_date DESC";
	public static final String FILE_NAME = "file_name";
	//-----------
	public static final String DELETED = "deleted";
	public static final String CURRENT = "current";
	public static final String ALL = "all";

	private static CaseManagementManager cmm = (CaseManagementManager) SpringUtils.getBean("caseManagementManager");


	private EFormUtil() {}

	public static String saveEForm(EForm eForm) {
		return saveEForm(eForm.getFormName(), eForm.getFormSubject(), eForm.getFormFileName(), eForm.getFormHtml(), eForm.getFormCreator(), eForm.getPatientIndependent());
	}
	public static String saveEForm(String formName, String formSubject, String fileName, String htmlStr) {
		return saveEForm(formName, formSubject, fileName, htmlStr, null, null);
	}
	public static String saveEForm(String formName, String formSubject, String fileName, String htmlStr, Boolean patientIndependent) {
		return saveEForm(formName, formSubject, fileName, htmlStr, null, patientIndependent);
	}
	public static String saveEForm(String formName, String formSubject, String fileName, String htmlStr, String creator, Boolean patientIndependent) {
		//called by the upload action, puts the uploaded form into DB
		String nowDate = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd");
		String nowTime = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "HH:mm:ss");
		htmlStr = "\n" + org.apache.commons.lang.StringEscapeUtils.escapeSql(htmlStr);
		formName = org.apache.commons.lang.StringEscapeUtils.escapeSql(formName);
		formSubject = org.apache.commons.lang.StringEscapeUtils.escapeSql(formSubject);
		fileName = org.apache.commons.lang.StringEscapeUtils.escapeSql(fileName);
		if (creator == null) creator = "NULL";
		else creator = "'" + creator + "'";
		String sql = "INSERT INTO eform (form_name, file_name, subject, form_date, form_time, form_creator, status, form_html, patient_independent) VALUES " +
				"('" + formName + "', '" + fileName + "', '" + formSubject + "', '" + nowDate + "', '" + nowTime + "', " + creator + ", 1, '" + htmlStr + "', " + patientIndependent +")";
		return (runSQLinsert(sql));
	}

	public static ArrayList listEForms(String sortBy, String deleted) {
		//sends back a list of forms that were uploaded (those that can be added to the patient)
		String sql = "";
		if (deleted.equals("deleted")) {
			sql = "SELECT * FROM eform where status=0 ORDER BY " + sortBy;
		} else if (deleted.equals("current")) {
			sql = "SELECT * FROM eform where status=1 ORDER BY " + sortBy;
		} else if (deleted.equals("all")) {
			sql = "SELECT * FROM eform ORDER BY " + sortBy;
		}
		ResultSet rs = getSQL(sql);
		ArrayList results = new ArrayList();
		try {
			while (rs.next()) {
				Hashtable curht = new Hashtable();
				curht.put("fid", rsGetString(rs, "fid"));
				curht.put("formName", rsGetString(rs, "form_name"));
				curht.put("formSubject", rsGetString(rs, "subject"));
				curht.put("formFileName", rsGetString(rs, "file_name"));
				curht.put("formDate", rsGetString(rs, "form_date"));
				curht.put("formDateAsDate", rs.getDate("form_date"));
				curht.put("formTime", rsGetString(rs, "form_time"));
				results.add(curht);
			}
			rs.close();
		} catch (Exception sqe) {
			sqe.printStackTrace();
		}
		return(results);
	}

	public static ArrayList listImages() {
		String imagePath = OscarProperties.getInstance().getProperty("eform_image");
		System.out.println("Img Path: " + imagePath);
		File dir = new File(imagePath);
		String[] files = dir.list();
		ArrayList fileList;
		if( files != null )
		fileList = new ArrayList(Arrays.asList(files));
		else
		fileList = new ArrayList();

		return fileList;
	}

	public static ArrayList listPatientEForms(String sortBy, String deleted, String demographic_no) {
		//sends back a list of forms added to the patient
		String sql = "";
		if (deleted.equals("deleted")) {
			sql = "SELECT * FROM eform_data where status=0 AND (eform_data.patient_independent is null OR eform_data.patient_independent=0) AND demographic_no=" + demographic_no + " ORDER BY " + sortBy;
		} else if (deleted.equals("current")) {
			sql = "SELECT * FROM eform_data where status=1 AND (eform_data.patient_independent is null OR eform_data.patient_independent=0) AND demographic_no=" + demographic_no + " ORDER BY " + sortBy;
		} else if (deleted.equals("all")) {
			sql = "SELECT * FROM eform_data WHERE (eform_data.patient_independent is null OR eform_data.patient_independent=0) AND demographic_no=" + demographic_no + " ORDER BY " + sortBy;
		}
		ResultSet rs = getSQL(sql);
		ArrayList results = new ArrayList();
		try {
			while (rs.next()) {
				Hashtable curht = new Hashtable();
				curht.put("fdid", oscar.Misc.getString(rs,"fdid"));
				curht.put("fid", rsGetString(rs, "fid"));
				curht.put("formName", rsGetString(rs, "form_name"));
				curht.put("formSubject", rsGetString(rs, "subject"));
				curht.put("formDate", rsGetString(rs, "form_date"));
				curht.put("formDateAsDate", rs.getDate("form_date"));
				results.add(curht);
			}
			rs.close();
		} catch (Exception sqe) {
			sqe.printStackTrace();
		}
		return(results);
	}

	public static Hashtable loadEForm(String fid) {
		//opens an eform that was uploaded (used to fill out patient data)
		String sql = "SELECT * FROM eform where fid=" + fid + " LIMIT 1";
		ResultSet rs = getSQL(sql);
		Hashtable curht = new Hashtable();
		try {
			rs.next();
			//must have FID and form_name otherwise throws null pointer on the hashtable
			curht.put("fid", rsGetString(rs, "fid"));
			curht.put("formName", rsGetString(rs, "form_name"));
			curht.put("formSubject", rsGetString(rs, "subject"));
			curht.put("formFileName", rsGetString(rs, "file_name"));
			curht.put("formDate", rsGetString(rs, "form_date"));
			curht.put("formTime", rsGetString(rs, "form_time"));
			curht.put("formCreator", rsGetString(rs, "form_creator"));
			curht.put("formHtml", rsGetString(rs, "form_html"));
			curht.put("patientIndependent", rs.getBoolean("patient_independent"));
			rs.close();
		} catch (SQLException sqe) {
			curht.put("formName", "");
			curht.put("formHtml", "No Such Form in Database");
			sqe.printStackTrace();
		}
		return(curht);
	}

	public static Hashtable loadPatientEForm(String fdid) {
		//shows an eform that was added to the patient previously
		String sql = "SELECT * FROM eform_data where fdid=" + fdid + " LIMIT 1";
		ResultSet rs = getSQL(sql);
		Hashtable curht = new Hashtable();
		try {
			rs.next();
			curht.put("fid", rsGetString(rs, "fid"));
			curht.put("demographic_no", rsGetString(rs, "demographic_no"));
			curht.put("provider_no", rsGetString(rs, "form_provider"));
			curht.put("formName", oscar.Misc.getString(rs,"form_name"));
			curht.put("formSubject", oscar.Misc.getString(rs,"subject"));
			//curht.put("formTime", rsGetString(rs, "form_time"));
			//curht.put("formDate", rsGetString(rs, "form_date"));
			curht.put("formHtml", rsGetString(rs, "form_data"));
			rs.close();
		} catch (SQLException sqe) {
			curht.put("formName", "");
			curht.put("formSubject", "");
			curht.put("formHtml", "No Such Form in Database");
			sqe.printStackTrace();
		}
		return(curht);
	}

	public static void updateEForm(EFormBase updatedForm) {
		//Updates the form - used by editForm
		String formHtml = "\n" + org.apache.commons.lang.StringEscapeUtils.escapeSql(updatedForm.getFormHtml());
		String formName = org.apache.commons.lang.StringEscapeUtils.escapeSql(updatedForm.getFormName());
		String formSubject = org.apache.commons.lang.StringEscapeUtils.escapeSql(updatedForm.getFormSubject());
		String fileName = org.apache.commons.lang.StringEscapeUtils.escapeSql(updatedForm.getFormFileName());
		String sql = "UPDATE eform SET " +
				"form_name='" + formName + "', " +
				"file_name='" + fileName + "', " +
				"subject='" + formSubject + "', " +
				"form_date='" + updatedForm.getFormDate() + "', " +
				"form_time='" + updatedForm.getFormTime() + "', " +
				"form_html='" + formHtml + "', " +
				"patient_independent=" + updatedForm.getPatientIndependent() + " " +
				"WHERE fid=" + updatedForm.getFid() + ";";
		runSQL(sql);
	}

/*
+--------------+--------------+------+-----+---------+----------------+
| Field        | Type         | Null | Key | Default | Extra          |
+--------------+--------------+------+-----+---------+----------------+
| fid          | int(8)       |      | PRI | NULL    | auto_increment |
| form_name    | varchar(255) | YES  |     | NULL    |                |
| file_name    | varchar(255) | YES  |     | NULL    |                |
| subject      | varchar(255) | YES  |     | NULL    |                |
| form_date    | date         | YES  |     | NULL    |                |
| form_time    | time         | YES  |     | NULL    |                |
| form_creator | varchar(255) | YES  |     | NULL    |                |
| status       | tinyint(1)   |      |     | 1       |                |
| form_html    | text         | YES  |     | NULL    |                |
+--------------+--------------+------+-----+---------+----------------+
 */

	public static String getEFormParameter(String fid, String Column) {
		String dbColumn = "";
		if (Column.equalsIgnoreCase("formName")) dbColumn = "form_name";
		else if (Column.equalsIgnoreCase("formSubject")) dbColumn = "subject";
		else if (Column.equalsIgnoreCase("formFileName")) dbColumn = "file_name";
		else if (Column.equalsIgnoreCase("formDate")) dbColumn = "form_date";
		else if (Column.equalsIgnoreCase("formTime")) dbColumn = "form_time";
		else if (Column.equalsIgnoreCase("formStatus")) dbColumn = "status";
		else if (Column.equalsIgnoreCase("formHtml")) dbColumn = "form_html";
		else if (Column.equalsIgnoreCase("patientIndependent")) dbColumn = "patient_independent";
		String sql = "SELECT " + dbColumn + " FROM eform WHERE fid=" + fid;
		ResultSet rs = getSQL(sql);
		try {
			while (rs.next()) {
				return rsGetString(rs, dbColumn);
			}
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}
		return null;
	}

	public static void delEForm(String fid) {
		//deletes the form so no one can add it to the patient (sets status to deleted)
		String sql = "UPDATE eform SET status=0 WHERE fid=" + fid;
		runSQL(sql);
	}

	public static void restoreEForm(String fid) {
		String sql = "UPDATE eform SET status=1 WHERE fid=" + fid;
		runSQL(sql);
	}

	public static void removeEForm(String fdid) {
		//deletes the form from the patient's records (sets status to deleted)
		String sql = "UPDATE eform_data SET status=0 WHERE fdid=" + fdid;
		runSQL(sql);
	}

	public static void unRemoveEForm(String fdid) {
		//undeletes the patient record form
		String sql = "UPDATE eform_data SET status=1 WHERE fdid=" + fdid;
		runSQL(sql);
	}

	public static ArrayList getValues(ArrayList names, String sql) {
		//gets the values for each column name in the sql (used by DatabaseAP)
		ResultSet rs = getSQL(sql);
		ArrayList values = new ArrayList();
		try {
			while (rs.next()) {
				values = new ArrayList();
				for (int i=0; i<names.size(); i++) {
					try {
						values.add(oscar.Misc.getString(rs,(String) names.get(i)));
						System.out.println("VALUE ====" + rs.getObject((String) names.get(i)) + "|");
					} catch (Exception sqe) {
						values.add("<(" + names.get(i) + ")NotFound>");
						sqe.printStackTrace();
					}
				}
			}
			rs.close();
		} catch (SQLException sqe) { sqe.printStackTrace(); }
		return(values);
	}

	public static String addEForm(EForm eForm) {
		//Adds an eform to the patient
		//open own connection - must be same connection for last_insert_id
		String html = charEscape(eForm.getFormHtml(), '\'');
		String sql = "INSERT INTO eform_data (fid, form_name, subject, demographic_no, status, form_date, form_time, form_provider, form_data, patient_independent)" +
				"VALUES ('" + eForm.getFid() + "', '" + eForm.getFormName() + "', '" + StringEscapeUtils.escapeSql(eForm.getFormSubject()) +
				"', '" + eForm.getDemographicNo() + "', 1, '" + eForm.getFormDate() + "', '" + eForm.getFormTime() + "', '" + eForm.getProviderNo() +
				"', '" + html + "', " + eForm.getPatientIndependent() + ")";
		try {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			db.RunSQL(sql);
			sql = "SELECT LAST_INSERT_ID()";
			ResultSet rs = db.GetSQL(sql);
			rs.next();
			String lastID = oscar.Misc.getString(rs,"LAST_INSERT_ID()");
			rs.close();
			return(lastID);
		} catch (SQLException sqe) { sqe.printStackTrace(); }
		return "";
	}

	//used by addEForm for escaping characters
	public static String charEscape(String S, char a) {
		if (null == S) {
			return S;
		}
		int N = S.length();
		StringBuffer sb = new StringBuffer(N);
		for (int i = 0; i < N; i++) {
			char c = S.charAt(i);
			//escape the escape characters
			if (c == '\\') {
				sb.append("\\\\");
			}
			else if (c == a) {
				sb.append("\\" + a);
			}
			else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static void addEFormValues(ArrayList names, ArrayList values, String fdid, String fid, String demographic_no) {
		//adds parsed values and names to DB
		//names.size and values.size must equal!
		String sql = "";
		try {  //opens it's own connection - prevents pulling many connections from the pool at once
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			for (int i=0; i<names.size(); i++) {
				sql = "INSERT INTO eform_values(fdid, fid, demographic_no, var_name, var_value) VALUES " +
						"(" + fdid + ", " + fid + ", " + demographic_no + ", '" + names.get(i) + "', '" + UtilMisc.charEscape((String) values.get(i), '\'') + "')";
				System.out.println("SQL ====" + sql);
				db.RunSQL(sql);
			}
		} catch (SQLException sqe) { sqe.printStackTrace(); }
	}

	public static boolean formExistsInDB(String eFormName) {
		String sql = "SELECT * FROM eform WHERE status=1 AND form_name='" + eFormName + "'";
		try {
			ResultSet rs = getSQL(sql);
			if (rs.next()) {
				rs.close();
				return true;
			} else {
				rs.close();
				return false;
			}
		} catch (SQLException sqe) { sqe.printStackTrace(); }
		return false;
	}

	public static int formExistsInDBn(String formName, String fid) {
		//returns # of forms in the DB with that name other than itself
		String sql = "SELECT count(*) AS count FROM eform WHERE status=1 AND form_name='" + formName + "' AND fid!=" + fid;
		try {
			ResultSet rs = getSQL(sql);
			while (rs.next()) {
				int numberRecords = rs.getInt("count");
				rs.close();
				return numberRecords;
			}
		} catch (SQLException sqe) { sqe.printStackTrace(); }
		return 0;
	}

	//--------------eform groups---------
	public static ArrayList getEFormGroups() {
		String sql;
		sql = "SELECT DISTINCT eform_groups.group_name, count(*)-1 AS 'count' FROM eform_groups " +
				"LEFT JOIN eform ON eform.fid=eform_groups.fid WHERE eform.status=1 OR eform_groups.fid=0 " +
				"GROUP BY eform_groups.group_name;";
		ArrayList al = new ArrayList();
		try {
			ResultSet rs = getSQL(sql);
			while (rs.next()) {
				Hashtable curhash = new Hashtable();
				curhash.put("groupName", oscar.Misc.getString(rs,"group_name"));
				curhash.put("count", oscar.Misc.getString(rs,"count"));
				al.add(curhash);
			}
		} catch (SQLException sqe) { sqe.printStackTrace(); }
		return al;
	}

	public static ArrayList getEFormGroups(String demographic_no) {
		String sql;
		sql = "SELECT eform_groups.group_name, count(*)-1 AS 'count' FROM eform_groups " +
				"LEFT JOIN eform_data ON eform_data.fid=eform_groups.fid " +
				"WHERE (eform_data.status=1 AND eform_data.demographic_no=" + demographic_no + ") OR eform_groups.fid=0 " +
				"GROUP BY eform_groups.group_name";
		ArrayList al = new ArrayList();
		try {
			ResultSet rs = getSQL(sql);
			while (rs.next()) {
				Hashtable curhash = new Hashtable();
				curhash.put("groupName", oscar.Misc.getString(rs,"group_name"));
				curhash.put("count", oscar.Misc.getString(rs,"count"));
				al.add(curhash);
			}
		} catch (SQLException sqe) { sqe.printStackTrace(); }
		return al;
	}

	public static void delEFormGroup(String name) {
		String sql = "DELETE FROM eform_groups WHERE group_name='" + name + "'";
		runSQL(sql);
	}

	public static void addEFormToGroup(String groupName, String fid) {
		try {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			String sql1 = "SELECT eform_groups.fid FROM eform_groups, eform WHERE eform_groups.fid=" + fid +
					" AND eform_groups.fid=eform.fid AND eform.status=1 AND eform_groups.group_name='" + groupName +"'";
			ResultSet rs = db.GetSQL(sql1);
			if (!rs.next()) {
				String sql = "INSERT INTO eform_groups (fid, group_name) " +
				"VALUES (" + fid + ", '" + groupName + "')";
				db.RunSQL(sql);
			}
		} catch (SQLException sqe) { sqe.printStackTrace(); }
	}

	public static void remEFormFromGroup(String groupName, String fid) {
		String sql = "DELETE FROM eform_groups WHERE group_name='" + groupName + "' and fid=" + fid + ";";
		runSQL(sql);
	}

	public static ArrayList listEForms(String sortBy, String deleted, String group) {
		//sends back a list of forms that were uploaded (those that can be added to the patient)
		String sql = "";
		if (deleted.equals("deleted")) {
			sql = "SELECT * FROM eform, eform_groups where eform.status=0 AND eform.fid=eform_groups.fid AND eform_groups.group_name='" + group + "' ORDER BY " + sortBy;
		} else if (deleted.equals("current")) {
			sql = "SELECT * FROM eform, eform_groups where eform.status=1 AND eform.fid=eform_groups.fid AND eform_groups.group_name='" + group + "' ORDER BY " + sortBy;
		} else if (deleted.equals("all")) {
			sql = "SELECT * FROM eform AND eform.fid=eform_groups.fid AND eform_groups.group_name='" + group + "' ORDER BY " + sortBy;
		}
		ResultSet rs = getSQL(sql);
		ArrayList results = new ArrayList();
		try {
			while (rs.next()) {
				Hashtable curht = new Hashtable();
				curht.put("fid", rsGetString(rs, "fid"));
				curht.put("formName", rsGetString(rs, "form_name"));
				curht.put("formSubject", rsGetString(rs, "subject"));
				curht.put("formFileName", rsGetString(rs, "file_name"));
				curht.put("formDate", rsGetString(rs, "form_date"));
				curht.put("formTime", rsGetString(rs, "form_time"));
				results.add(curht);
			}
			rs.close();
		} catch (Exception sqe) {
			sqe.printStackTrace();
		}
		return(results);
	}

	public static ArrayList listPatientEForms(String sortBy, String deleted, String demographic_no, String groupName) {
		//sends back a list of forms added to the patient
		String sql = "";
		if (deleted.equals("deleted")) {
			sql = "SELECT * FROM eform_data, eform_groups WHERE eform_data.status=0 AND (eform_data.patient_independent is null OR eform_data.patient_independent=0) AND eform_data.demographic_no=" + demographic_no +
					" AND eform_data.fid=eform_groups.fid AND eform_groups.group_name='" + groupName + "' ORDER BY " + sortBy;
		} else if (deleted.equals("current")) {
			sql = "SELECT * FROM eform_data, eform_groups WHERE eform_data.status=1 AND (eform_data.patient_independent is null OR eform_data.patient_independent=0) AND eform_data.demographic_no=" + demographic_no +
					" AND eform_data.fid=eform_groups.fid AND eform_groups.group_name='" + groupName + "' ORDER BY " + sortBy;
		} else if (deleted.equals("all")) {
			sql = "SELECT * FROM eform_data, eform_groups WHERE (eform_data.patient_independent is null OR eform_data.patient_independent=0) AND eform_data.demographic_no=" + demographic_no +
					" AND eform_data.fid=eform_groups.fid AND eform_groups.group_name='" + groupName + "' ORDER BY " + sortBy;
		}
		ResultSet rs = getSQL(sql);
		ArrayList results = new ArrayList();
		try {
			while (rs.next()) {
				Hashtable curht = new Hashtable();
				curht.put("fdid", oscar.Misc.getString(rs,"fdid"));
				curht.put("fid", rsGetString(rs, "fid"));
				curht.put("formName", rsGetString(rs, "form_name"));
				curht.put("formSubject", rsGetString(rs, "subject"));
				curht.put("formDate", rsGetString(rs, "form_date"));
				results.add(curht);
			}
			rs.close();
		} catch (Exception sqe) {
			sqe.printStackTrace();
		}
		return(results);
	}

	public static void writeEformTemplate(ArrayList paramNames, ArrayList paramValues, EForm eForm, String fdid, String programNo) {
		if (eForm==null) return;

		String[] template = {"EncounterNote","SocialHistory","FamilyHistory","MedicalHistory","OngoingConcerns","RiskFactors","Reminders","OtherMeds"};
		String[] code = {"","SocHistory","FamHistory","MedHistory","Concerns","RiskFactors","Reminders","OMeds"};
		for (int i=0; i<code.length; i++) {
			String text = eForm.getTemplate(template[i]);
			if (empty(text)) continue;

			text = putTemplateValues(paramNames, paramValues, new StringBuffer(text));
			Long noteId = saveCMNote(eForm, programNo, code[i], text);

			if (noteId!=null) {
				CaseManagementNoteLink cmLink = new CaseManagementNoteLink(CaseManagementNoteLink.EFORMDATA, Long.valueOf(fdid), noteId);
				CaseManagementNoteLinkDAO cmDao = (CaseManagementNoteLinkDAO) SpringUtils.getBean("CaseManagementNoteLinkDAO");
				cmDao.save(cmLink);
			}
		}
	}


	//------------------private
	private static void runSQL(String sql) {
		try {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			db.RunSQL(sql);
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}
	}

	private static String runSQLinsert(String sql) {
		try {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			db.RunSQL(sql);
			sql = "SELECT LAST_INSERT_ID()";
			ResultSet rs = db.GetSQL(sql);
			rs.next();
			String lastID = oscar.Misc.getString(rs,"LAST_INSERT_ID()");
			rs.close();
			return(lastID);
		} catch (SQLException sqe) { sqe.printStackTrace(); }
		return "";
	}

	private static ResultSet getSQL(String sql) {
		ResultSet rs = null;
		try {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
			rs = db.GetSQL(sql);
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}
		return(rs);
	}

	private static String rsGetString(ResultSet rs, String column) throws SQLException {
		//protects agianst null values;
		String thisStr = oscar.Misc.getString(rs,column);
		if (thisStr == null) return "";
		return thisStr;
	}

	private static String putTemplateValues(ArrayList paramNames, ArrayList paramValues, StringBuffer sb) {
		if (sb.length()==0) return "";

		//Replace "$T{" with "$t{", making tags work regardless of case
		while (sb.indexOf("$T{")>=0) {
			int tagAt = sb.indexOf("$T{");
			sb = sb.replace(tagAt, tagAt+2, "$t");
		}

		//Replace field markers with values
		while (sb.indexOf("$t{")>=0) {
			int fieldBegin = sb.indexOf("$t{");
			int fieldEnd = sb.indexOf("}", fieldBegin);
			String field = sb.substring(fieldBegin+3, fieldEnd);
			if (paramNames.contains(field)) {
				sb = sb.replace(fieldBegin, fieldEnd+1, (String)paramValues.get(paramNames.indexOf(field)));
			} else {
				System.out.println("EForm Template Error! Cannot find template field $t{"+field+"} in eform");
				sb = sb.delete(fieldBegin, fieldBegin+2);
			}
		}
		return sb.toString();
	}

	private static Long saveCMNote(EForm eForm, String programNo, String code, String note) {
		if (empty(note)) return null;

		CaseManagementNote cNote = createCMNote(eForm.getDemographicNo(), eForm.getProviderNo(), programNo, note);
		if (!empty(code)) {
			Set<CaseManagementIssue> scmi = createCMIssue(eForm.getDemographicNo(), code);
			cNote.setIssues(scmi);
		}
		cmm.saveNoteSimple(cNote);
		return cNote.getId();
	}

	private static CaseManagementNote createCMNote(String demographicNo, String providerNo, String programNo, String note) {
		CaseManagementNote cmNote = new CaseManagementNote();
		cmNote.setUpdate_date(new Date());
		cmNote.setObservation_date(new Date());
		cmNote.setDemographic_no(demographicNo);
		cmNote.setProviderNo(providerNo);
		cmNote.setSigning_provider_no(providerNo);
		cmNote.setSigned(true);
		cmNote.setHistory("");
		cmNote.setReporter_caisi_role("1");  //caisi_role for "doctor"
		cmNote.setReporter_program_team("0");
		cmNote.setProgram_no(programNo);
		cmNote.setUuid(UUID.randomUUID().toString());
		cmNote.setNote(note);

		return cmNote;
	}

	private static Set<CaseManagementIssue> createCMIssue(String demographicNo, String code) {
		Issue isu = cmm.getIssueInfoByCode(code);
		CaseManagementIssue cmIssu = cmm.getIssueById(demographicNo, isu.getId().toString());
		if (cmIssu==null) {
			cmIssu = new CaseManagementIssue();
			cmIssu.setDemographic_no(demographicNo);
			cmIssu.setIssue_id(isu.getId());
			cmIssu.setType(isu.getType());
			cmm.saveCaseIssue(cmIssu);
		}

		Set<CaseManagementIssue> sCmIssu = new HashSet<CaseManagementIssue>();
		sCmIssu.add(cmIssu);
		return sCmIssu;
	}

	private static boolean empty(String s) {
		return (s==null || s.trim().equals(""));
	}
}
