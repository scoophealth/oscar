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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="oscar.oscarSurveillance.*,java.util.*"%>

<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />

<%
	String surveyId = request.getParameter("surveyId");
	SurveillanceMaster sMaster = SurveillanceMaster.getInstance();
	Survey survey = sMaster.getSurveyById(surveyId);

	ArrayList answerList = survey.getAnswerCount(surveyId);
	ArrayList statusList = survey.getStatusCount(surveyId);

	SurveyInfo surveyInfo = new SurveyInfo();

	ArrayList fileList = surveyInfo.getFileNames(surveyId);
	int total = 0;
%>
<head>
<link href="/oscar/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="/oscar/css/datepicker.css" rel="stylesheet" type="text/css">
<link href="/oscar/css/DT_bootstrap.css" rel="stylesheet" type="text/css">
<link href="/oscar/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
</head>
<div class="page-header">
	<h4>
		<bean:message key="oscarSurveillance.Surveillance.msgSurveillance" />
	</h4>
</div>


<ul class="nav nav-pills">
	<!--tab menus-->
	<li class="active"><a data-toggle="tab" href="#ans-details">Files
			<span class="badge badge-important"><%=survey.hasExport() ? fileList.size() + "" : ""%></span>
	</a></li>
	<li class=""><a data-toggle="tab" href="#qstn-details">Question
			details</a></li>
	<li class=""><a data-toggle="tab" href="#ans-details">Answer
			details <span class="badge badge-important"><%=answerList.size()%></span>
	</a></li>
	<li class=""><a data-toggle="tab" href="#st-details">Status
			details <span class="badge badge-important"><%=statusList.size()%></span>
	</a></li>
	<!--/tab menus-->
</ul>

<div class="tab-content">
	<div class="tab-pane fade active in" id="file-details">
		<%
			if (survey.hasExport()) {
		%>
		<div class="pull-right">
			<a
				href="CreateSurveillanceFile.do?surveyId=<%=survey.getSurveyId()%>"
				class="btn btn-primary contentLink">Create File</a>
		</div>

		<table class="table table-bordered table-striped table-hover">
			<tbody>
				<tr>

					<%
						for (int i = 0; i < fileList.size(); i++) {
								String[] file = (String[]) fileList.get(i);
					%>
					<th><a
						href="../Download?dir_property=surveillance_directory&filename=<%=file[0]%>"
						target="_blank" title="<%=file[0]%> create on :<%=file[1]%>">
							<%=file[0]%>
					</a></th>
					<%
						}
					%>
				
			</tbody>
		</table>
		<%
			} else {
		%>
		<strong>No Export </strong>
		<%
			}
		%>
	</div>
	<div class="tab-pane fade in" id="qstn-details">
		<dl class="dl-horizontal">
			<dt>Survey Question</dt>
			<dd><%=survey.getSurveyQuestion()%></dd>
			<dt>Randomness</dt>
			<dd><%=survey.getRandomness()%></dd>
			<dt>Period</dt>
			<dd><%=survey.getPeriod()%></dd>
		</dl>
	</div>
</div>

<div class="tab-pane fade in" id="ans-details">

	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>Answer</th>
				<th>Count</th>
			</tr>
		</thead>
		<tbody>
			<%
				for (int i = 0; i < answerList.size(); i++) {
					String[] s = (String[]) answerList.get(i);
			%>
			<tr>
				<td><%=survey.getAnswerStringById(s[0])%></td>
				<td><%=s[1]%></td>
			</tr>
			<%
				}
			%>

		</tbody>
	</table>
</div>
<div class="tab-pane fade in" id="st-details">

	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>Status</th>
				<th>Value</th>
			</tr>
		</thead>
		<tbody>
			<%
				for (int i = 0; i < statusList.size(); i++) {
					String[] s = (String[]) statusList.get(i);
					total += Integer.parseInt(s[1]);
			%>
			<tr>
				<td><%=s[0]%></td>
				<td><%=s[1]%></td>
			</tr>
			<%
				}
			%>
			<tr>
				<td>Total</td>
				<td><%=total%></td>
			</tr>
		</tbody>
	</table>
</div>

