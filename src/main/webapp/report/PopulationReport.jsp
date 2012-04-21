<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Population Report</title>
    <style type="text/css">
		<!--
		body {
			font-family: sans-serif;
			margin: 2.5% 5%;
		}
		
		table {
			border: 1px solid black;
			border-collapse: collapse;
			margin: 0%;
			padding: 0%;
			width: 100%;
		}
		
		caption {
			background: #B0C4DE none repeat scroll 0%;
			border: 1px solid black;
			padding: 0.2em 0.5em;
			text-align: left;
			text-transform: uppercase;
		}
		
		th {
			border: 1px solid black;
			font-weight: normal;
			margin: 0%;
			padding: 0.2em 0.5em;
			text-align: left;
			vertical-align: middle;
		}
		
		td {
			border: 1px solid black;
			margin: 0%;
			padding: 0.2em 0.5em;
			text-align: center;
			vertical-align: middle;
		}
		
		thead {
			text-transform: uppercase;
		}
		
		thead th {
			background: #4682B4 none repeat scroll 0%;
		}
		
		thead td {
			background: #B0C4DE none repeat scroll 0%;
		}
		
		tbody th {
			background: #4682B4 none repeat scroll 0%;
			color: #FFFFFF;
			width: 30%;
		}
		
		tbody td {
			background: #FFFFFF none repeat scroll 0%;
		}
		
		tbody tr.odd th {
			background: #4682B4 none repeat scroll 0%;
		}
		
		tbody tr.odd td {
			background: #B0C4DE none repeat scroll 0%;
		}
		
		table.header {
			border: none;
		}
		
		td.header {
			border: none;
		}
		
		thead td.header {
			background: #F08080 none repeat scroll 0%;
			color: #FFFFFF;
		}
		
		td.notes {
			text-align: left;
		}
		
		th.notes {
			background: #FFFFFF;
			color: #000000;
			width: 10%;
		}
		-->
	</style>
