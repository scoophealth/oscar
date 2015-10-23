
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

<%@page import="java.util.Calendar"%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<title>Facility Messages</title>
</head>

<script>
function openBrWindow(theURL,winName,features) { 
  window.open(theURL,winName,features);
}

</script>
<body>
<body>
<table border="0" cellspacing="0" cellpadding="0" width="100%"
	bgcolor="#CCCCFF">
	<tr class="subject">
		<th colspan="4">OSCAR</th>
	</tr>

	<tr>
		<td class="searchTitle" colspan="4">Facility Message Editor</td>
	</tr>
</table>

<br />
<html:form action="/FacilityMessage">
	<input type="hidden" name="method" value="save" />
	<html:hidden property="facility_message.id" />
	<table width="60%" border="0" cellpadding="0" cellspacing="1"
		bgcolor="#C0C0C0">
		<tr>
			<td class="fieldTitle">Expiry Day:&nbsp;</td>
			<td class="fieldValue"><html:text
				property="facility_message.expiry_day" /> <%
                      	Calendar rightNow = Calendar.getInstance();              
   	                  	int year = rightNow.get(Calendar.YEAR);
   	                  	int month = rightNow.get(Calendar.MONTH)+1;
   	                  	int day = rightNow.get(Calendar.DAY_OF_MONTH);
   	                  	String formattedDate = year + "-" + month + "-" + day;
                     %> <a href="#"
				onClick="openBrWindow('calendar/oscarCalendarPopup.jsp?type=caisi&openerForm=facilityMessageForm&amp;openerElement=facility_message.expiry_day&amp;year=<%=year %>&amp;month=<%=month %>','','width=300,height=300')"><img
				border="0" src="images/calendar.jpg" /></a></td>
			<td></td>
		</tr>
		<tr>
			<td class="fieldTitle">Expiry Time:&nbsp;</td>
			<td class="fieldValue">Hour: <html:select
				property="facility_message.expiry_hour">
				<%for(int x=1;x<24;x++){ %>
				<html:option value="<%=String.valueOf(x) %>"><%=x %></html:option>
				<% } %>
			</html:select> &nbsp;&nbsp; Minute: <html:select
				property="facility_message.expiry_minute">
				<%for(int x=0;x<60;x++) {%>
				<html:option value="<%=String.valueOf(x) %>"><%=x %></html:option>
				<% } %>
			</html:select></td>
			<td></td>
		</tr>
		<tr>
			<td class="fieldTitle">Message&nbsp;</td>
			<td colspan="2" class="fieldValue"><html:text size="60"
				property="facility_message.message" /></td>
		</tr>

		<tr>
			<td class="fieldTitle">Facility&nbsp;</td>
			<td>
			<%
						String role = (String)request.getAttribute("issueRole");
						pageContext.setAttribute("issue_role",role);
					%> <select name="facility_message.facilityId" id="facilityId">
				<option value="0">&nbsp;</option>
				<c:forEach var="facility" items="${facilities}" varStatus="status">
					<c:choose>
						<c:when
							test="${facility.id == facilityMessageForm.map.facility_message.facilityId}">
							<option value="<c:out value="${facility.id}"/>" selected><c:out
								value="${facility.name}" /></option>
						</c:when>
						<c:otherwise>
							<option value="<c:out value="${facility.id}"/>"><c:out
								value="${facility.name}" /></option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select></td>
		</tr>
		
				<tr>
			<td class="fieldTitle">Program&nbsp;</td>
			<td>
				<select name="facility_message.programId" id="programId">
					<option value=""></option>
					<c:forEach var="program" items="${programs}" varStatus="status">
					<c:choose>
						<c:when
							test="${program.id == facilityMessageForm.map.facility_message.programId}">
							<option value="<c:out value="${program.id}"/>" selected><c:out
								value="${program.name}" /></option>
						</c:when>
						<c:otherwise>
							<option value="<c:out value="${program.id}"/>"><c:out
								value="${program.name}" /></option>
						</c:otherwise>
					</c:choose>

				</c:forEach>
				</select>
			</td>
		</tr>


		<tr>
			<td class="fieldValue" colspan="3"><html:submit>Save</html:submit>
			<input type="button" value="Cancel"
				onclick="location.href='FacilityMessage.do'" /></td>
		</tr>
	</table>
</html:form>
</body>
</html>
