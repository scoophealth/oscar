<%--

    Copyright (c) 2007 Peter Hutten-Czapski based on OSCAR general requirements
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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.form.*"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page
	import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*,oscar.oscarEncounter.oscarMeasurements.*,oscar.oscarEncounter.oscarMeasurements.bean.*,oscar.oscarEncounter.oscarMeasurements.pageUtil.*"%>


<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Congestive Heart Failure Record</title>

<style type="text/css" media="print">
.hideprint {
	display: none;
}

body {
	font-size: 100%;
}

td {
	font-family: Arial, Verdana, Helvetica, sans-serif;
	font-size: 60%;
}

input {
	font-family: Arial, Verdana, Helvetica, sans-serif;
	font-size: 90%;
}
</style>

<style type="text/css" media="screen">
body {
	font-size: 100%;
}

td {
	font-family: Arial, Verdana, Helvetica, sans-serif;
	font-size: 90%;
}

.fine {
	font-family: Arial, Verdana, Helvetica, sans-serif;
	font-size: 80%;
}

input {
	border: 2px solid #ccc;
}

input:focus {
	border: 2px solid #000;
}
</style>

<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />

<!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>

<!-- language for the calendar -->
<script type="text/javascript"
	src="../share/calendar/lang/calendar-en.js"></script>

<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>



<%
	String formClass = "chf";
	String formLink = "formchf.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);
%>

<script type="text/javascript" language="Javascript">
var temp;
temp = "";
    function onSave() {
        if (temp != "") { document.forms[0].action = temp; }
        document.forms[0].target="_self";        
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        return ret;
    }
    
    function onSaveExit() {
        if (temp != "") { document.forms[0].action = temp; }
        document.forms[0].target="_self";
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret == true)
        {
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        return ret;
    }
    function onPrint() {
        window.print();
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
    
	function isNumber(ss){
		var s = ss.value;
        var i;
        for (i = 0; i < s.length; i++){
            // Check that current character is number.
            var c = s.charAt(i);
			if (c == '.') {
				continue;
			} else if (((c < "0") || (c > "9"))) {
                        alert('Invalid '+s+' in field ' + ss.name + "\nPlease only use numbers");
                        ss.focus();
                        return false;
			}
        }
        // All characters are numbers.
        return true;
        }

/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var digital= new Date();
var year=digital.getFullYear();
var minYear=(year -20);
var maxYear=year;

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
                alert('Invalid '+pass+' in field ' + dateBox.name+ '\n Try YYYY/MM/DD');
                dateBox.focus();   // needed to have IE recognise the select
                dateBox.blur();    // needed to have IE recognise the select
                dateBox.select();  // select the text at the wrong dates
                return false;
            }
        }
        catch (ex)
        {
            alert('Catch Invalid Date in field ' + dateBox.name + '\n Try YYYY/MM/DD');
            dateBox.focus();
            dateBox.blur();
            dateBox.select();
            return false;
        }
        return true;
    }

    function checkAllDates()
    {
        var b = true;
        if(valDate(document.forms[0].dateFlu)==false){
            b = false;
        }else
        if(valDate(document.forms[0].datePneumo)==false){
            b = false;
        }else
        if(valDate(document.forms[0].dateEcho)==false){
            b = false;
        }else
        if(valDate(document.forms[0].dateCXR)==false){
            b = false;
        }else
        if(valDate(document.forms[0].dateEKG)==false){
            b = false;
        }else
        if(valDate(document.forms[0].date1)==false){
            b = false;
        }else
        if(valDate(document.forms[0].date2)==false){
            b = false;
        }else
        if(valDate(document.forms[0].date3)==false){
            b = false;
        }

        return b;
    }
function calToday(field) {
	var calDate=new Date();
	varMonth = calDate.getMonth()+1;
	varMonth = varMonth>9? varMonth : ("0"+varMonth);
	varDate = calDate.getDate()>9? calDate.getDate(): ("0"+calDate.getDate());
	field.value = calDate.getFullYear() + '/' + (varMonth) + '/' + varDate;
}

// the above is more or less boilerplate for all forms mildly tweaked
//  functions specifically added for this form follow

function wtEnglish2Metric(obj,obj2) {
	//convert from lb at obj to kg at obj2
	if(isNumber(obj) && obj.value>5) {
		weight = obj.value;
		weightM = Math.round(weight * 10 * 0.4536) / 10 ;  // Round to the nearest 0.1 Kg
		obj2.value = weightM;
	}else {
	// could alert the user but its only important if the kg wt is incorrect 
        // as the imperial weight is not saved 
	}
}

function checkIt(obj,pattern,message) {
// checks input against passed pattern, if matches returns true
// if not then expresses alert and focuses back on the object	
	var testRegEx= new RegExp(pattern);
	//var teste=obj.value
	//alert("pattern="+pattern+"RegEx="+testRegEx+"obj.value="+obj.value+"test="+testRegEx.test(teste));
	if ((testRegEx.test(obj.value)) !=true && obj.value !="") {
		alert(message);
		obj.focus();
		return false;
	}
	return true;
}

function setNYHA(num) {
// logic to set the NYHA class based on symtoms clicked for level num
	var maxSymptom=-1;  
	var symptom = new Array(6)
	symptom[0]="fatigue"+num;
	symptom[1]="dizzy"+num;
	symptom[2]="SOBOE"+num;
	symptom[3]="SOBresting"+num;
	symptom[4]="orthopnea"+num;
	symptom[5]="PND"+num;
	var NYHA = new Array(5)
	NYHA[0]=""	// I know I know but this makes the iterator= NYHA class level
	NYHA[1]="NYHAI"+num
	NYHA[2]="NYHAII"+num
	NYHA[3]="NYHAIII"+num
	NYHA[4]="NYHAIV"+num

	for (i=0; i<6; i++){
		if (document.forms[0].elements[symptom[i]].checked){  
		maxSymptom=i		
		}
	}
	for (i=1; i<5; i++) { 
		document.forms[0].elements[NYHA[i]].checked=false; 	//clear all NYHA checkboxes in the series
	}
	switch(maxSymptom) {
		case -1:
			document.forms[0].elements[NYHA[1]].checked=true; // No symptoms
			break;
		case 0:
		case 1:
			document.forms[0].elements[NYHA[2]].checked=true;
			break;
		case 2:
			document.forms[0].elements[NYHA[3]].checked=true;
			break;
		default:
			document.forms[0].elements[NYHA[4]].checked=true;  // Cases 3,4,5			
	}
	return;
}

function setTarget(drugClass, num) {
// logic to set the target flag for a drugClass for level num 
// based on the value of the selection
	if (document.forms[0].elements[drugClass+"sel"+num].selectedIndex == 0) {
		return;
	}
	document.forms[0].elements[drugClass+num].value = document.forms[0].elements[drugClass+"sel"+num].options[document.forms[0].elements[drugClass+"sel"+num].selectedIndex].text;	
	if (document.forms[0].elements[drugClass+"sel"+num].options[document.forms[0].elements[drugClass+"sel"+num].selectedIndex].value=='Target') { 
		document.forms[0].elements[drugClass+"targetY"+num].checked=true; 
		document.forms[0].elements[drugClass+"targetN"+num].checked=false; 
	} else {
		document.forms[0].elements[drugClass+"targetY"+num].checked=false; 
		document.forms[0].elements[drugClass+"targetN"+num].checked=true; 
	}		

	document.forms[0].elements[drugClass+"sel"+num].value="";

	return;
}


function geteGFR(cr){
// Function that takes the passes creatinine in umol/l and returns eGFR in ml/min
// Revised MDRD equation for all races except black where the number is 1.23x higher:
// eGFR = 186 x (creat/88.4)E-1.154 x ageE-0.203 x F
// Ontario clinical chemists use 186 as the mulitplier, others use 175
	var today = new Date();
	var year=today.getFullYear();
	var age=year-document.forms[0].birthDate.value.split('/',1);
	if (age<18 | age>85) { 
		//alert("Age needs to be between 18 and 85 for the MDRD equation to be valid");
		return "";
	} 
	var female_sex=false;
	if (document.forms[0].sex.value="F") {
		female_sex=true;
	}
	var eGFR= Math.round(Math.min((186.0  
              * Math.pow((cr/88.4),-1.154)
              * Math.pow(age, -0.203)	
              * (female_sex ? 0.742 : 1.0)),90)); // changes in values over (60) 90 120 are not clinically significant
    return eGFR;
}
</script>

<html:base />
</head>

<body bgproperties="fixed" class="chfform" bgcolor="#EEEEFF"
	onLoad="javascript:window.focus()" topmargin="0" leftmargin="0"
	rightmargin="0">
