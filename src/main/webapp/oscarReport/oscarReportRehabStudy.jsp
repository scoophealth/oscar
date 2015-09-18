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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
	GregorianCalendar now = new GregorianCalendar();
	GregorianCalendar cal = (GregorianCalendar) now.clone();
	String today = now.get(Calendar.YEAR) + "-"
			+ (now.get(Calendar.MONTH) + 1) + "-"
			+ now.get(Calendar.DATE);
%>
<%@ page import="java.util.*,oscar.oscarReport.data.*, java.net.*"%>

<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />

<%!String[][] forms = {{"form2MinWalk", "2 Minutes Walk"},
			{"formCESD", "CESD"}, {"formCaregiver", "Caregiver"},
			{"formCostQuestionnaire", "Cost Questionnaire"},
			{"formFalls", "Falls History"},
			{"formGripStrength", "Grip Strength"},
			{"formHomeFalls", "Home Falls"}, {"formIntakeInfo", "Intake"},
			{"formLateLifeFDIDisability", "Late Life FDI Disability"},
			{"formLateLifeFDIFunction", "Late Life FDI Function"},
			{"formSF36", "SF36"}, {"formSF36Caregiver", "SF36 Caregiver"},
			{"formSelfAdministered", "Self Administered"},
			{"formSelfEfficacy", "Self Efficacy"},
			{"formSelfManagement", "Self Management"},
			{"formTreatmentPref", "Treatment Preference"},
			{"formInternetAccess", "Internet Access"},
			{"formSatisfactionScale", "Satisfaction Scale"}};%>

<div class="page-header">
	<h4>Rehab Study Reports</h4>
</div>

<form class="well form-horizontal"
	action="${ctx}/oscarReport/RptRehabStudy.do" id="rehabForm">
	<fieldset>
		<h4>
			Rehab Report <br> <small>Please select the form, begin
				and end dates.</small>
		</h4>
		<div class="row-fluid">
			<div class="control-group">
				<label class="control-label">Form</label>
				<div class="controls">

					<select name="formName">
						<%
							for (int i = 0; i < forms.length; i++) {
								String selected = new String();
								String selectedForm = request.getParameter("formName");
								if (selectedForm != null && selectedForm.equals(forms[i][0])) {
									selected = " selected=\"selected\" ";
								}
						%>
						<option value="<%=forms[i][0]%>" <%=selected%>><%=forms[i][1]%></option>
						<%
							}
						%>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Start Date</label>
				<div class="controls">
					<input type="text" id="startDate" name="startDate"
						value="<%=request.getParameter("startDate") != null ? request
					.getParameter("startDate") : ""%>" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">End Date</label>
				<div class="controls">
					<input type="text" id="endDate" name="endDate"
						value="<%=request.getParameter("endDate") != null ? request
					.getParameter("endDate") : ""%>" />
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn btn-primary">Run Report</button>
				</div>
			</div>
		</div>
	</fieldset>
</form>


<c:if test="${not empty headers}">
	<table id="resultTable"
		class="table table-bordered table-striped table-condensed table-hover" style="font-size:12px;">
		<thead>
			<tr>
				<c:forEach items="${headers}" var="item">
					<th>${item}</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${rows}" var="row">
				<tr>
					<c:forEach items="${row}" var="rowItem">
						<td>${rowItem}</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</c:if>

<script>
	var startDt = $("#startDate").datepicker({
		format : "yyyy-mm-dd"
	});

	var endDt = $("#endDate").datepicker({
		format : "yyyy-mm-dd"
	});
	$(document).ready(function() {
		$('#rehabForm').validate({
			rules : {
				startDate : {
					required : true,
					oscarDate : true
				},
				endDate : {
					required : true,
					oscarDate : true
				}
			}
		});
	});

	registerFormSubmit('rehabForm', 'dynamic-content');

	$(document).ready(function() {
		$('#resultTable').dataTable( {
			"sScrollX": "720",
		} );
	} );
</script>
