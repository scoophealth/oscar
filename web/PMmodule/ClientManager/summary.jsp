<!--
	Copyright (c) 2001-2002.
	
	Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
	This software is published under the GPL GNU General Public License.
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
	See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
	
	OSCAR TEAM
	
	This software was written for Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada
-->
<%@ include file="/taglibs.jsp"%>

<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.PMmodule.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.model.ClientReferral"%>
<%@page import="java.util.Date"%>

<script>
var XMLHttpRequestObject = false;

if (window.XMLHttpRequest) {
	XMLHttpRequestObject = new XMLHttpRequest();
} else if (window.ActiveXObject) {
	XMLHttpRequestObject = new ActiveXObject("Microsoft.XMLHTTP");
}

window.onload = new Function("summary_load();");

function summary_load() {
	showEMPILinks();
}

function consent_mandatory() {
	<c:if test="${empty consent and empty remote_consent}">
		openConsent();
	</c:if>
}

function openIntakeA(view) {
	var url = '<html:rewrite action="/PMmodule/IntakeA.do"/>';
		url += "?formIntakeALock=N&viewIntakeA=" + view + "&demographicNo=";
		url += '<c:out value="${client.demographicNo}"/>';		
	location.href = url;
}

function openIntakeC(view) {
	var url = '<html:rewrite action="/PMmodule/IntakeC.do"/>';
		url += "?formIntakeCLock=N&viewIntakeC=" + view + "&demographicNo=";
		url += '<c:out value="${client.demographicNo}"/>';		
	location.href = url;
}

function openConsent(){
	var url = '<html:rewrite action="/PMmodule/Consent.do"/>';
		url += '?method=form&id='+ '<c:out value="${client.demographicNo}"/>';
	window.open(url,'consent');
}	

function showConsent(){
	var url = '<html:rewrite action="/PMmodule/Consent.do?method=view" />';
		url += "&clientName="+'<c:out value="${client.formattedName}"/>';
		url += "&demographicNo="+'<c:out value="${client.demographicNo}"/>';
	window.open(url);	
}		

function showEMPILinks() {
	if (XMLHttpRequestObject) {
		var obj = document.getElementById('empi_links');
		
		XMLHttpRequestObject.open("GET",'<html:rewrite action="/PMmodule/ClientManager"/>?method=getLinks&id=<c:out value="${client.demographicNo}"/>');
		
		XMLHttpRequestObject.onreadystatechange = function()
		{
			if (XMLHttpRequestObject.readyState == 4 && XMLHttpRequestObject.status == 200) {
				obj.innerHTML = XMLHttpRequestObject.responseText;
			}
		}
		
		XMLHttpRequestObject.send(null);
	}		
}
</script>

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th>Personal Information</th>
		</tr>
	</table>
</div>

<table class="simple" cellspacing="2" cellpadding="3">
	<tr>
		<th width="20%">Name</th>
		<td><c:out value="${client.formattedName}" /></td>
	</tr>
	<tr>
		<th width="20%">Master File #</th>
		<td><c:out value="${client.demographicNo}" /></td>
	</tr>
	<tr>
		<th width="20%">Date of Birth</th>
		<td><c:out value="${client.yearOfBirth}" />/<c:out value="${client.monthOfBirth}" />/<c:out value="${client.dateOfBirth}" /></td>
	</tr>
	<tr>
		<th width="20%">Health Card</th>
		<td><c:out value="${client.hin}" />&nbsp;<c:out value="${client.ver}" /></td>
	</tr>
	<tr>
		<th width="20%">Resources</th>
		<td>
		<%
		if (session.getAttribute("userrole") != null && ((String) session.getAttribute("userrole")).indexOf("admin") != -1) {
			Integer demographicNo = ((Demographic) request.getAttribute("client")).getDemographicNo();
			pageContext.setAttribute("demographicNo",demographicNo);
		%>
			<a href="javascript:void(0);" onclick="window.open('<c:out value="${ctx}"/>/demographic/demographiccontrol.jsp?displaymode=edit&dboperation=search_detail&demographic_no=<c:out value="${demographicNo}"/>','master_file');return false;">OSCAR Master File</a>
		<%
 		}
		%>
		</td>
	</tr>
	<tr>
		<th width="20%">EMPI</th>
		<td><span id='empi_links'>Loading...</span></td>
	</tr>