<html:form action="/form/formname">


	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="ID"
		value="<%= props.getProperty("ID", "0") %>" />
	<input type="hidden" name="provider_no" value=<%=""+provNo%> />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="sex"
		value="<%= props.getProperty("sex", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />


	<table class="hideprint" bgcolor="#778899" width=100%>
		<!-- hideprint class is defined in our CSS not to display on print, color is slate grey -->
		<tr>
			<td align="left"><input type="hidden" name="submit" value="exit" />
			<input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();" />
			<input type="button" value="Print"
				onclick="javascript:return onPrint();" /></td>
			<td><a href="javascript: popupFixedPage(700,960,'chf.html');">Tips</a>&nbsp;&nbsp;

			<a
				href="javascript: popPage('formlabreq07.jsp?demographic_no=<%=demoNo%>&formId=0&provNo=<%=provNo%>&labType=CHF','LabReq');">LAB</a>

			</td>
		</tr>
	</table>

	<div align="left">
	<table width="0" border="2" cellspacing="0">
		<tr valign="top">
			<td width="100%" colspan="6">
			<table border="0">
				<tr valign="top">
					<td width="33%">Patient Name: <input type="hidden"
						name="pName" value="<%= props.getProperty("pName", "") %>" /><%= props.getProperty("pName", "") %>
					<br>
					DOB: <input type="hidden" name="birthDate"
						value="<%= props.getProperty("birthDate", "0") %>" /><%= props.getProperty("birthDate", "0") %>
					</td>
					<td width="33%">Diagnosis: <input tabindex="1" type="checkbox"
						name="SHFDx" id="SHFDx" <%= props.getProperty("SHFDx", "") %>><label
						for="SHFDx"> Systolic Heart Failure</label><BR>
					(LVEF&lt;40% - consider ICD if &lt;30%)</td>
					<td width="33%"><input tabindex="2" type="checkbox"
						name="PSFDx" id="PSFDx" <%= props.getProperty("PSFDx", "") %>><label
						for="PSFDx"> Heart Failure with Preserved Systolic
					Function (PSF)</label></td>
				</tr>
			</table>

			Annual Influenza Vaccine: <input tabindex="3" type="text"
				class="CHFInput" name="dateFlu" id="dateFlu"
				onchange="checkIt(this,'^20\\d{2}\/\\d{2}\/\\d{2}$','The immunisation date should be in the format 20YY/MM/DD');"
				size="10" value="<%= props.getProperty("dateFlu", "") %>" /> <img
				src="../images/cal.gif" class="hideprint" id="dateFlu_cal">

			Pneumococcal Vaccine: <input tabindex="4" type="text"
				class="CHFInput" name="datePneumo" id="datePneumo"
				onchange="checkIt(this,'^\\d{4}\/\\d{2}\/\\d{2}$','The immunisation date should be in the format YYYY/MM/DD');"
				size="10" value="<%= props.getProperty("datePneumo", "") %>" /> <img
				src="../images/cal.gif" class="hideprint" id="datePneumo_cal">
			<span class="fine"> (A single re-immunization is appropriate
			after 5 years.)</span></td>
		</tr>


		<tr valign="top">
			<td width="100%" bgcolor="#F3F3F3" colspan="6"><b>Initial
			Investigations</b></td>
		</tr>
		<tr valign="top">
			<td colspan="6">
			<table border="0">
				<tr valign="top">
					<td width="47%"><u>Laboratory Testing to Assist Diagnosis:</u>
					<br>
					Echocardiography: <input tabindex="5" type="text" class="CHFInput"
						name="dateEcho" id="dateEcho"
						onchange="checkIt(this,'^(19|20)\\d{2}\/\\d{2}\/\\d{2}$','The echocardiography date should be in the format YYYY/MM/DD');"
						size="10" value="<%= props.getProperty("dateEcho", "") %>" /> <img
						src="../images/cal.gif" class="hideprint" id="dateEcho_cal"><br>
					<span class="fine">consider echo within first yr of
					diagnosis</span> <br>
					Chest radiograph: <input tabindex="6" type="text" class="CHFInput"
						name="dateCXR" id="dateCXR"
						onchange="checkIt(this,'^(19|20)\\d{2}\/\\d{2}\/\\d{2}$','The radiography date should be in the format YYYY/MM/DD');"
						size="10" value="<%= props.getProperty("dateCXR", "") %>" /> <img
						src="../images/cal.gif" class="hideprint" id="dateCXR_cal">
					<br>
					<input tabindex="7" type="checkbox" name="CXRedema" id="CXRedema"
						<%= props.getProperty("CXRedema", "") %>> <label
						for="CXRedema"> interstitial edema </label> <input tabindex="8"
						type="checkbox" name="CXRcardiomegaly" id="CXRcardiomegaly"
						<%= props.getProperty("CXRcardiomegaly", "") %>> <label
						for="CXRcardiomegaly"> cardiomegaly </label> <input tabindex="9"
						type="checkbox" name="CXReffusion" id="CXReffusion"
						<%= props.getProperty("CXReffusion", "") %>> <label
						for="CXReffusion"> pleural effusion</label> Electrocardiogram: <input
						tabindex="10" type="text" class="CHFInput" name="dateEKG"
						id="dateEKG"
						onchange="checkIt(this,'^(19|20)\\d{2}\/\\d{2}\/\\d{2}$','The electrocardiography date should be in the format YYYY/MM/DD');"
						size="10" value="<%= props.getProperty("dateEKG", "") %>" /> <img
						src="../images/cal.gif" class="hideprint" id="dateEKG_cal">
					</td>
					<td width="6%">&nbsp;</td>
					<td width="47%"><u>Laboratory Testing to Identify Systemic
					Disorders:</u> <br>
					<i>If diagnostic suspicion is high consider:</i> <br>
					<input tabindex="11" type="checkbox" name="CBC" id="CBC"
						<%= props.getProperty("CBC", "") %>><label for="CBC">
					CBC </label> <input tabindex="12" type="checkbox" name="lytes" id="lytes"
						<%= props.getProperty("lytes", "") %>><label for="lytes">
					electrolytes </label> <input tabindex="13" type="checkbox" name="creat"
						id="creat" <%= props.getProperty("creat", "") %>><label
						for="creat"> renal function </label> <input tabindex="14"
						type="checkbox" name="ua" id="ua"
						<%= props.getProperty("ua", "") %>><label for="ua">
					urinalysis </label> <br>
					<input tabindex="15" type="checkbox" name="RBS" id="RBS"
						<%= props.getProperty("RBS", "") %>><label for="RBS">
					glucose </label> <input tabindex="16" type="checkbox" name="lipids"
						id="lipids" <%= props.getProperty("lipids", "") %>><label
						for="lipids"> lipids </label> <input tabindex="17" type="checkbox"
						name="LFT" id="LFT" <%= props.getProperty("LFT", "") %>><label
						for="LFT"> liver enzymes </label> <input tabindex="18"
						type="checkbox" name="TSH" id="TSH"
						<%= props.getProperty("TSH", "") %>><label for="TSH">
					thyroid </label></td>
				</tr>
			</table>
			</td>
		</tr>


		<tr valign="top">
			<td width="26%" bgcolor="#D9D9D9" colspan="3"><b>Required
			Elements of Care</b></td>
			<td width="23%" bgcolor="#D9D9D9">Date:<input tabindex="19"
				type="text" class="CHFInput" name="date1" id="date1" size="10"
				value="<%= props.getProperty("date1", "") %>" /> <img
				src="../images/cal.gif" class="hideprint" id="date1_cal"></td>
			<td width="24%" bgcolor="#D9D9D9">Date:<input tabindex="119"
				type="text" class="CHFInput" name="date2" id="date2" size="10"
				value="<%= props.getProperty("date2", "") %>" /> <img
				src="../images/cal.gif" class="hideprint" id="date2_cal"></td>
			<td width="25%" bgcolor="#D9D9D9">Date:<input tabindex="219"
				type="text" class="CHFInput" name="date3" id="date3" size="10"
				value="<%= props.getProperty("date3", "") %>" /> <img
				src="../images/cal.gif" class="hideprint" id="date3_cal"></td>
		</tr>
		<script type="text/javascript" language="Javascript">
