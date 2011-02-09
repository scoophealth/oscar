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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of EfmData
 *
 *
 * EfmData.java
 *
 * Created on July 28, 2005, 1:54 PM
 */
package oscar.eform.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.common.OtherIdManager;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.eform.EFormLoader;
import oscar.eform.EFormUtil;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler;
import oscar.oscarEncounter.oscarMeasurements.util.WriteNewMeasurements;
import oscar.util.StringBuilderUtils;
import oscar.util.UtilDateUtilities;

public class EForm extends EFormBase {
	private static EFormDataDao eFormDataDao = (EFormDataDao) SpringUtils.getBean("EFormDataDao");

	private static String appointment_no = "-1";
	private static String appt_provider = "";
	private static HashMap sql_params = new HashMap();
	private static Logger log = MiscUtils.getLogger();
	private String parentAjaxId = null;
	private HashMap<String, String> fieldValues = new HashMap<String, String>();
	private int needValueInForm = 0;
	private boolean setAP2nd = false;

	private static final String VAR_NAME = "var_name";
	private static final String VAR_VALUE = "var_value";
	private static final String REF_FID = "fid";
	private static final String REF_VAR_NAME = "ref_var_name";
	private static final String REF_VAR_VALUE = "ref_var_value";
	private static final String TABLE_NAME = "table_name";
	private static final String TABLE_ID = "table_id";
	private static final String OTHER_KEY = "other_key";

	public EForm() {
	}

	public EForm(String fid, String demographicNo) {
		loadEForm(fid, demographicNo);
	}

	public EForm(String fid, String demographicNo, String providerNo) {
		// used to load an uploaded eform
		loadEForm(fid, demographicNo);
		this.providerNo = providerNo;
	}

	public EForm(String fdid) {
		EFormData eFormData = eFormDataDao.find(new Integer(fdid));
		if (eFormData != null) {
			this.fdid = fdid;
			this.fid = eFormData.getFormId().toString();
			this.providerNo = eFormData.getProviderNo();
			this.demographicNo = eFormData.getDemographicId().toString();
			this.formName = eFormData.getFormName();
			this.formSubject = eFormData.getSubject();
			this.formDate = eFormData.getFormDate().toString();
			this.formHtml = eFormData.getFormData();
			this.patientIndependent = eFormData.getPatientIndependent();
			this.roleType = eFormData.getRoleType();
		} else {
			this.formName = "";
			this.formSubject = "";
			this.formHtml = "No Such Form in Database";
		}
	}

	public void loadEForm(String fid, String demographicNo) {
		Hashtable loaded = (Hashtable) EFormUtil.loadEForm(fid);
		this.fid = fid;
		this.formName = (String) loaded.get("formName");
		this.formHtml = (String) loaded.get("formHtml");
		this.formSubject = (String) loaded.get("formSubject");
		this.formDate = (String) loaded.get("formDate");
		this.formFileName = (String) loaded.get("formFileName");
		this.formCreator = (String) loaded.get("formCreator");
		this.demographicNo = demographicNo;
		this.patientIndependent = (Boolean) loaded.get("patientIndependent");
		this.roleType = (String) loaded.get("roleType");
	}

	public void setAppointmentNo(String appt_no) {
        this.appointment_no = blank(appt_no) ? "-1" : appt_no;
	}

	public void setApptProvider(String appt_prvd) {
        this.appt_provider = blank(appt_prvd) ? "" : appt_prvd;
	}

	public void setAction(String pAjaxId) {
		parentAjaxId = pAjaxId;
		setAction();
	}

	public void setAction() {
		setAction(false);
	}

