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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%-- This JSP is the first page you see when you enter 'report by template' --%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
	String roleName$ = (String) session.getAttribute("userrole") + ","
			+ (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.reporting" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_admin&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.apache.commons.lang.StringUtils"%>

<%
	LoggedInInfo loggedInInfo = LoggedInInfo
			.getLoggedInInfoFromSession(request);
	Provider provider = loggedInInfo.getLoggedInProvider();
%>
<html:html locale="true">
<head>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/global.js"></script>
<title>OSCAR Products</title>
<link href="<%=request.getContextPath()%>/css/bootstrap.css"
	rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/datepicker.css"
	rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/DT_bootstrap.css"
	rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/bootstrap-responsive.css"
	rel="stylesheet" type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/font-awesome.min.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">

<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/bootstrap.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/bootstrap-datepicker.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery.validate.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery.dataTables.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/DT_bootstrap.js"></script>
<script type="text/javascript" language="JavaScript"
	src="<%=request.getContextPath()%>/share/javascript/Oscar.js"></script>


<link
	href="<%=request.getContextPath()%>/library/bootstrap2-datepicker/datepicker3.css"
	rel="stylesheet">

<script
	src="<%=request.getContextPath()%>/library/bootstrap2-datepicker/bootstrap-datepicker.js"></script>

<script>
	$(document)
			.ready(
					function() {

						listReports();

						$("#btnAdd").bind('click', function() {
							/*
							var e = {
								name : 'testing',
								eformId : 3,
								expiryDate : new Date(),
								providerNo : '999998'
							};
							$.ajax({
								url : '../../ws/rs/reporting/eformReportTool/add',
								type : 'POST',
								data : JSON.stringify(e),
								contentType : "application/json; charset=utf-8",
								dataType : 'json',
								success : function(data) {
									//	alert(data);
									listReports();
								},
								error : function(data) {
									alert('error:' + data);
								}
							});
							 */
							fetchEFormsAndOpenDialog();

						});

						$("#new-report")
								.dialog(
										{
											autoOpen : false,
											height : 525,
											width : 825,
											modal : true,
											buttons : {
												"Add" : {
													class : "btn btn-primary",
													text : "Save",
													click : function() {
														var e = {
															name : $(
																	"#eformReportToolName")
																	.val(),
															eformId : $(
																	"#eformReportToolEformId")
																	.val()
														};
														
														if ($(
																"#eformExpiryDate")
																.val() != null
																&& $(
																		"#eformExpiryDate")
																		.val()
																		.length > 0) {
															e.expiryDateString = $(
																	"#eformExpiryDate")
																	.val();
														}
														
														if ($("#eformStartDate").val() != null	&& $("#eformStartDate").val().length > 0) {
															e.startDateString = $("#eformStartDate").val();
														}
														if ($("#eformEndDate").val() != null	&& $("#eformEndDate").val().length > 0) {
															e.endDateString = $("#eformEndDate").val();
														}
														
														if ($("#eformReportToolUseAsTableName").attr('checked') == 'checked') {	
															e.useNameAsTableName = true;
														}
														
														//alert(JSON.stringify(e));

														$
																.ajax({
																	url : '../../ws/rs/reporting/eformReportTool/add',
																	type : 'POST',
																	data : JSON
																			.stringify(e),
																	contentType : "application/json; charset=utf-8",
																	dataType : 'json',
																	success : function(
																			data) {
																		//	alert(data);
																		listReports();
																	},
																	error : function(
																			data) {
																		alert('error:'
																				+ data);
																	}
																});

														$(this).dialog("close");

													}
												},
												Cancel : {
													class : "btn",
													text : "Cancel",
													click : function() {

														$(this).dialog("close");
													}
												}
											},
											close : function() {

											}
										});

					});

	function fetchEFormsAndOpenDialog() {
		jQuery.getJSON("../../ws/rs/forms/allEForms", {}, function(xml) {
			$("#eformReportToolEformId option").remove();
			//alert(JSON.stringify(xml));
			if (xml.content) {
				for (var x = 0; x < xml.content.length; x++) {
					$("#eformReportToolEformId").append(
							"<option value=\""+xml.content[x].id+"\">"
									+ xml.content[x].formName + "</option>");
				}
			}
			$('#new-report').dialog('open');

		});
	}

	function populate(eftId) {
		$("#loader_"+eftId).show();
		
		var e = {
			id : eftId
		};
		$.ajax({
			url : '../../ws/rs/reporting/eformReportTool/populate',
			type : 'POST',
			data : JSON.stringify(e),
			contentType : "application/json; charset=utf-8",
			dataType : 'json',
			success : function(data) {
				listReports();
			},
			error : function(data) {
				$("#loader_"+eftId).hide();
				alert('error:' + data);
			}
		});
	}

	function markLatest(eftId) {
		$("#loader_"+eftId).show();
		var e = {
			id : eftId
		};
		$.ajax({
			url : '../../ws/rs/reporting/eformReportTool/markLatest',
			type : 'POST',
			data : JSON.stringify(e),
			contentType : "application/json; charset=utf-8",
			dataType : 'json',
			success : function(data) {
				listReports();
			},
			error : function(data) {
				$("#loader_"+eftId).hide();
				alert('error:' + data);
			}
		});
	}

	function removeItem(eftId) {
		if (confirm("Are you sure? This will delete your temporary table")) {
			var e = {
				id : eftId
			};
			$("#loader_"+eftId).show();
			$.ajax({
				url : '../../ws/rs/reporting/eformReportTool/remove',
				type : 'POST',
				data : JSON.stringify(e),
				contentType : "application/json; charset=utf-8",
				dataType : 'json',
				success : function(data) {
					listReports();

				},
				error : function(data) {
					$("#loader_"+eftId).hide();
					alert('error:' + data);
				}
			});
		}
	}

	function listReports() {
		$
				.ajax({
					url : '../../ws/rs/reporting/eformReportTool/list',
					type : 'GET',
					dataType : 'json',
					success : function(data) {

						$("#listTable tbody").empty();

						if (data.content) {
							for (var x = 0; x < data.content.length; x++) {
								var e = data.content[x];
								var dateLastPopulated = "";
								var expiryDate = "";
								if (e.dateLastPopulated != null) {
									dateLastPopulated = new Date(
											e.dateLastPopulated);
									
									
								}
								if(e.expiryDate != null) {
									expiryDate = new Date(e.expiryDate);
								}
								$("#listTable tbody")
										.append(
												"<tr> <td><span id='loader_" + e.id + "' style='display:none'><img src='../../images/DMSLoader.gif'></span></td><td><a onClick=\"removeItem('"
														+ e.id
														+ "')\">(Remove)</a>&nbsp;<a onClick=\"populate('"
														+ e.id
														+ "')\">(Populate)</a> &nbsp;<a onClick=\"markLatest('"
														+ e.id
														+ "')\">(MarkLatest)</a></td> <td>"
														+ e.name
														+ "</td> <td>"
														+ e.tableName
														+ "</td> <td>"
														+ e.eformName
														+ "</td> <td>"
														+ new Date(
																e.dateCreated)
														+ "</td> <td>"
														+ expiryDate
														+ "</td> <td>"
														+ dateLastPopulated
														+ "</td>  <td>"
														+ e.latestMarked
														+ "</td>  <td>"
														+ e.numRecordsInTable
														+ "</td></tr>");
							}
						}

					},
					error : function(data) {
						alert('error:' + data);
					}
				});
	}
</script>

<style>
.red {
	color: red
}
</style>

</head>

<body vlink="#0000FF" class="BodyStyle">
	<h4>EForm Reporting Tool</h4>


	<!--  display list of existing tables made, with ability to delete any one of them -->
	<table id="listTable" class="table" width="100%">
		<thead>
			<th></th>
			<th></th>
			<th>Name</th>
			<th>Table Name</th>
			<th>Eform Name</th>
			<th>Created</th>
			<th>Expires</th>
			<th>Last Populated</th>
			<th>Latest Marked</th>
			<th># of Records</th>
		</thead>
		<tbody></tbody>
	</table>

	<!-- button to add new  -->
	<button id="btnAdd">Add New</button>

	<!-- add new should show form with name, eform name, and expiry date -->

	<div id="new-report" title="Create new OSCAR EForm Report table">
		<p class="validateTips"></p>

		<form id="reportForm">

			<div>
				<div class="controls controls-row">
					<div class="control-group span4" id="group1">
						<label class="control-label" for="eformReportToolEformId">Choose
							EForm:</label>
						<div class="controls">
							<select id="eformReportToolEformId"
								name="eformReportTool.eformId">
								<option></option>
							</select>
						</div>
					</div>
				</div>
				
				<div class="controls controls-row">
					<div class="control-group span4" id="group2">
						<label class="control-label" for="eformReportToolName">Name:</label>
						<div class="controls">
							<input type="text" name="eformReportTool.name"
								id="eformReportToolName" />
								
							<input type="checkbox" name="eformReportTool.useAsTableName" id="eformReportToolUseAsTableName"/>Use as table name

						</div>
					</div>
				</div>
				<div class="controls controls-row">


					<div class="control-group span8" id="group3">
						<label class="control-label" for="eformExpiryDate">Expiry
							Date:</label>
						<div class="controls">
							<input type="text" name="eformReportTool.expiryDate"
								id="eformExpiryDate" value="" />
						</div>
					</div>

				</div>

			</div>
			
			<div class="controls controls-row">
				<div class="control-group span8" id="group5">
					<label class="control-label" for="eformReportToolStartDate">Start Date (opt):</label>
						<div class="controls">
							<input type="text" name="eformReportTool.startDate"
								id="eformStartDate" value="" />
						</div>
						
						<label class="control-label" for="eformReportToolStartDate">End Date (opt):</label>
						<div class="controls">
							<input type="text" name="eformReportTool.endDate"
								id="eformEndDate" value="" />
						</div>
					
						
				</div>
					
			</div>

		</form>
	</div>


	<script type="text/javascript">
		$('#eformExpiryDate').datepicker({
			format : "yyyy-mm-dd",
			todayBtn : "linked",
			autoclose : true,
			todayHighlight : true
		});
		$('#eformStartDate').datepicker({
			format : "yyyy-mm-dd",
			todayBtn : "linked",
			autoclose : true,
			todayHighlight : true
		});
		$('#eformEndDate').datepicker({
			format : "yyyy-mm-dd",
			todayBtn : "linked",
			autoclose : true,
			todayHighlight : true
		});
	</script>

</body>
</html:html>
