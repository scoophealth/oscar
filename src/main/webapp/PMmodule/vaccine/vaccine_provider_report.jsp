
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<table width="100%" cellspacing="3" cellpadding="3">
	<tr>
		<td align="left" colspan="2"><img src=images/caisi_logo.gif
			border="0" width="624" height="83"></td>
	</tr>

	<tr>
		<td align="center" colspan="2">Vaccine Provider Report <br />
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
	<Tr vheight="15">
		<td></td>
		<td></td>
	</Tr>
	<!-- allergies -->
	<tr>
		<td>Fever: <c:out value="${intakeMap['Fever']}" /></td>
		<td>Reaction to Vaccine: <c:out value="${intakeMap['Reaction']}" /></td>
	</tr>
	<tr>
		<td>Contraindicated Health Conditions:</td>
		<td><c:out
			value="${intakeMap['Contraindicated Health Conditions']}" />
	</tr>
	<tr>
		<td>Vaccination History:</td>
		<td><c:out value="${intakeMap['Vaccination History']}" />
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
