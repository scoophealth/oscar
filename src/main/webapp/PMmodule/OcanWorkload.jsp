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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@page import="java.util.*"%>
<%@page import="org.oscarehr.common.model.OcanStaffForm" %>
<%@page import="org.oscarehr.common.model.OcanStaffFormData" %>
<%@page import="org.oscarehr.common.dao.OcanStaffFormDataDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%
	OcanStaffFormDataDao ocanStaffFormDataDao = (OcanStaffFormDataDao)SpringUtils.getBean("ocanStaffFormDataDao");
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	List<Provider> providers = providerDao.getActiveProviders();
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	Provider provider = loggedInInfo.getLoggedInProvider();
%>
<html:html locale="true">
<head>
<script src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath() %>/js/jquery.dataTables.js" type="text/javascript"></script>

<title>OCAN Workload</title>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">


<style title="currentStyle" type="text/css">
			@import "../css/demo_page.css";
			@import "../css/demo_table.css";
</style>

<style>
body
{
	text-align: center;
}

div#demo
{
	margin-left: auto;
	margin-right: auto;
	width: 90%;
	text-align: left;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$('#ocanTable').dataTable({
	        "aaSorting": [[ 1, "desc" ]]
	    });
	} );


	function beginReassign(assessmentId,consumerId) {
		$("#reassign_form input[name='assessmentId']").val(assessmentId);
		$("#reassign_form input[name='consumerId']").val(consumerId);
		$("#reassign_div").html("<span id=\"reassign_text\">Reassign assessment " + assessmentId + ":</span>" + $("#reassign_div").html());
		$("#reassign_div").show();
	}

	function submitReassign() {
		var c = confirm("Are you sure you would like to reassign. All OCANs for this consumer will be reassigned as well.");
		if(c) {
			$("#reassign_form").submit();
		} else {
			$("#reassign_div").hide();
			$("#reassign_text").remove();
		}
	}
</script>

</head>

<body>

<Br/>
<h2 style="text-align:center">OCAN Workload View</h2>
<br/>

<div id="demo">
			<table id="ocanTable" cellpadding="0" cellspacing="0" border="0" class="display" width="100%">
				<thead>
					<tr>
						<th>Assessment ID</th>
						<th>Client ID</th>
						<th>Last Name</th>
						<th>First Name</th>
						<th>Status</th>
						<th>Start Date</th>
						<th>Completion Date</th>
						<th>Reason for Assessment</th>
						<th>OCAN Lead</th>
						<th>Reassessment Time Frame</th>
						<th>Worker</th>
					</tr>
				</thead>
				<tbody>
					<%
						SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
						List<OcanStaffForm> ocans = (List<OcanStaffForm>)request.getAttribute("ocans");

						for(int x=0;x<ocans.size();x++) {
							OcanStaffForm ocan = ocans.get(x);

							Date startDate = null;
							String startDateStr = "";
							if(ocan.getOcanType().equals("FULL") || ocan.getOcanType().equals("SELF")) {
								if(ocan.getClientStartDate() != null && ocan.getStartDate() != null) {
									startDate = new Date((long)Math.min(ocan.getClientStartDate().getTime(),ocan.getStartDate().getTime()));
								} else if(ocan.getStartDate() != null) {
									startDate = ocan.getStartDate();
								} else {
									startDate = ocan.getClientStartDate();
								}
							}
							if(startDate != null) {
								startDateStr = dateFormatter.format(startDate);
							}

							String completionDateStr="";
							if(ocan.getCompletionDate()!=null) {
								completionDateStr = dateFormatter.format(ocan.getCompletionDate());
							}

							boolean ocanLead = false;
							List<OcanStaffFormData> ocanFormData = ocanStaffFormDataDao.findByForm(ocan.getId());
							for(OcanStaffFormData data:ocanFormData) {
								if(data.getQuestion().equals("completedByOCANLead")) {
									if(data.getAnswer().equals("TRUE")) ocanLead=true;
									break;
								}
							}

							String reassessmentTimeFrame="";
							if(ocan.getReasonForAssessment().equals("IA")||ocan.getReasonForAssessment().equals("RA")) {
								if(startDate != null) {
									Calendar c = Calendar.getInstance();
									c.setTime(startDate);
									c.add(Calendar.MONTH,6);
									String startOfTimeFrame = dateFormatter.format(c.getTime());
									c.add(Calendar.DAY_OF_MONTH,30);
									String endOfTimeFrame = dateFormatter.format(c.getTime());
									reassessmentTimeFrame = startOfTimeFrame + " - " + endOfTimeFrame;
								}
							}

							Provider p = providerDao.getProvider(ocan.getProviderNo());

					%>
					<tr class="gradeB">
						<td style="text-align:right"><%=ocan.getAssessmentId() %></td>
						<td style="text-align:right"><a href="<%=request.getContextPath()%>/PMmodule/ClientManager.do?id=<%=ocan.getClientId()%>"><%=ocan.getClientId() %></a></td>
						<td><%=ocan.getLastName() %></td>
						<td><%=ocan.getFirstName() %></td>
						<td style="text-align:center"><%=ocan.getAssessmentStatus() %></td>
						<td style="text-align:center"><%=startDateStr %></td>
						<td style="text-align:center"><%=completionDateStr %></td>
						<td style="text-align:center"><%=convertOcanReasonUserString(ocan.getReasonForAssessment()) %></td>
						<td style="text-align:center"><%=(ocanLead)?"Yes":"No" %></td>
						<td style="text-align:center"><%=reassessmentTimeFrame %></td>
						<td nowrap="nowrap">
							<a href="javascript:void(0);" onClick="beginReassign('<%=ocan.getAssessmentId()%>','<%=ocan.getClientId()%>')"><%=p.getFormattedName() %></a>
						</td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>
</div>

<br/><br/>

<div id="reassign_div" style="display:none">

	<form id="reassign_form" action="<%=request.getContextPath()%>/PMmodule/OcanWorkload.do" method="POST">
		<input type="hidden" name="reassign_old_provider" value="<%=provider.getProviderNo()%>"/>
		<input type="hidden" name="assessmentId" value=""/>
		<input type="hidden" name="consumerId" value=""/>
		<input type="hidden" name="method" value="reassign"/>
		<select name="reassign_new_provider" id="reassign_new_provider">
		<%
			for(Provider p:providers) {
				%><option value="<%=p.getProviderNo()%>" <%=(p.getProviderNo().equals(provider.getProviderNo()))?" selected=\"selected\" ":"" %> ><%=p.getFormattedName()%></option><%
			}
		%>
		</select>
		<input type="button" value="Reassign" onclick="submitReassign();"/>
	</form>
</div>

</html:html>
<%!

String convertOcanReasonUserString(String type) {
	if(type == null) {
		return "OCAN";
	}
	if(type.equals("IA")) {
		return "Initial Assessment";
	}
	if(type.equals("RA")) {
		return "Reassessment";
	}
	if(type.equals("DIS")) {
		return "(Prior to) Discharge";
	}
	if(type.equals("OTHR")) {
		return "Other";
	}
	if(type.equals("SC")) {
		return "Significant Change";
	}
	if(type.equals("REV")) {
		return "Review";
	}
	if(type.equals("REK")) {
		return "Re-key";
	}
	return "";
}
%>
