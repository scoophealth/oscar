<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

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

<%@ page import="oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="org.oscarehr.util.LoggedInInfo" %>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Palliative Care</title>
<link rel="stylesheet" type="text/css" href="palliativeCareStyles.css" />
<link rel="stylesheet" type="text/css" media="print" href="print.css" />
<html:base />
</head>

<%
	String formClass = "PalliativeCare";
	String formLink = "formpalliativecare.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "PalliativeCare/";
%>

<script type="text/javascript" language="Javascript">

    function onPrint() {
        document.forms[0].submit.value="print";
        var ret = checkAllDates();
        if(ret==true)
        {
            window.print();
        }
        return ret;
    }
    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true)
        {
            ret = confirm("Are you sure you want to save this form?");
        }
        
        return ret;
    }
    
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret == true)
        {
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        return ret;
    }
    function popupPage(varpage) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage, "palliativeCare", windowprops);
        if (popup.opener == null) {
            popup.opener = self;
        }
    }

/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=3100;

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
//            alert('dateString'+dateString);
            return true;
        }
        var dt = dateString.split('/');
        var y = dt[0];
        var m = dt[1];
        var d = dt[2];
        var orderString = m + '/' + d + '/' + y;
        var pass = isDate(orderString);

        if(pass!=true)
        {
            alert('Invalid '+pass+' in field ' + dateBox.name);
            dateBox.focus();
            return false;
        }
    }
    catch (ex)
    {
        alert('Catch Invalid Date in field ' + dateBox.name);
        dateBox.focus();
        return false;
    }
    return true;
}

function checkAllDates()
{
    var b = true;
    if(valDate(document.forms[0].date1)==false){
        b = false;
    }else
    if(valDate(document.forms[0].date2)==false){
        b = false;
    }else
    if(valDate(document.forms[0].date3)==false){
        b = false;
    }else
    if(valDate(document.forms[0].date4)==false){
        b = false;
    }

    return b;
}
</script>




<body bgproperties="fixed" onLoad="javascript:window.focus()"
	topmargin="0" leftmargin="0" rightmargin="0">