</table>

<br />

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th>Bed Reservation</th>
		</tr>
	</table>
</div>

<table class="simple" cellspacing="2" cellpadding="3">
	<c:choose>
		<c:when test="${empty bedDemographic}">
			<tr>
				<td>
					<span style="color:red">No bed reserved</span>
				</td>
			</tr>
		</c:when>
		<c:otherwise>
			<tr>
				<th width="20%">Assigned</th>
				<td><c:out value="${bedDemographic.bedName}" /> (<c:out value="${bedDemographic.roomName}" /> <c:out value="${bedDemographic.programName}" />)</td>
			</tr>
			<tr>
				<th width="20%">Status</th>
				<td><c:out value="${bedDemographic.statusName}" /></td>
			</tr>
			<tr>
				<th width="20%">Late Pass</th>
				<td><c:out value="${bedDemographic.latePass}" /></td>
			</tr>
			<tr>
				<th width="20%">Until</th>
				<td><fmt:formatDate value="${bedDemographic.reservationEnd}" pattern="yyyy-MM-dd" /></td>
			</tr>
		</c:otherwise>
	</c:choose>
</table>

<br />

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th>Intake Forms</th>
		</tr>
	</table>
</div>

<table class="simple" cellspacing="2" cellpadding="3">
	<thead>
		<tr>
			<th>Form Name</th>
			<th>Most Recent</th>
			<th>Staff</th>
			<th>Status</th>
			<th>Actions</th>
		</tr>
	</thead>
	<c:if test="${requestScope.intakeAEnabled eq 'true'}">
		<tr>
			<td width="20%">CAISI Intake</td>
			<c:if test="${intakeADate != null}">
				<td><fmt:formatDate value="${intakeADate}" pattern="yyyy-MM-dd kk:mm" /></td>
				<td><c:out value="${intakeAProvider}" /></td>
				<td></td>
				<td><input type="button" value="Update" onclick="javascript:openIntakeA('Y')" /></td>
			</c:if>
			<c:if test="${intakeADate == null}">
				<td><span style="color:red">None found</span></td>
				<td></td>
				<td></td>
				<td><input type="button" value="New Form" onclick="javascript:openIntakeA('N')" /></td>
			</c:if>
		</tr>
	</c:if>
	<c:if test="${requestScope.intakeCEnabled eq 'true'}">
		<tr>
			<td width="20%">Street Health Mental Health Intake</td>
			<c:if test="${intakeCDate != null}">
				<td><fmt:formatDate value="${intakeCDate}" pattern="yyyy-MM-dd kk:mm" /></td>
				<td><c:out value="${intakeCProvider}" /></td>
				<td></td>
				<td><input type="button" value="Update" onclick="javascript:openIntakeC('Y')" /></td>
			</c:if>
			<c:if test="${intakeCDate == null}">
				<td><span style="color:red">None found</span></td>
				<td></td>
				<td></td>
				<td><input type="button" value="New Form" onclick="javascript:openIntakeC('N')" /></td>
			</c:if>
		</tr>
	</c:if>
</table>

<br />

<c:if test="${empty remote_consent}">
	<div class="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
			<tr>
				<th>Consent</th>
			</tr>
		</table>
	</div>
	
	<table class="simple" cellspacing="2" cellpadding="3">
		<thead>
			<tr>
				<th>Form Name</th>
				<th>Most Recent</th>
				<th>Staff</th>
				<th>Status</th>
				<th>Exclusions</th>
				<th>Actions</th>
			</tr>
		</thead>
		<tr>
			<td width="20%">Consent Form</td>
			<c:choose>
				<c:when test="${empty consent}">
					<td><span style="color:red">None found</span></td>
					<td><span style="color:red"></span></td>
					<td></td>
					<td></td>
					<td><input type="button" value="New Consent" onclick="javascript:openConsent()" /></td>
				</c:when>
				<c:otherwise>
					<td><fmt:formatDate value="${consent.dateSigned}" pattern="yyyy-MM-dd kk:mm" /></td>
					<td><c:out value="${consent.providerName}" /></td>
					<td><c:out value="${consent.status}" /></td>
					<td><c:out value="${consent.exclusionString}" /></td>
					<td><input type="button" value="Update" onclick="javascript:openConsent()" /></td>
				</c:otherwise>
			</c:choose>
		</tr>
	</table>
	
	<br />
