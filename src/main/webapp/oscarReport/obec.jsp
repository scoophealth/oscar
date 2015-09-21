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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_billing" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting&type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page
	import="java.math.*, java.util.*, java.sql.*, oscar.*, oscar.util.DateUtils, java.net.*"%>
<%
	if (session.getValue("user") == null)
		response.sendRedirect("../logout.jsp");
	String user_no;
	user_no = (String) session.getAttribute("user");
%>

<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />

<%
	GregorianCalendar now = new GregorianCalendar();
	int curYear = now.get(Calendar.YEAR);
	int curMonth = (now.get(Calendar.MONTH) + 1);
	int curDay = now.get(Calendar.DAY_OF_MONTH);
	DateUtils dateUtils = new DateUtils();
	String tomorrowDate = dateUtils.NextDay(curDay, curMonth, curYear);
	String clinic = "";
	Properties proppies = oscar.OscarProperties.getInstance();
	String homepath = proppies.getProperty("DOCUMENT_DIR");
	session.setAttribute("obecdownload", homepath);
%>
<%
	int flag = 0, rowCount = 0;
	String obectxt = (String) request.getAttribute("obectxt") == null
			? ""
			: (String) request.getAttribute("obectxt");
	String xml_vdate = request.getParameter("xml_vdate") == null
			? tomorrowDate
			: request.getParameter("xml_vdate");
	String numDays = request.getParameter("numDays") == null
			? "4"
			: request.getParameter("numDays");
%>


<div class="page-header">
	<h4>
		Overnight Batch Eligibility Checking Report
		<div class="pull-right">
			<button name="print" onclick="window.print()" class="btn">
				<i class="icon-print icon-black"></i>
				<bean:message key="global.btnPrint" />
			</button>
		</div>
	</h4>
</div>


<form action="${ctx}/oscarReport/obec.do" class="well form-horizontal"
	id="obecForm">
	<fieldset>
		<h4>
			OBEC Report <br> <small>Please select the service begin
				date and number of days.</small>
		</h4>
		<div class="row-fluid">
			<div class="control-group">
				<label class="control-label">Begin Date</label>
				<div class="controls">

					<input id="xml_vdate" type="text" name="xml_vdate"
						value="<%=xml_vdate%>" placeholder="Service Begin Date">
				</div>
			</div>
			<div class="control-group" id="providerDiv">
				<label class="control-label">Days</label>

				<div class="controls">

					<input type="text" id="numDays" name="numDays" value="<%=numDays%>"
						class="input-mini">
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<input type="submit" name="Submit" value="Create Report"
						class="btn btn-primary">
				</div>
			</div>
		</div>

	</fieldset>
</form>

<%
	if (request.getAttribute("obectxt") != null)
		if (obectxt.compareTo("0") != 0 && obectxt.compareTo("") != 0) {
%>
<div class="alert alert-success">
	<h4>Success!</h4>
	<a
		href="${ctx}/servlet/OscarDownload?homepath=obecdownload&filename=<%=obectxt%>">File
		Created <%=obectxt%></a>
</div>
<%
	} else {
%>
<div class="alert alert-block">
	<h4>Warning!</h4>
	File not created!
</div>
<%
	}
%>


<script>
	var startDt = $("#xml_vdate").datepicker({
		format : "yyyy-mm-dd"
	});

	$(document).ready(function() {
		$('#obecForm').validate({
			rules : {
				xml_vdate : {
					required : true,
					oscarDate : true
				},
				numDays : {
					required : true,
					number : true
				}
			}
		});
	});

	registerFormSubmit('obecForm', 'dynamic-content');
</script>
