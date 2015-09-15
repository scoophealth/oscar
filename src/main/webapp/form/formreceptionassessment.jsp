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
<%@page import="org.oscarehr.util.LoggedInInfo" %>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>


<!-- link rel="stylesheet" href="Style1.css" type="text/css" media="all" -->


<title>INTAKE A. RECEPTION ASSESSMENT</title>
<link rel="stylesheet" type="text/css"
	href="formreceptionassessment.css">
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
	String formClass = "ReceptionAssessment";
	
	String formLink = "formreceptionassessment.jsp";

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
		else if(!isCurrency(document.forms[0].amtReceived.value) )
		{
	        alert ("You must type in a valid currency in the field.");
		
			document.forms[0].amtReceived.value = "";
			document.forms[0].amtReceived.focus();
		
            ret = false;
        }
		else if(!isInteger(document.forms[0].frequencyOfSeeingDoctor.value))
		{
	        alert ("You must type in a number in the field.");
		
			document.forms[0].frequencyOfSeeingDoctor.value = "";
			document.forms[0].frequencyOfSeeingDoctor.focus();
		
            ret = false;
        }
		else if(!isInteger(document.forms[0].frequencyOfSeeingEmergencyRoomDoctor.value))
		{
	        alert ("You must type in a number in the field.");
		
			document.forms[0].frequencyOfSeeingEmergencyRoomDoctor.value = "";
			document.forms[0].frequencyOfSeeingEmergencyRoomDoctor.focus();
		
            ret = false;
        }
