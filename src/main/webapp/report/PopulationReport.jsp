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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ include file="/taglibs.jsp"%>

<div class="page-header">
	<h4>
		<bean:message key="admin.admin.popRpt" />
		<div class="pull-right">
			<button name='print' onClick='window.print()' class="btn">
				<i class="icon-print icon-black"></i>
				<bean:message key="global.btnPrint" />
			</button>
		</div>
	</h4>
</div>

<table
	class="table table-bordered table-striped table-condensed table-hover">
	<colgroup>
		<col class="span7"></col>
		<col class="span2"></col>
	</colgroup>
	<thead>
		<tr>
			<td>Health of the Homeless: CAISI Population Health Report</td>
			<td><c:out value="${date}"></c:out></td>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>Number of Individuals with Specific Acute and Chronic
				Conditions who currently reside in Toronto Shelters which are part
				of the CAISI Project</td>
			<td><c:out value="${time}"></c:out></td>
		</tr>
	</tbody>
</table>

<!-- Shelter Population -->
<table
	class="table table-bordered table-striped table-condensed table-hover">
	<colgroup>
		<col class="span7"></col>
		<col class="span2"></col>
	</colgroup>
	<caption>Homeless Shelter Population</caption>
	<tbody>
		<tr>
			<th>Clients who used a CAISI shelter in the past year</th>
			<td><c:out value="${shelterPopulation.pastYear}"></c:out></td>
		</tr>
		<tr class="odd">
			<th>Current number of individuals in CAISI shelters</th>
			<td><c:out value="${shelterPopulation.current}"></c:out></td>
		</tr>
	</tbody>
</table>

<!-- Shelter Usage -->
<table
	class="table table-bordered table-striped table-condensed table-hover">
	<colgroup>
		<col class="span4"></col>
		<col class="span4"></col>
	</colgroup>
	<caption>Intensity of Shelter Use</caption>
	<tbody>
		<tr>
			<th>Low Use: (1 - 10 days / 4 yr)</th>
			<td><c:out value="${shelterUsage.low}"></c:out></td>
		</tr>
		<tr class="odd">
			<th>Moderate Use: (11 - 179 days / 4 yr)</th>
			<td><c:out value="${shelterUsage.medium}"></c:out></td>
		</tr>
		<tr>
			<th>High Use: (180 - 730 days / 4 yr)</th>
			<td><c:out value="${shelterUsage.high}"></c:out></td>
		</tr>
	</tbody>
</table>

