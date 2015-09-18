<!DOCTYPE html>
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
<%@page import="org.oscarehr.common.model.SecRole"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.text.DateFormatSymbols"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.web.OcanReportingBean" %>
<%@page import="org.oscarehr.common.model.Demographic" %>

<%@ include file="/taglibs.jsp"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	List<Demographic> clients = OcanReportingBean.getOCANClients(loggedInInfo.getCurrentFacility().getId());
%>

<html>
<head>
<title><bean:message key="admin.admin.ocanReporting"/></title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">

<script>
	var ctx = '<%=request.getContextPath()%>';
</script>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script>
	function generateIndividualNeedRatingOverTimeReport() {
		//check to make sure a client is selected
		if($("#client").val()=='') {
			alert('Please choose a client for the report.');
			return;
		}

		//check to make sure atleast 1 assessment is selected
		var selected=false;
		$("[name='assessment']").each(function() {
			if($(this).is(':checked')) {
				selected=true;
			}
		});

		if(!selected) {
			alert('Please choose atleast 1 assessment for the report.');
			return;
		}

		//check to make sure atleast 1 need rating is selected
		if(!$("#needs_unmet").is(':checked') && !$("#needs_met").is(':checked') && !$("#needs_no").is(':checked') && !$("#needs_unknown").is(':checked') ) {
			alert('Please choose atleast 1 need rating for the report.');
			return;
		}

		//load report
//		alert('Loading Individual Need Rating over Time Report...');


		$("#ocanForm input[name='method']").val("generateIndividualNeedRatingOverTimeReport");
		$("#ocanForm").submit();
	}



	function generateNeedRatingOverTimeReport() {
		//check to make sure a client is selected
		if($("#client").val()=='') {
			alert('Please choose a client for the report.');
			return;
		}

		//check to make sure atleast 1 assessment is selected
		var selected=false;
		$("[name='assessment']").each(function() {
			if($(this).is(':checked')) {
				selected=true;
			}
		});

		if(!selected) {
			alert('Please choose atleast 1 assessment for the report.');
			return;
		}

		//check to make sure atleast 1 domain is selected
		selected=false;
		$("[name='domains']").each(function() {
			if($(this).is(':checked')) {
				selected=true;
			}
		});

		if(!selected) {
			alert('Please choose atleast 1 domain for the report.');
			return;
		}


		//load report
		alert('Loading Need Rating over Time Report...');

		$("#ocanForm input[name='method']").val("generateNeedRatingOverTimeReport");
		$("#ocanForm").submit();

	}

	function generateSummaryOfActionsAndCommentsReport() {
		//check to make sure a client is selected
		if($("#client").val()=='') {
			alert('Please choose a client for the report.');
			return;
		}

		//check to make sure atleast 1 assessment is selected
		var selected=false;
		$("[name='assessment']").each(function() {
			if($(this).is(':checked')) {
				selected=true;
			}
		});

		if(!selected) {
			alert('Please choose atleast 1 assessment for the report.');
			return;
		}

		//check to make sure atleast 1 domain is selected
		selected=false;
		$("[name='domains']").each(function() {
			if($(this).is(':checked')) {
				selected=true;
			}
		});

		if(!selected) {
			alert('Please choose atleast 1 domain for the report.');
			return;
		}

		//load report
		alert('Loading Summary of Actions and Comments Report...');

		$("#ocanForm input[name='method']").val("generateSummaryOfActionsAndCommentsReport");
		$("#ocanForm").submit();

	}

	function toggleNeeds() {
		var status = $("#needs_all").is(':checked');
		var val = "";
		if(status) {
			val=true;
		} else {
			val=false;
		}
		$("#needs_unmet").attr('checked',val);
		$("#needs_met").attr('checked',val);
		$("#needs_no").attr('checked',val);
		$("#needs_unknown").attr('checked',val);
	}

	function toggleDomains() {
		var status = $("#domains_all").is(':checked');
		var val = "";
		if(status) {
			val=true;
		} else {
			val=false;
		}

		$("input[name='domains']").each(function(){
			$(this).attr('checked',val);
		});
	}


	function selectReportType() {
		$("#assessment_table").hide();
		$("#assessment_table").html("<tr><td colspan=\"2\">Select Assessments</td></tr>");
		$("#client_table").hide();
		$("#client").val("");
		$("#generate_ind_need_table").hide();
		$("#generate_need_table").hide();
		$("#generate_summary_table").hide();
		$("#needs_table").hide();
		$("#comments_table").hide();
		$('input:radio[name=comments][value=comments]').attr('checked',true);
		$("#needs_met").attr('checked',false);
		$("#needs_unmet").attr('checked',false);
		$("#needs_no").attr('checked',false);
		$("#needs_unknown").attr('checked',false);
		$("#needs_all").attr('checked',false);
		$("#domains_table").hide();
		$("#domains_all").attr('checked',false);
		$("input[name='domains']").each(function(){
			$(this).attr('checked',false);
		});

		var v = $("#reportType").val();
		if(v == 'ind_need_time') {
			$("#client").val("");
			$("#client_table").show();
		} else if(v == 'need_time') {
			$("#client").val("");
			$("#client_table").show();
		} else if(v == 'summary_actions') {
			$("#client").val("");
			$("#client_table").show();
		} else {
			$("#client_table").hide();
		}
	}

	function selectClient() {
		$("#assessment_table").html("<tr><td colspan=\"2\">Select Assessments</td></tr>");
		var v = $("#client").val();
		if(v=='') {
			return;
		}


        $.getJSON(ctx+"/OcanReporting.do?method=getAssessments&clientId="+v,
                function(data,textStatus){
                  for(var x=0;x<data.length;x++) {
                	  //alert(data[x].value + " - " + data[x].label);
                	  $("#assessment_table").append("<tr> <td><input type=\"checkbox\" name=\"assessment\" value=\""+data[x].value+";"+data[x].label+"\"/></td><td>"+data[x].label+"</td></tr>");
                  }
                  $("#assessment_table").show();

                  if($("#reportType").val() == 'ind_need_time') {
                	  $("#generate_ind_need_table").show();
                	  $("#needs_table").show();
                  }
                  if($("#reportType").val() == 'need_time') {
                	  $("#domains_table").show();
                   	  $("#generate_need_table").show();
                  }
                  if($("#reportType").val() == 'summary_actions') {
                	$("#comments_table").show();
                	$("#domains_table").show();
                  	$("#generate_summary_table").show();
                  }
        });

	}
