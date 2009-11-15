<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%-- This JSP is the multi-site admin site detail page --%>
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

<%@page import="org.oscarehr.common.model.Site"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Clinic</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">

<script type="text/javascript" language="JavaScript"
	src="../share/javascript/prototype.js"></script>
<script type="text/javascript" language="JavaScript"
	src="../share/javascript/Oscar.js"></script>
<link href="<html:rewrite page='/css/displaytag.css'/>" rel="stylesheet" ></link>
<style>.button {border:1px solid #666666;} </style>

</head>

<body vlink="#0000FF" class="BodyStyle" onload="$('colorField').style.backgroundColor=$('colorField').value;">
<nested:form action="/admin/ManageSites">
<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">admin</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>Add New Satellite Site</td>
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
	<tr><td>Site Name:<sup style="color:red">*</sup></td><td><nested:text property="site.name" maxlength="30"></nested:text></td></tr>
	<tr><td>Short Name:<sup style="color:red">*</sup></td><td><nested:text property="site.shortName" maxlength="10"></nested:text></td></tr>
	<tr><td>Theme Color:<sup style="color:red">*</sup></td><td><nested:text styleId="colorField" property="site.bgColor" onclick="popup(350,450,'../colorpicker/colorpicker.htm','colorpicker');return false;"></nested:text>
	</td></tr>
	<tr><td>Active:</td><td><nested:checkbox property="site.status" value="1"/></td></tr>
	<tr><td>Telephone:</td><td><nested:text property="site.phone"></nested:text></td></tr>
	<tr><td>FAX:</td><td><nested:text property="site.fax"></nested:text></td></tr>
	<tr><td>Address:</td><td><nested:text property="site.address"></nested:text></td></tr>
	<tr><td>City:</td><td><nested:text property="site.city"></nested:text></td></tr>
	<tr><td>Province:</td><td><nested:select property="site.province">
					<html:option value="AB" >AB-Alberta</html:option>
					<html:option value="BC" >BC-British Columbia</html:option>
					<html:option value="MB" >MB-Manitoba</html:option>
					<html:option value="NB" >NB-New Brunswick</html:option>
					<html:option value="NL" >NL-Newfoundland & Labrador</html:option>
					<html:option value="NT" >NT-Northwest Territory</html:option>
					<html:option value="NS" >NS-Nova Scotia</html:option>
					<html:option value="NU" >NU-Nunavut</html:option>
					<html:option value="ON" >ON-Ontario</html:option>
					<html:option value="PE" >PE-Prince Edward Island</html:option>
					<html:option value="QC" >QC-Quebec</html:option>
					<html:option value="SK" >SK-Saskatchewan</html:option>
					<html:option value="YT" >YT-Yukon</html:option>	
	</nested:select></td></tr>
	<tr><td>Postal Code:</td><td><nested:text property="site.postal"></nested:text></td></tr>
	</table>

	<nested:hidden property="site.siteId"/>
	<input name="method" type="hidden" value="save"></input>
	<nested:submit styleClass="button" >Save</nested:submit> <nested:submit styleClass="button" onclick="this.form.method.value='view'">Cancel</nested:submit>

  		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</nested:form>


</html:html>
