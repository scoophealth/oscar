<%@ include file="/casemgmt/taglibs.jsp" %>

<%@ page import="org.oscarehr.casemgmt.model.*" %>
<%@ page import="org.oscarehr.casemgmt.web.formbeans.*" %>


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

			%>
			<td><input type="checkbox" name="check_issue" value="<c:out value="${issue.issue_id}"/>" <%=checked %> onclick="document.caseManagementViewForm.submit();"/></td>
			<td><c:out value="${issue.issue.description }"/></td>
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