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
<title><bean:message key="oscarEncounter.formRourke2.title" /></title>
<html:base />
<link rel="stylesheet" type="text/css" media="screen"
	href="rourkeStyle.css">
<link rel="stylesheet" type="text/css" media="print"
	href="printRourke.css">
</head>

<%
    String formClass = "Rourke";
    String formLink = "formrourkep2.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "Rourke/";
    props.setProperty("c_lastVisited", "p2");
%>

<script type="text/javascript" language="Javascript">
    function onPrint() {
//        document.forms[0].submit.value="print";
//        var ret = checkAllDates();
//        if(ret==true)
//        {
//            ret = confirm("<bean:message key="oscarEncounter.formRourke2.msgSavePrintPreview"/>");
//        }
//        return ret;
        window.print();
    }
    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true)
        {
            ret = confirm("<bean:message key="oscarEncounter.formRourke2.msgSave"/>");
        }
        return ret;
    }
    
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret == true)
        {
            ret = confirm("<bean:message key="oscarEncounter.formRourke2.msgSaveExit"/>");
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
          alert ("<bean:message key="oscarEncounter.formRourke2.msgTypeNumbers"/>");
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
		value=<%=props.getProperty("c_lastVisited", "p2")%> />
	<input type="hidden" name="submit" value="exit" />

	<table cellspacing="0" cellpadding="0" class="Header" class="hidePrint">
		<tr>
			<td nowrap="true"><input type="submit"
				value="<bean:message key="oscarEncounter.formRourke2.btnSave"/>"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke2.btnSaveExit"/>"
				onclick="javascript:return onSaveExit();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke2.btnExit"/> "
				onclick="javascript:return onExit();" /> <input type="button"
				value="<bean:message key="oscarEncounter.formRourke2.btnPrint"/>"
				onclick="javascript:return onPrint();" /></td>
			<td align="center" width="100%"><a name="length"
				href="javascript:popup('graphLengthWeight.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">
			<bean:message key="oscarEncounter.formRourke2.btnGraphLenght" /></a><br>
			<a name="headCirc"
				href="javascript:popup('graphHeadCirc.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">
			<bean:message key="oscarEncounter.formRourke2.btnGraphHead" /></a></td>
			<td nowrap="true"><a
				href="formrourkep1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2.btnpage1" /></a>&nbsp;|&nbsp; <a><bean:message
				key="oscarEncounter.formRourke2.msgPage2" /></a>&nbsp;|&nbsp; <a
				href="formrourkep3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2.btnPage3" /></a></td>
		</tr>
	</table>

	<table cellspacing="0" cellpadding="0" border="0" width="100%">
		<tr class="titleBar">
			<th><bean:message key="oscarEncounter.formRourke2.msgTitle" /></th>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" width="100%" border="0">
		<tr valign="top">
			<td nowrap align="center"><bean:message
				key="oscarEncounter.formRourke2.formBirthRemarks" /><br>
			<textarea name="c_birthRemarks" cols="17" rows="2"><%= props.getProperty("c_birthRemarks", "") %></textarea>
			</td>
			<td nowrap align="center"><bean:message
				key="oscarEncounter.formRourke2.formRiskFactors" /><br>
			<textarea name="c_riskFactors" rows="2" cols="17"><%= props.getProperty("c_riskFactors", "") %></textarea>
			</td>
			<td width="65%" nowrap align="center">
			<p><bean:message key="oscarEncounter.formRourke2.msgName" />: <input
				type="text" name="c_pName" maxlength="60" size="30"
				value="<%= props.getProperty("c_pName", "") %>" readonly="true" />
			&nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke2.msgBirthDate" /> (yyyy/mm/dd): <input
				type="text" name="c_birthDate" size="10" maxlength="10"
				value="<%= props.getProperty("c_birthDate", "") %>" readonly="true">
			&nbsp;&nbsp; <% if(!  ((FrmRourkeRecord)rec).isFemale(demoNo))
                {
                    %><bean:message
				key="oscarEncounter.formRourke3.msgMale" />
			<%
                }else
                {
                    %><bean:message
				key="oscarEncounter.formRourke3.msgFemale" />
			<%
                }
                %>
			</p>
			<p><bean:message key="oscarEncounter.formRourke2.formLenght" />:
			<input type="text" name="c_length" size="6" maxlength="6"
				value="<%= props.getProperty("c_length", "") %>" /> cm &nbsp;&nbsp;
			<bean:message key="oscarEncounter.formRourke2.formHeadCirc" />: <input
				type="text" name="c_headCirc" size="6" maxlength="6"
				value="<%= props.getProperty("c_headCirc", "") %>" /> cm
			&nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke2.formBirthWt" /> <input type="text"
				name="c_birthWeight" size="6" maxlength="7"
				value="<%= props.getProperty("c_birthWeight", "") %>" /> <bean:message
				key="oscarEncounter.formRourke2.msgBirthWtUnit" /> &nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke2.formDischargeWt" />: <input
				type="text" name="c_dischargeWeight" size="6" maxlength="7"
				value="<%= props.getProperty("c_dischargeWeight", "") %>"> <bean:message
				key="oscarEncounter.formRourke2.msgDischargeWtUnit" /></p>
			</td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" width="100%" border="1">
		<tr align="center">
			<td class="column"><a>AGE</a><br>
			</td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke2.form4Months" /></a></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke2.form6Months" /></a></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke2.form9Months" /></a> <bean:message
				key="oscarEncounter.formRourke2.msgOptional" /></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke2.form12Months" /></a></td>
		</tr>
		<tr align="center">
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke2.msgDate" /></a></td>
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
			<td><bean:message key="oscarEncounter.formRourke2.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke2.formWt" /></td>
			<td><bean:message key="oscarEncounter.formRourke2.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke2.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke2.formWt2" /></td>
			<td><bean:message key="oscarEncounter.formRourke2.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke2.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke2.formWt" /></td>
			<td><bean:message key="oscarEncounter.formRourke2.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke2.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke2.formWt3" /></td>
			<td><bean:message key="oscarEncounter.formRourke2.HdCirc47" /></td>
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
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke2.msgParentalConcerns" /></a></td>
			<td colspan="3"><textarea name="p2_pConcern4m" cols="25"
				rows="2" class="wide" style="width: 100%"><%= props.getProperty("p2_pConcern4m", "") %></textarea>
			</td>
			<td colspan="3"><textarea name="p2_pConcern6m" cols="25"
				rows="2" class="wide" style="width: 100%"><%= props.getProperty("p2_pConcern6m", "") %></textarea>
			</td>
			<td colspan="3"><textarea name="p2_pConcern9m" cols="25"
				rows="2" class="wide" style="width: 100%"><%= props.getProperty("p2_pConcern9m", "") %></textarea>
			</td>
			<td colspan="3"><textarea name="p2_pConcern12m" cols="25"
				rows="2" class="wide" style="width: 100%"><%= props.getProperty("p2_pConcern12m", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke2.msgnutrition" /></a></td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_nutrition4m" cols="25"
						class="wide"><%= props.getProperty("p2_nutrition4m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_breastFeeding4m"
						<%= props.getProperty("p2_breastFeeding4m", "") %> /></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>n_breastFeeding');return false;"><bean:message
						key="oscarEncounter.formRourke2.btnBreastFeeding" /></a><bean:message
						key="oscarEncounter.formRourke2.msgBreastFeedingUnit" /></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_formulaFeeding4m"
						<%= props.getProperty("p2_formulaFeeding4m", "") %> /></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formFormulaFeeding" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_cereal4m" <%= props.getProperty("p2_cereal4m", "") %> /></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formIronFortified" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_nutrition6m" cols="25"
						class="wide"><%= props.getProperty("p2_nutrition6m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_breastFeeding6m"
						<%= props.getProperty("p2_breastFeeding6m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>n_breastFeeding');return false;"><bean:message
						key="oscarEncounter.formRourke2.btnBreastFeeding" /></a><bean:message
						key="oscarEncounter.formRourke2.msgBreastFeedingUnit" /></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_formulaFeeding6m"
						<%= props.getProperty("p2_formulaFeeding6m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formFormulaFeedingIronFortified" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_bottle6m" <%= props.getProperty("p2_bottle6m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formNoBottles" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_vegFruit6m" <%= props.getProperty("p2_vegFruit6m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formVeg" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_egg6m" <%= props.getProperty("p2_egg6m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formNoEgg" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_choking6m" <%= props.getProperty("p2_choking6m", "") %>></td>
					<td><a href="#"
						onclick="popup('<%=resource%>s_choking');return false;"><bean:message
						key="oscarEncounter.formRourke2.formChokingSafeFood" /></a>*</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_nutrition9m" cols="25"
						class="wide"><%= props.getProperty("p2_nutrition9m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_breastFeeding9m"
						<%= props.getProperty("p2_breastFeeding9m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>n_breastFeeding');return false;"><bean:message
						key="oscarEncounter.formRourke2.btnBreastFeeding" /></a><bean:message
						key="oscarEncounter.formRourke2.msgBreastFeedingUnit" /></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_formulaFeeding9m"
						<%= props.getProperty("p2_formulaFeeding9m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formFormulaFeeding" /><br>
					<bean:message key="oscarEncounter.formRourke2.formIronFortified" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_bottle9m" <%= props.getProperty("p2_bottle9m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formNoBottles" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_meat9m" <%= props.getProperty("p2_meat9m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formMeat" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_milk9m" <%= props.getProperty("p2_milk9m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formMilk" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_egg9m" <%= props.getProperty("p2_egg9m", "") %>></td>
					<td>No egg white, nuts or honey</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_choking9m" <%= props.getProperty("p2_choking9m", "") %>></td>
					<td><a href="#"
						onclick="popup('<%=resource%>s_choking');return false;"><bean:message
						key="oscarEncounter.formRourke2.formChokingSafeFood" /></a>*</td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_nutrition12m" cols="25"
						class="wide"><%= props.getProperty("p2_nutrition12m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_milk12m" <%= props.getProperty("p2_milk12m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formHomogenizedMilk" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_bottle12m" <%= props.getProperty("p2_bottle12m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formEncourageCup" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_appetite12m"
						<%= props.getProperty("p2_appetite12m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formAppetiteReduced" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr>
					<td><a><bean:message
						key="oscarEncounter.formRourke2.msgEducationAdvice" /></a></td>
				</tr>
				<tr>
					<td align="right"><b><bean:message
						key="oscarEncounter.formRourke2.msgSafety" /></b></td>
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
					<td align="right"><b><bean:message
						key="oscarEncounter.formRourke2.msgBehaviour" /></b></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td align="right"><b><bean:message
						key="oscarEncounter.formRourke2.msgFamily" /></b></td>
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
					<td align="right"><b><bean:message
						key="oscarEncounter.formRourke2.msgOther" /></b></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_educationAdvice4m"
						class="wide"><%= props.getProperty("p2_educationAdvice4m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_carSeat4m" <%= props.getProperty("p2_carSeat4m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>s_motorVehicleAccidents');return false;"><bean:message
						key="oscarEncounter.formRourke2.formCarSeat" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_stairs4m" <%= props.getProperty("p2_stairs4m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formWalker" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_bath4m" <%= props.getProperty("p2_bath4m", "") %>></td>
					<td><i><a href="#"
						onclick="popup('<%=resource%>s_drowning');return false;"><bean:message
						key="oscarEncounter.formRourke2.formBathSafety" /></a>*</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_sleeping4m" <%= props.getProperty("p2_sleeping4m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>b_nightWaking');return false;">Night
					waking/crying</a>*</b></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_parent4m" <%= props.getProperty("p2_parent4m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formParentChildInteraction" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_childCare4m"
						<%= props.getProperty("p2_childCare4m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formChildCare" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_family4m" <%= props.getProperty("p2_family4m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formFamilyConflict" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_teething4m" <%= props.getProperty("p2_teething4m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formSiblings" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_educationAdvice6m"
						class="wide"><%= props.getProperty("p2_educationAdvice6m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_poison6m" <%= props.getProperty("p2_poison6m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>s_poisons');return false;"><bean:message
						key="oscarEncounter.formRourke2.btnPoisons" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_electric6m" <%= props.getProperty("p2_electric6m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formElectricPlugs" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_sleeping6m" <%= props.getProperty("p2_sleeping6m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>b_nightWaking');return false;"><bean:message
						key="oscarEncounter.formRourke2.formNightWaking" /></a>*</b></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_parent6m" <%= props.getProperty("p2_parent6m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formParentChildInteraction" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_childCare6m"
						<%= props.getProperty("p2_childCare6m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formChildCare" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_educationAdvice9m"
						class="wide"><%= props.getProperty("p2_educationAdvice9m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_childProof9m"
						<%= props.getProperty("p2_childProof9m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formChildProofing" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_separation9m"
						<%= props.getProperty("p2_separation9m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formSeparation" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_sleeping9m" <%= props.getProperty("p2_sleeping9m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>b_nightWaking');return false;"><bean:message
						key="oscarEncounter.formRourke2.formNightWaking" /></a>*</b></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_dayCare9m" <%= props.getProperty("p2_dayCare9m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>hri_dayCare');return false;"><bean:message
						key="oscarEncounter.formRourke2.formAssessDay" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_homeVisit9m"
						<%= props.getProperty("p2_homeVisit9m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>hri_homeVisits');return false;"><bean:message
						key="oscarEncounter.formRourke2.formAssessHomeVisit" /></a>*</b></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_smoke9m" <%= props.getProperty("p2_smoke9m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>o_secondHandSmoke');return false;"><bean:message
						key="oscarEncounter.formRourke2.formSecondHandSmoke" /></a>*</b></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_educationAdvice12m"
						class="wide"><%= props.getProperty("p2_educationAdvice12m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_poison12m" <%= props.getProperty("p2_poison12m", "") %> /></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>s_poisons');return false;"><bean:message
						key="oscarEncounter.formRourke2.btnPoisons" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_electric12m"
						<%= props.getProperty("p2_electric12m", "") %> /></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formElectricPlugs" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_carbon12m" <%= props.getProperty("p2_carbon12m", "") %> /></td>
					<td>Carbon monoxide/<br>
					&nbsp;&nbsp;<i><a href="#"
						onclick="popup('<%=resource%>s_burns');return false;"><bean:message
						key="oscarEncounter.formRourke2.formSmokeDetectors" /></a>*</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_hotWater12m"
						<%= props.getProperty("p2_hotWater12m", "") %> /></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formHotWater" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_sleeping12m"
						<%= props.getProperty("p2_sleeping12m", "") %> /></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>b_nightWaking');return false;"><bean:message
						key="oscarEncounter.formRourke2.formNightWaking" /></a>*</b></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_parent12m" <%= props.getProperty("p2_parent12m", "") %> /></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formParentChildInteraction" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_teething12m"
						<%= props.getProperty("p2_teething12m", "") %> /></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formTeething" /><b><a href="#"
						onclick="popup('<%=resource%>o_dentalCare');return false;"><bean:message
						key="oscarEncounter.formRourke2.btnDentalCare" /></a>*</b></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke2.msgDevelopment" /></a><br>
			<bean:message key="oscarEncounter.formRourke2.msgDecelopmentDesc" />
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_development4m" class="wide"><%= props.getProperty("p2_development4m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_turnHead4m" <%= props.getProperty("p2_turnHead4m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formTurnsHead" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_laugh4m" <%= props.getProperty("p2_laugh4m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formLaughs" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_headSteady4m"
						<%= props.getProperty("p2_headSteady4m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formHeadSteady" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_grasp4m" <%= props.getProperty("p2_grasp4m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formGrasps" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_concern4m" <%= props.getProperty("p2_concern4m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formNoParentConcern" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_development6m" class="wide"><%= props.getProperty("p2_development6m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_follow6m" <%= props.getProperty("p2_follow6m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formFollowsMovingObjects" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_respond6m" <%= props.getProperty("p2_respond6m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formRespondsName" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_babbles6m" <%= props.getProperty("p2_babbles6m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formBabbles" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_rolls6m" <%= props.getProperty("p2_rolls6m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formRollsFromBack" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_sits6m" <%= props.getProperty("p2_sits6m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formSitsWithSupport" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_mouth6m" <%= props.getProperty("p2_mouth6m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formBringHandsToMouth" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_concern6m" <%= props.getProperty("p2_concern6m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formNoParentConcern" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_development9m" class="wide"><%= props.getProperty("p2_development9m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_looks9m" <%= props.getProperty("p2_looks9m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formLooksForHiddenToy" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_babbles9m" <%= props.getProperty("p2_babbles9m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formDifferentSounds" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_sits9m" <%= props.getProperty("p2_sits9m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formSitsWithoutSupport" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_stands9m" <%= props.getProperty("p2_stands9m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formStandsWithSupport" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_opposes9m" <%= props.getProperty("p2_opposes9m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formOpposesThumbAndIndex" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_reaches9m" <%= props.getProperty("p2_reaches9m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formReachestobePicked" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_noParentsConcerns9m"
						<%= props.getProperty("p2_noParentsConcerns9m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formNoParentConcern" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_development12m"
						class="wide"><%= props.getProperty("p2_development12m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_understands12m"
						<%= props.getProperty("p2_understands12m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formUnderstandsSimpleRequests" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_chatters12m"
						<%= props.getProperty("p2_chatters12m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formChatters" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_crawls12m" <%= props.getProperty("p2_crawls12m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formCrawls" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_pulls12m" <%= props.getProperty("p2_pulls12m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formPullsToStand" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_emotions12m"
						<%= props.getProperty("p2_emotions12m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formShowsManyEmotions" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_noParentConcerns12m"
						<%= props.getProperty("p2_noParentConcerns12m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formNoParentConcern" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke2.msgPhysicalExamination" /></a><br>
			<bean:message
				key="oscarEncounter.formRourke2.msgPhysicalExaminationDesc" /></td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_physical4m" class="wide"><%= props.getProperty("p2_physical4m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_eyes4m" <%= props.getProperty("p2_eyes4m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formRedReflex" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_cover4m" <%= props.getProperty("p2_cover4m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>pe_cover');return false;"><bean:message
						key="oscarEncounter.formRourke2.formCover" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_hearing4m" <%= props.getProperty("p2_hearing4m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formHearing" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_babbling4m" <%= props.getProperty("p2_babbling4m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formBabbling" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_hips4m" <%= props.getProperty("p2_hips4m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formHips" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_physical6m" class="wide"><%= props.getProperty("p2_physical6m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_fontanelles6m"
						<%= props.getProperty("p2_fontanelles6m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_eyes6m" <%= props.getProperty("p2_eyes6m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formRedReflex" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_cover6m" <%= props.getProperty("p2_cover6m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>pe_cover');return false;"><bean:message
						key="oscarEncounter.formRourke2.formCover" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_hearing6m" <%= props.getProperty("p2_hearing6m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formHearing" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_hips6m" <%= props.getProperty("p2_hips6m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formHips" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_physical9m" class="wide"><%= props.getProperty("p2_physical9m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_eyes9m" <%= props.getProperty("p2_eyes9m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formRedReflex" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_cover9m" <%= props.getProperty("p2_cover9m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>pe_cover');return false;"><bean:message
						key="oscarEncounter.formRourke2.formCover" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_hearing9m" <%= props.getProperty("p2_hearing9m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formHearing" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_physical12m" class="wide"><%= props.getProperty("p2_physical12m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_eyes12m" <%= props.getProperty("p2_eyes12m", "") %>></td>
					<td width="100%"><bean:message
						key="oscarEncounter.formRourke2.formRedReflex" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_cover12m" <%= props.getProperty("p2_cover12m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>pe_cover');return false;"><bean:message
						key="oscarEncounter.formRourke2.formCover" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_hearing12m" <%= props.getProperty("p2_hearing12m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formHearing" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_hips12m" <%= props.getProperty("p2_hips12m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formHips" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke2.msgProblems" /></a></td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_problems6m" class="wide"><%= props.getProperty("p2_problems6m", "") %></textarea></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_problems4m" class="wide"><%= props.getProperty("p2_problems4m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_tb6m" <%= props.getProperty("p2_tb6m", "") %>></td>
					<td width="100%"><bean:message
						key="oscarEncounter.formRourke2.formTBexposure" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_problems9m" class="wide"><%= props.getProperty("p2_problems9m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_antiHbs9m" <%= props.getProperty("p2_antiHbs9m", "") %>></td>
					<td width="100%"><b><a href="#"
						onclick="popup('<%=resource%>i_hepB');return false;"><bean:message
						key="oscarEncounter.formRourke2.btnAntiHB" /></a><bean:message
						key="oscarEncounter.formRourke2.formAntiHB" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_hgb9m" <%= props.getProperty("p2_hgb9m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke2.formHgb" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellspacing="0" cellpadding="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p2_problems12m" class="wide"><%= props.getProperty("p2_problems12m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_hgb12m" <%= props.getProperty("p2_hgb12m", "") %>></td>
					<td width="100%"><bean:message
						key="oscarEncounter.formRourke2.formHgb" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p2_serum12m" <%= props.getProperty("p2_serum12m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke2.formSerumLead" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke2.msgImmunization" /></a><br>
			<bean:message key="oscarEncounter.formRourke2.msgImmunizarionDesc" />
			</td>
			<td colspan="3" valign="top"><textarea name="p2_immunization4m"
				class="wide"><%= props.getProperty("p2_immunization4m", "") %></textarea></td>
			<td colspan="3" valign="top"><textarea name="p2_immunization6m"
				class="wide"><%= props.getProperty("p2_immunization6m", "") %></textarea></td>
			<td colspan="3" valign="top"><textarea name="p2_immunization9m"
				class="wide"><%= props.getProperty("p2_immunization9m", "") %></textarea></td>
			<td colspan="3" valign="top"><textarea name="p2_immunization12m"
				class="wide"><%= props.getProperty("p2_immunization12m", "") %></textarea></td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke2.formSignature" /></a></td>
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

	<table cellspacing="0" cellpadding="0" class="Header" class="hidePrint">
		<tr>
			<td nowrap="true"><input type="submit"
				value="<bean:message key="oscarEncounter.formRourke2.btnSave"/>"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke2.btnSaveExit"/>"
				onclick="javascript:return onSaveExit();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke2.btnExit"/>"
				onclick="javascript:return onExit();"> <input type="button"
				value="<bean:message key="oscarEncounter.formRourke2.btnPrint"/>"
				onclick="javascript:return onPrint();" /></td>
			<td align="center" width="100%">
			<% if(formId > 0)
        { %> <a name="length"
				href="javascript:popup('graphLengthWeight.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">
			<bean:message key="oscarEncounter.formRourke2.btnGraphLenght" /></a><br>
			<a name="headCirc"
				href="javascript:popup('graphHeadCirc.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">
			<bean:message key="oscarEncounter.formRourke2.btnGraphHead" /></a> <% }else
        {
            %>&nbsp;<%
        }
        %>
			</td>
			<td nowrap="true"><a
				href="formrourkep1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2.btnpage1" /></a>&nbsp;|&nbsp; <a><bean:message
				key="oscarEncounter.formRourke2.msgPage2" /></a>&nbsp;|&nbsp; <a
				href="formrourkep3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke2.btnPage3" /></a></td>
		</tr>
	</table>

</html:form>
</body>
</html:html>
