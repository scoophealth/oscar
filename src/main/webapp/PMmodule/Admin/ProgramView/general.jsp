
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">
	<tr>
		<th title="Programs">General Information</th>
		<th title="Templates" class="nofocus">
			<a onclick="javascript:clickTab2('General','Vacancy Templates');return false;" href="javascript:void(0)">Vacancy Templates</a>
		</th>
	</tr>
</table>
</div>
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="20%">Name:</td>
		<td><c:out value="${program.name}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Facility:</td>
		<td><c:out value="${facilityName}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Description:</td>
		<td><c:out value="${program.description}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">HIC:</td>
		<td><c:out value="${program.hic}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Type:</td>
		<td><c:out value="${program.type}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Location:</td>
		<td><c:out value="${program.location}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Client Participation:</td>
		<td><c:out value="${program.numOfMembers}" />/<c:out
			value="${program.maxAllowed}" />&nbsp;(<c:out
			value="${program.queueSize}" /> waiting)</td>
	</tr>
	<tr class="b">
		<td width="20%">Holding Tank:</td>
		<td><c:out value="${program.holdingTank}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Allow Batch Admissions:</td>
		<td><c:out value="${program.allowBatchAdmission}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Allow Batch Discharges:</td>
		<td><c:out value="${program.allowBatchDischarge}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Man Or Woman:</td>
		<td><c:out value="${program.manOrWoman}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Transgender:</td>
		<td><c:out value="${program.transgender}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">First Nation:</td>
		<td><c:out value="${program.firstNation}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Bed Program Affiliated:</td>
		<td><c:out value="${program.bedProgramAffiliated}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Alcohol:</td>
		<td><c:out value="${program.alcohol}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Abstinence Support?</td>
		<td><c:out value="${program.abstinenceSupport}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Physical Health:</td>
		<td><c:out value="${program.physicalHealth}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Mental Health:</td>
		<td><c:out value="${program.mentalHealth}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Housing:</td>
		<td><c:out value="${program.housing}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Exclusive View:</td>
		<td><c:out value="${program.exclusiveView}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Minimum Age:</td>
		<td><c:out value="${program.ageMin}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Maximum Age:</td>
		<td><c:out value="${program.ageMax}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Enable Mandatory Encounter Time:</td>
		<td><c:out value="${program.enableEncounterTime}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Enable Mandatory Transportation Time:</td>
		<td><c:out value="${program.enableEncounterTransportationTime}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Email Notification Addresses (csv):</td>
		<td><c:out value="${program.emailNotificationAddressesCsv}" /></td>
	</tr>
	<tr class="b">
		<td width="20%">Last referral notificaiton time:</td>
		<td><c:out value="${program.lastReferralNotification}" /></td>
	</tr>
</table>
