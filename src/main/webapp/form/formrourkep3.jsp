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

<title><bean:message key="oscarEncounter.formRourke3.title" /></title>

<html:base />

<link rel="stylesheet" type="text/css" media="screen"
	href="rourkeStyle.css">

<link rel="stylesheet" type="text/css" media="print"
	href="printRourke.css">

</head>



<%

    String formClass = "Rourke";

    String formLink = "formrourkep3.jsp";



    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));

    int formId = Integer.parseInt(request.getParameter("formId"));

    int provNo = Integer.parseInt((String) session.getAttribute("user"));

    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);

    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request), demoNo, formId);



    FrmData fd = new FrmData();

    String resource = fd.getResource();

    resource = resource + "Rourke/";

    props.setProperty("c_lastVisited", "p3");

%>



<script type="text/javascript" language="Javascript">

    function onPrint() {

//        document.forms[0].submit.value="print";

//        var ret = checkAllDates();

//        if(ret==true)

//        {

//            ret = confirm("<bean:message key="oscarEncounter.formRourke3.msgSaveAndPrintPreview"/>");

//        }

//        return ret;

        window.print();

    }

    function onSave() {

        document.forms[0].submit.value="save";

        var ret = checkAllDates();

        if(ret==true)

        {

            ret = confirm("<bean:message key="oscarEncounter.formRourke3.msgSave"/>");

        }

        return ret;

    }

    

    function onSaveExit() {

        document.forms[0].submit.value="exit";

        var ret = checkAllDates();

        if(ret == true)

        {

            ret = confirm("<bean:message key="oscarEncounter.formRourke3.msgSaveExit"/>");

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

          alert ("<bean:message key="oscarEncounter.formRourke3.msgTypeANumber"/>");

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
		value=<%=props.getProperty("c_lastVisited", "p3")%> />

	<input type="hidden" name="submit" value="exit" />



	<table cellpadding="0" cellspacing="0" class="Header" class="hidePrint">

		<tr>

			<td nowrap="true"><input type="submit"
				value="<bean:message key="oscarEncounter.formRourke3.btnSave"/>"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke3.btnSaveExit"/>"
				onclick="javascript:return onSaveExit();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke3.btnExit"/>"
				onclick="javascript:return onExit();"> <input type="button"
				value="<bean:message key="oscarEncounter.formRourke3.btnPrint"/>"
				onclick="javascript:return onPrint();" /></td>

			<td align="center" width="100%"><a name="length"
				href="javascript:popup('graphLengthWeight.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">

			<bean:message key="oscarEncounter.formRourke3.btnGraphLenght" /></a><br>

			<a name="headCirc"
				href="javascript:popup('graphHeadCirc.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">

			<bean:message key="oscarEncounter.formRourke3.btnGraphHead" /></a></td>

			<td nowrap="true"><a
				href="formrourkep1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke3.btnPage1" /></a>&nbsp;|&nbsp; <a
				href="formrourkep2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke3.btnPage2" /></a>&nbsp;|&nbsp; <a><bean:message
				key="oscarEncounter.formRourke3.msgPage3" /></a></td>

		</tr>

	</table>



	<table cellpadding="0" cellspacing="0" border="0" width="100%">

		<tr class="titleBar">

			<th><bean:message
				key="oscarEncounter.formRourke3.msgRourkeBabyRecord" /></th>

		</tr>

	</table>

	<table cellpadding="0" cellspacing="0" width="100%" border="0">

		<tr valign="top">

			<td nowrap align="center"><bean:message
				key="oscarEncounter.formRourke3.msgBirthRemarks" /><br>

			<textarea name="c_birthRemarks" rows="2" cols="17"><%= props.getProperty("c_birthRemarks", "") %></textarea>

			</td>

			<td nowrap align="center"><bean:message
				key="oscarEncounter.formRourke3.msgRiskFactors" /><br>

			<textarea name="c_riskFactors" rows="2" cols="17"><%= props.getProperty("c_riskFactors", "") %></textarea>

			</td>

			<td width="65%" nowrap align="center">

			<p><bean:message key="oscarEncounter.formRourke3.msgName" />: <input
				type="text" name="c_pName" maxlength="60" size="30"
				value="<%= props.getProperty("c_pName", "") %>" readonly="true" />

			&nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke3.msgBirthDate" /> (yyyy/mm/dd): <input
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

			<p><bean:message key="oscarEncounter.formRourke3.formLenght" />:
			<input type="text" name="c_length" size="6" maxlength="6"
				value="<%= props.getProperty("c_length", "") %>" /> <bean:message
				key="oscarEncounter.formRourke3.msgLenghtUnit" /> &nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke3.formHeadCirc" />: <input type="text"
				name="c_headCirc" size="6" maxlength="6"
				value="<%= props.getProperty("c_headCirc", "") %>" /> <bean:message
				key="oscarEncounter.formRourke3.msgHeadCircUnit" /> &nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke3.formBirthWt" />: <input type="text"
				name="c_birthWeight" size="6" maxlength="7"
				value="<%= props.getProperty("c_birthWeight", "") %>" /> <bean:message
				key="oscarEncounter.formRourke3.msgBirthUnit" /> &nbsp;&nbsp; <bean:message
				key="oscarEncounter.formRourke3.formDischargeWt" />: <input
				type="text" name="c_dischargeWeight" size="6" maxlength="7"
				value="<%= props.getProperty("c_dischargeWeight", "") %>"> <bean:message
				key="oscarEncounter.formRourke3.msgDischargeWtUnit" /></p>

			</td>

		</tr>

	</table>

	<table cellpadding="0" cellspacing="0" width="100%" border="1">

		<tr align="center">

			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke3.msgAge" /></a></td>

			<td class="row"><a><bean:message
				key="oscarEncounter.formRourke3.msg18Months" /></a></td>

			<td class="row"><a><bean:message
				key="oscarEncounter.formRourke3.msg2-3years" /></a></td>

			<td class="row"><a><bean:message
				key="oscarEncounter.formRourke3.msg4-5years" /></a></td>

		</tr>

		<tr align="center">

			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke3.msgDate" /></a></td>

			<td>(yyyy/mm/dd) <input type="text" name="p3_date18m" size="10"
				value="<%=props.getProperty("p3_date18m", "")%>" /></td>

			<td>(yyyy/mm/dd) <input type="text" name="p3_date2y" size="10"
				value="<%=props.getProperty("p3_date2y", "")%>" /></td>

			<td>(yyyy/mm/dd) <input type="text" name="p3_date4y" size="10"
				value="<%=props.getProperty("p3_date4y", "")%>" /></td>

		</tr>

		<tr align="center">

			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke3.msgGrowth" /></a></td>

			<td>

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr>

					<td align="center"><bean:message
						key="oscarEncounter.formRourke3.formHt" /><br>
					<input type="text" class="wide" name="p3_ht18m" size="4"
						maxlength="5" value="<%= props.getProperty("p3_ht18m", "") %>"></td>

					<td align="center"><bean:message
						key="oscarEncounter.formRourke3.formWt" /><br>
					<input type="text" class="wide" name="p3_wt18m" size="4"
						maxlength="5" value="<%= props.getProperty("p3_wt18m", "") %>"></td>

					<td align="center"><bean:message
						key="oscarEncounter.formRourke3.formHdCirc" /><br>
					<input type="text" class="wide" name="p3_hc18m" size="4"
						maxlength="5" value="<%= props.getProperty("p3_hc18m", "") %>"></td>

				</tr>

			</table>

			</td>

			<td>

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr>

					<td align="center"><bean:message
						key="oscarEncounter.formRourke3.formHt" /><br>
					<input type="text" class="wide" name="p3_ht2y" size="4"
						maxlength="5" value="<%= props.getProperty("p3_ht2y", "") %>"></td>

					<td align="center"><bean:message
						key="oscarEncounter.formRourke3.formWt" /><br>
					<input type="text" class="wide" name="p3_wt2y" size="4"
						maxlength="5" value="<%= props.getProperty("p3_wt2y", "") %>"></td>

				</tr>

			</table>

			</td>

			<td>

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr>

					<td align="center"><bean:message
						key="oscarEncounter.formRourke3.formHt" /><br>
					<input type="text" class="wide" name="p3_ht4y" size="4"
						maxlength="5" value="<%= props.getProperty("p3_ht4y", "") %>"></td>

					<td align="center"><bean:message
						key="oscarEncounter.formRourke3.formWt" /><br>
					<input type="text" class="wide" name="p3_wt4y" size="4"
						maxlength="5" value="<%= props.getProperty("p3_wt4y", "") %>"></td>

				</tr>

			</table>

			</td>

		</tr>

		<tr align="center">

			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke3.msgParentalConcerns" /></a></td>

			<td><textarea name="p3_pConcern18m" style="width: 100%"
				cols="10" rows="2"><%= props.getProperty("p3_pConcern18m", "") %></textarea>

			</td>

			<td><textarea name="p3_pConcern2y" style="width: 100%" cols="10"
				rows="2"><%= props.getProperty("p3_pConcern2y", "") %></textarea></td>

			<td><textarea name="p3_pConcern4y" style="width: 100%" cols="10"
				rows="2"><%= props.getProperty("p3_pConcern4y", "") %></textarea></td>

		</tr>

		<tr>

			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke3.msgNutrition" /></a>:</td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_nutrition18m" cols="25"
						class="wide"><%= props.getProperty("p3_nutrition18m", "") %></textarea></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_bottle18m" <%= props.getProperty("p3_bottle18m", "") %> /></td>

					<td width="100%"><bean:message
						key="oscarEncounter.formRourke3.formNoBottles" /></td>

				</tr>

			</table>

			</td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_nutrition2y" cols="25"
						class="wide"><%= props.getProperty("p3_nutrition2y", "") %></textarea></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_milk2y" <%= props.getProperty("p3_milk2y", "") %>></td>

					<td width="100%"><bean:message
						key="oscarEncounter.formRourke3.formHomogenized" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_food2y" <%= props.getProperty("p3_food2y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formFoodGuide" /></td>

				</tr>

			</table>

			</td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_nutrition4y" cols="25"
						class="wide"><%= props.getProperty("p3_nutrition4y", "") %></textarea></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_milk4y" <%= props.getProperty("p3_milk4y", "") %>></td>

					<td width="100%"><bean:message
						key="oscarEncounter.formRourke3.form2-100milk" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_food4y" <%= props.getProperty("p3_food4y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formFoodGuide" /></td>

				</tr>

			</table>

			</td>

		</tr>

		<tr>

			<td class="column" valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr>

					<td align="center" nowrap="true"><b><bean:message
						key="oscarEncounter.formRourke3.msgEducationalAdvice" /></b></td>

				</tr>

				<tr>

					<td align="right"><b><bean:message
						key="oscarEncounter.formRourke3.msgSafety" /></b></td>

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
						key="oscarEncounter.formRourke3.msgBehaviour" /></b></td>

				</tr>

				<tr>
					<td>&nbsp;</td>
				</tr>

				<tr>

					<td align="right"><b><bean:message
						key="oscarEncounter.formRourke3.msfFamily" /></b></td>

				</tr>

				<tr>
					<td>&nbsp;</td>
				</tr>

				<tr>
					<td>&nbsp;</td>
				</tr>

				<tr>

					<td align="right"><b><bean:message
						key="oscarEncounter.formRourke3.msgOther" /></b></td>

				</tr>

			</table>

			</td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_educationAdvice18m"
						cols="25" class="wide"><%= props.getProperty("p3_educationAdvice18m", "") %></textarea></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_bath18m" <%= props.getProperty("p3_bath18m", "") %>></td>

					<td width="100%"><i><a href="#"
						onclick="popup('<%=resource%>s_drowning');return false;"><bean:message
						key="oscarEncounter.formRourke3.btnbathSafety" /></a>*</i></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_choking18m" <%= props.getProperty("p3_choking18m", "") %>></td>

					<td><a href="#"
						onclick="popup('<%=resource%>s_choking');return false;"><bean:message
						key="oscarEncounter.formRourke3.btnChokngSafeToys" /></a>*</td>

				</tr>

				<tr>

					<td>&nbsp;</td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_temperment18m"
						<%= props.getProperty("p3_temperment18m", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formTemperment" /></td>

				</tr>

				<tr>

					<td valign="top">&nbsp;</td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_limit18m" <%= props.getProperty("p3_limit18m", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formLimitSetting" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_social18m" <%= props.getProperty("p3_social18m", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formSocializingOpp" /></td>

				</tr>

				<tr>

					<td valign="top">&nbsp;</td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_dental18m" <%= props.getProperty("p3_dental18m", "") %>></td>

					<td><b><a href="#"
						onclick="popup('<%=resource%>o_dentalCare');return false;"><bean:message
						key="oscarEncounter.formRourke3.formDentalCare" /></a>*</b></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_toilet18m" <%= props.getProperty("p3_toilet18m", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formToiletTraining" /></td>

				</tr>

			</table>

			</td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_educationAdvice2y"
						cols="25" class="wide"><%= props.getProperty("p3_educationAdvice2y", "") %></textarea></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_bike2y" <%= props.getProperty("p3_bike2y", "") %>></td>

					<td width="100%"><i><a href="#"
						onclick="popup('<%=resource%>s_falls');return false;"><bean:message
						key="oscarEncounter.formRourke3.formBikeHelmets" /></a>*</i></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_matches2y" <%= props.getProperty("p3_matches2y", "") %>></td>

					<td><bean:message key="oscarEncounter.formRourke3.formMatches" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_carbon2y" <%= props.getProperty("p3_carbon2y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formCarbonMonoxide" />/ <i><a
						href="#" onclick="popup('<%=resource%>s_burns');return false;"><bean:message
						key="oscarEncounter.formRourke3.formSmokeDetectors" /></a>*</i></td>

				</tr>

				<tr>

					<td>&nbsp;</td>

				</tr>

				<tr>

					<td>&nbsp;</td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_parent2y" <%= props.getProperty("p3_parent2y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formParentChildInteraction" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_social2y" <%= props.getProperty("p3_social2y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formSocializingOpp" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_dayCare2y" <%= props.getProperty("p3_dayCare2y", "") %>></td>

					<td><b><a href="#"
						onclick="popup('<%=resource%>hri_dayCare');return false;"><bean:message
						key="oscarEncounter.formRourke3.formAssessDayCare" /></a>*</b></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_dental2y" <%= props.getProperty("p3_dental2y", "") %>></td>

					<td><b><a href="#"
						onclick="popup('<%=resource%>o_dentalCare');return false;"><bean:message
						key="oscarEncounter.formRourke3.formDentalCareCheckUp" /></a>*</b></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_toilet2y" <%= props.getProperty("p3_toilet2y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formToiletTraining" /></td>

				</tr>

			</table>

			</td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_educationAdvice4y"
						cols="25" class="wide"><%= props.getProperty("p3_educationAdvice4y", "") %></textarea></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_bike4y" <%= props.getProperty("p3_bike4y", "") %>></td>

					<td width="100%"><i><a href="#"
						onclick="popup('<%=resource%>s_falls');return false;"><bean:message
						key="oscarEncounter.formRourke3.formBikeHelmets" /></a>*</i></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_matches4y" <%= props.getProperty("p3_matches4y", "") %>></td>

					<td><bean:message key="oscarEncounter.formRourke3.formMatches" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_carbon4y" <%= props.getProperty("p3_carbon4y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formCarbonMonoxide" />/ <i><a
						href="#" onclick="popup('<%=resource%>s_burns');return false;"><bean:message
						key="oscarEncounter.formRourke3.formSmokeDetectors" /></a>*</i></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_water4y" <%= props.getProperty("p3_water4y", "") %>></td>

					<td><a href="#"
						onclick="popup('<%=resource%>s_drowning');return false;"><bean:message
						key="oscarEncounter.formRourke3.formWaterSafety" /></a></td>

				</tr>

				<tr>

					<td valign="top">&nbsp;</td>

				</tr>

				<tr>

					<td valign="top">&nbsp;</td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_social4y" <%= props.getProperty("p3_social4y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formSocializingOpp" /></td>

				</tr>

				<tr>

					<td valign="top">&nbsp;</td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_dental4y" <%= props.getProperty("p3_dental4y", "") %>></td>

					<td><b><a href="#"
						onclick="popup('<%=resource%>o_dentalCare');return false;"><bean:message
						key="oscarEncounter.formRourke3.formDentalCareCheckUp" /></a>*</b></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_school4y" <%= props.getProperty("p3_school4y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formSchoolReadiness" /></td>

				</tr>

			</table>

			</td>

		</tr>

		<tr>

			<td class="column">

			<div align="center"><b><bean:message
				key="oscarEncounter.formRourke3.msgDevelopment" /></b><br>

			<bean:message key="oscarEncounter.formRourke3.msgDevelopmentDesc" />

			</div>

			</td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_development18m" cols="25"
						class="wide"><%= props.getProperty("p3_development18m", "") %></textarea></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_points18m" <%= props.getProperty("p3_points18m", "") %>></td>

					<td width="100%"><bean:message
						key="oscarEncounter.formRourke3.formPoints" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_words18m" <%= props.getProperty("p3_words18m", "") %>></td>

					<td><bean:message key="oscarEncounter.formRourke3.form5Words" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_picks18m" <%= props.getProperty("p3_picks18m", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formFingerFood" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_walks18m" <%= props.getProperty("p3_walks18m", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formWalkAlone" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_stacks18m" <%= props.getProperty("p3_stacks18m", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formStack3Blocks" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_affection18m"
						<%= props.getProperty("p3_affection18m", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formShowAffection" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_showParents18m"
						<%= props.getProperty("p3_showParents18m", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formPointShow" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_looks18m" <%= props.getProperty("p3_looks18m", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formLooksWhenTalk" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_noParentsConcerns18m"
						<%= props.getProperty("p3_noParentsConcerns18m", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formNoParentsConcerns" /></td>

				</tr>

			</table>

			</td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_development2y" cols="25"
						class="wide"><%= props.getProperty("p3_development2y", "") %></textarea></td>

				</tr>

				<tr>

					<td colspan="2"><b><bean:message
						key="oscarEncounter.formRourke3.msg2Years" /></b></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_word2y" <%= props.getProperty("p3_word2y", "") %>></td>

					<td width="100%"><bean:message
						key="oscarEncounter.formRourke3.formNewWordWeek" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_sentence2y" <%= props.getProperty("p3_sentence2y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.form2WordSentences" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_run2y" <%= props.getProperty("p3_run2y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formTriesToRun" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_container2y"
						<%= props.getProperty("p3_container2y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formPutObjectsContainer" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_copies2y" <%= props.getProperty("p3_copies2y", "") %>></td>

					<td><bean:message key="oscarEncounter.formRourke3.formCopies" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_skills2y" <%= props.getProperty("p3_skills2y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formDevelopNewSkills" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_noParentsConcerns2y"
						<%= props.getProperty("p3_noParentsConcerns2y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formNoParentsConcerns" /></td>

				</tr>

			</table>
			<br>

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_development3y" cols="25"
						class="wide"><%= props.getProperty("p3_development3y", "") %></textarea></td>

				</tr>

				<tr>

					<td colspan="2"><b><bean:message
						key="oscarEncounter.formRourke3.msg3Years" /></b></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_understands3y"
						<%= props.getProperty("p3_understands3y", "") %>></td>

					<td width="100%"><bean:message
						key="oscarEncounter.formRourke3.formUnderstands2StepDirection" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_twists3y" <%= props.getProperty("p3_twists3y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formTurnsKnobs" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_turnPages3y"
						<%= props.getProperty("p3_turnPages3y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formTurnsOnePage" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_share3y" <%= props.getProperty("p3_share3y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formShareSomeTime" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_listens3y" <%= props.getProperty("p3_listens3y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formListenMusic" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_noParentsConcerns3y"
						<%= props.getProperty("p3_noParentsConcerns3y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formNoParentsConcerns" /></td>

				</tr>

			</table>

			</td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_development4y" cols="25"
						class="wide"><%= props.getProperty("p3_development4y", "") %></textarea></td>

				</tr>

				<tr>

					<td colspan="2"><b><bean:message
						key="oscarEncounter.formRourke3.msg4Years" /></b></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_understands4y"
						<%= props.getProperty("p3_understands4y", "") %>></td>

					<td width="100%"><bean:message
						key="oscarEncounter.formRourke3.formUnderstandsRelated3PartDirection" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_questions4y"
						<%= props.getProperty("p3_questions4y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formAsksQuestions" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_oneFoot4y" <%= props.getProperty("p3_oneFoot4y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formStandsOn1Foot" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_draws4y" <%= props.getProperty("p3_draws4y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formDraw3PartsPerson" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_toilet4y" <%= props.getProperty("p3_toilet4y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formToiletTrained" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_comfort4y" <%= props.getProperty("p3_comfort4y", "") %>></td>

					<td>Tries to comfort someone who is upset</td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_noParentsConcerns4y"
						<%= props.getProperty("p3_noParentsConcerns4y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formNoParentsConcerns" /></td>

				</tr>

			</table>
			<br>

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_development5y" cols="25"
						class="wide"><%= props.getProperty("p3_development5y", "") %></textarea></td>

				</tr>

				<tr>

					<td colspan="2"><b><bean:message
						key="oscarEncounter.formRourke3.msg5Years" /></b></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_counts5y" <%= props.getProperty("p3_counts5y", "") %>></td>

					<td width="100%"><bean:message
						key="oscarEncounter.formRourke3.formCounts10" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_speaks5y" <%= props.getProperty("p3_speaks5y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formSpeaksClearly" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_ball5y" <%= props.getProperty("p3_ball5y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formPlayWithBall" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_hops5y" <%= props.getProperty("p3_hops5y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formHops1Foot" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_shares5y" <%= props.getProperty("p3_shares5y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formSharesWillingly" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_alone5y" <%= props.getProperty("p3_alone5y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formWorksAlone20Minutes" /></td>

				</tr>



				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_separate5y" <%= props.getProperty("p3_separate5y", "") %>></td>

					<td>Separates easily from parents</td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_noParentsConcerns5y"
						<%= props.getProperty("p3_noParentsConcerns5y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formNoParentsConcerns" /></td>

				</tr>

			</table>

			</td>

		</tr>

		<tr>

			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke3.msgPhysicalExamination" /></a><br>

			<bean:message
				key="oscarEncounter.formRourke3.msgPhysicalExaminationDecs" /></td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_physical18m" cols="25"
						class="wide"><%= props.getProperty("p3_physical18m", "") %></textarea></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_eyes18m" <%= props.getProperty("p3_eyes18m", "") %>></td>

					<td width="100%"><bean:message
						key="oscarEncounter.formRourke3.formRedEyes" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_cover18m" <%= props.getProperty("p3_cover18m", "") %>></td>

					<td><b><a href="#"
						onclick="popup('<%=resource%>pe_cover');return false;"><bean:message
						key="oscarEncounter.formRourke3.btnCoverTest" /></a>*</b></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_hearing18m" <%= props.getProperty("p3_hearing18m", "") %>></td>

					<td><b><bean:message
						key="oscarEncounter.formRourke3.msgHearing" /></b></td>

				</tr>

			</table>

			</td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_physical2y" cols="25"
						class="wide"><%= props.getProperty("p3_physical2y", "") %></textarea></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_visual2y" <%= props.getProperty("p3_visual2y", "") %>></td>

					<td width="100%"><bean:message
						key="oscarEncounter.formRourke3.formVisualAcuity" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_cover2y" <%= props.getProperty("p3_cover2y", "") %>></td>

					<td><b><a href="#"
						onclick="popup('<%=resource%>pe_cover');return false;"><bean:message
						key="oscarEncounter.formRourke3.btnCoverTest" /></a>*</b></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_hearing2y" <%= props.getProperty("p3_hearing2y", "") %>></td>

					<td><b><bean:message
						key="oscarEncounter.formRourke3.msgHearing" /></b></td>

				</tr>

			</table>

			</td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_physical4y" cols="25"
						class="wide"><%= props.getProperty("p3_physical4y", "") %></textarea></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_visual4y" <%= props.getProperty("p3_visual4y", "") %>></td>

					<td width="100%"><bean:message
						key="oscarEncounter.formRourke3.formVisualAcuity" /></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_cover4y" <%= props.getProperty("p3_cover4y", "") %>></td>

					<td><b><a href="#"
						onclick="popup('<%=resource%>pe_cover');return false;"><bean:message
						key="oscarEncounter.formRourke3.btnCoverTest" /></a>*</b></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_hearing4y" <%= props.getProperty("p3_hearing4y", "") %>></td>

					<td><b><bean:message
						key="oscarEncounter.formRourke3.msgHearing" /></b></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_blood4y" <%= props.getProperty("p3_blood4y", "") %>></td>

					<td><bean:message
						key="oscarEncounter.formRourke3.formBloodPressure" /></td>

				</tr>

			</table>

			</td>

		</tr>

		<tr>

			<td class="column">

			<div align="center"><b><bean:message
				key="oscarEncounter.formRourke3.msgProblems" /></b></div>

			</td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_problems18m" cols="25"
						class="wide"><%= props.getProperty("p3_problems18m", "") %></textarea></td>

				</tr>

			</table>

			</td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_problems2y" cols="25"
						class="wide"><%= props.getProperty("p3_problems2y", "") %></textarea></td>

				</tr>

				<tr>

					<td valign="top"><input type="checkbox" class="chk"
						name="p3_serum2y" <%= props.getProperty("p3_serum2y", "") %>></td>

					<td width="100%"><i><a href="#"
						onclick="popup('<%=resource%>pp_leadScreening');return false;"><bean:message
						key="oscarEncounter.formRourke3.msgSerumLead" /></a>*</i></td>

				</tr>

			</table>

			</td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td colspan="2"><textarea name="p3_problems4y" cols="25"
						class="wide"><%= props.getProperty("p3_problems4y", "") %></textarea></td>

				</tr>

			</table>

			</td>

		</tr>

		<tr>

			<td class="column">

			<div align="center"><b><bean:message
				key="oscarEncounter.formRourke3.msgImmunization" /></b><br>

			<bean:message key="oscarEncounter.formRourke3.msgImmunizationDesc" />

			</div>

			</td>

			<td valign="top"><textarea name="p3_immunization18m" cols="25"
				class="wide"><%= props.getProperty("p3_immunization18m", "") %></textarea></td>

			<td valign="top">

			<table cellpadding="0" cellspacing="0" width="100%">

				<tr align="center">

					<td><textarea name="p3_immunization2y" cols="25" class="wide"><%= props.getProperty("p3_immunization2y", "") %></textarea></td>

				</tr>

			</table>

			</td>

			<td valign="top"><textarea name="p3_immunization4y" cols="25"
				class="wide"><%= props.getProperty("p3_immunization4y", "") %></textarea></td>

		</tr>

		<tr>

			<td class="column"><a><bean:message
				key="oscarEncounter.formRourke3.formSignature" /></a></td>

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



	<table cellpadding="0" cellspacing="0" class="Header" class="hidePrint">

		<tr>

			<td nowrap="true"><input type="submit"
				value="<bean:message key="oscarEncounter.formRourke3.btnSave"/>"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke3.btnSaveExit"/>"
				onclick="javascript:return onSaveExit();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formRourke3.btnExit"/>"
				onclick="javascript:return onExit();"> <input type="button"
				value="<bean:message key="oscarEncounter.formRourke3.btnPrint"/>"
				onclick="javascript:return onPrint();" /></td>

			<td align="center" width="100%"><a name="length"
				href="javascript:popup('graphLengthWeight.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">

			<bean:message key="oscarEncounter.formRourke3.btnGraphLenght" /></a><br>

			<a name="headCirc"
				href="javascript:popup('graphHeadCirc.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>');">

			<bean:message key="oscarEncounter.formRourke3.btnGraphHead" /></a></td>

			<td nowrap="true"><a
				href="formrourkep1.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke3.btnPage1" /></a>&nbsp;|&nbsp; <a
				href="formrourkep2.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>"><bean:message
				key="oscarEncounter.formRourke3.btnPage2" /></a>&nbsp;|&nbsp; <a><bean:message
				key="oscarEncounter.formRourke3.msgPage3" /></a></td>

		</tr>

	</table>



</html:form>

</body>

</html:html>
