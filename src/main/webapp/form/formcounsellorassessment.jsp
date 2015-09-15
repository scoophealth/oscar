<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

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

<%@ page import="oscar.form.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>


<!-- link rel="stylesheet" href="Style1.css" type="text/css" media="all" -->


<title>INTAKE B. COUNSELLOR ASSESSMENT</title>
<link rel="stylesheet" type="text/css"
	href="formcounsellorassessment.css">
<link rel="stylesheet" type="text/css" media="print" href="print.css">
<style type="text/css" media="print">
BODY {
	font-size: 85%;
}

TABLE {
	font-size: 85%;
}
</style>
<html:base />
</head>

<%
	String formClass = "CounsellorAssessment";
	
	String formLink = "formcounsellorassessment.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);
	
%>

<script type="text/javascript" language="Javascript">

    function onPrint() {
//        document.forms[0].submit.value="print";
//        var ret = checkAllDates();
//        if(ret==true)
//        {
//            ret = confirm("Do you wish to save this form and view the print preview?");
//        }
//        return ret;
        window.print();
    }
    function onSave() 
	{
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
		
//alert("onSave: checkAllDates() = " + ret);		
		if(ret)
		{
			ret = checkAllIntegers();
		}
        if(ret)
        {
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
		
    }
    
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
		if(ret)
		{
			ret = checkAllIntegers();
		}	
        if(ret)
        {
            ret = confirm("Are you sure you wish to save and close this window?");
        }
		return ret;
    }

    function popupPage(varpage) { //open a new popup window
      var page = "" + varpage;
      windowprops = "height=600,width=700,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=20,left=20";
      var popup=window.open(page, "printlocation", windowprops);
      if (popup != null) {
        if (popup.opener == null) {
          popup.opener = self;
        }
      }
    }

