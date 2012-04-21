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
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.Measurements.msgDefineNewMeasurementGroup" /></title>

</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<html:errors />
<html:form
	action="/oscarEncounter/oscarMeasurements/DefineNewMeasurementGroup.do">
	<table class="MainTable" id="scrollNumber1" name="encounterTable">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn"><bean:message
				key="oscarEncounter.Measurements.msgMeasurements" /></td>
			<td class="MainTableTopRowRightColumn" width="400">
			<table class="TopStatusBar">
				<tr>
					<td><bean:message
						key="oscarEncounter.Measurements.msgDefineNewMeasurementGroup" /></td>
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
							<td>
						<tr>
							<td align="left"><bean:message
								key="oscarEncounter.oscarMeasurements.addMeasurementGroup.createNewMeasurementGroupName" />
							</td>
						</tr>
						<tr>
							<td><html:text property="groupName" size="35" /></td>
						</tr>
						<tr>
							<td align="left"><bean:message
								key="oscarEncounter.oscarMeasurements.addMeasurementGroup.selectStyleSheet" />
							</td>
						</tr>
						<tr>
							<td><html:select property="styleSheet" style="width:250">
								<html:options collection="allStyleSheets" property="cssId"
									labelProperty="styleSheetName" />
							</html:select></td>
						</tr>
						<tr>
							<td>
							<table>
								<tr>
									<td><input type="button" name="Button"
										value="<bean:message key="global.btnClose"/>"
										onClick="window.close()"></td>
									<td><input type="button" name="Button"
										value="<bean:message key="oscarEncounter.oscarMeasurements.MeasurementsAction.continueBtn"/>"
										onclick="submit();" /></td>
								</tr>
							</table>
							</td>
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
