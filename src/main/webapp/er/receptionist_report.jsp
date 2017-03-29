
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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_er" rights="r" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_er");%>
</security:oscarSec>
<%
	if(!authed2) {
		return;
	}
%>


<%@ include file="/taglibs.jsp"%>
<%@page import="org.oscarehr.casemgmt.web.CaseManagementViewAction"%>

<table width="100%" cellspacing="3" cellpadding="3">
	<tr>
		<td align="left" colspan="2"><img src=images/caisi_logo.gif
			border="0" width="624" height="83"></td>
	</tr>

	<tr>
		<td align="center" colspan="2">ER Receptionist Report <br />
		<br />
		</td>
	</tr>

	<tr>
		<td>Client Name:</td>
		<td><c:out value="${client_name}" /></td>
	</tr>
	<tr>
		<td>Date of Birth:</td>
		<td><c:out value="${client_dob}" /></td>
	</tr>
	<tr>
		<td>Health Card:</td>
		<td><c:out value="${client_healthCard}" /></td>
	</tr>
	<tr>
		<td valign="top">Current Programs:</td>
		<td>
		<table width="100%">
			<tr>
				<td><b>Program Name</b></td>
				<td><b>Admission Date</b></td>
				<td><b>Contact Info</b></td>
			</tr>
			<c:forEach var="admission" items="${admissions}">
				<tr>
					<td valign="top"><c:out value="${admission.program.name}" /></td>
					<td valign="top"><fmt:formatDate pattern="yyyy-MM-dd"
						value="${admission.admissionDate}" /></td>
					<td valign="top"><c:out value="${admission.program.address}" />
					<br />
					Phone:<c:out value="${admission.program.phone}" /> <br />
					Fax:<c:out value="${admission.program.fax}" /> <br />
					</td>
				</tr>
			</c:forEach>
		</table>
		</td>
	</tr>
	<tr>
		<td valign="top">Current Issues:</td>
		<td valign="top">
		<table width="100%">
			<c:if test="${not empty Prescriptions}">
				<tr>
				
					<td><b>Code</b></td>
					<td><b>Issue</b></td>
					<td><b>Location</b></td>
					<td><b>Acute</b></td>
					<td><b>Certain</b></td>
					<td><b>Major</b></td>
					<td><b>Resolved</b></td>
					<td><b>type</b></td>
				</tr>
			</c:if>
			<c:forEach var="issue" items="${remote_issues}">
			<%
				CaseManagementViewAction.IssueDisplay issue = (CaseManagementViewAction.IssueDisplay)pageContext.getAttribute("issue");
				String priority = "";				
				if("allergy".equals(issue.priority))
					priority="yellow";
			%>
				<tr>
					<td><c:out value="${issue.code}" /></td>
					<td bgcolor="<%=priority%>"><c:out value="${issue.description }" /></td>
					<td><c:out value="${issue.location}" /></td>
					<td><c:out value="${issue.acute}" /></td>
					<td><c:out value="${issue.certain}" /></td>
					<td><c:out value="${issue.major}" /></td>
					<td><c:out value="${issue.resolved}" /></td>
					<td><c:out value="${issue.role}" /></td>
				</tr>
			</c:forEach>
		</table>
		</td>
	</tr>
	<tr>
		<td>Prescriptions:</td>
		<td>
		<table>
		<c:if test="${not empty Prescriptions}">
			<tr class="title">
				<td><b>Start Date</b></td>
				<td><b>Prescription Details</b></td>
				<td><b>Location Prescribed</b></td>
			</tr>
		</c:if>
<c:forEach var="prescription" items="${Prescriptions}">
	<tr>
		<td>
			<c:if test="${prescription.expired}">
			*
			</c:if>
			<fmt:formatDate pattern="MM/dd/yy" value="${prescription.rxDate}"/>
		</td>
		
		<%String styleColor=""; %>
		<c:if test="${!prescription.expired && prescription.archived}">
		<%styleColor="style=\"color:red;text-decoration: line-through;\"";%>
		</c:if>
		<c:if test="${prescription.expired && prescription.archived}">
		<%styleColor="style=\"text-decoration: line-through;\"";%>
		</c:if>
		<c:if test="${!prescription.expired && !prescription.archived}">
		<%styleColor="style=\"color:red;\"";%>
		</c:if>
		<td bgcolor="white">
			<span <%= styleColor%> ><c:out value="${prescription.special}"/></span>			
		</td>
		
		<td bgcolor="white">
			<c:if test="${empty prescription.remoteFacilityName}">
				local
			</c:if>
			<c:if test="${not empty prescription.remoteFacilityName}">
				<c:out value="${prescription.remoteFacilityName}"/>
			</c:if>
		</td>
		
	</tr>
</c:forEach>
		</table>
		</td>
	</tr>
	<!-- allergies -->
	<tr>
		<td>Allergies:</td>
		<td>
		<table width="100%" border="0" cellpadding="0" cellspacing="1">
			<tr class="title">
				<td><b>Update date</b></td>
				<td><b>Allergy description</b></td>
				<td><b>Reaction</b></td>
			</tr>
			<c:forEach var="allergy" items="${allergies}">
				<tr>
					<td bgcolor="white"><fmt:formatDate pattern="MM/dd/yy"
						value="${allergy.entry_date}" /></td>
					<td bgcolor="white"><c:out value="${allergy.description}" /></td>
					<td bgcolor="white"><c:out value="${allergy.reaction}" /></td>
				</tr>
			</c:forEach>
		</table>

		</td>
	</tr>
	<tr>
		<td colspan="2"><a href="javascript:void(0);"
			onclick="window.open('oscarPrevention/index.jsp?demographic_no=<c:out value="${demographicNo}"/>','prevention','width=600,height=600');return false;">Immunization
		Record</a></td>
	</tr>
	<tr>
		<td colspan="2"><input type="button" value="Print"
			onclick="window.print()" /> &nbsp; <input type="button"
			value="Back to Client Search"
			onclick="location.href='provider/er_clerk.jsp'" /></td>
	</tr>
</table>

<br />
<br />
<h5>We cannot provide any assurance that the information enclosed
is accurate, complete, or up-to-date for any particular purpose. Please
verify this information before relying upon it. We do not assume
responsibility for the consequences of any reliance on this information.</h5>
<h5>The CAISI Project: Agencies and clients integrating care to end
chronic homelessness. www.caisi.ca please join the CAISI mailing lists
at: https://lists.sourceforge.net/lists/listinfo/oscarmcmaster-caisi
Client Access to Integrated Services and Information (CAISI)</h5>
