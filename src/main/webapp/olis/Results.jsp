<%@ page language="java" contentType="text/html;" %>
<%@page import="java.util.*,oscar.oscarDB.DBHandler,java.sql.ResultSet, oscar.oscarLab.ca.all.parsers.Factory, oscar.oscarLab.ca.all.parsers.OLISHL7Handler, oscar.oscarLab.ca.all.parsers.OLISHL7Handler.OLISError, org.oscarehr.olis.OLISResultsAction, org.oscarehr.util.SpringUtils" %>
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

function filterResults(select) {
	if (select.value == "") { 
		jQuery(".evenLine").show();
		jQuery(".oddLine").show();	
	} else {
		jQuery(".evenLine").hide();
		jQuery(".oddLine").hide();
		jQuery("tr[reportingFacility='" + select.value + "']").show();
	}
}
</script>
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
			if(resp == null) { resp = "";};
			%>
			<!--  RAW HL7
				<%=resp%>
			-->
			<%
			try {
				OLISHL7Handler reportHandler = (OLISHL7Handler) Factory.getHandler("OLIS_HL7", resp);
				List<OLISError> errors = reportHandler.getReportErrors();
				if (errors.size() > 0) {
					for (OLISError error : errors) {
					%>
						<div class="error"><%=error.getText().replaceAll("\\n", "<br />")%></div>
					<%
					}
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("error",e);
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
						<td colspan=9>
						Filter by reporting facility:
						<select onChange="filterResults(this)">
							<option value="">(No filter)</option>
							<%  List<String> labs = new ArrayList<String>();
								OLISHL7Handler result;
								String name;
								for (String resultUuid : resultList) {
									result = OLISResultsAction.searchResultsMap.get(resultUuid);
									name = oscar.Misc.getStr(result.getPerformingFacilityName(), "").trim();
									if (!name.equals("")) { labs.add(name); }
								}
								HashSet<String> uniqueNames = new HashSet<String>(labs);
								for (String lab: uniqueNames) {
							%>
								<option value="<%=lab%>"><%=lab%></option>
							<% } %>
						</select>
						</td>
					</tr>
					<tr><th></th>
						<th></th>
						<th>Health Number</th>
						<th>Patient Name</th>
						<th>Sex</th>
						<th>Date of Test</th>
						<th>Discipline</th>
						<th>Tests</th>
						<th>Ordering Practitioner</th>
					</tr>
					
					<%  int lineNum = 0;
						for (String resultUuid : resultList) {
						result = OLISResultsAction.searchResultsMap.get(resultUuid);
					%>
					<tr class="<%=++lineNum % 2 == 1 ? "oddLine" : "evenLine"%>" reportingFacility="<%=result.getPerformingFacilityName() %>">
						<td>
							<div id="<%=resultUuid %>_result"></div>
							<input type="button" onClick="addToInbox('<%=resultUuid %>'); return false;" id="<%=resultUuid %>" value="Add to Inbox" />
						</td>
						<td>
							
							<input type="button" onClick="preview('<%=resultUuid %>'); return false;" id="<%=resultUuid %>_preview" value="Preview" />
						</td>
						<td><%=result.getHealthNum() %></td>
						<td><%=result.getPatientName() %></td>
						<td><%=result.getSex() %></td>
						<td><%=result.getTimeStamp(0, 0) %></td>
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
						<td> <%=result.getShortDocName() %> </td>
						 
					</tr>
					<% } %>
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
