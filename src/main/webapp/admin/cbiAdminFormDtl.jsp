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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.common.model.OcanStaffFormData"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="oscar.util.CBIUtil"%>
<%@page import="org.oscarehr.util.SpringUtils"%>

<%!
public String getFieldVal(int ocanStaffFormId, String key)
{
	String val = "";
	
	List<OcanStaffFormData> existingAnswers=OcanForm.getStaffAnswers(ocanStaffFormId, key, OcanForm.PRE_POPULATION_LEVEL_ALL);

	if(existingAnswers.size()>0) {
		val = existingAnswers.get(0).getAnswer();
	}
	
	return val;
}
%>

<%
CBIUtil cbiUtil = new CBIUtil();
OcanStaffForm ocanStaffForm = null;

LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
Integer currentFacilityId=loggedInInfo.getCurrentFacility().getId();

String submissionId = "";
int formId = -1;
if(request.getParameter("submissionId")!=null && request.getParameter("submissionId").trim().length()>0)
{
	submissionId = request.getParameter("submissionId");
	ocanStaffForm = cbiUtil.getCBIFormDataBySubmissionId(currentFacilityId, Integer.parseInt(submissionId));
}

if(ocanStaffForm!=null)
{
	formId = ocanStaffForm.getId();
	%>
	<table border="1" class="table_form_dtl" bordercolor="#C0CBDB">
		<tr>
			<td width="25%" class="label">Last Name at Birth: </td>
			<td width="25%"><%=ocanStaffForm.getLastNameAtBirth() %></td>
			<td width="25%" class="label">Current Last Name:</td>
			<td width="25%"><%=ocanStaffForm.getLastName() %></td>
		</tr>
		<tr>
			<td class="label">Middle Name: </td>
			<td><%=getFieldVal(formId, "middle") %></td>
			<td class="label">First Name: </td>
			<td><%=ocanStaffForm.getFirstName() %></td>
		</tr>
		<tr>
			<td class="label">Preferred Name: </td>
			<td><%=getFieldVal(formId, "preferred") %></td>
			<td class="label">Address Line 1: </td>
			<td><%=ocanStaffForm.getAddressLine1()%></td>
		</tr>
		<tr>
			<td class="label">Address Line 2: </td>
			<td><%=ocanStaffForm.getAddressLine2()%></td>
			<td class="label">City: </td>
			<td><%=ocanStaffForm.getCity()%></td>
		</tr>
		<tr>
			<td class="label">Province: </td>
			<td><%=ocanStaffForm.getProvince()%></td>
			<td class="label">Postal Code (e.g. M4H 2T1): </td>
			<td><%=ocanStaffForm.getPostalCode()%></td>
		</tr>
		<tr>
			<td class="label">Phone Number: </td>
			<td><%=ocanStaffForm.getPhoneNumber()%></td>
			<td class="label">Ext: </td>
			<td><%=getFieldVal(formId, "extension") %></td>
		</tr>
		<tr>
			<td class="label">Email address: </td>
			<td><%=ocanStaffForm.getEmail()%></td>
			<td class="label">Date of Birth (YYYY-MM-DD): </td>
			<td><%=getFieldVal(formId, "date_of_birth") %></td>
		</tr>
		<tr>
			<td class="label">Estimated Age: </td>
			<td><%=ocanStaffForm.getEstimatedAge()%></td>
			<td class="label">Health Card # and Version Code: </td>
			<td><%=ocanStaffForm.getHcNumber()%>&nbsp;&nbsp;<%=ocanStaffForm.getHcVersion()%></td>
		</tr>
		<tr>
			<td class="label">Issuing Territory: </td>
			<td><%=getFieldVal(formId, "issuingTerritory") %></td>
			<td class="label">Service Recipient Location: </td>
			<td><%=getFieldVal(formId, "service_recipient_location") %></td>
		</tr>
		<tr>
			<td class="label">LHIN Consumer Resides in: </td>
			<td><%=getFieldVal(formId, "service_recipient_lhin") %></td>
			<td class="label">Gender: </td>
			<td><%=getFieldVal(formId, "gender") %></td>
		</tr>
		<tr>
			<td class="label">Marital Status: </td>
			<td><%=getFieldVal(formId, "marital_status") %></td>
		</tr>
	</table>
	<%
}
else
{
	%>
	<p><b>Form Details Not Found</b></p>
	<%
}
%>
