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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<%@page import="java.util.*"%>
<%@page import="org.oscarehr.common.model.Episode" %>
<%@page import="org.oscarehr.common.dao.EpisodeDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>

<%

%>
<html:html locale="true">
<head>
<script src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

<title>Completion of Pregnancy</title>


<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/css/OscarStandardLayout.css">
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/share/javascript/Oscar.js"></script>
<script src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">
<style>
div#demo
{
	margin-left: auto;
	margin-right: auto;
	width: 90%;
	text-align: left;
}
</style>
<script>
	$(document).ready(function(){
		$("#endDate").datepicker({ dateFormat: "yy-mm-dd" });
		<%
			if(request.getAttribute("close") != null) {
				%>
				window.opener.reloadNav('pregnancy');
				window.close();				
				<%				
			}
		%>
	});
	
	function validate() {
		patt1=new RegExp("^\\d{4}\\-\\d{2}\\-\\d{2}$");
		if(!patt1.test($("#endDate").val())) {
			alert("Please enter a completion date.");
			return false;
		}
		return true;
	}
</script>
</head>

<body>

	<br/>
	<h2 style="text-align:center">Pregnancy Management</h2>
	<br/>
<%
	if(request.getAttribute("error") != null) {
%>
	<h2 style="color:red"><%=request.getAttribute("error") %></h2>
<% 
	return;
	} 
%>

	<form action="Pregnancy.do">
		<fieldset>
			<h5>Set the date of completion to close this episode</h5>
			<table>
				<tr>
					<td><b>Completion Date:</b></td>
					<td>
						<input type="hidden" name="method" value="doComplete"/>
						<input type="hidden" name="episodeId" value="<%=request.getParameter("episodeId")%>"/>
						<input id="endDate" name="endDate" type="text"/>
					</td>
				</tr>
				<tr>
					<td><b>Notes:</b></td>
					<td>
						<textarea id="notes" name="notes" cols="40" rows="4"></textarea>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="submit" value="Submit" onclick="return validate();"/>
					</td>
				</tr>
			</table>
		</fieldset>
	</form>

	<br/>


	<form action="Pregnancy.do">
		<fieldset>
			<h5>Delete this episode. Use this if the pregnancy was created in error.</h5>
			<table>
				<tr>
					<td><b>Notes:</b></td>
					<td>
						<input type="hidden" name="method" value="doDelete"/>
						<input type="hidden" name="episodeId" value="<%=request.getParameter("episodeId")%>"/>
						<textarea id="notes" name="notes" cols="40" rows="4"></textarea>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="submit" value="Submit" onclick="return validate();"/>
					</td>
				</tr>
			</table>
		</fieldset>
	</form>

</html:html>
