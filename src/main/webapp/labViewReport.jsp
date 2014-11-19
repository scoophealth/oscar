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
<!DOCTYPE HTML>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.common.dao.OscarLogDao"%>
<%@page import="org.oscarehr.common.model.OscarLog"%>
<%@page import="oscar.oscarDB.DBHandler"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.io.IOException"%>

<%@page import="org.oscarehr.common.model.Hl7TextInfo"%>
<%@page import="org.oscarehr.common.model.Hl7TextMessage"%>
<%@page import="org.oscarehr.common.model.Provider"%>

<%@page import="org.oscarehr.common.dao.Hl7TextInfoDao"%>
<%@page import="org.oscarehr.common.dao.Hl7TextMessageDao"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>

<%@page import="org.oscarehr.util.LoggedInInfo" %>

<%@page import="org.apache.commons.lang.StringEscapeUtils" %>


<%
String action = request.getParameter("action");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

Hl7TextInfoDao hl7TextInfoDao = SpringUtils.getBean(Hl7TextInfoDao.class);
Hl7TextMessageDao hl7TextMessageDao = SpringUtils.getBean(Hl7TextMessageDao.class);
ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
OscarLogDao oscarLogDao = SpringUtils.getBean(OscarLogDao.class);

String providerNo = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();
%>
<html>
<head>
<title>Bug 3392 Impact Report</title>
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="share/calendar/calendar.css" title="win2k-cold-1" />

<!-- main calendar program -->
<script type="text/javascript" src="share/calendar/calendar.js"></script>

<!-- language for the calendar -->
<script type="text/javascript"
	src="share/calendar/lang/calendar-en.js"></script>

<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="share/calendar/calendar-setup.js"></script>


</head>
<body>
	<h2>BUG 3392 Impact Report</h2>

	<h5>This report will indicate all audit log entries where a
		provider viewed an affected GDML Lab report.</h5>
	<h5>Please note that if your OSP has patched this system, then all
		GDML results past and present display accurately.</h5>

	<%
		if (!"run".equals(action)) {
	%>
	<form action="labViewReport.jsp">
		<input type="hidden" name="action" value="run" />
		<h4>Enter the date the system was upgraded (format:yyyy-MM-dd):</h4>
		<input type="text" name="startDate" id="startDate" /><img src="images/cal.gif"
			id="startDateCal" />

		<h4>Enter the date the system was patched (if applicable) (format:yyyy-MM-dd):</h4>
		<input type="text" name="endDate"  id="endDate" /><img src="images/cal.gif"
			id="endDateCal" />

		<h4>Press submit button, and wait for results. Time to run varies
			on clinic size</h4>
		<input type="submit" />

	</form>
	<script type="text/javascript">
		Calendar.setup({
			inputField : "startDate",
			ifFormat : "%Y-%m-%d",
			showsTime : false,
			button : "startDateCal"
		});
		
		Calendar.setup({
			inputField : "endDate",
			ifFormat : "%Y-%m-%d",
			showsTime : false,
			button : "endDateCal"
		});
	</script>
	<%
		}
	%>



