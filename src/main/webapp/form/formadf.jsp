<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
	String formClass = "Adf";
	String formLink = "formadf.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    //FrmData fd = new FrmData();    String resource = fd.getResource(); resource = resource + "ob/riskinfo/";

	//get project_home
	String project_home = request.getContextPath().substring(1);	
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>MEDICAL HISTORY AND ADMISSION EXAMINATION</title>
<link rel="stylesheet" type="text/css" href="arStyle.css">
<html:base />
</head>


<script type="text/javascript" language="Javascript">

    function reset() {
        document.forms[0].target = "apptProviderSearch";
        document.forms[0].action = "/<%=project_home%>/form/formname.do" ;
	}
    function onPrint() {
        document.forms[0].submit.value="print"; //printAR1
        var ret = checkAllDates();
        if(ret==true)
        {
            //ret = confirm("Do you wish to save this form and view the print preview?");
            popupFixedPage(650,850,'../provider/notice.htm');
            document.forms[0].action = "../form/createpdf?__title=&__cfgfile=&__cfgfile=&__template=";
            document.forms[0].target="planner";
            document.forms[0].submit();
            document.forms[0].target="apptProviderSearch";
        }
        return ret;
    }
    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true) {
            reset();
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret == true) {
            reset();
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        return ret;
    }
    function popupPage(varpage) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage, "ar2", windowprops);
        if (popup.opener == null) {
            popup.opener = self;
        }
    }
    function popPage(varpage,pageName) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage,pageName, windowprops);
        //if (popup.opener == null) {
        //    popup.opener = self;
        //}
        popup.focus();
    }
    function popupFixedPage(vheight,vwidth,varpage) { 
       var page = "" + varpage;
       windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=10,screenY=0,top=0,left=0";
       var popup=window.open(page, "planner", windowprop);
    }

/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=9900;

    function isInteger(s){
        var i;
        for (i = 0; i < s.length; i++){
            // Check that current character is number.
            var c = s.charAt(i);
            if (((c < "0") || (c > "9"))) return false;
        }
        // All characters are numbers.
        return true;
    }

    function stripCharsInBag(s, bag){
        var i;
        var returnString = "";
        // Search through string's characters one by one.
        // If character is not in bag, append to returnString.
        for (i = 0; i < s.length; i++){
            var c = s.charAt(i);
            if (bag.indexOf(c) == -1) returnString += c;
        }
        return returnString;
    }

    function daysInFebruary (year){
        // February has 29 days in any year evenly divisible by four,
        // EXCEPT for centurial years which are not also divisible by 400.
        return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
    }
    function DaysArray(n) {
        for (var i = 1; i <= n; i++) {
            this[i] = 31
            if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
            if (i==2) {this[i] = 29}
       }
       return this
    }

    function isDate(dtStr){
        var daysInMonth = DaysArray(12)
        var pos1=dtStr.indexOf(dtCh)
        var pos2=dtStr.indexOf(dtCh,pos1+1)
        var strMonth=dtStr.substring(0,pos1)
        var strDay=dtStr.substring(pos1+1,pos2)
        var strYear=dtStr.substring(pos2+1)
        strYr=strYear
        if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
        if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
        for (var i = 1; i <= 3; i++) {
            if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
        }
        month=parseInt(strMonth)
        day=parseInt(strDay)
        year=parseInt(strYr)
        if (pos1==-1 || pos2==-1){
            return "format"
        }
        if (month<1 || month>12){
            return "month"
        }
        if (day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
            return "day"
        }
        if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
            return "year"
        }
        if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
            return "date"
        }
    return true
    }


    function checkTypeIn(obj) {
      if(!checkTypeNum(obj.value) ) {
          alert ("You must type in a number in the field.");
        }
    }

    function valDate(dateBox)
    {
        try
        {
            var dateString = dateBox.value;
            if(dateString == "")
            {
				//alert('dateString'+dateString);
                return true;
            }
            var dt = dateString.split('/');
            //var y = dt[2];  var m = dt[1];  var d = dt[0];
            var y = dt[0];  var m = dt[1];  var d = dt[2];
            var orderString = m + '/' + d + '/' + y;
            var pass = isDate(orderString);

            if(pass!=true)
            {
                alert('Invalid '+pass+' in field ' + dateBox.name);
                dateBox.focus();
                return false;
            }
        }  catch (ex)  {
            alert('Catch Invalid Date in field ' + dateBox.name);
            dateBox.focus();
            return false;
        }
        return true;
    }

    function checkAllDates() {
        var b = true;
        if(valDate(document.forms[0].pg1_eddByDate)==false){
            b = false;
        } else if(valDate(document.forms[0].pg1_eddByUs)==false){
            b = false;
        } 

        return b;
    }
