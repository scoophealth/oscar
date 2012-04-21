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

<%@page import="java.util.*, org.oscarehr.hospitalReportManager.*, org.oscarehr.hospitalReportManager.model.HRMCategory, org.oscarehr.hospitalReportManager.dao.HRMCategoryDao, org.oscarehr.util.SpringUtils"%>

<%
	
	String deepColor = "#CCCCFF", weakColor = "#EEEEFF";

	String country = request.getLocale().getCountry();
	

%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<title>Show Mappings</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css"
	href="../share/css/eformStyle.css">

<script type="text/javascript" language="JavaScript"
	src="../share/javascript/Oscar.js"></script>
</head>

<body onunload="updateAjax()" class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="175"><bean:message
			key="eform.showmyform.msgMyForm" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Add Mapping</td>
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
		<td class="MainTableLeftColumn" valign="top">
			</td>
		<td class="MainTableRightColumn" valign="top">
			<form method="post" action="<%=request.getContextPath() %>/hospitalReportManager/Mapping.do">
				Report class: <select name="class"><option value="Medical Records Report">Medical Records Report</option><option value="Diagnostic Imaging Report">Diagnostic Imaging Report</option><option value="Cardio Respiratory Report">Cardio Respiratory Report</option></select><br />
				Sub-class: <input type="text" name="subclass" /><br />  
				Sub-class mmenoic: <input type="text" name="mnemonic" /><br />
				Sub-class description: <input type="text" name="description" /><br />
				Sending Facility ID (* for all): <input type="text" name="sendingFacilityId" value="*" /><br /> 
				Category: <select name="category">
				<%
				HRMCategoryDao categoryDao = (HRMCategoryDao) SpringUtils.getBean("HRMCategoryDao");
				List<HRMCategory> categoryList = categoryDao.findAll();
				for (HRMCategory category : categoryList) {
				%>
				<option value="<%=category.getId() %>"><%=category.getCategoryName() %></option>
				<% } %>
				
				</select><br /><br />
				
				<input type="submit" name="submit" value="Save" />
				
			</form>
		</td>
	</tr>
</table>

</body>
</html:html>