<%
	if ("run".equals(action)) {
		String startDate = request.getParameter("startDate");		
		String endDate = request.getParameter("endDate");
		
		if(startDate == null || startDate.length() != 10) {
			startDate = "1990-01-01";
		}
		if(endDate == null || endDate.length() != 10) {
			endDate = formatter.format(new Date());
		}

		ResultSet rs3 = null;
		List<Integer> allGDMLWithDiffIds = new ArrayList<Integer>();

		try {
			
			out.println("<h5>Using Start Date of "+ startDate + " and end date of " + endDate+".</h5>");

			allGDMLWithDiffIds = filterByGDML(getAllLabsThatDontMatch());
			
			out.println("<h5>Found <span style=\"color:red\">" + allGDMLWithDiffIds.size() + "</span> GDML lab report versions that were affected.</h5>");

			if(allGDMLWithDiffIds.size() > 0) {
	
				out.println("<table border=\"1\" style=\"width:100%\">");
				out.println("<tr><td><b>Lab</b></td><td><b>Access</b></td><td><b>Information</b></td></tr>");
				
				
				for (Integer id : allGDMLWithDiffIds) {
					
					out.println("<tr>");
					Hl7TextInfo info = hl7TextInfoDao.findLabId(id);
									
					MiscUtils.getLogger().info("\nchecking affected LAB NO " + id + "/" + info.getAccessionNumber() + " for patient " + info.getLastName() + "," + info.getFirstName());
					
					//get the accession
					String acc = info.getAccessionNumber();					
					
					%>
						<td>
							<a target="_blank" href="lab/CA/ALL/labDisplay.jsp?segmentID=<%=info.getLabNumber() %>&multiID=&providerNo=<%=providerNo %>&searchProviderNo=<%=providerNo %>"><%=info.getLabNumber() %></a>
							<br/>
							<b>Accession:</b> <%=info.getAccessionNumber() %><br/>
							<b>Patient Name:</b> <%=info.getLastName() %>, <%=info.getFirstName() %><br/>
							<b>Patient HIN:</b> <%=info.getHealthNumber() %>
						</td>
						<td>
					<%
					
					//with the accession, we basically run the bad query, and then see what results it linked to
					//the result we want is the hl7TextMessage.lab_id
					List<Integer> badHl7MessageIds = new ArrayList<Integer>();
					rs3 = DBHandler.GetSQL(" select id,lab_no from hl7TextInfo where accessionNum = '"+acc+"'");
					while(rs3.next()) {
						badHl7MessageIds.add(rs3.getInt("id"));
						//lab nos here are the ones that could have been accessed
						List<Integer> accesses = checkAccess(rs3.getInt("lab_no"),startDate,endDate);
						MiscUtils.getLogger().info("LAB " + rs3.getInt("lab_no") + " Accessed " + accesses.size() + " times");
						//MiscUtils.getLogger().info("these are the IDs (not lab nos that the bad query would have returned." + rs3.getInt("id"));
						
						//On <date>, Provider <xxx> accessed lab <link|number> with accession.
						for(Integer logId : accesses) {
							OscarLog logEntry = oscarLogDao.find(logId.longValue());
							Provider p = providerDao.getProvider(logEntry.getProviderNo());
							String provider = (p!=null)?p.getFormattedName():p.getProviderNo();
							
							%>
							<span>On <%=dateTimeFormatter.format(logEntry.getCreated())%>,<%=provider %> viewed lab <a target="_blank" href="lab/CA/ALL/labDisplay.jsp?segmentID=<%=info.getLabNumber() %>&multiID=&providerNo=<%=providerNo %>&searchProviderNo=<%=providerNo %>"><%=rs3.getInt("lab_no") %></a></span>
							<br/>
							<%
						}
						
					}
					rs3.close();
					rs3=null;
					
					out.println("</td>");
					
					
					out.println("<td>");
					//for each from above, get the text info for that message, these are the additional results 
					//that would have been appended INSTEAD of the correct ones.
					for(Integer badId:badHl7MessageIds) {
						rs3 = DBHandler.GetSQL("select * from hl7TextInfo i where i.lab_no=" + badId);
						while(rs3.next()) {
							if(!acc.equals(rs3.getString("accessionNum"))) {
								MiscUtils.getLogger().info("DATA from LAB  " + badId + "/" + rs3.getString("accessionNum") + " would have shown. That lab belongs to " + rs3.getString("last_name") + "," + rs3.getString("first_name") );
								%>
									<span>Results from <%=rs3.getString("accessionNum")%> (<%=rs3.getString("last_name") + "," + rs3.getString("first_name") %>) would have been displayed in place of the correct ones.</span>
								<% 
							}
						}
						rs3.close();
						rs3=null;
					}
					
					out.println("</td>");
					out.println("</tr>");
				}
				
				out.println("</table>");
			}
		} finally {
			if (rs3 != null) {
				rs3.close();
			}
		}
	}
%>
</body>
</html>



<%!
// A few methods here because the DAOs don't have something which will do it.

/*
* Returns the lab_no's of the hl7TextInfo records that are affected (id != lab_no)
*
*/
 List<Integer> getAllLabsThatDontMatch() throws SQLException {
	List<Integer> allWithDiffIds = new ArrayList<Integer>();
	ResultSet rs = null;
	try {
		rs = DBHandler.GetSQL("select lab_no from hl7TextInfo where id != lab_no");
		while (rs.next()) {
			allWithDiffIds.add(rs.getInt("lab_no"));
		}
		rs.close();
		rs = null;

	} finally {
		if(rs != null) {
			rs.close();
		}
	}
	return allWithDiffIds;
}

/*
* The lab type is in the hl7TextMessage, so we load it by the lab_no, and check the type. We
* only return the ones where the type is GDML.
*/
 List<Integer> filterByGDML(List<Integer> allWithDiffIds) throws SQLException {
	 List<Integer> allGDMLWithDiffIds = new ArrayList<Integer>();
	 ResultSet rs = null;
	 
	 for (Integer id : allWithDiffIds) {
			//is this a GDML?
			try {
				rs = DBHandler.GetSQL("select lab_id,type from hl7TextMessage where lab_id = "	+ id);
				if (rs.next()) {
					String type = rs.getString("type");
					if ("GDML".equals(type)) {
						allGDMLWithDiffIds.add(id);
					}
				} else {
					MiscUtils.getLogger().info("weird..no message for this header..that's another problem you have");
				}
				
			} finally {
				if(rs != null )
					rs.close();
				rs = null;
			}
		}
	 return allGDMLWithDiffIds;
}
 
/*
* This will check all lab read's for the specified period for this lab, and return 
  the log ids.
*/
 List<Integer> checkAccess(Integer labNo, String startDate, String endDate) throws SQLException {
	 List<Integer> results = new ArrayList<Integer>();
	 
	ResultSet rs = null;
		
	String sql = "select * from log where action='read' and content='lab' and dateTime >= '"
			+ StringEscapeUtils.escapeSql(startDate)
			+ "' and dateTime <= '"
			+ StringEscapeUtils.escapeSql(endDate)
			+ "' and contentId = '" + labNo + "'";
	try {
		rs = DBHandler.GetSQL(sql);
	
		while (rs.next()) {
			results.add(rs.getInt("id"));
		}
	}finally {
		if(rs != null) 
			rs.close();
		rs = null;
	}
	
	return results;
 }
%>