</script>
</head>

<body>
<h3>OCAN Reporting - 2.0.1</h3>

<form method="post" id="ocanForm" action="../OcanReporting.do">
	<input type="hidden" name="method" value=""/>
	<table class="borderedTableAndCells" style="border:none" onChange="selectReportType()">
		<tr>
			<td>Select Type of Report:</td>
			<td >
				<select name="reportType" id="reportType">
					<option value="">Select</option>
					<option value="ind_need_time">Individual Need Rating over Time</option>
					<option value="need_time">Needs over Time</option>
					<option value="summary_actions">Summary of Actions and Comments</option>
				</select>
			</td>
		</tr>
	</table>
	<br/>
	<table class="borderedTableAndCells" id="client_table" style="display:none;">
		<tr>
			<td>Select Client</td>
			<td>
				<select name="client" id="client" onChange="selectClient();">
					<option value="">Select</option>
					<%
						for(Demographic d:clients) {
					%>
						<option value="<%=d.getDemographicNo()%>"><%=d.getFormattedName() %></option>
					<% } %>
				</select>
			</td>
		</tr>
	</table>

	<br/>
	<table class="borderedTableAndCells" id="assessment_table" style="display:none;">
		<tr><td colspan="2">Select Assessments</td></tr>
	</table>

	<br/>
	<table class="borderedTableAndCells" id="needs_table" style="display:none;">
		<tr>
			<td colspan="2">Select Need Rating</td>
		</tr>
		<tr>
			<td><input type="checkbox" value="all" name="needs_all" id="needs_all" onChange="toggleNeeds()"/>
			<td>Select All</td>
		</tr>
		<tr>
			<td><input type="checkbox" value="unmet" name="needs_unmet" id="needs_unmet"/>
			<td>Unmet Needs</td>
		</tr>
		<tr>
			<td><input type="checkbox" value="met" name="needs_met" id="needs_met" />
			<td>Met Needs</td>
		</tr>
		<tr>
			<td><input type="checkbox" value="no" name="needs_no" id="needs_no"/>
			<td>No Needs</td>
		</tr>
		<tr>
			<td><input type="checkbox" value="unknown" name="needs_unknown" id="needs_unknown" />
			<td>Unknown</td>
		</tr>
	</table>
	<br/>
	<table class="borderedTableAndCells" id="domains_table" style="display:none;">
		<tr>
			<td colspan="6">Select Domains to be listed:</td>
		</tr>
		<tr>
			<td><input type="checkbox" name="domains_all" id="domains_all" value="domains_all" onChange="toggleDomains();"/></td><td colspan="5">Select All</td>
		</tr>
		<tr>
			<td><input type="checkbox" name="domains" value="1;Accommodation"/></td><td>Accommodation</td>
			<td><input type="checkbox" name="domains" value="9;Psychological Distress"/></td><td>Psychological Distress</td>
			<td><input type="checkbox" name="domains" value="17;Sexual Expression"/></td><td>Sexual Expression</td>
		</tr>
		<tr>
			<td><input type="checkbox" name="domains" value="2;Food"/></td><td>Food</td>
			<td><input type="checkbox" name="domains" value="10;Safety to Self"/></td><td>Safety to Self</td>
			<td><input type="checkbox" name="domains" value="18;Child Care"/></td><td>Child Care</td>
		</tr>
		<tr>
			<td><input type="checkbox" name="domains" value="3;Looking After The Home"/></td><td>Looking after the home</td>
			<td><input type="checkbox" name="domains" value="11;Safety to Others"/></td><td>Safety to Others</td>
			<td><input type="checkbox" name="domains" value="19;Other Dependents"/></td><td>Other Dependents</td>
		</tr>
		<tr>
			<td><input type="checkbox" name="domains" value="4;Self-Care"/></td><td>Self-Care</td>
			<td><input type="checkbox" name="domains" value="12;Alcohol"/></td><td>Alcohol</td>
			<td><input type="checkbox" name="domains" value="20;Basic Education"/></td><td>Basic Education</td>
		</tr>
		<tr>
			<td><input type="checkbox" name="domains" value="5;Daytime Activities"/></td><td>Daytime Activities</td>
			<td><input type="checkbox" name="domains" value="13;Drugs"/></td><td>Drugs</td>
			<td><input type="checkbox" name="domains" value="21;Telephone"/></td><td>Telephone</td>
		</tr>
		<tr>
			<td><input type="checkbox" name="domains" value="6;Physical Health"/></td><td>Physical Health</td>
			<td><input type="checkbox" name="domains" value="14;Other Addictions"/></td><td>Other Addictions</td>
			<td><input type="checkbox" name="domains" value="22;Transport"/></td><td>Transport</td>
		</tr>
		<tr>
			<td><input type="checkbox" name="domains" value="7;Psychotic Symptoms"/></td><td>Psychotic Symptoms</td>
			<td><input type="checkbox" name="domains" value="15;Company"/></td><td>Company</td>
			<td><input type="checkbox" name="domains" value="23;Money"/></td><td>Money</td>
		</tr>
		<tr>
			<td><input type="checkbox" name="domains" value="8;Info on Condition and Treatment"/></td><td>Info on Condition and Treatment</td>
			<td><input type="checkbox" name="domains" value="16;Intimate Relationships"/></td><td>Intimate Relationships</td>
			<td><input type="checkbox" name="domains" value="24;Benefits"/></td><td>Benefits</td>
		</tr>

	</table>
	<br/>
	<table class="borderedTableAndCells" id="comments_table" style="display:none;">
		<tr>
			<td><input type="radio" value="comments" name="comments" id="comments" />
			<td>Comments</td>
		</tr>
		<tr>
			<td><input type="radio" value="no_comments" name="comments" id="comments" />
			<td>No Comments</td>
		</tr>
	</table>
	<br/>
	<table id="generate_ind_need_table" style="display:none;">
		<tr>
			<td colspan="2"><input type="button" onClick="generateIndividualNeedRatingOverTimeReport()" value="Generate Individual Need Rating over Time Report"/></td>
		</tr>
	</table>
	<table id="generate_need_table" style="display:none;">
		<tr>
			<td colspan="2"><input type="button" onClick="generateNeedRatingOverTimeReport()" value="Generate Need Rating over Time Report"/></td>
		</tr>
	</table>
	<table id="generate_summary_table" style="display:none;">
		<tr>
			<td colspan="2"><input type="button" onClick="generateSummaryOfActionsAndCommentsReport()" value="Generate Summary of Actions and Comments Report"/></td>
		</tr>
	</table>
</form>
</body>
</html>

