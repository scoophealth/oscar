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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page
	import="java.util.*,oscar.oscarBilling.ca.bc.data.BillingCodeData,oscar.oscarBilling.ca.bc.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<html:html locale="true">



<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Add Billing Code</title>
<link rel="stylesheet" type="text/css"
	href="../../../oscarEncounter/encounterStyles.css">
<script type="text/javascript">

function isNumeric(strString){
    var validNums = "0123456789.";
    var strChar;
    var retval = true;

    for (i = 0; i < strString.length && retval == true; i++){
       strChar = strString.charAt(i);
       if (validNums.indexOf(strChar) == -1){
          retval = false;
       }
    }
     return retval;
}

function checkUnits(){
	if  (!isNumeric(document.BillingAddCodeForm.value.value)){
		alert("Price has to be a numeric value");
	        document.BillingAddCodeForm.value.focus();
		return false;
	}
	return true;
}



</script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF" onLoad="setValues()">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Billing</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Add Billing Code</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><a
					href="javascript:popupStart(300,400,'Help.jsp')"><bean:message
					key="global.help" /></a> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">&nbsp; &nbsp;</td>
		<td class="MainTableRightColumn" valign="top"><html:form
			action="/billing/CA/BC/billingAddCode"
			onsubmit="return checkUnits();">
			<%
                    BillingAddCodeForm frm = (BillingAddCodeForm) request.getAttribute("BillingAddCodeForm");
					String isEdit = request.getParameter("edit")!=null?request.getParameter("edit"):"";
                    if (request.getAttribute("code") != null){
                        frm.setCode((String) request.getAttribute("code"));
                        frm.setDesc((String) request.getAttribute("desc"));
                        frm.setValue((String) request.getAttribute("value"));
                    }

                if (request.getAttribute("returnMessage") != null){%>

			<table>
				<tr>
					<td style="font-color: red;"><%=request.getAttribute("returnMessage")%></td>
				</tr>
			</table>
			<%}%>
			<html:hidden property="whereTo" value="" />

			<table width="50%">
				<!--<tr>
                        <td>Code ID</td>
                        <td><html:text property="codeId"/></td>
                    </tr>-->

				<tr>
					<td width="23%"><strong>Service Code:</strong></td>
					<td width="77%"><html:text property="code" maxlength="5" /></td>
				</tr>
				<tr>
					<td><strong>Description:</strong></td>
					<td><html:text property="desc" /></td>
				</tr>
				<tr>
					<td><strong>Price:</strong></td>
					<td><html:text property="value" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><html:submit value="Add" /><html:button
						onclick="javascript: document.location = 'billingCodeAdjust.jsp'"
						property="back" value="Back" />
				</tr>
			</table>
		</html:form></td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
