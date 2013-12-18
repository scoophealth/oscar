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

package oscar.form;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.Document;

import oscar.OscarProperties;
import oscar.form.dao.EAVAttributeValueDao;
import oscar.form.dao.EAVEntityDao;
import oscar.form.dao.EAVFieldDao;
import oscar.form.dao.EAVFormDao;
import oscar.form.data.EAVFormAttributeValue;
import oscar.form.data.EAVFormEntity;
import oscar.form.data.EAVFormField;
import oscar.form.data.EAVFormName;
import oscar.util.JDBCUtil;
import oscar.util.UtilDateUtilities;

public class FrmRecordHelpEAV {

	public static String db_url = "localhost";
	public static String db_user = "root";
	public static String db_password = "root";
	public static String db_schema = "oscar_12_1";

	private List<String> entityField = Arrays.asList(new String[] { "demographic_no", "formCreated", "ID", "ref_form" });

	private String _dateFormat = "yyyy/MM/dd";
	private String _newDateFormat = "yyyy-MM-dd"; //handles both date formats, but yyyy/MM/dd is displayed to avoid deprecation

	public void setDateFormat(String s) {// "dd/MM/yyyy"
		_dateFormat = s;
	}

	public Properties getFormRecord(String formName, int demographic_no, int existingID) throws SQLException {
		return getFormRecord(formName, demographic_no, existingID, false);
	}

	/**
	 * retrieve the informations of the selected row that fit the query (AND ONLY ONE ROW!!!)</br>
	 * the query is a select query, ex : "select * from xxx where id = n" </br>
	 * the result is returned as a list of Properties (attribute,value)</br>
	 * note : the boolean value are returned with the value "on" and "off"
	 * @param sql : the sql query
	 * @return : a Properties that contains the attributes and value
	 * @throws SQLException : if something wrong appends 
	 */
	public Properties getFormRecord(String formName, int demographic_no, int existingID, boolean print) //int demographicNo, int existingID)
	        throws SQLException {
		Properties props = new Properties();

		Connection con = getConnectionLocalEAV();

		EAVFormName form = new EAVFormDao().selectForm(formName, con);
		List<EAVFormField> fields = new EAVFieldDao().selectFields(form.getForm_id_eav(), con);
		EAVFormEntity entity = new EAVEntityDao().selectEntity(existingID, demographic_no, con);
		List<EAVFormAttributeValue> attrValues = new EAVAttributeValueDao().selectAttributeValue(entity.getEntity_id_eav(), con);

		for (int i = 0; i < fields.size(); i++) {
			String fieldName = fields.get(i).getField_name();
			String fieldType = fields.get(i).getField_type();

			// Search property in the entity
			Object value;
			value = getProperty(entity, fieldName);

			// Search the value in the AttrValues
			if (value == null) {
				for (EAVFormAttributeValue av : attrValues) {
					if (((String) getProperty(av, "attribute_name")) != null && ((String) getProperty(av, "attribute_name")).equalsIgnoreCase(fieldName)) {
						if (av.getAttribute_value_bigint() != null) {
							value = av.getAttribute_value_bigint();
						} else if (av.getAttribute_value_bit() != null) {
							value = av.getAttribute_value_bit();
						} else if (av.getAttribute_value_char() != null) {
							value = av.getAttribute_value_char();
						} else if (av.getAttribute_value_date() != null) {
							value = av.getAttribute_value_date();
						} else if (av.getAttribute_value_double() != null) {
							value = av.getAttribute_value_double();
						} else if (av.getAttribute_value_enum() != null) {
							value = av.getAttribute_value_enum();
						} else if (av.getAttribute_value_int() != null) {
							value = av.getAttribute_value_int();
						} else if (av.getAttribute_value_int_unsigned() != null) {
							value = av.getAttribute_value_int_unsigned();
						} else if (av.getAttribute_value_text() != null) {
							value = av.getAttribute_value_text();
						} else if (av.getAttribute_value_time() != null) {
							value = av.getAttribute_value_time();
						} else if (av.getAttribute_value_varchar() != null) {
							value = av.getAttribute_value_varchar();
						}
					}

				}
			}
			if (!print) {
				if (fieldType.startsWith("TINY")) {
					if ((Integer) value == 1) value = "checked='checked'";
					else value = "";
				} else if (fieldType.equalsIgnoreCase("date")) value = UtilDateUtilities.DateToString(new java.util.Date(((java.sql.Date) value).getTime()), _dateFormat);
				else if (fieldType.equalsIgnoreCase("timestamp")) value = 
						UtilDateUtilities.DateToString(new java.sql.Timestamp(((Timestamp) value).getTime()), "yyyy/MM/dd HH:mm:ss");
			} else {
				// <10 to check it's a value 0 or 1.
				if (fieldType.startsWith("TINY") && (Integer) value < 10) {
					if ((Integer) value == 1) value = "on";
					else value = "off";
				} else if (fieldType.equalsIgnoreCase("date")) value = UtilDateUtilities.DateToString(new java.util.Date(((java.sql.Date) value).getTime()), _dateFormat);
			}

			if (value != null) props.setProperty(fieldName, value.toString());

		}
		return props;
	}

