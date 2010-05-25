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
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMessages;

import oscar.eform.EFormLoader;
import oscar.eform.EFormUtil;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler;
import oscar.oscarEncounter.oscarMeasurements.util.WriteNewMeasurements;
import oscar.util.StringBufferUtils;
import oscar.util.UtilDateUtilities;

public class EForm extends EFormBase {
	public static String APPT_PROVIDER_ID = "appt_provider_id";
	public static String APPT_PROVIDER_NAME = "appt_provider_name";
	public static String APPT_MC_NO = "appt_mc_no";
	public static String PATIENT_TFNOTES = "patient_tfnotes";
	public static String RESIDENT_TFNOTES = "resident_tfnotes";
	private static String OSCAR_JS_PATH = "${oscar_javascript_path}";
    private static Log log = LogFactory.getLog(EForm.class);
    
    private String parentAjaxId = null;
    
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
        fid = (String) ht.get("fid");
        providerNo = (String) ht.get("provider_no");
        demographicNo = (String) ht.get("demographic_no");
        formName = (String) ht.get("formName");
        formSubject = (String) ht.get("formSubject");
        //formTime = curht.get("formTime");
        formDate = (String) ht.get("formDate");
        formHtml = (String) ht.get("formHtml");
        patientIndependent = (Boolean) ht.get("patientIndependent");
    }
    
    public void loadEForm(String fid, String demographicNo) {
        Hashtable loaded = (Hashtable) EFormUtil.loadEForm(fid);
        this.fid = fid;
        formName = (String) loaded.get("formName");
        formHtml = (String) loaded.get("formHtml");
        this.formSubject = (String) loaded.get("formSubject");
        this.formDate = (String) loaded.get("formDate");
        this.formFileName = (String) loaded.get("formFileName");
        this.formCreator = (String) loaded.get("formCreator");
        this.demographicNo = demographicNo;
        this.patientIndependent = (Boolean) loaded.get("patientIndependent");
    }
    
    public void setAction(String pAjaxId) {
        parentAjaxId = pAjaxId;
        setAction();
    }
    
    public void setAction() {
        //sets action= in the form
        StringBuffer html = new StringBuffer(formHtml);
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
            //html = html.delete(pointer, pointer2);
            endtag = html.indexOf(">", index+1);
            name = "";
        }
        if (index < 0) return;
        index += 5;
        StringBuffer action = new StringBuffer("action=\"../eform/addEForm.do?efmfid=" + fid + 
                        "&efmdemographic_no=" + demographicNo + 
                        "&efmprovider_no=" + providerNo);
        if( parentAjaxId != null )
            action.append("&parentAjaxId=" + parentAjaxId);
        
        action.append("\"");
        
        String method = "method=\"POST\"";
        html.insert(index, " " + action.toString() + " " + name + method);
        formHtml = html.toString();
    }
    
    //------------------Saving the Form (inserting value= statements)---------------------
    public void setValues(ArrayList names, ArrayList values) {
        if (names.size() != values.size()) return;
        StringBuffer html = new StringBuffer(formHtml);
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
        formHtml = html.toString();
    }
    
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
            //int endindex = nextIndex(html, "</select>", "</SELECT>", pointer);
            if (endindex < 0) return html; //if closing tag not found
            int valueLoc = nextIndex(html, " value=" + value, " value=\"" + value, pointer);
            if (valueLoc < 0 || valueLoc > endindex) {
                valueLoc = nextIndex(html, " VALUE=" + value, " VALUE=\"" + value, pointer);
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
        int index;
        int open = text.substring(0, pointer).lastIndexOf("<");
        int close = text.substring(pointer).indexOf(">") + pointer + 1;
        index = text.substring(pointer, close).indexOf('=') + pointer;
        if ((index >= 0) && (index < close)) {
            if (text.charAt(index+1) == '"') {
                int nextSpot = nextIndex(text, "\"", ">", index+2);
                if (text.charAt(nextSpot) == '"') nextSpot++;
                return nextSpot;
            }
        }
        return nextIndex(text, " ", ">", pointer);
    }
    
    //--------------------------Setting APs utilities----------------------------------------
    public void setDatabaseAPs() {
        //ArrayList names = EFormLoader.getInstance().getNames();
        String marker = EFormLoader.getInstance().getMarker();  //default: marker: "oscarDB="
        //marker = marker.toLowerCase(); //nothing is case sensitive
        //formHtml = formHtml.toLowerCase();
        StringBuffer html = new StringBuffer(formHtml);
        String apName = "";
        DatabaseAP curAP = null;
        int markerLoc;
        int pointer = 0;
        while ((markerLoc = StringBufferUtils.indexOfIgnoreCase(html, marker, pointer)) >= 0) {
            log.debug("===============START CYCLE===========");
            pointer = (markerLoc + marker.length());  //move to the AP string
            apName = getAPstr(html, pointer);  //gets varname from oscarDB=varname
            pointer++;
            log.debug("AP ====" + apName);
            curAP = EFormLoader.getInstance().getAP(apName);
            if (curAP == null) {
                log.debug("SWITCHING TO MEASUREMENTS");
                curAP = getMeasurement(apName);
                if (curAP == null) continue;
            }
            String fieldType = getFieldType(html, pointer); //textarea, text, hidden etc..
            if ((fieldType.equals("")) || (apName.equals(""))) continue;
            //sets up the pointer where to write the value
            if (!fieldType.equals("textarea")) {
                pointer += apName.length();
            }
            html = putValue(curAP, fieldType, pointer, html);
            //html.insert(pointer, dbValue(curAP, fieldType));
            log.debug("Marker ==== " + markerLoc);
            log.debug("FIELD TYPE ====" + fieldType);
            log.debug("=================End Cycle==============");
        }
        formHtml = html.toString();
    }
    
    private String getAPstr(StringBuffer html, int startIndex) {
        //Isolates the databaseAP string
        int endIndex = nextSpot(html, startIndex);
        String isolated = html.substring(startIndex+1, endIndex);
        return isolated;
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
            sql = DatabaseAP.parserReplace("demographic", demographicNo, sql);
            if (providerNo != null){
                sql = DatabaseAP.parserReplace("provider", providerNo, sql);
            }
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
    
    public void setNowDateTime() {
        formTime = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "HH:mm:ss");
        formDate = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd");
    }
    //------------------------------------------------------------------------------------
    
    public DatabaseAP getMeasurement(String apName) {
        int typeStart = apName.indexOf("m$");
        int fieldStart = apName.indexOf("#");
        if ((typeStart < 0) || (fieldStart < 0)) return null;
        typeStart += 2;
        fieldStart++;
        Hashtable data = new Hashtable();
        String type = apName.substring(typeStart, fieldStart-1);
        String field = apName.substring(fieldStart);
        String output = null;
        data = EctMeasurementsDataBeanHandler.getLast(demographicNo, type);
        if (data == null) return null;
        output = (String) data.get(field);
        log.debug("FIELD-------------------------------------------------=\"" + field + "\"");
        DatabaseAP curAP = new DatabaseAP();
        curAP.setApName(apName);
        curAP.setApOutput(output);
        return curAP;
    }
    
    public ActionMessages setMeasurements(ArrayList names, ArrayList values) {
        return(WriteNewMeasurements.addMeasurements(names, values, demographicNo, providerNo));
    }

	public void setSpecial(Hashtable sparam) {
        String marker = EFormLoader.getInstance().getMarker();  //default: marker: "oscarDB="
        StringBuffer html = new StringBuffer(formHtml);

        int markerLoc;
        int pointer = 0;
        while ((markerLoc = StringBufferUtils.indexOfIgnoreCase(html, marker, pointer)) >= 0) {
            log.debug("===============START CYCLE===========");
            pointer = (markerLoc + marker.length());  //move to the AP string
            String varName = getAPstr(html, pointer);  //gets varname from oscarDB=varname
            pointer++;
			if (!sparam.containsKey(varName)) continue;

            DatabaseAP curAP = getSpecial(varName, sparam);
            if (curAP == null) continue;

            String fieldType = getFieldType(html, pointer); //textarea, text, hidden etc..
            if ((fieldType.equals("")) || (curAP.getApName().equals(""))) continue;
            //sets up the pointer where to write the value
            if (!fieldType.equals("textarea")) {
                pointer += curAP.getApName().length();
            }
            html = putSpecialValue(curAP, fieldType, pointer, html);
            log.debug("Marker ==== " + markerLoc);
            log.debug("FIELD TYPE ====" + fieldType);
            log.debug("=================End Cycle==============");
        }
        formHtml = html.toString();
	}

	public void setContextPath(String contextPath) {
		if (contextPath==null) return;

		String oscarJS = contextPath+"/share/javascript/";
		formHtml = formHtml.replace(OSCAR_JS_PATH, oscarJS);
	}

	private DatabaseAP getSpecial(String apName, Hashtable sparam) {
		DatabaseAP ap = null;
		String sql = null;
		String output = null;

		if (apName.equalsIgnoreCase(APPT_PROVIDER_NAME)) {
			sql = "SELECT last_name, first_name FROM provider WHERE provider_no = '"+sparam.get(apName)+"'";
			output = "${last_name}, ${first_name}";
		} else if (apName.equalsIgnoreCase(APPT_PROVIDER_ID)) {
			sql = "SELECT provider_no FROM provider WHERE provider_no = '"+sparam.get(apName)+"'";
			output = "${provider_no}";
		} else if (apName.equalsIgnoreCase(APPT_MC_NO)) {
			sql = "SELECT value FROM appointmentExt WHERE appointment_no = "+sparam.get(apName);
			output = "${value}";
		} else if (apName.equalsIgnoreCase(PATIENT_TFNOTES)) {
			sql = "SELECT count(0) FROM eform_data WHERE demographic_no="+demographicNo+" AND fid="+fid;
			output = "${count(0)}";
		} else if (apName.equalsIgnoreCase(RESIDENT_TFNOTES)) {
			sql = "SELECT count(0) FROM eform_values v , eform_data d WHERE var_name='residentId' AND var_value='"+sparam.get(apName)+"' AND v.fdid=d.fdid AND d.status=1";
			output = "${count(0)}";
		}
		if (sql!=null) ap = new DatabaseAP(apName, sql, output);

		return ap;
	}

    private StringBuffer putSpecialValue(DatabaseAP ap, String type, int pointer, StringBuffer html) {
        String sql = ap.getApSQL();
        String output = ap.getApOutput();
        if (sql != null) {
            log.debug("SQL----" + sql);
            sql = DatabaseAP.parserClean(sql);  //replaces all other ${apName} expressions with 'apName'
            ArrayList names = DatabaseAP.parserGetNames(output); //a list of ${apName} --> apName
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
}
