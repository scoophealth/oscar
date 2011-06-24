<%@ page language="java" contentType="text/html;" %>
<%@page import="java.util.*,oscar.oscarDB.DBHandler,java.sql.ResultSet, oscar.oscarLab.ca.all.parsers.OLISHL7Handler, org.oscarehr.olis.OLISResultsAction, org.oscarehr.util.SpringUtils" %>
	
	
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
</script>
<style type="text/css">
.oddLine { 
	background-color: #cccccc;
}
.evenLine { } 
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
			List<String> resultList = (List<String>) request.getAttribute("resultList");
			
			if (resultList != null) {
			%>
			<table>
				<tr>
					<td colspan=8>Showing <%=resultList.size() %> result(s)</td>
				</tr>
				<% if (resultList.size() > 0) { %>
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
						OLISHL7Handler result = OLISResultsAction.searchResultsMap.get(resultUuid);
					%>
					<tr class="<%=++lineNum % 2 == 1 ? "oddLine" : "evenLine"%>">
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