	/**
	 * Find a property in a bean. Return the corresponding object if the property exists, null in the other case
	 * @param obj : the bean
	 * @param propName : the name of the property
	 * @return the corresponding object if the property exists, null in the other case
	 */
	public Object getProperty(Object obj, String propName) {
		Object val = null;
		try {
			val = PropertyUtils.getSimpleProperty(obj, propName);
		} catch (IllegalAccessException e) {
			//
		} catch (InvocationTargetException e) {
			//
		} catch (NoSuchMethodException e) {
			//
		}

		return val;
	}

	/**
	 * Save a form to the database.</br>
	 * note : of Properties content save_as_xml to true, the form is also saved in a XML File 
	 * @param props : Properties containing the fields of the form
	 * @param sql : a SQL select query from the table
	 * @return int id; the id of the inserted row.
	 * @throws SQLException : if something wrong appends
	 */
	public synchronized int saveFormRecord(Properties props, String formName) throws SQLException {

		Connection connection = getConnectionLocalEAV();

		PreparedStatement stt = connection.prepareStatement("select ifnull(MAX(e.ID),0), (Select form_id_eav from eav_form_name where form_name "
				+ "= ?) from eav_form_entity e, eav_form_name f WHERE e.ref_form = f.form_name AND f.form_name = ?;");
		stt.setString(1, formName);
		stt.setString(2, formName);
		ResultSet rs = stt.executeQuery();
		int id = 0, refForm = -1;
		if (rs.next()) {
			id = rs.getInt(1);
			refForm = rs.getInt(2);

			EAVFormEntity ent = new EAVFormEntity();
			ent.setDemographic_no(Integer.parseInt(props.getProperty("demographic_no")));

			String fromCreatedValue = props.getProperty("formCreated");
			java.util.Date d = null;
			if ((fromCreatedValue == null) || (fromCreatedValue.indexOf('/') != -1)) d = UtilDateUtilities.StringToDate(fromCreatedValue, _dateFormat);
			else d = UtilDateUtilities.StringToDate(fromCreatedValue, _newDateFormat);

			ent.setFormCreated(new java.sql.Date(d.getTime()));
			ent.setFormEdited(new java.sql.Timestamp( UtilDateUtilities.Today().getTime()));
			ent.setId(id+1);
			ent.setRef_form(refForm);

			int refFormEntity = new EAVEntityDao().insertEntity(ent, connection);

			PreparedStatement stt2 = connection.prepareStatement("select * FROM eav_form_field f, eav_form_name n " + 
					" WHERE f.ref_form = n.form_id_eav AND n.form_name = ?;");
			stt2.setString(1, formName);
			ResultSet rsfield = stt2.executeQuery();
			List<EAVFormAttributeValue> list = new ArrayList<EAVFormAttributeValue>();
			while (rsfield.next()) {
				String collName = rsfield.getString("field_name");
				if (!entityField.contains(collName)) {
					
					list.add(prepareAttributeValue(refFormEntity, rsfield, props));
					
				}
			}
			
			new EAVAttributeValueDao().insertAttributeValues(list, connection);

		}
		rs.close();

		String saveAsXml = OscarProperties.getInstance().getProperty("save_as_xml", "false");

		if (saveAsXml.equalsIgnoreCase("true")) {

			String demographicNo = props.getProperty("demographic_no");
			java.util.Date d = UtilDateUtilities.now();
			String now = UtilDateUtilities.DateToString(d, "yyyyMMddHHmmss");
			String place = OscarProperties.getInstance().getProperty("form_record_path", "/root");

			if (!place.endsWith(System.getProperty("file.separator"))) place = place + System.getProperty("file.separator");
			String fileName = place + formName + "_" + demographicNo + "_" + now + ".xml";

			try {
				Document doc = JDBCUtil.toDocument(rs);
				JDBCUtil.saveAsXML(doc, fileName);
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			}
		}

		return id;
	}

