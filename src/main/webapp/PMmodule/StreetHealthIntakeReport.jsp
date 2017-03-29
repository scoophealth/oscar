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
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib uri="/WEB-INF/streethealth.tld" prefix="sh"%>

<%
String project_home = request.getContextPath().substring(1);
%>


<html:html xhtml="true" locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Street Health Mental Health Report</title>
<html:base />
</head>
<body>
StreetHealth Intake Report
<br />

<script type="text/javascript">
			function download() {
				location.href="/<%=project_home%>/PMmodule/StreetHealthIntakeReportAction.do?action=download&startDate=<%=request.getParameter("startDate")%>";
			}
		</script>
<table height="15" align="center">
	<tr>
		<td class="style76" align="center"><input type="button"
			name="backToClientSearch" value="Back"
			onclick="javascript:history.back();" /> <input type="button"
			name="downLoadCSVFile" value="Download"
			onclick="javascript:download();" /></td>
	</tr>
</table>


<table width="100%" border="1">

	<!-- header -->
	<tr>
		<td>&nbsp;</td>
		<td>Data Element</td>
		<td>Valid Categories</td>
		<td>Data for CM</td>

		<c:forEach var="date" items="${dates}">
			<td><fmt:formatDate value="${date.startDate}"
				pattern="yyyy/MM/dd" />- <fmt:formatDate value="${date.endDate}"
				pattern="yyyy/MM/dd" /></td>
		</c:forEach>
	</tr>

	<sh:report_element num="7" question="Total Service Recipients"
		answerProps="total_service_recipient"></sh:report_element>
	<sh:report_element num="8" question="Gender" answerProps="gender"></sh:report_element>
	<sh:report_element num="9" question="Age" answerProps="age"></sh:report_element>
	<sh:report_element num="10" question="Service Recipient Location"
		answerProps="service_recipient_location"></sh:report_element>
	<sh:report_element num="11" question="Aboriginal Origin"
		answerProps="aboriginal"></sh:report_element>
	<sh:report_element num="12"
		question="Service Recipient Preferred Language" answerProps="language"></sh:report_element>
	<sh:report_element num="13" question="Baseline Legal Status"
		answerProps="blegal"></sh:report_element>
	<sh:report_element num="14" question="Current Legal Status"
		answerProps="blegal"></sh:report_element>
	<sh:report_element num="15" question="Community Treatment Orders"
		answerProps="cto"></sh:report_element>
	<sh:report_element num="16" question="Diagnostic Categories"
		answerProps="diag"></sh:report_element>
	<sh:report_element num="16a" question="Other Illness Information"
		answerProps="illness"></sh:report_element>
	<sh:report_element num="17" question="Presenting Issues"
		answerProps="presenting_issues"></sh:report_element>
	<sh:report_element num="18" question="Source of Referral"
		answerProps="referral"></sh:report_element>
	<sh:report_element num="19" question="Exit Disposition"
		answerProps="exit"></sh:report_element>
	<!-- <sh:report_element num="20" question="Baseline Psychiatric Hospitalizations" answerProps="psych_hospitalizations"></sh:report_element>
		-->
	<sh:report_element num="21"
		question="Current Psychiatric Hospitalizations"
		answerProps="cpsych_hospitalizations"></sh:report_element>

	<sh:report_element num="22" question="Baseline Living Arrangement"
		answerProps="living_arrangement"></sh:report_element>
	<sh:report_element num="23" question="Current Living Arrangement"
		answerProps="living_arrangement"></sh:report_element>
	<sh:report_element num="24" question="Baseline Primary Residence Type"
		answerProps="residence_type"></sh:report_element>
	<sh:report_element num="24a" question="Baseline Residence Status"
		answerProps="residence_status"></sh:report_element>
	<sh:report_element num="25" question="Current Primary Residence Type"
		answerProps="residence_type"></sh:report_element>
	<sh:report_element num="25a" question="Current Residence Status"
		answerProps="residence_status"></sh:report_element>
	<sh:report_element num="26" question="Baseline Employment Status"
		answerProps="employment_status"></sh:report_element>
	<sh:report_element num="27" question="Current Employment Status"
		answerProps="employment_status"></sh:report_element>
	<sh:report_element num="28" question="Baseline Educational Status"
		answerProps="educational_status"></sh:report_element>
	<sh:report_element num="29" question="Current Educational Status"
		answerProps="educational_status"></sh:report_element>
	<sh:report_element num="29a"
		question="Current Highest Level of Education"
		answerProps="level_education"></sh:report_element>
	<sh:report_element num="30" question="Baseline Primary Income Source"
		answerProps="income_source"></sh:report_element>
	<sh:report_element num="31" question="Current Primary Income Source"
		answerProps="income_source"></sh:report_element>
</table>

</body>
</html:html>