</script>

<body bgproperties="fixed" topmargin="0" leftmargin="1" rightmargin="1">
<!--
@oscar.formDB Table="formAdf" 
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'" 
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL" 
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL" 
@oscar.formDB Field="formEdited" Type="timestamp"  
-->
<html:form action="/form/formname">
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<!--input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" /-->
	<input type="hidden" name="submit" value="exit" />


	<table class="Head" class="hidePrint">
		<tr>
			<td align="left">
			<%
  if (!bView) {
%> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <%
  }
%> <input type="button" value="Exit"
				onclick="javascript:return onExit();" /> <input type="button"
				value="Print" onclick="javascript:window.print();" /></td>
			<%
  if (!bView) {
%>
			<td align="right"><a
				href="javascript: popupFixedPage(700,950,'../decision/antenatal/antenatalplanner.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>');">Planner</a>
			</td>
			<%
  }
%>
		</tr>
	</table>


	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr bgcolor="#486ebd">
			<th align='CENTER'><font face="Arial, Helvetica, sans-serif"
				color="#FFFFFF">MEDICAL HISTORY AND ADMISSION EXAMINATION</font></th>
		</tr>
		<tr>
			<td align="center" bgcolor="#CCCCCC"><b><font
				face="Verdana, Arial, Helvetica, sans-serif">(To be submitted
			by examining physician or completed within 7 days after arrival) </font></b></td>
		</tr>
	</table>


	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr width="100%">
			<td width="15%" align="left">Patient Name</td>
			<td width="35%"><input type="text" name="c_patientname"
				style="width: 100%" size="60" maxlength="60"
				value="<%= props.getProperty("c_surname", "")+", " + props.getProperty("c_givenName", "") %>"
				@oscar.formDB /></td>
			<td width="15%" align="right">Resident No.</td>
			<td width="35%"><input type="text" name="residentno"
				style="width: 100%" size="20" maxlength="20"
				value="<%= props.getProperty("residentno", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td width="20%" align="left">Examining Physician</td>
			<td width="80%" colspan='3'><input type="text"
				name="c_physician" style="width: 100%" size="30" maxlength="60"
				value="<%= props.getProperty("c_physician", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Address</td>
			<td width="45%"><input type="text" name="c_address"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("c_address", "") %>" @oscar.formDB />
			</td>
			<td align="right">Phone &nbsp</td>
			<td><input type="text" name="c_phone" style="width: 100%"
				size="20" maxlength="20"
				value="<%= props.getProperty("c_phone", "") %>" @oscar.formDB /></td>
		</tr>
	</table>
	<br>
	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr bgcolor="#486ebd">
			<th align=CENTER><font face="Arial, Helvetica, sans-serif"
				color="#FFFFFF">MEDICAL HISTORY </font></th>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="3">Chief Complaint <textarea name="cComplait"
				style="width: 100%" cols="20" rows="3" @oscar.formDB dbType="text" /><%= props.getProperty("cComplait", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td colspan="3">History of Present Illness <textarea
				name="histPresIll" style="width: 100%" cols="20" rows="3"
				@oscar.formDB dbType="text" /><%= props.getProperty("histPresIll", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td valign="top" width="10%" rowspan='5'>Past Health:</td>
			<td>Childhood</td>
			<td><input type="text" name="childhood" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("childhood", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td width="10%">Adult</td>
			<td><input type="text" name="adult" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("adult", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Operations</td>
			<td><input type="text" name="operations" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("operations", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Injuries</td>
			<td><input type="text" name="injuries" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("injuries", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Mental Illness</td>
			<td><input type="text" name="mentalIll" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("mentalIll", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td colspan="3">Family History <textarea name="familyHist"
				style="width: 100%" cols="20" rows="3" @oscar.formDB dbType="text" /><%= props.getProperty("familyHist", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td colspan="3">Social History<b>(Refer to Section 10 in
			DSF)</b> <textarea name="socialHist" style="width: 100%" cols="20"
				rows="3" @oscar.formDB dbType="text" /><%= props.getProperty("socialHist", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td valign="top" width="10%" rowspan='9'>Systemic:</td>
			<td>General</td>
			<td><input type="text" name="general" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("general", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Skin</td>
			<td><input type="text" name="histSkin" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("histSkin", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Head & Neck</td>
			<td><input type="text" name="headNeck" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("headNeck", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Respiratory</td>
			<td><input type="text" name="respiratory" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("respiratory", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Cardiovascular</td>
			<td><input type="text" name="cardiovascular" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("cardiovascular", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>G.I.</td>
			<td><input type="text" name="gi" style="width: 100%" size="60"
				maxlength="80" value="<%= props.getProperty("gi", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td>G.U</td>
			<td><input type="text" name="gu" style="width: 100%" size="60"
				maxlength="80" value="<%= props.getProperty("gu", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td>C.N.S.</td>
			<td><input type="text" name="cns" style="width: 100%" size="60"
				maxlength="80" value="<%= props.getProperty("cns", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td>Extremities</td>
			<td><input type="text" name="histExtremities"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("histExtremities", "") %>"
				@oscar.formDB /></td>
		</tr>

		<tr>
			<td colspan="3">Allergies<b>(Refer to Section 11 in DSF)</b> <textarea
				name="allergies" style="width: 100%" cols="30" rows="3"
				@oscar.formDB dbType="text" /><%= props.getProperty("allergies", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td colspan="3">Sensitivities to Drugs <textarea
				name="sensitivityDrug" style="width: 100%" cols="20" rows="3"
				@oscar.formDB dbType="text" /><%= props.getProperty("sensitivityDrug", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td colspan="3">Current Medications<b>(Refer to Section 9 in
			DSF)</b> <textarea name="currentMedication" style="width: 100%" cols="30"
				rows="3" @oscar.formDB dbType="text" /><%= props.getProperty("currentMedication", "") %></textarea>
			</td>
		</tr>
	</table>
	<br class="break">

	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr bgcolor="#486ebd">
			<th align=CENTER><font face="Arial, Helvetica, sans-serif"
				color="#FFFFFF">ADMISSION EXAMINATION </font></th>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td width="10%">Temp.</td>
			<td width="22%"><input type="text" name="temp"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("temp", "") %>" @oscar.formDB /></td>
			<td width="10%" align="right">Pulse &nbsp</td>
			<td width="22%"><input type="text" name="pulse"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("pulse", "") %>" @oscar.formDB /></td>
			<td width="10%" align="right">Resp.&nbsp</td>
			<td><input type="text" name="resp" style="width: 100%" size="60"
				maxlength="80" value="<%= props.getProperty("resp", "") %>"
				@oscar.formDB /></td>
		</tr>
		<tr>
			<td>B.P</td>
			<td><input type="text" name="bp" style="width: 100%" size="60"
				maxlength="80" value="<%= props.getProperty("bp", "") %>"
				@oscar.formDB /></td>
			<td align="right">Height &nbsp</td>
			<td><input type="text" name="height" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("height", "") %>" @oscar.formDB /></td>
			<td align="right">Weight &nbsp</td>
			<td><input type="text" name="weight" style="width: 100%"
				size="60" maxlength="80"
				value="<%= props.getProperty("weight", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td colspan='6'>Physical Condition <textarea
				name="physicalCondition" style="width: 100%" cols="20" rows="3"
				@oscar.formDB dbType="text" /><%= props.getProperty("physicalCondition", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td colspan='6'>Mental Condition <textarea
				name="mentalCondition" style="width: 100%" cols="20" rows="3"
				@oscar.formDB dbType="text" /><%= props.getProperty("mentalCondition", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td>Skin</td>
			<td colspan='5'><input type="text" name="skin"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("skin", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Eyes</td>
			<td colspan='5'><input type="text" name="eyes"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("eyes", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Ears</td>
			<td colspan='5'><input type="text" name="ears"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("ears", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Nose</td>
			<td colspan='5'><input type="text" name="nose"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("nose", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Mouth,Teeth</td>
			<td colspan='5'><input type="text" name="mouthTeeth"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("mouthTeeth", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Throat</td>
			<td colspan='5'><input type="text" name="throat"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("throat", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Neck</td>
			<td colspan='5'><input type="text" name="neck"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("neck", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Chest</td>
			<td colspan='5'><input type="text" name="chest"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("chest", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Heart</td>
			<td colspan='5'><input type="text" name="heart"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("heart", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Abdomen</td>
			<td colspan='5'><input type="text" name="abdomen"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("abdomen", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Genitalia</td>
			<td colspan='5'><input type="text" name="genitalia"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("genitalia", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Lymphatics</td>
			<td colspan='5'><input type="text" name="lymphatics"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("lymphatics", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Blood Vessels</td>
			<td colspan='5'><input type="text" name="bloodVessels"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("bloodVessels", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Locomotor</td>
			<td colspan='5'><input type="text" name="locomotor"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("locomotor", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Extremities</td>
			<td colspan='5'><input type="text" name="extremities"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("extremities", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td>Rectal</td>
			<td colspan='5'><input type="text" name="rectal"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("rectal", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Vaginal</td>
			<td colspan='5'><input type="text" name="vaginal"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("vaginal", "") %>" @oscar.formDB /></td>
		</tr>
		<tr>
			<td>Neurological</td>
			<td colspan='5'><input type="text" name="neurological"
				style="width: 100%" size="60" maxlength="80"
				value="<%= props.getProperty("neurological", "") %>" @oscar.formDB />
			</td>
		</tr>
		<tr>
			<td colspan="6">Behavior Problems <textarea
				name="behaviorProblem" style="width: 100%" cols="20" rows="5"
				@oscar.formDB dbType="text" /><%= props.getProperty("behaviorProblem", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td colspan="6">Functional Limitations <textarea
				name="functionalLimitation" style="width: 100%" cols="20" rows="5"
				@oscar.formDB dbType="text" /><%= props.getProperty("functionalLimitation", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td colspan="6">Diagnoses <textarea name="diagnoses"
				style="width: 100%" cols="20" rows="5" @oscar.formDB dbType="text" /><%= props.getProperty("diagnoses", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td colspan='3'>Date (yyyy/mm/dd) <input type="text"
				name="sigDate" size="10" maxlength="10"
				value="<%= props.getProperty("sigDate", "") %>" @oscar.formDB
				dbType="date" /></td>
			<td colspan='3'>Physician's Signature <input type="text"
				name="signature" size="50" maxlength="60"
				value="<%= props.getProperty("signature", "") %>" @oscar.formDB />
			</td>
		</tr>
	</table>


	<table class="Head" class="hidePrint">
		<tr>
			<td align="left">
			<%
  if (!bView) {
%> <input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <%
  }
%> <input type="button" value="Exit"
				onclick="javascript:return onExit();" /> <input type="button"
				value="Print" onclick="javascript:window.print();" /></td>
			<%
  if (!bView) {
%>
			<td align="right"><a
				href="javascript: popupFixedPage(700,950,'../decision/antenatal/antenatalplanner.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>');">Planner</a>
			</td>
			<%
  }
%>
		</tr>
	</table>


</html:form>
</body>
</html:html>