	public void setAction(boolean unset) {
		// sets action= in the form
		StringBuilder html = new StringBuilder(this.formHtml);
		int index = StringBuilderUtils.indexOfIgnoreCase(html, "<form", 0);
		int endtag = html.indexOf(">", index + 1);
		// --remove all previous actions, methods and names from the form tag
		if (index < 0) return;

		int pointer, pointer2;
		while (((pointer = StringBuilderUtils.indexOfIgnoreCase(html, " action=", index)) >= 0) && (pointer < endtag)) {
			pointer2 = nextSpot(html, pointer + 1);
			html = html.delete(pointer, pointer2);
			endtag = html.indexOf(">", index + 1);
		}
		while (((pointer = StringBuilderUtils.indexOfIgnoreCase(html, " method=", index)) >= 0) && (pointer < endtag)) {
			pointer2 = nextSpot(html, pointer + 1);
			html = html.delete(pointer, pointer2);
			endtag = html.indexOf(">", index + 1);
		}
		pointer = StringBuilderUtils.indexOfIgnoreCase(html, " name=", index);
		String name = "name=\"saveEForm\" ";
		if ((pointer >= 0) && (pointer < endtag)) {
			pointer2 = nextSpot(html, pointer + 1);
			endtag = html.indexOf(">", index + 1);
			name = "";
		}
		if (index < 0) return;
		if (unset) {
			this.formHtml = html.toString();
			return;
		}
		index += 5;
		StringBuilder action = new StringBuilder("action=\"../eform/addEForm.do?efmfid=" + this.fid + "&efmdemographic_no=" + this.demographicNo + "&efmprovider_no=" + this.providerNo);
		if (this.parentAjaxId != null) action.append("&parentAjaxId=" + this.parentAjaxId);

		action.append("\"");

		String method = "method=\"POST\"";
		html.insert(index, " " + action.toString() + " " + name + method);
		this.formHtml = html.toString();
	}

	// ------------------Saving the Form (inserting value= statements)---------------------
	public void setValues(ArrayList names, ArrayList values) {
		if (names.size() != values.size()) return;
		StringBuilder html = new StringBuilder(this.formHtml);
		int pointer = 0;
		int nameEnd;
		while ((pointer = StringBuilderUtils.indexOfIgnoreCase(html, "name=", pointer)) >= 0) {
			nameEnd = nextSpot(html, pointer);
			if (nameEnd < 0) continue;
			pointer += 5;
			if (html.charAt(pointer) == '"') {
				pointer++;
				nameEnd--; // if name="..." (quotes)
			}
			String nameFound = html.substring(pointer, nameEnd);
			int i;
			if ((i = names.indexOf(nameFound)) < 0) continue;
			html = putValue((String) values.get(i), pointer, html);

		}
		this.formHtml = html.toString();
	}

	// --------------------------Setting APs utilities----------------------------------------
	public void setDatabaseAPs() {
		StringBuilder html = new StringBuilder(this.formHtml);
		String marker = EFormLoader.getInstance().getMarker(); // default: marker: "oscarDB="
		if (demographicNo == null) demographicNo = "";
		if (providerNo == null) providerNo = "";
		for (int i = 0; i < 2; i++) { // run the following twice if "count"-type field is found
			int markerLoc = -1;
			while ((markerLoc = getFieldIndex(html, markerLoc + 1)) >= 0) {
				log.debug("===============START CYCLE===========");
				String fieldHeader = getFieldHeader(html, markerLoc);
				String apName = EFormUtil.getAttribute(marker, fieldHeader); // gets varname from oscarDB=varname
				if (blank(apName)) {
					if (!setAP2nd) saveFieldValue(html, markerLoc);
					continue;
				}

				log.debug("AP ====" + apName);
				if (setAP2nd && !apName.startsWith("e$")) continue; // ignore non-e$ oscarDB on 2nd run

				int needing = needValueInForm;
				DatabaseAP curAP = EFormLoader.getInstance().getAP(EFormUtil.removeQuotes(apName));
				if (curAP == null) curAP = getExtra(EFormUtil.removeQuotes(apName), fieldHeader);
				if (curAP == null) continue;

				String fieldType = getFieldType(html, markerLoc + 1); // textarea, text, hidden etc..
				if ((fieldType.equals("")) || (EFormUtil.removeQuotes(apName).equals(""))) continue;
				// sets up the pointer where to write the value
				int pointer = markerLoc + EFormUtil.getAttributePos(marker, fieldHeader) + marker.length() + 1;
				if (!fieldType.equals("textarea")) {
					pointer += apName.length();
				}
				if (!setAP2nd) { // 1st run
					html = putValue(curAP, fieldType, pointer, html);
					saveFieldValue(html, markerLoc);
				} else { // 2nd run
					if (needing > needValueInForm) html = putValue(curAP, fieldType, pointer, html);
				}

				log.debug("Marker ==== " + markerLoc);
				log.debug("FIELD TYPE ====" + fieldType);
				log.debug("=================End Cycle==============");
			}
			formHtml = html.toString();
			if (needValueInForm > 0) setAP2nd = true;
			else i = 2;
		}
	}