// alert(getLastDateRecordedInMonths("BP"));
</script>
		<tr valign="top">
			<td width="5%" bgcolor="#E6E6E6" rowspan="8"><b>Physical
			Examination *</b></td>
			<td width="21%" colspan="2">Weight</td>
			<td width="23%"><span class="hideprint"><input
				tabindex="20" type="text" style="background: #eeeeff"
				onchange="wtEnglish2Metric(this,weight1);" size="3" maxlength="5">&nbsp;lb
			&rarr; &nbsp;</span> <input tabindex="21" type="text" class="CHFTextarea"
				name="weight1"
				onchange="checkIt(this,'^\\d{2,3}(\\.\\d)?$','Adult weight in kilograms is usually between 40 and 150 (enter just the number)');"
				size="3" maxlength="5"
				value="<%= props.getProperty("weight1", "") %>" />kg</td>
			<td width="24%"><span class="hideprint"><input
				tabindex="120" type="text" style="background: #eeeeff"
				onchange="wtEnglish2Metric(this,weight2);" size="3" maxlength="4">&nbsp;lb
			&rarr; &nbsp;</span> <input tabindex="121" type="text" class="CHFTextarea"
				name="weight2"
				onchange="checkIt(this,'^\\d{2,3}(\\.\\d)?$','Adult weight in kilograms is usually between 40 and 150 (enter just the number)');"
				size="3" maxlength="5"
				value="<%= props.getProperty("weight2", "") %>" />kg</td>
			<td width="25%"><span class="hideprint"><input
				tabindex="220" type="text" style="background: #eeeeff"
				onchange="wtEnglish2Metric(this,weight3);" size="3" maxlength="4">&nbsp;lb
			&rarr; &nbsp;</span> <input tabindex="221" type="text" class="CHFTextarea"
				name="weight3"
				onchange="checkIt(this,'^\\d{2,3}(\\.\\d)?$','Adult weight in kilograms is usually between 40 and 150 (enter just the number)');"
				size="3" maxlength="5"
				value="<%= props.getProperty("weight3", "") %>" />kg</td>
		</tr>

		<tr valign="top">
			<td width="21%" colspan="2">Symptoms of Heart Failure</td>
			<td width="23%"><input tabindex="22" type="checkbox"
				name="fatigue1" id="fatigue1" onclick="setNYHA('1')"
				<%= props.getProperty("fatigue1", "") %>><label
				for="fatigue1"> Fatigue</label> <br>
			<input tabindex="23" type="checkbox" name="dizzy1" id="dizzy1"
				onclick="setNYHA('1')" <%= props.getProperty("dizzy1", "") %>><label
				for="dizzy1"> Dizziness</label> <br>
			<input tabindex="24" type="checkbox" name="SOBOE1" id="SOBOE1"
				onclick="setNYHA('1')" <%= props.getProperty("SOBOE1", "") %>><label
				for="SOBOE1"> Dyspnea on exertion</label> <br>
			<input tabindex="25" type="checkbox" name="SOBresting1"
				id=SOBresting1 " onclick="setNYHA('1')"
				<%= props.getProperty("SOBresting1", "") %>><label
				for="SOBresting1"> Dyspnea at rest</label> <br>
			<input tabindex="26" type="checkbox" name="orthopnea1"
				id="orthopnea1" onclick="setNYHA('1')"
				<%= props.getProperty("orthopnea1", "") %>><label
				for="orthopnea1"> Orthopnea</label> <br>
			<input tabindex="27" type="checkbox" name="PND1" id="PND1"
				onclick="setNYHA('1')" <%= props.getProperty("PND1", "") %>><label
				for="PND1"> PND</label></td>

			<td width="24%"><input tabindex="122" type="checkbox"
				name="fatigue2" id="fatigue2" onclick="setNYHA('2')"
				<%= props.getProperty("fatigue2", "") %>><label
				for="fatigue2"> Fatigue</label> <br>
			<input tabindex="123" type="checkbox" name="dizzy2" id="dizzy2"
				onclick="setNYHA('2')" <%= props.getProperty("dizzy2", "") %>><label
				for="dizzy2"> Dizziness</label> <br>
			<input tabindex="124" type="checkbox" name="SOBOE2" id="SOBOE2"
				onclick="setNYHA('2')" <%= props.getProperty("SOBOE2", "") %>><label
				for="SOBOE2"> Dyspnea on exertion</label> <br>
			<input tabindex="125" type="checkbox" name="SOBresting2"
				id=SOBresting2 " onclick="setNYHA('2')"
				<%= props.getProperty("SOBresting2", "") %>><label
				for="SOBresting2"> Dyspnea at rest</label> <br>
			<input tabindex="126" type="checkbox" name="orthopnea2"
				id="orthopnea2" onclick="setNYHA('2')"
				<%= props.getProperty("orthopnea2", "") %>><label
				for="orthopnea2"> Orthopnea</label> <br>
			<input tabindex="127" type="checkbox" name="PND2" id="PND2"
				onclick="setNYHA('2')" <%= props.getProperty("PND2", "") %>><label
				for="PND2"> PND</label></td>

			<td width="25%"><input tabindex="222" type="checkbox"
				name="fatigue3" id="fatigue3" onclick="setNYHA('3')"
				<%= props.getProperty("fatigue3", "") %>><label
				for="fatigue3"> Fatigue</label> <br>
			<input tabindex="223" type="checkbox" name="dizzy3" id="dizzy3"
				onclick="setNYHA('3')" <%= props.getProperty("dizzy3", "") %>><label
				for="dizzy3"> Dizziness</label> <br>
			<input tabindex="224" type="checkbox" name="SOBOE3" id="SOBOE3"
				onclick="setNYHA('3')" <%= props.getProperty("SOBOE3", "") %>><label
				for="SOBOE3"> Dyspnea on exertion</label> <br>
			<input tabindex="225" type="checkbox" name="SOBresting3"
				id=SOBresting3 " onclick="setNYHA('3')"
				<%= props.getProperty("SOBresting3", "") %>><label
				for="SOBresting3"> Dyspnea at rest</label> <br>
			<input tabindex="226" type="checkbox" name="orthopnea3"
				id="orthopnea3" onclick="setNYHA('3')"
				<%= props.getProperty("orthopnea3", "") %>><label
				for="orthopnea3"> Orthopnea</label> <br>
			<input tabindex="227" type="checkbox" name="PND3" id="PND3"
				onclick="setNYHA('3')" <%= props.getProperty("PND3", "") %>><label
				for="PND3"> PND</label></td>

		</tr>

		<tr valign="top">
			<td width="21%" colspan="2">NYHA Functional Capacity
			Classification &sigma;</td>
			<td width="23%"><input tabindex="28" type="checkbox"
				name="NYHAI1" id="NYHAI1"
				onclick="NYHAII1.checked=false;NYHAIII1.checked=false;NYHAIV1.checked=false;"
				<%= props.getProperty("NYHAI1", "") %>><label for="NYHAI1">
			Class I </label> <input tabindex="29" type="checkbox" name="NYHAII1"
				id="NYHAII1"
				onclick="NYHAI1.checked=false;NYHAIII1.checked=false;NYHAIV1.checked=false;"
				<%= props.getProperty("NYHAII1", "") %>><label for="NYHAII1">
			Class II </label> <br>
			<input tabindex="30" type="checkbox" name="NYHAIII1" id="NYHAIII1"
				onclick="NYHAI1.checked=false;NYHAII1.checked=false;NYHAIV1.checked=false;"
				<%= props.getProperty("NYHAIII1", "") %>><label
				for="NYHAIII1"> Class III </label> <input tabindex="31"
				type="checkbox" name="NYHAIV1" id="NYHAIV1"
				onclick="NYHAI1.checked=false;NYHAII1.checked=false;NYHAIII1.checked=false;"
				<%= props.getProperty("NYHAIV1", "") %>><label for="NYHAIV1">
			Class IV </label></td>
			<td width="24%"><input tabindex="128" type="checkbox"
				name="NYHAI2" id="NYHAI2"
				onclick="NYHAII2.checked=false;NYHAIII2.checked=false;NYHAIV2.checked=false;"
				<%= props.getProperty("NYHAI2", "") %>><label for="NYHAI2">
			Class I </label> <input tabindex="129" type="checkbox" name="NYHAII2"
				id="NYHAII2"
				onclick="NYHAI2.checked=false;NYHAIII2.checked=false;NYHAIV2.checked=false;"
				<%= props.getProperty("NYHAII2", "") %>><label for="NYHAII2">
			Class II </label> <br>
			<input tabindex="130" type="checkbox" name="NYHAIII2" id="NYHAIII2"
				onclick="NYHAI2.checked=false;NYHAII2.checked=false;NYHAIV2.checked=false;"
				<%= props.getProperty("NYHAIII2", "") %>><label
				for="NYHAIII2"> Class III </label> <input tabindex="131"
				type="checkbox" name="NYHAIV2" id="NYHAIV2"
				onclick="NYHAI2.checked=false;NYHAII2.checked=false;NYHAIII2.checked=false;"
				<%= props.getProperty("NYHAIV2", "") %>><label for="NYHAIV2">
			Class IV </label></td>

			<td width="25%"><input tabindex="228" type="checkbox"
				name="NYHAI3" id="NYHAI3"
				onclick="NYHAII3.checked=false;NYHAIII3.checked=false;NYHAIV3.checked=false;"
				<%= props.getProperty("NYHAI3", "") %>><label for="NYHAI3">
			Class I </label> <input tabindex="229" type="checkbox" name="NYHAII3"
				id="NYHAII3"
				onclick="NYHAI3.checked=false;NYHAIII3.checked=false;NYHAIV3.checked=false;"
				<%= props.getProperty("NYHAII3", "") %>><label for="NYHAII3">
			Class II </label> <br>
			<input tabindex="230" type="checkbox" name="NYHAIII3" id="NYHAIII3"
				onclick="NYHAI3.checked=false;NYHAII3.checked=false;NYHAIV3.checked=false;"
				<%= props.getProperty("NYHAIII3", "") %>><label
				for="NYHAIII3"> Class III </label> <input tabindex="231"
				type="checkbox" name="NYHAIV3" id="NYHAIV3"
				onclick="NYHAI3.checked=false;NYHAII3.checked=false;NYHAIII3.checked=false;"
				<%= props.getProperty("NYHAIV3", "") %>><label for="NYHAIV3">
			Class IV </label></td>

		</tr>

		<tr valign="top">
			<td width="21%" colspan="2">Blood Pressure</td>
			<td width="23%"><input tabindex="41" type="text"
				class="CHFTextarea" name="BP1"
				onchange="checkIt(this,'^\\d{2,3}\/\\d{2,3}$','BPs should be numbers in the format NNN/NN');"
				maxlength="7" size="6" value="<%= props.getProperty("BP1", "") %>" />
			mmHg</td>
			<td width="24%"><input tabindex="141" type="text"
				class="CHFTextarea" name="BP2"
				onchange="checkIt(this,'^\\d{2,3}\/\\d{2,3}$','BPs should be numbers in the format NNN/NN');"
				maxlength="7" size="6" value="<%= props.getProperty("BP2", "") %>" />
			mmHg</td>
			<td width="25%"><input tabindex="241" type="text"
				class="CHFTextarea" name="BP3"
				onchange="checkIt(this,'^\\d{2,3}\/\\d{2,3}$','BPs should be numbers in the format NNN/NN');"
				maxlength="7" size="6" value="<%= props.getProperty("BP3", "") %>" />
			mmHg</td>
		</tr>

		<tr valign="top">
			<td width="21%" colspan="2">JVP Elevation</td>
			<td width="23%"><!--	// onClick="JVPno1.checked=!JVPyes1.checked;" mimics radiobutton behavior, but perhaps the click is in error?
	// and the JVP was not assessed and the user wants to leave this blank?  
	// perhaps better code is onClick="JVPno1.checked=false;"  --> <input
				tabindex="42" type="checkbox" name="JVPyes1" id="JVPyes1"
				onClick="JVPno1.checked=!JVPyes1.checked;"
				<%= props.getProperty("JVPyes1", "") %>><label for="JVPyes1">
			yes </label> <input tabindex="43" type="checkbox" name="JVPno1" id="JVPno1"
				onClick="JVPyes1.checked=!JVPno1.checked;"
				<%= props.getProperty("JVPno1", "") %>><label for="JVPno1">
			no</label></td>
			<td width="24%"><input tabindex="142" type="checkbox"
				name="JVPyes2" id="JVPyes2"
				onClick="JVPno2.checked=!JVPyes2.checked;"
				<%= props.getProperty("JVPyes2", "") %>><label for="JVPyes2">
			yes </label> <input tabindex="143" type="checkbox" name="JVPno2" id="JVPno2"
				onClick="JVPyes2.checked=!JVPno2.checked;"
				<%= props.getProperty("JVPno2", "") %>><label for="JVPno2">
			no</label></td>
			<td width="25%"><input tabindex="242" type="checkbox"
				name="JVPyes3" id="JVPyes3"
				onClick="JVPno3.checked=!JVPyes3.checked;"
				<%= props.getProperty("JVPyes3", "") %>><label for="JVPyes3">
			yes </label> <input tabindex="243" type="checkbox" name="JVPno3" id="JVPno3"
				onClick="JVPyes3.checked=!JVPno3.checked;"
				<%= props.getProperty("JVPno3", "") %>><label for="JVPno3">
			no</label></td>
		</tr>

		<tr valign="top">
			<td width="21%" colspan="2">Pitting Edema</td>
			<td width="23%"><input tabindex="44" type="checkbox"
				name="edemayes1" id="edemayes1"
				onClick="edemano1.checked=!edemayes1.checked;"
				<%= props.getProperty("edemayes1", "") %>><label
				for="edemayes1"> yes </label> <input tabindex="45" type="checkbox"
				name="edemano1" id="edemano1"
				onClick="edemayes1.checked=!edemano1.checked;"
				<%= props.getProperty("edemano1", "") %>><label
				for="edemano1"> no</label> <br>
			<input tabindex="46" type="text" class="CHFTextarea" name="edema1"
				maxlength="30" size="30"
				value="<%= props.getProperty("edema1", "") %>" /></td>
			<td width="24%"><input tabindex="144" type="checkbox"
				name="edemayes2" id="edemayes2"
				onClick="edemano2.checked=!edemayes2.checked;"
				<%= props.getProperty("edemayes2", "") %>><label
				for="edemayes2"> yes </label> <input tabindex="145" type="checkbox"
				name="edemano2" id="edemano2"
				onClick="edemayes2.checked=!edemano2.checked;"
				<%= props.getProperty("edemano2", "") %>><label
				for="edemano2"> no</label> <br>
			<input tabindex="146" type="text" class="CHFTextarea" name="edema2"
				maxlength="30" size="30"
				value="<%= props.getProperty("edema2", "") %>" /></td>
			<td width="25%"><input tabindex="244" type="checkbox"
				name="edemayes3" id="edemayes3"
				onClick="edemano3.checked=!edemayes3.checked;"
				<%= props.getProperty("edemayes3", "") %>><label
				for="edemayes3"> yes </label> <input tabindex="245" type="checkbox"
				name="edemano3" id="edemano3"
				onClick="edemayes3.checked=!edemano3.checked;"
				<%= props.getProperty("edemano3", "") %>><label
				for="edemano3"> no</label> <br>
			<input tabindex="246" type="text" class="CHFTextarea" name="edema3"
				maxlength="30" size="30"
				value="<%= props.getProperty("edema3", "") %>" /></td>
		</tr>

		<tr valign="top">
			<td width="21%" colspan="2">Lung Crackles and/or wheezing</td>
			<td width="23%"><input tabindex="51" type="checkbox"
				name="cracklesyes1" id="cracklesyes1"
				onClick="cracklesno1.checked=!cracklesyes1.checked;"
				<%= props.getProperty("cracklesyes1", "") %>><label
				for="cracklesyes1"> yes </label> <input tabindex="52"
				type="checkbox" name="cracklesno1" id="cracklesno1"
				onClick="cracklesyes1.checked=!cracklesno1.checked;"
				<%= props.getProperty("cracklesno1", "") %>><label
				for="cracklesno1"> no</label> <br>
			<input tabindex="53" type="text" class="CHFTextarea" name="crackles1"
				maxlength="30" size="30"
				value="<%= props.getProperty("crackles1", "") %>" /></td>
			<td width="24%"><input tabindex="151" type="checkbox"
				name="cracklesyes2" id="cracklesyes2"
				onClick="cracklesno2.checked=!cracklesyes2.checked;"
				<%= props.getProperty("cracklesyes2", "") %>><label
				for="cracklesyes2"> yes </label> <input tabindex="152"
				type="checkbox" name="cracklesno2" id="cracklesno2"
				onClick="cracklesyes2.checked=!cracklesno2.checked;"
				<%= props.getProperty("cracklesno2", "") %>><label
				for="cracklesno2"> no</label> <br>
			<input tabindex="153" type="text" class="CHFTextarea"
				name="crackles2" maxlength="30" size="30"
				value="<%= props.getProperty("crackles2", "") %>" /></td>
			<td width="25%"><input tabindex="251" type="checkbox"
				name="cracklesyes3" id="cracklesyes3"
				onClick="cracklesno3.checked=!cracklesyes3.checked;"
				<%= props.getProperty("cracklesyes3", "") %>><label
				for="cracklesyes3"> yes </label> <input tabindex="252"
				type="checkbox" name="cracklesno3" id="cracklesno3"
				onClick="cracklesyes3.checked=!cracklesno3.checked;"
				<%= props.getProperty("cracklesno3", "") %>><label
				for="cracklesno3"> no</label> <br>
			<input tabindex="253" type="text" class="CHFTextarea"
				name="crackles3" maxlength="30" size="30"
				value="<%= props.getProperty("crackles3", "") %>" /></td>
		</tr>

		<tr valign="top">
			<td width="21%" colspan="2">Signs of Pharmacological Intolerance</td>
			<td width="23%"><input tabindex="61" type="text"
				class="CHFTextarea" name="PI1" maxlength="60" size="30"
				value="<%= props.getProperty("PI1", "") %>" /></td>
			<td width="24%"><input tabindex="161" type="text"
				class="CHFTextarea" name="PI2" maxlength="60" size="30"
				value="<%= props.getProperty("PI2", "") %>" /></td>
			<td width="25%"><input tabindex="261" type="text"
				class="CHFTextarea" name="PI3" maxlength="60" size="30"
				value="<%= props.getProperty("PI3", "") %>" /></td>
		</tr>

		<tr valign="top">
			<td width="5%" bgcolor="#E6E6E6" rowspan="4"><b>Lab *</b></td>
			<td width="21%" colspan="2">Na+</td>
			<td width="23%"><input tabindex="62" type="text"
				class="CHFTextarea" name="Na1"
				onchange="checkIt(this,'^\\d{2,3}(\\.\\d)?$','Sodium normal range is 137-145!\nPlease use just numbers in the format NNN');"
				maxlength="3" size="4" value="<%= props.getProperty("Na1", "") %>" />
			mmol/L</td>
			<td width="24%"><input tabindex="162" type="text"
				class="CHFTextarea" name="Na2"
				onchange="checkIt(this,'^\\d{2,3}(\\.\\d)?$','Sodium normal range is 137-145!\nPlease use just numbers in the format NNN');"
				maxlength="3" size="4" value="<%= props.getProperty("Na2", "") %>" />
			mmol/L</td>
			<td width="25%"><input tabindex="262" type="text"
				class="CHFTextarea" name="Na3"
				onchange="checkIt(this,'^\\d{2,3}(\\.\\d)?$','Sodium normal range is 137-145!\nPlease use just numbers in the format NNN');"
				maxlength="3" size="4" value="<%= props.getProperty("Na3", "") %>" />
			mmol/L</td>
		</tr>

		<tr valign="top">
			<td width="21%" colspan="2">K+</td>
			<td width="23%"><input tabindex="63" type="text"
				class="CHFTextarea" name="K1"
				onchange="checkIt(this,'^\\d(\\.\\d)?$','Potassium is usually between 3.5-5.1!\nPlease use just numbers in the format N or N.N');"
				maxlength="3" size="4" value="<%= props.getProperty("K1", "") %>" />
			mmol/L</td>
			<td width="24%"><input tabindex="163" type="text"
				class="CHFTextarea" name="K2"
				onchange="checkIt(this,'^\\d(\\.\\d)?$','Potassium is usually between 3.5-5.1!\nPlease use just numbers in the format N or N.N');"
				maxlength="3" size="4" value="<%= props.getProperty("K2", "") %>" />
			mmol/L</td>
			<td width="25%"><input tabindex="263" type="text"
				class="CHFTextarea" name="K3"
				onchange="checkIt(this,'^\\d(\\.\\d)?$','Potassium is usually between 3.5-5.1!\nPlease use just numbers in the format N or N.N');"
				maxlength="3" size="4" value="<%= props.getProperty("K3", "") %>" />
			mmol/L</td>
		</tr>

		<tr valign="top">
			<td width="21%" colspan="2">Serum Creatinine &uarr; <span
				class="fine"> <br>
			male &lt;114 female &lt;90 umol/L</span></td>

			<td width="23%"><input tabindex="64" type="text"
				class="CHFTextarea" name="creat1"
				onchange="if (checkIt(this,'^\\d{2,3}$','Creatinine is usually between 62-115!\n Please use just numbers in the format NN or NNN')) {eGFR1.value=geteGFR(this.value);};"
				maxlength="3" size="4"
				value="<%= props.getProperty("creat1", "") %>" /> mmol/L</td>
			<td width="24%"><input tabindex="164" type="text"
				class="CHFTextarea" name="creat2"
				onchange="if (checkIt(this,'^\\d{2,3}$','Creatinine is usually between 62-115!\n Please use just numbers in the format NN or NNN')) {eGFR2.value=geteGFR(this.value);};"
				maxlength="3" size="4"
				value="<%= props.getProperty("creat2", "") %>" /> mmol/L</td>
			<td width="25%"><input tabindex="264" type="text"
				class="CHFTextarea" name="creat3"
				onchange="if (checkIt(this,'^\\d{2,3}$','Creatinine is usually between 62-115!\n Please use just numbers in the format NN or NNN')) {eGFR3.value=geteGFR(this.value);};"
				maxlength="3" size="4"
				value="<%= props.getProperty("creat3", "") %>" /> mmol/L</td>
		</tr>
		</tr>
		<tr valign="top">
			<td width="21%" colspan="2">eGFR <span class="fine">(+/-
			refer if &lt; 60mL/min)</span></td>
			<td width="23%"><input tabindex="65" type="text"
				class="CHFTextarea" name="eGFR1"
				onchange="checkIt(this,'^\\d{2,3}$','The glomerular filtration rate is usually between 30-100!\nPlease type just numbers');"
				maxlength="3" size="4" value="<%= props.getProperty("eGFR1", "") %>" />
			mL/min</td>
			<td width="24%"><input tabindex="165" type="text"
				class="CHFTextarea" name="eGFR2"
				onchange="checkIt(this,'^\\d{2,3}$','The glomerular filtration rate is usually between 30-100!\nPlease type just numbers');"
				maxlength="3" size="4" value="<%= props.getProperty("eGFR2", "") %>" />
			mL/min</td>
			<td width="25%"><input tabindex="265" type="text"
				class="CHFTextarea" name="eGFR3"
				onchange="checkIt(this,'^\\d{2,3}$','The glomerular filtration rate is usually between 30-100!\nPlease type just numbers');"
				maxlength="3" size="4" value="<%= props.getProperty("eGFR3", "") %>" />
			mL/min</td>
		</tr>

		<tr valign="top">
			<td width="5%" bgcolor="#E6E6E6" rowspan="5"><b>Patient Self
			Management*</b></td>
			<td width="21%" colspan="2"># ER visits for HF<span class="fine">
			since last assessment</span></td>
			<td width="23%"><input tabindex="66" type="text"
				class="CHFTextarea" name="ervisits1" maxlength="20" size="4"
				value="<%= props.getProperty("ervisits1", "") %>" /> visits</td>
			<td width="24%"><input tabindex="166" type="text"
				class="CHFTextarea" name="ervisits12" maxlength="20" size="4"
				value="<%= props.getProperty("ervisits2", "") %>" /> visits</td>
			<td width="25%"><input tabindex="266" type="text"
				class="CHFTextarea" name="ervisits13" maxlength="20" size="4"
				value="<%= props.getProperty("ervisits3", "") %>" /> visits</td>
		</tr>

		<tr valign="top">
			<td width="21%" colspan="2">Education / self management training
			</td>
			<td width="23%"><input tabindex="71" type="checkbox"
				name="compliance1" <%= props.getProperty("compliance1", "") %>>
			Patient medication use <br>
			<input tabindex="72" type="checkbox" name="fluids1"
				<%= props.getProperty("fluids1", "") %>> Salt / fluid
			vigilance &and; <br>
			<input tabindex="73" type="checkbox" name="weights1"
				<%= props.getProperty("weights1", "") %>> Daily weight
			monitoring &and; <br>
			<input tabindex="74" type="checkbox" name="exercise1"
				<%= props.getProperty("exercise1", "") %>> Exercise /
			activity</td>

			<td width="23%"><input tabindex="171" type="checkbox"
				name="compliance2" <%= props.getProperty("compliance2", "") %>>
			Patient medication use <br>
			<input tabindex="172" type="checkbox" name="fluids2"
				<%= props.getProperty("fluids2", "") %>> Salt / fluid
			vigilance &and; <br>
			<input tabindex="173" type="checkbox" name="weights2"
				<%= props.getProperty("weights2", "") %>> Daily weight
			monitoring &and; <br>
			<input tabindex="174" type="checkbox" name="exercise2"
				<%= props.getProperty("exercise2", "") %>> Exercise /
			activity</td>

			<td width="23%"><input tabindex="271" type="checkbox"
				name="compliance3" <%= props.getProperty("compliance3", "") %>>
			Patient medication use <br>
			<input tabindex="272" type="checkbox" name="fluids3"
				<%= props.getProperty("fluids3", "") %>> Salt / fluid
			vigilance &and; <br>
			<input tabindex="273" type="checkbox" name="weights3"
				<%= props.getProperty("weights3", "") %>> Daily weight
			monitoring &and; <br>
			<input tabindex="274" type="checkbox" name="exercise3"
				<%= props.getProperty("exercise3", "") %>> Exercise /
			activity</td>
		</tr>

		<tr valign="top">
			<td width="21%" colspan="2">Target Modifiable Risk Factors for
			Heart Failure and Coronary Artery Disease</td>
			<td width="23%"><input tabindex="76" type="checkbox"
				name="discussbp1" <%= props.getProperty("discussbp1", "") %>>
			Hypertension <br>
			<input tabindex="77" type="checkbox" name="discussdm1"
				<%= props.getProperty("discussdm1", "") %>> Diabetes <br>
			<input tabindex="78" type="checkbox" name="discusslipids1"
				<%= props.getProperty("discusslipids1", "") %>>
			Hyperlipidemia <br>
			<input tabindex="79" type="checkbox" name="discusssmoking1"
				<%= props.getProperty("discusssmoking1", "") %>> Smoking <br>
			<input tabindex="80" type="checkbox" name="discussweight1"
				<%= props.getProperty("discussweight1", "") %>> Overweight /
			Obesity</td>

			<td width="24%"><input tabindex="176" type="checkbox"
				name="discussbp2" <%= props.getProperty("discussbp2", "") %>>
			Hypertension <br>
			<input tabindex="177" type="checkbox" name="discussdm2"
				<%= props.getProperty("discussdm2", "") %>> Diabetes <br>
			<input tabindex="178" type="checkbox" name="discusslipids2"
				<%= props.getProperty("discusslipids2", "") %>>
			Hyperlipidemia <br>
			<input tabindex="179" type="checkbox" name="discusssmoking2"
				<%= props.getProperty("discusssmoking2", "") %>> Smoking <br>
			<input tabindex="180" type="checkbox" name="discussweight2"
				<%= props.getProperty("discussweight2", "") %>> Overweight /
			Obesity</td>

			<td width="25%"><input tabindex="276" type="checkbox"
				name="discussbp3" <%= props.getProperty("discussbp3", "") %>>
			Hypertension <br>
			<input tabindex="277" type="checkbox" name="discussdm3"
				<%= props.getProperty("discussdm3", "") %>> Diabetes <br>
			<input tabindex="278" type="checkbox" name="discusslipids3"
				<%= props.getProperty("discusslipids3", "") %>>
			Hyperlipidemia <br>
			<input tabindex="279" type="checkbox" name="discusssmoking3"
				<%= props.getProperty("discusssmoking3", "") %>> Smoking <br>
			<input tabindex="280" type="checkbox" name="discussweight3"
				<%= props.getProperty("discussweight3", "") %>> Overweight /
			Obesity</td>
		</tr>

		<tr valign="top">
			<td width="21%" colspan="2">Collaborative Goal Setting <br>
			<span class="fine"> Indicate goal &rarr; </span></td>
			<td width="23%"><input tabindex="81" type="text"
				class="CHFTextarea" name="goal1" size="30" maxlength="60"
				value="<%= props.getProperty("goal1", "") %>" /></td>
			<td width="24%"><input tabindex="181" type="text"
				class="CHFTextarea" name="goal2" size="30" maxlength="60"
				value="<%= props.getProperty("goal2", "") %>" /></td>
			<td width="25%"><input tabindex="281" type="text"
				class="CHFTextarea" name="goal3" size="30" maxlength="60"
				value="<%= props.getProperty("goal3", "") %>" /></td>
		</tr>

		<tr valign="top">
			<td width="21%" colspan="2">Self Management <br>
			<span class="fine">Indicate challenge &rarr;</span></td>
			<td width="23%"><input tabindex="82" type="text"
				class="CHFTextarea" name="challenge1" size="30" maxlength="60"
				value="<%= props.getProperty("challenge1", "") %>" /></td>
			<td width="24%"><input tabindex="182" type="text"
				class="CHFTextarea" name="challenge2" size="30" maxlength="60"
				value="<%= props.getProperty("challenge2", "") %>" /></td>
			<td width="25%"><input tabindex="282" type="text"
				class="CHFTextarea" name="challenge3" size="30" maxlength="60"
				value="<%= props.getProperty("challenge3", "") %>" /></td>
		</tr>

		<tr valign="top">
			<td width="5%" bgcolor="#E6E6E6" rowspan="10"><b>Pharmacologic
			Management *</b>
			<p align="center"><b>For <u>Systolic</u> Heart Failure</b></p>
			</td>
			<td class="hideprint" width="94%" colspan="5"><b>NB: The CCS
			Guidelines are specific for the treatment of heart failure with
			systolic dysfunction and do not apply to patients with PSF. However,
			the following drug agents can be used to treat the <u>underlying
			cause</u> of heart failure with PSF. See below.</b></td>
		</tr>
		<tr valign="top">
			<td width="3%" bgcolor="#F3F3F3" rowspan="3">First Line</td>
			<td width="17%">ACEI<span class="fine">ACE inhibitor</span> <br>
			<input tabindex="83" type="checkbox" name="ACEintolerant"
				id="antiACE" <%= props.getProperty("ACEintolerant", "") %>><label
				for="antiACE" class="fine">Intolerant</label> <br>
			<input tabindex="84" type="checkbox" name="ACEcontraindicated"
				id="noACE" <%= props.getProperty("ACEcontraindicated", "") %>><label
				for="noACE" class="fine">Contraindicated</label></td>
			<td width="23%"><input tabindex="85" type="text"
				class="CHFTextarea" name="ACE1" size="30" maxlength="40"
				value="<%= props.getProperty("ACE1", "") %>" /> <select
				name="ACEsel1" size="1" class="hideprint"
				onchange="setTarget('ACE',1);">
				<option value="" selected="selected">- pick ACE / dose /
				frequency -</option>
				<option value="Captopril 12.5mg tid">Captopril 12.5mg tid</option>
				<option value="Target">Captopril 25mg tid</option>
				<option value="Target">Captopril 50mg tid</option>
				<option value="Enalapril 1.25mg bid">Enalapril 1.25mg bid</option>
				<option value="Enalapril 2.5mg bid">Enalapril 2.5mg bid</option>
				<option value="Target">Enalapril 5mg bid</option>
				<option value="Target">Enalapril 10mg bid</option>
				<option value="Ramipril 1.25mg bid">Ramipril 1.25mg bid</option>
				<option value="Ramipril 2.5mg bid">Ramipril 2.5mg bid</option>
				<option value="Target">Ramipril 5mg bid</option>
				<option value="Lisinopril 2.5mg od">Lisinopril 2.5mg od</option>
				<option value="Lisinopril 5mg od">Lisinopril 5mg od</option>
				<option value="Lisinopril 10mg od">Lisinopril 10mg od</option>
				<option value="Target">Lisinopril 20mg od</option>
			</select> At Target Dose? <input tabindex="86" type="checkbox"
				name="ACEtargetY1" id="ACEtargetY1"
				onClick="ACEtargetN1.checked=!ACEtargetY1.checked;"
				<%= props.getProperty("ACEtargetY1", "") %>><label
				for="ACEtargetY1"> yes </label> <input tabindex="87" type="checkbox"
				name="ACEtargetN1" id="ACEtargetN1"
				onClick="ACEtargetY1.checked=!ACEtargetN1.checked;"
				<%= props.getProperty("ACEtargetN1", "") %>><label
				for="ACEtargetN1"> no</label></td>
			<td width="24%"><input tabindex="185" type="text"
				class="CHFTextarea" name="ACE2" size="30" maxlength="40"
				value="<%= props.getProperty("ACE2", "") %>" /> <select
				name="ACEsel2" size="1" class="hideprint"
				onchange="setTarget('ACE',2);">
				<option value="" selected="selected">- pick ACE / dose /
				frequency -</option>
				<option value="Captopril 12.5mg tid">Captopril 12.5mg tid</option>
				<option value="Target">Captopril 25mg tid</option>
				<option value="Target">Captopril 50mg tid</option>
				<option value="Enalapril 1.25mg bid">Enalapril 1.25mg bid</option>
				<option value="Enalapril 2.5mg bid">Enalapril 2.5mg bid</option>
				<option value="Target">Enalapril 5mg bid</option>
				<option value="Target">Enalapril 10mg bid</option>
				<option value="Ramipril 1.25mg bid">Ramipril 1.25mg bid</option>
				<option value="Ramipril 2.5mg bid">Ramipril 2.5mg bid</option>
				<option value="Target">Ramipril 5mg bid</option>
				<option value="Lisinopril 2.5mg od">Lisinopril 2.5mg od</option>
				<option value="Lisinopril 5mg od">Lisinopril 5mg od</option>
				<option value="Lisinopril 10mg od">Lisinopril 10mg od</option>
				<option value="Target">Lisinopril 20mg od</option>
			</select> At Target Dose? <input tabindex="186" type="checkbox"
				name="ACEtargetY2" id="ACEtargetY2"
				onClick="ACEtargetN2.checked=!ACEtargetY2.checked;"
				<%= props.getProperty("ACEtargetY2", "") %>><label
				for="ACEtargetY2"> yes </label> <input tabindex="187"
				type="checkbox" name="ACEtargetN2" id="ACEtargetN2"
				onClick="ACEtargetY2.checked=!ACEtargetN2.checked;"
				<%= props.getProperty("ACEtargetN2", "") %>><label
				for="ACEtargetN2"> no</label></td>
			<td width="25%"><input tabindex="285" type="text"
				class="CHFTextarea" name="ACE3" size="30" maxlength="40"
				value="<%= props.getProperty("ACE3", "") %>" /> <select
				name="ACEsel3" size="1" class="hideprint"
				onchange="setTarget('ACE',3);">
				<option value="" selected="selected">- pick ACE / dose /
				frequency -</option>
				<option value="Captopril 12.5mg tid">Captopril 12.5mg tid</option>
				<option value="Target">Captopril 25mg tid</option>
				<option value="Target">Captopril 50mg tid</option>
				<option value="Enalapril 1.25mg bid">Enalapril 1.25mg bid</option>
				<option value="Enalapril 2.5mg bid">Enalapril 2.5mg bid</option>
				<option value="Target">Enalapril 5mg bid</option>
				<option value="Target">Enalapril 10mg bid</option>
				<option value="Ramipril 1.25mg bid">Ramipril 1.25mg bid</option>
				<option value="Ramipril 2.5mg bid">Ramipril 2.5mg bid</option>
				<option value="Target">Ramipril 5mg bid</option>
				<option value="Lisinopril 2.5mg od">Lisinopril 2.5mg od</option>
				<option value="Lisinopril 5mg od">Lisinopril 5mg od</option>
				<option value="Lisinopril 10mg od">Lisinopril 10mg od</option>
				<option value="Target">Lisinopril 20mg od</option>
			</select> At Target Dose? <input tabindex="286" type="checkbox"
				name="ACEtargetY3" id="ACEtargetY3"
				onClick="ACEtargetN3.checked=!ACEtargetY3.checked;"
				<%= props.getProperty("ACEtargetY3", "") %>><label
				for="ACEtargetY3"> yes </label> <input tabindex="287"
				type="checkbox" name="ACEtargetN3" id="ACEtargetN3"
				onClick="ACEtargetY3.checked=!ACEtargetN3.checked;"
				<%= props.getProperty("ACEtargetN3", "") %>><label
				for="ACEtargetN3"> no</label></td>
		</tr>

		<tr valign="top">
			<td width="17%">&beta; Blocker <br>
			<input tabindex="88" type="checkbox" name="bbintolerant" id="antibb"
				<%= props.getProperty("bbintolerant", "") %>><label
				for="antibb" class="fine">Intolerant</label> <br>
			<input tabindex="89" type="checkbox" name="bbcontraindicated"
				id="nobb" <%= props.getProperty("bbcontraindicated", "") %>><label
				for="nobb" class="fine">Contraindicated</label></td>
			<td width="23%"><input tabindex="90" type="text"
				class="CHFTextarea" name="bb1" size="30" maxlength="40"
				value="<%= props.getProperty("bb1", "") %>" /> <select
				name="bbsel1" size="1" class="hideprint"
				onchange="setTarget('bb',1);">
				<option value="" selected="selected">pick
				&beta;blocker/dose/frequency</option>
				<option value="Metoprolol 12.5mg bid">Metoprolol 12.5mg bid</option>
				<option value="Metoprolol 25mg bid">Metoprolol 25mg bid</option>
				<option value="Target">Metoprolol 50mg bid</option>
				<option value="Target">Metoprolol 100mg bid</option>
				<option value="Bisoprolol 2.5mg od">Bisoprolol 2.5mg od</option>
				<option value="Bisoprolol 5mg od">Bisoprolol 5mg od</option>
				<option value="Target">Bisoprolol 10mg od</option>
				<option value="Carvedilol 3.125mg bid">Carvedilol 3.125mg
				bid</option>
				<option value="Carvedilol 6.25mg bid">Carvedilol 6.125mg
				bid</option>
				<option value="Carvedilol 12.5mg bid">Carvedilol 12.5mg bid</option>
				<option value="Target">Carvedilol 25mg bid</option>
			</select> At Target Dose? <input tabindex="91" type="checkbox"
				name="bbtargetY1" id="bbtargetY1"
				onClick="bbtargetN1.checked=!bbtargetY1.checked;"
				<%= props.getProperty("bbtargetY1", "") %>><label
				for="bbtargetY1"> yes </label> <input tabindex="92" type="checkbox"
				name="bbtargetN1" id="bbtargetN1"
				onClick="bbtargetY1.checked=!bbtargetN1.checked;"
				<%= props.getProperty("bbtargetN1", "") %>><label
				for="bbtargetN1"> no</label></td>
			<td width="24%"><input tabindex="190" type="text"
				class="CHFTextarea" name="bb2" size="30" maxlength="40"
				value="<%= props.getProperty("bb2", "") %>" /> <select
				name="bbsel2" size="1" class="hideprint"
				onchange="setTarget('bb',2);">
				<option value="" selected="selected">pick
				&beta;blocker/dose/frequency</option>
				<option value="Metoprolol 12.5mg bid">Metoprolol 12.5mg bid</option>
				<option value="Metoprolol 25mg bid">Metoprolol 25mg bid</option>
				<option value="Target">Metoprolol 50mg bid</option>
				<option value="Target">Metoprolol 100mg bid</option>
				<option value="Bisoprolol 2.5mg od">Bisoprolol 2.5mg od</option>
				<option value="Bisoprolol 5mg od">Bisoprolol 5mg od</option>
				<option value="Target">Bisoprolol 10mg od</option>
				<option value="Carvedilol 3.125mg bid">Carvedilol 3.125mg
				bid</option>
				<option value="Carvedilol 6.25mg bid">Carvedilol 6.125mg
				bid</option>
				<option value="Carvedilol 12.5mg bid">Carvedilol 12.5mg bid</option>
				<option value="Target">Carvedilol 25mg bid</option>
			</select> At Target Dose? <input tabindex="191" type="checkbox"
				name="bbtargetY2" id="bbtargetY2"
				onClick="bbtargetN2.checked=!bbtargetY2.checked;"
				<%= props.getProperty("bbtargetY2", "") %>><label
				for="bbtargetY2"> yes </label> <input tabindex="192" type="checkbox"
				name="bbtargetN2" id="bbtargetN2"
				onClick="bbtargetY2.checked=!bbtargetN2.checked;"
				<%= props.getProperty("bbtargetN2", "") %>><label
				for="bbtargetN2"> no</label></td>
			<td width="25%"><input tabindex="290" type="text"
				class="CHFTextarea" name="bb3" size="30" maxlength="40"
				value="<%= props.getProperty("bb3", "") %>" /> <select
				name="bbsel3" size="1" class="hideprint"
				onchange="setTarget('bb',3);">
				<option value="" selected="selected">pick
				&beta;blocker/dose/frequency</option>
				<option value="Metoprolol 12.5mg bid">Metoprolol 12.5mg bid</option>
				<option value="Metoprolol 25mg bid">Metoprolol 25mg bid</option>
				<option value="Target">Metoprolol 50mg bid</option>
				<option value="Target">Metoprolol 100mg bid</option>
				<option value="Bisoprolol 2.5mg od">Bisoprolol 2.5mg od</option>
				<option value="Bisoprolol 5mg od">Bisoprolol 5mg od</option>
				<option value="Target">Bisoprolol 10mg od</option>
				<option value="Carvedilol 3.125mg bid">Carvedilol 3.125mg
				bid</option>
				<option value="Carvedilol 6.25mg bid">Carvedilol 6.125mg
				bid</option>
				<option value="Carvedilol 12.5mg bid">Carvedilol 12.5mg bid</option>
				<option value="Target">Carvedilol 25mg bid</option>
			</select> At Target Dose? <input tabindex="291" type="checkbox"
				name="bbtargetY3" id="bbtargetY3"
				onClick="bbtargetN3.checked=!bbtargetY3.checked;"
				<%= props.getProperty("bbtargetY3", "") %>><label
				for="bbtargetY3"> yes </label> <input tabindex="292" type="checkbox"
				name="bbtargetN3" id="bbtargetN3"
				onClick="bbtargetY3.checked=!bbtargetN3.checked;"
				<%= props.getProperty("bbtargetN3", "") %>><label
				for="bbtargetN3"> no</label></td>
		</tr>

		<tr valign="top">
			<td width="17%">ARB <span class="fine">Angiotensin
			Receptor Blocker if ACEI intolerant</span></td>
			<td width="23%"><input tabindex="93" type="text"
				class="CHFTextarea" name="ARB1" size="30" maxlength="40"
				value="<%= props.getProperty("ARB1", "") %>" /> <select
				name="ARBsel1" size="1" class="hideprint"
				onchange="setTarget('ARB',1);">
				<option value="" selected="selected">- pick ARB / dose /
				frequency -</option>
				<option value="Candesartan 4mg od">Candesartan 4mg od</option>
				<option value="Candesartan 8mg od">Candesartan 8mg od</option>
				<option value="Candesartan 16mg od">Candesartan 16mg od</option>
				<option value="Target">Candesartan 32mg od</option>
				<option value="Valsartan 40mg bid">Valsartan 40mg bid</option>
				<option value="Valsartan 80mg bid">Valsartan 80mg bid</option>
				<option value="Target">Valsartan 160mg bid</option>
			</select> At Target Dose? <input tabindex="94" type="checkbox"
				name="ARBtargetY1" id="ARBtargetY1"
				onClick="ARBtargetN1.checked=!ARBtargetY1.checked;"
				<%= props.getProperty("ARBtargetY1", "") %>><label
				for="ARBtargetY1"> yes </label> <input tabindex="95" type="checkbox"
				name="ARBtargetN1" id="ARBtargetN1"
				onClick="ARBtargetY1.checked=!ARBtargetN1.checked;"
				<%= props.getProperty("ARBtargetN1", "") %>><label
				for="ARBtargetN1"> no</label></td>
			<td width="24%"><input tabindex="193" type="text"
				class="CHFTextarea" name="ARB2" size="30" maxlength="40"
				value="<%= props.getProperty("ARB2", "") %>" /> <select
				name="ARBsel2" size="1" class="hideprint"
				onchange="setTarget('ARB',2);">
				<option value="" selected="selected">- pick ARB / dose /
				frequency -</option>
				<option value="Candesartan 4mg od">Candesartan 4mg od</option>
				<option value="Candesartan 8mg od">Candesartan 8mg od</option>
				<option value="Candesartan 16mg od">Candesartan 16mg od</option>
				<option value="Target">Candesartan 32mg od</option>
				<option value="Valsartan 40mg bid">Valsartan 40mg bid</option>
				<option value="Valsartan 80mg bid">Valsartan 80mg bid</option>
				<option value="Target">Valsartan 160mg bid</option>
			</select> At Target Dose? <input tabindex="194" type="checkbox"
				name="ARBtargetY2" id="ARBtargetY2"
				onClick="ARBtargetN2.checked=!ARBtargetY2.checked;"
				<%= props.getProperty("ARBtargetY2", "") %>><label
				for="ARBtargetY2"> yes </label> <input tabindex="195"
				type="checkbox" name="ARBtargetN2" id="ARBtargetN2"
				onClick="ARBtargetY2.checked=!ARBtargetN2.checked;"
				<%= props.getProperty("ARBtargetN2", "") %>><label
				for="ARBtargetN2"> no</label></td>
			<td width="25%"><input tabindex="293" type="text"
				class="CHFTextarea" name="ARB3" size="30" maxlength="40"
				value="<%= props.getProperty("ARB3", "") %>" /> <select
				name="ARBsel3" size="1" class="hideprint"
				onchange="setTarget('ARB',3);">
				<option value="" selected="selected">- pick ARB / dose /
				frequency -</option>
				<option value="Candesartan 4mg od">Candesartan 4mg od</option>
				<option value="Candesartan 8mg od">Candesartan 8mg od</option>
				<option value="Candesartan 16mg od">Candesartan 16mg od</option>
				<option value="Target">Candesartan 32mg od</option>
				<option value="Valsartan 40mg bid">Valsartan 40mg bid</option>
				<option value="Valsartan 80mg bid">Valsartan 80mg bid</option>
				<option value="Target">Valsartan 160mg bid</option>
			</select> At Target Dose? <input tabindex="294" type="checkbox"
				name="ARBtargetY3" id="ARBtargetY3"
				onClick="ARBtargetN3.checked=!ARBtargetY3.checked;"
				<%= props.getProperty("ARBtargetY3", "") %>><label
				for="ARBtargetY3"> yes </label> <input tabindex="295"
				type="checkbox" name="ARBtargetN3" id="ARBtargetN3"
				onClick="ARBtargetY3.checked=!ARBtargetN3.checked;"
				<%= props.getProperty("ARBtargetN3", "") %>><label
				for="ARBtargetN3"> no</label></td>
		</tr>

		<tr valign="top">
			<td width="3%" bgcolor="#F3F3F3" rowspan="3">Symptom Relief</td>
			<td width="17%">Loop Diuretic <span class="fine">minimum
			effective dose</span></td>
			<td width="23%"><input tabindex="96" type="text"
				class="CHFTextarea" name="loop1" size="30" maxlength="40"
				value="<%= props.getProperty("loop1", "") %>" /><BR>
			Agent / dose</td>
			<td width="24%"><input tabindex="196" type="text"
				class="CHFTextarea" name="loop2" size="30" maxlength="40"
				value="<%= props.getProperty("loop2", "") %>" /><BR>
			Agent / dose</td>
			<td width="25%"><input tabindex="296" type="text"
				class="CHFTextarea" name="loop3" size="30" maxlength="40"
				value="<%= props.getProperty("loop3", "") %>" /><BR>
			Agent / dose</td>
		</tr>

		<tr valign="top">
			<td width="17%">Spironolactone <span class="fine">(LVEF&lt;30%)
			Class III - IV Heart Failure</span></td>
			<td width="23%"><input tabindex="97" type="text"
				class="CHFTextarea" name="spironolactone1"
				onchange="checkIt(this,'^\\d{2,3}(\\.\\d)?$','The daily Spironolactone dose usually is between 12.5 and 200\n(enter just the number)');"
				size="30" maxlength="4"
				value="<%= props.getProperty("spironolactone1", "") %>" /><br>
			mg/d dose</td>
			<td width="24%"><input tabindex="197" type="text"
				class="CHFTextarea" name="spironolactone2"
				onchange="checkIt(this,'^\\d{2,3}(\\.\\d)?$','The daily Spironolactone dose usually is between 12.5 and 200\n(enter just the number)');"
				size="30" maxlength="20"
				value="<%= props.getProperty("spironolactone2", "") %>" /><br>
			mg/d dose</td>
			<td width="25%"><input tabindex="297" type="text"
				class="CHFTextarea" name="spironolactone3"
				onchange="checkIt(this,'^\\d{2,3}(\\.\\d)?$','The daily Spironolactone dose usually is between 12.5 and 200\n(enter just the number)');"
				size="30" maxlength="20"
				value="<%= props.getProperty("spironolactone3", "") %>" /><br>
			mg/d dose</td>
		</tr>

		<tr valign="top">
			<td width="17%">Digoxin <span class="fine">If A-fib or
			advanced HF</span></td>
			<td width="23%"><input tabindex="98" type="text"
				class="CHFTextarea" name="digoxin1"
				onchange="checkIt(this,'^0\\.\\d{3,4}$','The daily Digoxin dose usually is between 0.0625 and 0.250\n(enter just the number)');"
				size="30" maxlength="6"
				value="<%= props.getProperty("digoxin1", "") %>" /><br>
			mg/d dose</td>
			<td width="24%"><input tabindex="198" type="text"
				class="CHFTextarea" name="digoxin2"
				onchange="checkIt(this,'^0\\.\\d{3,4}$','The daily Digoxin dose usually is between 0.0625 and 0.250\n(enter just the number)');"
				size="30" maxlength="6"
				value="<%= props.getProperty("digoxin2", "") %>" /><br>
			mg/d dose</td>
			<td width="25%"><input tabindex="298" type="text"
				class="CHFTextarea" name="digoxin3"
				onchange="checkIt(this,'^0\\.\\d{3,4}$','The daily Digoxin dose usually is between 0.0625 and 0.250\n(enter just the number)');"
				size="30" maxlength="6"
				value="<%= props.getProperty("digoxin3", "") %>" /><br>
			mg/d dose</td>
		</tr>
		<tr valign="top">
			<td width="3%" bgcolor="#F3F3F3" rowspan="2">Preventive</td>
			<td width="17%">Consider ASA</td>
			<td width="23%"><input tabindex="99" type="text"
				class="CHFTextarea" name="ASA1"
				onchange="checkIt(this,'^\\d{2,4}$','The daily aspirin dose usually is between 80 and 325\n(enter just the number)');"
				size="30" maxlength="4" value="<%= props.getProperty("ASA1", "") %>" /><br>
			mg/d dose</td>
			<td width="24%"><input tabindex="199" type="text"
				class="CHFTextarea" name="ASA2"
				onchange="checkIt(this,'^\\d{2,4}$','The daily aspirin dose usually is between 80 and 325\n(enter just the number)');"
				size="30" maxlength="4" value="<%= props.getProperty("ASA2", "") %>" /><br>
			mg/d dose</td>
			<td width="25%"><input tabindex="299" type="text"
				class="CHFTextarea" name="ASA3"
				onchange="checkIt(this,'^\\d{2,4}$','The daily aspirin dose usually is between 80 and 325\n(enter just the number)');"
				size="30" maxlength="4" value="<%= props.getProperty("ASA3", "") %>" /><br>
			mg/d dose</td>
		</tr>
		<tr valign="top">
			<td width="17%">Anticoagulant <br>
			<span class="fine">if A-fib present</span></td>
			<td width="23%"><input tabindex="100" type="text"
				class="CHFTextarea" name="anticoagulant1" size="30" maxlength="40"
				value="<%= props.getProperty("anticoagulant1", "") %>" /> Agent /
			dose</td>
			<td width="24%"><input tabindex="200" type="text"
				class="CHFTextarea" name="anticoagulant2" size="30" maxlength="40"
				value="<%= props.getProperty("anticoagulant2", "") %>" /> Agent /
			dose</td>
			<td width="25%"><input tabindex="300" type="text"
				class="CHFTextarea" name="anticoagulant3" size="30" maxlength="40"
				value="<%= props.getProperty("anticoagulant3", "") %>" /> Agent /
			dose</td>
		</tr>

		</td>
		<td width="21%" colspan="2">Signature</td>
		<td width="23%"><input type="text" class="CHFTextarea"
			name="sig1" size="30" maxlength="20"
			value="<%= props.getProperty("sig1", "") %>" /></td>
		<td width="24%"><input type="text" class="CHFTextarea"
			name="sig2" size="30" maxlength="20"
			value="<%= props.getProperty("sig2", "") %>" /></td>
		<td width="25%"><input type="text" class="CHFTextarea"
			name="sig3" size="30" maxlength="20"
			value="<%= props.getProperty("sig3", "") %>" /></td>
		</tr>

		<tr valign="top" class="hideprint">
			<td width="100%" colspan="6">&sigma; NYHA Classification: Class
			I - no symptoms; Class II - symptoms with ordinary activity; Class
			III - symptoms with less than ordinary activity; Class IV - symptoms
			at rest
			<p>An increase in serum creatinine up to 30% is not uncommon when
			an ACEI or ARB is introduced; if stabilizes at &lt;30% above
			baseline, may continue medication however closer long-term monitoring
			may be required.</p>
			<p>&and; Essential for patients with fluid retention or
			congestion not easily controlled with diuretics, or in patients with
			significant renal dysfunction</p>
			</td>
		</tr>
	</table>

	</div>
