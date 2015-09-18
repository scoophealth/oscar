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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.*"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.common.dao.SecRoleDao"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.text.DateFormatSymbols"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.web.*"%>

<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />


<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	Integer facilityId=loggedInInfo.getCurrentFacility().getId();

	List<OcanSubmissionLog> submissions = OcanReportUIBean.getAllOcanSubmissions(facilityId);
	List<OcanStaffForm> unsentForms = OcanReportUIBean.getAllUnsubmittedOcanForms(facilityId);
	
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
	java.text.SimpleDateFormat formatter2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
%>

<div class="page-header">
	<h4>OCAN IAR Report - v2.0.6</h4>
</div>


<form method="post" id="ocanForm" action="${ctx}/oscarReport/ocan_report_export_iar.jsp">
	<h5>IAR Submissions</h5>
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>Submission Id</th>
				<th>Submission Date</th>
				<th># of Records</th>
				<th>Result</th>
			</tr>
		</thead>
		<%
			for(OcanSubmissionLog submission:submissions) {
		%>
		<tr>
			<td><a href="#"
				onclick="popup('ocan_iar_detail.jsp?submissionId=<%=submission.getId()%>');return false;"><%=submission.getId()%></a></td>
			<td><%=formatter.format(submission.getSubmitDateTime())%></td>
			<td><%=submission.getRecords().size()%></td>
			<td><%=submission.getResult()%></td>
		</tr>
		<%
			}
		%>
	</table>

	<h5>Pending OCAN Forms</h5>
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>
					<label class="checkbox inline">
						<input type="checkbox" name="checkAll2" id="checkA" />All
					</label>
				</th>
				<th>Form Id</th>
				<th>Date Started</th>
				<th>Date Completed</th>
				<th>Client</th>
				<th>Provider</th>
			</tr>
		</thead>
		<%
			for(OcanStaffForm form:unsentForms) {
		%>
		<tr>
			<td>
				<label class="checkbox inline">
				<input type="checkbox" name="test1"
				value="<%=form.getAssessmentId()%>">
				</label>
				</td>
			<td><%=form.getId()%></td>
			<td><%=formatter2.format(form.getStartDate())%></td>
			<td><%=formatter2.format(form.getCompletionDate())%></td>
			<td><%=form.getClientId()%></td>
			<td><%=form.getProviderName()%></td>
		</tr>
		<%
			}
		%>
	</table>
	<div class="control-group">
	<div class="controls">
		<button id="subPending" onclick="submitPending();return false;"
			class="btn btn-primary">Submit Pending Records</button>
		<button id="subPending" onclick="submitManual();return false;"
			class="btn">Generate Manual File</button>
	</div>
	</div>
</form>

<script>
	$(function() {

		$("#checkA").bind("click", function() {
			$("[name = test1]:checkbox").attr("checked", this.checked);

		});

		$("[name = test1]:checkbox").bind(
				"click",
				function() {
					var $chk = $("[name = test1]:checkbox");
					$("#checkA").attr("checked",
							$chk.length == $chk.filter(":checked").length);
					if ($(this).attr("checked")) {
						$(this).attr("checked", true);
					} else {
						$(this).attr("checked", false);
					}

				})
	});

	function submitPending() {
		$("#subPending").val("Please Wait");
		$("#SubPending").attr('disabled', 'true');

		var arrChk1 = $("input[name=test1][checked]");

		var fieldList = [];
		for ( var i = 0; i < arrChk1.length; i++) {

			fieldList.push(arrChk1[i].value);

		}
		jQuery
				.ajax({
					url : "${ctx}/OcanIarSubmit.do?method=submit&account="
							+ fieldList + "",
					dataType : "html",
					success : function(data) {
						$('#ocanForm').action = '${ctx}/oscarReport/ocan_iar.jsp?assessmentIds='
								+ fieldList;
						submitForm('ocanForm', 'dynamic-content');
					},
					error : function() {
						alert('An error occurred');
						$("#subPending").val("Submit Pending Records");
						$("#SubPending").attr('disabled', 'false');

					}
				});

	}

	function submitManual() {
		var arrChk1 = $("input[name=test1][checked]");
		//checkbox value1
		var fieldList = [];
		for ( var i = 0; i < arrChk1.length; i++) {

			fieldList.push(arrChk1[i].value);

		}
		$('#ocanForm').action = '${ctx}/oscarReport/ocan_report_export_iar_manual.jsp?assessmentIds='
				+ fieldList;
		$('#ocanForm').submit();

	}
</script>
