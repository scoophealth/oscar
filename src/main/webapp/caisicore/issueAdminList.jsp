
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



<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>
<title>MyIssues ~ Issue List</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<button onclick="location.href='issueAdmin.do?method=edit'">Add
Issue</button> 

&nbsp;

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script>
function archiveIssues() {
	var ids = "";
	$("input:checked").each(function () {
       var id = $(this).val();
       ids = ids + "," + id;
    });
	if(ids.length > 0) {
		ids = ids.substring(1);
	}

	$.ajax({
        url: '<%=request.getContextPath()%>/issueAdmin.do?method=archiveIssues&ids=' + ids,
        method: 'GET',
        success: function(returnData){
            window.location.reload();
        }
    });

	
}
</script>

<button onclick="archiveIssues()">Archive Checked Issue(s)</button>

<table border="0" cellpadding="0" cellspacing="1" bgcolor="#C0C0C0">
	<thead>
		<tr class="title">
			<!--
    <th><bean:message key="issueAdmin.id"/></th>
-->
			<th></th>
			<th><bean:message key="issueAdmin.code" /></th>
			<th><bean:message key="issueAdmin.description" /></th>
			<th><bean:message key="issueAdmin.role" /></th>
			<!-- 
    <th><bean:message key="issueAdmin.update_date"/></th>
-->
		</tr>
	</thead>
	<tbody>
		<c:forEach var="issueAdmin" items="${issueAdmins}" varStatus="status">
			<c:choose>
				<c:when test="${status.count % 2 == 0}">
					<tr class="even">
				</c:when>
				<c:otherwise>
					<tr class="odd">
				</c:otherwise>
			</c:choose>
			<!--Not allow to edit issue
    <td><a href="issueAdmin.do?method=edit&amp;id=<c:out value="${issueAdmin.id}"/>"><c:out value="${issueAdmin.code}"/></a></td>
-->
			<td><input type="checkbox" name="checkedIssue" value="<c:out value="${issueAdmin.id}" />"/></td>
			<td><c:out value="${issueAdmin.code}" /></a></td>
			<td><c:out value="${issueAdmin.description}" /></td>
			<td><c:out value="${issueAdmin.role}" /></td>
			<!--
    <td><c:out value="${issueAdmin.update_date}"/></td>
-->
			</tr>
		</c:forEach>
	</tbody>
</table>