/*		
		else if(!isInteger(document.forms[0].lastIssueDate.value))
		{
            ret = false;
        }
*/
        return ret;
	}

    function checkAllDates()
    {
        var b = true;
        if(valDate(document.forms[0].assessDate)==false){
            b = false;
        }else
        if(valDate(document.forms[0].enterSeatonDate)==false){
            b = false;
        }else
        if(valDate(document.forms[0].dateOfReadmission)==false){
            b = false;
        }else
        if(valDate(document.forms[0].datesAtSeaton)==false){
            b = false;
        }else
        if(valDate(document.forms[0].lastIssueDate)==false){
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
					<td colspan="3" class="style63">INTAKE A. RECEPTION ASSESSMENT
					</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td class="style76">Assessment Date (YYYY/MM/DD): <br>
					<input type="text" name="assessDate"
						value="<%=props.getProperty("assessDate", "")%>"></td>
					<td class="style76">Assessment start time: <br>
					<input type="text" name="assessStartTime"
						value="<%=props.getProperty("assessStartTime", "")%>"> am
					/ pm</br>
					</td>
					<td class="style76">&nbsp;</td>
				</tr>
				<tr>
					<td class="style76">Date client entered Seaton House
					(YYYY/MM/DD)<br>
					<input type="text" name="enterSeatonDate"
						value="<%=props.getProperty("enterSeatonDate", "")%>"></td>
					<td class="style76"><input type="checkbox"
						name="cbox_newClient" value="Y"
						<%=(props.getProperty("cbox_newClient", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_newClient);">New
					Client</span></td>
					<td class="style76"><input type="checkbox"
						name="cbox_dateOfReadmission" value="Y"
						<%=(props.getProperty("cbox_dateOfReadmission", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_dateOfReadmission);">Re-admission.
					Give date of last admission (YYYY/MM/DD).</span> <input type="text"
						name="dateOfReadmission"
						value="<%=props.getProperty("dateOfReadmission", "")%>"></td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="3" align="center" class="style51">Notes for
					completing this intake:</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="3" align="left" class="style76">Reception Staff:<br>
					1. Please complete every section of this intake. If you are unable
					to complete a section - use 'Comments' section to explain why
					(e.g., client refused to answer, interview cut short).<br>
					2. Before beginning this intake, please read this paragraph to the
					client:"We would like to ask you some questions to ensure that you
					are getting the health care that you need and want while at Seaton
					House. Any answers you give us will be kept private in your Seaton
					House file. We will collect numbers from the answers that we get
					from everyone on a regular basis to try and make the system better
					at Seaton House and in the City but in this case we would not use
					your name. You don't have to answer these questions. Your stay at
					Seaton House will not change if you don't answer these questions"</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="3" align="center" class="style51">Identifying
					Data</td>
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
					<td align="left" class="style76">Language(s) Spoken:</td>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_speakEnglish" value="Y"
						<%=(props.getProperty("cbox_speakEnglish", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_speakEnglish);">English
					</span></td>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_speakFrench" value="Y"
						<%=(props.getProperty("cbox_speakFrench", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_speakFrench);">French</span>
					</td>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_speakOther" value="Y"
						<%=(props.getProperty("cbox_speakOther", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_speakOther);">Other:
					</span> <input type="text" name="speakOther"
						value="<%=props.getProperty("speakOther", "")%>"></td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td align="center" class="style51">1. Reason for Admission</td>
				</tr>
				<tr>
					<td align="left" class="style76">What is your reason for
					coming to Seaton House? (state briefly in client's own words if
					possible)</td>
				</tr>
				<tr>
					<td align="left" class="style76"><textArea
						name="reasonToSeaton" cols="75"><%=props.getProperty("reasonToSeaton", "")%></textArea>
					</td>
				</tr>
				<tr>
					<td align="left" class="style76">Have you ever stayed at
					Seaton House before? (give dates) <input type="text"
						name="datesAtSeaton" size="55"
						value="<%=props.getProperty("datesAtSeaton", "")%>"></td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td align="center" class="style51">2. Assistance Required</td>
				</tr>
				<tr>
					<td align="left" class="style76">Do you require assistance
					with any of the following (state briefly in clients own words if
					possible)</td>
				</tr>
				<tr>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_assistInHealth" value="Y"
						<%=(props.getProperty("cbox_assistInHealth", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInHealth);">Physical
					or Mental Health, including medication</span></td>
				</tr>
				<tr>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_assistInIdentification" value="Y"
						<%=(props.getProperty("cbox_assistInIdentification", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInIdentification);">Obtaining
					Identification</span></td>
				</tr>
				<tr>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_assistInAddictions" value="Y"
						<%=(props.getProperty("cbox_assistInAddictions", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInAddictions);">Addictions</span>
					</td>
				</tr>
				<tr>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_assistInHousing" value="Y"
						<%=(props.getProperty("cbox_assistInHousing", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInHousing);">Housing
					issues</span></td>
				</tr>
				<tr>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_assistInEducation" value="Y"
						<%=(props.getProperty("cbox_assistInEducation", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInEducation);">Education
					Issues</span></td>
				</tr>
				<tr>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_assistInEmployment" value="Y"
						<%=(props.getProperty("cbox_assistInEmployment", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInEmployment);">Employment
					issues</span></td>
				</tr>

				<tr>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_assistInFinance" value="Y"
						<%=(props.getProperty("cbox_assistInFinance", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInFinance);">Financial
					issues</span></td>
				</tr>
				<tr>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_assistInLegal" value="Y"
						<%=(props.getProperty("cbox_assistInLegal", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInLegal);">Legal
					issues</span></td>
				</tr>
				<tr>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_assistInImmigration" value="Y"
						<%=(props.getProperty("cbox_assistInImmigration", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInImmigration);">Immigration
					issues</span></td>
				</tr>

			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="4" align="center" class="style51">3.
					Identification</td>
				</tr>
				<tr>
					<td colspan="2" align="left" class="style76">What
					identification do you have?</td>
					<td colspan="2" align="left" class="style76"><input
						type="checkbox" name="cbox_noID" value="Y"
						<%=(props.getProperty("cbox_noID", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_noID);">No
					ID</span></td>

				</tr>
				<tr>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_sinCard" value="Y"
						<%=(props.getProperty("cbox_sinCard", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_sinCard);">SIN
					Card:</span></td>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_healthCard" value="Y"
						<%=(props.getProperty("cbox_healthCard", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_healthCard);">Ontario
					Health Card#</span> <br>
					<input type="text" name="healthCardNum"
						value="<%=props.getProperty("healthCardNum", "")%>"></td>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_birthCertificate" value="Y"
						<%=(props.getProperty("cbox_birthCertificate", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_birthCertificate);">Birth
					Certificate</span></td>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_citzenshipCard" value="Y"
						<%=(props.getProperty("cbox_citzenshipCard", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_citzenshipCard);">Citizenship
					Card</span></td>

				</tr>
				<tr>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_immigrant" value="Y"
						<%=(props.getProperty("cbox_immigrant", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_immigrant);">Landed
					Immigrant</span></td>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_refugee" value="Y"
						<%=(props.getProperty("cbox_refugee", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_refugee);">Convention
					Refugee</span></td>
					<td colspan="2" align="left" class="style76"><input
						type="checkbox" name="cbox_otherID" value="Y"
						<%=(props.getProperty("cbox_otherID", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_otherID);">Other
					(specify)</span> <input type="text" name="otherIdentification" size="35"
						value="<%=props.getProperty("otherIdentification", "")%>">

					</td>

				</tr>
				<tr>
					<td colspan="2" align="left" class="style76">STAFF: Please
					photocopy and certify ID, and put copy in client file</td>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_idFiled" value="Y"
						<%=(props.getProperty("cbox_idFiled", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_idFiled);">ID
					copied and filed</span></td>
					<td align="left" class="style76"><input type="checkbox"
						name="cbox_idNone" value="Y"
						<%=(props.getProperty("cbox_idNone", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_idNone);">ID
					not available</span></td>

				</tr>
				<tr>
					<td colspan="4" align="left" valign="top" class="style76">
					Comments:<br>
					<textArea name="commentsOnID" cols="75"><%=props.getProperty("commentsOnID", "")%></textArea>

					</td>
				</tr>

			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="9" align="center" class="style51">4. ON-LINE
					CHECK</td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">What has been
					your main source of income in the past 12 months (check one)</td>
				</tr>
				<tr>
					<td colspan="1" width="10%" align="left" class="style76"><input
						type="checkbox" name="cbox_OW" value="Y"
						<%=(props.getProperty("cbox_OW", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_OW);">OW</span>
					</td>
					<td colspan="1" width="12%" align="left" class="style76"><input
						type="checkbox" name="cbox_ODSP" value="Y"
						<%=(props.getProperty("cbox_ODSP", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_ODSP);">ODSP</span>
					</td>
					<td colspan="1" width="12%" align="left" class="style76"><input
						type="checkbox" name="cbox_WSIB" value="Y"
						<%=(props.getProperty("cbox_WSIB", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_WSIB);">WSIB</span>
					</td>
					<td colspan="2" width="15%" align="left" class="style76"><input
						type="checkbox" name="cbox_Employment" value="Y"
						<%=(props.getProperty("cbox_Employment", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_Employment);">Employment</span>
					</td>
					<td colspan="1" width="11%" align="left" class="style76"><input
						type="checkbox" name="cbox_EI" value="Y"
						<%=(props.getProperty("cbox_EI", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_EI);">EI</span>
					</td>
					<td colspan="1" width="11%" align="left" class="style76"><input
						type="checkbox" name="cbox_OAS" value="Y"
						<%=(props.getProperty("cbox_OAS", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_OAS);">OAS</span>
					</td>
					<td colspan="1" width="10%" align="left" class="style76"><input
						type="checkbox" name="cbox_CPP" value="Y"
						<%=(props.getProperty("cbox_CPP", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_CPP);">CPP</span>
					</td>
					<td width="19%" colspan="2" align="left" class="style76"><input
						type="checkbox" name="cbox_OtherIncome" value="Y"
						<%=(props.getProperty("cbox_OtherIncome", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_OtherIncome);">Other</span>
					</td>
				</tr>
				<tr>
					<td colspan="5" align="left" class="style76">OW/ODSP On-Line
					check completed by worker?</td>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_onlineCheck" value="yes"
						<%=(props.getProperty("radio_onlineCheck", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_onlineCheck[0]);">Yes</span>
					</td>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_onlineCheck" value="no"
						<%=(props.getProperty("radio_onlineCheck", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_onlineCheck[1]);">No</span>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_active" value="active"
						<%=(props.getProperty("radio_active", "")).equalsIgnoreCase("active")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_active[0]);">Active</span>
					</td>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_active" value="inactive"
						<%=(props.getProperty("radio_active", "")).equalsIgnoreCase("inactive")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_active[1]);">Inactive</span>
					</td>
					<td colspan="5" align="left" class="style76"><input
						type="checkbox" name="cbox_noRecord" value="Y"
						<%=(props.getProperty("cbox_noRecord", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_noRecord);">No
					Record</span></td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">Last issuance
					date (YYYY/MM/DD): <input type="text" name="lastIssueDate"
						value="<%=props.getProperty("lastIssueDate", "")%>"></td>
					<td colspan="3" align="left" class="style76">Office: <input
						type="text" name="office"
						value="<%=props.getProperty("office", "")%>"></td>
					<td colspan="2" align="left" class="style76">Worker#: <input
						type="text" name="workerNum"
						value="<%=props.getProperty("workerNum", "")%>"></td>
					<td colspan="2" align="left" class="style76">Amount Received:
					<br>
					$ <input type="text" name="amtReceived" maxlength="9"
						value="<%=props.getProperty("amtReceived", "")%>" size="10">
					</td>

				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="9" align="center" class="style51">5. HEALTH</td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">Physical Health</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">1. Do you have a
					regular medical doctor or specialist?</td>
					<td width="11%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_hasDoctor" value="yes"
						<%=(props.getProperty("radio_hasDoctor", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasDoctor[0]);">Yes</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_hasDoctor" value="no"
						<%=(props.getProperty("radio_hasDoctor", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasDoctor[1]);">No</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_hasDoctor" value="dontKnow"
						<%=(props.getProperty("radio_hasDoctor", "")).equalsIgnoreCase("dontKnow")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasDoctor[2]);">Don't
					know</span></td>

				</tr>
				<tr>
					<td colspan="9" class="style76">If yes, what is the name,
					address and phone # of your doctor or specialist?</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">Name: <input
						type="text" name="doctorName"
						value="<%=props.getProperty("doctorName", "")%>"></td>
					<td colspan="5" align="left" class="style76">Phone #: <input
						type="text" name="doctorPhone"
						value="<%=props.getProperty("doctorPhone", "")%>">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Ext. <input type="text"
						name="doctorPhoneExt"
						value="<%=props.getProperty("doctorPhoneExt", "")%>"></td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">Address: <input
						type="text" name="doctorAddress" size="70" maxlength="150"
						value="<%=props.getProperty("doctorAddress", "")%>"></td>
				</tr>

				<tr>
					<td colspan="7" align="left" class="style76">If yes, Would you
					be able to see this doctor again if you needed to see a doctor?</td>
					<td width="8%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_seeDoctor" value="yes"
						<%=(props.getProperty("radio_seeDoctor", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_seeDoctor[0]);">Yes</span>
					</td>
					<td width="15%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_seeDoctor" value="no"
						<%=(props.getProperty("radio_seeDoctor", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_seeDoctor[1]);">No</span>
					</td>
				</tr>

				<tr>
					<td colspan="7" align="left" class="style76">2. Do you have
					any health issue that we should know about in the event of an
					emergency?</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_healthIssue" value="yes"
						<%=(props.getProperty("radio_healthIssue", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_healthIssue[0]);">Yes</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_healthIssue" value="no"
						<%=(props.getProperty("radio_healthIssue", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_healthIssue[1]);">No</span>
					</td>
				</tr>
				<tr>
					<td colspan="9" class="style76">If yes, give details <br>
					<br>
					<textArea name="healthIssueDetails" cols="75"><%=props.getProperty("healthIssueDetails", "")%></textArea>
					</td>
				</tr>
				<tr>
					<td colspan="9" class="style76">3. Do you have any of the
					following (read list and check all that apply):</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76"><input
						type="checkbox" name="cbox_hasDiabetes" value="Y"
						<%=(props.getProperty("cbox_hasDiabetes", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_hasDiabetes);">Diabetes.
					If yes --></span></td>
					<td colspan="2" align="left" class="style76"><input
						type="checkbox" name="cbox_insulin" value="Y"
						<%=(props.getProperty("cbox_insulin", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_insulin);">Insulin
					dependent?</span></td>
					<td colspan="1" align="left" class="style76"><input
						type="checkbox" name="cbox_epilepsy" value="Y"
						<%=(props.getProperty("cbox_epilepsy", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_epilepsy);">Epilepsy</span>
					</td>
					<td colspan="2" align="left" class="style76"><input
						type="checkbox" name="cbox_bleeding" value="Y"
						<%=(props.getProperty("cbox_bleeding", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_bleeding);">Bleeding
					Disorder</span></td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76"><input
						type="checkbox" name="cbox_hearingImpair" value="Y"
						<%=(props.getProperty("cbox_hearingImpair", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_hearingImpair);">Hearing
					Impairment</span></td>
					<td width="19%" colspan="1" align="left" class="style76"><input
						type="checkbox" name="cbox_visualImpair" value="Y"
						<%=(props.getProperty("cbox_visualImpair", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_visualImpair);">Visual
					Impairment</span></td>
					<td colspan="6" align="left" class="style76"><input
						type="checkbox" name="cbox_mobilityImpair" value="Y"
						<%=(props.getProperty("cbox_mobilityImpair", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_mobilityImpair);">Mobility
					impairment. Give details: </span> <input type="text" name="mobilityImpair"
						size="35" value="<%=props.getProperty("mobilityImpair", "")%>">

					</td>
				</tr>
				<tr>
					<td colspan="5" align="left" class="style76">4. Do you have
					any other health concern that you wish to share with us?</td>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_otherHealthConcern" value="yes"
						<%=(props.getProperty("radio_otherHealthConcern", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_otherHealthConcern[0]);">Yes</span>
					</td>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_otherHealthConcern" value="no"
						<%=(props.getProperty("radio_otherHealthConcern", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_otherHealthConcern[1]);">No</span>

					</td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">If yes, give
					details including duration of problem, any medications taken
					(include over the counter meds), outcome, etc <br>
					<br>
					<textArea name="otherHealthConerns" cols="75"><%=props.getProperty("otherHealthConerns", "")%></textArea>
					</td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">Medications</td>
				</tr>
				<tr>
					<td colspan="5" align="left" class="style76">1. Are you
					presently taking any medications?</td>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_takeMedication" value="yes"
						<%=(props.getProperty("radio_takeMedication", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_takeMedication[0]);">Yes</span>
					</td>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_takeMedication" value="no"
						<%=(props.getProperty("radio_takeMedication", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_takeMedication[1]);">No</span>
					</td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">Name(s) of
					Medication(s) <input type="text" name="namesOfMedication" size="70"
						value="<%=props.getProperty("namesOfMedication", "")%>"></td>
				</tr>
				<tr>
					<td colspan="5" align="left" class="style76">2. Do you need
					help obtaining medication?</td>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_helpObtainMedication" value="yes"
						<%=(props.getProperty("radio_helpObtainMedication", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_helpObtainMedication[0]);">Yes</span>
					</td>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_helpObtainMedication" value="no"
						<%=(props.getProperty("radio_helpObtainMedication", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_helpObtainMedication[1]);">No</span>
					</td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">If yes, give
					details <br>
					<br>
					<textArea name="helpObtainMedication" cols="75"><%=props.getProperty("helpObtainMedication", "")%></textArea>

					</td>
				</tr>

				<tr>
					<td colspan="5" align="left" class="style76">3. Are you
					allergic to any medications?</td>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_allergicToMedication" value="yes"
						<%=(props.getProperty("radio_allergicToMedication", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_allergicToMedication[0]);">Yes</span>
					</td>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_allergicToMedication" value="no"
						<%=(props.getProperty("radio_allergicToMedication", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_allergicToMedication[1]);">No</span>
					</td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">If yes, give
					medication names:<br>
					<br>
					<input type="text" name="allergicToMedicationName" size="75"
						value="<%=props.getProperty("allergicToMedicationName", "")%>">
					<br>
					<br>
					If yes, give reaction (hives, rash, anaphylaxis=breathing problems,
					shortness of breath, itching swelling of mouth and throat) <textArea
						name="reactionToMedication" cols="75"><%=props.getProperty("reactionToMedication", "")%></textArea>

					</td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">Mental Health</td>
				</tr>
				<tr>
					<td colspan="5" align="left" class="style76">1. Do you have
					any mental health concerns that you wish to share with us?</td>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_mentalHealthConcerns" value="yes"
						<%=(props.getProperty("radio_mentalHealthConcerns", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_mentalHealthConcerns[0]);">Yes</span>
					</td>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_mentalHealthConcerns" value="no"
						<%=(props.getProperty("radio_mentalHealthConcerns", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_mentalHealthConcerns[1]);">No</span>
					</td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">If yes, give
					diagnosis (schizophrenia, depression etc, and date diagnosed)<br>
					<br>
					<textArea name="mentalHealthConcerns" cols="75"><%=props.getProperty("mentalHealthConcerns", "")%></textArea>

					</td>
				</tr>
			</table>
			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">6. SURVEY
					MODULE - ACCESS TO HEALTH CARE</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">Please read the
					following statement to the client:"To better provide care at Seaton
					House, I'd like to ask you a few more questions about your access
					to health care"</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">1. Not counting
					when you were an overnight patient, in the past 12 months, how many
					times have you seen a general practitioner or family physician
					about your physical, emotional or mental health? <input type="text"
						name="frequencyOfSeeingDoctor"
						value="<%=props.getProperty("frequencyOfSeeingDoctor", "")%>">
					</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">2. Where did the
					most recent contact take place? (Read list, Mark one only.)</td>
				</tr>
				<tr>
					<td colspan="2" align="left" class="style76"><input
						type="checkbox" name="cbox_visitWalkInClinic" value="Y"
						<%=(props.getProperty("cbox_visitWalkInClinic", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_visitWalkInClinic);">Walk-in
					clinic</span> <br>
					<input type="checkbox" name="cbox_visitHealthCenter" value="Y"
						<%=(props.getProperty("cbox_visitHealthCenter", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_visitHealthCenter);">Community
					health centre</span></td>
					<td colspan="2" align="left" class="style76"><input
						type="checkbox" name="cbox_visitEmergencyRoom" value="Y"
						<%=(props.getProperty("cbox_visitEmergencyRoom", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_visitEmergencyRoom);">Hospital
					emergency room</span><br>
					<input type="checkbox" name="cbox_visitOthers" value="Y"
						<%=(props.getProperty("cbox_visitOthers", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_visitOthers);">Other
					(specify)</span></td>
					<td colspan="2" align="left" valign="top" class="style76"><input
						type="checkbox" name="cbox_visitHealthOffice" value="Y"
						<%=(props.getProperty("cbox_visitHealthOffice", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_visitHealthOffice);">Health
					Professionals office</span></td>

				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;If more than 1 contact
					and not in emergency room: <br>
					3. Would you be able to see this doctor again if you needed to see
					a doctor?</td>
					<td width="17%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_seeSameDoctor" value="yes"
						<%=(props.getProperty("radio_seeSameDoctor", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_seeSameDoctor[0]);">Yes</span>
					</td>
					<td width="23%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_seeSameDoctor" value="no"
						<%=(props.getProperty("radio_seeSameDoctor", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_seeSameDoctor[1]);">No</span>
					</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">4. In the past 12
					months, how many times have you seen a physician in a hospital
					Emergency room? <input type="text"
						name="frequencyOfSeeingEmergencyRoomDoctor" size="15"
						value="<%=props.getProperty("frequencyOfSeeingEmergencyRoomDoctor", "")%>">
					</td>
				</tr>

				<tr>
					<td colspan="4" align="left" class="style76">5. During the
					past 12 months, was there ever a time when you needed health care
					or advice but did not receive it?</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_didNotReceiveHealthCare" value="yes"
						<%=(props.getProperty("radio_didNotReceiveHealthCare", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_didNotReceiveHealthCare[0]);">Yes
					</span><br>

					<input type="radio" name="radio_didNotReceiveHealthCare"
						value="dontKnow"
						<%=(props.getProperty("radio_didNotReceiveHealthCare", "")).equalsIgnoreCase("dontKnow")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_didNotReceiveHealthCare[1]);">Don't
					know</span></td>
					<td width="23%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_didNotReceiveHealthCare" value="no"
						<%=(props.getProperty("radio_didNotReceiveHealthCare", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_didNotReceiveHealthCare[2]);">No
					</span><br>
					<input type="radio" name="radio_didNotReceiveHealthCare"
						value="refuse"
						<%=(props.getProperty("radio_didNotReceiveHealthCare", "")).equalsIgnoreCase("refuse")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_didNotReceiveHealthCare[3]);">Refuse</span><br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; to answer</td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">If yes, Thinking
					of the most recent time, what was the type of care that was needed?
					(Do not read list. Mark all that apply)</td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76"><input
						type="checkbox" name="cbox_treatPhysicalHealth" value="Y"
						<%=(props.getProperty("cbox_treatPhysicalHealth", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_treatPhysicalHealth);">Treatment
					of a physical </span><br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;health problem <br>
					<input type="checkbox" name="cbox_treatMentalHealth" value="Y"
						<%=(props.getProperty("cbox_treatMentalHealth", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_treatMentalHealth);">Treatment
					of an emotional or </span><br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;mental health problem</td>
					<td colspan="2" align="left" class="style76"><input
						type="checkbox" name="cbox_regularCheckup" value="Y"
						<%=(props.getProperty("cbox_regularCheckup", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_regularCheckup);">A
					regular check-up</span><br>
					<input type="checkbox" name="cbox_treatOtherReasons" value="Y"
						<%=(props.getProperty("cbox_treatOtherReasons", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_treatOtherReasons);">Any
					other reason (specify)</span><br>
					<input type="text" name="treatOtherReasons" size="35"
						value="<%=props.getProperty("treatOtherReasons", "")%>"></td>
					<td colspan="2" align="left" valign="top" class="style76"><input
						type="checkbox" name="cbox_treatInjury" value="Y"
						<%=(props.getProperty("cbox_treatInjury", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_treatInjury);">Care
					of injury</span></td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">6. If you had a
					physical, emotional or mental health problem that you needed help
					with, where would you go for help? (Read list. Mark one only)</td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76"><input
						type="checkbox" name="cbox_goToWalkInClinic" value="Y"
						<%=(props.getProperty("cbox_goToWalkInClinic", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_goToWalkInClinic);">Walk-in
					clinic</span><br>
					<input type="checkbox" name="cbox_goToHealthCenter" value="Y"
						<%=(props.getProperty("cbox_goToHealthCenter", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_goToHealthCenter);">Community
					health centre</span></td>
					<td colspan="2" align="left" class="style76"><input
						type="checkbox" name="cbox_goToEmergencyRoom" value="Y"
						<%=(props.getProperty("cbox_goToEmergencyRoom", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_goToEmergencyRoom);">Hospital
					emergency room</span><br>
					<input type="checkbox" name="cbox_goToOthers" value="Y"
						<%=(props.getProperty("cbox_goToOthers", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_goToOthers);">Other
					(specify)</span><br>
					<input type="text" name="goToOthers" size="35"
						value="<%=props.getProperty("goToOthers", "")%>"></td>
					<td colspan="2" align="left" valign="top" class="style76"><input
						type="checkbox" name="cbox_HealthOffice" value="Y"
						<%=(props.getProperty("cbox_HealthOffice", "")).equalsIgnoreCase("Y")?"checked":""%>>
					<span
						onClick="javascript:clickCheckBox(document.forms[0].cbox_HealthOffice);">Health
					Professionals office</span></td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">7. Do you have an
					appointment to see a general practitioner or family doctor in the
					next 3 months?</td>
					<td width="17%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_appmtSeeDoctorIn3Mths" value="yes"
						<%=(props.getProperty("radio_appmtSeeDoctorIn3Mths", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_appmtSeeDoctorIn3Mths[0]);">Yes</span>
					</td>
					<td width="23%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_appmtSeeDoctorIn3Mths" value="no"
						<%=(props.getProperty("radio_appmtSeeDoctorIn3Mths", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_appmtSeeDoctorIn3Mths[1]);">(A)No</span>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">8. Do you feel
					you would benefit from having a regular doctor or do you need a
					regular doctor?</td>
					<td width="17%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_needRegularDoctor" value="yes"
						<%=(props.getProperty("radio_needRegularDoctor", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_needRegularDoctor[0]);">(B)Yes</span>
					</td>
					<td width="23%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_needRegularDoctor" value="no"
						<%=(props.getProperty("radio_needRegularDoctor", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_needRegularDoctor[1]);">No</span>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">9. Would you
					object to having an appointment with a regular doctor in the next 4
					weeks?</td>
					<td width="17%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_objectToRegularDoctorIn4Wks" value="yes"
						<%=(props.getProperty("radio_objectToRegularDoctorIn4Wks", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_objectToRegularDoctorIn4Wks[0]);">Yes</span>
					</td>
					<td width="23%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_objectToRegularDoctorIn4Wks" value="no"
						<%=(props.getProperty("radio_objectToRegularDoctorIn4Wks", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_objectToRegularDoctorIn4Wks[1]);">(C)No</span>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="left" class="style76">10. How would you
					rate your overall health?</td>
					<td width="19%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_rateOverallHealth" value="poor"
						<%=(props.getProperty("radio_rateOverallHealth", "")).equalsIgnoreCase("poor")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_rateOverallHealth[0]);">Poor</span>
					</td>
					<td width="19%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_rateOverallHealth" value="fair"
						<%=(props.getProperty("radio_rateOverallHealth", "")).equalsIgnoreCase("fair")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_rateOverallHealth[1]);">Fair</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_rateOverallHealth" value="good"
						<%=(props.getProperty("radio_rateOverallHealth", "")).equalsIgnoreCase("good")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_rateOverallHealth[2]);">Good</span>
					</td>
					<td colspan="1" align="left" class="style76"><input
						type="radio" name="radio_rateOverallHealth" value="excellent"
						<%=(props.getProperty("radio_rateOverallHealth", "")).equalsIgnoreCase("excellent")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_rateOverallHealth[3]);">Excellent</span>
					</td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Intake Staff: Has a
					"YES" been selected for an A, B, AND C question?<br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; If yes, then ask: "Would
					you be willing to speak to a researcher about a study on accessing
					primary health care?"</td>
				</tr>
				<tr>
					<td colspan="2" align="left" class="style76"><input
						type="radio" name="radio_speakToResearcher" value="yes"
						<%=(props.getProperty("radio_speakToResearcher", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_speakToResearcher[0]);">Yes</span>
					</td>
					<td colspan="4" align="left" class="style76"><input
						type="radio" name="radio_speakToResearcher" value="no"
						<%=(props.getProperty("radio_speakToResearcher", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_speakToResearcher[1]);">No</span>
					</td>
				</tr>
			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="4" align="center" class="style51">7. EMERGENCY
					CONTACT</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">Please give the
					name/ address/ phone # of someone who may be contacted in the event
					of an emergency?</td>
				</tr>
				<tr>
					<td colspan="2" align="left" class="style76">Name: <input
						type="text" name="contactName"
						value="<%=props.getProperty("contactName", "")%>"></td>
					<td colspan="2" width="50%" align="left" class="style76">
					Phone #: <input type="text" name="contactPhone"
						value="<%=props.getProperty("contactPhone", "")%>"></td>

				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">Address: <input
						type="text" name="contactAddress"
						value="<%=props.getProperty("contactAddress", "")%>"></td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">What relationship
					is this person to you? (friend, sister, next-of-kin, etc.) <input
						type="text" name="contactRelationship"
						value="<%=props.getProperty("contactRelationship", "")%>">
					</td>
				</tr>
			</table>

			<table width="95%" border="0">
				<tr>
					<td class="style76">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;For
					Staff Use Only</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="4" align="center" class="style51">8. STAFF
					RATINGS AND IDENTIFIED ISSUES</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">Based on my own
					observations and from information from other members on my team:</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">I think this
					person currently has uncontrolled severe mental illness (like
					schizophrenia, bipolar disorder)</td>
				</tr>

				<tr>
					<td colspan="1" width="22%" align="left" class="style76">&nbsp;

					</td>
					<td width="26%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_hasMentalIllness" value="yes"
						<%=(props.getProperty("radio_hasMentalIllness", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasMentalIllness[0]);">Yes</span>

					</td>
					<td width="28%" colspan="1" align="left" valign="top"
						class="style76"><input type="radio"
						name="radio_hasMentalIllness" value="uncertain"
						<%=(props.getProperty("radio_hasMentalIllness", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasMentalIllness[1]);">Uncertain</span>

					</td>
					<td width="24%" colspan="1" align="left" valign="top"
						class="style76"><input type="radio"
						name="radio_hasMentalIllness" value="no"
						<%=(props.getProperty("radio_hasMentalIllness", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasMentalIllness[2]);">No</span>

					</td>

				</tr>

				<tr>
					<td colspan="4" align="left" class="style76">I am concerned
					that this person has severe problems from uncontrolled drinking</td>
				</tr>

				<tr>
					<td colspan="1" width="22%" align="left" class="style76">&nbsp;

					</td>
					<td width="26%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_hasDrinkingProblem" value="yes"
						<%=(props.getProperty("radio_hasDrinkingProblem", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasDrinkingProblem[0]);">Yes</span>

					</td>
					<td width="28%" colspan="1" align="left" valign="top"
						class="style76"><input type="radio"
						name="radio_hasDrinkingProblem" value="uncertain"
						<%=(props.getProperty("radio_hasDrinkingProblem", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasDrinkingProblem[1]);">Uncertain</span>

					</td>
					<td width="24%" colspan="1" align="left" valign="top"
						class="style76"><input type="radio"
						name="radio_hasDrinkingProblem" value="no"
						<%=(props.getProperty("radio_hasDrinkingProblem", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasDrinkingProblem[2]);">No</span>

					</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">I am concerned
					that this person has severe problems from uncontrolled drug use</td>
				</tr>

				<tr>
					<td colspan="1" width="22%" align="left" class="style76">&nbsp;

					</td>
					<td width="26%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_hasDrugProblem" value="yes"
						<%=(props.getProperty("radio_hasDrugProblem", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasDrugProblem[0]);">Yes</span>

					</td>
					<td width="28%" colspan="1" align="left" valign="top"
						class="style76"><input type="radio"
						name="radio_hasDrugProblem" value="uncertain"
						<%=(props.getProperty("radio_hasDrugProblem", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasDrugProblem[1]);">Uncertain</span>

					</td>
					<td width="24%" colspan="1" align="left" valign="top"
						class="style76"><input type="radio"
						name="radio_hasDrugProblem" value="no"
						<%=(props.getProperty("radio_hasDrugProblem", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasDrugProblem[2]);">No</span>
					</td>
				</tr>


				<tr>
					<td colspan="4" align="left" class="style76">I am concerned
					that this person has severe problems from an uncontrolled physical
					health problem</td>
				</tr>

				<tr>
					<td colspan="1" width="22%" align="left" class="style76">&nbsp;

					</td>
					<td width="26%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_hasHealthProblem" value="yes"
						<%=(props.getProperty("radio_hasHealthProblem", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasHealthProblem[0]);">Yes</span>

					</td>
					<td width="28%" colspan="1" align="left" valign="top"
						class="style76"><input type="radio"
						name="radio_hasHealthProblem" value="uncertain"
						<%=(props.getProperty("radio_hasHealthProblem", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasHealthProblem[1]);">Uncertain</span>

					</td>
					<td width="24%" colspan="1" align="left" valign="top"
						class="style76"><input type="radio"
						name="radio_hasHealthProblem" value="no"
						<%=(props.getProperty("radio_hasHealthProblem", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasHealthProblem[2]);">No</span>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">I am concerned
					that this person is handicapped or disabled by severe behaviour
					problems</td>
				</tr>

				<tr>
					<td colspan="1" width="22%" align="left" class="style76">&nbsp;

					</td>
					<td width="26%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_hasBehaviorProblem" value="yes"
						<%=(props.getProperty("radio_hasBehaviorProblem", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasBehaviorProblem[0]);">Yes</span>

					</td>
					<td width="28%" colspan="1" align="left" valign="top"
						class="style76"><input type="radio"
						name="radio_hasBehaviorProblem" value="uncertain"
						<%=(props.getProperty("radio_hasBehaviorProblem", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasBehaviorProblem[1]);">Uncertain</span>

					</td>
					<td width="24%" colspan="1" align="left" valign="top"
						class="style76"><input type="radio"
						name="radio_hasBehaviorProblem" value="no"
						<%=(props.getProperty("radio_hasBehaviorProblem", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_hasBehaviorProblem[2]);">No</span>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">I think this
					person will need our (Seaton House's) services for more than 60
					days</td>
				</tr>

				<tr>
					<td colspan="1" width="22%" align="left" class="style76">&nbsp;

					</td>
					<td width="26%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_needSeatonService" value="yes"
						<%=(props.getProperty("radio_needSeatonService", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_needSeatonService[0]);">Yes</span>

					</td>
					<td width="28%" colspan="1" align="left" valign="top"
						class="style76"><input type="radio"
						name="radio_needSeatonService" value="uncertain"
						<%=(props.getProperty("radio_needSeatonService", "")).equalsIgnoreCase("uncertain")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_needSeatonService[1]);">Uncertain</span>

					</td>
					<td width="24%" colspan="1" align="left" valign="top"
						class="style76"><input type="radio"
						name="radio_needSeatonService" value="no"
						<%=(props.getProperty("radio_needSeatonService", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_needSeatonService[2]);">No</span>
					</td>
				</tr>

				<tr>
					<td colspan="4" align="left" class="style76">I am concerned
					that this person is handicapped or disabled by severe behaviour
					problems</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">PRINT AND SIGN
					NAME:</td>
				</tr>
			</table>
			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">9. ORIENTATION
					TO SEATON HOUSE</td>
				</tr>
				<tr>
					<td colspan="1" width="25%" align="left" class="style76">
					Seaton House Tour given?</td>
					<td width="10%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_seatonTour" value="yes"
						<%=(props.getProperty("radio_seatonTour", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_seatonTour[0]);">Yes</span>

					</td>
					<td colspan="4" align="left" valign="top" class="style76"><input
						type="radio" name="radio_seatonTour" value="no"
						<%=(props.getProperty("radio_seatonTour", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_seatonTour[1]);">No
					(If no, give reason)</span> <input type="text" name="seatonNotToured"
						size="55" value="<%=props.getProperty("seatonNotToured", "")%>">
					</td>
				</tr>
				<tr>
					<td colspan="1" width="25%" align="left" class="style76">
					Information pamphlet issued?</td>
					<td width="10%" colspan="1" align="left" class="style76"><input
						type="radio" name="radio_pamphletIssued" value="yes"
						<%=(props.getProperty("radio_pamphletIssued", "")).equalsIgnoreCase("yes")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_pamphletIssued[0]);">Yes</span>

					</td>
					<td colspan="4" align="left" valign="top" class="style76"><input
						type="radio" name="radio_pamphletIssued" value="no"
						<%=(props.getProperty("radio_pamphletIssued", "")).equalsIgnoreCase("no")?"checked":""%>>
					<span
						onClick="javascript:clickRadio(document.forms[0].radio_pamphletIssued[1]);">No
					(If no, give reason)</span> <input type="text" name="pamphletNotIssued"
						size="55" value="<%=props.getProperty("pamphletNotIssued", "")%>">
					</td>
				</tr>

			</table>
			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">10.
					COMMENTS/SUMMARY</td>
				</tr>
				<tr>
					<td colspan="6" align="center" class="style76"><textArea
						name="summary" cols="95" rows="7"><%=props.getProperty("summary", "")%></textArea>
					</td>
				</tr>

			</table>
			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">COMPLETED BY</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">Completed by:
					(sign and print name)<br>
					<br>
					<input type="text" name="completedBy" size="45"
						value="<%=props.getProperty("completedBy", "")%>"></td>
					<td width="28%" colspan="1" align="center" class="style76">
					Time Assessment Completed:</td>
					<td width="22%" colspan="1" align="left" valign="middle"
						STYLE="text-align: right" class="style76"><input type="text"
						name="assessCompleteTime" size="15"
						value="<%=props.getProperty("assessCompleteTime", "")%>">
					am/pm</td>
				</tr>

			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="3" align="center" class="style51">11. TRIAGE-
					REFERRAL TO A SEATON HOUSE PROGRAM</td>
				</tr>
				<tr>
					<td colspan="3" align="left" class="style76">Based on my own
					observations (and from information from other members on my team) I
					believe this client may be appropriate for one of the following
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
					<td align="center" class="style76"><input type="submit"
						value="Save" onclick="javascript:return onSave();" /> <input
						type="submit" value="Save and Exit"
						onclick="javascript:return onSaveExit();" /> <input type="submit"
						value="Exit" onclick="javascript:return onExit();" /> <input
						type="button" value="Print" onclick="javascript:return onPrint();" />

					</td>
				</tr>
			</table>

			<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->

			</td>
		</tr>
	</table>

</html:form>
</body>
</html:html>
