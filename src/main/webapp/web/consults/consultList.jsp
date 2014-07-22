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
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/library/bootstrap/3.0.0/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/library/bootstrap/3.0.0/assets/css/DT_bootstrap.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/datepicker.css">

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/library/bootstrap/3.0.0/assets/js/DT_bootstrap.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/bootstrap-datepicker.js"></script>

<style>
	.datepicker {z-index: 9999;}
	.date-input {width: 80px;}
	#consultList_length {display: inline-block !important;}
	.hidden {display: none;}
	.team {display: inline-block !important; vertical-align: top; margin-right: 20px;}
	#search-options{margin-left: 20px;}
	.center {text-align: center;}
</style>
<%
	String curProvider_no = (String) session.getAttribute("user");
	String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    String basePath = request.getContextPath();
	String userName = request.getParameter("userName");
	String providerNo = request.getParameter("providerNo");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String today = formatter.format(new Date());
	
	String team = StringUtils.defaultString((String) request.getAttribute("teamVar"));
    boolean isSiteAccessPrivacy=false;
    boolean isTeamAccessPrivacy=false; 
%>
	<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false"> <%isSiteAccessPrivacy=true; %></security:oscarSec>
	<security:oscarSec objectName="_team_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false"> <%isTeamAccessPrivacy=true; %></security:oscarSec>
<%
EctConsultationFormRequestUtil consultUtil = new EctConsultationFormRequestUtil();
if (isTeamAccessPrivacy) {
	consultUtil.estTeamsByTeam(curProvider_no);
}
else if (isSiteAccessPrivacy) {
	consultUtil.estTeamsBySite(curProvider_no);
}
else {
	consultUtil.estTeams();
}
%>
<div id="consult-list" data-ng-init="init(<%=curProvider_no%>)">
	<h4 style="display: inline">Consultation List</h4> | 
	<a href="javascript:popupOscarConsultationConfig(700,960,'<%=request.getContextPath()%>/oscarEncounter/oscarConsultationRequest/config/ShowAllServices.jsp')" class="consultButtonsActive">
    	<bean:message key="consultationList.editSpecialists"/>
    </a>&nbsp;
    
    <!-- 
    <input type="text" class="disabled" value="Patient Name"/>&nbsp;
	<button id="newConsult" class="btn disabled"><i class="icon-plus"></i>&nbsp;<bean:message key="consultationList.btn.newConsult"/></button>
	-->
	<br /><br />
<div id="teamDiv" class="team">
	<label><bean:message key="consultationList.selectTeam" />:</label>
	<select id="team" name="team">                                
		<option value=""><bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.formViewAll"/></option>                                
	    <%                              
	    if (team.equals("-1")) { 
	    %>
	    	<option value="-1" selected ><bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.formTeamNotApplicable"/></option>
	    <%
	    } else {
	    %>
	    	<option value="-1"><bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.formTeamNotApplicable"/></option>
	    <% 
	    }
	    for (Object aTeam : consultUtil.teamVec){
	       	if (((String)aTeam).equals(team)){
	    %>
	        <option value="<%=aTeam%>" selected><%=aTeam%></option>
	    <%
	    	} else {
	    %>
	    	<option value="<%=aTeam%>"><%=aTeam%></option>
	    <%
	    	}
	    }%>
	</select>
</div>
<table class="table table-striped table-hover" id="consultList">
</table>
</div>
<input type="hidden" id="userName" value="<%=userName%>" />
<input type="hidden" id="providerNo" value="<%=providerNo%>" />

