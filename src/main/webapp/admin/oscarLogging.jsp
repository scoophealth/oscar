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

<%@page import="oscar.util.*, oscar.*, java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%
  
%>

<html>

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>oscarLogging</title>
<script src="../share/javascript/Oscar.js"></script>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />

<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<SCRIPT LANGUAGE="JavaScript">
        function CheckData() {
        var Date = document.report.reportDate.value;
        if (Date == "") {
        alert("Fill in the date");
        return(false);
        }
        return(true);
        }
    </SCRIPT>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="100">oscarLogging</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>OSCAR Logging</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="1.6.11" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><<bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">&nbsp;</td>
		<td valign="top" class="MainTableRightColumn">
		<% 
            String reportDate = request.getParameter("reportDate");
            String reportType = request.getParameter("reportType");
            boolean runReport;
            if (reportDate == null) {
                reportDate = UtilDateUtilities.getToday("yyyy-MM-dd");
                runReport = false;
            }
            else {
                runReport = true;
            }
            if (reportType == null) {
                reportType = "general";
            }
            %>
		<form name="report" id="report" action="oscarLogging.jsp"
			onsubmit="return CheckData();">
		<table border="0">
			<tr>
				<td>Enter Date to view report for (yyyy-mm-dd)</td>
				<td><input type="text" id="reportDate" name="reportDate"
					size="10" value="<%= reportDate %>"> <%--<html:text property="reportDate" size="9" styleId="reportDate" />--%>
				<a id="date"><img title="Calendar" src="../images/cal.gif"
					alt="Calendar" border="0" /></a>
			</tr>
			<tr>
				<td>Enter Report to view:</td>
				<td>
				<center><select name="reportType">
					<option value="general" <% if (reportType.equals("general")) { %>
						selected <% } %>>General Report</option>
					<option value="mysql" <% if (reportType.equals("mysql")) { %>
						selected <% } %>>MySQL Transaction Report</option></center>
				</td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Get Report"></td>
		</table>
		</form>
		<br>
		<hr>
		<%
            if (runReport) {
                Properties pr = OscarProperties.getInstance();
                String path = pr.getProperty("LOGGING_PATH");
                String suffix = reportDate.replaceAll("-", "");
                String fileName = "";
                String contentString = "";
                if (reportType.equals("general")) { 
                    fileName = path + "report" + suffix + ".html";
                    ReadLocalFile.writeStreamFromFile(fileName, out);
                }
                else if (reportType.equals("mysql")) {
                    fileName = path + "reportmysql" + suffix + ".html";
                    ReadLocalFile.writeStreamFromFile(fileName, out);
                }
             } %>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
	</tr>
</table>
<script type="text/javascript">
    Calendar.setup( { inputField : "reportDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );
    </script>

</body>
</html>