</html:form>
</body>
<script type="text/javascript">


// due to a glitch in how these form fields are handled by calander-setup.js
// the date parameter that gets passed in Calander.setup gets overwritten
// ... eventually by the current date, even with an empty input field
// so for the following to work you need to patch calander-setup.js;cdem  

// the following code sets the date to last November for the appropriate calanders

var dflu = new Date();
if (dflu.getMonth() < 10)
{
 dflu.setMonth(10); // (last) November is the shot season, so set the calander to it. 
 dflu.setFullYear(dflu.getFullYear()-1);
}


Calendar.setup({ inputField : "dateFlu", ifFormat : "%Y/%m/%d", date : dflu, showsTime :false, button : "dateFlu_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "datePneumo", ifFormat : "%Y/%m/%d",  date : dflu, showsTime :false, button : "datePneumo_cal", singleClick : true, step : 1 });

Calendar.setup({ inputField : "dateEcho", ifFormat : "%Y/%m/%d", showsTime :false, button : "dateEcho_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "dateCXR", ifFormat : "%Y/%m/%d", showsTime :false, button : "dateCXR_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "dateEKG", ifFormat : "%Y/%m/%d", showsTime :false, button : "dateEKG_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "date1", ifFormat : "%Y/%m/%d", showsTime :false, button : "date1_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "date2", ifFormat : "%Y/%m/%d", showsTime :false, button : "date2_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "date3", ifFormat : "%Y/%m/%d", showsTime :false, button : "date3_cal", singleClick : true, step : 1 });

</script>
</html:html>
