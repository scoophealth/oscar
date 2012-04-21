
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
<%@ page import="org.caisi.model.*"%>
<%@ page import="java.util.Calendar,java.util.GregorianCalendar"%>
<%
	    GregorianCalendar now=new GregorianCalendar();
	    int curYear=now.get(Calendar.YEAR);
	    int curMonth=now.get(Calendar.MONTH);
	    int curDay=now.get(Calendar.DAY_OF_MONTH);
	%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<script>
	function openBrWindow(theURL,winName,features) { 
	  window.open(theURL,winName,features);
	}
	
	function validateRoleForm(form) {
		if(form.elements['role.name'].value == '') {
			alert('You must provide a name');
			return false;
		}
	}
	</script>

<title>Roles</title>

</head>
<body>
<div style="color: red"><%@ include file="messages.jsp"%>
</div>
<div style="overflow: auto; height: 480px; width: 480px;">
<table border="0" cellspacing="0" cellpadding="0" width="100%"
	bgcolor="#CCCCFF">
	<tr class="subject">
		<th colspan="4">Roles</th>
	</tr>
	<tr>
		<td class="searchTitle" colspan="3">Existing Role List</td>
		<td class="searchTitle" colspan="1">User Defined</td>
	</tr>

	<c:forEach var="role" items="${requestScope.roleList}">
		<tr>
			<td class="fieldValue" colspan="3"><c:out value="${role.name}" /></td>
			<td class="fieldValue"><c:out value="${role.userDefined}" /></td>
		</tr>

	</c:forEach>
	<tr>
		<td class="searchTitle" colspan="4">Create New Role</td>
	</tr>
</table>

<br>

<table width="60%" border="0" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0">
	<html:form action="/CaisiRole"
		onsubmit="return validateRoleForm(this);">
		<input type="hidden" name="method" value="save">
		<tr>
			<td class="fieldTitle">Name:</td>
			<td class="fieldValue"><html:text property="role.name" size="50"
				maxlength="255" /></td>
		</tr>
		<tr>
			<td class="fieldValue" colspan="2" align="left"><html:submit
				styleClass="button">Save</html:submit> <input type="button"
				value="close" onclick="self.close();" /></td>


		</tr>
	</html:form>
</table>
</div>
</body>
</html>
