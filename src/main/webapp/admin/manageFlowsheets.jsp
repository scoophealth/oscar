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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.util.*,oscar.oscarReport.reportByTemplate.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.MeasurementTemplateFlowSheetConfig" %>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.MeasurementFlowSheet" %>
<%@ page import="org.oscarehr.common.model.Flowsheet" %>
<%@ page import="org.oscarehr.common.dao.FlowsheetDao" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>

<%
	String method = request.getParameter("method");
	if(method != null ) {
		if(method.equals("disable")) {
			String name = request.getParameter("name");
			MeasurementTemplateFlowSheetConfig.getInstance().disableFlowsheet(name);
		}
		if(method.equals("enable")) {
			String name = request.getParameter("name");
			MeasurementTemplateFlowSheetConfig.getInstance().enableFlowsheet(name);
		}
		response.sendRedirect("manageFlowsheets.jsp");
	}

%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Manage Flowsheets</title>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">

<script type="text/javascript" language="JavaScript"
	src="../share/javascript/prototype.js"></script>
<script type="text/javascript" language="JavaScript"
	src="../share/javascript/Oscar.js"></script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/jquery_css/smoothness/jquery-ui-1.10.2.custom.min.css"/>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>
<script>
jQuery.noConflict();
jQuery(function() {
    jQuery( document ).tooltip();
  });
</script>

<style type="text/css">
table.outline {
	margin-top: 50px;
	border-bottom: 1pt solid #888888;
	border-left: 1pt solid #888888;
	border-top: 1pt solid #888888;
	border-right: 1pt solid #888888;
}

table.grid {
	border-bottom: 1pt solid #888888;
	border-left: 1pt solid #888888;
	border-top: 1pt solid #888888;
	border-right: 1pt solid #888888;
}

td.gridTitles {
	border-bottom: 2pt solid #888888;
	font-weight: bold;
	text-align: center;
}

td.gridTitlesWOBottom {
	font-weight: bold;
	text-align: center;
}

td.middleGrid {
	border-left: 1pt solid #888888;
	border-right: 1pt solid #888888;
	text-align: center;
}

label {
	float: left;
	width: 120px;
	font-weight: bold;
}

label.checkbox {
	float: left;
	width: 116px;
	font-weight: bold;
}

label.fields {
	float: left;
	width: 80px;
	font-weight: bold;
}

span.labelLook {
	font-weight: bold;
}

input,textarea,select { //
	margin-bottom: 5px;
}

textarea {
	width: 450px;
	height: 100px;
}

.boxes {
	width: 1em;
}

#submitbutton {
	margin-left: 120px;
	margin-top: 5px;
	width: 90px;
}

br {
	clear: left;
}
</style>
</head>

<body vlink="#0000FF" class="BodyStyle">

<table class="MainTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">admin</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar" style="width: 100%;">
			<tr>
				<td>Manage Flowsheets</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" width="160px;">
		&nbsp;</td>
		<td class="MainTableRightColumn" valign="top">
			<h3>Flowsheets</h3>
			<br/>
			<table width="80%">
				<tr>
					<td><b>Name</b></td>
					<td><b>Universal</B></td>
					<td><b>Dx Triggers</B></td>
					<td><b>Program Triggers</B></td>
					<td><b>Type</b></td>
					<td><b>Enabled</b></td>					
					<td><b>Actions</b></td>
				</tr>
			<%
			Hashtable<String, String> systemFlowsheets = MeasurementTemplateFlowSheetConfig.getInstance().getFlowsheetDisplayNames();
				for(String name:systemFlowsheets.keySet()) {
					String displayName = systemFlowsheets.get(name);
					MeasurementFlowSheet flowSheet = MeasurementTemplateFlowSheetConfig.getInstance().getFlowSheet(name);
					
					//load from db to know if it's enabled or not.
					Flowsheet fs = MeasurementTemplateFlowSheetConfig.getInstance().getFlowsheetSettings().get(flowSheet.getName());
					boolean enabled=true;
					if(fs != null) {
						enabled = fs.isEnabled();
					}
					String type="System";
					if(fs!=null) {
						type = (fs.isExternal())?"System":"Custom";
					}


					if(!flowSheet.getDisplayName().equals("Health Tracker")){
					%>
						
						<tr>
							<td><%=flowSheet.getDisplayName()%></td>
							<td><%=flowSheet.isUniversal() %></td>
							<td><%=flowSheet.getDxTriggersString() %></td>
							<td><%=flowSheet.getProgramTriggersString() %></td>
							<td><%=type %></td>
							<td><%=enabled%></td>
							<td>
								<%if(enabled) { %>
									<a href="manageFlowsheets.jsp?method=disable&name=<%=flowSheet.getName()%>">Disable</a>
								<% } else { %>
									<a href="manageFlowsheets.jsp?method=enable&name=<%=flowSheet.getName()%>">Enable</a>
								<% } %>								
							</td>
						</tr>
					<%
					}
				}
			%>
			</table>
				
			<br/><br/><br/>
			
			<form enctype="multipart/form-data" method="POST" action="<%=request.getContextPath()%>/admin/manageFlowsheetsUpload.jsp">
				<input type="file" name="flowsheet_file"/>
				<span title="<bean:message key="global.uploadWarningBody"/>" style="vertical-align:middle;font-family:arial;font-size:20px;font-weight:bold;color:#ABABAB;cursor:pointer"><img border="0" src="../images/icon_alertsml.gif"/></span></span>
        
				&nbsp;
				<input type="submit" value="Upload" />
			</form>					
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>

		<td class="MainTableBottomRowRightColumn">&nbsp;</td>
	</tr>
</table>
</html:html>
