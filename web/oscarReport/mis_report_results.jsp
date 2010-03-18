<%@page import="java.util.List"%>
<%@page import="org.oscarehr.web.MisReportUIBean"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%-- 
/*
* Copyright (c) 2007-2009. CAISI, Toronto. All Rights Reserved.
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. 
* 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. 
* 
* You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.  
* 
* This software was written for 
* CAISI, 
* Toronto, Ontario, Canada 
*/
--%>
<%
	int startYear = Integer.parseInt(request.getParameter("startYear"));
	int startMonth = Integer.parseInt(request.getParameter("startMonth"));
	int endYear = Integer.parseInt(request.getParameter("endYear"));
	int endMonth = Integer.parseInt(request.getParameter("endMonth"));
	
	String functionalCentreId=request.getParameter("functionalCentreId");

	MisReportUIBean misReportUIBean=new MisReportUIBean(functionalCentreId, startYear, startMonth, endYear, endMonth);
%>

<%@include file="/layouts/caisi_html_top.jspf"%>

<h3>MIS Report</h3>
<span style="font-weight:bold">Functional Centre : </span><%=misReportUIBean.getFunctionalCentreDescription()%>
<br />
<span style="font-weight:bold">Dates : </span><%=misReportUIBean.getDateRangeForDisplay()%>

<br /><br />

<table class="genericTable borderedTableAndCells" style="font-size:12px">
	<%
		int rowCounter=0;
		for (MisReportUIBean.DataRow dataRow : misReportUIBean.getDataRows())
		{
			rowCounter++;
			String backgroundColour;
			if (rowCounter%2==0) backgroundColour="#eeeeee";
			else backgroundColour="#dddddd";
				
			%>
				<tr class="genericTableRow" style="background-color:<%=backgroundColour%>">
					<td style="font-weight:bold"><%=dataRow.dataReportId%></td>
					<td style="font-weight:bold"><%=StringEscapeUtils.escapeHtml(dataRow.dataReportDescription)%></td>
					<td><%=dataRow.dataReportResult%></td>
				</tr>
			<%
		}
	%>
</table>

<%@include file="/layouts/caisi_html_bottom.jspf"%>