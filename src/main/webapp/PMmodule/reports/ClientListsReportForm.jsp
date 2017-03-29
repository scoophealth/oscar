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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="java.util.List"%>
<%@page
	import="org.oscarehr.PMmodule.model.*,org.oscarehr.common.model.*"%>
<%@ include file="/taglibs.jsp"%>

<div class="h4">
<h5>Client Lists Report Criteria</h5>
</div>
<div class="axial"><html:form
	action="/PMmodule/Reports/ClientListsReport">
	<input type="hidden" name="method" value="report" />

	<table border="0" cellspacing="2" cellpadding="3">
		<tr>
			<th>Status</th>
			<td><select name="form.admissionStatus">
				<option value="">any</option>
				<%
							for (Demographic.PatientStatus patientStatus : Demographic.PatientStatus.values())
							{
								%>
				<option value="<%=patientStatus.name()%>"><%=patientStatus.name()%></option>
				<%
							}
						%>
			</select></td>
		</tr>

		<tr>
			<th>Seen by provider</th>
			<td><select name="form.providerId">
				<option value="">Ignore Option</option>
				<%
							for (Provider provider : (List<Provider>)request.getAttribute("providers"))
							{
								%>
				<option value="<%=provider.getProviderNo()%>"><%=provider.getFormattedName()%></option>
				<%						
							}
						%>
			</select></td>
		</tr>

		<tr>
			<th>Seen between</th>
			<td>Start Date <input type="text" name="form.seenStartDate"
				size="15" /> (yyyy-mm-dd) - End Date <input type="text"
				name="form.seenEndDate" size="15" /> (yyyy-mm-dd)</td>
		</tr>

		<tr>
			<th>Has been in program</th>
			<td><select name="form.programId">
				<option value="">any</option>
				<%
							for (Program program : (List<Program>)request.getAttribute("programs"))
							{
								%>
				<option value="<%=program.getId()%>"><%=program.getName()%>
				- <%=program.getDescription()%></option>
				<%						
							}
						%>
			</select></td>
		</tr>

		<tr>
			<th>Admitted between</th>
			<td>Start Date <input type="text" name="form.enrolledStartDate"
				size="15" /> (yyyy-mm-dd) - End Date <input type="text"
				name="form.enrolledEndDate" size="15" /> (yyyy-mm-dd)</td>
		</tr>

		<tr>
			<td align="center" colspan="2"><html:submit
				value="Generate Report" /></td>
		</tr>
	</table>

</html:form></div>