</c:if>

<c:if test="${not empty remote_consent}">
	<div class="tabs">
		<table cellpadding="3" cellspacing="0" border="0">
			<tr>
				<th>Consent (Remote)</th>
			</tr>
		</table>
	</div>
	
	<table class="simple" cellspacing="2" cellpadding="3">
		<thead>
			<tr>
				<th>Agency</th>
				<th>Date</th>
				<th>Status</th>
				<th>Exclusions</th>
			</tr>
		</thead>
		<tr>
			<td><c:out value="${remote_consent_agency_name}" /></td>
			<td><c:out value="${remote_consent_date.value}" /></td>
			<td><c:out value="${remote_consent.value}" /></td>
			<td><c:out value="${remote_consent_exclusions.value}" /></td>
		</tr>
	</table>
	
	<br />
</c:if>

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th>Current Programs</th>
		</tr>
	</table>
</div>

<display:table class="simple" cellspacing="2" cellpadding="3" id="admission" name="admissions" export="false" pagesize="10" requestURI="/PMmodule/ClientManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	<display:setProperty name="basic.msg.empty_list" value="This client is not currently admitted to any programs." />
	
	<display:column property="programName" sortable="true" title="Program Name" />
	<display:column property="programType" sortable="true" title="Program Type" />
	<display:column property="admissionDate" format="{0, date, yyyy-MM-dd kk:mm}" sortable="true" title="Admission Date" />
	<display:column sortable="true" title="Days in Program">
		<%
			Admission tempAdmission = (Admission) pageContext.getAttribute("admission");
			Date admissionDate = tempAdmission.getAdmissionDate();
			Date dischargeDate = tempAdmission.getDischargeDate() != null ? tempAdmission.getDischargeDate() : new Date();
			
			long diff = dischargeDate.getTime() - admissionDate.getTime();
			diff = diff / 1000; // seconds
			diff = diff / 60; // minutes
			diff = diff / 60; // hours
			diff = diff / 24; // days

			String numDays = String.valueOf(diff);
		%>
		<%=numDays%>
	</display:column>
	<display:column property="temporaryAdmission" sortable="true" title="Temporary Admission" />
	<display:column property="admissionNotes" sortable="true" title="Admission Notes" />
</display:table>

<br />
<br />

<div class="tabs">
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th>Referrals</th>
		</tr>
	</table>
</div>

<display:table class="simple" cellspacing="2" cellpadding="3" id="referral" name="referrals" export="false" pagesize="10" requestURI="/PMmodule/ClientManager.do">
	<display:setProperty name="paging.banner.placement" value="bottom" />
	
	<display:column property="programName" sortable="true" title="Program Name" />
	<display:column property="programType" sortable="true" title="Program Type" />
	<display:column property="referralDate" format="{0, date, yyyy-MM-dd kk:mm}" sortable="true" title="Referral Date" />
	<display:column property="providerFormattedName" sortable="true" title="Referring Provider" />
	<display:column sortable="true" title="Days in Queue">
		<%
			ClientReferral tempReferral = (ClientReferral) pageContext.getAttribute("referral");
			Date referralDate = tempReferral.getReferralDate();
			Date currentDate = new Date();
			
			long diff = currentDate.getTime() - referralDate.getTime();
			diff = diff / 1000; // seconds
			diff = diff / 60; // minutes
			diff = diff / 60; // hours
			diff = diff / 24; // days

			String numDays = String.valueOf(diff);
		%>
		<%=numDays%>
	</display:column>
</display:table>
