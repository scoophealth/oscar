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
<%@page import="org.oscarehr.util.LoggedInInfo"%>

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
	String formLink = "formrourke2.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request) ,demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "Rourke/";
    props.setProperty("c_lastVisited", "2");
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
            alert(ex);
            alert('Catch Invalid Date in field ' + dateBox.name);
            dateBox.focus();
            return false;
        }
        return true;
    }

    function checkAllDates()
    {
        var b = true;
        if(valDate(document.forms[0].p2_date4m)==false){
            b = false;
        }else
        if(valDate(document.forms[0].p2_date6m)==false){
            b = false;
        }else
        if(valDate(document.forms[0].p2_date9m)==false){
            b = false;
        }else
        if(valDate(document.forms[0].p2_date12m)==false){
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
	<input type="hidden" name="formId" value="<%=formId%>" />
	<input type="hidden" name="c_lastVisited"
		value=<%=props.getProperty("c_lastVisited", "2")%> />
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
			1</a>&nbsp;|&nbsp; <a>Page 2</a>&nbsp;|&nbsp; <a
				href="formrourke3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Page
			3</a></td>
		</tr>
	</table>

	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr class="titleBar">
			<th>Rourke Baby Record: EVIDENCE BASED INFANT/CHILD HEALTH
			MAINTENANCE GUIDE II</th>
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
			<td class="column"><a>AGE</a><br>
			</td>
			<td colspan="3" class="row"><a>4 months</a></td>
			<td colspan="3" class="row"><a>6 months</a></td>
			<td colspan="3" class="row"><a>9 months</a> (optional)</td>
			<td colspan="3" class="row"><a>12-13 months</a></td>
		</tr>
		<tr align="center">
			<td class="column"><a>DATE</a></td>
			<td colspan="3">(yyyy/mm/dd) <input type="text" name="p2_date4m"
				size="10" value="<%=props.getProperty("p2_date4m", "")%>" /></td>
			<td colspan="3">(yyyy/mm/dd) <input type="text" name="p2_date6m"
				size="10" value="<%=props.getProperty("p2_date6m", "")%>" /></td>
			<td colspan="3">(yyyy/mm/dd) <input type="text" name="p2_date9m"
				size="10" value="<%=props.getProperty("p2_date9m", "")%>" /></td>
			<td colspan="3">(yyyy/mm/dd) <input type="text"
				name="p2_date12m" size="10"
				value="<%=props.getProperty("p2_date12m", "")%>" /></td>
		</tr>
		<tr align="center">
			<td class="column" rowspan="2"><a>GROWTH</a></td>
			<td>Ht. <small>(cm)</small></td>
			<td>Wt. <small>(kg)</small></td>
			<td>Hd. Circ <small>(cm)</small></td>
			<td>Ht. <small>(cm)</small></td>
			<td>Wt. <small>(kg)<br>
			(x2 BW)</small></td>
			<td>Hd. Circ <small>(cm)</small></td>
			<td>Ht. <small>(cm)</small></td>
			<td>Wt. <small>(kg)</small></td>
			<td>Hd. Circ <small>(cm)</small></td>
			<td>Ht. <small>(cm)</small></td>
			<td>Wt. <small>(kg)<br>
			(x3 BW)</small></td>
			<td>Hd. Circ <small>(cm)<br>
			(av. 47cm)</small></td>
		</tr>
		<tr align="center">
			<td><input type="text" class="wide" name="p2_ht4m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_ht4m", "") %>"></td>
			<td><input type="text" class="wide" name="p2_wt4m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_wt4m", "") %>"></td>
			<td><input type="text" class="wide" name="p2_hc4m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_hc4m", "") %>"></td>
			<td><input type="text" class="wide" name="p2_ht6m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_ht6m", "") %>"></td>
			<td><input type="text" class="wide" name="p2_wt6m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_wt6m", "") %>"></td>
			<td><input type="text" class="wide" name="p2_hc6m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_hc6m", "") %>"></td>
			<td><input type="text" class="wide" name="p2_ht9m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_ht9m", "") %>"></td>
			<td><input type="text" class="wide" name="p2_wt9m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_wt9m", "") %>"></td>
			<td><input type="text" class="wide" name="p2_hc9m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_hc9m", "") %>"></td>
			<td><input type="text" class="wide" name="p2_ht12m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_ht12m", "") %>"></td>
			<td><input type="text" class="wide" name="p2_wt12m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_wt12m", "") %>"></td>
			<td><input type="text" class="wide" name="p2_hc12m" size="4"
				maxlength="5" value="<%= props.getProperty("p2_hc12m", "") %>"></td>
		</tr>
		<tr align="center">
			<td class="column"><a>PARENTAL CONCERNS</a></td>
			<td colspan="3"><textarea name="p2_pConcern4m"
				style="width: 100%" cols="10" rows="2"><%= props.getProperty("p2_pConcern4m", "") %></textarea>
			</td>
			<td colspan="3"><textarea name="p2_pConcern6m"
				style="width: 100%" cols="10" rows="2"><%= props.getProperty("p2_pConcern6m", "") %></textarea>
			</td>
			<td colspan="3"><textarea name="p2_pConcern9m"
				style="width: 100%" cols="10" rows="2"><%= props.getProperty("p2_pConcern9m", "") %></textarea>
			</td>
			<td colspan="3"><textarea name="p2_pConcern12m"
				style="width: 100%" cols="10" rows="2"><%= props.getProperty("p2_pConcern12m", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td class="column"><a>NUTRITION</a></td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_nutrition4m"
						value="<%= props.getProperty("p2_nutrition4m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p2_breastFeeding4m"
						<%= props.getProperty("p2_breastFeeding4m", "") %> /></td>
					<td><b><a href="<%=resource%>n_breastFeeding">Breast
					feeding</a>*<br>
					&nbsp;&nbsp;Vit.D 10ug=400IU/day*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p2_formulaFeeding4m"
						<%= props.getProperty("p2_formulaFeeding4m", "") %> /></td>
					<td><i>Formula Feeding</i> (Fe fortified)</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_cereal4m"
						<%= props.getProperty("p2_cereal4m", "") %> /></td>
					<td><i>Iron fortified cereal</i></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_nutrition6m"
						value="<%= props.getProperty("p2_nutrition6m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p2_breastFeeding6m"
						<%= props.getProperty("p2_breastFeeding6m", "") %>></td>
					<td><b><a href="<%=resource%>n_breastFeeding">Breast
					feeding</a>*<br>
					&nbsp;&nbsp;Vit.D 10ug=400IU/day*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p2_formulaFeeding6m"
						<%= props.getProperty("p2_formulaFeeding6m", "") %>></td>
					<td><i>Formula Feeding<br>
					Iron fortified follow-up formula</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_bottle6m"
						<%= props.getProperty("p2_bottle6m", "") %>></td>
					<td>No bottles in bed</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_vegFruit6m"
						<%= props.getProperty("p2_vegFruit6m", "") %>></td>
					<td>Veg/fruits</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_egg6m"
						<%= props.getProperty("p2_egg6m", "") %>></td>
					<td>No egg white, nuts, or honey</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_choking6m"
						<%= props.getProperty("p2_choking6m", "") %>></td>
					<td><a href="<%=resource%>s_choking">Choking/safe food</a>*</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_nutrition9m"
						value="<%= props.getProperty("p2_nutrition9m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p2_breastFeeding9m"
						<%= props.getProperty("p2_breastFeeding9m", "") %>></td>
					<td><b><a href="<%=resource%>n_breastFeeding">Breast
					feeding</a>*<br>
					&nbsp;&nbsp;Vit.D 10ug=400IU/day*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p2_formulaFeeding9m"
						<%= props.getProperty("p2_formulaFeeding9m", "") %>></td>
					<td><i>Formula Feeding<br>
					Iron fortified follow-up formula</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_bottle9m"
						<%= props.getProperty("p2_bottle9m", "") %>></td>
					<td>No bottles in bed</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_meat9m"
						<%= props.getProperty("p2_meat9m", "") %>></td>
					<td>Meat & alternatives*</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_milk9m"
						<%= props.getProperty("p2_milk9m", "") %>></td>
					<td>Milk Products*</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_egg9m"
						<%= props.getProperty("p2_egg9m", "") %>></td>
					<td>No egg white, nuts or honey</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_choking9m"
						<%= props.getProperty("p2_choking9m", "") %>></td>
					<td><a href="<%=resource%>s_choking">Choking/safe food</a>*</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_nutrition12m"
						value="<%= props.getProperty("p2_nutrition12m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_milk12m"
						<%= props.getProperty("p2_milk12m", "") %>></td>
					<td>Homogenized milk</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_bottle12m"
						<%= props.getProperty("p2_bottle12m", "") %>></td>
					<td>Encourage cup vs bottle</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_appetite12m"
						<%= props.getProperty("p2_appetite12m", "") %>></td>
					<td>Appetite reduced</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column">
			<table width="100%">
				<tr>
					<td><a>EDUCATION &amp; ADVICE</a></td>
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
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td align="right"><b>Other</b></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_educationAdvice4m"
						value="<%= props.getProperty("p2_educationAdvice4m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_carSeat4m"
						<%= props.getProperty("p2_carSeat4m", "") %>></td>
					<td><b><a href="<%=resource%>s_motorVehicleAccidents">Car
					seat (toddler)</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_stairs4m"
						<%= props.getProperty("p2_stairs4m", "") %>></td>
					<td><i>Stairs/walker</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_bath4m"
						<%= props.getProperty("p2_bath4m", "") %>></td>
					<td><i><a href="<%=resource%>s_drowning">Bath safety*;
					safe toys</a>*</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_sleeping4m"
						<%= props.getProperty("p2_sleeping4m", "") %>></td>
					<td><b><a href="<%=resource%>b_nightWaking">Night
					waking/crying</a>*</b></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_parent4m"
						<%= props.getProperty("p2_parent4m", "") %>></td>
					<td>Parent/child interaction</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_childCare4m"
						<%= props.getProperty("p2_childCare4m", "") %>></td>
					<td>Child care/return to work</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_family4m"
						<%= props.getProperty("p2_family4m", "") %>></td>
					<td>Family conflict/stress</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_teething4m"
						<%= props.getProperty("p2_teething4m", "") %>></td>
					<td>Siblings</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_educationAdvice6m"
						value="<%= props.getProperty("p2_educationAdvice6m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_poison6m"
						<%= props.getProperty("p2_poison6m", "") %>></td>
					<td><b><a href="<%=resource%>s_poisons">Poisons*; PCC#</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_electric6m"
						<%= props.getProperty("p2_electric6m", "") %>></td>
					<td><i>Electric plugs</i></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_sleeping6m"
						<%= props.getProperty("p2_sleeping6m", "") %>></td>
					<td><b><a href="<%=resource%>b_nightWaking">Night
					waking/crying</a>*</b></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_parent6m"
						<%= props.getProperty("p2_parent6m", "") %>></td>
					<td>Parent/child interaction</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_childCare6m"
						<%= props.getProperty("p2_childCare6m", "") %>></td>
					<td>Child care/return to work</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_educationAdvice9m"
						value="<%= props.getProperty("p2_educationAdvice9m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_childProof9m"
						<%= props.getProperty("p2_childProof9m", "") %>></td>
					<td>Childproofing</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_separation9m"
						<%= props.getProperty("p2_separation9m", "") %>></td>
					<td>Separation anxiety</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_sleeping9m"
						<%= props.getProperty("p2_sleeping9m", "") %>></td>
					<td><b><a href="<%=resource%>b_nightWaking">Night
					waking/crying</a>*</b></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_dayCare9m"
						<%= props.getProperty("p2_dayCare9m", "") %>></td>
					<td><b><a href="<%=resource%>hri_dayCare">Assess day
					care need</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_homeVisit9m"
						<%= props.getProperty("p2_homeVisit9m", "") %>></td>
					<td><b><a href="<%=resource%>hri_homeVisits">Assess
					home visit need</a>*</b></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_smoke9m"
						<%= props.getProperty("p2_smoke9m", "") %>></td>
					<td><b><a href="<%=resource%>o_secondHandSmoke">Second
					hand smoke</a>*</b></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_educationAdvice12m"
						value="<%= props.getProperty("p2_educationAdvice12m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_poison12m"
						<%= props.getProperty("p2_poison12m", "") %> /></td>
					<td><b><a href="<%=resource%>s_poisons">Poisons/PCC#</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_electric12m"
						<%= props.getProperty("p2_electric12m", "") %> /></td>
					<td><i>Electrical plugs</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_carbon12m"
						<%= props.getProperty("p2_carbon12m", "") %> /></td>
					<td>Carbon monoxide/<br>
					&nbsp;&nbsp;<i><a href="<%=resource%>s_burns">Smoke
					detectors</a>*</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_hotWater12m"
						<%= props.getProperty("p2_hotWater12m", "") %> /></td>
					<td><i>Hot Water &lt; 54&deg;C</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_sleeping12m"
						<%= props.getProperty("p2_sleeping12m", "") %> /></td>
					<td><b><a href="<%=resource%>b_nightWaking">Night
					waking/crying</a>*</b></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_parent12m"
						<%= props.getProperty("p2_parent12m", "") %> /></td>
					<td>Parent/child interaction</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_teething12m"
						<%= props.getProperty("p2_teething12m", "") %> /></td>
					<td>Teething/<b><a href="<%=resource%>o_dentalCare">Dental
					care</a>*</b></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a>DEVELOPMENT</a><br>
			(Inquiry &amp; observation of milestones)<br>
			Tasks are set after the time of normal milestone acquisition.<br>
			Absence of any item suggests the need for further assessment of
			development<br>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_development4m"
						value="<%= props.getProperty("p2_development4m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_turnHead4m"
						<%= props.getProperty("p2_turnHead4m", "") %>></td>
					<td>Turns head toward sounds</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_laugh4m"
						<%= props.getProperty("p2_laugh4m", "") %>></td>
					<td>Laughs/squeals at parent</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_headSteady4m"
						<%= props.getProperty("p2_headSteady4m", "") %>></td>
					<td>Head steady</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_grasp4m"
						<%= props.getProperty("p2_grasp4m", "") %>></td>
					<td>Grasps/reaches</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_concern4m"
						<%= props.getProperty("p2_concern4m", "") %>></td>
					<td>No parent concern</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_development6m"
						value="<%= props.getProperty("p2_development6m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_follow6m"
						<%= props.getProperty("p2_follow6m", "") %>></td>
					<td>Follows a moving object</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_respond6m"
						<%= props.getProperty("p2_respond6m", "") %>></td>
					<td>Responds to own name</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_babbles6m"
						<%= props.getProperty("p2_babbles6m", "") %>></td>
					<td>Babbles</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_rolls6m"
						<%= props.getProperty("p2_rolls6m", "") %>></td>
					<td>Rolls from back to stomach or stomach to back</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_sits6m"
						<%= props.getProperty("p2_sits6m", "") %>></td>
					<td>Sits with support</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_mouth6m"
						<%= props.getProperty("p2_mouth6m", "") %>></td>
					<td>Brings hands/toys to mouth</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_concern6m"
						<%= props.getProperty("p2_concern6m", "") %>></td>
					<td>No parent concern</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_development9m"
						value="<%= props.getProperty("p2_development9m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_looks9m"
						<%= props.getProperty("p2_looks9m", "") %>></td>
					<td>Looks for hidden toy</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_babbles9m"
						<%= props.getProperty("p2_babbles9m", "") %>></td>
					<td>Babbles different sounds & to get attention</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_sits9m"
						<%= props.getProperty("p2_sits9m", "") %>></td>
					<td>Sits without support</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_stands9m"
						<%= props.getProperty("p2_stands9m", "") %>></td>
					<td>Stands with support</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_opposes9m"
						<%= props.getProperty("p2_opposes9m", "") %>></td>
					<td>Opposes thumb & index finger</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_reaches9m"
						<%= props.getProperty("p2_reaches9m", "") %>></td>
					<td>Reaches to be picked up & held</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p2_noParentsConcerns9m"
						<%= props.getProperty("p2_noParentsConcerns9m", "") %>></td>
					<td>No parent concerns</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_development12m"
						value="<%= props.getProperty("p2_development12m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p2_understands12m"
						<%= props.getProperty("p2_understands12m", "") %>></td>
					<td>Understands simple requests, e.g. find your shoes</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_chatters12m"
						<%= props.getProperty("p2_chatters12m", "") %>></td>
					<td>Chatters using 3 different sounds</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_crawls12m"
						<%= props.getProperty("p2_crawls12m", "") %>></td>
					<td>Crawls or 'bum' shuffles</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_pulls12m"
						<%= props.getProperty("p2_pulls12m", "") %>></td>
					<td>Pulls to stand/walks holding on</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_emotions12m"
						<%= props.getProperty("p2_emotions12m", "") %>></td>
					<td>Shows many emotions</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p2_noParentConcerns12m"
						<%= props.getProperty("p2_noParentConcerns12m", "") %>></td>
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
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_physical4m"
						value="<%= props.getProperty("p2_physical4m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_eyes4m"
						<%= props.getProperty("p2_eyes4m", "") %>></td>
					<td><i>Eyes (red reflex)</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_cover4m"
						<%= props.getProperty("p2_cover4m", "") %>></td>
					<td><b><a href="<%=resource%>pe_cover">Cover/uncover
					test & inquiry</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_hearing4m"
						<%= props.getProperty("p2_hearing4m", "") %>></td>
					<td><b>Hearing inquiry</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_babbling4m"
						<%= props.getProperty("p2_babbling4m", "") %>></td>
					<td>Babbling</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_hips4m"
						<%= props.getProperty("p2_hips4m", "") %>></td>
					<td><b>Hips</b></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_physical6m"
						value="<%= props.getProperty("p2_physical6m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox"
						name="p2_fontanelles6m"
						<%= props.getProperty("p2_fontanelles6m", "") %>></td>
					<td>Fontanelles</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_eyes6m"
						<%= props.getProperty("p2_eyes6m", "") %>></td>
					<td><i>Eyes (red reflex)</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_cover6m"
						<%= props.getProperty("p2_cover6m", "") %>></td>
					<td><b><a href="<%=resource%>pe_cover">Cover/uncover
					test & inquiry</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_hearing6m"
						<%= props.getProperty("p2_hearing6m", "") %>></td>
					<td><b>Hearing inquiry</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_hips6m"
						<%= props.getProperty("p2_hips6m", "") %>></td>
					<td><b>Hips</b></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_physical9m"
						value="<%= props.getProperty("p2_physical9m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_eyes9m"
						<%= props.getProperty("p2_eyes9m", "") %>></td>
					<td><i>Eyes (red reflex)</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_cover9m"
						<%= props.getProperty("p2_cover9m", "") %>></td>
					<td><b><a href="<%=resource%>pe_cover">Cover/uncover
					test &amp; inquiry</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_hearing9m"
						<%= props.getProperty("p2_hearing9m", "") %>></td>
					<td><b> Hearing inquiry</b></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_physical12m"
						value="<%= props.getProperty("p2_physical12m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_eyes12m"
						<%= props.getProperty("p2_eyes12m", "") %>></td>
					<td width="100%"><i>Eyes (red reflex)</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_cover12m"
						<%= props.getProperty("p2_cover12m", "") %>></td>
					<td><b><a href="<%=resource%>pe_cover">Cover/uncover
					test &amp; inquiry</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_hearing12m"
						<%= props.getProperty("p2_hearing12m", "") %>></td>
					<td><b> Hearing inquiry</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_hips12m"
						<%= props.getProperty("p2_hips12m", "") %>></td>
					<td><b>Hips</b></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a>PROBLEMS &amp; PLANS</a></td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_problems6m"
						value="<%= props.getProperty("p2_problems6m", "") %>" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_problems4m"
						value="<%= props.getProperty("p2_problems4m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_tb6m"
						<%= props.getProperty("p2_tb6m", "") %>></td>
					<td width="100%">Inquire about possible TB exposure</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_problems9m"
						value="<%= props.getProperty("p2_problems9m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_antiHbs9m"
						<%= props.getProperty("p2_antiHbs9m", "") %>></td>
					<td width="100%"><b><a href="<%=resource%>i_hepB">Anti-HBs
					& HbsAG</a>*</b><br>
					&nbsp;&nbsp;(If HbsAg pos mother)</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_hgb9m"
						<%= props.getProperty("p2_hgb9m", "") %>></td>
					<td>Hgb. (If at risk)*</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_problems12m"
						value="<%= props.getProperty("p2_problems12m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_hgb12m"
						<%= props.getProperty("p2_hgb12m", "") %>></td>
					<td width="100%">Hgb. (If at risk)*</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_serum12m"
						<%= props.getProperty("p2_serum12m", "") %>></td>
					<td><i>Serum lead (If at risk)*</i></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a>IMMUNIZATION</a><br>
			Guidelines may vary by province</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_immunization4m"
						value="<%= props.getProperty("p2_immunization4m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_hib4m"
						<%= props.getProperty("p2_hib4m", "") %>></td>
					<td width="100%"><b>HIB</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_polio4m"
						<%= props.getProperty("p2_polio4m", "") %>></td>
					<td><b>aPDT polio</b></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_immunization6m"
						value="<%= props.getProperty("p2_immunization6m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_hib6m"
						<%= props.getProperty("p2_hib6m", "") %>></td>
					<td width="100%"><b>HIB</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_polio6m"
						<%= props.getProperty("p2_polio6m", "") %>></td>
					<td><b>aPDT polio</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_hepB6m"
						<%= props.getProperty("p2_hepB6m", "") %>></td>
					<td><b><a href="<%=resource%>i_hepB">Hep.B vaccine</a>*</b></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_immunization9m"
						value="<%= props.getProperty("p2_immunization9m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_tbSkin9m"
						<%= props.getProperty("p2_tbSkin9m", "") %>></td>
					<td width="100%"><a href="<%=resource%>i_tbSkinTesting">TB
					skin text?</a>*</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table width="100%">
				<tr align="center">
					<td colspan="2"><input type="text" class="wide"
						name="p2_immunization12m"
						value="<%= props.getProperty("p2_immunization12m", "") %>" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_mmr12m"
						<%= props.getProperty("p2_mmr12m", "") %>></td>
					<td width="100%"><b>MMR</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" name="p2_varicella12m"
						<%= props.getProperty("p2_varicella12m", "") %>></td>
					<td><b><a href="<%=resource%>i_varicellaVaccine">Varicella
					vaccine</a>*</b></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a>Signature</a></td>
			<td colspan="3"><input type="text" class="wide"
				style="width: 100%" name="p2_signature4m"
				value="<%= props.getProperty("p2_signature4m", "") %>" /></td>
			<td colspan="3"><input type="text" class="wide"
				style="width: 100%" name="p2_signature6m"
				value="<%= props.getProperty("p2_signature6m", "") %>" /></td>
			<td colspan="3"><input type="text" class="wide"
				style="width: 100%" name="p2_signature9m"
				value="<%= props.getProperty("p2_signature9m", "") %>" /></td>
			<td colspan="3"><input type="text" class="wide"
				style="width: 100%" name="p2_signature12m"
				value="<%= props.getProperty("p2_signature12m", "") %>" /></td>
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
			<td align="center" width="100%">
			<% if(formId > 0)
        { %> <a name="length"
				href="javascript:popup('graphLengthWeight.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">
			Graph Length and Weight</a><br>
			<a name="headCirc"
				href="javascript:popup('graphHeadCirc.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">
			Graph Head Circumference</a> <% }else
        {
            %>&nbsp;<%
        }
        %>
			</td>
			<td nowrap="true"><a
				href="formrourke1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Page
			1</a>&nbsp;|&nbsp; <a>Page 2</a>&nbsp;|&nbsp; <a
				href="formrourke3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Page
			3</a></td>
		</tr>
	</table>

</html:form>
</body>
</html:html>
