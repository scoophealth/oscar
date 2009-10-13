<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<!--
/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster Unviersity
 * Hamilton
 * Ontario, Canada
 */
-->
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
<%@page import="java.util.*"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Referral Doctor</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">

<script type="text/javascript" language="JavaScript"
	src="../share/javascript/prototype.js"></script>
<script type="text/javascript" language="JavaScript"
	src="../share/javascript/Oscar.js"></script>
<link href="<html:rewrite page='/css/displaytag.css'/>" rel="stylesheet" ></link>
</head>

<body vlink="#0000FF" class="BodyStyle">

<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">admin</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>Manage Referral Doctors</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" width="160px;">&nbsp;
		</td>
		<td class="MainTableRightColumn" valign="top">

<%
    String URL =request.getContextPath()+"/admin/ManageBillingReferral.do?method=edit";
    request.setAttribute("referrals", request.getSession().getAttribute("referrals"));
%>


<nested:form action="/admin/ManageBillingReferral?method=searchbyname">
    <label>
      <input type="radio" name="SearchBy" value="radio" id="SearchBy_0" onclick="javascript:this.form.action='<%= request.getContextPath() %>/admin/ManageBillingReferral.do?method=searchbyno'">
      ReferralNo</label>
    <label>
      <input type="radio" name="SearchBy" value="radio" id="SearchBy_1" checked="true" onclick="javascript:this.form.action='<%= request.getContextPath() %>/admin/ManageBillingReferral.do?method=searchbyname'">
      Name</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <nested:text property="search"></nested:text>
	<nested:submit style="border:1px solid #666666;">Search</nested:submit>
        <nested:submit style="border:1px solid #666666;" onclick="this.form.action=<%= URL %>">Add</nested:submit>
</nested:form>

<display:table name="referrals" id="referral" class="its" pagesize="15" style="border:1px solid #666666; width:99%;margin-top:2px;">
    <display:column property="referralNo" title="Referral No" href="<%= URL %>" paramId="referralNo"/>
    <display:column property="firstName" title="First Name" />
    <display:column property="lastName" title="Last Name" />
    <display:column property="specialty" title="Specialty" />
    <display:column property="city" title="City" />
    <display:column property="phone" title="Phone" />
    <display:column property="fax" title="Fax" />
</display:table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>

</html:html>