<!-- Modal -->
<div id="searchModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="searchModalLabel" aria-hidden="true">
 	<div class="modal-dialog">
 		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h3 id="searchModalLabel"><bean:message key="consultationList.searchOptions" /></h3>
			</div>
			<div class="modal-body">
			
				<bean:message key="consultationList.startDate" />:
				<div class="input-append date" id="date-start" data-date="<%=today%>" data-date-format="dd-mm-yyyy">
					<input size="16" type="text" class="date-input" name="startDate" id="startDate"> <span class="add-on"><i
						class="icon-calendar"></i></span>
				</div>
				<br />
				<bean:message key="consultationList.endDate" />:
				<div class="input-append date" id="date-end" data-date="<%=today%>" data-date-format="dd-mm-yyyy">
					<input size="16" type="text" class="date-input" name="endDate" id="endDate"> <span class="add-on"><i class="icon-calendar"></i></span>
				</div>
				<div>
					Status: <select id="status" name="status" class="form-control">
						<option value="" selected="selected">All</option>
						<option value="1"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgNoth" /></option>
						<option value="2"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgSpecCall" /></option>
						<option value="3"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgPatCall" /></option>
						<option value="6"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgPreliminary" /></option>
						<option value="4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgCompleted" /></option>
					</select>
				</div>
				<p>
					<input id="referralDate" name="dateType" checked="checked" value="referralDate" type="radio"/> <bean:message key="consultationList.referralDate" />
				</p>
				<p>
					<input name="dateType" value="appointmentDate" type="radio"/> <bean:message key="consultationList.appointmentDate" />
				</p>
				<p>
					<input name="complete" id="complete" value="1" type="checkbox"> <bean:message key="consultationList.includeCompleted" />
				</p>
			</div>
			<div class="modal-footer">
				<input type="button" in="closeDiv" class="btn" data-dismiss="modal" aria-hidden="true" value="<bean:message key='global.close' />">
				<input id="reset" type="button" in="closeDiv" class="btn" aria-hidden="true" value="<bean:message key='global.reset' />">
				<input id="search" type="button" in="closeDiv" class="btn btn-primary" data-dismiss="modal" aria-hidden="true" value="<bean:message key='global.search' />"/>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function () {
	var withOption = false;
	var startDate, endDate, dateType, complete, team, status;
	var dataTable= $('#consultList').dataTable({
		"bProcessing": true,
        "bServerSide": true,
        "sAjaxSource": "<%=basePath%>/consults/Consultations.do",
		"aLengthMenu": [ 10, 25, 50, 100 ],
		"bFilter": true,
        "aoColumns": [
			{ "sTitle": "", "bSortable": false, "mData": "urgency", "sClass": "center", "mRender": function(data, type, row) {
				var urgencyId = parseInt(data);
				switch(urgencyId) {
				case 1:
					return '<i class="icon-exclamation-sign"></i>';
				case 5:
					return '<i class="icon-warning-sign"></i>';
				case 2:
				case 3:
					return '';
				}
			} },
			{"sTitle":"<bean:message key='consultationList.header.action' />","mData":null, "sClass": "center", "bSortable":false,"bSearchable":false,"mRender":function(data, type, row) {
				// return "<a href='#' onClick='preview(" + row.id + ")'><i class='icon-edit'></i></a>";
				return "<a href='javascript:editConsult(" + row.id + ", " + row.demographicNo + ")' rel='" + row.id + "'><i class='icon-edit'></i></a>";
			}, "sClass": "center" },
           	{ "sTitle": "<bean:message key='consultationList.header.patient' />", "mData": "patient", "sClass": "center" },
 			{ "sTitle": "<bean:message key='consultationList.header.service' />", "mData": "serviceDesc", "sClass": "center" },
 		    { "sTitle": "<bean:message key='consultationList.header.consultant' />", "mData": "specialistName",  "sClass": "center" },
		    { "sTitle": "<bean:message key='consultationList.header.team' />", "mData": "sendTo", "sClass": "center" },
		    { "sTitle": "<bean:message key='consultationList.header.status' />" , "mData": "status", "sClass": "center","mRender": function(data, type, row) {
		    	var statusId = parseInt(data);
		    	switch(statusId) {
		    	case 1:
		    		return '<bean:message key="consultationList.status.nothing" />';
		    	case 2:
		    		return '<bean:message key="consultationList.status.specialistCallback" />';
		    	case 3:
		    		return '<bean:message key="consultationList.status.patientCallback" />';
		    	case 4:
		    		return '<bean:message key="consultationList.status.completed" />';
		    	case 6:
		    		return '<bean:message key="consultationList.status.preliminary" />';
		    	}
		    } },
		 	{ "sTitle": "<bean:message key='consultationList.header.mrp' />", "mData": "providerName", "sClass": "center" },
			{ "sTitle": "<bean:message key='consultationList.header.appointmentDate' />", "mData": "appointmentDate", "sClass": "center", "mRender": function(data, type, row) {
		    	return data.replace("T", "");
		    } },
		    { "sTitle": "<bean:message key='consultationList.header.followup' />", "mData": "followUpDate", "sClass": "center" },
		    { "sTitle": "<bean:message key='consultationList.header.referralDate' />", "mData": "referralDate", "sClass": "center" }
		],
		"fnServerParams": function ( aoData ) {
			startDate = $("#startDate").val();
			endDate = $("#endDate").val();
			dateType = $("input[name=dateType]:checked").val();
			complete = $("#complete").prop("checked");
			team = $("#team").val();
			status = $("#status").val();
			
			aoData.push( { "name": "startDate", "value": startDate } );
			aoData.push( { "name": "endDate", "value": endDate } );
			aoData.push( { "name": "dateType", "value": dateType } );
			aoData.push( { "name": "complete", "value": complete } );
			aoData.push( { "name": "team", "value": team } );
			aoData.push( { "name": "withOption", "value": withOption } );
			aoData.push( { "name": "status", "value": status } );
		}
	});
	
	//The patch Boot3/Datatables patch - insert after you initialise dataTable
	$('div.dataTables_filter label select').addClass('form-control');
	$('div.dataTables_filter label input').addClass('form-control').css({"width" : "200px","margin-right" : "0px"});
	//The patch Boot3/Datatables patch -end
	
	$("#consultList_length").before($("#teamDiv").removeClass("hidden"));
	
	var options = $('<button class="btn btn-default" id="search-options" style="margin-left:0px">options <span class="caret"></span></button>');
	options.appendTo('div.dataTables_filter label');	
	
	$("#search-options").click(function() {
		$('#searchModal').modal('show');	
	});
	$('#date-start').datepicker({
		format: "yyyy-mm-dd"
	});
	$('#date-end').datepicker({
		format: "yyyy-mm-dd"
	});
	$("#team").on("change", function() {
		dataTable._fnAjaxUpdate();
	});
	$("#search").on("click", function() {
		withOption = true;
		dataTable._fnAjaxUpdate();
	});
	$("#reset").on("click", function() {
		$("#startDate").val("");
		$("#endDate").val("");
		$("#status option:first-child").attr("selected", "selected");
		$("#complete").removeAttr("checked");
		$("#referralDate").prop("checked", true);
	});
	
	$("#newConsult").on("click", function() {
		var demographicNo = $("#demographicNo").val();
		if (demographicNo == null || demographicNo.indexOf("?") >= 0) {
			alert("Please select a patient first");
		} else {
			newConsult(demographicNo);	
		}
	});	
	
});

