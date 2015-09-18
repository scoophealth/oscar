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

<%@page import="java.util.*"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>

<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />

<div class="page-header">
	<h4>Provider Service Report Form</h4>
</div>

<form action="${ctx}/oscarReport/provider_service_report_export.jsp"
	class="well form-horizontal" id="psrForm">

	<fieldset>
		<h4>
			Export to csv <br>
			<small>This will provide a break down of all unique
				encounters of a demographic to a provider, broken down by month and
				for the entire interval as well. This only does the numbers for a
				program of type bed or service.</small>
		</h4>
		<div class="row-fluid">
			<div class="control-group">
				<label class="control-label">Start Date</label>
				<div class="controls">
					<input id="startDate" name="startDate" class="input-mini" size="7"
						type="text" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">EndDate (inclusive)</label>
				<div class="controls">
					<input id="endDate" name="endDate" class="input-mini" size="7"
						type="text" />
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn btn-primary">
						<i class="icon-download-alt icon-white"></i> Export
					</button>
				</div>
			</div>
		</div>
	</fieldset>
</form>

<script>
	var startDt = $("#startDate").datepicker({
		format : "mm/yyyy",
		viewMode : "months",
		minViewMode : "months"
	});

	var endDt = $("#endDate").datepicker({
		format : "mm/yyyy",
		viewMode : "months",
		minViewMode : "months"
	});

	$(document).ready(function() {
		$('#psrForm').validate({
			rules : {
				startDate : {
					required : true,
					oscarMonth : true
				},
				endDate : {
					required : true,
					oscarMonth : true
				}
			},
			submitHandler : function(form) {
				form.submit();
			}
		});
	});
</script>