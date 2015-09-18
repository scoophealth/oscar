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
	key="oscarReport.RptByExample.MsgMyFavorites" /></title>

</head>
<script type="text/javascript">    
    function set(text1, text2) {
         document.forms[0].newQuery.value=text1;
         document.forms[0].newName.value=text2;
    };

    function confirmDelete(id) {        
        var answer = confirm("Are you sure you want to delete the selected query?");
        if(answer){
            document.forms[0].toDelete.value='true';
            document.forms[0].id.value=id;
            document.forms[0].submit();
        }
    };
    function closeAndRefresh(){
        self.opener.document.location.reload();
        self.close();
    }
</script>
<body vlink="#0000FF" class="BodyStyle">

<html:form action="/oscarReport/RptByExamplesFavorite.do">
	<table class="MainTable" id="scrollNumber1" name="encounterTable">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn"><bean:message
				key="oscarReport.CDMReport.msgReport" /></td>
			<td class="MainTableTopRowRightColumn">
			<table class="TopStatusBar">
				<tr>
					<td><bean:message
						key="oscarReport.RptByExample.MsgQueryByExamples" /> - <bean:message
						key="oscarReport.RptByExample.MsgMyFavorites" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableLeftColumn" valign="top"></td>
			<td class="MainTableRightColumn">
			<table>
				<tr class="Header">
					<td align="left" width="150"><bean:message
						key="oscarReport.RptByExample.MsgName" /></td>
					<td align="left" width="500"><bean:message
						key="oscarReport.RptByExample.MsgQuery" /></td>
					<td width="100"></td>
				</tr>
				<input type="hidden" name="newName" />
				<input type="hidden" name="newQuery" />
				<input type="hidden" name="toDelete" value="false" />
				<input type="hidden" name="id" value="error" />
				<logic:iterate id="favorite" name="allFavorites"
					property="favoriteVector">
					<tr class="data">
						<td><bean:write name="favorite" property="queryName" /></td>
						<td><bean:write name="favorite" property="query" /></td>
						<td><input type="button" name="editButton"
							value="<bean:message key='oscarReport.RptByExample.MsgEdit'/>"
							onClick="javascript:set('<bean:write name="favorite" property="queryWithEscapeChar"/>','<bean:write name="favorite" property="queryName"/>'); submit(); return false;" /><input
							type="button" name="deleteButton"
							value="<bean:message key='oscarReport.RptByExample.MsgDelete'/>"
							onClick="javascript:confirmDelete('<bean:write name="favorite" property="id"/>'); return false;" /></td>
						</td>
					</tr>
				</logic:iterate>
				<tr>
					<td><input type="button"
						value="<bean:message key='global.btnClose'/>"
						onClick="javascript:closeAndRefresh();" />
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