	private EAVFormAttributeValue prepareAttributeValue(int ref_form_entity, ResultSet rs, Properties props) throws SQLException {

		EAVFormAttributeValue bean = new EAVFormAttributeValue();
		bean.setAttribute_name(rs.getString("field_name"));
		bean.setRef_form_entity(ref_form_entity);
		bean.setRef_field(Integer.parseInt(rs.getString("field_id_eav")));
		bean.setRef_form(Integer.parseInt(rs.getString("ref_form")));

		String fieldType = rs.getString("field_type");
		String dataType = fieldType.toLowerCase().split(" |\\(")[0];
		String stringValue = props.getProperty(rs.getString("field_name"));

		if(stringValue != null && !stringValue.isEmpty())
		{
		if (dataType.equalsIgnoreCase("int")) {
			if (fieldType.contains("unsigned")) bean.setAttribute_value_int_unsigned(Integer.parseInt(stringValue));
			else bean.setAttribute_value_int(Integer.parseInt(stringValue));
	
		}
		else if (dataType.equalsIgnoreCase("date")) {

			java.util.Date d = null;
			if ((stringValue == null) || (stringValue.indexOf('/') != -1)) d = UtilDateUtilities.StringToDate(stringValue, _dateFormat);
			else d = UtilDateUtilities.StringToDate(stringValue, _newDateFormat);
			MiscUtils.getLogger().info("XXXXXXXXXXX " + stringValue.toString() + " XXXXXXXXXXXXXXX");
			bean.setAttribute_value_date(new java.sql.Date(d.getTime()));

		}
		else if (dataType.equalsIgnoreCase("timestamp") || dataType.equalsIgnoreCase("time") || dataType.equalsIgnoreCase("datetime")) {
			bean.setAttribute_value_time((Time) UtilDateUtilities.StringToDate(stringValue, "yyyyMMddHHmmss"));
		
		}
		else if (dataType.equalsIgnoreCase("varchar")) {
			bean.setAttribute_value_varchar(stringValue);
			
		}
		else if (dataType.equalsIgnoreCase("tinyint")) {
			if (stringValue.equalsIgnoreCase("on") || stringValue.equalsIgnoreCase("checked='checked'")) bean.setAttribute_value_bit(1);
			else bean.setAttribute_value_bit(0);
			
		}
		else if (dataType.equalsIgnoreCase("enum")) {
			bean.setAttribute_value_enum(stringValue);
			
		}
		else if (dataType.equalsIgnoreCase("double")) {
			bean.setAttribute_value_double(Double.valueOf(stringValue));
			
		}
		else if (dataType.equalsIgnoreCase("text")) {
			bean.setAttribute_value_text(stringValue);
			
		}
		else if (dataType.equalsIgnoreCase("char")) {
			bean.setAttribute_value_char(stringValue.charAt(0));
			
		}
		else if (dataType.equalsIgnoreCase("bigInt")) {
			bean.setAttribute_value_bigint(Integer.parseInt(stringValue));
			
		}
		
		
		}
		
		return bean;
		

	}

	/**
	* get attributes of a form into a Properties, according to the select query
	* @param sql : the select query
	* @return Properties : containing the attributes and values
	* @throws SQLException
	*/
	public Properties getPrintRecord(int demographic_no, int existingID, String formName) throws SQLException {
		return getFormRecord(formName, demographic_no, existingID, true);
	}

	/**
	 * get attributes of a form into a Properties, according to the select query
	 * @param sql : the select query
	 * @return List<Properties> : a list of properties containing the attributes and values
	 * @throws SQLException
	 */
	public List<Properties> getPrintRecords(String formName, int demographic_no) throws SQLException {

		String sql = "SELECT ID FROM eav_form_entity ent, eav_form_name form " + " WHERE demographic_no = " + demographic_no + "AND form.form_name = \"" + formName + "\" and ent.ref_form = form.form_id_eav;";

		ArrayList<Properties> results = new ArrayList<Properties>();
		Connection connection = getConnectionLocalEAV();

		PreparedStatement st = connection.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			results.add(getPrintRecord(demographic_no, rs.getInt("ID"), formName));
		}