<!-- Mortality -->
<c:if test="${not empty mortalities}">
	<table
		class="table table-bordered table-striped table-condensed table-hover">
		<colgroup>
			<col class="span5"></col>
			<col class="span4"></col>
		</colgroup>
		<caption>Mortality In Shelters</caption>
		<tbody>
			<tr>
				<th>Death Count (# of deaths in past year)</th>
				<td><c:out value="${mortalities.count}"></c:out></td>
			</tr>
			<tr class="odd">
				<th>Death Rate (# of deaths in past year / population)</th>
				<td><c:out value="${mortalities.percent}"></c:out></td>
			</tr>
		</tbody>
	</table>
</c:if>

<!-- Major Medical Condition -->
<table
	class="table table-bordered table-striped table-condensed table-hover">
	<caption>Major Medical Condition</caption>
	<colgroup>
		<col class="span2"></col>
		<col class="span3"></col>
		<col class="span4"></col>
	</colgroup>
	<thead>
		<tr>
			<th>Condition</th>
			<td>Number of Cases in CAISI Shelters</td>
			<td>Prevalence of Condition in CAISI Shelters</td>
		</tr>
	</thead>
	<tbody>
		<c:forEach varStatus="status" var="condition"
			items="${majorMedicalConditions}">
			<tr>
				<th><c:out value="${condition.key}"></c:out></th>
				<td><c:choose>
						<c:when
							test="${condition.value.count > 0 && condition.value.count < 5}">1 - 5</c:when>
						<c:otherwise>
							<c:out value="${condition.value.count}"></c:out>
						</c:otherwise>
					</c:choose></td>
				<td><c:out value="${condition.value.percent}"></c:out></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<!-- Major Mental Illness -->
<table
	class="table table-bordered table-striped table-condensed table-hover">
	<caption>Major Mental Illness</caption>
	<colgroup>
		<col class="span2"></col>
		<col class="span3"></col>
		<col class="span4"></col>
	</colgroup>
	<thead>
		<tr>
			<th>Condition</th>
			<td>Number of Cases in CAISI Shelters</td>
			<td>Prevalence of Condition in CAISI Shelters</td>
		</tr>
	</thead>
	<tbody>
		<c:forEach varStatus="status" var="condition"
			items="${majorMentalIllnesses}">
			<tr>
				<th><c:out value="${condition.key}"></c:out></th>
				<td><c:choose>
						<c:when
							test="${condition.value.count > 0 && condition.value.count < 5}">1 - 5</c:when>
						<c:otherwise>
							<c:out value="${condition.value.count}"></c:out>
						</c:otherwise>
					</c:choose></td>
				<td><c:out value="${condition.value.percent}"></c:out></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<!-- Serious Medical Conditions -->
<table
	class="table table-bordered table-striped table-condensed table-hover">
	<caption>Incidence in the past year of Serious Medical
		Conditions</caption>
	<colgroup>
		<col class="span2"></col>
		<col class="span3"></col>
		<col class="span4"></col>
	</colgroup>
	<thead>
		<tr>
			<th>Condition</th>
			<td>Number of Cases in CAISI Shelters</td>
			<td>Prevalence of Condition in CAISI Shelters</td>
		</tr>
	</thead>
	<tbody>
		<c:forEach varStatus="status" var="condition"
			items="${seriousMedicalConditions}">
			<tr>
				<th><c:out value="${condition.key}"></c:out></th>
				<td><c:choose>
						<c:when
							test="${condition.value.count > 0 && condition.value.count < 5}">1 - 5</c:when>
						<c:otherwise>
							<c:out value="${condition.value.count}"></c:out>
						</c:otherwise>
					</c:choose></td>
				<td><c:out value="${condition.value.percent}"></c:out></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<!-- Notes -->
<table
	class="table table-bordered table-striped table-condensed table-hover">
	<caption>Notes on the Data</caption>
	<colgroup>
		<col class="span9"></col>
	</colgroup>
	<tbody>
		<tr>
			<td>1) <b>List of all Shelters in the CAISI
					System</b>: John Howard Society (service agency), Salvation Army
				Gateway (shelter), Salvation Army Maxwell Meighen (shelter), Seaton
				House (shelter), Sherbourne Health Center (health care agency),
				Street Health (outreach program).
			</td>
		</tr>
		<tr>
			<td>2) The indicator of &quot;Intensity of Shelter
				Use&quot; uses values produced by New York City Health and
				Department of Homeless Services (2005). As CAISI has not been
				operational for 4 years we are using the same values for the length
				of stay but only within the life of CAISI (approximately 15 months)
				rather than 4 years. This means that the level of intensity reported
				in this report are higher than comparable New York statistics.</td>
		</tr>
		<tr>
			<td>3) In each case where a prevalence or
				incidence rate is reported, the denominator represents the number of
				clients <b>currently</b> in a bed program at CAISI Shelters.
			</td>
		</tr>
		<tr>
			<td>4) The specific conditions are defined using
				ICD-10 codes. The numerator value is defined as the number of
				clients in the denominator who have been diagnosed with a condition
				associated with one or more of the ICD-10 codes that follow.</td>
		</tr>
	</tbody>
</table>

<c:forEach var="categoryCodeDescription"
	items="${categoryCodeDescriptions}">
	<table
		class="table table-bordered table-striped table-condensed table-hover">
		<caption>
			<c:out value="${categoryCodeDescription.key}"></c:out>
		</caption>
		<colgroup>
			<col class="span2"></col>
			<col class="span7"></col>
		</colgroup>
		<thead>
			<tr>
				<th>Code</th>
				<th>Description</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="codeDescription"
				items="${categoryCodeDescription.value}">
				<tr>
					<th><c:out value="${codeDescription.key}"></c:out></th>
					<td><c:out value="${codeDescription.value}"></c:out></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</c:forEach>
