
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>



<%@ include file="/casemgmt/taglibs.jsp"%>

<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.issues" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_casemgmt.issues");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="org.oscarehr.casemgmt.model.*"%>
<%@ page import="org.oscarehr.casemgmt.web.formbeans.*"%>
<%@page import="org.oscarehr.casemgmt.web.CaseManagementViewAction"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.Map"%>
<%@page import="org.oscarehr.casemgmt.web.CheckBoxBean"%>


<%
	Map dxMap = (Map)request.getAttribute("dxMap");
%>
<b>Issue Filter Create Report View:</b>

<table width="100%" border="0" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0">
	<tr class="title">
		<td></td>
		<td>Code</td>
		<td>Issue</td>
		<td>Location</td>
		<td>Acute</td>
		<td>Certain</td>
		<td>Major</td>
		<td>Resolved</td>
		<td>type</td>
		<td></td>
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
				CheckBoxBean checkBoxBean= (CheckBoxBean)pageContext.getAttribute("issue");
				CaseManagementViewAction.IssueDisplay issue = checkBoxBean.getIssueDisplay();
				String issueCompositeId=issue.codeType+"."+issue.code;
				String[] checkedIssues=request.getParameterValues("check_issue");
				if(checkedIssues != null) {
					for(int x=0;x<checkedIssues.length;x++) {
						if(checkedIssues[x].equals(issueCompositeId)) {
							checked="CHECKED";
						}
					}
				}
				String priority = "";				
				if("allergy".equals(issue.priority))
					priority="yellow";
			%>
			<td>
				<input type="checkbox" name="check_issue" value="<%=issueCompositeId%>" <%=checked%> onclick="document.caseManagementViewForm.submit();" />
			</td>
			<td><c:out value="${issue.issueDisplay.code}" /></td>
			<td bgcolor="<%=priority%>"><c:out value="${issue.issueDisplay.description }" /></td>
			<td><c:out value="${issue.issueDisplay.location}" /></td>
			<td><c:out value="${issue.issueDisplay.acute}" /></td>
			<td><c:out value="${issue.issueDisplay.certain}" /></td>
			<td><c:out value="${issue.issueDisplay.major}" /></td>
			<td><c:out value="${issue.issueDisplay.resolved}" /></td>
			<td><c:out value="${issue.issueDisplay.role}" /></td>
			<td>
			<%
					if("major".equals(issue.major)) {
						if(dxMap.get(issue.code) != null) {
							%>Dx<%
						} else {
							%><span
				style="text-decoration: underline; cursor: pointer; color: blue"
				onclick="document.caseManagementViewForm.hideActiveIssue.value='false';document.caseManagementViewForm.method.value='addToDx';document.caseManagementViewForm.issue_code.value='<c:out value="${issue.issueDisplay.code}"/>';document.caseManagementViewForm.submit(); return false;">+Dx</span>
			<%
						}
					}
				%>
			</td>
		</tr>
	</c:forEach>
</table>
<logic:equal name="caseManagementViewForm" property="hideActiveIssue"
	value="true">
	<span style="text-decoration: underline; cursor: pointer; color: blue"
		onclick="document.caseManagementViewForm.hideActiveIssue.value='false';document.caseManagementViewForm.method.value='setHideActiveIssues';document.caseManagementViewForm.submit(); return false;">show
	resolved issues</span>
</logic:equal>
<logic:notEqual name="caseManagementViewForm" property="hideActiveIssue"
	value="true">
	<span style="text-decoration: underline; cursor: pointer; color: blue"
		onclick="document.caseManagementViewForm.hideActiveIssue.value='true';document.caseManagementViewForm.method.value='setHideActiveIssues';document.caseManagementViewForm.submit(); return false;">hide
	resolved issues</span>
</logic:notEqual>

