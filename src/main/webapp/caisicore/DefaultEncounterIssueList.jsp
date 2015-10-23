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
<%@ include file="/taglibs.jsp"%>


<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="java.util.List,org.caisi.model.DefaultIssue" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.caisi.model.DefaultIssue" %>
<%@page import="org.oscarehr.casemgmt.dao.IssueDAO" %>
<%@page import="org.oscarehr.casemgmt.model.Issue" %>
<%@page import="java.lang.NumberFormatException" %>
<%@page import="java.lang.Exception" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="org.oscarehr.common.model.Provider" %>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<title>Please assign default encounter issues</title>
</head>
<%
	int colNum = 5;
%>
<body>
<table border="0" cellspacing="0" cellpadding="1" width="100%"
	bgcolor="#CCCCFF">
	<tr class="subject">
		<th colspan="<%=colNum %>">CAISI</th>
	</tr>
	<tr>
		<td class="searchTitle" colspan="<%=colNum %>">Please assign default encounter issues</td>
	</tr>
</table>

<br />

<%@ include file="messages.jsp"%>

<br />
<table width="100%" border="0" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0">
	<tr class="title">
		<td width="25%">Assigned Date</td>
		<td width="25%">Lastname</td>
		<td width="25%">Firstname</td>
		<td width="25%">Update Date</td>
		<td width="25%">Issues</td>
	</tr>

	<%
//1.get list object
//2.traverse the list and get issue name list by issueCodes
//3.output the record to the page
	boolean flag = true;
	String style="color:black;", bgcolor="white";
	IssueDAO issueDao = SpringUtils.getBean(IssueDAO.class);
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	List<DefaultIssue> issueList = (List<DefaultIssue>)request.getAttribute("issueList");
	if (issueList != null) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (DefaultIssue defaultIssue : issueList) {
			bgcolor=flag? "#EEEEFF" : "white";
			flag = !flag;
			String[] issueIds = defaultIssue.getIssueIds().split(",");
			if (issueIds.length == 0) continue;
			StringBuilder sb = new StringBuilder();
			for (int i=0; i < issueIds.length; i++) {
				try {
					Long issueId = Long.valueOf(issueIds[i]);
					Issue issue = issueDao.getIssue(issueId);
					sb.append(issue.getDescription());
					if (i != (issueIds.length - 1)) {
						sb.append(",");
					}
				} catch(NumberFormatException e) {
					continue;
				} catch (Exception e) {
					continue;
				}
			}
			Provider provider = providerDao.getProvider(defaultIssue.getProviderNo());
			out.print("<tr style="+style+" bgcolor=" + bgcolor + ">");
			out.print("<td>"+sdf.format(defaultIssue.getAssignedtime())+"</td>");
			out.print("<td>"+provider.getLastName()+"</td>");
			out.print("<td>"+provider.getFirstName()+"</td>");
			out.print("<td>"+sdf.format(defaultIssue.getUpdatetime())+"</td>");
			out.print("<td>"+sb.toString()+"</td>");
			out.print("</tr>");

		}
	} else {
		out.print("<tr style="+style+" bgcolor=" + bgcolor + ">");
		out.print("<td align=\"center\" colspan=\"" + colNum + "\" width=\"100%\" height=\"30px\">" + "There's no default issues!" + "</td>");
		out.print("</tr>");
	}
	
%>
	
</table>

<br />

<table>
	<tr>
		<td><input type="button" value="Back"
			onclick="location.href='<%=request.getContextPath()%>/admin/admin.jsp'" /></td>
		<td><input type="button" value="Assign default issues"
			onclick="location.href='<%=request.getContextPath()%>/DefaultEncounterIssue.do?method=edit'" /></td>
		<%
		if (issueList != null){
		%>
			<td><input type="button" value="Remove default issues" onclick="location.href='<%=request.getContextPath()%>/DefaultEncounterIssue.do?method=editRemove'");
		<%
		}
		%>
	
	</tr>
</table>

</body>
</html>