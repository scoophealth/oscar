
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

<html:hidden property="cpp.reminders" />
<table width="100%" border="0" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0">
	<%if(!OscarProperties.getInstance().isTorontoRFQ())  { %>
	<tr>
		<td bgcolor="white">Primary Health Care Provider</td>
		<td bgcolor="white"><html:text property="cpp.primaryPhysician" /></td>
	</tr>
	<%} %>
	<tr>
		<td bgcolor="white">Primary Counsellor/Caseworker</td>
		<td bgcolor="white"><html:text property="cpp.primaryCounsellor" /></td>
	</tr>
	<tr>
		<td bgcolor="white">Other File Number</td>
		<td bgcolor="white"><html:text property="cpp.otherFileNumber" /></td>
	</tr>
	<tr height="10">
		<td bgcolor="white" colspan="2">&nbsp;</td>
	</tr>

	<tr>
		<td bgcolor="white">Updated Last</td>
		<td bgcolor="white"><c:out
			value="${requestScope.cpp.update_date}" /></td>
	</tr>


	<tr class="title">
		<td>Social History</td>
		<td>Family History</td>
	</tr>
	<tr>
		<td bgcolor="white"><html:textarea property="cpp.socialHistory"
			rows="5" cols="45" /></td>
		<td bgcolor="white"><html:textarea property="cpp.familyHistory"
			rows="5" cols="45" /></td>
	</tr>

	<tr class="title">
		<td>Medical History</td>
		<td>Past Medications</td>
	</tr>
	<tr>
		<td bgcolor="white"><html:textarea property="cpp.medicalHistory"
			rows="5" cols="45" /></td>
		<td bgcolor="white"><html:textarea property="cpp.pastMedications"
			rows="5" cols="45" /></td>
	</tr>

	<tr class="title">
		<td colspan="2">Other Support Systems</td>
	</tr>
	<tr>
		<td colspan="2" bgcolor="white"><html:textarea
			property="cpp.otherSupportSystems" rows="2" cols="95" /></td>
	</tr>
</table>
<html:submit value="Save"
	onclick="this.form.method.value='patientCPPSave'" />
<html:submit value="Print Preview"
	onclick="this.form.method.value='patientCppPrintPreview'"></html:submit>
<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="casemgmt">
		<div style="color: blue"><I><c:out value="${message}" /></I></div>
	</html:messages>
</logic:messagesPresent>