<html:form action="/form/formname">


	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="ID"
		value="<%= props.getProperty("ID", "0") %>" />
	<input type="hidden" name="provider_no"
		value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="provNo"
		value="<%= request.getParameter("provNo") %>" />
	<input type="hidden" name="submit" value="exit" />

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left"><input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();" />
			<input type="button" value="Print"
				onclick="javascript:return onPrint();" /></td>
		</tr>
	</table>

	<table width="100%">
		<tr>
			<td class="title" colspan="2">Palliative Care<br>
			Patient Care Flowsheet</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td width="50%" align="center">Patient Name: <input
				type="hidden" name="pName"
				value="<%=props.getProperty("pName", "")%>" /> <%=props.getProperty("pName", "")%>
			</td>
			<td width="50%" align="center">Diagnosis: <input type="text"
				name="diagnosis" size="40"
				value="<%=props.getProperty("diagnosis", "")%>" /><br>
			<br>
			</td>
		</tr>
		<tr>
			<td class="format" colspan="2">
			<table border="1" cellspacing="0" width="100%">
				<tr class="date">
					<td width="12%"><b>DATE</b></td>
					<td width="22%" align="right">(yyyy/mm/dd) <input type="text"
						name="date1" value="<%=props.getProperty("date1", "")%>" /></td>
					<td width="22%" align="right">(yyyy/mm/dd) <input type="text"
						name="date2" value="<%=props.getProperty("date2", "")%>" /></td>
					<td width="22%" align="right">(yyyy/mm/dd) <input type="text"
						name="date3" value="<%=props.getProperty("date3", "")%>" /></td>
					<td width="22%" align="right">(yyyy/mm/dd) <input type="text"
						name="date4" value="<%=props.getProperty("date4", "")%>" /></td>
				</tr>
				<tr class="pain">
					<td><b><a
						href="javascript: popupPage('<%=resource%>pain');">PAIN</a></b></td>
					<td><textarea name="pain1"><%=props.getProperty("pain1", "")%></textarea></td>
					<td><textarea name="pain2"><%=props.getProperty("pain2", "")%></textarea></td>
					<td><textarea name="pain3"><%=props.getProperty("pain3", "")%></textarea></td>
					<td><textarea name="pain4"><%=props.getProperty("pain4", "")%></textarea></td>
				</tr>
				<tr class="giBowels">
					<td><b><a href="javascript: popupPage('<%=resource%>gi');">GI:</a></b><br>
					Bowels<br>
					-diarrhea -constipation</td>
					<td><textarea name="giBowels1"><%=props.getProperty("giBowels1", "")%></textarea></td>
					<td><textarea name="giBowels2"><%=props.getProperty("giBowels2", "")%></textarea></td>
					<td><textarea name="giBowels3"><%=props.getProperty("giBowels3", "")%></textarea></td>
					<td><textarea name="giBowels4"><%=props.getProperty("giBowels4", "")%></textarea></td>
				</tr>
				<tr class="giNausea">
					<td>Nausea & Vomiting</td>
					<td><textarea name="giNausea1"><%=props.getProperty("giNausea1", "")%></textarea></td>
					<td><textarea name="giNausea2"><%=props.getProperty("giNausea2", "")%></textarea></td>
					<td><textarea name="giNausea3"><%=props.getProperty("giNausea3", "")%></textarea></td>
					<td><textarea name="giNausea4"><%=props.getProperty("giNausea4", "")%></textarea></td>
				</tr>
				<tr class="giDysphagia">
					<td>Dysphagia</td>
					<td><textarea name="giDysphagia1"><%=props.getProperty("giDysphagia1", "")%></textarea></td>
					<td><textarea name="giDysphagia2"><%=props.getProperty("giDysphagia2", "")%></textarea></td>
					<td><textarea name="giDysphagia3"><%=props.getProperty("giDysphagia3", "")%></textarea></td>
					<td><textarea name="giDysphagia4"><%=props.getProperty("giDysphagia4", "")%></textarea></td>
				</tr>
				<tr class="giHiccups">
					<td>Hiccups</td>
					<td><textarea name="giHiccups1"><%=props.getProperty("giHiccups1", "")%></textarea></td>
					<td><textarea name="giHiccups2"><%=props.getProperty("giHiccups2", "")%></textarea></td>
					<td><textarea name="giHiccups3"><%=props.getProperty("giHiccups3", "")%></textarea></td>
					<td><textarea name="giHiccups4"><%=props.getProperty("giHiccups4", "")%></textarea></td>
				</tr>
				<tr class="giMouth">
					<td>Mouth problems</td>
					<td><textarea name="giMouth1"><%=props.getProperty("giMouth1", "")%></textarea></td>
					<td><textarea name="giMouth2"><%=props.getProperty("giMouth2", "")%></textarea></td>
					<td><textarea name="giMouth3"><%=props.getProperty("giMouth3", "")%></textarea></td>
					<td><textarea name="giMouth4"><%=props.getProperty("giMouth4", "")%></textarea></td>
				</tr>
				<tr class="gu">
					<td><b><a href="javascript: popupPage('<%=resource%>gu');">GU:</a></b><br>
					Retention<br>
					Incontinence</td>
					<td><textarea name="gu1"><%=props.getProperty("gu1", "")%></textarea></td>
					<td><textarea name="gu2"><%=props.getProperty("gu2", "")%></textarea></td>
					<td><textarea name="gu3"><%=props.getProperty("gu3", "")%></textarea></td>
					<td><textarea name="gu4"><%=props.getProperty("gu4", "")%></textarea></td>
				</tr>
				<tr class="skinUlcers">
					<td><b><a
						href="javascript: popupPage('<%=resource%>skin');">SKIN:</a></b><br>
					Ulcers</td>
					<td><textarea name="skinUlcers1"><%=props.getProperty("skinUlcers1", "")%></textarea></td>
					<td><textarea name="skinUlcers2"><%=props.getProperty("skinUlcers2", "")%></textarea></td>
					<td><textarea name="skinUlcers3"><%=props.getProperty("skinUlcers3", "")%></textarea></td>
					<td><textarea name="skinUlcers4"><%=props.getProperty("skinUlcers4", "")%></textarea></td>
				</tr>
				<tr class="skinPruritis">
					<td>Pruritis</td>
					<td><textarea name="skinPruritis1"><%=props.getProperty("skinPruritis1", "")%></textarea></td>
					<td><textarea name="skinPruritis2"><%=props.getProperty("skinPruritis2", "")%></textarea></td>
					<td><textarea name="skinPruritis3"><%=props.getProperty("skinPruritis3", "")%></textarea></td>
					<td><textarea name="skinPruritis4"><%=props.getProperty("skinPruritis4", "")%></textarea></td>
				</tr>
				<tr class="psychAgitation">
					<td><b><a
						href="javascript: popupPage('<%=resource%>psych');">PSYCH:</a></b><br>
					Agitation<br>
					Myoclonus</td>
					<td><textarea name="psychAgitation1"><%=props.getProperty("psychAgitation1", "")%></textarea></td>
					<td><textarea name="psychAgitation2"><%=props.getProperty("psychAgitation2", "")%></textarea></td>
					<td><textarea name="psychAgitation3"><%=props.getProperty("psychAgitation3", "")%></textarea></td>
					<td><textarea name="psychAgitation4"><%=props.getProperty("psychAgitation4", "")%></textarea></td>
				</tr>
				<tr class="psychAnorexia">
					<td>Anorexia</td>
					<td><textarea name="psychAnorexia1"><%=props.getProperty("psychAnorexia1", "")%></textarea></td>
					<td><textarea name="psychAnorexia2"><%=props.getProperty("psychAnorexia2", "")%></textarea></td>
					<td><textarea name="psychAnorexia3"><%=props.getProperty("psychAnorexia3", "")%></textarea></td>
					<td><textarea name="psychAnorexia4"><%=props.getProperty("psychAnorexia4", "")%></textarea></td>
				</tr>
				<tr class="psychAnxiety">
					<td>Anxiety</td>
					<td><textarea name="psychAnxiety1"><%=props.getProperty("psychAnxiety1", "")%></textarea></td>
					<td><textarea name="psychAnxiety2"><%=props.getProperty("psychAnxiety2", "")%></textarea></td>
					<td><textarea name="psychAnxiety3"><%=props.getProperty("psychAnxiety3", "")%></textarea></td>
					<td><textarea name="psychAnxiety4"><%=props.getProperty("psychAnxiety4", "")%></textarea></td>
				</tr>
				<tr class="psychDepression">
					<td>Depression</td>
					<td><textarea name="psychDepression1"><%=props.getProperty("psychDepression1", "")%></textarea></td>
					<td><textarea name="psychDepression2"><%=props.getProperty("psychDepression2", "")%></textarea></td>
					<td><textarea name="psychDepression3"><%=props.getProperty("psychDepression3", "")%></textarea></td>
					<td><textarea name="psychDepression4"><%=props.getProperty("psychDepression4", "")%></textarea></td>
				</tr>
				<tr class="psychFatigue">
					<td>Fatigue</td>
					<td><textarea name="psychFatigue1"><%=props.getProperty("psychFatigue1", "")%></textarea></td>
					<td><textarea name="psychFatigue2"><%=props.getProperty("psychFatigue2", "")%></textarea></td>
					<td><textarea name="psychFatigue3"><%=props.getProperty("psychFatigue3", "")%></textarea></td>
					<td><textarea name="psychFatigue4"><%=props.getProperty("psychFatigue4", "")%></textarea></td>
				</tr>
				<tr class="psychSomnolence">
					<td>Somnolence</td>
					<td><textarea name="psychSomnolence1"><%=props.getProperty("psychSomnolence1", "")%></textarea></td>
					<td><textarea name="psychSomnolence2"><%=props.getProperty("psychSomnolence2", "")%></textarea></td>
					<td><textarea name="psychSomnolence3"><%=props.getProperty("psychSomnolence3", "")%></textarea></td>
					<td><textarea name="psychSomnolence4"><%=props.getProperty("psychSomnolence4", "")%></textarea></td>
				</tr>
				<tr class="respCough">
					<td><b><a
						href="javascript: popupPage('<%=resource%>resp');">RESP:</a></b><br>
					Cough</td>
					<td><textarea name="respCough1"><%=props.getProperty("respCough1", "")%></textarea></td>
					<td><textarea name="respCough2"><%=props.getProperty("respCough2", "")%></textarea></td>
					<td><textarea name="respCough3"><%=props.getProperty("respCough3", "")%></textarea></td>
					<td><textarea name="respCough4"><%=props.getProperty("respCough4", "")%></textarea></td>
				</tr>
				<tr class="respDyspnea">
					<td>Dyspnea</td>
					<td><textarea name="respDyspnea1"><%=props.getProperty("respDyspnea1", "")%></textarea></td>
					<td><textarea name="respDyspnea2"><%=props.getProperty("respDyspnea2", "")%></textarea></td>
					<td><textarea name="respDyspnea3"><%=props.getProperty("respDyspnea3", "")%></textarea></td>
					<td><textarea name="respDyspnea4"><%=props.getProperty("respDyspnea4", "")%></textarea></td>
				</tr>
				<tr class="respFever">
					<td>Fever</td>
					<td><textarea name="respFever1"><%=props.getProperty("respFever1", "")%></textarea></td>
					<td><textarea name="respFever2"><%=props.getProperty("respFever2", "")%></textarea></td>
					<td><textarea name="respFever3"><%=props.getProperty("respFever3", "")%></textarea></td>
					<td><textarea name="respFever4"><%=props.getProperty("respFever4", "")%></textarea></td>
				</tr>
				<tr class="respCaregiver">
					<td>Caregiver coping</td>
					<td><textarea name="respCaregiver1"><%=props.getProperty("respCaregiver1", "")%></textarea></td>
					<td><textarea name="respCaregiver2"><%=props.getProperty("respCaregiver2", "")%></textarea></td>
					<td><textarea name="respCaregiver3"><%=props.getProperty("respCaregiver3", "")%></textarea></td>
					<td><textarea name="respCaregiver4"><%=props.getProperty("respCaregiver4", "")%></textarea></td>
				</tr>
				<tr class="other">
					<td><b><a
						href="javascript: popupPage('<%=resource%>other');">Other
					Issues / FU Plan</a></b></td>
					<td><textarea name="other1"><%=props.getProperty("other1", "")%></textarea></td>
					<td><textarea name="other2"><%=props.getProperty("other2", "")%></textarea></td>
					<td><textarea name="other3"><%=props.getProperty("other3", "")%></textarea></td>
					<td><textarea name="other4"><%=props.getProperty("other4", "")%></textarea></td>
				</tr>
				<tr class="signature">
					<td>Signature</td>
					<td><input type="text" name="signature1"
						value="<%=props.getProperty("signature1", "")%>" /></td>
					<td><input type="text" name="signature2"
						value="<%=props.getProperty("signature2", "")%>" /></td>
					<td><input type="text" name="signature3"
						value="<%=props.getProperty("signature3", "")%>" /></td>
					<td><input type="text" name="signature4"
						value="<%=props.getProperty("signature4", "")%>" /></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left"><input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();" />
			<input type="button" value="Print"
				onclick="javascript:return onPrint();" /></td>
		</tr>
	</table>

</html:form>
</body>
</html:html>
