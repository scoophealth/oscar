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
	key="oscarEncounter.Measurements.msgDisplayMeasurementTypes" /></title>

<script type="text/javascript">
    function set(target) {
     document.forms[0].forward.value=target;
};
</script>
</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<html:errors />
<html:form
	action="/oscarEncounter/oscarMeasurements/DeleteMeasurementTypes">
	<table class="MainTable" id="scrollNumber1" name="encounterTable">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn"><bean:message
				key="oscarEncounter.Measurements.msgMeasurements" /></td>
			<td class="MainTableTopRowRightColumn">
			<table class="TopStatusBar">
				<tr>
					<td><bean:message
						key="oscarEncounter.Measurements.msgDisplayMeasurementTypes" /></td>
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
							<logic:present name="messages">
								<logic:iterate id="msg" name="messages">
									<bean:write name="msg" />
									<br>
								</logic:iterate>
							</logic:present>
						</tr>
						<tr>
							<td>
						<tr>
							<th align="left" class="Header" width="5"><bean:message
								key="oscarEncounter.oscarMeasurements.Measurements.headingType" />
							</th>
							<th align="left" class="Header" width="20"><bean:message
								key="oscarEncounter.oscarMeasurements.Measurements.headingDisplayName" />
							</th>
							<th align="left" class="Header" width="10"><bean:message
								key="oscarEncounter.oscarMeasurements.Measurements.headingTypeDesc" />
							</th>
							<th align="left" class="Header" width="300"><bean:message
								key="oscarEncounter.oscarMeasurements.Measurements.headingMeasuringInstrc" />
							</th>
							<th align="left" class="Header" width="300"><bean:message
								key="oscarEncounter.oscarMeasurements.Measurements.headingValidation" />
							</th>
							<th align="left" class="Header" width="10"><bean:message
								key="oscarEncounter.oscarMeasurements.MeasurementAction.headingDelete" />
							</th>
						</tr>
						<logic:iterate id="measurementType" name="measurementTypes"
							property="measurementTypeVector" indexId="ctr">
							<tr class="data">
								<td width="5"><a
									href="exportMeasurement.jsp?mType=<bean:write name="measurementType" property="type" />"
									target="_blank"> <bean:write name="measurementType"
									property="type" /> </a></td>
								<td width="20"><bean:write name="measurementType"
									property="typeDisplayName" /></td>
								<td width="10"><bean:write name="measurementType"
									property="typeDesc" /></td>
								<td width="300"><bean:write name="measurementType"
									property="measuringInstrc" /></td>
								<td width="300"><bean:write name="measurementType"
									property="validation" /></td>
								<td width="10"><input type="checkbox" name="deleteCheckbox"
									value="<bean:write name="measurementType" property="id" />"</td>
							</tr>
						</logic:iterate>
						<tr>
							<td><input type="button" name="Button"
								value="<bean:message key="global.btnClose"/>"
								onClick="window.close()"></td>
							<td><input type="button" name="Button"
								value="<bean:message key="oscarEncounter.oscarMeasurements.displayHistory.headingDelete"/>"
								onclick="submit();" /></td>
						</tr>

						</td>
						</tr>
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
