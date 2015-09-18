<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.form.study.*"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Type 2 Diabetes Record</title>
<link rel="stylesheet" type="text/css" href="../styles.css" />
<!--link rel="stylesheet" type="text/css" media="print" href="../print.css"/-->
<html:base />
</head>

<%
    String provNo = (String) session.getAttribute("user");
    String demoNo = request.getParameter("demographic_no");
    String studyId = request.getParameter("study_no");
    oscar.form.data.FrmData.PatientForm pform = (new oscar.form.data.FrmData()).getCurrentPatientForm(demoNo, studyId);
    int formId = (pform == null) ? 0: Integer.parseInt(pform.getFormId());
	String[] studyNameLink = (new oscar.form.data.FrmData()).getStudyNameLink(studyId);
    FrmStudyRecord rec = (new FrmStudyRecordFactory()).factory(studyNameLink[0]);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request), Integer.parseInt(demoNo), formId);
%>

<script type="text/javascript" language="Javascript">
<!-- start javascript ---- 

    function onPrint() {
        window.print();
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
    /*
    if(valDate(document.forms[0].dateDX)==false){
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
    }else
    if(valDate(document.forms[0].date4)==false){
        b = false;
    }else
    if(valDate(document.forms[0].date5)==false){
        b = false;
    }
*/
    return b;
}
function calToday(field) {
	var calDate=new Date();
	field.value = calDate.getFullYear() + '/' + (calDate.getMonth()+1) + '/' + calDate.getDate();
}
-->
</script>



<body bgproperties="fixed" class="Type2DiabetesForm" bgcolor="#EEEEFF"
	onLoad="javascript:window.focus()" topmargin="0" leftmargin="0"
	rightmargin="0">
