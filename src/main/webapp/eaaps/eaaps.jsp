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
<security:oscarSec roleName="<%=roleName$%>" objectName="_eaaps" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_eaaps");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<html>
<head>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css" />

<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/global.js"></script>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>

<title>EAAPs Info</title>

<html:base />
</head>

<body vlink="#0000FF" class="BodyStyle">
	<body class="mainbody" vlink="#0000FF">
	<table class="MainTable" id="scrollNumber1" name="encounterTable"
		style="margin: 0px;">
		<tr class="topbar">
			<td class="MainTableTopRowLeftColumn" width="60px">eAAPS</td>
			<td class="MainTableTopRowRightColumn">
				<table class="TopStatusBar">
					<tr>
						<td>Electronic Asthma Action Plan Status Information</td>
						<td style="text-align: right;"></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table cellspacing="0" id="queue_provider_table" style="margin: 0px;">
		<tr>
			<td colspan="2"><a id="dbInfo"></a></td>
		</tr>
		<tr>
			<td>

	<c:choose>
		<c:when test="${empty eaapsInfo}">
			<h4>eAAPS information is not available</h4>
		</c:when>
		<c:otherwise>
			<h2 style="margin: 1em;">
				<c:out value="${eaapsInfo.message}" />
			</h2>

			<c:choose>
				<c:when test="${not empty eaapsInfo.url}">
					<p style="padding: 0 2em;">
						<a href="${eaapsInfo.url}" target="_blank">${eaapsInfo.url}</a>
					</p>
				</c:when>
				<c:otherwise>
					<p style="padding: 0 2em;">
						URL is not provided by AAP server
					</p>
				</c:otherwise>
			</c:choose>

			<ul>
				<c:choose>
					<c:when test="${eaapsInfo.eligibleForStudy}">
						<li>Patient is eligible for study</li>
					</c:when>
					<c:otherwise>
						<li>Patient is <b>not</b> eligible for study
						</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${eaapsInfo.questionnaireStarted}">
						<li>Patient started the questionnaire</li>
					</c:when>
					<c:otherwise>
						<li>Patient hasn't started the questionnaire</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${eaapsInfo.medsConfirmed}">
						<li>Patient medications are confirmed</li>
					</c:when>
					<c:otherwise>
						<li>Patient medications haven't been confirmed</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${eaapsInfo.recommendationsAvailable}">
						<li>Recommendations are available for this patient</li>
					</c:when>
					<c:otherwise>
						<li>Recommendations are <b>not</b> available for this patient
						</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${eaapsInfo.recommendationsReviewStarted}">
						<li>Recommendations review completed for this patient</li>
					</c:when>
					<c:when test="${eaapsInfo.recommendationsReviewCompleted}">
						<li>Recommendations review completed for this patient</li>
					</c:when>
					<c:otherwise>
						<li>Recommendations review hasn't started for this patient</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${eaapsInfo.aapAvailable}">
						<li>Asthma action plan is available for this patient</li>
					</c:when>
					<c:otherwise>
						<li>Asthma action plan is <b>not</b> available for this
							patient
						</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${eaapsInfo.aapReviewStarted}">
						<li>Asthma action plan review has started for this patient</li>
					</c:when>
					<c:when test="${eaapsInfo.aapReviewCompleted}">
						<li>Asthma action plan review completed for this patient</li>
					</c:when>
					<c:otherwise>
						<li>Asthma action plan hasn't been reviewed for this patient</li>
					</c:otherwise>
				</c:choose>
			</ul>
		</c:otherwise>
	</c:choose>
	
	<button
		style="margin: 1em 3em;" 
		onclick="window.close()">Close</button>
	
	</td>
	</tr>
	</table>
</body>


</html>