</head>
<body>
    <table class="header">
		<thead>
			<tr>
				<td class="header">Health of the Homeless: CAISI Population Health Report</td>
	            <td class="header"><c:out value="${date}"></c:out></td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="header">Number of Individuals with Specific Acute and Chronic Conditions who currently reside in Toronto Shelters which are part of the CAISI Project</td> 
				<td class="header"><c:out value="${time}"></c:out></td>
			</tr>
		</tbody>
	</table>
    <br />
    <br />
    <!-- Shelter Population -->
    <table>
        <caption>Homeless Shelter Population</caption>
        <tbody>
            <tr>
                <th scope="row">Clients who used a CAISI shelter in the past year</th>
                <td><c:out value="${shelterPopulation.pastYear}"></c:out></td>
            </tr>
            <tr class="odd">
                <th scope="row">Current number of individuals in CAISI shelters</th>
                <td><c:out value="${shelterPopulation.current}"></c:out></td>
            </tr>
        </tbody>
    </table>
    <br />
    <!-- Shelter Usage -->
    <table>
        <caption>Intensity of Shelter Use</caption>
        <tbody>
            <tr>
                <th scope="row">Low Use: (1 - 10 days / 4 yr)</th>
                <td><c:out value="${shelterUsage.low}"></c:out></td>
            </tr>
            <tr class="odd">
                <th scope="row">Moderate Use: (11 - 179 days / 4 yr)</th>
                <td><c:out value="${shelterUsage.medium}"></c:out></td>
            </tr>
            <tr>
                <th scope="row">High Use: (180 - 730 days / 4 yr)</th>
                <td><c:out value="${shelterUsage.high}"></c:out></td>
            </tr>
        </tbody>
    </table>
    <br />
    <!-- Mortality -->
    <c:if test="${not empty mortalities}">
    <table>
        <caption>Mortality In Shelters</caption>
        <tbody>
            <tr>
                <th scope="row">Death Count (# of deaths in past year)</th>
                <td><c:out value="${mortalities.count}"></c:out></td>
            </tr>
            <tr class="odd">
                <th scope="row">Death Rate (# of deaths in past year / population)</th>
                <td><c:out value="${mortalities.percent}"></c:out></td>
            </tr>
        </tbody>
    </table>
    </c:if>
    <br />
    <!-- Major Medical Condition -->
    <table>
        <caption>Major Medical Condition</caption>
        <thead>
            <tr>
                <th scope="col">Condition</th>
                <td scope="col">Number of Cases in CAISI Shelters</td>
                <td scope="col">Prevalence of Condition in CAISI Shelters</td>
            </tr>
        </thead>
        <tbody>
            <c:forEach varStatus="status" var="condition" items="${majorMedicalConditions}">
            <c:choose><c:when test="${status.index % 2 == 0}"><tr></c:when><c:otherwise><tr class="odd"></c:otherwise></c:choose>
                <th scope="row"><c:out value="${condition.key}"></c:out></th>
                <td><c:choose><c:when test="${condition.value.count > 0 && condition.value.count < 5}">1 - 5</c:when><c:otherwise><c:out value="${condition.value.count}"></c:out></c:otherwise></c:choose></td>
                <td><c:out value="${condition.value.percent}"></c:out></td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
    <br />
    <!-- Major Mental Illness -->
    <table>
        <caption>Major Mental Illness</caption>
        <thead>
            <tr>
                <th scope="col">Condition</th>
                <td scope="col">Number of Cases in CAISI Shelters</td>
                <td scope="col">Prevalence of Condition in CAISI Shelters</td>
            </tr>
        </thead>
        <tbody>
            <c:forEach varStatus="status" var="condition" items="${majorMentalIllnesses}">
            <c:choose><c:when test="${status.index % 2 == 0}"><tr></c:when><c:otherwise><tr class="odd"></c:otherwise></c:choose>
                <th scope="row"><c:out value="${condition.key}"></c:out></th>
                <td><c:choose><c:when test="${condition.value.count > 0 && condition.value.count < 5}">1 - 5</c:when><c:otherwise><c:out value="${condition.value.count}"></c:out></c:otherwise></c:choose></td>
                <td><c:out value="${condition.value.percent}"></c:out></td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
    <br />
    <!-- Serious Medical Conditions -->
    <table>
        <caption>Incidence in the past year of Serious Medical Conditions</caption>
        <thead>
            <tr>
                <th scope="col">Condition</th>
                <td scope="col">Number of Cases in CAISI Shelters</td>
                <td scope="col">Prevalence of Condition in CAISI Shelters</td>
            </tr>
        </thead>
        <tbody>
            <c:forEach varStatus="status" var="condition" items="${seriousMedicalConditions}">
            <c:choose><c:when test="${status.index % 2 == 0}"><tr></c:when><c:otherwise><tr class="odd"></c:otherwise></c:choose>
                <th scope="row"><c:out value="${condition.key}"></c:out></th>
                <td><c:choose><c:when test="${condition.value.count > 0 && condition.value.count < 5}">1 - 5</c:when><c:otherwise><c:out value="${condition.value.count}"></c:out></c:otherwise></c:choose></td>
                <td><c:out value="${condition.value.percent}"></c:out></td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
    <br />
    <br />
    <!-- Notes -->
    <table>
        <caption>Notes on the Data</caption>
        <tbody>
        	<tr>
        		<td class="notes">1) <b>List of all Shelters in the CAISI System</b>: John Howard Society (service agency), Salvation Army Gateway (shelter), Salvation Army Maxwell Meighen (shelter), Seaton House (shelter), Sherbourne Health Center (health care agency), Street Health (outreach program).</td>
        	</tr>
        	<tr>
        		<td class="notes">2) The indicator of &quot;Intensity of Shelter Use&quot; uses values produced by New York City Health and Department of Homeless Services (2005).  As CAISI has not been operational for 4 years we are using the same values for the length of stay but only within the life of CAISI (approximately 15 months) rather than 4 years.  This means that the level of intensity reported in this report are higher than comparable New York statistics.</td>
        	</tr>
        	<tr>
        		<td class="notes">3) In each case where a prevalence or incidence rate is reported, the denominator represents the number of clients <b>currently</b> in a bed program at CAISI Shelters.</td>
        	</tr>
        	<tr>
        		<td class="notes">4) The specific conditions are defined using ICD-10 codes.  The numerator value is defined as the number of clients in the denominator who have been diagnosed with a condition associated with one or more of the ICD-10 codes that follow.</td>
        	</tr>
        </tbody>
    </table>
    <br />
	<c:forEach var="categoryCodeDescription" items="${categoryCodeDescriptions}">
		<table>
			<caption><c:out value="${categoryCodeDescription.key}"></c:out></caption>
			<thead>
				<tr>
					<th scope="col">Code</th>
					<th scope="col">Description</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="codeDescription" items="${categoryCodeDescription.value}">
					<tr>
						<th class="notes"><c:out value="${codeDescription.key}"></c:out></th>
						<td class="notes"><c:out value="${codeDescription.value}"></c:out></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<br />
	</c:forEach>
</body>
</html>
