<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>
<%@ page language="java" contentType="text/html;" %>
<%@page import="com.indivica.olis.queries.*,org.oscarehr.olis.OLISSearchAction,java.util.*,oscar.oscarLab.ca.all.parsers.Factory, oscar.oscarLab.ca.all.parsers.OLISHL7Handler, oscar.oscarLab.ca.all.parsers.OLISHL7Handler.OLISError, org.oscarehr.olis.OLISResultsAction, org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.util.MiscUtils" %>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
    jQuery.noConflict();
</script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/css/OscarStandardLayout.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/share/javascript/Oscar.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/share/javascript/oscarMDSIndex.js"></script>
	
<script type="text/javascript">
function addToInbox(uuid) {
	jQuery(uuid).attr("disabled", "disabled");
	jQuery.ajax({
		url: "<%=request.getContextPath() %>/olis/AddToInbox.do",
		data: "uuid=" + uuid,
		success: function(data) {
			jQuery("#" + uuid + "_result").html(data);
		}
	});
}
function preview(uuid) {
	reportWindow('<%=request.getContextPath()%>/lab/CA/ALL/labDisplayOLIS.jsp?segmentID=0&preview=true&uuid=' + uuid);
}

function save(uuid) {
	jQuery(uuid).attr("disabled", "disabled");
	jQuery.ajax({
		url: "<%=request.getContextPath() %>/olis/AddToInbox.do",
		data: "uuid=" + uuid + "&file=true",
		success: function(data) {
			jQuery("#" + uuid + "_result").html(data);
		}
	});
}

function ack(uuid) {
	jQuery(uuid).attr("disabled", "disabled");
	jQuery.ajax({
		url: "<%=request.getContextPath() %>/olis/AddToInbox.do?ack=true",
		data: "uuid=" + uuid + "&ack=true",
		success: function(data) {
			jQuery("#" + uuid + "_result").html(data);
		}
	});
}

var patientFilter = "";
var labFilter = "";
function filterResults(select) {
	if (select.name == "labFilter") {
		labFilter = select.value;
	} else if(select.name == "patientFilter") {
		patientFilter = select.value;
	}
	var performFilter = function() {
		var visible = (patientFilter == "" || jQuery(this).attr("patientName") == patientFilter)
				   && (labFilter == "" || jQuery(this).attr("reportingLaboratory") == labFilter);
		if (visible) { jQuery(this).show(); }
		else { jQuery(this).hide(); }
	};
	jQuery(".evenLine").each(performFilter);
	jQuery(".oddLine").each(performFilter);
}
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/share/javascript/sortable.js"></script>
<style type="text/css">
.oddLine { 
	background-color: #cccccc;
}
.evenLine { } 

.error {
	border: 1px solid red;
	color: red;
	font-weight: bold;
	margin: 10px;
	padding: 10px;
}
</style>
	
<title>OLIS Search Results</title>
</head>
<body>