		return results;
	}

	/**
	 * get attributes of a form into a Properties, according to the select query
	 * @param sql : the select query
	 * @return List<Properties> : a list of properties containing the attributes and values
	 * @throws SQLException
	 */
	public List<Properties> getPrintRecords(String formName, int demographic_no, int existingID) throws SQLException {

		String sql = "SELECT ID FROM eav_form_entity ent, eav_form_name form " + " WHERE demographic_no = " + demographic_no + "AND form.form_name = \"" + formName + "\" and ent.ref_form = form.form_id_eav" + "AND ent.ID = " + existingID + ";";

		ArrayList<Properties> results = new ArrayList<Properties>();

		Connection connection = getConnectionLocalEAV();

		PreparedStatement st = connection.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			results.add(getPrintRecord(demographic_no, rs.getInt("ID"), formName));
		}

		return results;
	}

	/**
	 * get attributes of a form into a Properties, according to the select query
	 * @param sql : the select query
	 * @return List<Properties> : a list of properties containing the attributes and values
	 * @throws SQLException
	 */
	public List<Properties> getPrintRecords(String formName, int demographic_no, String date) throws SQLException {

		String sql = "SELECT ID FROM eav_form_entity ent, eav_form_name form " + " WHERE demographic_no = " + demographic_no + "AND form.form_name = \"" + formName + "\" and ent.ref_form = form.form_id_eav" + "AND formEdited > '" + date + "';";

		ArrayList<Properties> results = new ArrayList<Properties>();

		Connection connection = getConnectionLocalEAV();

		PreparedStatement st = connection.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			results.add(getPrintRecord(demographic_no, rs.getInt("ID"), formName));
		}

		return results;
	}

	/**
	 * Get a list of demographic id that responds to the querry
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public List<Integer> getDemographicIds(String form, String date) throws SQLException {

		String sql = "SELECT demographic_no FROM eav_form_entity ent, eav_form_name form " + "WHERE ent.ref_form = form.form_id_eav and form.form_name = \"" + form + "\" " + "AND formEdited > '" + date + "';";

		List<Integer> results = new ArrayList<Integer>();

		Connection connection = getConnectionLocalEAV();

		PreparedStatement st = connection.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			results.add(Integer.parseInt(rs.getString("demographic_no")));
		}

		return results;
	}

	/**
	 * retun a properties containig the pair Attribute/value contained in the resultset</br>
	 * note : the boolean value are returned with the value "on" and "off"
	 * @param rs : the resultSet
	 * @return : a Properties containing all the pair Attribute/value
	 * @throws SQLException : if something goes wrong
	 */
	private Properties getResultsAsProperties(ResultSet rs) throws SQLException {
		Properties p = new Properties();
		ResultSetMetaData md = rs.getMetaData();
		for (int i = 1; i <= md.getColumnCount(); i++) {
			String name = md.getColumnName(i);
			String value;

			if (md.getColumnTypeName(i).startsWith("TINY") && md.getScale(i) == 1) {
				if (rs.getInt(i) == 1) value = "on";
				else value = "off";
			} else if (md.getColumnTypeName(i).equalsIgnoreCase("date")) value = UtilDateUtilities.DateToString(new java.util.Date(rs.getDate(i).getTime()), _dateFormat);
			else value = oscar.Misc.getString(rs, i);

			if (value != null) p.setProperty(name, value);
		}

		return (p);
	}

	/**
	 * 
	 * @param submit 
	 * @return
	 */
	public String findActionValue(String submit) {
		if (submit != null && submit.equalsIgnoreCase("print")) {
			return "print";
		} else if (submit != null && submit.equalsIgnoreCase("save")) {
			return "save";
		} else if (submit != null && submit.equalsIgnoreCase("exit")) {
			return "exit";
		} else if (submit != null && submit.equalsIgnoreCase("graph")) {
			return "graph";
		} else if (submit != null && submit.equalsIgnoreCase("printall")) {
			return "printAll";
		} else if (submit != null && submit.equalsIgnoreCase("printLabReq")) {
			return "printLabReq";
		} else {
			return "failure";
		}
	}

	/**
	 * return an action URL
	 * @param where ?
	 * @param action ?
	 * @param demoId ?
	 * @param formId the id of the form( I guess...)
	 * @return
	 */
	public String createActionURL(String where, String action, String demoId, String formId) {
		String temp = null;

		if (action.equalsIgnoreCase("print")) {
			temp = where + "?demoNo=" + demoId + "&formId=" + formId; // + "&study_no=" + studyId +
			// "&study_link" + studyLink;
		} else if (action.equalsIgnoreCase("save")) {
			temp = where + "?demographic_no=" + demoId + "&formId=" + formId; // "&study_no=" +
			// studyId +
			// "&study_link" +
			// studyLink; //+
		} else if (action.equalsIgnoreCase("exit")) {
			temp = where;
		} else {
			temp = where;
		}

		return temp;
	}

	public Connection getConnectionLocalEAV() throws SQLException {
		
		for(int i = 0 ; i < 10 ; i ++)
		MiscUtils.getLogger().info(" -------------------------------------- ");
		
		Connection con = DbConnectionFilter.getThreadLocalDbConnection();
		MiscUtils.getLogger().info("Driver = " + con.getMetaData().getDriverName());
		MiscUtils.getLogger().info("URL = " + con.getMetaData().getURL());
		MiscUtils.getLogger().info("User = " + con.getMetaData().getUserName());
		
		return con;
	}

}
