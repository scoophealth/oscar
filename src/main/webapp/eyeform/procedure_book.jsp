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

<%@page import="org.oscarehr.eyeform.model.EyeformProcedureBook"%>
<%@page import="org.oscarehr.eyeform.web.ProcedureBookAction"%>


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

 
<html>
<head>
	<title></title>
	<link rel="stylesheet" type="text/css" href='<html:rewrite page="/jsCalendar/skins/aqua/theme.css" />' />

		<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/calendar/calendar.css" title="win2k-cold-1" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar-setup.js"></script>
		
</head>
<body>
Book Procedure
<br />

<html:form action="/eyeform/ProcedureBook.do">
	<table style="margin-left:auto;margin-right:auto;background-color:#f0f0f0;border-collapse:collapse">
		<input type="hidden" name="method" value="save"/>
				
		<html:hidden property="data.id"/>
		<html:hidden property="data.demographicNo"/>		
		<html:hidden property="data.appointmentNo"/>
		
						
		<tr>
			<td class="genericTableHeader">Procedure name</td>
			<td class="genericTableData">
				<html:text property="data.procedureName" size="50"/>
			</td>
		</tr>
		
		
		<tr>
			<td class="genericTableHeader">Eye</td>
			<td class="genericTableData">
				<html:select property="data.eye">
					<html:option value="OU">OU</html:option>
					<html:option value="OD">OD</html:option>
					<html:option value="OS">OS</html:option>
					<html:option value="OD then OS">OD then OS</html:option>
					<html:option value="OS then OD">OS then OD</html:option>
				</html:select>
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Location</td>
			<td class="genericTableData">
					<html:text property="data.location" size="35"/>		
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Comment</td>
			<td class="genericTableData">
					<html:textarea rows="5" cols="40" property="data.comment"></html:textarea>		
			</td>
		</tr>

		<tr style="background-color:white">
			<td colspan="2">
				<br />
				
				
							&nbsp;&nbsp;&nbsp;&nbsp;
							<html:submit value="Book Procedure" />
						
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" name="cancel" value="Cancel" onclick="window.close()" />

			</td>
		</tr>
	</table>

</html:form>

</body>
</html>