/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=3100;

    function isInteger(s)
	{
        var i;
//alert("isInteger: s =" + s);			
		
        for (i = 0; i < s.length; i++)
		{
            // Check that current character is number.
            var c = s.charAt(i);
            if (((c < "0") || (c > "9")))
			{
				return false;
			}
        }
        // All characters are numbers.
        return true;
    }

    function isCurrency(s)
	{
        var i;
//alert("isInteger: s =" + s);			
		
        for (i = 0; i < s.length; i++)
		{
            // Check that current character is number.
            var c = s.charAt(i);
//alert("isInteger: c[" + i + "] =" + c);			
            if (  ((c < "0") || (c > "9"))  &&  c != "."  )
			{
				return false;
			}
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



	function checkAllIntegers()
	{
        var ret = true;
		
        if(!isInteger(document.forms[0].year.value))
		{
	        alert ("You must type in a number in the field.");
		
			document.forms[0].year.value = "";
			document.forms[0].year.focus();
            ret = false;
        }
		else if(!isInteger(document.forms[0].drinksPerDay.value) )
		{
	        alert ("You must type in a number in the field.");
		
			document.forms[0].drinksPerDay.value = "";
			document.forms[0].drinksPerDay.focus();
		
            ret = false;
        }
		else if(!isInteger(document.forms[0].drinksPerWeek.value))
		{
	        alert ("You must type in a number in the field.");
		
			document.forms[0].drinksPerWeek.value = "";
			document.forms[0].drinksPerWeek.focus();
		
            ret = false;
        }
		else if(!isInteger(document.forms[0].drinksPerMonth.value))
		{
	        alert ("You must type in a number in the field.");
		
			document.forms[0].drinksPerMonth.value = "";
			document.forms[0].drinksPerMonth.focus();
		
            ret = false;
        }
		else if(!isInteger(document.forms[0].yearsOfEducation.value))
		{
	        alert ("You must type in a number in the field.");
		
			document.forms[0].yearsOfEducation.value = "";
			document.forms[0].yearsOfEducation.focus();
		
            ret = false;
        }
		else if(!isInteger(document.forms[0].howLongEmployed.value))
		{
	        alert ("You must type in a number in the field.");
		
			document.forms[0].howLongEmployed.value = "";
			document.forms[0].howLongEmployed.focus();
		
            ret = false;
        }
		else if(!isInteger(document.forms[0].howLongUnemployed.value))
		{
	        alert ("You must type in a number in the field.");
		
			document.forms[0].howLongUnemployed.value = "";
			document.forms[0].howLongUnemployed.focus();
		
            ret = false;
        }
		else if(!isCurrency(document.forms[0].amtOwing.value))
		{
	        alert ("You must type in a currency amount in the field.");
		
			document.forms[0].amtOwing.value = "";
			document.forms[0].amtOwing.focus();
		
            ret = false;
        }
		else if(!isCurrency(document.forms[0].howMuchYouReceive.value))
		{
	        alert ("You must type in a currency amount in the field.");
		
			document.forms[0].howMuchYouReceive.value = "";
			document.forms[0].howMuchYouReceive.focus();
		
            ret = false;
        }
		
        return ret;
	}

    function checkAllDates()
    {
        var b = true;
        if(valDate(document.forms[0].dateAssessment)==false)
		{
            b = false;
        }
		else if(valDate(document.forms[0].dateEnteredSeaton)==false)
		{
            b = false;
        }
		else if(valDate(document.forms[0].dateExitedSeaton)==false)
		{
            b = false;
        }
		else if(valDate(document.forms[0].dateLastDoctor1Contact)==false)
		{
            b = false;
        }
		else if(valDate(document.forms[0].dateLastDoctor2Contact)==false)
		{
            b = false;
        }
		else if(valDate(document.forms[0].dateLivedThere)==false)
		{
            b = false;
        }
		else if(valDate(document.forms[0].dateLastContact1)==false)
		{
            b = false;
        }
		else if(valDate(document.forms[0].dateLastContact2)==false)
		{
            b = false;
        }
		else if(valDate(document.forms[0].dateLastContact3)==false)
		{
            b = false;
        }
		else if(valDate(document.forms[0].dateLastContact4)==false)
		{
            b = false;
        }
		else if(valDate(document.forms[0].whenMadeAppforOtherIncome)==false)
		{
            b = false;
        }

        return b;
    }
	
	function clickCheckBox(fieldObj)
	{
		if(fieldObj.checked)
		{
			fieldObj.checked = false;
		}
		else if(!fieldObj.checked)
		{
			fieldObj.checked = true;
		}
	}	
	function clickRadio(fieldObj)
	{
		if(fieldObj.checked)
		{
			fieldObj.checked = false;
		}
		else if(!fieldObj.checked)
		{
			fieldObj.checked = true;
		}
	}	
	
</script>


<body>

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


	<table width="100%" border="0">
		<tr>
			<td width="5%"></td>

			<td>
			<table width="95%" border="0">
				<tr>
					<td colspan="3" class="style63">INTAKE B. COUNSELLOR
					ASSESSMENT</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td class="style76">Date of assessment (YYYY/MM/DD): <br>
					<input type="text" name="dateAssessment"
						value="<%=props.getProperty("dateAssessment", "")%>"></td>
					<td class="style76">Assessment start time: <br>
					<input type="text" name="assessStartTime"
						value="<%=props.getProperty("assessStartTime", "")%>"> am
					/ pm</br>
					</td>
					<td class="style76">Date client entered Seaton House
					(YYYY/MM/DD): &nbsp; <input type="text" name="dateEnteredSeaton"
						value="<%=props.getProperty("dateEnteredSeaton", "")%>"> <br>
					Date client exited Seaton House (YYYY/MM/DD):
					&nbsp;&nbsp;&nbsp;&nbsp; <input type="text" name="dateExitedSeaton"
						value="<%=props.getProperty("dateExitedSeaton", "")%>"></td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="3" align="center" class="style51">CLIENT
					INFORMATION</td>
				</tr>
				<tr>
					<td align="left" class="style76">Client's Surname <input
						type="text" name="clientSurname"
						value="<%=props.getProperty("clientSurname", "")%>"></td>
					<td align="left" class="style76">Client's First Name <input
						type="text" name="clientFirstName"
						value="<%=props.getProperty("clientFirstName", "")%>"></td>
					<td align="left" class="style76">Date of Birth<br>
					(MM/DD/YYYY)<br>
					<select name="month">
						<option value=""
							<%=props.getProperty("month", "").equals("")?"selected":""%>></option>
						<option value="1"
							<%=props.getProperty("month", "").equals("1")?"selected":""%>>1</option>
						<option value="2"
							<%=props.getProperty("month", "").equals("2")?"selected":""%>>2</option>
						<option value="3"
							<%=props.getProperty("month", "").equals("3")?"selected":""%>>3</option>
						<option value="4"
							<%=props.getProperty("month", "").equals("4")?"selected":""%>>4</option>
						<option value="5"
							<%=props.getProperty("month", "").equals("5")?"selected":""%>>5</option>
						<option value="6"
							<%=props.getProperty("month", "").equals("6")?"selected":""%>>6</option>
						<option value="7"
							<%=props.getProperty("month", "").equals("7")?"selected":""%>>7</option>
						<option value="8"
							<%=props.getProperty("month", "").equals("8")?"selected":""%>>8</option>
						<option value="9"
							<%=props.getProperty("month", "").equals("9")?"selected":""%>>9</option>
						<option value="10"
							<%=props.getProperty("month", "").equals("10")?"selected":""%>>10</option>
						<option value="11"
							<%=props.getProperty("month", "").equals("11")?"selected":""%>>11</option>
						<option value="12"
							<%=props.getProperty("month", "").equals("12")?"selected":""%>>12</option>
					</select> <select name="day">
						<option value=""
							<%=props.getProperty("day", "").equals("")?"selected":""%>></option>
						<option value="1"
							<%=props.getProperty("day", "").equals("1")?"selected":""%>>1</option>
						<option value="2"
							<%=props.getProperty("day", "").equals("2")?"selected":""%>>2</option>
						<option value="3"
							<%=props.getProperty("day", "").equals("3")?"selected":""%>>3</option>
						<option value="4"
							<%=props.getProperty("day", "").equals("4")?"selected":""%>>4</option>
						<option value="5"
							<%=props.getProperty("day", "").equals("5")?"selected":""%>>5</option>
						<option value="6"
							<%=props.getProperty("day", "").equals("6")?"selected":""%>>6</option>
						<option value="7"
							<%=props.getProperty("day", "").equals("7")?"selected":""%>>7</option>
						<option value="8"
							<%=props.getProperty("day", "").equals("8")?"selected":""%>>8</option>
						<option value="9"
							<%=props.getProperty("day", "").equals("9")?"selected":""%>>9</option>
						<option value="10"
							<%=props.getProperty("day", "").equals("10")?"selected":""%>>10</option>
						<option value="11"
							<%=props.getProperty("day", "").equals("11")?"selected":""%>>11</option>
						<option value="12"
							<%=props.getProperty("day", "").equals("12")?"selected":""%>>12</option>
						<option value="13"
							<%=props.getProperty("day", "").equals("13")?"selected":""%>>13</option>
						<option value="14"
							<%=props.getProperty("day", "").equals("14")?"selected":""%>>14</option>
						<option value="15"
							<%=props.getProperty("day", "").equals("15")?"selected":""%>>15</option>
						<option value="16"
							<%=props.getProperty("day", "").equals("16")?"selected":""%>>16</option>
						<option value="17"
							<%=props.getProperty("day", "").equals("17")?"selected":""%>>17</option>
						<option value="18"
							<%=props.getProperty("day", "").equals("18")?"selected":""%>>18</option>
						<option value="19"
							<%=props.getProperty("day", "").equals("19")?"selected":""%>>19</option>
						<option value="20"
							<%=props.getProperty("day", "").equals("20")?"selected":""%>>20</option>
						<option value="21"
							<%=props.getProperty("day", "").equals("21")?"selected":""%>>21</option>
						<option value="22"
							<%=props.getProperty("day", "").equals("22")?"selected":""%>>22</option>
						<option value="23"
							<%=props.getProperty("day", "").equals("23")?"selected":""%>>23</option>
						<option value="24"
							<%=props.getProperty("day", "").equals("24")?"selected":""%>>24</option>
						<option value="25"
							<%=props.getProperty("day", "").equals("25")?"selected":""%>>25</option>
						<option value="26"
							<%=props.getProperty("day", "").equals("26")?"selected":""%>>26</option>
						<option value="27"
							<%=props.getProperty("day", "").equals("27")?"selected":""%>>27</option>
						<option value="28"
							<%=props.getProperty("day", "").equals("28")?"selected":""%>>28</option>
						<option value="29"
							<%=props.getProperty("day", "").equals("29")?"selected":""%>>29</option>
						<option value="30"
							<%=props.getProperty("day", "").equals("30")?"selected":""%>>30</option>
						<option value="31"
							<%=props.getProperty("day", "").equals("31")?"selected":""%>>31</option>
					</select> <input type="text" name="year" size="7" maxlength="4"
						value="<%=props.getProperty("year", "")%>"></td>
				</tr>
			</table>
			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">LANGUAGES
					SPOKEN</td>
				</tr>
				<tr>
					<td colspan="1" align="left" class="style76"><input
						type="checkbox" name="cbox_speakEnglish" value="Y"
						<%=(props.getProperty("cbox_speakEnglish", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_speakEnglish);">English</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="checkbox" name="cbox_speakFrench" value="Y"
						<%=(props.getProperty("cbox_speakFrench", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_speakFrench);">French</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="checkbox" name="cbox_speakSpanish" value="Y"
						<%=(props.getProperty("cbox_speakSpanish", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_speakSpanish);">Spanish</span>
					</td>

					<td colspan="3" align="left" class="style76"><input
						type="checkbox" name="cbox_speakOther" value="Y"
						<%=(props.getProperty("cbox_speakOther", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_speakOther);">Other:
					</span> <input type="text" name="speakOther" size="55"
						value="<%=props.getProperty("speakOther", "")%>"></td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td align="center" class="style51">NOTES FOR COMPLETING THIS
					INTAKE FORM</td>
				</tr>
				<tr>
					<td align="left" class="style76">Counsellor:<br>
					1. Please review Assessment A, including 'Assistance Required'
					section to respond to client's request for assistance.<br>
					2. Each numbered section of this intake corresponds to a
					Case-Management page (attached). After completing each section,
					enter complete the relevant case management page, including a
					timeline for review of client goals.</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td align="center" class="style51">INTRODUCTION</td>
				</tr>
				<tr>
					<td align="left" class="style76">Counsellor:<br>
					1.Please introduce yourself to your client: give your name and some
					information about yourself (e.g., how long you have worked at
					Seaton House).<br>
					2. Please read the following statement to the client:"We are trying
					to ensure that you and others that stay at Seaton House have access
					to health care and other services if you need it. Please answer
					these questions so that we can better help you."</td>
				</tr>
				<tr>
					<td align="left" class="style76">Question 1. How did you hear
					about Seaton House? &nbsp;&nbsp;&nbsp; <input type="text"
						name="howHearAboutSeaton" size="55"
						value="<%=props.getProperty("howHearAboutSeaton", "")%>">

					</td>
				</tr>
				<tr>
					<td align="left" class="style76">Question 2. Where were you
					before coming to Seaton House? &nbsp;&nbsp;&nbsp; <input
						type="text" name="whereBeforeSeaton" size="55"
						value="<%=props.getProperty("whereBeforeSeaton", "")%>"></td>
				</tr>

			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="4" align="center" class="style51">1.
					IDENTIFICATION</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">NOTE TO
					COUNSELLORS:<br>
					ID is required for some internal referrals. If SIN, Health Card or
					Birth Certificate is missing, please begin process of securing a
					replacement</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">Is photocopy of
					ID included in this file? &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input
						type="radio" name="radio_hasIDInFile" value="yes"
						<%=(props.getProperty("radio_hasIDInFile", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasIDInFile[0]);">Yes</span>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="radio"
						name="radio_hasIDInFile" value="no"
						<%=(props.getProperty("radio_hasIDInFile", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasIDInFile[1]);">No
					-->&nbsp;&nbsp; give status of securing ID in Case Management
					Notes*</span></td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">1. What
					identification do you need assistance with?</td>
				</tr>
				<tr>
				<tr>
					<td colspan="1" align="left" class="style76">
					<table width="100%" border="0">
						<tr>
							<td colspan="1" align="left" class="style76"><input
								type="checkbox" name="cbox_assistWithSINCard" value="Y"
								<%=(props.getProperty("cbox_assistWithSINCard", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_assistWithSINCard);">SIN
							Card:</span> <br>
							<input type="checkbox" name="cbox_assistWithImmigrant" value="Y"
								<%=(props.getProperty("cbox_assistWithImmigrant", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_assistWithImmigrant);">Landed
							Immigrant</span></td>
							<td colspan="1" align="left" class="style76"><input
								type="checkbox" name="cbox_assistWithHealthCard" value="Y"
								<%=(props.getProperty("cbox_assistWithHealthCard", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_assistWithHealthCard);">Ontario
							Health Card</span><br>
							<input type="checkbox" name="cbox_assistWithRefugee" value="Y"
								<%=(props.getProperty("cbox_assistWithRefugee", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_assistWithRefugee);">Convention
							Refugee</span></td>
							<td colspan="1" align="left" class="style76"><input
								type="checkbox" name="cbox_assistWithBirthCert" value="Y"
								<%=(props.getProperty("cbox_assistWithBirthCert", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_assistWithBirthCert);">Birth
							Certificate</span><br>
							<input type="checkbox" name="cbox_assistWithNone" value="Y"
								<%=(props.getProperty("cbox_assistWithNone", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_assistWithNone);">None</span>
							</td>
							<td colspan="1" align="left" class="style76"><input
								type="checkbox" name="cbox_assistWithCitizenCard" value="Y"
								<%=(props.getProperty("cbox_assistWithCitizenCard", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_assistWithCitizenCard);">Citizenship
							Card</span><br>
							<input type="checkbox" name="cbox_assistWithOther" value="Y"
								<%=(props.getProperty("cbox_assistWithOther", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_assistWithOther);">Other</span>
							&nbsp;&nbsp; <input type="text" name="assistWithOther" size="35"
								value="<%=props.getProperty("assistWithOther", "")%>"></td>
						</tr>
						<tr>
							<td colspan="4" align="left" valign="top" class="style76">
							Comments:<br>
							<textArea name="commentsOnID" cols="75"><%=props.getProperty("commentsOnID", "")%></textArea>

							</td>
						</tr>

					</table>
					</td>
				</tr>
			</table>
			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">2. PHYSICAL AND
					MENTAL HEALTH</td>
				</tr>
				<tr>
					<td align="left" class="style76">
					<table width="100%" border="0">
						<tr>
							<td colspan="2" align="left" class="style76">1. Do you have
							any health care coverage?</td>
							<td colspan="1" align="left" class="style76"><input
								type="radio" name="radio_haveHealthCoverage" value="no"
								<%=(props.getProperty("radio_haveHealthCoverage","")).equalsIgnoreCase("no")?"checked":""%>>

							<span
								onClick="javascript:clickRadio(document.forms[0].radio_haveHealthCoverage[0]);">No</span>
							</td>
							<td colspan="1" align="left" class="style76"><input
								type="radio" name="radio_haveHealthCoverage" value="yes"
								<%=(props.getProperty("radio_haveHealthCoverage","")).equalsIgnoreCase("yes")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_haveHealthCoverage[1]);">Yes</span>
							</td>
							<td colspan="1" align="left" class="style76">If yes, what
							type:</td>
							<td colspan="1" align="left" class="style76"><input
								type="checkbox" name="cbox_haveOHIP" value="Y"
								<%=(props.getProperty("cbox_haveOHIP", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_haveOHIP);">OHIP</span>
							</td>
						</tr>
						<tr>
							<td colspan="5" align="left" class="style76">&nbsp;</td>

							<td align="left" class="style76"><input type="checkbox"
								name="cbox_haveODSP" value="Y"
								<%=(props.getProperty("cbox_haveODSP", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_haveODSP);">ODSP</span>
							</td>
						</tr>

						<tr>
							<td colspan="5" align="left" class="style76">&nbsp;</td>

							<td align="left" class="style76"><input type="checkbox"
								name="cbox_haveODB" value="Y"
								<%=(props.getProperty("cbox_haveODB", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_haveODB);">ODB
							Card</span></td>
						</tr>
						<tr>
							<td colspan="5" align="left" class="style76">&nbsp;</td>

							<td colspan="1" align="left" class="style76"><input
								type="checkbox" name="cbox_haveOther1" value="Y"
								<%=(props.getProperty("cbox_haveOther1", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_haveOther1);">Other
							(specify)</span> <input type="text" name="haveOther" size="27"
								value="<%=props.getProperty("haveOther", "")%>"></td>
						</tr>
					</table>
					<table width="100%" border="0">

						<tr>
							<td colspan="6" class="style76">2. Has a health professional
							ever diagnosed you with or do you believe you have a problem with
							mental health?</td>
						</tr>

						<tr>
							<td colspan="2" class="style76">&nbsp;</td>

							<td colspan="1" class="style76"><input type="radio"
								name="radio_haveMentalProblem" value="yes"
								<%=(props.getProperty("radio_haveMentalProblem", "")).equalsIgnoreCase("yes")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_haveMentalProblem[0]);">Yes</span>
							</td>
							<td colspan="1" class="style76"><input type="radio"
								name="radio_haveMentalProblem" value="no"
								<%=(props.getProperty("radio_haveMentalProblem", "")).equalsIgnoreCase("no")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_haveMentalProblem[1]);">No</span>
							</td>
							<td colspan="1" class="style76"><input type="radio"
								name="radio_haveMentalProblem" value="dontknow"
								<%=(props.getProperty("radio_haveMentalProblem", "")).equalsIgnoreCase("dontknow")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_haveMentalProblem[2]);">Don't
							know</span></td>
							<td colspan="1" class="style76"><input type="radio"
								name="radio_haveMentalProblem" value="refuse"
								<%=(props.getProperty("radio_haveMentalProblem", "")).equalsIgnoreCase("refuse")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_haveMentalProblem[3]);">Refuse
							to answer</span></td>
						</tr>

					</table>
					<table width="100%" border="1">
						<tr>
							<td colspan="6" class="style76">3. Do you have any of the
							following conditions? (read list and check all that apply)</td>
						</tr>

						<tr>
							<td colspan="2" class="style76"><input type="checkbox"
								name="cbox_haveSchizophrenia" value="Y"
								<%=(props.getProperty("cbox_haveSchizophrenia", "")).equalsIgnoreCase("Y")?"checked":""%>>
							Schizophrenia <br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_haveSchizophrenia);">If
							yes, is this being cared for by a doctor?</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForSchizophrenia" value="yes"
								<%=(props.getProperty("radio_caredForSchizophrenia", "")).equalsIgnoreCase("yes")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForSchizophrenia[0]);">Yes
							--> Q4</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForSchizophrenia" value="no"
								<%=(props.getProperty("radio_caredForSchizophrenia", "")).equalsIgnoreCase("no")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForSchizophrenia[1]);">No
							--> Q5</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForSchizophrenia" value="dontKnow"
								<%=(props.getProperty("radio_caredForSchizophrenia", "")).equalsIgnoreCase("dontKnow")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForSchizophrenia[2]);">Don't
							know</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForSchizophrenia" value="refuse"
								<%=(props.getProperty("radio_caredForSchizophrenia", "")).equalsIgnoreCase("refuse")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForSchizophrenia[3]);">Refuse
							to answer</span></td>
						</tr>

						<tr>
							<td colspan="2" class="style76"><input type="checkbox"
								name="cbox_haveManic" value="Y"
								<%=(props.getProperty("cbox_haveManic", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_haveManic);">Manic
							depression / bipolar disorder</span> <br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							If yes, is this being cared for by a doctor?</td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForManic" value="yes"
								<%=(props.getProperty("radio_caredForManic", "")).equalsIgnoreCase("yes")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForManic[0]);">Yes
							--> Q4</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForManic" value="no"
								<%=(props.getProperty("radio_caredForManic", "")).equalsIgnoreCase("no")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForManic[1]);">No
							--> Q5</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForManic" value="dontKnow"
								<%=(props.getProperty("radio_caredForManic", "")).equalsIgnoreCase("dontKnow")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForManic[2]);">Don't
							know</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForManic" value="refuse"
								<%=(props.getProperty("radio_caredForManic", "")).equalsIgnoreCase("refuse")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForManic[3]);">Refuse
							to answer</span></td>
						</tr>
						<tr>
							<td colspan="2" class="style76"><input type="checkbox"
								name="cbox_haveDepression" value="Y"
								<%=(props.getProperty("cbox_haveDepression", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_haveDepression);">Depression
							</span><br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							If yes, is this being cared for by a doctor?</td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForDepression" value="yes"
								<%=(props.getProperty("radio_caredForDepression", "")).equalsIgnoreCase("yes")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForDepression[0]);">Yes
							--> Q4</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForDepression" value="no"
								<%=(props.getProperty("radio_caredForDepression", "")).equalsIgnoreCase("no")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForDepression[1]);">No
							--> Q5</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForDepression" value="dontKnow"
								<%=(props.getProperty("radio_caredForDepression", "")).equalsIgnoreCase("dontKnow")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForDepression[2]);">Don't
							know</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForDepression" value="refuse"
								<%=(props.getProperty("radio_caredForDepression", "")).equalsIgnoreCase("refuse")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForDepression[3]);">Refuse
							to answer</span></td>
						</tr>
						<tr>
							<td colspan="2" class="style76"><input type="checkbox"
								name="cbox_haveAnxiety" value="Y"
								<%=(props.getProperty("cbox_haveAnxiety", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_haveAnxiety);">Anxiety
							disorder (panic attacks, generalized anxiety)</span> <br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							If yes, is this being cared for by a doctor?</td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForAnxiety" value="yes"
								<%=(props.getProperty("radio_caredForAnxiety", "")).equalsIgnoreCase("yes")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForAnxiety[0]);">Yes
							--> Q4</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForAnxiety" value="no"
								<%=(props.getProperty("radio_caredForAnxiety", "")).equalsIgnoreCase("no")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForAnxiety[1]);">No
							--> Q5</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForAnxiety" value="dontKnow"
								<%=(props.getProperty("radio_caredForAnxiety", "")).equalsIgnoreCase("dontKnow")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForAnxiety[2]);">Don't
							know</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForAnxiety" value="refuse"
								<%=(props.getProperty("radio_caredForAnxiety", "")).equalsIgnoreCase("refuse")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForAnxiety[3]);">Refuse
							to answer</span></td>
						</tr>
						<tr>
							<td colspan="2" class="style76"><input type="checkbox"
								name="cbox_haveOther2" value="Y"
								<%=(props.getProperty("cbox_haveOther2", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_haveOther2);">Other
							(specify) </span><br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							If yes, is this being cared for by a doctor?</td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForOther" value="yes"
								<%=(props.getProperty("radio_caredForOther", "")).equalsIgnoreCase("yes")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForOther[0]);">Yes
							--> Q4</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForOther" value="no"
								<%=(props.getProperty("radio_caredForOther", "")).equalsIgnoreCase("no")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForOther[1]);">No
							--> Q5</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForOther" value="dontKnow"
								<%=(props.getProperty("radio_caredForOther", "")).equalsIgnoreCase("dontKnow")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForOther[2]);">Don't
							know</span></td>
							<td colspan="1" valign="bottom" class="style76"><input
								type="radio" name="radio_caredForOther" value="refuse"
								<%=(props.getProperty("radio_caredForOther", "")).equalsIgnoreCase("refuse")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_caredForOther[3]);">Refuse
							to answer</span></td>
						</tr>
					</table>

					<table width="100%" border="1">
						<tr>
							<td colspan="4" class="style76">4a. Doctor's Name & Address
							<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="text"
								name="doctor1NameAddr" size="55"
								value="<%=props.getProperty("doctor1NameAddr", "")%>"></td>
							<td colspan="2" valign="top" class="style76">Phone #
							&nbsp;&nbsp;&nbsp; <input type="text" name="doctor1Phone"
								size="25" value="<%=props.getProperty("doctor1Phone", "")%>">
							</td>
						</tr>
						<tr>
							<td colspan="6" class="style76">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date of last contact with
							Doctor (YYYY/MM/DD)&nbsp;&nbsp;&nbsp; <input type="text"
								name="dateLastDoctor1Contact" size="55"
								value="<%=props.getProperty("dateLastDoctor1Contact", "")%>">
							</td>
						</tr>

					</table>

					<table width="100%" border="1">
						<tr>
							<td colspan="4" class="style76">4b. Doctor's Name & Address
							<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="text"
								name="doctor2NameAddr" size="55"
								value="<%=props.getProperty("doctor2NameAddr", "")%>"></td>
							<td colspan="2" valign="top" class="style76">Phone #
							&nbsp;&nbsp;&nbsp; <input type="text" name="doctor2Phone"
								size="25" value="<%=props.getProperty("doctor2Phone", "")%>">
							</td>
						</tr>
						<tr>
							<td colspan="6" class="style76">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date of last contact with
							Doctor (YYYY/MM/DD)&nbsp;&nbsp;&nbsp; <input type="text"
								name="dateLastDoctor2Contact" size="55"
								value="<%=props.getProperty("dateLastDoctor2Contact", "")%>">
							</td>
						</tr>
					</table>

					<table width="100%" border="1">
						<tr>
							<td colspan="2" class="style76">5. Some clients need
							assistance with medications, such as safe storage, remembering to
							take them, etc. Do you require assistance with any medications?</td>
							<td colspan="2" valign="bottom" class="style76"><input
								type="radio" name="radio_needAssistWithMedication" value="yes"
								<%=(props.getProperty("radio_needAssistWithMedication", "")).equalsIgnoreCase("yes")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_needAssistWithMedication[0]);">Yes
							--> Q6</span></td>
							<td colspan="2" valign="bottom" class="style76"><input
								type="radio" name="radio_needAssistWithMedication" value="no"
								<%=(props.getProperty("radio_needAssistWithMedication", "")).equalsIgnoreCase("no")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_needAssistWithMedication[1]);">No
							--> Section 3</span></td>
						</tr>
					</table>

					<table width="100%" border="1">
						<tr>
							<td colspan="6" class="style76">6. Do you need help with any
							of the following: (read list and check all that apply)</td>
						</tr>
						<tr>
							<td colspan="2" valign="top" class="style76"><input
								type="checkbox" name="cbox_rememberToTakeMedication" value="Y"
								<%=(props.getProperty("cbox_rememberToTakeMedication", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_rememberToTakeMedication);">remembering
							to take medication </span><br>
							<input type="checkbox" name="cbox_getMoreMedication" value="Y"
								<%=(props.getProperty("cbox_getMoreMedication", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_getMoreMedication);">getting
							more medication</span></td>
							<td colspan="2" valign="top" class="style76"><input
								type="checkbox" name="cbox_storeMedication" value="Y"
								<%=(props.getProperty("cbox_storeMedication", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_storeMedication);">safe
							storage of medication </span><br>
							<input type="checkbox" name="cbox_needHelpInOther" value="Y"
								<%=(props.getProperty("cbox_needHelpInOther", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_needHelpInOther);">Other
							(specify)</span></td>
							<td colspan="2" valign="top" class="style76"><input
								type="checkbox" name="cbox_takePrescribedMedication" value="Y"
								<%=(props.getProperty("cbox_takePrescribedMedication", "")).equalsIgnoreCase("no")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_takePrescribedMedication);">taking
							the medications as prescribed</span></td>
						</tr>

						<tr>
							<td colspan="6" align="left" valign="top" class="style76">
							Comments:<br>
							<textArea name="commentsOnNeedHelp" cols="75"><%=props.getProperty("commentsOnNeedHelp", "")%></textArea>

							</td>
						</tr>

					</table>

					</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">3. ADDICTIONS</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">3.1 ALCOHOL USE</td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">1. Do you drink
					alcohol?</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_doYouDrink" value="yes"
						<%=(props.getProperty("radio_doYouDrink", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_doYouDrink[0]);">Yes</span>
					</td>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_doYouDrink" value="no"
						<%=(props.getProperty("radio_doYouDrink", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_doYouDrink[1]);">No
					--> Section 3.2</span></td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_doYouDrink" value="refuse"
						<%=(props.getProperty("radio_doYouDrink", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_doYouDrink[2]);">Refuse
					to answer</span></td>
				</tr>

				<tr>
					<td colspan="1" align="left" class="style76">2. How much and
					how often?</td>
					<td colspan="1" align="left" class="style76"><input
						type="text" name="drinksPerDay" size="3"
						value="<%=props.getProperty("drinksPerDay", "")%>">
					drinks/day</td>
					<td colspan="1" align="left" class="style76"><input
						type="text" name="drinksPerWeek" size="3"
						value="<%=props.getProperty("drinksPerWeek", "")%>">
					drinks/week</td>
					<td colspan="1" align="left" class="style76"><input
						type="text" name="drinksPerMonth" size="3"
						value="<%=props.getProperty("drinksPerMonth", "")%>">
					drinks/month</td>

					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_howMuchDrink" value="uncertain"
						<%=(props.getProperty("radio_howMuchDrink", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_howMuchDrink[0]);">Uncertain</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_howMuchDrink" value="refuse"
						<%=(props.getProperty("radio_howMuchDrink", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_howMuchDrink[1]);">Refuse
					to answer</span></td>
				</tr>


				<tr>
					<td colspan="2" align="left" class="style76">3. Do you ever
					drink any of the following:</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_drinkThese" value="rubbingAlcohol"
						<%=(props.getProperty("radio_drinkThese", "")).equalsIgnoreCase("rubbingAlcohol")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_drinkThese[0]);">Rubbing
					Alcohol</span></td>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_drinkThese" value="chineseCookingWine"
						<%=(props.getProperty("radio_drinkThese", "")).equalsIgnoreCase("chineseCookingWine")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_drinkThese[1]);">Chinese
					cooking wine</span></td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_drinkThese" value="mouthwash"
						<%=(props.getProperty("radio_drinkThese", "")).equalsIgnoreCase("mouthwash")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_drinkThese[2]);">Mouthwash</span>
					</td>
				</tr>

				<tr>
					<td colspan="2" class="style76">4. In the past year have you
					seen a physician about drug or alcohol addiction issues?</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_seenDoctorRegAlcohol" value="yes"
						<%=(props.getProperty("radio_seenDoctorRegAlcohol", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_seenDoctorRegAlcohol[0]);">Yes</span>
					</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_seenDoctorRegAlcohol" value="no"
						<%=(props.getProperty("radio_seenDoctorRegAlcohol", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_seenDoctorRegAlcohol[1]);">No</span>
					</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_seenDoctorRegAlcohol" value="uncertain"
						<%=(props.getProperty("radio_seenDoctorRegAlcohol", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_seenDoctorRegAlcohol[2]);">Uncertain</span>
					</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_seenDoctorRegAlcohol" value="refuse"
						<%=(props.getProperty("radio_seenDoctorRegAlcohol", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_seenDoctorRegAlcohol[3]);">Refuse
					to answer</span></td>
				</tr>

				<tr>
					<td colspan="2" class="style76">5. Do you want help with
					quitting or reducing drinking?</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_wantHelpQuit" value="yes"
						<%=(props.getProperty("radio_wantHelpQuit", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_wantHelpQuit[0]);">Yes</span>
					</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_wantHelpQuit" value="no"
						<%=(props.getProperty("radio_wantHelpQuit", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_wantHelpQuit[1]);">No</span>
					</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_wantHelpQuit" value="uncertain"
						<%=(props.getProperty("radio_wantHelpQuit", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_wantHelpQuit[2]);">Uncertain</span>
					</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_wantHelpQuit" value="refuse"
						<%=(props.getProperty("radio_wantHelpQuit", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_wantHelpQuit[3]);">Refuse
					to answer</span></td>
				</tr>

				<tr>
					<td colspan="6" align="left" valign="top" class="style76">
					Comments:<br>
					<textArea name="commentsOnAlcohol" cols="75"><%=props.getProperty("commentsOnAlcohol", "")%></textArea>

					</td>
				</tr>

				<tr>
					<td colspan="6" align="left" valign="top" class="style76">3.2
					STREET DRUGS & SOLVENT USE</td>
				</tr>

				<tr>
					<td colspan="2" class="style76">1a. Do you use street drugs or
					solvents?</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_useDrugs" value="yes"
						<%=(props.getProperty("radio_useDrugs", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_useDrugs[0]);">Yes</span>
					</td>
					<td colspan="2" valign="top" class="style76"><input
						type="radio" name="radio_useDrugs" value="no"
						<%=(props.getProperty("radio_useDrugs", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_useDrugs[1]);">No
					--> Section 4</span></td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_useDrugs" value="refuse"
						<%=(props.getProperty("radio_useDrugs", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_useDrugs[2]);">Refuse
					to answer</span></td>
				</tr>

				<tr>
					<td colspan="4" class="style76">1b. What type? How often?</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_drugUseFrequency" value="uncertain"
						<%=(props.getProperty("radio_drugUseFrequency", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_drugUseFrequency[0]);">Uncertain</span>
					</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_drugUseFrequency" value="refuse"
						<%=(props.getProperty("radio_drugUseFrequency", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_drugUseFrequency[1]);">Refuse
					to answer</span></td>
				</tr>

				<tr>
					<td colspan="2" class="style76">2. Do you want help with
					quitting or reducing drug or solvent use?</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_wantHelpQuitDrug" value="yes"
						<%=(props.getProperty("radio_wantHelpQuitDrug", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_wantHelpQuitDrug[0]);">Yes</span>
					</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_wantHelpQuitDrug" value="no"
						<%=(props.getProperty("radio_wantHelpQuitDrug", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_wantHelpQuitDrug[1]);">No</span>
					</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_wantHelpQuitDrug" value="uncertain"
						<%=(props.getProperty("radio_wantHelpQuitDrug", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_wantHelpQuitDrug[2]);">Uncertain</span>
					</td>

					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_wantHelpQuitDrug" value="refuse"
						<%=(props.getProperty("radio_wantHelpQuitDrug", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_wantHelpQuitDrug[3]);">Refuse
					to answer</span></td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">Comments:<br>
					<textArea name="commentsOnStreetDrugs" cols="75"><%=props.getProperty("commentsOnStreetDrugs", "")%></textArea>

					</td>
				</tr>


			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">4. HOUSING</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">1. What type of
					housing are you interested in? (e.g., shared, independent, rooming
					house) <br>
					<input type="text" name="housingInterested" size="90"
						value="<%=props.getProperty("housingInterested", "")%>">
					</td>
				</tr>


				<tr>
					<td colspan="3" align="left" class="style76">2. Do you want an
					appointment with a housing counsellor?</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_wantAppmt" value="yes"
						<%=(props.getProperty("radio_wantAppmt", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_wantAppmt[0]);">Yes</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_wantAppmt" value="no"
						<%=(props.getProperty("radio_wantAppmt", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_wantAppmt[1]);">No</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_wantAppmt" value="uncertain"
						<%=(props.getProperty("radio_wantAppmt", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_wantAppmt[2]);">Uncertain</span>
					</td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">3. What was your
					last address? <br>
					<input type="text" name="clientLastAddress" size="90"
						value="<%=props.getProperty("clientLastAddress", "")%>">
					</td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">4. Where was the
					last place you paid rent? (give details if different from Q3) <br>
					<input type="text" name="clientLastAddressPayRent" size="90"
						value="<%=props.getProperty("clientLastAddressPayRent", "")%>">
					</td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">5. How long did
					you live there? Give approximate date
					(YYYY/MM/DD)&nbsp;&nbsp;&nbsp; <input type="text"
						name="dateLivedThere" size="50"
						value="<%=props.getProperty("dateLivedThere", "")%>"></td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">6. Who were you
					living with? (Roommate, family, etc.) &nbsp;&nbsp;&nbsp; <input
						type="text" name="livedWithWhom" size="50"
						value="<%=props.getProperty("livedWithWhom", "")%>"></td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">7. Have you ever
					lived in subsidized housing (If yes, specify)?</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_livedInSubsidized" value="yes"
						<%=(props.getProperty("radio_livedInSubsidized", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_livedInSubsidized[0]);">Yes
					--> Q8</span></td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_livedInSubsidized" value="no"
						<%=(props.getProperty("radio_livedInSubsidized", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_livedInSubsidized[1]);">No
					--> Section 5</span></td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_livedInSubsidized" value="uncertain"
						<%=(props.getProperty("radio_livedInSubsidized", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_livedInSubsidized[2]);">Uncertain</span>
					</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_livedInSubsidized" value="refuse"
						<%=(props.getProperty("radio_livedInSubsidized", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_livedInSubsidized[3]);">Refuse
					to answer</span></td>

				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">8. Do you owe any
					money to a landlord of a subsidized unit?</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_owedRent" value="yes"
						<%=(props.getProperty("radio_owedRent", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_owedRent[0]);">Yes
					--> Q9</span></td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_owedRent" value="no"
						<%=(props.getProperty("radio_owedRent", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_owedRent[1]);">No
					--> Section 5</span></td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_owedRent" value="uncertain"
						<%=(props.getProperty("radio_owedRent", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_owedRent[2]);">Uncertain</span>
					</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_owedRent" value="refuse"
						<%=(props.getProperty("radio_owedRent", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_owedRent[3]);">Refuse
					to answer</span></td>

				</tr>

				<tr>
					<td colspan="4" align="left" class="style76">9. Where do you
					owe money? &nbsp;&nbsp;&nbsp; <input type="text"
						name="whereOweRent" size="50"
						value="<%=props.getProperty("whereOweRent", "")%>"></td>
					<td colspan="2" align="left" class="style76">Amount $:
					&nbsp;&nbsp;&nbsp; <input type="text" name="amtOwing" size="5"
						value="<%=props.getProperty("amtOwing", "")%>"></td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">Comments:<br>
					<textArea name="commentsOnHousing" cols="75"><%=props.getProperty("commentsOnHousing", "")%></textArea>
					</td>
				</tr>
			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">5. EDUCATION</td>
				</tr>
				<tr>
					<td colspan="5" align="left" class="style76">1. Excluding
					kindergarten, how many years of elementary and high school have you
					successfully completed?</td>
					<td width="30%" colspan="1" align="left" class="style76"><input
						type="text" name="yearsOfEducation" size="5"
						value="<%=props.getProperty("yearsOfEducation", "")%>"></td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">

					<table width="100%" border="0">

						<tr>
							<td class="style76">Do you have any of the following:</td>
						</tr>
						<tr>
							<td colspan="1" align="left" class="style76"><input
								type="checkbox" name="cbox_haveHighSchool" value="Y"
								<%=(props.getProperty("cbox_haveHighSchool", 	"")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_haveHighSchool);">High
							School diploma</span></td>
							<td colspan="1" align="left" class="style76"><input
								type="checkbox" name="cbox_haveCollege" value="Y"
								<%=(props.getProperty("cbox_haveCollege", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_haveCollege);">College
							diploma</span></td>
							<td colspan="2" align="left" class="style76"><input
								type="checkbox" name="cbox_haveUniversity" value="Y"
								<%=(props.getProperty("cbox_haveUniversity", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_haveUniversity);">University
							undergraduate degree (BA, BSc etc)</span></td>
							<td colspan="2" valign="top" class="style76"><input
								type="checkbox" name="cbox_haveOther3" value="Y"
								<%=(props.getProperty("cbox_haveOther3", "")).equalsIgnoreCase("Y")?"checked":""%>>
							<span
								onClick="javascript:clickCheckBox(document.forms[0].cbox_haveOther3);">Other</span>
							</td>

						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">2. Are you
					interested in going back to school?</td>
					<td width="9%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_interestBackToSchool" value="yes"
						<%=(props.getProperty("radio_interestBackToSchool", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_interestBackToSchool[0]);">Yes</span>
					</td>
					<td width="9%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_interestBackToSchool" value="no"
						<%=(props.getProperty("radio_interestBackToSchool", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_interestBackToSchool[1]);">No</span>
					</td>
					<td width="13%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_interestBackToSchool" value="uncertain"
						<%=(props.getProperty("radio_interestBackToSchool", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_interestBackToSchool[2]);">Uncertain</span>
					</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_interestBackToSchool" value="refuse"
						<%=(props.getProperty("radio_interestBackToSchool", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_interestBackToSchool[3]);">Refuse
					to answer</span></td>

				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">3. Do you require
					a referral for ESL classes?</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_requireReferralToESL" value="yes"
						<%=(props.getProperty("radio_requireReferralToESL", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_requireReferralToESL[0]);">Yes</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_requireReferralToESL" value="no"
						<%=(props.getProperty("radio_requireReferralToESL", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_requireReferralToESL[1]);">No</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_requireReferralToESL" value="uncertain"
						<%=(props.getProperty("radio_requireReferralToESL", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_requireReferralToESL[2]);">Uncertain</span>
					</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_requireReferralToESL" value="refuse"
						<%=(props.getProperty("radio_requireReferralToESL", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_requireReferralToESL[3]);">Refuse
					to answer</span></td>

				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">Comments:<br>
					<textArea name="commentsOnEducation" cols="75"><%=props.getProperty("commentsOnEducation", "")%></textArea>
					</td>
				</tr>

			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">6. EMPLOYMENT</td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">1. Are you
					currently employed?</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_currentlyEmployed" value="yes"
						<%=(props.getProperty("radio_currentlyEmployed", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_currentlyEmployed[0]);">Yes</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_currentlyEmployed" value="no"
						<%=(props.getProperty("radio_currentlyEmployed", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_currentlyEmployed[1]);">No
					--> Skip to Q3</span></td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_currentlyEmployed" value="uncertain"
						<%=(props.getProperty("radio_currentlyEmployed", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_currentlyEmployed[2]);">Uncertain</span>
					</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_currentlyEmployed" value="refuse"
						<%=(props.getProperty("radio_currentlyEmployed", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_currentlyEmployed[3]);">Refuse
					to answer</span></td>

				</tr>


				<tr>
					<td colspan="3" align="left" class="style76">2. If yes, how
					long have you been employed? &nbsp;&nbsp;&nbsp; <input type="text"
						name="howLongEmployed" size="5"
						value="<%=props.getProperty("howLongEmployed", "")%>"></td>
					<td colspan="3" align="left" class="style76">If no, how long
					have you been unemployed? &nbsp;&nbsp;&nbsp; <input type="text"
						name="howLongUnemployed" size="5"
						value="<%=props.getProperty("howLongUnemployed", "")%>">
					</td>

				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">3. What is your
					usual occupation? &nbsp;&nbsp;&nbsp; <input type="text"
						name="usualOccupation" size="50"
						value="<%=props.getProperty("usualOccupation", "")%>"></td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">4. Are you
					interested in Employment Counselling or Job Training?</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_interestedInTraining" value="yes"
						<%=(props.getProperty("radio_interestedInTraining", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_interestedInTraining[0]);">Yes</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_interestedInTraining" value="no"
						<%=(props.getProperty("radio_interestedInTraining", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_interestedInTraining[1]);">No</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_interestedInTraining" value="uncertain"
						<%=(props.getProperty("radio_interestedInTraining", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_interestedInTraining[2]);">Uncertain</span>
					</td>
					<td colspan="1" valign="top" class="style76"><input
						type="radio" name="radio_interestedInTraining" value="refuse"
						<%=(props.getProperty("radio_interestedInTraining", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_interestedInTraining[3]);">Refuse
					to answer</span></td>

				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">Comments:<br>
					<textArea name="commentsOnEmployment" cols="75"><%=props.getProperty("commentsOnEmployment", "")%></textArea>

					</td>
				</tr>

			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="9" align="center" class="style51">7. FINANCIAL</td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">1. What has been
					your main source of income in the past 12 months (Please state
					source and monthly amount) <br>
					<input type="text" name="mainSourceOfIncome" size="70"
						value="<%=props.getProperty("mainSourceOfIncome", "")%>">
					</td>
				</tr>

				<tr>
					<td colspan="1" align="left" class="style76"><input
						type="checkbox" name="cbox_OW" value="Y"
						<%=(props.getProperty("cbox_OW", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_OW);">OW:</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="checkbox" name="cbox_ODSP" value="Y"
						<%=(props.getProperty("cbox_ODSP", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_ODSP);">ODSP</span>
					</td>
					<td colspan="2" align="left" class="style76"><input
						type="checkbox" name="cbox_employment" value="Y"
						<%=(props.getProperty("cbox_employment", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_employment);">Employment
					--> Q2</span></td>
					<td colspan="1" align="left" class="style76"><input
						type="checkbox" name="cbox_UI" value="Y"
						<%=(props.getProperty("cbox_UI", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_UI);">UI
					--> Q2</span></td>
					<td colspan="1" align="left" class="style76"><input
						type="checkbox" name="cbox_OAS" value="Y"
						<%=(props.getProperty("cbox_OAS", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_OAS);">OAS
					--> Q2</span></td>
					<td colspan="1" align="left" class="style76"><input
						type="checkbox" name="cbox_CPP" value="Y"
						<%=(props.getProperty("cbox_CPP", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_CPP);">CPP
					--> Q2</span></td>
					<td colspan="2" align="left" class="style76"><input
						type="checkbox" name="cbox_other" value="Y"
						<%=(props.getProperty("cbox_other", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_other);">Other
					(specify) --> Q2</span></td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">2. Approximately
					how much do you receive: $ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input
						type="text" name="howMuchYouReceive" size="7"
						value="<%=props.getProperty("howMuchYouReceive", "")%>">
					</td>
				</tr>

				<tr>
					<td colspan="5" align="left" class="style76">3. Do you have a
					Public Trustee?</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_havePublicTrustee" value="yes"
						<%=(props.getProperty("radio_havePublicTrustee", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_havePublicTrustee[0]);">Yes
					--> Q4</span></td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_havePublicTrustee" value="no"
						<%=(props.getProperty("radio_havePublicTrustee", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_havePublicTrustee[1]);">No</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_havePublicTrustee" value="uncertain"
						<%=(props.getProperty("radio_havePublicTrustee", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_havePublicTrustee[2]);">Uncertain</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_havePublicTrustee" value="refuse"
						<%=(props.getProperty("radio_havePublicTrustee", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_havePublicTrustee[3]);">Refuse
					to answer</span></td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">4. Name, address
					and phone # of Public Trustee <br>
					<input type="text" name="publicTrusteeInfo" size="120"
						value="<%=props.getProperty("publicTrusteeInfo", "")%>">

					</td>
				</tr>

				<tr>
					<td colspan="5" align="left" class="style76">5. Are you
					entitled to any other type(s) of income?</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_entitledToOtherIncome" value="yes"
						<%=(props.getProperty("radio_entitledToOtherIncome", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_entitledToOtherIncome[0]);">Yes
					--></span></td>
					<td colspan="2" align="left" class="style76">Type: <input
						type="text" name="typeOfIncome" size="35"
						value="<%=props.getProperty("typeOfIncome", "")%>"></td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_entitledToOtherIncome" value="no"
						<%=(props.getProperty("radio_entitledToOtherIncome", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_entitledToOtherIncome[1]);">No</span>
					</td>
				</tr>
				<tr>
					<td colspan="9" class="style76">
					<table width="95%" border="0">
						<tr>
							<td colspan="9" class="style76">6. Have you ever made an
							application for any other type of income?</td>
						</tr>

						<tr>
							<td colspan="1" align="left" class="style76"><input
								type="radio" name="radio_everMadeAppforOtherIncome" value="no"
								<%=(props.getProperty("radio_everMadeAppforOtherIncome", "")).equalsIgnoreCase("no")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_everMadeAppforOtherIncome[0]);">No</span>
							</td>

							<td colspan="1" align="left" class="style76"><nobr> <input
								type="radio" name="radio_everMadeAppforOtherIncome" value="yes"
								<%=(props.getProperty("radio_everMadeAppforOtherIncome", "")).equalsIgnoreCase("yes")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_everMadeAppforOtherIncome[1]);">Yes
							--></span></nobr></td>
							<td colspan="4" align="left" class="style76">Type: <input
								type="text" name="everMadeAppforOtherIncome"
								value="<%=props.getProperty("everMadeAppforOtherIncome", "")%>">
							</td>
							<td colspan="3" align="left" class="style76"><nobr>When
							(YYYY/MM/DD): <input type="text" name="whenMadeAppforOtherIncome"
								value="<%=props.getProperty("whenMadeAppforOtherIncome", "")%>">
							</nobr></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">Comments:<br>
					<textArea name="commentsOnFinance" cols="75"><%=props.getProperty("commentsOnFinance", "")%></textArea>
					</td>
				</tr>

			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="9" align="center" class="style51">10. LEGAL
					ISSUES</td>
				</tr>

				<tr>
					<td colspan="4" align="left" class="style76">1. Have you ever
					been incarcerated?</td>
					<td colspan="2" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_everBeenJailed" value="yes"
						<%=(props.getProperty("radio_everBeenJailed", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_everBeenJailed[0]);">Yes
					--> Q2</span> </nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_everBeenJailed" value="no"
						<%=(props.getProperty("radio_everBeenJailed", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_everBeenJailed[1]);">No
					--> Q3</span> </nobr></td>

					<td colspan="2" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_everBeenJailed" value="refuse"
						<%=(props.getProperty("radio_everBeenJailed", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_everBeenJailed[2]);">Refuse
					to answer</span> </nobr></td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">2. Give brief
					history of incarceration(s) (date, reason, location)<br>
					<textArea name="historyOfJail" cols="75"><%=props.getProperty("historyOfJail", "")%></textArea>
					</td>
				</tr>

				<tr>
					<td colspan="9">
					<table width="100%" border="0">
						<tr>
							<td colspan="4" align="left" class="style76">3. Do you have
							any current outstanding legal issues that you need assistance
							with?</td>
							<td colspan="1" align="left" valign="top" class="style76"><nobr>
							<input type="radio" name="radio_needAssistInLegal" value="yes"
								<%=(props.getProperty("radio_needAssistInLegal", "")).equalsIgnoreCase("yes")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_needAssistInLegal[0]);">Yes</span>
							</nobr></td>
							<td colspan="2" align="left" valign="top" class="style76"><nobr>
							<input type="radio" name="radio_needAssistInLegal" value="no"
								<%=(props.getProperty("radio_needAssistInLegal", "")).equalsIgnoreCase("no")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_needAssistInLegal[1]);">No
							--> Section11</span> </nobr></td>

							<td colspan="2" align="left" valign="top" class="style76"><nobr>
							<input type="radio" name="radio_needAssistInLegal" value="refuse"
								<%=(props.getProperty("radio_needAssistInLegal", "")).equalsIgnoreCase("refuse")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_needAssistInLegal[2]);">Refuse
							to answer</span> </nobr></td>
						</tr>
						<tr>
							<td colspan="9" class="style76">If yes, specify:
							&nbsp;&nbsp;&nbsp; <input type="text" name="needAssistInLegal"
								size="55"
								value="<%=props.getProperty("needAssistInLegal", "")%>">

							</td>
						</tr>
						<tr>
							<td colspan="9" align="left" class="style76">Comments:<br>
							<textArea name="commentsOnLegalIssues" cols="75"><%=props.getProperty("commentsOnLegalIssues", "")%></textArea>
							</td>
						</tr>

					</table>
					</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="9" align="center" class="style51">11. IMMIGRATION
					ISSUES</td>
				</tr>
				<tr>
					<td colspan="3" align="left" valign="top" class="style76">1.
					Are you a Canadian Citizen?</td>
					<td colspan="2" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_citizen" value="yes"
						<%=(props.getProperty("radio_citizen", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_citizen[0]);">Yes
					--> Section 11</span> </nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><input
						type="radio" name="radio_citizen" value="no"
						<%=(props.getProperty("radio_citizen", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_citizen[1]);">No</span>
					</td>
					<td colspan="1" align="left" valign="top" class="style76"><input
						type="radio" name="radio_citizen" value="uncertain"
						<%=(props.getProperty("radio_citizen", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_citizen[2]);">Uncertain</span>
					</td>
					<td colspan="2" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_citizen" value="refuse"
						<%=(props.getProperty("radio_citizen", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_citizen[3]);">Refuse
					to answer</span> </nobr></td>
				</tr>

				<tr>
					<td colspan="9">
					<table width="100%" border="0">
						<tr>
							<td colspan="9" align="left" class="style76">2. What is your
							status in Canada (read list and check one)</td>
						</tr>
						<tr>
							<td colspan="2" align="left" valign="top" class="style76"><nobr>
							<input type="radio" name="radio_yourCanadianStatus"
								value="immigrant"
								<%=(props.getProperty("radio_yourCanadianStatus", "")).equalsIgnoreCase("immigrant")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_yourCanadianStatus[0]);">Landed
							Immigrant --> Q4</span> </nobr></td>
							<td colspan="2" align="left" valign="top" class="style76"><nobr>
							<input type="radio" name="radio_yourCanadianStatus"
								value="conventionRefugee"
								<%=(props.getProperty("radio_yourCanadianStatus", "")).equalsIgnoreCase("conventionRefugee")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_yourCanadianStatus[1]);">Convention
							Refugee --> Q4</span> </nobr></td>

							<td colspan="3" align="left" valign="top" class="style76"><nobr>
							<input type="radio" name="radio_yourCanadianStatus"
								value="conventionRefugeeApp"
								<%=(props.getProperty("radio_yourCanadianStatus", "")).equalsIgnoreCase("conventionRefugeeApp")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_yourCanadianStatus[2]);">Convention
							Refugee applicant --> Q4</span> </nobr></td>
							<td colspan="2" align="left" valign="top" class="style76"><nobr>
							<input type="radio" name="radio_yourCanadianStatus"
								value="sponsoredImmigrant"
								<%=(props.getProperty("radio_yourCanadianStatus", "")).equalsIgnoreCase("sponsoredImmigrant")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_yourCanadianStatus[3]);">Sponsored
							Immigrant --> Q3</span> </nobr></td>

						</tr>
						<tr>
							<td colspan="2" align="left" valign="top" class="style76"><nobr>
							<input type="radio" name="radio_yourCanadianStatus"
								value="visitor"
								<%=(props.getProperty("radio_yourCanadianStatus", "")).equalsIgnoreCase("visitor")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_yourCanadianStatus[4]);">Visitor
							--> Q4</span> </nobr></td>
							<td colspan="2" align="left" valign="top" class="style76"><nobr>
							<input type="radio" name="radio_yourCanadianStatus"
								value="ministerPermit"
								<%=(props.getProperty("radio_yourCanadianStatus", "")).equalsIgnoreCase("ministerPermit")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_yourCanadianStatus[5]);">Minister's
							Permit --> Q4</span> </nobr></td>

							<td colspan="5" align="left" valign="top" class="style76"><nobr>
							<input type="radio" name="radio_yourCanadianStatus" value="other"
								<%=(props.getProperty("radio_yourCanadianStatus", "")).equalsIgnoreCase("other")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_yourCanadianStatus[6]);">Other
							(specify) --> Q4</span> </nobr> &nbsp;&nbsp;&nbsp; <input type="text"
								name="yourCanadianStatus"
								value="<%=props.getProperty("yourCanadianStatus", "")%>">
							</td>
						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan="9">
					<table width="100%" border="0">
						<tr>
							<td colspan="7" align="left" class="style76">3. If sponsored
							immigrant - has there been a sponsorship breakdown?</td>
							<td colspan="1" align="left" valign="top" class="style76"><nobr>
							<input type="radio" name="radio_sponsorshipBreakdown" value="yes"
								<%=(props.getProperty("radio_sponsorshipBreakdown", "")).equalsIgnoreCase("yes")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_sponsorshipBreakdown[0]);">Yes</span>
							</nobr></td>
							<td colspan="1" align="left" valign="top" class="style76"><nobr>
							<input type="radio" name="radio_sponsorshipBreakdown" value="no"
								<%=(props.getProperty("radio_sponsorshipBreakdown", "")).equalsIgnoreCase("no")?"checked":""%>>
							<span
								onClick="javascript:clickRadio(document.forms[0].radio_sponsorshipBreakdown[1]);">No</span>
							</nobr></td>
						</tr>
						<tr>
							<td colspan="6" class="style76">*If yes, why?
							&nbsp;&nbsp;&nbsp; <input type="text"
								name="whySponsorshipBreakdown" size="55"
								value="<%=props.getProperty("whySponsorshipBreakdown", "")%>">
							</td>
							<td colspan="3" class="style76">Name of Sponsor: <input
								type="text" name="sponsorName" size="35"
								value="<%=props.getProperty("sponsorName", "")%>"></td>

						</tr>

					</table>
					</td>
				</tr>

				<tr>
					<td colspan="9" class="style76">4. Do you need a referral to a
					community agency to help you with immigration issues? <input
						type="text" name="needHelpWithImmigration" size="35"
						value="<%=props.getProperty("needHelpWithImmigration", "")%>">
					</td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">
					CommentsOnImmigration:<br>
					<textArea name="CommentsOnImmigration" cols="75"><%=props.getProperty("CommentsOnImmigration", "")%></textArea>
					</td>
				</tr>

			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="9" align="center" class="style51">12. COMMUNITY
					SUPPORTS</td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">*Counsellor:
					include any information collected in Section 2, Questions 3 & 4</td>
				</tr>

				<tr>
					<td colspan="7" align="left" valign="top" class="style76">1.
					Are you currently involved with any other Community Agencies*?</td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_involvedOtherAgencies" value="yes"
						<%=(props.getProperty("radio_involvedOtherAgencies", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_involvedOtherAgencies[0]);">Yes</span>
					</nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><input
						type="radio" name="radio_involvedOtherAgencies" value="no"
						<%=(props.getProperty("radio_involvedOtherAgencies", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_involvedOtherAgencies[1]);">No</span>
					</td>
				</tr>


				<tr>
					<td colspan="9">
					<table width="100%" border="0">
						<tr>
							<td colspan="7" align="left" class="style76">2a. Name of
							Agency: <input type="text" name="agency1Name" size="55"
								value="<%=props.getProperty("agency1Name", "")%>"></td>
						</tr>
						<tr>
							<td colspan="6" align="left" valign="top" class="style76"><nobr>
							Contact Person: <input type="text" name="contact1Name" size="35"
								value="<%=props.getProperty("contact1Name", "")%>"> </nobr></td>
							<td colspan="3" align="left" valign="top" class="style76"><nobr>
							Phone # <input type="text" name="contact1Phone" size="12"
								value="<%=props.getProperty("contact1Phone", "")%>"> </nobr></td>
						</tr>
						<tr>
							<td colspan="6" class="style76">Assistance provided:
							&nbsp;&nbsp;&nbsp; <input type="text" name="assistProvided1"
								size="55" value="<%=props.getProperty("assistProvided1", "")%>">
							</td>
							<td colspan="3" class="style76">Date of last contact with
							Agency (YYYY/MM/DD) <input type="text" name="dateLastContact1"
								size="35" value="<%=props.getProperty("dateLastContact1", "")%>">
							</td>
						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan="9">
					<table width="100%" border="0">
						<tr>
							<td colspan="7" align="left" class="style76">2b. Name of
							Agency: <input type="text" name="agency2Name" size="55"
								value="<%=props.getProperty("agency2Name", "")%>"></td>
						</tr>
						<tr>
							<td colspan="6" align="left" valign="top" class="style76"><nobr>
							Contact Person: <input type="text" name="contact2Name" size="35"
								value="<%=props.getProperty("contact2Name", "")%>"> </nobr></td>
							<td colspan="3" align="left" valign="top" class="style76"><nobr>
							Phone # <input type="text" name="contact2Phone" size="12"
								value="<%=props.getProperty("contact2Phone", "")%>"> </nobr>
							</td>
						</tr>
						<tr>
							<td colspan="6" class="style76">Assistance provided:
							&nbsp;&nbsp;&nbsp; <input type="text" name="assistProvided2"
								size="55" value="<%=props.getProperty("assistProvided2", "")%>">
							</td>
							<td colspan="3" class="style76">Date of last contact with
							Agency (YYYY/MM/DD) <input type="text" name="dateLastContact2"
								size="35" value="<%=props.getProperty("dateLastContact2", "")%>">
							</td>

						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan="9">
					<table width="100%" border="0">
						<tr>
							<td colspan="7" align="left" class="style76">2c. Name of
							Agency: <input type="text" name="agency3Name" size="55"
								value="<%=props.getProperty("agency3Name", "")%>"></td>
						</tr>
						<tr>
							<td colspan="6" align="left" valign="top" class="style76"><nobr>
							Contact Person: <input type="text" name="contact3Name" size="35"
								value="<%=props.getProperty("contact3Name", "")%>"> </nobr></td>
							<td colspan="3" align="left" valign="top" class="style76"><nobr>
							Phone # <input type="text" name="contact3Phone" size="12"
								value="<%=props.getProperty("contact3Phone", "")%>"> </nobr></td>
						</tr>
						<tr>
							<td colspan="6" class="style76">Assistance provided:
							&nbsp;&nbsp;&nbsp; <input type="text" name="assistProvided3"
								size="55" value="<%=props.getProperty("assistProvided3", "")%>">
							</td>
							<td colspan="3" class="style76">Date of last contact with
							Agency (YYYY/MM/DD) <input type="text" name="dateLastContact3"
								size="35" value="<%=props.getProperty("dateLastContact3", "")%>">
							</td>
						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan="9">
					<table width="100%" border="0">
						<tr>
							<td colspan="7" align="left" class="style76">2d. Name of
							Agency: <input type="text" name="agency4Name" size="55"
								value="<%=props.getProperty("agency4Name", "")%>"></td>
						</tr>
						<tr>
							<td colspan="6" align="left" valign="top" class="style76"><nobr>
							Contact Person: <input type="text" name="contact4Name" size="35"
								value="<%=props.getProperty("contact4Name", "")%>"> </nobr></td>
							<td colspan="3" align="left" valign="top" class="style76"><nobr>
							Phone # <input type="text" name="contact4Phone" size="12"
								value="<%=props.getProperty("contact4Phone", "")%>"> </nobr></td>
						</tr>
						<tr>
							<td colspan="6" class="style76">Assistance provided:
							&nbsp;&nbsp;&nbsp; <input type="text" name="assistProvided4"
								size="55" value="<%=props.getProperty("assistProvided4", "")%>">
							</td>
							<td colspan="3" class="style76">Date of last contact with
							Agency (YYYY/MM/DD) <input type="text" name="dateLastContact4"
								size="35" value="<%=props.getProperty("dateLastContact4", "")%>">
							</td>
						</tr>
					</table>
					</td>
				</tr>

			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="left" class="style76">FOR STAFF USE
					ONLY</td>
				</tr>
			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">STAFF RATINGS
					AND IDENTIFIED ISSUES</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">Based on my own
					observations and from information from other members on my team:</td>
				</tr>

				<tr>
					<td colspan="3" align="left" class="style76">I think this
					person currently has uncontrolled severe mental illness (like
					schizophrenia or manic depression)</td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_mentalIllness" value="yes"
						<%=(props.getProperty("radio_mentalIllness", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_mentalIllness[0]);">Yes</span>
					</nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_mentalIllness" value="uncertain"
						<%=(props.getProperty("radio_mentalIllness", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_mentalIllness[1]);">Uncertain</span>
					</nobr></td>

					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_mentalIllness" value="no"
						<%=(props.getProperty("radio_mentalIllness", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_mentalIllness[2]);">No</span>
					</nobr></td>
				</tr>

				<tr>
					<td colspan="3" align="left" class="style76">I am concerned
					that this person has severe problems from uncontrolled drinking</td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_drinking" value="yes"
						<%=(props.getProperty("radio_drinking", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_drinking[0]);">Yes</span>
					</nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_drinking" value="uncertain"
						<%=(props.getProperty("radio_drinking", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_drinking[1]);">Uncertain</span>
					</nobr></td>

					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_drinking" value="no"
						<%=(props.getProperty("radio_drinking", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_drinking[2]);">No</span>
					</nobr></td>
				</tr>

				<tr>
					<td colspan="3" align="left" class="style76">I am concerned
					that this person has severe problems from uncontrolled drug use</td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_drugUse" value="yes"
						<%=(props.getProperty("radio_drugUse", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_drugUse[0]);">Yes</span>
					</nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_drugUse" value="uncertain"
						<%=(props.getProperty("radio_drugUse", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_drugUse[1]);">Uncertain</span>
					</nobr></td>

					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_drugUse" value="no"
						<%=(props.getProperty("radio_drugUse", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_drugUse[2]);">No</span>
					</nobr></td>
				</tr>


				<tr>
					<td colspan="3" align="left" class="style76">I am concerned
					that this person has uncontrolled physical health problems</td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_healthProblem" value="yes"
						<%=(props.getProperty("radio_healthProblem", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_healthProblem[0]);">Yes</span>
					</nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_healthProblem" value="uncertain"
						<%=(props.getProperty("radio_healthProblem", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_healthProblem[1]);">Uncertain</span>
					</nobr></td>

					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_healthProblem" value="no"
						<%=(props.getProperty("radio_healthProblem", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_healthProblem[2]);">No</span>
					</nobr></td>
				</tr>

				<tr>
					<td colspan="3" align="left" class="style76">I am concerned
					that this person is handicapped or disabled by severe behaviour
					problems</td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_behaviorProblem" value="yes"
						<%=(props.getProperty("radio_behaviorProblem", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_behaviorProblem[0]);">Yes</span>
					</nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_behaviorProblem" value="uncertain"
						<%=(props.getProperty("radio_behaviorProblem", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_behaviorProblem[1]);">Uncertain</span>
					</nobr></td>

					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_behaviorProblem" value="no"
						<%=(props.getProperty("radio_behaviorProblem", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_behaviorProblem[2]);">No</span>
					</nobr></td>
				</tr>

				<tr>
					<td colspan="3" align="left" class="style76">I think this
					person will need our (Seaton House's) services for more than 60
					days</td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_need60DaysSeatonServices"
						value="yes"
						<%=(props.getProperty("radio_need60DaysSeatonServices", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_need60DaysSeatonServices[0]);">Yes</span>
					</nobr></td>
					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_need60DaysSeatonServices"
						value="uncertain"
						<%=(props.getProperty("radio_need60DaysSeatonServices", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_need60DaysSeatonServices[1]);">Uncertain</span>
					</nobr></td>

					<td colspan="1" align="left" valign="top" class="style76"><nobr>
					<input type="radio" name="radio_need60DaysSeatonServices"
						value="no"
						<%=(props.getProperty("radio_need60DaysSeatonServices", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_need60DaysSeatonServices[2]);">No</span>
					</nobr></td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">Completed by:
					(Sign and Print Name) &nbsp;&nbsp;&nbsp; <input type="text"
						name="completedBy1" size="55"
						value="<%=props.getProperty("completedBy1", "")%>"></td>
				</tr>



			</table>



			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">COMPLETED BY</td>
				</tr>
				<tr>
					<td colspan="3" align="left" valign="top" class="style76">
					Completed by: &nbsp;&nbsp;&nbsp; <input type="text"
						name="completedBy2" size="45"
						value="<%=props.getProperty("completedBy2", "")%>"></td>
					<td width="49%" colspan="3" align="left" valign="top"
						class="style76">Date Assessment Completed
					(YYYY/MM/DD):&nbsp;&nbsp;&nbsp; <input type="text"
						name="assessCompleteTime" size="25"
						value="<%=props.getProperty("assessCompleteTime", "")%>">
					am/pm</td>
				</tr>
				<tr>
					<td colspan="3" align="left" valign="top" class="style76">
					Follow-up dates/appointments: &nbsp;&nbsp;&nbsp; <input type="text"
						name="followupAppmts" size="25"
						value="<%=props.getProperty("followupAppmts", "")%>"></td>
					<td width="49%" colspan="3" align="left" valign="top"
						class="style76">&nbsp;&nbsp;&nbsp;</td>
				</tr>
				<tr>
					<td colspan="3" align="left" valign="top" class="style76">
					&nbsp;&nbsp;&nbsp;</td>
					<td width="49%" colspan="3" align="left" valign="top"
						class="style76">&nbsp;&nbsp;&nbsp;</td>
				</tr>

			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="3" align="center" class="style51">TRIAGE-
					REFERRAL TO A SEATON HOUSE PROGRAM</td>
				</tr>
				<tr>
					<td colspan="3" align="left" class="style76">Based on my own
					observations (and from information from other members on my team) <br>
					I believe this client may be appropriate for one of the following
					programs:</td>
				</tr>

				<tr>
					<td width="36%" colspan="1" align="center" class="style76">
					Admission Criteria</td>
					<td width="33%" colspan="1" align="center" class="style76">
					Exclusion Criteria:</td>
					<td width="31%" colspan="1" align="center" class="style76">
					Triage Contact #s:</td>
				</tr>
				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><input
						type="checkbox" name="cbox_pamphletIssued" value="Y"
						<%=(props.getProperty("cbox_pamphletIssued", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_pamphletIssued);">Emergency
					Hostel Program</span>
					<ul>
						<li>Homeless man</li>
						<li>No other current resources</li>
						<li>No source of income</li>
						<li>Willing to follow case plan</li>
					</ul>
					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					<ul>
						<li>Violence or illicit drug use may result in client being
						barred from the Hostel</li>
					</ul>
					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake: <br>
					(416) 392-5529 <br>
					(416) 338-3196 <br>
					(416) 338-3197<br>
					<br>

					Steering Committee Member:<br>
					Maurice Jefferson (416) 392-5531</td>
				</tr>

				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><input
						type="checkbox" name="cbox_hostel" value="Y"
						<%=(props.getProperty("cbox_hostel", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_hostel);">Hostel
					- Fusion of Care Team</span>
					<ul>
						<li>Emergency Hostel Program client</li>
						<li>High mental or physical health needs</li>
					</ul>
					</td>
					<td colspan="1" align="left" class="style76">&nbsp;</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake: <br>
					(416) 338-3311<br>
					<br>

					Steering Committee Member:<br>
					Shawn Yoder (416) 392-6706</td>
				</tr>
				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><input
						type="checkbox" name="cbox_rotaryClub" value="Y"
						<%=(props.getProperty("cbox_rotaryClub", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_rotaryClub);">Rotary
					Club of Toronto Infirmary</span>
					<ul>
						<li>Client is to be discharged from hospital and requires: <br>
						(i) Frequent physician or nursing follow-up and/or <br>
						(ii) Rehabilitation to return to previous level of functioning.</li>
					</ul>
					Client may be admitted to the Infirmary if he exhibits one or more
					of the following health conditions:
					<ul>
						<li>Requires follow-up care (post-operative care and wound
						care)</li>
						<li>Uncontrolled/poorly controlled chronic illness such as
						diabetes, cirrhosis, seizures, CHF and HIV</li>
						<li>Terminal illness that requires palliative care for
						comfort</li>
					</ul>

					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					<ul>
						<li>Client unable to transfer safely (move from bed to chair
						unassisted)</li>
						<li>Client is incontinent and using a wheelchair</li>
					</ul>
					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake: <br>
					(416) 392-5598<br>
					<br>

					Steering Committee Member:<br>
					Karen Smith (416) 392-5598</td>
				</tr>


				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><input
						type="checkbox" name="cbox_annexHarm" value="Y"
						<%=(props.getProperty("cbox_annexHarm", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_annexHarm);">Annex
					Harm Reduction Program</span>
					<ul>
						<li>Chronic alcohol, substance use (including non-palatable
						substances)</li>
						<li>Severe uncontrolled physical illness</li>
						<li>Severe uncontrolled mental illness</li>
						<li>Severe uncontrolled behaviour problems</li>
						<li>Chronically homeless</li>
					</ul>

					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					<ul>
						<li>Client wishing to maintain abstinence may not do well in
						this environment</li>
					</ul>
					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake: <br>
					(416) 392-5519<br>
					<br>

					Steering Committee Member: <br>
					Ken Mendonca (416) 392-5522</td>
				</tr>

				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><input
						type="checkbox" name="cbox_longTermProgram" value="Y"
						<%=(props.getProperty("cbox_longTermProgram", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_longTermProgram);">Long
					Term Program</span>
					<ul>
						<li>Age 65+ - refer direct from Reception</li>
					</ul>
					OR
					<ul>
						<li>Age 50+</li>
						<li>Willing and able to pay rent</li>
					</ul>
					OR Any age and one of the following criteria:
					<ul>
						<li>On pension</li>
						<li>Physically or socially vulnerable</li>
						<li>Mental health issues</li>
						<li>Physical disability</li>
						<li>Short-term disability (non-ambulatory)</li>
					</ul>

					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					<ul>
						<li>Client poses significant risk to residents of long term
						program</li>
						<li>History of aggressive or predatory behaviour</li>
					</ul>
					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake:<br>
					(416) 392-6049 <br>
					(416) 338-3175<br>
					<br>

					Steering Committee Member:<br>
					Mark Headley (416) 392-5543</td>
				</tr>
				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><input
						type="checkbox" name="cbox_birchmountResidence" value="Y"
						<%=(props.getProperty("cbox_birchmountResidence", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_birchmountResidence);">Birchmount
					Residence</span>
					<ul>
						<li>Age 55+ (men aged 50-55 considered)</li>
						<li>Chronic health issues</li>
						<li>Able to carry out the activities of daily living without
						staff assistance</li>
						<li>Able to climb stairs</li>
						<li>Able to behave in a reliable, predictable and respectful
						manner in the community</li>
						<li>Stable mental health</li>
						<li>Able to act appropriately and responsibly if consuming
						alcohol and willing to cooperate with a harm reduction approach</li>
						<li>Willing to take medication as prescribed by the attending
						physician and cooperative with medical and Nursing plan and attend
						medical appointments and procedures as required.</li>
						<li>Willing to be financially responsible and pay rent</li>
						<li>Has legal status in Canada</li>
						<li>Willing to cooperate with plan for future housing or
						appropriate care facility</li>
					</ul>

					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					<ul>
						<li>Recent history of incarceration, criminal activity,
						violence</li>
						<li>Requires bedside nursing or personal care</li>
						<li>Requires oxygen, IV or intrusive medical procedures of
						equipment</li>
						<li>Incontinence issues</li>
						<li>Danger to self or others</li>
						<li>Abuse of medications or street drugs</li>
					</ul>
					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake: <br>
					(416) 392-6167<br>
					<br>

					Steering Committee Member:<br>
					Carla O'Brien (416) 392-5543<br>
					<br>
					Referrals to Birchmount are made by Long Term program.<br>
					<br>

					N.B. Client must be resident of Seaton House Long Term program for
					more than 30 days prior to admission (some exceptions considered).
					</td>
				</tr>

				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><input
						type="checkbox" name="cbox_oNeillHouse" value="Y"
						<%=(props.getProperty("cbox_oNeillHouse", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_oNeillHouse);">O'Neill
					House</span>
					<ul>
						<li>Severe problems related to street drug use (Crack
						cocaine)</li>
						<li>Client does not want treatment</li>
						<li>Barred from Hostel due to drug use</li>
						<li>2 or more incident reports for drug use</li>
					</ul>

					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake <br>
					(416) 392-5407<br>
					<br>

					Steering Committee Member:<br>
					Tom Fulgosi (416) 392-5436</td>
				</tr>

				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><input
						type="checkbox" name="cbox_fortYork" value="Y"
						<%=(props.getProperty("cbox_fortYork", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_fortYork);">Fort
					York Residence</span>
					<ul>
						<li>Willing and able to work</li>
						<li>If employed: willing to save 60% of income</li>
						<li>Willing to do volunteer work</li>
						<li>Has ID (hard copy)</li>
						<li>Has up-to-date resume</li>
						<li>Willing and able to follow case plan</li>
					</ul>

					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Clients with substance use issues will be required to complete a
					Rehab program prior to admission to Fort York Residence.</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake:<br>
					Mark Horne: # TBA*<br>
					<br>

					Steering Committee Member#: TBA*</td>
				</tr>
				<tr>
					<td colspan="1" align="left" valign="top" class="style76"><input
						type="checkbox" name="cbox_downsviewDells" value="Y"
						<%=(props.getProperty("cbox_downsviewDells", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_downsviewDells);">Downsview
					Dells</span>
					<ul>
						<li>Chemically dependent now wishing abstinence</li>
						<li>Willing and able to complete 30-day treatment at Humber
						River Regional Hospital</li>
					</ul>

					</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Disruptive behaviour</td>
					<td colspan="1" align="left" valign="top" class="style76"><br>
					<br>
					Triage/Intake: <br>
					(416) 392-5452<br>
					<br>

					Steering Committee:<br>
					MemberDon Inglis (416) 392-5452</td>
				</tr>
			</table>

			<table width="95%" border="0">
				<tr>
					<td class="style76">* Phone numbers for Fort York Residence
					will be available shortly.</td>
				</tr>
			</table>

			<table width="95%" border="0">
				<tr>
					<td align="center"><input type="submit" value="Save"
						onclick="javascript:return onSave();" /> <input type="submit"
						value="Save and Exit" onclick="javascript:return onSaveExit();" />
					<input type="submit" value="Exit"
						onclick="javascript:return onExit();" /> <input type="button"
						value="Print" onclick="javascript:return onPrint();" /></td>
				</tr>
			</table>

			<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->

			</td>
		</tr>
	</table>

</html:form>
</body>
</html:html>
