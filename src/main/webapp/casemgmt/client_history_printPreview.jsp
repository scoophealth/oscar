
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>



<%@ page import="org.oscarehr.casemgmt.model.*"%>
<%@ page import="org.oscarehr.casemgmt.web.formbeans.*"%>
<%@ page import="oscar.OscarProperties"%>
<style type="text/css" media="print">
.header {
	display: none;
}

.header INPUT {
	display: none;
}

.header A {
	display: none;
}
</style>
<table width="100%" border="0" cellpadding="0" cellspacing="1">

	<tr>
		<td><b>Client Name : </b><c:out
			value="${requestScope.casemgmt_demoName}" /></td>
	</tr>
	<tr>
		<td><b>Age : </b><c:out value="${requestScope.casemgmt_demoAge}" /></td>
		<td><b>DOB : </b><c:out value="${requestScope.casemgmt_demoDOB}" /></td>
	</tr>
	<tr>
		<td><b>Team : </b><c:out value="${requestScope.teamName}" /></td>
		<td><b>Other File Number : </b><c:out
			value="${cpp.otherFileNumber}" /></td>
	</tr>

	<%if(!OscarProperties.getInstance().isTorontoRFQ())  { %>
	<tr>
		<td><b>Primary Health Care Provider : </b><c:out
			value="${cpp.primaryPhysician}" /></td>
	</tr>
	<%} %>
	<tr>
		<td><b>Primary Counsellor/Caseworker : </b><c:out
			value="${cpp.primaryCounsellor}" /></td>
	</tr>

	<tr height="10">
		<td bgcolor="white" colspan="2">&nbsp;</td>
	</tr>

	<tr>
		<td><b>Updated Last : </b><c:out
			value="${requestScope.cpp.update_date}" /></td>
	</tr>


	<tr>
		<td><b>Social History</b></td>
		<td><b>Family History</b></td>
	</tr>
	<tr>
		<td><c:out value="${cpp.socialHistory}" /></td>
		<td><c:out value="${cpp.familyHistory}" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><b>Medical History</b></td>
		<td><b>Past Medications</b></td>
	</tr>
	<tr>
		<td><c:out value="${cpp.medicalHistory}" /></td>
		<td><c:out value="${cpp.pastMedications}" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td colspan="2"><b>Other Support Systems</b></td>
	</tr>
	<tr>
		<td colspan="2"><c:out value="${cpp.otherSupportSystems}" /></td>
	</tr>

	<tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
</table>
<table class="header">
	<tr>
		<td><input type="button" value="Print" onclick="window.print()">
		<input type="button" value="Close" onclick="window.close()" /></td>
	</tr>
</table>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="casemgmt">
		<div style="color: blue"><I><c:out value="${message}" /></I></div>
	</html:messages>
</logic:messagesPresent>
