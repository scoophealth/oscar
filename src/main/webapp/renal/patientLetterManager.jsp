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

<%@page import="java.util.*, java.io.*, org.oscarehr.util.MiscUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />

<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<!--I18n-->
<title>ORN Pilot</title>
<script src="../share/javascript/Oscar.js"></script>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />

<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>


<%
	if(request.getParameter("action") != null && request.getParameter("action").equals("save")) {
		String documentDir = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR","");
		try {
			File f = new File(documentDir,"orn_patient_letter.txt");
			PrintWriter pw = new PrintWriter(new FileWriter(f),true);
			pw.println(request.getParameter("letter"));
			pw.close();
		}catch(IOException e) {
			MiscUtils.getLogger().error("Error",e);
		}
		%>
			<script>
				$(document).ready(function(){
					window.close();
				});
			</script>	
		<%
	}
%>

<%
	String currentLetter = "";

	String documentDir = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR","");
	File f = new File(documentDir,"orn_patient_letter.txt");
	if(f.exists()) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(f));
			String line =null;
			while((line=in.readLine())!=null) {
				currentLetter += line + "\n";
			}
			in.close();
		}catch(IOException e) {
			MiscUtils.getLogger().error("Error",e);
		}
	}

%>
</head>

<body class="BodyStyle" vlink="#0000FF">

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td style="max-width:200px;" class="MainTableTopRowLeftColumn">Patient Letter</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Patient Letter Manager</td>
				<td>&nbsp;</td>
				<td style="text-align: right">
					<a href="javascript:popupStart(300,400,'About.jsp')"><bean:message key="global.about" /></a> | 
					<a href="javascript:popupStart(300,400,'License.jsp')"><bean:message key="global.license" /></a>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" nowrap="nowrap">
		    
		</td>
		<td valign="top" class="MainTableRightColumn">
			<h4>Use this section to customize the patient letter generated from the screening report.</h4>
			<br/>
			<form action="patientLetterManager.jsp?action=save">
				<input type="hidden" name="action" value="save"/>
				<textarea name="letter" rows="30" cols="80"><%=currentLetter %></textarea>
				<br/>
				<input type="submit" value="Save & Exit"/>
 			</form>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
	</tr>
</table>
<script type="text/javascript">
    //Calendar.setup( { inputField : "asofDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );
</script>

</body>
</html:html>
<%!

%>