function editConsult(requestId, demographicNo) {
	//popupOscarConsultationConfig('2000','1280', '<%=request.getContextPath()%>/web/partials/consult/consultRequestForm.jsp?requestId=' + requestId + '&demographicNo=' + demographicNo + '&providerNo=' + <%=curProvider_no%>);

	popupOscarConsultationConfig('2000','1280', '<%=request.getContextPath()%>/oscarEncounter/ViewRequest.do?requestId=' + requestId + '&de=' + demographicNo);
		
}

function newConsult(demographicNo) {
	popupOscarConsultationConfig('2000','1280', '<%=request.getContextPath()%>/web/partials/consult/consultRequestForm.jsp?demographicNo=' + demographicNo + '&providerNo=' + <%=curProvider_no%>);
}

function preview(id) {
	var url = '<%=basePath%>/oscarEncounter/ViewRequest.do?requestId='+id;
    var windowprops = "height=700,width=960,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
    window.open(url, "viwe", windowprops);
}

function popupOscarConsultationConfig(vheight,vwidth,varpage) { //open a new popup window
	var page = varpage;
	windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
	var popup=window.open(varpage, "<bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgConsConfig"/>", windowprops);
	if (popup != null) {
		if (popup.opener == null) {
	    	popup.opener = self;
	    }
  	}
}
</script>