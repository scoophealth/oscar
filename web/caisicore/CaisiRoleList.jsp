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

<%@ include file="/taglibs.jsp"%>
<%@ page import="org.caisi.model.*"%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Roles</title>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />


<script>
		function openBrWindow(theURL,winName,features) { 
		  window.open(theURL,winName,features);
		}
		
		function Check(e) {
			e.checked = true;
		}
		
		function Clear(e) {
			e.checked = false;
		}
		    
		function CheckAll() {
			var ml = document.ticklerForm;
			var len = ml.elements.length;
			for (var i = 0; i < len; i++) {
			    var e = ml.elements[i];
			    if (e.name == "checkbox") {
					Check(e);
			    }
			}
		}
		
		function ClearAll() {
			var ml = document.ticklerForm;
			var len = ml.elements.length;
			for (var i = 0; i < len; i++) {
			    var e = ml.elements[i];
			    if (e.name == "checkbox") {
				Clear(e);
			    }
			}
		}
	</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="1" width="100%"
	bgcolor="#CCCCFF">
	<tr class="subject">
		<th colspan="4">Roles</th>
	</tr>

	<br />
	<%@ include file="messages.jsp"%>
	<br />

	<form name="caisiRoleListForm" action="CaisiRoleAssigner.do"
		method="post"><input type="hidden" name="change_provider"
		value="0" /> <input type="hidden" name="method" value="assign" />
	<table width="50%" border="0" cellpadding="0" cellspacing="1"
		bgcolor="#C0C0C0">
		<tr class="title">
			<th>Provider Name</th>
			<th>Role</th>
		</tr>

		<tr>
			<%int index=0; 
			String bgcolor;
		%>
			<c:forEach var="provider" items="${providers}">
				<%
				if(index++%2!=0) {
					bgcolor="white";
				} else {
					bgcolor="#EEEEFF";
				}
			%>
				<tr bgcolor="<%=bgcolor %>" align="center">
					<td><c:out value="${provider.formattedName}" /></td>
					<td><select
						name="select_<c:out value="${provider.provider_no}"/>"
						onchange="this.form.change_provider.value='<c:out value="${provider.provider_no}"/>';this.form.submit();">
						<option value="0"></option>
						<c:forEach var="role" items="${roles}">
							<%String selected="";
								Role role = (Role)pageContext.getAttribute("role");
								Provider p = (Provider)pageContext.getAttribute("provider");
								if(p != null && p.getRole() != null) {
									if((long)p.getRole().getRole_id() == role.getId().longValue()) {
										selected="selected";
									}
								}
							%>

							<option value="<c:out value="${role.id}"/>" <%=selected %>><c:out
								value="${role.name}" /></option>

						</c:forEach>
					</select></td>
				</tr>
			</c:forEach>
		</tr>
	</table>
	</form>
	<br />
	<input type="button" value="Add new Role"
		onclick="location.href='CaisiRole.do'" />
</body>
</html>