	public void setNowDateTime() {
		this.formTime = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "HH:mm:ss");
		this.formDate = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd");
	}

	// ------------------------------------------------------------------------------------

	public DatabaseAP getExtra(String apName, String fieldHeader) {
		Pattern p = Pattern.compile("\\b[a-z]\\$[^ \\$#]+#[^\n]+");
		Matcher m = p.matcher(apName);
		if (!m.matches()) return null;

		String module = apName.substring(0, apName.indexOf("$"));
		String type = apName.substring(apName.indexOf("$") + 1, apName.indexOf("#"));
		String field = apName.substring(apName.indexOf("#") + 1, apName.length());
		DatabaseAP curAP = null;
		if (module.equals("m")) {
			log.debug("SWITCHING TO MEASUREMENTS");
			Hashtable data = EctMeasurementsDataBeanHandler.getLast(this.demographicNo, type);
			if (data != null) {
				curAP = new DatabaseAP();
				curAP.setApName(apName);
				curAP.setApOutput((String) data.get(field));
			}
		} else if (module.equals("e")) {
			log.debug("SWITCHING TO EFORM_VALUES");
			String eform_name = EFormUtil.getAttribute("eform$name", fieldHeader);
			String var_value = EFormUtil.getAttribute("var$value", fieldHeader);
			String ref = EFormUtil.getAttribute("ref$", fieldHeader, true);
			String ref_name = null, ref_value = null, ref_fid = fid;
			if (!blank(ref) && ref.contains("=")) {
				ref_name = ref.substring(4, ref.indexOf("="));
				ref_value = EFormUtil.removeQuotes(ref.substring(ref.indexOf("=") + 1));
			} else {
				ref_name = blank(ref) ? "" : ref.substring(4);
			}
			if (!blank(eform_name)) ref_fid = getRefFid(eform_name);
			if ((!blank(var_value) && var_value.trim().startsWith("{")) || (!blank(ref_value) && ref_value.trim().startsWith("{"))) {
				if (setAP2nd) { // 2nd run, put value in required field
					var_value = findValueInForm(var_value);
					ref_value = findValueInForm(ref_value);
					needValueInForm--;
				} else { // 1st run, note the need to reference other value in form
					needValueInForm++;
					return null;
				}
			}
			if (type.equalsIgnoreCase("count") && var_value == null) type = "countname";
			if (!ref_name.equals("")) {
				type += "_ref";
				if (ref_value == null) type += "name";
			}
			curAP = EFormLoader.getInstance().getAP("_eform_values_" + type);
			if (curAP != null) {
				setSqlParams(VAR_NAME, field);
				setSqlParams(REF_VAR_NAME, ref_name);
				setSqlParams(VAR_VALUE, var_value);
				setSqlParams(REF_VAR_VALUE, ref_value);
				setSqlParams(REF_FID, ref_fid);
			}
		} else if (module.equals("o")) {
			log.debug("SWITCHING TO OTHER_ID");
			String table_name = "", table_id = "";
			curAP = EFormLoader.getInstance().getAP("_other_id");
			if (type.equalsIgnoreCase("patient")) {
				table_name = OtherIdManager.DEMOGRAPHIC.toString();
				table_id = this.demographicNo;
			} else if (type.equalsIgnoreCase("appointment")) {
				table_name = OtherIdManager.APPOINTMENT.toString();
				table_id = appointment_no;
				if (blank(table_id)) table_id = "-1";
			}
			setSqlParams(OTHER_KEY, field);
			setSqlParams(TABLE_NAME, table_name);
			setSqlParams(TABLE_ID, table_id);
		}
		return curAP;
	}

	public ActionMessages setMeasurements(ArrayList names, ArrayList values) {
		return (WriteNewMeasurements.addMeasurements(names, values, this.demographicNo, this.providerNo));
	}

	public void setContextPath(String contextPath) {
		if (blank(contextPath)) return;

		String oscarJS = contextPath + "/share/javascript/";
		this.formHtml = this.formHtml.replace(jsMarker, oscarJS);
	}

	public String getTemplate() {
		// Get content between "<!-- <template>" & "</template> -->"
		if (blank(formHtml)) return "";

		String sTemplateBegin = "<template>";
		String sTemplateEnd = "</template>";
		String sCommentBegin = "<!--", sCommentEnd = "-->";
		String text = "";

		int templateBegin = -1, templateEnd = -1;
		boolean searching = true;
		while (searching) {
			templateBegin = EFormUtil.findIgnoreCase(sTemplateBegin, formHtml, templateBegin + 1);
			templateEnd = EFormUtil.findIgnoreCase(sTemplateEnd, formHtml, templateBegin);
			int commentBegin = formHtml.lastIndexOf(sCommentBegin, templateBegin);
			int commentEnd = formHtml.indexOf(sCommentEnd, commentBegin);
			if (templateBegin == -1 || templateEnd == -1 || commentBegin == -1 || commentEnd == -1) {
				searching = false;
			} else if (commentEnd > templateEnd) {
				text += formHtml.substring(templateBegin + sTemplateBegin.length(), templateEnd);
			}
		}
		return text;
	}

	// ----------------------------------private
	private StringBuilder putValue(String value, int pointer, StringBuilder html) {
		// inserts value= into tag or textarea
		pointer -= 2; // take it back to name^="
		String tagType = getFieldType(html, pointer);
		pointer = nextSpot(html, pointer);

		if (tagType.equals("textarea")) {
			pointer = html.indexOf(">", pointer) + 1;
			int endPointer = html.indexOf("<", pointer);
			html.delete(pointer, endPointer);
			html.insert(pointer, value);
		} else if (tagType.equals("text") || tagType.equals("hidden")) {
			String insertStr = "value=\"" + value + "\"";
			html.insert(pointer, " " + insertStr);
		} else if (tagType.equals("checkbox")) {
			html.insert(pointer, " checked");
		} else if (tagType.equals("select")) {
			int endindex = StringBuilderUtils.indexOfIgnoreCase(html, "</select>", pointer);
			if (endindex < 0) return html; // if closing tag not found
			int valueLoc = nextIndex(html, " value=" + value, " value=\"" + value, pointer);
			if (valueLoc < 0 || valueLoc > endindex) {
				valueLoc = nextIndex(html, " VALUE=" + value, " VALUE=\"" + value, pointer);
			}
			if (valueLoc < 0 || valueLoc > endindex) return html;
			pointer = nextSpot(html, valueLoc + 1);
			html.insert(pointer, " selected");
		} else if (tagType.equals("radio")) {
			int endindex = html.indexOf(">", pointer);
			int valindexS = nextIndex(html, " value=", " VALUE=", pointer);
			if (valindexS < 0 || valindexS > endindex) return html; // if value= not found in tag
			valindexS += 7;
			if (html.charAt(valindexS) == '"') valindexS++;
			int valindexE = valindexS + value.length();
			if (html.substring(valindexS, valindexE).equals(value)) {
				pointer = nextSpot(html, valindexE);
				html.insert(pointer, " checked");
			}
		}
		return html;
	}

	private int nextIndex(StringBuilder text, String option1, String option2, int pointer) {
		// returns the index of option1 or option2 whichever one is closer and exists
		int index;
		int option1i = text.indexOf(option1, pointer);
		int option2i = text.indexOf(option2, pointer);
		if (option1i < 0) index = option2i;
		else if (option2i < 0 || option1i < option2i) index = option1i;
		else index = option2i;
		return index;
	}

	private int nextSpot(StringBuilder text, int pointer) {
		int end = nextIndex(text, "\n", "\r", pointer);
		if (end < 0) end = text.length();
		int index = text.substring(pointer, end).indexOf('=') + pointer;
		if (index >= 0) {
			if (text.charAt(index + 1) == '"') {
				int close = text.substring(index + 2, end).indexOf("\"") + (index + 2);
				if (close > 0) return close + 1;
			}
			if (text.charAt(index + 1) == '\'') {
				int close = text.substring(index + 2, end).indexOf("'") + (index + 2);
				if (close > 0) return close + 1;
			}
			pointer = index;
		}
		return nextIndex(text, " ", ">", pointer);
	}

	private String getFieldType(StringBuilder html, int pointer) {
		// pointer can be any place in the tag - isolates tag and sends back field type
		int open = html.substring(0, pointer).lastIndexOf("<");
		int close = html.substring(pointer).indexOf(">") + pointer + 1;
		String tag = html.substring(open, close);
		log.debug("TAG ====" + tag);
		int start; // <input type="^text".....
		int end; // <input type="text^"....
		String typeStr = null;
		if (tag.substring(1, 9).equalsIgnoreCase("textarea")) return "textarea";
		if (tag.substring(1, 7).equalsIgnoreCase("select")) return "select";
		log.debug("TAG PROCESS ====" + tag.substring(1, 9));
		if ((start = tag.toLowerCase().indexOf(" type=")) >= 0) {
			start += 6; // account for type=...
			if (tag.charAt(start) == '\"') { // account for type="..."
				start += 1;
				end = tag.indexOf("\"", start);
			} else {
				int nextSpace = tag.indexOf(" ", start);
				int nextBracket = tag.indexOf(">", start);
				if (nextSpace < 0) end = nextBracket;
				else if ((nextBracket < 0) || (nextSpace < nextBracket)) end = nextSpace;
				else end = nextBracket;

			}
			return tag.substring(start, end).toLowerCase();
		}
		return "";
	}

	private StringBuilder putValue(DatabaseAP ap, String type, int pointer, StringBuilder html) {
		// pointer set up to where to write the value
		String sql = ap.getApSQL();
		String output = ap.getApOutput();
		// replace ${demographic} with demogrpahicNo
		if (!blank(sql)) {
			sql = replaceAllFields(sql);
			log.debug("SQL----" + sql);
			ArrayList names = DatabaseAP.parserGetNames(output); // a list of ${apName} --> apName
			sql = DatabaseAP.parserClean(sql); // replaces all other ${apName} expressions with 'apName'
			ArrayList values = EFormUtil.getValues(names, sql);
			if (values.size() != names.size()) {
				output = "";
			} else {
				for (int i = 0; i < names.size(); i++) {
					output = DatabaseAP.parserReplace((String) names.get(i), (String) values.get(i), output);
				}
			}
		}
		if (type.equals("textarea")) {
			pointer = html.indexOf(">", pointer) + 1;
			html.insert(pointer, output);
		} else if (type.equals("select")) {
			int selectEnd = StringBuilderUtils.indexOfIgnoreCase(html, "</select>", pointer);
			if (selectEnd >= 0) {
				int valueLoc = nextIndex(html, " value=" + output, " value=\"" + output, pointer);
				if (valueLoc < 0 || valueLoc > selectEnd) {
					valueLoc = nextIndex(html, " VALUE=" + output, " VALUE=\"" + output, pointer);
				}
				log.debug("VALUELOC====" + output);
				if (valueLoc < 0 || valueLoc > selectEnd) return html;
				pointer = nextSpot(html, valueLoc);
				html = html.insert(pointer, " selected");
			}
			pointer++;
		} else {
			String quote = output.contains("\"") ? "'" : "\"";
			html.insert(pointer, " value="+quote+output+quote);
		}
		return (html);
	}

	private String replaceAllFields(String sql) {
		sql = DatabaseAP.parserReplace("demographic", demographicNo, sql);
		sql = DatabaseAP.parserReplace("provider", providerNo, sql);

		sql = DatabaseAP.parserReplace("appt_no", appointment_no, sql);
		sql = DatabaseAP.parserReplace("appt_provider", appt_provider, sql);

		sql = DatabaseAP.parserReplace(REF_FID, getSqlParams(REF_FID), sql);
		sql = DatabaseAP.parserReplace(VAR_NAME, getSqlParams(VAR_NAME), sql);
		sql = DatabaseAP.parserReplace(VAR_VALUE, getSqlParams(VAR_VALUE), sql);
		sql = DatabaseAP.parserReplace(REF_VAR_NAME, getSqlParams(REF_VAR_NAME), sql);
		sql = DatabaseAP.parserReplace(REF_VAR_VALUE, getSqlParams(REF_VAR_VALUE), sql);
		sql = DatabaseAP.parserReplace(TABLE_NAME, getSqlParams(TABLE_NAME), sql);
		sql = DatabaseAP.parserReplace(TABLE_ID, getSqlParams(TABLE_ID), sql);
		sql = DatabaseAP.parserReplace(OTHER_KEY, getSqlParams(OTHER_KEY), sql);
		return sql;
	}

	private String getSqlParams(String key) {
		if (sql_params.containsKey(key)) {
		    String val = (String) sql_params.get(key);
		    return val==null ? "" : val.replace("\"", "\\\"");
		}
		return "";
	}

	private void setSqlParams(String key, String value) {
		if (sql_params.containsKey(key)) sql_params.remove(key);
		sql_params.put(key, value);
	}

	private String findValueInForm(String name) {
		// name format = {xxx}
		if (blank(name) || !name.trim().startsWith("{") || !name.trim().endsWith("}")) return name;

		// extract content from brackets {}
		name = name.trim().substring(1, name.trim().length() - 1).toLowerCase();
		if (blank(name)) return "";

		String value = fieldValues.get(name);
		return value == null ? "" : value;
	}

	private int getFieldIndex(StringBuilder html, int from) {
		if (html == null) return -1;

		Integer[] index = new Integer[3];
		index[0] = StringBuilderUtils.indexOfIgnoreCase(html, "<input", from);
		index[1] = StringBuilderUtils.indexOfIgnoreCase(html, "<select", from);
		index[2] = StringBuilderUtils.indexOfIgnoreCase(html, "<textarea", from);

		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < index.length; i++)
			if (index[i] >= 0) list.add(index[i]);

		int min = list.isEmpty() ? -1 : list.get(0);
		for (int i = 1; i < list.size(); i++)
			min = min > list.get(i) ? list.get(i) : min;

		return min;
	}

	private String getFieldHeader(String html, int fieldIndex) {
		StringBuilder sb_html = new StringBuilder(html);
		return getFieldHeader(sb_html, fieldIndex);
	}

	private String getFieldHeader(StringBuilder html, int fieldIndex) {
		if (html == null || fieldIndex < 0) return "";
		if (html.charAt(fieldIndex) != '<') return ""; // field header must be "<...>"

		// look for char '>' which is NOT inside quotes ("..." or '...')
		int end = fieldIndex;
		boolean quoteOpen = false, quote2Open = false;
		for (int i = fieldIndex + 1; i < html.length(); i++) {
			char c = html.charAt(i);
			if (c == '"' && !quoteOpen) quote2Open = !quote2Open;
			if (c == '\'' && !quote2Open) quoteOpen = !quoteOpen;
			if (!quoteOpen && !quote2Open && c == '>') {
				end = i + 1;
				break;
			}
		}
		return html.substring(fieldIndex, end);
	}

	private void saveFieldValue(StringBuilder html, int fieldIndex) {
		String header = getFieldHeader(html, fieldIndex);
		if (blank(header)) return;

		String name = EFormUtil.getAttribute("name", header);
		String value = EFormUtil.getAttribute("value", header);
		if (blank(name)) return;

		if (header.toLowerCase().startsWith("<input ")) {
			String type = EFormUtil.getAttribute("type", header);
			if (blank(type)) return;

			if (type.equalsIgnoreCase("radio")) {
				String checked = EFormUtil.getAttribute("checked", header);
				if (blank(checked) || !checked.equalsIgnoreCase("checked")) return;
			}
		} else if (header.toLowerCase().startsWith("<select ")) {
			String selects = html.substring(fieldIndex, html.indexOf("</select>", fieldIndex));
			int pos = selects.indexOf("<option ", 0);
			while (pos >= 0) {
				String option = getFieldHeader(selects, pos);
				String selected = EFormUtil.getAttribute("selected", option);
				if (!blank(selected) && selected.equalsIgnoreCase("selected")) {
					value = EFormUtil.getAttribute("value", option);
					break;
				}
				pos = selects.indexOf("<option ", pos + 1);
			}
		} else if (header.toLowerCase().startsWith("<textarea ")) {
			int fieldEnd = html.indexOf("</textarea>", fieldIndex);
			value = html.substring(fieldIndex + header.length(), fieldEnd).trim();
			if (value.startsWith("\n")) value = value.substring(1); // remove 1st line break, UNIX style
			else if (value.startsWith("\r\n")) value = value.substring(2); // remove 1st line break, WINDOWS style
		}
		name = name.toLowerCase();
		if (!blank(value)) fieldValues.put(name, value);
	}

	private String getRefFid(String eform_name) {
		if (blank(eform_name)) return fid;

		String refFid = EFormUtil.getEFormIdByName(eform_name);
		if (blank(refFid)) refFid = fid;
		return refFid;
	}

	private boolean blank(String s) {
		return (s == null || s.trim().equals(""));
	}
}
