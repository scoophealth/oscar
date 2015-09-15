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
<title>Rourke Baby Record</title>
<html:base />
<link rel="stylesheet" type="text/css" media="screen"
	href="rourkeStyle.css">
<link rel="stylesheet" type="text/css" media="print"
	href="printRourke.css">
</head>

<%
	String formClass = "Rourke";
	String formLink = "formrourke3.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "Rourke/";
    props.setProperty("c_lastVisited", "3");
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
                var s = dateBox.name;
                alert('Invalid '+pass+' in field ' + s.substring(3));
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
        if(valDate(document.forms[0].p3_date18m)==false){
            b = false;
        }else
        if(valDate(document.forms[0].p3_date2y)==false){
            b = false;
        }else
        if(valDate(document.forms[0].p3_date4y)==false){
            b = false;
        }

        return b;

    }

    function popup(link) {
    windowprops = "height=700, width=960,location=no,"
    + "scrollbars=yes, menubars=no, toolbars=no, resizable=no, top=0, left=0 titlebar=yes";
    window.open(link, "_blank", windowprops);
    }

</script>


<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
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
	<input type="hidden" name="formId" value="<%=formId%>" />
	<input type="hidden" name="c_lastVisited"
		value=<%=props.getProperty("c_lastVisited", "3")%> />
	<input type="hidden" name="submit" value="exit" />

	<table class="Header" class="hidePrint">
		<tr>
			<td nowrap="true"><input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();">
			<input type="button" value="Print"
				onclick="javascript:return onPrint();" /></td>
			<td align="center" width="100%"><a name="length"
				href="javascript:popup('graphLengthWeight.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">
			Graph Length and Weight</a><br>
			<a name="headCirc"
				href="javascript:popup('graphHeadCirc.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">
			Graph Head Circumference</a></td>
			<td nowrap="true"><a
				href="formrourke1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Page
			1</a>&nbsp;|&nbsp; <a
				href="formrourke2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Page
			2</a>&nbsp;|&nbsp; <a>Page 3</a></td>
		</tr>
	</table>

	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr class="titleBar">
			<th>Rourke Baby Record: EVIDENCE BASED INFANT/CHILD HEALTH
			MAINTENANCE GUIDE III</th>
		</tr>
	</table>
	<table width="100%" border="0" cellspacing="1" cellpadding="2">
		<tr valign="top">
			<td nowrap align="center">Birth remarks<br>
			<textarea name="c_birthRemarks" rows="2" cols="17"><%= props.getProperty("c_birthRemarks", "") %></textarea>
			</td>
			<td nowrap align="center">Risk Factors/Family History<br>
			<textarea name="c_riskFactors" rows="2" cols="17"><%= props.getProperty("c_riskFactors", "") %></textarea>
			</td>
			<td width="65%" nowrap align="center">
			<p>Name: <input type="text" name="c_pName" maxlength="60"
				size="30" value="<%= props.getProperty("c_pName", "") %>"
				readonly="true" /> &nbsp;&nbsp; Birth Date (yyyy/mm/dd): <input
				type="text" name="c_birthDate" size="10" maxlength="10"
				value="<%= props.getProperty("c_birthDate", "") %>" readonly="true">
			&nbsp;&nbsp; <%= ((FrmRourkeRecord) rec).isFemale(demoNo) == true ? "Female" : "Male" %>
			</p>
			<p>Length: <input type="text" name="c_length" size="6"
				maxlength="6" value="<%= props.getProperty("c_length", "") %>" /> cm
			&nbsp;&nbsp; Head Circ: <input type="text" name="c_headCirc" size="6"
				maxlength="6" value="<%= props.getProperty("c_headCirc", "") %>" />
			cm &nbsp;&nbsp; Birth Wt: <input type="text" name="c_birthWeight"
				size="6" maxlength="7"
				value="<%= props.getProperty("c_birthWeight", "") %>" /> kg
			&nbsp;&nbsp; Discharge Wt: <input type="text"
				name="c_dischargeWeight" size="6" maxlength="7"
				value="<%= props.getProperty("c_dischargeWeight", "") %>">
			kg</p>
			</td>
		</tr>
	</table>
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
		<tr align="center">
			<td class="column"><a>AGE</a></td>
			<td class="row"><a>18 months</a></td>
			<td class="row"><a>2-3 years</a></td>
			<td class="row"><a>4-5 years</a></td>
		</tr>
		<tr align="center">
			<td class="column"><a>DATE</a></td>
			<td>(yyyy/mm/dd) <input type="text" name="p3_date18m" size="10"
				value="<%=props.getProperty("p3_date18m", "")%>" /></td>
			<td>(yyyy/mm/dd) <input type="text" name="p3_date2y" size="10"
				value="<%=props.getProperty("p3_date2y", "")%>" /></td>
			<td>(yyyy/mm/dd) <input type="text" name="p3_date4y" size="10"
				value="<%=props.getProperty("p3_date4y", "")%>" /></td>
		</tr>
		<tr align="center">
			<td class="column"><a>GROWTH</a></td>
			<td>
			<table width="100%">
				<tr>
					<td align="center">Ht. <small>(cm)</small><br>
					<input type="text" class="wide" name="p3_ht18m" size="4"
						maxlength="5" value="<%= props.getProperty("p3_ht18m", "") %>"></td>
					<td align="center">Wt. <small>(kg)</small><br>
					<input type="text" class="wide" name="p3_wt18m" size="4"
						maxlength="5" value="<%= props.getProperty("p3_wt18m", "") %>"></td>
					<td align="center">Hd. Circ <small>(cm)</small><br>
					<input type="text" class="wide" name="p3_hc18m" size="4"
						maxlength="5" value="<%= props.getProperty("p3_hc18m", "") %>"></td>
				</tr>
			</table>
			</td>
			<td>
			<table width="100%">
				<tr>
					<td align="center">Ht. <small>(cm)</small><br>
					<input type="text" class="wide" name="p3_ht2y" size="4"
						maxlength="5" value="<%= props.getProperty("p3_ht2y", "") %>"></td>
					<td align="center">Wt. <small>(kg)</small><br>
					<input type="text" class="wide" name="p3_wt2y" size="4"
						maxlength="5" value="<%= props.getProperty("p3_wt2y", "") %>"></td>
				</tr>
			</table>
			</td>
			<td>
			<table width="100%">
				<tr>
					<td align="center">Ht. <small>(cm)</small><br>
					<input type="text" class="wide" name="p3_ht4y" size="4"
						maxlength="5" value="<%= props.getProperty("p3_ht4y", "") %>"></td>
					<td align="center">Wt. <small>(kg)</small><br>
					<input type="text" class="wide" name="p3_wt4y" size="4"
						maxlength="5" value="<%= props.getProperty("p3_wt4y", "") %>"></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr align="center">
			<td class="column"><a>PARENTAL CONCERNS</a></td>
			<td><textarea name="p3_pConcern18m" style="width: 100%"
				cols="10" rows="2"><%= props.getProperty("p3_pConcern18m", "") %></textarea>
			</td>
			<td><textarea name="p3_pConcern2y" style="width: 100%" cols="10"
				rows="2"><%= props.getProperty("p3_pConcern2y", "") %></textarea></td>
			<td><textarea name="p3_pConcern4y" style="width: 100%" cols="10"
				rows="2"><%= props.getProperty("p3_pConcern4y", "") %></textarea></td>
		</tr>
		<tr>
			<td class="column"><a>NUTRITION</a>:</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_nutrition18m"
						value="<%= props.getProperty("p3_nutrition18m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_bottle18m"
						<%= props.getProperty("p3_bottle18m", "") %> /></td>
					<td width="100%">No bottles</td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_nutrition2y"
						value="<%= props.getProperty("p3_nutrition2y", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_milk2y"
						<%= props.getProperty("p3_milk2y", "") %>></td>
					<td width="100%">Homogenized or 2% milk</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_food2y"
						<%= props.getProperty("p3_food2y", "") %>></td>
					<td>Canada's Food Guide</td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_nutrition4y"
						value="<%= props.getProperty("p3_nutrition4y", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_milk4y"
						<%= props.getProperty("p3_milk4y", "") %>></td>
					<td width="100%">2% milk</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_food4y"
						<%= props.getProperty("p3_food4y", "") %>></td>
					<td>Canada's Food Guide</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column" valign="top">
			<table width="100%">
				<tr>
					<td align="center" nowrap="true"><b>EDUCATION &amp; ADVICE</b></td>
				</tr>
				<tr>
					<td align="right"><b>Safety</b></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td align="right"><b>Behaviour</b></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td align="right"><b>Family</b></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td align="right"><b>Other</b></td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_educationAdvice18m"
						value="<%= props.getProperty("p3_educationAdvice18m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_bath18m"
						<%= props.getProperty("p3_bath18m", "") %>></td>
					<td width="100%"><i><a href="<%=resource%>s_drowning">Bath
					safety</a>*</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_choking18m"
						<%= props.getProperty("p3_choking18m", "") %>></td>
					<td><a href="<%=resource%>s_choking">Choking/safe toys</a>*</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p3_temperment18m"
						<%= props.getProperty("p3_temperment18m", "") %>></td>
					<td>Temperment</td>
				</tr>
				<tr>
					<td valign="top">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_limit18m"
						<%= props.getProperty("p3_limit18m", "") %>></td>
					<td>Limit setting</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_social18m"
						<%= props.getProperty("p3_social18m", "") %>></td>
					<td>Socializing opportunities</td>
				</tr>
				<tr>
					<td valign="top">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_dental18m"
						<%= props.getProperty("p3_dental18m", "") %>></td>
					<td><b><a href="<%=resource%>o_dentalCare">Dental Care</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_toilet18m"
						<%= props.getProperty("p3_toilet18m", "") %>></td>
					<td>Toilet training</td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_educationAdvice2y"
						value="<%= props.getProperty("p3_educationAdvice2y", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_bike2y"
						<%= props.getProperty("p3_bike2y", "") %>></td>
					<td width="100%"><i><a href="<%=resource%>s_falls">Bike
					Helmets</a>*</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_matches2y"
						<%= props.getProperty("p3_matches2y", "") %>></td>
					<td>Matches</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_carbon2y"
						<%= props.getProperty("p3_carbon2y", "") %>></td>
					<td>Carbon monoxide/ <i><a href="<%=resource%>s_burns">Smoke
					detectors</a>*</i></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_parent2y"
						<%= props.getProperty("p3_parent2y", "") %>></td>
					<td>Parent/child interaction</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_social2y"
						<%= props.getProperty("p3_social2y", "") %>></td>
					<td>Socializing opportunities</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_dayCare2y"
						<%= props.getProperty("p3_dayCare2y", "") %>></td>
					<td><b><a href="<%=resource%>hri_dayCare">Assess day
					care & preschool needs</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_dental2y"
						<%= props.getProperty("p3_dental2y", "") %>></td>
					<td><b><a href="<%=resource%>o_dentalCare">Dental
					Care/check up</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_toilet2y"
						<%= props.getProperty("p3_toilet2y", "") %>></td>
					<td>Toilet training</td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_educationAdvice4y"
						value="<%= props.getProperty("p3_educationAdvice4y", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_bike4y"
						<%= props.getProperty("p3_bike4y", "") %>></td>
					<td width="100%"><i><a href="<%=resource%>s_falls">Bike
					Helmets</a>*</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_matches4y"
						<%= props.getProperty("p3_matches4y", "") %>></td>
					<td>Matches</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_carbon4y"
						<%= props.getProperty("p3_carbon4y", "") %>></td>
					<td>Carbon monoxide/ <i><a href="<%=resource%>s_burns">Smoke
					detectors</a>*</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_water4y"
						<%= props.getProperty("p3_water4y", "") %>></td>
					<td><a href="<%=resource%>s_drowning">Water Safety</a></td>
				</tr>
				<tr>
					<td valign="top">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_social4y"
						<%= props.getProperty("p3_social4y", "") %>></td>
					<td>Socializing opportunities</td>
				</tr>
				<tr>
					<td valign="top">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_dental4y"
						<%= props.getProperty("p3_dental4y", "") %>></td>
					<td><b><a href="<%=resource%>o_dentalCare">Dental
					Care/check up</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_school4y"
						<%= props.getProperty("p3_school4y", "") %>></td>
					<td>School readiness</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column">
			<div align="center"><b>DEVELOPMENT</b><br>
			(Inquiry &amp; observation of milestones)<br>
			Tasks are set after the time of normal milestone acquisition.<br>
			Absence of any item suggests the need for further assessment of
			development<br>
			</div>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_development18m"
						value="<%= props.getProperty("p3_development18m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_points18m"
						<%= props.getProperty("p3_points18m", "") %>></td>
					<td width="100%">Points to pictures (eg. show me the ...) and
					to 3 different body parts</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_words18m"
						<%= props.getProperty("p3_words18m", "") %>></td>
					<td>At least 5 words</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_picks18m"
						<%= props.getProperty("p3_picks18m", "") %>></td>
					<td>Picks up and eats finger food</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_walks18m"
						<%= props.getProperty("p3_walks18m", "") %>></td>
					<td>Walks alone</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_stacks18m"
						<%= props.getProperty("p3_stacks18m", "") %>></td>
					<td>Stacks at least 3 blocks</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_affection18m"
						<%= props.getProperty("p3_affection18m", "") %>></td>
					<td>Shows affection</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p3_showParents18m"
						<%= props.getProperty("p3_showParents18m", "") %>></td>
					<td>Points to show parent something</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_looks18m"
						<%= props.getProperty("p3_looks18m", "") %>></td>
					<td>Looks at you when talking/playing together</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p3_noParentsConcerns18m"
						<%= props.getProperty("p3_noParentsConcerns18m", "") %>></td>
					<td>No parent concerns</td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_development2y"
						value="<%= props.getProperty("p3_development2y", "") %>" /></td>
				</tr>
				<tr>
					<td colspan="2"><b>2 years</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_word2y"
						<%= props.getProperty("p3_word2y", "") %>></td>
					<td width="100%">At least 1 new word/week</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_sentence2y"
						<%= props.getProperty("p3_sentence2y", "") %>></td>
					<td>2-word sentences</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_run2y"
						<%= props.getProperty("p3_run2y", "") %>></td>
					<td>Tries to run</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_container2y"
						<%= props.getProperty("p3_container2y", "") %>></td>
					<td>Puts objects into small container</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_copies2y"
						<%= props.getProperty("p3_copies2y", "") %>></td>
					<td>Copies adult's actions</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_skills2y"
						<%= props.getProperty("p3_skills2y", "") %>></td>
					<td>Continues to develop new skills</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p3_noParentsConcerns2y"
						<%= props.getProperty("p3_noParentsConcerns2y", "") %>></td>
					<td>No parent concerns</td>
				</tr>
			</table>
			<br>
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_development3y"
						value="<%= props.getProperty("p3_development3y", "") %>" /></td>
				</tr>
				<tr>
					<td colspan="2"><b>3 years</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p3_understands3y"
						<%= props.getProperty("p3_understands3y", "") %>></td>
					<td width="100%">Understands 2 step direction</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_twists3y"
						<%= props.getProperty("p3_twists3y", "") %>></td>
					<td>Twists lids off jars or turns knobs</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_turnPages3y"
						<%= props.getProperty("p3_turnPages3y", "") %>></td>
					<td>Turns pages one at a time</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_share3y"
						<%= props.getProperty("p3_share3y", "") %>></td>
					<td>Share some of the time</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_listens3y"
						<%= props.getProperty("p3_listens3y", "") %>></td>
					<td>Listens to music or stories for 5-10 minutes with adults</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p3_noParentsConcerns3y"
						<%= props.getProperty("p3_noParentsConcerns3y", "") %>></td>
					<td>No parent concerns</td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_development4y"
						value="<%= props.getProperty("p3_development4y", "") %>" /></td>
				</tr>
				<tr>
					<td colspan="2"><b>4 years</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p3_understands4y"
						<%= props.getProperty("p3_understands4y", "") %>></td>
					<td width="100%">Understands related 3 part direction</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_questions4y"
						<%= props.getProperty("p3_questions4y", "") %>></td>
					<td>Asks a lot of questions</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_oneFoot4y"
						<%= props.getProperty("p3_oneFoot4y", "") %>></td>
					<td>Stands on 1 foot for 1-3 seconds</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_draws4y"
						<%= props.getProperty("p3_draws4y", "") %>></td>
					<td>Draws a person with at least 3 body parts</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_toilet4y"
						<%= props.getProperty("p3_toilet4y", "") %>></td>
					<td>Toilet trained during the day</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_comfort4y"
						<%= props.getProperty("p3_comfort4y", "") %>></td>
					<td>Tries to comfort someone who is upset</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p3_noParentsConcerns4y"
						<%= props.getProperty("p3_noParentsConcerns4y", "") %>></td>
					<td>No parent concerns</td>
				</tr>
			</table>
			<br>
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_development5y"
						value="<%= props.getProperty("p3_development5y", "") %>" /></td>
				</tr>
				<tr>
					<td colspan="2"><b>5 years</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_counts5y"
						<%= props.getProperty("p3_counts5y", "") %>></td>
					<td width="100%">Counts to 10 and knows common colours &
					shapes</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_speaks5y"
						<%= props.getProperty("p3_speaks5y", "") %>></td>
					<td>Speaks clearly in sentences</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_ball5y"
						<%= props.getProperty("p3_ball5y", "") %>></td>
					<td>Throws & catches a ball</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_hops5y"
						<%= props.getProperty("p3_hops5y", "") %>></td>
					<td>Hops on 1 foot</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_shares5y"
						<%= props.getProperty("p3_shares5y", "") %>></td>
					<td>Shares willingly</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_alone5y"
						<%= props.getProperty("p3_alone5y", "") %>></td>
					<td>Works alone at an activity for 20-30 minutes</td>
				</tr>

				<tr>
					<td valign="top"><input type="checkbox" name="p3_separate5y"
						<%= props.getProperty("p3_separate5y", "") %>></td>
					<td>Separates easily from parents</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p3_noParentsConcerns5y"
						<%= props.getProperty("p3_noParentsConcerns5y", "") %>></td>
					<td>No parent concerns</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a>PHYSICAL EXAMINATION</a><br>
			Evidence based screening for specific conditions is highlighted, but
			an appropriate age-specific focused physical examination is
			recommended at each visit</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_physical18m"
						value="<%= props.getProperty("p3_physical18m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_eyes18m"
						<%= props.getProperty("p3_eyes18m", "") %>></td>
					<td width="100%"><i>Eyes (red reflex)</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_cover18m"
						<%= props.getProperty("p3_cover18m", "") %>></td>
					<td><b><a href="<%=resource%>pe_cover">Cover/uncover
					test & inquiry</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_hearing18m"
						<%= props.getProperty("p3_hearing18m", "") %>></td>
					<td><b>Hearing inquiry</b></td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_physical2y"
						value="<%= props.getProperty("p3_physical2y", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_visual2y"
						<%= props.getProperty("p3_visual2y", "") %>></td>
					<td width="100%"><i>Visual acuity</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_cover2y"
						<%= props.getProperty("p3_cover2y", "") %>></td>
					<td><b><a href="<%=resource%>pe_cover">Cover/uncover
					test & inquiry</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_hearing2y"
						<%= props.getProperty("p3_hearing2y", "") %>></td>
					<td><b>Hearing inquiry</b></td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_physical4y"
						value="<%= props.getProperty("p3_physical4y", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_visual4y"
						<%= props.getProperty("p3_visual4y", "") %>></td>
					<td width="100%"><i>Visual acuity</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_cover4y"
						<%= props.getProperty("p3_cover4y", "") %>></td>
					<td><b><a href="<%=resource%>pe_cover">Cover/uncover
					test &amp; inquiry</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_hearing4y"
						<%= props.getProperty("p3_hearing4y", "") %>></td>
					<td><b>Hearing inquiry</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_blood4y"
						<%= props.getProperty("p3_blood4y", "") %>></td>
					<td><i>Blood pressure</i></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column">
			<div align="center"><b>PROBLEMS &amp; PLANS</b></div>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_problems18m"
						value="<%= props.getProperty("p3_problems18m", "") %>" /></td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_problems2y"
						value="<%= props.getProperty("p3_problems2y", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_serum2y"
						<%= props.getProperty("p3_serum2y", "") %>></td>
					<td width="100%"><i><a
						href="<%=resource%>pp_leadScreening">Serum lead (If at risk)</a>*</i></td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_problems4y"
						value="<%= props.getProperty("p3_problems4y", "") %>" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column">
			<div align="center"><b>IMMUNIZATION</b><br>
			Guidelines may vary by province</div>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_immunization18m"
						value="<%= props.getProperty("p3_immunization18m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_hib18m"
						<%= props.getProperty("p3_hib18m", "") %>></td>
					<td width="100%"><b>HIB</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_polio18m"
						<%= props.getProperty("p3_polio18m", "") %>></td>
					<td><b>aPDT polio</b></td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td><input type="text" class="wide" name="p3_immunization2y"
						value="<%= props.getProperty("p3_immunization2y", "") %>" /></td>
				</tr>
			</table>
			</td>
			<td valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p3_immunization4y"
						value="<%= props.getProperty("p3_immunization4y", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_mmr4y"
						<%= props.getProperty("p3_mmr4y", "") %>></td>
					<td width="100%"><b>MMR</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p3_polio4y"
						<%= props.getProperty("p3_polio4y", "") %>></td>
					<td><b>aPDT polio</b></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a>Signature</a></td>
			<td><input type="text" class="wide" style="width: 100%"
				name="p3_signature18m"
				value="<%= props.getProperty("p3_signature18m", "") %>" /></td>
			<td><input type="text" class="wide" style="width: 100%"
				name="p3_signature2y"
				value="<%= props.getProperty("p3_signature2y", "") %>" /></td>
			<td><input type="text" class="wide" style="width: 100%"
				name="p3_signature4y"
				value="<%= props.getProperty("p3_signature4y", "") %>" /></td>
		</tr>

	</table>

	<table class="Header" class="hidePrint">
		<tr>
			<td nowrap="true"><input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();">
			<input type="button" value="Print"
				onclick="javascript:return onPrint();" /></td>
			<td align="center" width="100%"><a name="length"
				href="javascript:popup('graphLengthWeight.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">
			Graph Length and Weight</a><br>
			<a name="headCirc"
				href="javascript:popup('graphHeadCirc.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">
			Graph Head Circumference</a></td>
			<td nowrap="true"><a
				href="formrourke1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Page
			1</a>&nbsp;|&nbsp; <a
				href="formrourke2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Page
			2</a>&nbsp;|&nbsp; <a>Page 3</a></td>
		</tr>
	</table>

</html:form>
</body>
</html:html>