<table style="width:600px;" class="MainTable" align="left">
	<tbody><tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="175">OLIS</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tbody><tr>
				<td>Results</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><a href="javascript:popupStart(300,400,'Help.jsp')"><u>H</u>elp</a> | <a href="javascript:popupStart(300,400,'About.jsp')">About</a> | <a href="javascript:popupStart(300,400,'License.jsp')">License</a></td>
			</tr>
			</tbody>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<%
			if (request.getAttribute("searchException") != null) {
			%>
				<div class="error">Could not perform the OLIS query due to the following exception:<br /><%=((Exception) request.getAttribute("searchException")).getLocalizedMessage() %></div>
			<%
			} %>
			
			<%
			if (request.getAttribute("errors") != null) {
				// Show the errors to the user				
				for (String error : (List<String>) request.getAttribute("errors")) { %>
					<div class="error"><%=error.replaceAll("\\n", "<br />") %></div>
				<% }
			}
			String resp = (String) request.getAttribute("olisResponseContent");
			if(resp == null) { resp = ""; }
			%>
			<!--  RAW HL7
				<%=resp%>
			-->
			<%
			boolean hasBlockedContent = false;
			try {
				if(resp != null && resp.length()>0) {
					OLISHL7Handler reportHandler = (OLISHL7Handler) Factory.getHandler("OLIS_HL7", resp);
					if(reportHandler != null) {
						List<OLISError> errors = reportHandler.getReportErrors();
						if (errors.size() > 0) {
							for (OLISError error : errors) {
							%>
								<div class="error"><%=error.getIndentifer()%>:<%=error.getText().replaceAll("\\n", "<br />")%></div>
							<%
							}
						}
						hasBlockedContent = reportHandler.isReportBlocked();
					}
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("error",e);
			}
			if (hasBlockedContent) { 
			%>
			<form  action="<%=request.getContextPath()%>/olis/Search.do" onsubmit="return confirm('Are you sure you want to resubmit this query with a patient consent override?')">
				<input type="hidden" name="redo" value="true" />
				<input type="hidden" name="uuid" value="<%=(String)request.getAttribute("searchUuid")%>" />
				<input type="hidden" name="force" value="true" />				
				<input type="submit" value="Submit Override Consent" /> 
				Authorized by: 
				<select id="blockedInformationIndividual" name="blockedInformationIndividual">
					<option value="patient">Patient</option>
					<option value="substitute">Substitute Decision Maker</option>					
				</select>
			</form>
			<%
			}
			List<String> resultList = (List<String>) request.getAttribute("resultList");
			
			
			if (resultList != null) {
			%>
			<table>
				<tr>
					<td colspan=8>Showing <%=resultList.size() %> result(s)</td>
				</tr>
				<% if (resultList.size() > 0) { %>
					<tr>
						<td colspan="4">
						Filter by patient name:
						<select name="patientFilter" onChange="filterResults(this)">
							<option value="">All Patients</option>
							<%  List<String> names = new ArrayList<String>();
								OLISHL7Handler result;
								String name;
								for (String resultUuid : resultList) {
									result = OLISResultsAction.searchResultsMap.get(resultUuid);
									name = oscar.Misc.getStr(result.getPatientName(), "").trim();
									if (!name.equals("")) { names.add(name); }
								}
								for (String tmp: new HashSet<String>(names)) {
							%>
								<option value="<%=tmp%>"><%=tmp%></option>
							<% } %>
						</select>
						</td>
						<td colspan="5">
						Filter by reporting laboratory:
						<select name="labFilter" onChange="filterResults(this)">
							<option value="">All Labs</option>
							<%  List<String> labs = new ArrayList<String>();
								for (String resultUuid : resultList) {
									result = OLISResultsAction.searchResultsMap.get(resultUuid);
									name = oscar.Misc.getStr(result.getReportingFacilityName(), "").trim();
									if (!name.equals("")) { labs.add(name); }
								}
								for (String tmp: new HashSet<String>(labs)) {
							%>
								<option value="<%=tmp%>"><%=tmp%></option>
							<% } %>
						</select>
						</td>
					</tr>
					<tr><td colspan="10">
					<table class="sortable" id="resultsTable">
					<tr><th class="unsortable"></th>
						<th class="unsortable"></th>
						<th class="unsortable"></th>
						<th class="unsortable"></th>
						<th>Health Number</th>
						<th>Patient Name</th>
						<th>Sex</th>
						<th>Date of Test</th>
						<th>Discipline</th>
						<th>Tests</th>
						<th>Status</th>
						<th>Ordering Practitioner</th>
						<th>Admitting Practitioner</th>
					</tr>
					
					<%  int lineNum = 0;
						for (String resultUuid : resultList) {
						result = OLISResultsAction.searchResultsMap.get(resultUuid);
					%>
					<tr class="<%=++lineNum % 2 == 1 ? "oddLine" : "evenLine"%>" patientName="<%=result.getPatientName()%>" reportingLaboratory="<%=result.getReportingFacilityName()%>">
						<td>
							<div id="<%=resultUuid %>_result"></div>
							<input type="button" onClick="addToInbox('<%=resultUuid %>'); return false;" id="<%=resultUuid %>" value="Add to Inbox" />
						</td>
						<td>
							
							<input type="button" onClick="preview('<%=resultUuid %>'); return false;" id="<%=resultUuid %>_preview" value="Preview" />
						</td>
						
						<td>							
							<input type="button" onClick="save('<%=resultUuid %>'); return false;" id="<%=resultUuid %>_save" value="Save/File" />
						</td>
						
						<td>							
							<input type="button" onClick="ack('<%=resultUuid %>'); return false;" id="<%=resultUuid %>_ack" value="Acknowledge" />
						</td>
						
						<td><%=result.getHealthNum() %></td>
						<td><%=result.getPatientName() %></td>
						<td align="center"><%=result.getSex() %></td>
						<td><%=result.getSpecimenReceivedDateTime() %></td>
						<td style="width:200px;">
						<%
						String discipline = result.getCategoryList();
						%>
						<%=discipline %>
						</td>
						
						<td style="width:200px;">
						<%
						Integer obrCount = result.getOBRCount();
						String tests = result.getTestList();
						%>
						
						<%=tests %>
						</td>
						<td><%= ( (String) ( result.getOrderStatus().equals("F") ? "Final" : result.getOrderStatus().equals("C") ? "Corrected" : "Partial") )%></td>
						<td> <%=result.getShortDocName() %> </td>
						<td> <%=result.getAdmittingProviderNameShort()%></td>
						 
					</tr>					
					<% } %>
					</table></td></tr>
				<% } %>
			</table>
			<%
			}
			%>
		</td>
	</tr></tbody>
</table>
<!-- RAW HL7 ERP
<%=request.getAttribute("unsignedResponse") %>
-->
</body>
</html>
