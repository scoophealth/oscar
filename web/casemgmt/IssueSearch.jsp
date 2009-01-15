<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%-- Updated by Eugene Petruhin on 08 jan 2009 while fixing #2482832 & #2494061 --%>

<%@ include file="/casemgmt/taglibs.jsp" %>

<%
    String demographicNo = request.getParameter("demographicNo");
    String sessionFrmName = "caseManagementEntryForm" + demographicNo;
    org.oscarehr.casemgmt.web.formbeans.CaseManagementEntryFormBean form=(org.oscarehr.casemgmt.web.formbeans.CaseManagementEntryFormBean) session.getAttribute(sessionFrmName);
    pageContext.setAttribute("caseManagementEntryForm", form);
 %>

<html>
<head>
	<title>Issue Search</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>	
	<link rel="stylesheet" href="<c:out value="${ctx}"/>/css/casemgmt.css" type="text/css">
</head>

<body>
<nested:form action="/CaseManagementEntry">
<c:url value="/casemgmt/CaseManagementEntry.jsp?demographicNo=${param.demographicNo}&providerNo=${param.providerNo}&demoName=${requestScope.demoName}&demoAge=${requestScope.demoAge}&demoDOB=${requestScope.demoDOB}" var="url" />
<script type="text/javascript">
function backToNote(from)
{
	
	if (from==null)	location.href="<c:out value='${url}' escapeXml='false'/>";
	else location.href="<c:out value='${url}' escapeXml='false'/>"+"&from="+from;
	return false;
}
</script>
<html:hidden property="demographicNo"/>
<html:hidden property="providerNo"/>
<input type="hidden" name="method" value="issueSearch"/>
<input type="hidden" name="lastPage" value="true"/>
<input type="hidden" name="change_diagnosis" value="<c:out value="${change_diagnosis}"/>"/>
<input type="hidden" name="change_diagnosis_id" value="<c:out value="${change_diagnosis_id}"/>"/>

<c:if test="${param.from=='casemgmt'||requestScope.from=='casemgmt'}">
<input type="hidden" name="from" value="casemgmt" />
</c:if>

<b>Client name: <I><c:out value="${requestScope.demoName}" /></I></b>
<br><br>

<P><b>Search the Issue </b></P>
<nested:text property="searString"></nested:text>
<nested:submit value="search" onclick="this.form.method.value='issueSearch';"/>
 
<nested:equal property="showList" value="true">
<P><b>Issue List</b></P>
<table width="100%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
	<tr class="title">
		<td></td>
		<td>Issue Code</td>
		<td>Issue Description</td>
		<td>Issue Role</td>
	</tr>


<nested:iterate indexId="ind" id="newIssueCheckList" property="newIssueCheckList" type="org.oscarehr.casemgmt.web.CheckIssueBoxBean">
<tr bgcolor="<%= (ind.intValue()%2==0)?"#EEEEFF":"white" %>" align="center">
<td><nested:checkbox indexed="true" name="newIssueCheckList" property="checked"></nested:checkbox>
<td><nested:write name="newIssueCheckList" property="issue.code"/></td>
<logic:equal name="newIssueCheckList" property="issue.priority" value="allergy">
	<td bgcolor="yellow"><nested:write name="newIssueCheckList" property="issue.description"/></td>
</logic:equal>
<logic:notEqual name="newIssueCheckList" property="issue.priority" value="allergy">
	<td><nested:write name="newIssueCheckList" property="issue.description"/></td>
</logic:notEqual>
<td><nested:write name="newIssueCheckList" property="issue.role"/></td>
</tr>
</nested:iterate>
</table>
<br>
<nested:submit value="add checked issue" onclick="this.form.method.value='issueAdd';"/>

</nested:equal>

<logic:equal name="from" value="casemgmt" scope="request">
<nested:submit value="back to notes" onclick="this.form.method.value='edit';backToNote('casemgmt');return false;"/>
</logic:equal>
<logic:notEqual name="from" value="casemgmt" scope="request">
<nested:submit value="back to notes" onclick="this.form.method.value='edit';backToNote(); return false;"/>
</logic:notEqual>

</nested:form>
</body>
</html>