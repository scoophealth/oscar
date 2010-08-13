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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.common.OtherIdManager;

import oscar.eform.EFormLoader;
import oscar.eform.EFormUtil;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler;
import oscar.oscarEncounter.oscarMeasurements.util.WriteNewMeasurements;
import oscar.util.StringBufferUtils;
import oscar.util.UtilDateUtilities;

public class EForm extends EFormBase {
    private static String appointment_no = "";
    private static String appt_provider = "";
    private static HashMap sql_params = new HashMap();
    private static Log log = LogFactory.getLog(EForm.class);
    private String parentAjaxId = null;
    private boolean setAP2nd=false, hasCountType=false;

    private static final String VAR_NAME = "var_name";
    private static final String VAR_VALUE = "var_value";
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
        //used to load an uploaded eform
        loadEForm(fid, demographicNo);
        this.providerNo = providerNo;
    }
    
    public EForm(String fdid) {
        //used to load form_data that was added to the patient
        Hashtable ht = EFormUtil.loadPatientEForm(fdid);
        this.fdid = fdid;
        this.fid = (String) ht.get("fid");
        this.providerNo = (String) ht.get("provider_no");
        this.demographicNo = (String) ht.get("demographic_no");
        this.formName = (String) ht.get("formName");
        this.formSubject = (String) ht.get("formSubject");
        this.formDate = (String) ht.get("formDate");
        this.formHtml = (String) ht.get("formHtml");
        this.patientIndependent = (Boolean) ht.get("patientIndependent");
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
    }

    public void setAppointmentNo(String appointment_no) {
        this.appointment_no = appointment_no;
    }

    public void setApptProvider(String appt_provider) {
        this.appt_provider = appt_provider;
    }

    public void setAction(String pAjaxId) {
        parentAjaxId = pAjaxId;
        setAction();
    }
    
    public void setAction() {
        setAction(false);
    }
    
    public void setAction(boolean unset) {
        //sets action= in the form
        StringBuffer html = new StringBuffer(this.formHtml);
        int index = StringBufferUtils.indexOfIgnoreCase(html, "<form", 0);
        int endtag = html.indexOf(">", index+1);
        //--remove all previous actions, methods and names from the form tag
        if (index < 0) return;
        
        int pointer, pointer2;
        while (((pointer = StringBufferUtils.indexOfIgnoreCase(html, " action=", index)) >= 0) && (pointer < endtag)) {
            pointer2 = nextSpot(html, pointer+1);
            html = html.delete(pointer, pointer2);
            endtag = html.indexOf(">", index+1);
        }
        while (((pointer = StringBufferUtils.indexOfIgnoreCase(html, " method=", index)) >= 0) && (pointer < endtag)) {
            pointer2 = nextSpot(html, pointer+1);
            html = html.delete(pointer, pointer2);
            endtag = html.indexOf(">", index+1);
        }
        pointer = StringBufferUtils.indexOfIgnoreCase(html, " name=", index);
        String name = "name=\"saveEForm\" ";
        if ((pointer >= 0) && (pointer < endtag)) {
            pointer2 = nextSpot(html, pointer+1);
            html = html.delete(pointer, pointer2);
            endtag = html.indexOf(">", index+1);
            name = "";
        }
        if (index < 0) return;
        if (unset) {
            this.formHtml = html.toString();
            return;
        }
        index += 5;
        StringBuffer action = new StringBuffer("action=\"../eform/addEForm.do?efmfid=" + this.fid +
                        "&efmdemographic_no=" + this.demographicNo +
                        "&efmprovider_no=" + this.providerNo);
        if( this.parentAjaxId != null )
            action.append("&parentAjaxId=" + this.parentAjaxId);
        
        action.append("\"");
        
        String method = "method=\"POST\"";
        html.insert(index, " " + action.toString() + " " + name + method);
        this.formHtml = html.toString();
    }

    //------------------Saving the Form (inserting value= statements)---------------------
    public void setValues(ArrayList names, ArrayList values) {
        if (names.size() != values.size()) return;
        StringBuffer html = new StringBuffer(this.formHtml);
        int pointer = 0;
        int nameEnd;
        while ((pointer = StringBufferUtils.indexOfIgnoreCase(html, "name=", pointer)) >= 0) {
            nameEnd = nextSpot(html, pointer);
            if (nameEnd < 0) continue;
            pointer += 5;
            if (html.charAt(pointer) == '"') {
                pointer++;
                nameEnd--;  //if name="..." (quotes)
            }
            String nameFound = html.substring(pointer, nameEnd);
            int i;
            if ((i = names.indexOf(nameFound)) < 0) continue;
            html = putValue((String) values.get(i), pointer, html);
	    
        }
        this.formHtml = html.toString();
    }
    
    //--------------------------Setting APs utilities----------------------------------------
    public void setDatabaseAPs() {
        String marker = EFormLoader.getInstance().getMarker();  //default: marker: "oscarDB="
        StringBuffer html = new StringBuffer(this.formHtml);
        String apName = "";
        DatabaseAP curAP = null;
        if (demographicNo==null) demographicNo = "";
        if (providerNo==null) providerNo = "";
        for (int i=0; i<2; i++) { //run the following twice if "count"-type field is found
            int markerLoc;
            int pointer = 0;
            while ((markerLoc = StringBufferUtils.indexOfIgnoreCase(html, marker, pointer)) >= 0) {
                log.debug("===============START CYCLE===========");
                pointer = (markerLoc + marker.length());  //move to the AP string
                apName = getAPstr(html, pointer);  //gets varname from oscarDB=varname
                pointer++;
                log.debug("AP ====" + apName);
                curAP = EFormLoader.getInstance().getAP(EFormUtil.removeQuotes(apName));
                if (curAP == null) curAP = getExtra(EFormUtil.removeQuotes(apName));
                if (curAP == null) continue;

                String fieldType = getFieldType(html, pointer); //textarea, text, hidden etc..
                if ((fieldType.equals("")) || (EFormUtil.removeQuotes(apName).equals(""))) continue;
                //sets up the pointer where to write the value
                if (!fieldType.equals("textarea")) {
                    pointer += apName.length();
                }
                html = putValue(curAP, fieldType, pointer, html);
                log.debug("Marker ==== " + markerLoc);
                log.debug("FIELD TYPE ====" + fieldType);
                log.debug("=================End Cycle==============");
            }
            formHtml = html.toString();
            if (hasCountType) setAP2nd=true;
            else i=2;
        }
    }
    
    public void setNowDateTime() {
        this.formTime = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "HH:mm:ss");
        this.formDate = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd");
    }
    //------------------------------------------------------------------------------------

    public DatabaseAP getExtra(String apName) {
        Pattern p = Pattern.compile("\\b[a-z]\\$[^ \\$#]+#[^\n]+");
        Matcher m = p.matcher(apName);
        if (!m.matches()) return null;

        String module = apName.substring(0, apName.indexOf("$"));
        String type = apName.substring(apName.indexOf("$")+1, apName.indexOf("#"));
        String field = apName.substring(apName.indexOf("#")+1, apName.length());
        DatabaseAP curAP = null;
        if (module.equals("m")) {
            log.debug("SWITCHING TO MEASUREMENTS");
            Hashtable data = EctMeasurementsDataBeanHandler.getLast(this.demographicNo, type);
            if (data!=null) {
                curAP = new DatabaseAP();
                curAP.setApName(apName);
                curAP.setApOutput((String)data.get(field));
            }
        } else if (module.equals("e")) {
            log.debug("SWITCHING TO EFORM_VALUES");
            String[] values={"",""}, varnames={"",""};
            if (type.equalsIgnoreCase("count")) {
                varnames = getVarnamesFromField(field);
                values = getValuesFromField(field);
                if (values[0].trim().startsWith("{") || values[1].trim().startsWith("{")) {
                    if (setAP2nd) { //2nd run, put value into count type
                        values[0] = findValueInForm(values[0]);
                        values[1] = findValueInForm(values[1]);
                    } else { //1st run, mark count type exists
                        hasCountType = true;
                        return null;
                    }
                }
                if (values[0].equals("")) type = "countall";
                if (!varnames[1].equals("")) {
                    type += "_ref";
                    if (values[1].equals("")) type += "all";
                }
            }
            curAP = EFormLoader.getInstance().getAP("_eform_values_"+type);
            if (curAP!=null) {
                setSqlParams(VAR_NAME, varnames[0]);
                setSqlParams(REF_VAR_NAME, varnames[1]);
                setSqlParams(VAR_VALUE, values[0]);
                setSqlParams(REF_VAR_VALUE, values[1]);
            }
        } else if (module.equals("o")) {
            log.debug("SWITCHING TO OTHER_ID");
            String table_name="", table_id="";
            curAP = EFormLoader.getInstance().getAP("_other_id");
            if (type.equalsIgnoreCase("patient")) {
                table_name = OtherIdManager.DEMOGRAPHIC.toString();
                table_id = this.demographicNo;
            } else if (type.equalsIgnoreCase("appointment")) {
                table_name = OtherIdManager.APPOINTMENT.toString();
                table_id = appointment_no;
                if (table_id==null) table_id = "-1";
            }
            setSqlParams(OTHER_KEY, field);
            setSqlParams(TABLE_NAME, table_name);
            setSqlParams(TABLE_ID, table_id);
        }
        return curAP;
    }

    public ActionMessages setMeasurements(ArrayList names, ArrayList values) {
        return(WriteNewMeasurements.addMeasurements(names, values, this.demographicNo, this.providerNo));
    }

	public void setContextPath(String contextPath) {
		if (contextPath==null) return;

		String oscarJS = contextPath+"/share/javascript/";
		this.formHtml = this.formHtml.replace(jsMarker, oscarJS);
	}

	public String getTemplate() {
	// Get content between "<!-- <template>" & "</template> -->"
        if (this.formHtml==null) return "";

		String sTemplateBegin = "<template>";
		String sTemplateEnd = "</template>";
        String sCommentBegin="<!--", sCommentEnd="-->";
        String text = "";

        int templateBegin=-1, templateEnd=-1;
        boolean searching = true;
        while (searching) {
            templateBegin = EFormUtil.findIgnoreCase(sTemplateBegin, formHtml, templateBegin+1);
            templateEnd = EFormUtil.findIgnoreCase(sTemplateEnd, formHtml, templateBegin);
            int commentBegin = formHtml.lastIndexOf(sCommentBegin, templateBegin);
            int commentEnd = formHtml.indexOf(sCommentEnd, commentBegin);
            if (templateBegin==-1 || templateEnd==-1 || commentBegin==-1 || commentEnd==-1) {
                searching = false;
            } else if (commentEnd>templateEnd) {
                text += EFormUtil.removeBlanks(formHtml.substring(templateBegin+sTemplateBegin.length(), templateEnd));
            }
        }
        return text;
	}



    //----------------------------------private
    private StringBuffer putValue(String value, int pointer, StringBuffer html) {
        //inserts value= into tag or textarea
        pointer -= 2;  //take it back to name^="
        String tagType = getFieldType(html, pointer);
        pointer = nextSpot(html, pointer);

        if (tagType.equals("textarea")) {
            pointer = html.indexOf(">", pointer) + 1;
            int endPointer = html.indexOf("<", pointer);
            html.delete(pointer, endPointer);
            html.insert(pointer, value);
        }
        else if (tagType.equals("text") || tagType.equals("hidden")) {
            String insertStr = "value=\"" + value + "\"";
            html.insert(pointer, " " + insertStr);
        }
        else if (tagType.equals("checkbox")) {
            html.insert(pointer, " checked");
        }
        else if (tagType.equals("select")) {
            int endindex = StringBufferUtils.indexOfIgnoreCase(html, "</select>", pointer);
            if (endindex < 0) return html; //if closing tag not found
            int valueLoc = nextIndex(html, " value="+value, " value=\""+value, pointer);
            if (valueLoc < 0 || valueLoc > endindex) {
                valueLoc = nextIndex(html, " VALUE="+value, " VALUE=\""+value, pointer);
            }
            if (valueLoc < 0 || valueLoc > endindex) return html;
            pointer = nextSpot(html, valueLoc+1);
            html.insert(pointer, " selected");
        }
        else if (tagType.equals("radio")) {
            int endindex = html.indexOf(">", pointer);
            int valindexS = nextIndex(html, " value=", " VALUE=", pointer);
            if (valindexS < 0 || valindexS > endindex) return html; //if value= not found in tag
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

    private int nextIndex(StringBuffer text, String option1, String option2, int pointer) {
        //returns the index of option1 or option2 whichever one is closer and exists
        int index;
        int option1i = text.indexOf(option1, pointer);
        int option2i = text.indexOf(option2, pointer);
        if (option1i < 0) index = option2i;
        else if (option2i < 0 || option1i < option2i) index = option1i;
        else index = option2i;
        return index;
    }

    private int nextSpot(StringBuffer text, int pointer) {
        int end = nextIndex(text, "\n", "\r", pointer);
        if (end<0) end = text.length();
        int index = text.substring(pointer, end).indexOf('=') + pointer;
        if (index>=0) {
            if (text.charAt(index+1)=='"') {
                int close = text.substring(index+2, end).indexOf("\"") + (index+2);
                if (close>0) return close+1;
            }
            if (text.charAt(index+1)=='\'') {
                int close = text.substring(index+2, end).indexOf("'") + (index+2);
                if (close>0) return close+1;
            }
            pointer = index;
        }
        return nextIndex(text, " ", ">", pointer);
    }

    private String getAPstr(StringBuffer html, int startIndex) {
        //Isolates the databaseAP string
        int endIndex = nextSpot(html, startIndex);
        String isolated = html.substring(startIndex+1, endIndex);
        return isolated.trim();
    }

    private String getFieldType(StringBuffer html, int pointer) {
        //pointer can be any place in the tag - isolates tag and sends back field type
        int open = html.substring(0, pointer).lastIndexOf("<");
        int close = html.substring(pointer).indexOf(">") + pointer + 1;
        String tag = html.substring(open, close);
        log.debug("TAG ====" + tag);
        int start;  //<input type="^text".....
        int end;    //<input type="text^"....
        String typeStr = null;
        if (tag.substring(1, 9).equalsIgnoreCase("textarea")) return "textarea";
        if (tag.substring(1, 7).equalsIgnoreCase("select")) return "select";
        log.debug("TAG PROCESS ====" + tag.substring(1,9));
        if ((start = tag.toLowerCase().indexOf(" type=")) >= 0) {
            start += 6;                     //account for type=...
            if (tag.charAt(start) == '\"') {  //account for type="..."
                start += 1;
		end = tag.indexOf("\"", start);
            }
            else {
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

    private StringBuffer putValue(DatabaseAP ap, String type, int pointer, StringBuffer html) {
        //pointer set up to where to write the value
        String sql = ap.getApSQL();
        String output = ap.getApOutput();
        //replace ${demographic} with demogrpahicNo
        if (sql != null) {
			sql = replaceAllFields(sql);
            log.debug("SQL----" + sql);
            ArrayList names = DatabaseAP.parserGetNames(output); //a list of ${apName} --> apName
            sql = DatabaseAP.parserClean(sql);  //replaces all other ${apName} expressions with 'apName'
            ArrayList values = EFormUtil.getValues(names, sql);
            if (values.size() != names.size()) {
                output = "";
            } else {
                for (int i=0; i<names.size(); i++) {
                    output = DatabaseAP.parserReplace((String) names.get(i), (String) values.get(i), output);
                }
            }
        }
        if (type.equals("textarea")) {
            pointer = html.indexOf(">", pointer) + 1;
            html.insert(pointer, output);
        } else if (type.equals("select")) {
            int selectEnd = StringBufferUtils.indexOfIgnoreCase(html, "</select>", pointer);
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
            html.insert(pointer, " value=\"" + output + "\"");
        }
        return(html);
    }

	private String replaceAllFields(String sql) {
        sql = DatabaseAP.parserReplace("demographic", demographicNo, sql);
        sql = DatabaseAP.parserReplace("provider", providerNo, sql);
        sql = DatabaseAP.parserReplace("fid", fid, sql);
        
        sql = DatabaseAP.parserReplace("appt_no", appointment_no, sql);
        sql = DatabaseAP.parserReplace("appt_provider", appt_provider, sql);

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
        if (sql_params.containsKey(key)) return (String)sql_params.get(key);
        else return "";
    }

    private void setSqlParams(String key, String value) {
        if (sql_params.containsKey(key)) sql_params.remove(key);
        sql_params.put(key, value);
    }

    private String[] getVarnamesFromField(String field) {
        String[] varnames = {"",""};
        if (field==null || field.trim().equals("")) return varnames;

        String[] parts = field.split("@@");
        int limit = parts.length<=2 ? parts.length : 2;
        for (int i=0; i<limit; i++) {
            if (parts[i].contains("==")) {
                varnames[i] = parts[i].substring(0, parts[i].indexOf("=="));
            } else {
                varnames[i] = parts[i];
            }
        }
        return varnames;
    }

    private String[] getValuesFromField(String field) {
        String[] values = {"",""};
        if (field==null || !field.contains("==")) return values;

        String[] parts = field.split("@@");
        int limit = parts.length<=2 ? parts.length : 2;
        for (int i=0; i<limit; i++) {
            if (parts[i].contains("==")) {
                values[i] = EFormUtil.removeQuotes(parts[i].substring(parts[i].indexOf("==")+2));
            }
        }
        return values;
    }

    private String findValueInForm(String name) {
        //name format = {xxx}
        if (name==null || !name.trim().startsWith("{") || !name.trim().endsWith("}")) return "";

        //extract content from brackets {}
        name = name.trim().substring(1, name.trim().length()-1);
        if (name.trim().equals("")) return "";

        Pattern pfield = Pattern.compile("<input [^<>]* name[ ]*=[^<>]*>|<select [^<>]* name[ ]*=[^<>]*>|<textarea [^<>]* name[ ]*=[^<>]*>", Pattern.CASE_INSENSITIVE);
        Matcher mfield = pfield.matcher(formHtml);
        while (mfield.find()) { //found a field
            String field = mfield.group();
            Pattern p1 = Pattern.compile("\\bname[ ]*=[ ]*\"[^>\"]*\"|\\bname[ ]*=[ ]*'[^>']*'|\\bname[ ]*=[^ >]*", Pattern.CASE_INSENSITIVE);
            Matcher m1 = p1.matcher(field);
            if (m1.find()) { //found a field name
                String fieldname = m1.group();
                fieldname = EFormUtil.removeQuotes(fieldname.substring(fieldname.indexOf("=")+1));
                if (name.equalsIgnoreCase(fieldname)) { //field name equal parameter "name"
                    if (field.toLowerCase().startsWith("<input")) {
                        p1 = Pattern.compile("\\btype[ ]*=[ ]*['\"]?radio['\"]?", Pattern.CASE_INSENSITIVE);
                        m1 = p1.matcher(field);
                        if (m1.find() && !field.toLowerCase().contains(" checked")) continue;

                        p1 = Pattern.compile("\\bvalue[ ]*=[ ]*\"[^>\"]*\"|\\bvalue[ ]*=[ ]*'[^>']*'|\\bvalue[ ]*=[^ >]*", Pattern.CASE_INSENSITIVE);
                        m1 = p1.matcher(field);
                        if (m1.find()) {
                            String value = m1.group();
                            value = EFormUtil.removeQuotes(value.substring(value.indexOf("=")+1));
                            return value;
                        }
                        break;

                    } else if (field.toLowerCase().startsWith("<select")) {
                        String partHtml = formHtml.substring(mfield.end());
                        p1 = Pattern.compile("<option [^<>]*>|</select>", Pattern.CASE_INSENSITIVE);
                        m1 = p1.matcher(partHtml);
                        while (m1.find()) {
                            String entry = m1.group();
                            if (!entry.toLowerCase().contains(" selected")) continue;
                            if (entry.equalsIgnoreCase("</select>")) break;

                            p1 = Pattern.compile("\\bvalue[ ]*=[ ]*\"[^>\"]*\"|\\bvalue[ ]*=[ ]*'[^>']*'|\\bvalue[ ]*=[^ >]*", Pattern.CASE_INSENSITIVE);
                            m1 = p1.matcher(entry);
                            if (m1.find()) {
                                String value = m1.group();
                                value = EFormUtil.removeQuotes(value.substring(value.indexOf("=")+1));
                                return value;
                            }
                        }
                        break;

                    } else if (field.toLowerCase().startsWith("<textarea")) {
                        String partHtml = formHtml.substring(mfield.end());
                        partHtml = partHtml.substring(0, partHtml.toLowerCase().indexOf("</textarea>"));
                        if (partHtml.startsWith("\n") || partHtml.startsWith("\r")) {
                            partHtml = partHtml.substring(1);
                        }
                        return partHtml;
                    }
                }
            }
        }
        return "";
    }
}
