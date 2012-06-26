<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<%@page import="org.oscarehr.common.model.Billingreferral"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Add/Edit Referral Doctor</title>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">

<script type="text/javascript" language="JavaScript" src="../share/javascript/prototype.js"></script>
<script type="text/javascript" language="JavaScript" src="../share/javascript/Oscar.js"></script>
<link href="<html:rewrite page='/css/displaytag.css'/>" rel="stylesheet" ></link>
<style>.button {border:1px solid #666666;} </style>

</head>

<body vlink="#0000FF" class="BodyStyle" onload="$('colorField').style.backgroundColor=$('colorField').value;">
<nested:form action="/admin/ManageBillingReferral">
<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Admin</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>Edit Referral Details</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" width="160px;">
		&nbsp;</td>
		<td class="MainTableRightColumn" valign="top">
			<html:messages id="errors" header="errors.header" footer="errors.footer">
				<li><bean:write name="errors" /></li>
			</html:messages>

	<table>
	<tr><td>Referral No:</td><td><nested:text property="referral.referralNo"></nested:text></td></tr>
	<tr><td>lastName:</td><td><nested:text property="referral.lastName"></nested:text></td></tr>
	<tr><td>firstName:</td><td><nested:text property="referral.firstName"></nested:text></td></tr>
	</td></tr>
	<tr><td>specialty:</td><td><nested:text property="referral.specialty"></nested:text></td></tr>
	<tr><td>address1:</td><td><nested:text property="referral.address1"></nested:text></td></tr>
	<tr><td>address2:</td><td><nested:text property="referral.address2"></nested:text></td></tr>
	<tr><td>city:</td><td><nested:text property="referral.city"></nested:text></td></tr>
	<tr><td>Province:</td><td><nested:text property="referral.province"></nested:text></td></tr>
	<tr><td>country:</td><td><nested:text property="referral.country"></nested:text></td></tr>
        <tr><td>postal:</td><td><nested:text property="referral.postal"></nested:text></td></tr>
	<tr><td>phone:</td><td><nested:text property="referral.phone"></nested:text></td></tr>
        <tr><td>fax:</td><td><nested:text property="referral.fax"></nested:text></td></tr>
	</table>

	<nested:hidden property="referral.billingreferralNo"/>
	<input name="method" type="hidden" value="update"></input>
	<nested:submit styleClass="button" >Save</nested:submit> <nested:submit styleClass="button" onclick="this.form.method.value='list'">Cancel</nested:submit>

  		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</nested:form>


</html:html>
