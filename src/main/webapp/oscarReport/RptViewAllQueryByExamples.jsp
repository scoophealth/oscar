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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.util.*,oscar.oscarReport.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">
<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarReport.RptByExample.MsgQueryByExamples" /> - <bean:message
	key="oscarReport.RptByExample.MsgAllQueriesExecuted" /></title>

<script type="text/javascript">
    function set(text) {
         document.forms[1].newQuery.value=text;
    };
</script>

</head>

<body vlink="#0000FF" class="BodyStyle">

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="oscarReport.CDMReport.msgReport" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<html:form action="/oscarReport/RptViewAllQueryByExamples.do">
				<tr>
					<td><bean:message
						key="oscarReport.RptByExample.MsgAllQueriesExecutedFrom" />: <html:text
						property="startDate" size="8" /> <bean:message
						key="oscarReport.RptByExample.MsgTo" /> <html:text
						property="endDate" size="8" /> <input type="submit"
						value="Refresh" /></td>
				</tr>
			</html:form>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top"></td>
		<td class="MainTableRightColumn">
		<table>
			<tr class="Header">
				<td align="left" width="140"><bean:message
					key="oscarReport.RptByExample.MsgDate" /></td>
				<td align="left" width="400"><bean:message
					key="oscarReport.RptByExample.MsgQuery" /></td>
				<td align="left" width="100"><bean:message
					key="oscarReport.RptByExample.MsgProvider" /></td>
				<td></td>
			</tr>

			<html:form action="/oscarReport/RptByExamplesFavorite.do">
				<input type="hidden" name="newQuery" value="error" />
				<logic:iterate id="queryInfo" name="allQueries"
					property="queryVector">
					<tr class="data">
						<td><bean:write name="queryInfo" property="date" /></td>
						<td><bean:write name="queryInfo" property="query" /></td>
						<td><bean:write name="queryInfo" property="providerLastName" />,
						<bean:write name="queryInfo" property="providerFirstName" /></td>
						<td><input type="button"
							value="<bean:message key='oscarReport.RptByExample.MsgAddToFavorite'/>"
							onclick="javascript:set('<bean:write name="queryInfo" property="queryWithEscapeChar"/>'); submit();" /></td>
					</tr>
				</logic:iterate>
			</html:form>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>

</body>
</html:html>