<html:form action="/form/study/studyname">


	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="ID"
		value="<%= props.getProperty("ID", "0") %>" />
	<input type="hidden" name="provider_no" value=<%=""+provNo%> />
	<input type="hidden" name="study_no" value=<%=studyId%> />
	<input type="hidden" name="study_name" value=<%=studyNameLink[0]%> />
	<input type="hidden" name="study_link" value=<%=studyNameLink[1]%> />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />

	<table class="Head" width="100%" class="hidePrint">
		<tr>
			<td align="left"><input type="hidden" name="submit" value="exit">
			<input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();" />
			<input type="button" value="Print"
				onclick="javascript:return onPrint();" /></td>
			<td align="right"><a
				href="dmdata.jsp?demographic_no=<%=demoNo%>">View Data</a> | <a
				href="dm2ping.jsp?demographic_no=<%=demoNo%>">Send to PING</a></td>
		</tr>
	</table>

	<table border="0" cellspacing="3" cellpadding="0" width="100%">
		<tr>
			<th align=CENTER colspan="4" bgcolor="#CCCCFF">TYPE 2 DIABETES
			RECORD</th>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>Name: <input type="text" class="Type2DiabetesInput"
				name="pName" readonly="true" size="30"
				value="<%= props.getProperty("pName", "") %>" /></td>
			<td>DOB<small>(yyyy/mm/dd)</small>: <input type="text"
				class="Type2DiabetesInput" readonly="true" name="birthDate"
				size="11" value="<%= props.getProperty("birthDate", "") %>"
				readonly="true" /></td>
			<td>Date of Dx<small>(yyyy/mm/dd)</small>: <input type="text"
				class="Type2DiabetesInput" name="dateDx" size="11"
				value="<%=props.getProperty("dateDx", "") %>" /></td>
			<td>Height: <input type="text" class="Type2DiabetesInput"
				name="height" size="5"
				value="<%= props.getProperty("height", "") %>" /></td>
		</tr>
	</table>
	<table border="1" cellspacing="0" cellpadding="0">
		<tr>
			<td>
			<table width="100%">
				<tr>
					<td><span class="ping">Date <small>(complete
					q3-6 months)</small></span></td>
					<td align="right"><small>(yyyy/mm/dd)</small></td>
				</tr>
			</table>
			</td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="date1" value="<%= props.getProperty("date1", "") %>"
				onDblClick="calToday(this)" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="date2" value="<%= props.getProperty("date2", "") %>"
				onDblClick="calToday(this)" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="date3" value="<%= props.getProperty("date3", "") %>"
				onDblClick="calToday(this)" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="date4" value="<%= props.getProperty("date4", "") %>"
				onDblClick="calToday(this)" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="date5" value="<%= props.getProperty("date5", "") %>"
				onDblClick="calToday(this)" /></td>
		</tr>
		<tr>
			<td align="left"><span class="ping">Weight (BMI ideally
			&lt;27)</span></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="weight1" value="<%= props.getProperty("weight1", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="weight2" value="<%= props.getProperty("weight2", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="weight3" value="<%= props.getProperty("weight3", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="weight4" value="<%= props.getProperty("weight4", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="weight5" value="<%= props.getProperty("weight5", "") %>" /></td>
		</tr>
		<tr>
			<td align="left"><span class="ping">*BP (ideally
			&lt;130/80)</span></td>
			<td><input type="text" class="Type2DiabetesTextarea" name="bp1"
				value="<%= props.getProperty("bp1", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea" name="bp2"
				value="<%= props.getProperty("bp2", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea" name="bp3"
				value="<%= props.getProperty("bp3", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea" name="bp4"
				value="<%= props.getProperty("bp4", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea" name="bp5"
				value="<%= props.getProperty("bp5", "") %>" /></td>
		</tr>
		<tr>
			<td align="left" rowspan="3">
			<table border="0" nowrap="true">
				<tr>
					<td colspan="4" align="left" nowrap="true">GLUCOSE <small>(insulin
					q3mo, OHA q6mo)</small></td>
					<td align="right"><u><span class="ping">HbAic</span></u></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td nowrap="true" align="center"><small>Optimal</small></td>
					<td nowrap="true" align="center"><small>Suboptimal</small></td>
					<td nowrap="true" align="center"><small>Inadequate</small></td>
				</tr>
				<tr>
					<td align="left"><small>&nbsp;<br>
					HbAic<br>
					FBS</small></td>
					<td nowrap="true" align="center"><small>(target goal)<br>
					&lt;,0.07<br>
					4-7</small></td>
					<td nowrap="true" align="center"><small>(action may be
					required)<br>
					0.07-0.084<br>
					7.1-10</small></td>
					<td nowrap="true" align="center"><small>(action
					required)<br>
					&gt;0.084<br>
					&gt;10</small></td>
					<td align="right" valign="top"><u>FBS</u></td>
				</tr>
				<tr>
					<td colspan="5" align="left"><span class="ping"><u>HOME</u><br>
					(check glucometer q yearly)<br>
					RANGE</span></td>
				</tr>
			</table>
			</td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="glucoseA1"><%= props.getProperty("glucoseA1", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="glucoseA2"><%= props.getProperty("glucoseA2", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="glucoseA3"><%= props.getProperty("glucoseA3", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="glucoseA4"><%= props.getProperty("glucoseA4", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="glucoseA5"><%= props.getProperty("glucoseA5", "") %></textarea></td>
		</tr>
		<tr>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="glucoseB1"><%= props.getProperty("glucoseB1", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="glucoseB2"><%= props.getProperty("glucoseB2", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="glucoseB3"><%= props.getProperty("glucoseB3", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="glucoseB4"><%= props.getProperty("glucoseB4", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="glucoseB5"><%= props.getProperty("glucoseB5", "") %></textarea></td>
		</tr>
		<tr>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="glucoseC1"><%= props.getProperty("glucoseC1", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="glucoseC2"><%= props.getProperty("glucoseC2", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="glucoseC3"><%= props.getProperty("glucoseC3", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="glucoseC4"><%= props.getProperty("glucoseC4", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="glucoseC5"><%= props.getProperty("glucoseC5", "") %></textarea></td>
		</tr>
		<tr>
			<td align="left">RENAL<br>
			1. Dip for macroalbuminuria (q 3-6 months)<br>
			<span style="padding-left: 20px;"></span> <small>*if -ve see
			step 2, if +ve see step 3</small></td>
			<td><textarea style="height: 50px;"
				class="Type2DiabetesTextarea" name="renal1"><%= props.getProperty("renal1", "") %></textarea></td>
			<td><textarea style="height: 50px;"
				class="Type2DiabetesTextarea" name="renal2"><%= props.getProperty("renal2", "") %></textarea></td>
			<td><textarea style="height: 50px;"
				class="Type2DiabetesTextarea" name="renal3"><%= props.getProperty("renal3", "") %></textarea></td>
			<td><textarea style="height: 50px;"
				class="Type2DiabetesTextarea" name="renal4"><%= props.getProperty("renal4", "") %></textarea></td>
			<td><textarea style="height: 50px;"
				class="Type2DiabetesTextarea" name="renal5"><%= props.getProperty("renal5", "") %></textarea></td>
		</tr>
		<tr>
			<td align="left">2. <b><span class="ping">*Urine
			alb:creat ratio yearly</span></b><br>
			<small>*if +ve (female &gt;=2.8 or male &gt;=2.0) see step 3</small>
			</td>
			<td><textarea style="height: 37px;"
				class="Type2DiabetesTextarea" name="urineRatio1"><%= props.getProperty("urineRatio1", "") %></textarea></td>
			<td><textarea style="height: 37px;"
				class="Type2DiabetesTextarea" name="urineRatio2"><%= props.getProperty("urineRatio2", "") %></textarea></td>
			<td><textarea style="height: 37px;"
				class="Type2DiabetesTextarea" name="urineRatio3"><%= props.getProperty("urineRatio3", "") %></textarea></td>
			<td><textarea style="height: 37px;"
				class="Type2DiabetesTextarea" name="urineRatio4"><%= props.getProperty("urineRatio4", "") %></textarea></td>
			<td><textarea style="height: 37px;"
				class="Type2DiabetesTextarea" name="urineRatio5"><%= props.getProperty("urineRatio5", "") %></textarea></td>
		</tr>
		<tr>
			<td align="left">3. <b>*24-hr urine cr clearance &
			albuminuria</b><br>
			<span style="padding-left: 20px;"></span> q 6-12 mos<br>
			<span style="padding-left: 20px;"></span> <small>*if &gt;
			30mg albumin ?ACE<br>
			</small> <span style="padding-left: 20px;"></span> Nephrologist (if cr. clear
			<font face="Symbols">&#223</font> by 60%)</td>
			<td><textarea style="height: 67px;"
				class="Type2DiabetesTextarea" name="urineClearance1"><%= props.getProperty("urineClearance1", "") %></textarea></td>
			<td><textarea style="height: 67px;"
				class="Type2DiabetesTextarea" name="urineClearance2"><%= props.getProperty("urineClearance2", "") %></textarea></td>
			<td><textarea style="height: 67px;"
				class="Type2DiabetesTextarea" name="urineClearance3"><%= props.getProperty("urineClearance3", "") %></textarea></td>
			<td><textarea style="height: 67px;"
				class="Type2DiabetesTextarea" name="urineClearance4"><%= props.getProperty("urineClearance4", "") %></textarea></td>
			<td><textarea style="height: 67px;"
				class="Type2DiabetesTextarea" name="urineClearance5"><%= props.getProperty("urineClearance5", "") %></textarea></td>
		</tr>
		<tr>
			<td rowspan="3">
			<table nowrap="true" width="100%">
				<tr>
					<td align="left" colspan="4"><span class="ping">*LIPIDS</span>
					(monitor every 1-3y)</td>
					<td align="right"><span class="ping">TG</span></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="center"><small>TG</small></td>
					<td align="center"><small>LDL</small></td>
					<td align="center"><small>TC/HDL</small></td>
					<td align="right">LDL</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="center"><small>&lt;2.0</small></td>
					<td align="center"><small>&lt;2.5</small></td>
					<td align="center"><small>&lt;4.0</small></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td colspan="5" align="right">TC/HDL</td>
				</tr>
			</table>
			</td>
			<td><textarea style="height: 30px;"
				class="Type2DiabetesTextarea" name="lipidsA1"><%= props.getProperty("lipidsA1", "") %></textarea></td>
			<td><textarea style="height: 30px;"
				class="Type2DiabetesTextarea" name="lipidsA2"><%= props.getProperty("lipidsA2", "") %></textarea></td>
			<td><textarea style="height: 30px;"
				class="Type2DiabetesTextarea" name="lipidsA3"><%= props.getProperty("lipidsA3", "") %></textarea></td>
			<td><textarea style="height: 30px;"
				class="Type2DiabetesTextarea" name="lipidsA4"><%= props.getProperty("lipidsA4", "") %></textarea></td>
			<td><textarea style="height: 30px;"
				class="Type2DiabetesTextarea" name="lipidsA5"><%= props.getProperty("lipidsA5", "") %></textarea></td>
		</tr>
		<tr>
			<td><textarea style="height: 30px;"
				class="Type2DiabetesTextarea" name="lipidsB1"><%= props.getProperty("lipidsB1", "") %></textarea></td>
			<td><textarea style="height: 30px;"
				class="Type2DiabetesTextarea" name="lipidsB2"><%= props.getProperty("lipidsB2", "") %></textarea></td>
			<td><textarea style="height: 30px;"
				class="Type2DiabetesTextarea" name="lipidsB3"><%= props.getProperty("lipidsB3", "") %></textarea></td>
			<td><textarea style="height: 30px;"
				class="Type2DiabetesTextarea" name="lipidsB4"><%= props.getProperty("lipidsB4", "") %></textarea></td>
			<td><textarea style="height: 30px;"
				class="Type2DiabetesTextarea" name="lipidsB5"><%= props.getProperty("lipidsB5", "") %></textarea></td>
		</tr>
		<tr>
			<td><textarea style="height: 30px;"
				class="Type2DiabetesTextarea" name="lipidsC1"><%= props.getProperty("lipidsC1", "") %></textarea></td>
			<td><textarea style="height: 30px;"
				class="Type2DiabetesTextarea" name="lipidsC2"><%= props.getProperty("lipidsC2", "") %></textarea></td>
			<td><textarea style="height: 30px;"
				class="Type2DiabetesTextarea" name="lipidsC3"><%= props.getProperty("lipidsC3", "") %></textarea></td>
			<td><textarea style="height: 30px;"
				class="Type2DiabetesTextarea" name="lipidsC4"><%= props.getProperty("lipidsC4", "") %></textarea></td>
			<td><textarea style="height: 30px;"
				class="Type2DiabetesTextarea" name="lipidsC5"><%= props.getProperty("lipidsC5", "") %></textarea></td>
		</tr>
		<tr>
			<td align="left"><b><span class="ping">EYES (@dx then
			q2-4yrs)</span></b><br>
			Ophthalmologist: <input type="text" class="Type2DiabetesInput"
				name="ophthalmologist"
				value="<%= props.getProperty("ophthalmologist", "") %>" /></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="eyes1"><%= props.getProperty("eyes1", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="eyes2"><%= props.getProperty("eyes2", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="eyes3"><%= props.getProperty("eyes3", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="eyes4"><%= props.getProperty("eyes4", "") %></textarea></td>
			<td><textarea style="height: 42px;"
				class="Type2DiabetesTextarea" name="eyes5"><%= props.getProperty("eyes5", "") %></textarea></td>
		</tr>
		<tr>
			<td align="left"><b><span class="ping">FEET</span></b> check
			skin (q visit)<br>
			<span style="padding-left: 20px;"></span> annually (sensation,
			vibration, reflexes, pulses,<br>
			infection)</td>
			<td><textarea style="height: 50px;"
				class="Type2DiabetesTextarea" name="feet1"><%= props.getProperty("feet1", "") %></textarea></td>
			<td><textarea style="height: 50px;"
				class="Type2DiabetesTextarea" name="feet2"><%= props.getProperty("feet2", "") %></textarea></td>
			<td><textarea style="height: 50px;"
				class="Type2DiabetesTextarea" name="feet3"><%= props.getProperty("feet3", "") %></textarea></td>
			<td><textarea style="height: 50px;"
				class="Type2DiabetesTextarea" name="feet4"><%= props.getProperty("feet4", "") %></textarea></td>
			<td><textarea style="height: 50px;"
				class="Type2DiabetesTextarea" name="feet5"><%= props.getProperty("feet5", "") %></textarea></td>
		</tr>
		<tr>
			<td>
			<table width="100%">
				<tr>
					<td colspan="4" align="left">MEDICATIONS</td>
				</tr>
				<tr>
					<td>1. METFORMIN</td>
					<td><input type="checkbox" name="metformin"
						<%= props.getProperty("metformin", "") %> /></td>
					<td>5. <span class="ping">ACE INHIBITOR</span></td>
					<td nowrap="true"><input type="checkbox" name="aceInhibitor"
						<%= props.getProperty("aceInhibitor", "") %> />*</td>
				</tr>
				<tr>
					<td>2. GLYBURIDE</td>
					<td><input type="checkbox" name="glyburide"
						<%= props.getProperty("glyburide", "") %> /></td>
					<td>6. <span class="ping">ASA</span> &gt;30 YR</td>
					<td align="left"><input type="checkbox" name="asa"
						<%= props.getProperty("asa", "") %> /></td>
				</tr>
				<tr>
					<td>3. OTHER OHA</td>
					<td><input type="checkbox" name="otherOha"
						<%= props.getProperty("otherOha", "") %> /></td>
					<td colspan="2">7. <input type="text"
						class="Type2DiabetesInput" name="otherBox7"
						value="<%= props.getProperty("otherBox7", "") %>" /></td>
				</tr>
				<tr>
					<td>4. INSULIN</td>
					<td><input type="checkbox" name="insulin"
						<%= props.getProperty("insulin", "") %> /></td>
					<td colspan="2">8. <input type="text"
						class="Type2DiabetesInput" name="otherBox8"
						value="<%= props.getProperty("otherBox8", "") %>" /></td>
				</tr>
			</table>
			</td>
			<td><textarea style="height: 120px;"
				class="Type2DiabetesTextarea" name="meds1"><%= props.getProperty("meds1", "") %></textarea></td>
			<td><textarea style="height: 120px;"
				class="Type2DiabetesTextarea" name="meds2"><%= props.getProperty("meds2", "") %></textarea></td>
			<td><textarea style="height: 120px;"
				class="Type2DiabetesTextarea" name="meds3"><%= props.getProperty("meds3", "") %></textarea></td>
			<td><textarea style="height: 120px;"
				class="Type2DiabetesTextarea" name="meds4"><%= props.getProperty("meds4", "") %></textarea></td>
			<td><textarea style="height: 120px;"
				class="Type2DiabetesTextarea" name="meds5"><%= props.getProperty("meds5", "") %></textarea></td>
		</tr>
		<tr>
			<td align="left">*LIFESTYLE <b><span class="ping">Smoking</span></b>
			(Y/N)</td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="lifestyle1" value="<%= props.getProperty("lifestyle1", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="lifestyle2" value="<%= props.getProperty("lifestyle2", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="lifestyle3" value="<%= props.getProperty("lifestyle3", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="lifestyle4" value="<%= props.getProperty("lifestyle4", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="lifestyle5" value="<%= props.getProperty("lifestyle5", "") %>" /></td>
		</tr>
		<tr>
			<td style="padding-left: 60px;" align="left"><i><span
				class="ping">Exercise</span></i> (min/wk)</td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="exercise1" value="<%= props.getProperty("exercise1", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="exercise2" value="<%= props.getProperty("exercise2", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="exercise3" value="<%= props.getProperty("exercise3", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="exercise4" value="<%= props.getProperty("exercise4", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="exercise5" value="<%= props.getProperty("exercise5", "") %>" /></td>
		</tr>
		<tr>
			<td style="padding-left: 60px;" align="left">Alcohol</td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="alcohol1" value="<%= props.getProperty("alcohol1", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="alcohol2" value="<%= props.getProperty("alcohol2", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="alcohol3" value="<%= props.getProperty("alcohol3", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="alcohol4" value="<%= props.getProperty("alcohol4", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="alcohol5" value="<%= props.getProperty("alcohol5", "") %>" /></td>
		</tr>
		<tr>
			<td style="padding-left: 60px;" align="left">Sexual Function</td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="sexualFunction1"
				value="<%= props.getProperty("sexualFunction1", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="sexualFunction2"
				value="<%= props.getProperty("sexualFunction2", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="sexualFunction3"
				value="<%= props.getProperty("sexualFunction3", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="sexualFunction4"
				value="<%= props.getProperty("sexualFunction4", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="sexualFunction5"
				value="<%= props.getProperty("sexualFunction5", "") %>" /></td>
		</tr>
		<tr>
			<td style="padding-left: 60px;" align="left"><i>Diet</i></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="diet1" value="<%= props.getProperty("diet1", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="diet2" value="<%= props.getProperty("diet2", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="diet3" value="<%= props.getProperty("diet3", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="diet4" value="<%= props.getProperty("diet4", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="diet5" value="<%= props.getProperty("diet5", "") %>" /></td>
		</tr>
		<tr>
			<td align="left">OTHER/PLAN</td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="otherPlan1" value="<%= props.getProperty("otherPlan1", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="otherPlan2" value="<%= props.getProperty("otherPlan2", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="otherPlan3" value="<%= props.getProperty("otherPlan3", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="otherPlan4" value="<%= props.getProperty("otherPlan4", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="otherPlan5" value="<%= props.getProperty("otherPlan5", "") %>" /></td>
		</tr>
		<tr>
			<td rowspan="1">
			<table>
				<tr>
					<td>Consultant:</td>
					<td><input type="text" class="Type2DiabetesInput"
						name="consultant"
						value="<%= props.getProperty("consultant", "") %>" /></td>
				</tr>
				<tr>
					<td>Diabetic Educator:</td>
					<td><input type="text" class="Type2DiabetesInput"
						name="educator" value="<%= props.getProperty("educator", "") %>" /></td>
				</tr>
				<tr>
					<td>Nutritionist:</td>
					<td><input type="text" class="Type2DiabetesInput"
						name="nutritionist"
						value="<%= props.getProperty("nutritionist", "") %>" /></td>
				</tr>
			</table>
			</td>
			<td><textarea style="height: 75px;"
				class="Type2DiabetesTextarea" name="cdn1"><%= props.getProperty("cdn1", "") %></textarea></td>
			<td><textarea style="height: 75px;"
				class="Type2DiabetesTextarea" name="cdn2"><%= props.getProperty("cdn2", "") %></textarea></td>
			<td><textarea style="height: 75px;"
				class="Type2DiabetesTextarea" name="cdn3"><%= props.getProperty("cdn3", "") %></textarea></td>
			<td><textarea style="height: 75px;"
				class="Type2DiabetesTextarea" name="cdn4"><%= props.getProperty("cdn4", "") %></textarea></td>
			<td><textarea style="height: 75px;"
				class="Type2DiabetesTextarea" name="cdn5"><%= props.getProperty("cdn5", "") %></textarea></td>
		</tr>
		<tr>
			<td>Initials</td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="initials1" value="<%= props.getProperty("initials1", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="initials2" value="<%= props.getProperty("initials2", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="initials3" value="<%= props.getProperty("initials3", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="initials4" value="<%= props.getProperty("initials4", "") %>" /></td>
			<td><input type="text" class="Type2DiabetesTextarea"
				name="initials5" value="<%= props.getProperty("initials5", "") %>" /></td>
		</tr>
		<tr class="Type2DiabetesFooter">
			<td colspan="3">*Revised 2004<br>
			DISCLAIMER: This is a guidline only and should be modified according
			to current evidence and guidelines<br>
			<br>
			<b>GRADE A</b>: good evidence <span style="padding-left: 20px;"></span><i>GRADE
			B</i>: fair evidence<br>
			<br>
			OHA = ORAL HYPOGLYCEMIC AGENT *HOPE TRIAL: Ramipril for diabetics
			with other risk factor: HTN, smoking, increased TC, decreased LDL, MA
			</td>
			<td colspan="3" align="right">
			<table class="TableWithBorder" bordercolor="#000000" width="100%">
				<tr>
					<td>RESOURCE MATERIAL</td>
				</tr>
				<tr>
					<td><input type="checkbox" name="resource1"
						<%= props.getProperty("resource1", "") %> /> Things You Should
					Know About Diabetes-Type2</td>
				</tr>
				<tr>
					<td><input type="checkbox" name="resource2"
						<%= props.getProperty("resource2", "") %> /> Services Available
					for People with Diabetes in Hamilton</td>
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
