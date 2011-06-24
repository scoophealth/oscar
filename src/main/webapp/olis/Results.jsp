<%@ page language="java" contentType="text/html;" %>
<%@page import="java.util.*,oscar.oscarDB.DBHandler,java.sql.ResultSet, oscar.oscarLab.ca.all.parsers.OLISHL7Handler, org.oscarehr.olis.OLISResultsAction, org.oscarehr.util.SpringUtils" %>
	
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<link rel="stylesheet" type="text/css"
	href="../../../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<script type="text/javascript" src="../../../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
	
<script type="text/javascript">
function addToInbox(uuid) {
	jQuery(uuid).attr("disabled", "disabled");
	jQuery.ajax({
		url: "<%=request.getContextPath() %>/olis/AddToInbox.do",
		data: "uuid=" + uuid,
		success: function(data) {
			jQuery(uuid + "_result").innerHTML(data);
		}
	});
}
</script>
	
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
			String resp = (String) request.getAttribute("unsignedResponse");
			if(resp == null) { resp = "";};
			%>
				<%=resp%>
			<%
			List<String> resultList = (List<String>) request.getAttribute("resultList");
			
			if (resultList != null) {
			%>
			<table>
				<tr>
					<td colspan=8>Showing <%=resultList.size() %> result(s)</td>
				</tr>
				<% if (resultList.size() > 0) { %>
				<tr>
					<th>Health Number</th>
					<th>Patient Name</th>
					<th>Sex</th>
					<th>Date of Test</th>
					<th>Discipline</th>
					<th>Tests</th>
					<th></th>
				</tr>
				
					<% for (String resultUuid : resultList) {
						OLISHL7Handler result = OLISResultsAction.searchResultsMap.get(resultUuid);
					%>
					<tr>
						<td><%=result.getHealthNum() %></td>
						<td><%=result.getPatientName() %></td>
						<td><%=result.getSex() %></td>
						<td><%=result.getTimeStamp(0, 0) %></td>
						<td>
						<%
						ArrayList<String> disciplineArray;
						disciplineArray = result.getHeaders();
						
						String next = "";
						if (disciplineArray != null && disciplineArray.size() > 0) next = disciplineArray.get(0);

						int sepMark;
						if ((sepMark = next.indexOf("<br />")) < 0) {
							if ((sepMark = next.indexOf(" ")) < 0) sepMark = next.length();
						}
						String discipline = next.substring(0, sepMark).trim();

						for (int i = 1; disciplineArray != null && i < disciplineArray.size(); i++) {

							next = disciplineArray.get(i);
							if ((sepMark = next.indexOf("<br />")) < 0) {
								if ((sepMark = next.indexOf(" ")) < 0) sepMark = next.length();
							}

							if (!next.trim().equals("")) discipline = discipline + "/" + next.substring(0, sepMark);
						}
						%>
						<%=discipline %>
						</td>
						
						<td>
						<%
						Integer obrCount = result.getOBRCount();
						String tests = "";
						
						for (int j = 0; j < obrCount; j++) {
							tests += result.getOBRName(j);
						}
						
						%>
						
						<%=tests %>
						</td>
						
						<td>
						<div id="<%=resultUuid %>_result"></div>
						<input type="button" onClick="addToInbox('<%=resultUuid %>')" id="<%=resultUuid %>" value="Add to Inbox" /></td> 
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

</body>
</html>