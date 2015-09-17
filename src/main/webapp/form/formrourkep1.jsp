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
<title><bean:message key="oscarEncounter.formRourke1.title" /></title>
<html:base />
<link rel="stylesheet" type="text/css" media="screen"
	href="rourkeStyle.css">
<link rel="stylesheet" type="text/css" media="print"
	href="printRourke.css">

<%
    String formClass = "Rourke";
    String formLink = "formrourkep1.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    FrmData fd = new FrmData();
    String resource = fd.getResource();
    resource = resource + "Rourke/";
    props.setProperty("c_lastVisited", "p1");
%>
</head>

<script type="text/javascript" language="Javascript">
    function onPrint() {
//        document.forms[0].submit.value="print";
//        var ret = checkAllDates();
//        if(ret==true)
//        {
//            ret = confirm("<bean:message key="oscarEncounter.formRourke1.msgSavePrintPreview"/>");
//        }
//        return ret;
        window.print();
    }
    function onSave() {
        document.forms[0].submit.value="save";
        var ret = checkAllDates();
        if(ret==true)
        {
            ret = confirm("<bean:message key="oscarEncounter.formRourke1.msgSave"/>");
        }
        return ret;
    }
    
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret == true)
        {
            ret = confirm("<bean:message key="oscarEncounter.formRourke1.msgSaveExit"/>");
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
          alert ("<bean:message key="oscarEncounter.formRourke1.msgTypeNumbers"/>");
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
        if(valDate(document.forms[0].p1_date1w)==false){
            b = false;
        }else
        if(valDate(document.forms[0].p1_date2w)==false){
            b = false;
        }else
        if(valDate(document.forms[0].p1_date1m)==false){
            b = false;
        }else
        if(valDate(document.forms[0].p1_date2m)==false){
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
		value=<%=props.getProperty("c_lastVisited", "p1")%> />
	<input type="hidden" name="submit" value="exit" />

	<table cellpadding="0" cellspacing="0" class="Header" class="hidePrint">
		<tr>
			<td nowrap="true"><input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnSave"/>"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnSaveExit"/>"
				onclick="javascript:return onSaveExit();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnExit"/>"
				onclick="javascript:return onExit();"> <input type="button"
				value="<bean:message key="oscarEncounter.formRourke1.btnPrint"/>"
				onclick="javascript:return onPrint();" /></td>
			<td align="center" width="100%"><a name="length"
				href="javascript:popup('graphLengthWeight.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">
			<bean:message key="oscarEncounter.formRourke1.btnGraphLenghtWeight" /></a><br>
			<a name="headCirc"
				href="javascript:popup('graphHeadCirc.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">
			<bean:message key="oscarEncounter.formRourke1.btnGraphHead" /></a></td>
			<td nowrap="true"><a><bean:message
				key="oscarEncounter.formRourke1.msgPage1" /></a>&nbsp;|&nbsp; <a
				href="formrourkep2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke1.btnPage2" /></a>&nbsp;|&nbsp; <a
				href="formrourkep3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke1.btnPage3" /></a></td>
		</tr>
	</table>

	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr class="titleBar">
			<th><bean:message
				key="oscarEncounter.formRourke1.msgRourkeBabyRecord" /></th>
		</tr>
	</table>
	<table cellpadding="0" cellspacing="0" width="100%" border="0">
		<tr valign="top">
			<td nowrap align="center"><bean:message
				key="oscarEncounter.formRourke1.formBirhtRemarks" /><br>
			<textarea name="c_birthRemarks" rows="2" cols="17"><%= props.getProperty("c_birthRemarks", "") %></textarea>
			</td>
			<td nowrap align="center"><bean:message
				key="oscarEncounter.formRourke1.formRiksFactors" /><br>
			<textarea name="c_riskFactors" rows="2" cols="17"><%= props.getProperty("c_riskFactors", "") %></textarea>
			</td>
			<td width="65%" nowrap align="center">
			<p><bean:message key="oscarEncounter.formRourke1.msgName" />: <input
				type="text" name="c_pName" maxlength="60" size="30"
				value="<%= props.getProperty("c_pName", "") %>" readonly="true" />
			&nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke1.msgBirthDate" /> (yyyy/mm/dd): <input
				type="text" name="c_birthDate" size="10" maxlength="10"
				value="<%= props.getProperty("c_birthDate", "") %>" readonly="true">
			&nbsp;&nbsp; <% if(! ((FrmRourkeRecord)rec).isFemale(demoNo))
                {
                    %><bean:message
				key="oscarEncounter.formRourke1.msgMale" />
			<%
                }else
                {
                    %><bean:message
				key="oscarEncounter.formRourke1.msgFemale" />
			<%
                }
                %>
			</p>
			<p><bean:message key="oscarEncounter.formRourke1.msgLenght" />: <input
				type="text" name="c_length" size="6" maxlength="6"
				value="<%= props.getProperty("c_length", "") %>" /> <bean:message
				key="oscarEncounter.formRourke1.msgLenghtUnit" /> &nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke1.msgHeadCirc" />: <input type="text"
				name="c_headCirc" size="6" maxlength="6"
				value="<%= props.getProperty("c_headCirc", "") %>" /> <bean:message
				key="oscarEncounter.formRourke1.msgHeadCircUnit" /> &nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke1.msgBirthWt" />: <input type="text"
				name="c_birthWeight" size="6" maxlength="7"
				value="<%= props.getProperty("c_birthWeight", "") %>" /> <bean:message
				key="oscarEncounter.formRourke1.msgBirthWtUnit" /> &nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke1.msgDischargeWt" />: <input
				type="text" name="c_dischargeWeight" size="6" maxlength="7"
				value="<%= props.getProperty("c_dischargeWeight", "") %>"> <bean:message
				key="oscarEncounter.formRourke1.msgDischargeWtUnit" /></p>
			</td>
		</tr>
	</table>
	<table cellpadding="0" cellspacing="0" width="100%" border="1">
		<tr align="center">
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.btnAge" /></a></td>
			<td colspan="3" class="row"><bean:message
				key="oscarEncounter.formRourke1.msgWithin" /> <a><bean:message
				key="oscarEncounter.formRourke1.btn1Week" /></a></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke1.btn2Weeks" /></a> <bean:message
				key="oscarEncounter.formRourke1.msgOptional" /></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke1.btn1month" /></a> <bean:message
				key="oscarEncounter.formRourke1.msgOptional" /></td>
			<td colspan="3" class="row"><a><bean:message
				key="oscarEncounter.formRourke1.btn2Months" /></a></td>
		</tr>
		<tr align="center">
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgDate" /></a></td>
			<td colspan="3"><bean:message
				key="oscarEncounter.formRourke1.formDate" /> <input type="text"
				name="p1_date1w" size="10"
				value="<%=props.getProperty("p1_date1w", "")%>" /></td>
			<td colspan="3"><bean:message
				key="oscarEncounter.formRourke1.formDate" /> <input type="text"
				name="p1_date2w" size="10"
				value="<%=props.getProperty("p1_date2w", "")%>" /></td>
			<td colspan="3"><bean:message
				key="oscarEncounter.formRourke1.formDate" /> <input type="text"
				name="p1_date1m" size="10"
				value="<%=props.getProperty("p1_date1m", "")%>" /></td>
			<td colspan="3"><bean:message
				key="oscarEncounter.formRourke1.formDate" /> <input type="text"
				name="p1_date2m" size="10"
				value="<%=props.getProperty("p1_date2m", "")%>" /></td>
		</tr>
		<tr align="center">
			<td class="column" rowspan="2"><a><bean:message
				key="oscarEncounter.formRourke1.btnGrowth" /></a></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHdCirc" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formWt" /></td>
			<td><bean:message key="oscarEncounter.formRourke1.formHdCirc" /></td>
		</tr>
		<tr align="center">
			<td><input type="text" class="wide" name="p1_ht1w" size="4"
				maxlength="5" value="<%= props.getProperty("p1_ht1w", "") %>"></td>
			<td><input type="text" class="wide" name="p1_wt1w" size="4"
				maxlength="5" value="<%= props.getProperty("p1_wt1w", "") %>"></td>
			<td><input type="text" class="wide" name="p1_hc1w" size="4"
				maxlength="5" value="<%= props.getProperty("p1_hc1w", "") %>"></td>
			<td><input type="text" class="wide" name="p1_ht2w" size="4"
				maxlength="5" value="<%= props.getProperty("p1_ht2w", "") %>"></td>
			<td><input type="text" class="wide" name="p1_wt2w" size="4"
				maxlength="5" value="<%= props.getProperty("p1_wt2w", "") %>"></td>
			<td><input type="text" class="wide" name="p1_hc2w" size="4"
				maxlength="5" value="<%= props.getProperty("p1_hc2w", "") %>"></td>
			<td><input type="text" class="wide" name="p1_ht1m" size="4"
				maxlength="5" value="<%= props.getProperty("p1_ht1m", "") %>"></td>
			<td><input type="text" class="wide" name="p1_wt1m" size="4"
				maxlength="5" value="<%= props.getProperty("p1_wt1m", "") %>"></td>
			<td><input type="text" class="wide" name="p1_hc1m" size="4"
				maxlength="5" value="<%= props.getProperty("p1_hc1m", "") %>"></td>
			<td><input type="text" class="wide" name="p1_ht2m" size="4"
				maxlength="5" value="<%= props.getProperty("p1_ht2m", "") %>"></td>
			<td><input type="text" class="wide" name="p1_wt2m" size="4"
				maxlength="5" value="<%= props.getProperty("p1_wt2m", "") %>"></td>
			<td><input type="text" class="wide" name="p1_hc2m" size="4"
				maxlength="5" value="<%= props.getProperty("p1_hc2m", "") %>"></td>
		</tr>
		<tr align="center">
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.formParentalConcerns" /></a></td>
			<td colspan="3"><textarea name="p1_pConcern1w"
				style="width: 100%" cols="10" rows="2"><%= props.getProperty("p1_pConcern1w", "") %></textarea>
			</td>
			<td colspan="3"><textarea name="p1_pConcern2w"
				style="width: 100%" cols="10" rows="2"><%= props.getProperty("p1_pConcern2w", "") %></textarea>
			</td>
			<td colspan="3"><textarea name="p1_pConcern1m"
				style="width: 100%" cols="10" rows="2"><%= props.getProperty("p1_pConcern1m", "") %></textarea>
			</td>
			<td colspan="3"><textarea name="p1_pConcern2m"
				style="width: 100%" cols="10" rows="2"><%= props.getProperty("p1_pConcern2m", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgNutrition" />:</a></td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_nutrition1w" cols="25"
						class="wide"><%= props.getProperty("p1_nutrition1w", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_breastFeeding1w"
						<%= props.getProperty("p1_breastFeeding1w", "") %> /></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>n_breastFeeding');return false;"><bean:message
						key="oscarEncounter.formRourke1.btnBreastFeeding" /></a><bean:message
						key="oscarEncounter.formRourke1.msgBreastFeedingDescr" /></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_formulaFeeding1w"
						<%= props.getProperty("p1_formulaFeeding1w", "") %> /></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.msgFormulaFeeding" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_stoolUrine1w"
						<%= props.getProperty("p1_stoolUrine1w", "") %> /></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formStoolPatern" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_nutrition2w" cols="25"
						class="wide"><%= props.getProperty("p1_nutrition2w", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_breastFeeding2w"
						<%= props.getProperty("p1_breastFeeding2w", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>n_breastFeeding');return false;"><bean:message
						key="oscarEncounter.formRourke1.btnBreastFeeding" /></a><bean:message
						key="oscarEncounter.formRourke1.msgBreastFeedingDescr" /></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_formulaFeeding2w"
						<%= props.getProperty("p1_formulaFeeding2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.msgFormulaFeeding" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_stoolUrine2w"
						<%= props.getProperty("p1_stoolUrine2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formStoolPatern" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%" height="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_nutrition1m" cols="25"
						class="wide"><%= props.getProperty("p1_nutrition2m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_breastFeeding1m"
						<%= props.getProperty("p1_breastFeeding1m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>n_breastFeeding');return false;"><bean:message
						key="oscarEncounter.formRourke1.btnBreastFeeding" /></a><bean:message
						key="oscarEncounter.formRourke1.msgBreastFeedingDescr" /></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_formulaFeeding1m"
						<%= props.getProperty("p1_formulaFeeding1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.msgFormulaFeeding" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_stoolUrine1m"
						<%= props.getProperty("p1_stoolUrine1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formStoolPatern" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_nutrition2m" cols="25"
						class="wide"><%= props.getProperty("p1_nutrition2m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_breastFeeding2m"
						<%= props.getProperty("p1_breastFeeding2m", "") %>></td>
					<td nowrap="true"><b><a href="#"
						onclick="popup('<%=resource%>n_breastFeeding');return false;"><bean:message
						key="oscarEncounter.formRourke1.btnBreastFeeding" /></a><bean:message
						key="oscarEncounter.formRourke1.msgBreastFeedingDescr" /></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_formulaFeeding2m"
						<%= props.getProperty("p1_formulaFeeding2m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.msgFormulaFeeding" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column">
			<table cellpadding="0" cellspacing="0" width="100%" class="column">
				<tr>
					<td nowrap="true"><a><bean:message
						key="oscarEncounter.formRourke1.msgEducational" /></a></td>
				</tr>
				<tr>
					<td align="right"><bean:message
						key="oscarEncounter.formRourke1.msgSafety" /></td>
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
					<td align="right"><bean:message
						key="oscarEncounter.formRourke1.msgBehaviour" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td align="right"><bean:message
						key="oscarEncounter.formRourke1.msgFamily" /></td>
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
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td align="right"><bean:message
						key="oscarEncounter.formRourke1.msgOther" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_educationAdvice1w"
						cols="25" class="wide"><%= props.getProperty("p1_educationAdvice1w", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_carSeat1w" <%= props.getProperty("p1_carSeat1w", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>s_motorVehicleAccidents');return false;"><bean:message
						key="oscarEncounter.formRourke1.formCarSeat" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_cribSafety1w"
						<%= props.getProperty("p1_cribSafety1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formCribSafety" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_sleeping1w" <%= props.getProperty("p1_sleeping1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formSleeping" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_sooth1w" <%= props.getProperty("p1_sooth1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formSoothability" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_bonding1w" <%= props.getProperty("p1_bonding1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formParenting" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_fatigue1w" <%= props.getProperty("p1_fatigue1w", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formFatigue" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_family1w" <%= props.getProperty("p1_family1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFamilyConflict" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_siblings1w" <%= props.getProperty("p1_siblings1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formSiblings" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_homeVisit1w"
						<%= props.getProperty("p1_homeVisit1w", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>hri_homeVisits');return false;"><bean:message
						key="oscarEncounter.formRourke1.btnHomeVisit" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_sleepPos1w" <%= props.getProperty("p1_sleepPos1w", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>o_sleepPosition');return false;"><bean:message
						key="oscarEncounter.formRourke1.btnSleepPosition" /></a>*</b>
					<td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_temp1w" <%= props.getProperty("p1_temp1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formTemperatureControl" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_smoke1w" <%= props.getProperty("p1_smoke1w", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>o_secondHandSmoke');return false;"><bean:message
						key="oscarEncounter.formRourke1.formSecondHandSmoke" /></a>*</b></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_educationAdvice2w"
						cols="25" class="wide"><%= props.getProperty("p1_educationAdvice2w", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_carSeat2w" <%= props.getProperty("p1_carSeat2w", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>s_motorVehicleAccidents');return false;"><bean:message
						key="oscarEncounter.formRourke1.formCarSeat" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_cribSafety2w"
						<%= props.getProperty("p1_cribSafety2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formCribSafety" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_sleeping2w" <%= props.getProperty("p1_sleeping2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formSleeping" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_sooth2w" <%= props.getProperty("p1_sooth2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formSoothability" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_bonding2w" <%= props.getProperty("p1_bonding2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formParenting" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_fatigue2w" <%= props.getProperty("p1_fatigue2w", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formFatigue" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_family2w" <%= props.getProperty("p1_family2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFamilyConflict" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_siblings2w" <%= props.getProperty("p1_siblings2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formSiblings" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_homeVisit2w"
						<%= props.getProperty("p1_homeVisit2w", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>hri_homeVisits');return false;"><bean:message
						key="oscarEncounter.formRourke1.btnHomeVisit" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_sleepPos2w" <%= props.getProperty("p1_sleepPos2w", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>o_sleepPosition');return false;"><bean:message
						key="oscarEncounter.formRourke1.btnSleepPosition" />*</a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_temp2w" <%= props.getProperty("p1_temp2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formTemperatureControl" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_smoke2w" <%= props.getProperty("p1_smoke2w", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>o_secondHandSmoke'); return false;"><bean:message
						key="oscarEncounter.formRourke1.formSecondHandSmoke" /></a>* </b></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_educationAdvice1m"
						cols="25" class="wide"><%= props.getProperty("p1_educationAdvice1m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_carbonMonoxide1m"
						<%= props.getProperty("p1_carbonMonoxide1m", "") %>></td>
					<td>Carbon monoxide/ <i><a href="#"
						onclick="popup('<%=resource%>s_burns');return false;">Smoke
					detectors</a>*</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_sleepwear1m"
						<%= props.getProperty("p1_sleepwear1m", "") %>></td>
					<td><i><a href="#"
						onclick="popup('<%=resource%>s_burns');return false;"><bean:message
						key="oscarEncounter.formRourke1.formSleepwear" /></a></i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_hotWater1m" <%= props.getProperty("p1_hotWater1m", "") %>></td>
					<td><i><a href="#"
						onclick="popup('<%=resource%>s_burns');return false;"><bean:message
						key="oscarEncounter.formRourke1.btnHotWater" /></a>*</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_toys1m" <%= props.getProperty("p1_toys1m", "") %>></td>
					<td><a href="#"
						onclick="popup('<%=resource%>s_choking');return false;"><bean:message
						key="oscarEncounter.formRourke1.btnSafeToys" /></a>*</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_crying1m" <%= props.getProperty("p1_crying1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formSleeping" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_sooth1m" <%= props.getProperty("p1_sooth1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formSoothability" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_interaction1m"
						<%= props.getProperty("p1_interaction1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formParentChildinteraction" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_supports1m" <%= props.getProperty("p1_supports1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formAssessSupports" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_educationAdvice2m"
						cols="25" class="wide"><%= props.getProperty("p1_educationAdvice2m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_falls2m" <%= props.getProperty("p1_falls2m", "") %>></td>
					<td><i><a href="#"
						onclick="popup('<%=resource%>s_falls');return false;"><bean:message
						key="oscarEncounter.formRourke1.btnFalls" /></a>*</i></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_toys2m" <%= props.getProperty("p1_toys2m", "") %>></td>
					<td><a href="#"
						onclick="popup('<%=resource%>s_choking');return false;"><bean:message
						key="oscarEncounter.formRourke1.btnSafeToys" /></a>*</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_crying2m" <%= props.getProperty("p1_crying2m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formSleeping" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_sooth2m" <%= props.getProperty("p1_sooth2m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formSoothability" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_interaction2m"
						<%= props.getProperty("p1_interaction2m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formParentChildinteraction" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_stress2m" <%= props.getProperty("p1_stress2m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formDepression" /></td>
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
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_fever2m" <%= props.getProperty("p1_fever2m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFeverControl" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgDevelopment" /></a><br>
			<bean:message key="oscarEncounter.formRourke1.msgDevelopmentDesc" />
			</td>
			<td colspan="3" valign="top" align="center">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_development1w" cols="25"
						class="wide"><%= props.getProperty("p1_development1w", "") %></textarea></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top" align="center">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_development2w" cols="25"
						class="wide"><%= props.getProperty("p1_development2w", "") %></textarea></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_development1m" cols="25"
						class="wide"><%= props.getProperty("p1_development1m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_focusGaze1m"
						<%= props.getProperty("p1_focusGaze1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFocusesGaze" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_startles1m" <%= props.getProperty("p1_startles1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formSuddenNoise" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_sucks1m" <%= props.getProperty("p1_sucks1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formSucksWell" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_noParentsConcerns1m"
						<%= props.getProperty("p1_noParentsConcerns1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formNoparentConcerns" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_development2m" cols="25"
						class="wide"><%= props.getProperty("p1_development2m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_followMoves2m"
						<%= props.getProperty("p1_followMoves2m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFollowsMovement" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_sounds2m" <%= props.getProperty("p1_sounds2m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formVarietyOfSounds" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_headUp2m" <%= props.getProperty("p1_headUp2m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formHoldHeadsUp" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_cuddled2m" <%= props.getProperty("p1_cuddled2m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.EnjoysBeingTouched" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_noParentConcerns2m"
						<%= props.getProperty("p1_noParentConcerns2m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formNoparentConcerns" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgPhysicalExamination" /></a><br>
			<bean:message
				key="oscarEncounter.formRourke1.msgPhysicalExaminationDesc" />
			</div>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_physical1w" cols="25"
						class="wide"><%= props.getProperty("p1_physical1w", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_skin1w" <%= props.getProperty("p1_skin1w", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formDrySkin" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_fontanelles1w"
						<%= props.getProperty("p1_fontanelles1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_eyes1w" <%= props.getProperty("p1_eyes1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formRedReflex" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_ears1w" <%= props.getProperty("p1_ears1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formEarDrums" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_heartLungs1w"
						<%= props.getProperty("p1_heartLungs1w", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formHeart" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_umbilicus1w"
						<%= props.getProperty("p1_umbilicus1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formUmbilicus" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_femoralPulses1w"
						<%= props.getProperty("p1_femoralPulses1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFemoralPulses" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_hips1w" <%= props.getProperty("p1_hips1w", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formHips" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_testicles1w"
						<%= props.getProperty("p1_testicles1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formTescicles" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_maleUrinary1w"
						<%= props.getProperty("p1_maleUrinary1w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formMaleUrinaryStream" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_physical2w" cols="25"
						class="wide"><%= props.getProperty("p1_physical2w", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_skin2w" <%= props.getProperty("p1_skin2w", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formDrySkin" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_fontanelles2w"
						<%= props.getProperty("p1_fontanelles2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_eyes2w" <%= props.getProperty("p1_eyes2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formRedReflex" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_ears2w" <%= props.getProperty("p1_ears2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formEarDrums" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_heartLungs2w"
						<%= props.getProperty("p1_heartLungs2w", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formHeart" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_umbilicus2w"
						<%= props.getProperty("p1_umbilicus2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formUmbilicus" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_femoralPulses2w"
						<%= props.getProperty("p1_femoralPulses2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFemoralPulses" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_hips2w" <%= props.getProperty("p1_hips2w", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formHips" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_testicles2w"
						<%= props.getProperty("p1_testicles2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formTescicles" /><br>
					</td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_maleUrinary2w"
						<%= props.getProperty("p1_maleUrinary2w", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formMaleUrinaryStream" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_physical1m" cols="25"
						class="wide"><%= props.getProperty("p1_physical1m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_fontanelles1m"
						<%= props.getProperty("p1_fontanelles1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_eyes1m" <%= props.getProperty("p1_eyes1m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formRedReflex" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_cover1m" <%= props.getProperty("p1_cover1m", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>pe_cover');return false;"><bean:message
						key="oscarEncounter.formRourke1.btnCoverTest" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_hearing1m" <%= props.getProperty("p1_hearing1m", "") %>></td>
					<td><b><bean:message
						key="oscarEncounter.formRourke1.formHearingInquirity" /></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_heart1m" <%= props.getProperty("p1_heart1m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formHeart1" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_hips1m" <%= props.getProperty("p1_hips1m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formHips" /></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_physical2m" cols="25"
						class="wide"><%= props.getProperty("p1_physical2m", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_fontanelles2m"
						<%= props.getProperty("p1_fontanelles2m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formFontanelles" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_eyes2m" <%= props.getProperty("p1_eyes2m", "") %>></td>
					<td><bean:message
						key="oscarEncounter.formRourke1.formRedReflex" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_cover2m" <%= props.getProperty("p1_cover2m", "") %>></td>
					<td></i><b><a href="#"
						onclick="popup('<%=resource%>pe_cover');return false;"><bean:message
						key="oscarEncounter.formRourke1.btnCoverTest" /></a>*</b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_hearing2m" <%= props.getProperty("p1_hearing2m", "") %>></td>
					<td><b><bean:message
						key="oscarEncounter.formRourke1.formHearingInquirity" /></b></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_heart2m" <%= props.getProperty("p1_heart2m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formHeart1" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_hips2m" <%= props.getProperty("p1_hips2m", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formHips" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgProblemsAndPlans" /></a></td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_problems1w" cols="25"
						class="wide"><%= props.getProperty("p1_problems1w", "") %></textarea></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_pkuThyroid1w"
						<%= props.getProperty("p1_pkuThyroid1w", "") %>></td>
					<td><bean:message key="oscarEncounter.formRourke1.formThyroid" /></td>
				</tr>
				<tr>
					<td valign="top"><input type="checkbox" class="chk"
						name="p1_hemoScreen1w"
						<%= props.getProperty("p1_hemoScreen1w", "") %>></td>
					<td><b><a href="#"
						onclick="popup('<%=resource%>pp_hemoglobinopathyScreening');return false;"><bean:message
						key="oscarEncounter.formRourke1.formHemoglobinopathy" /></a> (if at
					risk)*</b></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_problems2w" cols="25"
						class="wide"><%= props.getProperty("p1_problems2w", "") %></textarea></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_problems1m" cols="25"
						class="wide"><%= props.getProperty("p1_problems1m", "") %></textarea></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr align="center">
					<td colspan="2"><textarea name="p1_problems2m" cols="25"
						class="wide"><%= props.getProperty("p1_problems2m", "") %></textarea></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.msgImmunization" /></a><br>
			<bean:message key="oscarEncounter.formRourke1.msgImmunizationDesc" />
			</td>
			<td colspan="3" valign="top"><textarea name="p1_immunization1w"
				cols="25" class="wide"><%= props.getProperty("p1_immunization1w", "") %></textarea></td>
			<td colspan="3" valign="top">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td colspan="2" align="center"><textarea
						name="p1_immunization2w" cols="25" class="wide"><%= props.getProperty("p1_immunization2w", "") %></textarea></td>
				</tr>
			</table>
			</td>
			<td colspan="3" valign="top"><textarea name="p1_immunization1m"
				cols="25" class="wide"><%= props.getProperty("p1_immunization1m", "") %></textarea></td>
			<td colspan="3" valign="top"><textarea name="p1_immunization2m"
				cols="25" class="wide"><%= props.getProperty("p1_immunization2m", "") %></textarea></td>
		</tr>
		<tr>
			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke1.formSignature" /></a></td>
			<td colspan="3"><input type="text" class="wide"
				style="width: 100%" name="p1_signature1w"
				value="<%= props.getProperty("p1_signature1w", "") %>" /></td>
			<td colspan="3"><input type="text" class="wide"
				style="width: 100%" name="p1_signature2w"
				value="<%= props.getProperty("p1_signature2w", "") %>" /></td>
			<td colspan="3"><input type="text" class="wide"
				style="width: 100%" name="p1_signature1m"
				value="<%= props.getProperty("p1_signature1m", "") %>" /></td>
			<td colspan="3"><input type="text" class="wide"
				style="width: 100%" name="p1_signature2m"
				value="<%= props.getProperty("p1_signature2m", "") %>" /></td>
		</tr>

	</table>

	<table cellpadding="0" cellspacing="0" class="Header" class="hidePrint">
		<tr>
			<td nowrap="true"><input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnSave"/>"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnSaveExit"/>"
				onclick="javascript:return onSaveExit();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke1.btnExit"/>"
				onclick="javascript:return onExit();"> <input type="button"
				value="<bean:message key="oscarEncounter.formRourke1.btnPrint"/>"
				onclick="javascript:return onPrint();" /></td>
			<td align="center" width="100%"><a name="length"
				href="javascript:popup('graphLengthWeight.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">
			<bean:message key="oscarEncounter.formRourke1.btnGraphLenghtWeight" /></a><br>
			<a name="headCirc"
				href="javascript:popup('graphHeadCirc.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">
			<bean:message key="oscarEncounter.formRourke1.btnGraphHead" /></a></td>
			<td nowrap="true"><a><bean:message
				key="oscarEncounter.formRourke1.msgPage1" /></a>&nbsp;|&nbsp; <a
				href="formrourkep2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke1.btnPage2" /></a>&nbsp;|&nbsp; <a
				href="formrourkep3.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke1.btnPage3" /></a></td>
		</tr>
	</table>

</html:form>
</body>
</html:html>
