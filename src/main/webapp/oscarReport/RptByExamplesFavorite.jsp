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
<script language="JavaScript" type="text/JavaScript">

</script>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarReport.RptByExample.MsgQueryByExamples" /> - <bean:message
	key="oscarReport.RptByExample.MsgEditMyFavorite" /></title>
</head>

<body vlink="#0000FF" class="BodyStyle">
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<html:form action="/oscarReport/RptByExamplesFavorite.do">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn"><bean:message
				key="oscarReport.CDMReport.msgReport" /></td>
			<td class="MainTableTopRowRightColumn">
			<table class="TopStatusBar">
				<tr>
					<td><bean:message
						key="oscarReport.RptByExample.MsgQueryByExamples" /> - <bean:message
						key="oscarReport.RptByExample.MsgEditMyFavorite" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableLeftColumn" valign="top"></td>
			<td class="MainTableRightColumn">
			<table>
				<tr>
					<td><html:text property="favoriteName" size="40" /></td>
				</tr>
				<tr>
					<td><html:textarea property="query" cols="80" rows="3" /></td>
				</tr>
				<tr>
					<td><input type="button" value="Add" onclick="submit();" /> <input
						type="button"
						value="<bean:message key='oscarReport.RptByExample.MsgCancel'/>"
						onclick="javascript:history.back(1);" /></td>
				</tr>
				<tr></tr>

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
