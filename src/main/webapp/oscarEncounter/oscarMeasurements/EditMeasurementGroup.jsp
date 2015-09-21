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
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.measurements" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_admin.measurements");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@ page import="java.util.*,oscar.oscarReport.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.Measurements.msgEditMeasurementGroup" /></title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<script type="text/javascript">
    function set(target) {
     document.forms[0].forward.value=target;
};
</script>

</head>

<style>

select {
min-width:400px;
}

</style>

<body class="BodyStyle" vlink="#0000FF"
	onload="window.resizeTo(1000,500)">
<!--  -->
<html:errors />
<html:form
	action="/oscarEncounter/oscarMeasurements/EditMeasurementGroup.do">
	<table class="MainTable" id="scrollNumber1" name="encounterTable">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn"><bean:message
				key="oscarEncounter.Measurements.msgMeasurements" /></td>
			<td class="MainTableTopRowRightColumn">
			<table class="TopStatusBar">
				<tr>
					<td><bean:message
						key="oscarEncounter.Measurements.msgEditMeasurementGroup" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableLeftColumn"></td>
			<td class="MainTableRightColumn">
			<table border=0 cellspacing=4 width=800>
				<tr>
					<td>
					<table>
						<tr>
							<th align="left"><bean:message
								key="oscarEncounter.oscarMeasurements.MeasurementGroup.allTypes" />
							</th>

							<th align="left"><bean:write name="groupName" /></th>
						</tr>
						<tr>
							<td><bean:message
								key="oscarEncounter.oscarMeasurements.MeasurementGroup.add2Group" /><bean:write
								name="groupName" /></td>
							<td><bean:message
								key="oscarEncounter.oscarMeasurements.MeasurementGroup.deleteTypes" /><bean:write
								name="groupName" /></td>
						<tr>
							<td><html:select multiple="true" property="selectedAddTypes"
								size="10">
								<html:options collection="allTypeDisplayNames"
									property="typeDisplayName" labelProperty="typeDisplayName" />
							</html:select></td>
							<td><html:select multiple="true"
								property="selectedDeleteTypes" size="10">
								<html:options collection="existingTypeDisplayNames"
									property="typeDisplayName" labelProperty="typeDisplayName" />
							</html:select></td>
						</tr>
						<tr>
							<input type="hidden" name="forward" value="error" />
							<td><input type="button" name="button"
								value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.addBtn"/>"
								onclick="set('add');submit();" /></td>
							<td><input type="button" name="button"
								value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.deleteBtn"/>"
								onclick="set('delete');submit();" /></td>
						</tr>
						<tr>
							<td><input type="button" name="Button"
								value="<bean:message key="global.btnClose"/>"
								onClick="window.close()"></td>
							<td></td>
						</tr>
						<input type="hidden" name="groupName"
							value="<bean:write name="groupName"/>" />
					</table>
					</td>
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
