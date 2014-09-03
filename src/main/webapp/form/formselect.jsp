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

<%
  if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
%>
<%@ page import="java.util.*,oscar.oscarReport.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.admin.btnSelectForm" /></title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<script type="text/javascript">
    function set(target) {
     document.forms[0].forward.value=target;
};    
</script>

</head>

<body class="BodyStyle" vlink="#0000FF"
	onload="window.resizeTo(1000,500)">
<!--  -->
<html:form action="/form/select.do">
	<table class="MainTable" id="scrollNumber1" name="encounterTable">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn"><bean:message
				key="admin.admin.btnSelectForm" /></td>
			<td class="MainTableTopRowRightColumn" width="400">
			<table class="TopStatusBar">
				<tr>
					<td></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableLeftColumn"></td>
			<td class="MainTableRightColumn">
			<table border=0 cellspacing=4 width=400>
				<tr>
					<td>
					<table>
						<tr>
							<th align="left"><bean:message
								key="oscarEncounter.form.msgAllAvailableForms" /></th>
							<th></th>
							<th align="left"><bean:message
								key="oscarEncounter.form.msgSelectedForms" /></th>
						</tr>
						<td><html:select multiple="true" property="selectedAddTypes"
							size="10" style="width:200">
							<html:options collection="formHiddenVector" property="formName"
								labelProperty="formName" />
						</html:select></td>
						<td>
						<table>
							<tr>
								<td><input type="button" name="button" style='width: 75'
									value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.addBtn"/> >>"
									onclick="set('add');submit();" /></td>
							</tr>
							<tr>
								<td><input type="button" name="button" style='width: 75'
									value="<< <bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.deleteBtn"/>"
									onclick="set('delete');submit();" /></td>
							</tr>
						</table>
						</td>
						<td><html:select multiple="true"
							property="selectedDeleteTypes" size="10" style="width:200">
							<html:options collection="formShownVector" property="formName"
								labelProperty="formName" />
						</html:select></td>
						</tr>
						<tr>
							<input type="hidden" name="forward" value="error" />
						</tr>
						<tr>
							<td><input type="button" name="Button"
								value="<bean:message key="global.btnClose"/>"
								onClick="window.close()"></td>
							<td></td>
						</tr>
					</table>
					</td>
					<td><input type="button" name="button" style="width: 80"
						value="Move Up" onclick="set('up');submit();" /> <br>
					<input type="button" name="button" style="width: 80"
						value="Move Down" onclick="set('down');submit();" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableBottomRowLeftColumn"></td>
			<td class="MainTableBottomRowRightColumn"></td>
		</tr>
	</table>
</html:form>
</body>
</html:html>
