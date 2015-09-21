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
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.oscarEncounter.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.bean.*"%>
<%@ page import="java.util.Vector"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_flowsheet" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_flowsheet");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%
    String demo = ((Integer) request.getAttribute("demographicNo")).toString();	
%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscarEncounter.Index.oldMeasurements" />
</title>
<html:base />

</head>


<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<script type="text/javascript" language=javascript>
    
</script>
<body topmargin="0" leftmargin="0" vlink="#0000FF"
	onload="window.focus();">
<html:errors />
<html:form action="/oscarEncounter/oscarMeasurements/DeleteData">
	<table>
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
				<td class="Header" colspan="9"><oscar:nameage
					demographicNo="<%=demo%>" /></td>
				<tr>
				</tr>
				<tr>
					<td>
				<tr>
					<logic:present name="data" property="canPlot">
						<th align="left" class="Header" width="5"></th>
						<th align="left" class="Header" width="5"><bean:message
							key="oscarEncounter.oscarMeasurements.displayHistory.headingType" />
						</th>
					</logic:present>
					<logic:notPresent name="data" property="canPlot">
						<th colspan="2" align="left" class="Header" width="5"><bean:message
							key="oscarEncounter.oscarMeasurements.displayHistory.headingType" />
						</th>
					</logic:notPresent>
					<th align="left" class="Header" width="20"><bean:message
						key="oscarEncounter.oscarMeasurements.displayHistory.headingProvider" />
					</th>
					<th align="left" class="Header" width="200"><bean:message
						key="oscarEncounter.oscarMeasurements.displayHistory.headingMeasuringInstruction" />
					</th>
					<th align="left" class="Header" width="10"><bean:message
						key="oscarEncounter.oscarMeasurements.displayHistory.headingData" />
					</th>
					<th align="left" class="Header" width="300"><bean:message
						key="oscarEncounter.oscarMeasurements.displayHistory.headingComments" />
					</th>
					<th align="left" class="Header" width="150"><bean:message
						key="oscarEncounter.oscarMeasurements.displayHistory.headingObservationDate" />
					</th>
					<th align="left" class="Header" width="150"><bean:message
						key="oscarEncounter.oscarMeasurements.displayHistory.headingDateEntered" />
					</th>
					<security:oscarSec roleName="<%=roleName$%>" objectName="_flowsheet" rights="w">
					<th align="left" class="Header" width="10"><bean:message
						key="oscarEncounter.oscarMeasurements.displayHistory.headingDelete" />
					</th>
					</security:oscarSec> 
					
				</tr>
				<logic:iterate id="data" name="measurementsData" property="measurementsDataVector" indexId="ctr">
					<logic:present name="data" property="remoteFacility">
						<tr class="data" style="background-color:#ffcccc" >
					</logic:present>
					<logic:notPresent name="data" property="remoteFacility">
					    <tr class="data" >
					</logic:notPresent>
						<logic:present name="data" property="canPlot">
							<td width="5">
								<img src="img/chart.gif" title="<bean:message key="oscarEncounter.oscarMeasurements.displayHistory.plot"/>"
								onclick="window.open('../../servlet/oscar.oscarEncounter.oscarMeasurements.pageUtil.ScatterPlotChartServlet?type=<bean:write name="data" property="type"/>&mInstrc=<bean:write name="data" property="measuringInstrc"/>')" />
							</td>
							<td width="5">
								<a title="<bean:write name="data" property="typeDescription" />"><bean:write name="data" property="type" /></a>
							</td>
						</logic:present>
						<logic:notPresent name="data" property="canPlot">
							<td width="5" colspan=2>
								<a title="<bean:write name="data" property="typeDescription" />">
								<bean:write name="data" property="type" /></a>
							</td>
						</logic:notPresent>		
							<td width="20">
								<bean:write name="data" property="providerFirstName" /> 
								<bean:write name="data" property="providerLastName" />
								<logic:present name="data" property="remoteFacility">
									<br /><span style="color:#990000"> @: <bean:write name="data" property="remoteFacility" /><span>
								</logic:present>
							</td>
							<td width="200"><bean:write name="data"	property="measuringInstrc" /></td>
							<td width="10"><bean:write name="data" property="dataField" /></td>
							<td width="300"><bean:write name="data" property="comments" /></td>
							<td width="150"><bean:write name="data" property="dateObservedAsDate" format="yyyy-MM-dd" /></td>
							<td width="150"><bean:write name="data" property="dateEnteredAsDate" format="yyyy-MM-dd"/></td>
							<security:oscarSec roleName="<%=roleName$%>" objectName="_flowsheet" rights="w">
							<td width="10">
							<logic:present name="data" property="remoteFacility">
					    		&nbsp;
							</logic:present>
							<logic:notPresent name="data" property="remoteFacility">
					    		<input type="checkbox" name="deleteCheckbox" value="<bean:write name="data" property="id" />">
							</logic:notPresent>
                              
                            </td>
                            </security:oscarSec>
						</tr>					
					</logic:iterate>
				</td>
				</tr>
			</table>
			<table>
				<tr>
					<td><input type="button" name="Button"
						value="List Old Measurements Index"
						onClick="javascript: popupPage(300,800,'SetupHistoryIndex.do')"></td>
					<td><input type="button" name="Button"
						value="<bean:message key="global.btnPrint"/>"
						onClick="window.print()"></td>
					<td><input type="button" name="Button"
						value="<bean:message key="global.btnClose"/>"
						onClick="window.close()"></td>
					<security:oscarSec roleName="<%=roleName$%>" objectName="_flowsheet" rights="w">
					<td><input type="button" name="Button"
						value="<bean:message key="oscarEncounter.oscarMeasurements.displayHistory.headingDelete"/>"
						onclick="submit();" /></td>
					</security:oscarSec>
					<logic:present name="data" property="canPlot">
						<td><input type="button" name="Button" value="Graph"
							onClick="javascript: popupPage(300,800,'../../oscarEncounter/GraphMeasurements.do?demographic_no=<%=demo%>&type=<bean:write name="type" />')" />
						</td>
					</logic:present>
					<logic:present name="type">
						<input type="hidden" name="type"
							value="<bean:write name="type" />" />
					</logic:present>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>
</body>
</html:html>
