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
<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.reporting");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@page import="java.io.File"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<%@page import="oscar.util.*, oscar.*, java.util.*"%>

<div class="page-header">
	<h4>
		<bean:message key="admin.admin.serverLog" />
	</h4>
</div>

<%
	String reportDate = request.getParameter("reportDate");
	String reportType = request.getParameter("reportType");
	boolean runReport;
	if (reportDate == null) {
		reportDate = UtilDateUtilities.getToday("yyyy-MM-dd");
		runReport = false;
	} else {
		runReport = true;
	}
	if (reportType == null) {
		reportType = "general";
	}
%>
<form id="logForm" action="${ctx}/admin/oscarLogging.jsp" class="well form-horizontal">

	<fieldset>
		<h4>
			View Server Log <br>
			<small>Please select the date to view report on and log type.</small>
		</h4>
				<div class="control-group">
					<label class="control-label">Date</label>
					<div class="controls">
						<input type="text" id="reportDate" name="reportDate" class="span3"
							size="10" value="<%=reportDate%>">
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">Select Report to view</label>
					<div class="controls">
						<select name="reportType" id="reportType" class="span3">
							<option value="general" <%if (reportType.equals("general")) {%>
								selected <%}%>>General Report</option>
							<option value="mysql" <%if (reportType.equals("mysql")) {%>
								selected <%}%>>MySQL Transaction Report</option>
						</select>
					</div>
				</div>
				<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn btn-primary">
						<i class="icon-download-alt icon-white"></i> Get Report
					</button>
				</div>
			</div>
	</fieldset>
</form>

<%
	if (runReport) {
		Properties pr = OscarProperties.getInstance();
		String path = pr.getProperty("LOGGING_PATH");
		String suffix = reportDate.replaceAll("-", "");
		String fileName = "";
		String contentString = "";

		if (reportType.equals("general")) {
			fileName = path + "report" + suffix + ".html";
		} else if (reportType.equals("mysql")) {
			fileName = path + "reportmysql" + suffix + ".html";
		}

		String temp = FileUtils.readFileToString(new File(fileName),
				"UTF-8");
		out.write("<pre id=\"log-results\">"+temp+"</pre>");
	}
%>

<script>
	var startDt = $("#reportDate").datepicker({
		format : "yyyy-mm-dd"
	});
	
	var endDt = $("#endDate").datepicker({
		format : "mm/yyyy",
		viewMode : "months",
		minViewMode : "months"
	});
	
	$(document).ready(function() {
		 $('#logForm').validate(
		 {
		  	rules: { 
		  		reportDate: { 
			  		required: true,
			  		oscarDate: true
		 		}  
		  }
		 });
	});
	
	registerFormSubmit('logForm', 'dynamic-content');
	
</script>
