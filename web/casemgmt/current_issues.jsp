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

<%@ include file="/casemgmt/taglibs.jsp" %>

<%@ page import="org.oscarehr.casemgmt.model.*" %>
<%@ page import="org.oscarehr.casemgmt.web.formbeans.*" %>
<%@page import="org.oscarehr.PMmodule.service.IntegratorManager"%>
<%@page import="org.oscarehr.util.SpringUtils"%>


Issue Filter Create Report View:

<table width="100%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
	<tr class="title">
		<td></td>
		<td>Issue</td>
		<td>Acute</td>
		<td>Certain</td>
		<td>Major</td>
		<td>Resolved</td>
		<td>type</td>
	</tr>
	<%int index=0; String bgcolor="white"; %>
	<c:forEach var="issue" items="${Issues}">
		<%	
			
			if(index++%2!=0) {
				bgcolor="white";
			} else {
				bgcolor="#EEEEFF";
			}			
		%>
		
		<tr bgcolor="<%=bgcolor %>" align="center">
			<%
				String checked="";
				CaseManagementIssue issue = (CaseManagementIssue)pageContext.getAttribute("issue");
				if(issue == null) {
					System.out.println("issue=null");	
				}
				String issue_id = String.valueOf(issue.getIssue_id());
				
				String set_issues[] = (String[])request.getAttribute("checked_issues");
				if(set_issues != null) {
					for(int x=0;x<set_issues.length;x++) {
						if(set_issues[x].equals(issue_id)) {
							checked="CHECKED";
						}
					}
				}
				String priority = "";				
				if("allergy".equals(issue.getIssue().getPriority()))
					priority="yellow";
			%>
			<td><input type="checkbox" name="check_issue" value="<c:out value="${issue.issue_id}"/>" <%=checked %> onclick="document.caseManagementViewForm.submit();"/></td>
			<td bgcolor=<%=priority%>><c:out value="${issue.issue.description }"/></td>
			<td><c:if test="${issue.acute=='true'}">acute</c:if>
			<c:if test="${issue.acute=='false'}">chronic</c:if>
			</td>
			<td><c:if test="${issue.certain=='true'}">certain</c:if>
			<c:if test="${issue.certain=='false'}">uncertain</c:if>
			</td>
			<td><c:if test="${issue.major=='true'}">major</c:if>
			<c:if test="${issue.major=='false'}">not major</c:if>
			</td>
			<td><c:if test="${issue.resolved=='true'}">resolved</c:if>
			<c:if test="${issue.resolved=='false'}">unresolved</c:if>
			</td>
			<td><c:out value="${issue.type }"/></td>
		</tr>
	</c:forEach>
</table>
<logic:equal name="caseManagementViewForm" property="hideActiveIssue" value="true">
	<span style="text-decoration: underline;cursor:pointer;color: blue" onclick="document.caseManagementViewForm.hideActiveIssue.value='false';document.caseManagementViewForm.method.value='setHideActiveIssues';document.caseManagementViewForm.submit(); return false;" >show resolved issues</span>
</logic:equal>
<logic:notEqual name="caseManagementViewForm" property="hideActiveIssue" value="true">
	<span style="text-decoration: underline;cursor:pointer;color: blue" onclick="document.caseManagementViewForm.hideActiveIssue.value='true';document.caseManagementViewForm.method.value='setHideActiveIssues';document.caseManagementViewForm.submit(); return false;" >hide resolved issues</span>
</logic:notEqual>

<%
	IntegratorManager integratorManager=(IntegratorManager)SpringUtils.beanFactory.getBean("integratorManager");
	if (integratorManager.isEnabled())
	{
	%>
		<br />
		<a id="showRemoteIssuesLink" href="" onclick="document.getElementById('remoteIssues').style.display='block';document.getElementById('showRemoteIssuesLink').style.display='none';document.getElementById('hideRemoteIssuesLink').style.display='inline';return(false);" >show issues from other agencies</a>
		<a id="hideRemoteIssuesLink" href="" style="display:none" onclick="document.getElementById('remoteIssues').style.display='none'; document.getElementById('showRemoteIssuesLink').style.display='inline';document.getElementById('hideRemoteIssuesLink').style.display='none';return(false);" >hide issues from other agencies</a>
		<br />
		<table id="remoteIssues" style="display:none" width="100%" border="0"  cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
			<tr class="title">
				<td>Agency</td>
				<td>Issue</td>
				<td>Acute</td>
				<td>Certain</td>
				<td>Major</td>
				<td>Resolved</td>
				<td>type</td>
			</tr>
		</table>
		
	<%
	}
%>
